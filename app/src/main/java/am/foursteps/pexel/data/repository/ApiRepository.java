package am.foursteps.pexel.data.repository;

import androidx.annotation.MainThread;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import am.foursteps.pexel.data.local.dao.FavoritePhotoDao;
import am.foursteps.pexel.data.local.entity.FavoritePhotoEntity;
import am.foursteps.pexel.data.remote.Resource;
import am.foursteps.pexel.data.remote.api.ApiService;
import am.foursteps.pexel.data.remote.model.ApiResponse;
import am.foursteps.pexel.data.remote.model.Image;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class ApiRepository {

    private ApiService mApiService;
    private FavoritePhotoDao  mFavoritePhotoDao;


    public ApiRepository(ApiService apiService, FavoritePhotoDao favoritePhotoDao) {
        mApiService = apiService;
        mFavoritePhotoDao = favoritePhotoDao;
    }

    @MainThread
    public Single<Resource<ApiResponse>> searchPhotos(String query, int perPage, int page) {

        return mApiService.searchPhotos(query, perPage, page)
                .subscribeOn(Schedulers.io())
                .flatMap(response -> {
                    List<Image> imageList = response.getPhotos();
                    List<FavoritePhotoEntity> photoEntityList = mFavoritePhotoDao.getAll().blockingGet();
                    for (Image image : imageList) {
                        String key = image.getHeight() + "_" + image.getWidth() + "_" + image.getUrl();
                        for (int i = 0; i < photoEntityList.size(); i++) {
                            if (photoEntityList.get(i).getPrimaryKey().equals(key)) {
                                image.setIsFavorite(true);
                            }
                        }
                    }
                    response.setPhotos(imageList);
                    return Single.just(Resource.success(response));
                })
                .doOnError(Timber::e)
                .onErrorResumeNext(throwable -> Single.just(
                        Resource.error(throwable.getMessage(), new ApiResponse())))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @MainThread
    public Single<Resource<ApiResponse>> curatedPhotos(int perPage, int page) {

        return mApiService.curatedPhotos(perPage, page)
                .subscribeOn(Schedulers.io())
                .flatMap(response -> {
                            List<Image> imageList = response.getPhotos();
                            List<FavoritePhotoEntity> photoEntityList = mFavoritePhotoDao.getAll().blockingGet();
                            for (Image image : imageList) {
                                String key = image.getHeight() + "_" + image.getWidth() + "_" + image.getUrl();
                                for (int i = 0; i < photoEntityList.size(); i++) {
                                    if (photoEntityList.get(i).getPrimaryKey().equals(key)) {
                                        image.setIsFavorite(true);
                                    }
                                }
                            }
                            response.setPhotos(imageList);
                            return Single.just(Resource.success(response));
                        }
                )
                .doOnError(Timber::e)
                .onErrorResumeNext(throwable -> Single.just(
                        Resource.error(throwable.getMessage(), new ApiResponse())))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @MainThread
    public Single<Resource<? extends List<FavoritePhotoEntity>>> getFavorites() {

        return mFavoritePhotoDao.getAll()
                .subscribeOn(Schedulers.io())
                .flatMap(favorites -> {
                    if (favorites!= null) {
                        return Single.just(Resource.success(favorites));
                    } else {
                        return Single.just(Resource.error("Null Result from Db", new ArrayList<FavoritePhotoEntity>()));
                    }
                })
                .doOnError(throwable -> {
                    Timber.e("error->" + throwable);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @MainThread
    public Single<Boolean> insertFavorite(FavoritePhotoEntity srcEntity) {
        try {
            mFavoritePhotoDao.insert(srcEntity);
            return Single.just(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Single.just(false);
        }
    }

    @MainThread
    public Single<Boolean> deleteFavorite(String primaryKey) {
        try {
            mFavoritePhotoDao.delete(primaryKey);
            return Single.just(true);
        } catch (Exception e) {
            e.printStackTrace();
            Timber.e("error-> Haven't this object");
            return Single.just(false);
        }
    }
}
