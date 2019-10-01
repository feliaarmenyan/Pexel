package am.foursteps.pexel.data.local.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;

public class PaginationItem implements Parcelable {
    @DrawableRes
    private int  mProfileCircleImage;
    @DrawableRes
    private int  mImage;

    private float progress;


    public PaginationItem() {
    }

    public PaginationItem(int profileCircleImage, int image) {
        mProfileCircleImage = profileCircleImage;
        mImage = image;
    }

    public int getProfileCircleImage() {
        return mProfileCircleImage;
    }

    public void setProfileCircleImage(int profileCircleImage) {
        mProfileCircleImage = profileCircleImage;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int image) {
        mImage = image;
    }


    public float getProgres() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    protected PaginationItem(Parcel in) {
        mProfileCircleImage = in.readInt();
        mImage = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mProfileCircleImage);
        dest.writeInt(mImage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PaginationItem> CREATOR = new Parcelable.Creator<PaginationItem>() {
        @Override
        public PaginationItem createFromParcel(Parcel in) {
            return new PaginationItem(in);
        }

        @Override
        public PaginationItem[] newArray(int size) {
            return new PaginationItem[size];
        }
    };
}
