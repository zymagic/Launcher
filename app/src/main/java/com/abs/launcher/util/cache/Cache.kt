package com.abs.launcher.util.cache

/**
 * Created by zy on 17-12-8.
 */
interface Cache<K, V> {
    fun put(key: K, value: V)
    fun get(key: K): V?
    fun remove(key: K): V?
    fun all(): Map<K, V> {
        return HashMap()
    }
}
