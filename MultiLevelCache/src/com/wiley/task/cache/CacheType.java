package com.wiley.task.cache;

/**
 * Типы кэша
 */
public enum CacheType {
    MEMORY, /* Кэш, хранящий объекты в оперативной памяти */
    FILESYSTEM, /* Кэш, хранящий объекты в файловой системе */
    MEMORY_FILESYSTEM /* Двухуровневый кэш, первый уровень MEMORY, второй уровень FILESYSTEM */
}
