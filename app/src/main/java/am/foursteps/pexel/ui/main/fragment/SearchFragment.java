package am.foursteps.pexel.ui.main.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.entity.FavoritePhotoEntity;
import am.foursteps.pexel.data.remote.model.Image;
import am.foursteps.pexel.databinding.FragmentSearchListBinding;
import am.foursteps.pexel.factory.ViewModelFactory;
import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;
import am.foursteps.pexel.ui.base.util.BottomSheetSizeHelper;
import am.foursteps.pexel.ui.base.util.PhotoFullScreenHelper;
import am.foursteps.pexel.ui.base.util.RxBus;
import am.foursteps.pexel.ui.main.activity.MainActivity;
import am.foursteps.pexel.ui.main.adapter.ImageAdapter;
import am.foursteps.pexel.ui.main.viewmodel.MainViewModel;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import timber.log.Timber;

public class SearchFragment extends Fragment implements OnRecyclerItemClickListener<Image> {


    private FragmentSearchListBinding mBinding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private PublishProcessor<Integer> paginator = PublishProcessor.create();
    private ImageAdapter mImageAdapter;
    private boolean loading = false;
    private int pageNumber = 1;
    private final int VISIBLE_THRESHOLD = 4;
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager layoutManager;
    private String mSearchText;


    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    ModelMapper mModelMapper;

    private MainViewModel mMainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        initialiseViewModel();
        PRDownloader.cancelAll();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_list, container, false);
        subscribeForData();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).updateStatusBarColor("#034D59");
        mImageAdapter = new ImageAdapter(this);
        layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mBinding.fragmentSearchRecyclerView.setLayoutManager(layoutManager);
        mBinding.fragmentSearchRecyclerView.setAdapter(mImageAdapter);
        setUpLoadMoreListener();
        mMainViewModel.fetchSearchPhotoList(mSearchText, 20, pageNumber);
        mBinding.fragmentSearchSwipeRefresh.setOnRefreshListener(() -> {
            layoutManager.getInitialPrefetchItemCount();
            mBinding.fragmentSearchSwipeRefresh.postDelayed(() -> mBinding.fragmentSearchSwipeRefresh.setRefreshing(false), 500);
        });

        mBinding.fragmentListToolbarEditTextBack.setOnClickListener(v -> {
            mBinding.fragmentListToolbarEditText.getText().clear();
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            requireActivity().onBackPressed();
        });
        mBinding.fragmentListToolbarEditTextClose.setOnClickListener(view1 -> mBinding.fragmentListToolbarEditText.getText().clear());
        mBinding.fragmentListToolbarEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mBinding.fragmentListToolbarEditTextClose.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mBinding.fragmentListToolbarEditText.getText().toString().equals("")) {
                    mBinding.fragmentListToolbarEditTextClose.setVisibility(View.GONE);
                } else {
                    mBinding.fragmentListToolbarEditTextClose.setVisibility(View.VISIBLE);
                }
                mImageAdapter.clearItems();
                mSearchText = charSequence.toString();
                subscribeForData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Disposable disposable = RxBus.getInstance()
                .listenProgress()
                .subscribe(progressEvent -> {
                    if (mImageAdapter != null) {
                        mImageAdapter.updateItem(progressEvent.getPosition(), progressEvent.getProgress());
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void initialiseViewModel() {
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        mMainViewModel.getSearchListLiveData().observe(this, apiResponseResource -> {
            if (apiResponseResource.isSuccess()) {
                mImageAdapter.addItems(apiResponseResource.data.getPhotos());
            } else {
            }
        });
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        mMainViewModel.onStop();
        super.onDestroy();
    }

    private void setUpLoadMoreListener() {
        mBinding.fragmentSearchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager
                            .findLastVisibleItemPosition();
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                        Timber.e("jjjjjjjjjjjjjjjjjj"
                                + "" + dy);
                        pageNumber++;
                        paginator.onNext(pageNumber);
                        loading = true;
                    }
                }
            }
        });


    }

    private void subscribeForData() {
        Disposable disposable = paginator
                .onBackpressureDrop()
                .doOnNext(page -> {
                    loading = true;
                    mBinding.fragmentSearchProgressBar.setVisibility(View.VISIBLE);
                    mMainViewModel.fetchSearchPhotoList(mSearchText, 20, page);
                }).subscribe(items -> {
                    loading = false;
                    mBinding.fragmentSearchProgressBar.setVisibility(View.INVISIBLE);
                });
        compositeDisposable.add(disposable);
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
                        bottomSheetSizeHelper.ItemClick(getLayoutInflater(), requireContext(), position, item);
                    }
                });
        }
    }
}
