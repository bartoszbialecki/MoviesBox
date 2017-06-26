package com.example.moviesbox.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.moviesbox.R;

public class SharedPreferencesRepositoryImpl implements SharedPreferencesRepository {
    // region VARIABLES
    private Context mContext;
    private SharedPreferences mPreferences;
    // endregion

    // region CONSTRUCTORS
    public SharedPreferencesRepositoryImpl(Context context) {
        mContext = context;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    // endregion

    // region SHARED PREFERENCES REPOSITORY METHODS
    @Override
    public String sortOrder() {
        return mPreferences.getString(mContext.getString(R.string.pref_sort_orders_key),
                mContext.getString(R.string.pref_sort_orders_popular));
    }
    // endregion
}
