package com.thoughtworks.xstream.converters.extended;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.Subject;

public class SubjectConverter extends AbstractCollectionConverter {
    public SubjectConverter(Mapper mapper) {
        super(mapper);
    }

    public boolean canConvert(Class type) {
        return type.equals(Subject.class);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Subject subject = (Subject) source;
        marshalPrincipals(subject.getPrincipals(), writer, context);
        marshalPublicCredentials(subject.getPublicCredentials(), writer, context);
        marshalPrivateCredentials(subject.getPrivateCredentials(), writer, context);
        marshalReadOnly(subject.isReadOnly(), writer);
    }

    protected void marshalPrincipals(Set principals, HierarchicalStreamWriter writer, MarshallingContext context) {
        writer.startNode("principals");
        for (Object principal : principals) {
            writeItem(principal, context, writer);
        }
        writer.endNode();
    }

    protected void marshalPublicCredentials(Set pubCredentials, HierarchicalStreamWriter writer, MarshallingContext context) {
    }

    protected void marshalPrivateCredentials(Set privCredentials, HierarchicalStreamWriter writer, MarshallingContext context) {
    }

    protected void marshalReadOnly(boolean readOnly, HierarchicalStreamWriter writer) {
        writer.startNode("readOnly");
        writer.setValue(String.valueOf(readOnly));
        writer.endNode();
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return new Subject(unmarshalReadOnly(reader), unmarshalPrincipals(reader, context), unmarshalPublicCredentials(reader, context), unmarshalPrivateCredentials(reader, context));
    }

    protected Set unmarshalPrincipals(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return populateSet(reader, context);
    }

    protected Set unmarshalPublicCredentials(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return Collections.EMPTY_SET;
    }

    protected Set unmarshalPrivateCredentials(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return Collections.EMPTY_SET;
    }

    protected boolean unmarshalReadOnly(HierarchicalStreamReader reader) {
        reader.moveDown();
        boolean readOnly = Boolean.getBoolean(reader.getValue());
        reader.moveUp();
        return readOnly;
    }

    protected Set populateSet(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Set set = new HashSet();
        reader.moveDown();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            Object elementl = readItem(reader, context, set);
            reader.moveUp();
            set.add(elementl);
        }
        reader.moveUp();
        return set;
    }
}
