package com.google.protobuf;

import com.google.protobuf.FieldSet.FieldDescriptorLite;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/* renamed from: com.google.protobuf.f */
class SmallSortedMap<K extends Comparable<K>, V> extends AbstractMap<K, V> {
    private final int f916a;
    private List<SmallSortedMap> f917b;
    private Map<K, V> f918c;
    private boolean f919d;
    private volatile SmallSortedMap f920e;

    /* renamed from: com.google.protobuf.f.a */
    private static class SmallSortedMap {
        private static final Iterator<Object> f906a;
        private static final Iterable<Object> f907b;

        private SmallSortedMap() {
        }

        static {
            f906a = new SmallSortedMap();
            f907b = new SmallSortedMap();
        }

        static <T> Iterable<T> m1028a() {
            return f907b;
        }
    }

    /* renamed from: com.google.protobuf.f.b */
    private class SmallSortedMap implements Entry<K, V>, Comparable<SmallSortedMap> {
        final /* synthetic */ SmallSortedMap f908a;
        private final K f909b;
        private V f910c;

        public /* synthetic */ int compareTo(Object obj) {
            return m1031a((SmallSortedMap) obj);
        }

        public /* synthetic */ Object getKey() {
            return m1032a();
        }

        SmallSortedMap(SmallSortedMap smallSortedMap, Entry<K, V> entry) {
            this(smallSortedMap, (Comparable) entry.getKey(), entry.getValue());
        }

        SmallSortedMap(SmallSortedMap smallSortedMap, K k, V v) {
            this.f908a = smallSortedMap;
            this.f909b = k;
            this.f910c = v;
        }

        public K m1032a() {
            return this.f909b;
        }

        public V getValue() {
            return this.f910c;
        }

        public int m1031a(SmallSortedMap smallSortedMap) {
            return m1032a().compareTo(smallSortedMap.m1032a());
        }

        public V setValue(V v) {
            this.f908a.m1043e();
            V v2 = this.f910c;
            this.f910c = v;
            return v2;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            if (m1030a(this.f909b, entry.getKey()) && m1030a(this.f910c, entry.getValue())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int i = 0;
            int hashCode = this.f909b == null ? 0 : this.f909b.hashCode();
            if (this.f910c != null) {
                i = this.f910c.hashCode();
            }
            return hashCode ^ i;
        }

        public String toString() {
            return this.f909b + "=" + this.f910c;
        }

        private boolean m1030a(Object obj, Object obj2) {
            if (obj == null) {
                return obj2 == null;
            } else {
                return obj.equals(obj2);
            }
        }
    }

    /* renamed from: com.google.protobuf.f.c */
    private class SmallSortedMap implements Iterator<Entry<K, V>> {
        final /* synthetic */ SmallSortedMap f911a;
        private int f912b;
        private boolean f913c;
        private Iterator<Entry<K, V>> f914d;

        private SmallSortedMap(SmallSortedMap smallSortedMap) {
            this.f911a = smallSortedMap;
            this.f912b = -1;
        }

        public /* synthetic */ Object next() {
            return m1034a();
        }

        public boolean hasNext() {
            return this.f912b + 1 < this.f911a.f917b.size() || m1033b().hasNext();
        }

        public Entry<K, V> m1034a() {
            this.f913c = true;
            int i = this.f912b + 1;
            this.f912b = i;
            if (i < this.f911a.f917b.size()) {
                return (Entry) this.f911a.f917b.get(this.f912b);
            }
            return (Entry) m1033b().next();
        }

        public void remove() {
            if (this.f913c) {
                this.f913c = false;
                this.f911a.m1043e();
                if (this.f912b < this.f911a.f917b.size()) {
                    SmallSortedMap smallSortedMap = this.f911a;
                    int i = this.f912b;
                    this.f912b = i - 1;
                    smallSortedMap.m1041c(i);
                    return;
                }
                m1033b().remove();
                return;
            }
            throw new IllegalStateException("remove() was called before next()");
        }

        private Iterator<Entry<K, V>> m1033b() {
            if (this.f914d == null) {
                this.f914d = this.f911a.f918c.entrySet().iterator();
            }
            return this.f914d;
        }
    }

    /* renamed from: com.google.protobuf.f.d */
    private class SmallSortedMap extends AbstractSet<Entry<K, V>> {
        final /* synthetic */ SmallSortedMap f915a;

        private SmallSortedMap(SmallSortedMap smallSortedMap) {
            this.f915a = smallSortedMap;
        }

        public /* synthetic */ boolean add(Object obj) {
            return m1035a((Entry) obj);
        }

        public Iterator<Entry<K, V>> iterator() {
            return new SmallSortedMap(null);
        }

        public int size() {
            return this.f915a.size();
        }

        public boolean contains(Object obj) {
            Entry entry = (Entry) obj;
            Object obj2 = this.f915a.get(entry.getKey());
            Object value = entry.getValue();
            return obj2 == value || (obj2 != null && obj2.equals(value));
        }

        public boolean m1035a(Entry<K, V> entry) {
            if (contains(entry)) {
                return false;
            }
            this.f915a.m1046a((Comparable) entry.getKey(), entry.getValue());
            return true;
        }

        public boolean remove(Object obj) {
            Entry entry = (Entry) obj;
            if (!contains(entry)) {
                return false;
            }
            this.f915a.remove(entry.getKey());
            return true;
        }

        public void clear() {
            this.f915a.clear();
        }
    }

    /* renamed from: com.google.protobuf.f.1 */
    static class SmallSortedMap extends SmallSortedMap<FieldDescriptorType, Object> {
        SmallSortedMap(int i) {
            super(null);
        }

