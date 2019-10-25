package am.foursteps.pexel.ui.main.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.entity.FavoritePhotoEntity;
import am.foursteps.pexel.data.remote.model.Image;
import am.foursteps.pexel.databinding.ItemPaginationListBinding;
import am.foursteps.pexel.ui.base.util.ImageUrlHelper;
import am.foursteps.pexel.utils.DimensionUtils;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private ItemPaginationListBinding mBinding;

    public ItemPaginationListBinding getBinding() {
        return mBinding;
    }

   public ItemViewHolder(@NonNull ItemPaginationListBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }
   public void bindImage(Image image,boolean updateAll) {
       if (updateAll) {
           ImageUrlHelper.ImageUrl(mBinding.photoImage, image.getSrc());
           int maxHeight = DimensionUtils.getDisplayHeight(mBinding.getRoot().getContext()) * 2 / 3;

           int proportionalHeight = image.getHeight()
                   * DimensionUtils.getDisplayWidth(mBinding.getRoot().getContext())
                   / image.getWidth();
           mBinding.photoImage.getLayoutParams().height = Math.min(maxHeight, proportionalHeight);
           bindFavoriteItemImage(image);

           mBinding.itemPagingDownload.setVisibility(View.VISIBLE);
           mBinding.itemPagingProgressBar.setVisibility(View.GONE);
           image.setDownloadProgress(-1);
       } else {
           if (image.getDownloadProgress() < 1 && image.getDownloadProgress()>=0) {
               mBinding.itemPagingDownload.setVisibility(View.GONE);
               mBinding.itemPagingProgressBar.setVisibility(View.VISIBLE);
               mBinding.itemPagingProgressBar.setProgress((int) (image.getDownloadProgress() * 100));
           } else {
               mBinding.itemPagingDownload.setVisibility(View.VISIBLE);
               mBinding.itemPagingProgressBar.setVisibility(View.GONE);
               image.setDownloadProgress(-1);
           }
       }
   }

    public void bindFavoriteItemImage(Image image) {
        if (!image.getIsFavorite()) {
            mBinding.itemPagingFavorite.setImageResource(R.drawable.ic_favorite_black);
        } else {
            mBinding.itemPagingFavorite.setImageResource(R.drawable.ic_favorite_red);
        }
    }

    public void bindEntity(FavoritePhotoEntity favoritePhotoEntity, boolean updateAll) {
        if(updateAll) {
            ImageUrlHelper.ImageUrl(mBinding.photoImage, favoritePhotoEntity.getImageSrc());
            int maxHeight = DimensionUtils.getDisplayHeight(mBinding.getRoot().getContext()) * 2 / 3;

            int proportionalHeight = favoritePhotoEntity.getHeight()
                    * DimensionUtils.getDisplayWidth(mBinding.getRoot().getContext())
                    / favoritePhotoEntity.getWidth();
            mBinding.photoImage.getLayoutParams().height = Math.min(maxHeight, proportionalHeight);
            bindFavoriteItemEntity(favoritePhotoEntity);
        }else{
            if (favoritePhotoEntity.getDownloadProgress() < 1) {
                mBinding.itemPagingDownload.setVisibility(View.GONE);
                mBinding.itemPagingProgressBar.setVisibility(View.VISIBLE);
                mBinding.itemPagingProgressBar.setProgress((int) (favoritePhotoEntity.getDownloadProgress() * 100));
            } else {
                mBinding.itemPagingDownload.setVisibility(View.VISIBLE);
                mBinding.itemPagingProgressBar.setVisibility(View.GONE);
                favoritePhotoEntity.setDownloadProgress(-1);
            }
        }
    }
    public void bindFavoriteItemEntity(FavoritePhotoEntity favoritePhotoEntity) {
        mBinding.itemPagingFavorite.setImageResource(R.drawable.ic_favorite_red);
    }

}