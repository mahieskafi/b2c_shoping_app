package com.srp.eways.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.srp.eways.di.DIMain;

import java.util.Arrays;
import java.util.List;


public class PreferenceCache implements ICache {

    private static final String ITEM_SEPARATOR = "itemSeparator";

    private final SharedPreferences mPreferences;

    private static PreferenceCache sInstance;

    public static PreferenceCache getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceCache(context);
        }

        return sInstance;
    }

    protected PreferenceCache(Context context) {
        mPreferences = context.getSharedPreferences(DIMain.getPreferencesProvider().getPreferencesFileName(), Context.MODE_PRIVATE);
    }

    @Override
    public void put(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    @Override
    public void put(String key, List<String> objects) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < objects.size(); ++i) {
            stringBuilder.append(objects.get(i));

            if (i != objects.size() - 1) {
                stringBuilder.append(ITEM_SEPARATOR);
            }
        }

        mPreferences.edit().putString(key, stringBuilder.toString()).apply();
    }

    @Override
    public String getString(String key) {
        return mPreferences.getString(key, null);
    }

    @Override
    public List<String> getStringList(String key) {
        String savedStringList = mPreferences.getString(key, null);

        if (savedStringList == null) {
            return null;
        }

        return Arrays.asList(savedStringList.split(ITEM_SEPARATOR));
    }

    @Override
    public void put(String key, int value) {
        mPreferences.edit().putInt(key, value).apply();
    }

    @Override
    public int getInt(String key) {
        return mPreferences.getInt(key, 0);
    }

    @Override
    public void put(String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).apply();
    }

    @Override
    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }
    
    @Override
    public void remove(String key) {
        mPreferences.edit().remove(key).commit();
    }

}
