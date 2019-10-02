package am.foursteps.pexel.data.remote.api;


import am.foursteps.pexel.data.remote.model.ApiResponse;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Aren on 1/28/19.
 */
public interface ApiService {

    @GET("search")
    Single<ApiResponse> searchPhotos(@Query("query") String query, @Query("per_page") int pageCount, @Query("page") int page);

    @GET("curated")
    Single<ApiResponse> curatedPhotos(@Query("per_page") int pageCount, @Query("page") int page);

    @GET("search/{:id}")
    Single<ResponseBody> onePhoto(@Path("id") int id);
}
