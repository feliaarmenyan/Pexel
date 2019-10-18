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
    private OnRecyclerItemClickListener<FavoritePhotoEntity> onRecyclerItemClickListener;


    public FavoriteAdapter(OnRecyclerItemClickListener<FavoritePhotoEntity> onRecyclerItemClickListener) {
        super(onRecyclerItemClickListener);
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
        items = new ArrayList<>();
    }

    public void addItems(List<FavoritePhotoEntity> items) {
      super.addItems(items);
    }

    public void updateItem(int position, float progress) {
        FavoritePhotoEntity favoritePhotoEntity = getItem(position);
        favoritePhotoEntity.setDownloadProgress(progress);
        super.updateItem(position, favoritePhotoEntity);
    }

    public void removeItem(int position) {
        super.removeItem(position);
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPaginationListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_pagination_list, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, FavoritePhotoEntity item, int position) {
        ItemViewHolder itemViewHolder =  ((ItemViewHolder)holder);
        ((ItemViewHolder) holder).bindEntity(item, true);
        ViewCompat.setTransitionName(itemViewHolder.getBinding().photoImage, "Item" + holder.getAdapterPosition());

        itemViewHolder.getBinding().itemPagingConstraint.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(itemViewHolder.getBinding().itemPagingConstraint, item, holder.getAdapterPosition()));
        itemViewHolder.getBinding().itemPagingFavorite.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(itemViewHolder.getBinding().itemPagingFavorite, item, holder.getAdapterPosition()));
        itemViewHolder.getBinding().itemPagingDownload.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(itemViewHolder.getBinding().itemPagingDownload, item, holder.getAdapterPosition()));
        itemViewHolder.getBinding().itemPagingShare.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(itemViewHolder.getBinding().itemPagingShare, item, holder.getAdapterPosition()));
        itemViewHolder.getBinding().photoImage.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(itemViewHolder.getBinding().photoImage, item, holder.getAdapterPosition()));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty() && payloads.get(0) != null) {
            ((ItemViewHolder) holder).bindEntity((FavoritePhotoEntity) payloads.get(payloads.size() - 1), false);
        }else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

}
