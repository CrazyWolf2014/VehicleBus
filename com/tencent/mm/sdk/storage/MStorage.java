package com.tencent.mm.sdk.storage;

import android.os.Looper;

public abstract class MStorage {
    private final MStorageEvent<IOnStorageChange, String> bV;
    private final MStorageEvent<IOnStorageLoaded, String> bW;

    public interface IOnStorageChange {
        void onNotifyChange(String str);
    }

    public interface IOnStorageLoaded {
        void onNotifyLoaded();
    }

    /* renamed from: com.tencent.mm.sdk.storage.MStorage.1 */
    class C11241 extends MStorageEvent<IOnStorageChange, String> {
        final /* synthetic */ MStorage bX;

        C11241(MStorage mStorage) {
            this.bX = mStorage;
        }

        protected /* synthetic */ void processEvent(Object obj, Object obj2) {
            IOnStorageChange iOnStorageChange = (IOnStorageChange) obj;
            String str = (String) obj2;
            MStorage mStorage = this.bX;
            iOnStorageChange.onNotifyChange(str);
        }
    }

    /* renamed from: com.tencent.mm.sdk.storage.MStorage.2 */
    class C11252 extends MStorageEvent<IOnStorageLoaded, String> {
        final /* synthetic */ MStorage bX;

        C11252(MStorage mStorage) {
            this.bX = mStorage;
        }

        protected /* synthetic */ void processEvent(Object obj, Object obj2) {
            IOnStorageLoaded iOnStorageLoaded = (IOnStorageLoaded) obj;
            MStorage mStorage = this.bX;
            iOnStorageLoaded.onNotifyLoaded();
        }
    }

    public MStorage() {
        this.bV = new C11241(this);
        this.bW = new C11252(this);
    }

    public void add(IOnStorageChange iOnStorageChange) {
        this.bV.add(iOnStorageChange, Looper.getMainLooper());
    }

    public void addLoadedListener(IOnStorageLoaded iOnStorageLoaded) {
        this.bW.add(iOnStorageLoaded, Looper.getMainLooper());
    }

    public void doNotify() {
        this.bV.event("*");
        this.bV.doNotify();
    }

    public void doNotify(String str) {
        this.bV.event(str);
        this.bV.doNotify();
    }

    public void lock() {
        this.bV.lock();
    }

    public void remove(IOnStorageChange iOnStorageChange) {
        this.bV.remove(iOnStorageChange);
    }

    public void removeLoadedListener(IOnStorageLoaded iOnStorageLoaded) {
        this.bW.remove(iOnStorageLoaded);
    }

    public void unlock() {
        this.bV.unlock();
    }
}
