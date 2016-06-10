package com.thoughtworks.xstream.io.xml;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import com.thoughtworks.xstream.core.util.FastStack;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import java.io.Writer;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

public class PrettyPrintWriter extends AbstractXmlWriter {
    private static final char[] AMP;
    private static final char[] APOS;
    private static final char[] CLOSE;
    private static final char[] CR;
    private static final char[] GT;
    private static final char[] LT;
    private static final char[] NULL;
    private static final char[] QUOT;
    public static int XML_1_0;
    public static int XML_1_1;
    public static int XML_QUIRKS;
    protected int depth;
    private final FastStack elementStack;
    private final char[] lineIndenter;
    private final int mode;
    private String newLine;
    private boolean readyForNewLine;
    private boolean tagInProgress;
    private boolean tagIsEmpty;
    private final QuickWriter writer;

    static {
        XML_QUIRKS = -1;
        XML_1_0 = 0;
        XML_1_1 = 1;
        NULL = "&#x0;".toCharArray();
        AMP = "&amp;".toCharArray();
        LT = "&lt;".toCharArray();
        GT = "&gt;".toCharArray();
        CR = "&#xd;".toCharArray();
        QUOT = "&quot;".toCharArray();
        APOS = "&apos;".toCharArray();
        CLOSE = "</".toCharArray();
    }

    private PrettyPrintWriter(Writer writer, int mode, char[] lineIndenter, NameCoder nameCoder, String newLine) {
        super(nameCoder);
        this.elementStack = new FastStack(16);
        this.writer = new QuickWriter(writer);
        this.lineIndenter = lineIndenter;
        this.newLine = newLine;
        this.mode = mode;
        if (mode < XML_QUIRKS || mode > XML_1_1) {
            throw new IllegalArgumentException("Not a valid XML mode");
        }
    }

    public PrettyPrintWriter(Writer writer, char[] lineIndenter, String newLine, XmlFriendlyReplacer replacer) {
        this(writer, XML_QUIRKS, lineIndenter, replacer, newLine);
    }

    public PrettyPrintWriter(Writer writer, int mode, char[] lineIndenter, NameCoder nameCoder) {
        this(writer, mode, lineIndenter, nameCoder, SpecilApiUtil.LINE_SEP);
    }

    public PrettyPrintWriter(Writer writer, int mode, char[] lineIndenter, XmlFriendlyReplacer replacer) {
        this(writer, mode, lineIndenter, replacer, SpecilApiUtil.LINE_SEP);
    }

    public PrettyPrintWriter(Writer writer, char[] lineIndenter, String newLine) {
        this(writer, lineIndenter, newLine, new XmlFriendlyReplacer());
    }

    public PrettyPrintWriter(Writer writer, int mode, char[] lineIndenter) {
        this(writer, mode, lineIndenter, new XmlFriendlyNameCoder());
    }

    public PrettyPrintWriter(Writer writer, char[] lineIndenter) {
        this(writer, XML_QUIRKS, lineIndenter);
    }

    public PrettyPrintWriter(Writer writer, String lineIndenter, String newLine) {
        this(writer, lineIndenter.toCharArray(), newLine);
    }

    public PrettyPrintWriter(Writer writer, int mode, String lineIndenter) {
        this(writer, mode, lineIndenter.toCharArray());
    }

    public PrettyPrintWriter(Writer writer, String lineIndenter) {
        this(writer, lineIndenter.toCharArray());
    }

    public PrettyPrintWriter(Writer writer, int mode, NameCoder nameCoder) {
        this(writer, mode, new char[]{' ', ' '}, nameCoder);
    }

    public PrettyPrintWriter(Writer writer, int mode, XmlFriendlyReplacer replacer) {
        this(writer, mode, new char[]{' ', ' '}, replacer);
    }

    public PrettyPrintWriter(Writer writer, NameCoder nameCoder) {
        this(writer, XML_QUIRKS, new char[]{' ', ' '}, nameCoder, SpecilApiUtil.LINE_SEP);
    }

