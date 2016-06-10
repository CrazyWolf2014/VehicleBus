package com.thoughtworks.xstream.converters.reflection;

import com.thoughtworks.xstream.converters.reflection.ReflectionProvider.Visitor;
import com.thoughtworks.xstream.core.JVM;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.ksoap2.SoapEnvelope;
import org.xbill.DNS.WKSRecord.Service;

public class PureJavaReflectionProvider implements ReflectionProvider {
    protected FieldDictionary fieldDictionary;
    private transient Map serializedDataCache;

    /* renamed from: com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider.1 */
    class C08871 extends ObjectInputStream {
        final /* synthetic */ Class val$type;

        C08871(InputStream x0, Class cls) {
            this.val$type = cls;
            super(x0);
        }

        protected Class resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            return Class.forName(desc.getName(), false, this.val$type.getClassLoader());
        }
    }

    public PureJavaReflectionProvider() {
        this(new FieldDictionary(new ImmutableFieldKeySorter()));
    }

    public PureJavaReflectionProvider(FieldDictionary fieldDictionary) {
        this.fieldDictionary = fieldDictionary;
        init();
    }

    public Object newInstance(Class type) {
        try {
            Constructor[] constructors = type.getDeclaredConstructors();
            for (Constructor constructor : constructors) {
                if (constructor.getParameterTypes().length == 0) {
                    if (!constructor.isAccessible()) {
                        constructor.setAccessible(true);
                    }
                    return constructor.newInstance(new Object[0]);
                }
            }
            if (Serializable.class.isAssignableFrom(type)) {
                return instantiateUsingSerialization(type);
            }
            throw new ObjectAccessException("Cannot construct " + type.getName() + " as it does not have a no-args constructor");
        } catch (InstantiationException e) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), e);
        } catch (IllegalAccessException e2) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), e2);
        } catch (InvocationTargetException e3) {
            if (e3.getTargetException() instanceof RuntimeException) {
                throw ((RuntimeException) e3.getTargetException());
            } else if (e3.getTargetException() instanceof Error) {
                throw ((Error) e3.getTargetException());
            } else {
                throw new ObjectAccessException("Constructor for " + type.getName() + " threw an exception", e3.getTargetException());
            }
        }
    }

    private Object instantiateUsingSerialization(Class type) {
        try {
            Object readObject;
            synchronized (this.serializedDataCache) {
                byte[] data = (byte[]) this.serializedDataCache.get(type);
                if (data == null) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    DataOutputStream stream = new DataOutputStream(bytes);
                    stream.writeShort(-21267);
                    stream.writeShort(5);
                    stream.writeByte(Service.SFTP);
                    stream.writeByte(Opcodes.FREM);
                    stream.writeUTF(type.getName());
                    stream.writeLong(ObjectStreamClass.lookup(type).getSerialVersionUID());
                    stream.writeByte(2);
                    stream.writeShort(0);
                    stream.writeByte(SoapEnvelope.VER12);
                    stream.writeByte(Opcodes.IREM);
                    data = bytes.toByteArray();
                    this.serializedDataCache.put(type, data);
                }
                readObject = new C08871(new ByteArrayInputStream(data), type).readObject();
            }
            return readObject;
        } catch (IOException e) {
            throw new ObjectAccessException("Cannot create " + type.getName() + " by JDK serialization", e);
        } catch (ClassNotFoundException e2) {
            throw new ObjectAccessException("Cannot find class " + e2.getMessage(), e2);
        }
    }

    public void visitSerializableFields(Object object, Visitor visitor) {
        Iterator iterator = this.fieldDictionary.fieldsFor(object.getClass());
        while (iterator.hasNext()) {
            Field field = (Field) iterator.next();
            if (fieldModifiersSupported(field)) {
                validateFieldAccess(field);
                try {
                    visitor.visit(field.getName(), field.getType(), field.getDeclaringClass(), field.get(object));
                } catch (IllegalArgumentException e) {
                    throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e);
                } catch (IllegalAccessException e2) {
                    throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e2);
                }
            }
        }
    }

    public void writeField(Object object, String fieldName, Object value, Class definedIn) {
        Field field = this.fieldDictionary.field(object.getClass(), fieldName, definedIn);
        validateFieldAccess(field);
        try {
            field.set(object, value);
        } catch (IllegalArgumentException e) {
            throw new ObjectAccessException("Could not set field " + object.getClass() + "." + field.getName(), e);
        } catch (IllegalAccessException e2) {
            throw new ObjectAccessException("Could not set field " + object.getClass() + "." + field.getName(), e2);
        }
    }

    public Class getFieldType(Object object, String fieldName, Class definedIn) {
        return this.fieldDictionary.field(object.getClass(), fieldName, definedIn).getType();
    }

    public boolean fieldDefinedInClass(String fieldName, Class type) {
        Field field = this.fieldDictionary.fieldOrNull(type, fieldName, null);
        return field != null && fieldModifiersSupported(field);
    }

    protected boolean fieldModifiersSupported(Field field) {
        int modifiers = field.getModifiers();
        return (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) ? false : true;
    }

    protected void validateFieldAccess(Field field) {
        if (!Modifier.isFinal(field.getModifiers())) {
            return;
        }
        if (JVM.is15()) {
            field.setAccessible(true);
            return;
        }
        throw new ObjectAccessException("Invalid final field " + field.getDeclaringClass().getName() + "." + field.getName());
    }

    public Field getField(Class definedIn, String fieldName) {
        return this.fieldDictionary.field(definedIn, fieldName, null);
    }

    public Field getFieldOrNull(Class definedIn, String fieldName) {
        return this.fieldDictionary.fieldOrNull(definedIn, fieldName, null);
    }

    public void setFieldDictionary(FieldDictionary dictionary) {
        this.fieldDictionary = dictionary;
    }

    private Object readResolve() {
        init();
        return this;
    }

    protected void init() {
        this.serializedDataCache = new WeakHashMap();
    }
}
