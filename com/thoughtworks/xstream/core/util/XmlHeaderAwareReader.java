package com.thoughtworks.xstream.core.util;

import com.ifoer.entity.Constant;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

public final class XmlHeaderAwareReader extends Reader {
    private static final String KEY_ENCODING = "encoding";
    private static final String KEY_VERSION = "version";
    private static final int STATE_ATTR_NAME = 3;
    private static final int STATE_ATTR_VALUE = 4;
    private static final int STATE_AWAIT_XML_HEADER = 2;
    private static final int STATE_BOM = 0;
    private static final int STATE_START = 1;
    private static final String XML_TOKEN = "?xml";
    private final InputStreamReader reader;
    private final double version;

    public XmlHeaderAwareReader(InputStream in) throws UnsupportedEncodingException, IOException {
        PushbackInputStream[] pin = new PushbackInputStream[STATE_START];
        pin[STATE_BOM] = in instanceof PushbackInputStream ? (PushbackInputStream) in : new PushbackInputStream(in, 64);
        Map header = getHeader(pin);
        this.version = Double.parseDouble((String) header.get(KEY_VERSION));
        this.reader = new InputStreamReader(pin[STATE_BOM], (String) header.get(KEY_ENCODING));
    }

    private Map getHeader(PushbackInputStream[] in) throws IOException {
        byte[] pushbackData;
        int i;
        Map header = new HashMap();
        header.put(KEY_ENCODING, "utf-8");
        header.put(KEY_VERSION, Constant.APP_VERSION);
        int state = STATE_BOM;
        ByteArrayOutputStream out = new ByteArrayOutputStream(64);
        int i2 = STATE_BOM;
        char valueEnd = '\u0000';
        StringBuffer name = new StringBuffer();
        StringBuffer value = new StringBuffer();
        boolean escape = false;
        while (i2 != -1) {
            i2 = in[STATE_BOM].read();
            if (i2 != -1) {
                out.write(i2);
                char ch = (char) i2;
                switch (state) {
                    case STATE_BOM /*0*/:
                        if ((ch == '\u00ef' && out.size() == STATE_START) || ((ch == '\u00bb' && out.size() == STATE_AWAIT_XML_HEADER) || (ch == '\u00bf' && out.size() == STATE_ATTR_NAME))) {
                            if (ch != '\u00bf') {
                                break;
                            }
                            out.reset();
                            state = STATE_START;
                            break;
                        } else if (out.size() > STATE_START) {
                            i2 = -1;
                            break;
                        } else {
                            state = STATE_START;
                        }
                        break;
                    case STATE_START /*1*/:
                        if (!Character.isWhitespace(ch)) {
                            if (ch != '<') {
                                i2 = -1;
                                break;
                            }
                            state = STATE_AWAIT_XML_HEADER;
                            break;
                        }
                        break;
                    case STATE_AWAIT_XML_HEADER /*2*/:
                        if (Character.isWhitespace(ch)) {
                            if (!name.toString().equals(XML_TOKEN)) {
                                i2 = -1;
                                break;
                            }
                            state = STATE_ATTR_NAME;
                            name.setLength(STATE_BOM);
                            break;
                        }
                        name.append(Character.toLowerCase(ch));
                        if (!XML_TOKEN.startsWith(name.substring(STATE_BOM))) {
                            i2 = -1;
                            break;
                        }
                        break;
                    case STATE_ATTR_NAME /*3*/:
                        if (!Character.isWhitespace(ch)) {
                            if (ch != SignatureVisitor.INSTANCEOF) {
                                ch = Character.toLowerCase(ch);
                                if (!Character.isLetter(ch)) {
                                    i2 = -1;
                                    break;
                                }
                                name.append(ch);
                                break;
                            }
                            state = STATE_ATTR_VALUE;
                            break;
                        } else if (name.length() <= 0) {
                            break;
                        } else {
                            i2 = -1;
                            break;
                        }
                    case STATE_ATTR_VALUE /*4*/:
                        if (valueEnd != '\u0000') {
                            if (ch != '\\' || escape) {
                                if (ch == valueEnd && !escape) {
                                    valueEnd = '\u0000';
                                    state = STATE_ATTR_NAME;
                                    header.put(name.toString(), value.toString());
                                    name.setLength(STATE_BOM);
                                    value.setLength(STATE_BOM);
                                    break;
                                }
                                escape = false;
                                if (ch == '\n') {
                                    i2 = -1;
                                    break;
                                }
                                value.append(ch);
                                break;
                            }
                            escape = true;
                            break;
                        } else if (ch != '\"' && ch != '\'') {
                            i2 = -1;
                            break;
                        } else {
                            valueEnd = ch;
                            break;
                        }
                    default:
                        break;
                }
            }
            pushbackData = out.toByteArray();
            i = pushbackData.length;
            while (true) {
                i2 = i - 1;
                if (i > 0) {
                    return header;
                }
                try {
                    in[STATE_BOM].unread(pushbackData[i2]);
                } catch (IOException e) {
                    i2 += STATE_START;
                    in[STATE_BOM] = new PushbackInputStream(in[STATE_BOM], i2);
                }
                i = i2;
            }
        }
        pushbackData = out.toByteArray();
        i = pushbackData.length;
        while (true) {
            i2 = i - 1;
            if (i > 0) {
                return header;
            }
            in[STATE_BOM].unread(pushbackData[i2]);
            i = i2;
        }
    }

    public String getEncoding() {
        return this.reader.getEncoding();
    }

    public double getVersion() {
        return this.version;
    }

    public void mark(int readAheadLimit) throws IOException {
        this.reader.mark(readAheadLimit);
    }

    public boolean markSupported() {
        return this.reader.markSupported();
    }

    public int read() throws IOException {
        return this.reader.read();
    }

    public int read(char[] cbuf, int offset, int length) throws IOException {
        return this.reader.read(cbuf, offset, length);
    }

    public int read(char[] cbuf) throws IOException {
        return this.reader.read(cbuf);
    }

    public boolean ready() throws IOException {
        return this.reader.ready();
    }

    public void reset() throws IOException {
        this.reader.reset();
    }

    public long skip(long n) throws IOException {
        return this.reader.skip(n);
    }

    public void close() throws IOException {
        this.reader.close();
    }

    public boolean equals(Object obj) {
        return this.reader.equals(obj);
    }

    public int hashCode() {
        return this.reader.hashCode();
    }

    public String toString() {
        return this.reader.toString();
    }
}
