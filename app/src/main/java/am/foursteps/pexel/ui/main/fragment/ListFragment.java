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

import org.modelmapper.ModelMapper;

import javax.inject.Inject;

import am.foursteps.pexel.AppConstants;
import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.def.AnimationType;
import am.foursteps.pexel.data.local.entity.FavoritePhotoEntity;
import am.foursteps.pexel.data.remote.model.Image;
import am.foursteps.pexel.databinding.FragmentImageListBinding;
import am.foursteps.pexel.factory.ViewModelFactory;
import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;
import am.foursteps.pexel.ui.base.util.BottomSheetSizeHelper;
import am.foursteps.pexel.ui.base.util.PhotoFullScreenHelper;
import am.foursteps.pexel.ui.base.util.RxBus;
import am.foursteps.pexel.ui.main.activity.MainActivity;
import am.foursteps.pexel.ui.main.adapter.ImageAdapter;
import am.foursteps.pexel.ui.main.viewmodel.MainViewModel;
import am.foursteps.pexel.utils.ActivityUtil;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;

public class ListFragment extends Fragment implements OnRecyclerItemClickListener<Image> {


    private FragmentImageListBinding bindingList;
    private CompositeDisposable downloadDisposable = new CompositeDisposable();
    private CompositeDisposable paginationDisposable = new CompositeDisposable();
    private CompositeDisposable favoriteDisposable = new CompositeDisposable();
    private PublishProcessor<Integer> paginator = PublishProcessor.create();
    private ImageAdapter mImageAdapter;
    private boolean loading = false;
    private int pageNumber = 1;
    private final int VISIBLE_THRESHOLD = 4;
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager layoutManager;

    //todo mtacel aveli lavy
    private int clickedViewId;

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    ModelMapper mModelMapper;

    private MainViewModel mMainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        initialiseViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (bindingList == null) {
            bindingList = DataBindingUtil.inflate(inflater, R.layout.fragment_image_list, container, false);
            mImageAdapter = new ImageAdapter(this);
            layoutManager = new LinearLayoutManager(requireContext());
            bindingList.imageList.setLayoutManager(layoutManager);
            bindingList.imageList.setAdapter(mImageAdapter);
            subscribeForData();
            setUpLoadMoreListener();
        }
        return bindingList.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).updateStatusBarColor("#034D59");

        bindingList.activityMainToolbarLogo.setOnClickListener(view1 -> bindingList.imageList.smoothScrollToPosition(0));
        bindingList.fragmentListSwipeRefresh.setOnRefreshListener(() -> {
            mImageAdapter.clearItems();
            pageNumber = 0;
            paginator.onNext(pageNumber);
        });

        bindingList.fragmentListToolbarFavoriteIcon.setOnClickListener(v -> {
            PRDownloader.cancelAll();
            clickedViewId = 0;
            ActivityUtil.pushFragment(new FavoriteFragment(), requireFragmentManager(), R.id.main_content, true, AnimationType.RIGHT_TO_LEFT);
        });
        bindingList.fragmentListToolbarSearchIcon.setOnClickListener(v -> {
            PRDownloader.cancelAll();
            clickedViewId = 0;
            ActivityUtil.pushFragment(new SearchFragment(), requireFragmentManager(), R.id.main_content, true, AnimationType.RIGHT_TO_LEFT);
        });
    }

    private void initialiseViewModel() {
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        mMainViewModel.getListLiveData().observe(this, apiResponseResource -> {
            loading = false;
            bindingList.fragmentListProgressBar.setVisibility(View.INVISIBLE);
            if (bindingList.fragmentListSwipeRefresh.isRefreshing()) {
                bindingList.fragmentListSwipeRefresh.setRefreshing(false);
            }
            if (apiResponseResource.isSuccess()) {
                mImageAdapter.addItems(apiResponseResource.data.getPhotos());
            } else {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Disposable disposable = RxBus.getInstance()
                .listenKey()
                .subscribe(item -> mImageAdapter.updateItem(item));
        favoriteDisposable.add(disposable);

        Disposable disposableProgress = RxBus.getInstance()
                .listenProgress()
                .subscribe(progressEvent -> {
                    if (mImageAdapter != null && mImageAdapter.getItemCount() > 0) {
                        mImageAdapter.updateItem(progressEvent.getPosition(), progressEvent.getProgress());
                    }
                });
        downloadDisposable.add(disposableProgress);
    }

    @Override
    public void onPause() {
        if (clickedViewId != R.id.photo_image) {
            downloadDisposable.clear();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        downloadDisposable.clear();
        favoriteDisposable.clear();
        paginationDisposable.clear();
        mMainViewModel.onStop();
        super.onDestroy();
    }

    private void setUpLoadMoreListener() {
        bindingList.imageList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager
                        .findLastVisibleItemPosition();
                if (!loading
                        && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    pageNumber++;
                    paginator.onNext(pageNumber);
                    loading = true;
                }
            }
        });
    }

    private void subscribeForData() {
        Disposable disposable = paginator
                .onBackpressureDrop()
                .doOnNext(page -> {
                    loading = true;
                    bindingList.fragmentListProgressBar.setVisibility(View.VISIBLE);
                    mMainViewModel.fetchPhotoList(AppConstants.PER_PAGE, page);
                }).subscribe();
        paginationDisposable.add(disposable);
        paginator.onNext(pageNumber);
    }

    private void setupItemFavorite(Image item, final int position) {
        String key = item.getHeight() + "_" + item.getWidth() + "_" + item.getUrl();
        if (!item.getIsFavorite()) {
            FavoritePhotoEntity favoritePhotoEntity = mModelMapper.map(item, FavoritePhotoEntity.class);
            favoritePhotoEntity.setPrimaryKey(key);
            mMainViewModel.insertFavorite(favoritePhotoEntity);
            mMainViewModel.getIsInsert().observe(this, success -> {
                mMainViewModel.getIsInsert().removeObservers(this);
                if (success) {
                    item.setIsFavorite(true);
                    mImageAdapter.updateItemImage(position, item);
                } else {
                    Toast.makeText(requireContext(), "Exception with insert in Code", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mMainViewModel.deleteFavorite(key);
            mMainViewModel.getIsDelete().observe(this, success -> {
                mMainViewModel.getIsDelete().removeObservers(this);
                if (success) {
                    item.setIsFavorite(false);
                    mImageAdapter.updateItemImage(position, item);
                } else {
                    Toast.makeText(requireContext(), "Exception with delete in Code", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onItemClicked(View view, Image item, int position) {
        clickedViewId = view.getId();
        switch (view.getId()) {
            case R.id.photo_image:
                PhotoFullScreenHelper photoFullScreenHelper = new PhotoFullScreenHelper();
                photoFullScreenHelper.fullScreen(requireFragmentManager(), view, item.getSrc());
                break;
            case R.id.item_paging_favorite:
                setupItemFavorite(item, position);
                break;
            case R.id.item_paging_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mImageAdapter.getUrl(position));
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
}

