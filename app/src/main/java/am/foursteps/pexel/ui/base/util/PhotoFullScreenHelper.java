package am.foursteps.pexel.ui.base.util;

import android.transition.Fade;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.remote.model.ImageSrc;
import am.foursteps.pexel.ui.main.fragment.ItemFragment;
import am.foursteps.pexel.utils.DetailsTransition;

public class PhotoFullScreenHelper {

    public PhotoFullScreenHelper() {
    }


    public void fullScreen(FragmentManager fragmentManager, View view, ImageSrc src){
        ItemFragment listFragment = ItemFragment.newInstance(src);
        listFragment.setSharedElementEnterTransition(new DetailsTransition());
        listFragment.setEnterTransition(new Fade());
        listFragment.setExitTransition(new Fade());
        listFragment.setSharedElementReturnTransition(new DetailsTransition());

        fragmentManager
                .beginTransaction()
                .addSharedElement(view, "image")
                .replace(R.id.main_content, listFragment)
                .addToBackStack(null)
                .commit();
    }
}
