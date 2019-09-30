package com.evon.pexel;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;

public class ChooseSizeItem implements Parcelable {
    @DrawableRes
    private int mCheckIcon;
    private String mSize;

    public ChooseSizeItem(int checkIcon, String size) {
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

    protected ChooseSizeItem(Parcel in) {
        mCheckIcon = in.readInt();
        mSize = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCheckIcon);
        dest.writeString(mSize);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChooseSizeItem> CREATOR = new Parcelable.Creator<ChooseSizeItem>() {
        @Override
        public ChooseSizeItem createFromParcel(Parcel in) {
            return new ChooseSizeItem(in);
        }

        @Override
        public ChooseSizeItem[] newArray(int size) {
            return new ChooseSizeItem[size];
        }
    };}
