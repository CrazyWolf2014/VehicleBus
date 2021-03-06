package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

public final class EAN8Reader extends UPCEANReader {
    private final int[] decodeMiddleCounters;

    public EAN8Reader() {
        this.decodeMiddleCounters = new int[4];
    }

    protected int decodeMiddle(BitArray bitArray, int[] iArr, StringBuffer stringBuffer) throws NotFoundException {
        int[] iArr2 = this.decodeMiddleCounters;
        iArr2[0] = 0;
        iArr2[1] = 0;
        iArr2[2] = 0;
        iArr2[3] = 0;
        int size = bitArray.getSize();
        int i = iArr[1];
        int i2 = 0;
        while (i2 < 4 && i < size) {
            stringBuffer.append((char) (UPCEANReader.decodeDigit(bitArray, iArr2, i, L_PATTERNS) + 48));
            int i3 = i;
            for (int i4 : iArr2) {
                i3 += i4;
            }
            i2++;
            i = i3;
        }
        i = UPCEANReader.findGuardPattern(bitArray, i, true, MIDDLE_PATTERN)[1];
        i2 = 0;
        while (i2 < 4 && i < size) {
            stringBuffer.append((char) (UPCEANReader.decodeDigit(bitArray, iArr2, i, L_PATTERNS) + 48));
            i3 = i;
            for (int i42 : iArr2) {
                i3 += i42;
            }
            i2++;
            i = i3;
        }
        return i;
    }

    BarcodeFormat getBarcodeFormat() {
        return BarcodeFormat.EAN_8;
    }
}
