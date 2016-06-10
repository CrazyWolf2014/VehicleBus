package org.codehaus.jackson.org.objectweb.asm;

import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.ksoap2.transport.ServiceConnection;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;

public class ClassWriter implements ClassVisitor {
    public static final int COMPUTE_FRAMES = 2;
    public static final int COMPUTE_MAXS = 1;
    static final byte[] f2250a;
    MethodWriter f2251A;
    MethodWriter f2252B;
    private short f2253D;
    Item[] f2254E;
    String f2255F;
    private final boolean f2256G;
    private final boolean f2257H;
    boolean f2258I;
    ClassReader f2259J;
    int f2260b;
    int f2261c;
    final ByteVector f2262d;
    Item[] f2263e;
    int f2264f;
    final Item f2265g;
    final Item f2266h;
    final Item f2267i;
    private int f2268j;
    private int f2269k;
    private int f2270l;
    private int f2271m;
    private int f2272n;
    private int[] f2273o;
    private int f2274p;
    private ByteVector f2275q;
    private int f2276r;
    private int f2277s;
    private AnnotationWriter f2278t;
    private AnnotationWriter f2279u;
    private Attribute f2280v;
    private int f2281w;
    private ByteVector f2282x;
    FieldWriter f2283y;
    FieldWriter f2284z;

    static {
        byte[] bArr = new byte[220];
        String str = "AAAAAAAAAAAAAAAABCKLLDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIDNOAAAAAAGGGGGGGHHFBFAAFFAAQPIIJJIIIIIIIIIIIIIIIIII";
        for (int i = 0; i < bArr.length; i += COMPUTE_MAXS) {
            bArr[i] = (byte) (str.charAt(i) - 65);
        }
        f2250a = bArr;
    }

    public ClassWriter(int i) {
        boolean z = true;
        this.f2261c = COMPUTE_MAXS;
        this.f2262d = new ByteVector();
        this.f2263e = new Item[KEYRecord.OWNER_ZONE];
        this.f2264f = (int) (0.75d * ((double) this.f2263e.length));
        this.f2265g = new Item();
        this.f2266h = new Item();
        this.f2267i = new Item();
        this.f2257H = (i & COMPUTE_MAXS) != 0;
        if ((i & COMPUTE_FRAMES) == 0) {
            z = false;
        }
        this.f2256G = z;
    }

    public ClassWriter(ClassReader classReader, int i) {
        this(i);
        classReader.m1725a(this);
        this.f2259J = classReader;
    }

    private Item m2455a(Item item) {
        Item item2 = this.f2263e[item.f1706j % this.f2263e.length];
        while (item2 != null && (item2.f1700b != item.f1700b || !item.m1745a(item2))) {
            item2 = item2.f1707k;
        }
        return item2;
    }

    private void m2456a(int i, int i2, int i3) {
        this.f2262d.m1717b(i, i2).putShort(i3);
    }

    private Item m2457b(String str) {
        this.f2266h.m1743a(8, str, null, null);
        Item a = m2455a(this.f2266h);
        if (a != null) {
            return a;
        }
        this.f2262d.m1717b(8, newUTF8(str));
        int i = this.f2261c;
        this.f2261c = i + COMPUTE_MAXS;
        a = new Item(i, this.f2266h);
        m2458b(a);
        return a;
    }

    private void m2458b(Item item) {
        int length;
        if (this.f2261c > this.f2264f) {
            length = this.f2263e.length;
            int i = (length * COMPUTE_FRAMES) + COMPUTE_MAXS;
            Item[] itemArr = new Item[i];
            for (int i2 = length - 1; i2 >= 0; i2--) {
                Item item2 = this.f2263e[i2];
                while (item2 != null) {
                    int length2 = item2.f1706j % itemArr.length;
                    Item item3 = item2.f1707k;
                    item2.f1707k = itemArr[length2];
                    itemArr[length2] = item2;
                    item2 = item3;
                }
            }
            this.f2263e = itemArr;
            this.f2264f = (int) (((double) i) * 0.75d);
        }
        length = item.f1706j % this.f2263e.length;
        item.f1707k = this.f2263e[length];
        this.f2263e[length] = item;
    }

