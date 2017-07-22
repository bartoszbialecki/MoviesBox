package com.example.moviesbox.presenter;

import com.example.moviesbox.R;
import com.example.moviesbox.data.MoviesRepository;
import com.example.moviesbox.data.Settings;
import com.example.moviesbox.data.SharedPreferencesRepository;
import com.example.moviesbox.model.Movie;
import com.example.moviesbox.view.MoviesView;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MoviesPresenterImpl implements MoviesPresenter {
    // region VARIABLES
    private MoviesView mView;
    private SharedPreferencesRepository mPreferencesRepository;
    private MoviesRepository mMoviesRepository;
    private Settings mSettings;
    // endregion

    // region CONSTRUCTOR
    public MoviesPresenterImpl(MoviesView view, SharedPreferencesRepository preferencesRepository,
                               MoviesRepository moviesRepository, Settings settings) {
        mView = view;
        mPreferencesRepository = preferencesRepository;
        mMoviesRepository = moviesRepository;
        mSettings = settings;
    }
    // endregion

    // region PRESENTER METHODS
    @Override
    public void start() {
        loadPreferences();
    }

    @Override
    public void stop() {

    }
    // endregion

    // region MOVIES PRESENTER
    @Override
    public void fetchMovies(boolean forceLoad) {
        if (!mMoviesRepository.getSortOrder().equals(MoviesRepository.FAVORITES) && !mView.checkNetworkConnection()) {
            mView.showErrorMessage(R.string.no_network);

            return;
        }

        mMoviesRepository.getMovies(forceLoad, mSettings.getCurrentLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Movie>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Movie> movies) {
                        mView.hideContentLoading();
                        mView.showMovies(movies);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.hideContentLoading();
                        mView.showErrorMessage(e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void refreshList() {
        if (!mMoviesRepository.getSortOrder().equals(MoviesRepository.FAVORITES) && !mView.checkNetworkConnection()) {
            mView.showErrorMessage(R.string.no_network);
            mView.stopRefreshing();

            return;
        }

        mView.clearList();
        mView.showContentLoading();

        mMoviesRepository.refreshList();
        fetchMovies(true);
    }

    @Override
    public void onSortOrderPreferencesChange() {
        @MoviesRepository.SortOrder String sortOrder = mPreferencesRepository.sortOrder();

        if (!sortOrder.equals(mMoviesRepository.getSortOrder())) {
            mMoviesRepository.setSortOrder(sortOrder);

            refreshList();
            mView.onSortOrderChanged(sortOrder);
        }
    }

    @Override
    public void onListScrolled() {
        if (mMoviesRepository.canGetMoreMovies()) {
            fetchMovies(true);
        }
    }

    @Override
    public void onMovieChanged(int movieId) {
        mMoviesRepository.updateMovie(movieId);
    }
    // endregion

    // region PRIVATE METHODS
    private void loadPreferences() {
        @MoviesRepository.SortOrder String sortOrder = mPreferencesRepository.sortOrder();

        mMoviesRepository.setSortOrder(sortOrder);
        mView.onSortOrderChanged(sortOrder);
    }
    // endregion
}
