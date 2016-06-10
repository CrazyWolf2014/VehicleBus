package org.apache.commons.codec.binary;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FileOptions;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.json.HTTP;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;

public class Base64 implements BinaryEncoder, BinaryDecoder {
    static final int BASELENGTH = 255;
    static final byte[] CHUNK_SEPARATOR;
    static final int CHUNK_SIZE = 76;
    static final int EIGHTBIT = 8;
    static final int FOURBYTE = 4;
    static final int LOOKUPLENGTH = 64;
    static final byte PAD = (byte) 61;
    static final int SIGN = -128;
    static final int SIXTEENBIT = 16;
    static final int TWENTYFOURBITGROUP = 24;
    private static byte[] base64Alphabet;
    private static byte[] lookUpBase64Alphabet;

    static {
        int i;
        CHUNK_SEPARATOR = HTTP.CRLF.getBytes();
        base64Alphabet = new byte[BASELENGTH];
        lookUpBase64Alphabet = new byte[LOOKUPLENGTH];
        for (i = 0; i < BASELENGTH; i++) {
            base64Alphabet[i] = (byte) -1;
        }
        for (i = 90; i >= 65; i--) {
            base64Alphabet[i] = (byte) (i - 65);
        }
        for (i = Opcodes.ISHR; i >= 97; i--) {
            base64Alphabet[i] = (byte) ((i - 97) + 26);
        }
        for (i = 57; i >= 48; i--) {
            base64Alphabet[i] = (byte) ((i - 48) + 52);
        }
        base64Alphabet[43] = (byte) 62;
        base64Alphabet[47] = (byte) 63;
        for (i = 0; i <= 25; i++) {
            lookUpBase64Alphabet[i] = (byte) (i + 65);
        }
        i = 26;
        int j = 0;
        while (i <= 51) {
            lookUpBase64Alphabet[i] = (byte) (j + 97);
            i++;
            j++;
        }
        i = 52;
        j = 0;
        while (i <= 61) {
            lookUpBase64Alphabet[i] = (byte) (j + 48);
            i++;
            j++;
        }
        lookUpBase64Alphabet[62] = (byte) 43;
        lookUpBase64Alphabet[63] = (byte) 47;
    }

    private static boolean isBase64(byte octect) {
        if (octect != 61 && base64Alphabet[octect] == -1) {
            return false;
        }
        return true;
    }

    public static boolean isArrayByteBase64(byte[] arrayOctect) {
        if (length == 0) {
            return true;
        }
        for (byte isBase64 : discardWhitespace(arrayOctect)) {
            if (!isBase64(isBase64)) {
                return false;
            }
        }
        return true;
    }

    public static byte[] encodeBase64(byte[] binaryData) {
        return encodeBase64(binaryData, false);
    }

    public static byte[] encodeBase64Chunked(byte[] binaryData) {
        return encodeBase64(binaryData, true);
    }

    public Object decode(Object pObject) throws DecoderException {
        if (pObject instanceof byte[]) {
            return decode((byte[]) pObject);
        }
        throw new DecoderException("Parameter supplied to Base64 decode is not a byte[]");
    }

