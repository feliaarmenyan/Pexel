package com.evon.pexel.view.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.evon.pexel.R;
import com.evon.pexel.model.def.AnimationType;
import com.evon.pexel.utils.ActivityUtil;

public class MainActivity extends AppCompatActivity {
    private Bundle mTmpReenterState;
//    static final String EXTRA_CURRENT_ALBUM_POSITION = "extra_current_item_position";
//    public static int currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityUtil.pushFragment(new ListFragment(), getSupportFragmentManager(), R.id.container, false, AnimationType.NONE);
//        currentPosition = mTmpReenterStatterState.getInt(EXTRA_CURRENT_ALBUM_POSITION);
    }
}
