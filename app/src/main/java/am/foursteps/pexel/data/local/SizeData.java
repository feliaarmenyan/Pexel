package am.foursteps.pexel.data.local;

import androidx.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.List;

import am.foursteps.pexel.R;

public final class SizeData {
    private static SizeData ourInstance;
    private static List<Sizes> sItems = new ArrayList<>();

    static {
        sItems.add(new Sizes(R.drawable.ic_check, "Original "));
        sItems.add(new Sizes(R.drawable.ic_check_box, "Large (650 x 940)"));
        sItems.add(new Sizes(R.drawable.ic_check_box, "Medium (350 x 400)"));
        sItems.add(new Sizes(R.drawable.ic_check_box, "Small (130 x 200)"));
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

        public Sizes(int checkIcon) {
            mCheckIcon = checkIcon;
        }

        public Sizes(int checkIcon, String size) {
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
