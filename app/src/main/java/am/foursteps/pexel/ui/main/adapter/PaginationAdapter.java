package am.foursteps.pexel.ui.main.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.model.PaginationItem;
import am.foursteps.pexel.databinding.ItemPaginationListBinding;
import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;

/**
 * Created by amitshekhar on 15/03/17.
 */

public class PaginationAdapter extends RecyclerView.Adapter<PaginationAdapter.ItemViewHolder> {

    private List<PaginationItem> items;
    private OnRecyclerItemClickListener<PaginationItem> onRecyclerItemClickListener;

    public PaginationAdapter(OnRecyclerItemClickListener<PaginationItem> onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
        this.items = new ArrayList<>();
    }

    public void updateItem(int position, float progress){
        PaginationItem item =this.items.get(position);
        item.setProgress(progress);
        this.notifyItemChanged(position, item);
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPaginationListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_pagination_list, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(!payloads.isEmpty() && payloads.get(0)!=null){
            holder.bind((PaginationItem) payloads.get(0));
        }
        super.onBindViewHolder(holder, position, payloads);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(items.get(position));
        ViewCompat.setTransitionName(holder.mBinding.photoImage, "Item" + position);
        holder.mBinding.photoImage.setImageResource(R.drawable.ic_profile_5);
        holder.mBinding.itemPagingConstraint.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(holder.mBinding.itemPagingConstraint, items.get(position), position));
        holder.mBinding.itemPagingFavorite.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(holder.mBinding.itemPagingFavorite, items.get(position), position));
        holder.mBinding.itemPagingDownload.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(holder.mBinding.itemPagingDownload, items.get(position), position));
        holder.mBinding.itemPagingShare.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(holder.mBinding.itemPagingShare, items.get(position), position));
        holder.mBinding.photoImage.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(holder.mBinding.photoImage, items.get(position), position));
    }

    public void addItems(List<PaginationItem> items) {
        this.items.addAll(items);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private ItemPaginationListBinding mBinding;

        ItemViewHolder(@NonNull ItemPaginationListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(PaginationItem paginationItem) {
            Log.e("Log_tag", "Tick of Progress"+ paginationItem.getProgres());
            if(paginationItem.getProgres()>0 && paginationItem.getProgres() <100){
                mBinding.itemPagingDownload.setVisibility(View.GONE);
                mBinding.itemPagingProgressBar.setVisibility(View.VISIBLE);

                mBinding.itemPagingProgressBar.setProgress((int) paginationItem.getProgres());

            }else{
                mBinding.itemPagingDownload.setVisibility(View.VISIBLE);
                mBinding.itemPagingProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
