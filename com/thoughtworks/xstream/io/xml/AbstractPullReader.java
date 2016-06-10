package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.core.util.FastStack;
import com.thoughtworks.xstream.io.AttributeNameIterator;
import com.thoughtworks.xstream.io.naming.NameCoder;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;

public abstract class AbstractPullReader extends AbstractXmlReader {
    protected static final int COMMENT = 4;
    protected static final int END_NODE = 2;
    protected static final int OTHER = 0;
    protected static final int START_NODE = 1;
    protected static final int TEXT = 3;
    private final FastStack elementStack;
    private final FastStack lookahead;
    private final FastStack lookback;
    private boolean marked;
    private final FastStack pool;

    private static class Event {
        int type;
        String value;

        private Event() {
        }
    }

    protected abstract String pullElementName();

    protected abstract int pullNextEvent();

    protected abstract String pullText();

    protected AbstractPullReader(NameCoder nameCoder) {
        super(nameCoder);
        this.elementStack = new FastStack(16);
        this.pool = new FastStack(16);
        this.lookahead = new FastStack(COMMENT);
        this.lookback = new FastStack(COMMENT);
    }

    protected AbstractPullReader(XmlFriendlyReplacer replacer) {
        this((NameCoder) replacer);
    }

    public boolean hasMoreChildren() {
        mark();
        while (true) {
            switch (readEvent().type) {
                case START_NODE /*1*/:
                    reset();
                    return true;
                case END_NODE /*2*/:
                    reset();
                    return false;
                default:
            }
        }
    }

    public void moveDown() {
        int currentDepth = this.elementStack.size();
        while (this.elementStack.size() <= currentDepth) {
            move();
            if (this.elementStack.size() < currentDepth) {
                throw new RuntimeException();
            }
        }
    }

    public void moveUp() {
        int currentDepth = this.elementStack.size();
        while (this.elementStack.size() >= currentDepth) {
            move();
        }
    }

    private void move() {
        Event event = readEvent();
        this.pool.push(event);
        switch (event.type) {
            case START_NODE /*1*/:
                this.elementStack.push(pullElementName());
            case END_NODE /*2*/:
                this.elementStack.pop();
            default:
        }
    }

    private Event readEvent() {
        if (this.marked) {
            if (this.lookback.hasStuff()) {
                return (Event) this.lookahead.push(this.lookback.pop());
            }
            return (Event) this.lookahead.push(readRealEvent());
        } else if (this.lookback.hasStuff()) {
            return (Event) this.lookback.pop();
        } else {
            return readRealEvent();
        }
    }

    private Event readRealEvent() {
        Event event = this.pool.hasStuff() ? (Event) this.pool.pop() : new Event();
        event.type = pullNextEvent();
        if (event.type == TEXT) {
            event.value = pullText();
        } else if (event.type == START_NODE) {
            event.value = pullElementName();
        } else {
            event.value = null;
        }
        return event;
    }

    public void mark() {
        this.marked = true;
    }

    public void reset() {
        while (this.lookahead.hasStuff()) {
            this.lookback.push(this.lookahead.pop());
        }
        this.marked = false;
    }

    public String getValue() {
        String last = null;
        StringBuffer buffer = null;
        mark();
        Event event = readEvent();
        while (true) {
            if (event.type == TEXT) {
                String text = event.value;
                if (text != null && text.length() > 0) {
                    if (last == null) {
                        last = text;
                    } else {
                        if (buffer == null) {
                            buffer = new StringBuffer(last);
                        }
                        buffer.append(text);
                    }
                }
            } else if (event.type != COMMENT) {
                break;
            }
            event = readEvent();
        }
        reset();
        if (buffer != null) {
            return buffer.toString();
        }
        return last == null ? XmlPullParser.NO_NAMESPACE : last;
    }

    public Iterator getAttributeNames() {
        return new AttributeNameIterator(this);
    }

    public String getNodeName() {
        return unescapeXmlName((String) this.elementStack.peek());
    }

    public String peekNextChild() {
        mark();
        while (true) {
            Event ev = readEvent();
            switch (ev.type) {
                case START_NODE /*1*/:
                    reset();
                    return ev.value;
                case END_NODE /*2*/:
                    reset();
                    return null;
                default:
            }
        }
    }
}
