package com.wiley.task.cache.strategy;

import java.io.Serializable;

/**
 * Пустая стратегия вытеснения.
 * Используется по умолчанию, если не задана никакая другая стратегия.
 * Элементы не вытесняются при переполнении кэша.
 */
class NoOpCacheStrategy <K extends Serializable>
        implements CacheStrategy<K> {

    @Override
    public void init(K id) {}

    @Override
    public void recordAccess(K id) {}

    @Override
    public void remove(K id) {}

    @Override
    public void reset() {}

    @Override
    public K getExpiredId() {
        return null;
    }
}
