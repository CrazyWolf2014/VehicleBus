package com.thoughtworks.xstream.converters.reflection;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.core.Caching;
import com.thoughtworks.xstream.core.util.FastField;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SerializationMethodInvoker implements Caching {
    private static final Object[] EMPTY_ARGS;
    private static final Method NO_METHOD;
    private static final FastField[] OBJECT_TYPE_FIELDS;
    private Map cache;

    /* renamed from: com.thoughtworks.xstream.converters.reflection.SerializationMethodInvoker.1 */
    static class C08891 {
        C08891() {
        }

        private void noMethod() {
        }
    }

    public SerializationMethodInvoker() {
        this.cache = Collections.synchronizedMap(new HashMap());
        for (Object put : OBJECT_TYPE_FIELDS) {
            this.cache.put(put, NO_METHOD);
        }
    }

    static {
        NO_METHOD = new C08891().getClass().getDeclaredMethods()[0];
        EMPTY_ARGS = new Object[0];
        OBJECT_TYPE_FIELDS = new FastField[]{new FastField(Object.class, "readResolve"), new FastField(Object.class, "writeReplace"), new FastField(Object.class, "readObject"), new FastField(Object.class, "writeObject")};
    }

    public Object callReadResolve(Object result) {
        if (result == null) {
            return null;
        }
        Method readResolveMethod = getMethod(result.getClass(), "readResolve", null, true);
        if (readResolveMethod == null) {
            return result;
        }
        try {
            return readResolveMethod.invoke(result, EMPTY_ARGS);
        } catch (IllegalAccessException e) {
            throw new ObjectAccessException("Could not call " + result.getClass().getName() + ".readResolve()", e);
        } catch (InvocationTargetException e2) {
            throw new ObjectAccessException("Could not call " + result.getClass().getName() + ".readResolve()", e2.getTargetException());
        }
    }

    public Object callWriteReplace(Object object) {
        if (object == null) {
            return null;
        }
        Method writeReplaceMethod = getMethod(object.getClass(), "writeReplace", null, true);
        if (writeReplaceMethod == null) {
            return object;
        }
        try {
            return writeReplaceMethod.invoke(object, EMPTY_ARGS);
        } catch (IllegalAccessException e) {
            throw new ObjectAccessException("Could not call " + object.getClass().getName() + ".writeReplace()", e);
        } catch (InvocationTargetException e2) {
            throw new ObjectAccessException("Could not call " + object.getClass().getName() + ".writeReplace()", e2.getTargetException());
        }
    }

    public boolean supportsReadObject(Class type, boolean includeBaseClasses) {
        return getMethod(type, "readObject", new Class[]{ObjectInputStream.class}, includeBaseClasses) != null;
    }

    public void callReadObject(Class type, Object object, ObjectInputStream stream) {
        try {
            getMethod(type, "readObject", new Class[]{ObjectInputStream.class}, false).invoke(object, new Object[]{stream});
        } catch (IllegalAccessException e) {
            throw new ConversionException("Could not call " + object.getClass().getName() + ".readObject()", e);
        } catch (InvocationTargetException e2) {
            throw new ConversionException("Could not call " + object.getClass().getName() + ".readObject()", e2.getTargetException());
        }
    }

    public boolean supportsWriteObject(Class type, boolean includeBaseClasses) {
        return getMethod(type, "writeObject", new Class[]{ObjectOutputStream.class}, includeBaseClasses) != null;
    }

    public void callWriteObject(Class type, Object instance, ObjectOutputStream stream) {
        try {
            getMethod(type, "writeObject", new Class[]{ObjectOutputStream.class}, false).invoke(instance, new Object[]{stream});
        } catch (IllegalAccessException e) {
            throw new ConversionException("Could not call " + instance.getClass().getName() + ".writeObject()", e);
        } catch (InvocationTargetException e2) {
            throw new ConversionException("Could not call " + instance.getClass().getName() + ".writeObject()", e2.getTargetException());
        }
    }

    private Method getMethod(Class type, String name, Class[] parameterTypes, boolean includeBaseclasses) {
        Method method = getMethod(type, name, parameterTypes);
        return (method == NO_METHOD || !(includeBaseclasses || method.getDeclaringClass().equals(type))) ? null : method;
    }

    private Method getMethod(Class type, String name, Class[] parameterTypes) {
        FastField method = new FastField(type, name);
        Method result = (Method) this.cache.get(method);
        if (result == null) {
            try {
                result = type.getDeclaredMethod(name, parameterTypes);
                if (!result.isAccessible()) {
                    result.setAccessible(true);
                }
            } catch (NoSuchMethodException e) {
                result = getMethod(type.getSuperclass(), name, parameterTypes);
            }
            this.cache.put(method, result);
        }
        return result;
    }

    public void flushCache() {
        this.cache.keySet().retainAll(Arrays.asList(OBJECT_TYPE_FIELDS));
    }
}
