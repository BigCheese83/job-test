package com.wiley.task.cache.strategy;

import java.io.Serializable;
import java.util.*;

/**
 * LFU Least Frequently Used.
 * Те элементы, обращения к которым происходят реже всего, вытесняются в первую очередь.
 */
class LFUCacheStrategy <K extends Serializable>
        implements CacheStrategy<K> {

    private final Map<K, Long> map = new HashMap<>();

    @Override
    public void init(K id) {
        if (!map.containsKey(id)) {
            map.put(id, (long) 0);
        }
    }

    @Override
    public void recordAccess(K id) {
        if (map.containsKey(id)) {
            Long count = map.remove(id);
            map.put(id, ++count);
        }
    }

    @Override
    public void remove(K id) {
        map.remove(id);
    }

    @Override
    public void reset() {
        map.clear();
    }

    @Override
    public K getExpiredId() {
        List<Map.Entry<K, Long>> list = new ArrayList<>(map.entrySet());
        if (list.isEmpty()) return null;
        Collections.sort(list, new Comparator<Map.Entry<K, Long>>() {
            @Override
            public int compare(Map.Entry<K, Long> o1, Map.Entry<K, Long> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        return list.get(0).getKey();
    }
}
