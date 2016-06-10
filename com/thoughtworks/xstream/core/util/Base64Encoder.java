package com.thoughtworks.xstream.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.WKSRecord.Service;

public class Base64Encoder {
    private static final int[] REVERSE_MAPPING;
    private static final char[] SIXTY_FOUR_CHARS;

    static {
        SIXTY_FOUR_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
        REVERSE_MAPPING = new int[Service.NTP];
        for (int i = 0; i < SIXTY_FOUR_CHARS.length; i++) {
            REVERSE_MAPPING[SIXTY_FOUR_CHARS[i]] = i + 1;
        }
    }

    public String encode(byte[] input) {
        StringBuffer result = new StringBuffer();
        int outputCharCount = 0;
        for (int i = 0; i < input.length; i += 3) {
            int remaining = Math.min(3, input.length - i);
            int oneBigNumber = (((input[i] & KEYRecord.PROTOCOL_ANY) << 16) | ((remaining <= 1 ? 0 : input[i + 1] & KEYRecord.PROTOCOL_ANY) << 8)) | (remaining <= 2 ? 0 : input[i + 2] & KEYRecord.PROTOCOL_ANY);
            int j = 0;
            while (j < 4) {
                result.append(remaining + 1 > j ? SIXTY_FOUR_CHARS[(oneBigNumber >> ((3 - j) * 6)) & 63] : SignatureVisitor.INSTANCEOF);
                j++;
            }
            outputCharCount += 4;
            if (outputCharCount % 76 == 0) {
                result.append('\n');
            }
        }
        return result.toString();
    }

    public byte[] decode(String input) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            StringReader in = new StringReader(input);
            for (int i = 0; i < input.length(); i += 4) {
                int[] a = new int[]{mapCharToInt(in), mapCharToInt(in), mapCharToInt(in), mapCharToInt(in)};
                int oneBigNumber = ((((a[0] & 63) << 18) | ((a[1] & 63) << 12)) | ((a[2] & 63) << 6)) | (a[3] & 63);
                for (int j = 0; j < 3; j++) {
                    if (a[j + 1] >= 0) {
                        out.write((oneBigNumber >> ((2 - j) * 8)) & KEYRecord.PROTOCOL_ANY);
                    }
                }
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new Error(e + ": " + e.getMessage());
        }
    }

    private int mapCharToInt(Reader input) throws IOException {
        int c;
        do {
            c = input.read();
            if (c == -1) {
                return -1;
            }
            int result = REVERSE_MAPPING[c];
            if (result != 0) {
                return result - 1;
            }
        } while (c != 61);
        return -1;
    }
}
