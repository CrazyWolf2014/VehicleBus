package org.jivesoftware.smack.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jivesoftware.smack.util.collections.AbstractMapEntry;
import org.xbill.DNS.WKSRecord.Service;

public class Cache<K, V> implements Map<K, V> {
    protected LinkedList ageList;
    protected long cacheHits;
    protected long cacheMisses;
    protected LinkedList lastAccessedList;
    protected Map<K, CacheObject<V>> map;
    protected int maxCacheSize;
    protected long maxLifetime;

    /* renamed from: org.jivesoftware.smack.util.Cache.1 */
    class C09621 extends AbstractCollection<V> {
        Collection<CacheObject<V>> values;

        /* renamed from: org.jivesoftware.smack.util.Cache.1.1 */
        class C09611 implements Iterator<V> {
            Iterator<CacheObject<V>> it;

            C09611() {
                this.it = C09621.this.values.iterator();
            }

            public boolean hasNext() {
                return this.it.hasNext();
            }

            public V next() {
                return ((CacheObject) this.it.next()).object;
            }

            public void remove() {
                this.it.remove();
            }
        }

        C09621() {
            this.values = Cache.this.map.values();
        }

        public Iterator<V> iterator() {
            return new C09611();
        }

        public int size() {
            return this.values.size();
        }
    }

    /* renamed from: org.jivesoftware.smack.util.Cache.2 */
    class C09642 extends AbstractSet<Entry<K, V>> {
        private final Set<Entry<K, CacheObject<V>>> set;

        /* renamed from: org.jivesoftware.smack.util.Cache.2.1 */
        class C09631 implements Iterator<Entry<K, V>> {
            private final Iterator<Entry<K, CacheObject<V>>> it;

            /* renamed from: org.jivesoftware.smack.util.Cache.2.1.1 */
            class C13011 extends AbstractMapEntry<K, V> {
                C13011(Object obj, Object obj2) {
                    super(obj, obj2);
                }

                public V setValue(V v) {
                    throw new UnsupportedOperationException("Cannot set");
                }
            }

            C09631() {
                this.it = C09642.this.set.iterator();
            }

            public boolean hasNext() {
                return this.it.hasNext();
            }

            public Entry<K, V> next() {
                Entry entry = (Entry) this.it.next();
                return new C13011(entry.getKey(), ((CacheObject) entry.getValue()).object);
            }

            public void remove() {
                this.it.remove();
            }
        }

        C09642() {
            this.set = Cache.this.map.entrySet();
        }

        public Iterator<Entry<K, V>> iterator() {
            return new C09631();
        }

        public int size() {
            return this.set.size();
        }
    }

    private static class CacheObject<V> {
        public LinkedListNode ageListNode;
        public LinkedListNode lastAccessedListNode;
        public V object;
        public int readCount;

