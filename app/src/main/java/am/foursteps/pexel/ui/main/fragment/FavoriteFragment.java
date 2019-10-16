package am.foursteps.pexel.ui.main.fragment;

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

import java.util.List;

import javax.inject.Inject;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.entity.FavoritePhotoEntity;
import am.foursteps.pexel.databinding.FragmentFavoriteListBinding;
import am.foursteps.pexel.factory.ViewModelFactory;
import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;
import am.foursteps.pexel.ui.base.util.BottomSheetSizeHelper;
import am.foursteps.pexel.ui.base.util.PhotoFullScreenHelper;
import am.foursteps.pexel.ui.base.util.RxBus;
import am.foursteps.pexel.ui.main.activity.MainActivity;
import am.foursteps.pexel.ui.main.adapter.FavoriteAdapter;
import am.foursteps.pexel.ui.main.viewmodel.MainViewModel;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.disposables.CompositeDisposable;

public class FavoriteFragment extends Fragment implements OnRecyclerItemClickListener {


    private FragmentFavoriteListBinding mBinding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FavoriteAdapter mFavoriteAdapter;
    private LinearLayoutManager layoutManager;
    private List<FavoritePhotoEntity> mFavoritePhotoEntityList;

    @Inject
    ViewModelFactory viewModelFactory;

    private MainViewModel mMainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        initialiseViewModel();
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_list, container, false);
        mMainViewModel.fetchFavoriteList();
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).updateStatusBarColor("#034D59");
        mFavoriteAdapter = new FavoriteAdapter(this);
        layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mBinding.fragmentFavoriteRecyclerView.setLayoutManager(layoutManager);
        mBinding.fragmentFavoriteRecyclerView.setAdapter(mFavoriteAdapter);
//        mMainViewModel.fetchFavoriteList();
        mBinding.fragmentFavoriteSwipeRefresh.setOnRefreshListener(() -> {
            layoutManager.getInitialPrefetchItemCount();
            mBinding.fragmentFavoriteSwipeRefresh.postDelayed(() -> mBinding.fragmentFavoriteSwipeRefresh.setRefreshing(false), 500);
        });
        mBinding.favoriteToolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }


    private void initialiseViewModel() {
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        mMainViewModel.getFavoriteListLiveData().observe(this, resource -> {
            if (resource.isSuccess()) {
                mFavoriteAdapter.addItems(resource.data);
            } else {
                Toast.makeText(requireContext(), "You are NOT Favorite item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClicked(View view, Object item, int position) {
        switch (view.getId()) {
            case R.id.photo_image:
                PhotoFullScreenHelper photoFullScreenHelper = new PhotoFullScreenHelper();
                photoFullScreenHelper.fullScreen(requireFragmentManager(), view, ((FavoritePhotoEntity) item).getImageSrc());
                break;
            case R.id.item_paging_favorite:
                mMainViewModel.deleteFavorite(((FavoritePhotoEntity) item).getPrimaryKey());
                mMainViewModel.getIsDelete().observe(this, success -> {
                    mMainViewModel.getIsDelete().removeObservers(this);
                    if (success) {
                        mFavoriteAdapter.removeItem(position);
                    } else {
                        Toast.makeText(requireContext(), "Hargelis dzir coderd deleti", Toast.LENGTH_SHORT).show();
                    }
                });
//publish subject mi tex set anem mi tex subject
                requireActivity().runOnUiThread(()->
                        RxBus.getInstance().publish(((FavoritePhotoEntity) item).getPrimaryKey())
                );
                break;
            case R.id.item_paging_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "heey");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.item_paging_download:
                BottomSheetSizeHelper bottomSheetSizeHelper = new BottomSheetSizeHelper();
                bottomSheetSizeHelper.ItemClich(requireActivity(), getLayoutInflater(), requireContext(), position, ((FavoritePhotoEntity) item).getImageSrc());
                break;
        }
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

}
