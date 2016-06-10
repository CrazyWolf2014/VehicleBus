package com.google.protobuf;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.FieldSet.FieldDescriptorLite;
import com.google.protobuf.Internal.EnumLiteMap;
import com.google.protobuf.WireFormat.FieldType;
import com.google.protobuf.WireFormat.JavaType;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public abstract class GeneratedMessageLite extends AbstractMessageLite implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: com.google.protobuf.GeneratedMessageLite.1 */
    static /* synthetic */ class C02261 {
        static final /* synthetic */ int[] f877a;

        static {
            f877a = new int[JavaType.values().length];
            try {
                f877a[JavaType.MESSAGE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f877a[JavaType.ENUM.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public static final class GeneratedExtension<ContainingType extends MessageLite, Type> {
        private final ContainingType containingTypeDefaultInstance;
        private final Type defaultValue;
        private final C1062a descriptor;
        private final MessageLite messageDefaultInstance;

        private GeneratedExtension(ContainingType containingType, Type type, MessageLite messageLite, C1062a c1062a) {
            if (containingType == null) {
                throw new IllegalArgumentException("Null containingTypeDefaultInstance");
            } else if (c1062a.getLiteType() == FieldType.MESSAGE && messageLite == null) {
                throw new IllegalArgumentException("Null messageDefaultInstance");
            } else {
                this.containingTypeDefaultInstance = containingType;
                this.defaultValue = type;
                this.messageDefaultInstance = messageLite;
                this.descriptor = c1062a;
            }
        }

        public ContainingType getContainingTypeDefaultInstance() {
            return this.containingTypeDefaultInstance;
        }

        public int getNumber() {
            return this.descriptor.getNumber();
        }

        public MessageLite getMessageDefaultInstance() {
            return this.messageDefaultInstance;
        }
    }

    /* renamed from: com.google.protobuf.GeneratedMessageLite.b */
    static final class C0227b implements Serializable {
        private String f878a;
        private byte[] f879b;

        C0227b(MessageLite messageLite) {
            this.f878a = messageLite.getClass().getName();
            this.f879b = messageLite.toByteArray();
        }
    }

    public interface ExtendableMessageOrBuilder<MessageType extends ExtendableMessage> extends MessageLiteOrBuilder {
        <Type> Type getExtension(GeneratedExtension<MessageType, Type> generatedExtension);

        <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i);

        <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> generatedExtension);

        <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> generatedExtension);
    }

    /* renamed from: com.google.protobuf.GeneratedMessageLite.a */
    private static final class C1062a implements FieldDescriptorLite<C1062a> {
        private final EnumLiteMap<?> f1963a;
        private final int f1964b;
        private final FieldType f1965c;
        private final boolean f1966d;
        private final boolean f1967e;

        public /* synthetic */ int compareTo(Object obj) {
            return m2058a((C1062a) obj);
        }

        private C1062a(EnumLiteMap<?> enumLiteMap, int i, FieldType fieldType, boolean z, boolean z2) {
            this.f1963a = enumLiteMap;
            this.f1964b = i;
            this.f1965c = fieldType;
            this.f1966d = z;
            this.f1967e = z2;
        }

        public int getNumber() {
            return this.f1964b;
        }

        public FieldType getLiteType() {
            return this.f1965c;
        }

        public JavaType getLiteJavaType() {
            return this.f1965c.getJavaType();
        }

        public boolean isRepeated() {
            return this.f1966d;
        }

        public boolean isPacked() {
            return this.f1967e;
        }

        public EnumLiteMap<?> getEnumType() {
            return this.f1963a;
        }

        public com.google.protobuf.MessageLite.Builder internalMergeFrom(com.google.protobuf.MessageLite.Builder builder, MessageLite messageLite) {
            return ((Builder) builder).mergeFrom((GeneratedMessageLite) messageLite);
        }

        public int m2058a(C1062a c1062a) {
            return this.f1964b - c1062a.f1964b;
        }
    }

    public static abstract class Builder<MessageType extends GeneratedMessageLite, BuilderType extends Builder> extends com.google.protobuf.AbstractMessageLite.Builder<BuilderType> {
        public abstract MessageType getDefaultInstanceForType();

        public abstract BuilderType mergeFrom(MessageType messageType);

        protected Builder() {
        }

        public BuilderType clear() {
            return this;
        }

        public BuilderType clone() {
            throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
        }

        protected boolean parseUnknownField(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, int i) throws IOException {
            return codedInputStream.skipField(i);
        }
    }

    public static abstract class ExtendableBuilder<MessageType extends ExtendableMessage<MessageType>, BuilderType extends ExtendableBuilder<MessageType, BuilderType>> extends Builder<MessageType, BuilderType> implements ExtendableMessageOrBuilder<MessageType> {
        private FieldSet<C1062a> extensions;
        private boolean extensionsIsMutable;

        protected ExtendableBuilder() {
            this.extensions = FieldSet.m944b();
        }

        public BuilderType clear() {
            this.extensions.m962f();
            this.extensionsIsMutable = false;
            return (ExtendableBuilder) super.clear();
        }

        private void ensureExtensionsIsMutable() {
            if (!this.extensionsIsMutable) {
                this.extensions = this.extensions.m961e();
                this.extensionsIsMutable = true;
            }
        }

        private FieldSet<C1062a> buildExtensions() {
            this.extensions.m957c();
            this.extensionsIsMutable = false;
            return this.extensions;
        }

        private void verifyExtensionContainingType(GeneratedExtension<MessageType, ?> generatedExtension) {
            if (generatedExtension.getContainingTypeDefaultInstance() != getDefaultInstanceForType()) {
                throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
            }
        }

        public final <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.m953a(generatedExtension.descriptor);
        }

        public final <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.m959d(generatedExtension.descriptor);
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            Type b = this.extensions.m954b(generatedExtension.descriptor);
            if (b == null) {
                return generatedExtension.defaultValue;
            }
            return b;
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.m948a(generatedExtension.descriptor, i);
        }

        public BuilderType clone() {
            throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
        }

        public final <Type> BuilderType setExtension(GeneratedExtension<MessageType, Type> generatedExtension, Type type) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.m951a(generatedExtension.descriptor, (Object) type);
            return this;
        }

        public final <Type> BuilderType setExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i, Type type) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.m950a(generatedExtension.descriptor, i, (Object) type);
            return this;
        }

        public final <Type> BuilderType addExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, Type type) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.m956b(generatedExtension.descriptor, (Object) type);
            return this;
        }

        public final <Type> BuilderType clearExtension(GeneratedExtension<MessageType, ?> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.m958c(generatedExtension.descriptor);
            return this;
        }

        protected boolean extensionsAreInitialized() {
            return this.extensions.m965i();
        }

        protected boolean parseUnknownField(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, int i) throws IOException {
            boolean z;
            boolean z2 = false;
            int tagWireType = WireFormat.getTagWireType(i);
            GeneratedExtension findLiteExtensionByNumber = extensionRegistryLite.findLiteExtensionByNumber(getDefaultInstanceForType(), WireFormat.getTagFieldNumber(i));
            if (findLiteExtensionByNumber == null) {
                z = true;
            } else if (tagWireType == FieldSet.m934a(findLiteExtensionByNumber.descriptor.getLiteType(), false)) {
                z = false;
            } else if (findLiteExtensionByNumber.descriptor.f1966d && findLiteExtensionByNumber.descriptor.f1965c.isPackable() && tagWireType == FieldSet.m934a(findLiteExtensionByNumber.descriptor.getLiteType(), true)) {
                z = false;
                z2 = true;
            } else {
                z = true;
            }
            if (z) {
                return codedInputStream.skipField(i);
            }
            if (z2) {
                int pushLimit = codedInputStream.pushLimit(codedInputStream.readRawVarint32());
                Object findValueByNumber;
                if (findLiteExtensionByNumber.descriptor.getLiteType() == FieldType.ENUM) {
                    while (codedInputStream.getBytesUntilLimit() > 0) {
                        findValueByNumber = findLiteExtensionByNumber.descriptor.getEnumType().findValueByNumber(codedInputStream.readEnum());
                        if (findValueByNumber == null) {
                            return true;
                        }
                        ensureExtensionsIsMutable();
                        this.extensions.m956b(findLiteExtensionByNumber.descriptor, findValueByNumber);
                    }
                } else {
                    while (codedInputStream.getBytesUntilLimit() > 0) {
                        findValueByNumber = FieldSet.m936a(codedInputStream, findLiteExtensionByNumber.descriptor.getLiteType());
                        ensureExtensionsIsMutable();
                        this.extensions.m956b(findLiteExtensionByNumber.descriptor, findValueByNumber);
                    }
                }
                codedInputStream.popLimit(pushLimit);
            } else {
                Object build;
                switch (C02261.f877a[findLiteExtensionByNumber.descriptor.getLiteJavaType().ordinal()]) {
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        com.google.protobuf.MessageLite.Builder toBuilder;
                        if (!findLiteExtensionByNumber.descriptor.isRepeated()) {
                            MessageLite messageLite = (MessageLite) this.extensions.m954b(findLiteExtensionByNumber.descriptor);
                            if (messageLite != null) {
                                toBuilder = messageLite.toBuilder();
                                if (toBuilder == null) {
                                    toBuilder = findLiteExtensionByNumber.messageDefaultInstance.newBuilderForType();
                                }
                                if (findLiteExtensionByNumber.descriptor.getLiteType() != FieldType.GROUP) {
                                    codedInputStream.readGroup(findLiteExtensionByNumber.getNumber(), toBuilder, extensionRegistryLite);
                                } else {
                                    codedInputStream.readMessage(toBuilder, extensionRegistryLite);
                                }
                                build = toBuilder.build();
                                break;
                            }
                        }
                        toBuilder = null;
                        if (toBuilder == null) {
                            toBuilder = findLiteExtensionByNumber.messageDefaultInstance.newBuilderForType();
                        }
                        if (findLiteExtensionByNumber.descriptor.getLiteType() != FieldType.GROUP) {
                            codedInputStream.readMessage(toBuilder, extensionRegistryLite);
                        } else {
                            codedInputStream.readGroup(findLiteExtensionByNumber.getNumber(), toBuilder, extensionRegistryLite);
                        }
                        build = toBuilder.build();
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        build = findLiteExtensionByNumber.descriptor.getEnumType().findValueByNumber(codedInputStream.readEnum());
                        if (build == null) {
                            return true;
                        }
                        break;
                    default:
                        build = FieldSet.m936a(codedInputStream, findLiteExtensionByNumber.descriptor.getLiteType());
                        break;
                }
                if (findLiteExtensionByNumber.descriptor.isRepeated()) {
                    ensureExtensionsIsMutable();
                    this.extensions.m956b(findLiteExtensionByNumber.descriptor, build);
                } else {
                    ensureExtensionsIsMutable();
                    this.extensions.m951a(findLiteExtensionByNumber.descriptor, build);
                }
            }
            return true;
        }

        protected final void mergeExtensionFields(MessageType messageType) {
            ensureExtensionsIsMutable();
            this.extensions.m952a(messageType.extensions);
        }
    }

    public static abstract class ExtendableMessage<MessageType extends ExtendableMessage<MessageType>> extends GeneratedMessageLite implements ExtendableMessageOrBuilder<MessageType> {
        private final FieldSet<C1062a> extensions;

        protected class ExtensionWriter {
            private final Iterator<Entry<C1062a, Object>> iter;
            private final boolean messageSetWireFormat;
            private Entry<C1062a, Object> next;

            private ExtensionWriter(boolean z) {
                this.iter = ExtendableMessage.this.extensions.m964h();
                if (this.iter.hasNext()) {
                    this.next = (Entry) this.iter.next();
                }
                this.messageSetWireFormat = z;
            }

            public void writeUntil(int i, CodedOutputStream codedOutputStream) throws IOException {
                while (this.next != null && ((C1062a) this.next.getKey()).getNumber() < i) {
                    FieldDescriptorLite fieldDescriptorLite = (C1062a) this.next.getKey();
                    if (this.messageSetWireFormat && fieldDescriptorLite.getLiteJavaType() == JavaType.MESSAGE && !fieldDescriptorLite.isRepeated()) {
                        codedOutputStream.writeMessageSetExtension(fieldDescriptorLite.getNumber(), (MessageLite) this.next.getValue());
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
            this.extensions = extendableBuilder.buildExtensions();
        }

        private void verifyExtensionContainingType(GeneratedExtension<MessageType, ?> generatedExtension) {
            if (generatedExtension.getContainingTypeDefaultInstance() != getDefaultInstanceForType()) {
                throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
            }
        }

        public final <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.m953a(generatedExtension.descriptor);
        }

        public final <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.m959d(generatedExtension.descriptor);
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            Type b = this.extensions.m954b(generatedExtension.descriptor);
            if (b == null) {
                return generatedExtension.defaultValue;
            }
            return b;
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.m948a(generatedExtension.descriptor, i);
        }

        protected boolean extensionsAreInitialized() {
            return this.extensions.m965i();
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
    }

    protected GeneratedMessageLite() {
    }

    protected GeneratedMessageLite(Builder builder) {
    }

    public static <ContainingType extends MessageLite, Type> GeneratedExtension<ContainingType, Type> newSingularGeneratedExtension(ContainingType containingType, Type type, MessageLite messageLite, EnumLiteMap<?> enumLiteMap, int i, FieldType fieldType) {
        return new GeneratedExtension(type, messageLite, new C1062a(i, fieldType, false, false, null), null);
    }

    public static <ContainingType extends MessageLite, Type> GeneratedExtension<ContainingType, Type> newRepeatedGeneratedExtension(ContainingType containingType, MessageLite messageLite, EnumLiteMap<?> enumLiteMap, int i, FieldType fieldType, boolean z) {
        return new GeneratedExtension(Collections.emptyList(), messageLite, new C1062a(i, fieldType, true, z, null), null);
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new C0227b(this);
    }
}