    public PrettyPrintWriter(Writer writer, XmlFriendlyReplacer replacer) {
        this(writer, new char[]{' ', ' '}, SpecilApiUtil.LINE_SEP, replacer);
    }

    public PrettyPrintWriter(Writer writer, int mode) {
        this(writer, mode, new char[]{' ', ' '});
    }

    public PrettyPrintWriter(Writer writer) {
        this(writer, new char[]{' ', ' '});
    }

    public void startNode(String name) {
        String escapedName = encodeNode(name);
        this.tagIsEmpty = false;
        finishTag();
        this.writer.write('<');
        this.writer.write(escapedName);
        this.elementStack.push(escapedName);
        this.tagInProgress = true;
        this.depth++;
        this.readyForNewLine = true;
        this.tagIsEmpty = true;
    }

    public void startNode(String name, Class clazz) {
        startNode(name);
    }

    public void setValue(String text) {
        this.readyForNewLine = false;
        this.tagIsEmpty = false;
        finishTag();
        writeText(this.writer, text);
    }

    public void addAttribute(String key, String value) {
        this.writer.write(' ');
        this.writer.write(encodeAttribute(key));
        this.writer.write((char) SignatureVisitor.INSTANCEOF);
        this.writer.write('\"');
        writeAttributeValue(this.writer, value);
        this.writer.write('\"');
    }

    protected void writeAttributeValue(QuickWriter writer, String text) {
        writeText(text, true);
    }

