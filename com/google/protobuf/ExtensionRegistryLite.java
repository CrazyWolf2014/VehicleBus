package com.google.protobuf;

import com.google.protobuf.GeneratedMessageLite.GeneratedExtension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;

public class ExtensionRegistryLite {
    private static final ExtensionRegistryLite EMPTY;
    private final Map<C0221a, GeneratedExtension<?, ?>> extensionsByNumber;

    /* renamed from: com.google.protobuf.ExtensionRegistryLite.a */
    private static final class C0221a {
        private final Object f869a;
        private final int f870b;

        C0221a(Object obj, int i) {
            this.f869a = obj;
            this.f870b = i;
        }

        public int hashCode() {
            return (System.identityHashCode(this.f869a) * InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) + this.f870b;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0221a)) {
                return false;
            }
            C0221a c0221a = (C0221a) obj;
            if (this.f869a == c0221a.f869a && this.f870b == c0221a.f870b) {
                return true;
            }
            return false;
        }
    }

    public static ExtensionRegistryLite newInstance() {
        return new ExtensionRegistryLite();
    }

    public static ExtensionRegistryLite getEmptyRegistry() {
        return EMPTY;
    }

    public ExtensionRegistryLite getUnmodifiable() {
        return new ExtensionRegistryLite(this);
    }

    public <ContainingType extends MessageLite> GeneratedExtension<ContainingType, ?> findLiteExtensionByNumber(ContainingType containingType, int i) {
        return (GeneratedExtension) this.extensionsByNumber.get(new C0221a(containingType, i));
    }

    public final void add(GeneratedExtension<?, ?> generatedExtension) {
        this.extensionsByNumber.put(new C0221a(generatedExtension.getContainingTypeDefaultInstance(), generatedExtension.getNumber()), generatedExtension);
    }

    ExtensionRegistryLite() {
        this.extensionsByNumber = new HashMap();
    }

    ExtensionRegistryLite(ExtensionRegistryLite extensionRegistryLite) {
        if (extensionRegistryLite == EMPTY) {
            this.extensionsByNumber = Collections.emptyMap();
        } else {
            this.extensionsByNumber = Collections.unmodifiableMap(extensionRegistryLite.extensionsByNumber);
        }
    }

    private ExtensionRegistryLite(boolean z) {
        this.extensionsByNumber = Collections.emptyMap();
    }

    static {
        EMPTY = new ExtensionRegistryLite(true);
    }
}
