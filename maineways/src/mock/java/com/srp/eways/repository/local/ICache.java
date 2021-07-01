package com.srp.eways.repository.local;

import java.util.List;

public interface ICache {

    void put(String key, String value);

    String getString(String key);

    void put(String key, List<String> objects);

    List<String> getStringList(String key);

    void remove(String key);

}
