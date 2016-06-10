package org.xbill.DNS;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;

public final class Address {
    public static final int IPv4 = 1;
    public static final int IPv6 = 2;

    private Address() {
    }

    private static byte[] parseV4(String str) {
        byte[] bArr = new byte[4];
        int length = str.length();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i < length) {
            int i5;
            char charAt = str.charAt(i);
            if (charAt < '0' || charAt > '9') {
                if (charAt != '.') {
                    return null;
                }
                if (i3 == 3) {
                    return null;
                }
                if (i4 == 0) {
                    return null;
                }
                i5 = i3 + IPv4;
                bArr[i3] = (byte) i2;
                i3 = i5;
                i4 = 0;
                i5 = 0;
            } else if (i4 == 3) {
                return null;
            } else {
                if (i4 > 0 && i2 == 0) {
                    return null;
                }
                i4 += IPv4;
                i5 = (charAt - 48) + (i2 * 10);
                if (i5 > KEYRecord.PROTOCOL_ANY) {
                    return null;
                }
            }
            i += IPv4;
            i2 = i5;
        }
        if (i3 != 3) {
            return null;
        }
        if (i4 == 0) {
            return null;
        }
        bArr[i3] = (byte) i2;
        return bArr;
    }

    private static byte[] parseV6(String str) {
        int i;
        int i2;
        int i3 = -1;
        Object obj = new byte[16];
        String[] split = str.split(":", -1);
        int length = split.length - 1;
        if (split[0].length() != 0) {
            i = 0;
        } else if (length - 0 <= 0 || split[IPv4].length() != 0) {
            return null;
        } else {
            i = IPv4;
        }
        if (split[length].length() != 0) {
            i2 = length;
        } else if (length - i <= 0 || split[length - 1].length() != 0) {
            return null;
        } else {
            i2 = length - 1;
        }
        if ((i2 - i) + IPv4 > 8) {
            return null;
        }
        i = 0;
        for (int i4 = i; i4 <= i2; i4 += IPv4) {
            int i5;
            if (split[i4].length() == 0) {
                if (i3 >= 0) {
                    return null;
                }
                i3 = i;
            } else if (split[i4].indexOf(46) < 0) {
                length = 0;
                while (length < split[i4].length()) {
                    try {
                        if (Character.digit(split[i4].charAt(length), 16) < 0) {
                            return null;
                        }
                        length += IPv4;
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
                length = Integer.parseInt(split[i4], 16);
                if (length > InBandBytestreamManager.MAXIMUM_BLOCK_SIZE || length < 0) {
                    return null;
                }
                int i6 = i + IPv4;
                obj[i] = (byte) (length >>> 8);
                i = i6 + IPv4;
                obj[i6] = (byte) (length & KEYRecord.PROTOCOL_ANY);
            } else if (i4 < i2) {
                return null;
            } else {
                if (i4 > 6) {
                    return null;
                }
                byte[] toByteArray = toByteArray(split[i4], IPv4);
                if (toByteArray == null) {
                    return null;
                }
                i5 = 0;
                while (i5 < 4) {
                    length = i + IPv4;
                    obj[i] = toByteArray[i5];
                    i5 += IPv4;
                    i = length;
                }
                if (i >= 16 && i3 < 0) {
                    return null;
                }
                if (i3 >= 0) {
                    i5 = 16 - i;
                    System.arraycopy(obj, i3, obj, i3 + i5, i - i3);
                    for (i = i3; i < i3 + i5; i += IPv4) {
                        obj[i] = null;
                    }
                }
                return obj;
            }
        }
        if (i >= 16) {
        }
        if (i3 >= 0) {
            i5 = 16 - i;
            System.arraycopy(obj, i3, obj, i3 + i5, i - i3);
            for (i = i3; i < i3 + i5; i += IPv4) {
                obj[i] = null;
            }
        }
        return obj;
    }

    public static int[] toArray(String str, int i) {
        byte[] toByteArray = toByteArray(str, i);
        if (toByteArray == null) {
            return null;
        }
        int[] iArr = new int[toByteArray.length];
        for (int i2 = 0; i2 < toByteArray.length; i2 += IPv4) {
            iArr[i2] = toByteArray[i2] & KEYRecord.PROTOCOL_ANY;
        }
        return iArr;
    }

    public static int[] toArray(String str) {
        return toArray(str, IPv4);
    }

    public static byte[] toByteArray(String str, int i) {
        if (i == IPv4) {
            return parseV4(str);
        }
        if (i == IPv6) {
            return parseV6(str);
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static boolean isDottedQuad(String str) {
        if (toByteArray(str, IPv4) != null) {
            return true;
        }
        return false;
    }

    public static String toDottedQuad(byte[] bArr) {
        return (bArr[0] & KEYRecord.PROTOCOL_ANY) + "." + (bArr[IPv4] & KEYRecord.PROTOCOL_ANY) + "." + (bArr[IPv6] & KEYRecord.PROTOCOL_ANY) + "." + (bArr[3] & KEYRecord.PROTOCOL_ANY);
    }

    public static String toDottedQuad(int[] iArr) {
        return iArr[0] + "." + iArr[IPv4] + "." + iArr[IPv6] + "." + iArr[3];
    }

    private static Record[] lookupHostName(String str) throws UnknownHostException {
        try {
            Record[] run = new Lookup(str).run();
            if (run != null) {
                return run;
            }
            throw new UnknownHostException("unknown host");
        } catch (TextParseException e) {
            throw new UnknownHostException("invalid name");
        }
    }

    private static InetAddress addrFromRecord(String str, Record record) throws UnknownHostException {
        return InetAddress.getByAddress(str, ((ARecord) record).getAddress().getAddress());
    }

    public static InetAddress getByName(String str) throws UnknownHostException {
        try {
            return getByAddress(str);
        } catch (UnknownHostException e) {
            return addrFromRecord(str, lookupHostName(str)[0]);
        }
    }

    public static InetAddress[] getAllByName(String str) throws UnknownHostException {
        InetAddress[] inetAddressArr;
        try {
            inetAddressArr = new InetAddress[IPv4];
            inetAddressArr[0] = getByAddress(str);
            return inetAddressArr;
        } catch (UnknownHostException e) {
            Record[] lookupHostName = lookupHostName(str);
            inetAddressArr = new InetAddress[lookupHostName.length];
            for (int i = 0; i < lookupHostName.length; i += IPv4) {
                inetAddressArr[i] = addrFromRecord(str, lookupHostName[i]);
            }
            return inetAddressArr;
        }
    }

    public static InetAddress getByAddress(String str) throws UnknownHostException {
        byte[] toByteArray = toByteArray(str, IPv4);
        if (toByteArray != null) {
            return InetAddress.getByAddress(str, toByteArray);
        }
        toByteArray = toByteArray(str, IPv6);
        if (toByteArray != null) {
            return InetAddress.getByAddress(str, toByteArray);
        }
        throw new UnknownHostException("Invalid address: " + str);
    }

    public static InetAddress getByAddress(String str, int i) throws UnknownHostException {
        if (i == IPv4 || i == IPv6) {
            byte[] toByteArray = toByteArray(str, i);
            if (toByteArray != null) {
                return InetAddress.getByAddress(str, toByteArray);
            }
            throw new UnknownHostException("Invalid address: " + str);
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static String getHostName(InetAddress inetAddress) throws UnknownHostException {
        Record[] run = new Lookup(ReverseMap.fromAddress(inetAddress), 12).run();
        if (run != null) {
            return ((PTRRecord) run[0]).getTarget().toString();
        }
        throw new UnknownHostException("unknown address");
    }

    public static int familyOf(InetAddress inetAddress) {
        if (inetAddress instanceof Inet4Address) {
            return IPv4;
        }
        if (inetAddress instanceof Inet6Address) {
            return IPv6;
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static int addressLength(int i) {
        if (i == IPv4) {
            return 4;
        }
        if (i == IPv6) {
            return 16;
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static InetAddress truncate(InetAddress inetAddress, int i) {
        int i2 = 0;
        int addressLength = addressLength(familyOf(inetAddress)) * 8;
        if (i < 0 || i > addressLength) {
            throw new IllegalArgumentException("invalid mask length");
        }
        if (i != addressLength) {
            byte[] address = inetAddress.getAddress();
            for (addressLength = (i / 8) + IPv4; addressLength < address.length; addressLength += IPv4) {
                address[addressLength] = (byte) 0;
            }
            for (addressLength = 0; addressLength < i % 8; addressLength += IPv4) {
                i2 |= IPv4 << (7 - addressLength);
            }
            addressLength = i / 8;
            address[addressLength] = (byte) (i2 & address[addressLength]);
            try {
                inetAddress = InetAddress.getByAddress(address);
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException("invalid address");
            }
        }
        return inetAddress;
    }
}
