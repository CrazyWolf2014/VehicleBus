package com.google.protobuf;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.FieldSet.FieldDescriptorLite;
import com.google.protobuf.GeneratedMessageLite.C0227b;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public abstract class GeneratedMessage extends AbstractMessage implements Serializable {
    protected static boolean alwaysUseFieldBuilders = false;
    private static final long serialVersionUID = 1;
    private final UnknownFieldSet unknownFields;

    /* renamed from: com.google.protobuf.GeneratedMessage.2 */
    static /* synthetic */ class C02232 {
        static final /* synthetic */ int[] f876a;

        static {
            f876a = new int[JavaType.values().length];
            try {
                f876a[JavaType.MESSAGE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f876a[JavaType.ENUM.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    protected interface BuilderParent {
        void markDirty();
    }

    public static final class FieldAccessorTable {
        private final Descriptor descriptor;
        private final C0224a[] fields;

        /* renamed from: com.google.protobuf.GeneratedMessage.FieldAccessorTable.a */
        private interface C0224a {
            com.google.protobuf.Message.Builder m968a();

            Object m969a(Builder builder);

            Object m970a(Builder builder, int i);

            Object m971a(GeneratedMessage generatedMessage);

            Object m972a(GeneratedMessage generatedMessage, int i);

            void m973a(Builder builder, int i, Object obj);

            void m974a(Builder builder, Object obj);

            void m975b(Builder builder, Object obj);

            boolean m976b(Builder builder);

            boolean m977b(GeneratedMessage generatedMessage);

            int m978c(Builder builder);

            int m979c(GeneratedMessage generatedMessage);

            void m980d(Builder builder);
        }

        /* renamed from: com.google.protobuf.GeneratedMessage.FieldAccessorTable.c */
        private static class C1059c implements C0224a {
            protected final Class f1944a;
            protected final Method f1945b;
            protected final Method f1946c;
            protected final Method f1947d;
            protected final Method f1948e;
            protected final Method f1949f;
            protected final Method f1950g;
            protected final Method f1951h;
            protected final Method f1952i;
            protected final Method f1953j;

            C1059c(FieldDescriptor fieldDescriptor, String str, Class<? extends GeneratedMessage> cls, Class<? extends Builder> cls2) {
                this.f1945b = GeneratedMessage.getMethodOrDie(cls, "get" + str + "List", new Class[0]);
                this.f1946c = GeneratedMessage.getMethodOrDie(cls2, "get" + str + "List", new Class[0]);
                this.f1947d = GeneratedMessage.getMethodOrDie(cls, "get" + str, Integer.TYPE);
                this.f1948e = GeneratedMessage.getMethodOrDie(cls2, "get" + str, Integer.TYPE);
                this.f1944a = this.f1947d.getReturnType();
                this.f1949f = GeneratedMessage.getMethodOrDie(cls2, "set" + str, Integer.TYPE, this.f1944a);
                this.f1950g = GeneratedMessage.getMethodOrDie(cls2, "add" + str, this.f1944a);
                this.f1951h = GeneratedMessage.getMethodOrDie(cls, "get" + str + "Count", new Class[0]);
                this.f1952i = GeneratedMessage.getMethodOrDie(cls2, "get" + str + "Count", new Class[0]);
                this.f1953j = GeneratedMessage.getMethodOrDie(cls2, "clear" + str, new Class[0]);
            }

            public Object m2032a(GeneratedMessage generatedMessage) {
                return GeneratedMessage.invokeOrDie(this.f1945b, generatedMessage, new Object[0]);
            }

            public Object m2030a(Builder builder) {
                return GeneratedMessage.invokeOrDie(this.f1946c, builder, new Object[0]);
            }

            public void m2035a(Builder builder, Object obj) {
                m2041d(builder);
                for (Object b : (List) obj) {
                    m2036b(builder, b);
                }
            }

            public Object m2033a(GeneratedMessage generatedMessage, int i) {
                return GeneratedMessage.invokeOrDie(this.f1947d, generatedMessage, Integer.valueOf(i));
            }

            public Object m2031a(Builder builder, int i) {
                return GeneratedMessage.invokeOrDie(this.f1948e, builder, Integer.valueOf(i));
            }

            public void m2034a(Builder builder, int i, Object obj) {
                GeneratedMessage.invokeOrDie(this.f1949f, builder, Integer.valueOf(i), obj);
            }

            public void m2036b(Builder builder, Object obj) {
                GeneratedMessage.invokeOrDie(this.f1950g, builder, obj);
            }

            public boolean m2038b(GeneratedMessage generatedMessage) {
                throw new UnsupportedOperationException("hasField() called on a singular field.");
            }

            public boolean m2037b(Builder builder) {
                throw new UnsupportedOperationException("hasField() called on a singular field.");
            }

            public int m2040c(GeneratedMessage generatedMessage) {
                return ((Integer) GeneratedMessage.invokeOrDie(this.f1951h, generatedMessage, new Object[0])).intValue();
            }

            public int m2039c(Builder builder) {
                return ((Integer) GeneratedMessage.invokeOrDie(this.f1952i, builder, new Object[0])).intValue();
            }

            public void m2041d(Builder builder) {
                GeneratedMessage.invokeOrDie(this.f1953j, builder, new Object[0]);
            }

            public com.google.protobuf.Message.Builder m2029a() {
                throw new UnsupportedOperationException("newBuilderForField() called on a non-Message type.");
            }
        }

        /* renamed from: com.google.protobuf.GeneratedMessage.FieldAccessorTable.f */
        private static class C1060f implements C0224a {
            protected final Class<?> f1954a;
            protected final Method f1955b;
            protected final Method f1956c;
            protected final Method f1957d;
            protected final Method f1958e;
            protected final Method f1959f;
            protected final Method f1960g;

            C1060f(FieldDescriptor fieldDescriptor, String str, Class<? extends GeneratedMessage> cls, Class<? extends Builder> cls2) {
                this.f1955b = GeneratedMessage.getMethodOrDie(cls, "get" + str, new Class[0]);
                this.f1956c = GeneratedMessage.getMethodOrDie(cls2, "get" + str, new Class[0]);
                this.f1954a = this.f1955b.getReturnType();
                this.f1957d = GeneratedMessage.getMethodOrDie(cls2, "set" + str, this.f1954a);
                this.f1958e = GeneratedMessage.getMethodOrDie(cls, "has" + str, new Class[0]);
                this.f1959f = GeneratedMessage.getMethodOrDie(cls2, "has" + str, new Class[0]);
                this.f1960g = GeneratedMessage.getMethodOrDie(cls2, "clear" + str, new Class[0]);
            }

            public Object m2045a(GeneratedMessage generatedMessage) {
                return GeneratedMessage.invokeOrDie(this.f1955b, generatedMessage, new Object[0]);
            }

            public Object m2043a(Builder builder) {
                return GeneratedMessage.invokeOrDie(this.f1956c, builder, new Object[0]);
            }

            public void m2048a(Builder builder, Object obj) {
                GeneratedMessage.invokeOrDie(this.f1957d, builder, obj);
            }

            public Object m2046a(GeneratedMessage generatedMessage, int i) {
                throw new UnsupportedOperationException("getRepeatedField() called on a singular field.");
            }

            public Object m2044a(Builder builder, int i) {
                throw new UnsupportedOperationException("getRepeatedField() called on a singular field.");
            }

            public void m2047a(Builder builder, int i, Object obj) {
                throw new UnsupportedOperationException("setRepeatedField() called on a singular field.");
            }

            public void m2049b(Builder builder, Object obj) {
                throw new UnsupportedOperationException("addRepeatedField() called on a singular field.");
            }

            public boolean m2051b(GeneratedMessage generatedMessage) {
                return ((Boolean) GeneratedMessage.invokeOrDie(this.f1958e, generatedMessage, new Object[0])).booleanValue();
            }

            public boolean m2050b(Builder builder) {
                return ((Boolean) GeneratedMessage.invokeOrDie(this.f1959f, builder, new Object[0])).booleanValue();
            }

            public int m2053c(GeneratedMessage generatedMessage) {
                throw new UnsupportedOperationException("getRepeatedFieldSize() called on a singular field.");
            }

            public int m2052c(Builder builder) {
                throw new UnsupportedOperationException("getRepeatedFieldSize() called on a singular field.");
            }

            public void m2054d(Builder builder) {
                GeneratedMessage.invokeOrDie(this.f1960g, builder, new Object[0]);
            }

            public com.google.protobuf.Message.Builder m2042a() {
                throw new UnsupportedOperationException("newBuilderForField() called on a non-Message type.");
            }
        }

        /* renamed from: com.google.protobuf.GeneratedMessage.FieldAccessorTable.b */
        private static final class C1271b extends C1059c {
            private final Method f2375k;
            private final Method f2376l;

            C1271b(FieldDescriptor fieldDescriptor, String str, Class<? extends GeneratedMessage> cls, Class<? extends Builder> cls2) {
                super(fieldDescriptor, str, cls, cls2);
                this.f2375k = GeneratedMessage.getMethodOrDie(this.a, "valueOf", EnumValueDescriptor.class);
                this.f2376l = GeneratedMessage.getMethodOrDie(this.a, "getValueDescriptor", new Class[0]);
            }

            public Object m2549a(GeneratedMessage generatedMessage) {
                List arrayList = new ArrayList();
                for (Object access$1400 : (List) super.m2032a(generatedMessage)) {
                    arrayList.add(GeneratedMessage.invokeOrDie(this.f2376l, access$1400, new Object[0]));
                }
                return Collections.unmodifiableList(arrayList);
            }

            public Object m2547a(Builder builder) {
                List arrayList = new ArrayList();
                for (Object access$1400 : (List) super.m2030a(builder)) {
                    arrayList.add(GeneratedMessage.invokeOrDie(this.f2376l, access$1400, new Object[0]));
                }
                return Collections.unmodifiableList(arrayList);
            }

            public Object m2550a(GeneratedMessage generatedMessage, int i) {
                return GeneratedMessage.invokeOrDie(this.f2376l, super.m2033a(generatedMessage, i), new Object[0]);
            }

            public Object m2548a(Builder builder, int i) {
                return GeneratedMessage.invokeOrDie(this.f2376l, super.m2031a(builder, i), new Object[0]);
            }

            public void m2551a(Builder builder, int i, Object obj) {
                super.m2034a(builder, i, GeneratedMessage.invokeOrDie(this.f2375k, null, obj));
            }

            public void m2552b(Builder builder, Object obj) {
                super.m2036b(builder, GeneratedMessage.invokeOrDie(this.f2375k, null, obj));
            }
        }

        /* renamed from: com.google.protobuf.GeneratedMessage.FieldAccessorTable.d */
        private static final class C1272d extends C1059c {
            private final Method f2377k;

            C1272d(FieldDescriptor fieldDescriptor, String str, Class<? extends GeneratedMessage> cls, Class<? extends Builder> cls2) {
                super(fieldDescriptor, str, cls, cls2);
                this.f2377k = GeneratedMessage.getMethodOrDie(this.a, "newBuilder", new Class[0]);
            }

            private Object m2553a(Object obj) {
                return this.a.isInstance(obj) ? obj : ((com.google.protobuf.Message.Builder) GeneratedMessage.invokeOrDie(this.f2377k, null, new Object[0])).mergeFrom((Message) obj).build();
            }

            public void m2555a(Builder builder, int i, Object obj) {
                super.m2034a(builder, i, m2553a(obj));
            }

            public void m2556b(Builder builder, Object obj) {
                super.m2036b(builder, m2553a(obj));
            }

            public com.google.protobuf.Message.Builder m2554a() {
                return (com.google.protobuf.Message.Builder) GeneratedMessage.invokeOrDie(this.f2377k, null, new Object[0]);
            }
        }

        /* renamed from: com.google.protobuf.GeneratedMessage.FieldAccessorTable.e */
        private static final class C1273e extends C1060f {
            private Method f2378h;
            private Method f2379i;

            C1273e(FieldDescriptor fieldDescriptor, String str, Class<? extends GeneratedMessage> cls, Class<? extends Builder> cls2) {
                super(fieldDescriptor, str, cls, cls2);
                this.f2378h = GeneratedMessage.getMethodOrDie(this.a, "valueOf", EnumValueDescriptor.class);
                this.f2379i = GeneratedMessage.getMethodOrDie(this.a, "getValueDescriptor", new Class[0]);
            }

            public Object m2558a(GeneratedMessage generatedMessage) {
                return GeneratedMessage.invokeOrDie(this.f2379i, super.m2045a(generatedMessage), new Object[0]);
            }

            public Object m2557a(Builder builder) {
                return GeneratedMessage.invokeOrDie(this.f2379i, super.m2043a(builder), new Object[0]);
            }

            public void m2559a(Builder builder, Object obj) {
                super.m2048a(builder, GeneratedMessage.invokeOrDie(this.f2378h, null, obj));
            }
        }

        /* renamed from: com.google.protobuf.GeneratedMessage.FieldAccessorTable.g */
        private static final class C1274g extends C1060f {
            private final Method f2380h;

            C1274g(FieldDescriptor fieldDescriptor, String str, Class<? extends GeneratedMessage> cls, Class<? extends Builder> cls2) {
                super(fieldDescriptor, str, cls, cls2);
                this.f2380h = GeneratedMessage.getMethodOrDie(this.a, "newBuilder", new Class[0]);
            }

            private Object m2560a(Object obj) {
                return this.a.isInstance(obj) ? obj : ((com.google.protobuf.Message.Builder) GeneratedMessage.invokeOrDie(this.f2380h, null, new Object[0])).mergeFrom((Message) obj).build();
            }

            public void m2562a(Builder builder, Object obj) {
                super.m2048a(builder, m2560a(obj));
            }

            public com.google.protobuf.Message.Builder m2561a() {
                return (com.google.protobuf.Message.Builder) GeneratedMessage.invokeOrDie(this.f2380h, null, new Object[0]);
            }
        }

        public FieldAccessorTable(Descriptor descriptor, String[] strArr, Class<? extends GeneratedMessage> cls, Class<? extends Builder> cls2) {
            this.descriptor = descriptor;
            this.fields = new C0224a[descriptor.getFields().size()];
            for (int i = 0; i < this.fields.length; i++) {
                FieldDescriptor fieldDescriptor = (FieldDescriptor) descriptor.getFields().get(i);
                if (fieldDescriptor.isRepeated()) {
                    if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                        this.fields[i] = new C1272d(fieldDescriptor, strArr[i], cls, cls2);
                    } else if (fieldDescriptor.getJavaType() == JavaType.ENUM) {
                        this.fields[i] = new C1271b(fieldDescriptor, strArr[i], cls, cls2);
                    } else {
                        this.fields[i] = new C1059c(fieldDescriptor, strArr[i], cls, cls2);
                    }
                } else if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                    this.fields[i] = new C1274g(fieldDescriptor, strArr[i], cls, cls2);
                } else if (fieldDescriptor.getJavaType() == JavaType.ENUM) {
                    this.fields[i] = new C1273e(fieldDescriptor, strArr[i], cls, cls2);
                } else {
                    this.fields[i] = new C1060f(fieldDescriptor, strArr[i], cls, cls2);
                }
            }
        }

        private C0224a getField(FieldDescriptor fieldDescriptor) {
            if (fieldDescriptor.getContainingType() != this.descriptor) {
                throw new IllegalArgumentException("FieldDescriptor does not match message type.");
            } else if (!fieldDescriptor.isExtension()) {
                return this.fields[fieldDescriptor.getIndex()];
            } else {
                throw new IllegalArgumentException("This type does not have extensions.");
            }
        }
    }

    public static final class GeneratedExtension<ContainingType extends Message, Type> {
        private C0225a descriptorRetriever;
        private final Method enumGetValueDescriptor;
        private final Method enumValueOf;
        private final Message messageDefaultInstance;
        private final Class singularType;

        /* renamed from: com.google.protobuf.GeneratedMessage.GeneratedExtension.1 */
        class C10611 implements C0225a {
            final /* synthetic */ FieldDescriptor f1961a;
            final /* synthetic */ GeneratedExtension f1962b;

            C10611(GeneratedExtension generatedExtension, FieldDescriptor fieldDescriptor) {
                this.f1962b = generatedExtension;
                this.f1961a = fieldDescriptor;
            }

            public FieldDescriptor m2055a() {
                return this.f1961a;
            }
        }

        private GeneratedExtension(C0225a c0225a, Class cls, Message message) {
            if (!Message.class.isAssignableFrom(cls) || cls.isInstance(message)) {
                this.descriptorRetriever = c0225a;
                this.singularType = cls;
                this.messageDefaultInstance = message;
                if (ProtocolMessageEnum.class.isAssignableFrom(cls)) {
                    this.enumValueOf = GeneratedMessage.getMethodOrDie(cls, "valueOf", EnumValueDescriptor.class);
                    this.enumGetValueDescriptor = GeneratedMessage.getMethodOrDie(cls, "getValueDescriptor", new Class[0]);
                    return;
                }
                this.enumValueOf = null;
                this.enumGetValueDescriptor = null;
                return;
            }
            throw new IllegalArgumentException("Bad messageDefaultInstance for " + cls.getName());
        }

        public void internalInit(FieldDescriptor fieldDescriptor) {
            if (this.descriptorRetriever != null) {
                throw new IllegalStateException("Already initialized.");
            }
            this.descriptorRetriever = new C10611(this, fieldDescriptor);
        }

        public FieldDescriptor getDescriptor() {
            if (this.descriptorRetriever != null) {
                return this.descriptorRetriever.m981a();
            }
            throw new IllegalStateException("getDescriptor() called before internalInit()");
        }

        public Message getMessageDefaultInstance() {
            return this.messageDefaultInstance;
        }

        private Object fromReflectionType(Object obj) {
            FieldDescriptor descriptor = getDescriptor();
            if (!descriptor.isRepeated()) {
                return singularFromReflectionType(obj);
            }
            if (descriptor.getJavaType() != JavaType.MESSAGE && descriptor.getJavaType() != JavaType.ENUM) {
                return obj;
            }
            List arrayList = new ArrayList();
            for (Object singularFromReflectionType : (List) obj) {
                arrayList.add(singularFromReflectionType(singularFromReflectionType));
            }
            return arrayList;
        }

        private Object singularFromReflectionType(Object obj) {
            switch (C02232.f876a[getDescriptor().getJavaType().ordinal()]) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    if (this.singularType.isInstance(obj)) {
                        return obj;
                    }
                    return this.messageDefaultInstance.newBuilderForType().mergeFrom((Message) obj).build();
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    return GeneratedMessage.invokeOrDie(this.enumValueOf, null, (EnumValueDescriptor) obj);
                default:
                    return obj;
            }
        }

        private Object toReflectionType(Object obj) {
            FieldDescriptor descriptor = getDescriptor();
            if (!descriptor.isRepeated()) {
                return singularToReflectionType(obj);
            }
            if (descriptor.getJavaType() != JavaType.ENUM) {
                return obj;
            }
            List arrayList = new ArrayList();
            for (Object singularToReflectionType : (List) obj) {
                arrayList.add(singularToReflectionType(singularToReflectionType));
            }
            return arrayList;
        }

        private Object singularToReflectionType(Object obj) {
            switch (C02232.f876a[getDescriptor().getJavaType().ordinal()]) {
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    return GeneratedMessage.invokeOrDie(this.enumGetValueDescriptor, obj, new Object[0]);
                default:
                    return obj;
            }
        }
    }

    /* renamed from: com.google.protobuf.GeneratedMessage.a */
    private interface C0225a {
        FieldDescriptor m981a();
    }

    /* renamed from: com.google.protobuf.GeneratedMessage.1 */
    static class C10571 implements C0225a {
        final /* synthetic */ Message f1941a;
        final /* synthetic */ int f1942b;

        C10571(Message message, int i) {
            this.f1941a = message;
            this.f1942b = i;
        }

        public FieldDescriptor m2028a() {
            return (FieldDescriptor) this.f1941a.getDescriptorForType().getExtensions().get(this.f1942b);
        }
    }

    public interface ExtendableMessageOrBuilder<MessageType extends ExtendableMessage> extends MessageOrBuilder {
        <Type> Type getExtension(GeneratedExtension<MessageType, Type> generatedExtension);

        <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i);

        <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> generatedExtension);

        <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> generatedExtension);
    }

    public static abstract class Builder<BuilderType extends Builder> extends com.google.protobuf.AbstractMessage.Builder<BuilderType> {
        private BuilderParent builderParent;
        private boolean isClean;
        private C1058a meAsParent;
        private UnknownFieldSet unknownFields;

        /* renamed from: com.google.protobuf.GeneratedMessage.Builder.a */
        private class C1058a implements BuilderParent {
            final /* synthetic */ Builder f1943a;

            private C1058a(Builder builder) {
                this.f1943a = builder;
            }

            public void markDirty() {
                this.f1943a.onChanged();
            }
        }

        protected abstract FieldAccessorTable internalGetFieldAccessorTable();

        protected Builder() {
            this(null);
        }

        protected Builder(BuilderParent builderParent) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
            this.builderParent = builderParent;
        }

        void dispose() {
            this.builderParent = null;
        }

        protected void onBuilt() {
            if (this.builderParent != null) {
                markClean();
            }
        }

        protected void markClean() {
            this.isClean = true;
        }

        protected boolean isClean() {
            return this.isClean;
        }

        public BuilderType clone() {
            throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
        }

        public BuilderType clear() {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
            onChanged();
            return this;
        }

        public Descriptor getDescriptorForType() {
            return internalGetFieldAccessorTable().descriptor;
        }

        public Map<FieldDescriptor, Object> getAllFields() {
            return Collections.unmodifiableMap(getAllFieldsMutable());
        }

        private Map<FieldDescriptor, Object> getAllFieldsMutable() {
            Map treeMap = new TreeMap();
            for (FieldDescriptor fieldDescriptor : internalGetFieldAccessorTable().descriptor.getFields()) {
                if (fieldDescriptor.isRepeated()) {
                    List list = (List) getField(fieldDescriptor);
                    if (!list.isEmpty()) {
                        treeMap.put(fieldDescriptor, list);
                    }
                } else if (hasField(fieldDescriptor)) {
                    treeMap.put(fieldDescriptor, getField(fieldDescriptor));
                }
            }
            return treeMap;
        }

        public com.google.protobuf.Message.Builder newBuilderForField(FieldDescriptor fieldDescriptor) {
            return internalGetFieldAccessorTable().getField(fieldDescriptor).m968a();
        }

        public boolean hasField(FieldDescriptor fieldDescriptor) {
            return internalGetFieldAccessorTable().getField(fieldDescriptor).m976b(this);
        }

        public Object getField(FieldDescriptor fieldDescriptor) {
            Object a = internalGetFieldAccessorTable().getField(fieldDescriptor).m969a(this);
            if (fieldDescriptor.isRepeated()) {
                return Collections.unmodifiableList((List) a);
            }
            return a;
        }

        public BuilderType setField(FieldDescriptor fieldDescriptor, Object obj) {
            internalGetFieldAccessorTable().getField(fieldDescriptor).m974a(this, obj);
            return this;
        }

        public BuilderType clearField(FieldDescriptor fieldDescriptor) {
            internalGetFieldAccessorTable().getField(fieldDescriptor).m980d(this);
            return this;
        }

        public int getRepeatedFieldCount(FieldDescriptor fieldDescriptor) {
            return internalGetFieldAccessorTable().getField(fieldDescriptor).m978c(this);
        }

        public Object getRepeatedField(FieldDescriptor fieldDescriptor, int i) {
            return internalGetFieldAccessorTable().getField(fieldDescriptor).m970a(this, i);
        }

        public BuilderType setRepeatedField(FieldDescriptor fieldDescriptor, int i, Object obj) {
            internalGetFieldAccessorTable().getField(fieldDescriptor).m973a(this, i, obj);
            return this;
        }

        public BuilderType addRepeatedField(FieldDescriptor fieldDescriptor, Object obj) {
            internalGetFieldAccessorTable().getField(fieldDescriptor).m975b(this, obj);
            return this;
        }

        public final BuilderType setUnknownFields(UnknownFieldSet unknownFieldSet) {
            this.unknownFields = unknownFieldSet;
            onChanged();
            return this;
        }

        public final BuilderType mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
            this.unknownFields = UnknownFieldSet.newBuilder(this.unknownFields).mergeFrom(unknownFieldSet).build();
            onChanged();
            return this;
        }

        public boolean isInitialized() {
            for (FieldDescriptor fieldDescriptor : getDescriptorForType().getFields()) {
                if (fieldDescriptor.isRequired() && !hasField(fieldDescriptor)) {
                    return false;
                }
                if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                    if (fieldDescriptor.isRepeated()) {
                        for (Message isInitialized : (List) getField(fieldDescriptor)) {
                            if (!isInitialized.isInitialized()) {
                                return false;
                            }
                        }
                        continue;
                    } else if (hasField(fieldDescriptor) && !((Message) getField(fieldDescriptor)).isInitialized()) {
                        return false;
                    }
                }
            }
            return true;
        }

        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        protected boolean parseUnknownField(CodedInputStream codedInputStream, com.google.protobuf.UnknownFieldSet.Builder builder, ExtensionRegistryLite extensionRegistryLite, int i) throws IOException {
            return builder.mergeFieldFrom(i, codedInputStream);
        }

        protected BuilderParent getParentForChildren() {
            if (this.meAsParent == null) {
                this.meAsParent = new C1058a();
            }
            return this.meAsParent;
        }

        protected final void onChanged() {
            if (this.isClean && this.builderParent != null) {
                this.builderParent.markDirty();
                this.isClean = false;
            }
        }
    }

    public static abstract class ExtendableBuilder<MessageType extends ExtendableMessage, BuilderType extends ExtendableBuilder> extends Builder<BuilderType> implements ExtendableMessageOrBuilder<MessageType> {
        private FieldSet<FieldDescriptor> extensions;

        protected ExtendableBuilder() {
            this.extensions = FieldSet.m944b();
        }

        protected ExtendableBuilder(BuilderParent builderParent) {
            super(builderParent);
            this.extensions = FieldSet.m944b();
        }

        public BuilderType clear() {
            this.extensions = FieldSet.m944b();
            return (ExtendableBuilder) super.clear();
        }

        public BuilderType clone() {
            throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
        }

        private void ensureExtensionsIsMutable() {
            if (this.extensions.m960d()) {
                this.extensions = this.extensions.m961e();
            }
        }

        private void verifyExtensionContainingType(GeneratedExtension<MessageType, ?> generatedExtension) {
            if (generatedExtension.getDescriptor().getContainingType() != getDescriptorForType()) {
                throw new IllegalArgumentException("Extension is for type \"" + generatedExtension.getDescriptor().getContainingType().getFullName() + "\" which does not match message type \"" + getDescriptorForType().getFullName() + "\".");
            }
        }

        public final <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.m953a(generatedExtension.getDescriptor());
        }

        public final <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.m959d(generatedExtension.getDescriptor());
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            FieldDescriptorLite descriptor = generatedExtension.getDescriptor();
            Object b = this.extensions.m954b(descriptor);
            if (b != null) {
                return generatedExtension.fromReflectionType(b);
            }
            if (descriptor.isRepeated()) {
                return Collections.emptyList();
            }
            if (descriptor.getJavaType() == JavaType.MESSAGE) {
                return generatedExtension.getMessageDefaultInstance();
            }
            return generatedExtension.fromReflectionType(descriptor.getDefaultValue());
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i) {
            verifyExtensionContainingType(generatedExtension);
            return generatedExtension.singularFromReflectionType(this.extensions.m948a(generatedExtension.getDescriptor(), i));
        }

        public final <Type> BuilderType setExtension(GeneratedExtension<MessageType, Type> generatedExtension, Type type) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.m951a(generatedExtension.getDescriptor(), generatedExtension.toReflectionType(type));
            onChanged();
            return this;
        }

        public final <Type> BuilderType setExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i, Type type) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.m950a(generatedExtension.getDescriptor(), i, generatedExtension.singularToReflectionType(type));
            onChanged();
            return this;
        }

        public final <Type> BuilderType addExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, Type type) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.m956b(generatedExtension.getDescriptor(), generatedExtension.singularToReflectionType(type));
            onChanged();
            return this;
        }

        public final <Type> BuilderType clearExtension(GeneratedExtension<MessageType, ?> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.m958c(generatedExtension.getDescriptor());
            onChanged();
            return this;
        }

        protected boolean extensionsAreInitialized() {
            return this.extensions.m965i();
        }

        private FieldSet<FieldDescriptor> buildExtensions() {
            this.extensions.m957c();
            return this.extensions;
        }

        public boolean isInitialized() {
            return super.isInitialized() && extensionsAreInitialized();
        }

        protected boolean parseUnknownField(CodedInputStream codedInputStream, com.google.protobuf.UnknownFieldSet.Builder builder, ExtensionRegistryLite extensionRegistryLite, int i) throws IOException {
            return com.google.protobuf.AbstractMessage.Builder.mergeFieldFrom(codedInputStream, builder, extensionRegistryLite, this, i);
        }

        public Map<FieldDescriptor, Object> getAllFields() {
            Map access$1100 = getAllFieldsMutable();
            access$1100.putAll(this.extensions.m963g());
            return Collections.unmodifiableMap(access$1100);
        }

        public Object getField(FieldDescriptor fieldDescriptor) {
            if (!fieldDescriptor.isExtension()) {
                return super.getField(fieldDescriptor);
            }
            verifyContainingType(fieldDescriptor);
            Object b = this.extensions.m954b((FieldDescriptorLite) fieldDescriptor);
            if (b != null) {
                return b;
            }
            if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                return DynamicMessage.getDefaultInstance(fieldDescriptor.getMessageType());
            }
            return fieldDescriptor.getDefaultValue();
        }

        public int getRepeatedFieldCount(FieldDescriptor fieldDescriptor) {
            if (!fieldDescriptor.isExtension()) {
                return super.getRepeatedFieldCount(fieldDescriptor);
            }
            verifyContainingType(fieldDescriptor);
            return this.extensions.m959d(fieldDescriptor);
        }

        public Object getRepeatedField(FieldDescriptor fieldDescriptor, int i) {
            if (!fieldDescriptor.isExtension()) {
                return super.getRepeatedField(fieldDescriptor, i);
            }
            verifyContainingType(fieldDescriptor);
            return this.extensions.m948a((FieldDescriptorLite) fieldDescriptor, i);
        }

        public boolean hasField(FieldDescriptor fieldDescriptor) {
            if (!fieldDescriptor.isExtension()) {
                return super.hasField(fieldDescriptor);
            }
            verifyContainingType(fieldDescriptor);
            return this.extensions.m953a((FieldDescriptorLite) fieldDescriptor);
        }

        public BuilderType setField(FieldDescriptor fieldDescriptor, Object obj) {
            if (!fieldDescriptor.isExtension()) {
                return (ExtendableBuilder) super.setField(fieldDescriptor, obj);
            }
            verifyContainingType(fieldDescriptor);
            ensureExtensionsIsMutable();
            this.extensions.m951a((FieldDescriptorLite) fieldDescriptor, obj);
            onChanged();
            return this;
        }

        public BuilderType clearField(FieldDescriptor fieldDescriptor) {
            if (!fieldDescriptor.isExtension()) {
                return (ExtendableBuilder) super.clearField(fieldDescriptor);
            }
            verifyContainingType(fieldDescriptor);
            ensureExtensionsIsMutable();
            this.extensions.m958c((FieldDescriptorLite) fieldDescriptor);
            onChanged();
            return this;
        }

        public BuilderType setRepeatedField(FieldDescriptor fieldDescriptor, int i, Object obj) {
            if (!fieldDescriptor.isExtension()) {
                return (ExtendableBuilder) super.setRepeatedField(fieldDescriptor, i, obj);
            }
            verifyContainingType(fieldDescriptor);
            ensureExtensionsIsMutable();
            this.extensions.m950a((FieldDescriptorLite) fieldDescriptor, i, obj);
            onChanged();
            return this;
        }

        public BuilderType addRepeatedField(FieldDescriptor fieldDescriptor, Object obj) {
            if (!fieldDescriptor.isExtension()) {
                return (ExtendableBuilder) super.addRepeatedField(fieldDescriptor, obj);
            }
            verifyContainingType(fieldDescriptor);
            ensureExtensionsIsMutable();
            this.extensions.m956b((FieldDescriptorLite) fieldDescriptor, obj);
            onChanged();
            return this;
        }

        protected final void mergeExtensionFields(ExtendableMessage extendableMessage) {
            ensureExtensionsIsMutable();
            this.extensions.m952a(extendableMessage.extensions);
            onChanged();
        }

        private void verifyContainingType(FieldDescriptor fieldDescriptor) {
            if (fieldDescriptor.getContainingType() != getDescriptorForType()) {
                throw new IllegalArgumentException("FieldDescriptor does not match message type.");
            }
        }
    }

    public static abstract class ExtendableMessage<MessageType extends ExtendableMessage> extends GeneratedMessage implements ExtendableMessageOrBuilder<MessageType> {
        private final FieldSet<FieldDescriptor> extensions;

        protected class ExtensionWriter {
            private final Iterator<Entry<FieldDescriptor, Object>> iter;
            private final boolean messageSetWireFormat;
            private Entry<FieldDescriptor, Object> next;

            private ExtensionWriter(boolean z) {
                this.iter = ExtendableMessage.this.extensions.m964h();
                if (this.iter.hasNext()) {
                    this.next = (Entry) this.iter.next();
                }
                this.messageSetWireFormat = z;
            }

            public void writeUntil(int i, CodedOutputStream codedOutputStream) throws IOException {
                while (this.next != null && ((FieldDescriptor) this.next.getKey()).getNumber() < i) {
                    FieldDescriptorLite fieldDescriptorLite = (FieldDescriptor) this.next.getKey();
                    if (this.messageSetWireFormat && fieldDescriptorLite.getLiteJavaType() == WireFormat.JavaType.MESSAGE && !fieldDescriptorLite.isRepeated()) {
                        codedOutputStream.writeMessageSetExtension(fieldDescriptorLite.getNumber(), (Message) this.next.getValue());
                    } else {
                        FieldSet.m939a(fieldDescriptorLite, this.next.getValue(), codedOutputStream);
                    }
                    if (this.iter.hasNext()) {
                        this.next = (Entry) this.iter.next();
                    } else {
                        this.next = null;
                    }
                }
            }
        }

        protected ExtendableMessage() {
            this.extensions = FieldSet.m935a();
        }

        protected ExtendableMessage(ExtendableBuilder<MessageType, ?> extendableBuilder) {
            super(extendableBuilder);
            this.extensions = extendableBuilder.buildExtensions();
        }

        private void verifyExtensionContainingType(GeneratedExtension<MessageType, ?> generatedExtension) {
            if (generatedExtension.getDescriptor().getContainingType() != getDescriptorForType()) {
                throw new IllegalArgumentException("Extension is for type \"" + generatedExtension.getDescriptor().getContainingType().getFullName() + "\" which does not match message type \"" + getDescriptorForType().getFullName() + "\".");
            }
        }

        public final <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.m953a(generatedExtension.getDescriptor());
        }

        public final <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.m959d(generatedExtension.getDescriptor());
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            FieldDescriptorLite descriptor = generatedExtension.getDescriptor();
            Object b = this.extensions.m954b(descriptor);
            if (b != null) {
                return generatedExtension.fromReflectionType(b);
            }
            if (descriptor.isRepeated()) {
                return Collections.emptyList();
            }
            if (descriptor.getJavaType() == JavaType.MESSAGE) {
                return generatedExtension.getMessageDefaultInstance();
            }
            return generatedExtension.fromReflectionType(descriptor.getDefaultValue());
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i) {
            verifyExtensionContainingType(generatedExtension);
            return generatedExtension.singularFromReflectionType(this.extensions.m948a(generatedExtension.getDescriptor(), i));
        }

        protected boolean extensionsAreInitialized() {
            return this.extensions.m965i();
        }

        public boolean isInitialized() {
            return super.isInitialized() && extensionsAreInitialized();
        }

        protected ExtensionWriter newExtensionWriter() {
            return new ExtensionWriter(false, null);
        }

        protected ExtensionWriter newMessageSetExtensionWriter() {
            return new ExtensionWriter(true, null);
        }

        protected int extensionsSerializedSize() {
            return this.extensions.m966j();
        }

        protected int extensionsSerializedSizeAsMessageSet() {
            return this.extensions.m967k();
        }

        protected Map<FieldDescriptor, Object> getExtensionFields() {
            return this.extensions.m963g();
        }

        public Map<FieldDescriptor, Object> getAllFields() {
            Map access$800 = getAllFieldsMutable();
            access$800.putAll(getExtensionFields());
            return Collections.unmodifiableMap(access$800);
        }

        public boolean hasField(FieldDescriptor fieldDescriptor) {
            if (!fieldDescriptor.isExtension()) {
                return super.hasField(fieldDescriptor);
            }
            verifyContainingType(fieldDescriptor);
            return this.extensions.m953a((FieldDescriptorLite) fieldDescriptor);
        }

        public Object getField(FieldDescriptor fieldDescriptor) {
            if (!fieldDescriptor.isExtension()) {
                return super.getField(fieldDescriptor);
            }
            verifyContainingType(fieldDescriptor);
            Object b = this.extensions.m954b((FieldDescriptorLite) fieldDescriptor);
            if (b != null) {
                return b;
            }
            if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                return DynamicMessage.getDefaultInstance(fieldDescriptor.getMessageType());
            }
            return fieldDescriptor.getDefaultValue();
        }

        public int getRepeatedFieldCount(FieldDescriptor fieldDescriptor) {
            if (!fieldDescriptor.isExtension()) {
                return super.getRepeatedFieldCount(fieldDescriptor);
            }
            verifyContainingType(fieldDescriptor);
            return this.extensions.m959d(fieldDescriptor);
        }

        public Object getRepeatedField(FieldDescriptor fieldDescriptor, int i) {
            if (!fieldDescriptor.isExtension()) {
                return super.getRepeatedField(fieldDescriptor, i);
            }
            verifyContainingType(fieldDescriptor);
            return this.extensions.m948a((FieldDescriptorLite) fieldDescriptor, i);
        }

        private void verifyContainingType(FieldDescriptor fieldDescriptor) {
            if (fieldDescriptor.getContainingType() != getDescriptorForType()) {
                throw new IllegalArgumentException("FieldDescriptor does not match message type.");
            }
        }
    }

    protected abstract FieldAccessorTable internalGetFieldAccessorTable();

    protected abstract com.google.protobuf.Message.Builder newBuilderForType(BuilderParent builderParent);

    static {
        alwaysUseFieldBuilders = false;
    }

    protected GeneratedMessage() {
        this.unknownFields = UnknownFieldSet.getDefaultInstance();
    }

    protected GeneratedMessage(Builder<?> builder) {
        this.unknownFields = builder.getUnknownFields();
    }

    static void enableAlwaysUseFieldBuildersForTesting() {
        alwaysUseFieldBuilders = true;
    }

    public Descriptor getDescriptorForType() {
        return internalGetFieldAccessorTable().descriptor;
    }

    private Map<FieldDescriptor, Object> getAllFieldsMutable() {
        Map treeMap = new TreeMap();
        for (FieldDescriptor fieldDescriptor : internalGetFieldAccessorTable().descriptor.getFields()) {
            if (fieldDescriptor.isRepeated()) {
                List list = (List) getField(fieldDescriptor);
                if (!list.isEmpty()) {
                    treeMap.put(fieldDescriptor, list);
                }
            } else if (hasField(fieldDescriptor)) {
                treeMap.put(fieldDescriptor, getField(fieldDescriptor));
            }
        }
        return treeMap;
    }

    public boolean isInitialized() {
        for (FieldDescriptor fieldDescriptor : getDescriptorForType().getFields()) {
            if (fieldDescriptor.isRequired() && !hasField(fieldDescriptor)) {
                return false;
            }
            if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                if (fieldDescriptor.isRepeated()) {
                    for (Message isInitialized : (List) getField(fieldDescriptor)) {
                        if (!isInitialized.isInitialized()) {
                            return false;
                        }
                    }
                    continue;
                } else if (hasField(fieldDescriptor) && !((Message) getField(fieldDescriptor)).isInitialized()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Map<FieldDescriptor, Object> getAllFields() {
        return Collections.unmodifiableMap(getAllFieldsMutable());
    }

    public boolean hasField(FieldDescriptor fieldDescriptor) {
        return internalGetFieldAccessorTable().getField(fieldDescriptor).m977b(this);
    }

    public Object getField(FieldDescriptor fieldDescriptor) {
        return internalGetFieldAccessorTable().getField(fieldDescriptor).m971a(this);
    }

    public int getRepeatedFieldCount(FieldDescriptor fieldDescriptor) {
        return internalGetFieldAccessorTable().getField(fieldDescriptor).m979c(this);
    }

    public Object getRepeatedField(FieldDescriptor fieldDescriptor, int i) {
        return internalGetFieldAccessorTable().getField(fieldDescriptor).m972a(this, i);
    }

    public final UnknownFieldSet getUnknownFields() {
        return this.unknownFields;
    }

    public static <ContainingType extends Message, Type> GeneratedExtension<ContainingType, Type> newMessageScopedGeneratedExtension(Message message, int i, Class cls, Message message2) {
        return new GeneratedExtension(cls, message2, null);
    }

    public static <ContainingType extends Message, Type> GeneratedExtension<ContainingType, Type> newFileScopedGeneratedExtension(Class cls, Message message) {
        return new GeneratedExtension(cls, message, null);
    }

    private static Method getMethodOrDie(Class cls, String str, Class... clsArr) {
        try {
            return cls.getMethod(str, clsArr);
        } catch (Throwable e) {
            throw new RuntimeException("Generated message class \"" + cls.getName() + "\" missing method \"" + str + "\".", e);
        }
    }

    private static Object invokeOrDie(Method method, Object obj, Object... objArr) {
        Throwable e;
        try {
            return method.invoke(obj, objArr);
        } catch (Throwable e2) {
            throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", e2);
        } catch (InvocationTargetException e3) {
            e2 = e3.getCause();
            if (e2 instanceof RuntimeException) {
                throw ((RuntimeException) e2);
            } else if (e2 instanceof Error) {
                throw ((Error) e2);
            } else {
                throw new RuntimeException("Unexpected exception thrown by generated accessor method.", e2);
            }
        }
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new C0227b(this);
    }
}
