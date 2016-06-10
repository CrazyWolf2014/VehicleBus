package com.google.zxing.qrcode.decoder;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.reedsolomon.GF256;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;
import java.util.Hashtable;
import org.xbill.DNS.KEYRecord;

public final class Decoder {
    private final ReedSolomonDecoder rsDecoder;

    public Decoder() {
        this.rsDecoder = new ReedSolomonDecoder(GF256.QR_CODE_FIELD);
    }

    private void correctErrors(byte[] bArr, int i) throws ChecksumException {
        int i2 = 0;
        int length = bArr.length;
        int[] iArr = new int[length];
        for (int i3 = 0; i3 < length; i3++) {
            iArr[i3] = bArr[i3] & KEYRecord.PROTOCOL_ANY;
        }
        try {
            this.rsDecoder.decode(iArr, bArr.length - i);
            while (i2 < i) {
                bArr[i2] = (byte) iArr[i2];
                i2++;
            }
        } catch (ReedSolomonException e) {
            throw ChecksumException.getChecksumInstance();
        }
    }

    public DecoderResult decode(BitMatrix bitMatrix) throws ChecksumException, FormatException, NotFoundException {
        return decode(bitMatrix, null);
    }

    public DecoderResult decode(BitMatrix bitMatrix, Hashtable hashtable) throws FormatException, ChecksumException {
        BitMatrixParser bitMatrixParser = new BitMatrixParser(bitMatrix);
        Version readVersion = bitMatrixParser.readVersion();
        ErrorCorrectionLevel errorCorrectionLevel = bitMatrixParser.readFormatInformation().getErrorCorrectionLevel();
        DataBlock[] dataBlocks = DataBlock.getDataBlocks(bitMatrixParser.readCodewords(), readVersion, errorCorrectionLevel);
        int i = 0;
        for (DataBlock numDataCodewords : dataBlocks) {
            i += numDataCodewords.getNumDataCodewords();
        }
        byte[] bArr = new byte[i];
        i = 0;
        for (DataBlock numDataCodewords2 : dataBlocks) {
            byte[] codewords = numDataCodewords2.getCodewords();
            int numDataCodewords3 = numDataCodewords2.getNumDataCodewords();
            correctErrors(codewords, numDataCodewords3);
            int i2 = 0;
            while (i2 < numDataCodewords3) {
                int i3 = i + 1;
                bArr[i] = codewords[i2];
                i2++;
                i = i3;
            }
        }
        return DecodedBitStreamParser.decode(bArr, readVersion, errorCorrectionLevel, hashtable);
    }

    public DecoderResult decode(boolean[][] zArr) throws ChecksumException, FormatException, NotFoundException {
        return decode(zArr, null);
    }

    public DecoderResult decode(boolean[][] zArr, Hashtable hashtable) throws ChecksumException, FormatException, NotFoundException {
        int length = zArr.length;
        BitMatrix bitMatrix = new BitMatrix(length);
        for (int i = 0; i < length; i++) {
            for (int i2 = 0; i2 < length; i2++) {
                if (zArr[i][i2]) {
                    bitMatrix.set(i2, i);
                }
            }
        }
        return decode(bitMatrix, hashtable);
    }
}
