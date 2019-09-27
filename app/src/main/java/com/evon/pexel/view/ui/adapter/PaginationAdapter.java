package com.evon.pexel.view.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
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


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).bind(items.get(position));
        ViewCompat.setTransitionName(((ItemViewHolder) holder).mBinding.photoImage, "Item" + position);
        ((ItemViewHolder) holder).mBinding.photoImage.setImageResource(R.drawable.ic_profile_5);
        ((ItemViewHolder) holder).mBinding.itemPagingConstraint.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(((ItemViewHolder) holder).mBinding.itemPagingConstraint, items.get(position), position));
        ((ItemViewHolder) holder).mBinding.itemPagingFavorite.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(((ItemViewHolder) holder).mBinding.itemPagingFavorite, items.get(position), position));
        ((ItemViewHolder) holder).mBinding.itemPagingDownload.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(((ItemViewHolder) holder).mBinding.itemPagingDownload, items.get(position), position));
        ((ItemViewHolder) holder).mBinding.itemPagingShare.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(((ItemViewHolder) holder).mBinding.itemPagingShare, items.get(position), position));
        ((ItemViewHolder) holder).mBinding.photoImage.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(((ItemViewHolder) holder).mBinding.photoImage, items.get(position), position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ItemPaginationBinding mBinding;

        public ItemViewHolder(@NonNull ItemPaginationBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(PaginationItem paginationItem) {
//            mBinding.itemPagingCircleImage.setImageResource(paginationItem.getProfileCircleImage());
//            mBinding.imageView.setImageResource(paginationItem.getImage());
        }
    }
}