    private Item m2459c(Item item) {
        this.f2253D = (short) (this.f2253D + COMPUTE_MAXS);
        Item item2 = new Item(this.f2253D, this.f2265g);
        m2458b(item2);
        if (this.f2254E == null) {
            this.f2254E = new Item[16];
        }
        if (this.f2253D == this.f2254E.length) {
            Object obj = new Item[(this.f2254E.length * COMPUTE_FRAMES)];
            System.arraycopy(this.f2254E, 0, obj, 0, this.f2254E.length);
            this.f2254E = obj;
        }
        this.f2254E[this.f2253D] = item2;
        return item2;
    }

    int m2460a(int i, int i2) {
        this.f2266h.f1700b = 15;
        this.f2266h.f1702d = ((long) i) | (((long) i2) << 32);
        this.f2266h.f1706j = Integer.MAX_VALUE & ((i + 15) + i2);
        Item a = m2455a(this.f2266h);
        if (a == null) {
            String str = this.f2254E[i].f1703g;
            String str2 = this.f2254E[i2].f1703g;
            this.f2266h.f1701c = m2471c(getCommonSuperClass(str, str2));
            a = new Item(0, this.f2266h);
            m2458b(a);
        }
        return a.f1701c;
    }

    int m2461a(String str, int i) {
        this.f2265g.f1700b = 14;
        this.f2265g.f1701c = i;
        this.f2265g.f1703g = str;
        this.f2265g.f1706j = Integer.MAX_VALUE & ((str.hashCode() + 14) + i);
        Item a = m2455a(this.f2265g);
        if (a == null) {
            a = m2459c(this.f2265g);
        }
        return a.f1699a;
    }

    Item m2462a(double d) {
        this.f2265g.m1740a(d);
        Item a = m2455a(this.f2265g);
        if (a != null) {
            return a;
        }
        this.f2262d.putByte(6).putLong(this.f2265g.f1702d);
        a = new Item(this.f2261c, this.f2265g);
        m2458b(a);
        this.f2261c += COMPUTE_FRAMES;
        return a;
    }

    Item m2463a(float f) {
        this.f2265g.m1741a(f);
        Item a = m2455a(this.f2265g);
        if (a != null) {
            return a;
        }
        this.f2262d.putByte(4).putInt(this.f2265g.f1701c);
        int i = this.f2261c;
        this.f2261c = i + COMPUTE_MAXS;
        a = new Item(i, this.f2265g);
        m2458b(a);
        return a;
    }

    Item m2464a(int i) {
        this.f2265g.m1742a(i);
        Item a = m2455a(this.f2265g);
        if (a != null) {
            return a;
        }
        this.f2262d.putByte(3).putInt(i);
        int i2 = this.f2261c;
        this.f2261c = i2 + COMPUTE_MAXS;
        a = new Item(i2, this.f2265g);
        m2458b(a);
        return a;
    }

    Item m2465a(long j) {
        this.f2265g.m1744a(j);
        Item a = m2455a(this.f2265g);
        if (a != null) {
            return a;
        }
        this.f2262d.putByte(5).putLong(j);
        a = new Item(this.f2261c, this.f2265g);
        m2458b(a);
        this.f2261c += COMPUTE_FRAMES;
        return a;
    }

