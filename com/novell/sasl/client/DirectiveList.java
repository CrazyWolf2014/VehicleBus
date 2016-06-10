package com.novell.sasl.client;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.harmony.javax.security.sasl.SaslException;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

class DirectiveList {
    private static final int STATE_LOOKING_FOR_COMMA = 6;
    private static final int STATE_LOOKING_FOR_DIRECTIVE = 2;
    private static final int STATE_LOOKING_FOR_EQUALS = 4;
    private static final int STATE_LOOKING_FOR_FIRST_DIRECTIVE = 1;
    private static final int STATE_LOOKING_FOR_VALUE = 5;
    private static final int STATE_NO_UTF8_SUPPORT = 9;
    private static final int STATE_SCANNING_NAME = 3;
    private static final int STATE_SCANNING_QUOTED_STRING_VALUE = 7;
    private static final int STATE_SCANNING_TOKEN_VALUE = 8;
    private String m_curName;
    private int m_curPos;
    private ArrayList m_directiveList;
    private String m_directives;
    private int m_errorPos;
    private int m_scanStart;
    private int m_state;

    DirectiveList(byte[] bArr) {
        this.m_curPos = 0;
        this.m_state = STATE_LOOKING_FOR_FIRST_DIRECTIVE;
        this.m_directiveList = new ArrayList(10);
        this.m_scanStart = 0;
        this.m_errorPos = -1;
        try {
            this.m_directives = new String(bArr, AsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            this.m_state = STATE_NO_UTF8_SUPPORT;
        }
    }

    void parseDirectives() throws SaslException {
        String str = "<no name>";
        if (this.m_state == STATE_NO_UTF8_SUPPORT) {
            throw new SaslException("No UTF-8 support on platform");
        }
        boolean z = false;
        boolean z2 = false;
        while (this.m_curPos < this.m_directives.length()) {
            boolean charAt = this.m_directives.charAt(this.m_curPos);
            switch (this.m_state) {
                case STATE_LOOKING_FOR_FIRST_DIRECTIVE /*1*/:
                case STATE_LOOKING_FOR_DIRECTIVE /*2*/:
                    if (isWhiteSpace(charAt)) {
                        continue;
                    } else if (isValidTokenChar(charAt)) {
                        this.m_scanStart = this.m_curPos;
                        this.m_state = STATE_SCANNING_NAME;
                        break;
                    } else {
                        this.m_errorPos = this.m_curPos;
                        throw new SaslException("Parse error: Invalid name character");
                    }
                case STATE_SCANNING_NAME /*3*/:
                    if (isValidTokenChar(charAt)) {
                        continue;
                    } else if (isWhiteSpace(charAt)) {
                        str = this.m_directives.substring(this.m_scanStart, this.m_curPos);
                        this.m_state = STATE_LOOKING_FOR_EQUALS;
                        break;
                    } else if (SignatureVisitor.INSTANCEOF == charAt) {
                        str = this.m_directives.substring(this.m_scanStart, this.m_curPos);
                        this.m_state = STATE_LOOKING_FOR_VALUE;
                        break;
                    } else {
                        this.m_errorPos = this.m_curPos;
                        throw new SaslException("Parse error: Invalid name character");
                    }
                case STATE_LOOKING_FOR_EQUALS /*4*/:
                    if (isWhiteSpace(charAt)) {
                        continue;
                    } else if (SignatureVisitor.INSTANCEOF == charAt) {
                        this.m_state = STATE_LOOKING_FOR_VALUE;
                        break;
                    } else {
                        this.m_errorPos = this.m_curPos;
                        throw new SaslException("Parse error: Expected equals sign '='.");
                    }
                case STATE_LOOKING_FOR_VALUE /*5*/:
                    if (isWhiteSpace(charAt)) {
                        continue;
                    } else if (true == charAt) {
                        this.m_scanStart = this.m_curPos + STATE_LOOKING_FOR_FIRST_DIRECTIVE;
                        this.m_state = STATE_SCANNING_QUOTED_STRING_VALUE;
                        break;
                    } else if (isValidTokenChar(charAt)) {
                        this.m_scanStart = this.m_curPos;
                        this.m_state = STATE_SCANNING_TOKEN_VALUE;
                        break;
                    } else {
                        this.m_errorPos = this.m_curPos;
                        throw new SaslException("Parse error: Unexpected character");
                    }
                case STATE_LOOKING_FOR_COMMA /*6*/:
                    if (isWhiteSpace(charAt)) {
                        continue;
                    } else if (charAt) {
                        this.m_state = STATE_LOOKING_FOR_DIRECTIVE;
                        break;
                    } else {
                        this.m_errorPos = this.m_curPos;
                        throw new SaslException("Parse error: Expected a comma.");
                    }
                case STATE_SCANNING_QUOTED_STRING_VALUE /*7*/:
                    if (true == charAt) {
                        z = true;
                    }
                    if (true == charAt && true != r3) {
                        addDirective(str, z);
                        this.m_state = STATE_LOOKING_FOR_COMMA;
                        z = false;
                        break;
                    }
                case STATE_SCANNING_TOKEN_VALUE /*8*/:
                    if (isValidTokenChar(charAt)) {
                        continue;
                    } else if (isWhiteSpace(charAt)) {
                        addDirective(str, false);
                        this.m_state = STATE_LOOKING_FOR_COMMA;
                        break;
                    } else if (true == charAt) {
                        addDirective(str, false);
                        this.m_state = STATE_LOOKING_FOR_DIRECTIVE;
                        break;
                    } else {
                        this.m_errorPos = this.m_curPos;
                        throw new SaslException("Parse error: Invalid value character");
                    }
                default:
                    break;
            }
            this.m_curPos += STATE_LOOKING_FOR_FIRST_DIRECTIVE;
            z2 = charAt;
        }
        switch (this.m_state) {
            case STATE_LOOKING_FOR_DIRECTIVE /*2*/:
                throw new SaslException("Parse error: Trailing comma.");
            case STATE_SCANNING_NAME /*3*/:
            case STATE_LOOKING_FOR_EQUALS /*4*/:
            case STATE_LOOKING_FOR_VALUE /*5*/:
                throw new SaslException("Parse error: Missing value.");
            case STATE_SCANNING_QUOTED_STRING_VALUE /*7*/:
                throw new SaslException("Parse error: Missing closing quote.");
            case STATE_SCANNING_TOKEN_VALUE /*8*/:
                addDirective(str, false);
            default:
        }
    }

    boolean isValidTokenChar(char c) {
        if ((c < '\u0000' || c > ' ') && ((c < ':' || c > '@') && ((c < '[' || c > ']') && ',' != c && '%' != c && '(' != c && ')' != c && '{' != c && '}' != c && '\u007f' != c))) {
            return true;
        }
        return false;
    }

    boolean isWhiteSpace(char c) {
        if ('\t' == c || '\n' == c || '\r' == c || ' ' == c) {
            return true;
        }
        return false;
    }

    void addDirective(String str, boolean z) {
        int i;
        String str2;
        if (z) {
            StringBuffer stringBuffer = new StringBuffer(this.m_curPos - this.m_scanStart);
            int i2 = 0;
            i = this.m_scanStart;
            while (i < this.m_curPos) {
                if ('\\' == this.m_directives.charAt(i)) {
                    i += STATE_LOOKING_FOR_FIRST_DIRECTIVE;
                }
                stringBuffer.setCharAt(i2, this.m_directives.charAt(i));
                i2 += STATE_LOOKING_FOR_FIRST_DIRECTIVE;
                i += STATE_LOOKING_FOR_FIRST_DIRECTIVE;
            }
            str2 = new String(stringBuffer);
        } else {
            str2 = this.m_directives.substring(this.m_scanStart, this.m_curPos);
        }
        if (this.m_state == STATE_SCANNING_QUOTED_STRING_VALUE) {
            i = STATE_LOOKING_FOR_FIRST_DIRECTIVE;
        } else {
            i = STATE_LOOKING_FOR_DIRECTIVE;
        }
        this.m_directiveList.add(new ParsedDirective(str, str2, i));
    }

    Iterator getIterator() {
        return this.m_directiveList.iterator();
    }
}
