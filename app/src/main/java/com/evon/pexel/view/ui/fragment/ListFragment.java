package com.evon.pexel.view.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
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

import com.evon.pexel.PaginationItem;
import com.evon.pexel.R;
import com.evon.pexel.base.interfaces.OnRecyclerItemClickListener;
import com.evon.pexel.databinding.FragmentListBinding;
import com.evon.pexel.model.def.AnimationType;
import com.evon.pexel.utils.ActivityUtil;
import com.evon.pexel.view.ui.DetailsTransition;
import com.evon.pexel.view.ui.activity.MainActivity;
import com.evon.pexel.view.ui.adapter.PaginationAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

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

    private int mPosition;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        setUpLoadMoreListener();
        subscribeForData();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).updateStatusBarColor("#034D59");
        paginationAdapter = new PaginationAdapter(this);
        layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.fragmentListRecyclerView.setLayoutManager(layoutManager);
        binding.fragmentListRecyclerView.setAdapter(paginationAdapter);
        binding.fragmentListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        binding.fragmentListSwipeRefresh.setOnRefreshListener(() -> {
            layoutManager.getInitialPrefetchItemCount();
            binding.fragmentListSwipeRefresh.postDelayed(() -> binding.fragmentListSwipeRefresh.setRefreshing(false), 500);
        });
        binding.fragmentListToolbarSearchIcon.setOnClickListener(v -> {
            binding.fragmentListToolbarLinearLayout.setVisibility(View.GONE);
            binding.fragmentListToolbarEditTextLinearLayout.setVisibility(View.VISIBLE);
        });
        binding.fragmentListToolbarEditTextBack.setOnClickListener(v -> {
            binding.fragmentListToolbarLinearLayout.setVisibility(View.VISIBLE);
            binding.fragmentListToolbarEditTextLinearLayout.setVisibility(View.GONE);
            binding.fragmentListToolbarEditText.getText().clear();
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        });
        binding.fragmentListToolbarEditTextClose.setOnClickListener(view1 -> binding.fragmentListToolbarEditText.getText().clear());
        binding.fragmentListToolbarFavoriteIcon.setOnClickListener(v -> ActivityUtil.pushFragment(new FavoriteFragment(), requireFragmentManager(), R.id.main_content, true, AnimationType.RIGHT_TO_LEFT));
        binding.fragmentListToolbarEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.fragmentListToolbarEditTextClose.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.fragmentListToolbarEditText.getText().toString().equals("")) {
                    binding.fragmentListToolbarEditTextClose.setVisibility(View.GONE);
                } else {
                    binding.fragmentListToolbarEditTextClose.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                BottomSheetChooseSize bottomSheetChooseSize = new BottomSheetChooseSize();
                bottomSheetChooseSize.show(requireFragmentManager(), bottomSheetChooseSize.getTag());
                Toast.makeText(requireContext(), "Download", Toast.LENGTH_SHORT).show();
        }
    }


}
