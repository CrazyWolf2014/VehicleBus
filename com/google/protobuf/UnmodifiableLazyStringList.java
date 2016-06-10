package com.google.protobuf;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.RandomAccess;

public class UnmodifiableLazyStringList extends AbstractList<String> implements LazyStringList, RandomAccess {
    private final LazyStringList list;

    /* renamed from: com.google.protobuf.UnmodifiableLazyStringList.1 */
    class C02371 implements ListIterator<String> {
        ListIterator<String> f901a;
        final /* synthetic */ int f902b;
        final /* synthetic */ UnmodifiableLazyStringList f903c;

        C02371(UnmodifiableLazyStringList unmodifiableLazyStringList, int i) {
            this.f903c = unmodifiableLazyStringList;
            this.f902b = i;
            this.f901a = this.f903c.list.listIterator(this.f902b);
        }

        public /* synthetic */ void add(Object obj) {
            m1026b((String) obj);
        }

        public /* synthetic */ Object next() {
            return m1023a();
        }

        public /* synthetic */ Object previous() {
            return m1025b();
        }

        public /* synthetic */ void set(Object obj) {
            m1024a((String) obj);
        }

        public boolean hasNext() {
            return this.f901a.hasNext();
        }

        public String m1023a() {
            return (String) this.f901a.next();
        }

        public boolean hasPrevious() {
            return this.f901a.hasPrevious();
        }

        public String m1025b() {
            return (String) this.f901a.previous();
        }

        public int nextIndex() {
            return this.f901a.nextIndex();
        }

        public int previousIndex() {
            return this.f901a.previousIndex();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void m1024a(String str) {
            throw new UnsupportedOperationException();
        }

        public void m1026b(String str) {
            throw new UnsupportedOperationException();
        }
    }

    /* renamed from: com.google.protobuf.UnmodifiableLazyStringList.2 */
    class C02382 implements Iterator<String> {
        Iterator<String> f904a;
        final /* synthetic */ UnmodifiableLazyStringList f905b;

        C02382(UnmodifiableLazyStringList unmodifiableLazyStringList) {
            this.f905b = unmodifiableLazyStringList;
            this.f904a = this.f905b.list.iterator();
        }

        public /* synthetic */ Object next() {
            return m1027a();
        }

        public boolean hasNext() {
            return this.f904a.hasNext();
        }

        public String m1027a() {
            return (String) this.f904a.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public UnmodifiableLazyStringList(LazyStringList lazyStringList) {
        this.list = lazyStringList;
    }

    public String get(int i) {
        return (String) this.list.get(i);
    }

    public int size() {
        return this.list.size();
    }

    public ByteString getByteString(int i) {
        return this.list.getByteString(i);
    }

    public void add(ByteString byteString) {
        throw new UnsupportedOperationException();
    }

    public ListIterator<String> listIterator(int i) {
        return new C02371(this, i);
    }

    public Iterator<String> iterator() {
        return new C02382(this);
    }
}
