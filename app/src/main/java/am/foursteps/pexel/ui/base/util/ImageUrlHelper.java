package am.foursteps.pexel.ui.base.util;

import android.widget.ImageView;

import am.foursteps.pexel.GlideApp;
import am.foursteps.pexel.data.remote.model.ImageSrc;

public class ImageUrlHelper {

   public static void ImageUrl(ImageView imageView, ImageSrc imageSrc){
        GlideApp.with(imageView.getContext())
                .load(imageSrc.getOriginal())
                .thumbnail(GlideApp.with(imageView.getContext())
                        .load(imageSrc.getMedium())
                        .thumbnail(GlideApp.with(imageView.getContext())
                                .load(imageSrc.getSmall())))
                .dontAnimate().dontTransform().into(imageView);

    }
}
