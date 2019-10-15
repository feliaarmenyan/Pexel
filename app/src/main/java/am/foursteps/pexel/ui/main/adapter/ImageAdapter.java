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
import am.foursteps.pexel.data.remote.model.Image;
import am.foursteps.pexel.data.remote.model.ImageSrc;
import am.foursteps.pexel.databinding.ItemPaginationListBinding;
import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;
import am.foursteps.pexel.ui.base.util.ImageUrlHelper;
import am.foursteps.pexel.ui.main.viewholder.ItemViewHolder;
import am.foursteps.pexel.utils.DimensionUtils;

public class ImageAdapter extends RecyclerBaseAdapter<Image> {

    private List<Image> items;
    private ItemPaginationListBinding mBinding;
    private OnRecyclerItemClickListener<Image> onRecyclerItemClickListener;
    private ItemViewHolder mItemViewHolder;
    private float mProgress;


    public ImageAdapter(OnRecyclerItemClickListener<Image> onRecyclerItemClickListener) {
        super(onRecyclerItemClickListener);
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
        this.items = new ArrayList<>();
    }

    public String getUrl(int pos) {
        return items.get(pos).getUrl();
    }


    public void addItems(List<Image> items) {
        int startIndex = this.items.size();
        this.items.addAll(items);
        this.notifyItemRangeChanged(startIndex, items.size());
    }

    public void updateItem(int position, float progress){
        Image item = this.items.get(position);
        mProgress = progress;
        this.notifyItemChanged(position, item);
    }

    public void updateItem(int position, Image image, float progress) {
        this.items.set(position, image);
        mProgress = progress;
        this.notifyItemChanged(position, image);
    }

    public void clearItems(){
        items.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPaginationListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_pagination_list, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(!payloads.isEmpty() && payloads.get(0)!=null){
            if (mProgress < 0) {
                ((ItemViewHolder) holder).bindFavoriteItemImage((Image) payloads.get(0));
            } else {
                ((ItemViewHolder) holder).bindImage((Image) payloads.get(0),false);
            }
        }
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, Image item) {
        mBinding = mItemViewHolder.getBinding();
        mItemViewHolder = new ItemViewHolder(mBinding);
        mItemViewHolder.bindImage(item,true);
        ViewCompat.setTransitionName(mBinding.photoImage, "Item" + holder.getAdapterPosition());

        mBinding.itemPagingConstraint.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(mBinding.itemPagingConstraint, item, holder.getAdapterPosition()));
        mBinding.itemPagingFavorite.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(mBinding.itemPagingFavorite, item, holder.getAdapterPosition()));
        mBinding.itemPagingDownload.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(mBinding.itemPagingDownload, item, holder.getAdapterPosition()));
        mBinding.itemPagingShare.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(mBinding.itemPagingShare, item, holder.getAdapterPosition()));
        mBinding.photoImage.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(mBinding.photoImage, item, holder.getAdapterPosition()));
    }

}
