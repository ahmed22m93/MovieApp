package com.example.my.moviesapp.Utilities;


import android.content.Context;
import android.content.SharedPreferences;

public class WorkWithSharedPreference {

    private static final String SHARED_PREFERENCE_NAME = "sortMovies";
    private static final String SHARED_PREFERENCE_SORT_BY = "sortBy";
    private static final String SHARED_PREFERENCE_DEFAULT = "popular";

    public static void setDataToShared( Context context , String data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCE_SORT_BY, data);
        editor.commit();
    }

    public static String getDataFromShared(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, context.MODE_PRIVATE);
        String sortBy = sharedPreferences.getString(SHARED_PREFERENCE_SORT_BY, SHARED_PREFERENCE_DEFAULT);
        return sortBy;
    }
}
