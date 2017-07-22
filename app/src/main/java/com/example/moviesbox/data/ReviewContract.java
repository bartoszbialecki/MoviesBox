package com.example.moviesbox.data;

import android.provider.BaseColumns;

public final class ReviewContract {
    private ReviewContract() {}

    public static final class ReviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }
}
