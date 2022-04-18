package com.example.jetpack.util;


import android.content.Context;

public class SharedPrefUtils {

    private static final String PREF_APP = "play_trade";

    private SharedPrefUtils() {
        throw new UnsupportedOperationException("");
    }


    static public int getIntData(Context context, String key) {
        return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getInt(key, 0);
    }


    static public void saveData(Context context, String key, int val) {
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putInt(key, val).apply();
    }


}

