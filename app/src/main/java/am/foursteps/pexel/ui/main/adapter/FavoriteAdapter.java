package am.foursteps.pexel.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.entity.FavoritePhotoEntity;
import am.foursteps.pexel.databinding.ItemPaginationListBinding;
import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;
import am.foursteps.pexel.ui.main.viewholder.ItemViewHolder;

public class FavoriteAdapter extends RecyclerBaseAdapter<FavoritePhotoEntity> {
    private List<FavoritePhotoEntity> items;
    private ItemPaginationListBinding mBinding;
    private OnRecyclerItemClickListener<FavoritePhotoEntity> onRecyclerItemClickListener;
    private ItemViewHolder mItemViewHolder;
    private float mProgress;


    public FavoriteAdapter(OnRecyclerItemClickListener<FavoritePhotoEntity> onRecyclerItemClickListener) {
        super(onRecyclerItemClickListener);
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
        items = new ArrayList<>();
    }

    public void addItems(List<FavoritePhotoEntity> items) {
        int startIndex = this.items.size();
        this.items.addAll(items);
        this.notifyItemRangeChanged(startIndex, items.size());
    }

    public void updateItem(int position, float progress) {
        FavoritePhotoEntity item = this.items.get(position);
        mProgress = progress;
        this.notifyItemChanged(position, item);
    }

    public void removeItem(int position) {
        items.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, items.size());
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPaginationListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_pagination_list, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty() && payloads.get(0) != null) {
                ((ItemViewHolder) holder).bindEntity((FavoritePhotoEntity) payloads.get(0), false);
        }else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, FavoritePhotoEntity item) {
        mBinding = mItemViewHolder.getBinding();
        mItemViewHolder = new ItemViewHolder(mBinding);
        mItemViewHolder.bindEntity(item, true);
        ViewCompat.setTransitionName(mBinding.photoImage, "Item" + holder.getAdapterPosition());

        mBinding.itemPagingConstraint.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(mBinding.itemPagingConstraint, item, holder.getAdapterPosition()));
        mBinding.itemPagingFavorite.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(mBinding.itemPagingFavorite, item, holder.getAdapterPosition()));
        mBinding.itemPagingDownload.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(mBinding.itemPagingDownload, item, holder.getAdapterPosition()));
        mBinding.itemPagingShare.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(mBinding.itemPagingShare, item, holder.getAdapterPosition()));
        mBinding.photoImage.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(mBinding.photoImage, item, holder.getAdapterPosition()));
    }
}
