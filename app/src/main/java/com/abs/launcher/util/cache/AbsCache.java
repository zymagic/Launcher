package com.abs.launcher.util.cache;

/**
 * Created by zy on 17-5-19.
 */

public abstract class AbsCache<K, T> implements Cache<K, T> {

    private KeyTransformer<K> mKeyTransformer;

    public AbsCache setKeyTransformer(KeyTransformer<K> transformer) {
        mKeyTransformer = transformer;
        return this;
    }

    private K transformKey(K key) {
        return mKeyTransformer == null ? key : mKeyTransformer.transform(key);
    }

    @Override
    public void put(K key, T object) {
        onPut(transformKey(key), object);
    }

    @Override
    public T get(K key) {
        return onGet(transformKey(key));
    }

    @Override
    public T remove(K key) {
        return onRemove(transformKey(key));
    }

    protected abstract void onPut(K key, T object);
    protected abstract T onGet(K key);
    protected abstract T onRemove(K key);

    public interface KeyTransformer<K> {
        K transform(K key);
    }
}
