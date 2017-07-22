package com.example.moviesbox.data;

import android.provider.BaseColumns;

public final class TrailerContract {
    private TrailerContract() {}

    public static final class TrailerEntry implements BaseColumns {
        public static final String TABLE_NAME = "trailers";

        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_ISO_6391 = "iso_6391";
        public static final String COLUMN_ISO_31661 = "iso_31661";
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }
}
