package de.greenrobot.dao.identityscope;

import de.greenrobot.dao.internal.LongHashMap;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.ReentrantLock;

public class IdentityScopeLong<T> implements IdentityScope<Long, T> {
    private final ReentrantLock lock;
    private final LongHashMap<Reference<T>> map;

    public IdentityScopeLong() {
        this.map = new LongHashMap();
        this.lock = new ReentrantLock();
    }

    public T get(Long key) {
        return get2(key.longValue());
    }

    public T getNoLock(Long key) {
        return get2NoLock(key.longValue());
    }

    public T get2(long key) {
        this.lock.lock();
        try {
            Reference<T> ref = (Reference) this.map.get(key);
            if (ref != null) {
                return ref.get();
            }
            return null;
        } finally {
            this.lock.unlock();
        }
    }

    public T get2NoLock(long key) {
        Reference<T> ref = (Reference) this.map.get(key);
        if (ref != null) {
            return ref.get();
        }
        return null;
    }

    public void put(Long key, T entity) {
        put2(key.longValue(), entity);
    }

    public void putNoLock(Long key, T entity) {
        put2NoLock(key.longValue(), entity);
    }

    public void put2(long key, T entity) {
        this.lock.lock();
        try {
            this.map.put(key, new WeakReference(entity));
        } finally {
            this.lock.unlock();
        }
    }

    public void put2NoLock(long key, T entity) {
        this.map.put(key, new WeakReference(entity));
    }

    public boolean detach(Long key, T entity) {
        this.lock.lock();
        try {
            if (get(key) != entity || entity == null) {
                this.lock.unlock();
                return false;
            }
            remove(key);
            return true;
        } finally {
            this.lock.unlock();
        }
    }

    public void remove(Long key) {
        this.lock.lock();
        try {
            this.map.remove(key.longValue());
        } finally {
            this.lock.unlock();
        }
    }

    public void remove(Iterable<Long> keys) {
        this.lock.lock();
        try {
            for (Long key : keys) {
                this.map.remove(key.longValue());
            }
        } finally {
            this.lock.unlock();
        }
    }

    public void clear() {
        this.lock.lock();
        try {
            this.map.clear();
        } finally {
            this.lock.unlock();
        }
    }

    public void lock() {
        this.lock.lock();
    }

    public void unlock() {
        this.lock.unlock();
    }

    public void reserveRoom(int count) {
        this.map.reserveRoom(count);
    }
}
