package org.codehaus.jackson.impl;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.codehaus.jackson.smile.SmileConstants;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.Name;
import org.codehaus.jackson.util.CharTypes;
import org.ksoap2.SoapEnvelope;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;

public final class Utf8StreamParser extends StreamBasedParserBase {
    private static final byte BYTE_0 = (byte) 0;
    static final byte BYTE_LF = (byte) 10;
    private static final int[] sInputCodesLatin1;
    private static final int[] sInputCodesUtf8;
    protected ObjectCodec _objectCodec;
    private int _quad1;
    protected int[] _quadBuffer;
    protected final BytesToNameCanonicalizer _symbols;
    protected boolean _tokenIncomplete;

    /* renamed from: org.codehaus.jackson.impl.Utf8StreamParser.1 */
    static /* synthetic */ class C09371 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    static {
        sInputCodesUtf8 = CharTypes.getInputCodeUtf8();
        sInputCodesLatin1 = CharTypes.getInputCodeLatin1();
    }

    public Utf8StreamParser(IOContext ctxt, int features, InputStream in, ObjectCodec codec, BytesToNameCanonicalizer sym, byte[] inputBuffer, int start, int end, boolean bufferRecyclable) {
        super(ctxt, features, in, inputBuffer, start, end, bufferRecyclable);
        this._quadBuffer = new int[16];
        this._tokenIncomplete = false;
        this._objectCodec = codec;
        this._symbols = sym;
        if (!Feature.CANONICALIZE_FIELD_NAMES.enabledIn(features)) {
            _throwInternal();
        }
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }

