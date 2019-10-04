package am.foursteps.pexel.data.remote.interceptor;


import androidx.annotation.NonNull;

import java.io.IOException;

import am.foursteps.pexel.AppConstants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request.Builder requestBuilder = originalRequest.newBuilder()
                .header("Authorization", AppConstants.O_PEXELS_API_KEY)
                .method(originalRequest.method(), originalRequest.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
