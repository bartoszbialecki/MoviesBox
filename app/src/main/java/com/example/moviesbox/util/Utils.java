package com.example.moviesbox.util;

import android.content.Context;
import android.content.res.Configuration;

public class Utils {
    public static boolean isLandscapeOrientation(Context context) {
        return context != null && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
