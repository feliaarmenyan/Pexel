package am.foursteps.pexel.ui.main.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.downloader.PRDownloader;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import javax.inject.Inject;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.entity.FavoritePhotoEntity;
import am.foursteps.pexel.databinding.FragmentFavoriteListBinding;
import am.foursteps.pexel.factory.ViewModelFactory;
import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;
import am.foursteps.pexel.ui.base.util.BottomSheetSizeHelper;
import am.foursteps.pexel.ui.base.util.DownloadIds;
import am.foursteps.pexel.ui.base.util.PhotoFullScreenHelper;
import am.foursteps.pexel.ui.base.util.RxBus;
import am.foursteps.pexel.ui.main.activity.MainActivity;
import am.foursteps.pexel.ui.main.adapter.FavoriteAdapter;
import am.foursteps.pexel.ui.main.viewmodel.MainViewModel;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class FavoriteFragment extends Fragment implements OnRecyclerItemClickListener {

    private FragmentFavoriteListBinding mBinding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FavoriteAdapter mFavoriteAdapter;
    private LinearLayoutManager layoutManager;

    private int clickedViewId;

    @Inject
    ViewModelFactory viewModelFactory;

    private MainViewModel mMainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        initialiseViewModel();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_list, container, false);
            mMainViewModel.fetchFavoriteList();
            mFavoriteAdapter = new FavoriteAdapter(this);
            layoutManager = new LinearLayoutManager(requireContext());
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            mBinding.fragmentFavoriteRecyclerView.setLayoutManager(layoutManager);
            mBinding.fragmentFavoriteRecyclerView.setAdapter(mFavoriteAdapter);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Disposable disposable = RxBus.getInstance()
                .listenProgress()
                .subscribe(progressEvent -> {
                    if (mFavoriteAdapter != null && mFavoriteAdapter.getItemCount() > 0) {
                        mFavoriteAdapter.updateItem(progressEvent.getPosition(), progressEvent.getProgress());
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).updateStatusBarColor("#034D59");

        mBinding.fragmentFavoriteSwipeRefresh.setOnRefreshListener(() -> {
            mBinding.fragmentFavoriteSwipeRefresh.postDelayed(() -> mBinding.fragmentFavoriteSwipeRefresh.setRefreshing(false), 500);
            mFavoriteAdapter.clearItems();
            mMainViewModel.fetchFavoriteList();
        });
        mBinding.favoriteToolbar.setNavigationOnClickListener(v -> {
            PRDownloader.cancelAll();
            requireActivity().onBackPressed();
        });
    }

    private void initialiseViewModel() {
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        mMainViewModel.getFavoriteListLiveData().observe(this, resource -> {
            if (mBinding.fragmentFavoriteSwipeRefresh.isRefreshing()) {
                mBinding.fragmentFavoriteSwipeRefresh.setRefreshing(false);
            }
            if (resource.isSuccess()) {
                mFavoriteAdapter.addItems(resource.data);
            } else {
                Toast.makeText(requireContext(), "You are NOT Favorite item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClicked(View view, Object item, int position) {
        clickedViewId = view.getId();
        switch (view.getId()) {
            case R.id.photo_image:
                PhotoFullScreenHelper photoFullScreenHelper = new PhotoFullScreenHelper();
                photoFullScreenHelper.fullScreen(requireFragmentManager(), view, ((FavoritePhotoEntity) item).getImageSrc());
                break;
            case R.id.item_paging_favorite:
                int downloadId = DownloadIds.getInstance().getDownloadId(position);
                if (downloadId != -1) {
                    PRDownloader.cancel(downloadId);
                }
                mMainViewModel.deleteFavorite(((FavoritePhotoEntity) item).getPrimaryKey());
                mMainViewModel.getIsDelete().observe(this, success -> {
                    mMainViewModel.getIsDelete().removeObservers(this);
                    if (success) {
                        mFavoriteAdapter.removeItem(position);
                        requireActivity().runOnUiThread(() ->
                                RxBus.getInstance().publishKey(((FavoritePhotoEntity) item).getPrimaryKey()));
                    } else {
                        Toast.makeText(requireContext(), "Error on Deleting", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.item_paging_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "heey");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.item_paging_download:
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                Permissions.check(requireContext(), permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        BottomSheetSizeHelper bottomSheetSizeHelper = new BottomSheetSizeHelper();
                        bottomSheetSizeHelper.ItemClick(getLayoutInflater(), requireContext(), position, item);
                    }
                });
                break;
        }
    }

    @Override
    public void onPause() {
        if (clickedViewId != R.id.photo_image) {
            PRDownloader.cancelAll();
            compositeDisposable.clear();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (clickedViewId != R.id.photo_image) {
            PRDownloader.cancelAll();
            compositeDisposable.clear();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        PRDownloader.cancelAll();
        compositeDisposable.clear();
        mMainViewModel.onStop();
        super.onDestroy();
    }
}
