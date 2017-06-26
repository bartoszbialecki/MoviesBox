package com.example.moviesbox.service;

import com.example.moviesbox.model.Movies;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesApiService {
    @GET("movie/popular")
    Single<Movies> getPopularMovies(@Query("page") int page, @Query("language") String language);

    @GET("movie/top_rated")
    Single<Movies> getTopRatedMovies(@Query("page") int page, @Query("language") String language);
}
