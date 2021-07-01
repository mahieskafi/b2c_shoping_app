package com.srp.eways.repository.local;

import java.util.List;

public interface ICache {

    void put(String key, String value);

    String getString(String key);

    void put(String key, List<String> objects);

    List<String> getStringList(String key);

    void put(String key, int value);

    int getInt(String key);

    void put(String key, boolean value);

    boolean getBoolean(String key);

    void remove(String key);

}
