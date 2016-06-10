package org.codehaus.jackson.org.objectweb.asm;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.tencent.mm.sdk.openapi.BaseResp.ErrCode;
import org.apache.harmony.javax.security.auth.callback.ConfirmationCallback;
import org.codehaus.jackson.Base64Variant;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.ksoap2.transport.ServiceConnection;
import org.kxml2.wap.Wbxml;
import org.kxml2.wap.WbxmlParser;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

class MethodWriter implements MethodVisitor {
    private int f2295A;
    private Handler f2296B;
    private Handler f2297C;
    private int f2298D;
    private ByteVector f2299E;
    private int f2300F;
    private ByteVector f2301G;
    private int f2302H;
    private ByteVector f2303I;
    private Attribute f2304J;
    private boolean f2305K;
    private int f2306L;
    private final int f2307M;
    private Label f2308N;
    private Label f2309O;
    private Label f2310P;
    private int f2311Q;
    private int f2312R;
    private int f2313S;
    MethodWriter f2314a;
    final ClassWriter f2315b;
    private int f2316c;
    private final int f2317d;
    private final int f2318e;
    private final String f2319f;
    String f2320g;
    int f2321h;
    int f2322i;
    int f2323j;
    int[] f2324k;
    private ByteVector f2325l;
    private AnnotationWriter f2326m;
    private AnnotationWriter f2327n;
    private AnnotationWriter[] f2328o;
    private AnnotationWriter[] f2329p;
    private Attribute f2330q;
    private ByteVector f2331r;
    private int f2332s;
    private int f2333t;
    private int f2334u;
    private ByteVector f2335v;
    private int f2336w;
    private int[] f2337x;
    private int f2338y;
    private int[] f2339z;

    MethodWriter(ClassWriter classWriter, int i, String str, String str2, String str3, String[] strArr, boolean z, boolean z2) {
        int i2;
        int i3 = 0;
        this.f2331r = new ByteVector();
        if (classWriter.f2251A == null) {
            classWriter.f2251A = this;
        } else {
            classWriter.f2252B.f2314a = this;
        }
        classWriter.f2252B = this;
        this.f2315b = classWriter;
        this.f2316c = i;
        this.f2317d = classWriter.newUTF8(str);
        this.f2318e = classWriter.newUTF8(str2);
        this.f2319f = str2;
        this.f2320g = str3;
        if (strArr != null && strArr.length > 0) {
            this.f2323j = strArr.length;
            this.f2324k = new int[this.f2323j];
            for (i2 = 0; i2 < this.f2323j; i2++) {
                this.f2324k[i2] = classWriter.newClass(strArr[i2]);
            }
        }
        if (!z2) {
            i3 = z ? 1 : 2;
        }
        this.f2307M = i3;
        if (z || z2) {
            if (z2 && "<init>".equals(str)) {
                this.f2316c |= ServiceConnection.DEFAULT_BUFFER_SIZE;
            }
            i2 = Type.getArgumentsAndReturnSizes(this.f2319f) >> 2;
            if ((i & 8) != 0) {
                i2--;
            }
            this.f2333t = i2;
            this.f2308N = new Label();
            Label label = this.f2308N;
            label.f1708a |= 8;
            visitLabel(this.f2308N);
        }
    }

    static int m2474a(byte[] bArr, int i) {
        return ((((bArr[i] & KEYRecord.PROTOCOL_ANY) << 24) | ((bArr[i + 1] & KEYRecord.PROTOCOL_ANY) << 16)) | ((bArr[i + 2] & KEYRecord.PROTOCOL_ANY) << 8)) | (bArr[i + 3] & KEYRecord.PROTOCOL_ANY);
    }

    static int m2475a(int[] iArr, int[] iArr2, int i, int i2) {
        int i3 = i2 - i;
        int i4 = 0;
        while (i4 < iArr.length) {
            if (i < iArr[i4] && iArr[i4] <= i2) {
                i3 += iArr2[i4];
            } else if (i2 < iArr[i4] && iArr[i4] <= i) {
                i3 -= iArr2[i4];
            }
            i4++;
        }
        return i3;
    }

    private void m2476a(int i, int i2) {
        while (i < i2) {
            int i3 = this.f2339z[i];
            int i4 = -268435456 & i3;
            if (i4 == 0) {
                i4 = i3 & 1048575;
                switch (i3 & 267386880) {
                    case 24117248:
                        this.f2335v.putByte(7).putShort(this.f2315b.newClass(this.f2315b.f2254E[i4].f1703g));
                        break;
                    case 25165824:
                        this.f2335v.putByte(8).putShort(this.f2315b.f2254E[i4].f1701c);
                        break;
                    default:
                        this.f2335v.putByte(i4);
                        break;
                }
            }
            StringBuffer stringBuffer = new StringBuffer();
            i4 >>= 28;
            while (true) {
                int i5 = i4 - 1;
                if (i4 > 0) {
                    stringBuffer.append('[');
                    i4 = i5;
                } else {
                    if ((i3 & 267386880) != 24117248) {
                        switch (i3 & 15) {
                            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                                stringBuffer.append('I');
                                break;
                            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                                stringBuffer.append('F');
                                break;
                            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                                stringBuffer.append('D');
                                break;
                            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                                stringBuffer.append('Z');
                                break;
                            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                                stringBuffer.append('B');
                                break;
                            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                                stringBuffer.append('C');
                                break;
                            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                                stringBuffer.append('S');
                                break;
                            default:
                                stringBuffer.append('J');
                                break;
                        }
                    }
                    stringBuffer.append('L');
                    stringBuffer.append(this.f2315b.f2254E[i3 & 1048575].f1703g);
                    stringBuffer.append(';');
                    this.f2335v.putByte(7).putShort(this.f2315b.newClass(stringBuffer.toString()));
                }
            }
            i++;
        }
    }

    private void m2477a(int i, int i2, int i3) {
        int i4 = (i2 + 3) + i3;
        if (this.f2339z == null || this.f2339z.length < i4) {
            this.f2339z = new int[i4];
        }
        this.f2339z[0] = i;
        this.f2339z[1] = i2;
        this.f2339z[2] = i3;
        this.f2338y = 3;
    }

    private void m2478a(int i, Label label) {
        Edge edge = new Edge();
        edge.f1681a = i;
        edge.f1682b = label;
        edge.f1683c = this.f2310P.f1717j;
        this.f2310P.f1717j = edge;
    }

    private void m2479a(Object obj) {
        if (obj instanceof String) {
            this.f2335v.putByte(7).putShort(this.f2315b.newClass((String) obj));
        } else if (obj instanceof Integer) {
            this.f2335v.putByte(((Integer) obj).intValue());
        } else {
            this.f2335v.putByte(8).putShort(((Label) obj).f1710c);
        }
    }

    private void m2480a(Label label, Label[] labelArr) {
        int i = 0;
        if (this.f2310P != null) {
            if (this.f2307M == 0) {
                this.f2310P.f1715h.m1737a((int) Opcodes.LOOKUPSWITCH, 0, null, null);
                m2478a(0, label);
                Label a = label.m1747a();
                a.f1708a |= 16;
                for (int i2 = 0; i2 < labelArr.length; i2++) {
                    m2478a(0, labelArr[i2]);
                    Label a2 = labelArr[i2].m1747a();
                    a2.f1708a |= 16;
                }
            } else {
                this.f2311Q--;
                m2478a(this.f2311Q, label);
                while (i < labelArr.length) {
                    m2478a(this.f2311Q, labelArr[i]);
                    i++;
                }
            }
            m2489e();
        }
    }