    protected void writeText(QuickWriter writer, String text) {
        writeText(text, false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeText(java.lang.String r7, boolean r8) {
        /*
        r6 = this;
        r2 = r7.length();
        r1 = 0;
    L_0x0005:
        if (r1 >= r2) goto L_0x0130;
    L_0x0007:
        r0 = r7.charAt(r1);
        switch(r0) {
            case 0: goto L_0x004d;
            case 9: goto L_0x0095;
            case 10: goto L_0x0095;
            case 13: goto L_0x008d;
            case 34: goto L_0x007d;
            case 38: goto L_0x0065;
            case 39: goto L_0x0085;
            case 60: goto L_0x006d;
            case 62: goto L_0x0075;
            default: goto L_0x000e;
        };
    L_0x000e:
        r3 = java.lang.Character.isDefined(r0);
        if (r3 == 0) goto L_0x00a3;
    L_0x0014:
        r3 = java.lang.Character.isISOControl(r0);
        if (r3 != 0) goto L_0x00a3;
    L_0x001a:
        r3 = r6.mode;
        r4 = XML_QUIRKS;
        if (r3 == r4) goto L_0x009d;
    L_0x0020:
        r3 = 55295; // 0xd7ff float:7.7485E-41 double:2.73194E-319;
        if (r0 <= r3) goto L_0x009d;
    L_0x0025:
        r3 = 57344; // 0xe000 float:8.0356E-41 double:2.83317E-319;
        if (r0 >= r3) goto L_0x009d;
    L_0x002a:
        r3 = new com.thoughtworks.xstream.io.StreamException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Invalid character 0x";
        r4 = r4.append(r5);
        r5 = java.lang.Integer.toHexString(r0);
        r4 = r4.append(r5);
        r5 = " in XML stream";
        r4 = r4.append(r5);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
    L_0x004d:
        r3 = r6.mode;
        r4 = XML_QUIRKS;
        if (r3 != r4) goto L_0x005d;
    L_0x0053:
        r3 = r6.writer;
        r4 = NULL;
        r3.write(r4);
    L_0x005a:
        r1 = r1 + 1;
        goto L_0x0005;
    L_0x005d:
        r3 = new com.thoughtworks.xstream.io.StreamException;
        r4 = "Invalid character 0x0 in XML stream";
        r3.<init>(r4);
        throw r3;
    L_0x0065:
        r3 = r6.writer;
        r4 = AMP;
        r3.write(r4);
        goto L_0x005a;
    L_0x006d:
        r3 = r6.writer;
        r4 = LT;
        r3.write(r4);
        goto L_0x005a;
    L_0x0075:
        r3 = r6.writer;
        r4 = GT;
        r3.write(r4);
        goto L_0x005a;
    L_0x007d:
        r3 = r6.writer;
        r4 = QUOT;
        r3.write(r4);
        goto L_0x005a;
    L_0x0085:
        r3 = r6.writer;
        r4 = APOS;
        r3.write(r4);
        goto L_0x005a;
    L_0x008d:
        r3 = r6.writer;
        r4 = CR;
        r3.write(r4);
        goto L_0x005a;
    L_0x0095:
        if (r8 != 0) goto L_0x000e;
    L_0x0097:
        r3 = r6.writer;
        r3.write(r0);
        goto L_0x005a;
    L_0x009d:
        r3 = r6.writer;
        r3.write(r0);
        goto L_0x005a;
    L_0x00a3:
        r3 = r6.mode;
        r4 = XML_1_0;
        if (r3 != r4) goto L_0x00e4;
    L_0x00a9:
        r3 = 9;
        if (r0 < r3) goto L_0x00c1;
    L_0x00ad:
        r3 = 11;
        if (r0 == r3) goto L_0x00c1;
    L_0x00b1:
        r3 = 12;
        if (r0 == r3) goto L_0x00c1;
    L_0x00b5:
        r3 = 14;
        if (r0 == r3) goto L_0x00c1;
    L_0x00b9:
        r3 = 15;
        if (r0 < r3) goto L_0x00e4;
    L_0x00bd:
        r3 = 31;
        if (r0 > r3) goto L_0x00e4;
    L_0x00c1:
        r3 = new com.thoughtworks.xstream.io.StreamException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Invalid character 0x";
        r4 = r4.append(r5);
        r5 = java.lang.Integer.toHexString(r0);
        r4 = r4.append(r5);
        r5 = " in XML 1.0 stream";
        r4 = r4.append(r5);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
    L_0x00e4:
        r3 = r6.mode;
        r4 = XML_QUIRKS;
        if (r3 == r4) goto L_0x0117;
    L_0x00ea:
        r3 = 65534; // 0xfffe float:9.1833E-41 double:3.2378E-319;
        if (r0 == r3) goto L_0x00f4;
    L_0x00ef:
        r3 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        if (r0 != r3) goto L_0x0117;
    L_0x00f4:
        r3 = new com.thoughtworks.xstream.io.StreamException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Invalid character 0x";
        r4 = r4.append(r5);
        r5 = java.lang.Integer.toHexString(r0);
        r4 = r4.append(r5);
        r5 = " in XML stream";
        r4 = r4.append(r5);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
    L_0x0117:
        r3 = r6.writer;
        r4 = "&#x";
        r3.write(r4);
        r3 = r6.writer;
        r4 = java.lang.Integer.toHexString(r0);
        r3.write(r4);
        r3 = r6.writer;
        r4 = 59;
        r3.write(r4);
        goto L_0x005a;
    L_0x0130:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.thoughtworks.xstream.io.xml.PrettyPrintWriter.writeText(java.lang.String, boolean):void");
    }

    public void endNode() {
        this.depth--;
        if (this.tagIsEmpty) {
            this.writer.write('/');
            this.readyForNewLine = false;
            finishTag();
            this.elementStack.popSilently();
        } else {
            finishTag();
            this.writer.write(CLOSE);
            this.writer.write((String) this.elementStack.pop());
            this.writer.write('>');
        }
        this.readyForNewLine = true;
        if (this.depth == 0) {
            this.writer.flush();
        }
    }

    private void finishTag() {
        if (this.tagInProgress) {
            this.writer.write('>');
        }
        this.tagInProgress = false;
        if (this.readyForNewLine) {
            endOfLine();
        }
        this.readyForNewLine = false;
        this.tagIsEmpty = false;
    }

    protected void endOfLine() {
        this.writer.write(getNewLine());
        for (int i = 0; i < this.depth; i++) {
            this.writer.write(this.lineIndenter);
        }
    }

    public void flush() {
        this.writer.flush();
    }

    public void close() {
        this.writer.close();
    }

    protected String getNewLine() {
        return this.newLine;
    }
}
