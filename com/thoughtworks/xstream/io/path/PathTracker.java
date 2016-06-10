package com.thoughtworks.xstream.io.path;

import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public class PathTracker {
    private int capacity;
    private Path currentPath;
    private Map[] indexMapStack;
    private String[] pathStack;
    private int pointer;

    public PathTracker() {
        this(16);
    }

    public PathTracker(int initialCapacity) {
        this.capacity = Math.max(1, initialCapacity);
        this.pathStack = new String[this.capacity];
        this.indexMapStack = new Map[this.capacity];
    }

    public void pushElement(String name) {
        if (this.pointer + 1 >= this.capacity) {
            resizeStacks(this.capacity * 2);
        }
        this.pathStack[this.pointer] = name;
        Map indexMap = this.indexMapStack[this.pointer];
        if (indexMap == null) {
            indexMap = new HashMap();
            this.indexMapStack[this.pointer] = indexMap;
        }
        if (indexMap.containsKey(name)) {
            indexMap.put(name, new Integer(((Integer) indexMap.get(name)).intValue() + 1));
        } else {
            indexMap.put(name, new Integer(1));
        }
        this.pointer++;
        this.currentPath = null;
    }

    public void popElement() {
        this.indexMapStack[this.pointer] = null;
        this.pathStack[this.pointer] = null;
        this.currentPath = null;
        this.pointer--;
    }

    public String peekElement() {
        return peekElement(0);
    }

    public String peekElement(int i) {
        if (i < (-this.pointer) || i > 0) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        int idx = (this.pointer + i) - 1;
        int index = ((Integer) this.indexMapStack[idx].get(this.pathStack[idx])).intValue();
        if (index <= 1) {
            return this.pathStack[idx];
        }
        StringBuffer chunk = new StringBuffer(this.pathStack[idx].length() + 6);
        chunk.append(this.pathStack[idx]).append('[').append(index).append(']');
        return chunk.toString();
    }

    public int depth() {
        return this.pointer;
    }

    private void resizeStacks(int newCapacity) {
        String[] newPathStack = new String[newCapacity];
        Map[] newIndexMapStack = new Map[newCapacity];
        int min = Math.min(this.capacity, newCapacity);
        System.arraycopy(this.pathStack, 0, newPathStack, 0, min);
        System.arraycopy(this.indexMapStack, 0, newIndexMapStack, 0, min);
        this.pathStack = newPathStack;
        this.indexMapStack = newIndexMapStack;
        this.capacity = newCapacity;
    }

    public Path getPath() {
        if (this.currentPath == null) {
            String[] chunks = new String[(this.pointer + 1)];
            chunks[0] = XmlPullParser.NO_NAMESPACE;
            int i = -this.pointer;
            while (true) {
                i++;
                if (i > 0) {
                    break;
                }
                chunks[this.pointer + i] = peekElement(i);
            }
            this.currentPath = new Path(chunks);
        }
        return this.currentPath;
    }
}
