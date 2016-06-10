package com.thoughtworks.xstream.core;

import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.io.AbstractReader;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.path.Path;
import com.thoughtworks.xstream.io.path.PathTracker;
import com.thoughtworks.xstream.io.path.PathTrackingReader;
import com.thoughtworks.xstream.mapper.Mapper;

public class ReferenceByXPathUnmarshaller extends AbstractReferenceUnmarshaller {
    protected boolean isNameEncoding;
    private PathTracker pathTracker;

    public ReferenceByXPathUnmarshaller(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
        super(root, reader, converterLookup, mapper);
        this.pathTracker = new PathTracker();
        this.reader = new PathTrackingReader(reader, this.pathTracker);
        this.isNameEncoding = reader.underlyingReader() instanceof AbstractReader;
    }

    protected Object getReferenceKey(String reference) {
        String decodeNode;
        if (this.isNameEncoding) {
            decodeNode = ((AbstractReader) this.reader.underlyingReader()).decodeNode(reference);
        } else {
            decodeNode = reference;
        }
        Path path = new Path(decodeNode);
        return reference.charAt(0) != '/' ? this.pathTracker.getPath().apply(path) : path;
    }

    protected Object getCurrentReferenceKey() {
        return this.pathTracker.getPath();
    }
}
