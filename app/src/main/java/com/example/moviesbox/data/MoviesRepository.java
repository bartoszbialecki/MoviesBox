package com.example.moviesbox.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.StringDef;

import com.example.moviesbox.model.Movie;
import com.example.moviesbox.model.Movies;
import com.example.moviesbox.service.MoviesApiClient;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MoviesRepository implements MoviesDataSource {
    // region CONSTANTS
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top-rated";
    public static final String FAVORITES = "favorites";

    private static final int PAGE_LIMIT = 20;

    private static MoviesRepository sInstance;
    // endregion

    // region TYPE DEFINITIONS
    @StringDef({POPULAR, TOP_RATED, FAVORITES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SortOrder {}
    // endregion

    // region VARIABLES
    private Context mContext;
    private List<Movie> mCachedMovies;
    @SortOrder private String mSortOrder;
    private boolean mLoading;
    private int mCurrentPage = 1;
    private int mTotalPages;
    // endregion

    // region CONSTRUCTORS
    private MoviesRepository(Context context) {
        mContext = context.getApplicationContext();
    }
    // endregion

    // region PUBLIC METHODS
    public static MoviesRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MoviesRepository(context);
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
        boolean remoteRequest = true;

        switch (mSortOrder) {
            case POPULAR:
                request = MoviesApiClient.getInstance().getPopularMovies(mCurrentPage, language);

                break;
            case TOP_RATED:
                request = MoviesApiClient.getInstance().getTopRatedMovies(mCurrentPage, language);

                break;
            case FAVORITES:
                remoteRequest = false;

                request = Single.create(new SingleOnSubscribe<Movies>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<Movies> emitter) throws Exception {
                        if (!emitter.isDisposed()) {
                            String[] projection = new String[] { "count(*)" };

                            Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                    projection, null, null, null);

                            Movies movies = new Movies();

                            if (cursor != null) {
                                int count = 0;

                                if (cursor.getCount() > 0) {
                                    cursor.moveToFirst();
                                    count = cursor.getInt(0);
                                    int pages = (count + PAGE_LIMIT - 1) / PAGE_LIMIT;

                                    movies.setTotalResults(count);
                                    movies.setTotalPages(pages);
                                    movies.setCurrentPage(mCurrentPage);
                                }

                                cursor.close();

                                if (count > 0) {
                                    cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                            MovieContract.MovieEntry.PROJECTION_ALL, null, null,
                                            MovieContract.MovieEntry.SORT_ORDER_DEFAULT + " LIMIT " + PAGE_LIMIT + " OFFSET " + (mCurrentPage - 1) * PAGE_LIMIT);

                                    if (cursor != null) {
                                        if (cursor.moveToFirst()) {
                                            List<Movie> movieList = new ArrayList<>();

                                            do {
                                                Movie movie = cursorToMovie(cursor);
                                                movieList.add(movie);
                                            } while (cursor.moveToNext());

                                            if (movieList.size() > 0) {
                                                movies.setMovies(movieList);
                                            }
                                        }

                                        cursor.close();
                                    }
                                }
                            }

                            if (!emitter.isDisposed()) {
                                emitter.onSuccess(movies);
                            }
                        }
                    }
                });

                break;
        }

        if (request == null || mLoading) {
            return Single.just((List<Movie>)new ArrayList<Movie>());
        }

        mLoading = true;

        final boolean finalRemoteRequest = remoteRequest;

        return request
                .doOnSuccess(new Consumer<Movies>() {
                    @Override
                    public void accept(@NonNull Movies movies) throws Exception {
                        mLoading = false;
                        mTotalPages = movies.getTotalPages();
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
                        List<Movie> movieList = movies.getMovies();

                        if (finalRemoteRequest) {
                            // if we fetch data from remote web service, we want to get some information
                            // from the local database if exists
                            String[] projection = {MovieContract.MovieEntry.COLUMN_FAVORITE};

                            for (Movie movie : movieList) {
                                Cursor cursor = mContext.getContentResolver().query(ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, movie.getId()),
                                        projection, null, null, null);

                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        int favorite = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORITE));

                                        movie.setFavorite(favorite == 1);
                                    }

                                    cursor.close();
                                }
                            }
                        }

                        if (mCachedMovies == null) {
                            mCachedMovies = new ArrayList<>();
                        }

                        if (movieList == null) {
                            movieList = new ArrayList<>();
                        }

                        mCachedMovies.addAll(movieList);

                        return Single.just(movieList);
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

    @Override
    public Movie getMovie(int movieId) {
        for (Movie movie : mCachedMovies) {
            if (movie.getId() == movieId) {
                return movie;
            }
        }

        return null;
    }

    @Override
    public void updateMovie(int movieId) {
        for (Movie movie : mCachedMovies) {
            if (movie.getId() == movieId) {
                if (!movie.isFavorite()) {
                    mCachedMovies.remove(movie);
                    break;
                }
            }
        }
    }
    // endregion

    // region PRIVATE METHODS
    private Movie cursorToMovie(Cursor cursor) {
        Movie movie = new Movie();

        movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));
        movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT)));
        movie.setVideo(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_HAS_VIDEO)) == 1);
        movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
        movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
        movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE)));
        movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
        movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH)));
        movie.setAdult(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ADULT)) == 1);
        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
        movie.setReleaseDate(new Date(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE))));
        movie.setFavorite(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORITE)) == 1);

        return movie;
    }
    // endregion
}
