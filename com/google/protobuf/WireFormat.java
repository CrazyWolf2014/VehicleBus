package com.google.protobuf;

/* renamed from: com.google.protobuf.k */
enum WireFormat extends FieldType {
    WireFormat(String str, int i, JavaType javaType, int i2) {
        super(i, javaType, i2, null);
    }

    public boolean isPackable() {
        return false;
    }
}
