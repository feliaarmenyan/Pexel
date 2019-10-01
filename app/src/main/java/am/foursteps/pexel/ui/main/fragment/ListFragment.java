package am.foursteps.pexel.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.SizeData;
import am.foursteps.pexel.data.local.def.AnimationType;
import am.foursteps.pexel.data.local.model.PaginationItem;
import am.foursteps.pexel.databinding.BottomSheetBinding;
import am.foursteps.pexel.databinding.FragmentImageListBinding;
import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;
import am.foursteps.pexel.ui.main.activity.MainActivity;
import am.foursteps.pexel.ui.main.adapter.ChooseSizeAdapter;
import am.foursteps.pexel.ui.main.adapter.PaginationAdapter;
import am.foursteps.pexel.utils.ActivityUtil;
import am.foursteps.pexel.utils.DetailsTransition;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

public class ListFragment extends Fragment implements OnRecyclerItemClickListener {


    private FragmentImageListBinding bindingList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PublishProcessor<Integer> paginator = PublishProcessor.create();
    private PaginationAdapter paginationAdapter;
    private boolean loading = false;
    private int pageNumber = 1;
    private final int VISIBLE_THRESHOLD = 4;
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager layoutManager;

    private int mPosition;

    private ChooseSizeAdapter mSizeAdapter;
    private static List<Size> sItems = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        bindingList = DataBindingUtil.inflate(inflater, R.layout.fragment_image_list, container, false);
        subscribeForData();
        return bindingList.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).updateStatusBarColor("#034D59");
        paginationAdapter = new PaginationAdapter(this);
        layoutManager = new LinearLayoutManager(requireContext());
        bindingList.imageList.setLayoutManager(layoutManager);
        bindingList.imageList.setAdapter(paginationAdapter);
        setUpLoadMoreListener();

        bindingList.fragmentListSwipeRefresh.setOnRefreshListener(() -> {
            layoutManager.getInitialPrefetchItemCount();
            bindingList.fragmentListSwipeRefresh.postDelayed(() -> bindingList.fragmentListSwipeRefresh.setRefreshing(false), 500);
        });
        bindingList.fragmentListToolbarFavoriteIcon.setOnClickListener(v -> ActivityUtil.pushFragment(new FavoriteFragment(), requireFragmentManager(), R.id.main_content, true, AnimationType.RIGHT_TO_LEFT));
        bindingList.fragmentListToolbarSearchIcon.setOnClickListener(v -> ActivityUtil.pushFragment(new SearchFragment(), requireFragmentManager(), R.id.main_content, true, AnimationType.RIGHT_TO_LEFT));
    }
    @Override
    public void onDestroy() {
        compositeDisposable.clear();
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
                })
                .concatMapSingle(page -> dataFromNetwork(page)
                        .subscribeOn(Schedulers.io())
                        .doOnError(throwable -> {
                            // handle error
                        }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    paginationAdapter.addItems(items);
                    paginationAdapter.notifyDataSetChanged();
                    loading = false;
                    bindingList.fragmentListProgressBar.setVisibility(View.INVISIBLE);
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
                        item.setImage(R.drawable.ic_profile_5);
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
                ItemFragment listFragment = ItemFragment.newInstance();

                mPosition = position;
                listFragment.setSharedElementEnterTransition(new DetailsTransition());
                listFragment.setEnterTransition(new Fade());
                setExitTransition(new Fade());
                listFragment.setSharedElementReturnTransition(new DetailsTransition());

                requireFragmentManager()
                        .beginTransaction()
                        .addSharedElement(view, "image")
                        .replace(R.id.main_content, listFragment)
                        .addToBackStack(null)
                        .commit();
                break;


            case R.id.item_paging_favorite:

                break;
            case R.id.item_paging_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "heey");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.item_paging_download:
                BottomSheetBinding mBinding = BottomSheetBinding.inflate(getLayoutInflater());
                BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
                dialog.setContentView(mBinding.getRoot());

                mSizeAdapter = new ChooseSizeAdapter((v, i, position1) -> {
                    switch (v.getId()) {
                        //todo

                    }
                });
                mBinding.bottomSheetChooseSizeRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
                mSizeAdapter.setChooseSizeList(SizeData.getInstance().getItems());
                mBinding.bottomSheetChooseSizeRecyclerView.setAdapter(mSizeAdapter);

                dialog.show();
                mBinding.bottomSheetChooseSizeClose.setOnClickListener(view1 -> dialog.dismiss());
                mBinding.bottomSheetChooseSizeDownloadButton.setOnClickListener(view12 -> {
                    dialog.dismiss();


                    CountDownTimer mCountDownTimer;
                    mCountDownTimer=new CountDownTimer(5000,1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            progress+=20;
                            paginationAdapter.updateItem(position, progress);
                        }

                        @Override
                        public void onFinish() {
                            //Do what you want
                            paginationAdapter.updateItem(position, 100);
                            progress = 0;
                        }
                    };
                    mCountDownTimer.start();


//                    Handler handler = new Handler();
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            progress+=20;
//                            paginationAdapter.updateItem(position, progress);
//                            handler.postDelayed(this, 1000);
//
//                        }
//                    }, 100);

                });
                break;
        }
    }
    float progress = 0;


}