    Item m2466a(Object obj) {
        if (obj instanceof Integer) {
            return m2464a(((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return m2464a(((Byte) obj).intValue());
        }
        if (obj instanceof Character) {
            return m2464a(((Character) obj).charValue());
        }
        if (obj instanceof Short) {
            return m2464a(((Short) obj).intValue());
        }
        if (obj instanceof Boolean) {
            return m2464a(((Boolean) obj).booleanValue() ? COMPUTE_MAXS : 0);
        } else if (obj instanceof Float) {
            return m2463a(((Float) obj).floatValue());
        } else {
            if (obj instanceof Long) {
                return m2465a(((Long) obj).longValue());
            }
            if (obj instanceof Double) {
                return m2462a(((Double) obj).doubleValue());
            }
            if (obj instanceof String) {
                return m2457b((String) obj);
            }
            if (obj instanceof Type) {
                Type type = (Type) obj;
                return m2467a(type.getSort() == 10 ? type.getInternalName() : type.getDescriptor());
            }
            throw new IllegalArgumentException(new StringBuffer().append("value ").append(obj).toString());
        }
    }

    Item m2467a(String str) {
        this.f2266h.m1743a(7, str, null, null);
        Item a = m2455a(this.f2266h);
        if (a != null) {
            return a;
        }
        this.f2262d.m1717b(7, newUTF8(str));
        int i = this.f2261c;
        this.f2261c = i + COMPUTE_MAXS;
        a = new Item(i, this.f2266h);
        m2458b(a);
        return a;
    }

    Item m2468a(String str, String str2) {
        this.f2266h.m1743a(12, str, str2, null);
        Item a = m2455a(this.f2266h);
        if (a != null) {
            return a;
        }
        m2456a(12, newUTF8(str), newUTF8(str2));
        int i = this.f2261c;
        this.f2261c = i + COMPUTE_MAXS;
        a = new Item(i, this.f2266h);
        m2458b(a);
        return a;
    }

    Item m2469a(String str, String str2, String str3) {
        this.f2267i.m1743a(9, str, str2, str3);
        Item a = m2455a(this.f2267i);
        if (a != null) {
            return a;
        }
        m2456a(9, newClass(str), newNameType(str2, str3));
        int i = this.f2261c;
        this.f2261c = i + COMPUTE_MAXS;
        a = new Item(i, this.f2267i);
        m2458b(a);
        return a;
    }

    Item m2470a(String str, String str2, String str3, boolean z) {
        int i = z ? 11 : 10;
        this.f2267i.m1743a(i, str, str2, str3);
        Item a = m2455a(this.f2267i);
        if (a != null) {
            return a;
        }
        m2456a(i, newClass(str), newNameType(str2, str3));
        int i2 = this.f2261c;
        this.f2261c = i2 + COMPUTE_MAXS;
        Item item = new Item(i2, this.f2267i);
        m2458b(item);
        return item;
    }

    int m2471c(String str) {
        this.f2265g.m1743a(13, str, null, null);
        Item a = m2455a(this.f2265g);
        if (a == null) {
            a = m2459c(this.f2265g);
        }
        return a.f1699a;
    }

    protected String getCommonSuperClass(String str, String str2) {
        try {
            Class cls = Class.forName(str.replace('/', '.'));
            Class cls2 = Class.forName(str2.replace('/', '.'));
            if (cls.isAssignableFrom(cls2)) {
                return str;
            }
            if (cls2.isAssignableFrom(cls)) {
                return str2;
            }
            if (cls.isInterface() || cls2.isInterface()) {
                return "java/lang/Object";
            }
            do {
                cls = cls.getSuperclass();
            } while (!cls.isAssignableFrom(cls2));
            return cls.getName().replace('.', '/');
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public int newClass(String str) {
        return m2467a(str).f1699a;
    }

    public int newConst(Object obj) {
        return m2466a(obj).f1699a;
    }

    public int newField(String str, String str2, String str3) {
        return m2469a(str, str2, str3).f1699a;
    }

    public int newMethod(String str, String str2, String str3, boolean z) {
        return m2470a(str, str2, str3, z).f1699a;
    }

    public int newNameType(String str, String str2) {
        return m2468a(str, str2).f1699a;
    }

    public int newUTF8(String str) {
        this.f2265g.m1743a(COMPUTE_MAXS, str, null, null);
        Item a = m2455a(this.f2265g);
        if (a == null) {
            this.f2262d.putByte(COMPUTE_MAXS).putUTF8(str);
            int i = this.f2261c;
            this.f2261c = i + COMPUTE_MAXS;
            a = new Item(i, this.f2265g);
            m2458b(a);
        }
        return a.f1699a;
    }

    public byte[] toByteArray() {
        int i;
        int i2 = (this.f2272n * COMPUTE_FRAMES) + 24;
        FieldWriter fieldWriter = this.f2283y;
        int i3 = 0;
        while (fieldWriter != null) {
            int i4 = i3 + COMPUTE_MAXS;
            i2 += fieldWriter.m2472a();
            fieldWriter = fieldWriter.f2285a;
            i3 = i4;
        }
        MethodWriter methodWriter = this.f2251A;
        int i5 = 0;
        while (methodWriter != null) {
            i4 = i5 + COMPUTE_MAXS;
            i2 += methodWriter.m2490a();
            methodWriter = methodWriter.f2314a;
            i5 = i4;
        }
        if (this.f2270l != 0) {
            i = COMPUTE_MAXS;
            i2 += 8;
            newUTF8("Signature");
        } else {
            i = 0;
        }
        if (this.f2274p != 0) {
            i += COMPUTE_MAXS;
            i2 += 8;
            newUTF8("SourceFile");
        }
        if (this.f2275q != null) {
            i += COMPUTE_MAXS;
            i2 += this.f2275q.f1676b + 4;
            newUTF8("SourceDebugExtension");
        }
        if (this.f2276r != 0) {
            i += COMPUTE_MAXS;
            i2 += 10;
            newUTF8("EnclosingMethod");
        }
        if ((this.f2268j & Opcodes.ACC_DEPRECATED) != 0) {
            i += COMPUTE_MAXS;
            i2 += 6;
            newUTF8("Deprecated");
        }
        if ((this.f2268j & Flags.EXTEND) != 0 && ((this.f2260b & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) < 49 || (this.f2268j & ServiceConnection.DEFAULT_BUFFER_SIZE) != 0)) {
            i += COMPUTE_MAXS;
            i2 += 6;
            newUTF8("Synthetic");
        }
        if (this.f2282x != null) {
            i += COMPUTE_MAXS;
            i2 += this.f2282x.f1676b + 8;
            newUTF8("InnerClasses");
        }
        if (this.f2278t != null) {
            i += COMPUTE_MAXS;
            i2 += this.f2278t.m2453a() + 8;
            newUTF8("RuntimeVisibleAnnotations");
        }
        if (this.f2279u != null) {
            i += COMPUTE_MAXS;
            i2 += this.f2279u.m2453a() + 8;
            newUTF8("RuntimeInvisibleAnnotations");
        }
        int i6 = i2;
        if (this.f2280v != null) {
            i6 += this.f2280v.m1713a(this, null, 0, -1, -1);
            i += this.f2280v.m1712a();
        }
        ByteVector byteVector = new ByteVector(this.f2262d.f1676b + i6);
        byteVector.putInt(-889275714).putInt(this.f2260b);
        byteVector.putShort(this.f2261c).putByteArray(this.f2262d.f1675a, 0, this.f2262d.f1676b);
        byteVector.putShort(((393216 | ((this.f2268j & ServiceConnection.DEFAULT_BUFFER_SIZE) / 64)) ^ -1) & this.f2268j).putShort(this.f2269k).putShort(this.f2271m);
        byteVector.putShort(this.f2272n);
        for (i2 = 0; i2 < this.f2272n; i2 += COMPUTE_MAXS) {
            byteVector.putShort(this.f2273o[i2]);
        }
        byteVector.putShort(i3);
        for (FieldWriter fieldWriter2 = this.f2283y; fieldWriter2 != null; fieldWriter2 = fieldWriter2.f2285a) {
            fieldWriter2.m2473a(byteVector);
        }
        byteVector.putShort(i5);
        for (MethodWriter methodWriter2 = this.f2251A; methodWriter2 != null; methodWriter2 = methodWriter2.f2314a) {
            methodWriter2.m2491a(byteVector);
        }
        byteVector.putShort(i);
        if (this.f2270l != 0) {
            byteVector.putShort(newUTF8("Signature")).putInt(COMPUTE_FRAMES).putShort(this.f2270l);
        }
        if (this.f2274p != 0) {
            byteVector.putShort(newUTF8("SourceFile")).putInt(COMPUTE_FRAMES).putShort(this.f2274p);
        }
        if (this.f2275q != null) {
            i = this.f2275q.f1676b - 2;
            byteVector.putShort(newUTF8("SourceDebugExtension")).putInt(i);
            byteVector.putByteArray(this.f2275q.f1675a, COMPUTE_FRAMES, i);
        }
        if (this.f2276r != 0) {
            byteVector.putShort(newUTF8("EnclosingMethod")).putInt(4);
            byteVector.putShort(this.f2276r).putShort(this.f2277s);
        }
        if ((this.f2268j & Opcodes.ACC_DEPRECATED) != 0) {
            byteVector.putShort(newUTF8("Deprecated")).putInt(0);
        }
        if ((this.f2268j & Flags.EXTEND) != 0 && ((this.f2260b & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) < 49 || (this.f2268j & ServiceConnection.DEFAULT_BUFFER_SIZE) != 0)) {
            byteVector.putShort(newUTF8("Synthetic")).putInt(0);
        }
        if (this.f2282x != null) {
            byteVector.putShort(newUTF8("InnerClasses"));
            byteVector.putInt(this.f2282x.f1676b + COMPUTE_FRAMES).putShort(this.f2281w);
            byteVector.putByteArray(this.f2282x.f1675a, 0, this.f2282x.f1676b);
        }
        if (this.f2278t != null) {
            byteVector.putShort(newUTF8("RuntimeVisibleAnnotations"));
            this.f2278t.m2454a(byteVector);
        }
        if (this.f2279u != null) {
            byteVector.putShort(newUTF8("RuntimeInvisibleAnnotations"));
            this.f2279u.m2454a(byteVector);
        }
        if (this.f2280v != null) {
            this.f2280v.m1714a(this, null, 0, -1, -1, byteVector);
        }
        if (!this.f2258I) {
            return byteVector.f1675a;
        }
        Object classWriter = new ClassWriter(COMPUTE_FRAMES);
        new ClassReader(byteVector.f1675a).accept(classWriter, 4);
        return classWriter.toByteArray();
    }

    public void visit(int i, int i2, String str, String str2, String str3, String[] strArr) {
        int i3 = 0;
        this.f2260b = i;
        this.f2268j = i2;
        this.f2269k = newClass(str);
        this.f2255F = str;
        if (str2 != null) {
            this.f2270l = newUTF8(str2);
        }
        this.f2271m = str3 == null ? 0 : newClass(str3);
        if (strArr != null && strArr.length > 0) {
            this.f2272n = strArr.length;
            this.f2273o = new int[this.f2272n];
            while (i3 < this.f2272n) {
                this.f2273o[i3] = newClass(strArr[i3]);
                i3 += COMPUTE_MAXS;
            }
        }
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, COMPUTE_FRAMES);
        if (z) {
            annotationWriter.f2248g = this.f2278t;
            this.f2278t = annotationWriter;
        } else {
            annotationWriter.f2248g = this.f2279u;
            this.f2279u = annotationWriter;
        }
        return annotationWriter;
    }

    public void visitAttribute(Attribute attribute) {
        attribute.f1673a = this.f2280v;
        this.f2280v = attribute;
    }

    public void visitEnd() {
    }

    public FieldVisitor visitField(int i, String str, String str2, String str3, Object obj) {
        return new FieldWriter(this, i, str, str2, str3, obj);
    }

    public void visitInnerClass(String str, String str2, String str3, int i) {
        int i2 = 0;
        if (this.f2282x == null) {
            this.f2282x = new ByteVector();
        }
        this.f2281w += COMPUTE_MAXS;
        this.f2282x.putShort(str == null ? 0 : newClass(str));
        this.f2282x.putShort(str2 == null ? 0 : newClass(str2));
        ByteVector byteVector = this.f2282x;
        if (str3 != null) {
            i2 = newUTF8(str3);
        }
        byteVector.putShort(i2);
        this.f2282x.putShort(i);
    }

    public MethodVisitor visitMethod(int i, String str, String str2, String str3, String[] strArr) {
        return new MethodWriter(this, i, str, str2, str3, strArr, this.f2257H, this.f2256G);
    }

    public void visitOuterClass(String str, String str2, String str3) {
        this.f2276r = newClass(str);
        if (str2 != null && str3 != null) {
            this.f2277s = newNameType(str2, str3);
        }
    }

    public void visitSource(String str, String str2) {
        if (str != null) {
            this.f2274p = newUTF8(str);
        }
        if (str2 != null) {
            this.f2275q = new ByteVector().putUTF8(str2);
        }
    }
}
