package am.foursteps.pexel.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import am.foursteps.pexel.data.local.entity.FavoritePhotoSrcEntity;
import io.reactivex.Flowable;

@Dao
public interface FavoritePhotoSrcDao {

    @Transaction
    @Query("SELECT * FROM favoritephotosrcentity")
    Flowable<List<FavoritePhotoSrcEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FavoritePhotoSrcEntity favoritePhotoSrcEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<FavoritePhotoSrcEntity> favoritePhotoSrcEntities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAll(List<FavoritePhotoSrcEntity> favoritePhotoSrcEntities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(FavoritePhotoSrcEntity favoritePhotoSrcEntity);

    @Query("DELETE FROM FavoritePhotoSrcEntity WHERE id=:id")
    void delete(long id);

    @Query("DELETE FROM favoritephotosrcentity")
    void deleteAll();
}

