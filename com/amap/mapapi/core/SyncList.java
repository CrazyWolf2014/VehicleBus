package com.amap.mapapi.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/* renamed from: com.amap.mapapi.core.s */
public class SyncList<T> implements List<T> {
    protected LinkedList<T> f393a;

    public SyncList() {
        this.f393a = new LinkedList();
    }

    public synchronized void m544c(T t) {
        add(t);
    }

    public synchronized void add(int i, T t) {
        this.f393a.add(i, t);
    }

    public synchronized boolean addAll(Collection<? extends T> collection) {
        return this.f393a.addAll(collection);
    }

    public synchronized boolean addAll(int i, Collection<? extends T> collection) {
        return this.f393a.addAll(i, collection);
    }

    public synchronized void clear() {
        this.f393a.clear();
    }

    public synchronized boolean contains(Object obj) {
        return this.f393a.contains(obj);
    }

    public synchronized boolean containsAll(Collection<?> collection) {
        return this.f393a.containsAll(collection);
    }

    public synchronized T get(int i) {
        T t;
        t = null;
        try {
            t = this.f393a.get(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public synchronized int indexOf(Object obj) {
        return this.f393a.indexOf(obj);
    }

    public synchronized boolean isEmpty() {
        return this.f393a.isEmpty();
    }

    public synchronized Iterator<T> iterator() {
        return this.f393a.listIterator();
    }

    public synchronized int lastIndexOf(Object obj) {
        return this.f393a.lastIndexOf(obj);
    }

    public synchronized ListIterator<T> listIterator() {
        return this.f393a.listIterator();
    }

    public synchronized ListIterator<T> listIterator(int i) {
        return this.f393a.listIterator(i);
    }

    public synchronized T remove(int i) {
        return this.f393a.remove(i);
    }

    public synchronized boolean remove(Object obj) {
        return this.f393a.remove(obj);
    }

    public synchronized boolean removeAll(Collection<?> collection) {
        return this.f393a.removeAll(collection);
    }

    public synchronized boolean retainAll(Collection<?> collection) {
        return this.f393a.retainAll(collection);
    }

    public synchronized T set(int i, T t) {
        return this.f393a.set(i, t);
    }

    public synchronized int size() {
        return this.f393a.size();
    }

    public synchronized List<T> subList(int i, int i2) {
        return this.f393a.subList(i, i2);
    }

    public synchronized Object[] toArray() {
        return this.f393a.toArray();
    }

    public synchronized <V> V[] toArray(V[] vArr) {
        return this.f393a.toArray(vArr);
    }

    public synchronized boolean add(T t) {
        return this.f393a.add(t);
    }
}
