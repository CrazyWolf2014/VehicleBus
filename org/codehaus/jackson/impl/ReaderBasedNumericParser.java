package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.Reader;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.IOContext;
import org.xbill.DNS.WKSRecord.Service;

public abstract class ReaderBasedNumericParser extends ReaderBasedParserBase {
    public ReaderBasedNumericParser(IOContext pc, int features, Reader r) {
        super(pc, features, r);
    }

    protected final JsonToken parseNumberText(int ch) throws IOException, JsonParseException {
        boolean negative = ch == 45;
        int i = this._inputPtr;
        int startPtr = i - 1;
        int inputLen = this._inputEnd;
        if (negative) {
            if (i < this._inputEnd) {
                int ptr = i + 1;
                ch = this._inputBuffer[i];
                if (ch > 57 || ch < 48) {
                    reportUnexpectedNumberChar(ch, "expected digit (0-9) to follow minus sign, for valid numeric value");
                }
                i = ptr;
            }
            if (negative) {
                startPtr++;
            }
            this._inputPtr = startPtr;
            return parseNumberText2(negative);
        }
        int intLen = 1;
        while (i < this._inputEnd) {
            ptr = i + 1;
            ch = this._inputBuffer[i];
            if (ch < 48 || ch > 57) {
                int fractLen = 0;
                if (ch == 46) {
                    while (ptr < inputLen) {
                        i = ptr + 1;
                        ch = this._inputBuffer[ptr];
                        if (ch < 48 || ch > 57) {
                            if (fractLen == 0) {
                                reportUnexpectedNumberChar(ch, "Decimal point not followed by a digit");
                            }
                            ptr = i;
                        } else {
                            fractLen++;
                            ptr = i;
                        }
                    }
                    i = ptr;
                    if (negative) {
                        startPtr++;
                    }
                    this._inputPtr = startPtr;
                    return parseNumberText2(negative);
                }
                int expLen = 0;
                if (ch == Service.HOSTNAME || ch == 69) {
                    if (ptr >= inputLen) {
                        i = ptr;
                    } else {
                        i = ptr + 1;
                        ch = this._inputBuffer[ptr];
                        if (ch != 45 && ch != 43) {
                            ptr = i;
                        } else if (i < inputLen) {
                            ptr = i + 1;
                            ch = this._inputBuffer[i];
                        }
                        while (ch <= 57 && ch >= 48) {
                            expLen++;
                            if (ptr >= inputLen) {
                                i = ptr;
                            } else {
                                i = ptr + 1;
                                ch = this._inputBuffer[ptr];
                                ptr = i;
                            }
                        }
                        if (expLen == 0) {
                            reportUnexpectedNumberChar(ch, "Exponent indicator not followed by a digit");
                        }
                    }
                    if (negative) {
                        startPtr++;
                    }
                    this._inputPtr = startPtr;
                    return parseNumberText2(negative);
                }
                i = ptr - 1;
                this._inputPtr = i;
                this._textBuffer.resetWithShared(this._inputBuffer, startPtr, i - startPtr);
                return reset(negative, intLen, fractLen, expLen);
            }
            intLen++;
            if (intLen == 2 && this._inputBuffer[ptr - 2] == '0') {
                reportInvalidNumber("Leading zeroes not allowed");
                i = ptr;
            } else {
                i = ptr;
            }
        }
        if (negative) {
            startPtr++;
        }
        this._inputPtr = startPtr;
        return parseNumberText2(negative);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final org.codehaus.jackson.JsonToken parseNumberText2(boolean r15) throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        /*
        r14 = this;
        r13 = 45;
        r12 = 57;
        r11 = 48;
        r8 = r14._textBuffer;
        r5 = r8.emptyAndGetCurrentSegment();
        r6 = 0;
        if (r15 == 0) goto L_0x0014;
    L_0x000f:
        r7 = r6 + 1;
        r5[r6] = r13;
        r6 = r7;
    L_0x0014:
        r4 = 0;
        r1 = 0;
    L_0x0016:
        r8 = r14._inputPtr;
        r9 = r14._inputEnd;
        if (r8 < r9) goto L_0x00eb;
    L_0x001c:
        r8 = r14.loadMore();
        if (r8 != 0) goto L_0x00eb;
    L_0x0022:
        r0 = 0;
        r1 = 1;
    L_0x0024:
        if (r4 != 0) goto L_0x0046;
    L_0x0026:
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "Missing integer part (next char ";
        r8 = r8.append(r9);
        r9 = org.codehaus.jackson.impl.JsonParserMinimalBase._getCharDesc(r0);
        r8 = r8.append(r9);
        r9 = ")";
        r8 = r8.append(r9);
        r8 = r8.toString();
        r14.reportInvalidNumber(r8);
    L_0x0046:
        r3 = 0;
        r8 = 46;
        if (r0 != r8) goto L_0x0064;
    L_0x004b:
        r7 = r6 + 1;
        r5[r6] = r0;
        r6 = r7;
    L_0x0050:
        r8 = r14._inputPtr;
        r9 = r14._inputEnd;
        if (r8 < r9) goto L_0x011a;
    L_0x0056:
        r8 = r14.loadMore();
        if (r8 != 0) goto L_0x011a;
    L_0x005c:
        r1 = 1;
    L_0x005d:
        if (r3 != 0) goto L_0x0064;
    L_0x005f:
        r8 = "Decimal point not followed by a digit";
        r14.reportUnexpectedNumberChar(r0, r8);
    L_0x0064:
        r2 = 0;
        r8 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        if (r0 == r8) goto L_0x006d;
    L_0x0069:
        r8 = 69;
        if (r0 != r8) goto L_0x00d9;
    L_0x006d:
        r8 = r5.length;
        if (r6 < r8) goto L_0x0077;
    L_0x0070:
        r8 = r14._textBuffer;
        r5 = r8.finishCurrentSegment();
        r6 = 0;
    L_0x0077:
        r7 = r6 + 1;
        r5[r6] = r0;
        r8 = r14._inputPtr;
        r9 = r14._inputEnd;
        if (r8 >= r9) goto L_0x013b;
    L_0x0081:
        r8 = r14._inputBuffer;
        r9 = r14._inputPtr;
        r10 = r9 + 1;
        r14._inputPtr = r10;
        r0 = r8[r9];
    L_0x008b:
        if (r0 == r13) goto L_0x0091;
    L_0x008d:
        r8 = 43;
        if (r0 != r8) goto L_0x015b;
    L_0x0091:
        r8 = r5.length;
        if (r7 < r8) goto L_0x0158;
    L_0x0094:
        r8 = r14._textBuffer;
        r5 = r8.finishCurrentSegment();
        r6 = 0;
    L_0x009b:
        r7 = r6 + 1;
        r5[r6] = r0;
        r8 = r14._inputPtr;
        r9 = r14._inputEnd;
        if (r8 >= r9) goto L_0x0143;
    L_0x00a5:
        r8 = r14._inputBuffer;
        r9 = r14._inputPtr;
        r10 = r9 + 1;
        r14._inputPtr = r10;
        r0 = r8[r9];
    L_0x00af:
        r6 = r7;
    L_0x00b0:
        if (r0 > r12) goto L_0x00d2;
    L_0x00b2:
        if (r0 < r11) goto L_0x00d2;
    L_0x00b4:
        r2 = r2 + 1;
        r8 = r5.length;
        if (r6 < r8) goto L_0x00c0;
    L_0x00b9:
        r8 = r14._textBuffer;
        r5 = r8.finishCurrentSegment();
        r6 = 0;
    L_0x00c0:
        r7 = r6 + 1;
        r5[r6] = r0;
        r8 = r14._inputPtr;
        r9 = r14._inputEnd;
        if (r8 < r9) goto L_0x014b;
    L_0x00ca:
        r8 = r14.loadMore();
        if (r8 != 0) goto L_0x014b;
    L_0x00d0:
        r1 = 1;
        r6 = r7;
    L_0x00d2:
        if (r2 != 0) goto L_0x00d9;
    L_0x00d4:
        r8 = "Exponent indicator not followed by a digit";
        r14.reportUnexpectedNumberChar(r0, r8);
    L_0x00d9:
        if (r1 != 0) goto L_0x00e1;
    L_0x00db:
        r8 = r14._inputPtr;
        r8 = r8 + -1;
        r14._inputPtr = r8;
    L_0x00e1:
        r8 = r14._textBuffer;
        r8.setCurrentLength(r6);
        r8 = r14.reset(r15, r4, r3, r2);
        return r8;
    L_0x00eb:
        r8 = r14._inputBuffer;
        r9 = r14._inputPtr;
        r10 = r9 + 1;
        r14._inputPtr = r10;
        r0 = r8[r9];
        if (r0 < r11) goto L_0x0024;
    L_0x00f7:
        if (r0 > r12) goto L_0x0024;
    L_0x00f9:
        r4 = r4 + 1;
        r8 = 2;
        if (r4 != r8) goto L_0x0109;
    L_0x00fe:
        r8 = r6 + -1;
        r8 = r5[r8];
        if (r8 != r11) goto L_0x0109;
    L_0x0104:
        r8 = "Leading zeroes not allowed";
        r14.reportInvalidNumber(r8);
    L_0x0109:
        r8 = r5.length;
        if (r6 < r8) goto L_0x0113;
    L_0x010c:
        r8 = r14._textBuffer;
        r5 = r8.finishCurrentSegment();
        r6 = 0;
    L_0x0113:
        r7 = r6 + 1;
        r5[r6] = r0;
        r6 = r7;
        goto L_0x0016;
    L_0x011a:
        r8 = r14._inputBuffer;
        r9 = r14._inputPtr;
        r10 = r9 + 1;
        r14._inputPtr = r10;
        r0 = r8[r9];
        if (r0 < r11) goto L_0x005d;
    L_0x0126:
        if (r0 > r12) goto L_0x005d;
    L_0x0128:
        r3 = r3 + 1;
        r8 = r5.length;
        if (r6 < r8) goto L_0x0134;
    L_0x012d:
        r8 = r14._textBuffer;
        r5 = r8.finishCurrentSegment();
        r6 = 0;
    L_0x0134:
        r7 = r6 + 1;
        r5[r6] = r0;
        r6 = r7;
        goto L_0x0050;
    L_0x013b:
        r8 = "expected a digit for number exponent";
        r0 = r14.getNextChar(r8);
        goto L_0x008b;
    L_0x0143:
        r8 = "expected a digit for number exponent";
        r0 = r14.getNextChar(r8);
        goto L_0x00af;
    L_0x014b:
        r8 = r14._inputBuffer;
        r9 = r14._inputPtr;
        r10 = r9 + 1;
        r14._inputPtr = r10;
        r0 = r8[r9];
        r6 = r7;
        goto L_0x00b0;
    L_0x0158:
        r6 = r7;
        goto L_0x009b;
    L_0x015b:
        r6 = r7;
        goto L_0x00b0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.ReaderBasedNumericParser.parseNumberText2(boolean):org.codehaus.jackson.JsonToken");
    }
}
