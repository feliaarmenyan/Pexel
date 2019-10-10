package am.foursteps.pexel.ui.main.viewmodel;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import am.foursteps.pexel.data.local.dao.FavoritePhotoDao;
import am.foursteps.pexel.data.local.dao.FavoritePhotoSrcDao;
import am.foursteps.pexel.data.remote.Resource;
import am.foursteps.pexel.data.remote.api.ApiService;
import am.foursteps.pexel.data.remote.model.ApiResponse;
import am.foursteps.pexel.data.repository.ApiRepository;
import am.foursteps.pexel.ui.base.BaseViewModel;


public class MainViewModel extends BaseViewModel {

    private ApiRepository mApiRepository;
    private MutableLiveData<Resource<ApiResponse>> listLiveData = new MutableLiveData<>();

    @Inject
    public MainViewModel(ApiService apiService, FavoritePhotoDao favoritePhotoDao, FavoritePhotoSrcDao favoritePhotoSrcDao) {
        this.mApiRepository = new ApiRepository(apiService,favoritePhotoDao,favoritePhotoSrcDao);
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


    public MutableLiveData<Resource<ApiResponse>> getListLiveData() {
        return listLiveData;
    }

    public MutableLiveData<Resource<ApiResponse>> getSearchListLiveData() {
        return listLiveData;
    }

}
