package com.paypal.android.p022a;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPOutputStream;
import org.codehaus.jackson.smile.SmileConstants;
import org.xbill.DNS.Flags;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.paypal.android.a.a */
public final class C0832a {
    private static final byte[] f1558a;
    private static final byte[] f1559b;
    private static final byte[] f1560c;
    private static final byte[] f1561d;
    private static final byte[] f1562e;
    private static final byte[] f1563f;

    /* renamed from: com.paypal.android.a.a.a */
    public static class C0820a extends FilterOutputStream {
        private boolean f1504a;
        private int f1505b;
        private byte[] f1506c;
        private int f1507d;
        private int f1508e;
        private boolean f1509f;
        private byte[] f1510g;
        private boolean f1511h;
        private int f1512i;
        private byte[] f1513j;

        public C0820a(OutputStream outputStream, int i) {
            boolean z = true;
            super(outputStream);
            this.f1509f = (i & 8) != 8;
            if ((i & 1) != 1) {
                z = false;
            }
            this.f1504a = z;
            this.f1507d = this.f1504a ? 3 : 4;
            this.f1506c = new byte[this.f1507d];
            this.f1505b = 0;
            this.f1508e = 0;
            this.f1511h = false;
            this.f1510g = new byte[4];
            this.f1512i = i;
            this.f1513j = C0832a.m1552b(i);
        }

        public final void close() throws IOException {
            if (this.f1505b > 0) {
                if (this.f1504a) {
                    this.out.write(C0832a.m1549a(this.f1506c, 0, this.f1505b, this.f1510g, 0, this.f1512i));
                    this.f1505b = 0;
                } else {
                    throw new IOException("Base64 input not properly padded.");
                }
            }
            super.close();
            this.f1506c = null;
            this.out = null;
        }

        public final void write(int i) throws IOException {
            byte[] bArr;
            int i2;
            if (this.f1504a) {
                bArr = this.f1506c;
                i2 = this.f1505b;
                this.f1505b = i2 + 1;
                bArr[i2] = (byte) i;
                if (this.f1505b >= this.f1507d) {
                    this.out.write(C0832a.m1549a(this.f1506c, 0, this.f1507d, this.f1510g, 0, this.f1512i));
                    this.f1508e += 4;
                    if (this.f1509f && this.f1508e >= 76) {
                        this.out.write(10);
                        this.f1508e = 0;
                    }
                    this.f1505b = 0;
                }
            } else if (this.f1513j[i & Service.LOCUS_CON] > (byte) -5) {
                bArr = this.f1506c;
                i2 = this.f1505b;
                this.f1505b = i2 + 1;
                bArr[i2] = (byte) i;
                if (this.f1505b >= this.f1507d) {
                    this.out.write(this.f1510g, 0, C0832a.m1551b(this.f1506c, 0, this.f1510g, 0, this.f1512i));
                    this.f1505b = 0;
                }
            } else if (this.f1513j[i & Service.LOCUS_CON] != (byte) -5) {
                throw new IOException("Invalid character in Base64 data.");
            }
        }

        public final void write(byte[] bArr, int i, int i2) throws IOException {
            for (int i3 = 0; i3 < i2; i3++) {
                write(bArr[i + i3]);
            }
        }
    }

    static {
        f1558a = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
        f1559b = new byte[]{(byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -5, (byte) -5, (byte) -9, (byte) -9, (byte) -5, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -5, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 62, (byte) -9, (byte) -9, (byte) -9, (byte) 63, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, SmileConstants.HEADER_BYTE_1, (byte) 59, (byte) 60, (byte) 61, (byte) -9, (byte) -9, (byte) -9, (byte) -1, (byte) -9, (byte) -9, (byte) -9, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, Flags.CD, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, (byte) 24, (byte) 25, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 26, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_FALSE, SmileConstants.TOKEN_LITERAL_TRUE, (byte) 36, (byte) 37, (byte) 38, (byte) 39, (byte) 40, SmileConstants.HEADER_BYTE_2, (byte) 42, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) -9, (byte) -9, (byte) -9, (byte) -9};
        f1560c = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
        f1561d = new byte[]{(byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -5, (byte) -5, (byte) -9, (byte) -9, (byte) -5, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -5, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 62, (byte) -9, (byte) -9, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, SmileConstants.HEADER_BYTE_1, (byte) 59, (byte) 60, (byte) 61, (byte) -9, (byte) -9, (byte) -9, (byte) -1, (byte) -9, (byte) -9, (byte) -9, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, Flags.CD, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, (byte) 24, (byte) 25, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 63, (byte) -9, (byte) 26, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_FALSE, SmileConstants.TOKEN_LITERAL_TRUE, (byte) 36, (byte) 37, (byte) 38, (byte) 39, (byte) 40, SmileConstants.HEADER_BYTE_2, (byte) 42, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) -9, (byte) -9, (byte) -9, (byte) -9};
        f1562e = new byte[]{(byte) 45, (byte) 48, (byte) 49, (byte) 50, (byte) 51, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 95, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122};
        f1563f = new byte[]{(byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -5, (byte) -5, (byte) -9, (byte) -9, (byte) -5, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -5, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 0, (byte) -9, (byte) -9, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) -9, (byte) -9, (byte) -9, (byte) -1, (byte) -9, (byte) -9, (byte) -9, Flags.CD, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, (byte) 24, (byte) 25, (byte) 26, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_FALSE, SmileConstants.TOKEN_LITERAL_TRUE, (byte) 36, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 37, (byte) -9, (byte) 38, (byte) 39, (byte) 40, SmileConstants.HEADER_BYTE_2, (byte) 42, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, (byte) 50, (byte) 51, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, SmileConstants.HEADER_BYTE_1, (byte) 59, (byte) 60, (byte) 61, (byte) 62, (byte) 63, (byte) -9, (byte) -9, (byte) -9, (byte) -9};
    }

