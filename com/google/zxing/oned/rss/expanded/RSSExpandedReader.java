package com.google.zxing.oned.rss.expanded;

import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.OneDReader;
import com.google.zxing.oned.rss.AbstractRSSReader;
import com.google.zxing.oned.rss.DataCharacter;
import com.google.zxing.oned.rss.FinderPattern;
import com.google.zxing.oned.rss.RSSUtils;
import com.google.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder;
import java.util.Hashtable;
import java.util.Vector;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;
import org.ksoap2.SoapEnvelope;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public final class RSSExpandedReader extends AbstractRSSReader {
    private static final int[] EVEN_TOTAL_SUBSET;
    private static final int[][] FINDER_PATTERNS;
    private static final int[][] FINDER_PATTERN_SEQUENCES;
    private static final int FINDER_PAT_A = 0;
    private static final int FINDER_PAT_B = 1;
    private static final int FINDER_PAT_C = 2;
    private static final int FINDER_PAT_D = 3;
    private static final int FINDER_PAT_E = 4;
    private static final int FINDER_PAT_F = 5;
    private static final int[] GSUM;
    private static final int LONGEST_SEQUENCE_SIZE;
    private static final int MAX_PAIRS = 11;
    private static final int[] SYMBOL_WIDEST;
    private static final int[][] WEIGHTS;
    private final int[] currentSequence;
    private final Vector pairs;
    private final int[] startEnd;

    static {
        SYMBOL_WIDEST = new int[]{7, FINDER_PAT_F, FINDER_PAT_E, FINDER_PAT_D, FINDER_PAT_B};
        EVEN_TOTAL_SUBSET = new int[]{FINDER_PAT_E, 20, 52, Service.X400_SND, 204};
        GSUM = new int[]{LONGEST_SEQUENCE_SIZE, 348, 1388, 2948, 3988};
        FINDER_PATTERNS = new int[][]{new int[]{FINDER_PAT_B, 8, FINDER_PAT_E, FINDER_PAT_B}, new int[]{FINDER_PAT_D, 6, FINDER_PAT_E, FINDER_PAT_B}, new int[]{FINDER_PAT_D, FINDER_PAT_E, 6, FINDER_PAT_B}, new int[]{FINDER_PAT_D, FINDER_PAT_C, 8, FINDER_PAT_B}, new int[]{FINDER_PAT_C, 6, FINDER_PAT_F, FINDER_PAT_B}, new int[]{FINDER_PAT_C, FINDER_PAT_C, 9, FINDER_PAT_B}};
        WEIGHTS = new int[][]{new int[]{FINDER_PAT_B, FINDER_PAT_D, 9, 27, 81, 32, 96, 77}, new int[]{20, 60, Opcodes.GETFIELD, Opcodes.FNEG, Opcodes.D2L, 7, 21, 63}, new int[]{Opcodes.ANEWARRAY, Opcodes.I2B, 13, 39, Service.UUCP_PATH, Service.EMFIS_DATA, 209, 205}, new int[]{Wbxml.EXT_1, Opcodes.IFGT, 49, Opcodes.I2S, 19, 57, Opcodes.LOOKUPSWITCH, 91}, new int[]{62, Opcodes.INVOKEDYNAMIC, Service.PROFILE, Opcodes.MULTIANEWARRAY, Opcodes.RET, 85, 44, Service.CISCO_SYS}, new int[]{Opcodes.INVOKEINTERFACE, Service.STATSRV, Opcodes.NEWARRAY, Service.BL_IDM, FINDER_PAT_E, 12, 36, Opcodes.IDIV}, new int[]{Service.AUTH, Flags.FLAG8, Opcodes.LRETURN, 97, 80, 29, 87, 50}, new int[]{Opcodes.FCMPG, 28, 84, 41, Service.NTP, Opcodes.IFLE, 52, Opcodes.IFGE}, new int[]{46, Service.NETBIOS_DGM, 203, Opcodes.NEW, Service.NETBIOS_SSN, 206, Wbxml.LITERAL_AC, Opcodes.IF_ACMPNE}, new int[]{76, 17, 51, Opcodes.IFEQ, 37, Service.SUNRPC, Opcodes.ISHR, Opcodes.IFLT}, new int[]{43, Service.PWDGEN, Opcodes.ARETURN, Opcodes.FMUL, Service.RTELNET, SoapEnvelope.VER11, Service.NNTP, Opcodes.I2C}, new int[]{16, 48, Opcodes.D2F, 10, 30, 90, 59, Opcodes.RETURN}, new int[]{Service.POP_2, Opcodes.INEG, Service.NETBIOS_NS, AsyncTaskManager.REQUEST_SUCCESS_CODE, Opcodes.GETSTATIC, Opcodes.IREM, Service.LOCUS_MAP, Opcodes.IF_ICMPLE}, new int[]{70, 210, 208, 202, Opcodes.INVOKESTATIC, Service.CISCO_FNA, Opcodes.PUTSTATIC, Service.SFTP}, new int[]{Service.INGRES_NET, Opcodes.ATHROW, Opcodes.DCMPL, 31, 93, 68, 204, Opcodes.ARRAYLENGTH}, new int[]{Opcodes.LCMP, 22, 66, Opcodes.IFNULL, Opcodes.IRETURN, 94, 71, FINDER_PAT_C}, new int[]{6, 18, 54, Opcodes.IF_ICMPGE, 64, Wbxml.EXT_0, Opcodes.IFNE, 40}, new int[]{SoapEnvelope.VER12, Opcodes.FCMPL, 25, 75, 14, 42, Opcodes.IAND, Opcodes.GOTO}, new int[]{79, 26, 78, 23, 69, 207, Opcodes.IFNONNULL, Opcodes.DRETURN}, new int[]{Service.X400, 98, 83, 38, Opcodes.FREM, Service.CISCO_TNA, Opcodes.INVOKEVIRTUAL, Opcodes.IUSHR}, new int[]{Opcodes.IF_ICMPLT, 61, Opcodes.INVOKESPECIAL, Service.LOCUS_CON, Opcodes.TABLESWITCH, 88, 53, Opcodes.IF_ICMPEQ}, new int[]{55, Opcodes.IF_ACMPEQ, 73, 8, 24, 72, FINDER_PAT_F, 15}, new int[]{45, Service.LOC_SRV, Wbxml.EXT_2, SmileConstants.TOKEN_PREFIX_SHORT_UNICODE, 58, Opcodes.FRETURN, 100, 89}};
        FINDER_PATTERN_SEQUENCES = new int[][]{new int[]{LONGEST_SEQUENCE_SIZE, LONGEST_SEQUENCE_SIZE}, new int[]{LONGEST_SEQUENCE_SIZE, FINDER_PAT_B, FINDER_PAT_B}, new int[]{LONGEST_SEQUENCE_SIZE, FINDER_PAT_C, FINDER_PAT_B, FINDER_PAT_D}, new int[]{LONGEST_SEQUENCE_SIZE, FINDER_PAT_E, FINDER_PAT_B, FINDER_PAT_D, FINDER_PAT_C}, new int[]{LONGEST_SEQUENCE_SIZE, FINDER_PAT_E, FINDER_PAT_B, FINDER_PAT_D, FINDER_PAT_D, FINDER_PAT_F}, new int[]{LONGEST_SEQUENCE_SIZE, FINDER_PAT_E, FINDER_PAT_B, FINDER_PAT_D, FINDER_PAT_E, FINDER_PAT_F, FINDER_PAT_F}, new int[]{LONGEST_SEQUENCE_SIZE, LONGEST_SEQUENCE_SIZE, FINDER_PAT_B, FINDER_PAT_B, FINDER_PAT_C, FINDER_PAT_C, FINDER_PAT_D, FINDER_PAT_D}, new int[]{LONGEST_SEQUENCE_SIZE, LONGEST_SEQUENCE_SIZE, FINDER_PAT_B, FINDER_PAT_B, FINDER_PAT_C, FINDER_PAT_C, FINDER_PAT_D, FINDER_PAT_E, FINDER_PAT_E}, new int[]{LONGEST_SEQUENCE_SIZE, LONGEST_SEQUENCE_SIZE, FINDER_PAT_B, FINDER_PAT_B, FINDER_PAT_C, FINDER_PAT_C, FINDER_PAT_D, FINDER_PAT_E, FINDER_PAT_F, FINDER_PAT_F}, new int[]{LONGEST_SEQUENCE_SIZE, LONGEST_SEQUENCE_SIZE, FINDER_PAT_B, FINDER_PAT_B, FINDER_PAT_C, FINDER_PAT_D, FINDER_PAT_D, FINDER_PAT_E, FINDER_PAT_E, FINDER_PAT_F, FINDER_PAT_F}};
        LONGEST_SEQUENCE_SIZE = FINDER_PATTERN_SEQUENCES[FINDER_PATTERN_SEQUENCES.length - 1].length;
    }

    public RSSExpandedReader() {
        this.pairs = new Vector(MAX_PAIRS);
        this.startEnd = new int[FINDER_PAT_C];
        this.currentSequence = new int[LONGEST_SEQUENCE_SIZE];
    }

    private void adjustOddEvenCounts(int i) throws NotFoundException {
        Object obj;
        Object obj2;
        Object obj3;
        Object obj4 = null;
        Object obj5 = FINDER_PAT_B;
        int count = AbstractRSSReader.count(this.oddCounts);
        int count2 = AbstractRSSReader.count(this.evenCounts);
        int i2 = (count + count2) - i;
        Object obj6 = (count & FINDER_PAT_B) == FINDER_PAT_B ? FINDER_PAT_B : LONGEST_SEQUENCE_SIZE;
        Object obj7 = (count2 & FINDER_PAT_B) == 0 ? FINDER_PAT_B : LONGEST_SEQUENCE_SIZE;
        if (count > 13) {
            obj = FINDER_PAT_B;
            obj2 = LONGEST_SEQUENCE_SIZE;
        } else if (count < FINDER_PAT_E) {
            obj = LONGEST_SEQUENCE_SIZE;
            obj2 = FINDER_PAT_B;
        } else {
            obj = LONGEST_SEQUENCE_SIZE;
            obj2 = LONGEST_SEQUENCE_SIZE;
        }
        if (count2 > 13) {
            obj3 = LONGEST_SEQUENCE_SIZE;
            obj4 = FINDER_PAT_B;
        } else {
            obj3 = count2 < FINDER_PAT_E ? FINDER_PAT_B : LONGEST_SEQUENCE_SIZE;
        }
        if (i2 == FINDER_PAT_B) {
            if (obj6 != null) {
                if (obj7 != null) {
                    throw NotFoundException.getNotFoundInstance();
                }
                obj = obj2;
                obj5 = obj3;
                obj3 = FINDER_PAT_B;
            } else if (obj7 == null) {
                throw NotFoundException.getNotFoundInstance();
            } else {
                obj4 = FINDER_PAT_B;
                obj5 = obj3;
                obj3 = obj;
                obj = obj2;
            }
        } else if (i2 == -1) {
            if (obj6 != null) {
                if (obj7 != null) {
                    throw NotFoundException.getNotFoundInstance();
                }
                r12 = obj3;
                obj3 = obj;
                obj = FINDER_PAT_B;
                obj5 = r12;
            } else if (obj7 == null) {
                throw NotFoundException.getNotFoundInstance();
            } else {
                obj3 = obj;
                obj = obj2;
            }
        } else if (i2 != 0) {
            throw NotFoundException.getNotFoundInstance();
        } else if (obj6 != null) {
            if (obj7 == null) {
                throw NotFoundException.getNotFoundInstance();
            } else if (count < count2) {
                obj4 = FINDER_PAT_B;
                r12 = obj3;
                obj3 = obj;
                obj = FINDER_PAT_B;
                obj5 = r12;
            } else {
                obj3 = FINDER_PAT_B;
                obj = obj2;
            }
        } else if (obj7 != null) {
            throw NotFoundException.getNotFoundInstance();
        } else {
            obj5 = obj3;
            obj3 = obj;
            obj = obj2;
        }
        if (obj != null) {
            if (obj3 != null) {
                throw NotFoundException.getNotFoundInstance();
            }
            AbstractRSSReader.increment(this.oddCounts, this.oddRoundingErrors);
        }
        if (obj3 != null) {
            AbstractRSSReader.decrement(this.oddCounts, this.oddRoundingErrors);
        }
        if (obj5 != null) {
            if (obj4 != null) {
                throw NotFoundException.getNotFoundInstance();
            }
            AbstractRSSReader.increment(this.evenCounts, this.oddRoundingErrors);
        }
        if (obj4 != null) {
            AbstractRSSReader.decrement(this.evenCounts, this.evenRoundingErrors);
        }
    }

    private boolean checkChecksum() {
        ExpandedPair expandedPair = (ExpandedPair) this.pairs.elementAt(LONGEST_SEQUENCE_SIZE);
        DataCharacter leftChar = expandedPair.getLeftChar();
        int i = FINDER_PAT_C;
        int checksumPortion = expandedPair.getRightChar().getChecksumPortion();
        for (int i2 = FINDER_PAT_B; i2 < this.pairs.size(); i2 += FINDER_PAT_B) {
            expandedPair = (ExpandedPair) this.pairs.elementAt(i2);
            checksumPortion += expandedPair.getLeftChar().getChecksumPortion();
            i += FINDER_PAT_B;
            if (expandedPair.getRightChar() != null) {
                checksumPortion += expandedPair.getRightChar().getChecksumPortion();
                i += FINDER_PAT_B;
            }
        }
        return (checksumPortion % 211) + ((i + -4) * 211) == leftChar.getValue();
    }

    private boolean checkPairSequence(Vector vector, FinderPattern finderPattern) throws NotFoundException {
        int size = vector.size() + FINDER_PAT_B;
        if (size > this.currentSequence.length) {
            throw NotFoundException.getNotFoundInstance();
        }
        int i;
        for (i = LONGEST_SEQUENCE_SIZE; i < vector.size(); i += FINDER_PAT_B) {
            this.currentSequence[i] = ((ExpandedPair) vector.elementAt(i)).getFinderPattern().getValue();
        }
        this.currentSequence[size - 1] = finderPattern.getValue();
        for (int i2 = LONGEST_SEQUENCE_SIZE; i2 < FINDER_PATTERN_SEQUENCES.length; i2 += FINDER_PAT_B) {
            int[] iArr = FINDER_PATTERN_SEQUENCES[i2];
            if (iArr.length >= size) {
                boolean z;
                for (i = LONGEST_SEQUENCE_SIZE; i < size; i += FINDER_PAT_B) {
                    if (this.currentSequence[i] != iArr[i]) {
                        z = false;
                        break;
                    }
                }
                z = FINDER_PAT_B;
                if (z) {
                    return size == iArr.length;
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static Result constructResult(Vector vector) throws NotFoundException {
        String parseInformation = AbstractExpandedDecoder.createDecoder(BitArrayBuilder.buildBitArray(vector)).parseInformation();
        ResultPoint[] resultPoints = ((ExpandedPair) vector.elementAt(LONGEST_SEQUENCE_SIZE)).getFinderPattern().getResultPoints();
        ResultPoint[] resultPoints2 = ((ExpandedPair) vector.lastElement()).getFinderPattern().getResultPoints();
        ResultPoint[] resultPointArr = new ResultPoint[FINDER_PAT_E];
        resultPointArr[LONGEST_SEQUENCE_SIZE] = resultPoints[LONGEST_SEQUENCE_SIZE];
        resultPointArr[FINDER_PAT_B] = resultPoints[FINDER_PAT_B];
        resultPointArr[FINDER_PAT_C] = resultPoints2[LONGEST_SEQUENCE_SIZE];
        resultPointArr[FINDER_PAT_D] = resultPoints2[FINDER_PAT_B];
        return new Result(parseInformation, null, resultPointArr, BarcodeFormat.RSS_EXPANDED);
    }

    private void findNextPair(BitArray bitArray, Vector vector, int i) throws NotFoundException {
        int[] iArr = this.decodeFinderCounters;
        iArr[LONGEST_SEQUENCE_SIZE] = LONGEST_SEQUENCE_SIZE;
        iArr[FINDER_PAT_B] = LONGEST_SEQUENCE_SIZE;
        iArr[FINDER_PAT_C] = LONGEST_SEQUENCE_SIZE;
        iArr[FINDER_PAT_D] = LONGEST_SEQUENCE_SIZE;
        int size = bitArray.getSize();
        if (i < 0) {
            i = vector.isEmpty() ? LONGEST_SEQUENCE_SIZE : ((ExpandedPair) vector.lastElement()).getFinderPattern().getStartEnd()[FINDER_PAT_B];
        }
        Object obj = vector.size() % FINDER_PAT_C != 0 ? FINDER_PAT_B : null;
        int i2 = LONGEST_SEQUENCE_SIZE;
        int i3 = i;
        while (i3 < size) {
            i2 = !bitArray.get(i3) ? FINDER_PAT_B : LONGEST_SEQUENCE_SIZE;
            if (i2 == 0) {
                break;
            }
            i3 += FINDER_PAT_B;
        }
        int i4 = i3;
        i3 = LONGEST_SEQUENCE_SIZE;
        int i5 = i2;
        i2 = i4;
        for (int i6 = i3; i6 < size; i6 += FINDER_PAT_B) {
            if ((bitArray.get(i6) ^ i5) != 0) {
                iArr[i3] = iArr[i3] + FINDER_PAT_B;
            } else {
                if (i3 == FINDER_PAT_D) {
                    if (obj != null) {
                        reverseCounters(iArr);
                    }
                    if (AbstractRSSReader.isFinderPattern(iArr)) {
                        this.startEnd[LONGEST_SEQUENCE_SIZE] = i2;
                        this.startEnd[FINDER_PAT_B] = i6;
                        return;
                    }
                    if (obj != null) {
                        reverseCounters(iArr);
                    }
                    i2 += iArr[LONGEST_SEQUENCE_SIZE] + iArr[FINDER_PAT_B];
                    iArr[LONGEST_SEQUENCE_SIZE] = iArr[FINDER_PAT_C];
                    iArr[FINDER_PAT_B] = iArr[FINDER_PAT_D];
                    iArr[FINDER_PAT_C] = LONGEST_SEQUENCE_SIZE;
                    iArr[FINDER_PAT_D] = LONGEST_SEQUENCE_SIZE;
                    i3--;
                } else {
                    i3 += FINDER_PAT_B;
                }
                iArr[i3] = FINDER_PAT_B;
                i5 = i5 == 0 ? FINDER_PAT_B : LONGEST_SEQUENCE_SIZE;
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static int getNextSecondBar(BitArray bitArray, int i) {
        boolean z = bitArray.get(i);
        while (i < bitArray.size && bitArray.get(i) == z) {
            i += FINDER_PAT_B;
        }
        z = !z;
        while (i < bitArray.size && bitArray.get(i) == r0) {
            i += FINDER_PAT_B;
        }
        return i;
    }

    private static boolean isNotA1left(FinderPattern finderPattern, boolean z, boolean z2) {
        return (finderPattern.getValue() == 0 && z && z2) ? false : true;
    }

    private FinderPattern parseFoundFinderPattern(BitArray bitArray, int i, boolean z) {
        int i2;
        int i3;
        int i4;
        int length;
        if (z) {
            i2 = this.startEnd[LONGEST_SEQUENCE_SIZE] - 1;
            while (i2 >= 0 && !bitArray.get(i2)) {
                i2--;
            }
            i3 = i2 + FINDER_PAT_B;
            i2 = this.startEnd[LONGEST_SEQUENCE_SIZE] - i3;
            i4 = this.startEnd[FINDER_PAT_B];
        } else {
            i3 = this.startEnd[LONGEST_SEQUENCE_SIZE];
            i2 = this.startEnd[FINDER_PAT_B] + FINDER_PAT_B;
            while (bitArray.get(i2) && i2 < bitArray.size) {
                i2 += FINDER_PAT_B;
            }
            i4 = i2;
            i2 -= this.startEnd[FINDER_PAT_B];
        }
        int[] iArr = this.decodeFinderCounters;
        for (length = iArr.length - 1; length > 0; length--) {
            iArr[length] = iArr[length - 1];
        }
        iArr[LONGEST_SEQUENCE_SIZE] = i2;
        try {
            length = AbstractRSSReader.parseFinderValue(iArr, FINDER_PATTERNS);
            iArr = new int[FINDER_PAT_C];
            iArr[LONGEST_SEQUENCE_SIZE] = i3;
            iArr[FINDER_PAT_B] = i4;
            return new FinderPattern(length, iArr, i3, i4, i);
        } catch (NotFoundException e) {
            return null;
        }
    }

    private static void reverseCounters(int[] iArr) {
        int length = iArr.length;
        for (int i = LONGEST_SEQUENCE_SIZE; i < length / FINDER_PAT_C; i += FINDER_PAT_B) {
            int i2 = iArr[i];
            iArr[i] = iArr[(length - i) - 1];
            iArr[(length - i) - 1] = i2;
        }
    }

    DataCharacter decodeDataCharacter(BitArray bitArray, FinderPattern finderPattern, boolean z, boolean z2) throws NotFoundException {
        int i;
        int length;
        int i2;
        int length2;
        int[] iArr = this.dataCharacterCounters;
        iArr[LONGEST_SEQUENCE_SIZE] = LONGEST_SEQUENCE_SIZE;
        iArr[FINDER_PAT_B] = LONGEST_SEQUENCE_SIZE;
        iArr[FINDER_PAT_C] = LONGEST_SEQUENCE_SIZE;
        iArr[FINDER_PAT_D] = LONGEST_SEQUENCE_SIZE;
        iArr[FINDER_PAT_E] = LONGEST_SEQUENCE_SIZE;
        iArr[FINDER_PAT_F] = LONGEST_SEQUENCE_SIZE;
        iArr[6] = LONGEST_SEQUENCE_SIZE;
        iArr[7] = LONGEST_SEQUENCE_SIZE;
        if (z2) {
            OneDReader.recordPatternInReverse(bitArray, finderPattern.getStartEnd()[LONGEST_SEQUENCE_SIZE], iArr);
        } else {
            OneDReader.recordPattern(bitArray, finderPattern.getStartEnd()[FINDER_PAT_B] + FINDER_PAT_B, iArr);
            i = LONGEST_SEQUENCE_SIZE;
            for (length = iArr.length - 1; i < length; length--) {
                i2 = iArr[i];
                iArr[i] = iArr[length];
                iArr[length] = i2;
                i += FINDER_PAT_B;
            }
        }
        float count = ((float) AbstractRSSReader.count(iArr)) / ((float) 17);
        int[] iArr2 = this.oddCounts;
        int[] iArr3 = this.evenCounts;
        float[] fArr = this.oddRoundingErrors;
        float[] fArr2 = this.evenRoundingErrors;
        for (length = LONGEST_SEQUENCE_SIZE; length < iArr.length; length += FINDER_PAT_B) {
            float f = (1.0f * ((float) iArr[length])) / count;
            i = (int) (0.5f + f);
            if (i < FINDER_PAT_B) {
                i = FINDER_PAT_B;
            } else if (i > 8) {
                i = 8;
            }
            int i3 = length >> FINDER_PAT_B;
            if ((length & FINDER_PAT_B) == 0) {
                iArr2[i3] = i;
                fArr[i3] = f - ((float) i);
            } else {
                iArr3[i3] = i;
                fArr2[i3] = f - ((float) i);
            }
        }
        adjustOddEvenCounts(17);
        int value = ((z2 ? LONGEST_SEQUENCE_SIZE : FINDER_PAT_B) + ((finderPattern.getValue() * FINDER_PAT_E) + (z ? LONGEST_SEQUENCE_SIZE : FINDER_PAT_C))) - 1;
        int i4 = LONGEST_SEQUENCE_SIZE;
        i = iArr2.length - 1;
        length = LONGEST_SEQUENCE_SIZE;
        while (i >= 0) {
            if (isNotA1left(finderPattern, z, z2)) {
                length += WEIGHTS[value][i * FINDER_PAT_C] * iArr2[i];
            }
            i--;
            i4 = iArr2[i] + i4;
        }
        i = LONGEST_SEQUENCE_SIZE;
        i2 = LONGEST_SEQUENCE_SIZE;
        for (length2 = iArr3.length - 1; length2 >= 0; length2--) {
            if (isNotA1left(finderPattern, z, z2)) {
                i += WEIGHTS[value][(length2 * FINDER_PAT_C) + FINDER_PAT_B] * iArr3[length2];
            }
            i2 += iArr3[length2];
        }
        length += i;
        if ((i4 & FINDER_PAT_B) != 0 || i4 > 13 || i4 < FINDER_PAT_E) {
            throw NotFoundException.getNotFoundInstance();
        }
        i = (13 - i4) / FINDER_PAT_C;
        length2 = SYMBOL_WIDEST[i];
        return new DataCharacter(GSUM[i] + ((RSSUtils.getRSSvalue(iArr2, length2, true) * EVEN_TOTAL_SUBSET[i]) + RSSUtils.getRSSvalue(iArr3, 9 - length2, false)), length);
    }

    public Result decodeRow(int i, BitArray bitArray, Hashtable hashtable) throws NotFoundException {
        reset();
        decodeRow2pairs(i, bitArray);
        return constructResult(this.pairs);
    }

    Vector decodeRow2pairs(int i, BitArray bitArray) throws NotFoundException {
        while (true) {
            ExpandedPair retrieveNextPair = retrieveNextPair(bitArray, this.pairs, i);
            this.pairs.addElement(retrieveNextPair);
            if (retrieveNextPair.mayBeLast()) {
                if (checkChecksum()) {
                    return this.pairs;
                }
                if (retrieveNextPair.mustBeLast()) {
                    break;
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    public void reset() {
        this.pairs.setSize(LONGEST_SEQUENCE_SIZE);
    }

    ExpandedPair retrieveNextPair(BitArray bitArray, Vector vector, int i) throws NotFoundException {
        FinderPattern parseFoundFinderPattern;
        DataCharacter decodeDataCharacter;
        boolean z = vector.size() % FINDER_PAT_C == 0;
        int i2 = -1;
        boolean z2 = true;
        do {
            findNextPair(bitArray, vector, i2);
            parseFoundFinderPattern = parseFoundFinderPattern(bitArray, i, z);
            if (parseFoundFinderPattern == null) {
                i2 = getNextSecondBar(bitArray, this.startEnd[LONGEST_SEQUENCE_SIZE]);
                continue;
            } else {
                z2 = LONGEST_SEQUENCE_SIZE;
                continue;
            }
        } while (z2);
        boolean checkPairSequence = checkPairSequence(vector, parseFoundFinderPattern);
        DataCharacter decodeDataCharacter2 = decodeDataCharacter(bitArray, parseFoundFinderPattern, z, true);
        try {
            decodeDataCharacter = decodeDataCharacter(bitArray, parseFoundFinderPattern, z, false);
        } catch (NotFoundException e) {
            if (checkPairSequence) {
                decodeDataCharacter = null;
            } else {
                throw e;
            }
        }
        return new ExpandedPair(decodeDataCharacter2, decodeDataCharacter, parseFoundFinderPattern, checkPairSequence);
    }
}
