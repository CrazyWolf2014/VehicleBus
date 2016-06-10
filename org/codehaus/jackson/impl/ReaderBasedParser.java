package org.codehaus.jackson.impl;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.io.IOException;
import java.io.Reader;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;
import org.codehaus.jackson.sym.CharsToNameCanonicalizer;
import org.codehaus.jackson.util.CharTypes;
import org.codehaus.jackson.util.TextBuffer;
import org.ksoap2.SoapEnvelope;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;

public final class ReaderBasedParser extends ReaderBasedNumericParser {
    protected ObjectCodec _objectCodec;
    protected final CharsToNameCanonicalizer _symbols;
    protected boolean _tokenIncomplete;

    /* renamed from: org.codehaus.jackson.impl.ReaderBasedParser.1 */
    static /* synthetic */ class C09361 {
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

    public ReaderBasedParser(IOContext ioCtxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st) {
        super(ioCtxt, features, r);
        this._tokenIncomplete = false;
        this._objectCodec = codec;
        this._symbols = st;
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }

    public final String getText() throws IOException, JsonParseException {
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
        switch (C09361.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
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
        switch (C09361.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
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
        switch (C09361.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
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
        switch (C09361.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
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
            boolean inObject = this._parsingContext.inObject();
            if (inObject) {
                this._parsingContext.setCurrentName(_parseFieldName(i));
                this._currToken = JsonToken.FIELD_NAME;
                i = _skipWS();
                if (i != 58) {
                    _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
                }
                i = _skipWS();
            }
            switch (i) {
                case Type.ATMA /*34*/:
                    this._tokenIncomplete = true;
                    jsonToken = JsonToken.VALUE_STRING;
                    break;
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
                    break;
                case Service.MIT_DOV /*91*/:
                    if (!inObject) {
                        this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                    }
                    jsonToken = JsonToken.START_ARRAY;
                    break;
                case Service.DCP /*93*/:
                case Service.LOCUS_MAP /*125*/:
                    _reportUnexpectedChar(i, "expected a value");
                    break;
                case Service.ISO_TSAP /*102*/:
                    _matchToken(JsonToken.VALUE_FALSE);
                    jsonToken = JsonToken.VALUE_FALSE;
                    break;
                case SoapEnvelope.VER11 /*110*/:
                    _matchToken(JsonToken.VALUE_NULL);
                    jsonToken = JsonToken.VALUE_NULL;
                    break;
                case Opcodes.INEG /*116*/:
                    break;
                case Service.NTP /*123*/:
                    if (!inObject) {
                        this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                    }
                    jsonToken = JsonToken.START_OBJECT;
                    break;
                default:
                    jsonToken = _handleUnexpectedValue(i);
                    break;
            }
            _matchToken(JsonToken.VALUE_TRUE);
            jsonToken = JsonToken.VALUE_TRUE;
            if (inObject) {
                this._nextToken = jsonToken;
                return this._currToken;
            }
            this._currToken = jsonToken;
            return jsonToken;
        }
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

    protected final String _parseFieldName(int i) throws IOException, JsonParseException {
        if (i != 34) {
            return _handleUnusualFieldName(i);
        }
        int start;
        int ptr = this._inputPtr;
        int hash = 0;
        int inputLen = this._inputEnd;
        if (ptr < inputLen) {
            int[] codes = CharTypes.getInputCodeLatin1();
            int maxCode = codes.length;
            do {
                int ch = this._inputBuffer[ptr];
                if (ch >= maxCode || codes[ch] == 0) {
                    hash = (hash * 31) + ch;
                    ptr++;
                } else if (ch == 34) {
                    start = this._inputPtr;
                    this._inputPtr = ptr + 1;
                    return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
                }
            } while (ptr < inputLen);
        }
        start = this._inputPtr;
        this._inputPtr = ptr;
        return _parseFieldName2(start, hash, 34);
    }

    private String _parseFieldName2(int startPtr, int hash, int endChar) throws IOException, JsonParseException {
        this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
        char[] outBuf = this._textBuffer.getCurrentSegment();
        int outPtr = this._textBuffer.getCurrentSegmentSize();
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(": was expecting closing '" + ((char) endChar) + "' for name");
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            char c = cArr[i];
            char i2 = c;
            if (i2 <= '\\') {
                if (i2 == '\\') {
                    c = _decodeEscaped();
                } else if (i2 <= endChar) {
                    if (i2 == endChar) {
                        this._textBuffer.setCurrentLength(outPtr);
                        TextBuffer tb = this._textBuffer;
                        return this._symbols.findSymbol(tb.getTextBuffer(), tb.getTextOffset(), tb.size(), hash);
                    } else if (i2 < ' ') {
                        _throwUnquotedSpace(i2, "name");
                    }
                }
            }
            hash = (hash * 31) + i2;
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = c;
            if (outPtr2 >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            } else {
                outPtr = outPtr2;
            }
        }
    }

    protected final String _handleUnusualFieldName(int i) throws IOException, JsonParseException {
        if (i == 39 && isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            return _parseApostropheFieldName();
        }
        int start;
        if (!isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            _reportUnexpectedChar(i, "was expecting double-quote to start field name");
        }
        int[] codes = CharTypes.getInputCodeLatin1JsNames();
        int maxCode = codes.length;
        boolean firstOk = i < maxCode ? codes[i] == 0 && (i < 48 || i > 57) : Character.isJavaIdentifierPart((char) i);
        if (!firstOk) {
            _reportUnexpectedChar(i, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int ptr = this._inputPtr;
        int hash = 0;
        int inputLen = this._inputEnd;
        if (ptr < inputLen) {
            do {
                int ch = this._inputBuffer[ptr];
                if (ch < maxCode) {
                    if (codes[ch] != 0) {
                        start = this._inputPtr - 1;
                        this._inputPtr = ptr;
                        return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
                    }
                } else if (!Character.isJavaIdentifierPart((char) ch)) {
                    start = this._inputPtr - 1;
                    this._inputPtr = ptr;
                    return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
                }
                hash = (hash * 31) + ch;
                ptr++;
            } while (ptr < inputLen);
        }
        start = this._inputPtr - 1;
        this._inputPtr = ptr;
        return _parseUnusualFieldName2(start, hash, codes);
    }

    protected final String _parseApostropheFieldName() throws IOException, JsonParseException {
        int start;
        int ptr = this._inputPtr;
        int hash = 0;
        int inputLen = this._inputEnd;
        if (ptr < inputLen) {
            int[] codes = CharTypes.getInputCodeLatin1();
            int maxCode = codes.length;
            do {
                int ch = this._inputBuffer[ptr];
                if (ch != 39) {
                    if (ch < maxCode && codes[ch] != 0) {
                        break;
                    }
                    hash = (hash * 31) + ch;
                    ptr++;
                } else {
                    start = this._inputPtr;
                    this._inputPtr = ptr + 1;
                    return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
                }
            } while (ptr < inputLen);
        }
        start = this._inputPtr;
        this._inputPtr = ptr;
        return _parseFieldName2(start, hash, 39);
    }

    protected final JsonToken _handleUnexpectedValue(int i) throws IOException, JsonParseException {
        if (!(i == 39 && isEnabled(Feature.ALLOW_SINGLE_QUOTES))) {
            _reportUnexpectedChar(i, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        }
        char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
        int outPtr = this._textBuffer.getCurrentSegmentSize();
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(": was expecting closing quote for a string value");
            }
            char[] cArr = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            char c = cArr[i2];
            char i3 = c;
            if (i3 <= '\\') {
                if (i3 == '\\') {
                    c = _decodeEscaped();
                } else if (i3 <= '\'') {
                    if (i3 == '\'') {
                        this._textBuffer.setCurrentLength(outPtr);
                        return JsonToken.VALUE_STRING;
                    } else if (i3 < ' ') {
                        _throwUnquotedSpace(i3, "string value");
                    }
                }
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = c;
            outPtr = outPtr2;
        }
    }

    private String _parseUnusualFieldName2(int startPtr, int hash, int[] codes) throws IOException, JsonParseException {
        this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
        char[] outBuf = this._textBuffer.getCurrentSegment();
        int outPtr = this._textBuffer.getCurrentSegmentSize();
        char maxCode = codes.length;
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                break;
            }
            char c = this._inputBuffer[this._inputPtr];
            char i = c;
            if (i > maxCode) {
                if (!Character.isJavaIdentifierPart(c)) {
                    break;
                }
            } else if (codes[i] != 0) {
                break;
            }
            this._inputPtr++;
            hash = (hash * 31) + i;
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = c;
            if (outPtr2 >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            } else {
                outPtr = outPtr2;
            }
        }
        this._textBuffer.setCurrentLength(outPtr);
        TextBuffer tb = this._textBuffer;
        return this._symbols.findSymbol(tb.getTextBuffer(), tb.getTextOffset(), tb.size(), hash);
    }

    protected void _finishString() throws IOException, JsonParseException {
        int ptr = this._inputPtr;
        int inputLen = this._inputEnd;
        if (ptr < inputLen) {
            int[] codes = CharTypes.getInputCodeLatin1();
            int maxCode = codes.length;
            do {
                int ch = this._inputBuffer[ptr];
                if (ch >= maxCode || codes[ch] == 0) {
                    ptr++;
                } else if (ch == 34) {
                    this._textBuffer.resetWithShared(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
                    this._inputPtr = ptr + 1;
                    return;
                }
            } while (ptr < inputLen);
        }
        this._textBuffer.resetWithCopy(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
        this._inputPtr = ptr;
        _finishString2();
    }

    protected void _finishString2() throws IOException, JsonParseException {
        char[] outBuf = this._textBuffer.getCurrentSegment();
        int outPtr = this._textBuffer.getCurrentSegmentSize();
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(": was expecting closing quote for a string value");
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            char c = cArr[i];
            char i2 = c;
            if (i2 <= '\\') {
                if (i2 == '\\') {
                    c = _decodeEscaped();
                } else if (i2 <= '\"') {
                    if (i2 == '\"') {
                        this._textBuffer.setCurrentLength(outPtr);
                        return;
                    } else if (i2 < ' ') {
                        _throwUnquotedSpace(i2, "string value");
                    }
                }
            }
            if (outPtr >= outBuf.length) {
                outBuf = this._textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            int outPtr2 = outPtr + 1;
            outBuf[outPtr] = c;
            outPtr = outPtr2;
        }
    }

    protected void _skipString() throws IOException, JsonParseException {
        this._tokenIncomplete = false;
        int inputPtr = this._inputPtr;
        int inputLen = this._inputEnd;
        char[] inputBuffer = this._inputBuffer;
        while (true) {
            if (inputPtr >= inputLen) {
                this._inputPtr = inputPtr;
                if (!loadMore()) {
                    _reportInvalidEOF(": was expecting closing quote for a string value");
                }
                inputPtr = this._inputPtr;
                inputLen = this._inputEnd;
            }
            int inputPtr2 = inputPtr + 1;
            char i = inputBuffer[inputPtr];
            if (i <= '\\') {
                if (i == '\\') {
                    this._inputPtr = inputPtr2;
                    char c = _decodeEscaped();
                    inputPtr = this._inputPtr;
                    inputLen = this._inputEnd;
                } else if (i <= '\"') {
                    if (i == '\"') {
                        this._inputPtr = inputPtr2;
                        return;
                    } else if (i < ' ') {
                        this._inputPtr = inputPtr2;
                        _throwUnquotedSpace(i, "string value");
                    }
                }
            }
            inputPtr = inputPtr2;
        }
    }

    protected void _matchToken(JsonToken token) throws IOException, JsonParseException {
        String matchStr = token.asString();
        int len = matchStr.length();
        for (int i = 1; i < len; i++) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in a value");
            }
            if (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
                _reportInvalidToken(matchStr.substring(0, i));
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
            char c = this._inputBuffer[this._inputPtr];
            if (!Character.isJavaIdentifierPart(c)) {
                break;
            }
            this._inputPtr++;
            sb.append(c);
        }
        _reportError("Unrecognized token '" + sb.toString() + "': was expecting 'null', 'true' or 'false'");
    }

    protected final void _skipCR() throws IOException {
        if ((this._inputPtr < this._inputEnd || loadMore()) && this._inputBuffer[this._inputPtr] == '\n') {
            this._inputPtr++;
        }
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    protected final void _skipLF() throws IOException {
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    private final int _skipWS() throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                break;
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = cArr[i];
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
                char[] cArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = cArr[i];
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
        char[] cArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        char c = cArr[i];
        if (c == '/') {
            _skipCppComment();
        } else if (c == '*') {
            _skipCComment();
        } else {
            _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
        }
    }

    private final void _skipCComment() throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                break;
            }
            char[] cArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            int i2 = cArr[i];
            if (i2 <= 42) {
                if (i2 == 42) {
                    if (this._inputPtr >= this._inputEnd && !loadMore()) {
                        break;
                    } else if (this._inputBuffer[this._inputPtr] == '/') {
                        this._inputPtr++;
                        return;
                    }
                } else if (i2 < 32) {
                    if (i2 == 10) {
                        _skipLF();
                    } else if (i2 == 13) {
                        _skipCR();
                    } else if (i2 != 9) {
                        _throwInvalidSpace(i2);
                    }
                }
            }
        }
        _reportInvalidEOF(" in a comment");
    }

    private final void _skipCppComment() throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                char[] cArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                int i2 = cArr[i];
                if (i2 < 32) {
                    if (i2 == 10) {
                        _skipLF();
                        return;
                    } else if (i2 == 13) {
                        _skipCR();
                        return;
                    } else if (i2 != 9) {
                        _throwInvalidSpace(i2);
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
        char[] cArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        char c = cArr[i];
        switch (c) {
            case Type.ATMA /*34*/:
            case Service.NI_FTP /*47*/:
            case Opcodes.DUP2 /*92*/:
                return c;
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
                    cArr = this._inputBuffer;
                    i = this._inputPtr;
                    this._inputPtr = i + 1;
                    int ch = cArr[i];
                    int digit = CharTypes.charToHex(ch);
                    if (digit < 0) {
                        _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
                    }
                    value = (value << 4) | digit;
                }
                return (char) value;
            default:
                return _handleUnrecognizedCharacterEscape(c);
        }
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
        r2 = r4[r5];
        r4 = 32;
        if (r2 <= r4) goto L_0x0006;
    L_0x001d:
        r0 = r10.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x0032;
    L_0x0023:
        r4 = 34;
        if (r2 != r4) goto L_0x002c;
    L_0x0027:
        r4 = r1.toByteArray();
        return r4;
    L_0x002c:
        r4 = 0;
        r4 = r9.reportInvalidChar(r10, r2, r4);
        throw r4;
    L_0x0032:
        r3 = r0;
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x003c;
    L_0x0039:
        r9.loadMoreGuaranteed();
    L_0x003c:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r2 = r4[r5];
        r0 = r10.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x0052;
    L_0x004c:
        r4 = 1;
        r4 = r9.reportInvalidChar(r10, r2, r4);
        throw r4;
    L_0x0052:
        r4 = r3 << 6;
        r3 = r4 | r0;
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x005f;
    L_0x005c:
        r9.loadMoreGuaranteed();
    L_0x005f:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r2 = r4[r5];
        r0 = r10.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x00b9;
    L_0x006f:
        if (r0 == r7) goto L_0x0077;
    L_0x0071:
        r4 = 2;
        r4 = r9.reportInvalidChar(r10, r2, r4);
        throw r4;
    L_0x0077:
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x0080;
    L_0x007d:
        r9.loadMoreGuaranteed();
    L_0x0080:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r2 = r4[r5];
        r4 = r10.usesPaddingChar(r2);
        if (r4 != 0) goto L_0x00b2;
    L_0x0090:
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
    L_0x00b2:
        r3 = r3 >> 4;
        r1.append(r3);
        goto L_0x0006;
    L_0x00b9:
        r4 = r3 << 6;
        r3 = r4 | r0;
        r4 = r9._inputPtr;
        r5 = r9._inputEnd;
        if (r4 < r5) goto L_0x00c6;
    L_0x00c3:
        r9.loadMoreGuaranteed();
    L_0x00c6:
        r4 = r9._inputBuffer;
        r5 = r9._inputPtr;
        r6 = r5 + 1;
        r9._inputPtr = r6;
        r2 = r4[r5];
        r0 = r10.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x00e4;
    L_0x00d6:
        if (r0 == r7) goto L_0x00dd;
    L_0x00d8:
        r4 = r9.reportInvalidChar(r10, r2, r8);
        throw r4;
    L_0x00dd:
        r3 = r3 >> 2;
        r1.appendTwoBytes(r3);
        goto L_0x0006;
    L_0x00e4:
        r4 = r3 << 6;
        r3 = r4 | r0;
        r1.appendThreeBytes(r3);
        goto L_0x0006;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.ReaderBasedParser._decodeBase64(org.codehaus.jackson.Base64Variant):byte[]");
    }

    protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, char ch, int bindex) throws IllegalArgumentException {
        return reportInvalidChar(b64variant, ch, bindex, null);
    }

    protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, char ch, int bindex, String msg) throws IllegalArgumentException {
        String base;
        if (ch <= ' ') {
            base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units";
        } else if (b64variant.usesPaddingChar(ch)) {
            base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        } else if (!Character.isDefined(ch) || Character.isISOControl(ch)) {
            base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        } else {
            base = "Illegal character '" + ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        }
        if (msg != null) {
            base = base + ": " + msg;
        }
        return new IllegalArgumentException(base);
    }
}
