package com.thoughtworks.xstream.converters.reflection;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.core.util.CustomObjectInputStream;
import com.thoughtworks.xstream.core.util.CustomObjectOutputStream;
import com.thoughtworks.xstream.core.util.CustomObjectOutputStream.StreamCallback;
import com.thoughtworks.xstream.core.util.HierarchicalStreams;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.io.Externalizable;
import java.io.IOException;
import java.io.NotActiveException;
import java.io.ObjectInputValidation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ExternalizableConverter implements Converter {
    private final ClassLoaderReference classLoaderReference;
    private Mapper mapper;

    /* renamed from: com.thoughtworks.xstream.converters.reflection.ExternalizableConverter.1 */
    class C11321 implements StreamCallback {
        final /* synthetic */ MarshallingContext val$context;
        final /* synthetic */ HierarchicalStreamWriter val$writer;

        C11321(HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
            this.val$writer = hierarchicalStreamWriter;
            this.val$context = marshallingContext;
        }

        public void writeToStream(Object object) {
            if (object == null) {
                this.val$writer.startNode("null");
                this.val$writer.endNode();
                return;
            }
            ExtendedHierarchicalStreamWriterHelper.startNode(this.val$writer, ExternalizableConverter.this.mapper.serializedClass(object.getClass()), object.getClass());
            this.val$context.convertAnother(object);
            this.val$writer.endNode();
        }

        public void writeFieldsToStream(Map fields) {
            throw new UnsupportedOperationException();
        }

        public void defaultWriteObject() {
            throw new UnsupportedOperationException();
        }

        public void flush() {
            this.val$writer.flush();
        }

        public void close() {
            throw new UnsupportedOperationException("Objects are not allowed to call ObjectOutput.close() from writeExternal()");
        }
    }

    /* renamed from: com.thoughtworks.xstream.converters.reflection.ExternalizableConverter.2 */
    class C11332 implements CustomObjectInputStream.StreamCallback {
        final /* synthetic */ UnmarshallingContext val$context;
        final /* synthetic */ Externalizable val$externalizable;
        final /* synthetic */ HierarchicalStreamReader val$reader;

        C11332(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext, Externalizable externalizable) {
            this.val$reader = hierarchicalStreamReader;
            this.val$context = unmarshallingContext;
            this.val$externalizable = externalizable;
        }

        public Object readFromStream() {
            this.val$reader.moveDown();
            Object streamItem = this.val$context.convertAnother(this.val$externalizable, HierarchicalStreams.readClassType(this.val$reader, ExternalizableConverter.this.mapper));
            this.val$reader.moveUp();
            return streamItem;
        }

        public Map readFieldsFromStream() {
            throw new UnsupportedOperationException();
        }

        public void defaultReadObject() {
            throw new UnsupportedOperationException();
        }

        public void registerValidation(ObjectInputValidation validation, int priority) throws NotActiveException {
            throw new NotActiveException("stream inactive");
        }

        public void close() {
            throw new UnsupportedOperationException("Objects are not allowed to call ObjectInput.close() from readExternal()");
        }
    }

    public ExternalizableConverter(Mapper mapper, ClassLoaderReference classLoaderReference) {
        this.mapper = mapper;
        this.classLoaderReference = classLoaderReference;
    }

    public ExternalizableConverter(Mapper mapper, ClassLoader classLoader) {
        this(mapper, new ClassLoaderReference(classLoader));
    }

    public ExternalizableConverter(Mapper mapper) {
        this(mapper, ExternalizableConverter.class.getClassLoader());
    }

    public boolean canConvert(Class type) {
        return Externalizable.class.isAssignableFrom(type);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        try {
            Externalizable externalizable = (Externalizable) source;
            CustomObjectOutputStream objectOutput = CustomObjectOutputStream.getInstance(context, new C11321(writer, context));
            externalizable.writeExternal(objectOutput);
            objectOutput.popCallback();
        } catch (IOException e) {
            throw new ConversionException("Cannot serialize " + source.getClass().getName() + " using Externalization", e);
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Class type = context.getRequiredType();
        try {
            Constructor defaultConstructor = type.getDeclaredConstructor((Class[]) null);
            if (!defaultConstructor.isAccessible()) {
                defaultConstructor.setAccessible(true);
            }
            Externalizable externalizable = (Externalizable) defaultConstructor.newInstance((Object[]) null);
            CustomObjectInputStream objectInput = CustomObjectInputStream.getInstance((DataHolder) context, new C11332(reader, context, externalizable), this.classLoaderReference);
            externalizable.readExternal(objectInput);
            objectInput.popCallback();
            return externalizable;
        } catch (NoSuchMethodException e) {
            throw new ConversionException("Cannot construct " + type.getClass() + ", missing default constructor", e);
        } catch (InvocationTargetException e2) {
            throw new ConversionException("Cannot construct " + type.getClass(), e2);
        } catch (InstantiationException e3) {
            throw new ConversionException("Cannot construct " + type.getClass(), e3);
        } catch (IllegalAccessException e4) {
            throw new ConversionException("Cannot construct " + type.getClass(), e4);
        } catch (IOException e5) {
            throw new ConversionException("Cannot externalize " + type.getClass(), e5);
        } catch (ClassNotFoundException e6) {
            throw new ConversionException("Cannot externalize " + type.getClass(), e6);
        }
    }
}
