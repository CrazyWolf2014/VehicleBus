package com.thoughtworks.xstream.converters.reflection;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider.Visitor;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.core.util.CustomObjectInputStream;
import com.thoughtworks.xstream.core.util.CustomObjectOutputStream;
import com.thoughtworks.xstream.core.util.CustomObjectOutputStream.StreamCallback;
import com.thoughtworks.xstream.core.util.HierarchicalStreams;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputValidation;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializableConverter extends AbstractReflectionConverter {
    private static final String ATTRIBUTE_CLASS = "class";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_SERIALIZATION = "serialization";
    private static final String ATTRIBUTE_VALUE_CUSTOM = "custom";
    private static final String ELEMENT_DEFAULT = "default";
    private static final String ELEMENT_FIELD = "field";
    private static final String ELEMENT_FIELDS = "fields";
    private static final String ELEMENT_NULL = "null";
    private static final String ELEMENT_UNSERIALIZABLE_PARENTS = "unserializable-parents";
    private final ClassLoaderReference classLoaderReference;

    /* renamed from: com.thoughtworks.xstream.converters.reflection.SerializableConverter.1 */
    class C11341 implements StreamCallback {
        final /* synthetic */ MarshallingContext val$context;
        final /* synthetic */ Class[] val$currentType;
        final /* synthetic */ Object val$source;
        final /* synthetic */ HierarchicalStreamWriter val$writer;
        final /* synthetic */ boolean[] val$writtenClassWrapper;

        C11341(HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext, Class[] clsArr, Object obj, boolean[] zArr) {
            this.val$writer = hierarchicalStreamWriter;
            this.val$context = marshallingContext;
            this.val$currentType = clsArr;
            this.val$source = obj;
            this.val$writtenClassWrapper = zArr;
        }

        public void writeToStream(Object object) {
            if (object == null) {
                this.val$writer.startNode(SerializableConverter.ELEMENT_NULL);
                this.val$writer.endNode();
                return;
            }
            ExtendedHierarchicalStreamWriterHelper.startNode(this.val$writer, SerializableConverter.this.mapper.serializedClass(object.getClass()), object.getClass());
            this.val$context.convertAnother(object);
            this.val$writer.endNode();
        }

        public void writeFieldsToStream(Map fields) {
            ObjectStreamClass objectStreamClass = ObjectStreamClass.lookup(this.val$currentType[0]);
            this.val$writer.startNode(SerializableConverter.ELEMENT_DEFAULT);
            for (String name : fields.keySet()) {
                if (SerializableConverter.this.mapper.shouldSerializeMember(this.val$currentType[0], name)) {
                    ObjectStreamField field = objectStreamClass.getField(name);
                    Object value = fields.get(name);
                    if (field == null) {
                        throw new ObjectAccessException("Class " + value.getClass().getName() + " may not write a field named '" + name + "'");
                    } else if (value != null) {
                        ExtendedHierarchicalStreamWriterHelper.startNode(this.val$writer, SerializableConverter.this.mapper.serializedMember(this.val$source.getClass(), name), value.getClass());
                        if (!(field.getType() == value.getClass() || field.getType().isPrimitive())) {
                            String attributeName = SerializableConverter.this.mapper.aliasForSystemAttribute(SerializableConverter.ATTRIBUTE_CLASS);
                            if (attributeName != null) {
                                this.val$writer.addAttribute(attributeName, SerializableConverter.this.mapper.serializedClass(value.getClass()));
                            }
                        }
                        this.val$context.convertAnother(value);
                        this.val$writer.endNode();
                    }
                }
            }
            this.val$writer.endNode();
        }

        public void defaultWriteObject() {
            boolean writtenDefaultFields = false;
            ObjectStreamClass objectStreamClass = ObjectStreamClass.lookup(this.val$currentType[0]);
            if (objectStreamClass != null) {
                ObjectStreamField[] fields = objectStreamClass.getFields();
                for (ObjectStreamField field : fields) {
                    Object value = SerializableConverter.this.readField(field, this.val$currentType[0], this.val$source);
                    if (value != null) {
                        if (!this.val$writtenClassWrapper[0]) {
                            this.val$writer.startNode(SerializableConverter.this.mapper.serializedClass(this.val$currentType[0]));
                            this.val$writtenClassWrapper[0] = true;
                        }
                        if (!writtenDefaultFields) {
                            this.val$writer.startNode(SerializableConverter.ELEMENT_DEFAULT);
                            writtenDefaultFields = true;
                        }
                        if (SerializableConverter.this.mapper.shouldSerializeMember(this.val$currentType[0], field.getName())) {
                            Class actualType = value.getClass();
                            ExtendedHierarchicalStreamWriterHelper.startNode(this.val$writer, SerializableConverter.this.mapper.serializedMember(this.val$source.getClass(), field.getName()), actualType);
                            if (!actualType.equals(SerializableConverter.this.mapper.defaultImplementationOf(field.getType()))) {
                                String attributeName = SerializableConverter.this.mapper.aliasForSystemAttribute(SerializableConverter.ATTRIBUTE_CLASS);
                                if (attributeName != null) {
                                    this.val$writer.addAttribute(attributeName, SerializableConverter.this.mapper.serializedClass(actualType));
                                }
                            }
                            this.val$context.convertAnother(value);
                            this.val$writer.endNode();
                        }
                    }
                }
                if (this.val$writtenClassWrapper[0] && !writtenDefaultFields) {
                    this.val$writer.startNode(SerializableConverter.ELEMENT_DEFAULT);
                    this.val$writer.endNode();
                } else if (writtenDefaultFields) {
                    this.val$writer.endNode();
                }
            }
        }

        public void flush() {
            this.val$writer.flush();
        }

        public void close() {
            throw new UnsupportedOperationException("Objects are not allowed to call ObjectOutputStream.close() from writeObject()");
        }
    }

    /* renamed from: com.thoughtworks.xstream.converters.reflection.SerializableConverter.2 */
    class C11352 implements CustomObjectInputStream.StreamCallback {
        final /* synthetic */ UnmarshallingContext val$context;
        final /* synthetic */ Class[] val$currentType;
        final /* synthetic */ HierarchicalStreamReader val$reader;
        final /* synthetic */ Object val$result;

        /* renamed from: com.thoughtworks.xstream.converters.reflection.SerializableConverter.2.1 */
        class C08881 implements Runnable {
            final /* synthetic */ ObjectInputValidation val$validation;

            C08881(ObjectInputValidation objectInputValidation) {
                this.val$validation = objectInputValidation;
            }

            public void run() {
                try {
                    this.val$validation.validateObject();
                } catch (InvalidObjectException e) {
                    throw new ObjectAccessException("Cannot validate object : " + e.getMessage(), e);
                }
            }
        }

        C11352(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext, Object obj, Class[] clsArr) {
            this.val$reader = hierarchicalStreamReader;
            this.val$context = unmarshallingContext;
            this.val$result = obj;
            this.val$currentType = clsArr;
        }

        public Object readFromStream() {
            this.val$reader.moveDown();
            Object value = this.val$context.convertAnother(this.val$result, HierarchicalStreams.readClassType(this.val$reader, SerializableConverter.this.mapper));
            this.val$reader.moveUp();
            return value;
        }

        public Map readFieldsFromStream() {
            Map fields = new HashMap();
            this.val$reader.moveDown();
            if (this.val$reader.getNodeName().equals(SerializableConverter.ELEMENT_FIELDS)) {
                while (this.val$reader.hasMoreChildren()) {
                    this.val$reader.moveDown();
                    if (this.val$reader.getNodeName().equals(SerializableConverter.ELEMENT_FIELD)) {
                        fields.put(this.val$reader.getAttribute(SerializableConverter.ATTRIBUTE_NAME), this.val$context.convertAnother(this.val$result, SerializableConverter.this.mapper.realClass(this.val$reader.getAttribute(SerializableConverter.ATTRIBUTE_CLASS))));
                        this.val$reader.moveUp();
                    } else {
                        throw new ConversionException("Expected <field/> element inside <field/>");
                    }
                }
            } else if (this.val$reader.getNodeName().equals(SerializableConverter.ELEMENT_DEFAULT)) {
                ObjectStreamClass objectStreamClass = ObjectStreamClass.lookup(this.val$currentType[0]);
                while (this.val$reader.hasMoreChildren()) {
                    this.val$reader.moveDown();
                    String name = SerializableConverter.this.mapper.realMember(this.val$currentType[0], this.val$reader.getNodeName());
                    if (SerializableConverter.this.mapper.shouldSerializeMember(this.val$currentType[0], name)) {
                        Class type;
                        String classAttribute = HierarchicalStreams.readClassAttribute(this.val$reader, SerializableConverter.this.mapper);
                        if (classAttribute != null) {
                            type = SerializableConverter.this.mapper.realClass(classAttribute);
                        } else {
                            ObjectStreamField field = objectStreamClass.getField(name);
                            if (field == null) {
                                throw new MissingFieldException(this.val$currentType[0].getName(), name);
                            }
                            type = field.getType();
                        }
                        fields.put(name, this.val$context.convertAnother(this.val$result, type));
                    }
                    this.val$reader.moveUp();
                }
            } else {
                throw new ConversionException("Expected <fields/> or <default/> element when calling ObjectInputStream.readFields()");
            }
            this.val$reader.moveUp();
            return fields;
        }

        public void defaultReadObject() {
            if (this.val$reader.hasMoreChildren()) {
                this.val$reader.moveDown();
                if (this.val$reader.getNodeName().equals(SerializableConverter.ELEMENT_DEFAULT)) {
                    while (this.val$reader.hasMoreChildren()) {
                        this.val$reader.moveDown();
                        String fieldName = SerializableConverter.this.mapper.realMember(this.val$currentType[0], this.val$reader.getNodeName());
                        if (SerializableConverter.this.mapper.shouldSerializeMember(this.val$currentType[0], fieldName)) {
                            Class type;
                            String classAttribute = HierarchicalStreams.readClassAttribute(this.val$reader, SerializableConverter.this.mapper);
                            if (classAttribute != null) {
                                type = SerializableConverter.this.mapper.realClass(classAttribute);
                            } else {
                                type = SerializableConverter.this.mapper.defaultImplementationOf(SerializableConverter.this.reflectionProvider.getFieldType(this.val$result, fieldName, this.val$currentType[0]));
                            }
                            SerializableConverter.this.reflectionProvider.writeField(this.val$result, fieldName, this.val$context.convertAnother(this.val$result, type), this.val$currentType[0]);
                        }
                        this.val$reader.moveUp();
                    }
                    this.val$reader.moveUp();
                    return;
                }
                throw new ConversionException("Expected <default/> element in readObject() stream");
            }
        }

        public void registerValidation(ObjectInputValidation validation, int priority) {
            this.val$context.addCompletionCallback(new C08881(validation), priority);
        }

        public void close() {
            throw new UnsupportedOperationException("Objects are not allowed to call ObjectInputStream.close() from readObject()");
        }
    }

    private static class UnserializableParentsReflectionProvider extends ReflectionProviderWrapper {

        /* renamed from: com.thoughtworks.xstream.converters.reflection.SerializableConverter.UnserializableParentsReflectionProvider.1 */
        class C11361 implements Visitor {
            final /* synthetic */ Visitor val$visitor;

            C11361(Visitor visitor) {
                this.val$visitor = visitor;
            }

            public void visit(String name, Class type, Class definedIn, Object value) {
                if (!Serializable.class.isAssignableFrom(definedIn)) {
                    this.val$visitor.visit(name, type, definedIn, value);
                }
            }
        }

        public UnserializableParentsReflectionProvider(ReflectionProvider reflectionProvider) {
            super(reflectionProvider);
        }

        public void visitSerializableFields(Object object, Visitor visitor) {
            this.wrapped.visitSerializableFields(object, new C11361(visitor));
        }
    }

    public SerializableConverter(Mapper mapper, ReflectionProvider reflectionProvider, ClassLoaderReference classLoaderReference) {
        super(mapper, new UnserializableParentsReflectionProvider(reflectionProvider));
        this.classLoaderReference = classLoaderReference;
    }

    public SerializableConverter(Mapper mapper, ReflectionProvider reflectionProvider, ClassLoader classLoader) {
        this(mapper, reflectionProvider, new ClassLoaderReference(classLoader));
    }

    public SerializableConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
        this(mapper, new UnserializableParentsReflectionProvider(reflectionProvider), new ClassLoaderReference(null));
    }

    public boolean canConvert(Class type) {
        return isSerializable(type);
    }

    private boolean isSerializable(Class type) {
        return Serializable.class.isAssignableFrom(type) && (this.serializationMethodInvoker.supportsReadObject(type, true) || this.serializationMethodInvoker.supportsWriteObject(type, true));
    }

    public void doMarshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        String attributeName = this.mapper.aliasForSystemAttribute(ATTRIBUTE_SERIALIZATION);
        if (attributeName != null) {
            writer.addAttribute(attributeName, ATTRIBUTE_VALUE_CUSTOM);
        }
        Class[] currentType = new Class[1];
        boolean[] writtenClassWrapper = new boolean[]{false};
        StreamCallback callback = new C11341(writer, context, currentType, source, writtenClassWrapper);
        boolean mustHandleUnserializableParent = false;
        try {
            for (Class cls : hierarchyFor(source.getClass())) {
                currentType[0] = cls;
                if (Serializable.class.isAssignableFrom(currentType[0])) {
                    if (mustHandleUnserializableParent) {
                        marshalUnserializableParent(writer, context, source);
                        mustHandleUnserializableParent = false;
                    }
                    String classAttributeName;
                    if (this.serializationMethodInvoker.supportsWriteObject(currentType[0], false)) {
                        writtenClassWrapper[0] = true;
                        writer.startNode(this.mapper.serializedClass(currentType[0]));
                        if (currentType[0] != this.mapper.defaultImplementationOf(currentType[0])) {
                            classAttributeName = this.mapper.aliasForSystemAttribute(ATTRIBUTE_CLASS);
                            if (classAttributeName != null) {
                                writer.addAttribute(classAttributeName, currentType[0].getName());
                            }
                        }
                        ObjectOutputStream objectOutputStream = CustomObjectOutputStream.getInstance(context, callback);
                        this.serializationMethodInvoker.callWriteObject(currentType[0], source, objectOutputStream);
                        objectOutputStream.popCallback();
                        writer.endNode();
                    } else if (this.serializationMethodInvoker.supportsReadObject(currentType[0], false)) {
                        writtenClassWrapper[0] = true;
                        writer.startNode(this.mapper.serializedClass(currentType[0]));
                        if (currentType[0] != this.mapper.defaultImplementationOf(currentType[0])) {
                            classAttributeName = this.mapper.aliasForSystemAttribute(ATTRIBUTE_CLASS);
                            if (classAttributeName != null) {
                                writer.addAttribute(classAttributeName, currentType[0].getName());
                            }
                        }
                        callback.defaultWriteObject();
                        writer.endNode();
                    } else {
                        writtenClassWrapper[0] = false;
                        callback.defaultWriteObject();
                        if (writtenClassWrapper[0]) {
                            writer.endNode();
                        }
                    }
                } else {
                    mustHandleUnserializableParent = true;
                }
            }
        } catch (IOException e) {
            throw new ObjectAccessException("Could not call defaultWriteObject()", e);
        }
    }

    protected void marshalUnserializableParent(HierarchicalStreamWriter writer, MarshallingContext context, Object replacedSource) {
        writer.startNode(ELEMENT_UNSERIALIZABLE_PARENTS);
        super.doMarshal(replacedSource, writer, context);
        writer.endNode();
    }

    private Object readField(ObjectStreamField field, Class type, Object instance) {
        try {
            Field javaField = type.getDeclaredField(field.getName());
            javaField.setAccessible(true);
            return javaField.get(instance);
        } catch (IllegalArgumentException e) {
            throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e);
        } catch (IllegalAccessException e2) {
            throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e2);
        } catch (NoSuchFieldException e3) {
            throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e3);
        } catch (SecurityException e4) {
            throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e4);
        }
    }

    protected List hierarchyFor(Class type) {
        List result = new ArrayList();
        while (type != Object.class) {
            result.add(type);
            type = type.getSuperclass();
        }
        Collections.reverse(result);
        return result;
    }

    public Object doUnmarshal(Object result, HierarchicalStreamReader reader, UnmarshallingContext context) {
        Class[] currentType = new Class[1];
        String attributeName = this.mapper.aliasForSystemAttribute(ATTRIBUTE_SERIALIZATION);
        if (attributeName == null || ATTRIBUTE_VALUE_CUSTOM.equals(reader.getAttribute(attributeName))) {
            CustomObjectInputStream.StreamCallback callback = new C11352(reader, context, result, currentType);
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                String nodeName = reader.getNodeName();
                if (nodeName.equals(ELEMENT_UNSERIALIZABLE_PARENTS)) {
                    super.doUnmarshal(result, reader, context);
                } else {
                    String classAttribute = HierarchicalStreams.readClassAttribute(reader, this.mapper);
                    if (classAttribute == null) {
                        currentType[0] = this.mapper.defaultImplementationOf(this.mapper.realClass(nodeName));
                    } else {
                        currentType[0] = this.mapper.realClass(classAttribute);
                    }
                    if (this.serializationMethodInvoker.supportsReadObject(currentType[0], false)) {
                        CustomObjectInputStream objectInputStream = CustomObjectInputStream.getInstance((DataHolder) context, callback, this.classLoaderReference);
                        this.serializationMethodInvoker.callReadObject(currentType[0], result, objectInputStream);
                        objectInputStream.popCallback();
                    } else {
                        try {
                            callback.defaultReadObject();
                        } catch (IOException e) {
                            throw new ObjectAccessException("Could not call defaultWriteObject()", e);
                        }
                    }
                }
                reader.moveUp();
            }
            return result;
        }
        throw new ConversionException("Cannot deserialize object with new readObject()/writeObject() methods");
    }

    protected void doMarshalConditionally(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        if (isSerializable(source.getClass())) {
            doMarshal(source, writer, context);
        } else {
            super.doMarshal(source, writer, context);
        }
    }

    protected Object doUnmarshalConditionally(Object result, HierarchicalStreamReader reader, UnmarshallingContext context) {
        return isSerializable(result.getClass()) ? doUnmarshal(result, reader, context) : super.doUnmarshal(result, reader, context);
    }
}
