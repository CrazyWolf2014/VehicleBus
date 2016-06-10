package com.google.protobuf;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.DescriptorProto;
import com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange;
import com.google.protobuf.DescriptorProtos.EnumDescriptorProto;
import com.google.protobuf.DescriptorProtos.EnumOptions;
import com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.MethodDescriptorProto;
import com.google.protobuf.DescriptorProtos.MethodOptions;
import com.google.protobuf.DescriptorProtos.ServiceDescriptorProto;
import com.google.protobuf.DescriptorProtos.ServiceOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.google.protobuf.FieldSet.FieldDescriptorLite;
import com.google.protobuf.Internal.EnumLite;
import com.google.protobuf.Internal.EnumLiteMap;
import com.google.protobuf.MessageLite.Builder;
import com.google.protobuf.WireFormat.FieldType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xmlpull.v1.XmlPullParser;

public final class Descriptors {

    /* renamed from: com.google.protobuf.Descriptors.1 */
    static /* synthetic */ class C02141 {
        static final /* synthetic */ int[] f858a;
        static final /* synthetic */ int[] f859b;

        static {
            f859b = new int[JavaType.values().length];
            try {
                f859b[JavaType.ENUM.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f859b[JavaType.MESSAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            f858a = new int[Type.values().length];
            try {
                f858a[Type.INT32.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f858a[Type.SINT32.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f858a[Type.SFIXED32.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f858a[Type.UINT32.ordinal()] = 4;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f858a[Type.FIXED32.ordinal()] = 5;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f858a[Type.INT64.ordinal()] = 6;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f858a[Type.SINT64.ordinal()] = 7;
            } catch (NoSuchFieldError e9) {
            }
            try {
                f858a[Type.SFIXED64.ordinal()] = 8;
            } catch (NoSuchFieldError e10) {
            }
            try {
                f858a[Type.UINT64.ordinal()] = 9;
            } catch (NoSuchFieldError e11) {
            }
            try {
                f858a[Type.FIXED64.ordinal()] = 10;
            } catch (NoSuchFieldError e12) {
            }
            try {
                f858a[Type.FLOAT.ordinal()] = 11;
            } catch (NoSuchFieldError e13) {
            }
            try {
                f858a[Type.DOUBLE.ordinal()] = 12;
            } catch (NoSuchFieldError e14) {
            }
            try {
                f858a[Type.BOOL.ordinal()] = 13;
            } catch (NoSuchFieldError e15) {
            }
            try {
                f858a[Type.STRING.ordinal()] = 14;
            } catch (NoSuchFieldError e16) {
            }
            try {
                f858a[Type.BYTES.ordinal()] = 15;
            } catch (NoSuchFieldError e17) {
            }
            try {
                f858a[Type.ENUM.ordinal()] = 16;
            } catch (NoSuchFieldError e18) {
            }
            try {
                f858a[Type.MESSAGE.ordinal()] = 17;
            } catch (NoSuchFieldError e19) {
            }
            try {
                f858a[Type.GROUP.ordinal()] = 18;
            } catch (NoSuchFieldError e20) {
            }
        }
    }

    public static class DescriptorValidationException extends Exception {
        private static final long serialVersionUID = 5750205775490483148L;
        private final String description;
        private final String name;
        private final Message proto;

        public String getProblemSymbolName() {
            return this.name;
        }

        public Message getProblemProto() {
            return this.proto;
        }

        public String getDescription() {
            return this.description;
        }

        private DescriptorValidationException(C0217b c0217b, String str) {
            super(c0217b.getFullName() + ": " + str);
            this.name = c0217b.getFullName();
            this.proto = c0217b.toProto();
            this.description = str;
        }

        private DescriptorValidationException(C0217b c0217b, String str, Throwable th) {
            this(c0217b, str);
            initCause(th);
        }

        private DescriptorValidationException(FileDescriptor fileDescriptor, String str) {
            super(fileDescriptor.getName() + ": " + str);
            this.name = fileDescriptor.getName();
            this.proto = fileDescriptor.toProto();
            this.description = str;
        }
    }

    public static final class FileDescriptor {
        private final FileDescriptor[] dependencies;
        private final EnumDescriptor[] enumTypes;
        private final FieldDescriptor[] extensions;
        private final Descriptor[] messageTypes;
        private final C0216a pool;
        private FileDescriptorProto proto;
        private final ServiceDescriptor[] services;

        public interface InternalDescriptorAssigner {
            ExtensionRegistry assignDescriptors(FileDescriptor fileDescriptor);
        }

        public FileDescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public String getPackage() {
            return this.proto.getPackage();
        }

        public FileOptions getOptions() {
            return this.proto.getOptions();
        }

        public List<Descriptor> getMessageTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.messageTypes));
        }

        public List<EnumDescriptor> getEnumTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.enumTypes));
        }

        public List<ServiceDescriptor> getServices() {
            return Collections.unmodifiableList(Arrays.asList(this.services));
        }

        public List<FieldDescriptor> getExtensions() {
            return Collections.unmodifiableList(Arrays.asList(this.extensions));
        }

        public List<FileDescriptor> getDependencies() {
            return Collections.unmodifiableList(Arrays.asList(this.dependencies));
        }

        public Descriptor findMessageTypeByName(String str) {
            if (str.indexOf(46) != -1) {
                return null;
            }
            if (getPackage().length() > 0) {
                str = getPackage() + '.' + str;
            }
            C0217b a = this.pool.m927a(str);
            return (a != null && (a instanceof Descriptor) && a.getFile() == this) ? (Descriptor) a : null;
        }

        public EnumDescriptor findEnumTypeByName(String str) {
            if (str.indexOf(46) != -1) {
                return null;
            }
            if (getPackage().length() > 0) {
                str = getPackage() + '.' + str;
            }
            C0217b a = this.pool.m927a(str);
            return (a != null && (a instanceof EnumDescriptor) && a.getFile() == this) ? (EnumDescriptor) a : null;
        }

        public ServiceDescriptor findServiceByName(String str) {
            if (str.indexOf(46) != -1) {
                return null;
            }
            if (getPackage().length() > 0) {
                str = getPackage() + '.' + str;
            }
            C0217b a = this.pool.m927a(str);
            return (a != null && (a instanceof ServiceDescriptor) && a.getFile() == this) ? (ServiceDescriptor) a : null;
        }

        public FieldDescriptor findExtensionByName(String str) {
            if (str.indexOf(46) != -1) {
                return null;
            }
            if (getPackage().length() > 0) {
                str = getPackage() + '.' + str;
            }
            C0217b a = this.pool.m927a(str);
            return (a != null && (a instanceof FieldDescriptor) && a.getFile() == this) ? (FieldDescriptor) a : null;
        }

        public static FileDescriptor buildFrom(FileDescriptorProto fileDescriptorProto, FileDescriptor[] fileDescriptorArr) throws DescriptorValidationException {
            FileDescriptor fileDescriptor = new FileDescriptor(fileDescriptorProto, fileDescriptorArr, new C0216a(fileDescriptorArr));
            if (fileDescriptorArr.length != fileDescriptorProto.getDependencyCount()) {
                throw new DescriptorValidationException("Dependencies passed to FileDescriptor.buildFrom() don't match those listed in the FileDescriptorProto.", null);
            }
            int i = 0;
            while (i < fileDescriptorProto.getDependencyCount()) {
                if (fileDescriptorArr[i].getName().equals(fileDescriptorProto.getDependency(i))) {
                    i++;
                } else {
                    throw new DescriptorValidationException("Dependencies passed to FileDescriptor.buildFrom() don't match those listed in the FileDescriptorProto.", null);
                }
            }
            fileDescriptor.crossLink();
            return fileDescriptor;
        }

        public static void internalBuildGeneratedFileFrom(String[] strArr, FileDescriptor[] fileDescriptorArr, InternalDescriptorAssigner internalDescriptorAssigner) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String append : strArr) {
                stringBuilder.append(append);
            }
            try {
                byte[] bytes = stringBuilder.toString().getBytes("ISO-8859-1");
                try {
                    try {
                        FileDescriptor buildFrom = buildFrom(FileDescriptorProto.parseFrom(bytes), fileDescriptorArr);
                        ExtensionRegistryLite assignDescriptors = internalDescriptorAssigner.assignDescriptors(buildFrom);
                        if (assignDescriptors != null) {
                            try {
                                buildFrom.setProto(FileDescriptorProto.parseFrom(bytes, assignDescriptors));
                            } catch (Throwable e) {
                                throw new IllegalArgumentException("Failed to parse protocol buffer descriptor for generated code.", e);
                            }
                        }
                    } catch (Throwable e2) {
                        throw new IllegalArgumentException("Invalid embedded descriptor for \"" + r1.getName() + "\".", e2);
                    }
                } catch (Throwable e22) {
                    throw new IllegalArgumentException("Failed to parse protocol buffer descriptor for generated code.", e22);
                }
            } catch (Throwable e222) {
                throw new RuntimeException("Standard encoding ISO-8859-1 not supported by JVM.", e222);
            }
        }

        private FileDescriptor(FileDescriptorProto fileDescriptorProto, FileDescriptor[] fileDescriptorArr, C0216a c0216a) throws DescriptorValidationException {
            int i;
            this.pool = c0216a;
            this.proto = fileDescriptorProto;
            this.dependencies = (FileDescriptor[]) fileDescriptorArr.clone();
            c0216a.m932a(getPackage(), this);
            this.messageTypes = new Descriptor[fileDescriptorProto.getMessageTypeCount()];
            for (i = 0; i < fileDescriptorProto.getMessageTypeCount(); i++) {
                this.messageTypes[i] = new Descriptor(this, null, i, null);
            }
            this.enumTypes = new EnumDescriptor[fileDescriptorProto.getEnumTypeCount()];
            for (i = 0; i < fileDescriptorProto.getEnumTypeCount(); i++) {
                this.enumTypes[i] = new EnumDescriptor(this, null, i, null);
            }
            this.services = new ServiceDescriptor[fileDescriptorProto.getServiceCount()];
            for (int i2 = 0; i2 < fileDescriptorProto.getServiceCount(); i2++) {
                this.services[i2] = new ServiceDescriptor(this, i2, null);
            }
            this.extensions = new FieldDescriptor[fileDescriptorProto.getExtensionCount()];
            for (i = 0; i < fileDescriptorProto.getExtensionCount(); i++) {
                this.extensions[i] = new FieldDescriptor(this, null, i, true, null);
            }
        }

        private void crossLink() throws DescriptorValidationException {
            int i = 0;
            for (Descriptor access$500 : this.messageTypes) {
                access$500.crossLink();
            }
            for (ServiceDescriptor access$600 : this.services) {
                access$600.crossLink();
            }
            FieldDescriptor[] fieldDescriptorArr = this.extensions;
            int length = fieldDescriptorArr.length;
            while (i < length) {
                fieldDescriptorArr[i].crossLink();
                i++;
            }
        }

        private void setProto(FileDescriptorProto fileDescriptorProto) {
            int i;
            int i2 = 0;
            this.proto = fileDescriptorProto;
            for (i = 0; i < this.messageTypes.length; i++) {
                this.messageTypes[i].setProto(fileDescriptorProto.getMessageType(i));
            }
            for (i = 0; i < this.enumTypes.length; i++) {
                this.enumTypes[i].setProto(fileDescriptorProto.getEnumType(i));
            }
            for (i = 0; i < this.services.length; i++) {
                this.services[i].setProto(fileDescriptorProto.getService(i));
            }
            while (i2 < this.extensions.length) {
                this.extensions[i2].setProto(fileDescriptorProto.getExtension(i2));
                i2++;
            }
        }
    }

    /* renamed from: com.google.protobuf.Descriptors.a */
    private static final class C0216a {
        static final /* synthetic */ boolean f862a;
        private final C0216a[] f863b;
        private final Map<String, C0217b> f864c;
        private final Map<C0215a, FieldDescriptor> f865d;
        private final Map<C0215a, EnumValueDescriptor> f866e;

        /* renamed from: com.google.protobuf.Descriptors.a.a */
        private static final class C0215a {
            private final C0217b f860a;
            private final int f861b;

            C0215a(C0217b c0217b, int i) {
                this.f860a = c0217b;
                this.f861b = i;
            }

            public int hashCode() {
                return (this.f860a.hashCode() * InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) + this.f861b;
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof C0215a)) {
                    return false;
                }
                C0215a c0215a = (C0215a) obj;
                if (this.f860a == c0215a.f860a && this.f861b == c0215a.f861b) {
                    return true;
                }
                return false;
            }
        }

        /* renamed from: com.google.protobuf.Descriptors.a.b */
        private static final class C1056b implements C0217b {
            private final String f1938a;
            private final String f1939b;
            private final FileDescriptor f1940c;

            public Message toProto() {
                return this.f1940c.toProto();
            }

            public String getName() {
                return this.f1938a;
            }

            public String getFullName() {
                return this.f1939b;
            }

            public FileDescriptor getFile() {
                return this.f1940c;
            }

            C1056b(String str, String str2, FileDescriptor fileDescriptor) {
                this.f1940c = fileDescriptor;
                this.f1939b = str2;
                this.f1938a = str;
            }
        }

        static {
            f862a = !Descriptors.class.desiredAssertionStatus();
        }

        C0216a(FileDescriptor[] fileDescriptorArr) {
            int i;
            int i2 = 0;
            this.f864c = new HashMap();
            this.f865d = new HashMap();
            this.f866e = new HashMap();
            this.f863b = new C0216a[fileDescriptorArr.length];
            for (i = 0; i < fileDescriptorArr.length; i++) {
                this.f863b[i] = fileDescriptorArr[i].pool;
            }
            i = fileDescriptorArr.length;
            while (i2 < i) {
                FileDescriptor fileDescriptor = fileDescriptorArr[i2];
                try {
                    m932a(fileDescriptor.getPackage(), fileDescriptor);
                } catch (DescriptorValidationException e) {
                    if (!f862a) {
                        throw new AssertionError();
                    }
                }
                i2++;
            }
        }

        C0217b m927a(String str) {
            C0217b c0217b = (C0217b) this.f864c.get(str);
            if (c0217b != null) {
                return c0217b;
            }
            for (C0216a c0216a : this.f863b) {
                c0217b = (C0217b) c0216a.f864c.get(str);
                if (c0217b != null) {
                    return c0217b;
                }
            }
            return null;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        com.google.protobuf.Descriptors.C0217b m928a(java.lang.String r7, com.google.protobuf.Descriptors.C0217b r8) throws com.google.protobuf.Descriptors.DescriptorValidationException {
            /*
            r6 = this;
            r5 = -1;
            r0 = ".";
            r0 = r7.startsWith(r0);
            if (r0 == 0) goto L_0x0034;
        L_0x0009:
            r0 = 1;
            r0 = r7.substring(r0);
            r0 = r6.m927a(r0);
        L_0x0012:
            if (r0 != 0) goto L_0x0082;
        L_0x0014:
            r0 = new com.google.protobuf.Descriptors$DescriptorValidationException;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = 34;
            r1 = r1.append(r2);
            r1 = r1.append(r7);
            r2 = "\" is not defined.";
            r1 = r1.append(r2);
            r1 = r1.toString();
            r2 = 0;
            r0.<init>(r1, r2);
            throw r0;
        L_0x0034:
            r0 = 46;
            r2 = r7.indexOf(r0);
            if (r2 != r5) goto L_0x0053;
        L_0x003c:
            r0 = r7;
        L_0x003d:
            r3 = new java.lang.StringBuilder;
            r1 = r8.getFullName();
            r3.<init>(r1);
        L_0x0046:
            r1 = ".";
            r4 = r3.lastIndexOf(r1);
            if (r4 != r5) goto L_0x0059;
        L_0x004e:
            r0 = r6.m927a(r7);
            goto L_0x0012;
        L_0x0053:
            r0 = 0;
            r0 = r7.substring(r0, r2);
            goto L_0x003d;
        L_0x0059:
            r1 = r4 + 1;
            r3.setLength(r1);
            r3.append(r0);
            r1 = r3.toString();
            r1 = r6.m927a(r1);
            if (r1 == 0) goto L_0x007e;
        L_0x006b:
            if (r2 == r5) goto L_0x0083;
        L_0x006d:
            r0 = r4 + 1;
            r3.setLength(r0);
            r3.append(r7);
            r0 = r3.toString();
            r0 = r6.m927a(r0);
            goto L_0x0012;
        L_0x007e:
            r3.setLength(r4);
            goto L_0x0046;
        L_0x0082:
            return r0;
        L_0x0083:
            r0 = r1;
            goto L_0x0012;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Descriptors.a.a(java.lang.String, com.google.protobuf.Descriptors$b):com.google.protobuf.Descriptors$b");
        }

        void m931a(C0217b c0217b) throws DescriptorValidationException {
            C0216a.m926b(c0217b);
            String fullName = c0217b.getFullName();
            int lastIndexOf = fullName.lastIndexOf(46);
            C0217b c0217b2 = (C0217b) this.f864c.put(fullName, c0217b);
            if (c0217b2 != null) {
                this.f864c.put(fullName, c0217b2);
                if (c0217b.getFile() != c0217b2.getFile()) {
                    throw new DescriptorValidationException('\"' + fullName + "\" is already defined in file \"" + c0217b2.getFile().getName() + "\".", null);
                } else if (lastIndexOf == -1) {
                    throw new DescriptorValidationException('\"' + fullName + "\" is already defined.", null);
                } else {
                    throw new DescriptorValidationException('\"' + fullName.substring(lastIndexOf + 1) + "\" is already defined in \"" + fullName.substring(0, lastIndexOf) + "\".", null);
                }
            }
        }

        void m932a(String str, FileDescriptor fileDescriptor) throws DescriptorValidationException {
            String str2;
            int lastIndexOf = str.lastIndexOf(46);
            if (lastIndexOf == -1) {
                str2 = str;
            } else {
                m932a(str.substring(0, lastIndexOf), fileDescriptor);
                str2 = str.substring(lastIndexOf + 1);
            }
            C0217b c0217b = (C0217b) this.f864c.put(str, new C1056b(str2, str, fileDescriptor));
            if (c0217b != null) {
                this.f864c.put(str, c0217b);
                if (!(c0217b instanceof C1056b)) {
                    throw new DescriptorValidationException('\"' + str2 + "\" is already defined (as something other than a " + "package) in file \"" + c0217b.getFile().getName() + "\".", null);
                }
            }
        }

        void m930a(FieldDescriptor fieldDescriptor) throws DescriptorValidationException {
            C0215a c0215a = new C0215a(fieldDescriptor.getContainingType(), fieldDescriptor.getNumber());
            FieldDescriptor fieldDescriptor2 = (FieldDescriptor) this.f865d.put(c0215a, fieldDescriptor);
            if (fieldDescriptor2 != null) {
                this.f865d.put(c0215a, fieldDescriptor2);
                throw new DescriptorValidationException("Field number " + fieldDescriptor.getNumber() + "has already been used in \"" + fieldDescriptor.getContainingType().getFullName() + "\" by field \"" + fieldDescriptor2.getName() + "\".", null);
            }
        }

        void m929a(EnumValueDescriptor enumValueDescriptor) {
            C0215a c0215a = new C0215a(enumValueDescriptor.getType(), enumValueDescriptor.getNumber());
            EnumValueDescriptor enumValueDescriptor2 = (EnumValueDescriptor) this.f866e.put(c0215a, enumValueDescriptor);
            if (enumValueDescriptor2 != null) {
                this.f866e.put(c0215a, enumValueDescriptor2);
            }
        }

        static void m926b(C0217b c0217b) throws DescriptorValidationException {
            String name = c0217b.getName();
            if (name.length() == 0) {
                throw new DescriptorValidationException("Missing name.", null);
            }
            Object obj = 1;
            int i = 0;
            while (i < name.length()) {
                char charAt = name.charAt(i);
                if (charAt >= '\u0080') {
                    obj = null;
                }
                if (!(Character.isLetter(charAt) || charAt == '_' || (Character.isDigit(charAt) && i > 0))) {
                    obj = null;
                }
                i++;
            }
            if (obj == null) {
                throw new DescriptorValidationException('\"' + name + "\" is not a valid identifier.", null);
            }
        }
    }

    /* renamed from: com.google.protobuf.Descriptors.b */
    private interface C0217b {
        FileDescriptor getFile();

        String getFullName();

        String getName();

        Message toProto();
    }

    public static final class Descriptor implements C0217b {
        private final Descriptor containingType;
        private final EnumDescriptor[] enumTypes;
        private final FieldDescriptor[] extensions;
        private final FieldDescriptor[] fields;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private final Descriptor[] nestedTypes;
        private DescriptorProto proto;

        public int getIndex() {
            return this.index;
        }

        public DescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public String getFullName() {
            return this.fullName;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        public MessageOptions getOptions() {
            return this.proto.getOptions();
        }

        public List<FieldDescriptor> getFields() {
            return Collections.unmodifiableList(Arrays.asList(this.fields));
        }

        public List<FieldDescriptor> getExtensions() {
            return Collections.unmodifiableList(Arrays.asList(this.extensions));
        }

        public List<Descriptor> getNestedTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.nestedTypes));
        }

        public List<EnumDescriptor> getEnumTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.enumTypes));
        }

        public boolean isExtensionNumber(int i) {
            for (ExtensionRange extensionRange : this.proto.getExtensionRangeList()) {
                if (extensionRange.getStart() <= i && i < extensionRange.getEnd()) {
                    return true;
                }
            }
            return false;
        }

        public FieldDescriptor findFieldByName(String str) {
            C0217b a = this.file.pool.m927a(this.fullName + '.' + str);
            if (a == null || !(a instanceof FieldDescriptor)) {
                return null;
            }
            return (FieldDescriptor) a;
        }

        public FieldDescriptor findFieldByNumber(int i) {
            return (FieldDescriptor) this.file.pool.f865d.get(new C0215a(this, i));
        }

        public Descriptor findNestedTypeByName(String str) {
            C0217b a = this.file.pool.m927a(this.fullName + '.' + str);
            if (a == null || !(a instanceof Descriptor)) {
                return null;
            }
            return (Descriptor) a;
        }

        public EnumDescriptor findEnumTypeByName(String str) {
            C0217b a = this.file.pool.m927a(this.fullName + '.' + str);
            if (a == null || !(a instanceof EnumDescriptor)) {
                return null;
            }
            return (EnumDescriptor) a;
        }

        private Descriptor(DescriptorProto descriptorProto, FileDescriptor fileDescriptor, Descriptor descriptor, int i) throws DescriptorValidationException {
            int i2;
            this.index = i;
            this.proto = descriptorProto;
            this.fullName = Descriptors.computeFullName(fileDescriptor, descriptor, descriptorProto.getName());
            this.file = fileDescriptor;
            this.containingType = descriptor;
            this.nestedTypes = new Descriptor[descriptorProto.getNestedTypeCount()];
            for (int i3 = 0; i3 < descriptorProto.getNestedTypeCount(); i3++) {
                this.nestedTypes[i3] = new Descriptor(descriptorProto.getNestedType(i3), fileDescriptor, this, i3);
            }
            this.enumTypes = new EnumDescriptor[descriptorProto.getEnumTypeCount()];
            for (i2 = 0; i2 < descriptorProto.getEnumTypeCount(); i2++) {
                this.enumTypes[i2] = new EnumDescriptor(fileDescriptor, this, i2, null);
            }
            this.fields = new FieldDescriptor[descriptorProto.getFieldCount()];
            for (i2 = 0; i2 < descriptorProto.getFieldCount(); i2++) {
                this.fields[i2] = new FieldDescriptor(fileDescriptor, this, i2, false, null);
            }
            this.extensions = new FieldDescriptor[descriptorProto.getExtensionCount()];
            for (i2 = 0; i2 < descriptorProto.getExtensionCount(); i2++) {
                this.extensions[i2] = new FieldDescriptor(fileDescriptor, this, i2, true, null);
            }
            fileDescriptor.pool.m931a((C0217b) this);
        }

        private void crossLink() throws DescriptorValidationException {
            int i = 0;
            for (Descriptor crossLink : this.nestedTypes) {
                crossLink.crossLink();
            }
            for (FieldDescriptor access$700 : this.fields) {
                access$700.crossLink();
            }
            FieldDescriptor[] fieldDescriptorArr = this.extensions;
            int length = fieldDescriptorArr.length;
            while (i < length) {
                fieldDescriptorArr[i].crossLink();
                i++;
            }
        }

        private void setProto(DescriptorProto descriptorProto) {
            int i;
            int i2 = 0;
            this.proto = descriptorProto;
            for (i = 0; i < this.nestedTypes.length; i++) {
                this.nestedTypes[i].setProto(descriptorProto.getNestedType(i));
            }
            for (i = 0; i < this.enumTypes.length; i++) {
                this.enumTypes[i].setProto(descriptorProto.getEnumType(i));
            }
            for (i = 0; i < this.fields.length; i++) {
                this.fields[i].setProto(descriptorProto.getField(i));
            }
            while (i2 < this.extensions.length) {
                this.extensions[i2].setProto(descriptorProto.getExtension(i2));
                i2++;
            }
        }
    }

    public static final class EnumDescriptor implements C0217b, EnumLiteMap<EnumValueDescriptor> {
        private final Descriptor containingType;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private EnumDescriptorProto proto;
        private EnumValueDescriptor[] values;

        public int getIndex() {
            return this.index;
        }

        public EnumDescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public String getFullName() {
            return this.fullName;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        public EnumOptions getOptions() {
            return this.proto.getOptions();
        }

        public List<EnumValueDescriptor> getValues() {
            return Collections.unmodifiableList(Arrays.asList(this.values));
        }

        public EnumValueDescriptor findValueByName(String str) {
            C0217b a = this.file.pool.m927a(this.fullName + '.' + str);
            if (a == null || !(a instanceof EnumValueDescriptor)) {
                return null;
            }
            return (EnumValueDescriptor) a;
        }

        public EnumValueDescriptor findValueByNumber(int i) {
            return (EnumValueDescriptor) this.file.pool.f866e.get(new C0215a(this, i));
        }

        private EnumDescriptor(EnumDescriptorProto enumDescriptorProto, FileDescriptor fileDescriptor, Descriptor descriptor, int i) throws DescriptorValidationException {
            this.index = i;
            this.proto = enumDescriptorProto;
            this.fullName = Descriptors.computeFullName(fileDescriptor, descriptor, enumDescriptorProto.getName());
            this.file = fileDescriptor;
            this.containingType = descriptor;
            if (enumDescriptorProto.getValueCount() == 0) {
                throw new DescriptorValidationException("Enums must contain at least one value.", null);
            }
            this.values = new EnumValueDescriptor[enumDescriptorProto.getValueCount()];
            for (int i2 = 0; i2 < enumDescriptorProto.getValueCount(); i2++) {
                this.values[i2] = new EnumValueDescriptor(fileDescriptor, this, i2, null);
            }
            fileDescriptor.pool.m931a((C0217b) this);
        }

        private void setProto(EnumDescriptorProto enumDescriptorProto) {
            this.proto = enumDescriptorProto;
            for (int i = 0; i < this.values.length; i++) {
                this.values[i].setProto(enumDescriptorProto.getValue(i));
            }
        }
    }

    public static final class EnumValueDescriptor implements C0217b, EnumLite {
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private EnumValueDescriptorProto proto;
        private final EnumDescriptor type;

        public int getIndex() {
            return this.index;
        }

        public EnumValueDescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public int getNumber() {
            return this.proto.getNumber();
        }

        public String getFullName() {
            return this.fullName;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public EnumDescriptor getType() {
            return this.type;
        }

        public EnumValueOptions getOptions() {
            return this.proto.getOptions();
        }

        private EnumValueDescriptor(EnumValueDescriptorProto enumValueDescriptorProto, FileDescriptor fileDescriptor, EnumDescriptor enumDescriptor, int i) throws DescriptorValidationException {
            this.index = i;
            this.proto = enumValueDescriptorProto;
            this.file = fileDescriptor;
            this.type = enumDescriptor;
            this.fullName = enumDescriptor.getFullName() + '.' + enumValueDescriptorProto.getName();
            fileDescriptor.pool.m931a((C0217b) this);
            fileDescriptor.pool.m929a(this);
        }

        private void setProto(EnumValueDescriptorProto enumValueDescriptorProto) {
            this.proto = enumValueDescriptorProto;
        }
    }

    public static final class FieldDescriptor implements C0217b, Comparable<FieldDescriptor>, FieldDescriptorLite<FieldDescriptor> {
        private static final FieldType[] table;
        private Descriptor containingType;
        private Object defaultValue;
        private EnumDescriptor enumType;
        private final Descriptor extensionScope;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private Descriptor messageType;
        private FieldDescriptorProto proto;
        private Type type;

        public enum JavaType {
            INT(Integer.valueOf(0)),
            LONG(Long.valueOf(0)),
            FLOAT(Float.valueOf(0.0f)),
            DOUBLE(Double.valueOf(0.0d)),
            BOOLEAN(Boolean.valueOf(false)),
            STRING(XmlPullParser.NO_NAMESPACE),
            BYTE_STRING(ByteString.EMPTY),
            ENUM(null),
            MESSAGE(null);
            
            private final Object defaultDefault;

            private JavaType(Object obj) {
                this.defaultDefault = obj;
            }
        }

        public enum Type {
            DOUBLE(JavaType.DOUBLE),
            FLOAT(JavaType.FLOAT),
            INT64(JavaType.LONG),
            UINT64(JavaType.LONG),
            INT32(JavaType.INT),
            FIXED64(JavaType.LONG),
            FIXED32(JavaType.INT),
            BOOL(JavaType.BOOLEAN),
            STRING(JavaType.STRING),
            GROUP(JavaType.MESSAGE),
            MESSAGE(JavaType.MESSAGE),
            BYTES(JavaType.BYTE_STRING),
            UINT32(JavaType.INT),
            ENUM(JavaType.ENUM),
            SFIXED32(JavaType.INT),
            SFIXED64(JavaType.LONG),
            SINT32(JavaType.INT),
            SINT64(JavaType.LONG);
            
            private JavaType javaType;

            private Type(JavaType javaType) {
                this.javaType = javaType;
            }

            public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type toProto() {
                return com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type.valueOf(ordinal() + 1);
            }

            public JavaType getJavaType() {
                return this.javaType;
            }

            public static Type valueOf(com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type type) {
                return values()[type.getNumber() - 1];
            }
        }

        public int getIndex() {
            return this.index;
        }

        public FieldDescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public int getNumber() {
            return this.proto.getNumber();
        }

        public String getFullName() {
            return this.fullName;
        }

        public JavaType getJavaType() {
            return this.type.getJavaType();
        }

        public com.google.protobuf.WireFormat.JavaType getLiteJavaType() {
            return getLiteType().getJavaType();
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public Type getType() {
            return this.type;
        }

        public FieldType getLiteType() {
            return table[this.type.ordinal()];
        }

        static {
            table = FieldType.values();
            if (Type.values().length != com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type.values().length) {
                throw new RuntimeException("descriptor.proto has a new declared type but Desrciptors.java wasn't updated.");
            }
        }

        public boolean isRequired() {
            return this.proto.getLabel() == Label.LABEL_REQUIRED;
        }

        public boolean isOptional() {
            return this.proto.getLabel() == Label.LABEL_OPTIONAL;
        }

        public boolean isRepeated() {
            return this.proto.getLabel() == Label.LABEL_REPEATED;
        }

        public boolean isPacked() {
            return getOptions().getPacked();
        }

        public boolean isPackable() {
            return isRepeated() && getLiteType().isPackable();
        }

        public boolean hasDefaultValue() {
            return this.proto.hasDefaultValue();
        }

        public Object getDefaultValue() {
            if (getJavaType() != JavaType.MESSAGE) {
                return this.defaultValue;
            }
            throw new UnsupportedOperationException("FieldDescriptor.getDefaultValue() called on an embedded message field.");
        }

        public FieldOptions getOptions() {
            return this.proto.getOptions();
        }

        public boolean isExtension() {
            return this.proto.hasExtendee();
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        public Descriptor getExtensionScope() {
            if (isExtension()) {
                return this.extensionScope;
            }
            throw new UnsupportedOperationException("This field is not an extension.");
        }

        public Descriptor getMessageType() {
            if (getJavaType() == JavaType.MESSAGE) {
                return this.messageType;
            }
            throw new UnsupportedOperationException("This field is not of message type.");
        }

        public EnumDescriptor getEnumType() {
            if (getJavaType() == JavaType.ENUM) {
                return this.enumType;
            }
            throw new UnsupportedOperationException("This field is not of enum type.");
        }

        public int compareTo(FieldDescriptor fieldDescriptor) {
            if (fieldDescriptor.containingType == this.containingType) {
                return getNumber() - fieldDescriptor.getNumber();
            }
            throw new IllegalArgumentException("FieldDescriptors can only be compared to other FieldDescriptors for fields of the same message type.");
        }

        private FieldDescriptor(FieldDescriptorProto fieldDescriptorProto, FileDescriptor fileDescriptor, Descriptor descriptor, int i, boolean z) throws DescriptorValidationException {
            this.index = i;
            this.proto = fieldDescriptorProto;
            this.fullName = Descriptors.computeFullName(fileDescriptor, descriptor, fieldDescriptorProto.getName());
            this.file = fileDescriptor;
            if (fieldDescriptorProto.hasType()) {
                this.type = Type.valueOf(fieldDescriptorProto.getType());
            }
            if (getNumber() <= 0) {
                throw new DescriptorValidationException("Field numbers must be positive integers.", null);
            } else if (!fieldDescriptorProto.getOptions().getPacked() || isPackable()) {
                if (z) {
                    if (fieldDescriptorProto.hasExtendee()) {
                        this.containingType = null;
                        if (descriptor != null) {
                            this.extensionScope = descriptor;
                        } else {
                            this.extensionScope = null;
                        }
                    } else {
                        throw new DescriptorValidationException("FieldDescriptorProto.extendee not set for extension field.", null);
                    }
                } else if (fieldDescriptorProto.hasExtendee()) {
                    throw new DescriptorValidationException("FieldDescriptorProto.extendee set for non-extension field.", null);
                } else {
                    this.containingType = descriptor;
                    this.extensionScope = null;
                }
                fileDescriptor.pool.m931a((C0217b) this);
            } else {
                throw new DescriptorValidationException("[packed = true] can only be specified for repeated primitive fields.", null);
            }
        }

        private void crossLink() throws DescriptorValidationException {
            C0217b a;
            if (this.proto.hasExtendee()) {
                a = this.file.pool.m928a(this.proto.getExtendee(), (C0217b) this);
                if (a instanceof Descriptor) {
                    this.containingType = (Descriptor) a;
                    if (!getContainingType().isExtensionNumber(getNumber())) {
                        throw new DescriptorValidationException('\"' + getContainingType().getFullName() + "\" does not declare " + getNumber() + " as an extension number.", null);
                    }
                }
                throw new DescriptorValidationException('\"' + this.proto.getExtendee() + "\" is not a message type.", null);
            }
            if (this.proto.hasTypeName()) {
                a = this.file.pool.m928a(this.proto.getTypeName(), (C0217b) this);
                if (!this.proto.hasType()) {
                    if (a instanceof Descriptor) {
                        this.type = Type.MESSAGE;
                    } else if (a instanceof EnumDescriptor) {
                        this.type = Type.ENUM;
                    } else {
                        throw new DescriptorValidationException('\"' + this.proto.getTypeName() + "\" is not a type.", null);
                    }
                }
                if (getJavaType() == JavaType.MESSAGE) {
                    if (a instanceof Descriptor) {
                        this.messageType = (Descriptor) a;
                        if (this.proto.hasDefaultValue()) {
                            throw new DescriptorValidationException("Messages can't have default values.", null);
                        }
                    }
                    throw new DescriptorValidationException('\"' + this.proto.getTypeName() + "\" is not a message type.", null);
                } else if (getJavaType() != JavaType.ENUM) {
                    throw new DescriptorValidationException("Field with primitive type has type_name.", null);
                } else if (a instanceof EnumDescriptor) {
                    this.enumType = (EnumDescriptor) a;
                } else {
                    throw new DescriptorValidationException('\"' + this.proto.getTypeName() + "\" is not an enum type.", null);
                }
            } else if (getJavaType() == JavaType.MESSAGE || getJavaType() == JavaType.ENUM) {
                throw new DescriptorValidationException("Field with message or enum type missing type_name.", null);
            }
            if (!this.proto.hasDefaultValue()) {
                if (!isRepeated()) {
                    switch (C02141.f859b[getJavaType().ordinal()]) {
                        case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                            this.defaultValue = this.enumType.getValues().get(0);
                            break;
                        case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                            this.defaultValue = null;
                            break;
                        default:
                            this.defaultValue = getJavaType().defaultDefault;
                            break;
                    }
                }
                this.defaultValue = Collections.emptyList();
            } else if (isRepeated()) {
                throw new DescriptorValidationException("Repeated fields cannot have default values.", null);
            } else {
                try {
                    switch (C02141.f858a[getType().ordinal()]) {
                        case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                            this.defaultValue = Integer.valueOf(TextFormat.parseInt32(this.proto.getDefaultValue()));
                            break;
                        case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                        case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                            this.defaultValue = Integer.valueOf(TextFormat.parseUInt32(this.proto.getDefaultValue()));
                            break;
                        case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                        case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                        case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                            this.defaultValue = Long.valueOf(TextFormat.parseInt64(this.proto.getDefaultValue()));
                            break;
                        case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                        case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                            this.defaultValue = Long.valueOf(TextFormat.parseUInt64(this.proto.getDefaultValue()));
                            break;
                        case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                            if (!this.proto.getDefaultValue().equals("inf")) {
                                if (!this.proto.getDefaultValue().equals("-inf")) {
                                    if (!this.proto.getDefaultValue().equals("nan")) {
                                        this.defaultValue = Float.valueOf(this.proto.getDefaultValue());
                                        break;
                                    } else {
                                        this.defaultValue = Float.valueOf(Float.NaN);
                                        break;
                                    }
                                }
                                this.defaultValue = Float.valueOf(Float.NEGATIVE_INFINITY);
                                break;
                            }
                            this.defaultValue = Float.valueOf(Float.POSITIVE_INFINITY);
                            break;
                        case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                            if (!this.proto.getDefaultValue().equals("inf")) {
                                if (!this.proto.getDefaultValue().equals("-inf")) {
                                    if (!this.proto.getDefaultValue().equals("nan")) {
                                        this.defaultValue = Double.valueOf(this.proto.getDefaultValue());
                                        break;
                                    } else {
                                        this.defaultValue = Double.valueOf(Double.NaN);
                                        break;
                                    }
                                }
                                this.defaultValue = Double.valueOf(Double.NEGATIVE_INFINITY);
                                break;
                            }
                            this.defaultValue = Double.valueOf(Double.POSITIVE_INFINITY);
                            break;
                        case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                            this.defaultValue = Boolean.valueOf(this.proto.getDefaultValue());
                            break;
                        case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                            this.defaultValue = this.proto.getDefaultValue();
                            break;
                        case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                            this.defaultValue = TextFormat.unescapeBytes(this.proto.getDefaultValue());
                            break;
                        case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                            this.defaultValue = this.enumType.findValueByName(this.proto.getDefaultValue());
                            if (this.defaultValue == null) {
                                throw new DescriptorValidationException("Unknown enum default value: \"" + this.proto.getDefaultValue() + '\"', null);
                            }
                            break;
                        case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                        case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                            throw new DescriptorValidationException("Message type had default value.", null);
                    }
                } catch (Throwable e) {
                    throw new DescriptorValidationException("Couldn't parse default value: " + e.getMessage(), e, null);
                } catch (Throwable e2) {
                    throw new DescriptorValidationException("Could not parse default value: \"" + this.proto.getDefaultValue() + '\"', e2, null);
                }
            }
            if (!isExtension()) {
                this.file.pool.m930a(this);
            }
            if (this.containingType != null && this.containingType.getOptions().getMessageSetWireFormat()) {
                if (!isExtension()) {
                    throw new DescriptorValidationException("MessageSets cannot have fields, only extensions.", null);
                } else if (!isOptional() || getType() != Type.MESSAGE) {
                    throw new DescriptorValidationException("Extensions of MessageSets must be optional messages.", null);
                }
            }
        }

        private void setProto(FieldDescriptorProto fieldDescriptorProto) {
            this.proto = fieldDescriptorProto;
        }

        public Builder internalMergeFrom(Builder builder, MessageLite messageLite) {
            return ((Message.Builder) builder).mergeFrom((Message) messageLite);
        }
    }

    public static final class MethodDescriptor implements C0217b {
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private Descriptor inputType;
        private Descriptor outputType;
        private MethodDescriptorProto proto;
        private final ServiceDescriptor service;

        public int getIndex() {
            return this.index;
        }

        public MethodDescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public String getFullName() {
            return this.fullName;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public ServiceDescriptor getService() {
            return this.service;
        }

        public Descriptor getInputType() {
            return this.inputType;
        }

        public Descriptor getOutputType() {
            return this.outputType;
        }

        public MethodOptions getOptions() {
            return this.proto.getOptions();
        }

        private MethodDescriptor(MethodDescriptorProto methodDescriptorProto, FileDescriptor fileDescriptor, ServiceDescriptor serviceDescriptor, int i) throws DescriptorValidationException {
            this.index = i;
            this.proto = methodDescriptorProto;
            this.file = fileDescriptor;
            this.service = serviceDescriptor;
            this.fullName = serviceDescriptor.getFullName() + '.' + methodDescriptorProto.getName();
            fileDescriptor.pool.m931a((C0217b) this);
        }

        private void crossLink() throws DescriptorValidationException {
            C0217b a = this.file.pool.m928a(this.proto.getInputType(), (C0217b) this);
            if (a instanceof Descriptor) {
                this.inputType = (Descriptor) a;
                a = this.file.pool.m928a(this.proto.getOutputType(), (C0217b) this);
                if (a instanceof Descriptor) {
                    this.outputType = (Descriptor) a;
                    return;
                }
                throw new DescriptorValidationException('\"' + this.proto.getOutputType() + "\" is not a message type.", null);
            }
            throw new DescriptorValidationException('\"' + this.proto.getInputType() + "\" is not a message type.", null);
        }

        private void setProto(MethodDescriptorProto methodDescriptorProto) {
            this.proto = methodDescriptorProto;
        }
    }

    public static final class ServiceDescriptor implements C0217b {
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private MethodDescriptor[] methods;
        private ServiceDescriptorProto proto;

        public int getIndex() {
            return this.index;
        }

        public ServiceDescriptorProto toProto() {
            return this.proto;
        }

        public String getName() {
            return this.proto.getName();
        }

        public String getFullName() {
            return this.fullName;
        }

        public FileDescriptor getFile() {
            return this.file;
        }

        public ServiceOptions getOptions() {
            return this.proto.getOptions();
        }

        public List<MethodDescriptor> getMethods() {
            return Collections.unmodifiableList(Arrays.asList(this.methods));
        }

        public MethodDescriptor findMethodByName(String str) {
            C0217b a = this.file.pool.m927a(this.fullName + '.' + str);
            if (a == null || !(a instanceof MethodDescriptor)) {
                return null;
            }
            return (MethodDescriptor) a;
        }

        private ServiceDescriptor(ServiceDescriptorProto serviceDescriptorProto, FileDescriptor fileDescriptor, int i) throws DescriptorValidationException {
            this.index = i;
            this.proto = serviceDescriptorProto;
            this.fullName = Descriptors.computeFullName(fileDescriptor, null, serviceDescriptorProto.getName());
            this.file = fileDescriptor;
            this.methods = new MethodDescriptor[serviceDescriptorProto.getMethodCount()];
            for (int i2 = 0; i2 < serviceDescriptorProto.getMethodCount(); i2++) {
                this.methods[i2] = new MethodDescriptor(fileDescriptor, this, i2, null);
            }
            fileDescriptor.pool.m931a((C0217b) this);
        }

        private void crossLink() throws DescriptorValidationException {
            for (MethodDescriptor access$2200 : this.methods) {
                access$2200.crossLink();
            }
        }

        private void setProto(ServiceDescriptorProto serviceDescriptorProto) {
            this.proto = serviceDescriptorProto;
            for (int i = 0; i < this.methods.length; i++) {
                this.methods[i].setProto(serviceDescriptorProto.getMethod(i));
            }
        }
    }

    private static String computeFullName(FileDescriptor fileDescriptor, Descriptor descriptor, String str) {
        if (descriptor != null) {
            return descriptor.getFullName() + '.' + str;
        }
        if (fileDescriptor.getPackage().length() > 0) {
            return fileDescriptor.getPackage() + '.' + str;
        }
        return str;
    }
}
