package org.jivesoftware.smackx.workgroup.util;

import java.util.Iterator;
import java.util.ListIterator;

/* compiled from: ModelUtil */
class ReverseListIterator<T> implements Iterator<T> {
    private ListIterator<T> _i;

    ReverseListIterator(ListIterator<T> listIterator) {
        this._i = listIterator;
        while (this._i.hasNext()) {
            this._i.next();
        }
    }

    public boolean hasNext() {
        return this._i.hasPrevious();
    }

    public T next() {
        return this._i.previous();
    }

    public void remove() {
        this._i.remove();
    }
}
