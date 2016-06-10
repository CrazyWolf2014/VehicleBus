package com.thoughtworks.xstream.io.path;

import com.thoughtworks.xstream.io.AbstractWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.WriterWrapper;

public class PathTrackingWriter extends WriterWrapper {
    private final boolean isNameEncoding;
    private final PathTracker pathTracker;

    public PathTrackingWriter(HierarchicalStreamWriter writer, PathTracker pathTracker) {
        super(writer);
        this.isNameEncoding = writer.underlyingWriter() instanceof AbstractWriter;
        this.pathTracker = pathTracker;
    }

    public void startNode(String name) {
        String encodeNode;
        PathTracker pathTracker = this.pathTracker;
        if (this.isNameEncoding) {
            encodeNode = ((AbstractWriter) this.wrapped.underlyingWriter()).encodeNode(name);
        } else {
            encodeNode = name;
        }
        pathTracker.pushElement(encodeNode);
        super.startNode(name);
    }

    public void startNode(String name, Class clazz) {
        String encodeNode;
        PathTracker pathTracker = this.pathTracker;
        if (this.isNameEncoding) {
            encodeNode = ((AbstractWriter) this.wrapped.underlyingWriter()).encodeNode(name);
        } else {
            encodeNode = name;
        }
        pathTracker.pushElement(encodeNode);
        super.startNode(name, clazz);
    }

    public void endNode() {
        super.endNode();
        this.pathTracker.popElement();
    }
}
