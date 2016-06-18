package com.wiley.task.cache;

import com.wiley.task.cache.strategy.CacheStrategy;
import com.wiley.task.cache.strategy.CacheStrategyFactory;
import com.wiley.task.cache.strategy.CacheStrategyType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Кэш, хранящий объекты в оперативной памяти.
 */
public class MemoryCache<K extends Serializable, V extends Serializable>
        implements Cache<K, V> {

    private final Map<K, V> map = new HashMap<>();
    private final int MAX_SIZE;
    private final CacheStrategy<K> strategy;

    public MemoryCache(int maxSize, CacheStrategyType strategyType) {
        MAX_SIZE = maxSize;
        strategy = CacheStrategyFactory.getStrategy(strategyType);
    }

    @Override
    public CacheEntry<K, V> put(K id, V value) {
        CacheEntry<K, V> expired = null;
        if (isFull()) {
            K expiredId = strategy.getExpiredId();
            if (expiredId != null) {
                expired = remove(expiredId);
            }
        }
        map.put(id, value);
        strategy.init(id);
        return expired;
    }

    @Override
    public CacheEntry<K, V> get(K id) {
        strategy.recordAccess(id);
        return toCacheEntry(id, map.get(id));
    }

    @Override
    public CacheEntry<K, V> remove(K id) {
        V removed = map.remove(id);
        strategy.remove(id);
        return toCacheEntry(id, removed);
    }

    @Override
    public void clear() {
        map.clear();
        strategy.reset();
    }

    @Override
    public long size() {
        return map.size();
    }

    @Override
    public boolean isFull() {
        return map.size() >= MAX_SIZE;
    }

    @Override
    public void print() {
        String str = "Memory cache size=" + map.size();
        str += (map.size() > 100) ? ". Too many elements for print." : (" content=" + map.toString());
        System.out.println(str);
    }

    private CacheEntry<K, V> toCacheEntry(K id, V value) {
        return value != null ? new CacheEntry<>(id, value) : null;
    }
}
