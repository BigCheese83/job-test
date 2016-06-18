package com.wiley.task.cache;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Реализация многоуровневого кэша.
 * Уровни кэша задаются в конструкторе объекта.
 * При помещении объекта в многоуровневый кэш, он помещается на первый уровень.
 * Если первый уровень заполнен, вытесняемый, согласно стратегии вытеснения,
 * элемент помещается на следующий уровень, и т.д. вниз по иерархии уровней.
 * При запросе объекта из кэша, он последовательно ищется на всех уровнях, начиная с первого.
 * Если элемент найден на любом уровне, кроме первого, то он удаляется с этого уровня
 * и помещается на первый уровень.
 */
public class MultiLevelCache<K extends Serializable, V extends Serializable>
        implements Cache<K, V> {

    private final List<Cache<K, V>> caches;

    public MultiLevelCache(Cache<K, V> ... caches) {
        if (caches.length < 1) {
            throw new IllegalArgumentException("Must be set minimum 1 parameter.");
        }
        this.caches = Arrays.asList(caches);
    }

    @Override
    public CacheEntry<K, V> put(K id, V value) {
        CacheEntry<K, V> expired = caches.get(0).put(id, value);
        for (int i = 1; i < caches.size(); i++) {
            if (expired == null) break;
            expired = caches.get(i).put(expired.getId(), expired.getValue());
        }
        return expired;
    }

    @Override
    public CacheEntry<K, V> get(K id) {
        CacheEntry<K, V> result = caches.get(0).get(id);
        if (result == null) {
            for (int i = 1; i < caches.size(); i++) {
                result = caches.get(i).get(id);
                if (result != null) {
                    caches.get(i).remove(id);
                    put(result.getId(), result.getValue());
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public CacheEntry<K, V> remove(K id) {
        for (Cache<K, V> cache : caches) {
            CacheEntry<K, V> result = cache.remove(id);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        for (Cache<K, V> cache : caches) {
            cache.clear();
        }
    }

    @Override
    public long size() {
        long result = 0;
        for (Cache<K, V> cache : caches) {
            result += cache.size();
        }
        return result;
    }

    @Override
    public boolean isFull() {
        for (Cache<K, V> cache : caches) {
            if (!cache.isFull()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void print() {
        System.out.println("MultiLevel cache.");
        for (int i = 0; i < caches.size(); i++) {
            System.out.print("Level" + (i+1) + " : ");
            caches.get(i).print();
        }
    }
}
