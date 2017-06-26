package com.example.moviesbox.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Settings {
    // region CONSTANTS
    private static final ArrayList<String> SUPPORTED_LANGUAGES = new ArrayList<>(Arrays.asList("en", "de", "pl"));
    private static Settings sInstance = new Settings();
    // endregion

    // region VARIABLES
    private String mCurrentLanguage = "en";
    // endregion

    // region CONSTRUCTOR
    private Settings() {
        setCurrentAppLanguage();
    }
    // endregion

    // region PUBLIC METHODS
    public static Settings getInstance() {
        return sInstance;
    }

    public String getCurrentLanguage() {
        return mCurrentLanguage;
    }

    public void reloadDataWhenConfigurationChanged() {
        setCurrentAppLanguage();
    }
    // endregion

    // region PRIVATE METHODS
    private void setCurrentAppLanguage() {
        String currentLanguage = Locale.getDefault().getLanguage().toLowerCase();

        if (SUPPORTED_LANGUAGES.contains(currentLanguage)) {
            mCurrentLanguage = currentLanguage;
        }
    }
    // endregion
}
