package org.json;

import com.cnmobi.im.view.RecordButton;
import java.util.HashMap;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public class XMLTokener extends JSONTokener {
    public static final HashMap entity;

    static {
        entity = new HashMap(8);
        entity.put("amp", XML.AMP);
        entity.put("apos", XML.APOS);
        entity.put("gt", XML.GT);
        entity.put("lt", XML.LT);
        entity.put("quot", XML.QUOT);
    }

    public XMLTokener(String s) {
        super(s);
    }

    public String nextCDATA() throws JSONException {
        StringBuffer sb = new StringBuffer();
        while (true) {
            char c = next();
            if (c == '\u0000') {
                break;
            }
            sb.append(c);
            int i = sb.length() - 3;
            if (i >= 0 && sb.charAt(i) == ']' && sb.charAt(i + 1) == ']' && sb.charAt(i + 2) == '>') {
                sb.setLength(i);
                return sb.toString();
            }
        }
        throw syntaxError("Unclosed CDATA.");
    }

    public Object nextContent() throws JSONException {
        char c;
        do {
            c = next();
        } while (Character.isWhitespace(c));
        if (c == '\u0000') {
            return null;
        }
        if (c == '<') {
            return XML.LT;
        }
        StringBuffer sb = new StringBuffer();
        while (c != '<' && c != '\u0000') {
            if (c == '&') {
                sb.append(nextEntity(c));
            } else {
                sb.append(c);
            }
            c = next();
        }
        back();
        return sb.toString().trim();
    }

    public Object nextEntity(char a) throws JSONException {
        StringBuffer sb = new StringBuffer();
        while (true) {
            char c = next();
            if (!Character.isLetterOrDigit(c) && c != '#') {
                break;
            }
            sb.append(Character.toLowerCase(c));
        }
        if (c == ';') {
            String s = sb.toString();
            Object e = entity.get(s);
            return e != null ? e : new StringBuilder(String.valueOf(a)).append(s).append(";").toString();
        } else {
            throw syntaxError("Missing ';' in XML entity: &" + sb);
        }
    }

    public Object nextMeta() throws JSONException {
        char c;
        do {
            c = next();
        } while (Character.isWhitespace(c));
        switch (c) {
            case KEYRecord.OWNER_USER /*0*/:
                throw syntaxError("Misshaped meta tag.");
            case Service.DSP /*33*/:
                return XML.BANG;
            case Type.ATMA /*34*/:
            case Service.RLP /*39*/:
                char q = c;
                do {
                    c = next();
                    if (c == '\u0000') {
                        throw syntaxError("Unterminated string.");
                    }
                } while (c != q);
                return Boolean.TRUE;
            case Service.NI_FTP /*47*/:
                return XML.SLASH;
            case RecordButton.MAX_TIME /*60*/:
                return XML.LT;
            case Service.NI_MAIL /*61*/:
                return XML.EQ;
            case Protocol.CFTP /*62*/:
                return XML.GT;
            case Service.VIA_FTP /*63*/:
                return XML.QUEST;
        }
        while (true) {
            c = next();
            if (Character.isWhitespace(c)) {
                return Boolean.TRUE;
            }
            switch (c) {
                case KEYRecord.OWNER_USER /*0*/:
                case Service.DSP /*33*/:
                case Type.ATMA /*34*/:
                case Service.RLP /*39*/:
                case Service.NI_FTP /*47*/:
                case RecordButton.MAX_TIME /*60*/:
                case Service.NI_MAIL /*61*/:
                case Protocol.CFTP /*62*/:
                case Service.VIA_FTP /*63*/:
                    back();
                    return Boolean.TRUE;
                default:
            }
        }
    }

    public Object nextToken() throws JSONException {
        char c;
        do {
            c = next();
        } while (Character.isWhitespace(c));
        StringBuffer sb;
        switch (c) {
            case KEYRecord.OWNER_USER /*0*/:
                throw syntaxError("Misshaped element.");
            case Service.DSP /*33*/:
                return XML.BANG;
            case Type.ATMA /*34*/:
            case Service.RLP /*39*/:
                char q = c;
                sb = new StringBuffer();
                while (true) {
                    c = next();
                    if (c == '\u0000') {
                        throw syntaxError("Unterminated string.");
                    } else if (c == q) {
                        return sb.toString();
                    } else {
                        if (c == '&') {
                            sb.append(nextEntity(c));
                        } else {
                            sb.append(c);
                        }
                    }
                }
            case Service.NI_FTP /*47*/:
                return XML.SLASH;
            case RecordButton.MAX_TIME /*60*/:
                throw syntaxError("Misplaced '<'.");
            case Service.NI_MAIL /*61*/:
                return XML.EQ;
            case Protocol.CFTP /*62*/:
                return XML.GT;
            case Service.VIA_FTP /*63*/:
                return XML.QUEST;
            default:
                sb = new StringBuffer();
                while (true) {
                    sb.append(c);
                    c = next();
                    if (Character.isWhitespace(c)) {
                        return sb.toString();
                    }
                    switch (c) {
                        case KEYRecord.OWNER_USER /*0*/:
                        case Service.DSP /*33*/:
                        case Service.NI_FTP /*47*/:
                        case Service.NI_MAIL /*61*/:
                        case Protocol.CFTP /*62*/:
                        case Service.VIA_FTP /*63*/:
                        case Service.MIT_DOV /*91*/:
                        case Service.DCP /*93*/:
                            back();
                            return sb.toString();
                        case Type.ATMA /*34*/:
                        case Service.RLP /*39*/:
                        case RecordButton.MAX_TIME /*60*/:
                            throw syntaxError("Bad character in a name.");
                        default:
                    }
                }
        }
    }
}
