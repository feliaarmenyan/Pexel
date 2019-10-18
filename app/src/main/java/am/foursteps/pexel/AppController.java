package am.foursteps.pexel;

import android.app.Activity;
import android.app.Application;

import com.downloader.PRDownloader;

import javax.inject.Inject;

import am.foursteps.pexel.di.component.DaggerApiComponent;
import am.foursteps.pexel.di.module.ApiModule;
import am.foursteps.pexel.di.module.DbModule;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


public class AppController extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerApiComponent.builder()
                .application(this)
                .dbModule(new DbModule())
                .apiModule(new ApiModule())
                .build()
                .inject(this);

        PRDownloader.initialize(getApplicationContext());

    }
}
