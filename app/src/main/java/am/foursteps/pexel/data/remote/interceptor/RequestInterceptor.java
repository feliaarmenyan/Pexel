package am.foursteps.pexel.data.remote.interceptor;


import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Objects;

import am.foursteps.pexel.AppConstants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    private static int remainingCountKeyA = -1;
    private static int remainingCountKeyB = -1;
    private static int remainingCountKeyC = -1;
    private static int remainingCountKeyD = -1;

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Response response;
        Request.Builder requestBuilder = originalRequest.newBuilder()
                .method(originalRequest.method(), originalRequest.body());
        if (remainingCountKeyA == -1) {
            requestBuilder.header("Authorization", AppConstants.O_PEXELS_API_KEY);
            Request request = requestBuilder.build();
            response = chain.proceed(request);
            int remainingCount = Integer.parseInt(Objects.requireNonNull(response.header("x-ratelimit-remaining")));
            if (remainingCount < 100) {
                remainingCountKeyA = 0;
            }
        } else if (remainingCountKeyB == -1) {
            requestBuilder.header("Authorization", AppConstants.A_PEXELS_API_KEY);
            Request request = requestBuilder.build();
            response = chain.proceed(request);
            int remainingCount = Integer.parseInt(Objects.requireNonNull(response.header("x-ratelimit-remaining")));
            if (remainingCount < 100) {
                remainingCountKeyB = 0;
            }
        } else if (remainingCountKeyC == -1) {
            requestBuilder.header("Authorization", AppConstants.S_PEXELS_API_KEY);
            Request request = requestBuilder.build();
            response = chain.proceed(request);
            int remainingCount = Integer.parseInt(Objects.requireNonNull(response.header("x-ratelimit-remaining")));
            if (remainingCount < 100) {
                remainingCountKeyC = 0;
            }
        } else {
            requestBuilder.header("Authorization", AppConstants.F_PEXELS_API_KEY);
            Request request = requestBuilder.build();
            response = chain.proceed(request);
            int remainingCount = Integer.parseInt(Objects.requireNonNull(response.header("x-ratelimit-remaining")));
            if (remainingCount < 100) {
                remainingCountKeyD = 0;
                remainingCountKeyA = -1;
                remainingCountKeyB = -1;
                remainingCountKeyC = -1;
            }
        }
        return response;
    }
}
