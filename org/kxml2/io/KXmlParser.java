package org.kxml2.io;

import com.amap.mapapi.location.LocationManagerProxy;
import com.cnmobi.im.util.XmppConnection;
import com.cnmobi.im.view.RecordButton;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;
import org.apache.harmony.javax.security.auth.callback.ConfirmationCallback;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.ksoap2.SoapEnvelope;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class KXmlParser implements XmlPullParser {
    private static final String ILLEGAL_TYPE = "Wrong event type";
    private static final int LEGACY = 999;
    private static final String UNEXPECTED_EOF = "Unexpected EOF";
    private static final int XML_DECL = 998;
    private int attributeCount;
    private String[] attributes;
    private int column;
    private boolean degenerated;
    private int depth;
    private String[] elementStack;
    private String encoding;
    private Hashtable entityMap;
    private String error;
    private boolean isWhitespace;
    private int line;
    private Object location;
    private String name;
    private String namespace;
    private int[] nspCounts;
    private String[] nspStack;
    private int[] peek;
    private int peekCount;
    private String prefix;
    private boolean processNsp;
    private Reader reader;
    private boolean relaxed;
    private char[] srcBuf;
    private int srcCount;
    private int srcPos;
    private int stackMismatch;
    private Boolean standalone;
    private boolean token;
    private char[] txtBuf;
    private int txtPos;
    private int type;
    private boolean unresolved;
    private String version;
    private boolean wasCR;

    public KXmlParser() {
        int i = Flags.FLAG8;
        this.elementStack = new String[16];
        this.nspStack = new String[8];
        this.nspCounts = new int[4];
        this.txtBuf = new char[Flags.FLAG8];
        this.attributes = new String[16];
        this.stackMismatch = 0;
        this.peek = new int[2];
        if (Runtime.getRuntime().freeMemory() >= 1048576) {
            i = Flags.FLAG2;
        }
        this.srcBuf = new char[i];
    }

    private final boolean adjustNsp() throws XmlPullParserException {
        int i = 0;
        boolean z = false;
        while (i < (this.attributeCount << 2)) {
            String substring;
            String str = this.attributes[i + 2];
            int indexOf = str.indexOf(58);
            if (indexOf != -1) {
                substring = str.substring(0, indexOf);
                str = str.substring(indexOf + 1);
            } else if (str.equals("xmlns")) {
                String str2 = str;
                str = null;
                substring = str2;
            } else {
                i += 4;
            }
            if (substring.equals("xmlns")) {
                int[] iArr = this.nspCounts;
                indexOf = this.depth;
                int i2 = iArr[indexOf];
                iArr[indexOf] = i2 + 1;
                int i3 = i2 << 1;
                this.nspStack = ensureCapacity(this.nspStack, i3 + 2);
                this.nspStack[i3] = str;
                this.nspStack[i3 + 1] = this.attributes[i + 3];
                if (str != null && this.attributes[i + 3].equals(XmlPullParser.NO_NAMESPACE)) {
                    error("illegal empty namespace");
                }
                Object obj = this.attributes;
                int i4 = i + 4;
                Object obj2 = this.attributes;
                i2 = this.attributeCount - 1;
                this.attributeCount = i2;
                System.arraycopy(obj, i4, obj2, i, (i2 << 2) - i);
                i -= 4;
            } else {
                z = true;
            }
            i += 4;
        }
        if (z) {
            i = (this.attributeCount << 2) - 4;
            while (i >= 0) {
                substring = this.attributes[i + 2];
                i4 = substring.indexOf(58);
                if (i4 != 0 || this.relaxed) {
                    if (i4 != -1) {
                        String substring2 = substring.substring(0, i4);
                        substring = substring.substring(i4 + 1);
                        str = getNamespace(substring2);
                        if (str != null || this.relaxed) {
                            this.attributes[i] = str;
                            this.attributes[i + 1] = substring2;
                            this.attributes[i + 2] = substring;
                        } else {
                            throw new RuntimeException(new StringBuffer().append("Undefined Prefix: ").append(substring2).append(" in ").append(this).toString());
                        }
                    }
                    i -= 4;
                } else {
                    throw new RuntimeException(new StringBuffer().append("illegal attribute name: ").append(substring).append(" at ").append(this).toString());
                }
            }
        }
        i = this.name.indexOf(58);
        if (i == 0) {
            error(new StringBuffer().append("illegal tag name: ").append(this.name).toString());
        }
        if (i != -1) {
            this.prefix = this.name.substring(0, i);
            this.name = this.name.substring(i + 1);
        }
        this.namespace = getNamespace(this.prefix);
        if (this.namespace == null) {
            if (this.prefix != null) {
                error(new StringBuffer().append("undefined prefix: ").append(this.prefix).toString());
            }
            this.namespace = XmlPullParser.NO_NAMESPACE;
        }
        return z;
    }

    private final String[] ensureCapacity(String[] strArr, int i) {
        if (strArr.length >= i) {
            return strArr;
        }
        Object obj = new String[(i + 16)];
        System.arraycopy(strArr, 0, obj, 0, strArr.length);
        return obj;
    }

    private final void error(String str) throws XmlPullParserException {
        if (!this.relaxed) {
            exception(str);
        } else if (this.error == null) {
            this.error = new StringBuffer().append("ERR: ").append(str).toString();
        }
    }

    private final void exception(String str) throws XmlPullParserException {
        if (str.length() >= 100) {
            str = new StringBuffer().append(str.substring(0, 100)).append(SpecilApiUtil.LINE_SEP).toString();
        }
        throw new XmlPullParserException(str, this, null);
    }

    private final String get(int i) {
        return new String(this.txtBuf, i, this.txtPos - i);
    }

    private final boolean isProp(String str, boolean z, String str2) {
        return !str.startsWith("http://xmlpull.org/v1/doc/") ? false : z ? str.substring(42).equals(str2) : str.substring(40).equals(str2);
    }

    private final void nextImpl() throws IOException, XmlPullParserException {
        boolean z = false;
        if (this.reader == null) {
            exception("No Input specified");
        }
        if (this.type == 3) {
            this.depth--;
        }
        do {
            this.attributeCount = -1;
            if (this.degenerated) {
                this.degenerated = false;
                this.type = 3;
                return;
            } else if (this.error != null) {
                while (r0 < this.error.length()) {
                    push(this.error.charAt(r0));
                    r0++;
                }
                this.error = null;
                this.type = 9;
                return;
            } else if (!this.relaxed || (this.stackMismatch <= 0 && (peek(0) != -1 || this.depth <= 0))) {
                this.prefix = null;
                this.name = null;
                this.namespace = null;
                this.type = peekType();
                switch (this.type) {
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        return;
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        parseStartTag(false);
                        return;
                    case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                        parseEndTag();
                        return;
                    case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                        if (!this.token) {
                            z = true;
                        }
                        pushText(60, z);
                        if (this.depth == 0 && this.isWhitespace) {
                            this.type = 7;
                            return;
                        }
                        return;
                    case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                        pushEntity();
                        return;
                    default:
                        this.type = parseLegacy(this.token);
                        break;
                }
            } else {
                r0 = (this.depth - 1) << 2;
                this.type = 3;
                this.namespace = this.elementStack[r0];
                this.prefix = this.elementStack[r0 + 1];
                this.name = this.elementStack[r0 + 2];
                if (this.stackMismatch != 1) {
                    this.error = new StringBuffer().append("missing end tag /").append(this.name).append(" inserted").toString();
                }
                if (this.stackMismatch > 0) {
                    this.stackMismatch--;
                    return;
                }
                return;
            }
        } while (this.type == XML_DECL);
    }

    private final void parseDoctype(boolean z) throws IOException, XmlPullParserException {
        Object obj = null;
        int i = 1;
        while (true) {
            int read = read();
            switch (read) {
                case ConfirmationCallback.UNSPECIFIED_OPTION /*-1*/:
                    error(UNEXPECTED_EOF);
                    return;
                case Service.RLP /*39*/:
                    if (obj != null) {
                        obj = null;
                        break;
                    } else {
                        int i2 = 1;
                        break;
                    }
                case RecordButton.MAX_TIME /*60*/:
                    if (obj == null) {
                        i++;
                        break;
                    }
                    break;
                case Protocol.CFTP /*62*/:
                    if (obj == null) {
                        i--;
                        if (i == 0) {
                            return;
                        }
                    }
                    break;
            }
            if (z) {
                push(read);
            }
        }
    }

    private final void parseEndTag() throws IOException, XmlPullParserException {
        read();
        read();
        this.name = readName();
        skip();
        read('>');
        int i = (this.depth - 1) << 2;
        if (this.depth == 0) {
            error("element stack empty");
            this.type = 9;
            return;
        }
        if (!this.name.equals(this.elementStack[i + 3])) {
            error(new StringBuffer().append("expected: /").append(this.elementStack[i + 3]).append(" read: ").append(this.name).toString());
            int i2 = i;
            while (i2 >= 0 && !this.name.toLowerCase().equals(this.elementStack[i2 + 3].toLowerCase())) {
                this.stackMismatch++;
                i2 -= 4;
            }
            if (i2 < 0) {
                this.stackMismatch = 0;
                this.type = 9;
                return;
            }
        }
        this.namespace = this.elementStack[i];
        this.prefix = this.elementStack[i + 1];
        this.name = this.elementStack[i + 2];
    }

    private final int parseLegacy(boolean z) throws IOException, XmlPullParserException {
        String str;
        boolean z2;
        String str2 = XmlPullParser.NO_NAMESPACE;
        read();
        int read = read();
        if (read == 63) {
            if ((peek(0) == SoapEnvelope.VER12 || peek(0) == 88) && (peek(1) == Service.POP_2 || peek(1) == 77)) {
                if (z) {
                    push(peek(0));
                    push(peek(1));
                }
                read();
                read();
                if ((peek(0) == Opcodes.IDIV || peek(0) == 76) && peek(1) <= 32) {
                    if (this.line != 1 || this.column > 4) {
                        error("PI must not start with xml");
                    }
                    parseStartTag(true);
                    if (this.attributeCount < 1 || !AlixDefine.VERSION.equals(this.attributes[2])) {
                        error("version expected");
                    }
                    this.version = this.attributes[3];
                    if (1 >= this.attributeCount || !"encoding".equals(this.attributes[6])) {
                        read = 1;
                    } else {
                        this.encoding = this.attributes[7];
                        read = 2;
                    }
                    if (read < this.attributeCount && "standalone".equals(this.attributes[(read * 4) + 2])) {
                        String str3 = this.attributes[(read * 4) + 3];
                        if ("yes".equals(str3)) {
                            this.standalone = new Boolean(true);
                        } else if ("no".equals(str3)) {
                            this.standalone = new Boolean(false);
                        } else {
                            error(new StringBuffer().append("illegal standalone value: ").append(str3).toString());
                        }
                        read++;
                    }
                    if (read != this.attributeCount) {
                        error("illegal xmldecl");
                    }
                    this.isWhitespace = true;
                    this.txtPos = 0;
                    return XML_DECL;
                }
            }
            read = 8;
            str = str2;
            z2 = true;
        } else if (read != 33) {
            error(new StringBuffer().append("illegal: <").append(read).toString());
            return 9;
        } else if (peek(0) == 45) {
            z2 = true;
            str = "--";
            read = 9;
        } else if (peek(0) == 91) {
            read = 5;
            str = "[CDATA[";
            z2 = true;
            z = true;
        } else {
            read = 10;
            str = "DOCTYPE";
            z2 = true;
        }
        for (int i = 0; i < str.length(); i++) {
            read(str.charAt(i));
        }
        if (read == 10) {
            parseDoctype(z);
        } else {
            boolean z3 = false;
            while (true) {
                boolean read2 = read();
                if (read2) {
                    error(UNEXPECTED_EOF);
                    return 9;
                }
                if (z) {
                    push(read2);
                }
                if ((z2 || read2 == z2) && peek(0) == z2 && peek(1) == 62) {
                    break;
                }
                z3 = read2;
            }
            if (z2 && r6) {
                error("illegal comment delimiter: --->");
            }
            read();
            read();
            if (z && !z2) {
                this.txtPos--;
            }
        }
        return read;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void parseStartTag(boolean r11) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
        r10 = this;
        r9 = 61;
        r1 = 32;
        r8 = 1;
        r7 = 62;
        r6 = 0;
        if (r11 != 0) goto L_0x000d;
    L_0x000a:
        r10.read();
    L_0x000d:
        r0 = r10.readName();
        r10.name = r0;
        r10.attributeCount = r6;
    L_0x0015:
        r10.skip();
        r0 = r10.peek(r6);
        if (r11 == 0) goto L_0x0029;
    L_0x001e:
        r2 = 63;
        if (r0 != r2) goto L_0x009d;
    L_0x0022:
        r10.read();
        r10.read(r7);
    L_0x0028:
        return;
    L_0x0029:
        r2 = 47;
        if (r0 != r2) goto L_0x0095;
    L_0x002d:
        r10.degenerated = r8;
        r10.read();
        r10.skip();
        r10.read(r7);
    L_0x0038:
        r0 = r10.depth;
        r1 = r0 + 1;
        r10.depth = r1;
        r0 = r0 << 2;
        r1 = r10.elementStack;
        r2 = r0 + 4;
        r1 = r10.ensureCapacity(r1, r2);
        r10.elementStack = r1;
        r1 = r10.elementStack;
        r2 = r0 + 3;
        r3 = r10.name;
        r1[r2] = r3;
        r1 = r10.depth;
        r2 = r10.nspCounts;
        r2 = r2.length;
        if (r1 < r2) goto L_0x0069;
    L_0x0059:
        r1 = r10.depth;
        r1 = r1 + 4;
        r1 = new int[r1];
        r2 = r10.nspCounts;
        r3 = r10.nspCounts;
        r3 = r3.length;
        java.lang.System.arraycopy(r2, r6, r1, r6, r3);
        r10.nspCounts = r1;
    L_0x0069:
        r1 = r10.nspCounts;
        r2 = r10.depth;
        r3 = r10.nspCounts;
        r4 = r10.depth;
        r4 = r4 + -1;
        r3 = r3[r4];
        r1[r2] = r3;
        r1 = r10.processNsp;
        if (r1 == 0) goto L_0x0136;
    L_0x007b:
        r10.adjustNsp();
    L_0x007e:
        r1 = r10.elementStack;
        r2 = r10.namespace;
        r1[r0] = r2;
        r1 = r10.elementStack;
        r2 = r0 + 1;
        r3 = r10.prefix;
        r1[r2] = r3;
        r1 = r10.elementStack;
        r0 = r0 + 2;
        r2 = r10.name;
        r1[r0] = r2;
        goto L_0x0028;
    L_0x0095:
        if (r0 != r7) goto L_0x009d;
    L_0x0097:
        if (r11 != 0) goto L_0x009d;
    L_0x0099:
        r10.read();
        goto L_0x0038;
    L_0x009d:
        r2 = -1;
        if (r0 != r2) goto L_0x00a6;
    L_0x00a0:
        r0 = "Unexpected EOF";
        r10.error(r0);
        goto L_0x0028;
    L_0x00a6:
        r0 = r10.readName();
        r2 = r0.length();
        if (r2 != 0) goto L_0x00b6;
    L_0x00b0:
        r0 = "attr name expected";
        r10.error(r0);
        goto L_0x0038;
    L_0x00b6:
        r2 = r10.attributeCount;
        r3 = r2 + 1;
        r10.attributeCount = r3;
        r2 = r2 << 2;
        r3 = r10.attributes;
        r4 = r2 + 4;
        r3 = r10.ensureCapacity(r3, r4);
        r10.attributes = r3;
        r3 = r10.attributes;
        r4 = r2 + 1;
        r5 = "";
        r3[r2] = r5;
        r2 = r10.attributes;
        r3 = r4 + 1;
        r5 = 0;
        r2[r4] = r5;
        r2 = r10.attributes;
        r4 = r3 + 1;
        r2[r3] = r0;
        r10.skip();
        r2 = r10.peek(r6);
        if (r2 == r9) goto L_0x0104;
    L_0x00e6:
        r2 = new java.lang.StringBuffer;
        r2.<init>();
        r3 = "Attr.value missing f. ";
        r2 = r2.append(r3);
        r0 = r2.append(r0);
        r0 = r0.toString();
        r10.error(r0);
        r0 = r10.attributes;
        r2 = "1";
        r0[r4] = r2;
        goto L_0x0015;
    L_0x0104:
        r10.read(r9);
        r10.skip();
        r0 = r10.peek(r6);
        r2 = 39;
        if (r0 == r2) goto L_0x0132;
    L_0x0112:
        r2 = 34;
        if (r0 == r2) goto L_0x0132;
    L_0x0116:
        r0 = "attr value delimiter missing!";
        r10.error(r0);
        r0 = r1;
    L_0x011c:
        r2 = r10.txtPos;
        r10.pushText(r0, r8);
        r3 = r10.attributes;
        r5 = r10.get(r2);
        r3[r4] = r5;
        r10.txtPos = r2;
        if (r0 == r1) goto L_0x0015;
    L_0x012d:
        r10.read();
        goto L_0x0015;
    L_0x0132:
        r10.read();
        goto L_0x011c;
    L_0x0136:
        r1 = "";
        r10.namespace = r1;
        goto L_0x007e;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kxml2.io.KXmlParser.parseStartTag(boolean):void");
    }

    private final int peek(int i) throws IOException {
        while (i >= this.peekCount) {
            int read;
            if (this.srcBuf.length <= 1) {
                read = this.reader.read();
            } else if (this.srcPos < this.srcCount) {
                char[] cArr = this.srcBuf;
                int i2 = this.srcPos;
                this.srcPos = i2 + 1;
                read = cArr[i2];
            } else {
                this.srcCount = this.reader.read(this.srcBuf, 0, this.srcBuf.length);
                read = this.srcCount <= 0 ? -1 : this.srcBuf[0];
                this.srcPos = 1;
            }
            int[] iArr;
            if (read == 13) {
                this.wasCR = true;
                iArr = this.peek;
                i2 = this.peekCount;
                this.peekCount = i2 + 1;
                iArr[i2] = 10;
            } else {
                if (read != 10) {
                    int[] iArr2 = this.peek;
                    int i3 = this.peekCount;
                    this.peekCount = i3 + 1;
                    iArr2[i3] = read;
                } else if (!this.wasCR) {
                    iArr = this.peek;
                    i2 = this.peekCount;
                    this.peekCount = i2 + 1;
                    iArr[i2] = 10;
                }
                this.wasCR = false;
            }
        }
        return this.peek[i];
    }

    private final int peekType() throws IOException {
        switch (peek(0)) {
            case ConfirmationCallback.UNSPECIFIED_OPTION /*-1*/:
                return 1;
            case Type.A6 /*38*/:
                return 6;
            case RecordButton.MAX_TIME /*60*/:
                switch (peek(1)) {
                    case Service.DSP /*33*/:
                    case Service.VIA_FTP /*63*/:
                        return LEGACY;
                    case Service.NI_FTP /*47*/:
                        return 3;
                    default:
                        return 2;
                }
            default:
                return 4;
        }
    }

    private final void push(int i) {
        this.isWhitespace = (i <= 32 ? 1 : 0) & this.isWhitespace;
        if (this.txtPos == this.txtBuf.length) {
            Object obj = new char[(((this.txtPos * 4) / 3) + 4)];
            System.arraycopy(this.txtBuf, 0, obj, 0, this.txtPos);
            this.txtBuf = obj;
        }
        char[] cArr = this.txtBuf;
        int i2 = this.txtPos;
        this.txtPos = i2 + 1;
        cArr[i2] = (char) i;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void pushEntity() throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
        r6 = this;
        r5 = 35;
        r1 = 1;
        r2 = 0;
        r0 = r6.read();
        r6.push(r0);
        r0 = r6.txtPos;
    L_0x000d:
        r3 = r6.read();
        r4 = 59;
        if (r3 != r4) goto L_0x0045;
    L_0x0015:
        r3 = r6.get(r0);
        r0 = r0 + -1;
        r6.txtPos = r0;
        r0 = r6.token;
        if (r0 == 0) goto L_0x0028;
    L_0x0021:
        r0 = r6.type;
        r4 = 6;
        if (r0 != r4) goto L_0x0028;
    L_0x0026:
        r6.name = r3;
    L_0x0028:
        r0 = r3.charAt(r2);
        if (r0 != r5) goto L_0x0088;
    L_0x002e:
        r0 = r3.charAt(r1);
        r2 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        if (r0 != r2) goto L_0x007f;
    L_0x0036:
        r0 = 2;
        r0 = r3.substring(r0);
        r1 = 16;
        r0 = java.lang.Integer.parseInt(r0, r1);
    L_0x0041:
        r6.push(r0);
    L_0x0044:
        return;
    L_0x0045:
        r4 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r3 >= r4) goto L_0x007b;
    L_0x0049:
        r4 = 48;
        if (r3 < r4) goto L_0x0051;
    L_0x004d:
        r4 = 57;
        if (r3 <= r4) goto L_0x007b;
    L_0x0051:
        r4 = 97;
        if (r3 < r4) goto L_0x0059;
    L_0x0055:
        r4 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        if (r3 <= r4) goto L_0x007b;
    L_0x0059:
        r4 = 65;
        if (r3 < r4) goto L_0x0061;
    L_0x005d:
        r4 = 90;
        if (r3 <= r4) goto L_0x007b;
    L_0x0061:
        r4 = 95;
        if (r3 == r4) goto L_0x007b;
    L_0x0065:
        r4 = 45;
        if (r3 == r4) goto L_0x007b;
    L_0x0069:
        if (r3 == r5) goto L_0x007b;
    L_0x006b:
        r0 = r6.relaxed;
        if (r0 != 0) goto L_0x0074;
    L_0x006f:
        r0 = "unterminated entity ref";
        r6.error(r0);
    L_0x0074:
        r0 = -1;
        if (r3 == r0) goto L_0x0044;
    L_0x0077:
        r6.push(r3);
        goto L_0x0044;
    L_0x007b:
        r6.push(r3);
        goto L_0x000d;
    L_0x007f:
        r0 = r3.substring(r1);
        r0 = java.lang.Integer.parseInt(r0);
        goto L_0x0041;
    L_0x0088:
        r0 = r6.entityMap;
        r0 = r0.get(r3);
        r0 = (java.lang.String) r0;
        if (r0 != 0) goto L_0x00b9;
    L_0x0092:
        r6.unresolved = r1;
        r1 = r6.unresolved;
        if (r1 == 0) goto L_0x00bb;
    L_0x0098:
        r0 = r6.token;
        if (r0 != 0) goto L_0x0044;
    L_0x009c:
        r0 = new java.lang.StringBuffer;
        r0.<init>();
        r1 = "unresolved: &";
        r0 = r0.append(r1);
        r0 = r0.append(r3);
        r1 = ";";
        r0 = r0.append(r1);
        r0 = r0.toString();
        r6.error(r0);
        goto L_0x0044;
    L_0x00b9:
        r1 = r2;
        goto L_0x0092;
    L_0x00bb:
        r1 = r0.length();
        if (r2 >= r1) goto L_0x0044;
    L_0x00c1:
        r1 = r0.charAt(r2);
        r6.push(r1);
        r2 = r2 + 1;
        goto L_0x00bb;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kxml2.io.KXmlParser.pushEntity():void");
    }

    private final void pushText(int i, boolean z) throws IOException, XmlPullParserException {
        int peek = peek(0);
        int i2 = 0;
        while (peek != -1 && peek != i) {
            if (i != 32 || (peek > 32 && peek != 62)) {
                if (peek == 38) {
                    if (z) {
                        pushEntity();
                    } else {
                        return;
                    }
                } else if (peek == 10 && this.type == 2) {
                    read();
                    push(32);
                } else {
                    push(read());
                }
                if (peek == 62 && i2 >= 2 && i != 93) {
                    error("Illegal: ]]>");
                }
                i2 = peek == 93 ? i2 + 1 : 0;
                peek = peek(0);
            } else {
                return;
            }
        }
    }

    private final int read() throws IOException {
        int peek;
        if (this.peekCount == 0) {
            peek = peek(0);
        } else {
            peek = this.peek[0];
            this.peek[0] = this.peek[1];
        }
        this.peekCount--;
        this.column++;
        if (peek == 10) {
            this.line++;
            this.column = 1;
        }
        return peek;
    }

    private final void read(char c) throws IOException, XmlPullParserException {
        char read = read();
        if (read != c) {
            error(new StringBuffer().append("expected: '").append(c).append("' actual: '").append((char) read).append("'").toString());
        }
    }

    private final String readName() throws IOException, XmlPullParserException {
        int i = this.txtPos;
        int peek = peek(0);
        if ((peek < 97 || peek > Opcodes.ISHR) && !((peek >= 65 && peek <= 90) || peek == 95 || peek == 58 || peek >= Wbxml.EXT_0 || this.relaxed)) {
            error("name expected");
        }
        while (true) {
            push(read());
            peek = peek(0);
            if ((peek < 97 || peek > Opcodes.ISHR) && ((peek < 65 || peek > 90) && !((peek >= 48 && peek <= 57) || peek == 95 || peek == 45 || peek == 58 || peek == 46 || peek >= Opcodes.INVOKESPECIAL))) {
                String str = get(i);
                this.txtPos = i;
                return str;
            }
        }
    }

    private final void skip() throws IOException {
        while (true) {
            int peek = peek(0);
            if (peek <= 32 && peek != -1) {
                read();
            } else {
                return;
            }
        }
    }

    public void defineEntityReplacementText(String str, String str2) throws XmlPullParserException {
        if (this.entityMap == null) {
            throw new RuntimeException("entity replacement text must be defined after setInput!");
        }
        this.entityMap.put(str, str2);
    }

    public int getAttributeCount() {
        return this.attributeCount;
    }

    public String getAttributeName(int i) {
        if (i < this.attributeCount) {
            return this.attributes[(i << 2) + 2];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getAttributeNamespace(int i) {
        if (i < this.attributeCount) {
            return this.attributes[i << 2];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getAttributePrefix(int i) {
        if (i < this.attributeCount) {
            return this.attributes[(i << 2) + 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getAttributeType(int i) {
        return "CDATA";
    }

    public String getAttributeValue(int i) {
        if (i < this.attributeCount) {
            return this.attributes[(i << 2) + 3];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getAttributeValue(String str, String str2) {
        int i = (this.attributeCount << 2) - 4;
        while (i >= 0) {
            if (this.attributes[i + 2].equals(str2) && (str == null || this.attributes[i].equals(str))) {
                return this.attributes[i + 3];
            }
            i -= 4;
        }
        return null;
    }

    public int getColumnNumber() {
        return this.column;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getEventType() throws XmlPullParserException {
        return this.type;
    }

    public boolean getFeature(String str) {
        return XmlPullParser.FEATURE_PROCESS_NAMESPACES.equals(str) ? this.processNsp : isProp(str, false, "relaxed") ? this.relaxed : false;
    }

    public String getInputEncoding() {
        return this.encoding;
    }

    public int getLineNumber() {
        return this.line;
    }

    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getNamespace(String str) {
        if ("xml".equals(str)) {
            return "http://www.w3.org/XML/1998/namespace";
        }
        if ("xmlns".equals(str)) {
            return "http://www.w3.org/2000/xmlns/";
        }
        for (int namespaceCount = (getNamespaceCount(this.depth) << 1) - 2; namespaceCount >= 0; namespaceCount -= 2) {
            if (str == null) {
                if (this.nspStack[namespaceCount] == null) {
                    return this.nspStack[namespaceCount + 1];
                }
            } else if (str.equals(this.nspStack[namespaceCount])) {
                return this.nspStack[namespaceCount + 1];
            }
        }
        return null;
    }

    public int getNamespaceCount(int i) {
        if (i <= this.depth) {
            return this.nspCounts[i];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getNamespacePrefix(int i) {
        return this.nspStack[i << 1];
    }

    public String getNamespaceUri(int i) {
        return this.nspStack[(i << 1) + 1];
    }

    public String getPositionDescription() {
        StringBuffer stringBuffer = new StringBuffer(this.type < XmlPullParser.TYPES.length ? XmlPullParser.TYPES[this.type] : "unknown");
        stringBuffer.append(' ');
        if (this.type == 2 || this.type == 3) {
            if (this.degenerated) {
                stringBuffer.append("(empty) ");
            }
            stringBuffer.append('<');
            if (this.type == 3) {
                stringBuffer.append('/');
            }
            if (this.prefix != null) {
                stringBuffer.append(new StringBuffer().append("{").append(this.namespace).append("}").append(this.prefix).append(":").toString());
            }
            stringBuffer.append(this.name);
            int i = this.attributeCount << 2;
            for (int i2 = 0; i2 < i; i2 += 4) {
                stringBuffer.append(' ');
                if (this.attributes[i2 + 1] != null) {
                    stringBuffer.append(new StringBuffer().append("{").append(this.attributes[i2]).append("}").append(this.attributes[i2 + 1]).append(":").toString());
                }
                stringBuffer.append(new StringBuffer().append(this.attributes[i2 + 2]).append("='").append(this.attributes[i2 + 3]).append("'").toString());
            }
            stringBuffer.append('>');
        } else if (this.type != 7) {
            if (this.type != 4) {
                stringBuffer.append(getText());
            } else if (this.isWhitespace) {
                stringBuffer.append("(whitespace)");
            } else {
                String text = getText();
                if (text.length() > 16) {
                    text = new StringBuffer().append(text.substring(0, 16)).append("...").toString();
                }
                stringBuffer.append(text);
            }
        }
        stringBuffer.append(new StringBuffer().append(XmppConnection.JID_SEPARATOR).append(this.line).append(":").append(this.column).toString());
        if (this.location != null) {
            stringBuffer.append(" in ");
            stringBuffer.append(this.location);
        } else if (this.reader != null) {
            stringBuffer.append(" in ");
            stringBuffer.append(this.reader.toString());
        }
        return stringBuffer.toString();
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Object getProperty(String str) {
        return isProp(str, true, "xmldecl-version") ? this.version : isProp(str, true, "xmldecl-standalone") ? this.standalone : isProp(str, true, LocationManagerProxy.KEY_LOCATION_CHANGED) ? this.location != null ? this.location : this.reader.toString() : null;
    }

    public String getText() {
        return (this.type < 4 || (this.type == 6 && this.unresolved)) ? null : get(0);
    }

    public char[] getTextCharacters(int[] iArr) {
        if (this.type < 4) {
            iArr[0] = -1;
            iArr[1] = -1;
            return null;
        } else if (this.type == 6) {
            iArr[0] = 0;
            iArr[1] = this.name.length();
            return this.name.toCharArray();
        } else {
            iArr[0] = 0;
            iArr[1] = this.txtPos;
            return this.txtBuf;
        }
    }

    public boolean isAttributeDefault(int i) {
        return false;
    }

    public boolean isEmptyElementTag() throws XmlPullParserException {
        if (this.type != 2) {
            exception(ILLEGAL_TYPE);
        }
        return this.degenerated;
    }

    public boolean isWhitespace() throws XmlPullParserException {
        if (!(this.type == 4 || this.type == 7 || this.type == 5)) {
            exception(ILLEGAL_TYPE);
        }
        return this.isWhitespace;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int next() throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r3 = this;
        r1 = 0;
        r2 = 4;
        r3.txtPos = r1;
        r0 = 1;
        r3.isWhitespace = r0;
        r0 = 9999; // 0x270f float:1.4012E-41 double:4.94E-320;
        r3.token = r1;
    L_0x000b:
        r3.nextImpl();
        r1 = r3.type;
        if (r1 >= r0) goto L_0x0014;
    L_0x0012:
        r0 = r3.type;
    L_0x0014:
        r1 = 6;
        if (r0 > r1) goto L_0x000b;
    L_0x0017:
        if (r0 < r2) goto L_0x001f;
    L_0x0019:
        r1 = r3.peekType();
        if (r1 >= r2) goto L_0x000b;
    L_0x001f:
        r3.type = r0;
        r0 = r3.type;
        if (r0 <= r2) goto L_0x0027;
    L_0x0025:
        r3.type = r2;
    L_0x0027:
        r0 = r3.type;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kxml2.io.KXmlParser.next():int");
    }

    public int nextTag() throws XmlPullParserException, IOException {
        next();
        if (this.type == 4 && this.isWhitespace) {
            next();
        }
        if (!(this.type == 3 || this.type == 2)) {
            exception("unexpected type");
        }
        return this.type;
    }

    public String nextText() throws XmlPullParserException, IOException {
        String text;
        if (this.type != 2) {
            exception("precondition: START_TAG");
        }
        next();
        if (this.type == 4) {
            text = getText();
            next();
        } else {
            text = XmlPullParser.NO_NAMESPACE;
        }
        if (this.type != 3) {
            exception("END_TAG expected");
        }
        return text;
    }

    public int nextToken() throws XmlPullParserException, IOException {
        this.isWhitespace = true;
        this.txtPos = 0;
        this.token = true;
        nextImpl();
        return this.type;
    }

    public void require(int i, String str, String str2) throws XmlPullParserException, IOException {
        if (i != this.type || ((str != null && !str.equals(getNamespace())) || (str2 != null && !str2.equals(getName())))) {
            exception(new StringBuffer().append("expected: ").append(XmlPullParser.TYPES[i]).append(" {").append(str).append("}").append(str2).toString());
        }
    }

    public void setFeature(String str, boolean z) throws XmlPullParserException {
        if (XmlPullParser.FEATURE_PROCESS_NAMESPACES.equals(str)) {
            this.processNsp = z;
        } else if (isProp(str, false, "relaxed")) {
            this.relaxed = z;
        } else {
            exception(new StringBuffer().append("unsupported feature: ").append(str).toString());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setInput(java.io.InputStream r7, java.lang.String r8) throws org.xmlpull.v1.XmlPullParserException {
        /*
        r6 = this;
        r5 = -1;
        r0 = 0;
        r6.srcPos = r0;
        r6.srcCount = r0;
        if (r7 != 0) goto L_0x000e;
    L_0x0008:
        r0 = new java.lang.IllegalArgumentException;
        r0.<init>();
        throw r0;
    L_0x000e:
        if (r8 != 0) goto L_0x015a;
    L_0x0010:
        r1 = r0;
    L_0x0011:
        r0 = r6.srcCount;	 Catch:{ Exception -> 0x006d }
        r2 = 4;
        if (r0 >= r2) goto L_0x001c;
    L_0x0016:
        r2 = r7.read();	 Catch:{ Exception -> 0x006d }
        if (r2 != r5) goto L_0x0057;
    L_0x001c:
        r0 = r6.srcCount;	 Catch:{ Exception -> 0x006d }
        r2 = 4;
        if (r0 != r2) goto L_0x015a;
    L_0x0021:
        switch(r1) {
            case -131072: goto L_0x008b;
            case 60: goto L_0x0091;
            case 65279: goto L_0x0067;
            case 3932223: goto L_0x00ab;
            case 1006632960: goto L_0x009e;
            case 1006649088: goto L_0x00bf;
            case 1010792557: goto L_0x00d4;
            default: goto L_0x0024;
        };	 Catch:{ Exception -> 0x006d }
    L_0x0024:
        r0 = r8;
    L_0x0025:
        r2 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
        r2 = r2 & r1;
        r3 = -16842752; // 0xfffffffffeff0000 float:-1.6947657E38 double:NaN;
        if (r2 != r3) goto L_0x0121;
    L_0x002c:
        r0 = "UTF-16BE";
        r1 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r2 = 0;
        r3 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r4 = 2;
        r3 = r3[r4];	 Catch:{ Exception -> 0x006d }
        r3 = r3 << 8;
        r4 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r5 = 3;
        r4 = r4[r5];	 Catch:{ Exception -> 0x006d }
        r3 = r3 | r4;
        r3 = (char) r3;	 Catch:{ Exception -> 0x006d }
        r1[r2] = r3;	 Catch:{ Exception -> 0x006d }
        r1 = 1;
        r6.srcCount = r1;	 Catch:{ Exception -> 0x006d }
    L_0x0044:
        if (r0 != 0) goto L_0x0048;
    L_0x0046:
        r0 = "UTF-8";
    L_0x0048:
        r1 = r6.srcCount;	 Catch:{ Exception -> 0x006d }
        r2 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x006d }
        r2.<init>(r7, r0);	 Catch:{ Exception -> 0x006d }
        r6.setInput(r2);	 Catch:{ Exception -> 0x006d }
        r6.encoding = r8;	 Catch:{ Exception -> 0x006d }
        r6.srcCount = r1;	 Catch:{ Exception -> 0x006d }
        return;
    L_0x0057:
        r0 = r1 << 8;
        r0 = r0 | r2;
        r1 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r3 = r6.srcCount;	 Catch:{ Exception -> 0x006d }
        r4 = r3 + 1;
        r6.srcCount = r4;	 Catch:{ Exception -> 0x006d }
        r2 = (char) r2;	 Catch:{ Exception -> 0x006d }
        r1[r3] = r2;	 Catch:{ Exception -> 0x006d }
        r1 = r0;
        goto L_0x0011;
    L_0x0067:
        r0 = "UTF-32BE";
        r1 = 0;
        r6.srcCount = r1;	 Catch:{ Exception -> 0x006d }
        goto L_0x0044;
    L_0x006d:
        r0 = move-exception;
        r1 = new org.xmlpull.v1.XmlPullParserException;
        r2 = new java.lang.StringBuffer;
        r2.<init>();
        r3 = "Invalid stream or encoding: ";
        r2 = r2.append(r3);
        r3 = r0.toString();
        r2 = r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2, r6, r0);
        throw r1;
    L_0x008b:
        r0 = "UTF-32LE";
        r1 = 0;
        r6.srcCount = r1;	 Catch:{ Exception -> 0x006d }
        goto L_0x0044;
    L_0x0091:
        r0 = "UTF-32BE";
        r1 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r2 = 0;
        r3 = 60;
        r1[r2] = r3;	 Catch:{ Exception -> 0x006d }
        r1 = 1;
        r6.srcCount = r1;	 Catch:{ Exception -> 0x006d }
        goto L_0x0044;
    L_0x009e:
        r0 = "UTF-32LE";
        r1 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r2 = 0;
        r3 = 60;
        r1[r2] = r3;	 Catch:{ Exception -> 0x006d }
        r1 = 1;
        r6.srcCount = r1;	 Catch:{ Exception -> 0x006d }
        goto L_0x0044;
    L_0x00ab:
        r0 = "UTF-16BE";
        r1 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r2 = 0;
        r3 = 60;
        r1[r2] = r3;	 Catch:{ Exception -> 0x006d }
        r1 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r2 = 1;
        r3 = 63;
        r1[r2] = r3;	 Catch:{ Exception -> 0x006d }
        r1 = 2;
        r6.srcCount = r1;	 Catch:{ Exception -> 0x006d }
        goto L_0x0044;
    L_0x00bf:
        r0 = "UTF-16LE";
        r1 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r2 = 0;
        r3 = 60;
        r1[r2] = r3;	 Catch:{ Exception -> 0x006d }
        r1 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r2 = 1;
        r3 = 63;
        r1[r2] = r3;	 Catch:{ Exception -> 0x006d }
        r1 = 2;
        r6.srcCount = r1;	 Catch:{ Exception -> 0x006d }
        goto L_0x0044;
    L_0x00d4:
        r0 = r7.read();	 Catch:{ Exception -> 0x006d }
        if (r0 != r5) goto L_0x00dd;
    L_0x00da:
        r0 = r8;
        goto L_0x0025;
    L_0x00dd:
        r2 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r3 = r6.srcCount;	 Catch:{ Exception -> 0x006d }
        r4 = r3 + 1;
        r6.srcCount = r4;	 Catch:{ Exception -> 0x006d }
        r4 = (char) r0;	 Catch:{ Exception -> 0x006d }
        r2[r3] = r4;	 Catch:{ Exception -> 0x006d }
        r2 = 62;
        if (r0 != r2) goto L_0x00d4;
    L_0x00ec:
        r2 = new java.lang.String;	 Catch:{ Exception -> 0x006d }
        r0 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r3 = 0;
        r4 = r6.srcCount;	 Catch:{ Exception -> 0x006d }
        r2.<init>(r0, r3, r4);	 Catch:{ Exception -> 0x006d }
        r0 = "encoding";
        r0 = r2.indexOf(r0);	 Catch:{ Exception -> 0x006d }
        if (r0 == r5) goto L_0x0024;
    L_0x00fe:
        r3 = r2.charAt(r0);	 Catch:{ Exception -> 0x006d }
        r4 = 34;
        if (r3 == r4) goto L_0x0111;
    L_0x0106:
        r3 = r2.charAt(r0);	 Catch:{ Exception -> 0x006d }
        r4 = 39;
        if (r3 == r4) goto L_0x0111;
    L_0x010e:
        r0 = r0 + 1;
        goto L_0x00fe;
    L_0x0111:
        r3 = r0 + 1;
        r0 = r2.charAt(r0);	 Catch:{ Exception -> 0x006d }
        r0 = r2.indexOf(r0, r3);	 Catch:{ Exception -> 0x006d }
        r0 = r2.substring(r3, r0);	 Catch:{ Exception -> 0x006d }
        goto L_0x0025;
    L_0x0121:
        r2 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
        r2 = r2 & r1;
        r3 = -131072; // 0xfffffffffffe0000 float:NaN double:NaN;
        if (r2 != r3) goto L_0x0142;
    L_0x0128:
        r0 = "UTF-16LE";
        r1 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r2 = 0;
        r3 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r4 = 3;
        r3 = r3[r4];	 Catch:{ Exception -> 0x006d }
        r3 = r3 << 8;
        r4 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r5 = 2;
        r4 = r4[r5];	 Catch:{ Exception -> 0x006d }
        r3 = r3 | r4;
        r3 = (char) r3;	 Catch:{ Exception -> 0x006d }
        r1[r2] = r3;	 Catch:{ Exception -> 0x006d }
        r1 = 1;
        r6.srcCount = r1;	 Catch:{ Exception -> 0x006d }
        goto L_0x0044;
    L_0x0142:
        r1 = r1 & -256;
        r2 = -272908544; // 0xffffffffefbbbf00 float:-1.162092E29 double:NaN;
        if (r1 != r2) goto L_0x0044;
    L_0x0149:
        r0 = "UTF-8";
        r1 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r2 = 0;
        r3 = r6.srcBuf;	 Catch:{ Exception -> 0x006d }
        r4 = 3;
        r3 = r3[r4];	 Catch:{ Exception -> 0x006d }
        r1[r2] = r3;	 Catch:{ Exception -> 0x006d }
        r1 = 1;
        r6.srcCount = r1;	 Catch:{ Exception -> 0x006d }
        goto L_0x0044;
    L_0x015a:
        r0 = r8;
        goto L_0x0044;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kxml2.io.KXmlParser.setInput(java.io.InputStream, java.lang.String):void");
    }

    public void setInput(Reader reader) throws XmlPullParserException {
        this.reader = reader;
        this.line = 1;
        this.column = 0;
        this.type = 0;
        this.name = null;
        this.namespace = null;
        this.degenerated = false;
        this.attributeCount = -1;
        this.encoding = null;
        this.version = null;
        this.standalone = null;
        if (reader != null) {
            this.srcPos = 0;
            this.srcCount = 0;
            this.peekCount = 0;
            this.depth = 0;
            this.entityMap = new Hashtable();
            this.entityMap.put("amp", AlixDefine.split);
            this.entityMap.put("apos", "'");
            this.entityMap.put("gt", ">");
            this.entityMap.put("lt", "<");
            this.entityMap.put("quot", "\"");
        }
    }

    public void setProperty(String str, Object obj) throws XmlPullParserException {
        if (isProp(str, true, LocationManagerProxy.KEY_LOCATION_CHANGED)) {
            this.location = obj;
            return;
        }
        throw new XmlPullParserException(new StringBuffer().append("unsupported property: ").append(str).toString());
    }

    public void skipSubTree() throws XmlPullParserException, IOException {
        require(2, null, null);
        int i = 1;
        while (i > 0) {
            int next = next();
            if (next == 3) {
                i--;
            } else if (next == 2) {
                i++;
            }
        }
    }
}
