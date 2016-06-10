package com.google.protobuf;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.Descriptors.FieldDescriptor.Type;
import com.google.protobuf.ExtensionRegistry.ExtensionInfo;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.UnknownFieldSet.Field;
import com.ifoer.mine.Contact;
import com.tencent.mm.sdk.platformtools.MAlarmHandler;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.codehaus.jackson.smile.SmileConstants;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.ksoap2.SoapEnvelope;
import org.xbill.DNS.Flags;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.TTL;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public final class TextFormat {
    private static final int BUFFER_SIZE = 4096;
    private static final C0233b DEFAULT_PRINTER;
    private static final C0233b SINGLE_LINE_PRINTER;

    /* renamed from: com.google.protobuf.TextFormat.1 */
    static /* synthetic */ class C02311 {
        static final /* synthetic */ int[] f883a;

        static {
            f883a = new int[Type.values().length];
            try {
                f883a[Type.INT32.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f883a[Type.SINT32.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f883a[Type.SFIXED32.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f883a[Type.INT64.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f883a[Type.SINT64.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f883a[Type.SFIXED64.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f883a[Type.BOOL.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f883a[Type.FLOAT.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f883a[Type.DOUBLE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                f883a[Type.UINT32.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                f883a[Type.FIXED32.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                f883a[Type.UINT64.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                f883a[Type.FIXED64.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                f883a[Type.STRING.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                f883a[Type.BYTES.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                f883a[Type.ENUM.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                f883a[Type.MESSAGE.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                f883a[Type.GROUP.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
        }
    }

    public static class ParseException extends IOException {
        private static final long serialVersionUID = 3196188060225107702L;

        public ParseException(String str) {
            super(str);
        }
    }

    /* renamed from: com.google.protobuf.TextFormat.a */
    static class C0232a extends IOException {
        C0232a(String str) {
            super(str);
        }
    }

    /* renamed from: com.google.protobuf.TextFormat.b */
    private static final class C0233b {
        final boolean f884a;

        private C0233b(boolean z) {
            this.f884a = z;
        }

        private void m990a(Message message, C0234c c0234c) throws IOException {
            for (Entry entry : message.getAllFields().entrySet()) {
                m989a((FieldDescriptor) entry.getKey(), entry.getValue(), c0234c);
            }
            m994a(message.getUnknownFields(), c0234c);
        }

        private void m989a(FieldDescriptor fieldDescriptor, Object obj, C0234c c0234c) throws IOException {
            if (fieldDescriptor.isRepeated()) {
                for (Object b : (List) obj) {
                    m995b(fieldDescriptor, b, c0234c);
                }
                return;
            }
            m995b(fieldDescriptor, obj, c0234c);
        }

        private void m995b(FieldDescriptor fieldDescriptor, Object obj, C0234c c0234c) throws IOException {
            if (fieldDescriptor.isExtension()) {
                c0234c.m1000a("[");
                if (fieldDescriptor.getContainingType().getOptions().getMessageSetWireFormat() && fieldDescriptor.getType() == Type.MESSAGE && fieldDescriptor.isOptional() && fieldDescriptor.getExtensionScope() == fieldDescriptor.getMessageType()) {
                    c0234c.m1000a(fieldDescriptor.getMessageType().getFullName());
                } else {
                    c0234c.m1000a(fieldDescriptor.getFullName());
                }
                c0234c.m1000a("]");
            } else if (fieldDescriptor.getType() == Type.GROUP) {
                c0234c.m1000a(fieldDescriptor.getMessageType().getName());
            } else {
                c0234c.m1000a(fieldDescriptor.getName());
            }
            if (fieldDescriptor.getJavaType() != JavaType.MESSAGE) {
                c0234c.m1000a(": ");
            } else if (this.f884a) {
                c0234c.m1000a(" { ");
            } else {
                c0234c.m1000a(" {\n");
                c0234c.m999a();
            }
            m997c(fieldDescriptor, obj, c0234c);
            if (fieldDescriptor.getJavaType() == JavaType.MESSAGE) {
                if (this.f884a) {
                    c0234c.m1000a("} ");
                    return;
                }
                c0234c.m1001b();
                c0234c.m1000a("}\n");
            } else if (this.f884a) {
                c0234c.m1000a(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            } else {
                c0234c.m1000a(SpecilApiUtil.LINE_SEP);
            }
        }

        private void m997c(FieldDescriptor fieldDescriptor, Object obj, C0234c c0234c) throws IOException {
            switch (C02311.f883a[fieldDescriptor.getType().ordinal()]) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    c0234c.m1000a(((Integer) obj).toString());
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    c0234c.m1000a(((Long) obj).toString());
                case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                    c0234c.m1000a(((Boolean) obj).toString());
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    c0234c.m1000a(((Float) obj).toString());
                case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                    c0234c.m1000a(((Double) obj).toString());
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                    c0234c.m1000a(TextFormat.unsignedToString(((Integer) obj).intValue()));
                case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                    c0234c.m1000a(TextFormat.unsignedToString(((Long) obj).longValue()));
                case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                    c0234c.m1000a("\"");
                    c0234c.m1000a(TextFormat.escapeText((String) obj));
                    c0234c.m1000a("\"");
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    c0234c.m1000a("\"");
                    c0234c.m1000a(TextFormat.escapeBytes((ByteString) obj));
                    c0234c.m1000a("\"");
                case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                    c0234c.m1000a(((EnumValueDescriptor) obj).getName());
                case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                    m990a((Message) obj, c0234c);
                default:
            }
        }

        private void m994a(UnknownFieldSet unknownFieldSet, C0234c c0234c) throws IOException {
            for (Entry entry : unknownFieldSet.asMap().entrySet()) {
                int intValue = ((Integer) entry.getKey()).intValue();
                Field field = (Field) entry.getValue();
                m988a(intValue, 0, field.getVarintList(), c0234c);
                m988a(intValue, 5, field.getFixed32List(), c0234c);
                m988a(intValue, 1, field.getFixed64List(), c0234c);
                m988a(intValue, 2, field.getLengthDelimitedList(), c0234c);
                for (UnknownFieldSet unknownFieldSet2 : field.getGroupList()) {
                    c0234c.m1000a(((Integer) entry.getKey()).toString());
                    if (this.f884a) {
                        c0234c.m1000a(" { ");
                    } else {
                        c0234c.m1000a(" {\n");
                        c0234c.m999a();
                    }
                    m994a(unknownFieldSet2, c0234c);
                    if (this.f884a) {
                        c0234c.m1000a("} ");
                    } else {
                        c0234c.m1001b();
                        c0234c.m1000a("}\n");
                    }
                }
            }
        }

        private void m988a(int i, int i2, List<?> list, C0234c c0234c) throws IOException {
            for (Object next : list) {
                c0234c.m1000a(String.valueOf(i));
                c0234c.m1000a(": ");
                TextFormat.printUnknownFieldValue(i2, next, c0234c);
                c0234c.m1000a(this.f884a ? MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR : SpecilApiUtil.LINE_SEP);
            }
        }
    }

    /* renamed from: com.google.protobuf.TextFormat.c */
    private static final class C0234c {
        private final Appendable f885a;
        private final StringBuilder f886b;
        private boolean f887c;

        private C0234c(Appendable appendable) {
            this.f886b = new StringBuilder();
            this.f887c = true;
            this.f885a = appendable;
        }

        public void m999a() {
            this.f886b.append("  ");
        }

        public void m1001b() {
            int length = this.f886b.length();
            if (length == 0) {
                throw new IllegalArgumentException(" Outdent() without matching Indent().");
            }
            this.f886b.delete(length - 2, length);
        }

        public void m1000a(CharSequence charSequence) throws IOException {
            int i = 0;
            int length = charSequence.length();
            for (int i2 = 0; i2 < length; i2++) {
                if (charSequence.charAt(i2) == '\n') {
                    m998a(charSequence.subSequence(i, length), (i2 - i) + 1);
                    i = i2 + 1;
                    this.f887c = true;
                }
            }
            m998a(charSequence.subSequence(i, length), length - i);
        }

        private void m998a(CharSequence charSequence, int i) throws IOException {
            if (i != 0) {
                if (this.f887c) {
                    this.f887c = false;
                    this.f885a.append(this.f886b);
                }
                this.f885a.append(charSequence);
            }
        }
    }

    /* renamed from: com.google.protobuf.TextFormat.d */
    private static final class C0235d {
        private static final Pattern f888i;
        private static final Pattern f889j;
        private static final Pattern f890k;
        private static final Pattern f891l;
        private static final Pattern f892m;
        private final CharSequence f893a;
        private final Matcher f894b;
        private String f895c;
        private int f896d;
        private int f897e;
        private int f898f;
        private int f899g;
        private int f900h;

        static {
            f888i = Pattern.compile("(\\s|(#.*$))++", 8);
            f889j = Pattern.compile("[a-zA-Z_][0-9a-zA-Z_+-]*+|[.]?[0-9+-][0-9a-zA-Z_.+-]*+|\"([^\"\n\\\\]|\\\\.)*+(\"|\\\\?$)|'([^'\n\\\\]|\\\\.)*+('|\\\\?$)", 8);
            f890k = Pattern.compile("-?inf(inity)?", 2);
            f891l = Pattern.compile("-?inf(inity)?f?", 2);
            f892m = Pattern.compile("nanf?", 2);
        }

        private C0235d(CharSequence charSequence) {
            this.f896d = 0;
            this.f897e = 0;
            this.f898f = 0;
            this.f899g = 0;
            this.f900h = 0;
            this.f893a = charSequence;
            this.f894b = f888i.matcher(charSequence);
            m1005n();
            m1008b();
        }

        public boolean m1006a() {
            return this.f895c.length() == 0;
        }

        public void m1008b() {
            this.f899g = this.f897e;
            this.f900h = this.f898f;
            while (this.f896d < this.f894b.regionStart()) {
                if (this.f893a.charAt(this.f896d) == '\n') {
                    this.f897e++;
                    this.f898f = 0;
                } else {
                    this.f898f++;
                }
                this.f896d++;
            }
            if (this.f894b.regionStart() == this.f894b.regionEnd()) {
                this.f895c = XmlPullParser.NO_NAMESPACE;
                return;
            }
            this.f894b.usePattern(f889j);
            if (this.f894b.lookingAt()) {
                this.f895c = this.f894b.group();
                this.f894b.region(this.f894b.end(), this.f894b.regionEnd());
            } else {
                this.f895c = String.valueOf(this.f893a.charAt(this.f896d));
                this.f894b.region(this.f896d + 1, this.f894b.regionEnd());
            }
            m1005n();
        }

        private void m1005n() {
            this.f894b.usePattern(f888i);
            if (this.f894b.lookingAt()) {
                this.f894b.region(this.f894b.end(), this.f894b.regionEnd());
            }
        }

        public boolean m1007a(String str) {
            if (!this.f895c.equals(str)) {
                return false;
            }
            m1008b();
            return true;
        }

        public void m1009b(String str) throws ParseException {
            if (!m1007a(str)) {
                throw m1010c("Expected \"" + str + "\".");
            }
        }

        public boolean m1011c() {
            if (this.f895c.length() == 0) {
                return false;
            }
            char charAt = this.f895c.charAt(0);
            if (('0' <= charAt && charAt <= '9') || charAt == SignatureVisitor.SUPER || charAt == SignatureVisitor.EXTENDS) {
                return true;
            }
            return false;
        }

        public String m1013d() throws ParseException {
            for (int i = 0; i < this.f895c.length(); i++) {
                char charAt = this.f895c.charAt(i);
                if (('a' > charAt || charAt > 'z') && (('A' > charAt || charAt > 'Z') && !(('0' <= charAt && charAt <= '9') || charAt == '_' || charAt == '.'))) {
                    throw m1010c("Expected identifier.");
                }
            }
            String str = this.f895c;
            m1008b();
            return str;
        }

        public int m1014e() throws ParseException {
            try {
                int parseInt32 = TextFormat.parseInt32(this.f895c);
                m1008b();
                return parseInt32;
            } catch (NumberFormatException e) {
                throw m1002a(e);
            }
        }

        public int m1015f() throws ParseException {
            try {
                int parseUInt32 = TextFormat.parseUInt32(this.f895c);
                m1008b();
                return parseUInt32;
            } catch (NumberFormatException e) {
                throw m1002a(e);
            }
        }

        public long m1016g() throws ParseException {
            try {
                long parseInt64 = TextFormat.parseInt64(this.f895c);
                m1008b();
                return parseInt64;
            } catch (NumberFormatException e) {
                throw m1002a(e);
            }
        }

        public long m1017h() throws ParseException {
            try {
                long parseUInt64 = TextFormat.parseUInt64(this.f895c);
                m1008b();
                return parseUInt64;
            } catch (NumberFormatException e) {
                throw m1002a(e);
            }
        }

        public double m1018i() throws ParseException {
            if (f890k.matcher(this.f895c).matches()) {
                boolean startsWith = this.f895c.startsWith("-");
                m1008b();
                return startsWith ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            } else if (this.f895c.equalsIgnoreCase("nan")) {
                m1008b();
                return Double.NaN;
            } else {
                try {
                    double parseDouble = Double.parseDouble(this.f895c);
                    m1008b();
                    return parseDouble;
                } catch (NumberFormatException e) {
                    throw m1004b(e);
                }
            }
        }

        public float m1019j() throws ParseException {
            if (f891l.matcher(this.f895c).matches()) {
                boolean startsWith = this.f895c.startsWith("-");
                m1008b();
                return startsWith ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
            } else if (f892m.matcher(this.f895c).matches()) {
                m1008b();
                return Float.NaN;
            } else {
                try {
                    float parseFloat = Float.parseFloat(this.f895c);
                    m1008b();
                    return parseFloat;
                } catch (NumberFormatException e) {
                    throw m1004b(e);
                }
            }
        }

        public boolean m1020k() throws ParseException {
            if (this.f895c.equals("true") || this.f895c.equals("t") || this.f895c.equals(Contact.RELATION_FRIEND)) {
                m1008b();
                return true;
            } else if (this.f895c.equals("false") || this.f895c.equals("f") || this.f895c.equals(Contact.RELATION_ASK)) {
                m1008b();
                return false;
            } else {
                throw m1010c("Expected \"true\" or \"false\".");
            }
        }

        public String m1021l() throws ParseException {
            return m1022m().toStringUtf8();
        }

        public ByteString m1022m() throws ParseException {
            List arrayList = new ArrayList();
            m1003a(arrayList);
            while (true) {
                if (!this.f895c.startsWith("'") && !this.f895c.startsWith("\"")) {
                    return ByteString.copyFrom(arrayList);
                }
                m1003a(arrayList);
            }
        }

        private void m1003a(List<ByteString> list) throws ParseException {
            char c = '\u0000';
            if (this.f895c.length() > 0) {
                c = this.f895c.charAt(0);
            }
            if (c != '\"' && c != '\'') {
                throw m1010c("Expected string.");
            } else if (this.f895c.length() < 2 || this.f895c.charAt(this.f895c.length() - 1) != c) {
                throw m1010c("String missing ending quote.");
            } else {
                try {
                    ByteString unescapeBytes = TextFormat.unescapeBytes(this.f895c.substring(1, this.f895c.length() - 1));
                    m1008b();
                    list.add(unescapeBytes);
                } catch (C0232a e) {
                    throw m1010c(e.getMessage());
                }
            }
        }

        public ParseException m1010c(String str) {
            return new ParseException((this.f897e + 1) + ":" + (this.f898f + 1) + ": " + str);
        }

        public ParseException m1012d(String str) {
            return new ParseException((this.f899g + 1) + ":" + (this.f900h + 1) + ": " + str);
        }

        private ParseException m1002a(NumberFormatException numberFormatException) {
            return m1010c("Couldn't parse integer: " + numberFormatException.getMessage());
        }

        private ParseException m1004b(NumberFormatException numberFormatException) {
            return m1010c("Couldn't parse number: " + numberFormatException.getMessage());
        }
    }

    private TextFormat() {
    }

    static {
        DEFAULT_PRINTER = new C0233b(null);
        SINGLE_LINE_PRINTER = new C0233b(null);
    }

    public static void print(Message message, Appendable appendable) throws IOException {
        DEFAULT_PRINTER.m990a(message, new C0234c(null));
    }

    public static void print(UnknownFieldSet unknownFieldSet, Appendable appendable) throws IOException {
        DEFAULT_PRINTER.m994a(unknownFieldSet, new C0234c(null));
    }

    public static String shortDebugString(Message message) {
        try {
            Appendable stringBuilder = new StringBuilder();
            SINGLE_LINE_PRINTER.m990a(message, new C0234c(null));
            return stringBuilder.toString().trim();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static String shortDebugString(UnknownFieldSet unknownFieldSet) {
        try {
            Appendable stringBuilder = new StringBuilder();
            SINGLE_LINE_PRINTER.m994a(unknownFieldSet, new C0234c(null));
            return stringBuilder.toString().trim();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static String printToString(Message message) {
        try {
            Appendable stringBuilder = new StringBuilder();
            print(message, stringBuilder);
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static String printToString(UnknownFieldSet unknownFieldSet) {
        try {
            Appendable stringBuilder = new StringBuilder();
            print(unknownFieldSet, stringBuilder);
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static void printField(FieldDescriptor fieldDescriptor, Object obj, Appendable appendable) throws IOException {
        DEFAULT_PRINTER.m989a(fieldDescriptor, obj, new C0234c(null));
    }

    public static String printFieldToString(FieldDescriptor fieldDescriptor, Object obj) {
        try {
            Appendable stringBuilder = new StringBuilder();
            printField(fieldDescriptor, obj, stringBuilder);
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static void printFieldValue(FieldDescriptor fieldDescriptor, Object obj, Appendable appendable) throws IOException {
        DEFAULT_PRINTER.m997c(fieldDescriptor, obj, new C0234c(null));
    }

    public static void printUnknownFieldValue(int i, Object obj, Appendable appendable) throws IOException {
        printUnknownFieldValue(i, obj, new C0234c(null));
    }

    private static void printUnknownFieldValue(int i, Object obj, C0234c c0234c) throws IOException {
        switch (WireFormat.getTagWireType(i)) {
            case KEYRecord.OWNER_USER /*0*/:
                c0234c.m1000a(unsignedToString(((Long) obj).longValue()));
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                c0234c.m1000a(String.format((Locale) null, "0x%016x", new Object[]{(Long) obj}));
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                c0234c.m1000a("\"");
                c0234c.m1000a(escapeBytes((ByteString) obj));
                c0234c.m1000a("\"");
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                DEFAULT_PRINTER.m994a((UnknownFieldSet) obj, c0234c);
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                c0234c.m1000a(String.format((Locale) null, "0x%08x", new Object[]{(Integer) obj}));
            default:
                throw new IllegalArgumentException("Bad tag: " + i);
        }
    }

    private static String unsignedToString(int i) {
        if (i >= 0) {
            return Integer.toString(i);
        }
        return Long.toString(((long) i) & Util.MAX_32BIT_VALUE);
    }

    private static String unsignedToString(long j) {
        if (j >= 0) {
            return Long.toString(j);
        }
        return BigInteger.valueOf(MAlarmHandler.NEXT_FIRE_INTERVAL & j).setBit(63).toString();
    }

    public static void merge(Readable readable, Builder builder) throws IOException {
        merge(readable, ExtensionRegistry.getEmptyRegistry(), builder);
    }

    public static void merge(CharSequence charSequence, Builder builder) throws ParseException {
        merge(charSequence, ExtensionRegistry.getEmptyRegistry(), builder);
    }

    public static void merge(Readable readable, ExtensionRegistry extensionRegistry, Builder builder) throws IOException {
        merge(toStringBuilder(readable), extensionRegistry, builder);
    }

    private static StringBuilder toStringBuilder(Readable readable) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        CharSequence allocate = CharBuffer.allocate(BUFFER_SIZE);
        while (true) {
            int read = readable.read(allocate);
            if (read == -1) {
                return stringBuilder;
            }
            allocate.flip();
            stringBuilder.append(allocate, 0, read);
        }
    }

    public static void merge(CharSequence charSequence, ExtensionRegistry extensionRegistry, Builder builder) throws ParseException {
        C0235d c0235d = new C0235d(null);
        while (!c0235d.m1006a()) {
            mergeField(c0235d, extensionRegistry, builder);
        }
    }

    private static void mergeField(C0235d c0235d, ExtensionRegistry extensionRegistry, Builder builder) throws ParseException {
        ExtensionInfo findExtensionByName;
        FieldDescriptor fieldDescriptor;
        String d;
        Object obj = null;
        Descriptor descriptorForType = builder.getDescriptorForType();
        if (c0235d.m1007a("[")) {
            StringBuilder stringBuilder = new StringBuilder(c0235d.m1013d());
            while (c0235d.m1007a(".")) {
                stringBuilder.append('.');
                stringBuilder.append(c0235d.m1013d());
            }
            findExtensionByName = extensionRegistry.findExtensionByName(stringBuilder.toString());
            if (findExtensionByName == null) {
                throw c0235d.m1012d("Extension \"" + stringBuilder + "\" not found in the ExtensionRegistry.");
            } else if (findExtensionByName.descriptor.getContainingType() != descriptorForType) {
                throw c0235d.m1012d("Extension \"" + stringBuilder + "\" does not extend message type \"" + descriptorForType.getFullName() + "\".");
            } else {
                c0235d.m1009b("]");
                fieldDescriptor = findExtensionByName.descriptor;
            }
        } else {
            d = c0235d.m1013d();
            FieldDescriptor findFieldByName = descriptorForType.findFieldByName(d);
            if (findFieldByName == null) {
                findFieldByName = descriptorForType.findFieldByName(d.toLowerCase(Locale.US));
                if (!(findFieldByName == null || findFieldByName.getType() == Type.GROUP)) {
                    findFieldByName = null;
                }
            }
            if (!(findFieldByName == null || findFieldByName.getType() != Type.GROUP || findFieldByName.getMessageType().getName().equals(d))) {
                findFieldByName = null;
            }
            if (findFieldByName == null) {
                throw c0235d.m1012d("Message type \"" + descriptorForType.getFullName() + "\" has no field named \"" + d + "\".");
            }
            fieldDescriptor = findFieldByName;
            findExtensionByName = null;
        }
        if (fieldDescriptor.getJavaType() != JavaType.MESSAGE) {
            c0235d.m1009b(":");
            switch (C02311.f883a[fieldDescriptor.getType().ordinal()]) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    obj = Integer.valueOf(c0235d.m1014e());
                    break;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    obj = Long.valueOf(c0235d.m1016g());
                    break;
                case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                    obj = Boolean.valueOf(c0235d.m1020k());
                    break;
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    obj = Float.valueOf(c0235d.m1019j());
                    break;
                case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                    obj = Double.valueOf(c0235d.m1018i());
                    break;
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                    obj = Integer.valueOf(c0235d.m1015f());
                    break;
                case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                    obj = Long.valueOf(c0235d.m1017h());
                    break;
                case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                    obj = c0235d.m1021l();
                    break;
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    obj = c0235d.m1022m();
                    break;
                case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                    EnumDescriptor enumType = fieldDescriptor.getEnumType();
                    if (c0235d.m1011c()) {
                        int e = c0235d.m1014e();
                        obj = enumType.findValueByNumber(e);
                        if (obj == null) {
                            throw c0235d.m1012d("Enum type \"" + enumType.getFullName() + "\" has no value with number " + e + '.');
                        }
                    }
                    d = c0235d.m1013d();
                    obj = enumType.findValueByName(d);
                    if (obj == null) {
                        throw c0235d.m1012d("Enum type \"" + enumType.getFullName() + "\" has no value named \"" + d + "\".");
                    }
                    break;
                case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                    throw new RuntimeException("Can't get here.");
                default:
                    break;
            }
        }
        String str;
        Builder newBuilderForField;
        c0235d.m1007a(":");
        if (c0235d.m1007a("<")) {
            str = ">";
        } else {
            c0235d.m1009b("{");
            str = "}";
        }
        if (findExtensionByName == null) {
            newBuilderForField = builder.newBuilderForField(fieldDescriptor);
        } else {
            newBuilderForField = findExtensionByName.defaultInstance.newBuilderForType();
        }
        while (!c0235d.m1007a(str)) {
            if (c0235d.m1006a()) {
                throw c0235d.m1010c("Expected \"" + str + "\".");
            }
            mergeField(c0235d, extensionRegistry, newBuilderForField);
        }
        obj = newBuilderForField.build();
        if (fieldDescriptor.isRepeated()) {
            builder.addRepeatedField(fieldDescriptor, obj);
        } else {
            builder.setField(fieldDescriptor, obj);
        }
    }

    static String escapeBytes(ByteString byteString) {
        StringBuilder stringBuilder = new StringBuilder(byteString.size());
        for (int i = 0; i < byteString.size(); i++) {
            byte byteAt = byteString.byteAt(i);
            switch (byteAt) {
                case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                    stringBuilder.append("\\a");
                    break;
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    stringBuilder.append("\\b");
                    break;
                case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                    stringBuilder.append("\\t");
                    break;
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                    stringBuilder.append("\\n");
                    break;
                case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                    stringBuilder.append("\\v");
                    break;
                case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                    stringBuilder.append("\\f");
                    break;
                case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                    stringBuilder.append("\\r");
                    break;
                case org.xbill.DNS.Type.ATMA /*34*/:
                    stringBuilder.append("\\\"");
                    break;
                case Service.RLP /*39*/:
                    stringBuilder.append("\\'");
                    break;
                case Opcodes.DUP2 /*92*/:
                    stringBuilder.append("\\\\");
                    break;
                default:
                    if (byteAt < 32) {
                        stringBuilder.append('\\');
                        stringBuilder.append((char) (((byteAt >>> 6) & 3) + 48));
                        stringBuilder.append((char) (((byteAt >>> 3) & 7) + 48));
                        stringBuilder.append((char) ((byteAt & 7) + 48));
                        break;
                    }
                    stringBuilder.append((char) byteAt);
                    break;
            }
        }
        return stringBuilder.toString();
    }

    static ByteString unescapeBytes(CharSequence charSequence) throws C0232a {
        ByteString copyFromUtf8 = ByteString.copyFromUtf8(charSequence.toString());
        byte[] bArr = new byte[copyFromUtf8.size()];
        int i = 0;
        int i2;
        for (int i3 = 0; i3 < copyFromUtf8.size(); i3 = i2 + 1) {
            byte byteAt = copyFromUtf8.byteAt(i3);
            if (byteAt != (byte) 92) {
                i2 = i + 1;
                bArr[i] = byteAt;
                i = i2;
                i2 = i3;
            } else if (i3 + 1 < copyFromUtf8.size()) {
                i2 = i3 + 1;
                byte byteAt2 = copyFromUtf8.byteAt(i2);
                int i4;
                if (isOctal(byteAt2)) {
                    i3 = digitValue(byteAt2);
                    if (i2 + 1 < copyFromUtf8.size() && isOctal(copyFromUtf8.byteAt(i2 + 1))) {
                        i2++;
                        i3 = (i3 * 8) + digitValue(copyFromUtf8.byteAt(i2));
                    }
                    if (i2 + 1 < copyFromUtf8.size() && isOctal(copyFromUtf8.byteAt(i2 + 1))) {
                        i2++;
                        i3 = (i3 * 8) + digitValue(copyFromUtf8.byteAt(i2));
                    }
                    i4 = i + 1;
                    bArr[i] = (byte) i3;
                    i = i4;
                } else {
                    switch (byteAt2) {
                        case org.xbill.DNS.Type.ATMA /*34*/:
                            i3 = i + 1;
                            bArr[i] = SmileConstants.TOKEN_LITERAL_FALSE;
                            break;
                        case Service.RLP /*39*/:
                            i3 = i + 1;
                            bArr[i] = (byte) 39;
                            break;
                        case Opcodes.DUP2 /*92*/:
                            i3 = i + 1;
                            bArr[i] = (byte) 92;
                            break;
                        case Service.SWIFT_RVF /*97*/:
                            i3 = i + 1;
                            bArr[i] = (byte) 7;
                            break;
                        case Service.TACNEWS /*98*/:
                            i3 = i + 1;
                            bArr[i] = (byte) 8;
                            break;
                        case Service.ISO_TSAP /*102*/:
                            i3 = i + 1;
                            bArr[i] = (byte) 12;
                            break;
                        case SoapEnvelope.VER11 /*110*/:
                            i3 = i + 1;
                            bArr[i] = (byte) 10;
                            break;
                        case Opcodes.FREM /*114*/:
                            i3 = i + 1;
                            bArr[i] = (byte) 13;
                            break;
                        case Opcodes.INEG /*116*/:
                            i3 = i + 1;
                            bArr[i] = (byte) 9;
                            break;
                        case Opcodes.FNEG /*118*/:
                            i3 = i + 1;
                            bArr[i] = Flags.CD;
                            break;
                        case SoapEnvelope.VER12 /*120*/:
                            if (i2 + 1 < copyFromUtf8.size() && isHex(copyFromUtf8.byteAt(i2 + 1))) {
                                i2++;
                                i3 = digitValue(copyFromUtf8.byteAt(i2));
                                if (i2 + 1 < copyFromUtf8.size() && isHex(copyFromUtf8.byteAt(i2 + 1))) {
                                    i2++;
                                    i3 = (i3 * 16) + digitValue(copyFromUtf8.byteAt(i2));
                                }
                                i4 = i + 1;
                                bArr[i] = (byte) i3;
                                i3 = i4;
                                break;
                            }
                            throw new C0232a("Invalid escape sequence: '\\x' with no digits");
                        default:
                            throw new C0232a("Invalid escape sequence: '\\" + ((char) byteAt2) + '\'');
                    }
                    i = i3;
                }
            } else {
                throw new C0232a("Invalid escape sequence: '\\' at end of string.");
            }
        }
        return ByteString.copyFrom(bArr, 0, i);
    }

    static String escapeText(String str) {
        return escapeBytes(ByteString.copyFromUtf8(str));
    }

    static String unescapeText(String str) throws C0232a {
        return unescapeBytes(str).toStringUtf8();
    }

    private static boolean isOctal(byte b) {
        return 48 <= b && b <= 55;
    }

    private static boolean isHex(byte b) {
        return (48 <= b && b <= 57) || ((97 <= b && b <= 102) || (65 <= b && b <= 70));
    }

    private static int digitValue(byte b) {
        if (48 <= b && b <= 57) {
            return b - 48;
        }
        if (97 > b || b > 122) {
            return (b - 65) + 10;
        }
        return (b - 97) + 10;
    }

    static int parseInt32(String str) throws NumberFormatException {
        return (int) parseInteger(str, true, false);
    }

    static int parseUInt32(String str) throws NumberFormatException {
        return (int) parseInteger(str, false, false);
    }

    static long parseInt64(String str) throws NumberFormatException {
        return parseInteger(str, true, true);
    }

    static long parseUInt64(String str) throws NumberFormatException {
        return parseInteger(str, false, true);
    }

    private static long parseInteger(String str, boolean z, boolean z2) throws NumberFormatException {
        int i;
        int i2 = 1;
        int i3 = 0;
        if (!str.startsWith("-", 0)) {
            i2 = 0;
        } else if (z) {
            i3 = 1;
        } else {
            throw new NumberFormatException("Number must be positive: " + str);
        }
        if (str.startsWith("0x", i3)) {
            i = i3 + 2;
            i3 = 16;
        } else if (str.startsWith(Contact.RELATION_ASK, i3)) {
            i = i3;
            i3 = 8;
        } else {
            i = i3;
            i3 = 10;
        }
        String substring = str.substring(i);
        if (substring.length() < 16) {
            long j;
            long parseLong = Long.parseLong(substring, i3);
            if (i2 != 0) {
                j = -parseLong;
            } else {
                j = parseLong;
            }
            if (z2) {
                return j;
            }
            if (z) {
                if (j <= TTL.MAX_VALUE && j >= -2147483648L) {
                    return j;
                }
                throw new NumberFormatException("Number out of range for 32-bit signed integer: " + str);
            } else if (j < 4294967296L && j >= 0) {
                return j;
            } else {
                throw new NumberFormatException("Number out of range for 32-bit unsigned integer: " + str);
            }
        }
        BigInteger negate;
        BigInteger bigInteger = new BigInteger(substring, i3);
        if (i2 != 0) {
            negate = bigInteger.negate();
        } else {
            negate = bigInteger;
        }
        if (z2) {
            if (z) {
                if (negate.bitLength() > 63) {
                    throw new NumberFormatException("Number out of range for 64-bit signed integer: " + str);
                }
            } else if (negate.bitLength() > 64) {
                throw new NumberFormatException("Number out of range for 64-bit unsigned integer: " + str);
            }
        } else if (z) {
            if (negate.bitLength() > 31) {
                throw new NumberFormatException("Number out of range for 32-bit signed integer: " + str);
            }
        } else if (negate.bitLength() > 32) {
            throw new NumberFormatException("Number out of range for 32-bit unsigned integer: " + str);
        }
        return negate.longValue();
    }
}
