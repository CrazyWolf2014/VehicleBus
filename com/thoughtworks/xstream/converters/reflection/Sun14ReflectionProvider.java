package com.thoughtworks.xstream.converters.reflection;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;
import sun.misc.Unsafe;

public class Sun14ReflectionProvider extends PureJavaReflectionProvider {
    private static final Exception exception;
    private static final Unsafe unsafe;
    private transient Map fieldOffsetCache;

    static {
        Unsafe u = null;
        Exception ex = null;
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            u = (Unsafe) unsafeField.get(null);
        } catch (Exception e) {
            ex = e;
        } catch (Exception e2) {
            ex = e2;
        } catch (Exception e22) {
            ex = e22;
        } catch (Exception e222) {
            ex = e222;
        }
        exception = ex;
        unsafe = u;
    }

    public Sun14ReflectionProvider(FieldDictionary dic) {
        super(dic);
    }

    public Object newInstance(Class type) {
        if (exception != null) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), exception);
        }
        try {
            return unsafe.allocateInstance(type);
        } catch (SecurityException e) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), e);
        } catch (InstantiationException e2) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), e2);
        } catch (IllegalArgumentException e3) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), e3);
        }
    }

    public void writeField(Object object, String fieldName, Object value, Class definedIn) {
        write(this.fieldDictionary.field(object.getClass(), fieldName, definedIn), object, value);
    }

    private void write(Field field, Object object, Object value) {
        if (exception != null) {
            throw new ObjectAccessException("Could not set field " + object.getClass() + "." + field.getName(), exception);
        }
        try {
            long offset = getFieldOffset(field);
            Class type = field.getType();
            if (!type.isPrimitive()) {
                unsafe.putObject(object, offset, value);
            } else if (type.equals(Integer.TYPE)) {
                unsafe.putInt(object, offset, ((Integer) value).intValue());
            } else if (type.equals(Long.TYPE)) {
                unsafe.putLong(object, offset, ((Long) value).longValue());
            } else if (type.equals(Short.TYPE)) {
                unsafe.putShort(object, offset, ((Short) value).shortValue());
            } else if (type.equals(Character.TYPE)) {
                unsafe.putChar(object, offset, ((Character) value).charValue());
            } else if (type.equals(Byte.TYPE)) {
                unsafe.putByte(object, offset, ((Byte) value).byteValue());
            } else if (type.equals(Float.TYPE)) {
                unsafe.putFloat(object, offset, ((Float) value).floatValue());
            } else if (type.equals(Double.TYPE)) {
                unsafe.putDouble(object, offset, ((Double) value).doubleValue());
            } else if (type.equals(Boolean.TYPE)) {
                unsafe.putBoolean(object, offset, ((Boolean) value).booleanValue());
            } else {
                throw new ObjectAccessException("Could not set field " + object.getClass() + "." + field.getName() + ": Unknown type " + type);
            }
        } catch (IllegalArgumentException e) {
            throw new ObjectAccessException("Could not set field " + object.getClass() + "." + field.getName(), e);
        }
    }

    private synchronized long getFieldOffset(Field f) {
        Long l;
        l = (Long) this.fieldOffsetCache.get(f);
        if (l == null) {
            l = new Long(unsafe.objectFieldOffset(f));
            this.fieldOffsetCache.put(f, l);
        }
        return l.longValue();
    }

    protected void validateFieldAccess(Field field) {
    }

    private Object readResolve() {
        init();
        return this;
    }

    protected void init() {
        super.init();
        this.fieldOffsetCache = new WeakHashMap();
    }
}
