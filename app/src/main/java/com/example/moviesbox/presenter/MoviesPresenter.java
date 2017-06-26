package com.example.moviesbox.presenter;

import com.example.moviesbox.view.MoviesView;

public interface MoviesPresenter extends BasePresenter<MoviesView> {
    void fetchMovies(boolean forceLoad);
    void refreshList();
    void onSortOrderPreferencesChange();
    void onListScrolled();
}
