package am.foursteps.pexel.ui.main.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.def.AnimationType;
import am.foursteps.pexel.databinding.ActivityMainBinding;
import am.foursteps.pexel.ui.main.fragment.ListFragment;
import am.foursteps.pexel.utils.ActivityUtil;

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

