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

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.modelmapper.ModelMapper;

import javax.inject.Inject;

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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ListFragment extends Fragment implements OnRecyclerItemClickListener<Image> {


    private FragmentImageListBinding bindingList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PublishProcessor<Integer> paginator = PublishProcessor.create();
    private ImageAdapter mImageAdapter;
    private boolean loading = false;
    private int pageNumber = 1;
    private final int VISIBLE_THRESHOLD = 4;
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager layoutManager;
    private FavoritePhotoEntity favoritePhotoEntity = new FavoritePhotoEntity();


    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    ModelMapper mModelMapper;

    private MainViewModel mMainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        initialiseViewModel();
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingList = DataBindingUtil.inflate(inflater, R.layout.fragment_image_list, container, false);
        subscribeForData();
        return bindingList.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).updateStatusBarColor("#034D59");
        mImageAdapter = new ImageAdapter(this);
        layoutManager = new LinearLayoutManager(requireContext());
        bindingList.imageList.setLayoutManager(layoutManager);
        bindingList.imageList.setAdapter(mImageAdapter);
        setUpLoadMoreListener();
        mMainViewModel.fetchPhotoList(20, pageNumber);

        bindingList.fragmentListSwipeRefresh.setOnRefreshListener(() -> {
            layoutManager.getInitialPrefetchItemCount();
            bindingList.fragmentListSwipeRefresh.postDelayed(() -> bindingList.fragmentListSwipeRefresh.setRefreshing(false), 500);
        });

        bindingList.fragmentListToolbarFavoriteIcon.setOnClickListener(v -> ActivityUtil.pushFragment(new FavoriteFragment(), requireFragmentManager(), R.id.main_content, true, AnimationType.RIGHT_TO_LEFT));
        bindingList.fragmentListToolbarSearchIcon.setOnClickListener(v -> ActivityUtil.pushFragment(new SearchFragment(), requireFragmentManager(), R.id.main_content, true, AnimationType.RIGHT_TO_LEFT));
    }

    private void initialiseViewModel() {
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        mMainViewModel.getListLiveData().observe(this, apiResponseResource -> {
            loading = false;
            bindingList.fragmentListProgressBar.setVisibility(View.INVISIBLE);
            if (apiResponseResource.isSuccess()) {
                mImageAdapter.addItems(apiResponseResource.data.getPhotos());
            } else {
                Toast.makeText(requireContext(), "You are NOT connected to the Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        RxBus.getInstance()
                .listen()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item ->mImageAdapter.updateItem((String) item));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        mMainViewModel.onStop();
        super.onDestroy();
    }

    /**
     * setting listener to get callback for load more
     */
    private void setUpLoadMoreListener() {
        bindingList.imageList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
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

    /**
     * subscribing for data
     */
    private void subscribeForData() {
        Disposable disposable = paginator
                .onBackpressureDrop()
                .doOnNext(page -> {
                    loading = true;
                    bindingList.fragmentListProgressBar.setVisibility(View.VISIBLE);
                    mMainViewModel.fetchPhotoList(20, page);
                }).subscribe();
        compositeDisposable.add(disposable);
        paginator.onNext(pageNumber);
    }

//            case R.id.item_paging_download:
//                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                Permissions.check(requireContext(), permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
//                    @Override
//                    public void onGranted() {
//                        BottomSheetSizeHelper bottomSheetSizeHelper = new BottomSheetSizeHelper();
//                        bottomSheetSizeHelper.ItemClich(requireActivity(), getLayoutInflater(), mImageAdapter, requireContext(), position, ((Image) item).getSrc());
//                    }
//                });
//                RxBus.getInstance().listen().subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        mImageAdapter.updateItem(position, integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//                break;
//        }
//    }

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
                    mImageAdapter.updateItem(position, item, -1f);
                } else {
                    Toast.makeText(requireContext(), "Hargelis dzir coderd inserti", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mMainViewModel.deleteFavorite(key);
            mMainViewModel.getIsDelete().observe(this, success -> {
                mMainViewModel.getIsDelete().removeObservers(this);
                if (success) {
                    item.setIsFavorite(false);
                    mImageAdapter.updateItem(position, item, -1f);
                } else {
                    Toast.makeText(requireContext(), "Hargelis dzir coderd deleti", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onItemClicked(View view, Image item, int position) {
        switch (view.getId()) {
            case R.id.photo_image:
                PhotoFullScreenHelper photoFullScreenHelper = new PhotoFullScreenHelper();
                photoFullScreenHelper.fullScreen(requireFragmentManager(), view, ((Image) item).getSrc());
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
                        bottomSheetSizeHelper.ItemClich(requireActivity(), getLayoutInflater(), requireContext(), position,  item.getSrc());
                    }
                });
                RxBus.getInstance()
                        .listen()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> mImageAdapter.updateItem(position, (Integer)integer));
                break;
        }

    }
}

