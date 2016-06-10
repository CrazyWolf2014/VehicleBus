package com.google.protobuf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.Descriptors.FieldDescriptor.Type;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;

public final class ExtensionRegistry extends ExtensionRegistryLite {
    private static final ExtensionRegistry EMPTY;
    private final Map<String, ExtensionInfo> extensionsByName;
    private final Map<C0220a, ExtensionInfo> extensionsByNumber;

    public static final class ExtensionInfo {
        public final Message defaultInstance;
        public final FieldDescriptor descriptor;

        private ExtensionInfo(FieldDescriptor fieldDescriptor) {
            this.descriptor = fieldDescriptor;
            this.defaultInstance = null;
        }

        private ExtensionInfo(FieldDescriptor fieldDescriptor, Message message) {
            this.descriptor = fieldDescriptor;
            this.defaultInstance = message;
        }
    }

    /* renamed from: com.google.protobuf.ExtensionRegistry.a */
    private static final class C0220a {
        private final Descriptor f867a;
        private final int f868b;

        C0220a(Descriptor descriptor, int i) {
            this.f867a = descriptor;
            this.f868b = i;
        }

        public int hashCode() {
            return (this.f867a.hashCode() * InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) + this.f868b;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0220a)) {
                return false;
            }
            C0220a c0220a = (C0220a) obj;
            if (this.f867a == c0220a.f867a && this.f868b == c0220a.f868b) {
                return true;
            }
            return false;
        }
    }

    public static ExtensionRegistry newInstance() {
        return new ExtensionRegistry();
    }

    public static ExtensionRegistry getEmptyRegistry() {
        return EMPTY;
    }

    public ExtensionRegistry getUnmodifiable() {
        return new ExtensionRegistry(this);
    }

    public ExtensionInfo findExtensionByName(String str) {
        return (ExtensionInfo) this.extensionsByName.get(str);
    }

    public ExtensionInfo findExtensionByNumber(Descriptor descriptor, int i) {
        return (ExtensionInfo) this.extensionsByNumber.get(new C0220a(descriptor, i));
    }

    public void add(GeneratedExtension<?, ?> generatedExtension) {
        if (generatedExtension.getDescriptor().getJavaType() != JavaType.MESSAGE) {
            add(new ExtensionInfo(null, null));
        } else if (generatedExtension.getMessageDefaultInstance() == null) {
            throw new IllegalStateException("Registered message-type extension had null default instance: " + generatedExtension.getDescriptor().getFullName());
        } else {
            add(new ExtensionInfo(generatedExtension.getMessageDefaultInstance(), null));
        }
    }

    public void add(FieldDescriptor fieldDescriptor) {
        if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
            throw new IllegalArgumentException("ExtensionRegistry.add() must be provided a default instance when adding an embedded message extension.");
        }
        add(new ExtensionInfo(null, null));
    }

    public void add(FieldDescriptor fieldDescriptor, Message message) {
        if (fieldDescriptor.getJavaType() != JavaType.MESSAGE) {
            throw new IllegalArgumentException("ExtensionRegistry.add() provided a default instance for a non-message extension.");
        }
        add(new ExtensionInfo(message, null));
    }

    private ExtensionRegistry() {
        this.extensionsByName = new HashMap();
        this.extensionsByNumber = new HashMap();
    }

    private ExtensionRegistry(ExtensionRegistry extensionRegistry) {
        super((ExtensionRegistryLite) extensionRegistry);
        this.extensionsByName = Collections.unmodifiableMap(extensionRegistry.extensionsByName);
        this.extensionsByNumber = Collections.unmodifiableMap(extensionRegistry.extensionsByNumber);
    }

    private ExtensionRegistry(boolean z) {
        super(ExtensionRegistryLite.getEmptyRegistry());
        this.extensionsByName = Collections.emptyMap();
        this.extensionsByNumber = Collections.emptyMap();
    }

    static {
        EMPTY = new ExtensionRegistry(true);
    }

    private void add(ExtensionInfo extensionInfo) {
        if (extensionInfo.descriptor.isExtension()) {
            this.extensionsByName.put(extensionInfo.descriptor.getFullName(), extensionInfo);
            this.extensionsByNumber.put(new C0220a(extensionInfo.descriptor.getContainingType(), extensionInfo.descriptor.getNumber()), extensionInfo);
            FieldDescriptor fieldDescriptor = extensionInfo.descriptor;
            if (fieldDescriptor.getContainingType().getOptions().getMessageSetWireFormat() && fieldDescriptor.getType() == Type.MESSAGE && fieldDescriptor.isOptional() && fieldDescriptor.getExtensionScope() == fieldDescriptor.getMessageType()) {
                this.extensionsByName.put(fieldDescriptor.getMessageType().getFullName(), extensionInfo);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("ExtensionRegistry.add() was given a FieldDescriptor for a regular (non-extension) field.");
    }
}
