package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.converters.ErrorWriter;
import com.thoughtworks.xstream.core.util.FastStack;
import com.thoughtworks.xstream.io.AttributeNameIterator;
import com.thoughtworks.xstream.io.naming.NameCoder;
import java.util.Iterator;

public abstract class AbstractDocumentReader extends AbstractXmlReader implements DocumentReader {
    private Object current;
    private FastStack pointers;

    private static class Pointer {
        public int f1661v;

        private Pointer() {
        }
    }

    protected abstract Object getChild(int i);

    protected abstract int getChildCount();

    protected abstract Object getParent();

    protected abstract void reassignCurrentElement(Object obj);

    protected AbstractDocumentReader(Object rootElement) {
        this(rootElement, new XmlFriendlyNameCoder());
    }

    protected AbstractDocumentReader(Object rootElement, NameCoder nameCoder) {
        super(nameCoder);
        this.pointers = new FastStack(16);
        this.current = rootElement;
        this.pointers.push(new Pointer());
        reassignCurrentElement(this.current);
    }

    protected AbstractDocumentReader(Object rootElement, XmlFriendlyReplacer replacer) {
        this(rootElement, (NameCoder) replacer);
    }

    public boolean hasMoreChildren() {
        if (((Pointer) this.pointers.peek()).f1661v < getChildCount()) {
            return true;
        }
        return false;
    }

    public void moveUp() {
        this.current = getParent();
        this.pointers.popSilently();
        reassignCurrentElement(this.current);
    }

    public void moveDown() {
        Pointer pointer = (Pointer) this.pointers.peek();
        this.pointers.push(new Pointer());
        this.current = getChild(pointer.f1661v);
        pointer.f1661v++;
        reassignCurrentElement(this.current);
    }

    public Iterator getAttributeNames() {
        return new AttributeNameIterator(this);
    }

    public void appendErrors(ErrorWriter errorWriter) {
    }

    public Object getCurrent() {
        return this.current;
    }

    public void close() {
    }
}
