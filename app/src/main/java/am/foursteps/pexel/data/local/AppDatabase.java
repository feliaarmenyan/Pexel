package am.foursteps.pexel.data.local;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import am.foursteps.pexel.data.local.dao.FavoritePhotoDao;
import am.foursteps.pexel.data.local.dao.FavoritePhotoSrcDao;
import am.foursteps.pexel.data.local.entity.FavoritePhotoEntity;
import am.foursteps.pexel.data.local.entity.FavoritePhotoSrcEntity;


@Database(entities = {FavoritePhotoEntity.class, FavoritePhotoSrcEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavoritePhotoDao mFavoritePhotoDao();

    public abstract FavoritePhotoSrcDao mFavoritePhotoSrcDao();

}
