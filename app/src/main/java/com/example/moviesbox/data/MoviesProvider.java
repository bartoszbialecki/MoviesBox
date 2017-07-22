package com.example.moviesbox.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.moviesbox.data.MovieContract.MovieEntry;

public class MoviesProvider extends ContentProvider {
    // region CONSTANTS
    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    // endregion

    // region VARIABLES
    private MoviesDbHelper mDbHelper = null;
    // endregion

    static {
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieEntry.TABLE_NAME, MOVIES);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieEntry.TABLE_NAME+ "/#", MOVIE_ID);
    }

    // region CONTENT PROVIDER METHODS
    @Override
    public boolean onCreate() {
        mDbHelper = new MoviesDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                return MovieEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                builder.setTables(MovieEntry.TABLE_NAME);

                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = MovieEntry.SORT_ORDER_DEFAULT;
                }

                break;
            case MOVIE_ID:
                builder.setTables(MovieEntry.TABLE_NAME);
                builder.appendWhere(MovieEntry._ID + " = " + uri.getLastPathSegment());

                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        checkContentValues(values);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                long id = db.insert(MovieEntry.TABLE_NAME, null, values);

                return getUriForId(id, uri);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        checkContentValues(values);

        if (values != null && values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int updateCount = 0;

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                updateCount = db.update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);

                break;
            case MOVIE_ID:
                String idStr = uri.getLastPathSegment();
                String where = MovieEntry._ID + " = " + idStr;

                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                updateCount = db.update(MovieEntry.TABLE_NAME, values, where, selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (updateCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updateCount;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int delCount = 0;

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                delCount = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);

                break;
            case MOVIE_ID:
                String idStr = uri.getLastPathSegment();
                String where = MovieEntry._ID + " = " + idStr;

                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                delCount = db.delete(MovieEntry.TABLE_NAME, where, selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (delCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return delCount;
    }
    // endregion

    // region PRIVATE METHODS
    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);

            getContext().getContentResolver().notifyChange(itemUri, null);

            return itemUri;
        }

        return null;
    }

    private void checkContentValues(ContentValues values) {
        if (values.containsKey(MovieEntry._ID)) {
            Integer id = values.getAsInteger(MovieEntry._ID);

            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Movie identifier must be provided.");
            }
        }

        if (values.containsKey(MovieEntry.COLUMN_TITLE)) {
            String title = values.getAsString(MovieEntry.COLUMN_TITLE);

            if (TextUtils.isEmpty(title)) {
                throw new IllegalArgumentException("Movie title cannot be empty.");
            }
        }

        if (values.containsKey(MovieEntry.COLUMN_ORIGINAL_TITLE)) {
            String originalTitle = values.getAsString(MovieEntry.COLUMN_ORIGINAL_TITLE);

            if (TextUtils.isEmpty(originalTitle)) {
                throw new IllegalArgumentException("Movie original title cannot be empty.");
            }
        }
    }
    // endregion
}
