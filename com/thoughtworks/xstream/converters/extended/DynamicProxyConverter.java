package com.thoughtworks.xstream.converters.extended;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.core.util.Fields;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.DynamicProxyMapper.DynamicProxy;
import com.thoughtworks.xstream.mapper.Mapper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class DynamicProxyConverter implements Converter {
    private static final InvocationHandler DUMMY;
    private static final Field HANDLER;
    private ClassLoaderReference classLoaderReference;
    private Mapper mapper;

    /* renamed from: com.thoughtworks.xstream.converters.extended.DynamicProxyConverter.1 */
    static class C08821 implements InvocationHandler {
        C08821() {
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    static {
        HANDLER = Fields.locate(Proxy.class, InvocationHandler.class, false);
        DUMMY = new C08821();
    }

    public DynamicProxyConverter(Mapper mapper) {
        this(mapper, DynamicProxyConverter.class.getClassLoader());
    }

    public DynamicProxyConverter(Mapper mapper, ClassLoaderReference classLoaderReference) {
        this.classLoaderReference = classLoaderReference;
        this.mapper = mapper;
    }

    public DynamicProxyConverter(Mapper mapper, ClassLoader classLoader) {
        this(mapper, new ClassLoaderReference(classLoader));
    }

    public boolean canConvert(Class type) {
        return type.equals(DynamicProxy.class) || Proxy.isProxyClass(type);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(source);
        addInterfacesToXml(source, writer);
        writer.startNode("handler");
        String attributeName = this.mapper.aliasForSystemAttribute("class");
        if (attributeName != null) {
            writer.addAttribute(attributeName, this.mapper.serializedClass(invocationHandler.getClass()));
        }
        context.convertAnother(invocationHandler);
        writer.endNode();
    }

    private void addInterfacesToXml(Object source, HierarchicalStreamWriter writer) {
        Class[] interfaces = source.getClass().getInterfaces();
        for (Class currentInterface : interfaces) {
            writer.startNode("interface");
            writer.setValue(this.mapper.serializedClass(currentInterface));
            writer.endNode();
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        List interfaces = new ArrayList();
        Class handlerType = null;
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String elementName = reader.getNodeName();
            if (elementName.equals("interface")) {
                interfaces.add(this.mapper.realClass(reader.getValue()));
            } else if (elementName.equals("handler")) {
                String attributeName = this.mapper.aliasForSystemAttribute("class");
                if (attributeName != null) {
                    handlerType = this.mapper.realClass(reader.getAttribute(attributeName));
                    break;
                }
            } else {
                continue;
            }
            reader.moveUp();
        }
        if (handlerType == null) {
            throw new ConversionException("No InvocationHandler specified for dynamic proxy");
        }
        Class[] interfacesAsArray = new Class[interfaces.size()];
        interfaces.toArray(interfacesAsArray);
        Object proxy = null;
        if (HANDLER != null) {
            proxy = Proxy.newProxyInstance(this.classLoaderReference.getReference(), interfacesAsArray, DUMMY);
        }
        InvocationHandler handler = (InvocationHandler) context.convertAnother(proxy, handlerType);
        reader.moveUp();
        if (HANDLER == null) {
            return Proxy.newProxyInstance(this.classLoaderReference.getReference(), interfacesAsArray, handler);
        }
        Fields.write(HANDLER, proxy, handler);
        return proxy;
    }
}