    public String getText() throws IOException, JsonParseException {
        JsonToken t = this._currToken;
        if (t != JsonToken.VALUE_STRING) {
            return _getText2(t);
        }
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            _finishString();
        }
        return this._textBuffer.contentsAsString();
    }

    protected final String _getText2(JsonToken t) {
        if (t == null) {
            return null;
        }
        switch (C09371.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return this._parsingContext.getCurrentName();
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return this._textBuffer.contentsAsString();
            default:
                return t.asString();
        }
    }

    public char[] getTextCharacters() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return null;
        }
        switch (C09371.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                if (!this._nameCopied) {
                    String name = this._parsingContext.getCurrentName();
                    int nameLen = name.length();
                    if (this._nameCopyBuffer == null) {
                        this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(nameLen);
                    } else if (this._nameCopyBuffer.length < nameLen) {
                        this._nameCopyBuffer = new char[nameLen];
                    }
                    name.getChars(0, nameLen, this._nameCopyBuffer, 0);
                    this._nameCopied = true;
                }
                return this._nameCopyBuffer;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                    break;
                }
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                break;
            default:
                return this._currToken.asCharArray();
        }
        return this._textBuffer.getTextBuffer();
    }

    public int getTextLength() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return 0;
        }
        switch (C09371.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return this._parsingContext.getCurrentName().length();
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                    break;
                }
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                break;
            default:
                return this._currToken.asCharArray().length;
        }
        return this._textBuffer.size();
    }

    public int getTextOffset() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return 0;
        }
        switch (C09371.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                    break;
                }
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                break;
            default:
                return 0;
        }
        return this._textBuffer.getTextOffset();
    }

    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException {
        if (this._currToken != JsonToken.VALUE_STRING && (this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT || this._binaryValue == null)) {
            _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
        }
        if (this._tokenIncomplete) {
            try {
                this._binaryValue = _decodeBase64(b64variant);
                this._tokenIncomplete = false;
            } catch (IllegalArgumentException iae) {
                throw _constructError("Failed to decode VALUE_STRING as base64 (" + b64variant + "): " + iae.getMessage());
            }
        }
        return this._binaryValue;
    }

    public JsonToken nextToken() throws IOException, JsonParseException {
        if (this._currToken == JsonToken.FIELD_NAME) {
            return _nextAfterName();
        }
        if (this._tokenIncomplete) {
            _skipString();
        }
        int i = _skipWSOrEnd();
        if (i < 0) {
            close();
            this._currToken = null;
            return null;
        }
        this._tokenInputTotal = (this._currInputProcessed + ((long) this._inputPtr)) - 1;
        this._tokenInputRow = this._currInputRow;
        this._tokenInputCol = (this._inputPtr - this._currInputRowStart) - 1;
        this._binaryValue = null;
        JsonToken jsonToken;
        if (i == 93) {
            if (!this._parsingContext.inArray()) {
                _reportMismatchedEndMarker(i, '}');
            }
            this._parsingContext = this._parsingContext.getParent();
            jsonToken = JsonToken.END_ARRAY;
            this._currToken = jsonToken;
            return jsonToken;
        } else if (i == Service.LOCUS_MAP) {
            if (!this._parsingContext.inObject()) {
                _reportMismatchedEndMarker(i, ']');
            }
            this._parsingContext = this._parsingContext.getParent();
            jsonToken = JsonToken.END_OBJECT;
            this._currToken = jsonToken;
            return jsonToken;
        } else {
            if (this._parsingContext.expectComma()) {
                if (i != 44) {
                    _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
                }
                i = _skipWS();
            }
            if (!this._parsingContext.inObject()) {
                return _nextTokenNotInObject(i);
            }
            this._parsingContext.setCurrentName(_parseFieldName(i).getName());
            this._currToken = JsonToken.FIELD_NAME;
            i = _skipWS();
            if (i != 58) {
                _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
            }
            i = _skipWS();
            if (i == 34) {
                this._tokenIncomplete = true;
                this._nextToken = JsonToken.VALUE_STRING;
                return this._currToken;
            }
            JsonToken t;
            switch (i) {
                case Service.MPM /*45*/:
                case Type.DNSKEY /*48*/:
                case Service.LOGIN /*49*/:
                case Type.NSEC3 /*50*/:
                case Service.LA_MAINT /*51*/:
                case Type.TLSA /*52*/:
                case SimpleResolver.DEFAULT_PORT /*53*/:
                case Opcodes.ISTORE /*54*/:
                case Service.ISI_GL /*55*/:
                case SmileConstants.MAX_SHORT_NAME_UNICODE_BYTES /*56*/:
                case Opcodes.DSTORE /*57*/:
                    t = parseNumberText(i);
                    break;
                case Service.MIT_DOV /*91*/:
                    t = JsonToken.START_ARRAY;
                    break;
                case Service.DCP /*93*/:
                case Service.LOCUS_MAP /*125*/:
                    _reportUnexpectedChar(i, "expected a value");
                    break;
                case Service.ISO_TSAP /*102*/:
                    _matchToken(JsonToken.VALUE_FALSE);
                    t = JsonToken.VALUE_FALSE;
                    break;
                case SoapEnvelope.VER11 /*110*/:
                    _matchToken(JsonToken.VALUE_NULL);
                    t = JsonToken.VALUE_NULL;
                    break;
                case Opcodes.INEG /*116*/:
                    break;
                case Service.NTP /*123*/:
                    t = JsonToken.START_OBJECT;
                    break;
                default:
                    t = _handleUnexpectedValue(i);
                    break;
            }
            _matchToken(JsonToken.VALUE_TRUE);
            t = JsonToken.VALUE_TRUE;
            this._nextToken = t;
            return this._currToken;
        }
    }

    private final JsonToken _nextTokenNotInObject(int i) throws IOException, JsonParseException {
        if (i == 34) {
            this._tokenIncomplete = true;
            JsonToken jsonToken = JsonToken.VALUE_STRING;
            this._currToken = jsonToken;
            return jsonToken;
        }
        switch (i) {
            case Service.MPM /*45*/:
            case Type.DNSKEY /*48*/:
            case Service.LOGIN /*49*/:
            case Type.NSEC3 /*50*/:
            case Service.LA_MAINT /*51*/:
            case Type.TLSA /*52*/:
            case SimpleResolver.DEFAULT_PORT /*53*/:
            case Opcodes.ISTORE /*54*/:
            case Service.ISI_GL /*55*/:
            case SmileConstants.MAX_SHORT_NAME_UNICODE_BYTES /*56*/:
            case Opcodes.DSTORE /*57*/:
                jsonToken = parseNumberText(i);
                this._currToken = jsonToken;
                return jsonToken;
            case Service.MIT_DOV /*91*/:
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                jsonToken = JsonToken.START_ARRAY;
                this._currToken = jsonToken;
                return jsonToken;
            case Service.DCP /*93*/:
            case Service.LOCUS_MAP /*125*/:
                _reportUnexpectedChar(i, "expected a value");
                break;
            case Service.ISO_TSAP /*102*/:
                _matchToken(JsonToken.VALUE_FALSE);
                jsonToken = JsonToken.VALUE_FALSE;
                this._currToken = jsonToken;
                return jsonToken;
            case SoapEnvelope.VER11 /*110*/:
                _matchToken(JsonToken.VALUE_NULL);
                jsonToken = JsonToken.VALUE_NULL;
                this._currToken = jsonToken;
                return jsonToken;
            case Opcodes.INEG /*116*/:
                break;
            case Service.NTP /*123*/:
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                jsonToken = JsonToken.START_OBJECT;
                this._currToken = jsonToken;
                return jsonToken;
            default:
                jsonToken = _handleUnexpectedValue(i);
                this._currToken = jsonToken;
                return jsonToken;
        }
        _matchToken(JsonToken.VALUE_TRUE);
        jsonToken = JsonToken.VALUE_TRUE;
        this._currToken = jsonToken;
        return jsonToken;
    }

    private final JsonToken _nextAfterName() {
        this._nameCopied = false;
        JsonToken t = this._nextToken;
        this._nextToken = null;
        if (t == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        } else if (t == JsonToken.START_OBJECT) {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        this._currToken = t;
        return t;
    }

    public void close() throws IOException {
        super.close();
        this._symbols.release();
    }

    protected final JsonToken parseNumberText(int c) throws IOException, JsonParseException {
        int outPtr;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        boolean negative = c == 45;
        if (negative) {
            outPtr = 0 + 1;
            outBuf[0] = SignatureVisitor.SUPER;
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            c = bArr[i] & KEYRecord.PROTOCOL_ANY;
            if (c < 48 || c > 57) {
                reportInvalidNumber("Missing integer part (next char " + JsonParserMinimalBase._getCharDesc(c) + ")");
            }
        } else {
            outPtr = 0;
        }
        if (c == 48) {
            _verifyNoLeadingZeroes();
        }
        int outPtr2 = outPtr + 1;
        outBuf[outPtr] = (char) c;
        int intLen = 1;
        int end = this._inputPtr + outBuf.length;
        if (end > this._inputEnd) {
            end = this._inputEnd;
        }
        while (this._inputPtr < end) {
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            c = bArr[i] & KEYRecord.PROTOCOL_ANY;
            if (c >= 48 && c <= 57) {
                intLen++;
                outPtr = outPtr2 + 1;
                outBuf[outPtr2] = (char) c;
                outPtr2 = outPtr;
            } else if (c == 46 || c == Service.HOSTNAME || c == 69) {
                return _parseFloatText(outBuf, outPtr2, c, negative, intLen);
            } else {
                this._inputPtr--;
                this._textBuffer.setCurrentLength(outPtr2);
                return resetInt(negative, intLen);
            }
        }
        return _parserNumber2(outBuf, outPtr2, negative, intLen);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final org.codehaus.jackson.JsonToken _parserNumber2(char[] r8, int r9, boolean r10, int r11) throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        /*
        r7 = this;
    L_0x0000:
        r0 = r7._inputPtr;
        r1 = r7._inputEnd;
        if (r0 < r1) goto L_0x0016;
    L_0x0006:
        r0 = r7.loadMore();
        if (r0 != 0) goto L_0x0016;
    L_0x000c:
        r0 = r7._textBuffer;
        r0.setCurrentLength(r9);
        r0 = r7.resetInt(r10, r11);
    L_0x0015:
        return r0;
    L_0x0016:
        r0 = r7._inputBuffer;
        r1 = r7._inputPtr;
        r2 = r1 + 1;
        r7._inputPtr = r2;
        r0 = r0[r1];
        r3 = r0 & 255;
        r0 = 57;
        if (r3 > r0) goto L_0x002a;
    L_0x0026:
        r0 = 48;
        if (r3 >= r0) goto L_0x0040;
    L_0x002a:
        r0 = 46;
        if (r3 == r0) goto L_0x0036;
    L_0x002e:
        r0 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        if (r3 == r0) goto L_0x0036;
    L_0x0032:
        r0 = 69;
        if (r3 != r0) goto L_0x0053;
    L_0x0036:
        r0 = r7;
        r1 = r8;
        r2 = r9;
        r4 = r10;
        r5 = r11;
        r0 = r0._parseFloatText(r1, r2, r3, r4, r5);
        goto L_0x0015;
    L_0x0040:
        r0 = r8.length;
        if (r9 < r0) goto L_0x004a;
    L_0x0043:
        r0 = r7._textBuffer;
        r8 = r0.finishCurrentSegment();
        r9 = 0;
    L_0x004a:
        r6 = r9 + 1;
        r0 = (char) r3;
        r8[r9] = r0;
        r11 = r11 + 1;
        r9 = r6;
        goto L_0x0000;
    L_0x0053:
        r0 = r7._inputPtr;
        r0 = r0 + -1;
        r7._inputPtr = r0;
        r0 = r7._textBuffer;
        r0.setCurrentLength(r9);
        r0 = r7.resetInt(r10, r11);
        goto L_0x0015;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.Utf8StreamParser._parserNumber2(char[], int, boolean, int):org.codehaus.jackson.JsonToken");
    }

    private final void _verifyNoLeadingZeroes() throws IOException, JsonParseException {
        if ((this._inputPtr < this._inputEnd || loadMore()) && this._inputBuffer[this._inputPtr] == null) {
            reportInvalidNumber("Leading zeroes not allowed");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final org.codehaus.jackson.JsonToken _parseFloatText(char[] r10, int r11, int r12, boolean r13, int r14) throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        /*
        r9 = this;
        r8 = 57;
        r7 = 48;
        r2 = 0;
        r0 = 0;
        r4 = 46;
        if (r12 != r4) goto L_0x0024;
    L_0x000a:
        r3 = r11 + 1;
        r4 = (char) r12;
        r10[r11] = r4;
        r11 = r3;
    L_0x0010:
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x00ba;
    L_0x0016:
        r4 = r9.loadMore();
        if (r4 != 0) goto L_0x00ba;
    L_0x001c:
        r0 = 1;
    L_0x001d:
        if (r2 != 0) goto L_0x0024;
    L_0x001f:
        r4 = "Decimal point not followed by a digit";
        r9.reportUnexpectedNumberChar(r12, r4);
    L_0x0024:
        r1 = 0;
        r4 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        if (r12 == r4) goto L_0x002d;
    L_0x0029:
        r4 = 69;
        if (r12 != r4) goto L_0x00a8;
    L_0x002d:
        r4 = r10.length;
        if (r11 < r4) goto L_0x0037;
    L_0x0030:
        r4 = r9._textBuffer;
        r10 = r4.finishCurrentSegment();
        r11 = 0;
    L_0x0037:
        r3 = r11 + 1;
        r4 = (char) r12;
        r10[r11] = r4;
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x0045;
    L_0x0042:
        r9.loadMoreGuaranteed();
    L_0x0045:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r12 = r4 & 255;
        r4 = 45;
        if (r12 == r4) goto L_0x0059;
    L_0x0055:
        r4 = 43;
        if (r12 != r4) goto L_0x00ef;
    L_0x0059:
        r4 = r10.length;
        if (r3 < r4) goto L_0x00ec;
    L_0x005c:
        r4 = r9._textBuffer;
        r10 = r4.finishCurrentSegment();
        r11 = 0;
    L_0x0063:
        r3 = r11 + 1;
        r4 = (char) r12;
        r10[r11] = r4;
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x0071;
    L_0x006e:
        r9.loadMoreGuaranteed();
    L_0x0071:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r12 = r4 & 255;
        r11 = r3;
    L_0x007e:
        if (r12 > r8) goto L_0x00a1;
    L_0x0080:
        if (r12 < r7) goto L_0x00a1;
    L_0x0082:
        r1 = r1 + 1;
        r4 = r10.length;
        if (r11 < r4) goto L_0x008e;
    L_0x0087:
        r4 = r9._textBuffer;
        r10 = r4.finishCurrentSegment();
        r11 = 0;
    L_0x008e:
        r3 = r11 + 1;
        r4 = (char) r12;
        r10[r11] = r4;
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x00de;
    L_0x0099:
        r4 = r9.loadMore();
        if (r4 != 0) goto L_0x00de;
    L_0x009f:
        r0 = 1;
        r11 = r3;
    L_0x00a1:
        if (r1 != 0) goto L_0x00a8;
    L_0x00a3:
        r4 = "Exponent indicator not followed by a digit";
        r9.reportUnexpectedNumberChar(r12, r4);
    L_0x00a8:
        if (r0 != 0) goto L_0x00b0;
    L_0x00aa:
        r4 = r9._inputPtr;
        r4 = r4 + -1;
        r9._inputPtr = r4;
    L_0x00b0:
        r4 = r9._textBuffer;
        r4.setCurrentLength(r11);
        r4 = r9.resetFloat(r13, r14, r2, r1);
        return r4;
    L_0x00ba:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r12 = r4 & 255;
        if (r12 < r7) goto L_0x001d;
    L_0x00c8:
        if (r12 > r8) goto L_0x001d;
    L_0x00ca:
        r2 = r2 + 1;
        r4 = r10.length;
        if (r11 < r4) goto L_0x00d6;
    L_0x00cf:
        r4 = r9._textBuffer;
        r10 = r4.finishCurrentSegment();
        r11 = 0;
    L_0x00d6:
        r3 = r11 + 1;
        r4 = (char) r12;
        r10[r11] = r4;
        r11 = r3;
        goto L_0x0010;
    L_0x00de:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r12 = r4 & 255;
        r11 = r3;
        goto L_0x007e;
    L_0x00ec:
        r11 = r3;
        goto L_0x0063;
    L_0x00ef:
        r11 = r3;
        goto L_0x007e;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.Utf8StreamParser._parseFloatText(char[], int, int, boolean, int):org.codehaus.jackson.JsonToken");
    }

    protected final Name _parseFieldName(int i) throws IOException, JsonParseException {
        if (i != 34) {
            return _handleUnusualFieldName(i);
        }
        if (this._inputPtr + 9 > this._inputEnd) {
            return slowParseFieldName();
        }
        byte[] input = this._inputBuffer;
        int[] codes = sInputCodesLatin1;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        int q = input[i2] & KEYRecord.PROTOCOL_ANY;
        if (codes[q] == 0) {
            i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            i = input[i2] & KEYRecord.PROTOCOL_ANY;
            if (codes[i] == 0) {
                q = (q << 8) | i;
                i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                i = input[i2] & KEYRecord.PROTOCOL_ANY;
                if (codes[i] == 0) {
                    q = (q << 8) | i;
                    i2 = this._inputPtr;
                    this._inputPtr = i2 + 1;
                    i = input[i2] & KEYRecord.PROTOCOL_ANY;
                    if (codes[i] == 0) {
                        q = (q << 8) | i;
                        i2 = this._inputPtr;
                        this._inputPtr = i2 + 1;
                        i = input[i2] & KEYRecord.PROTOCOL_ANY;
                        if (codes[i] == 0) {
                            this._quad1 = q;
                            return parseMediumFieldName(i, codes);
                        } else if (i == 34) {
                            return findName(q, 4);
                        } else {
                            return parseFieldName(q, i, 4);
                        }
                    } else if (i == 34) {
                        return findName(q, 3);
                    } else {
                        return parseFieldName(q, i, 3);
                    }
                } else if (i == 34) {
                    return findName(q, 2);
                } else {
                    return parseFieldName(q, i, 2);
                }
            } else if (i == 34) {
                return findName(q, 1);
            } else {
                return parseFieldName(q, i, 1);
            }
        } else if (q == 34) {
            return BytesToNameCanonicalizer.getEmptyName();
        } else {
            return parseFieldName(0, q, 0);
        }
    }

    protected final Name parseMediumFieldName(int q2, int[] codes) throws IOException, JsonParseException {
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
        if (codes[i2] == 0) {
            q2 = (q2 << 8) | i2;
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
            if (codes[i2] == 0) {
                q2 = (q2 << 8) | i2;
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
                if (codes[i2] == 0) {
                    q2 = (q2 << 8) | i2;
                    bArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
                    if (codes[i2] == 0) {
                        this._quadBuffer[0] = this._quad1;
                        this._quadBuffer[1] = q2;
                        return parseLongFieldName(i2);
                    } else if (i2 == 34) {
                        return findName(this._quad1, q2, 4);
                    } else {
                        return parseFieldName(this._quad1, q2, i2, 4);
                    }
                } else if (i2 == 34) {
                    return findName(this._quad1, q2, 3);
                } else {
                    return parseFieldName(this._quad1, q2, i2, 3);
                }
            } else if (i2 == 34) {
                return findName(this._quad1, q2, 2);
            } else {
                return parseFieldName(this._quad1, q2, i2, 2);
            }
        } else if (i2 == 34) {
            return findName(this._quad1, q2, 1);
        } else {
            return parseFieldName(this._quad1, q2, i2, 1);
        }
    }

    protected Name parseLongFieldName(int q) throws IOException, JsonParseException {
        int[] codes = sInputCodesLatin1;
        int qlen = 2;
        while (this._inputEnd - this._inputPtr >= 4) {
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
            if (codes[i2] == 0) {
                q = (q << 8) | i2;
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
                if (codes[i2] == 0) {
                    q = (q << 8) | i2;
                    bArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
                    if (codes[i2] == 0) {
                        q = (q << 8) | i2;
                        bArr = this._inputBuffer;
                        i = this._inputPtr;
                        this._inputPtr = i + 1;
                        i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
                        if (codes[i2] == 0) {
                            if (qlen >= this._quadBuffer.length) {
                                this._quadBuffer = growArrayBy(this._quadBuffer, qlen);
                            }
                            int qlen2 = qlen + 1;
                            this._quadBuffer[qlen] = q;
                            q = i2;
                            qlen = qlen2;
                        } else if (i2 == 34) {
                            return findName(this._quadBuffer, qlen, q, 4);
                        } else {
                            return parseEscapedFieldName(this._quadBuffer, qlen, q, i2, 4);
                        }
                    } else if (i2 == 34) {
                        return findName(this._quadBuffer, qlen, q, 3);
                    } else {
                        return parseEscapedFieldName(this._quadBuffer, qlen, q, i2, 3);
                    }
                } else if (i2 == 34) {
                    return findName(this._quadBuffer, qlen, q, 2);
                } else {
                    return parseEscapedFieldName(this._quadBuffer, qlen, q, i2, 2);
                }
            } else if (i2 == 34) {
                return findName(this._quadBuffer, qlen, q, 1);
            } else {
                return parseEscapedFieldName(this._quadBuffer, qlen, q, i2, 1);
            }
        }
        return parseEscapedFieldName(this._quadBuffer, qlen, 0, q, 0);
    }

    protected Name slowParseFieldName() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(": was expecting closing '\"' for name");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
        if (i2 == 34) {
            return BytesToNameCanonicalizer.getEmptyName();
        }
        return parseEscapedFieldName(this._quadBuffer, 0, 0, i2, 0);
    }

    private final Name parseFieldName(int q1, int ch, int lastQuadBytes) throws IOException, JsonParseException {
        return parseEscapedFieldName(this._quadBuffer, 0, q1, ch, lastQuadBytes);
    }

    private final Name parseFieldName(int q1, int q2, int ch, int lastQuadBytes) throws IOException, JsonParseException {
        this._quadBuffer[0] = q1;
        return parseEscapedFieldName(this._quadBuffer, 1, q2, ch, lastQuadBytes);
    }

    protected Name parseEscapedFieldName(int[] quads, int qlen, int currQuad, int ch, int currQuadBytes) throws IOException, JsonParseException {
        int[] codes = sInputCodesLatin1;
        while (true) {
            int qlen2;
            byte[] bArr;
            int i;
            if (codes[ch] != 0) {
                if (ch == 34) {
                    break;
                }
                if (ch != 92) {
                    _throwUnquotedSpace(ch, "name");
                } else {
                    ch = _decodeEscaped();
                }
                if (ch > Service.LOCUS_CON) {
                    if (currQuadBytes >= 4) {
                        if (qlen >= quads.length) {
                            quads = growArrayBy(quads, quads.length);
                            this._quadBuffer = quads;
                        }
                        qlen2 = qlen + 1;
                        quads[qlen] = currQuad;
                        currQuad = 0;
                        currQuadBytes = 0;
                    } else {
                        qlen2 = qlen;
                    }
                    if (ch < Flags.FLAG4) {
                        currQuad = (currQuad << 8) | ((ch >> 6) | Wbxml.EXT_0);
                        currQuadBytes++;
                        qlen = qlen2;
                    } else {
                        currQuad = (currQuad << 8) | ((ch >> 12) | SmileConstants.TOKEN_PREFIX_MISC_OTHER);
                        currQuadBytes++;
                        if (currQuadBytes >= 4) {
                            if (qlen2 >= quads.length) {
                                quads = growArrayBy(quads, quads.length);
                                this._quadBuffer = quads;
                            }
                            qlen = qlen2 + 1;
                            quads[qlen2] = currQuad;
                            currQuad = 0;
                            currQuadBytes = 0;
                        } else {
                            qlen = qlen2;
                        }
                        currQuad = (currQuad << 8) | (((ch >> 6) & 63) | Flags.FLAG8);
                        currQuadBytes++;
                    }
                    ch = (ch & 63) | Flags.FLAG8;
                    qlen2 = qlen;
                    if (currQuadBytes >= 4) {
                        currQuadBytes++;
                        currQuad = (currQuad << 8) | ch;
                        qlen = qlen2;
                    } else {
                        if (qlen2 >= quads.length) {
                            quads = growArrayBy(quads, quads.length);
                            this._quadBuffer = quads;
                        }
                        qlen = qlen2 + 1;
                        quads[qlen2] = currQuad;
                        currQuad = ch;
                        currQuadBytes = 1;
                    }
                    if (this._inputPtr >= this._inputEnd && !loadMore()) {
                        _reportInvalidEOF(" in field name");
                    }
                    bArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    ch = bArr[i] & KEYRecord.PROTOCOL_ANY;
                }
            }
            qlen2 = qlen;
            if (currQuadBytes >= 4) {
                if (qlen2 >= quads.length) {
                    quads = growArrayBy(quads, quads.length);
                    this._quadBuffer = quads;
                }
                qlen = qlen2 + 1;
                quads[qlen2] = currQuad;
                currQuad = ch;
                currQuadBytes = 1;
            } else {
                currQuadBytes++;
                currQuad = (currQuad << 8) | ch;
                qlen = qlen2;
            }
            _reportInvalidEOF(" in field name");
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = bArr[i] & KEYRecord.PROTOCOL_ANY;
        }
        if (currQuadBytes > 0) {
            if (qlen >= quads.length) {
                quads = growArrayBy(quads, quads.length);
                this._quadBuffer = quads;
            }
            qlen2 = qlen + 1;
            quads[qlen] = currQuad;
            qlen = qlen2;
        }
        Name name = this._symbols.findName(quads, qlen);
        if (name == null) {
            return addName(quads, qlen, currQuadBytes);
        }
        return name;
    }

    protected final Name _handleUnusualFieldName(int ch) throws IOException, JsonParseException {
        if (ch == 39 && isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            return _parseApostropheFieldName();
        }
        int qlen;
        if (!isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            _reportUnexpectedChar(ch, "was expecting double-quote to start field name");
        }
        int[] codes = CharTypes.getInputCodeUtf8JsNames();
        if (codes[ch] != 0) {
            _reportUnexpectedChar(ch, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int[] quads = this._quadBuffer;
        int currQuad = 0;
        int currQuadBytes = 0;
        int qlen2 = 0;
        while (true) {
            if (currQuadBytes < 4) {
                currQuadBytes++;
                currQuad = (currQuad << 8) | ch;
                qlen = qlen2;
            } else {
                if (qlen2 >= quads.length) {
                    quads = growArrayBy(quads, quads.length);
                    this._quadBuffer = quads;
                }
                qlen = qlen2 + 1;
                quads[qlen2] = currQuad;
                currQuad = ch;
                currQuadBytes = 1;
            }
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in field name");
            }
            ch = this._inputBuffer[this._inputPtr] & KEYRecord.PROTOCOL_ANY;
            if (codes[ch] != 0) {
                break;
            }
            this._inputPtr++;
            qlen2 = qlen;
        }
        if (currQuadBytes > 0) {
            if (qlen >= quads.length) {
                quads = growArrayBy(quads, quads.length);
                this._quadBuffer = quads;
            }
            qlen2 = qlen + 1;
            quads[qlen] = currQuad;
            qlen = qlen2;
        }
        Name name = this._symbols.findName(quads, qlen);
        if (name == null) {
            return addName(quads, qlen, currQuadBytes);
        }
        return name;
    }

    protected final Name _parseApostropheFieldName() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(": was expecting closing ''' for name");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int ch = bArr[i] & KEYRecord.PROTOCOL_ANY;
        if (ch == 39) {
            return BytesToNameCanonicalizer.getEmptyName();
        }
        int qlen;
        int[] quads = this._quadBuffer;
        int currQuad = 0;
        int currQuadBytes = 0;
        int[] codes = sInputCodesLatin1;
        int qlen2 = 0;
        while (ch != 39) {
            if (!(ch == 34 || codes[ch] == 0)) {
                if (ch != 92) {
                    _throwUnquotedSpace(ch, "name");
                } else {
                    ch = _decodeEscaped();
                }
                if (ch > Service.LOCUS_CON) {
                    if (currQuadBytes >= 4) {
                        if (qlen2 >= quads.length) {
                            quads = growArrayBy(quads, quads.length);
                            this._quadBuffer = quads;
                        }
                        qlen = qlen2 + 1;
                        quads[qlen2] = currQuad;
                        currQuad = 0;
                        currQuadBytes = 0;
                        qlen2 = qlen;
                    }
                    if (ch < Flags.FLAG4) {
                        currQuad = (currQuad << 8) | ((ch >> 6) | Wbxml.EXT_0);
                        currQuadBytes++;
                        qlen = qlen2;
                    } else {
                        currQuad = (currQuad << 8) | ((ch >> 12) | SmileConstants.TOKEN_PREFIX_MISC_OTHER);
                        currQuadBytes++;
                        if (currQuadBytes >= 4) {
                            if (qlen2 >= quads.length) {
                                quads = growArrayBy(quads, quads.length);
                                this._quadBuffer = quads;
                            }
                            qlen = qlen2 + 1;
                            quads[qlen2] = currQuad;
                            currQuad = 0;
                            currQuadBytes = 0;
                        } else {
                            qlen = qlen2;
                        }
                        currQuad = (currQuad << 8) | (((ch >> 6) & 63) | Flags.FLAG8);
                        currQuadBytes++;
                    }
                    ch = (ch & 63) | Flags.FLAG8;
                    qlen2 = qlen;
                }
            }
            if (currQuadBytes < 4) {
                currQuadBytes++;
                currQuad = (currQuad << 8) | ch;
                qlen = qlen2;
            } else {
                if (qlen2 >= quads.length) {
                    quads = growArrayBy(quads, quads.length);
                    this._quadBuffer = quads;
                }
                qlen = qlen2 + 1;
                quads[qlen2] = currQuad;
                currQuad = ch;
                currQuadBytes = 1;
            }
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in field name");
            }
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            ch = bArr[i] & KEYRecord.PROTOCOL_ANY;
            qlen2 = qlen;
        }
        if (currQuadBytes > 0) {
            if (qlen2 >= quads.length) {
                quads = growArrayBy(quads, quads.length);
                this._quadBuffer = quads;
            }
            qlen = qlen2 + 1;
            quads[qlen2] = currQuad;
        } else {
            qlen = qlen2;
        }
        Name name = this._symbols.findName(quads, qlen);
        if (name == null) {
            return addName(quads, qlen, currQuadBytes);
        }
        return name;
    }

    private final Name findName(int q1, int lastQuadBytes) throws JsonParseException {
        Name name = this._symbols.findName(q1);
        if (name != null) {
            return name;
        }
        this._quadBuffer[0] = q1;
        return addName(this._quadBuffer, 1, lastQuadBytes);
    }

    private final Name findName(int q1, int q2, int lastQuadBytes) throws JsonParseException {
        Name name = this._symbols.findName(q1, q2);
        if (name != null) {
            return name;
        }
        this._quadBuffer[0] = q1;
        this._quadBuffer[1] = q2;
        return addName(this._quadBuffer, 2, lastQuadBytes);
    }

    private final Name findName(int[] quads, int qlen, int lastQuad, int lastQuadBytes) throws JsonParseException {
        if (qlen >= quads.length) {
            quads = growArrayBy(quads, quads.length);
            this._quadBuffer = quads;
        }
        int qlen2 = qlen + 1;
        quads[qlen] = lastQuad;
        Name name = this._symbols.findName(quads, qlen2);
        if (name == null) {
            return addName(quads, qlen2, lastQuadBytes);
        }
        return name;
    }

    private final Name addName(int[] quads, int qlen, int lastQuadBytes) throws JsonParseException {
        int lastQuad;
        int byteLen = ((qlen << 2) - 4) + lastQuadBytes;
        if (lastQuadBytes < 4) {
            lastQuad = quads[qlen - 1];
            quads[qlen - 1] = lastQuad << ((4 - lastQuadBytes) << 3);
        } else {
            lastQuad = 0;
        }
        char[] cbuf = this._textBuffer.emptyAndGetCurrentSegment();
        int ix = 0;
        int cix = 0;
        while (ix < byteLen) {
            int i;
            int i2 = (quads[ix >> 2] >> ((3 - (ix & 3)) << 3)) & KEYRecord.PROTOCOL_ANY;
            ix++;
            if (i2 > Service.LOCUS_CON) {
                int needed;
                if ((i2 & SmileConstants.TOKEN_PREFIX_MISC_OTHER) == Wbxml.EXT_0) {
                    i2 &= 31;
                    needed = 1;
                } else if ((i2 & 240) == SmileConstants.TOKEN_PREFIX_MISC_OTHER) {
                    i2 &= 15;
                    needed = 2;
                } else if ((i2 & 248) == 240) {
                    i2 &= 7;
                    needed = 3;
                } else {
                    _reportInvalidInitial(i2);
                    i2 = 1;
                    needed = 1;
                }
                if (ix + needed > byteLen) {
                    _reportInvalidEOF(" in field name");
                }
                int ch2 = quads[ix >> 2] >> ((3 - (ix & 3)) << 3);
                ix++;
                if ((ch2 & Wbxml.EXT_0) != Flags.FLAG8) {
                    _reportInvalidOther(ch2);
                }
                i2 = (i2 << 6) | (ch2 & 63);
                if (needed > 1) {
                    ch2 = quads[ix >> 2] >> ((3 - (ix & 3)) << 3);
                    ix++;
                    if ((ch2 & Wbxml.EXT_0) != Flags.FLAG8) {
                        _reportInvalidOther(ch2);
                    }
                    i2 = (i2 << 6) | (ch2 & 63);
                    if (needed > 2) {
                        ch2 = quads[ix >> 2] >> ((3 - (ix & 3)) << 3);
                        ix++;
                        if ((ch2 & Wbxml.EXT_0) != Flags.FLAG8) {
                            _reportInvalidOther(ch2 & KEYRecord.PROTOCOL_ANY);
                        }
                        i2 = (i2 << 6) | (ch2 & 63);
                    }
                }
                if (needed > 2) {
                    i2 -= AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED;
                    if (cix >= cbuf.length) {
                        cbuf = this._textBuffer.expandCurrentSegment();
                    }
                    i = cix + 1;
                    cbuf[cix] = (char) (55296 + (i2 >> 10));
                    i2 = 56320 | (i2 & 1023);
                    if (i >= cbuf.length) {
                        cbuf = this._textBuffer.expandCurrentSegment();
                    }
                    cix = i + 1;
                    cbuf[i] = (char) i2;
                }
            }
            i = cix;
            if (i >= cbuf.length) {
                cbuf = this._textBuffer.expandCurrentSegment();
            }
            cix = i + 1;
            cbuf[i] = (char) i2;
        }
        String baseName = new String(cbuf, 0, cix);
        if (lastQuadBytes < 4) {
            quads[qlen - 1] = lastQuad;
        }
        return this._symbols.addName(baseName, quads, qlen);
    }

    protected void _finishString() throws IOException, JsonParseException {
        int ptr = this._inputPtr;
        if (ptr >= this._inputEnd) {
            loadMoreGuaranteed();
            ptr = this._inputPtr;
        }
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int[] codes = sInputCodesUtf8;
        int max = Math.min(this._inputEnd, outBuf.length + ptr);
        byte[] inputBuffer = this._inputBuffer;
        int outPtr = 0;
        while (ptr < max) {
            int c = inputBuffer[ptr] & KEYRecord.PROTOCOL_ANY;
            if (codes[c] != 0) {
                if (c == 34) {
                    this._inputPtr = ptr + 1;
                    this._textBuffer.setCurrentLength(outPtr);
                    return;
                }
                this._inputPtr = ptr;
                _finishString2(outBuf, outPtr);
            }
            ptr++;
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = (char) c;
            outPtr = outPtr2;
        }
        this._inputPtr = ptr;
        _finishString2(outBuf, outPtr);
    }

    private final void _finishString2(char[] outBuf, int outPtr) throws IOException, JsonParseException {
        int[] codes = sInputCodesUtf8;
        byte[] inputBuffer = this._inputBuffer;
        while (true) {
            int ptr = this._inputPtr;
            if (ptr >= this._inputEnd) {
                loadMoreGuaranteed();
                ptr = this._inputPtr;
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int max = Math.min(this._inputEnd, (outBuf.length - outPtr) + ptr);
            int ptr2 = ptr;
            int outPtr2 = outPtr;
            while (ptr2 < max) {
                ptr = ptr2 + 1;
                int c = inputBuffer[ptr2] & KEYRecord.PROTOCOL_ANY;
                if (codes[c] != 0) {
                    this._inputPtr = ptr;
                    if (c == 34) {
                        this._textBuffer.setCurrentLength(outPtr2);
                        return;
                    }
                    switch (codes[c]) {
                        case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                            c = _decodeEscaped();
                            outPtr = outPtr2;
                            break;
                        case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                            c = _decodeUtf8_2(c);
                            outPtr = outPtr2;
                            break;
                        case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                            if (this._inputEnd - this._inputPtr < 2) {
                                c = _decodeUtf8_3(c);
                                outPtr = outPtr2;
                                break;
                            }
                            c = _decodeUtf8_3fast(c);
                            outPtr = outPtr2;
                            break;
                        case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                            c = _decodeUtf8_4(c);
                            outPtr = outPtr2 + 1;
                            outBuf[outPtr2] = (char) (55296 | (c >> 10));
                            if (outPtr >= outBuf.length) {
                                outBuf = this._textBuffer.finishCurrentSegment();
                                outPtr = 0;
                            }
                            c = 56320 | (c & 1023);
                            break;
                        default:
                            if (c >= 32) {
                                _reportInvalidChar(c);
                                outPtr = outPtr2;
                                break;
                            }
                            _throwUnquotedSpace(c, "string value");
                            outPtr = outPtr2;
                            break;
                    }
                    if (outPtr >= outBuf.length) {
                        outBuf = this._textBuffer.finishCurrentSegment();
                        outPtr = 0;
                    }
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (char) c;
                    outPtr = outPtr2;
                } else {
                    outPtr = outPtr2 + 1;
                    outBuf[outPtr2] = (char) c;
                    ptr2 = ptr;
                    outPtr2 = outPtr;
                }
            }
            this._inputPtr = ptr2;
            outPtr = outPtr2;
        }
    }

    protected void _skipString() throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:42)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:66)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r7 = this;
        r6 = 0;
        r7._tokenIncomplete = r6;
        r1 = sInputCodesUtf8;
        r2 = r7._inputBuffer;
    L_0x0007:
        r4 = r7._inputPtr;
        r3 = r7._inputEnd;
        if (r4 < r3) goto L_0x004e;
    L_0x000d:
        r7.loadMoreGuaranteed();
        r4 = r7._inputPtr;
        r3 = r7._inputEnd;
        r5 = r4;
    L_0x0015:
        if (r5 >= r3) goto L_0x0028;
    L_0x0017:
        r4 = r5 + 1;
        r6 = r2[r5];
        r0 = r6 & 255;
        r6 = r1[r0];
        if (r6 == 0) goto L_0x004e;
    L_0x0021:
        r7._inputPtr = r4;
        r6 = 34;
        if (r0 != r6) goto L_0x002b;
    L_0x0027:
        return;
    L_0x0028:
        r7._inputPtr = r5;
        goto L_0x0007;
    L_0x002b:
        r6 = r1[r0];
        switch(r6) {
            case 1: goto L_0x003a;
            case 2: goto L_0x003e;
            case 3: goto L_0x0042;
            case 4: goto L_0x0046;
            default: goto L_0x0030;
        };
    L_0x0030:
        r6 = 32;
        if (r0 >= r6) goto L_0x004a;
    L_0x0034:
        r6 = "string value";
        r7._throwUnquotedSpace(r0, r6);
        goto L_0x0007;
    L_0x003a:
        r7._decodeEscaped();
        goto L_0x0007;
    L_0x003e:
        r7._skipUtf8_2(r0);
        goto L_0x0007;
    L_0x0042:
        r7._skipUtf8_3(r0);
        goto L_0x0007;
    L_0x0046:
        r7._skipUtf8_4(r0);
        goto L_0x0007;
    L_0x004a:
        r7._reportInvalidChar(r0);
        goto L_0x0007;
    L_0x004e:
        r5 = r4;
        goto L_0x0015;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.Utf8StreamParser._skipString():void");
    }

    protected final JsonToken _handleUnexpectedValue(int c) throws IOException, JsonParseException {
        if (!(c == 39 && isEnabled(Feature.ALLOW_SINGLE_QUOTES))) {
            _reportUnexpectedChar(c, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        }
        int outPtr = 0;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int[] codes = sInputCodesUtf8;
        byte[] inputBuffer = this._inputBuffer;
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int max = this._inputEnd;
            int max2 = this._inputPtr + (outBuf.length - outPtr);
            if (max2 < max) {
                max = max2;
            }
            while (this._inputPtr < max) {
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                c = inputBuffer[i] & KEYRecord.PROTOCOL_ANY;
                int outPtr2;
                if (c != 39 && codes[c] == 0) {
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (char) c;
                    outPtr = outPtr2;
                } else if (c == 39) {
                    this._textBuffer.setCurrentLength(outPtr);
                    return JsonToken.VALUE_STRING;
                } else {
                    switch (codes[c]) {
                        case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                            if (c != 34) {
                                c = _decodeEscaped();
                                break;
                            }
                            break;
                        case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                            c = _decodeUtf8_2(c);
                            break;
                        case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                            if (this._inputEnd - this._inputPtr < 2) {
                                c = _decodeUtf8_3(c);
                                break;
                            }
                            c = _decodeUtf8_3fast(c);
                            break;
                        case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                            c = _decodeUtf8_4(c);
                            outPtr2 = outPtr + 1;
                            outBuf[outPtr] = (char) (55296 | (c >> 10));
                            if (outPtr2 >= outBuf.length) {
                                outBuf = this._textBuffer.finishCurrentSegment();
                                outPtr = 0;
                            } else {
                                outPtr = outPtr2;
                            }
                            c = 56320 | (c & 1023);
                            break;
                        default:
                            if (c < 32) {
                                _throwUnquotedSpace(c, "string value");
                            }
                            _reportInvalidChar(c);
                            break;
                    }
                    if (outPtr >= outBuf.length) {
                        outBuf = this._textBuffer.finishCurrentSegment();
                        outPtr = 0;
                    }
                    outPtr2 = outPtr + 1;
                    outBuf[outPtr] = (char) c;
                    outPtr = outPtr2;
                }
            }
        }
    }

    protected void _matchToken(JsonToken token) throws IOException, JsonParseException {
        byte[] matchBytes = token.asByteArray();
        int len = matchBytes.length;
        for (int i = 1; i < len; i++) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            if (matchBytes[i] != this._inputBuffer[this._inputPtr]) {
                _reportInvalidToken(token.asString().substring(0, i));
            }
            this._inputPtr++;
        }
    }

    private void _reportInvalidToken(String matchedPart) throws IOException, JsonParseException {
        StringBuilder sb = new StringBuilder(matchedPart);
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                break;
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            char c = (char) _decodeCharForError(bArr[i]);
            if (!Character.isJavaIdentifierPart(c)) {
                break;
            }
            this._inputPtr++;
            sb.append(c);
        }
        _reportError("Unrecognized token '" + sb.toString() + "': was expecting 'null', 'true' or 'false'");
    }

    private final int _skipWS() throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                break;
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
            if (i2 > 32) {
                if (i2 != 47) {
                    return i2;
                }
                _skipComment();
            } else if (i2 != 32) {
                if (i2 == 10) {
                    _skipLF();
                } else if (i2 == 13) {
                    _skipCR();
                } else if (i2 != 9) {
                    _throwInvalidSpace(i2);
                }
            }
        }
        throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
    }

    private final int _skipWSOrEnd() throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
                if (i2 > 32) {
                    if (i2 != 47) {
                        return i2;
                    }
                    _skipComment();
                } else if (i2 != 32) {
                    if (i2 == 10) {
                        _skipLF();
                    } else if (i2 == 13) {
                        _skipCR();
                    } else if (i2 != 9) {
                        _throwInvalidSpace(i2);
                    }
                }
            } else {
                _handleEOF();
                return -1;
            }
        }
    }

    private final void _skipComment() throws IOException, JsonParseException {
        if (!isEnabled(Feature.ALLOW_COMMENTS)) {
            _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in a comment");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int c = bArr[i] & KEYRecord.PROTOCOL_ANY;
        if (c == 47) {
            _skipCppComment();
        } else if (c == 42) {
            _skipCComment();
        } else {
            _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
        }
    }

    private final void _skipCComment() throws IOException, JsonParseException {
        int[] codes = CharTypes.getInputCodeComment();
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
                int code = codes[i2];
                if (code != 0) {
                    switch (code) {
                        case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                            _skipLF();
                            break;
                        case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                            _skipCR();
                            break;
                        case Service.NAMESERVER /*42*/:
                            if (this._inputBuffer[this._inputPtr] != 47) {
                                break;
                            }
                            this._inputPtr++;
                            return;
                        default:
                            _reportInvalidChar(i2);
                            break;
                    }
                }
            } else {
                _reportInvalidEOF(" in a comment");
                return;
            }
        }
    }

    private final void _skipCppComment() throws IOException, JsonParseException {
        int[] codes = CharTypes.getInputCodeComment();
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
                int code = codes[i2];
                if (code != 0) {
                    switch (code) {
                        case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                            _skipLF();
                            return;
                        case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                            _skipCR();
                            return;
                        case Service.NAMESERVER /*42*/:
                            break;
                        default:
                            _reportInvalidChar(i2);
                            break;
                    }
                }
            } else {
                return;
            }
        }
    }

    protected final char _decodeEscaped() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in character escape sequence");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int c = bArr[i];
        switch (c) {
            case Type.ATMA /*34*/:
            case Service.NI_FTP /*47*/:
            case Opcodes.DUP2 /*92*/:
                return (char) c;
            case Service.TACNEWS /*98*/:
                return '\b';
            case Service.ISO_TSAP /*102*/:
                return '\f';
            case SoapEnvelope.VER11 /*110*/:
                return '\n';
            case Opcodes.FREM /*114*/:
                return '\r';
            case Opcodes.INEG /*116*/:
                return '\t';
            case Service.UUCP_PATH /*117*/:
                int value = 0;
                for (int i2 = 0; i2 < 4; i2++) {
                    if (this._inputPtr >= this._inputEnd && !loadMore()) {
                        _reportInvalidEOF(" in character escape sequence");
                    }
                    bArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    int ch = bArr[i];
                    int digit = CharTypes.charToHex(ch);
                    if (digit < 0) {
                        _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
                    }
                    value = (value << 4) | digit;
                }
                return (char) value;
            default:
                return _handleUnrecognizedCharacterEscape((char) _decodeCharForError(c));
        }
    }

    protected int _decodeCharForError(int firstByte) throws IOException, JsonParseException {
        int c = firstByte;
        if (c >= 0) {
            return c;
        }
        int needed;
        if ((c & SmileConstants.TOKEN_PREFIX_MISC_OTHER) == Wbxml.EXT_0) {
            c &= 31;
            needed = 1;
        } else if ((c & 240) == SmileConstants.TOKEN_PREFIX_MISC_OTHER) {
            c &= 15;
            needed = 2;
        } else if ((c & 248) == 240) {
            c &= 7;
            needed = 3;
        } else {
            _reportInvalidInitial(c & KEYRecord.PROTOCOL_ANY);
            needed = 1;
        }
        int d = nextByte();
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY);
        }
        c = (c << 6) | (d & 63);
        if (needed <= 1) {
            return c;
        }
        d = nextByte();
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY);
        }
        c = (c << 6) | (d & 63);
        if (needed <= 2) {
            return c;
        }
        d = nextByte();
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY);
        }
        return (c << 6) | (d & 63);
    }

    private final int _decodeUtf8_2(int c) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
        return ((c & 31) << 6) | (d & 63);
    }

    private final int _decodeUtf8_3(int c1) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        c1 &= 15;
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
        int c = (c1 << 6) | (d & 63);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
        return (c << 6) | (d & 63);
    }

    private final int _decodeUtf8_3fast(int c1) throws IOException, JsonParseException {
        c1 &= 15;
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
        int c = (c1 << 6) | (d & 63);
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
        return (c << 6) | (d & 63);
    }

    private final int _decodeUtf8_4(int c) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
        c = ((c & 7) << 6) | (d & 63);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
        c = (c << 6) | (d & 63);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
        return ((c << 6) | (d & 63)) - AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED;
    }

    private final void _skipUtf8_2(int c) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        c = bArr[i];
        if ((c & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(c & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
    }

    private final void _skipUtf8_3(int c) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        c = bArr[i];
        if ((c & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(c & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        c = bArr[i];
        if ((c & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(c & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
    }

    private final void _skipUtf8_4(int c) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int d = bArr[i];
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        d = bArr[i];
        if ((d & Wbxml.EXT_0) != Flags.FLAG8) {
            _reportInvalidOther(d & KEYRecord.PROTOCOL_ANY, this._inputPtr);
        }
    }

    protected final void _skipCR() throws IOException {
        if ((this._inputPtr < this._inputEnd || loadMore()) && this._inputBuffer[this._inputPtr] == 10) {
            this._inputPtr++;
        }
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    protected final void _skipLF() throws IOException {
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    private int nextByte() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        return bArr[i] & KEYRecord.PROTOCOL_ANY;
    }

    protected void _reportInvalidChar(int c) throws JsonParseException {
        if (c < 32) {
            _throwInvalidSpace(c);
        }
        _reportInvalidInitial(c);
    }

    protected void _reportInvalidInitial(int mask) throws JsonParseException {
        _reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(mask));
    }

    protected void _reportInvalidOther(int mask) throws JsonParseException {
        _reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(mask));
    }

    protected void _reportInvalidOther(int mask, int ptr) throws JsonParseException {
        this._inputPtr = ptr;
        _reportInvalidOther(mask);
    }

    public static int[] growArrayBy(int[] arr, int more) {
        if (arr == null) {
            return new int[more];
        }
        int[] old = arr;
        int len = arr.length;
        arr = new int[(len + more)];
        System.arraycopy(old, 0, arr, 0, len);
        return arr;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected byte[] _decodeBase64(org.codehaus.jackson.Base64Variant r10) throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        /*
        r9 = this;
        r8 = 3;
        r7 = -2;
        r1 = r9._getByteArrayBuilder();
    L_0x0006:
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x000f;
    L_0x000c:
        r9.loadMoreGuaranteed();
    L_0x000f:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r2 = r4 & 255;
        r4 = 32;
        if (r2 <= r4) goto L_0x0006;
    L_0x001f:
        r0 = r10.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x0034;
    L_0x0025:
        r4 = 34;
        if (r2 != r4) goto L_0x002e;
    L_0x0029:
        r4 = r1.toByteArray();
        return r4;
    L_0x002e:
        r4 = 0;
        r4 = r9.reportInvalidChar(r10, r2, r4);
        throw r4;
    L_0x0034:
        r3 = r0;
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x003e;
    L_0x003b:
        r9.loadMoreGuaranteed();
    L_0x003e:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r2 = r4 & 255;
        r0 = r10.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x0056;
    L_0x0050:
        r4 = 1;
        r4 = r9.reportInvalidChar(r10, r2, r4);
        throw r4;
    L_0x0056:
        r4 = r3 << 6;
        r3 = r4 | r0;
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x0063;
    L_0x0060:
        r9.loadMoreGuaranteed();
    L_0x0063:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r2 = r4 & 255;
        r0 = r10.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x00c1;
    L_0x0075:
        if (r0 == r7) goto L_0x007d;
    L_0x0077:
        r4 = 2;
        r4 = r9.reportInvalidChar(r10, r2, r4);
        throw r4;
    L_0x007d:
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x0086;
    L_0x0083:
        r9.loadMoreGuaranteed();
    L_0x0086:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r2 = r4 & 255;
        r4 = r10.usesPaddingChar(r2);
        if (r4 != 0) goto L_0x00ba;
    L_0x0098:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "expected padding character '";
        r4 = r4.append(r5);
        r5 = r10.getPaddingChar();
        r4 = r4.append(r5);
        r5 = "'";
        r4 = r4.append(r5);
        r4 = r4.toString();
        r4 = r9.reportInvalidChar(r10, r2, r8, r4);
        throw r4;
    L_0x00ba:
        r3 = r3 >> 4;
        r1.append(r3);
        goto L_0x0006;
    L_0x00c1:
        r4 = r3 << 6;
        r3 = r4 | r0;
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x00ce;
    L_0x00cb:
        r9.loadMoreGuaranteed();
    L_0x00ce:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r4 = r4[r5];
        r2 = r4 & 255;
        r0 = r10.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x00ee;
    L_0x00e0:
        if (r0 == r7) goto L_0x00e7;
    L_0x00e2:
        r4 = r9.reportInvalidChar(r10, r2, r8);
        throw r4;
    L_0x00e7:
        r3 = r3 >> 2;
        r1.appendTwoBytes(r3);
        goto L_0x0006;
    L_0x00ee:
        r4 = r3 << 6;
        r3 = r4 | r0;
        r1.appendThreeBytes(r3);
        goto L_0x0006;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.Utf8StreamParser._decodeBase64(org.codehaus.jackson.Base64Variant):byte[]");
    }

    protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, int ch, int bindex) throws IllegalArgumentException {
        return reportInvalidChar(b64variant, ch, bindex, null);
    }

    protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, int ch, int bindex, String msg) throws IllegalArgumentException {
        String base;
        if (ch <= 32) {
            base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units";
        } else if (b64variant.usesPaddingChar(ch)) {
            base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        } else if (!Character.isDefined(ch) || Character.isISOControl(ch)) {
            base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        } else {
            base = "Illegal character '" + ((char) ch) + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        }
        if (msg != null) {
            base = base + ": " + msg;
        }
        return new IllegalArgumentException(base);
    }
}