    private C0832a() {
    }

    public static String m1546a(byte[] bArr, int i) {
        return C0832a.m1547a(bArr, 0, bArr.length, 8);
    }

    private static String m1547a(byte[] bArr, int i, int i2, int i3) {
        ByteArrayOutputStream byteArrayOutputStream;
        C0820a c0820a;
        GZIPOutputStream gZIPOutputStream;
        IOException e;
        Throwable th;
        int i4 = i3 & 8;
        if ((i3 & 2) == 2) {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    c0820a = new C0820a(byteArrayOutputStream, i3 | 1);
                    try {
                        gZIPOutputStream = new GZIPOutputStream(c0820a);
                        try {
                            gZIPOutputStream.write(bArr, 0, i2);
                            gZIPOutputStream.close();
                            try {
                                gZIPOutputStream.close();
                            } catch (Exception e2) {
                            }
                            try {
                                c0820a.close();
                            } catch (Exception e3) {
                            }
                            try {
                                byteArrayOutputStream.close();
                            } catch (Exception e4) {
                            }
                            try {
                                return new String(byteArrayOutputStream.toByteArray(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
                            } catch (UnsupportedEncodingException e5) {
                                return new String(byteArrayOutputStream.toByteArray());
                            }
                        } catch (IOException e6) {
                            e = e6;
                            try {
                                e.printStackTrace();
                                try {
                                    gZIPOutputStream.close();
                                } catch (Exception e7) {
                                }
                                try {
                                    c0820a.close();
                                } catch (Exception e8) {
                                }
                                try {
                                    byteArrayOutputStream.close();
                                    return null;
                                } catch (Exception e9) {
                                    return null;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                try {
                                    gZIPOutputStream.close();
                                } catch (Exception e10) {
                                }
                                try {
                                    c0820a.close();
                                } catch (Exception e11) {
                                }
                                try {
                                    byteArrayOutputStream.close();
                                } catch (Exception e12) {
                                }
                                throw th;
                            }
                        }
                    } catch (IOException e13) {
                        e = e13;
                        gZIPOutputStream = null;
                        e.printStackTrace();
                        gZIPOutputStream.close();
                        c0820a.close();
                        byteArrayOutputStream.close();
                        return null;
                    } catch (Throwable th3) {
                        gZIPOutputStream = null;
                        th = th3;
                        gZIPOutputStream.close();
                        c0820a.close();
                        byteArrayOutputStream.close();
                        throw th;
                    }
                } catch (IOException e14) {
                    e = e14;
                    c0820a = null;
                    gZIPOutputStream = null;
                    e.printStackTrace();
                    gZIPOutputStream.close();
                    c0820a.close();
                    byteArrayOutputStream.close();
                    return null;
                } catch (Throwable th32) {
                    c0820a = null;
                    gZIPOutputStream = null;
                    th = th32;
                    gZIPOutputStream.close();
                    c0820a.close();
                    byteArrayOutputStream.close();
                    throw th;
                }
            } catch (IOException e15) {
                e = e15;
                c0820a = null;
                gZIPOutputStream = null;
                byteArrayOutputStream = null;
                e.printStackTrace();
                gZIPOutputStream.close();
                c0820a.close();
                byteArrayOutputStream.close();
                return null;
            } catch (Throwable th322) {
                c0820a = null;
                gZIPOutputStream = null;
                byteArrayOutputStream = null;
                th = th322;
                gZIPOutputStream.close();
                c0820a.close();
                byteArrayOutputStream.close();
                throw th;
            }
        }
        int i5 = i4 == 0 ? 1 : 0;
        i4 = (i2 << 2) / 3;
        byte[] bArr2 = new byte[((i5 != 0 ? i4 / 76 : 0) + (i4 + (i2 % 3 > 0 ? 4 : 0)))];
        int i6 = i2 - 2;
        int i7 = 0;
        i4 = 0;
        int i8 = 0;
        while (i4 < i6) {
            C0832a.m1549a(bArr, i4, 3, bArr2, i8, i3);
            int i9 = i7 + 4;
            if (i5 != 0 && i9 == 76) {
                bArr2[i8 + 4] = (byte) 10;
                i8++;
                i9 = 0;
            }
            i4 += 3;
            i8 += 4;
            i7 = i9;
        }
        if (i4 < i2) {
            C0832a.m1549a(bArr, i4, i2 - i4, bArr2, i8, i3);
            i8 += 4;
        }
        try {
            return new String(bArr2, 0, i8, AsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e16) {
            return new String(bArr2, 0, i8);
        }
    }

    private static byte[] m1549a(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4) {
        int i5 = 0;
        byte[] bArr3 = (i4 & 16) == 16 ? f1560c : (i4 & 32) == 32 ? f1562e : f1558a;
        int i6 = (i2 > 1 ? (bArr[i + 1] << 24) >>> 16 : 0) | (i2 > 0 ? (bArr[i] << 24) >>> 8 : 0);
        if (i2 > 2) {
            i5 = (bArr[i + 2] << 24) >>> 24;
        }
        i5 |= i6;
        switch (i2) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                bArr2[i3] = bArr3[i5 >>> 18];
                bArr2[i3 + 1] = bArr3[(i5 >>> 12) & 63];
                bArr2[i3 + 2] = (byte) 61;
                bArr2[i3 + 3] = (byte) 61;
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                bArr2[i3] = bArr3[i5 >>> 18];
                bArr2[i3 + 1] = bArr3[(i5 >>> 12) & 63];
                bArr2[i3 + 2] = bArr3[(i5 >>> 6) & 63];
                bArr2[i3 + 3] = (byte) 61;
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                bArr2[i3] = bArr3[i5 >>> 18];
                bArr2[i3 + 1] = bArr3[(i5 >>> 12) & 63];
                bArr2[i3 + 2] = bArr3[(i5 >>> 6) & 63];
                bArr2[i3 + 3] = bArr3[i5 & 63];
                break;
        }
        return bArr2;
    }

    private static int m1551b(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        byte[] b = C0832a.m1552b(i3);
        if (bArr[i + 2] == (byte) 61) {
            bArr2[i2] = (byte) ((((b[bArr[i + 1]] & KEYRecord.PROTOCOL_ANY) << 12) | ((b[bArr[i]] & KEYRecord.PROTOCOL_ANY) << 18)) >>> 16);
            return 1;
        } else if (bArr[i + 3] == (byte) 61) {
            int i4 = ((b[bArr[i + 2]] & KEYRecord.PROTOCOL_ANY) << 6) | (((b[bArr[i]] & KEYRecord.PROTOCOL_ANY) << 18) | ((b[bArr[i + 1]] & KEYRecord.PROTOCOL_ANY) << 12));
            bArr2[i2] = (byte) (i4 >>> 16);
            bArr2[i2 + 1] = (byte) (i4 >>> 8);
            return 2;
        } else {
            try {
                int i5 = ((((b[bArr[i]] & KEYRecord.PROTOCOL_ANY) << 18) | ((b[bArr[i + 1]] & KEYRecord.PROTOCOL_ANY) << 12)) | ((b[bArr[i + 2]] & KEYRecord.PROTOCOL_ANY) << 6)) | (b[bArr[i + 3]] & KEYRecord.PROTOCOL_ANY);
                bArr2[i2] = (byte) (i5 >> 16);
                bArr2[i2 + 1] = (byte) (i5 >> 8);
                bArr2[i2 + 2] = (byte) i5;
                return 3;
            } catch (Exception e) {
                System.out.println(XmlPullParser.NO_NAMESPACE + bArr[i] + ": " + b[bArr[i]]);
                System.out.println(XmlPullParser.NO_NAMESPACE + bArr[i + 1] + ": " + b[bArr[i + 1]]);
                System.out.println(XmlPullParser.NO_NAMESPACE + bArr[i + 2] + ": " + b[bArr[i + 2]]);
                System.out.println(XmlPullParser.NO_NAMESPACE + bArr[i + 3] + ": " + b[bArr[i + 3]]);
                return -1;
            }
        }
    }

    private static final byte[] m1552b(int i) {
        return (i & 16) == 16 ? f1561d : (i & 32) == 32 ? f1563f : f1559b;
    }
}
