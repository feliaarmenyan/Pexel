package am.foursteps.pexel.ui.main.viewholder;

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
       if(updateAll) {
           ImageUrlHelper.ImageUrl(mBinding.photoImage, image.getSrc());
           int maxHeight = DimensionUtils.getDisplayHeight(mBinding.getRoot().getContext()) * 2 / 3;

           int proportionalHeight = image.getHeight()
                   * DimensionUtils.getDisplayWidth(mBinding.getRoot().getContext())
                   / image.getWidth();
           mBinding.photoImage.getLayoutParams().height = Math.min(maxHeight, proportionalHeight);
           bindFavoriteItemImage(image);
       }else{
           //todo progress bar
       }
        }

    public void bindFavoriteItemImage(Image paginationItem) {
        if (!paginationItem.getIsFavorite()) {
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
            //todo progress bar
        }
    }
    public void bindFavoriteItemEntity(FavoritePhotoEntity favoritePhotoEntity) {
        mBinding.itemPagingFavorite.setImageResource(R.drawable.ic_favorite_red);
    }

}