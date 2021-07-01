package com.srp.eways.repository.local;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PreferenceCache implements ICache {

    private static final String ITEM_SEPARATOR = "itemSeparator";

    private final HashMap<String, String> mPreferences;

    private static PreferenceCache sInstance;

    public static PreferenceCache getInstance() {
        if (sInstance == null) {
            sInstance = new PreferenceCache();
        }

        return sInstance;
    }

    public PreferenceCache() {
        mPreferences = new HashMap<>();
    }

    @Override
    public void put(String key, String value) {
        mPreferences.put(key, value);
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

        mPreferences.put(key, stringBuilder.toString());
    }

    @Override
    public String getString(String key) {
        return mPreferences.get(key);
    }

    @Override
    public List<String> getStringList(String key) {
        String savedStringList = mPreferences.get(key);

        if (savedStringList == null) {
            return null;
        }

        return Arrays.asList(savedStringList.split(ITEM_SEPARATOR));
    }

//    @Override
//    public void put(String key, int value) {
//        mPreferences.edit().putInt(key, value).apply();
//    }
//
//    @Override
//    public int getInt(String key) {
//        return mPreferences.getInt(key, 0);
//    }
//
//    @Override
//    public void put(String key, boolean value) {
//        mPreferences.edit().putBoolean(key, value).apply();
//    }
//
//    @Override
//    public boolean getBoolean(String key) {
//        return mPreferences.getBoolean(key, false);
//    }

    @Override
    public void remove(String key) {
        mPreferences.remove(key);
    }

}
