package com.google.protobuf;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

public class LazyStringArrayList extends AbstractList<String> implements LazyStringList, RandomAccess {
    public static final LazyStringList EMPTY;
    private final List<Object> list;

    static {
        EMPTY = new UnmodifiableLazyStringList(new LazyStringArrayList());
    }

    public LazyStringArrayList() {
        this.list = new ArrayList();
    }

    public LazyStringArrayList(List<String> list) {
        this.list = new ArrayList(list);
    }

    public String get(int i) {
        Object obj = this.list.get(i);
        if (obj instanceof String) {
            return (String) obj;
        }
        ByteString byteString = (ByteString) obj;
        String toStringUtf8 = byteString.toStringUtf8();
        if (Internal.isValidUtf8(byteString)) {
            this.list.set(i, toStringUtf8);
        }
        return toStringUtf8;
    }

    public int size() {
        return this.list.size();
    }

    public String set(int i, String str) {
        return asString(this.list.set(i, str));
    }

    public void add(int i, String str) {
        this.list.add(i, str);
        this.modCount++;
    }

    public boolean addAll(int i, Collection<? extends String> collection) {
        boolean addAll = this.list.addAll(i, collection);
        this.modCount++;
        return addAll;
    }

    public String remove(int i) {
        Object remove = this.list.remove(i);
        this.modCount++;
        return asString(remove);
    }

    public void clear() {
        this.list.clear();
        this.modCount++;
    }

    public void add(ByteString byteString) {
        this.list.add(byteString);
        this.modCount++;
    }

    public ByteString getByteString(int i) {
        Object obj = this.list.get(i);
        if (!(obj instanceof String)) {
            return (ByteString) obj;
        }
        ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
        this.list.set(i, copyFromUtf8);
        return copyFromUtf8;
    }

    private String asString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        return ((ByteString) obj).toStringUtf8();
    }
}
