package com.evon.pexel.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.evon.pexel.PaginationItem;
import com.evon.pexel.R;
import com.evon.pexel.base.interfaces.OnRecyclerItemClickListener;
import com.evon.pexel.databinding.ItemPaginationBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amitshekhar on 15/03/17.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnRecyclerItemClickListener<PaginationItem> onRecyclerItemClickListener;
    List<PaginationItem> items = new ArrayList<>();

    public PaginationAdapter(OnRecyclerItemClickListener<PaginationItem> onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
        this.items = new ArrayList<>();
    }

    public PaginationAdapter() {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPaginationBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_pagination, parent, false);
        return new ItemViewHolder(binding);
    }

    public void addItems(List<PaginationItem> items) {
        this.items.addAll(items);
    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return ItemViewHolder.create(parent);
//    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).bind(items.get(position));
        ((ItemViewHolder) holder).mBinding.itemPagingConstraint.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(((ItemViewHolder) holder).mBinding.itemPagingConstraint, items.get(position), position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private ItemPaginationBinding mBinding;

        ItemViewHolder(@NonNull ItemPaginationBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
//        public ShipmentActivityHolder(@NonNull ItemPaginationBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//        static ItemViewHolder create(ViewGroup parent) {
//            return new ItemViewHolder(
//                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pagination, parent, false));
//        }

        void bind(PaginationItem paginationItem) {
            mBinding.itemPagingCircleImage.setImageResource(paginationItem.getProfileCircleImage());
            mBinding.itemPagingImageView.setImageResource(paginationItem.getImage());
        }
    }
}
