package com.thoughtworks.xstream.core.util;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import org.xbill.DNS.KEYRecord;

public class WeakCache extends AbstractMap {
    private final Map map;

    private interface Visitor {
        Object visit(Object obj);
    }

    /* renamed from: com.thoughtworks.xstream.core.util.WeakCache.1 */
    class C11391 implements Visitor {
        final /* synthetic */ Object val$value;

        C11391(Object obj) {
            this.val$value = obj;
        }

        public Object visit(Object element) {
            return element.equals(this.val$value) ? Boolean.TRUE : null;
        }
    }

    /* renamed from: com.thoughtworks.xstream.core.util.WeakCache.2 */
    class C11402 implements Visitor {
        final /* synthetic */ int[] val$i;

        C11402(int[] iArr) {
            this.val$i = iArr;
        }

        public Object visit(Object element) {
            int[] iArr = this.val$i;
            iArr[0] = iArr[0] + 1;
            return null;
        }
    }

    /* renamed from: com.thoughtworks.xstream.core.util.WeakCache.3 */
    class C11413 implements Visitor {
        final /* synthetic */ Collection val$collection;

        C11413(Collection collection) {
            this.val$collection = collection;
        }

        public Object visit(Object element) {
            this.val$collection.add(element);
            return null;
        }
    }

    /* renamed from: com.thoughtworks.xstream.core.util.WeakCache.4 */
    class C11424 implements Visitor {
        final /* synthetic */ Set val$set;

        /* renamed from: com.thoughtworks.xstream.core.util.WeakCache.4.1 */
        class C08971 implements Entry {
            final /* synthetic */ Entry val$entry;

            C08971(Entry entry) {
                this.val$entry = entry;
            }

            public Object getKey() {
                return this.val$entry.getKey();
            }

            public Object getValue() {
                return ((Reference) this.val$entry.getValue()).get();
            }

            public Object setValue(Object value) {
                return this.val$entry.setValue(WeakCache.this.createReference(value));
            }
        }

        C11424(Set set) {
            this.val$set = set;
        }

        public Object visit(Object element) {
            this.val$set.add(new C08971((Entry) element));
            return null;
        }
    }

    public WeakCache() {
        this(new WeakHashMap());
    }

    public WeakCache(Map map) {
        this.map = map;
    }

    public Object get(Object key) {
        Reference reference = (Reference) this.map.get(key);
        return reference != null ? reference.get() : null;
    }

    public Object put(Object key, Object value) {
        Reference ref = (Reference) this.map.put(key, createReference(value));
        return ref == null ? null : ref.get();
    }

    public Object remove(Object key) {
        Reference ref = (Reference) this.map.remove(key);
        return ref == null ? null : ref.get();
    }

    protected Reference createReference(Object value) {
        return new WeakReference(value);
    }

    public boolean containsValue(Object value) {
        if (((Boolean) iterate(new C11391(value), 0)) == Boolean.TRUE) {
            return true;
        }
        return false;
    }

    public int size() {
        if (this.map.size() == 0) {
            return 0;
        }
        int[] i = new int[]{0};
        iterate(new C11402(i), 0);
        return i[0];
    }

    public Collection values() {
        Collection collection = new ArrayList();
        if (this.map.size() != 0) {
            iterate(new C11413(collection), 0);
        }
        return collection;
    }

    public Set entrySet() {
        Set set = new HashSet();
        if (this.map.size() != 0) {
            iterate(new C11424(set), 2);
        }
        return set;
    }

    private Object iterate(Visitor visitor, int type) {
        Object result = null;
        Iterator iter = this.map.entrySet().iterator();
        while (result == null && iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            Object element = ((Reference) entry.getValue()).get();
            if (element != null) {
                switch (type) {
                    case KEYRecord.OWNER_USER /*0*/:
                        result = visitor.visit(element);
                        break;
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        result = visitor.visit(entry.getKey());
                        break;
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        result = visitor.visit(entry);
                        break;
                    default:
                        break;
                }
            }
            iter.remove();
        }
        return result;
    }

    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    public void clear() {
        this.map.clear();
    }

    public Set keySet() {
        return this.map.keySet();
    }

    public boolean equals(Object o) {
        return this.map.equals(o);
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public String toString() {
        return this.map.toString();
    }
}
