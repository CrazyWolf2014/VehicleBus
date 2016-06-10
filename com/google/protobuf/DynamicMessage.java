package com.google.protobuf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.FieldSet.FieldDescriptorLite;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public final class DynamicMessage extends AbstractMessage {
    private final FieldSet<FieldDescriptor> fields;
    private int memoizedSize;
    private final Descriptor type;
    private final UnknownFieldSet unknownFields;

    public static final class Builder extends com.google.protobuf.AbstractMessage.Builder<Builder> {
        private FieldSet<FieldDescriptor> fields;
        private final Descriptor type;
        private UnknownFieldSet unknownFields;

        private Builder(Descriptor descriptor) {
            this.type = descriptor;
            this.fields = FieldSet.m935a();
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public Builder clear() {
            if (this.fields == null) {
                throw new IllegalStateException("Cannot call clear() after build().");
            }
            this.fields.m962f();
            return this;
        }

        public Builder mergeFrom(Message message) {
            if (!(message instanceof DynamicMessage)) {
                return (Builder) super.mergeFrom(message);
            }
            DynamicMessage dynamicMessage = (DynamicMessage) message;
            if (dynamicMessage.type != this.type) {
                throw new IllegalArgumentException("mergeFrom(Message) can only merge messages of the same type.");
            }
            this.fields.m952a(dynamicMessage.fields);
            mergeUnknownFields(dynamicMessage.unknownFields);
            return this;
        }

        public DynamicMessage build() {
            if (this.fields == null || isInitialized()) {
                return buildPartial();
            }
            throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(new DynamicMessage(this.fields, this.unknownFields, null));
        }

        private DynamicMessage buildParsed() throws InvalidProtocolBufferException {
            if (isInitialized()) {
                return buildPartial();
            }
            throw com.google.protobuf.AbstractMessage.Builder.newUninitializedMessageException(new DynamicMessage(this.fields, this.unknownFields, null)).asInvalidProtocolBufferException();
        }

        public DynamicMessage buildPartial() {
            if (this.fields == null) {
                throw new IllegalStateException("build() has already been called on this Builder.");
            }
            this.fields.m957c();
            DynamicMessage dynamicMessage = new DynamicMessage(this.fields, this.unknownFields, null);
            this.fields = null;
            this.unknownFields = null;
            return dynamicMessage;
        }

        public Builder clone() {
            Builder builder = new Builder(this.type);
            builder.fields.m952a(this.fields);
            return builder;
        }

        public boolean isInitialized() {
            return DynamicMessage.isInitialized(this.type, this.fields);
        }

        public Descriptor getDescriptorForType() {
            return this.type;
        }

        public DynamicMessage getDefaultInstanceForType() {
            return DynamicMessage.getDefaultInstance(this.type);
        }

        public Map<FieldDescriptor, Object> getAllFields() {
            return this.fields.m963g();
        }

        public Builder newBuilderForField(FieldDescriptor fieldDescriptor) {
            verifyContainingType(fieldDescriptor);
            if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                return new Builder(fieldDescriptor.getMessageType());
            }
            throw new IllegalArgumentException("newBuilderForField is only valid for fields with message type.");
        }

        public boolean hasField(FieldDescriptor fieldDescriptor) {
            verifyContainingType(fieldDescriptor);
            return this.fields.m953a((FieldDescriptorLite) fieldDescriptor);
        }

        public Object getField(FieldDescriptor fieldDescriptor) {
            verifyContainingType(fieldDescriptor);
            Object b = this.fields.m954b((FieldDescriptorLite) fieldDescriptor);
            if (b != null) {
                return b;
            }
            if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                return DynamicMessage.getDefaultInstance(fieldDescriptor.getMessageType());
            }
            return fieldDescriptor.getDefaultValue();
        }

        public Builder setField(FieldDescriptor fieldDescriptor, Object obj) {
            verifyContainingType(fieldDescriptor);
            this.fields.m951a((FieldDescriptorLite) fieldDescriptor, obj);
            return this;
        }

        public Builder clearField(FieldDescriptor fieldDescriptor) {
            verifyContainingType(fieldDescriptor);
            this.fields.m958c((FieldDescriptorLite) fieldDescriptor);
            return this;
        }

        public int getRepeatedFieldCount(FieldDescriptor fieldDescriptor) {
            verifyContainingType(fieldDescriptor);
            return this.fields.m959d(fieldDescriptor);
        }

        public Object getRepeatedField(FieldDescriptor fieldDescriptor, int i) {
            verifyContainingType(fieldDescriptor);
            return this.fields.m948a((FieldDescriptorLite) fieldDescriptor, i);
        }

        public Builder setRepeatedField(FieldDescriptor fieldDescriptor, int i, Object obj) {
            verifyContainingType(fieldDescriptor);
            this.fields.m950a((FieldDescriptorLite) fieldDescriptor, i, obj);
            return this;
        }

        public Builder addRepeatedField(FieldDescriptor fieldDescriptor, Object obj) {
            verifyContainingType(fieldDescriptor);
            this.fields.m956b((FieldDescriptorLite) fieldDescriptor, obj);
            return this;
        }

        public UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        public Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
            this.unknownFields = unknownFieldSet;
            return this;
        }

        public Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
            this.unknownFields = UnknownFieldSet.newBuilder(this.unknownFields).mergeFrom(unknownFieldSet).build();
            return this;
        }

        private void verifyContainingType(FieldDescriptor fieldDescriptor) {
            if (fieldDescriptor.getContainingType() != this.type) {
                throw new IllegalArgumentException("FieldDescriptor does not match message type.");
            }
        }
    }

    private DynamicMessage(Descriptor descriptor, FieldSet<FieldDescriptor> fieldSet, UnknownFieldSet unknownFieldSet) {
        this.memoizedSize = -1;
        this.type = descriptor;
        this.fields = fieldSet;
        this.unknownFields = unknownFieldSet;
    }

    public static DynamicMessage getDefaultInstance(Descriptor descriptor) {
        return new DynamicMessage(descriptor, FieldSet.m944b(), UnknownFieldSet.getDefaultInstance());
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, CodedInputStream codedInputStream) throws IOException {
        return ((Builder) newBuilder(descriptor).mergeFrom(codedInputStream)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, CodedInputStream codedInputStream, ExtensionRegistry extensionRegistry) throws IOException {
        return ((Builder) newBuilder(descriptor).mergeFrom(codedInputStream, (ExtensionRegistryLite) extensionRegistry)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, ByteString byteString) throws InvalidProtocolBufferException {
        return ((Builder) newBuilder(descriptor).mergeFrom(byteString)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, ByteString byteString, ExtensionRegistry extensionRegistry) throws InvalidProtocolBufferException {
        return ((Builder) newBuilder(descriptor).mergeFrom(byteString, (ExtensionRegistryLite) extensionRegistry)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, byte[] bArr) throws InvalidProtocolBufferException {
        return ((Builder) newBuilder(descriptor).mergeFrom(bArr)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, byte[] bArr, ExtensionRegistry extensionRegistry) throws InvalidProtocolBufferException {
        return ((Builder) newBuilder(descriptor).mergeFrom(bArr, (ExtensionRegistryLite) extensionRegistry)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, InputStream inputStream) throws IOException {
        return ((Builder) newBuilder(descriptor).mergeFrom(inputStream)).buildParsed();
    }

    public static DynamicMessage parseFrom(Descriptor descriptor, InputStream inputStream, ExtensionRegistry extensionRegistry) throws IOException {
        return ((Builder) newBuilder(descriptor).mergeFrom(inputStream, (ExtensionRegistryLite) extensionRegistry)).buildParsed();
    }

    public static Builder newBuilder(Descriptor descriptor) {
        return new Builder(null);
    }

    public static Builder newBuilder(Message message) {
        return new Builder(null).mergeFrom(message);
    }

    public Descriptor getDescriptorForType() {
        return this.type;
    }

    public DynamicMessage getDefaultInstanceForType() {
        return getDefaultInstance(this.type);
    }

    public Map<FieldDescriptor, Object> getAllFields() {
        return this.fields.m963g();
    }

    public boolean hasField(FieldDescriptor fieldDescriptor) {
        verifyContainingType(fieldDescriptor);
        return this.fields.m953a((FieldDescriptorLite) fieldDescriptor);
    }

    public Object getField(FieldDescriptor fieldDescriptor) {
        verifyContainingType(fieldDescriptor);
        Object b = this.fields.m954b((FieldDescriptorLite) fieldDescriptor);
        if (b != null) {
            return b;
        }
        if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
            return getDefaultInstance(fieldDescriptor.getMessageType());
        }
        return fieldDescriptor.getDefaultValue();
    }

    public int getRepeatedFieldCount(FieldDescriptor fieldDescriptor) {
        verifyContainingType(fieldDescriptor);
        return this.fields.m959d(fieldDescriptor);
    }

    public Object getRepeatedField(FieldDescriptor fieldDescriptor, int i) {
        verifyContainingType(fieldDescriptor);
        return this.fields.m948a((FieldDescriptorLite) fieldDescriptor, i);
    }

    public UnknownFieldSet getUnknownFields() {
        return this.unknownFields;
    }

    private static boolean isInitialized(Descriptor descriptor, FieldSet<FieldDescriptor> fieldSet) {
        for (FieldDescriptorLite fieldDescriptorLite : descriptor.getFields()) {
            if (fieldDescriptorLite.isRequired() && !fieldSet.m953a(fieldDescriptorLite)) {
                return false;
            }
        }
        return fieldSet.m965i();
    }

    public boolean isInitialized() {
        return isInitialized(this.type, this.fields);
    }

    public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
        if (this.type.getOptions().getMessageSetWireFormat()) {
            this.fields.m955b(codedOutputStream);
            this.unknownFields.writeAsMessageSetTo(codedOutputStream);
            return;
        }
        this.fields.m949a(codedOutputStream);
        this.unknownFields.writeTo(codedOutputStream);
    }

    public int getSerializedSize() {
        int i = this.memoizedSize;
        if (i == -1) {
            if (this.type.getOptions().getMessageSetWireFormat()) {
                i = this.fields.m967k() + this.unknownFields.getSerializedSizeAsMessageSet();
            } else {
                i = this.fields.m966j() + this.unknownFields.getSerializedSize();
            }
            this.memoizedSize = i;
        }
        return i;
    }

    public Builder newBuilderForType() {
        return new Builder(null);
    }

    public Builder toBuilder() {
        return newBuilderForType().mergeFrom((Message) this);
    }

    private void verifyContainingType(FieldDescriptor fieldDescriptor) {
        if (fieldDescriptor.getContainingType() != this.type) {
            throw new IllegalArgumentException("FieldDescriptor does not match message type.");
        }
    }
}
