package com.thoughtworks.xstream.io.binary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class ReaderDepthState {
    private static final String EMPTY_STRING = "";
    private State current;

    /* renamed from: com.thoughtworks.xstream.io.binary.ReaderDepthState.1 */
    class C09001 implements Iterator {
        final /* synthetic */ Iterator val$attributeIterator;

        C09001(Iterator it) {
            this.val$attributeIterator = it;
        }

        public boolean hasNext() {
            return this.val$attributeIterator.hasNext();
        }

        public Object next() {
            return ((Attribute) this.val$attributeIterator.next()).name;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static class Attribute {
        String name;
        String value;

        private Attribute() {
        }
    }

    private static class State {
        List attributes;
        boolean hasMoreChildren;
        String name;
        State parent;
        String value;

        private State() {
        }
    }

    ReaderDepthState() {
    }

    public void push() {
        State newState = new State();
        newState.parent = this.current;
        this.current = newState;
    }

    public void pop() {
        this.current = this.current.parent;
    }

    public String getName() {
        return this.current.name;
    }

    public void setName(String name) {
        this.current.name = name;
    }

    public String getValue() {
        return this.current.value == null ? EMPTY_STRING : this.current.value;
    }

    public void setValue(String value) {
        this.current.value = value;
    }

    public boolean hasMoreChildren() {
        return this.current.hasMoreChildren;
    }

    public void setHasMoreChildren(boolean hasMoreChildren) {
        this.current.hasMoreChildren = hasMoreChildren;
    }

    public void addAttribute(String name, String value) {
        Attribute attribute = new Attribute();
        attribute.name = name;
        attribute.value = value;
        if (this.current.attributes == null) {
            this.current.attributes = new ArrayList();
        }
        this.current.attributes.add(attribute);
    }

    public String getAttribute(String name) {
        if (this.current.attributes == null) {
            return null;
        }
        for (Attribute attribute : this.current.attributes) {
            if (attribute.name.equals(name)) {
                return attribute.value;
            }
        }
        return null;
    }

    public String getAttribute(int index) {
        if (this.current.attributes == null) {
            return null;
        }
        return ((Attribute) this.current.attributes.get(index)).value;
    }

    public String getAttributeName(int index) {
        if (this.current.attributes == null) {
            return null;
        }
        return ((Attribute) this.current.attributes.get(index)).name;
    }

    public int getAttributeCount() {
        return this.current.attributes == null ? 0 : this.current.attributes.size();
    }

    public Iterator getAttributeNames() {
        if (this.current.attributes == null) {
            return Collections.EMPTY_SET.iterator();
        }
        return new C09001(this.current.attributes.iterator());
    }
}
