package com.thoughtworks.xstream.converters.extended;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.DefaultMapper;
import com.thoughtworks.xstream.mapper.Mapper;
import java.lang.reflect.Field;

public class JavaFieldConverter implements Converter {
    private final SingleValueConverter javaClassConverter;
    private final Mapper mapper;

    public JavaFieldConverter(ClassLoaderReference classLoaderReference) {
        this(new JavaClassConverter(classLoaderReference), new DefaultMapper(classLoaderReference));
    }

    public JavaFieldConverter(ClassLoader classLoader) {
        this(new ClassLoaderReference(classLoader));
    }

    protected JavaFieldConverter(SingleValueConverter javaClassConverter, Mapper mapper) {
        this.javaClassConverter = javaClassConverter;
        this.mapper = mapper;
    }

    public boolean canConvert(Class type) {
        return type.equals(Field.class);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Field field = (Field) source;
        Class type = field.getDeclaringClass();
        writer.startNode("name");
        writer.setValue(this.mapper.serializedMember(type, field.getName()));
        writer.endNode();
        writer.startNode("clazz");
        writer.setValue(this.javaClassConverter.toString(type));
        writer.endNode();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader r7, com.thoughtworks.xstream.converters.UnmarshallingContext r8) {
        /*
        r6 = this;
        r3 = 0;
        r1 = 0;
    L_0x0002:
        if (r3 == 0) goto L_0x0006;
    L_0x0004:
        if (r1 != 0) goto L_0x0034;
    L_0x0006:
        r4 = r7.hasMoreChildren();
        if (r4 == 0) goto L_0x0034;
    L_0x000c:
        r7.moveDown();
        r4 = r7.getNodeName();
        r5 = "name";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0023;
    L_0x001b:
        r3 = r7.getValue();
    L_0x001f:
        r7.moveUp();
        goto L_0x0002;
    L_0x0023:
        r4 = r7.getNodeName();
        r5 = "clazz";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x001f;
    L_0x002f:
        r1 = r7.getValue();
        goto L_0x001f;
    L_0x0034:
        r4 = r6.javaClassConverter;
        r0 = r4.fromString(r1);
        r0 = (java.lang.Class) r0;
        r4 = r6.mapper;	 Catch:{ NoSuchFieldException -> 0x0047 }
        r4 = r4.realMember(r0, r3);	 Catch:{ NoSuchFieldException -> 0x0047 }
        r4 = r0.getDeclaredField(r4);	 Catch:{ NoSuchFieldException -> 0x0047 }
        return r4;
    L_0x0047:
        r2 = move-exception;
        r4 = new com.thoughtworks.xstream.converters.ConversionException;
        r4.<init>(r2);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.thoughtworks.xstream.converters.extended.JavaFieldConverter.unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext):java.lang.Object");
    }
}
