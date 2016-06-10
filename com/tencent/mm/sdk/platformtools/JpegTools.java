package com.tencent.mm.sdk.platformtools;

import com.ifoer.mine.Contact;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class JpegTools {
    public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
    public static final int ORIENTATION_FLIP_VERTICAL = 4;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_ROTATE_180 = 3;
    public static final int ORIENTATION_ROTATE_270 = 8;
    public static final int ORIENTATION_ROTATE_90 = 6;
    public static final int ORIENTATION_TRANSPOSE = 5;
    public static final int ORIENTATION_TRANSVERSE = 7;
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final String TAG = "MicroMsg.JpegTools";
    private MBuf f1634B;
    private int f1635C;
    private boolean f1636D;

    public JpegTools(byte[] bArr) {
        this.f1634B = null;
        this.f1635C = -1;
        this.f1636D = true;
        this.f1634B = new MBuf();
        this.f1634B.setBuffer(bArr);
    }

    private void m1647a(int i) {
        this.f1634B.getBuffer().position(this.f1634B.getBuffer().position() + i);
    }

    public static String byte2HexString(byte b) {
        String str = XmlPullParser.NO_NAMESPACE;
        String toHexString = Integer.toHexString(b & KEYRecord.PROTOCOL_ANY);
        if (toHexString.length() == ORIENTATION_NORMAL) {
            toHexString = new StringBuilder(Contact.RELATION_ASK).append(toHexString).toString();
        }
        return str + toHexString.toUpperCase();
    }

    public int getOreiValue() {
        if (this.f1635C == -1) {
            return -1;
        }
        switch (this.f1635C) {
            case ORIENTATION_NORMAL /*1*/:
                return ORIENTATION_UNDEFINED;
            case ORIENTATION_ROTATE_180 /*3*/:
                return Opcodes.GETFIELD;
            case ORIENTATION_ROTATE_90 /*6*/:
                return 90;
            case ORIENTATION_ROTATE_270 /*8*/:
                return 270;
            default:
                return -1;
        }
    }

    public int parserJpeg() {
        void v = null;
        try {
            Object obj = (byte2HexString(this.f1634B.getBuffer().get()).equals("FF") && byte2HexString(this.f1634B.getBuffer().get()).equals("D8")) ? ORIENTATION_NORMAL : ORIENTATION_UNDEFINED;
            if (obj == null) {
                Log.m1665w(TAG, "this is not jpeg or no exif data!!!");
                return -1;
            }
            byte b;
            byte b2;
            byte b3;
            int i = ORIENTATION_UNDEFINED;
            do {
                b = this.f1634B.getBuffer().get();
                b2 = this.f1634B.getBuffer().get();
                this.f1634B.getBuffer().get();
                b3 = this.f1634B.getBuffer().get();
                if (byte2HexString(b).equals("FF")) {
                    if (!byte2HexString(b).equals("FF") || !byte2HexString(b2).equals("E1")) {
                        if (byte2HexString(b).equals("FF") && byte2HexString(b2).equals("D9")) {
                            i = -1;
                            break;
                        }
                        this.f1634B.getBuffer().position((this.f1634B.getOffset() + b3) - 2);
                        i += ORIENTATION_NORMAL;
                    } else {
                        i = (b3 & KEYRecord.PROTOCOL_ANY) - 2;
                        break;
                    }
                }
                i = -1;
                break;
            } while (i <= 100);
            Log.m1657e(TAG, "error while!");
            i = -1;
            if (i < 0) {
                Log.m1665w(TAG, "datalen is error ");
                return -1;
            }
            if ((new StringBuilder().append((char) this.f1634B.getBuffer().get()).append((char) this.f1634B.getBuffer().get()).append((char) this.f1634B.getBuffer().get()).append((char) this.f1634B.getBuffer().get()).toString().equals("Exif") ? ORIENTATION_NORMAL : ORIENTATION_UNDEFINED) == null) {
                Log.m1665w(TAG, "checkExifTag is error");
                return -1;
            }
            m1647a(ORIENTATION_FLIP_HORIZONTAL);
            byte b4 = this.f1634B.getBuffer().get();
            b = this.f1634B.getBuffer().get();
            String str = (((char) b4) == 'M' && ((char) b) == 'M') ? "MM" : (((char) b4) == 'I' && ((char) b) == 'I') ? "II" : XmlPullParser.NO_NAMESPACE;
            if (str.equals("MM") || str.equals("II")) {
                this.f1636D = str.equals("MM");
                boolean z = this.f1636D;
                b = this.f1634B.getBuffer().get();
                b2 = this.f1634B.getBuffer().get();
                if (z && byte2HexString(b).equals("00") && byte2HexString(b2).equals("2A")) {
                    obj = ORIENTATION_NORMAL;
                } else if (byte2HexString(b).equals("2A") && byte2HexString(b2).equals("00")) {
                    obj = ORIENTATION_NORMAL;
                } else {
                    Log.m1665w(TAG, "checkTiffTag: " + byte2HexString(b) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + byte2HexString(b2));
                    obj = ORIENTATION_UNDEFINED;
                }
                if (obj == null) {
                    Log.m1665w(TAG, "checkTiffTag  is error ");
                    return -1;
                }
                m1647a(ORIENTATION_FLIP_VERTICAL);
                b4 = this.f1634B.getBuffer().get();
                b = this.f1634B.getBuffer().get();
                i = b4 & KEYRecord.PROTOCOL_ANY;
                if (this.f1636D) {
                    i = b & KEYRecord.PROTOCOL_ANY;
                }
                int i2 = ORIENTATION_UNDEFINED;
                while (i2 < i && i2 < KEYRecord.PROTOCOL_ANY) {
                    b2 = this.f1634B.getBuffer().get();
                    b3 = this.f1634B.getBuffer().get();
                    if (this.f1636D && byte2HexString(b2).equals("01") && byte2HexString(b3).equals("12")) {
                        v = ORIENTATION_NORMAL;
                    } else if (!this.f1636D && byte2HexString(b2).equals("12") && byte2HexString(b3).equals("01")) {
                        v = ORIENTATION_NORMAL;
                    }
                    m1647a(ORIENTATION_FLIP_HORIZONTAL);
                    m1647a(ORIENTATION_FLIP_VERTICAL);
                    if (v != null) {
                        z = this.f1636D;
                        byte b5 = this.f1634B.getBuffer().get();
                        byte b6 = this.f1634B.getBuffer().get();
                        m1647a(ORIENTATION_FLIP_HORIZONTAL);
                        this.f1635C = z ? b6 & KEYRecord.PROTOCOL_ANY : b5 & KEYRecord.PROTOCOL_ANY;
                    } else {
                        m1647a(ORIENTATION_FLIP_VERTICAL);
                        i2 += ORIENTATION_NORMAL;
                    }
                }
                Log.m1655d(TAG, "orei " + this.f1635C);
                return getOreiValue();
            }
            Log.m1665w(TAG, "byteOrder  is error " + str);
            return -1;
        } catch (Exception e) {
            Log.m1657e(TAG, "parser jpeg error");
            return -1;
        }
    }
}
