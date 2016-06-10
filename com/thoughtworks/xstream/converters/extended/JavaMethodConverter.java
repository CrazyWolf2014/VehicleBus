package com.thoughtworks.xstream.converters.extended;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JavaMethodConverter implements Converter {
    private final SingleValueConverter javaClassConverter;

    public JavaMethodConverter(ClassLoaderReference classLoaderReference) {
        this(new JavaClassConverter(classLoaderReference));
    }

    public JavaMethodConverter(ClassLoader classLoader) {
        this(new ClassLoaderReference(classLoader));
    }

    protected JavaMethodConverter(SingleValueConverter javaClassConverter) {
        this.javaClassConverter = javaClassConverter;
    }

    public boolean canConvert(Class type) {
        return type.equals(Method.class) || type.equals(Constructor.class);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        if (source instanceof Method) {
            Method method = (Method) source;
            marshalMethod(writer, this.javaClassConverter.toString(method.getDeclaringClass()), method.getName(), method.getParameterTypes());
            return;
        }
        Constructor method2 = (Constructor) source;
        marshalMethod(writer, this.javaClassConverter.toString(method2.getDeclaringClass()), null, method2.getParameterTypes());
    }

    private void marshalMethod(HierarchicalStreamWriter writer, String declaringClassName, String methodName, Class[] parameterTypes) {
        writer.startNode("class");
        writer.setValue(declaringClassName);
        writer.endNode();
        if (methodName != null) {
            writer.startNode("name");
            writer.setValue(methodName);
            writer.endNode();
        }
        writer.startNode("parameter-types");
        for (Object singleValueConverter : parameterTypes) {
            writer.startNode("class");
            writer.setValue(this.javaClassConverter.toString(singleValueConverter));
            writer.endNode();
        }
        writer.endNode();
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        try {
            boolean isMethodNotConstructor = context.getRequiredType().equals(Method.class);
            reader.moveDown();
            Class declaringClass = (Class) this.javaClassConverter.fromString(reader.getValue());
            reader.moveUp();
            String methodName = null;
            if (isMethodNotConstructor) {
                reader.moveDown();
                methodName = reader.getValue();
                reader.moveUp();
            }
            reader.moveDown();
            List parameterTypeList = new ArrayList();
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                parameterTypeList.add(this.javaClassConverter.fromString(reader.getValue()));
                reader.moveUp();
            }
            Class[] parameterTypes = (Class[]) parameterTypeList.toArray(new Class[parameterTypeList.size()]);
            reader.moveUp();
            if (isMethodNotConstructor) {
                return declaringClass.getDeclaredMethod(methodName, parameterTypes);
            }
            return declaringClass.getDeclaredConstructor(parameterTypes);
        } catch (Throwable e) {
            throw new ConversionException(e);
        }
    }
}
