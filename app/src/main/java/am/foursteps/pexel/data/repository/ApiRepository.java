package am.foursteps.pexel.data.repository;

import androidx.annotation.MainThread;

import javax.inject.Singleton;

import am.foursteps.pexel.data.local.dao.FavoritePhotoDao;
import am.foursteps.pexel.data.local.dao.FavoritePhotoSrcDao;
import am.foursteps.pexel.data.remote.Resource;
import am.foursteps.pexel.data.remote.api.ApiService;
import am.foursteps.pexel.data.remote.model.ApiResponse;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class ApiRepository {

    private ApiService mApiService;
    private FavoritePhotoDao  mFavoritePhotoDao;
    private FavoritePhotoSrcDao mFavoritePhotoSrcDao;


    public ApiRepository(ApiService apiService, FavoritePhotoDao favoritePhotoDao, FavoritePhotoSrcDao favoritePhotoSrcDao) {
        mApiService = apiService;
        mFavoritePhotoDao = favoritePhotoDao;
        mFavoritePhotoSrcDao = favoritePhotoSrcDao;
    }

    @MainThread
    public Single<Resource<ApiResponse>> searchPhotos(String query, int perPage, int page) {

        return mApiService.searchPhotos(query, perPage, page)
                .subscribeOn(Schedulers.io())
                .flatMap(response -> Single.just(response == null
                        ? Resource.error("null result", new ApiResponse())
                        : Resource.success(response)))
                .doOnError(t -> {
                    Timber.e(t);
                })
                .onErrorResumeNext(throwable -> Single.just(
                        Resource.error(throwable.getMessage(), new ApiResponse())))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @MainThread
    public Single<Resource<ApiResponse>> curatedPhotos(int perPage, int page) {

        return mApiService.curatedPhotos(perPage, page)
                .subscribeOn(Schedulers.io())
                .flatMap(response -> Single.just(response == null
                        ? Resource.error("null result", new ApiResponse())
                        : Resource.success(response)))
                .doOnError(t -> {
                    Timber.e(t);
                })
                .onErrorResumeNext(throwable -> Single.just(
                        Resource.error(throwable.getMessage(), new ApiResponse())))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
