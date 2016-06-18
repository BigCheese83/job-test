package com.wiley.task.cache.builder;

import com.wiley.task.cache.Cache;
import com.wiley.task.cache.CacheType;
import com.wiley.task.cache.MultiLevelCache;
import com.wiley.task.cache.strategy.CacheStrategyType;

import java.io.Serializable;

/**
 * Created by Alexei on 27.03.2016.
 */
public final class MultiLevelCacheBuilder<K extends Serializable, V extends Serializable> {

    private static final int MAX_LEVEL_SIZE = 4;
    private int[] maxSizes;
    private CacheStrategyType[] strategyTypes;
    private CacheType cacheType;
    private Class<K> idClass;
    private Class<V> valueClass;

    private MultiLevelCacheBuilder() {
        maxSizes = new int[MAX_LEVEL_SIZE];
        strategyTypes = new CacheStrategyType[MAX_LEVEL_SIZE];
        for (int i = 0; i < MAX_LEVEL_SIZE; i++) {
            maxSizes[i] = Integer.MAX_VALUE;
            strategyTypes[i] = CacheStrategyType.NO_OP;
        }
    }

    public static <K extends Serializable, V extends Serializable> MultiLevelCacheBuilder<K, V> getCache(CacheType cacheType, Class<K> idClass, Class<V> valueClass) {
        if (cacheType == null) {
            throw new IllegalArgumentException("Not found cache.");
        }
        MultiLevelCacheBuilder<K, V> builder = new MultiLevelCacheBuilder<>();
        builder.cacheType = cacheType;
        builder.idClass = idClass;
        builder.valueClass = valueClass;
        return builder;
    }

    public MultiLevelCacheBuilder<K, V> setLevel1MaxSize(int maxSize) {
        maxSizes[0] = maxSize;
        return this;
    }

    public MultiLevelCacheBuilder<K, V> setLevel2MaxSize(int maxSize) {
        maxSizes[1] = maxSize;
        return this;
    }

    public MultiLevelCacheBuilder<K, V> setLevel3MaxSize(int maxSize) {
        maxSizes[2] = maxSize;
        return this;
    }

    public MultiLevelCacheBuilder<K, V> setLevel4MaxSize(int maxSize) {
        maxSizes[3] = maxSize;
        return this;
    }

    public MultiLevelCacheBuilder<K, V> setLevel1Strategy(CacheStrategyType strategyType) {
        strategyTypes[0] = strategyType;
        return this;
    }

    public MultiLevelCacheBuilder<K, V> setLevel2Strategy(CacheStrategyType strategyType) {
        strategyTypes[1] = strategyType;
        return this;
    }

    public MultiLevelCacheBuilder<K, V> setLevel3Strategy(CacheStrategyType strategyType) {
        strategyTypes[2] = strategyType;
        return this;
    }

    public MultiLevelCacheBuilder<K, V> setLevel4Strategy(CacheStrategyType strategyType) {
        strategyTypes[3] = strategyType;
        return this;
    }

    public Cache<K, V> build() {
        switch (cacheType) {
            case MEMORY_FILESYSTEM:
                Cache<K, V> memoryCache =
                        OneLevelCacheBuilder.getCache(CacheType.MEMORY, idClass, valueClass)
                                .setMaxSize(maxSizes[0])
                                .setStrategy(strategyTypes[0])
                                .build();
                Cache<K, V> fileCache =
                        OneLevelCacheBuilder.getCache(CacheType.FILESYSTEM, idClass, valueClass)
                                .setMaxSize(maxSizes[1])
                                .setStrategy(strategyTypes[1])
                                .build();
                return new MultiLevelCache<>(memoryCache, fileCache);
        }
        throw new IllegalArgumentException("Wrong cache type " + cacheType);
    }
}
