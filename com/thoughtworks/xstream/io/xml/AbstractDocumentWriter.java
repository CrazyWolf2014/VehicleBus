package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.core.util.FastStack;
import com.thoughtworks.xstream.io.naming.NameCoder;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDocumentWriter extends AbstractXmlWriter implements DocumentWriter {
    private final FastStack nodeStack;
    private final List result;

    protected abstract Object createNode(String str);

    public AbstractDocumentWriter(Object container, NameCoder nameCoder) {
        super(nameCoder);
        this.result = new ArrayList();
        this.nodeStack = new FastStack(16);
        if (container != null) {
            this.nodeStack.push(container);
            this.result.add(container);
        }
    }

    public AbstractDocumentWriter(Object container, XmlFriendlyReplacer replacer) {
        this(container, (NameCoder) replacer);
    }

    public final void startNode(String name) {
        this.nodeStack.push(createNode(name));
    }

    public final void endNode() {
        endNodeInternally();
        Object node = this.nodeStack.pop();
        if (this.nodeStack.size() == 0) {
            this.result.add(node);
        }
    }

    public void endNodeInternally() {
    }

    protected final Object getCurrent() {
        return this.nodeStack.peek();
    }

    public List getTopLevelNodes() {
        return this.result;
    }

    public void flush() {
    }

    public void close() {
    }
}
