package com.evon.pexel.model.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.evon.pexel.GlideApp;
import com.evon.pexel.R;


public class UserImageView extends AppCompatImageView {

    private String url;

    public UserImageView(Context context) {
        super(context);
    }

    public UserImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UserImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UserImageView, defStyle, 0);
        url = a.getString(R.styleable.UserImageView_imageURL);
        a.recycle();

    }

    public void setImageURL(String url) {

        GlideApp.with(getContext())
                .load(url)
//                .error(R.drawable.ic_no_image)
                .into(this);

    }

}