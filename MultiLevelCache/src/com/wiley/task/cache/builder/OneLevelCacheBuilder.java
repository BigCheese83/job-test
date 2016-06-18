package com.wiley.task.cache.builder;

import com.wiley.task.cache.Cache;
import com.wiley.task.cache.CacheType;
import com.wiley.task.cache.FileSystemCache;
import com.wiley.task.cache.MemoryCache;
import com.wiley.task.cache.strategy.CacheStrategyType;

import java.io.Serializable;

/**
 * Created by Alexei on 26.03.2016.
 */
public final class OneLevelCacheBuilder<K extends Serializable, V extends Serializable> {

    private int maxSize = Integer.MAX_VALUE;
    private CacheStrategyType strategyType = CacheStrategyType.NO_OP;
    private CacheType cacheType;

    private OneLevelCacheBuilder() {}

    public static <K extends Serializable, V extends Serializable> OneLevelCacheBuilder<K, V> getCache(CacheType cacheType, Class<K> idClass, Class<V> valueClass) {
        if (cacheType == null) {
            throw new IllegalArgumentException("Not found cache.");
        }
        OneLevelCacheBuilder<K, V> builder = new OneLevelCacheBuilder<>();
        builder.cacheType = cacheType;
        return builder;
    }

    public OneLevelCacheBuilder<K, V> setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public OneLevelCacheBuilder<K, V> setStrategy(CacheStrategyType strategyType) {
        this.strategyType = strategyType;
        return this;
    }

    public Cache<K, V> build() {
        switch (cacheType) {
            case MEMORY:
                return new MemoryCache<>(maxSize, strategyType);
            case FILESYSTEM:
                return new FileSystemCache<>(maxSize, strategyType);
        }
        throw new IllegalArgumentException("Wrong cache type " + cacheType);
    }
}
