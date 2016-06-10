package org.codehaus.jackson.util;

import java.util.Arrays;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;
import org.ksoap2.SoapEnvelope;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public final class CharTypes {
    private static final byte[] HEX_BYTES;
    private static final char[] HEX_CHARS;
    static final int[] sHexValues;
    static final int[] sInputCodes;
    static final int[] sInputCodesComment;
    static final int[] sInputCodesJsNames;
    static final int[] sInputCodesUtf8;
    static final int[] sInputCodesUtf8JsNames;
    static final int[] sOutputEscapes;

    static {
        int i;
        HEX_CHARS = "0123456789ABCDEF".toCharArray();
        int len = HEX_CHARS.length;
        HEX_BYTES = new byte[len];
        for (i = 0; i < len; i++) {
            HEX_BYTES[i] = (byte) HEX_CHARS[i];
        }
        int[] table = new int[KEYRecord.OWNER_ZONE];
        for (i = 0; i < 32; i++) {
            table[i] = -1;
        }
        table[34] = 1;
        table[92] = 1;
        sInputCodes = table;
        table = new int[sInputCodes.length];
        System.arraycopy(sInputCodes, 0, table, 0, sInputCodes.length);
        for (int c = Flags.FLAG8; c < KEYRecord.OWNER_ZONE; c++) {
            int code;
            if ((c & SmileConstants.TOKEN_PREFIX_MISC_OTHER) == Wbxml.EXT_0) {
                code = 2;
            } else if ((c & 240) == SmileConstants.TOKEN_PREFIX_MISC_OTHER) {
                code = 3;
            } else if ((c & 248) == 240) {
                code = 4;
            } else {
                code = -1;
            }
            table[c] = code;
        }
        sInputCodesUtf8 = table;
        table = new int[KEYRecord.OWNER_ZONE];
        Arrays.fill(table, -1);
        for (i = 33; i < KEYRecord.OWNER_ZONE; i++) {
            if (Character.isJavaIdentifierPart((char) i)) {
                table[i] = 0;
            }
        }
        table[64] = 0;
        table[35] = 0;
        table[42] = 0;
        table[45] = 0;
        table[43] = 0;
        sInputCodesJsNames = table;
        table = new int[KEYRecord.OWNER_ZONE];
        System.arraycopy(sInputCodesJsNames, 0, table, 0, sInputCodesJsNames.length);
        Arrays.fill(table, Flags.FLAG8, Flags.FLAG8, 0);
        sInputCodesUtf8JsNames = table;
        sInputCodesComment = new int[KEYRecord.OWNER_ZONE];
        System.arraycopy(sInputCodesUtf8, Flags.FLAG8, sInputCodesComment, Flags.FLAG8, Flags.FLAG8);
        Arrays.fill(sInputCodesComment, 0, 32, -1);
        sInputCodesComment[9] = 0;
        sInputCodesComment[10] = 10;
        sInputCodesComment[13] = 13;
        sInputCodesComment[42] = 42;
        table = new int[KEYRecord.OWNER_ZONE];
        for (i = 0; i < 32; i++) {
            table[i] = -(i + 1);
        }
        table[34] = 34;
        table[92] = 92;
        table[8] = 98;
        table[9] = Opcodes.INEG;
        table[12] = Service.ISO_TSAP;
        table[10] = SoapEnvelope.VER11;
        table[13] = Opcodes.FREM;
        sOutputEscapes = table;
        sHexValues = new int[Flags.FLAG8];
        Arrays.fill(sHexValues, -1);
        for (i = 0; i < 10; i++) {
            sHexValues[i + 48] = i;
        }
        for (i = 0; i < 6; i++) {
            sHexValues[i + 97] = i + 10;
            sHexValues[i + 65] = i + 10;
        }
    }

    public static final int[] getInputCodeLatin1() {
        return sInputCodes;
    }

    public static final int[] getInputCodeUtf8() {
        return sInputCodesUtf8;
    }

    public static final int[] getInputCodeLatin1JsNames() {
        return sInputCodesJsNames;
    }

    public static final int[] getInputCodeUtf8JsNames() {
        return sInputCodesUtf8JsNames;
    }

    public static final int[] getInputCodeComment() {
        return sInputCodesComment;
    }

    public static final int[] getOutputEscapes() {
        return sOutputEscapes;
    }

    public static int charToHex(int ch) {
        return ch > Service.LOCUS_CON ? -1 : sHexValues[ch];
    }

    public static void appendQuoted(StringBuilder sb, String content) {
        int[] escCodes = sOutputEscapes;
        char escLen = escCodes.length;
        int len = content.length();
        for (int i = 0; i < len; i++) {
            char c = content.charAt(i);
            if (c >= escLen || escCodes[c] == 0) {
                sb.append(c);
            } else {
                sb.append('\\');
                int escCode = escCodes[c];
                if (escCode < 0) {
                    sb.append('u');
                    sb.append('0');
                    sb.append('0');
                    int value = -(escCode + 1);
                    sb.append(HEX_CHARS[value >> 4]);
                    sb.append(HEX_CHARS[value & 15]);
                } else {
                    sb.append((char) escCode);
                }
            }
        }
    }

    public static char[] copyHexChars() {
        return (char[]) HEX_CHARS.clone();
    }

    public static byte[] copyHexBytes() {
        return (byte[]) HEX_BYTES.clone();
    }
}
