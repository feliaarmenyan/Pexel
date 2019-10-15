package am.foursteps.pexel.data.local;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import am.foursteps.pexel.data.local.converter.Converters;
import am.foursteps.pexel.data.local.dao.FavoritePhotoDao;
import am.foursteps.pexel.data.local.entity.FavoritePhotoEntity;


@Database(entities = {FavoritePhotoEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavoritePhotoDao mFavoritePhotoDao();
}
