package com.example.moviesbox.data;

import android.support.annotation.StringDef;

import com.example.moviesbox.model.Movie;
import com.example.moviesbox.model.Movies;
import com.example.moviesbox.service.MoviesApiClient;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MoviesRepository implements MoviesDataSource {
    // region CONSTANTS
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top-rated";

    private static MoviesRepository sInstance;
    // endregion

    // region TYPE DEFINITIONS
    @StringDef({POPULAR, TOP_RATED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SortOrder {}
    // endregion

    // region VARIABLES
    private List<Movie> mCachedMovies;
    @SortOrder private String mSortOrder;
    private boolean mLoading;
    private int mCurrentPage = 1;
    private int mTotalPages;
    // endregion

    // region PUBLIC METHODS
    public static MoviesRepository getInstance() {
        if (sInstance == null) {
            sInstance = new MoviesRepository();
        }

        return sInstance;
    }
    // endregion

    // region GETTERS AND SETTERS
    @SortOrder
    public String getSortOrder() {
        return mSortOrder;
    }

    public void setSortOrder(@SortOrder String sortOrder) {
        mSortOrder = sortOrder;
    }
    // endregion

    // region MOVIES DATA SOURCE
    @Override
    public Single<List<Movie>> getMovies(boolean forceLoad, String language) {
        if (mCachedMovies != null && !forceLoad) {
            return Single.just(mCachedMovies);
        }

        Single<Movies> request = null;

        switch (mSortOrder) {
            case POPULAR:
                request = MoviesApiClient.getInstance().getPopularMovies(mCurrentPage, language);

                break;
            case TOP_RATED:
                request = MoviesApiClient.getInstance().getTopRatedMovies(mCurrentPage, language);

                break;
        }

        if (request == null || mLoading) {
            return Single.just((List<Movie>)new ArrayList<Movie>());
        }

        mLoading = true;

        return request
                .doOnSuccess(new Consumer<Movies>() {
                    @Override
                    public void accept(@NonNull Movies movies) throws Exception {
                        mLoading = false;
                        mTotalPages = movies.getTotalPages();

                        if (mCachedMovies == null) {
                            mCachedMovies = new ArrayList<>();
                        }

                        mCachedMovies.addAll(movies.getMovies());
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mLoading = false;
                    }
                })
                .flatMap(new Function<Movies, SingleSource<? extends List<Movie>>>() {
                    @Override
                    public SingleSource<? extends List<Movie>> apply(@NonNull Movies movies) throws Exception {
                        return Single.just(movies.getMovies());
                    }
                });
    }

    @Override
    public boolean canGetMoreMovies() {
        if (!mLoading && (mCurrentPage < mTotalPages)) {
            mCurrentPage++;

            return true;
        }

        return false;
    }

    @Override
    public void refreshList() {
        mCurrentPage = 1;
        mCachedMovies = null;
        mTotalPages = 0;
    }
    // endregion
}
