package org.codehaus.jackson.smile;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.impl.StreamBasedParserBase;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.Name;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class SmileParser extends StreamBasedParserBase {
    private static final int[] NO_INTS;
    private static final String[] NO_STRINGS;
    protected static final ThreadLocal<SoftReference<SmileBufferRecycler<String>>> _smileRecyclerRef;
    protected boolean _got32BitFloat;
    protected boolean _mayContainRawBinary;
    protected ObjectCodec _objectCodec;
    protected int _quad1;
    protected int _quad2;
    protected int[] _quadBuffer;
    protected int _seenNameCount;
    protected String[] _seenNames;
    protected int _seenStringValueCount;
    protected String[] _seenStringValues;
    protected final SmileBufferRecycler<String> _smileBufferRecycler;
    protected final BytesToNameCanonicalizer _symbols;
    protected boolean _tokenIncomplete;
    protected int _typeByte;

    /* renamed from: org.codehaus.jackson.smile.SmileParser.1 */
    static /* synthetic */ class C09491 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 2;
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

    public enum Feature {
        REQUIRE_HEADER(true);
        
        final boolean _defaultState;
        final int _mask;

        public static int collectDefaults() {
            int flags = 0;
            for (Feature f : values()) {
                if (f.enabledByDefault()) {
                    flags |= f.getMask();
                }
            }
            return flags;
        }

        private Feature(boolean defaultState) {
            this._defaultState = defaultState;
            this._mask = 1 << ordinal();
        }

        public boolean enabledByDefault() {
            return this._defaultState;
        }

        public int getMask() {
            return this._mask;
        }
    }

    static {
        NO_INTS = new int[0];
        NO_STRINGS = new String[0];
        _smileRecyclerRef = new ThreadLocal();
    }

    public SmileParser(IOContext ctxt, int parserFeatures, int smileFeatures, ObjectCodec codec, BytesToNameCanonicalizer sym, InputStream in, byte[] inputBuffer, int start, int end, boolean bufferRecyclable) {
        super(ctxt, parserFeatures, in, inputBuffer, start, end, bufferRecyclable);
        this._tokenIncomplete = false;
        this._quadBuffer = NO_INTS;
        this._seenNames = NO_STRINGS;
        this._seenNameCount = 0;
        this._seenStringValues = null;
        this._seenStringValueCount = -1;
        this._objectCodec = codec;
        this._symbols = sym;
        this._tokenInputRow = -1;
        this._tokenInputCol = -1;
        this._smileBufferRecycler = _smileBufferRecycler();
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }

    protected boolean handleSignature(boolean consumeFirstByte, boolean throwException) throws IOException, JsonParseException {
        boolean z = false;
        if (consumeFirstByte) {
            this._inputPtr++;
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        if (this._inputBuffer[this._inputPtr] == 41) {
            int i = this._inputPtr + 1;
            this._inputPtr = i;
            if (i >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            if (this._inputBuffer[this._inputPtr] == 10) {
                i = this._inputPtr + 1;
                this._inputPtr = i;
                if (i >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr = this._inputBuffer;
                int i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                int ch = bArr[i2];
                int versionBits = (ch >> 4) & 15;
                if (versionBits != 0) {
                    _reportError("Header version number bits (0x" + Integer.toHexString(versionBits) + ") indicate unrecognized version; only 0x0 handled by parser");
                }
                if ((ch & 1) == 0) {
                    this._seenNames = null;
                    this._seenNameCount = -1;
                }
                if ((ch & 2) != 0) {
                    this._seenStringValues = NO_STRINGS;
                    this._seenStringValueCount = 0;
                }
                if ((ch & 4) != 0) {
                    z = true;
                }
                this._mayContainRawBinary = z;
                return true;
            } else if (!throwException) {
                return false;
            } else {
                _reportError("Malformed content: signature not valid, starts with 0x3a, 0x29, but followed by 0x" + Integer.toHexString(this._inputBuffer[this._inputPtr]) + ", not 0xA");
                return false;
            }
        } else if (!throwException) {
            return false;
        } else {
            _reportError("Malformed content: signature not valid, starts with 0x3a but followed by 0x" + Integer.toHexString(this._inputBuffer[this._inputPtr]) + ", not 0x29");
            return false;
        }
    }

    protected static final SmileBufferRecycler<String> _smileBufferRecycler() {
        SoftReference<SmileBufferRecycler<String>> ref = (SoftReference) _smileRecyclerRef.get();
        SmileBufferRecycler<String> br = ref == null ? null : (SmileBufferRecycler) ref.get();
        if (br != null) {
            return br;
        }
        br = new SmileBufferRecycler();
        _smileRecyclerRef.set(new SoftReference(br));
        return br;
    }

    protected void _finishString() throws IOException, JsonParseException {
        _throwInternal();
    }

    public void close() throws IOException {
        super.close();
        this._symbols.release();
    }

    protected void _releaseBuffers() throws IOException {
        super._releaseBuffers();
        String[] nameBuf = this._seenNames;
        if (nameBuf != null && nameBuf.length > 0) {
            this._seenNames = null;
            Arrays.fill(nameBuf, 0, this._seenNameCount, null);
            this._smileBufferRecycler.releaseSeenNamesBuffer(nameBuf);
        }
        String[] valueBuf = this._seenStringValues;
        if (valueBuf != null && valueBuf.length > 0) {
            this._seenStringValues = null;
            Arrays.fill(valueBuf, 0, this._seenStringValueCount, null);
            this._smileBufferRecycler.releaseSeenStringValuesBuffer(valueBuf);
        }
    }

    public boolean mayContainRawBinary() {
        return this._mayContainRawBinary;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.codehaus.jackson.JsonToken nextToken() throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        /*
        r11 = this;
        r10 = 2;
        r5 = 0;
        r4 = 0;
        r3 = 1;
        r6 = r11._tokenIncomplete;
        if (r6 == 0) goto L_0x000b;
    L_0x0008:
        r11._skipIncomplete();
    L_0x000b:
        r6 = r11._currInputProcessed;
        r8 = r11._inputPtr;
        r8 = (long) r8;
        r6 = r6 + r8;
        r8 = 1;
        r6 = r6 - r8;
        r11._tokenInputTotal = r6;
        r11._binaryValue = r5;
        r6 = r11._parsingContext;
        r6 = r6.inObject();
        if (r6 == 0) goto L_0x002d;
    L_0x0020:
        r6 = r11._currToken;
        r7 = org.codehaus.jackson.JsonToken.FIELD_NAME;
        if (r6 == r7) goto L_0x002d;
    L_0x0026:
        r3 = r11._handleFieldName();
        r11._currToken = r3;
    L_0x002c:
        return r3;
    L_0x002d:
        r6 = r11._inputPtr;
        r7 = r11._inputEnd;
        if (r6 < r7) goto L_0x0043;
    L_0x0033:
        r6 = r11.loadMore();
        if (r6 != 0) goto L_0x0043;
    L_0x0039:
        r11._handleEOF();
        r11.close();
        r11._currToken = r5;
        r3 = r5;
        goto L_0x002c;
    L_0x0043:
        r6 = r11._inputBuffer;
        r7 = r11._inputPtr;
        r8 = r7 + 1;
        r11._inputPtr = r8;
        r0 = r6[r7];
        r11._typeByte = r0;
        r6 = r0 >> 5;
        r6 = r6 & 7;
        switch(r6) {
            case 0: goto L_0x007a;
            case 1: goto L_0x0088;
            case 2: goto L_0x00f3;
            case 3: goto L_0x00f3;
            case 4: goto L_0x00f3;
            case 5: goto L_0x00f3;
            case 6: goto L_0x0105;
            case 7: goto L_0x0115;
            default: goto L_0x0056;
        };
    L_0x0056:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Invalid type marker byte 0x";
        r3 = r3.append(r4);
        r4 = r0 & 255;
        r4 = java.lang.Integer.toHexString(r4);
        r3 = r3.append(r4);
        r4 = " for expected value token";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r11._reportError(r3);
        r3 = r5;
        goto L_0x002c;
    L_0x007a:
        if (r0 != 0) goto L_0x0081;
    L_0x007c:
        r3 = "Invalid token byte 0x00";
        r11._reportError(r3);
    L_0x0081:
        r3 = r0 + -1;
        r3 = r11._handleSharedString(r3);
        goto L_0x002c;
    L_0x0088:
        r2 = r0 & 31;
        r6 = 4;
        if (r2 >= r6) goto L_0x00a9;
    L_0x008d:
        switch(r2) {
            case 0: goto L_0x0095;
            case 1: goto L_0x009f;
            case 2: goto L_0x00a4;
            default: goto L_0x0090;
        };
    L_0x0090:
        r3 = org.codehaus.jackson.JsonToken.VALUE_TRUE;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x0095:
        r3 = r11._textBuffer;
        r3.resetWithEmpty();
        r3 = org.codehaus.jackson.JsonToken.VALUE_STRING;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x009f:
        r3 = org.codehaus.jackson.JsonToken.VALUE_NULL;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x00a4:
        r3 = org.codehaus.jackson.JsonToken.VALUE_FALSE;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x00a9:
        r6 = 8;
        if (r2 >= r6) goto L_0x00bb;
    L_0x00ad:
        r6 = r2 & 3;
        if (r6 > r10) goto L_0x0056;
    L_0x00b1:
        r11._tokenIncomplete = r3;
        r11._numTypesValid = r4;
        r3 = org.codehaus.jackson.JsonToken.VALUE_NUMBER_INT;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x00bb:
        r6 = 12;
        if (r2 >= r6) goto L_0x00d3;
    L_0x00bf:
        r1 = r2 & 3;
        if (r1 > r10) goto L_0x0056;
    L_0x00c3:
        r11._tokenIncomplete = r3;
        r11._numTypesValid = r4;
        if (r1 != 0) goto L_0x00d1;
    L_0x00c9:
        r11._got32BitFloat = r3;
        r3 = org.codehaus.jackson.JsonToken.VALUE_NUMBER_FLOAT;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x00d1:
        r3 = r4;
        goto L_0x00c9;
    L_0x00d3:
        r3 = 26;
        if (r2 != r3) goto L_0x00ec;
    L_0x00d7:
        r3 = r11.handleSignature(r4, r4);
        if (r3 == 0) goto L_0x00ec;
    L_0x00dd:
        r3 = r11._currToken;
        if (r3 != 0) goto L_0x00e7;
    L_0x00e1:
        r3 = r11.nextToken();
        goto L_0x002c;
    L_0x00e7:
        r11._currToken = r5;
        r3 = r5;
        goto L_0x002c;
    L_0x00ec:
        r3 = "Unrecognized token byte 0x3A (malformed segment header?";
        r11._reportError(r3);
        goto L_0x0056;
    L_0x00f3:
        r4 = org.codehaus.jackson.JsonToken.VALUE_STRING;
        r11._currToken = r4;
        r4 = r11._seenStringValueCount;
        if (r4 < 0) goto L_0x0102;
    L_0x00fb:
        r11._addSeenStringValue();
    L_0x00fe:
        r3 = r11._currToken;
        goto L_0x002c;
    L_0x0102:
        r11._tokenIncomplete = r3;
        goto L_0x00fe;
    L_0x0105:
        r4 = r0 & 31;
        r4 = org.codehaus.jackson.smile.SmileUtil.zigzagDecode(r4);
        r11._numberInt = r4;
        r11._numTypesValid = r3;
        r3 = org.codehaus.jackson.JsonToken.VALUE_NUMBER_INT;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x0115:
        r4 = r0 & 31;
        switch(r4) {
            case 0: goto L_0x011c;
            case 4: goto L_0x011c;
            case 8: goto L_0x0124;
            case 12: goto L_0x012c;
            case 13: goto L_0x012c;
            case 14: goto L_0x012c;
            case 15: goto L_0x012c;
            case 24: goto L_0x014c;
            case 25: goto L_0x015e;
            case 26: goto L_0x017b;
            case 27: goto L_0x018d;
            case 29: goto L_0x0192;
            case 31: goto L_0x019a;
            default: goto L_0x011a;
        };
    L_0x011a:
        goto L_0x0056;
    L_0x011c:
        r11._tokenIncomplete = r3;
        r3 = org.codehaus.jackson.JsonToken.VALUE_STRING;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x0124:
        r11._tokenIncomplete = r3;
        r3 = org.codehaus.jackson.JsonToken.VALUE_EMBEDDED_OBJECT;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x012c:
        r3 = r11._inputPtr;
        r4 = r11._inputEnd;
        if (r3 < r4) goto L_0x0135;
    L_0x0132:
        r11.loadMoreGuaranteed();
    L_0x0135:
        r3 = r0 & 3;
        r3 = r3 << 8;
        r4 = r11._inputBuffer;
        r5 = r11._inputPtr;
        r6 = r5 + 1;
        r11._inputPtr = r6;
        r4 = r4[r5];
        r4 = r4 & 255;
        r3 = r3 + r4;
        r3 = r11._handleSharedString(r3);
        goto L_0x002c;
    L_0x014c:
        r3 = r11._parsingContext;
        r4 = r11._tokenInputRow;
        r5 = r11._tokenInputCol;
        r3 = r3.createChildArrayContext(r4, r5);
        r11._parsingContext = r3;
        r3 = org.codehaus.jackson.JsonToken.START_ARRAY;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x015e:
        r3 = r11._parsingContext;
        r3 = r3.inArray();
        if (r3 != 0) goto L_0x016d;
    L_0x0166:
        r3 = 93;
        r4 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r11._reportMismatchedEndMarker(r3, r4);
    L_0x016d:
        r3 = r11._parsingContext;
        r3 = r3.getParent();
        r11._parsingContext = r3;
        r3 = org.codehaus.jackson.JsonToken.END_ARRAY;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x017b:
        r3 = r11._parsingContext;
        r4 = r11._tokenInputRow;
        r5 = r11._tokenInputCol;
        r3 = r3.createChildObjectContext(r4, r5);
        r11._parsingContext = r3;
        r3 = org.codehaus.jackson.JsonToken.START_OBJECT;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x018d:
        r4 = "Invalid type marker byte 0xFB in value mode (would be END_OBJECT in key mode)";
        r11._reportError(r4);
    L_0x0192:
        r11._tokenIncomplete = r3;
        r3 = org.codehaus.jackson.JsonToken.VALUE_EMBEDDED_OBJECT;
        r11._currToken = r3;
        goto L_0x002c;
    L_0x019a:
        r11._currToken = r5;
        r3 = r5;
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.smile.SmileParser.nextToken():org.codehaus.jackson.JsonToken");
    }

    private final JsonToken _handleSharedString(int index) throws IOException, JsonParseException {
        if (index >= this._seenStringValueCount) {
            _reportInvalidSharedStringValue(index);
        }
        this._textBuffer.resetWithString(this._seenStringValues[index]);
        JsonToken jsonToken = JsonToken.VALUE_STRING;
        this._currToken = jsonToken;
        return jsonToken;
    }

    private final void _addSeenStringValue() throws IOException, JsonParseException {
        _finishToken();
        if (this._seenStringValueCount < this._seenStringValues.length) {
            String[] strArr = this._seenStringValues;
            int i = this._seenStringValueCount;
            this._seenStringValueCount = i + 1;
            strArr[i] = this._textBuffer.contentsAsString();
            return;
        }
        _expandSeenStringValues();
    }

    private final void _expandSeenStringValues() {
        String[] newShared;
        int newSize = Flags.FLAG5;
        String[] oldShared = this._seenStringValues;
        int len = oldShared.length;
        if (len == 0) {
            newShared = (String[]) this._smileBufferRecycler.allocSeenStringValuesBuffer();
            if (newShared == null) {
                newShared = new String[64];
            }
        } else if (len == Flags.FLAG5) {
            newShared = oldShared;
            this._seenStringValueCount = 0;
        } else {
            if (len == 64) {
                newSize = KEYRecord.OWNER_ZONE;
            }
            newShared = new String[newSize];
            System.arraycopy(oldShared, 0, newShared, 0, oldShared.length);
        }
        this._seenStringValues = newShared;
        String[] strArr = this._seenStringValues;
        int i = this._seenStringValueCount;
        this._seenStringValueCount = i + 1;
        strArr[i] = this._textBuffer.contentsAsString();
    }

    public String getCurrentName() throws IOException, JsonParseException {
        return this._parsingContext.getCurrentName();
    }

    public NumberType getNumberType() throws IOException, JsonParseException {
        if (this._got32BitFloat) {
            return NumberType.FLOAT;
        }
        return super.getNumberType();
    }

    public String getText() throws IOException, JsonParseException {
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            int tb = this._typeByte;
            int type = (tb >> 5) & 7;
            if (type == 2 || type == 3) {
                _decodeShortAsciiValue((tb & 63) + 1);
                return this._textBuffer.contentsAsString();
            } else if (type == 4 || type == 5) {
                _decodeShortUnicodeValue((tb & 63) + 2);
                return this._textBuffer.contentsAsString();
            } else {
                _finishToken();
            }
        }
        if (this._currToken == JsonToken.VALUE_STRING) {
            return this._textBuffer.contentsAsString();
        }
        JsonToken t = this._currToken;
        if (t == null) {
            return null;
        }
        if (t == JsonToken.FIELD_NAME) {
            return this._parsingContext.getCurrentName();
        }
        if (t.isNumeric()) {
            return getNumberValue().toString();
        }
        return this._currToken.asString();
    }

    public char[] getTextCharacters() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return null;
        }
        if (this._tokenIncomplete) {
            _finishToken();
        }
        switch (C09491.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return this._textBuffer.getTextBuffer();
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
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
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return getNumberValue().toString().toCharArray();
            default:
                return this._currToken.asCharArray();
        }
    }

    public int getTextLength() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return 0;
        }
        if (this._tokenIncomplete) {
            _finishToken();
        }
        switch (C09491.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return this._textBuffer.size();
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return this._parsingContext.getCurrentName().length();
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return getNumberValue().toString().length();
            default:
                return this._currToken.asCharArray().length;
        }
    }

    public int getTextOffset() throws IOException, JsonParseException {
        return 0;
    }

    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException {
        if (this._tokenIncomplete) {
            _finishToken();
        }
        if (this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT) {
            _reportError("Current token (" + this._currToken + ") not VALUE_EMBEDDED_OBJECT, can not access as binary");
        }
        return this._binaryValue;
    }

    protected byte[] _decodeBase64(Base64Variant b64variant) throws IOException, JsonParseException {
        _throwInternal();
        return null;
    }

    protected final JsonToken _handleFieldName() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int ch = bArr[i];
        this._typeByte = ch;
        int index;
        int len;
        Name n;
        String name;
        String[] strArr;
        switch ((ch >> 6) & 3) {
            case KEYRecord.OWNER_USER /*0*/:
                switch (ch) {
                    case Protocol.MERIT_INP /*32*/:
                        this._parsingContext.setCurrentName(XmlPullParser.NO_NAMESPACE);
                        return JsonToken.FIELD_NAME;
                    case Type.DNSKEY /*48*/:
                    case Service.LOGIN /*49*/:
                    case Type.NSEC3 /*50*/:
                    case Service.LA_MAINT /*51*/:
                        if (this._inputPtr >= this._inputEnd) {
                            loadMoreGuaranteed();
                        }
                        int i2 = (ch & 3) << 8;
                        byte[] bArr2 = this._inputBuffer;
                        int i3 = this._inputPtr;
                        this._inputPtr = i3 + 1;
                        index = i2 + (bArr2[i3] & KEYRecord.PROTOCOL_ANY);
                        if (index >= this._seenNameCount) {
                            _reportInvalidSharedName(index);
                        }
                        this._parsingContext.setCurrentName(this._seenNames[index]);
                        return JsonToken.FIELD_NAME;
                    case Type.TLSA /*52*/:
                        _handleLongFieldName();
                        return JsonToken.FIELD_NAME;
                    default:
                        break;
                }
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                index = ch & 63;
                if (index >= this._seenNameCount) {
                    _reportInvalidSharedName(index);
                }
                this._parsingContext.setCurrentName(this._seenNames[index]);
                return JsonToken.FIELD_NAME;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                len = (ch & 63) + 1;
                n = _findDecodedFromSymbols(len);
                if (n != null) {
                    name = n.getName();
                    this._inputPtr += len;
                } else {
                    name = _addDecodedToSymbols(len, _decodeShortAsciiName(len));
                }
                if (this._seenNames != null) {
                    if (this._seenNameCount >= this._seenNames.length) {
                        this._seenNames = _expandSeenNames(this._seenNames);
                    }
                    strArr = this._seenNames;
                    i = this._seenNameCount;
                    this._seenNameCount = i + 1;
                    strArr[i] = name;
                }
                this._parsingContext.setCurrentName(name);
                return JsonToken.FIELD_NAME;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                len = ch & 63;
                if (len <= 55) {
                    len += 2;
                    n = _findDecodedFromSymbols(len);
                    if (n != null) {
                        name = n.getName();
                        this._inputPtr += len;
                    } else {
                        name = _addDecodedToSymbols(len, _decodeShortUnicodeName(len));
                    }
                    if (this._seenNames != null) {
                        if (this._seenNameCount >= this._seenNames.length) {
                            this._seenNames = _expandSeenNames(this._seenNames);
                        }
                        strArr = this._seenNames;
                        i = this._seenNameCount;
                        this._seenNameCount = i + 1;
                        strArr[i] = name;
                    }
                    this._parsingContext.setCurrentName(name);
                    return JsonToken.FIELD_NAME;
                } else if (len == 59) {
                    if (!this._parsingContext.inObject()) {
                        _reportMismatchedEndMarker(Service.LOCUS_MAP, ']');
                    }
                    this._parsingContext = this._parsingContext.getParent();
                    return JsonToken.END_OBJECT;
                }
                break;
        }
        _reportError("Invalid type marker byte 0x" + Integer.toHexString(ch) + " for expected field name (or END_OBJECT marker)");
        return null;
    }

    private final String[] _expandSeenNames(String[] oldShared) {
        int newSize = Flags.FLAG5;
        int len = oldShared.length;
        String[] newShared;
        if (len == 0) {
            newShared = (String[]) this._smileBufferRecycler.allocSeenNamesBuffer();
            if (newShared == null) {
                return new String[64];
            }
            return newShared;
        } else if (len == Flags.FLAG5) {
            newShared = oldShared;
            this._seenNameCount = 0;
            return newShared;
        } else {
            if (len == 64) {
                newSize = KEYRecord.OWNER_ZONE;
            }
            newShared = new String[newSize];
            System.arraycopy(oldShared, 0, newShared, 0, oldShared.length);
            return newShared;
        }
    }

    private final String _addDecodedToSymbols(int len, String name) {
        if (len < 5) {
            return this._symbols.addName(name, this._quad1, 0).getName();
        }
        if (len < 9) {
            return this._symbols.addName(name, this._quad1, this._quad2).getName();
        }
        return this._symbols.addName(name, this._quadBuffer, (len + 3) >> 2).getName();
    }

    private final String _decodeShortAsciiName(int len) throws IOException, JsonParseException {
        int i;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        byte[] inBuf = this._inputBuffer;
        int inPtr = this._inputPtr;
        int inEnd = (inPtr + len) - 3;
        int inPtr2 = inPtr;
        int outPtr = 0;
        while (inPtr2 < inEnd) {
            i = outPtr + 1;
            inPtr = inPtr2 + 1;
            outBuf[outPtr] = (char) inBuf[inPtr2];
            outPtr = i + 1;
            inPtr2 = inPtr + 1;
            outBuf[i] = (char) inBuf[inPtr];
            i = outPtr + 1;
            inPtr = inPtr2 + 1;
            outBuf[outPtr] = (char) inBuf[inPtr2];
            outPtr = i + 1;
            inPtr2 = inPtr + 1;
            outBuf[i] = (char) inBuf[inPtr];
        }
        int left = len & 3;
        if (left > 0) {
            i = outPtr + 1;
            inPtr = inPtr2 + 1;
            outBuf[outPtr] = (char) inBuf[inPtr2];
            if (left > 1) {
                outPtr = i + 1;
                inPtr2 = inPtr + 1;
                outBuf[i] = (char) inBuf[inPtr];
                if (left > 2) {
                    i = outPtr + 1;
                    inPtr = inPtr2 + 1;
                    outBuf[outPtr] = (char) inBuf[inPtr2];
                }
            }
            this._inputPtr = inPtr;
            this._textBuffer.setCurrentLength(len);
            return this._textBuffer.contentsAsString();
        }
        inPtr = inPtr2;
        i = outPtr;
        this._inputPtr = inPtr;
        this._textBuffer.setCurrentLength(len);
        return this._textBuffer.contentsAsString();
    }

    private final String _decodeShortUnicodeName(int len) throws IOException, JsonParseException {
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int inPtr = this._inputPtr;
        this._inputPtr += len;
        int[] codes = SmileConstants.sUtf8UnitLengths;
        byte[] inBuf = this._inputBuffer;
        int end = inPtr + len;
        int inPtr2 = inPtr;
        int outPtr = 0;
        while (inPtr2 < end) {
            int i;
            inPtr = inPtr2 + 1;
            int i2 = inBuf[inPtr2] & KEYRecord.PROTOCOL_ANY;
            int code = codes[i2];
            if (code != 0) {
                switch (code) {
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        i2 = ((i2 & 31) << 6) | (inBuf[inPtr] & 63);
                        inPtr++;
                        i = outPtr;
                        continue;
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        inPtr2 = inPtr + 1;
                        inPtr = inPtr2 + 1;
                        i2 = (((i2 & 15) << 12) | ((inBuf[inPtr] & 63) << 6)) | (inBuf[inPtr2] & 63);
                        i = outPtr;
                        continue;
                    case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                        inPtr2 = inPtr + 1;
                        inPtr = inPtr2 + 1;
                        inPtr2 = inPtr + 1;
                        i2 = (((((i2 & 7) << 18) | ((inBuf[inPtr] & 63) << 12)) | ((inBuf[inPtr2] & 63) << 6)) | (inBuf[inPtr] & 63)) - AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED;
                        i = outPtr + 1;
                        outBuf[outPtr] = (char) (55296 | (i2 >> 10));
                        i2 = 56320 | (i2 & 1023);
                        inPtr = inPtr2;
                        continue;
                    default:
                        _reportError("Invalid byte " + Integer.toHexString(i2) + " in short Unicode text block");
                        break;
                }
            }
            i = outPtr;
            outPtr = i + 1;
            outBuf[i] = (char) i2;
            inPtr2 = inPtr;
        }
        this._textBuffer.setCurrentLength(outPtr);
        return this._textBuffer.contentsAsString();
    }

    private final Name _decodeLongUnicodeName(int[] quads, int byteLen, int quadLen) throws IOException, JsonParseException {
        int lastQuad;
        int lastQuadBytes = byteLen & 3;
        if (lastQuadBytes < 4) {
            lastQuad = quads[quadLen - 1];
            quads[quadLen - 1] = lastQuad << ((4 - lastQuadBytes) << 3);
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
                    _reportInvalidEOF(" in long field name");
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
            quads[quadLen - 1] = lastQuad;
        }
        return this._symbols.addName(baseName, quads, quadLen);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void _handleLongFieldName() throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        /*
        r12 = this;
        r11 = -4;
        r3 = r12._inputBuffer;
        r7 = 0;
        r2 = 0;
        r6 = 0;
    L_0x0006:
        r9 = r12._inputPtr;
        r10 = r9 + 1;
        r12._inputPtr = r10;
        r0 = r3[r9];
        if (r11 != r0) goto L_0x0060;
    L_0x0010:
        r2 = 0;
    L_0x0011:
        r1 = r7 << 2;
        if (r2 <= 0) goto L_0x002f;
    L_0x0015:
        r9 = r12._quadBuffer;
        r9 = r9.length;
        if (r7 < r9) goto L_0x0027;
    L_0x001a:
        r9 = r12._quadBuffer;
        r10 = r12._quadBuffer;
        r10 = r10.length;
        r10 = r10 + 256;
        r9 = _growArrayTo(r9, r10);
        r12._quadBuffer = r9;
    L_0x0027:
        r9 = r12._quadBuffer;
        r8 = r7 + 1;
        r9[r7] = r6;
        r1 = r1 + r2;
        r7 = r8;
    L_0x002f:
        r9 = r12._symbols;
        r10 = r12._quadBuffer;
        r4 = r9.findName(r10, r7);
        if (r4 == 0) goto L_0x00b3;
    L_0x0039:
        r5 = r4.getName();
    L_0x003d:
        r9 = r12._seenNames;
        if (r9 == 0) goto L_0x005a;
    L_0x0041:
        r9 = r12._seenNameCount;
        r10 = r12._seenNames;
        r10 = r10.length;
        if (r9 < r10) goto L_0x0050;
    L_0x0048:
        r9 = r12._seenNames;
        r9 = r12._expandSeenNames(r9);
        r12._seenNames = r9;
    L_0x0050:
        r9 = r12._seenNames;
        r10 = r12._seenNameCount;
        r11 = r10 + 1;
        r12._seenNameCount = r11;
        r9[r10] = r5;
    L_0x005a:
        r9 = r12._parsingContext;
        r9.setCurrentName(r5);
        return;
    L_0x0060:
        r6 = r0 & 255;
        r9 = r12._inputPtr;
        r10 = r9 + 1;
        r12._inputPtr = r10;
        r0 = r3[r9];
        if (r11 != r0) goto L_0x006e;
    L_0x006c:
        r2 = 1;
        goto L_0x0011;
    L_0x006e:
        r9 = r6 << 8;
        r10 = r0 & 255;
        r6 = r9 | r10;
        r9 = r12._inputPtr;
        r10 = r9 + 1;
        r12._inputPtr = r10;
        r0 = r3[r9];
        if (r11 != r0) goto L_0x0080;
    L_0x007e:
        r2 = 2;
        goto L_0x0011;
    L_0x0080:
        r9 = r6 << 8;
        r10 = r0 & 255;
        r6 = r9 | r10;
        r9 = r12._inputPtr;
        r10 = r9 + 1;
        r12._inputPtr = r10;
        r0 = r3[r9];
        if (r11 != r0) goto L_0x0092;
    L_0x0090:
        r2 = 3;
        goto L_0x0011;
    L_0x0092:
        r9 = r6 << 8;
        r10 = r0 & 255;
        r6 = r9 | r10;
        r9 = r12._quadBuffer;
        r9 = r9.length;
        if (r7 < r9) goto L_0x00aa;
    L_0x009d:
        r9 = r12._quadBuffer;
        r10 = r12._quadBuffer;
        r10 = r10.length;
        r10 = r10 + 256;
        r9 = _growArrayTo(r9, r10);
        r12._quadBuffer = r9;
    L_0x00aa:
        r9 = r12._quadBuffer;
        r8 = r7 + 1;
        r9[r7] = r6;
        r7 = r8;
        goto L_0x0006;
    L_0x00b3:
        r9 = r12._quadBuffer;
        r9 = r12._decodeLongUnicodeName(r9, r1, r7);
        r5 = r9.getName();
        goto L_0x003d;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.smile.SmileParser._handleLongFieldName():void");
    }

    private final Name _findDecodedFromSymbols(int len) throws IOException, JsonParseException {
        if (this._inputEnd - this._inputPtr < len) {
            _loadToHaveAtLeast(len);
        }
        int inPtr;
        byte[] inBuf;
        if (len < 5) {
            inPtr = this._inputPtr;
            inBuf = this._inputBuffer;
            int q = inBuf[inPtr] & KEYRecord.PROTOCOL_ANY;
            len--;
            if (len > 0) {
                inPtr++;
                q = (q << 8) + (inBuf[inPtr] & KEYRecord.PROTOCOL_ANY);
                len--;
                if (len > 0) {
                    inPtr++;
                    q = (q << 8) + (inBuf[inPtr] & KEYRecord.PROTOCOL_ANY);
                    if (len - 1 > 0) {
                        q = (q << 8) + (inBuf[inPtr + 1] & KEYRecord.PROTOCOL_ANY);
                    }
                }
            }
            this._quad1 = q;
            return this._symbols.findName(q);
        } else if (len >= 9) {
            return _findDecodedMedium(len);
        } else {
            inPtr = this._inputPtr;
            inBuf = this._inputBuffer;
            int inPtr2 = inPtr + 1;
            inPtr = inPtr2 + 1;
            inPtr2 = inPtr + 1;
            inPtr = inPtr2 + 1;
            int q1 = (((((inBuf[inPtr] << 8) + (inBuf[inPtr2] & KEYRecord.PROTOCOL_ANY)) << 8) + (inBuf[inPtr] & KEYRecord.PROTOCOL_ANY)) << 8) + (inBuf[inPtr2] & KEYRecord.PROTOCOL_ANY);
            inPtr2 = inPtr + 1;
            int q2 = inBuf[inPtr] & KEYRecord.PROTOCOL_ANY;
            len -= 5;
            if (len > 0) {
                inPtr = inPtr2 + 1;
                q2 = (q2 << 8) + (inBuf[inPtr2] & KEYRecord.PROTOCOL_ANY);
                len--;
                if (len >= 0) {
                    inPtr2 = inPtr + 1;
                    q2 = (q2 << 8) + (inBuf[inPtr] & KEYRecord.PROTOCOL_ANY);
                    if (len - 1 >= 0) {
                        inPtr = inPtr2 + 1;
                        q2 = (q2 << 8) + (inBuf[inPtr2] & KEYRecord.PROTOCOL_ANY);
                    }
                }
                this._quad1 = q1;
                this._quad2 = q2;
                return this._symbols.findName(q1, q2);
            }
            inPtr = inPtr2;
            this._quad1 = q1;
            this._quad2 = q2;
            return this._symbols.findName(q1, q2);
        }
    }

    private final Name _findDecodedMedium(int len) throws IOException, JsonParseException {
        int bufLen = (len + 3) >> 2;
        if (bufLen > this._quadBuffer.length) {
            this._quadBuffer = _growArrayTo(this._quadBuffer, bufLen);
        }
        int offset = 0;
        int inPtr = this._inputPtr;
        byte[] inBuf = this._inputBuffer;
        while (true) {
            int inPtr2 = inPtr + 1;
            inPtr = inPtr2 + 1;
            inPtr2 = inPtr + 1;
            inPtr = inPtr2 + 1;
            int offset2 = offset + 1;
            this._quadBuffer[offset] = ((((((inBuf[inPtr] << 8) & KEYRecord.PROTOCOL_ANY) | (inBuf[inPtr2] & KEYRecord.PROTOCOL_ANY)) << 8) | (inBuf[inPtr] & KEYRecord.PROTOCOL_ANY)) << 8) | (inBuf[inPtr2] & KEYRecord.PROTOCOL_ANY);
            len -= 4;
            if (len <= 3) {
                break;
            }
            offset = offset2;
        }
        if (len > 0) {
            inPtr2 = inPtr + 1;
            int q = inBuf[inPtr] & KEYRecord.PROTOCOL_ANY;
            len--;
            if (len >= 0) {
                inPtr = inPtr2 + 1;
                q = (q << 8) + (inBuf[inPtr2] & KEYRecord.PROTOCOL_ANY);
                if (len - 1 >= 0) {
                    q = (q << 8) + (inBuf[inPtr] & KEYRecord.PROTOCOL_ANY);
                    inPtr++;
                }
            }
            offset = offset2 + 1;
            this._quadBuffer[offset2] = q;
        } else {
            offset = offset2;
        }
        return this._symbols.findName(this._quadBuffer, offset);
    }

    private static int[] _growArrayTo(int[] arr, int minSize) {
        int[] newArray = new int[(minSize + 4)];
        if (arr != null) {
            System.arraycopy(arr, 0, newArray, 0, arr.length);
        }
        return newArray;
    }

    protected void _parseNumericValue(int expType) throws IOException, JsonParseException {
        if (this._tokenIncomplete) {
            int tb = this._typeByte;
            if (((tb >> 5) & 7) != 1) {
                _reportError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
            }
            this._tokenIncomplete = false;
            _finishNumberToken(tb);
        }
    }

    protected void _finishToken() throws IOException, JsonParseException {
        this._tokenIncomplete = false;
        int tb = this._typeByte;
        int type = (tb >> 5) & 7;
        if (type == 1) {
            _finishNumberToken(tb);
        } else if (type <= 3) {
            _decodeShortAsciiValue((tb & 63) + 1);
        } else if (type <= 5) {
            _decodeShortUnicodeValue((tb & 63) + 2);
        } else {
            if (type == 7) {
                switch ((tb & 31) >> 2) {
                    case KEYRecord.OWNER_USER /*0*/:
                        _decodeLongAscii();
                        return;
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        _decodeLongUnicode();
                        return;
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        this._binaryValue = _read7BitBinaryWithLength();
                        return;
                    case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                        _finishRawBinary();
                        return;
                }
            }
            _throwInternal();
        }
    }

    protected final void _finishNumberToken(int tb) throws IOException, JsonParseException {
        tb &= 31;
        int type = tb >> 2;
        if (type == 1) {
            int subtype = tb & 3;
            if (subtype == 0) {
                _finishInt();
                return;
            } else if (subtype == 1) {
                _finishLong();
                return;
            } else if (subtype == 2) {
                _finishBigInteger();
                return;
            } else {
                _throwInternal();
                return;
            }
        }
        if (type == 2) {
            switch (tb & 3) {
                case KEYRecord.OWNER_USER /*0*/:
                    _finishFloat();
                    return;
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    _finishDouble();
                    return;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    _finishBigDecimal();
                    return;
            }
        }
        _throwInternal();
    }

    private final void _finishInt() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int value = bArr[i];
        if (value < 0) {
            value &= 63;
        } else {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            bArr = this._inputBuffer;
            i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i];
            if (i2 >= 0) {
                value = (value << 7) + i2;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                bArr = this._inputBuffer;
                i = this._inputPtr;
                this._inputPtr = i + 1;
                i2 = bArr[i];
                if (i2 >= 0) {
                    value = (value << 7) + i2;
                    if (this._inputPtr >= this._inputEnd) {
                        loadMoreGuaranteed();
                    }
                    bArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    i2 = bArr[i];
                    if (i2 >= 0) {
                        value = (value << 7) + i2;
                        if (this._inputPtr >= this._inputEnd) {
                            loadMoreGuaranteed();
                        }
                        bArr = this._inputBuffer;
                        i = this._inputPtr;
                        this._inputPtr = i + 1;
                        i2 = bArr[i];
                        if (i2 >= 0) {
                            _reportError("Corrupt input; 32-bit VInt extends beyond 5 data bytes");
                        }
                    }
                }
            }
            value = (value << 6) + (i2 & 63);
        }
        this._numberInt = SmileUtil.zigzagDecode(value);
        this._numTypesValid = 1;
    }

    private final void _finishLong() throws IOException, JsonParseException {
        long l = (long) _fourBytesToInt();
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int value = bArr[i];
            if (value < 0) {
                this._numberLong = SmileUtil.zigzagDecode((l << 6) + ((long) (value & 63)));
                this._numTypesValid = 2;
                return;
            }
            l = (l << 7) + ((long) value);
        }
    }

    private final void _finishBigInteger() throws IOException, JsonParseException {
        this._numberBigInt = new BigInteger(_read7BitBinaryWithLength());
        this._numTypesValid = 4;
    }

    private final void _finishFloat() throws IOException, JsonParseException {
        int i = _fourBytesToInt();
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        int i2 = i << 7;
        byte[] bArr = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = i3 + 1;
        this._numberDouble = (double) Float.intBitsToFloat(i2 + bArr[i3]);
        this._numTypesValid = 8;
    }

    private final void _finishDouble() throws IOException, JsonParseException {
        long value = (((long) _fourBytesToInt()) << 28) + ((long) _fourBytesToInt());
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        long j = value << 7;
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        value = j + ((long) bArr[i]);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        j = value << 7;
        bArr = this._inputBuffer;
        i = this._inputPtr;
        this._inputPtr = i + 1;
        this._numberDouble = Double.longBitsToDouble(j + ((long) bArr[i]));
        this._numTypesValid = 8;
    }

    private final int _fourBytesToInt() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        int i2 = bArr[i];
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        int i3 = i2 << 7;
        byte[] bArr2 = this._inputBuffer;
        int i4 = this._inputPtr;
        this._inputPtr = i4 + 1;
        i2 = i3 + bArr2[i4];
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        i3 = i2 << 7;
        bArr2 = this._inputBuffer;
        i4 = this._inputPtr;
        this._inputPtr = i4 + 1;
        i2 = i3 + bArr2[i4];
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        i3 = i2 << 7;
        bArr2 = this._inputBuffer;
        i4 = this._inputPtr;
        this._inputPtr = i4 + 1;
        return i3 + bArr2[i4];
    }

    private final void _finishBigDecimal() throws IOException, JsonParseException {
        this._numberBigDecimal = new BigDecimal(new BigInteger(_read7BitBinaryWithLength()), SmileUtil.zigzagDecode(_readUnsignedVInt()));
        this._numTypesValid = 16;
    }

    private final int _readUnsignedVInt() throws IOException, JsonParseException {
        int value = 0;
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = bArr[i];
            if (i2 < 0) {
                return (value << 6) + (i2 & 63);
            }
            value = (value << 7) + i2;
        }
    }

    private final byte[] _read7BitBinaryWithLength() throws IOException, JsonParseException {
        int i;
        int byteLen = _readUnsignedVInt();
        byte[] result = new byte[byteLen];
        int lastOkPtr = byteLen - 7;
        int ptr = 0;
        while (ptr <= lastOkPtr) {
            if (this._inputEnd - this._inputPtr < 8) {
                _loadToHaveAtLeast(8);
            }
            byte[] bArr = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            int i3 = bArr[i2] << 25;
            byte[] bArr2 = this._inputBuffer;
            int i4 = this._inputPtr;
            this._inputPtr = i4 + 1;
            i3 += bArr2[i4] << 18;
            bArr2 = this._inputBuffer;
            i4 = this._inputPtr;
            this._inputPtr = i4 + 1;
            i3 += bArr2[i4] << 11;
            bArr2 = this._inputBuffer;
            i4 = this._inputPtr;
            this._inputPtr = i4 + 1;
            int i1 = i3 + (bArr2[i4] << 4);
            bArr = this._inputBuffer;
            i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            int x = bArr[i2];
            i1 += x >> 3;
            i3 = (x & 7) << 21;
            bArr2 = this._inputBuffer;
            i4 = this._inputPtr;
            this._inputPtr = i4 + 1;
            i3 += bArr2[i4] << 14;
            bArr2 = this._inputBuffer;
            i4 = this._inputPtr;
            this._inputPtr = i4 + 1;
            i3 += bArr2[i4] << 7;
            bArr2 = this._inputBuffer;
            i4 = this._inputPtr;
            this._inputPtr = i4 + 1;
            int i22 = i3 + bArr2[i4];
            i = ptr + 1;
            result[ptr] = (byte) (i1 >> 24);
            ptr = i + 1;
            result[i] = (byte) (i1 >> 16);
            i = ptr + 1;
            result[ptr] = (byte) (i1 >> 8);
            ptr = i + 1;
            result[i] = (byte) i1;
            i = ptr + 1;
            result[ptr] = (byte) (i22 >> 16);
            ptr = i + 1;
            result[i] = (byte) (i22 >> 8);
            i = ptr + 1;
            result[ptr] = (byte) i22;
            ptr = i;
        }
        int toDecode = result.length - ptr;
        if (toDecode > 0) {
            if (this._inputEnd - this._inputPtr < toDecode + 1) {
                _loadToHaveAtLeast(toDecode + 1);
            }
            bArr = this._inputBuffer;
            i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            int value = bArr[i2];
            int i5 = 1;
            while (i5 < toDecode) {
                i3 = value << 7;
                bArr2 = this._inputBuffer;
                i4 = this._inputPtr;
                this._inputPtr = i4 + 1;
                value = i3 + bArr2[i4];
                i = ptr + 1;
                result[ptr] = (byte) (value >> (7 - i5));
                i5++;
                ptr = i;
            }
            value <<= toDecode;
            bArr = this._inputBuffer;
            i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            result[ptr] = (byte) (bArr[i2] + value);
        }
        i = ptr;
        return result;
    }

    protected final void _decodeShortAsciiValue(int len) throws IOException, JsonParseException {
        if (this._inputEnd - this._inputPtr < len) {
            _loadToHaveAtLeast(len);
        }
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        byte[] inBuf = this._inputBuffer;
        int inPtr = this._inputPtr;
        int end = inPtr + len;
        int outPtr = 0;
        while (inPtr < end) {
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = (char) inBuf[inPtr];
            inPtr++;
            outPtr = outPtr2;
        }
        this._inputPtr = inPtr;
        this._textBuffer.setCurrentLength(len);
    }

    protected final void _decodeShortUnicodeValue(int len) throws IOException, JsonParseException {
        if (this._inputEnd - this._inputPtr < len) {
            _loadToHaveAtLeast(len);
        }
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int inPtr = this._inputPtr;
        this._inputPtr += len;
        int[] codes = SmileConstants.sUtf8UnitLengths;
        byte[] inputBuf = this._inputBuffer;
        int end = inPtr + len;
        int inPtr2 = inPtr;
        int outPtr = 0;
        while (inPtr2 < end) {
            int i;
            inPtr = inPtr2 + 1;
            int i2 = inputBuf[inPtr2] & KEYRecord.PROTOCOL_ANY;
            int code = codes[i2];
            if (code != 0) {
                switch (code) {
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        i2 = ((i2 & 31) << 6) | (inputBuf[inPtr] & 63);
                        inPtr++;
                        i = outPtr;
                        continue;
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        inPtr2 = inPtr + 1;
                        inPtr = inPtr2 + 1;
                        i2 = (((i2 & 15) << 12) | ((inputBuf[inPtr] & 63) << 6)) | (inputBuf[inPtr2] & 63);
                        i = outPtr;
                        continue;
                    case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                        inPtr2 = inPtr + 1;
                        inPtr = inPtr2 + 1;
                        inPtr2 = inPtr + 1;
                        i2 = (((((i2 & 7) << 18) | ((inputBuf[inPtr] & 63) << 12)) | ((inputBuf[inPtr2] & 63) << 6)) | (inputBuf[inPtr] & 63)) - AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED;
                        i = outPtr + 1;
                        outBuf[outPtr] = (char) (55296 | (i2 >> 10));
                        i2 = 56320 | (i2 & 1023);
                        inPtr = inPtr2;
                        continue;
                    default:
                        _reportError("Invalid byte " + Integer.toHexString(i2) + " in short Unicode text block");
                        break;
                }
            }
            i = outPtr;
            outPtr = i + 1;
            outBuf[i] = (char) i2;
            inPtr2 = inPtr;
        }
        this._textBuffer.setCurrentLength(outPtr);
    }

    private final void _decodeLongAscii() throws IOException, JsonParseException {
        int outPtr = 0;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        while (true) {
            int inPtr;
            int outPtr2;
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            int inPtr2 = this._inputPtr;
            int left = this._inputEnd - inPtr2;
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            left = Math.min(left, outBuf.length - outPtr);
            while (true) {
                inPtr = inPtr2 + 1;
                byte b = this._inputBuffer[inPtr2];
                if (b == -4) {
                    this._inputPtr = inPtr;
                    this._textBuffer.setCurrentLength(outPtr);
                    return;
                }
                outPtr2 = outPtr + 1;
                outBuf[outPtr] = (char) b;
                left--;
                if (left <= 0) {
                    break;
                }
                inPtr2 = inPtr;
                outPtr = outPtr2;
            }
            this._inputPtr = inPtr;
            outPtr = outPtr2;
        }
    }

    private final void _decodeLongUnicode() throws IOException, JsonParseException {
        int outPtr = 0;
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int[] codes = SmileConstants.sUtf8UnitLengths;
        byte[] inputBuffer = this._inputBuffer;
        while (true) {
            int ptr;
            int outPtr2;
            int ptr2 = this._inputPtr;
            if (ptr2 >= this._inputEnd) {
                loadMoreGuaranteed();
                ptr2 = this._inputPtr;
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int max = this._inputEnd;
            int max2 = ptr2 + (outBuf.length - outPtr);
            if (max2 < max) {
                max = max2;
                ptr = ptr2;
                outPtr2 = outPtr;
            } else {
                ptr = ptr2;
                outPtr2 = outPtr;
            }
            while (ptr < max) {
                ptr2 = ptr + 1;
                int c = inputBuffer[ptr] & KEYRecord.PROTOCOL_ANY;
                if (codes[c] != 0) {
                    this._inputPtr = ptr2;
                    if (c == Type.AXFR) {
                        this._textBuffer.setCurrentLength(outPtr2);
                        return;
                    }
                    switch (codes[c]) {
                        case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                            c = _decodeUtf8_2(c);
                            outPtr = outPtr2;
                            break;
                        case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
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
                            _reportInvalidChar(c);
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
                    ptr = ptr2;
                    outPtr2 = outPtr;
                }
            }
            this._inputPtr = ptr;
            outPtr = outPtr2;
        }
    }

    private final void _finishRawBinary() throws IOException, JsonParseException {
        int byteLen = _readUnsignedVInt();
        this._binaryValue = new byte[byteLen];
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        int ptr = 0;
        while (true) {
            int toAdd = Math.min(byteLen, this._inputEnd - this._inputPtr);
            System.arraycopy(this._inputBuffer, this._inputPtr, this._binaryValue, ptr, toAdd);
            this._inputPtr += toAdd;
            ptr += toAdd;
            byteLen -= toAdd;
            if (byteLen > 0) {
                loadMoreGuaranteed();
            } else {
                return;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void _skipIncomplete() throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        /*
        r5 = this;
        r3 = 0;
        r5._tokenIncomplete = r3;
        r2 = r5._typeByte;
        r3 = r2 >> 5;
        r3 = r3 & 7;
        switch(r3) {
            case 1: goto L_0x0010;
            case 2: goto L_0x0056;
            case 3: goto L_0x0056;
            case 4: goto L_0x005e;
            case 5: goto L_0x005e;
            case 6: goto L_0x000c;
            case 7: goto L_0x0066;
            default: goto L_0x000c;
        };
    L_0x000c:
        r5._throwInternal();
    L_0x000f:
        return;
    L_0x0010:
        r2 = r2 & 31;
        r3 = r2 >> 2;
        switch(r3) {
            case 1: goto L_0x0018;
            case 2: goto L_0x003e;
            default: goto L_0x0017;
        };
    L_0x0017:
        goto L_0x000c;
    L_0x0018:
        r3 = r2 & 3;
        switch(r3) {
            case 0: goto L_0x001e;
            case 1: goto L_0x0031;
            case 2: goto L_0x003a;
            default: goto L_0x001d;
        };
    L_0x001d:
        goto L_0x000c;
    L_0x001e:
        r1 = r5._inputEnd;
        r0 = r5._inputBuffer;
    L_0x0022:
        r3 = r5._inputPtr;
        if (r3 >= r1) goto L_0x0036;
    L_0x0026:
        r3 = r5._inputPtr;
        r4 = r3 + 1;
        r5._inputPtr = r4;
        r3 = r0[r3];
        if (r3 >= 0) goto L_0x0022;
    L_0x0030:
        goto L_0x000f;
    L_0x0031:
        r3 = 4;
        r5._skipBytes(r3);
        goto L_0x001e;
    L_0x0036:
        r5.loadMoreGuaranteed();
        goto L_0x001e;
    L_0x003a:
        r5._skip7BitBinary();
        goto L_0x000f;
    L_0x003e:
        r3 = r2 & 3;
        switch(r3) {
            case 0: goto L_0x0044;
            case 1: goto L_0x0049;
            case 2: goto L_0x004f;
            default: goto L_0x0043;
        };
    L_0x0043:
        goto L_0x000c;
    L_0x0044:
        r3 = 5;
        r5._skipBytes(r3);
        goto L_0x000f;
    L_0x0049:
        r3 = 10;
        r5._skipBytes(r3);
        goto L_0x000f;
    L_0x004f:
        r5._readUnsignedVInt();
        r5._skip7BitBinary();
        goto L_0x000f;
    L_0x0056:
        r3 = r2 & 63;
        r3 = r3 + 1;
        r5._skipBytes(r3);
        goto L_0x000f;
    L_0x005e:
        r3 = r2 & 63;
        r3 = r3 + 2;
        r5._skipBytes(r3);
        goto L_0x000f;
    L_0x0066:
        r2 = r2 & 31;
        r3 = r2 >> 2;
        switch(r3) {
            case 0: goto L_0x006e;
            case 1: goto L_0x006e;
            case 2: goto L_0x0086;
            case 3: goto L_0x006d;
            case 4: goto L_0x006d;
            case 5: goto L_0x006d;
            case 6: goto L_0x006d;
            case 7: goto L_0x008a;
            default: goto L_0x006d;
        };
    L_0x006d:
        goto L_0x000c;
    L_0x006e:
        r1 = r5._inputEnd;
        r0 = r5._inputBuffer;
    L_0x0072:
        r3 = r5._inputPtr;
        if (r3 >= r1) goto L_0x0082;
    L_0x0076:
        r3 = r5._inputPtr;
        r4 = r3 + 1;
        r5._inputPtr = r4;
        r3 = r0[r3];
        r4 = -4;
        if (r3 != r4) goto L_0x0072;
    L_0x0081:
        goto L_0x000f;
    L_0x0082:
        r5.loadMoreGuaranteed();
        goto L_0x006e;
    L_0x0086:
        r5._skip7BitBinary();
        goto L_0x000f;
    L_0x008a:
        r3 = r5._readUnsignedVInt();
        r5._skipBytes(r3);
        goto L_0x000f;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.smile.SmileParser._skipIncomplete():void");
    }

    protected void _skipBytes(int len) throws IOException, JsonParseException {
        while (true) {
            int toAdd = Math.min(len, this._inputEnd - this._inputPtr);
            this._inputPtr += toAdd;
            len -= toAdd;
            if (len > 0) {
                loadMoreGuaranteed();
            } else {
                return;
            }
        }
    }

    protected void _skip7BitBinary() throws IOException, JsonParseException {
        int origBytes = _readUnsignedVInt();
        int chunks = origBytes / 7;
        int encBytes = chunks * 8;
        origBytes -= chunks * 7;
        if (origBytes > 0) {
            encBytes += origBytes + 1;
        }
        _skipBytes(encBytes);
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

    protected void _reportInvalidSharedName(int index) throws IOException {
        if (this._seenNames == null) {
            _reportError("Encountered shared name reference, even though document header explicitly declared no shared name references are included");
        }
        _reportError("Invalid shared name reference " + index + "; only got " + this._seenNameCount + " names in buffer (invalid content)");
    }

    protected void _reportInvalidSharedStringValue(int index) throws IOException {
        if (this._seenStringValues == null) {
            _reportError("Encountered shared text value reference, even though document header did not declared shared text value references may be included");
        }
        _reportError("Invalid shared text value reference " + index + "; only got " + this._seenStringValueCount + " names in buffer (invalid content)");
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
}
