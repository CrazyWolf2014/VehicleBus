package com.tencent.mm.sdk.storage;

import android.os.Handler;
import android.os.Looper;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class MStorageEvent<T, E> {
    private int bY;
    private final Hashtable<T, Object> bZ;
    private final CopyOnWriteArraySet<E> ca;

    /* renamed from: com.tencent.mm.sdk.storage.MStorageEvent.1 */
    class C08781 implements Runnable {
        final /* synthetic */ Object cb;
        final /* synthetic */ Object cc;
        final /* synthetic */ MStorageEvent cd;

        C08781(MStorageEvent mStorageEvent, Object obj, Object obj2) {
            this.cd = mStorageEvent;
            this.cb = obj;
            this.cc = obj2;
        }

        public void run() {
            this.cd.processEvent(this.cb, this.cc);
        }
    }

    public MStorageEvent() {
        this.bY = 0;
        this.bZ = new Hashtable();
        this.ca = new CopyOnWriteArraySet();
    }

    private synchronized Vector<T> m1699e() {
        Vector<T> vector;
        vector = new Vector();
        vector.addAll(this.bZ.keySet());
        return vector;
    }

    private void m1700f() {
        Vector e = m1699e();
        if (e != null && e.size() > 0) {
            Map hashMap = new HashMap();
            Iterator it = e.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                Object obj = this.bZ.get(next);
                Iterator it2 = this.ca.iterator();
                while (it2.hasNext()) {
                    Object next2 = it2.next();
                    if (!(next2 == null || obj == null)) {
                        if (obj instanceof Looper) {
                            Looper looper = (Looper) obj;
                            Handler handler = (Handler) hashMap.get(looper);
                            if (handler == null) {
                                handler = new Handler(looper);
                                hashMap.put(looper, handler);
                            }
                            handler.post(new C08781(this, next, next2));
                        } else {
                            processEvent(next, next2);
                        }
                    }
                }
            }
            this.ca.clear();
        }
    }

    public synchronized void add(T t, Looper looper) {
        if (!this.bZ.containsKey(t)) {
            if (looper != null) {
                this.bZ.put(t, looper);
            } else {
                this.bZ.put(t, new Object());
            }
        }
    }

    public void doNotify() {
        if (!isLocked()) {
            m1700f();
        }
    }

    public boolean event(E e) {
        return this.ca.add(e);
    }

    public boolean isLocked() {
        return this.bY > 0;
    }

    public void lock() {
        this.bY++;
    }

    protected abstract void processEvent(T t, E e);

    public synchronized void remove(T t) {
        this.bZ.remove(t);
    }

    public synchronized void removeAll() {
        this.bZ.clear();
    }

    public void unlock() {
        this.bY--;
        if (this.bY <= 0) {
            this.bY = 0;
            m1700f();
        }
    }
}
