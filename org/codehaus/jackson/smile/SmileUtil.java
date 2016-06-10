package org.codehaus.jackson.smile;

public class SmileUtil {
    public static int zigzagEncode(int input) {
        if (input < 0) {
            return (input << 1) ^ -1;
        }
        return input << 1;
    }

    public static int zigzagDecode(int encoded) {
        if ((encoded & 1) == 0) {
            return encoded >>> 1;
        }
        return (encoded >>> 1) ^ -1;
    }

    public static long zigzagEncode(long input) {
        if (input < 0) {
            return (input << 1) ^ -1;
        }
        return input << 1;
    }

    public static long zigzagDecode(long encoded) {
        if ((1 & encoded) == 0) {
            return encoded >>> 1;
        }
        return (encoded >>> 1) ^ -1;
    }
}
