package am.foursteps.pexel.data.local;

import androidx.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.List;

import am.foursteps.pexel.R;

public final class SizeData {
    private static SizeData ourInstance;
    private static List<Sizes> sItems = new ArrayList<>();

    static {
        sItems.add(new Sizes(R.drawable.ic_check, "Original (3984 x 2656)"));
        sItems.add(new Sizes(R.drawable.ic_check_box, "Large (1920 x 1280)"));
        sItems.add(new Sizes(R.drawable.ic_check_box, "Medium (1280 x 853)"));
        sItems.add(new Sizes(R.drawable.ic_check_box, "Small (640 x 426)"));

    }

    public static SizeData getInstance() {
        if (ourInstance == null) {
            ourInstance = new SizeData();
        }
        return ourInstance;
    }

    private SizeData() {
    }

    public static List<Sizes> getItems() {
        return sItems;
    }

    public static class Sizes {

        @DrawableRes
        private int mCheckIcon;
        private String mSize;

        Sizes(int checkIcon, String size) {
            mCheckIcon = checkIcon;
            mSize = size;
        }

        public int getCheckIcon() {
            return mCheckIcon;
        }

        public void setCheckIcon(int checkIcon) {
            mCheckIcon = checkIcon;
        }

        public String getSize() {
            return mSize;
        }

        public void setSize(String size) {
            mSize = size;
        }

    }
}
