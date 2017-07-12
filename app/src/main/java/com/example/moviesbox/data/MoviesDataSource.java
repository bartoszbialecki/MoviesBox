package com.example.moviesbox.data;

import com.example.moviesbox.model.Movie;

import java.util.List;

import io.reactivex.Single;

public interface MoviesDataSource {
    Single<List<Movie>> getMovies(boolean forceLoad, String language);
    Movie getMovie(int movieId);
    boolean canGetMoreMovies();
    void refreshList();
}
