package com.hp.hpl.sparta.xpath;

import java.io.IOException;
import java.io.Reader;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class SimpleStreamTokenizer {
    private static final int QUOTE = -6;
    public static final int TT_EOF = -1;
    public static final int TT_NUMBER = -2;
    public static final int TT_WORD = -3;
    private static final int WHITESPACE = -5;
    private final StringBuffer buf_;
    private final int[] charType_;
    private char inQuote_;
    private int nextType_;
    public int nval;
    private boolean pushedBack_;
    private final Reader reader_;
    public String sval;
    public int ttype;

    public SimpleStreamTokenizer(Reader reader) throws IOException {
        int i = 0;
        this.ttype = Integer.MIN_VALUE;
        this.nval = Integer.MIN_VALUE;
        this.sval = XmlPullParser.NO_NAMESPACE;
        this.buf_ = new StringBuffer();
        this.charType_ = new int[KEYRecord.OWNER_ZONE];
        this.pushedBack_ = false;
        this.inQuote_ = '\u0000';
        this.reader_ = reader;
        while (i < this.charType_.length) {
            if ((65 <= i && i <= 90) || ((97 <= i && i <= Opcodes.ISHR) || i == 45)) {
                this.charType_[i] = TT_WORD;
            } else if (48 <= i && i <= 57) {
                this.charType_[i] = TT_NUMBER;
            } else if (i < 0 || i > 32) {
                this.charType_[i] = i;
            } else {
                this.charType_[i] = WHITESPACE;
            }
            i = (char) (i + 1);
        }
        nextToken();
    }

    public int nextToken() throws IOException {
        if (this.pushedBack_) {
            this.pushedBack_ = false;
            return this.ttype;
        }
        this.ttype = this.nextType_;
        boolean z;
        do {
            char c;
            int i;
            boolean z2 = false;
            int i2;
            do {
                int read = this.reader_.read();
                if (read != TT_EOF) {
                    c = this.charType_[read];
                } else if (this.inQuote_ != '\u0000') {
                    throw new IOException("Unterminated quote");
                } else {
                    c = TT_EOF;
                }
                if (this.inQuote_ == '\u0000' && c == WHITESPACE) {
                    i2 = 1;
                } else {
                    z = false;
                }
                if (z2 || i2 != 0) {
                    z2 = true;
                    continue;
                } else {
                    z2 = false;
                    continue;
                }
            } while (i2 != 0);
            if (c == '\'' || c == '\"') {
                if (this.inQuote_ == '\u0000') {
                    this.inQuote_ = (char) c;
                } else if (this.inQuote_ == c) {
                    this.inQuote_ = '\u0000';
                }
            }
            if (this.inQuote_ != '\u0000') {
                i = this.inQuote_;
            }
            z = z2 || !((this.ttype < TT_EOF || this.ttype == 39 || this.ttype == 34) && this.ttype == i);
            if (z) {
                switch (this.ttype) {
                    case TT_WORD /*-3*/:
                        this.sval = this.buf_.toString();
                        this.buf_.setLength(0);
                        break;
                    case TT_NUMBER /*-2*/:
                        this.nval = Integer.parseInt(this.buf_.toString());
                        this.buf_.setLength(0);
                        break;
                    case Type.ATMA /*34*/:
                    case Service.RLP /*39*/:
                        this.sval = this.buf_.toString().substring(1, this.buf_.length() + TT_EOF);
                        this.buf_.setLength(0);
                        break;
                }
                if (i != WHITESPACE) {
                    this.nextType_ = i == QUOTE ? read : i;
                }
            }
            switch (i) {
                case TT_WORD /*-3*/:
                case TT_NUMBER /*-2*/:
                case Type.ATMA /*34*/:
                case Service.RLP /*39*/:
                    this.buf_.append((char) read);
                    continue;
                default:
                    break;
            }
        } while (!z);
        return this.ttype;
    }

    public void ordinaryChar(char c) {
        this.charType_[c] = c;
    }

    public void pushBack() {
        this.pushedBack_ = true;
    }

    public String toString() {
        switch (this.ttype) {
            case TT_WORD /*-3*/:
            case Type.ATMA /*34*/:
                return new StringBuffer().append("\"").append(this.sval).append("\"").toString();
            case TT_NUMBER /*-2*/:
                return Integer.toString(this.nval);
            case TT_EOF /*-1*/:
                return "(EOF)";
            case Service.RLP /*39*/:
                return new StringBuffer().append("'").append(this.sval).append("'").toString();
            default:
                return new StringBuffer().append("'").append((char) this.ttype).append("'").toString();
        }
    }

    public void wordChars(char c, char c2) {
        while (c <= c2) {
            this.charType_[c] = TT_WORD;
            c = (char) (c + 1);
        }
    }
}
