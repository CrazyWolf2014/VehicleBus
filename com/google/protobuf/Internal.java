package com.google.protobuf;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;

public class Internal {

    public interface EnumLite {
        int getNumber();
    }

    public interface EnumLiteMap<T extends EnumLite> {
        T findValueByNumber(int i);
    }

    public static String stringDefaultValue(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), AsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Throwable e) {
            throw new IllegalStateException("Java VM does not support a standard character set.", e);
        }
    }

    public static ByteString bytesDefaultValue(String str) {
        try {
            return ByteString.copyFrom(str.getBytes("ISO-8859-1"));
        } catch (Throwable e) {
            throw new IllegalStateException("Java VM does not support a standard character set.", e);
        }
    }

    public static boolean isValidUtf8(ByteString byteString) {
        int size = byteString.size();
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            int byteAt = byteString.byteAt(i) & KEYRecord.PROTOCOL_ANY;
            if (byteAt < Flags.FLAG8) {
                i = i2;
            } else if (byteAt < Wbxml.EXT_2 || byteAt > 244) {
                return false;
            } else {
                if (i2 >= size) {
                    return false;
                }
                int i3 = i2 + 1;
                int byteAt2 = byteString.byteAt(i2) & KEYRecord.PROTOCOL_ANY;
                if (byteAt2 < Flags.FLAG8 || byteAt2 > Opcodes.ATHROW) {
                    return false;
                }
                if (byteAt <= 223) {
                    i = i3;
                } else if (i3 >= size) {
                    return false;
                } else {
                    i = i3 + 1;
                    i2 = byteString.byteAt(i3) & KEYRecord.PROTOCOL_ANY;
                    if (i2 < Flags.FLAG8 || i2 > Opcodes.ATHROW) {
                        return false;
                    }
                    if (byteAt <= 239) {
                        if ((byteAt == SmileConstants.TOKEN_PREFIX_MISC_OTHER && byteAt2 < SmileConstants.TOKEN_PREFIX_SHORT_UNICODE) || (byteAt == 237 && byteAt2 > Opcodes.IF_ICMPEQ)) {
                            return false;
                        }
                    } else if (i >= size) {
                        return false;
                    } else {
                        i2 = i + 1;
                        i = byteString.byteAt(i) & KEYRecord.PROTOCOL_ANY;
                        if (i < Flags.FLAG8 || i > Opcodes.ATHROW) {
                            return false;
                        }
                        if ((byteAt == 240 && byteAt2 < Opcodes.D2F) || (byteAt == 244 && byteAt2 > Opcodes.D2L)) {
                            return false;
                        }
                        i = i2;
                    }
                }
            }
        }
        return true;
    }
}
