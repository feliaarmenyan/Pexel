package com.evon.pexel.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evon.pexel.PaginationItem;
import com.evon.pexel.R;
import com.evon.pexel.base.interfaces.OnRecyclerItemClickListener;
import com.evon.pexel.databinding.FragmentListBinding;
import com.evon.pexel.view.adapter.PaginationAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

public class ListFragment extends Fragment implements OnRecyclerItemClickListener {

    private FragmentListBinding binding;

    public static final String TAG = ListFragment.class.getSimpleName();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        paginationAdapter = new PaginationAdapter(this);
        layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        if (savedInstanceState != null) {
//            binding.fragmentListRecyclerView.getLayoutManager().onRestoreInstanceState(
//                    savedInstanceState.getParcelable("KEY_LAYOUT"));
//        }else{
        binding.fragmentListRecyclerView.setLayoutManager(layoutManager);
        binding.fragmentListRecyclerView.setAdapter(paginationAdapter);
        binding.fragmentListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
//        }
        setUpLoadMoreListener();
        subscribeForData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fragmentListRecyclerView.setAdapter(paginationAdapter);
        binding.fragmentListSwipeRefresh.setOnRefreshListener(() -> {
            layoutManager.getInitialPrefetchItemCount();
            binding.fragmentListSwipeRefresh.postDelayed(() -> binding.fragmentListSwipeRefresh.setRefreshing(false), 500);
        });
    }
//        binding.fragmentListRecyclerView.addOnLayoutChangeListener(
//                new View.OnLayoutChangeListener() {
//                    @Override
//                    public void onLayoutChange(View view,
//                                               int left,
//                                               int top,
//                                               int right,
//                                               int bottom,
//                                               int oldLeft,
//                                               int oldTop,
//                                               int oldRight,
//                                               int oldBottom) {
//                        binding.fragmentListRecyclerView.removeOnLayoutChangeListener(this);
//                        final RecyclerView.LayoutManager layoutManager =
//                                binding.fragmentListRecyclerView.getLayoutManager();
//                        View viewAtPosition =
//                                layoutManager.findViewByPosition(layoutManager.getPosition(view));
//                        // Scroll to position if the view for the current position is null (not
//                        // currently part of layout manager children), or it's not completely
//                        // visible.
//                        if (viewAtPosition == null
//                                || layoutManager.isViewPartiallyVisible(viewAtPosition, false, true)){
//                            binding.fragmentListRecyclerView.post(()
//                                    -> layoutManager.scrollToPosition(layoutManager.getPosition(view)));
//                        }
//                    }
//                });
//    }
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable("KEY_LAYOUT", binding.fragmentListRecyclerView.getLayoutManager().onSaveInstanceState());
//    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    /**
     * setting listener to get callback for load more
     */
    private void setUpLoadMoreListener() {
        binding.fragmentListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    binding.fragmentListProgressBar.setVisibility(View.VISIBLE);
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
                    binding.fragmentListProgressBar.setVisibility(View.INVISIBLE);
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

    //   public void setExitSharedElementCallback(
//   new SharedElementCallback() {
//        @Override
//        public void onMapSharedElements(
//                List<String> names, Map<String, View> sharedElements) {
//            // Locate the ViewHolder for the clicked position.
//            RecyclerView.ViewHolder selectedViewHolder = recyclerView
//                    .findViewHolderForAdapterPosition(MainActivity.currentPosition);
//            if (selectedViewHolder == null || selectedViewHolder.itemView == null) {
//                return;
//            }
//
//            // Map the first shared element name to the child ImageView.
//            sharedElements
//                    .put(names.get(0),
//                            selectedViewHolder.itemView.findViewById(R.id.card_image));
//        }
//    });
    @Override
    public void onItemClicked(View view, Object item, int position) {
        switch (view.getId()) {
            case R.id.photo_image:
                ItemFragment listFragment = ItemFragment.newInstance();

                listFragment.setSharedElementEnterTransition(new DetailsTransition());
                listFragment.setEnterTransition(new Fade());
                setExitTransition(new Fade());
                listFragment.setSharedElementReturnTransition(new DetailsTransition());

                requireFragmentManager()
                        .beginTransaction()
//                        .setReorderingAllowed(true) // setAllowOptimization before 26.1.0
                        .addSharedElement(view, "image")
//                        .add(R.id.container, listFragment)
                        .addToBackStack(null)
                        .replace(R.id.container, listFragment)
                        .commit();

//                   .beginTransaction()
//                    .addToBackStack(TAG)
//                    .replace(R.id.content, galleryViewPagerFragment)
//                    .commit();

//               requireFragmentManager()
//                        .beginTransaction()
//                        .setReorderingAllowed(true) // setAllowOptimization before 26.1.0
//                        .addSharedElement(view, "image")
//                        .replace(R.id.container,
//                                new ListFragment(),
//                                ListFragment.class.getSimpleName())
//                        .addToBackStack(null)
//                        .commit();
                break;
            case R.id.item_paging_favorite:
                Toast.makeText(requireContext(), "Favorite", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_paging_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "heey");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.item_paging_download:
                Toast.makeText(requireContext(), "Download", Toast.LENGTH_SHORT).show();
        }
    }
}
