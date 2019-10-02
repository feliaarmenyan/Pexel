package am.foursteps.pexel.di.component;


import android.app.Application;

import javax.inject.Singleton;

import am.foursteps.pexel.AppController;
import am.foursteps.pexel.di.module.ActivityModule;
import am.foursteps.pexel.di.module.ApiModule;
import am.foursteps.pexel.di.module.FragmentModule;
import am.foursteps.pexel.di.module.ViewModelModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {ApiModule.class, ViewModelModule.class,
        AndroidSupportInjectionModule.class, ActivityModule.class, FragmentModule.class})
public interface ApiComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder apiModule(ApiModule apiModule);

        ApiComponent build();
    }

    void inject(AppController appController);
}
