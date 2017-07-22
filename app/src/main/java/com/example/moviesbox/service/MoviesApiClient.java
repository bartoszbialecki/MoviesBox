package com.example.moviesbox.service;

import com.example.moviesbox.BuildConfig;
import com.example.moviesbox.model.Movies;
import com.example.moviesbox.model.Reviews;
import com.example.moviesbox.model.Trailers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesApiClient {
    // region CONSTANTS
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final int TIMEOUT = 60;
    // endregion

    // region VARIABLES
    private static MoviesApiClient sInstance = new MoviesApiClient();
    private MoviesApiService mService;
    // endregion

    // region CONSTRUCTORS
    private MoviesApiClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("api_key", BuildConfig.MOVIES_DB_API_KEY)
                                .build();

                        Request.Builder requestBuilder = original.newBuilder()
                                .url(url);

                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mService = retrofit.create(MoviesApiService.class);
    }
    // endregion

    // region PUBLIC METHODS
    public static MoviesApiClient getInstance() {
        return sInstance;
    }

    public Single<Movies> getPopularMovies(int page, String language) {
        return mService.getMovies("popular", page, language);
    }

    public Single<Movies> getTopRatedMovies(int page, String language) {
        return mService.getMovies("top_rated", page, language);
    }

    public Single<Reviews> getReviews(int movieId, int page, String language) {
        return mService.getReviews(movieId, page, language);
    }

    public Single<Trailers> getTrailers(int movieId, String language) {
        return mService.getTrailers(movieId, language);
    }
    // endregion
}
