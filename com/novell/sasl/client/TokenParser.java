package com.novell.sasl.client;

import org.apache.harmony.javax.security.sasl.SaslException;

class TokenParser {
    private static final int STATE_DONE = 6;
    private static final int STATE_LOOKING_FOR_COMMA = 4;
    private static final int STATE_LOOKING_FOR_FIRST_TOKEN = 1;
    private static final int STATE_LOOKING_FOR_TOKEN = 2;
    private static final int STATE_PARSING_ERROR = 5;
    private static final int STATE_SCANNING_TOKEN = 3;
    private int m_curPos;
    private int m_scanStart;
    private int m_state;
    private String m_tokens;

    TokenParser(String str) {
        this.m_tokens = str;
        this.m_curPos = 0;
        this.m_scanStart = 0;
        this.m_state = STATE_LOOKING_FOR_FIRST_TOKEN;
    }

    String parseToken() throws SaslException {
        String str = null;
        if (this.m_state == STATE_DONE) {
            return null;
        }
        while (this.m_curPos < this.m_tokens.length() && str == null) {
            char charAt = this.m_tokens.charAt(this.m_curPos);
            switch (this.m_state) {
                case STATE_LOOKING_FOR_FIRST_TOKEN /*1*/:
                case STATE_LOOKING_FOR_TOKEN /*2*/:
                    if (isWhiteSpace(charAt)) {
                        continue;
                    } else if (isValidTokenChar(charAt)) {
                        this.m_scanStart = this.m_curPos;
                        this.m_state = STATE_SCANNING_TOKEN;
                        break;
                    } else {
                        this.m_state = STATE_PARSING_ERROR;
                        throw new SaslException("Invalid token character at position " + this.m_curPos);
                    }
                case STATE_SCANNING_TOKEN /*3*/:
                    if (isValidTokenChar(charAt)) {
                        continue;
                    } else if (isWhiteSpace(charAt)) {
                        str = this.m_tokens.substring(this.m_scanStart, this.m_curPos);
                        this.m_state = STATE_LOOKING_FOR_COMMA;
                        break;
                    } else if (',' == charAt) {
                        str = this.m_tokens.substring(this.m_scanStart, this.m_curPos);
                        this.m_state = STATE_LOOKING_FOR_TOKEN;
                        break;
                    } else {
                        this.m_state = STATE_PARSING_ERROR;
                        throw new SaslException("Invalid token character at position " + this.m_curPos);
                    }
                case STATE_LOOKING_FOR_COMMA /*4*/:
                    if (isWhiteSpace(charAt)) {
                        continue;
                    } else if (charAt == ',') {
                        this.m_state = STATE_LOOKING_FOR_TOKEN;
                        break;
                    } else {
                        this.m_state = STATE_PARSING_ERROR;
                        throw new SaslException("Expected a comma, found '" + charAt + "' at postion " + this.m_curPos);
                    }
                default:
                    break;
            }
            this.m_curPos += STATE_LOOKING_FOR_FIRST_TOKEN;
        }
        if (str != null) {
            return str;
        }
        switch (this.m_state) {
            case STATE_LOOKING_FOR_FIRST_TOKEN /*1*/:
            case STATE_LOOKING_FOR_COMMA /*4*/:
                return str;
            case STATE_LOOKING_FOR_TOKEN /*2*/:
                throw new SaslException("Trialing comma");
            case STATE_SCANNING_TOKEN /*3*/:
                str = this.m_tokens.substring(this.m_scanStart);
                this.m_state = STATE_DONE;
                return str;
            default:
                return str;
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
}
