package am.foursteps.pexel.di.module;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;

import javax.inject.Singleton;

import am.foursteps.pexel.data.local.AppDatabase;
import am.foursteps.pexel.data.local.dao.FavoritePhotoDao;
import am.foursteps.pexel.data.local.dao.FavoritePhotoSrcDao;
import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Provides
    @Singleton
    AppDatabase provideDatabase(@NonNull Application application) {
        return Room.databaseBuilder(application,
                AppDatabase.class, "pexel.db")
                .allowMainThreadQueries().build();
    }

    @Provides
    @Singleton
    FavoritePhotoDao favoritePhotoDao(@NonNull AppDatabase appDatabase) {
        return appDatabase.mFavoritePhotoDao();
    }


    @Provides
    @Singleton
    FavoritePhotoSrcDao favoritePhotoSrcDao(@NonNull AppDatabase appDatabase) {
        return appDatabase.mFavoritePhotoSrcDao();
    }

}
