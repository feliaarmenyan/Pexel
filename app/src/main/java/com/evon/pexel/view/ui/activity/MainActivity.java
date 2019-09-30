package com.evon.pexel.view.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.evon.pexel.R;
import com.evon.pexel.databinding.ActivityMainBinding;
import com.evon.pexel.model.def.AnimationType;
import com.evon.pexel.utils.ActivityUtil;
import com.evon.pexel.view.ui.fragment.ListFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ActivityUtil.pushFragment(new ListFragment(), getSupportFragmentManager(), R.id.main_content, false, AnimationType.NONE);
    }

    public void updateStatusBarColor(String color) {// Color must be in hexadecimal fromat
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor(color));
    }


}

