package am.foursteps.pexel.ui.main.viewmodel;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import am.foursteps.pexel.data.local.dao.FavoritePhotoDao;
import am.foursteps.pexel.data.local.entity.FavoritePhotoEntity;
import am.foursteps.pexel.data.remote.Resource;
import am.foursteps.pexel.data.remote.api.ApiService;
import am.foursteps.pexel.data.remote.model.ApiResponse;
import am.foursteps.pexel.data.repository.ApiRepository;
import am.foursteps.pexel.ui.base.BaseViewModel;


public class MainViewModel extends BaseViewModel {

    private ApiRepository mApiRepository;
    private MutableLiveData<Resource<ApiResponse>> listLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource<List<FavoritePhotoEntity>>> favoriteLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDelete = new MutableLiveData<>();
    private MutableLiveData<Boolean> isInsert = new MutableLiveData<>();


    @Inject
    public MainViewModel(ApiService apiService, FavoritePhotoDao favoritePhotoDao) {
        this.mApiRepository = new ApiRepository(apiService,favoritePhotoDao);
    }


    @SuppressLint("CheckResult")
    public void fetchPhotoList(int perPage, int page) {
        mApiRepository.curatedPhotos(perPage, page)
                .doOnSubscribe(this::addToDisposable)
                .subscribe(response -> {
                    getListLiveData().postValue(response);
                });
    }

    @SuppressLint("CheckResult")
    public void fetchSearchPhotoList(String searchString,int perPage, int page){
        mApiRepository.searchPhotos(searchString,perPage, page)
                .doOnSubscribe(this::addToDisposable)
                .subscribe(response -> {
                    getSearchListLiveData().postValue(response);
                });
    }

    @SuppressLint("CheckResult")
    public void fetchFavoriteList(){
        mApiRepository.getFavorites()
                .subscribe(response -> {
                    getFavoriteListLiveData().postValue((Resource<List<FavoritePhotoEntity>>) response);
                });
    }

    @SuppressLint("CheckResult")
    public void deleteFavorite(String primaryKey){
        mApiRepository.deleteFavorite(primaryKey)
                .doOnSubscribe(this::addToDisposable)
                .subscribe(response -> {
                    getIsDelete().postValue(response);
                });
    }

    @SuppressLint("CheckResult")
    public void insertFavorite(FavoritePhotoEntity favoritePhotoEntity){
        mApiRepository.insertFavorite(favoritePhotoEntity)
                .doOnSubscribe(this::addToDisposable)
                .subscribe(response -> {
                    getIsInsert().postValue(response);
                });
    }


    public MutableLiveData<Resource<ApiResponse>> getListLiveData() {
        return listLiveData;
    }

    public MutableLiveData<Resource<ApiResponse>> getSearchListLiveData() {
        return listLiveData;
    }

    public MutableLiveData<Resource<List<FavoritePhotoEntity>>> getFavoriteListLiveData() {
        return favoriteLiveData;
    }

    public MutableLiveData<Boolean> getIsDelete() {

        return isDelete;
    }

    public MutableLiveData<Boolean> getIsInsert() {
        return isInsert;
    }

}
