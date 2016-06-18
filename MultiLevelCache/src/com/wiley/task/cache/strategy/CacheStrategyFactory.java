package com.wiley.task.cache.strategy;

import java.io.Serializable;

/**
 * Фабрика для создания объектов CacheStrategy
 */
public final class CacheStrategyFactory {

    private static final CacheStrategy NO_OP_STRATEGY = new NoOpCacheStrategy();

    public static <K extends Serializable> CacheStrategy<K> getStrategy(CacheStrategyType type) {
        switch (type) {
            case LRU: return new LRUCacheStrategy<>();
            case LFU: return new LFUCacheStrategy<>();
            case MRU: return new MRUCacheStrategy<>();
        }
        return NO_OP_STRATEGY;
    }
}
