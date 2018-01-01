package com.abs.launcher.util.cache

/**
 * Created by zy on 17-12-8.
 */
abstract class AbsCache<K, V>: Cache<K, V> {
    var transformer: KeyTransformer<K>? = null

    private fun K.transform() = transformer?.transform(this) ?: this

    override fun put(key: K, value: V) {
        onPut(key.transform(), value)
    }

    override fun get(key: K): V? {
        return onGet(key.transform())
    }

    override fun remove(key: K): V? {
        return onRemove(key.transform())
    }

    abstract fun onPut(key: K, value: V)
    abstract fun onGet(key: K): V?
    abstract fun onRemove(key: K): V?
}

interface KeyTransformer<T> {
    fun transform(key: T): T
}