    public byte[] decode(byte[] pArray) {
        return decodeBase64(pArray);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
        int encodedDataLength;
        int dataIndex;
        byte val1;
        byte val2;
        int lengthDataBits = binaryData.length * EIGHTBIT;
        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
        int nbrChunks = 0;
        if (fewerThan24bits != 0) {
            encodedDataLength = (numberTriplets + 1) * FOURBYTE;
        } else {
            encodedDataLength = numberTriplets * FOURBYTE;
        }
        if (isChunked) {
            if (CHUNK_SEPARATOR.length == 0) {
                nbrChunks = 0;
            } else {
                nbrChunks = (int) Math.ceil((double) (((float) encodedDataLength) / 76.0f));
            }
            encodedDataLength += CHUNK_SEPARATOR.length * nbrChunks;
        }
        byte[] encodedData = new byte[encodedDataLength];
        int encodedIndex = 0;
        int nextSeparatorIndex = CHUNK_SIZE;
        int chunksSoFar = 0;
        int i = 0;
        while (i < numberTriplets) {
            byte val3;
            dataIndex = i * 3;
            byte b1 = binaryData[dataIndex];
            byte b2 = binaryData[dataIndex + 1];
            byte b3 = binaryData[dataIndex + 2];
            byte l = (byte) (b2 & 15);
            byte k = (byte) (b1 & 3);
            if ((b1 & SIGN) == 0) {
                val1 = (byte) (b1 >> 2);
            } else {
                val1 = (byte) ((b1 >> 2) ^ Wbxml.EXT_0);
            }
            if ((b2 & SIGN) == 0) {
                val2 = (byte) (b2 >> FOURBYTE);
            } else {
                val2 = (byte) ((b2 >> FOURBYTE) ^ 240);
            }
            if ((b3 & SIGN) == 0) {
                val3 = (byte) (b3 >> 6);
            } else {
                val3 = (byte) ((b3 >> 6) ^ Type.AXFR);
            }
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[(k << FOURBYTE) | val2];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[(l << 2) | val3];
            encodedData[encodedIndex + 3] = lookUpBase64Alphabet[b3 & 63];
            encodedIndex += FOURBYTE;
            if (isChunked && encodedIndex == nextSeparatorIndex) {
                System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedIndex, CHUNK_SEPARATOR.length);
                chunksSoFar++;
                nextSeparatorIndex = ((chunksSoFar + 1) * CHUNK_SIZE) + (CHUNK_SEPARATOR.length * chunksSoFar);
                encodedIndex += CHUNK_SEPARATOR.length;
            }
            i++;
        }
        dataIndex = i * 3;
        if (fewerThan24bits == EIGHTBIT) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 3);
            if ((b1 & SIGN) == 0) {
                val1 = (byte) (b1 >> 2);
            } else {
                val1 = (byte) ((b1 >> 2) ^ Wbxml.EXT_0);
            }
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[k << FOURBYTE];
            encodedData[encodedIndex + 2] = PAD;
            encodedData[encodedIndex + 3] = PAD;
        } else if (fewerThan24bits == SIXTEENBIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 15);
            k = (byte) (b1 & 3);
            if ((b1 & SIGN) == 0) {
                val1 = (byte) (b1 >> 2);
            } else {
                val1 = (byte) ((b1 >> 2) ^ Wbxml.EXT_0);
            }
            if ((b2 & SIGN) == 0) {
                val2 = (byte) (b2 >> FOURBYTE);
            } else {
                val2 = (byte) ((b2 >> FOURBYTE) ^ 240);
            }
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[(k << FOURBYTE) | val2];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex + 3] = PAD;
        }
        if (isChunked && chunksSoFar < nbrChunks) {
            System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedDataLength - CHUNK_SEPARATOR.length, CHUNK_SEPARATOR.length);
        }
        return encodedData;
    }

    public static byte[] decodeBase64(byte[] base64Data) {
        base64Data = discardNonBase64(base64Data);
        if (base64Data.length == 0) {
            return new byte[0];
        }
        int numberQuadruple = base64Data.length / FOURBYTE;
        int encodedIndex = 0;
        int lastData = base64Data.length;
        while (base64Data[lastData - 1] == 61) {
            lastData--;
            if (lastData == 0) {
                return new byte[0];
            }
        }
        byte[] decodedData = new byte[(lastData - numberQuadruple)];
        for (int i = 0; i < numberQuadruple; i++) {
            int dataIndex = i * FOURBYTE;
            byte marker0 = base64Data[dataIndex + 2];
            byte marker1 = base64Data[dataIndex + 3];
            byte b1 = base64Alphabet[base64Data[dataIndex]];
            byte b2 = base64Alphabet[base64Data[dataIndex + 1]];
            byte b3;
            if (marker0 != 61 && marker1 != 61) {
                b3 = base64Alphabet[marker0];
                byte b4 = base64Alphabet[marker1];
                decodedData[encodedIndex] = (byte) ((b1 << 2) | (b2 >> FOURBYTE));
                decodedData[encodedIndex + 1] = (byte) (((b2 & 15) << FOURBYTE) | ((b3 >> 2) & 15));
                decodedData[encodedIndex + 2] = (byte) ((b3 << 6) | b4);
            } else if (marker0 == 61) {
                decodedData[encodedIndex] = (byte) ((b1 << 2) | (b2 >> FOURBYTE));
            } else if (marker1 == 61) {
                b3 = base64Alphabet[marker0];
                decodedData[encodedIndex] = (byte) ((b1 << 2) | (b2 >> FOURBYTE));
                decodedData[encodedIndex + 1] = (byte) (((b2 & 15) << FOURBYTE) | ((b3 >> 2) & 15));
            }
            encodedIndex += 3;
        }
        return decodedData;
    }

    static byte[] discardWhitespace(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;
        for (int i = 0; i < data.length; i++) {
            switch (data[i]) {
                case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                case Protocol.MERIT_INP /*32*/:
                    break;
                default:
                    int bytesCopied2 = bytesCopied + 1;
                    groomedData[bytesCopied] = data[i];
                    bytesCopied = bytesCopied2;
                    break;
            }
        }
        byte[] packedData = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }

    static byte[] discardNonBase64(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;
        for (int i = 0; i < data.length; i++) {
            if (isBase64(data[i])) {
                int bytesCopied2 = bytesCopied + 1;
                groomedData[bytesCopied] = data[i];
                bytesCopied = bytesCopied2;
            }
        }
        byte[] packedData = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }

    public Object encode(Object pObject) throws EncoderException {
        if (pObject instanceof byte[]) {
            return encode((byte[]) pObject);
        }
        throw new EncoderException("Parameter supplied to Base64 encode is not a byte[]");
    }

    public byte[] encode(byte[] pArray) {
        return encodeBase64(pArray, false);
    }
}
