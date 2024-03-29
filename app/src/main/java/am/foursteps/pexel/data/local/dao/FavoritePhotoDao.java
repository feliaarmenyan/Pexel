package am.foursteps.pexel.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import am.foursteps.pexel.data.local.entity.FavoritePhotoEntity;
import io.reactivex.Single;


@Dao
public interface FavoritePhotoDao {

    @Transaction
    @Query("SELECT * FROM favoritephotoentity")
    Single<List<FavoritePhotoEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FavoritePhotoEntity favoritePhotoEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<FavoritePhotoEntity> favoritePhotoEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAll(List<FavoritePhotoEntity> favoritePhotoEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(FavoritePhotoEntity favoritePhotoEntity);

    @Query("DELETE FROM favoritephotoentity WHERE primaryKey=:primaryKey")
    void delete(String primaryKey);

    @Query("DELETE FROM favoritephotoentity")
    void deleteAll();
}
