package com.wiley.task.cache.strategy;

/**
 * Типы стратегий вытеснения кэша
 */
public enum CacheStrategyType {
    NO_OP,  /* Заглушка, пустая стратегия. Элементы не вытесняются при переполнении кэша */
    LRU,    /* LRU Least Recently Used */
    MRU,    /* MRU Most Recently Used */
    LFU     /* LFU Least Frequently Used */
}
