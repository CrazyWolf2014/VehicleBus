package org.kxml2.wap;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Vector;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public class WbxmlSerializer implements XmlSerializer {
    private int attrPage;
    Hashtable attrStartTable;
    Hashtable attrValueTable;
    Vector attributes;
    ByteArrayOutputStream buf;
    int depth;
    private String encoding;
    String name;
    String namespace;
    OutputStream out;
    String pending;
    Hashtable stringTable;
    ByteArrayOutputStream stringTableBuf;
    private int tagPage;
    Hashtable tagTable;

    public WbxmlSerializer() {
        this.stringTable = new Hashtable();
        this.buf = new ByteArrayOutputStream();
        this.stringTableBuf = new ByteArrayOutputStream();
        this.attributes = new Vector();
        this.attrStartTable = new Hashtable();
        this.attrValueTable = new Hashtable();
        this.tagTable = new Hashtable();
    }

    static void writeInt(OutputStream outputStream, int i) throws IOException {
        byte[] bArr = new byte[5];
        int i2 = 0;
        while (true) {
            int i3 = i2 + 1;
            bArr[i2] = (byte) (i & Service.LOCUS_CON);
            i >>= 7;
            if (i == 0) {
                break;
            }
            i2 = i3;
        }
        while (i3 > 1) {
            i3--;
            outputStream.write(bArr[i3] | Flags.FLAG8);
        }
        outputStream.write(bArr[0]);
    }

    private void writeStr(String str) throws IOException {
        int length = str.length();
        int i = 0;
        int i2 = 0;
        while (i2 < length) {
            while (i2 < length && str.charAt(i2) < 'A') {
                i2++;
            }
            int i3 = i2;
            while (i3 < length && str.charAt(i3) >= 'A') {
                i3++;
            }
            if (i3 - i2 > 10) {
                if (i2 > i && str.charAt(i2 - 1) == ' ' && this.stringTable.get(str.substring(i2, i3)) == null) {
                    this.buf.write(Service.CISCO_TNA);
                    writeStrT(str.substring(i, i3), false);
                } else {
                    if (i2 > i && str.charAt(i2 - 1) == ' ') {
                        i2--;
                    }
                    if (i2 > i) {
                        this.buf.write(Service.CISCO_TNA);
                        writeStrT(str.substring(i, i2), false);
                    }
                    this.buf.write(Service.CISCO_TNA);
                    writeStrT(str.substring(i2, i3), true);
                }
                i = i3;
            }
            i2 = i3;
        }
        if (i < length) {
            this.buf.write(Service.CISCO_TNA);
            writeStrT(str.substring(i, length), false);
        }
    }

    private final void writeStrT(String str, boolean z) throws IOException {
        Integer num = (Integer) this.stringTable.get(str);
        if (num != null) {
            writeInt(this.buf, num.intValue());
            return;
        }
        int size = this.stringTableBuf.size();
        if (str.charAt(0) < '0' || !z) {
            writeInt(this.buf, size);
        } else {
            str = new StringBuffer().append(' ').append(str).toString();
            writeInt(this.buf, size + 1);
        }
        this.stringTable.put(str, new Integer(size));
        if (str.charAt(0) == ' ') {
            this.stringTable.put(str.substring(1), new Integer(size + 1));
        }
        int lastIndexOf = str.lastIndexOf(32);
        if (lastIndexOf > 1) {
            this.stringTable.put(str.substring(lastIndexOf), new Integer(size + lastIndexOf));
            this.stringTable.put(str.substring(lastIndexOf + 1), new Integer((size + lastIndexOf) + 1));
        }
        writeStrI(this.stringTableBuf, str);
        this.stringTableBuf.flush();
    }

    public XmlSerializer attribute(String str, String str2, String str3) {
        this.attributes.addElement(str2);
        this.attributes.addElement(str3);
        return this;
    }

    public void cdsect(String str) throws IOException {
        text(str);
    }

    public void checkPending(boolean z) throws IOException {
        if (this.pending != null) {
            int size = this.attributes.size();
            int[] iArr = (int[]) this.tagTable.get(this.pending);
            ByteArrayOutputStream byteArrayOutputStream;
            int i;
            if (iArr == null) {
                byteArrayOutputStream = this.buf;
                i = size == 0 ? z ? 4 : 68 : z ? Service.CISCO_SYS : Wbxml.LITERAL_AC;
                byteArrayOutputStream.write(i);
                writeStrT(this.pending, false);
            } else {
                if (iArr[0] != this.tagPage) {
                    this.tagPage = iArr[0];
                    this.buf.write(0);
                    this.buf.write(this.tagPage);
                }
                byteArrayOutputStream = this.buf;
                i = size == 0 ? z ? iArr[1] : iArr[1] | 64 : z ? iArr[1] | Flags.FLAG8 : iArr[1] | Wbxml.EXT_0;
                byteArrayOutputStream.write(i);
            }
            int i2 = 0;
            while (i2 < size) {
                iArr = (int[]) this.attrStartTable.get(this.attributes.elementAt(i2));
                if (iArr == null) {
                    this.buf.write(4);
                    writeStrT((String) this.attributes.elementAt(i2), false);
                } else {
                    if (iArr[0] != this.attrPage) {
                        this.attrPage = iArr[0];
                        this.buf.write(0);
                        this.buf.write(this.attrPage);
                    }
                    this.buf.write(iArr[1]);
                }
                i2++;
                iArr = (int[]) this.attrValueTable.get(this.attributes.elementAt(i2));
                if (iArr == null) {
                    writeStr((String) this.attributes.elementAt(i2));
                } else {
                    if (iArr[0] != this.attrPage) {
                        this.attrPage = iArr[0];
                        this.buf.write(0);
                        this.buf.write(this.attrPage);
                    }
                    this.buf.write(iArr[1]);
                }
                i2++;
            }
            if (size > 0) {
                this.buf.write(1);
            }
            this.pending = null;
            this.attributes.removeAllElements();
        }
    }

    public void comment(String str) {
    }

    public void docdecl(String str) {
        throw new RuntimeException("Cannot write docdecl for WBXML");
    }

    public void endDocument() throws IOException {
        writeInt(this.out, this.stringTableBuf.size());
        this.out.write(this.stringTableBuf.toByteArray());
        this.out.write(this.buf.toByteArray());
        this.out.flush();
    }

    public XmlSerializer endTag(String str, String str2) throws IOException {
        if (this.pending != null) {
            checkPending(true);
        } else {
            this.buf.write(1);
        }
        this.depth--;
        return this;
    }

    public void entityRef(String str) {
        throw new RuntimeException("EntityReference not supported for WBXML");
    }

    public void flush() {
    }

    public int getDepth() {
        return this.depth;
    }

    public boolean getFeature(String str) {
        return false;
    }

    public String getName() {
        throw new RuntimeException("NYI");
    }

    public String getNamespace() {
        throw new RuntimeException("NYI");
    }

    public String getPrefix(String str, boolean z) {
        throw new RuntimeException("NYI");
    }

    public Object getProperty(String str) {
        return null;
    }

    public void ignorableWhitespace(String str) {
    }

    public void processingInstruction(String str) {
        throw new RuntimeException("PI NYI");
    }

    public void setAttrStartTable(int i, String[] strArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2] != null) {
                this.attrStartTable.put(strArr[i2], new int[]{i, i2 + 5});
            }
        }
    }

    public void setAttrValueTable(int i, String[] strArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2] != null) {
                this.attrValueTable.put(strArr[i2], new int[]{i, i2 + Service.STATSRV});
            }
        }
    }

    public void setFeature(String str, boolean z) {
        throw new IllegalArgumentException(new StringBuffer().append("unknown feature ").append(str).toString());
    }

    public void setOutput(OutputStream outputStream, String str) throws IOException {
        if (str == null) {
            str = AsyncHttpResponseHandler.DEFAULT_CHARSET;
        }
        this.encoding = str;
        this.out = outputStream;
        this.buf = new ByteArrayOutputStream();
        this.stringTableBuf = new ByteArrayOutputStream();
    }

    public void setOutput(Writer writer) {
        throw new RuntimeException("Wbxml requires an OutputStream!");
    }

    public void setPrefix(String str, String str2) {
        throw new RuntimeException("NYI");
    }

    public void setProperty(String str, Object obj) {
        throw new IllegalArgumentException(new StringBuffer().append("unknown property ").append(str).toString());
    }

    public void setTagTable(int i, String[] strArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2] != null) {
                this.tagTable.put(strArr[i2], new int[]{i, i2 + 5});
            }
        }
    }

    public void startDocument(String str, Boolean bool) throws IOException {
        this.out.write(3);
        this.out.write(1);
        if (str != null) {
            this.encoding = str;
        }
        if (this.encoding.toUpperCase().equals(AsyncHttpResponseHandler.DEFAULT_CHARSET)) {
            this.out.write(Opcodes.FMUL);
        } else if (this.encoding.toUpperCase().equals("ISO-8859-1")) {
            this.out.write(4);
        } else {
            throw new UnsupportedEncodingException(str);
        }
    }

    public XmlSerializer startTag(String str, String str2) throws IOException {
        if (str == null || XmlPullParser.NO_NAMESPACE.equals(str)) {
            checkPending(false);
            this.pending = str2;
            this.depth++;
            return this;
        }
        throw new RuntimeException("NSP NYI");
    }

    public XmlSerializer text(String str) throws IOException {
        checkPending(false);
        writeStr(str);
        return this;
    }

    public XmlSerializer text(char[] cArr, int i, int i2) throws IOException {
        checkPending(false);
        writeStr(new String(cArr, i, i2));
        return this;
    }

    void writeStrI(OutputStream outputStream, String str) throws IOException {
        outputStream.write(str.getBytes(this.encoding));
        outputStream.write(0);
    }

    public void writeWapExtension(int i, Object obj) throws IOException {
        checkPending(false);
        this.buf.write(i);
        switch (i) {
            case WbxmlParser.WAP_EXTENSION /*64*/:
            case Service.TACACS_DS /*65*/:
            case Protocol.RVD /*66*/:
                writeStrI(this.buf, (String) obj);
            case Flags.FLAG8 /*128*/:
            case Service.PWDGEN /*129*/:
            case Service.CISCO_FNA /*130*/:
                writeStrT((String) obj, false);
            case Wbxml.EXT_0 /*192*/:
            case Wbxml.EXT_1 /*193*/:
            case Wbxml.EXT_2 /*194*/:
            case Wbxml.OPAQUE /*195*/:
                byte[] bArr = (byte[]) obj;
                writeInt(this.buf, bArr.length);
                this.buf.write(bArr);
            default:
                throw new IllegalArgumentException();
        }
    }
}
