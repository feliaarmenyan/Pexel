package am.foursteps.pexel.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.remote.model.Image;
import am.foursteps.pexel.databinding.ItemPaginationListBinding;
import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;
import am.foursteps.pexel.ui.main.viewholder.ItemViewHolder;

public class ImageAdapter extends RecyclerBaseAdapter<Image> {

    public ImageAdapter(OnRecyclerItemClickListener<Image> onRecyclerItemClickListener) {
        super(onRecyclerItemClickListener);
    }

    public String getUrl(int pos) {
        return getItem(pos).getUrl();
    }

    public void addItems(List<Image> items) {
      super.addItems(items);
    }

    public void updateItem(String primaryKey){
        int index=-1;
        for (int i = 0; i < items.size(); i++) {
            String key = items.get(i).getHeight() + "_" + items.get(i).getWidth() + "_" + items.get(i).getUrl();
            if (key.equals(primaryKey)) {
                index = i;
                break;
            }
        }
        if(index!=-1){
            items.get(index).setIsFavorite(false);
            super.updateItem(index, getItem(index));
        }
    }

    public void updateItem(int position, float progress) {
        Image image = getItem(position);
        image.setDownloadProgress(progress);
        super.updateItem(position, image);
    }

    public void updateItemImage(int position, Image image) {
        super.updateItem(position, image);
    }

    public void clearItems(){
        super.clearItems();
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPaginationListBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_pagination_list, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty() && payloads.get(0) != null) {
            Image image = (Image) payloads.get(payloads.size() - 1);
            ((ItemViewHolder) holder).bindFavoriteItemImage(image);
            if (image.getDownloadProgress() >= 0) {
                ((ItemViewHolder) holder).bindImage(image, false);
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, Image item, int position) {
        ItemViewHolder itemViewHolder =  ((ItemViewHolder)holder);
        itemViewHolder.bindImage(item,true);
        ViewCompat.setTransitionName(itemViewHolder.getBinding().photoImage, "Item" + position);

        itemViewHolder.getBinding().itemPagingConstraint.setOnClickListener(view -> mOnRecyclerItemClickListener.onItemClicked(itemViewHolder.getBinding().itemPagingConstraint, item, position));
        itemViewHolder.getBinding().itemPagingFavorite.setOnClickListener(view -> mOnRecyclerItemClickListener.onItemClicked(itemViewHolder.getBinding().itemPagingFavorite, item, position));
        itemViewHolder.getBinding().itemPagingDownload.setOnClickListener(view -> mOnRecyclerItemClickListener.onItemClicked(itemViewHolder.getBinding().itemPagingDownload, item, position));
        itemViewHolder.getBinding().itemPagingShare.setOnClickListener(view -> mOnRecyclerItemClickListener.onItemClicked(itemViewHolder.getBinding().itemPagingShare, item, position));
        itemViewHolder.getBinding().photoImage.setOnClickListener(view -> mOnRecyclerItemClickListener.onItemClicked(itemViewHolder.getBinding().photoImage, item, position));
    }

}
