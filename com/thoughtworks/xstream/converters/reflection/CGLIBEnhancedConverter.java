package com.thoughtworks.xstream.converters.reflection;

import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider.Visitor;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.CGLIBMapper.Marker;
import com.thoughtworks.xstream.mapper.Mapper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.NoOp;
import org.xbill.DNS.KEYRecord.Flags;

public class CGLIBEnhancedConverter extends SerializableConverter {
    private static String CALLBACK_MARKER;
    private static String DEFAULT_NAMING_MARKER;
    private transient Map fieldCache;

    private static class ReverseEngineeredCallbackFilter implements CallbackFilter {
        private final Map callbackIndexMap;

        public ReverseEngineeredCallbackFilter(Map callbackIndexMap) {
            this.callbackIndexMap = callbackIndexMap;
        }

        public int accept(Method method) {
            if (this.callbackIndexMap.containsKey(method)) {
                return ((Integer) this.callbackIndexMap.get(method)).intValue();
            }
            ConversionException exception = new ConversionException("CGLIB callback not detected in reverse engineering");
            exception.add("CGLIB callback", method.toString());
            throw exception;
        }
    }

    private static final class ReverseEngineeringInvocationHandler implements InvocationHandler {
        private final Integer index;
        private final Map indexMap;

        public ReverseEngineeringInvocationHandler(int index, Map indexMap) {
            this.indexMap = indexMap;
            this.index = new Integer(index);
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            this.indexMap.put(this.indexMap.get(null), this.index);
            return null;
        }
    }

    private static class CGLIBFilteringReflectionProvider extends ReflectionProviderWrapper {

        /* renamed from: com.thoughtworks.xstream.converters.reflection.CGLIBEnhancedConverter.CGLIBFilteringReflectionProvider.1 */
        class C11311 implements Visitor {
            final /* synthetic */ Visitor val$visitor;

            C11311(Visitor visitor) {
                this.val$visitor = visitor;
            }

            public void visit(String name, Class type, Class definedIn, Object value) {
                if (!name.startsWith("CGLIB$")) {
                    this.val$visitor.visit(name, type, definedIn, value);
                }
            }
        }

        public CGLIBFilteringReflectionProvider(ReflectionProvider reflectionProvider) {
            super(reflectionProvider);
        }

        public void visitSerializableFields(Object object, Visitor visitor) {
            this.wrapped.visitSerializableFields(object, new C11311(visitor));
        }
    }

    static {
        DEFAULT_NAMING_MARKER = "$$EnhancerByCGLIB$$";
        CALLBACK_MARKER = "CGLIB$CALLBACK_";
    }

    public CGLIBEnhancedConverter(Mapper mapper, ReflectionProvider reflectionProvider, ClassLoaderReference classLoaderReference) {
        super(mapper, new CGLIBFilteringReflectionProvider(reflectionProvider), classLoaderReference);
        this.fieldCache = new HashMap();
    }

    public CGLIBEnhancedConverter(Mapper mapper, ReflectionProvider reflectionProvider, ClassLoader classLoader) {
        super(mapper, new CGLIBFilteringReflectionProvider(reflectionProvider), classLoader);
        this.fieldCache = new HashMap();
    }

