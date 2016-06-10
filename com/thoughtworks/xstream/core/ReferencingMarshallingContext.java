package com.thoughtworks.xstream.core;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.path.Path;

public interface ReferencingMarshallingContext extends MarshallingContext {
    Path currentPath();

    Object lookupReference(Object obj);

    void registerImplicit(Object obj);

    void replace(Object obj, Object obj2);
}
