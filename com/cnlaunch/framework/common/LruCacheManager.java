package com.cnlaunch.framework.common;

import android.util.LruCache;

public class LruCacheManager {
    private static int CACHE_SIZE;
    private static LruCacheManager instance;
    private LruCache<String, Object> lruCache;

    static {
        CACHE_SIZE = 30;
    }

    private LruCacheManager(int cacheSize) {
        this.lruCache = new LruCache(cacheSize);
    }

    public static LruCacheManager getInstance() {
        return getInstance(CACHE_SIZE);
    }

    public static LruCacheManager getInstance(int cacheSize) {
        if (instance == null) {
            synchronized (LruCacheManager.class) {
                if (instance == null) {
                    instance = new LruCacheManager(cacheSize);
                }
            }
        }
        return instance;
    }

    public void put(String key, Object value) {
        this.lruCache.put(key, value);
    }

    public Object get(String key) {
        return this.lruCache.get(key);
    }

    public Object remove(String key) {
        return this.lruCache.remove(key);
    }

    public void evictAll() {
        this.lruCache.evictAll();
    }

    public int maxSize() {
        return this.lruCache.maxSize();
    }

    public int size() {
        return this.lruCache.size();
    }

    public void trimToSize(int maxSize) {
        this.lruCache.trimToSize(maxSize);
    }
}
