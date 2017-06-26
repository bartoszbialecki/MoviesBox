package com.example.moviesbox.view;

import com.example.moviesbox.data.MoviesRepository;
import com.example.moviesbox.model.Movie;

import java.util.List;

public interface MoviesView extends BaseView {
    void showMovies(List<Movie> movies);
    void clearList();
    void showContentLoading();
    void hideContentLoading();
    void showEmptyResultsView();
    void hideEmptyResultsView();
    void showErrorMessage(String errorMessage);
    void onSortOrderChanged(@MoviesRepository.SortOrder String sortOrder);
}