    static void m2481a(byte[] bArr, int i, int i2) {
        bArr[i] = (byte) (i2 >>> 8);
        bArr[i + 1] = (byte) i2;
    }

    static void m2482a(int[] iArr, int[] iArr2, Label label) {
        if ((label.f1708a & 4) == 0) {
            label.f1710c = m2475a(iArr, iArr2, 0, label.f1710c);
            label.f1708a |= 4;
        }
    }

    static short m2483b(byte[] bArr, int i) {
        return (short) (((bArr[i] & KEYRecord.PROTOCOL_ANY) << 8) | (bArr[i + 1] & KEYRecord.PROTOCOL_ANY));
    }

    private void m2484b() {
        if (this.f2337x != null) {
            if (this.f2335v == null) {
                this.f2335v = new ByteVector();
            }
            m2487c();
            this.f2334u++;
        }
        this.f2337x = this.f2339z;
        this.f2339z = null;
    }

    private void m2485b(Frame frame) {
        int i = 0;
        int[] iArr = frame.f1686c;
        int[] iArr2 = frame.f1687d;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i2 < iArr.length) {
            int i5 = iArr[i2];
            if (i5 == 16777216) {
                i4++;
            } else {
                i3 += i4 + 1;
                i4 = 0;
            }
            if (i5 == 16777220 || i5 == 16777219) {
                i2++;
            }
            i2++;
        }
        i2 = 0;
        i4 = 0;
        while (i2 < iArr2.length) {
            i5 = iArr2[i2];
            i4++;
            if (i5 == 16777220 || i5 == 16777219) {
                i2++;
            }
            i2++;
        }
        m2477a(frame.f1685b.f1710c, i3, i4);
        i2 = 0;
        while (i3 > 0) {
            i4 = iArr[i2];
            int[] iArr3 = this.f2339z;
            int i6 = this.f2338y;
            this.f2338y = i6 + 1;
            iArr3[i6] = i4;
            if (i4 == 16777220 || i4 == 16777219) {
                i2++;
            }
            i2++;
            i3--;
        }
        while (i < iArr2.length) {
            i2 = iArr2[i];
            int[] iArr4 = this.f2339z;
            i4 = this.f2338y;
            this.f2338y = i4 + 1;
            iArr4[i4] = i2;
            if (i2 == 16777220 || i2 == 16777219) {
                i++;
            }
            i++;
        }
        m2484b();
    }

    static int m2486c(byte[] bArr, int i) {
        return ((bArr[i] & KEYRecord.PROTOCOL_ANY) << 8) | (bArr[i + 1] & KEYRecord.PROTOCOL_ANY);
    }

    private void m2487c() {
        int i = 64;
        int i2 = 0;
        int i3 = this.f2339z[1];
        int i4 = this.f2339z[2];
        if ((this.f2315b.f2260b & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) < 50) {
            this.f2335v.putShort(this.f2339z[0]).putShort(i3);
            m2476a(3, i3 + 3);
            this.f2335v.putShort(i4);
            m2476a(i3 + 3, (i3 + 3) + i4);
            return;
        }
        int i5;
        int i6;
        int i7 = this.f2337x[1];
        int i8 = this.f2334u == 0 ? this.f2339z[0] : (this.f2339z[0] - this.f2337x[0]) - 1;
        if (i4 == 0) {
            i5 = i3 - i7;
            switch (i5) {
                case ErrCode.ERR_SENT_FAILED /*-3*/:
                case Base64Variant.BASE64_VALUE_PADDING /*-2*/:
                case ConfirmationCallback.UNSPECIFIED_OPTION /*-1*/:
                    i = 248;
                    i7 = i3;
                    break;
                case KEYRecord.OWNER_USER /*0*/:
                    if (i8 >= 64) {
                        i = Type.IXFR;
                        break;
                    } else {
                        i = 0;
                        break;
                    }
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    i = Type.AXFR;
                    break;
                default:
                    i = KEYRecord.PROTOCOL_ANY;
                    break;
            }
            i6 = i7;
        } else if (i3 == i7 && i4 == 1) {
            if (i8 >= 63) {
                i = 247;
            }
            i5 = 0;
            i6 = i7;
        } else {
            i5 = 0;
            i = KEYRecord.PROTOCOL_ANY;
            i6 = i7;
        }
        if (i != 255) {
            i7 = 3;
            while (i2 < i6) {
                if (this.f2339z[i7] != this.f2337x[i7]) {
                    i = KEYRecord.PROTOCOL_ANY;
                } else {
                    i7++;
                    i2++;
                }
            }
        }
        switch (i) {
            case KEYRecord.OWNER_USER /*0*/:
                this.f2335v.putByte(i8);
            case WbxmlParser.WAP_EXTENSION /*64*/:
                this.f2335v.putByte(i8 + 64);
                m2476a(i3 + 3, i3 + 4);
            case 247:
                this.f2335v.putByte(247).putShort(i8);
                m2476a(i3 + 3, i3 + 4);
            case 248:
                this.f2335v.putByte(i5 + Type.IXFR).putShort(i8);
            case Type.IXFR /*251*/:
                this.f2335v.putByte(Type.IXFR).putShort(i8);
            case Type.AXFR /*252*/:
                this.f2335v.putByte(i5 + Type.IXFR).putShort(i8);
                m2476a(i6 + 3, i3 + 3);
            default:
                this.f2335v.putByte(KEYRecord.PROTOCOL_ANY).putShort(i8).putShort(i3);
                m2476a(3, i3 + 3);
                this.f2335v.putShort(i4);
                m2476a(i3 + 3, (i3 + 3) + i4);
        }
    }

    private void m2488d() {
        byte[] bArr = this.f2331r.f1675a;
        int[] iArr = new int[0];
        int[] iArr2 = new int[0];
        boolean[] zArr = new boolean[this.f2331r.f1676b];
        int i = 3;
        while (true) {
            int c;
            if (i == 3) {
                i = 2;
            }
            int i2 = i;
            i = 0;
            while (i < bArr.length) {
                int i3 = bArr[i] & KEYRecord.PROTOCOL_ANY;
                int i4 = 0;
                switch (ClassWriter.f2250a[i3]) {
                    case KEYRecord.OWNER_USER /*0*/:
                    case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                        i++;
                        break;
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                        i += 2;
                        break;
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                    case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                        i += 3;
                        break;
                    case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                        i += 5;
                        break;
                    case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                        if (i3 > 201) {
                            i3 = i3 < 218 ? i3 - 49 : i3 - 20;
                            c = m2486c(bArr, i + 1) + i;
                        } else {
                            c = m2483b(bArr, i + 1) + i;
                        }
                        c = m2475a(iArr, iArr2, i, c);
                        if ((c < -32768 || c > 32767) && !zArr[i]) {
                            c = (i3 == Opcodes.GOTO || i3 == Opcodes.JSR) ? 2 : 5;
                            zArr[i] = true;
                        } else {
                            c = 0;
                        }
                        i += 3;
                        i4 = c;
                        break;
                    case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                        i += 5;
                        break;
                    case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                        if (i2 == 1) {
                            i4 = -(m2475a(iArr, iArr2, 0, i) & 3);
                        } else if (!zArr[i]) {
                            i4 = i & 3;
                            zArr[i] = true;
                        }
                        i = (i + 4) - (i & 3);
                        i += (((m2474a(bArr, i + 8) - m2474a(bArr, i + 4)) + 1) * 4) + 12;
                        break;
                    case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                        if (i2 == 1) {
                            i4 = -(m2475a(iArr, iArr2, 0, i) & 3);
                        } else if (!zArr[i]) {
                            i4 = i & 3;
                            zArr[i] = true;
                        }
                        i = (i + 4) - (i & 3);
                        i += (m2474a(bArr, i + 4) * 8) + 8;
                        break;
                    case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                        if ((bArr[i + 1] & KEYRecord.PROTOCOL_ANY) != Service.CISCO_SYS) {
                            i += 4;
                            break;
                        } else {
                            i += 6;
                            break;
                        }
                    default:
                        i += 4;
                        break;
                }
                if (i4 != 0) {
                    Object obj = new int[(iArr.length + 1)];
                    Object obj2 = new int[(iArr2.length + 1)];
                    System.arraycopy(iArr, 0, obj, 0, iArr.length);
                    System.arraycopy(iArr2, 0, obj2, 0, iArr2.length);
                    obj[iArr.length] = i;
                    obj2[iArr2.length] = i4;
                    if (i4 > 0) {
                        i2 = 3;
                        iArr2 = obj2;
                        iArr = obj;
                    } else {
                        iArr2 = obj2;
                        iArr = obj;
                    }
                }
            }
            if (i2 < 3) {
                i2--;
            }
            if (i2 == 0) {
                ByteVector byteVector = new ByteVector(this.f2331r.f1676b);
                i = 0;
                while (i < this.f2331r.f1676b) {
                    c = bArr[i] & KEYRecord.PROTOCOL_ANY;
                    int i5;
                    switch (ClassWriter.f2250a[c]) {
                        case KEYRecord.OWNER_USER /*0*/:
                        case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                            byteVector.putByte(c);
                            i++;
                            continue;
                        case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                        case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                            byteVector.putByteArray(bArr, i, 2);
                            i += 2;
                            continue;
                        case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                        case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                        case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                        case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                            byteVector.putByteArray(bArr, i, 3);
                            i += 3;
                            continue;
                        case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                            byteVector.putByteArray(bArr, i, 5);
                            i += 5;
                            continue;
                        case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                            if (c > 201) {
                                c = c < 218 ? c - 49 : c - 20;
                                i2 = m2486c(bArr, i + 1) + i;
                            } else {
                                i2 = m2483b(bArr, i + 1) + i;
                            }
                            i3 = m2475a(iArr, iArr2, i, i2);
                            if (zArr[i]) {
                                if (c == Opcodes.GOTO) {
                                    byteVector.putByte(AsyncTaskManager.REQUEST_SUCCESS_CODE);
                                    i2 = i3;
                                } else if (c == Opcodes.JSR) {
                                    byteVector.putByte(201);
                                    i2 = i3;
                                } else {
                                    byteVector.putByte(c <= Opcodes.IF_ACMPNE ? ((c + 1) ^ 1) - 1 : c ^ 1);
                                    byteVector.putShort(8);
                                    byteVector.putByte(AsyncTaskManager.REQUEST_SUCCESS_CODE);
                                    i2 = i3 - 3;
                                }
                                byteVector.putInt(i2);
                            } else {
                                byteVector.putByte(c);
                                byteVector.putShort(i3);
                            }
                            i += 3;
                            continue;
                        case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                            i2 = m2475a(iArr, iArr2, i, m2474a(bArr, i + 1) + i);
                            byteVector.putByte(c);
                            byteVector.putInt(i2);
                            i += 5;
                            continue;
                        case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                            i2 = (i + 4) - (i & 3);
                            byteVector.putByte(Opcodes.TABLESWITCH);
                            byteVector.putByteArray(null, 0, (4 - (byteVector.f1676b % 4)) % 4);
                            i2 += 4;
                            byteVector.putInt(m2475a(iArr, iArr2, i, m2474a(bArr, i2) + i));
                            c = m2474a(bArr, i2);
                            i3 = i2 + 4;
                            byteVector.putInt(c);
                            i2 = (m2474a(bArr, i3) - c) + 1;
                            c = i3 + 4;
                            byteVector.putInt(m2474a(bArr, c - 4));
                            i5 = i2;
                            i2 = c;
                            c = i5;
                            while (c > 0) {
                                i3 = i2 + 4;
                                byteVector.putInt(m2475a(iArr, iArr2, i, i + m2474a(bArr, i2)));
                                c--;
                                i2 = i3;
                            }
                            break;
                        case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                            i2 = (i + 4) - (i & 3);
                            byteVector.putByte(Opcodes.LOOKUPSWITCH);
                            byteVector.putByteArray(null, 0, (4 - (byteVector.f1676b % 4)) % 4);
                            i3 = i2 + 4;
                            byteVector.putInt(m2475a(iArr, iArr2, i, m2474a(bArr, i2) + i));
                            i2 = m2474a(bArr, i3);
                            c = i3 + 4;
                            byteVector.putInt(i2);
                            i5 = i2;
                            i2 = c;
                            c = i5;
                            while (c > 0) {
                                byteVector.putInt(m2474a(bArr, i2));
                                i2 += 4;
                                i3 = i2 + 4;
                                byteVector.putInt(m2475a(iArr, iArr2, i, i + m2474a(bArr, i2)));
                                c--;
                                i2 = i3;
                            }
                            break;
                        case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                            if ((bArr[i + 1] & KEYRecord.PROTOCOL_ANY) != Service.CISCO_SYS) {
                                byteVector.putByteArray(bArr, i, 4);
                                i += 4;
                                break;
                            }
                            byteVector.putByteArray(bArr, i, 6);
                            i += 6;
                            continue;
                        default:
                            byteVector.putByteArray(bArr, i, 4);
                            i += 4;
                            continue;
                    }
                    i = i2;
                }
                if (this.f2334u > 0) {
                    if (this.f2307M == 0) {
                        this.f2334u = 0;
                        this.f2335v = null;
                        this.f2337x = null;
                        this.f2339z = null;
                        Frame frame = new Frame();
                        frame.f1685b = this.f2308N;
                        frame.m1738a(this.f2315b, this.f2316c, Type.getArgumentTypes(this.f2319f), this.f2333t);
                        m2485b(frame);
                        for (Label label = this.f2308N; label != null; label = label.f1716i) {
                            i2 = label.f1710c - 3;
                            if ((label.f1708a & 32) != 0 || (i2 >= 0 && zArr[i2])) {
                                m2482a(iArr, iArr2, label);
                                m2485b(label.f1715h);
                            }
                        }
                    } else {
                        this.f2315b.f2258I = true;
                    }
                }
                for (Handler handler = this.f2296B; handler != null; handler = handler.f1698f) {
                    m2482a(iArr, iArr2, handler.f1693a);
                    m2482a(iArr, iArr2, handler.f1694b);
                    m2482a(iArr, iArr2, handler.f1695c);
                }
                c = 0;
                while (c < 2) {
                    ByteVector byteVector2 = c == 0 ? this.f2299E : this.f2301G;
                    if (byteVector2 != null) {
                        byte[] bArr2 = byteVector2.f1675a;
                        for (i = 0; i < byteVector2.f1676b; i += 10) {
                            int c2 = m2486c(bArr2, i);
                            int a = m2475a(iArr, iArr2, 0, c2);
                            m2481a(bArr2, i, a);
                            m2481a(bArr2, i + 2, m2475a(iArr, iArr2, 0, c2 + m2486c(bArr2, i + 2)) - a);
                        }
                    }
                    c++;
                }
                if (this.f2303I != null) {
                    byte[] bArr3 = this.f2303I.f1675a;
                    for (i = 0; i < this.f2303I.f1676b; i += 4) {
                        m2481a(bArr3, i, m2475a(iArr, iArr2, 0, m2486c(bArr3, i)));
                    }
                }
                for (Attribute attribute = this.f2304J; attribute != null; attribute = attribute.f1673a) {
                    Label[] labels = attribute.getLabels();
                    if (labels != null) {
                        for (i = labels.length - 1; i >= 0; i--) {
                            m2482a(iArr, iArr2, labels[i]);
                        }
                    }
                }
                this.f2331r = byteVector;
                return;
            }
            i = i2;
        }
    }

    private void m2489e() {
        if (this.f2307M == 0) {
            Label label = new Label();
            label.f1715h = new Frame();
            label.f1715h.f1685b = label;
            label.m1752a(this, this.f2331r.f1676b, this.f2331r.f1675a);
            this.f2309O.f1716i = label;
            this.f2309O = label;
        } else {
            this.f2310P.f1714g = this.f2312R;
        }
        this.f2310P = null;
    }

    final int m2490a() {
        if (this.f2321h != 0) {
            return this.f2322i + 6;
        }
        int i;
        int length;
        if (this.f2305K) {
            m2488d();
        }
        int i2 = 8;
        if (this.f2331r.f1676b > 0) {
            this.f2315b.newUTF8("Code");
            i = ((this.f2331r.f1676b + 18) + (this.f2295A * 8)) + 8;
            if (this.f2299E != null) {
                this.f2315b.newUTF8("LocalVariableTable");
                i += this.f2299E.f1676b + 8;
            }
            if (this.f2301G != null) {
                this.f2315b.newUTF8("LocalVariableTypeTable");
                i += this.f2301G.f1676b + 8;
            }
            if (this.f2303I != null) {
                this.f2315b.newUTF8("LineNumberTable");
                i += this.f2303I.f1676b + 8;
            }
            if (this.f2335v != null) {
                this.f2315b.newUTF8(((this.f2315b.f2260b & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) >= 50 ? 1 : null) != null ? "StackMapTable" : "StackMap");
                i2 = i + (this.f2335v.f1676b + 8);
            } else {
                i2 = i;
            }
            if (this.f2304J != null) {
                i2 += this.f2304J.m1713a(this.f2315b, this.f2331r.f1675a, this.f2331r.f1676b, this.f2332s, this.f2333t);
            }
        }
        if (this.f2323j > 0) {
            this.f2315b.newUTF8("Exceptions");
            i2 += (this.f2323j * 2) + 8;
        }
        if ((this.f2316c & Flags.EXTEND) != 0 && ((this.f2315b.f2260b & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) < 49 || (this.f2316c & ServiceConnection.DEFAULT_BUFFER_SIZE) != 0)) {
            this.f2315b.newUTF8("Synthetic");
            i2 += 6;
        }
        if ((this.f2316c & Opcodes.ACC_DEPRECATED) != 0) {
            this.f2315b.newUTF8("Deprecated");
            i2 += 6;
        }
        if (this.f2320g != null) {
            this.f2315b.newUTF8("Signature");
            this.f2315b.newUTF8(this.f2320g);
            i2 += 8;
        }
        if (this.f2325l != null) {
            this.f2315b.newUTF8("AnnotationDefault");
            i2 += this.f2325l.f1676b + 6;
        }
        if (this.f2326m != null) {
            this.f2315b.newUTF8("RuntimeVisibleAnnotations");
            i2 += this.f2326m.m2453a() + 8;
        }
        if (this.f2327n != null) {
            this.f2315b.newUTF8("RuntimeInvisibleAnnotations");
            i2 += this.f2327n.m2453a() + 8;
        }
        if (this.f2328o != null) {
            this.f2315b.newUTF8("RuntimeVisibleParameterAnnotations");
            length = i2 + (((this.f2328o.length - this.f2313S) * 2) + 7);
            for (i = this.f2328o.length - 1; i >= this.f2313S; i--) {
                length += this.f2328o[i] == null ? 0 : this.f2328o[i].m2453a();
            }
        } else {
            length = i2;
        }
        if (this.f2329p != null) {
            this.f2315b.newUTF8("RuntimeInvisibleParameterAnnotations");
            length += ((this.f2329p.length - this.f2313S) * 2) + 7;
            for (i = this.f2329p.length - 1; i >= this.f2313S; i--) {
                length += this.f2329p[i] == null ? 0 : this.f2329p[i].m2453a();
            }
        }
        i2 = length;
        return this.f2330q != null ? i2 + this.f2330q.m1713a(this.f2315b, null, 0, -1, -1) : i2;
    }

    final void m2491a(ByteVector byteVector) {
        int i = 1;
        byteVector.putShort(((393216 | ((this.f2316c & ServiceConnection.DEFAULT_BUFFER_SIZE) / 64)) ^ -1) & this.f2316c).putShort(this.f2317d).putShort(this.f2318e);
        if (this.f2321h != 0) {
            byteVector.putByteArray(this.f2315b.f2259J.f1678b, this.f2321h, this.f2322i);
            return;
        }
        int i2 = this.f2331r.f1676b > 0 ? 1 : 0;
        if (this.f2323j > 0) {
            i2++;
        }
        if ((this.f2316c & Flags.EXTEND) != 0 && ((this.f2315b.f2260b & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) < 49 || (this.f2316c & ServiceConnection.DEFAULT_BUFFER_SIZE) != 0)) {
            i2++;
        }
        if ((this.f2316c & Opcodes.ACC_DEPRECATED) != 0) {
            i2++;
        }
        if (this.f2320g != null) {
            i2++;
        }
        if (this.f2325l != null) {
            i2++;
        }
        if (this.f2326m != null) {
            i2++;
        }
        if (this.f2327n != null) {
            i2++;
        }
        if (this.f2328o != null) {
            i2++;
        }
        if (this.f2329p != null) {
            i2++;
        }
        if (this.f2330q != null) {
            i2 += this.f2330q.m1712a();
        }
        byteVector.putShort(i2);
        if (this.f2331r.f1676b > 0) {
            i2 = (this.f2331r.f1676b + 12) + (this.f2295A * 8);
            if (this.f2299E != null) {
                i2 += this.f2299E.f1676b + 8;
            }
            if (this.f2301G != null) {
                i2 += this.f2301G.f1676b + 8;
            }
            if (this.f2303I != null) {
                i2 += this.f2303I.f1676b + 8;
            }
            int i3 = this.f2335v != null ? i2 + (this.f2335v.f1676b + 8) : i2;
            if (this.f2304J != null) {
                i3 += this.f2304J.m1713a(this.f2315b, this.f2331r.f1675a, this.f2331r.f1676b, this.f2332s, this.f2333t);
            }
            byteVector.putShort(this.f2315b.newUTF8("Code")).putInt(i3);
            byteVector.putShort(this.f2332s).putShort(this.f2333t);
            byteVector.putInt(this.f2331r.f1676b).putByteArray(this.f2331r.f1675a, 0, this.f2331r.f1676b);
            byteVector.putShort(this.f2295A);
            if (this.f2295A > 0) {
                for (Handler handler = this.f2296B; handler != null; handler = handler.f1698f) {
                    byteVector.putShort(handler.f1693a.f1710c).putShort(handler.f1694b.f1710c).putShort(handler.f1695c.f1710c).putShort(handler.f1697e);
                }
            }
            i2 = this.f2299E != null ? 1 : 0;
            if (this.f2301G != null) {
                i2++;
            }
            if (this.f2303I != null) {
                i2++;
            }
            if (this.f2335v != null) {
                i2++;
            }
            if (this.f2304J != null) {
                i2 += this.f2304J.m1712a();
            }
            byteVector.putShort(i2);
            if (this.f2299E != null) {
                byteVector.putShort(this.f2315b.newUTF8("LocalVariableTable"));
                byteVector.putInt(this.f2299E.f1676b + 2).putShort(this.f2298D);
                byteVector.putByteArray(this.f2299E.f1675a, 0, this.f2299E.f1676b);
            }
            if (this.f2301G != null) {
                byteVector.putShort(this.f2315b.newUTF8("LocalVariableTypeTable"));
                byteVector.putInt(this.f2301G.f1676b + 2).putShort(this.f2300F);
                byteVector.putByteArray(this.f2301G.f1675a, 0, this.f2301G.f1676b);
            }
            if (this.f2303I != null) {
                byteVector.putShort(this.f2315b.newUTF8("LineNumberTable"));
                byteVector.putInt(this.f2303I.f1676b + 2).putShort(this.f2302H);
                byteVector.putByteArray(this.f2303I.f1675a, 0, this.f2303I.f1676b);
            }
            if (this.f2335v != null) {
                if ((this.f2315b.f2260b & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) < 50) {
                    i = 0;
                }
                byteVector.putShort(this.f2315b.newUTF8(i != 0 ? "StackMapTable" : "StackMap"));
                byteVector.putInt(this.f2335v.f1676b + 2).putShort(this.f2334u);
                byteVector.putByteArray(this.f2335v.f1675a, 0, this.f2335v.f1676b);
            }
            if (this.f2304J != null) {
                this.f2304J.m1714a(this.f2315b, this.f2331r.f1675a, this.f2331r.f1676b, this.f2333t, this.f2332s, byteVector);
            }
        }
        if (this.f2323j > 0) {
            byteVector.putShort(this.f2315b.newUTF8("Exceptions")).putInt((this.f2323j * 2) + 2);
            byteVector.putShort(this.f2323j);
            for (i2 = 0; i2 < this.f2323j; i2++) {
                byteVector.putShort(this.f2324k[i2]);
            }
        }
        if ((this.f2316c & Flags.EXTEND) != 0 && ((this.f2315b.f2260b & InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) < 49 || (this.f2316c & ServiceConnection.DEFAULT_BUFFER_SIZE) != 0)) {
            byteVector.putShort(this.f2315b.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.f2316c & Opcodes.ACC_DEPRECATED) != 0) {
            byteVector.putShort(this.f2315b.newUTF8("Deprecated")).putInt(0);
        }
        if (this.f2320g != null) {
            byteVector.putShort(this.f2315b.newUTF8("Signature")).putInt(2).putShort(this.f2315b.newUTF8(this.f2320g));
        }
        if (this.f2325l != null) {
            byteVector.putShort(this.f2315b.newUTF8("AnnotationDefault"));
            byteVector.putInt(this.f2325l.f1676b);
            byteVector.putByteArray(this.f2325l.f1675a, 0, this.f2325l.f1676b);
        }
        if (this.f2326m != null) {
            byteVector.putShort(this.f2315b.newUTF8("RuntimeVisibleAnnotations"));
            this.f2326m.m2454a(byteVector);
        }
        if (this.f2327n != null) {
            byteVector.putShort(this.f2315b.newUTF8("RuntimeInvisibleAnnotations"));
            this.f2327n.m2454a(byteVector);
        }
        if (this.f2328o != null) {
            byteVector.putShort(this.f2315b.newUTF8("RuntimeVisibleParameterAnnotations"));
            AnnotationWriter.m2452a(this.f2328o, this.f2313S, byteVector);
        }
        if (this.f2329p != null) {
            byteVector.putShort(this.f2315b.newUTF8("RuntimeInvisibleParameterAnnotations"));
            AnnotationWriter.m2452a(this.f2329p, this.f2313S, byteVector);
        }
        if (this.f2330q != null) {
            this.f2330q.m1714a(this.f2315b, null, 0, -1, -1, byteVector);
        }
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.f2315b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.f2315b, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.f2248g = this.f2326m;
            this.f2326m = annotationWriter;
        } else {
            annotationWriter.f2248g = this.f2327n;
            this.f2327n = annotationWriter;
        }
        return annotationWriter;
    }

    public AnnotationVisitor visitAnnotationDefault() {
        this.f2325l = new ByteVector();
        return new AnnotationWriter(this.f2315b, false, this.f2325l, null, 0);
    }

    public void visitAttribute(Attribute attribute) {
        if (attribute.isCodeAttribute()) {
            attribute.f1673a = this.f2304J;
            this.f2304J = attribute;
            return;
        }
        attribute.f1673a = this.f2330q;
        this.f2330q = attribute;
    }

    public void visitCode() {
    }

    public void visitEnd() {
    }

    public void visitFieldInsn(int i, String str, String str2, String str3) {
        int i2 = 1;
        int i3 = -2;
        Item a = this.f2315b.m2469a(str, str2, str3);
        if (this.f2310P != null) {
            if (this.f2307M == 0) {
                this.f2310P.f1715h.m1737a(i, 0, this.f2315b, a);
            } else {
                char charAt = str3.charAt(0);
                switch (i) {
                    case Opcodes.GETSTATIC /*178*/:
                        i3 = this.f2311Q;
                        if (charAt == 'D' || charAt == 'J') {
                            i2 = 2;
                        }
                        i2 += i3;
                        break;
                    case Opcodes.PUTSTATIC /*179*/:
                        int i4 = this.f2311Q;
                        i2 = (charAt == 'D' || charAt == 'J') ? -2 : -1;
                        i2 += i4;
                        break;
                    case Opcodes.GETFIELD /*180*/:
                        i3 = this.f2311Q;
                        if (!(charAt == 'D' || charAt == 'J')) {
                            i2 = 0;
                        }
                        i2 += i3;
                        break;
                    default:
                        i2 = this.f2311Q;
                        if (charAt == 'D' || charAt == 'J') {
                            i3 = -3;
                        }
                        i2 += i3;
                        break;
                }
                if (i2 > this.f2312R) {
                    this.f2312R = i2;
                }
                this.f2311Q = i2;
            }
        }
        this.f2331r.m1717b(i, a.f1699a);
    }

    public void visitFrame(int i, int i2, Object[] objArr, int i3, Object[] objArr2) {
        int i4 = 0;
        if (this.f2307M != 0) {
            if (i == -1) {
                m2477a(this.f2331r.f1676b, i2, i3);
                for (int i5 = 0; i5 < i2; i5++) {
                    int[] iArr;
                    int i6;
                    if (objArr[i5] instanceof String) {
                        iArr = this.f2339z;
                        i6 = this.f2338y;
                        this.f2338y = i6 + 1;
                        iArr[i6] = this.f2315b.m2471c((String) objArr[i5]) | 24117248;
                    } else if (objArr[i5] instanceof Integer) {
                        iArr = this.f2339z;
                        i6 = this.f2338y;
                        this.f2338y = i6 + 1;
                        iArr[i6] = ((Integer) objArr[i5]).intValue();
                    } else {
                        iArr = this.f2339z;
                        i6 = this.f2338y;
                        this.f2338y = i6 + 1;
                        iArr[i6] = this.f2315b.m2461a(XmlPullParser.NO_NAMESPACE, ((Label) objArr[i5]).f1710c) | 25165824;
                    }
                }
                while (i4 < i3) {
                    int[] iArr2;
                    int i7;
                    if (objArr2[i4] instanceof String) {
                        iArr2 = this.f2339z;
                        i7 = this.f2338y;
                        this.f2338y = i7 + 1;
                        iArr2[i7] = this.f2315b.m2471c((String) objArr2[i4]) | 24117248;
                    } else if (objArr2[i4] instanceof Integer) {
                        iArr2 = this.f2339z;
                        i7 = this.f2338y;
                        this.f2338y = i7 + 1;
                        iArr2[i7] = ((Integer) objArr2[i4]).intValue();
                    } else {
                        iArr2 = this.f2339z;
                        i7 = this.f2338y;
                        this.f2338y = i7 + 1;
                        iArr2[i7] = this.f2315b.m2461a(XmlPullParser.NO_NAMESPACE, ((Label) objArr2[i4]).f1710c) | 25165824;
                    }
                    i4++;
                }
                m2484b();
                return;
            }
            int i8;
            if (this.f2335v == null) {
                this.f2335v = new ByteVector();
                i8 = this.f2331r.f1676b;
            } else {
                i8 = (this.f2331r.f1676b - this.f2336w) - 1;
                if (i8 < 0) {
                    if (i != 3) {
                        throw new IllegalStateException();
                    }
                    return;
                }
            }
            switch (i) {
                case KEYRecord.OWNER_USER /*0*/:
                    this.f2335v.putByte(KEYRecord.PROTOCOL_ANY).putShort(i8).putShort(i2);
                    for (i8 = 0; i8 < i2; i8++) {
                        m2479a(objArr[i8]);
                    }
                    this.f2335v.putShort(i3);
                    while (i4 < i3) {
                        m2479a(objArr2[i4]);
                        i4++;
                    }
                    break;
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    this.f2335v.putByte(i2 + Type.IXFR).putShort(i8);
                    for (i8 = 0; i8 < i2; i8++) {
                        m2479a(objArr[i8]);
                    }
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    this.f2335v.putByte(251 - i2).putShort(i8);
                    break;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    if (i8 >= 64) {
                        this.f2335v.putByte(Type.IXFR).putShort(i8);
                        break;
                    } else {
                        this.f2335v.putByte(i8);
                        break;
                    }
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    if (i8 < 64) {
                        this.f2335v.putByte(i8 + 64);
                    } else {
                        this.f2335v.putByte(247).putShort(i8);
                    }
                    m2479a(objArr2[0]);
                    break;
            }
            this.f2336w = this.f2331r.f1676b;
            this.f2334u++;
        }
    }

    public void visitIincInsn(int i, int i2) {
        if (this.f2310P != null && this.f2307M == 0) {
            this.f2310P.f1715h.m1737a((int) Service.CISCO_SYS, i, null, null);
        }
        if (this.f2307M != 2) {
            int i3 = i + 1;
            if (i3 > this.f2333t) {
                this.f2333t = i3;
            }
        }
        if (i > KEYRecord.PROTOCOL_ANY || i2 > Service.LOCUS_CON || i2 < -128) {
            this.f2331r.putByte(Wbxml.LITERAL_AC).m1717b(Service.CISCO_SYS, i).putShort(i2);
        } else {
            this.f2331r.putByte(Service.CISCO_SYS).m1716a(i, i2);
        }
    }

    public void visitInsn(int i) {
        this.f2331r.putByte(i);
        if (this.f2310P != null) {
            if (this.f2307M == 0) {
                this.f2310P.f1715h.m1737a(i, 0, null, null);
            } else {
                int i2 = this.f2311Q + Frame.f1684a[i];
                if (i2 > this.f2312R) {
                    this.f2312R = i2;
                }
                this.f2311Q = i2;
            }
            if ((i >= Opcodes.IRETURN && i <= Opcodes.RETURN) || i == Opcodes.ATHROW) {
                m2489e();
            }
        }
    }

    public void visitIntInsn(int i, int i2) {
        if (this.f2310P != null) {
            if (this.f2307M == 0) {
                this.f2310P.f1715h.m1737a(i, i2, null, null);
            } else if (i != Opcodes.NEWARRAY) {
                int i3 = this.f2311Q + 1;
                if (i3 > this.f2312R) {
                    this.f2312R = i3;
                }
                this.f2311Q = i3;
            }
        }
        if (i == 17) {
            this.f2331r.m1717b(i, i2);
        } else {
            this.f2331r.m1716a(i, i2);
        }
    }

    public void visitJumpInsn(int i, Label label) {
        Label label2 = null;
        if (this.f2310P != null) {
            if (this.f2307M == 0) {
                this.f2310P.f1715h.m1737a(i, 0, null, null);
                Label a = label.m1747a();
                a.f1708a |= 16;
                m2478a(0, label);
                if (i != Opcodes.GOTO) {
                    label2 = new Label();
                }
            } else if (i == Opcodes.JSR) {
                if ((label.f1708a & KEYRecord.OWNER_HOST) == 0) {
                    label.f1708a |= KEYRecord.OWNER_HOST;
                    this.f2306L++;
                }
                label2 = this.f2310P;
                label2.f1708a |= Flags.FLAG8;
                m2478a(this.f2311Q + 1, label);
                label2 = new Label();
            } else {
                this.f2311Q += Frame.f1684a[i];
                m2478a(this.f2311Q, label);
            }
        }
        if ((label.f1708a & 2) == 0 || label.f1710c - this.f2331r.f1676b >= -32768) {
            this.f2331r.putByte(i);
            label.m1749a(this, this.f2331r, this.f2331r.f1676b - 1, false);
        } else {
            if (i == Opcodes.GOTO) {
                this.f2331r.putByte(AsyncTaskManager.REQUEST_SUCCESS_CODE);
            } else if (i == Opcodes.JSR) {
                this.f2331r.putByte(201);
            } else {
                if (label2 != null) {
                    label2.f1708a |= 16;
                }
                this.f2331r.putByte(i <= Opcodes.IF_ACMPNE ? ((i + 1) ^ 1) - 1 : i ^ 1);
                this.f2331r.putShort(8);
                this.f2331r.putByte(AsyncTaskManager.REQUEST_SUCCESS_CODE);
            }
            label.m1749a(this, this.f2331r, this.f2331r.f1676b - 1, true);
        }
        if (this.f2310P != null) {
            if (label2 != null) {
                visitLabel(label2);
            }
            if (i == Opcodes.GOTO) {
                m2489e();
            }
        }
    }

    public void visitLabel(Label label) {
        this.f2305K |= label.m1752a(this, this.f2331r.f1676b, this.f2331r.f1675a);
        if ((label.f1708a & 1) == 0) {
            if (this.f2307M == 0) {
                Label label2;
                if (this.f2310P != null) {
                    if (label.f1710c == this.f2310P.f1710c) {
                        label2 = this.f2310P;
                        label2.f1708a |= label.f1708a & 16;
                        label.f1715h = this.f2310P.f1715h;
                        return;
                    }
                    m2478a(0, label);
                }
                this.f2310P = label;
                if (label.f1715h == null) {
                    label.f1715h = new Frame();
                    label.f1715h.f1685b = label;
                }
                if (this.f2309O != null) {
                    if (label.f1710c == this.f2309O.f1710c) {
                        label2 = this.f2309O;
                        label2.f1708a |= label.f1708a & 16;
                        label.f1715h = this.f2309O.f1715h;
                        this.f2310P = this.f2309O;
                        return;
                    }
                    this.f2309O.f1716i = label;
                }
                this.f2309O = label;
            } else if (this.f2307M == 1) {
                if (this.f2310P != null) {
                    this.f2310P.f1714g = this.f2312R;
                    m2478a(this.f2311Q, label);
                }
                this.f2310P = label;
                this.f2311Q = 0;
                this.f2312R = 0;
                if (this.f2309O != null) {
                    this.f2309O.f1716i = label;
                }
                this.f2309O = label;
            }
        }
    }

    public void visitLdcInsn(Object obj) {
        int i;
        Item a = this.f2315b.m2466a(obj);
        if (this.f2310P != null) {
            if (this.f2307M == 0) {
                this.f2310P.f1715h.m1737a(18, 0, this.f2315b, a);
            } else {
                i = (a.f1700b == 5 || a.f1700b == 6) ? this.f2311Q + 2 : this.f2311Q + 1;
                if (i > this.f2312R) {
                    this.f2312R = i;
                }
                this.f2311Q = i;
            }
        }
        i = a.f1699a;
        if (a.f1700b == 5 || a.f1700b == 6) {
            this.f2331r.m1717b(20, i);
        } else if (i >= KEYRecord.OWNER_ZONE) {
            this.f2331r.m1717b(19, i);
        } else {
            this.f2331r.m1716a(18, i);
        }
    }

    public void visitLineNumber(int i, Label label) {
        if (this.f2303I == null) {
            this.f2303I = new ByteVector();
        }
        this.f2302H++;
        this.f2303I.putShort(label.f1710c);
        this.f2303I.putShort(i);
    }

    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i) {
        int i2 = 2;
        if (str3 != null) {
            if (this.f2301G == null) {
                this.f2301G = new ByteVector();
            }
            this.f2300F++;
            this.f2301G.putShort(label.f1710c).putShort(label2.f1710c - label.f1710c).putShort(this.f2315b.newUTF8(str)).putShort(this.f2315b.newUTF8(str3)).putShort(i);
        }
        if (this.f2299E == null) {
            this.f2299E = new ByteVector();
        }
        this.f2298D++;
        this.f2299E.putShort(label.f1710c).putShort(label2.f1710c - label.f1710c).putShort(this.f2315b.newUTF8(str)).putShort(this.f2315b.newUTF8(str2)).putShort(i);
        if (this.f2307M != 2) {
            char charAt = str2.charAt(0);
            if (!(charAt == 'J' || charAt == 'D')) {
                i2 = 1;
            }
            i2 += i;
            if (i2 > this.f2333t) {
                this.f2333t = i2;
            }
        }
    }

    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        int i = 0;
        int i2 = this.f2331r.f1676b;
        this.f2331r.putByte(Opcodes.LOOKUPSWITCH);
        this.f2331r.putByteArray(null, 0, (4 - (this.f2331r.f1676b % 4)) % 4);
        label.m1749a(this, this.f2331r, i2, true);
        this.f2331r.putInt(labelArr.length);
        while (i < labelArr.length) {
            this.f2331r.putInt(iArr[i]);
            labelArr[i].m1749a(this, this.f2331r, i2, true);
            i++;
        }
        m2480a(label, labelArr);
    }

    public void visitMaxs(int i, int i2) {
        Label a;
        Label a2;
        Label label;
        Edge edge;
        int i3;
        Label label2;
        int length;
        int i4;
        if (this.f2307M == 0) {
            Handler handler = this.f2296B;
            while (handler != null) {
                a = handler.f1693a.m1747a();
                a2 = handler.f1695c.m1747a();
                Label a3 = handler.f1694b.m1747a();
                int c = 24117248 | this.f2315b.m2471c(handler.f1696d == null ? "java/lang/Throwable" : handler.f1696d);
                a2.f1708a |= 16;
                for (label = a; label != a3; label = label.f1716i) {
                    edge = new Edge();
                    edge.f1681a = c;
                    edge.f1682b = a2;
                    edge.f1683c = label.f1717j;
                    label.f1717j = edge;
                }
                handler = handler.f1698f;
            }
            Frame frame = this.f2308N.f1715h;
            frame.m1738a(this.f2315b, this.f2316c, Type.getArgumentTypes(this.f2319f), this.f2333t);
            m2485b(frame);
            a2 = this.f2308N;
            i3 = 0;
            while (a2 != null) {
                label2 = a2.f1718k;
                a2.f1718k = null;
                Frame frame2 = a2.f1715h;
                if ((a2.f1708a & 16) != 0) {
                    a2.f1708a |= 32;
                }
                a2.f1708a |= 64;
                length = frame2.f1687d.length + a2.f1714g;
                if (length <= i3) {
                    length = i3;
                }
                Edge edge2 = a2.f1717j;
                while (edge2 != null) {
                    a = edge2.f1682b.m1747a();
                    if (frame2.m1739a(this.f2315b, a.f1715h, edge2.f1681a) && a.f1718k == null) {
                        a.f1718k = label2;
                    } else {
                        a = label2;
                    }
                    edge2 = edge2.f1683c;
                    label2 = a;
                }
                a2 = label2;
                i3 = length;
            }
            length = i3;
            for (a2 = this.f2308N; a2 != null; a2 = a2.f1716i) {
                Frame frame3 = a2.f1715h;
                if ((a2.f1708a & 32) != 0) {
                    m2485b(frame3);
                }
                if ((a2.f1708a & 64) == 0) {
                    a = a2.f1716i;
                    int i5 = a2.f1710c;
                    i4 = (a == null ? this.f2331r.f1676b : a.f1710c) - 1;
                    if (i4 >= i5) {
                        length = Math.max(length, 1);
                        for (i3 = i5; i3 < i4; i3++) {
                            this.f2331r.f1675a[i3] = (byte) 0;
                        }
                        this.f2331r.f1675a[i4] = (byte) -65;
                        m2477a(i5, 0, 1);
                        int[] iArr = this.f2339z;
                        i5 = this.f2338y;
                        this.f2338y = i5 + 1;
                        iArr[i5] = this.f2315b.m2471c("java/lang/Throwable") | 24117248;
                        m2484b();
                    }
                }
            }
            this.f2332s = length;
        } else if (this.f2307M == 1) {
            for (Handler handler2 = this.f2296B; handler2 != null; handler2 = handler2.f1698f) {
                label2 = handler2.f1695c;
                a2 = handler2.f1694b;
                for (label = handler2.f1693a; label != a2; label = label.f1716i) {
                    Edge edge3 = new Edge();
                    edge3.f1681a = Integer.MAX_VALUE;
                    edge3.f1682b = label2;
                    if ((label.f1708a & Flags.FLAG8) == 0) {
                        edge3.f1683c = label.f1717j;
                        label.f1717j = edge3;
                    } else {
                        edge3.f1683c = label.f1717j.f1683c.f1683c;
                        label.f1717j.f1683c.f1683c = edge3;
                    }
                }
            }
            if (this.f2306L > 0) {
                this.f2308N.m1753b(null, 1, this.f2306L);
                length = 0;
                for (a = this.f2308N; a != null; a = a.f1716i) {
                    if ((a.f1708a & Flags.FLAG8) != 0) {
                        label2 = a.f1717j.f1683c.f1682b;
                        if ((label2.f1708a & Flags.FLAG5) == 0) {
                            length++;
                            label2.m1753b(null, ((((long) length) / 32) << 32) | (1 << (length % 32)), this.f2306L);
                        }
                    }
                }
                for (a = this.f2308N; a != null; a = a.f1716i) {
                    if ((a.f1708a & Flags.FLAG8) != 0) {
                        label = this.f2308N;
                        while (label != null) {
                            label.f1708a &= -2049;
                            label = label.f1716i;
                        }
                        a.f1717j.f1683c.f1682b.m1753b(a, 0, this.f2306L);
                    }
                }
            }
            label2 = this.f2308N;
            i3 = 0;
            while (label2 != null) {
                a2 = label2.f1718k;
                i4 = label2.f1713f;
                length = label2.f1714g + i4;
                if (length <= i3) {
                    length = i3;
                }
                edge = label2.f1717j;
                Edge edge4 = (label2.f1708a & Flags.FLAG8) != 0 ? edge.f1683c : edge;
                while (edge4 != null) {
                    label2 = edge4.f1682b;
                    if ((label2.f1708a & 8) == 0) {
                        label2.f1713f = edge4.f1681a == Integer.MAX_VALUE ? 1 : edge4.f1681a + i4;
                        label2.f1708a |= 8;
                        label2.f1718k = a2;
                        a = label2;
                    } else {
                        a = a2;
                    }
                    edge4 = edge4.f1683c;
                    a2 = a;
                }
                label2 = a2;
                i3 = length;
            }
            this.f2332s = i3;
        } else {
            this.f2332s = i;
            this.f2333t = i2;
        }
    }

    public void visitMethodInsn(int i, String str, String str2, String str3) {
        boolean z = i == Opcodes.INVOKEINTERFACE;
        Item a = i == Opcodes.INVOKEDYNAMIC ? this.f2315b.m2468a(str2, str3) : this.f2315b.m2470a(str, str2, str3, z);
        int i2 = a.f1701c;
        if (this.f2310P != null) {
            if (this.f2307M == 0) {
                this.f2310P.f1715h.m1737a(i, 0, this.f2315b, a);
            } else {
                int argumentsAndReturnSizes;
                if (i2 == 0) {
                    argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(str3);
                    a.f1701c = argumentsAndReturnSizes;
                } else {
                    argumentsAndReturnSizes = i2;
                }
                i2 = (i == Opcodes.INVOKESTATIC || i == Opcodes.INVOKEDYNAMIC) ? ((this.f2311Q - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3)) + 1 : (this.f2311Q - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3);
                if (i2 > this.f2312R) {
                    this.f2312R = i2;
                }
                this.f2311Q = i2;
                i2 = argumentsAndReturnSizes;
            }
        }
        if (z) {
            if (i2 == 0) {
                i2 = Type.getArgumentsAndReturnSizes(str3);
                a.f1701c = i2;
            }
            this.f2331r.m1717b(Opcodes.INVOKEINTERFACE, a.f1699a).m1716a(i2 >> 2, 0);
            return;
        }
        this.f2331r.m1717b(i, a.f1699a);
        if (i == Opcodes.INVOKEDYNAMIC) {
            this.f2331r.putShort(0);
        }
    }

    public void visitMultiANewArrayInsn(String str, int i) {
        Item a = this.f2315b.m2467a(str);
        if (this.f2310P != null) {
            if (this.f2307M == 0) {
                this.f2310P.f1715h.m1737a((int) Opcodes.MULTIANEWARRAY, i, this.f2315b, a);
            } else {
                this.f2311Q += 1 - i;
            }
        }
        this.f2331r.m1717b(Opcodes.MULTIANEWARRAY, a.f1699a).putByte(i);
    }

    public AnnotationVisitor visitParameterAnnotation(int i, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        if ("Ljava/lang/Synthetic;".equals(str)) {
            this.f2313S = Math.max(this.f2313S, i + 1);
            return new AnnotationWriter(this.f2315b, false, byteVector, null, 0);
        }
        byteVector.putShort(this.f2315b.newUTF8(str)).putShort(0);
        AnnotationVisitor annotationWriter = new AnnotationWriter(this.f2315b, true, byteVector, byteVector, 2);
        if (z) {
            if (this.f2328o == null) {
                this.f2328o = new AnnotationWriter[Type.getArgumentTypes(this.f2319f).length];
            }
            annotationWriter.f2248g = this.f2328o[i];
            this.f2328o[i] = annotationWriter;
            return annotationWriter;
        }
        if (this.f2329p == null) {
            this.f2329p = new AnnotationWriter[Type.getArgumentTypes(this.f2319f).length];
        }
        annotationWriter.f2248g = this.f2329p[i];
        this.f2329p[i] = annotationWriter;
        return annotationWriter;
    }

    public void visitTableSwitchInsn(int i, int i2, Label label, Label[] labelArr) {
        int i3 = 0;
        int i4 = this.f2331r.f1676b;
        this.f2331r.putByte(Opcodes.TABLESWITCH);
        this.f2331r.putByteArray(null, 0, (4 - (this.f2331r.f1676b % 4)) % 4);
        label.m1749a(this, this.f2331r, i4, true);
        this.f2331r.putInt(i).putInt(i2);
        while (i3 < labelArr.length) {
            labelArr[i3].m1749a(this, this.f2331r, i4, true);
            i3++;
        }
        m2480a(label, labelArr);
    }

    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        this.f2295A++;
        Handler handler = new Handler();
        handler.f1693a = label;
        handler.f1694b = label2;
        handler.f1695c = label3;
        handler.f1696d = str;
        handler.f1697e = str != null ? this.f2315b.newClass(str) : 0;
        if (this.f2297C == null) {
            this.f2296B = handler;
        } else {
            this.f2297C.f1698f = handler;
        }
        this.f2297C = handler;
    }

    public void visitTypeInsn(int i, String str) {
        Item a = this.f2315b.m2467a(str);
        if (this.f2310P != null) {
            if (this.f2307M == 0) {
                this.f2310P.f1715h.m1737a(i, this.f2331r.f1676b, this.f2315b, a);
            } else if (i == Opcodes.NEW) {
                int i2 = this.f2311Q + 1;
                if (i2 > this.f2312R) {
                    this.f2312R = i2;
                }
                this.f2311Q = i2;
            }
        }
        this.f2331r.m1717b(i, a.f1699a);
    }

    public void visitVarInsn(int i, int i2) {
        int i3;
        if (this.f2310P != null) {
            if (this.f2307M == 0) {
                this.f2310P.f1715h.m1737a(i, i2, null, null);
            } else if (i == Opcodes.RET) {
                Label label = this.f2310P;
                label.f1708a |= KEYRecord.OWNER_ZONE;
                this.f2310P.f1713f = this.f2311Q;
                m2489e();
            } else {
                i3 = this.f2311Q + Frame.f1684a[i];
                if (i3 > this.f2312R) {
                    this.f2312R = i3;
                }
                this.f2311Q = i3;
            }
        }
        if (this.f2307M != 2) {
            i3 = (i == 22 || i == 24 || i == 55 || i == 57) ? i2 + 2 : i2 + 1;
            if (i3 > this.f2333t) {
                this.f2333t = i3;
            }
        }
        if (i2 < 4 && i != Opcodes.RET) {
            this.f2331r.putByte(i < 54 ? (((i - 21) << 2) + 26) + i2 : (((i - 54) << 2) + 59) + i2);
        } else if (i2 >= KEYRecord.OWNER_ZONE) {
            this.f2331r.putByte(Wbxml.LITERAL_AC).m1717b(i, i2);
        } else {
            this.f2331r.m1716a(i, i2);
        }
        if (i >= 54 && this.f2307M == 0 && this.f2295A > 0) {
            visitLabel(new Label());
        }
    }
}
