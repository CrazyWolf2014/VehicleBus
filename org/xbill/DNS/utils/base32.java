package org.xbill.DNS.utils;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.xbill.DNS.KEYRecord;

public class base32 {
    private String alphabet;
    private boolean lowercase;
    private boolean padding;

    public static class Alphabet {
        public static final String BASE32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567=";
        public static final String BASE32HEX = "0123456789ABCDEFGHIJKLMNOPQRSTUV=";

        private Alphabet() {
        }
    }

    public base32(String str, boolean z, boolean z2) {
        this.alphabet = str;
        this.padding = z;
        this.lowercase = z2;
    }

    private static int blockLenToPadding(int i) {
        switch (i) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return 6;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return 4;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return 3;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return 1;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                return 0;
            default:
                return -1;
        }
    }

    private static int paddingToBlockLen(int i) {
        switch (i) {
            case KEYRecord.OWNER_USER /*0*/:
                return 5;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return 4;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return 3;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return 2;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return 1;
            default:
                return -1;
        }
    }

    public String toString(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < (bArr.length + 4) / 5; i++) {
            int i2;
            short[] sArr = new short[5];
            int[] iArr = new int[8];
            int i3 = 5;
            for (i2 = 0; i2 < 5; i2++) {
                if ((i * 5) + i2 < bArr.length) {
                    sArr[i2] = (short) (bArr[(i * 5) + i2] & KEYRecord.PROTOCOL_ANY);
                } else {
                    sArr[i2] = (short) 0;
                    i3--;
                }
            }
            int blockLenToPadding = blockLenToPadding(i3);
            iArr[0] = (byte) ((sArr[0] >> 3) & 31);
            iArr[1] = (byte) (((sArr[0] & 7) << 2) | ((sArr[1] >> 6) & 3));
            iArr[2] = (byte) ((sArr[1] >> 1) & 31);
            iArr[3] = (byte) (((sArr[1] & 1) << 4) | ((sArr[2] >> 4) & 15));
            iArr[4] = (byte) (((sArr[2] & 15) << 1) | ((sArr[3] >> 7) & 1));
            iArr[5] = (byte) ((sArr[3] >> 2) & 31);
            iArr[6] = (byte) (((sArr[3] & 3) << 3) | ((sArr[4] >> 5) & 7));
            iArr[7] = (byte) (sArr[4] & 31);
            for (i3 = 0; i3 < iArr.length - blockLenToPadding; i3++) {
                i2 = this.alphabet.charAt(iArr[i3]);
                if (this.lowercase) {
                    i2 = Character.toLowerCase(i2);
                }
                byteArrayOutputStream.write(i2);
            }
            if (this.padding) {
                for (i3 = iArr.length - blockLenToPadding; i3 < iArr.length; i3++) {
                    byteArrayOutputStream.write(61);
                }
            }
        }
        return new String(byteArrayOutputStream.toByteArray());
    }

    public byte[] fromString(String str) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = str.getBytes();
        for (byte b : bytes) {
            char c = (char) b;
            if (!Character.isWhitespace(c)) {
                byteArrayOutputStream.write((byte) Character.toUpperCase(c));
            }
        }
        if (!this.padding) {
            while (byteArrayOutputStream.size() % 8 != 0) {
                byteArrayOutputStream.write(61);
            }
        } else if (byteArrayOutputStream.size() % 8 != 0) {
            return null;
        }
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.reset();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        int i = 0;
        while (i < toByteArray.length / 8) {
            short[] sArr = new short[8];
            int[] iArr = new int[5];
            int i2 = 8;
            int i3 = 0;
            while (i3 < 8 && ((char) toByteArray[(i * 8) + i3]) != SignatureVisitor.INSTANCEOF) {
                sArr[i3] = (short) this.alphabet.indexOf(toByteArray[(i * 8) + i3]);
                if (sArr[i3] < (short) 0) {
                    return null;
                }
                i2--;
                i3++;
            }
            i2 = paddingToBlockLen(i2);
            if (i2 < 0) {
                return null;
            }
            iArr[0] = (sArr[0] << 3) | (sArr[1] >> 2);
            iArr[1] = (((sArr[1] & 3) << 6) | (sArr[2] << 1)) | (sArr[3] >> 4);
            iArr[2] = ((sArr[3] & 15) << 4) | ((sArr[4] >> 1) & 15);
            iArr[3] = ((sArr[4] << 7) | (sArr[5] << 2)) | (sArr[6] >> 3);
            iArr[4] = sArr[7] | ((sArr[6] & 7) << 5);
            i3 = 0;
            while (i3 < i2) {
                try {
                    dataOutputStream.writeByte((byte) (iArr[i3] & KEYRecord.PROTOCOL_ANY));
                    i3++;
                } catch (IOException e) {
                }
            }
            i++;
        }
        return byteArrayOutputStream.toByteArray();
    }
}
