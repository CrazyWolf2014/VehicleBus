package com.thoughtworks.xstream.core.util;

public class Pool {
    private final Factory factory;
    private final int initialPoolSize;
    private final int maxPoolSize;
    private transient Object mutex;
    private transient int nextAvailable;
    private transient Object[] pool;

    public interface Factory {
        Object newInstance();
    }

    public Pool(int initialPoolSize, int maxPoolSize, Factory factory) {
        this.mutex = new Object();
        this.initialPoolSize = initialPoolSize;
        this.maxPoolSize = maxPoolSize;
        this.factory = factory;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object fetchFromPool() {
        /*
        r6 = this;
        r3 = r6.mutex;
        monitor-enter(r3);
        r2 = r6.pool;	 Catch:{ all -> 0x001f }
        if (r2 != 0) goto L_0x0022;
    L_0x0007:
        r2 = r6.maxPoolSize;	 Catch:{ all -> 0x001f }
        r2 = new java.lang.Object[r2];	 Catch:{ all -> 0x001f }
        r6.pool = r2;	 Catch:{ all -> 0x001f }
        r2 = r6.initialPoolSize;	 Catch:{ all -> 0x001f }
        r6.nextAvailable = r2;	 Catch:{ all -> 0x001f }
    L_0x0011:
        r2 = r6.nextAvailable;	 Catch:{ all -> 0x001f }
        if (r2 <= 0) goto L_0x0022;
    L_0x0015:
        r2 = r6.factory;	 Catch:{ all -> 0x001f }
        r2 = r2.newInstance();	 Catch:{ all -> 0x001f }
        r6.putInPool(r2);	 Catch:{ all -> 0x001f }
        goto L_0x0011;
    L_0x001f:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x001f }
        throw r2;
    L_0x0022:
        r2 = r6.nextAvailable;	 Catch:{ all -> 0x001f }
        r4 = r6.maxPoolSize;	 Catch:{ all -> 0x001f }
        if (r2 != r4) goto L_0x004c;
    L_0x0028:
        r2 = r6.mutex;	 Catch:{ InterruptedException -> 0x002e }
        r2.wait();	 Catch:{ InterruptedException -> 0x002e }
        goto L_0x0022;
    L_0x002e:
        r0 = move-exception;
        r2 = new java.lang.RuntimeException;	 Catch:{ all -> 0x001f }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x001f }
        r4.<init>();	 Catch:{ all -> 0x001f }
        r5 = "Interrupted whilst waiting for a free item in the pool : ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x001f }
        r5 = r0.getMessage();	 Catch:{ all -> 0x001f }
        r4 = r4.append(r5);	 Catch:{ all -> 0x001f }
        r4 = r4.toString();	 Catch:{ all -> 0x001f }
        r2.<init>(r4);	 Catch:{ all -> 0x001f }
        throw r2;	 Catch:{ all -> 0x001f }
    L_0x004c:
        r2 = r6.pool;	 Catch:{ all -> 0x001f }
        r4 = r6.nextAvailable;	 Catch:{ all -> 0x001f }
        r5 = r4 + 1;
        r6.nextAvailable = r5;	 Catch:{ all -> 0x001f }
        r1 = r2[r4];	 Catch:{ all -> 0x001f }
        if (r1 != 0) goto L_0x0067;
    L_0x0058:
        r2 = r6.factory;	 Catch:{ all -> 0x001f }
        r1 = r2.newInstance();	 Catch:{ all -> 0x001f }
        r6.putInPool(r1);	 Catch:{ all -> 0x001f }
        r2 = r6.nextAvailable;	 Catch:{ all -> 0x001f }
        r2 = r2 + 1;
        r6.nextAvailable = r2;	 Catch:{ all -> 0x001f }
    L_0x0067:
        monitor-exit(r3);	 Catch:{ all -> 0x001f }
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.thoughtworks.xstream.core.util.Pool.fetchFromPool():java.lang.Object");
    }

    protected void putInPool(Object object) {
        synchronized (this.mutex) {
            Object[] objArr = this.pool;
            int i = this.nextAvailable - 1;
            this.nextAvailable = i;
            objArr[i] = object;
            this.mutex.notify();
        }
    }

    private Object readResolve() {
        this.mutex = new Object();
        return this;
    }
}
