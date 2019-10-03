package am.foursteps.pexel.ui.main.fragment;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.model.PaginationItem;
import am.foursteps.pexel.data.remote.model.Image;
import am.foursteps.pexel.data.remote.model.ImageSrc;
import am.foursteps.pexel.databinding.FragmentSearchListBinding;
import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;
import am.foursteps.pexel.ui.base.util.BottomSheetSizeHelper;
import am.foursteps.pexel.ui.base.util.PhotoFullScreenHelper;
import am.foursteps.pexel.ui.main.activity.MainActivity;
import am.foursteps.pexel.ui.main.adapter.PaginationAdapter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

public class SearchFragment extends Fragment implements OnRecyclerItemClickListener {


    private FragmentSearchListBinding mBinding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private PublishProcessor<Integer> paginator = PublishProcessor.create();
    private PaginationAdapter paginationAdapter;
    private boolean loading = false;
    private int pageNumber = 1;
    private final int VISIBLE_THRESHOLD = 4;
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager layoutManager;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_list, container, false);
        setUpLoadMoreListener();
        subscribeForData();
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).updateStatusBarColor("#034D59");
        paginationAdapter = new PaginationAdapter(this);
        layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mBinding.fragmentSearchRecyclerView.setLayoutManager(layoutManager);
        mBinding.fragmentSearchRecyclerView.setAdapter(paginationAdapter);
        mBinding.fragmentSearchRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
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
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    /**
     * setting listener to get callback for load more
     */
    private void setUpLoadMoreListener() {
        mBinding.fragmentSearchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    mBinding.fragmentSearchProgressBar.setVisibility(View.VISIBLE);
                })
                .concatMapSingle(page -> dataFromNetwork(page)
                        .subscribeOn(Schedulers.io())
                        .doOnError(throwable -> {
                            // handle error
                        }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
//                    paginationAdapter.addItems(items);
                    paginationAdapter.notifyDataSetChanged();
                    loading = false;
                    mBinding.fragmentSearchProgressBar.setVisibility(View.INVISIBLE);
                });

        compositeDisposable.add(disposable);

        paginator.onNext(pageNumber);

    }

    /**
     * Simulation of network data
     */
    private Single<List<PaginationItem>> dataFromNetwork(final int page) {
        return Single.just(true)
                .delay(1, TimeUnit.SECONDS)
                .map(value -> {
                    List<PaginationItem> items = new ArrayList<>();
                    for (int i = 1; i <= 10; i++) {
                        PaginationItem item = new PaginationItem();
                        item.setImage(R.drawable.adventure4);
                        item.setProfileCircleImage(R.drawable.adventure4);
                        items.add(item);
                    }
                    return items;
                });
    }

    @Override
    public void onItemClicked(View view, Object item, int position) {
        switch (view.getId()) {
            case R.id.photo_image:
                PhotoFullScreenHelper photoFullScreenHelper = new PhotoFullScreenHelper();
                photoFullScreenHelper.fullScreen(requireFragmentManager(), view, ((Image) item).getSrc());
                break;
            case R.id.item_paging_favorite:
                paginationAdapter.updateItem(position,0);
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
                bottomSheetSizeHelper.ItemClich(requireActivity(),getLayoutInflater(), paginationAdapter, requireContext(), position,((Image) item).getSrc());
                break;
        }
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

}
