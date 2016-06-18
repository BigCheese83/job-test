package com.wiley.task.cache.strategy;

import java.io.Serializable;

/**
 * Интерфейс стратегии вытеснения кэша
 *
 * @param <K> тип идентификаторов объектов в кэше
 * @see     LRUCacheStrategy
 * @see     MRUCacheStrategy
 * @see     LFUCacheStrategy
 * @see     NoOpCacheStrategy
 */
public interface CacheStrategy<K extends Serializable> {
    /**
     * Инициализирует информацию об использовании элемента с заданным идентификатором
     * @param id уникальный идентификатор объекта
     */
    void init(K id);

    /**
     * Обновляет информацию об использовании элемента с заданным идентификатором
     * @param id уникальный идентификатор объекта
     */
    void recordAccess(K id);

    /**
     * Удаляет информацию об использовании элемента с заданным идентификатором
     * @param id уникальный идентификатор объекта
     */
    void remove(K id);

    /**
     * Сброс информации об использовании элементов
     */
    void reset();

    /**
     * Возвращает идентификатор следующего вытесняемого элемента,
     * согласно имеющемуся алгоритму вытеснения.
     * @return идентификатор вытесняемого элемента
     */
    K getExpiredId();
}
