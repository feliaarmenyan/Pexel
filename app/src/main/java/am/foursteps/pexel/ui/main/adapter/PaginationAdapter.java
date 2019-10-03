package am.foursteps.pexel.ui.main.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.def.ItemType;
import am.foursteps.pexel.data.remote.model.Image;
import am.foursteps.pexel.databinding.ItemPaginationListBinding;
import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;
import am.foursteps.pexel.ui.base.util.ImageUrlHelper;
import am.foursteps.pexel.utils.DimensionUtils;

/**
 * Created by amitshekhar on 15/03/17.
 */

public class PaginationAdapter extends RecyclerView.Adapter<PaginationAdapter.ItemViewHolder> {

    private List<Image> items;
    private OnRecyclerItemClickListener<Image> onRecyclerItemClickListener;
    private ItemType mItemType;
    private float mprogress;


    public PaginationAdapter(OnRecyclerItemClickListener<Image> onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
        this.items = new ArrayList<>();
    }

    public String getUrl(int pos) {
        return items.get(pos).getUrl();
    }



    public void updateItem(int position, float progress){
        Image item = this.items.get(position);
        mprogress = progress;
//        item.setProgress(progress);
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
            if (mprogress < 0) {
                holder.bind((Image) payloads.get(0), mItemType);
            } else {
                holder.bind((Image) payloads.get(0));

            }
        }
        super.onBindViewHolder(holder, position, payloads);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(items.get(position));
        ViewCompat.setTransitionName(holder.mBinding.photoImage, "Item" + position);
        holder.mBinding.itemPagingConstraint.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(holder.mBinding.itemPagingConstraint, items.get(position), position));
        holder.mBinding.itemPagingFavorite.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(holder.mBinding.itemPagingFavorite, items.get(position), position));
        holder.mBinding.itemPagingDownload.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(holder.mBinding.itemPagingDownload, items.get(position), position));
        holder.mBinding.itemPagingShare.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(holder.mBinding.itemPagingShare, items.get(position), position));
        holder.mBinding.photoImage.setOnClickListener(view -> onRecyclerItemClickListener.onItemClicked(holder.mBinding.photoImage, items.get(position), position));
    }

    public void addItems(List<Image> items) {
        int startIndex = this.items.size();
        this.items.addAll(items);
        this.notifyItemRangeChanged(startIndex, items.size());
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

        void bind(Image image) {
            ImageUrlHelper.ImageUrl(mBinding.photoImage,image.getSrc());
            int maxHeight = DimensionUtils.getDisplayHeight(mBinding.getRoot().getContext()) * 2 / 3;

            int proportionalHeight = image.getHeight()
                    * DimensionUtils.getDisplayWidth(mBinding.getRoot().getContext())
                    /image.getWidth();
            mBinding.photoImage.getLayoutParams().height = Math.min(maxHeight, proportionalHeight);


//            if(paginationItem.getProgres()>0 && paginationItem.getProgres() <100){
//                mBinding.itemPagingDownload.setVisibility(View.GONE);
//                mBinding.itemPagingProgressBar.setVisibility(View.VISIBLE);
//
//                mBinding.itemPagingProgressBar.setProgress((int) paginationItem.getProgres());
//
//            }else{
//                mBinding.itemPagingDownload.setVisibility(View.VISIBLE);
//                mBinding.itemPagingProgressBar.setVisibility(View.GONE);
//            }
        }

        void bind(Image paginationItem, ItemType itemType) {
//            if (paginationItem.getItemType() == ItemType.NOTFAVORITE) {
//                paginationItem.setItemType(ItemType.FAVORITE);
//                mBinding.itemPagingFavorite.setImageResource(R.drawable.ic_favorite_red);
//            } else {
//                paginationItem.setItemType(ItemType.NOTFAVORITE);
//                mBinding.itemPagingFavorite.setImageResource(R.drawable.ic_favorite_black);
//            }

        }
    }
}