    public CGLIBEnhancedConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
        this(mapper, new CGLIBFilteringReflectionProvider(reflectionProvider), CGLIBEnhancedConverter.class.getClassLoader());
    }

    public boolean canConvert(Class type) {
        return (Enhancer.isEnhanced(type) && type.getName().indexOf(DEFAULT_NAMING_MARKER) > 0) || type == Marker.class;
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        int length;
        Class type = source.getClass();
        boolean hasFactory = Factory.class.isAssignableFrom(type);
        ExtendedHierarchicalStreamWriterHelper.startNode(writer, SharedPref.TYPE, type);
        context.convertAnother(type.getSuperclass());
        writer.endNode();
        writer.startNode("interfaces");
        Class[] interfaces = type.getInterfaces();
        int i = 0;
        while (true) {
            length = interfaces.length;
            if (i >= r0) {
                break;
            }
            if (interfaces[i] != Factory.class) {
                ExtendedHierarchicalStreamWriterHelper.startNode(writer, this.mapper.serializedClass(interfaces[i].getClass()), interfaces[i].getClass());
                context.convertAnother(interfaces[i]);
                writer.endNode();
            }
            i++;
        }
        writer.endNode();
        writer.startNode("hasFactory");
        writer.setValue(String.valueOf(hasFactory));
        writer.endNode();
        Callback[] callbacks = hasFactory ? ((Factory) source).getCallbacks() : getCallbacks(source);
        length = callbacks.length;
        if (r0 > 1) {
            if (hasFactory) {
                Map callbackIndexMap = createCallbackIndexMap((Factory) source);
                writer.startNode("callbacks");
                writer.startNode("mapping");
                context.convertAnother(callbackIndexMap);
                writer.endNode();
            } else {
                ConversionException exception = new ConversionException("Cannot handle CGLIB enhanced proxies without factory that have multiple callbacks");
                exception.add("proxy superclass", type.getSuperclass().getName());
                exception.add("number of callbacks", String.valueOf(callbacks.length));
                throw exception;
            }
        }
        boolean hasInterceptor = false;
        i = 0;
        while (true) {
            length = callbacks.length;
            if (i >= r0) {
                break;
            }
            Callback callback = callbacks[i];
            if (callback == null) {
                writer.startNode(this.mapper.serializedClass(null));
                writer.endNode();
            } else {
                hasInterceptor = hasInterceptor || MethodInterceptor.class.isAssignableFrom(callback.getClass());
                ExtendedHierarchicalStreamWriterHelper.startNode(writer, this.mapper.serializedClass(callback.getClass()), callback.getClass());
                context.convertAnother(callback);
                writer.endNode();
            }
            i++;
        }
        length = callbacks.length;
        if (r0 > 1) {
            writer.endNode();
        }
        try {
            Field field = type.getDeclaredField("serialVersionUID");
            field.setAccessible(true);
            long serialVersionUID = field.getLong(null);
            ExtendedHierarchicalStreamWriterHelper.startNode(writer, "serialVersionUID", String.class);
            writer.setValue(String.valueOf(serialVersionUID));
            writer.endNode();
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e2) {
            throw new ObjectAccessException("Access to serialVersionUID of " + type.getName() + " not allowed", e2);
        }
        if (hasInterceptor) {
            writer.startNode("instance");
            super.doMarshalConditionally(source, writer, context);
            writer.endNode();
        }
    }

    private Callback[] getCallbacks(Object source) {
        int i;
        Class type = source.getClass();
        List fields = (List) this.fieldCache.get(type.getName());
        if (fields == null) {
            fields = new ArrayList();
            this.fieldCache.put(type.getName(), fields);
            i = 0;
            while (true) {
                try {
                    Field field = type.getDeclaredField(CALLBACK_MARKER + i);
                    field.setAccessible(true);
                    fields.add(field);
                    i++;
                } catch (NoSuchFieldException e) {
                }
            }
        }
        List list = new ArrayList();
        i = 0;
        while (i < fields.size()) {
            try {
                list.add(((Field) fields.get(i)).get(source));
                i++;
            } catch (IllegalAccessException e2) {
                throw new ObjectAccessException("Access to " + type.getName() + "." + CALLBACK_MARKER + i + " not allowed", e2);
            }
        }
        return (Callback[]) list.toArray(new Callback[list.size()]);
    }

    private Map createCallbackIndexMap(Factory source) {
        Callback[] originalCallbacks = source.getCallbacks();
        Callback[] reverseEngineeringCallbacks = new Callback[originalCallbacks.length];
        Map callbackIndexMap = new HashMap();
        int idxNoOp = -1;
        int i = 0;
        while (true) {
            int length = originalCallbacks.length;
            if (i >= r0) {
                break;
            }
            Callback callback = originalCallbacks[i];
            if (callback == null) {
                reverseEngineeringCallbacks[i] = null;
            } else if (NoOp.class.isAssignableFrom(callback.getClass())) {
                reverseEngineeringCallbacks[i] = NoOp.INSTANCE;
                idxNoOp = i;
            } else {
                reverseEngineeringCallbacks[i] = createReverseEngineeredCallbackOfProperType(callback, i, callbackIndexMap);
            }
            i++;
        }
        source.setCallbacks(reverseEngineeringCallbacks);
        Set<Class> interfaces = new HashSet();
        Set<Object> methods = new HashSet();
        Class type = source.getClass();
        do {
            methods.addAll(Arrays.asList(type.getDeclaredMethods()));
            methods.addAll(Arrays.asList(type.getMethods()));
            interfaces.addAll(Arrays.asList(type.getInterfaces()));
            type = type.getSuperclass();
        } while (type != null);
        for (Class type2 : interfaces) {
            methods.addAll(Arrays.asList(type2.getDeclaredMethods()));
        }
        Iterator iter = methods.iterator();
        while (iter.hasNext()) {
            Method method = (Method) iter.next();
            method.setAccessible(true);
            if (Factory.class.isAssignableFrom(method.getDeclaringClass()) || (method.getModifiers() & 24) > 0) {
                iter.remove();
            } else {
                Class[] parameterTypes = method.getParameterTypes();
                Method calledMethod = method;
                try {
                    Object[] objArr;
                    if ((method.getModifiers() & Flags.FLAG5) > 0) {
                        calledMethod = source.getClass().getMethod(method.getName(), method.getParameterTypes());
                    }
                    callbackIndexMap.put(null, method);
                    if (parameterTypes == null) {
                        objArr = (Object[]) null;
                    } else {
                        objArr = createNullArguments(parameterTypes);
                    }
                    calledMethod.invoke(source, objArr);
                } catch (IllegalAccessException e) {
                    throw new ObjectAccessException("Access to " + calledMethod + " not allowed", e);
                } catch (InvocationTargetException e2) {
                } catch (NoSuchMethodException e3) {
                    ConversionException exception = new ConversionException("CGLIB enhanced proxies wit abstract nethod that has not been implemented");
                    exception.add("proxy superclass", type2.getSuperclass().getName());
                    exception.add("method", method.toString());
                    throw exception;
                } catch (Throwable th) {
                    source.setCallbacks(originalCallbacks);
                }
                if (callbackIndexMap.containsKey(method)) {
                    iter.remove();
                }
            }
        }
        if (idxNoOp >= 0) {
            Integer idx = new Integer(idxNoOp);
            for (Object put : methods) {
                callbackIndexMap.put(put, idx);
            }
        }
        source.setCallbacks(originalCallbacks);
        callbackIndexMap.remove(null);
        return callbackIndexMap;
    }

    private Object[] createNullArguments(Class[] parameterTypes) {
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < arguments.length; i++) {
            Class type = parameterTypes[i];
            if (type.isPrimitive()) {
                if (type == Byte.TYPE) {
                    arguments[i] = new Byte((byte) 0);
                } else if (type == Short.TYPE) {
                    arguments[i] = new Short((short) 0);
                } else if (type == Integer.TYPE) {
                    arguments[i] = new Integer(0);
                } else if (type == Long.TYPE) {
                    arguments[i] = new Long(0);
                } else if (type == Float.TYPE) {
                    arguments[i] = new Float(0.0f);
                } else if (type == Double.TYPE) {
                    arguments[i] = new Double(0.0d);
                } else if (type == Character.TYPE) {
                    arguments[i] = new Character('\u0000');
                } else {
                    arguments[i] = Boolean.FALSE;
                }
            }
        }
        return arguments;
    }

    private Callback createReverseEngineeredCallbackOfProperType(Callback callback, int index, Map callbackIndexMap) {
        Class iface = null;
        Class[] interfaces = callback.getClass().getInterfaces();
        int i = 0;
        while (i < interfaces.length) {
            if (Callback.class.isAssignableFrom(interfaces[i])) {
                iface = interfaces[i];
                if (iface == Callback.class) {
                    ConversionException exception = new ConversionException("Cannot handle CGLIB callback");
                    exception.add("CGLIB callback type", callback.getClass().getName());
                    throw exception;
                }
                interfaces = iface.getInterfaces();
                if (Arrays.asList(interfaces).contains(Callback.class)) {
                    break;
                }
                i = -1;
            }
            i++;
        }
        return (Callback) Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, new ReverseEngineeringInvocationHandler(index, callbackIndexMap));
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Enhancer enhancer = new Enhancer();
        reader.moveDown();
        enhancer.setSuperclass((Class) context.convertAnother(null, Class.class));
        reader.moveUp();
        reader.moveDown();
        List interfaces = new ArrayList();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            interfaces.add(context.convertAnother(null, this.mapper.realClass(reader.getNodeName())));
            reader.moveUp();
        }
        enhancer.setInterfaces((Class[]) interfaces.toArray(new Class[interfaces.size()]));
        reader.moveUp();
        reader.moveDown();
        boolean useFactory = Boolean.valueOf(reader.getValue()).booleanValue();
        enhancer.setUseFactory(useFactory);
        reader.moveUp();
        List callbacksToEnhance = new ArrayList();
        List callbacks = new ArrayList();
        Map callbackIndexMap = null;
        reader.moveDown();
        if ("callbacks".equals(reader.getNodeName())) {
            reader.moveDown();
            callbackIndexMap = (Map) context.convertAnother(null, HashMap.class);
            reader.moveUp();
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                readCallback(reader, context, callbacksToEnhance, callbacks);
                reader.moveUp();
            }
        } else {
            readCallback(reader, context, callbacksToEnhance, callbacks);
        }
        enhancer.setCallbacks((Callback[]) callbacksToEnhance.toArray(new Callback[callbacksToEnhance.size()]));
        if (callbackIndexMap != null) {
            enhancer.setCallbackFilter(new ReverseEngineeredCallbackFilter(callbackIndexMap));
        }
        reader.moveUp();
        Object obj = null;
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            if (reader.getNodeName().equals("serialVersionUID")) {
                enhancer.setSerialVersionUID(Long.valueOf(reader.getValue()));
            } else if (reader.getNodeName().equals("instance")) {
                obj = create(enhancer, callbacks, useFactory);
                super.doUnmarshalConditionally(obj, reader, context);
            }
            reader.moveUp();
        }
        if (obj == null) {
            obj = create(enhancer, callbacks, useFactory);
        }
        return this.serializationMethodInvoker.callReadResolve(obj);
    }

    private void readCallback(HierarchicalStreamReader reader, UnmarshallingContext context, List callbacksToEnhance, List callbacks) {
        Callback callback = (Callback) context.convertAnother(null, this.mapper.realClass(reader.getNodeName()));
        callbacks.add(callback);
        if (callback == null) {
            callbacksToEnhance.add(NoOp.INSTANCE);
        } else {
            callbacksToEnhance.add(callback);
        }
    }

    private Object create(Enhancer enhancer, List callbacks, boolean useFactory) {
        Object result = enhancer.create();
        if (useFactory) {
            ((Factory) result).setCallbacks((Callback[]) callbacks.toArray(new Callback[callbacks.size()]));
        }
        return result;
    }

    protected List hierarchyFor(Class type) {
        List typeHierarchy = super.hierarchyFor(type);
        typeHierarchy.remove(typeHierarchy.size() - 1);
        return typeHierarchy;
    }

    private Object readResolve() {
        this.fieldCache = new HashMap();
        return this;
    }
}
