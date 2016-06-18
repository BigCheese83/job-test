package com.wiley.task.cache;

import com.wiley.task.cache.strategy.CacheStrategy;
import com.wiley.task.cache.strategy.CacheStrategyFactory;
import com.wiley.task.cache.strategy.CacheStrategyType;

import java.io.*;

/**
 * Кэш, хранящий объекты в файловой системе.
 * Располагается в директории %TEMP%\fscache.
 * Для сохранения объекта в файл используется стандартный механизм сериализации.
 * Для вычисления имени файла используется хэш-код идентификатора объекта.
 * Первые два символа хэш-кода определяют имя директории, остальные оставшиеся - имя файла.
 */
public class FileSystemCache<K extends Serializable, V extends Serializable>
        implements Cache<K, V> {

    private final int MAX_SIZE;
    private final CacheStrategy<K> strategy;
    private final File root = new File(ROOT_PATH);
    private int cacheSize;
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String ROOT_PATH = System.getProperty("java.io.tmpdir") + "fscache" + SEPARATOR;

    public FileSystemCache(int maxSize, CacheStrategyType strategyType) {
        MAX_SIZE = maxSize;
        strategy = CacheStrategyFactory.getStrategy(strategyType);
        root.mkdirs();
        clear();
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
        File dir = getPath(id);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File cacheFile = new File(dir.getPath() + SEPARATOR + getFileName(id));
        boolean isExists = cacheFile.exists();
        saveValue(cacheFile, value);
        if (!isExists) {
            cacheSize++;
        }
        strategy.init(id);
        return expired;
    }

    @Override
    public CacheEntry<K, V> get(K id) {
        File dir = getPath(id);
        if (dir.exists()) {
            File cacheFile = new File(dir.getPath() + SEPARATOR + getFileName(id));
            strategy.recordAccess(id);
            return toCacheEntry(id, readValue(cacheFile));
        }
        return null;
    }

    @Override
    public CacheEntry<K, V> remove(K id) {
        File dir = getPath(id);
        if (dir.exists()) {
            File cacheFile = new File(dir.getPath() + SEPARATOR + getFileName(id));
            V value = readValue(cacheFile);
            cacheFile.delete();
            cacheSize--;
            strategy.remove(id);
            return toCacheEntry(id, value);
        }
        return null;
    }

    @Override
    public void clear() {
        deleteAllFiles(root);
        strategy.reset();
        cacheSize = 0;
    }

    @Override
    public long size() {
        return cacheSize;
    }

    @Override
    public boolean isFull() {
        return cacheSize >= MAX_SIZE;
    }

    @Override
    public void print() {
        System.out.println("FileSystem cache size=" + cacheSize);
    }

    private void saveValue(File file, V value) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            oos.writeObject(value);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("I/O Error create file " + file.getPath());
        }
    }

    private V readValue(File file) {
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                return (V)ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("I/O Error read file " + file.getPath());
            }
        }
        return null;
    }

    private File getPath(K id) {
        String hash = String.valueOf(id.hashCode());
        String dir = (hash.length() < 3) ? "00" : hash.substring(0, 2);
        return new File(ROOT_PATH + dir + SEPARATOR);
    }

    private String getFileName(K id) {
        String hash = String.valueOf(id.hashCode());
        return (hash.length() < 3) ? hash : hash.substring(2);
    }

    private int getFilesCount(File file) {
        if (!file.isDirectory()) return 0;
        int count = 0;
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    count += getFilesCount(f);
                } else {
                    count++;
                }
            }
        }
        return count;
    }

    private void deleteAllFiles(File file) {
        if (!file.exists()) return;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteAllFiles(f);
                }
            }
        }
        file.delete();
    }

    private CacheEntry<K, V> toCacheEntry(K id, V value) {
        return value != null ? new CacheEntry<>(id, value) : null;
    }
}
