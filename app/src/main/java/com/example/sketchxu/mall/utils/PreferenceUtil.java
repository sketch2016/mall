package com.example.sketchxu.mall.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.common.references.SharedReference;

public class PreferenceUtil {

    private static PreferenceUtil mInstance;

    public static final String PREFERENCE_NAME = "pref_mall";

    private SharedPreferences sp;

    private SharedPreferences.Editor editot;

    public static synchronized PreferenceUtil getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new PreferenceUtil(ctx);
        }

        return mInstance;
    }

    private PreferenceUtil(Context ctx) {
        sp = ctx.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editot = sp.edit();
    }

    public void putString(String key, String value) {
        editot.putString(key, value).commit();
    }

    public String getString(String key) {
        return sp.getString(key, "");
    }

    public void putInt(String key, int value) {
        editot.putInt(key, value).commit();
    }

    public int getInt(String key) {
        return sp.getInt(key, 0);
    }

    public void putBoolean(String key, boolean value) {
        editot.putBoolean(key, value).commit();
    }

    public Boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }
}
