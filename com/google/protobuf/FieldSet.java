package com.google.protobuf;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.google.protobuf.Internal.EnumLite;
import com.google.protobuf.Internal.EnumLiteMap;
import com.google.protobuf.MessageLite.Builder;
import com.google.protobuf.WireFormat.FieldType;
import com.google.protobuf.WireFormat.JavaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class FieldSet<FieldDescriptorType extends FieldDescriptorLite<FieldDescriptorType>> {
    private static final FieldSet f873c;
    private final SmallSortedMap<FieldDescriptorType, Object> f874a;
    private boolean f875b;

    /* renamed from: com.google.protobuf.FieldSet.1 */
    static /* synthetic */ class C02221 {
        static final /* synthetic */ int[] f871a;
        static final /* synthetic */ int[] f872b;

        static {
            f872b = new int[FieldType.values().length];
            try {
                f872b[FieldType.DOUBLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f872b[FieldType.FLOAT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f872b[FieldType.INT64.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f872b[FieldType.UINT64.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f872b[FieldType.INT32.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f872b[FieldType.FIXED64.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f872b[FieldType.FIXED32.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f872b[FieldType.BOOL.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f872b[FieldType.STRING.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                f872b[FieldType.BYTES.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                f872b[FieldType.UINT32.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                f872b[FieldType.SFIXED32.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                f872b[FieldType.SFIXED64.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                f872b[FieldType.SINT32.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                f872b[FieldType.SINT64.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                f872b[FieldType.GROUP.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                f872b[FieldType.MESSAGE.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                f872b[FieldType.ENUM.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            f871a = new int[JavaType.values().length];
            try {
                f871a[JavaType.INT.ordinal()] = 1;
            } catch (NoSuchFieldError e19) {
            }
            try {
                f871a[JavaType.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError e20) {
            }
            try {
                f871a[JavaType.FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError e21) {
            }
            try {
                f871a[JavaType.DOUBLE.ordinal()] = 4;
            } catch (NoSuchFieldError e22) {
            }
            try {
                f871a[JavaType.BOOLEAN.ordinal()] = 5;
            } catch (NoSuchFieldError e23) {
            }
            try {
                f871a[JavaType.STRING.ordinal()] = 6;
            } catch (NoSuchFieldError e24) {
            }
            try {
                f871a[JavaType.BYTE_STRING.ordinal()] = 7;
            } catch (NoSuchFieldError e25) {
            }
            try {
                f871a[JavaType.ENUM.ordinal()] = 8;
            } catch (NoSuchFieldError e26) {
            }
            try {
                f871a[JavaType.MESSAGE.ordinal()] = 9;
            } catch (NoSuchFieldError e27) {
            }
        }
    }

    public interface FieldDescriptorLite<T extends FieldDescriptorLite<T>> extends Comparable<T> {
        EnumLiteMap<?> getEnumType();

        JavaType getLiteJavaType();

        FieldType getLiteType();

        int getNumber();

        Builder internalMergeFrom(Builder builder, MessageLite messageLite);

        boolean isPacked();

        boolean isRepeated();
    }

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        return m961e();
    }

    private FieldSet() {
        this.f874a = SmallSortedMap.m1037a(16);
    }

    private FieldSet(boolean z) {
        this.f874a = SmallSortedMap.m1037a(0);
        m957c();
    }

    public static <T extends FieldDescriptorLite<T>> FieldSet<T> m935a() {
        return new FieldSet();
    }

    public static <T extends FieldDescriptorLite<T>> FieldSet<T> m944b() {
        return f873c;
    }

    static {
        f873c = new FieldSet(true);
    }

    public void m957c() {
        if (!this.f875b) {
            this.f874a.m1047a();
            this.f875b = true;
        }
    }

    public boolean m960d() {
        return this.f875b;
    }

    public FieldSet<FieldDescriptorType> m961e() {
        FieldSet<FieldDescriptorType> a = m935a();
        for (int i = 0; i < this.f874a.m1050c(); i++) {
            Entry b = this.f874a.m1048b(i);
            a.m951a((FieldDescriptorLite) b.getKey(), b.getValue());
        }
        for (Entry entry : this.f874a.m1051d()) {
            a.m951a((FieldDescriptorLite) entry.getKey(), entry.getValue());
        }
        return a;
    }

    public void m962f() {
        this.f874a.clear();
    }

    public Map<FieldDescriptorType, Object> m963g() {
        return this.f874a.m1049b() ? this.f874a : Collections.unmodifiableMap(this.f874a);
    }

    public Iterator<Entry<FieldDescriptorType, Object>> m964h() {
        return this.f874a.entrySet().iterator();
    }

    public boolean m953a(FieldDescriptorType fieldDescriptorType) {
        if (!fieldDescriptorType.isRepeated()) {
            return this.f874a.get(fieldDescriptorType) != null;
        } else {
            throw new IllegalArgumentException("hasField() can only be called on non-repeated fields.");
        }
    }

    public Object m954b(FieldDescriptorType fieldDescriptorType) {
        return this.f874a.get(fieldDescriptorType);
    }

    public void m951a(FieldDescriptorType fieldDescriptorType, Object obj) {
        if (!fieldDescriptorType.isRepeated()) {
            m940a(fieldDescriptorType.getLiteType(), obj);
        } else if (obj instanceof List) {
            List<Object> arrayList = new ArrayList();
            arrayList.addAll((List) obj);
            for (Object a : arrayList) {
                m940a(fieldDescriptorType.getLiteType(), a);
            }
            obj = arrayList;
        } else {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
        this.f874a.m1046a((Comparable) fieldDescriptorType, obj);
    }

    public void m958c(FieldDescriptorType fieldDescriptorType) {
        this.f874a.remove(fieldDescriptorType);
    }

    public int m959d(FieldDescriptorType fieldDescriptorType) {
        if (fieldDescriptorType.isRepeated()) {
            Object obj = this.f874a.get(fieldDescriptorType);
            if (obj == null) {
                return 0;
            }
            return ((List) obj).size();
        }
        throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
    }

    public Object m948a(FieldDescriptorType fieldDescriptorType, int i) {
        if (fieldDescriptorType.isRepeated()) {
            Object obj = this.f874a.get(fieldDescriptorType);
            if (obj != null) {
                return ((List) obj).get(i);
            }
            throw new IndexOutOfBoundsException();
        }
        throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
    }

    public void m950a(FieldDescriptorType fieldDescriptorType, int i, Object obj) {
        if (fieldDescriptorType.isRepeated()) {
            Object obj2 = this.f874a.get(fieldDescriptorType);
            if (obj2 == null) {
                throw new IndexOutOfBoundsException();
            }
            m940a(fieldDescriptorType.getLiteType(), obj);
            ((List) obj2).set(i, obj);
            return;
        }
        throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
    }

    public void m956b(FieldDescriptorType fieldDescriptorType, Object obj) {
        if (fieldDescriptorType.isRepeated()) {
            List arrayList;
            m940a(fieldDescriptorType.getLiteType(), obj);
            Object obj2 = this.f874a.get(fieldDescriptorType);
            if (obj2 == null) {
                arrayList = new ArrayList();
                this.f874a.m1046a((Comparable) fieldDescriptorType, (Object) arrayList);
            } else {
                arrayList = (List) obj2;
            }
            arrayList.add(obj);
            return;
        }
        throw new IllegalArgumentException("addRepeatedField() can only be called on repeated fields.");
    }

    private static void m940a(FieldType fieldType, Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        boolean z = false;
        switch (C02221.f871a[fieldType.getJavaType().ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                z = obj instanceof Integer;
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                z = obj instanceof Long;
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                z = obj instanceof Float;
                break;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                z = obj instanceof Double;
                break;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                z = obj instanceof Boolean;
                break;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                z = obj instanceof String;
                break;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                z = obj instanceof ByteString;
                break;
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                z = obj instanceof EnumLite;
                break;
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                z = obj instanceof MessageLite;
                break;
        }
        if (!z) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
    }

    public boolean m965i() {
        for (int i = 0; i < this.f874a.m1050c(); i++) {
            if (!m942a(this.f874a.m1048b(i))) {
                return false;
            }
        }
        for (Entry a : this.f874a.m1051d()) {
            if (!m942a(a)) {
                return false;
            }
        }
        return true;
    }

    private boolean m942a(Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite) entry.getKey();
        if (fieldDescriptorLite.getLiteJavaType() == JavaType.MESSAGE) {
            if (fieldDescriptorLite.isRepeated()) {
                for (MessageLite isInitialized : (List) entry.getValue()) {
                    if (!isInitialized.isInitialized()) {
                        return false;
                    }
                }
            } else if (!((MessageLite) entry.getValue()).isInitialized()) {
                return false;
            }
        }
        return true;
    }

    static int m934a(FieldType fieldType, boolean z) {
        if (z) {
            return 2;
        }
        return fieldType.getWireType();
    }

    public void m952a(FieldSet<FieldDescriptorType> fieldSet) {
        for (int i = 0; i < fieldSet.f874a.m1050c(); i++) {
            m945b(fieldSet.f874a.m1048b(i));
        }
        for (Entry b : fieldSet.f874a.m1051d()) {
            m945b(b);
        }
    }

    private void m945b(Entry<FieldDescriptorType, Object> entry) {
        Comparable comparable = (FieldDescriptorLite) entry.getKey();
        Object value = entry.getValue();
        Object obj;
        if (comparable.isRepeated()) {
            obj = this.f874a.get(comparable);
            if (obj == null) {
                this.f874a.m1046a(comparable, new ArrayList((List) value));
            } else {
                ((List) obj).addAll((List) value);
            }
        } else if (comparable.getLiteJavaType() == JavaType.MESSAGE) {
            obj = this.f874a.get(comparable);
            if (obj == null) {
                this.f874a.m1046a(comparable, value);
            } else {
                this.f874a.m1046a(comparable, comparable.internalMergeFrom(((MessageLite) obj).toBuilder(), (MessageLite) value).build());
            }
        } else {
            this.f874a.m1046a(comparable, value);
        }
    }

    public static Object m936a(CodedInputStream codedInputStream, FieldType fieldType) throws IOException {
        switch (C02221.f872b[fieldType.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return Double.valueOf(codedInputStream.readDouble());
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return Float.valueOf(codedInputStream.readFloat());
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return Long.valueOf(codedInputStream.readInt64());
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return Long.valueOf(codedInputStream.readUInt64());
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                return Integer.valueOf(codedInputStream.readInt32());
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return Long.valueOf(codedInputStream.readFixed64());
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                return Integer.valueOf(codedInputStream.readFixed32());
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return Boolean.valueOf(codedInputStream.readBool());
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                return codedInputStream.readString();
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                return codedInputStream.readBytes();
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                return Integer.valueOf(codedInputStream.readUInt32());
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                return Integer.valueOf(codedInputStream.readSFixed32());
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                return Long.valueOf(codedInputStream.readSFixed64());
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                return Integer.valueOf(codedInputStream.readSInt32());
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                return Long.valueOf(codedInputStream.readSInt64());
            case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle nested groups.");
            case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle embedded messages.");
            case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle enums.");
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    public void m949a(CodedOutputStream codedOutputStream) throws IOException {
        for (int i = 0; i < this.f874a.m1050c(); i++) {
            Entry b = this.f874a.m1048b(i);
            m939a((FieldDescriptorLite) b.getKey(), b.getValue(), codedOutputStream);
        }
        for (Entry entry : this.f874a.m1051d()) {
            m939a((FieldDescriptorLite) entry.getKey(), entry.getValue(), codedOutputStream);
        }
    }

    public void m955b(CodedOutputStream codedOutputStream) throws IOException {
        for (int i = 0; i < this.f874a.m1050c(); i++) {
            m941a(this.f874a.m1048b(i), codedOutputStream);
        }
        for (Entry a : this.f874a.m1051d()) {
            m941a(a, codedOutputStream);
        }
    }

    private void m941a(Entry<FieldDescriptorType, Object> entry, CodedOutputStream codedOutputStream) throws IOException {
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite) entry.getKey();
        if (fieldDescriptorLite.getLiteJavaType() != JavaType.MESSAGE || fieldDescriptorLite.isRepeated() || fieldDescriptorLite.isPacked()) {
            m939a(fieldDescriptorLite, entry.getValue(), codedOutputStream);
        } else {
            codedOutputStream.writeMessageSetExtension(((FieldDescriptorLite) entry.getKey()).getNumber(), (MessageLite) entry.getValue());
        }
    }

    private static void m937a(CodedOutputStream codedOutputStream, FieldType fieldType, int i, Object obj) throws IOException {
        if (fieldType == FieldType.GROUP) {
            codedOutputStream.writeGroup(i, (MessageLite) obj);
            return;
        }
        codedOutputStream.writeTag(i, m934a(fieldType, false));
        m938a(codedOutputStream, fieldType, obj);
    }

    private static void m938a(CodedOutputStream codedOutputStream, FieldType fieldType, Object obj) throws IOException {
        switch (C02221.f872b[fieldType.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                codedOutputStream.writeDoubleNoTag(((Double) obj).doubleValue());
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                codedOutputStream.writeFloatNoTag(((Float) obj).floatValue());
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                codedOutputStream.writeInt64NoTag(((Long) obj).longValue());
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                codedOutputStream.writeUInt64NoTag(((Long) obj).longValue());
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                codedOutputStream.writeInt32NoTag(((Integer) obj).intValue());
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                codedOutputStream.writeFixed64NoTag(((Long) obj).longValue());
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                codedOutputStream.writeFixed32NoTag(((Integer) obj).intValue());
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                codedOutputStream.writeBoolNoTag(((Boolean) obj).booleanValue());
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                codedOutputStream.writeStringNoTag((String) obj);
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                codedOutputStream.writeBytesNoTag((ByteString) obj);
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                codedOutputStream.writeUInt32NoTag(((Integer) obj).intValue());
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                codedOutputStream.writeSFixed32NoTag(((Integer) obj).intValue());
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                codedOutputStream.writeSFixed64NoTag(((Long) obj).longValue());
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                codedOutputStream.writeSInt32NoTag(((Integer) obj).intValue());
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                codedOutputStream.writeSInt64NoTag(((Long) obj).longValue());
            case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                codedOutputStream.writeGroupNoTag((MessageLite) obj);
            case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                codedOutputStream.writeMessageNoTag((MessageLite) obj);
            case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                codedOutputStream.writeEnumNoTag(((EnumLite) obj).getNumber());
            default:
        }
    }

    public static void m939a(FieldDescriptorLite<?> fieldDescriptorLite, Object obj, CodedOutputStream codedOutputStream) throws IOException {
        FieldType liteType = fieldDescriptorLite.getLiteType();
        int number = fieldDescriptorLite.getNumber();
        if (fieldDescriptorLite.isRepeated()) {
            List<Object> list = (List) obj;
            if (fieldDescriptorLite.isPacked()) {
                codedOutputStream.writeTag(number, 2);
                number = 0;
                for (Object b : list) {
                    number += m943b(liteType, b);
                }
                codedOutputStream.writeRawVarint32(number);
                for (Object a : list) {
                    m938a(codedOutputStream, liteType, a);
                }
                return;
            }
            for (Object b2 : list) {
                m937a(codedOutputStream, liteType, number, b2);
            }
            return;
        }
        m937a(codedOutputStream, liteType, number, obj);
    }

    public int m966j() {
        int i = 0;
        for (int i2 = 0; i2 < this.f874a.m1050c(); i2++) {
            Entry b = this.f874a.m1048b(i2);
            i += m946c((FieldDescriptorLite) b.getKey(), b.getValue());
        }
        for (Entry entry : this.f874a.m1051d()) {
            i += m946c((FieldDescriptorLite) entry.getKey(), entry.getValue());
        }
        return i;
    }

    public int m967k() {
        int i = 0;
        int i2 = 0;
        while (i < this.f874a.m1050c()) {
            i2 += m947c(this.f874a.m1048b(i));
            i++;
        }
        for (Entry c : this.f874a.m1051d()) {
            i2 += m947c(c);
        }
        return i2;
    }

    private int m947c(Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite) entry.getKey();
        if (fieldDescriptorLite.getLiteJavaType() != JavaType.MESSAGE || fieldDescriptorLite.isRepeated() || fieldDescriptorLite.isPacked()) {
            return m946c(fieldDescriptorLite, entry.getValue());
        }
        return CodedOutputStream.computeMessageSetExtensionSize(((FieldDescriptorLite) entry.getKey()).getNumber(), (MessageLite) entry.getValue());
    }

    private static int m933a(FieldType fieldType, int i, Object obj) {
        int computeTagSize = CodedOutputStream.computeTagSize(i);
        if (fieldType == FieldType.GROUP) {
            computeTagSize *= 2;
        }
        return computeTagSize + m943b(fieldType, obj);
    }

    private static int m943b(FieldType fieldType, Object obj) {
        switch (C02221.f872b[fieldType.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return CodedOutputStream.computeDoubleSizeNoTag(((Double) obj).doubleValue());
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return CodedOutputStream.computeFloatSizeNoTag(((Float) obj).floatValue());
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return CodedOutputStream.computeInt64SizeNoTag(((Long) obj).longValue());
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return CodedOutputStream.computeUInt64SizeNoTag(((Long) obj).longValue());
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                return CodedOutputStream.computeInt32SizeNoTag(((Integer) obj).intValue());
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return CodedOutputStream.computeFixed64SizeNoTag(((Long) obj).longValue());
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                return CodedOutputStream.computeFixed32SizeNoTag(((Integer) obj).intValue());
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return CodedOutputStream.computeBoolSizeNoTag(((Boolean) obj).booleanValue());
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                return CodedOutputStream.computeStringSizeNoTag((String) obj);
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                return CodedOutputStream.computeBytesSizeNoTag((ByteString) obj);
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                return CodedOutputStream.computeUInt32SizeNoTag(((Integer) obj).intValue());
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                return CodedOutputStream.computeSFixed32SizeNoTag(((Integer) obj).intValue());
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                return CodedOutputStream.computeSFixed64SizeNoTag(((Long) obj).longValue());
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                return CodedOutputStream.computeSInt32SizeNoTag(((Integer) obj).intValue());
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                return CodedOutputStream.computeSInt64SizeNoTag(((Long) obj).longValue());
            case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                return CodedOutputStream.computeGroupSizeNoTag((MessageLite) obj);
            case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                return CodedOutputStream.computeMessageSizeNoTag((MessageLite) obj);
            case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                return CodedOutputStream.computeEnumSizeNoTag(((EnumLite) obj).getNumber());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    public static int m946c(FieldDescriptorLite<?> fieldDescriptorLite, Object obj) {
        int i = 0;
        FieldType liteType = fieldDescriptorLite.getLiteType();
        int number = fieldDescriptorLite.getNumber();
        if (!fieldDescriptorLite.isRepeated()) {
            return m933a(liteType, number, obj);
        }
        if (fieldDescriptorLite.isPacked()) {
            for (Object b : (List) obj) {
                i += m943b(liteType, b);
            }
            return CodedOutputStream.computeRawVarint32Size(i) + (CodedOutputStream.computeTagSize(number) + i);
        }
        for (Object b2 : (List) obj) {
            i += m933a(liteType, number, b2);
        }
        return i;
    }
}
