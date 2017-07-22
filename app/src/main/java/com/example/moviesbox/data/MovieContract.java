package com.example.moviesbox.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.moviesbox.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private MovieContract() {}

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_HAS_VIDEO = "has_video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_FAVORITE = "favorite";
        // TODO: add genres

        public static final Uri CONTENT_URI = Uri.withAppendedPath(MovieContract.BASE_CONTENT_URI, TABLE_NAME);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final String[] PROJECTION_ALL = {_ID, COLUMN_TITLE,
                COLUMN_ORIGINAL_TITLE, COLUMN_ORIGINAL_LANGUAGE, COLUMN_VOTE_COUNT, COLUMN_HAS_VIDEO,
                COLUMN_VOTE_AVERAGE, COLUMN_POPULARITY, COLUMN_POSTER_PATH, COLUMN_BACKDROP_PATH,
                COLUMN_ADULT, COLUMN_OVERVIEW, COLUMN_RELEASE_DATE, COLUMN_FAVORITE};
        public static final String SORT_ORDER_DEFAULT = COLUMN_TITLE + " ASC";
    }
}
