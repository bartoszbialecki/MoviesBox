package com.example.moviesbox.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoviesDbHelper extends SQLiteOpenHelper {
    // region CONSTANTS
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    // endregion

    // region CONSTRUCTORS
    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // endregion

    // region SQLiteOpenHelper METHODS
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");

        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID                        + " INTEGER PRIMARY KEY, " +
                        MovieContract.MovieEntry.COLUMN_VOTE_COUNT          + " INTEGER DEFAULT 0, " +
                        MovieContract.MovieEntry.COLUMN_HAS_VIDEO           + " INTEGER DEFAULT 0, " +
                        MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE        + " REAL, " +
                        MovieContract.MovieEntry.COLUMN_TITLE               + " TEXT, " +
                        MovieContract.MovieEntry.COLUMN_POPULARITY          + " REAL, " +
                        MovieContract.MovieEntry.COLUMN_POSTER_PATH         + " TEXT, " +
                        MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE   + " TEXT, " +
                        MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE      + " TEXT, " +
                        MovieContract.MovieEntry.COLUMN_BACKDROP_PATH       + " TEXT, " +
                        MovieContract.MovieEntry.COLUMN_ADULT               + " INTEGER DEFAULT 0, " +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW            + " TEXT, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE        + " INTEGER, " +
                        MovieContract.MovieEntry.COLUMN_FAVORITE            + " INTEGER DEFAULT 0);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);

        final String SQL_CREATE_REVIEWS_TABLE =
                "CREATE TABLE " + ReviewContract.ReviewEntry.TABLE_NAME + " (" +
                        ReviewContract.ReviewEntry._ID              + " INTEGER PRIMARY KEY, " +
                        ReviewContract.ReviewEntry.COLUMN_AUTHOR    + " TEXT, " +
                        ReviewContract.ReviewEntry.COLUMN_CONTENT   + " TEXT, " +
                        ReviewContract.ReviewEntry.COLUMN_URL       + " TEXT, " +
                        ReviewContract.ReviewEntry.COLUMN_MOVIE_ID  + " INTEGER, " +
                        "FOREIGN KEY(`" + ReviewContract.ReviewEntry.COLUMN_MOVIE_ID + "`) " +
                            "REFERENCES " + MovieContract.MovieEntry.TABLE_NAME + "(`" + MovieContract.MovieEntry._ID +
                        "`) ON DELETE CASCADE);";

        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);

        final String SQL_CREATE_TRAILERS_TABLE =
                "CREATE TABLE " + TrailerContract.TrailerEntry.TABLE_NAME + " (" +
                        TrailerContract.TrailerEntry._ID                + " INTEGER PRIMARY KEY, " +
                        TrailerContract.TrailerEntry.COLUMN_KEY         + " TEXT, " +
                        TrailerContract.TrailerEntry.COLUMN_NAME        + " TEXT, " +
                        TrailerContract.TrailerEntry.COLUMN_SITE        + " TEXT, " +
                        TrailerContract.TrailerEntry.COLUMN_SIZE        + " INTEGER DEFAULT 0, " +
                        TrailerContract.TrailerEntry.COLUMN_TYPE        + " TEXT, " +
                        TrailerContract.TrailerEntry.COLUMN_ISO_6391    + " TEXT, " +
                        TrailerContract.TrailerEntry.COLUMN_ISO_31661   + " TEXT, " +
                        TrailerContract.TrailerEntry.COLUMN_MOVIE_ID    + " INTEGER, " +
                        "FOREIGN KEY(`" + TrailerContract.TrailerEntry.COLUMN_MOVIE_ID + "`) " +
                        "REFERENCES " + MovieContract.MovieEntry.TABLE_NAME + "(`" + MovieContract.MovieEntry._ID +
                        "`) ON DELETE CASCADE);";

        sqLiteDatabase.execSQL(SQL_CREATE_TRAILERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    // endregion
}
