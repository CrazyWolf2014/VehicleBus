package com.tencent.mm.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LRUMap<K, O> {
    private Map<K, TimeVal<O>> f1612c;
    private int f1613d;
    private int f1614e;
    private PreRemoveCallback<K, O> f1615f;

    /* renamed from: com.tencent.mm.algorithm.LRUMap.1 */
    class C08611 implements Comparator<Entry<K, TimeVal<O>>> {
        final /* synthetic */ LRUMap f1609g;

        C08611(LRUMap lRUMap) {
            this.f1609g = lRUMap;
        }

        public int compare(Entry<K, TimeVal<O>> entry, Entry<K, TimeVal<O>> entry2) {
            return ((TimeVal) entry.getValue()).f1611t.compareTo(((TimeVal) entry2.getValue()).f1611t);
        }
    }

    public interface OnClearListener<K, O> {
        void onClear(K k, O o);
    }

    public interface PreRemoveCallback<K, O> {
        void preRemoveCallback(K k, O o);
    }

    public class TimeVal<OO> {
        final /* synthetic */ LRUMap f1610g;
        public OO obj;
        public Long f1611t;

        public TimeVal(LRUMap lRUMap, OO oo) {
            this.f1610g = lRUMap;
            this.obj = oo;
            UpTime();
        }

        public void UpTime() {
            this.f1611t = Long.valueOf(System.currentTimeMillis());
        }
    }

    public LRUMap(int i) {
        this(i, null);
    }

    public LRUMap(int i, PreRemoveCallback<K, O> preRemoveCallback) {
        this.f1612c = null;
        this.f1615f = null;
        this.f1613d = i;
        this.f1614e = 0;
        this.f1615f = preRemoveCallback;
        this.f1612c = new HashMap();
    }

    public boolean check(K k) {
        return this.f1612c.containsKey(k);
    }

    public boolean checkAndUpTime(K k) {
        if (!this.f1612c.containsKey(k)) {
            return false;
        }
        ((TimeVal) this.f1612c.get(k)).UpTime();
        return true;
    }

    public void clear() {
        this.f1612c.clear();
    }

    public void clear(OnClearListener<K, O> onClearListener) {
        if (this.f1612c != null) {
            if (onClearListener != null) {
                for (Entry entry : this.f1612c.entrySet()) {
                    onClearListener.onClear(entry.getKey(), ((TimeVal) entry.getValue()).obj);
                }
            }
            this.f1612c.clear();
        }
    }

    public O get(K k) {
        return getAndUptime(k);
    }

    public O getAndUptime(K k) {
        TimeVal timeVal = (TimeVal) this.f1612c.get(k);
        if (timeVal == null) {
            return null;
        }
        timeVal.UpTime();
        return timeVal.obj;
    }

    public void remove(K k) {
        if (this.f1612c.containsKey(k)) {
            if (this.f1615f != null) {
                this.f1615f.preRemoveCallback(k, ((TimeVal) this.f1612c.get(k)).obj);
            }
            this.f1612c.remove(k);
        }
    }

    public void setMaxSize(int i) {
        if (i > 0) {
            this.f1613d = i;
        }
    }

    public void setPerDeleteSize(int i) {
        if (i > 0) {
            this.f1614e = i;
        }
    }

    public int size() {
        return this.f1612c.size();
    }

    public void update(K k, O o) {
        if (((TimeVal) this.f1612c.get(k)) == null) {
            this.f1612c.put(k, new TimeVal(this, o));
            if (this.f1612c.size() > this.f1613d) {
                int i;
                Object arrayList = new ArrayList(this.f1612c.entrySet());
                Collections.sort(arrayList, new C08611(this));
                if (this.f1614e <= 0) {
                    i = this.f1613d / 10;
                    if (i <= 0) {
                        i = 1;
                    }
                } else {
                    i = this.f1614e;
                }
                Iterator it = arrayList.iterator();
                int i2 = i;
                while (it.hasNext()) {
                    remove(((Entry) it.next()).getKey());
                    i = i2 - 1;
                    if (i > 0) {
                        i2 = i;
                    } else {
                        return;
                    }
                }
                return;
            }
            return;
        }
        ((TimeVal) this.f1612c.get(k)).UpTime();
        ((TimeVal) this.f1612c.get(k)).obj = o;
    }
}