        public /* synthetic */ Object put(Object obj, Object obj2) {
            return super.m1046a((FieldDescriptorLite) obj, obj2);
        }

        public void m2063a() {
            if (!m1049b()) {
                for (int i = 0; i < m1050c(); i++) {
                    Entry b = m1048b(i);
                    if (((FieldDescriptorLite) b.getKey()).isRepeated()) {
                        b.setValue(Collections.unmodifiableList((List) b.getValue()));
                    }
                }
                for (Entry entry : m1051d()) {
                    if (((FieldDescriptorLite) entry.getKey()).isRepeated()) {
                        entry.setValue(Collections.unmodifiableList((List) entry.getValue()));
                    }
                }
            }
            super.m1047a();
        }
    }

    public /* synthetic */ Object put(Object obj, Object obj2) {
        return m1046a((Comparable) obj, obj2);
    }

    static <FieldDescriptorType extends FieldDescriptorLite<FieldDescriptorType>> SmallSortedMap<FieldDescriptorType, Object> m1037a(int i) {
        return new SmallSortedMap(i);
    }

    private SmallSortedMap(int i) {
        this.f916a = i;
        this.f917b = Collections.emptyList();
        this.f918c = Collections.emptyMap();
    }

    public void m1047a() {
        if (!this.f919d) {
            this.f918c = this.f918c.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.f918c);
            this.f919d = true;
        }
    }

    public boolean m1049b() {
        return this.f919d;
    }

    public int m1050c() {
        return this.f917b.size();
    }

    public Entry<K, V> m1048b(int i) {
        return (Entry) this.f917b.get(i);
    }

    public Iterable<Entry<K, V>> m1051d() {
        return this.f918c.isEmpty() ? SmallSortedMap.m1028a() : this.f918c.entrySet();
    }

    public int size() {
        return this.f917b.size() + this.f918c.size();
    }

    public boolean containsKey(Object obj) {
        Comparable comparable = (Comparable) obj;
        return m1036a(comparable) >= 0 || this.f918c.containsKey(comparable);
    }

    public V get(Object obj) {
        Comparable comparable = (Comparable) obj;
        int a = m1036a(comparable);
        if (a >= 0) {
            return ((SmallSortedMap) this.f917b.get(a)).getValue();
        }
        return this.f918c.get(comparable);
    }

    public V m1046a(K k, V v) {
        m1043e();
        int a = m1036a((Comparable) k);
        if (a >= 0) {
            return ((SmallSortedMap) this.f917b.get(a)).setValue(v);
        }
        m1045g();
        int i = -(a + 1);
        if (i >= this.f916a) {
            return m1044f().put(k, v);
        }
        if (this.f917b.size() == this.f916a) {
            SmallSortedMap smallSortedMap = (SmallSortedMap) this.f917b.remove(this.f916a - 1);
            m1044f().put(smallSortedMap.m1032a(), smallSortedMap.getValue());
        }
        this.f917b.add(i, new SmallSortedMap(this, k, v));
        return null;
    }

    public void clear() {
        m1043e();
        if (!this.f917b.isEmpty()) {
            this.f917b.clear();
        }
        if (!this.f918c.isEmpty()) {
            this.f918c.clear();
        }
    }

    public V remove(Object obj) {
        m1043e();
        Comparable comparable = (Comparable) obj;
        int a = m1036a(comparable);
        if (a >= 0) {
            return m1041c(a);
        }
        if (this.f918c.isEmpty()) {
            return null;
        }
        return this.f918c.remove(comparable);
    }

    private V m1041c(int i) {
        m1043e();
        V value = ((SmallSortedMap) this.f917b.remove(i)).getValue();
        if (!this.f918c.isEmpty()) {
            Iterator it = m1044f().entrySet().iterator();
            this.f917b.add(new SmallSortedMap(this, (Entry) it.next()));
            it.remove();
        }
        return value;
    }

    private int m1036a(K k) {
        int compareTo;
        int i = 0;
        int size = this.f917b.size() - 1;
        if (size >= 0) {
            compareTo = k.compareTo(((SmallSortedMap) this.f917b.get(size)).m1032a());
            if (compareTo > 0) {
                return -(size + 2);
            }
            if (compareTo == 0) {
                return size;
            }
        }
        while (i <= size) {
            int i2 = (i + size) / 2;
            compareTo = k.compareTo(((SmallSortedMap) this.f917b.get(i2)).m1032a());
            if (compareTo < 0) {
                compareTo = i2 - 1;
                size = i;
            } else if (compareTo <= 0) {
                return i2;
            } else {
                int i3 = size;
                size = i2 + 1;
                compareTo = i3;
            }
            i = size;
            size = compareTo;
        }
        return -(i + 1);
    }

    public Set<Entry<K, V>> entrySet() {
        if (this.f920e == null) {
            this.f920e = new SmallSortedMap();
        }
        return this.f920e;
    }

    private void m1043e() {
        if (this.f919d) {
            throw new UnsupportedOperationException();
        }
    }

    private SortedMap<K, V> m1044f() {
        m1043e();
        if (this.f918c.isEmpty() && !(this.f918c instanceof TreeMap)) {
            this.f918c = new TreeMap();
        }
        return (SortedMap) this.f918c;
    }

    private void m1045g() {
        m1043e();
        if (this.f917b.isEmpty() && !(this.f917b instanceof ArrayList)) {
            this.f917b = new ArrayList(this.f916a);
        }
    }
}
