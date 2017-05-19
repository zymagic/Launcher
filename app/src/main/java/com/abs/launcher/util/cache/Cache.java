package com.abs.launcher.util.cache;

import java.util.Map;

/**
 * Created by zy on 17-5-19.
 */

public interface Cache<K, T> {
    T get(K key);
    void put(K key, T value);
    T remove(K key);
    Map<K, T> getAll();
    void clear();
}
