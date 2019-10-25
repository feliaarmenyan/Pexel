package am.foursteps.pexel.utils;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.def.AnimationType;

public class ActivityUtil {

    public static void pushFragment(@NonNull Fragment fragment, @NonNull FragmentManager fm, @IdRes int frameId, boolean addToBackStack, AnimationType type) {
        FragmentTransaction transaction = fm.beginTransaction();
        switch (type) {
            case BOTTOM_TO_TOP:
                transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top, R.anim.slide_in_bottom, R.anim.slide_out_top);
                break;
            case TOP_TO_BOTTOM:
                transaction.setCustomAnimations(R.anim.slide_out_top, R.anim.slide_in_right, R.anim.slide_out_top, R.anim.slide_in_right);
                break;
            case RIGHT_TO_LEFT:
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case LEFT_TO_RIGHT:
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
        if (addToBackStack) {
            transaction.replace(frameId, fragment, fragment.getClass().getSimpleName());
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        } else {
            transaction.replace(frameId, fragment, fragment.getClass().getSimpleName());
        }

        transaction.commit();
    }

    public static void backToHomeScreen(@NonNull FragmentManager fragmentManager) {
        int backStackCount = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            int backStackId = fragmentManager.getBackStackEntryAt(i).getId();
            fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
