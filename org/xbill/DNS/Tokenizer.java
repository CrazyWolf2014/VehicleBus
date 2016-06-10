package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.Util;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.xbill.DNS.utils.base16;
import org.xbill.DNS.utils.base32;
import org.xbill.DNS.utils.base64;

public class Tokenizer {
    public static final int COMMENT = 5;
    public static final int EOF = 0;
    public static final int EOL = 1;
    public static final int IDENTIFIER = 3;
    public static final int QUOTED_STRING = 4;
    public static final int WHITESPACE = 2;
    private static String delim;
    private static String quotes;
    private Token current;
    private String delimiters;
    private String filename;
    private PushbackInputStream is;
    private int line;
    private int multiline;
    private boolean quoting;
    private StringBuffer sb;
    private boolean ungottenToken;
    private boolean wantClose;

    public static class Token {
        public int type;
        public String value;

        private Token() {
            this.type = -1;
            this.value = null;
        }

        private Token set(int i, StringBuffer stringBuffer) {
            if (i < 0) {
                throw new IllegalArgumentException();
            }
            this.type = i;
            this.value = stringBuffer == null ? null : stringBuffer.toString();
            return this;
        }

        public String toString() {
            switch (this.type) {
                case Tokenizer.EOF /*0*/:
                    return "<eof>";
                case Tokenizer.EOL /*1*/:
                    return "<eol>";
                case Tokenizer.WHITESPACE /*2*/:
                    return "<whitespace>";
                case Tokenizer.IDENTIFIER /*3*/:
                    return "<identifier: " + this.value + ">";
                case Tokenizer.QUOTED_STRING /*4*/:
                    return "<quoted_string: " + this.value + ">";
                case Tokenizer.COMMENT /*5*/:
                    return "<comment: " + this.value + ">";
                default:
                    return "<unknown>";
            }
        }

        public boolean isString() {
            return this.type == Tokenizer.IDENTIFIER || this.type == Tokenizer.QUOTED_STRING;
        }

        public boolean isEOL() {
            return this.type == Tokenizer.EOL || this.type == 0;
        }
    }

    static class TokenizerException extends TextParseException {
        String message;

        public TokenizerException(String str, int i, String str2) {
            super(str + ":" + i + ": " + str2);
            this.message = str2;
        }

        public String getBaseMessage() {
            return this.message;
        }
    }

    static {
        delim = " \t\n;()\"";
        quotes = "\"";
    }

    public Tokenizer(InputStream inputStream) {
        if (!(inputStream instanceof BufferedInputStream)) {
            inputStream = new BufferedInputStream(inputStream);
        }
        this.is = new PushbackInputStream(inputStream, WHITESPACE);
        this.ungottenToken = false;
        this.multiline = EOF;
        this.quoting = false;
        this.delimiters = delim;
        this.current = new Token();
        this.sb = new StringBuffer();
        this.filename = "<none>";
        this.line = EOL;
    }

    public Tokenizer(String str) {
        this(new ByteArrayInputStream(str.getBytes()));
    }

    public Tokenizer(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
        this.wantClose = true;
        this.filename = file.getName();
    }

    private int getChar() throws IOException {
        int read = this.is.read();
        if (read == 13) {
            read = this.is.read();
            if (read != 10) {
                this.is.unread(read);
            }
            read = 10;
        }
        if (read == 10) {
            this.line += EOL;
        }
        return read;
    }

