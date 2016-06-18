package com.wiley.task.cache;

import java.io.Serializable;

/**
 * Элемент кэша, содержащий объект и уникальный идентификатор
 */
public class CacheEntry<K extends Serializable, V extends Serializable> {
    private final K id;
    private final V value;

    public CacheEntry(K id, V value) {
        this.id = id;
        this.value = value;
    }

    public K getId() {
        return id;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CacheEntry{id=" + id + ", value=" + value + "}";
    }
}
