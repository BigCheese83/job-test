package com.wiley.task.cache;

import com.wiley.task.cache.builder.MultiLevelCacheBuilder;
import com.wiley.task.cache.strategy.CacheStrategyType;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("======== Test Two-Level Cache ============");
        /* Конструирование экземпляра двухуровневого кэша.
         * Первый уровень - memory, размер 5, стратегия LRU
         * Второй уровень - file system, размер 10, стратегия LRU */
        Cache<String, String> cache =
                MultiLevelCacheBuilder.getCache(CacheType.MEMORY_FILESYSTEM, String.class, String.class)
                        .setLevel1MaxSize(5)
                        .setLevel1Strategy(CacheStrategyType.LRU)
                        .setLevel2MaxSize(10)
                        .setLevel2Strategy(CacheStrategyType.LRU)
                        .build();
        // Помещение объектов в кэш
        cache.put("1", "one");
        Thread.sleep(1);
        cache.put("2", "two");
        Thread.sleep(1);
        cache.put("3", "three");
        Thread.sleep(1);
        cache.put("4", "four");
        Thread.sleep(1);
        cache.put("5", "five");
        Thread.sleep(1);
        // Вывод содержимого кэша
        cache.print();
        // Запрос некоторых элементов кэша
        System.out.println(cache.get("1"));
        Thread.sleep(1);
        System.out.println(cache.get("3"));
        Thread.sleep(1);
        System.out.println(cache.get("3"));
        Thread.sleep(1);
        System.out.println(cache.get("2"));
        Thread.sleep(1);
        System.out.println(cache.get("5"));
        /* Первый уровень заполен, неиспользованный дольше всех элемент
         * вытесняется на второй уровень */
        cache.put("6", "six");
        Thread.sleep(1);
        cache.put("7", "seven");
        cache.print();
        System.out.println(cache.get("3"));
        // Запрашивается элемент из второго уровня кэша, он помещается на первый уровень
        System.out.println(cache.get("1"));
        cache.print();
    }
}
