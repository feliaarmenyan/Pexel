package com.evon.pexel.view.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.evon.pexel.R;
import com.evon.pexel.model.def.AnimationType;
import com.evon.pexel.utils.ActivityUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityUtil.pushFragment(new ListFragment(), getSupportFragmentManager(), R.id.main_container, false, AnimationType.NONE);

    }
}