    private void ungetChar(int i) throws IOException {
        if (i != -1) {
            this.is.unread(i);
            if (i == 10) {
                this.line--;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int skipWhitespace() throws java.io.IOException {
        /*
        r3 = this;
        r0 = 0;
    L_0x0001:
        r1 = r3.getChar();
        r2 = 32;
        if (r1 == r2) goto L_0x0019;
    L_0x0009:
        r2 = 9;
        if (r1 == r2) goto L_0x0019;
    L_0x000d:
        r2 = 10;
        if (r1 != r2) goto L_0x0015;
    L_0x0011:
        r2 = r3.multiline;
        if (r2 > 0) goto L_0x0019;
    L_0x0015:
        r3.ungetChar(r1);
        return r0;
    L_0x0019:
        r0 = r0 + 1;
        goto L_0x0001;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.Tokenizer.skipWhitespace():int");
    }

    private void checkUnbalancedParens() throws TextParseException {
        if (this.multiline > 0) {
            throw exception("unbalanced parentheses");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.xbill.DNS.Tokenizer.Token get(boolean r10, boolean r11) throws java.io.IOException {
        /*
        r9 = this;
        r1 = 4;
        r8 = 1;
        r7 = 0;
        r6 = -1;
        r5 = 0;
        r0 = r9.ungottenToken;
        if (r0 == 0) goto L_0x0032;
    L_0x0009:
        r9.ungottenToken = r5;
        r0 = r9.current;
        r0 = r0.type;
        r2 = 2;
        if (r0 != r2) goto L_0x0017;
    L_0x0012:
        if (r10 == 0) goto L_0x0032;
    L_0x0014:
        r0 = r9.current;
    L_0x0016:
        return r0;
    L_0x0017:
        r0 = r9.current;
        r0 = r0.type;
        r2 = 5;
        if (r0 != r2) goto L_0x0023;
    L_0x001e:
        if (r11 == 0) goto L_0x0032;
    L_0x0020:
        r0 = r9.current;
        goto L_0x0016;
    L_0x0023:
        r0 = r9.current;
        r0 = r0.type;
        if (r0 != r8) goto L_0x002f;
    L_0x0029:
        r0 = r9.line;
        r0 = r0 + 1;
        r9.line = r0;
    L_0x002f:
        r0 = r9.current;
        goto L_0x0016;
    L_0x0032:
        r0 = r9.skipWhitespace();
        if (r0 <= 0) goto L_0x0042;
    L_0x0038:
        if (r10 == 0) goto L_0x0042;
    L_0x003a:
        r0 = r9.current;
        r1 = 2;
        r0 = r0.set(r1, r7);
        goto L_0x0016;
    L_0x0042:
        r0 = 3;
        r2 = r9.sb;
        r2.setLength(r5);
    L_0x0048:
        r2 = r9.getChar();
        if (r2 == r6) goto L_0x0056;
    L_0x004e:
        r3 = r9.delimiters;
        r3 = r3.indexOf(r2);
        if (r3 == r6) goto L_0x013a;
    L_0x0056:
        if (r2 != r6) goto L_0x007b;
    L_0x0058:
        r1 = r9.quoting;
        if (r1 == 0) goto L_0x0063;
    L_0x005c:
        r0 = "EOF in quoted string";
        r0 = r9.exception(r0);
        throw r0;
    L_0x0063:
        r1 = r9.sb;
        r1 = r1.length();
        if (r1 != 0) goto L_0x0072;
    L_0x006b:
        r0 = r9.current;
        r0 = r0.set(r5, r7);
        goto L_0x0016;
    L_0x0072:
        r1 = r9.current;
        r2 = r9.sb;
        r0 = r1.set(r0, r2);
        goto L_0x0016;
    L_0x007b:
        r3 = r9.sb;
        r3 = r3.length();
        if (r3 != 0) goto L_0x0122;
    L_0x0083:
        if (r0 == r1) goto L_0x0122;
    L_0x0085:
        r3 = 40;
        if (r2 != r3) goto L_0x0093;
    L_0x0089:
        r2 = r9.multiline;
        r2 = r2 + 1;
        r9.multiline = r2;
        r9.skipWhitespace();
        goto L_0x0048;
    L_0x0093:
        r3 = 41;
        if (r2 != r3) goto L_0x00ac;
    L_0x0097:
        r2 = r9.multiline;
        if (r2 > 0) goto L_0x00a2;
    L_0x009b:
        r0 = "invalid close parenthesis";
        r0 = r9.exception(r0);
        throw r0;
    L_0x00a2:
        r2 = r9.multiline;
        r2 = r2 + -1;
        r9.multiline = r2;
        r9.skipWhitespace();
        goto L_0x0048;
    L_0x00ac:
        r3 = 34;
        if (r2 != r3) goto L_0x00c6;
    L_0x00b0:
        r2 = r9.quoting;
        if (r2 != 0) goto L_0x00bc;
    L_0x00b4:
        r9.quoting = r8;
        r0 = quotes;
        r9.delimiters = r0;
        r0 = r1;
        goto L_0x0048;
    L_0x00bc:
        r9.quoting = r5;
        r2 = delim;
        r9.delimiters = r2;
        r9.skipWhitespace();
        goto L_0x0048;
    L_0x00c6:
        r3 = 10;
        if (r2 != r3) goto L_0x00d2;
    L_0x00ca:
        r0 = r9.current;
        r0 = r0.set(r8, r7);
        goto L_0x0016;
    L_0x00d2:
        r3 = 59;
        if (r2 != r3) goto L_0x011c;
    L_0x00d6:
        r2 = r9.getChar();
        r3 = 10;
        if (r2 == r3) goto L_0x00e0;
    L_0x00de:
        if (r2 != r6) goto L_0x00f0;
    L_0x00e0:
        if (r11 == 0) goto L_0x00f7;
    L_0x00e2:
        r9.ungetChar(r2);
        r0 = r9.current;
        r1 = 5;
        r2 = r9.sb;
        r0 = r0.set(r1, r2);
        goto L_0x0016;
    L_0x00f0:
        r3 = r9.sb;
        r2 = (char) r2;
        r3.append(r2);
        goto L_0x00d6;
    L_0x00f7:
        if (r2 != r6) goto L_0x0106;
    L_0x00f9:
        if (r0 == r1) goto L_0x0106;
    L_0x00fb:
        r9.checkUnbalancedParens();
        r0 = r9.current;
        r0 = r0.set(r5, r7);
        goto L_0x0016;
    L_0x0106:
        r2 = r9.multiline;
        if (r2 <= 0) goto L_0x0114;
    L_0x010a:
        r9.skipWhitespace();
        r2 = r9.sb;
        r2.setLength(r5);
        goto L_0x0048;
    L_0x0114:
        r0 = r9.current;
        r0 = r0.set(r8, r7);
        goto L_0x0016;
    L_0x011c:
        r0 = new java.lang.IllegalStateException;
        r0.<init>();
        throw r0;
    L_0x0122:
        r9.ungetChar(r2);
        r2 = r9.sb;
        r2 = r2.length();
        if (r2 != 0) goto L_0x0169;
    L_0x012d:
        if (r0 == r1) goto L_0x0169;
    L_0x012f:
        r9.checkUnbalancedParens();
        r0 = r9.current;
        r0 = r0.set(r5, r7);
        goto L_0x0016;
    L_0x013a:
        r3 = 92;
        if (r2 != r3) goto L_0x015a;
    L_0x013e:
        r2 = r9.getChar();
        if (r2 != r6) goto L_0x014b;
    L_0x0144:
        r0 = "unterminated escape sequence";
        r0 = r9.exception(r0);
        throw r0;
    L_0x014b:
        r3 = r9.sb;
        r4 = 92;
        r3.append(r4);
    L_0x0152:
        r3 = r9.sb;
        r2 = (char) r2;
        r3.append(r2);
        goto L_0x0048;
    L_0x015a:
        r3 = r9.quoting;
        if (r3 == 0) goto L_0x0152;
    L_0x015e:
        r3 = 10;
        if (r2 != r3) goto L_0x0152;
    L_0x0162:
        r0 = "newline in quoted string";
        r0 = r9.exception(r0);
        throw r0;
    L_0x0169:
        r1 = r9.current;
        r2 = r9.sb;
        r0 = r1.set(r0, r2);
        goto L_0x0016;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.Tokenizer.get(boolean, boolean):org.xbill.DNS.Tokenizer$Token");
    }

    public Token get() throws IOException {
        return get(false, false);
    }

    public void unget() {
        if (this.ungottenToken) {
            throw new IllegalStateException("Cannot unget multiple tokens");
        }
        if (this.current.type == EOL) {
            this.line--;
        }
        this.ungottenToken = true;
    }

    public String getString() throws IOException {
        Token token = get();
        if (token.isString()) {
            return token.value;
        }
        throw exception("expected a string");
    }

    private String _getIdentifier(String str) throws IOException {
        Token token = get();
        if (token.type == IDENTIFIER) {
            return token.value;
        }
        throw exception("expected " + str);
    }

    public String getIdentifier() throws IOException {
        return _getIdentifier("an identifier");
    }

    public long getLong() throws IOException {
        String _getIdentifier = _getIdentifier("an integer");
        if (Character.isDigit(_getIdentifier.charAt(EOF))) {
            try {
                return Long.parseLong(_getIdentifier);
            } catch (NumberFormatException e) {
                throw exception("expected an integer");
            }
        }
        throw exception("expected an integer");
    }

    public long getUInt32() throws IOException {
        long j = getLong();
        if (j >= 0 && j <= Util.MAX_32BIT_VALUE) {
            return j;
        }
        throw exception("expected an 32 bit unsigned integer");
    }

    public int getUInt16() throws IOException {
        long j = getLong();
        if (j >= 0 && j <= 65535) {
            return (int) j;
        }
        throw exception("expected an 16 bit unsigned integer");
    }

    public int getUInt8() throws IOException {
        long j = getLong();
        if (j >= 0 && j <= 255) {
            return (int) j;
        }
        throw exception("expected an 8 bit unsigned integer");
    }

    public long getTTL() throws IOException {
        try {
            return TTL.parseTTL(_getIdentifier("a TTL value"));
        } catch (NumberFormatException e) {
            throw exception("expected a TTL value");
        }
    }

    public long getTTLLike() throws IOException {
        try {
            return TTL.parse(_getIdentifier("a TTL-like value"), false);
        } catch (NumberFormatException e) {
            throw exception("expected a TTL-like value");
        }
    }

    public Name getName(Name name) throws IOException {
        try {
            Name fromString = Name.fromString(_getIdentifier("a name"), name);
            if (fromString.isAbsolute()) {
                return fromString;
            }
            throw new RelativeNameException(fromString);
        } catch (TextParseException e) {
            throw exception(e.getMessage());
        }
    }

    public InetAddress getAddress(int i) throws IOException {
        try {
            return Address.getByAddress(_getIdentifier("an address"), i);
        } catch (UnknownHostException e) {
            throw exception(e.getMessage());
        }
    }

    public void getEOL() throws IOException {
        Token token = get();
        if (token.type != EOL && token.type != 0) {
            throw exception("expected EOL or EOF");
        }
    }

    private String remainingStrings() throws IOException {
        StringBuffer stringBuffer = null;
        while (true) {
            Token token = get();
            if (!token.isString()) {
                break;
            }
            if (stringBuffer == null) {
                stringBuffer = new StringBuffer();
            }
            stringBuffer.append(token.value);
        }
        unget();
        if (stringBuffer == null) {
            return null;
        }
        return stringBuffer.toString();
    }

    public byte[] getBase64(boolean z) throws IOException {
        String remainingStrings = remainingStrings();
        if (remainingStrings != null) {
            byte[] fromString = base64.fromString(remainingStrings);
            if (fromString != null) {
                return fromString;
            }
            throw exception("invalid base64 encoding");
        } else if (!z) {
            return null;
        } else {
            throw exception("expected base64 encoded string");
        }
    }

    public byte[] getBase64() throws IOException {
        return getBase64(false);
    }

    public byte[] getHex(boolean z) throws IOException {
        String remainingStrings = remainingStrings();
        if (remainingStrings != null) {
            byte[] fromString = base16.fromString(remainingStrings);
            if (fromString != null) {
                return fromString;
            }
            throw exception("invalid hex encoding");
        } else if (!z) {
            return null;
        } else {
            throw exception("expected hex encoded string");
        }
    }

    public byte[] getHex() throws IOException {
        return getHex(false);
    }

    public byte[] getHexString() throws IOException {
        byte[] fromString = base16.fromString(_getIdentifier("a hex string"));
        if (fromString != null) {
            return fromString;
        }
        throw exception("invalid hex encoding");
    }

    public byte[] getBase32String(base32 org_xbill_DNS_utils_base32) throws IOException {
        byte[] fromString = org_xbill_DNS_utils_base32.fromString(_getIdentifier("a base32 string"));
        if (fromString != null) {
            return fromString;
        }
        throw exception("invalid base32 encoding");
    }

    public TextParseException exception(String str) {
        return new TokenizerException(this.filename, this.line, str);
    }

    public void close() {
        if (this.wantClose) {
            try {
                this.is.close();
            } catch (IOException e) {
            }
        }
    }

    protected void finalize() {
        close();
    }
}