        public CacheObject(V v) {
            this.readCount = 0;
            this.object = v;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CacheObject)) {
                return false;
            }
            return this.object.equals(((CacheObject) obj).object);
        }

        public int hashCode() {
            return this.object.hashCode();
        }
    }

    private static class LinkedList {
        private LinkedListNode head;

        public LinkedList() {
            this.head = new LinkedListNode("head", null, null);
            LinkedListNode linkedListNode = this.head;
            LinkedListNode linkedListNode2 = this.head;
            LinkedListNode linkedListNode3 = this.head;
            linkedListNode2.previous = linkedListNode3;
            linkedListNode.next = linkedListNode3;
        }

        public LinkedListNode getFirst() {
            LinkedListNode linkedListNode = this.head.next;
            if (linkedListNode == this.head) {
                return null;
            }
            return linkedListNode;
        }

        public LinkedListNode getLast() {
            LinkedListNode linkedListNode = this.head.previous;
            if (linkedListNode == this.head) {
                return null;
            }
            return linkedListNode;
        }

        public LinkedListNode addFirst(LinkedListNode linkedListNode) {
            linkedListNode.next = this.head.next;
            linkedListNode.previous = this.head;
            linkedListNode.previous.next = linkedListNode;
            linkedListNode.next.previous = linkedListNode;
            return linkedListNode;
        }

        public LinkedListNode addFirst(Object obj) {
            LinkedListNode linkedListNode = new LinkedListNode(obj, this.head.next, this.head);
            linkedListNode.previous.next = linkedListNode;
            linkedListNode.next.previous = linkedListNode;
            return linkedListNode;
        }

        public LinkedListNode addLast(Object obj) {
            LinkedListNode linkedListNode = new LinkedListNode(obj, this.head, this.head.previous);
            linkedListNode.previous.next = linkedListNode;
            linkedListNode.next.previous = linkedListNode;
            return linkedListNode;
        }

        public void clear() {
            LinkedListNode last = getLast();
            while (last != null) {
                last.remove();
                last = getLast();
            }
            last = this.head;
            LinkedListNode linkedListNode = this.head;
            LinkedListNode linkedListNode2 = this.head;
            linkedListNode.previous = linkedListNode2;
            last.next = linkedListNode2;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (LinkedListNode linkedListNode = this.head.next; linkedListNode != this.head; linkedListNode = linkedListNode.next) {
                stringBuilder.append(linkedListNode.toString()).append(", ");
            }
            return stringBuilder.toString();
        }
    }

    private static class LinkedListNode {
        public LinkedListNode next;
        public Object object;
        public LinkedListNode previous;
        public long timestamp;

        public LinkedListNode(Object obj, LinkedListNode linkedListNode, LinkedListNode linkedListNode2) {
            this.object = obj;
            this.next = linkedListNode;
            this.previous = linkedListNode2;
        }

        public void remove() {
            this.previous.next = this.next;
            this.next.previous = this.previous;
        }

        public String toString() {
            return this.object.toString();
        }
    }

    public Cache(int i, long j) {
        this.cacheMisses = 0;
        if (i == 0) {
            throw new IllegalArgumentException("Max cache size cannot be 0.");
        }
        this.maxCacheSize = i;
        this.maxLifetime = j;
        this.map = new HashMap(Service.X400);
        this.lastAccessedList = new LinkedList();
        this.ageList = new LinkedList();
    }

    public synchronized V put(K k, V v) {
        V v2;
        v2 = null;
        if (this.map.containsKey(k)) {
            v2 = remove(k, true);
        }
        CacheObject cacheObject = new CacheObject(v);
        this.map.put(k, cacheObject);
        cacheObject.lastAccessedListNode = this.lastAccessedList.addFirst((Object) k);
        LinkedListNode addFirst = this.ageList.addFirst((Object) k);
        addFirst.timestamp = System.currentTimeMillis();
        cacheObject.ageListNode = addFirst;
        cullCache();
        return v2;
    }

    public synchronized V get(Object obj) {
        V v;
        deleteExpiredEntries();
        CacheObject cacheObject = (CacheObject) this.map.get(obj);
        if (cacheObject == null) {
            this.cacheMisses++;
            v = null;
        } else {
            cacheObject.lastAccessedListNode.remove();
            this.lastAccessedList.addFirst(cacheObject.lastAccessedListNode);
            this.cacheHits++;
            cacheObject.readCount++;
            v = cacheObject.object;
        }
        return v;
    }

    public synchronized V remove(Object obj) {
        return remove(obj, false);
    }

    public synchronized V remove(Object obj, boolean z) {
        V v;
        CacheObject cacheObject = (CacheObject) this.map.remove(obj);
        if (cacheObject == null) {
            v = null;
        } else {
            cacheObject.lastAccessedListNode.remove();
            cacheObject.ageListNode.remove();
            cacheObject.ageListNode = null;
            cacheObject.lastAccessedListNode = null;
            v = cacheObject.object;
        }
        return v;
    }

    public synchronized void clear() {
        for (Object remove : this.map.keySet().toArray()) {
            remove(remove);
        }
        this.map.clear();
        this.lastAccessedList.clear();
        this.ageList.clear();
        this.cacheHits = 0;
        this.cacheMisses = 0;
    }

    public synchronized int size() {
        deleteExpiredEntries();
        return this.map.size();
    }

    public synchronized boolean isEmpty() {
        deleteExpiredEntries();
        return this.map.isEmpty();
    }

    public synchronized Collection<V> values() {
        deleteExpiredEntries();
        return Collections.unmodifiableCollection(new C09621());
    }

    public synchronized boolean containsKey(Object obj) {
        deleteExpiredEntries();
        return this.map.containsKey(obj);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof CacheObject) {
                value = ((CacheObject) value).object;
            }
            put(entry.getKey(), value);
        }
    }

    public synchronized boolean containsValue(Object obj) {
        deleteExpiredEntries();
        return this.map.containsValue(new CacheObject(obj));
    }

    public synchronized Set<Entry<K, V>> entrySet() {
        deleteExpiredEntries();
        return new C09642();
    }

    public synchronized Set<K> keySet() {
        deleteExpiredEntries();
        return Collections.unmodifiableSet(this.map.keySet());
    }

    public long getCacheHits() {
        return this.cacheHits;
    }

    public long getCacheMisses() {
        return this.cacheMisses;
    }

    public int getMaxCacheSize() {
        return this.maxCacheSize;
    }

    public synchronized void setMaxCacheSize(int i) {
        this.maxCacheSize = i;
        cullCache();
    }

    public long getMaxLifetime() {
        return this.maxLifetime;
    }

    public void setMaxLifetime(long j) {
        this.maxLifetime = j;
    }

    protected synchronized void deleteExpiredEntries() {
        if (this.maxLifetime > 0) {
            LinkedListNode last = this.ageList.getLast();
            if (last != null) {
                long currentTimeMillis = System.currentTimeMillis() - this.maxLifetime;
                while (currentTimeMillis > last.timestamp) {
                    if (remove(last.object, true) == null) {
                        System.err.println("Error attempting to remove(" + last.object.toString() + ") - cacheObject not found in cache!");
                        last.remove();
                    }
                    last = this.ageList.getLast();
                    if (last == null) {
                        break;
                    }
                }
            }
        }
    }

    protected synchronized void cullCache() {
        if (this.maxCacheSize >= 0) {
            if (this.map.size() > this.maxCacheSize) {
                deleteExpiredEntries();
                int i = (int) (((double) this.maxCacheSize) * 0.9d);
                for (int size = this.map.size(); size > i; size--) {
                    if (remove(this.lastAccessedList.getLast().object, true) == null) {
                        System.err.println("Error attempting to cullCache with remove(" + this.lastAccessedList.getLast().object.toString() + ") - " + "cacheObject not found in cache!");
                        this.lastAccessedList.getLast().remove();
                    }
                }
            }
        }
    }
}
