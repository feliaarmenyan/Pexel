package com.evon.pexel.view.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.evon.pexel.ChooseSizeItem;
import com.evon.pexel.R;
import com.evon.pexel.base.interfaces.OnRecyclerItemClickListener;
import com.evon.pexel.databinding.BottomSheetChooseSizeBinding;
import com.evon.pexel.view.ui.adapter.ChooseSizeAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetChooseSize extends BottomSheetDialogFragment implements OnRecyclerItemClickListener {
    private BottomSheetChooseSizeBinding mBinding;
    private ChooseSizeAdapter mSizeAdapter;

    public BottomSheetChooseSize() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_choose_size, container, false);
        mSizeAdapter = new ChooseSizeAdapter(this);
        mBinding.bottomSheetChooseSizeRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        //adapterin andamner tal
        mBinding.bottomSheetChooseSizeRecyclerView.setAdapter(mSizeAdapter);
        setItems();
        return mBinding.getRoot();

    }

    private void setItems() {
        List<ChooseSizeItem> items = new ArrayList<>();
        items.add(new ChooseSizeItem(R.drawable.ic_check, "Original (3984 x 2656)"));
        items.add(new ChooseSizeItem(R.drawable.ic_check_box, "Large (1920 x 1280)"));
        items.add(new ChooseSizeItem(R.drawable.ic_check_box, "Medium (1280 x 853)"));
        items.add(new ChooseSizeItem(R.drawable.ic_check_box, "Small (640 x 426)"));
    }


    @Override
    public void onItemClicked(View view, Object item, int position) {


    }
//    private void openBottomSheet(){
//
//    mBinding.bottomSheetChooseSize.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mBinding.bottomSheetChooseSize.getScrollBarSize() != BottomSheetBehavior.STATE_EXPANDED) {
//                    mBinding.bottomSheetChooseSize.setScrollBarSize(BottomSheetBehavior.STATE_EXPANDED);
//                } else {
//                    mBinding.bottomSheetChooseSize.setScrollBarSize(BottomSheetBehavior.STATE_COLLAPSED);
//                }
//            }
//        });
//// callback for do something
//        mBinding.bottomSheetChooseSize.set(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View view, int newState) {
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btn_bottom_sheet.setText("Close Sheet");
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btn_bottom_sheet.setText("Expand Sheet");
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View view, float v) {
//
//            }
//        });
//    }
}
