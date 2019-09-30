package com.evon.pexel.view.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.evon.pexel.ChooseSizeItem;
import com.evon.pexel.R;
import com.evon.pexel.base.interfaces.OnRecyclerItemClickListener;
import com.evon.pexel.databinding.ItemChooseSizeBinding;

import java.util.ArrayList;
import java.util.List;

public class ChooseSizeAdapter extends RecyclerView.Adapter<ChooseSizeAdapter.ChooseSizeViewHolder> {
    private OnRecyclerItemClickListener<ChooseSizeItem> onRecyclerItemClickListener;
    List<ChooseSizeItem> items = new ArrayList<>();

    public ChooseSizeAdapter(OnRecyclerItemClickListener<ChooseSizeItem> onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
        items = new ArrayList<>();
    }

    public ChooseSizeAdapter() {
    }

    @NonNull
    @Override
    public ChooseSizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemChooseSizeBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_choose_size, parent, false);
        return new ChooseSizeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseSizeViewHolder holder, int position) {
        holder.bind(items.get(position));
        holder.mBinding.itemChooseSize.setOnClickListener(view -> {
            onRecyclerItemClickListener.onItemClicked(holder.mBinding.itemChooseSize, items.get(position), position);
        });

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class ChooseSizeViewHolder extends RecyclerView.ViewHolder {
        private ItemChooseSizeBinding mBinding;

        public ChooseSizeViewHolder(@NonNull ItemChooseSizeBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(ChooseSizeItem chooseSizeItem) {
//            mBinding.itemPagingCircleImage.setImageResource(paginationItem.getProfileCircleImage());
//            mBinding.imageView.setImageResource(paginationItem.getImage());
        }
    }
}
