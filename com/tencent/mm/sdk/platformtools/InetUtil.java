package com.tencent.mm.sdk.platformtools;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public final class InetUtil {
    private static final Pattern f1631A;
    private static final Pattern f1632y;
    private static final Pattern f1633z;

    static {
        f1632y = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
        f1633z = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
        f1631A = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
    }

    private InetUtil() {
    }

    public static InetAddress getByDottedAddress(String str) {
        if (isIPv4Address(str)) {
            return InetAddress.getByName(str);
        }
        if (isIPv6Address(str)) {
            return InetAddress.getByName(str);
        }
        throw new UnknownHostException("invalid ipv4 or ipv6 dotted string");
    }

    public static boolean isIPv4Address(String str) {
        return f1632y.matcher(str).matches();
    }

    public static boolean isIPv6Address(String str) {
        return isIPv6StdAddress(str) || isIPv6HexCompressedAddress(str);
    }

    public static boolean isIPv6HexCompressedAddress(String str) {
        return f1631A.matcher(str).matches();
    }

    public static boolean isIPv6StdAddress(String str) {
        return f1633z.matcher(str).matches();
    }
}
