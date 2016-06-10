package org.codehaus.jackson.smile;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.impl.JsonGeneratorBase;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public class SmileGenerator extends JsonGeneratorBase {
    protected static final long MAX_INT_AS_LONG = 2147483647L;
    private static final int MIN_BUFFER_LENGTH = 770;
    protected static final long MIN_INT_AS_LONG = -2147483648L;
    protected static final int SURR1_FIRST = 55296;
    protected static final int SURR1_LAST = 56319;
    protected static final int SURR2_FIRST = 56320;
    protected static final int SURR2_LAST = 57343;
    protected static final byte TOKEN_BYTE_BIG_DECIMAL = (byte) 42;
    protected static final byte TOKEN_BYTE_BIG_INTEGER = (byte) 38;
    protected static final byte TOKEN_BYTE_FLOAT_32 = (byte) 40;
    protected static final byte TOKEN_BYTE_FLOAT_64 = (byte) 41;
    protected static final byte TOKEN_BYTE_INT_32 = (byte) 36;
    protected static final byte TOKEN_BYTE_INT_64 = (byte) 37;
    protected static final byte TOKEN_BYTE_LONG_STRING_ASCII = (byte) -32;
    protected static final byte TOKEN_BYTE_LONG_STRING_UNICODE = (byte) -28;
    protected static final ThreadLocal<SoftReference<SmileBufferRecycler<SharedStringNode>>> _smileRecyclerRef;
    protected boolean _bufferRecyclable;
    protected int _bytesWritten;
    protected char[] _charBuffer;
    protected final int _charBufferLength;
    protected final IOContext _ioContext;
    protected final OutputStream _out;
    protected byte[] _outputBuffer;
    protected final int _outputEnd;
    protected int _outputTail;
    protected int _seenNameCount;
    protected SharedStringNode[] _seenNames;
    protected int _seenStringValueCount;
    protected SharedStringNode[] _seenStringValues;
    protected final SmileBufferRecycler<SharedStringNode> _smileBufferRecycler;
    protected int _smileFeatures;

    public enum Feature {
        WRITE_HEADER(true),
        WRITE_END_MARKER(false),
        ENCODE_BINARY_AS_7BIT(true),
        CHECK_SHARED_NAMES(true),
        CHECK_SHARED_STRING_VALUES(false);
        
        protected final boolean _defaultState;
        protected final int _mask;

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

    protected static final class SharedStringNode {
        public final int index;
        public SharedStringNode next;
        public final String value;

        public SharedStringNode(String value, int index, SharedStringNode next) {
            this.value = value;
            this.index = index;
            this.next = next;
        }
    }

    static {
        _smileRecyclerRef = new ThreadLocal();
    }

    public SmileGenerator(IOContext ctxt, int jsonFeatures, int smileFeatures, ObjectCodec codec, OutputStream out) {
        super(jsonFeatures, codec);
        this._outputTail = 0;
        this._smileFeatures = smileFeatures;
        this._ioContext = ctxt;
        this._smileBufferRecycler = _smileBufferRecycler();
        this._out = out;
        this._bufferRecyclable = true;
        this._outputBuffer = ctxt.allocWriteEncodingBuffer();
        this._outputEnd = this._outputBuffer.length;
        this._charBuffer = ctxt.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
        if (this._outputEnd < MIN_BUFFER_LENGTH) {
            throw new IllegalStateException("Internal encoding buffer length (" + this._outputEnd + ") too short, must be at least " + MIN_BUFFER_LENGTH);
        }
        if ((Feature.CHECK_SHARED_NAMES.getMask() & smileFeatures) == 0) {
            this._seenNames = null;
            this._seenNameCount = -1;
        } else {
            this._seenNames = (SharedStringNode[]) this._smileBufferRecycler.allocSeenNamesBuffer();
            if (this._seenNames == null) {
                this._seenNames = new SharedStringNode[64];
            }
            this._seenNameCount = 0;
        }
        if ((Feature.CHECK_SHARED_STRING_VALUES.getMask() & smileFeatures) == 0) {
            this._seenStringValues = null;
            this._seenStringValueCount = -1;
            return;
        }
        this._seenStringValues = (SharedStringNode[]) this._smileBufferRecycler.allocSeenStringValuesBuffer();
        if (this._seenStringValues == null) {
            this._seenStringValues = new SharedStringNode[64];
        }
        this._seenStringValueCount = 0;
    }

    public SmileGenerator(IOContext ctxt, int jsonFeatures, int smileFeatures, ObjectCodec codec, OutputStream out, byte[] outputBuffer, int offset, boolean bufferRecyclable) {
        super(jsonFeatures, codec);
        this._outputTail = 0;
        this._smileFeatures = smileFeatures;
        this._ioContext = ctxt;
        this._smileBufferRecycler = _smileBufferRecycler();
        this._out = out;
        this._bufferRecyclable = bufferRecyclable;
        this._outputTail = offset;
        this._outputBuffer = outputBuffer;
        this._outputEnd = this._outputBuffer.length;
        this._charBuffer = ctxt.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
        if (this._outputEnd < MIN_BUFFER_LENGTH) {
            throw new IllegalStateException("Internal encoding buffer length (" + this._outputEnd + ") too short, must be at least " + MIN_BUFFER_LENGTH);
        }
        if ((Feature.CHECK_SHARED_NAMES.getMask() & smileFeatures) == 0) {
            this._seenNames = null;
            this._seenNameCount = -1;
        } else {
            this._seenNames = (SharedStringNode[]) this._smileBufferRecycler.allocSeenNamesBuffer();
            if (this._seenNames == null) {
                this._seenNames = new SharedStringNode[64];
            }
            this._seenNameCount = 0;
        }
        if ((Feature.CHECK_SHARED_STRING_VALUES.getMask() & smileFeatures) == 0) {
            this._seenStringValues = null;
            this._seenStringValueCount = -1;
            return;
        }
        this._seenStringValues = (SharedStringNode[]) this._smileBufferRecycler.allocSeenStringValuesBuffer();
        if (this._seenStringValues == null) {
            this._seenStringValues = new SharedStringNode[64];
        }
        this._seenStringValueCount = 0;
    }

    public void writeHeader() throws IOException {
        int last = 0;
        if ((this._smileFeatures & Feature.CHECK_SHARED_NAMES.getMask()) != 0) {
            last = 0 | 1;
        }
        if ((this._smileFeatures & Feature.CHECK_SHARED_STRING_VALUES.getMask()) != 0) {
            last |= 2;
        }
        if ((this._smileFeatures & Feature.ENCODE_BINARY_AS_7BIT.getMask()) == 0) {
            last |= 4;
        }
        _writeBytes(SmileConstants.HEADER_BYTE_1, TOKEN_BYTE_FLOAT_64, (byte) 10, (byte) last);
    }

    protected static final SmileBufferRecycler<SharedStringNode> _smileBufferRecycler() {
        SoftReference<SmileBufferRecycler<SharedStringNode>> ref = (SoftReference) _smileRecyclerRef.get();
        SmileBufferRecycler<SharedStringNode> br = ref == null ? null : (SmileBufferRecycler) ref.get();
        if (br != null) {
            return br;
        }
        br = new SmileBufferRecycler();
        _smileRecyclerRef.set(new SoftReference(br));
        return br;
    }

    public JsonGenerator useDefaultPrettyPrinter() {
        return this;
    }

    public JsonGenerator setPrettyPrinter(PrettyPrinter pp) {
        return this;
    }

    public final void writeFieldName(String name) throws IOException, JsonGenerationException {
        if (this._writeContext.writeFieldName(name) == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        _writeFieldName(name);
    }

    public final void writeFieldName(SerializedString name) throws IOException, JsonGenerationException {
        if (this._writeContext.writeFieldName(name.getValue()) == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        _writeFieldName((SerializableString) name);
    }

    public final void writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        if (this._writeContext.writeFieldName(name.getValue()) == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        _writeFieldName(name);
    }

    public final void writeStringField(String fieldName, String value) throws IOException, JsonGenerationException {
        if (this._writeContext.writeFieldName(fieldName) == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        _writeFieldName(fieldName);
        writeString(value);
    }

    public SmileGenerator enable(Feature f) {
        this._smileFeatures |= f.getMask();
        return this;
    }

    public SmileGenerator disable(Feature f) {
        this._smileFeatures &= f.getMask() ^ -1;
        return this;
    }

    public final boolean isEnabled(Feature f) {
        return (this._smileFeatures & f.getMask()) != 0;
    }

    public SmileGenerator configure(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public void writeRaw(byte b) throws IOException, JsonGenerationException {
        _writeByte((byte) -8);
    }

    public void writeBytes(byte[] data, int offset, int len) throws IOException {
        _writeBytes(data, offset, len);
    }

    public final void writeStartArray() throws IOException, JsonGenerationException {
        _verifyValueWrite("start an array");
        this._writeContext = this._writeContext.createChildArrayContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartArray(this);
        } else {
            _writeByte((byte) -8);
        }
    }

    public final void writeEndArray() throws IOException, JsonGenerationException {
        if (!this._writeContext.inArray()) {
            _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        } else {
            _writeByte((byte) -7);
        }
        this._writeContext = this._writeContext.getParent();
    }

    public final void writeStartObject() throws IOException, JsonGenerationException {
        _verifyValueWrite("start an object");
        this._writeContext = this._writeContext.createChildObjectContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartObject(this);
        } else {
            _writeByte((byte) -6);
        }
    }

    public final void writeEndObject() throws IOException, JsonGenerationException {
        if (!this._writeContext.inObject()) {
            _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
        }
        this._writeContext = this._writeContext.getParent();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
        } else {
            _writeByte((byte) -5);
        }
    }

    private final void _writeFieldName(String name) throws IOException, JsonGenerationException {
        int len = name.length();
        if (len == 0) {
            _writeByte(SmileConstants.TOKEN_LITERAL_EMPTY_STRING);
            return;
        }
        if (this._seenNameCount >= 0) {
            int ix = _findSeenName(name);
            if (ix >= 0) {
                _writeSharedNameReference(ix);
                return;
            }
        }
        if (len > 56) {
            _writeNonShortFieldName(name, len);
            return;
        }
        byte typeToken;
        if (this._outputTail + Wbxml.LITERAL_AC >= this._outputEnd) {
            _flushBuffer();
        }
        name.getChars(0, len, this._charBuffer, 0);
        int origOffset = this._outputTail;
        this._outputTail++;
        int byteLen = _shortUTF8Encode(this._charBuffer, 0, len);
        byte[] bArr;
        int i;
        if (byteLen == len) {
            if (byteLen <= 64) {
                typeToken = (byte) (byteLen + Service.LOCUS_CON);
            } else {
                typeToken = SmileConstants.TOKEN_KEY_LONG_STRING;
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) -4;
            }
        } else if (byteLen <= 56) {
            typeToken = (byte) (byteLen + Opcodes.ARRAYLENGTH);
        } else {
            typeToken = SmileConstants.TOKEN_KEY_LONG_STRING;
            bArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) -4;
        }
        this._outputBuffer[origOffset] = typeToken;
        if (this._seenNameCount >= 0) {
            _addSeenName(name);
        }
    }

    private final void _writeNonShortFieldName(String name, int len) throws IOException, JsonGenerationException {
        _writeByte(SmileConstants.TOKEN_KEY_LONG_STRING);
        if (len > this._charBufferLength) {
            _slowUTF8Encode(name);
        } else {
            name.getChars(0, len, this._charBuffer, 0);
            int maxLen = (len + len) + len;
            if (maxLen <= this._outputBuffer.length) {
                if (this._outputTail + maxLen >= this._outputEnd) {
                    _flushBuffer();
                }
                _shortUTF8Encode(this._charBuffer, 0, len);
            } else {
                _mediumUTF8Encode(this._charBuffer, 0, len);
            }
        }
        if (this._seenNameCount >= 0) {
            _addSeenName(name);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) -4;
    }

    protected final void _writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        int charLen = name.charLength();
        if (charLen == 0) {
            _writeByte(SmileConstants.TOKEN_LITERAL_EMPTY_STRING);
            return;
        }
        byte[] bytes = name.asUnquotedUTF8();
        int byteLen = bytes.length;
        if (byteLen != charLen) {
            _writeFieldNameUnicode(name, bytes);
            return;
        }
        if (this._seenNameCount >= 0) {
            int ix = _findSeenName(name.getValue());
            if (ix >= 0) {
                _writeSharedNameReference(ix);
                return;
            }
        }
        if (byteLen <= 64) {
            if (this._outputTail + byteLen >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) (byteLen + Service.LOCUS_CON);
            System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, byteLen);
            this._outputTail += byteLen;
            if (this._seenNameCount >= 0) {
                _addSeenName(name.getValue());
                return;
            }
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = SmileConstants.TOKEN_KEY_LONG_STRING;
        if ((this._outputTail + byteLen) + 1 < this._outputEnd) {
            System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, byteLen);
            this._outputTail += byteLen;
        } else {
            _flushBuffer();
            if (byteLen < MIN_BUFFER_LENGTH) {
                System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, byteLen);
                this._outputTail += byteLen;
            } else {
                if (this._outputTail > 0) {
                    _flushBuffer();
                }
                this._out.write(bytes, 0, byteLen);
            }
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) -4;
        if (this._seenNameCount >= 0) {
            _addSeenName(name.getValue());
        }
    }

    protected final void _writeFieldNameUnicode(SerializableString name, byte[] bytes) throws IOException, JsonGenerationException {
        if (this._seenNameCount >= 0) {
            int ix = _findSeenName(name.getValue());
            if (ix >= 0) {
                _writeSharedNameReference(ix);
                return;
            }
        }
        int byteLen = bytes.length;
        if (byteLen <= 56) {
            if (this._outputTail + byteLen >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) (byteLen + Opcodes.ARRAYLENGTH);
            System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, byteLen);
            this._outputTail += byteLen;
            if (this._seenNameCount >= 0) {
                _addSeenName(name.getValue());
                return;
            }
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = SmileConstants.TOKEN_KEY_LONG_STRING;
        if ((this._outputTail + byteLen) + 1 < this._outputEnd) {
            System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, byteLen);
            this._outputTail += byteLen;
        } else {
            _flushBuffer();
            if (byteLen < MIN_BUFFER_LENGTH) {
                System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, byteLen);
                this._outputTail += byteLen;
            } else {
                if (this._outputTail > 0) {
                    _flushBuffer();
                }
                this._out.write(bytes, 0, byteLen);
            }
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) -4;
        if (this._seenNameCount >= 0) {
            _addSeenName(name.getValue());
        }
    }

    private final void _writeSharedNameReference(int ix) throws IOException, JsonGenerationException {
        if (ix >= this._seenNameCount) {
            throw new IllegalArgumentException("Internal error: trying to write shared name with index " + ix + "; but have only seen " + this._seenNameCount + " so far!");
        } else if (ix < 64) {
            _writeByte((byte) (ix + 64));
        } else {
            _writeBytes((byte) ((ix >> 8) + 48), (byte) ix);
        }
    }

    public void writeString(String text) throws IOException, JsonGenerationException {
        _verifyValueWrite("write String value");
        int len = text.length();
        if (len == 0) {
            _writeByte(SmileConstants.TOKEN_LITERAL_EMPTY_STRING);
        } else if (len > 65) {
            _writeNonSharedString(text, len);
        } else {
            if (this._seenStringValueCount >= 0) {
                int ix = _findSeenStringValue(text);
                if (ix >= 0) {
                    _writeSharedStringValueReference(ix);
                    return;
                }
            }
            if (this._outputTail + Wbxml.LITERAL_AC >= this._outputEnd) {
                _flushBuffer();
            }
            text.getChars(0, len, this._charBuffer, 0);
            int origOffset = this._outputTail;
            this._outputTail++;
            int byteLen = _shortUTF8Encode(this._charBuffer, 0, len);
            if (byteLen <= 64) {
                if (this._seenStringValueCount >= 0) {
                    _addSeenStringValue(text);
                }
                if (byteLen == len) {
                    this._outputBuffer[origOffset] = (byte) (byteLen + 63);
                    return;
                } else {
                    this._outputBuffer[origOffset] = (byte) (byteLen + Opcodes.IAND);
                    return;
                }
            }
            this._outputBuffer[origOffset] = byteLen == len ? TOKEN_BYTE_LONG_STRING_ASCII : TOKEN_BYTE_LONG_STRING_UNICODE;
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) -4;
        }
    }

    private final void _writeSharedStringValueReference(int ix) throws IOException, JsonGenerationException {
        if (ix >= this._seenStringValueCount) {
            throw new IllegalArgumentException("Internal error: trying to write shared String value with index " + ix + "; but have only seen " + this._seenStringValueCount + " so far!");
        } else if (ix < 31) {
            _writeByte((byte) (ix + 1));
        } else {
            _writeBytes((byte) ((ix >> 8) + SmileConstants.TOKEN_MISC_SHARED_STRING_LONG), (byte) ix);
        }
    }

    private final void _writeNonSharedString(String text, int len) throws IOException, JsonGenerationException {
        if (len > this._charBufferLength) {
            _writeByte(TOKEN_BYTE_LONG_STRING_UNICODE);
            _slowUTF8Encode(text);
            _writeByte((byte) -4);
            return;
        }
        text.getChars(0, len, this._charBuffer, 0);
        int maxLen = ((len + len) + len) + 2;
        if (maxLen > this._outputBuffer.length) {
            _writeByte(TOKEN_BYTE_LONG_STRING_UNICODE);
            _mediumUTF8Encode(this._charBuffer, 0, len);
            _writeByte((byte) -4);
            return;
        }
        if (this._outputTail + maxLen >= this._outputEnd) {
            _flushBuffer();
        }
        int origOffset = this._outputTail;
        _writeByte(TOKEN_BYTE_LONG_STRING_ASCII);
        if (_shortUTF8Encode(this._charBuffer, 0, len) > len) {
            this._outputBuffer[origOffset] = TOKEN_BYTE_LONG_STRING_UNICODE;
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) -4;
    }

    public void writeString(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        if (len > 65 || this._seenStringValueCount < 0 || len <= 0) {
            _verifyValueWrite("write String value");
            if (len == 0) {
                _writeByte(SmileConstants.TOKEN_LITERAL_EMPTY_STRING);
                return;
            } else if (len <= 64) {
                byte typeToken;
                if (this._outputTail + Wbxml.LITERAL_AC >= this._outputEnd) {
                    _flushBuffer();
                }
                origOffset = this._outputTail;
                this._outputTail++;
                int byteLen = _shortUTF8Encode(text, offset, offset + len);
                if (byteLen > 64) {
                    typeToken = TOKEN_BYTE_LONG_STRING_UNICODE;
                    r4 = this._outputBuffer;
                    r5 = this._outputTail;
                    this._outputTail = r5 + 1;
                    r4[r5] = (byte) -4;
                } else if (byteLen == len) {
                    typeToken = (byte) (byteLen + 63);
                } else {
                    typeToken = (byte) (byteLen + Opcodes.IAND);
                }
                this._outputBuffer[origOffset] = typeToken;
                return;
            } else {
                int maxLen = ((len + len) + len) + 2;
                if (maxLen <= this._outputBuffer.length) {
                    if (this._outputTail + maxLen >= this._outputEnd) {
                        _flushBuffer();
                    }
                    origOffset = this._outputTail;
                    _writeByte(TOKEN_BYTE_LONG_STRING_UNICODE);
                    if (_shortUTF8Encode(text, offset, offset + len) == len) {
                        this._outputBuffer[origOffset] = TOKEN_BYTE_LONG_STRING_ASCII;
                    }
                    r4 = this._outputBuffer;
                    r5 = this._outputTail;
                    this._outputTail = r5 + 1;
                    r4[r5] = (byte) -4;
                    return;
                }
                _writeByte(TOKEN_BYTE_LONG_STRING_UNICODE);
                _mediumUTF8Encode(text, offset, offset + len);
                _writeByte((byte) -4);
                return;
            }
        }
        writeString(new String(text, offset, len));
    }

    public final void writeString(SerializableString sstr) throws IOException, JsonGenerationException {
        _verifyValueWrite("write String value");
        String str = sstr.getValue();
        int len = str.length();
        if (len == 0) {
            _writeByte(SmileConstants.TOKEN_LITERAL_EMPTY_STRING);
            return;
        }
        if (len <= 65 && this._seenStringValueCount >= 0) {
            int ix = _findSeenStringValue(str);
            if (ix >= 0) {
                _writeSharedStringValueReference(ix);
                return;
            }
        }
        byte[] raw = sstr.asUnquotedUTF8();
        int byteLen = raw.length;
        if (byteLen <= 64) {
            if ((this._outputTail + byteLen) + 1 >= this._outputEnd) {
                _flushBuffer();
            }
            int typeToken = byteLen == len ? byteLen + 63 : byteLen + Opcodes.IAND;
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = (byte) typeToken;
            System.arraycopy(raw, 0, this._outputBuffer, this._outputTail, byteLen);
            this._outputTail += byteLen;
            if (this._seenStringValueCount >= 0) {
                _addSeenStringValue(sstr.getValue());
                return;
            }
            return;
        }
        _writeByte(byteLen == len ? TOKEN_BYTE_LONG_STRING_ASCII : TOKEN_BYTE_LONG_STRING_UNICODE);
        _writeBytes(raw, 0, raw.length);
        _writeByte((byte) -4);
    }

    public void writeRawUTF8String(byte[] text, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write String value");
        if (len == 0) {
            _writeByte(SmileConstants.TOKEN_LITERAL_EMPTY_STRING);
        } else if (this._seenStringValueCount >= 0) {
            throw new UnsupportedOperationException("Can not use direct UTF-8 write methods when 'Feature.CHECK_SHARED_STRING_VALUES' enabled");
        } else if (len <= 65) {
            if (this._outputTail + len >= this._outputEnd) {
                _flushBuffer();
            }
            if (len == 1) {
                r1 = this._outputBuffer;
                r2 = this._outputTail;
                this._outputTail = r2 + 1;
                r1[r2] = (byte) 64;
                r1 = this._outputBuffer;
                r2 = this._outputTail;
                this._outputTail = r2 + 1;
                r1[r2] = text[offset];
                return;
            }
            r1 = this._outputBuffer;
            r2 = this._outputTail;
            this._outputTail = r2 + 1;
            r1[r2] = (byte) (len + Opcodes.IAND);
            System.arraycopy(text, offset, this._outputBuffer, this._outputTail, len);
            this._outputTail += len;
        } else {
            int maxLen = ((len + len) + len) + 2;
            if (maxLen <= this._outputBuffer.length) {
                if (this._outputTail + maxLen >= this._outputEnd) {
                    _flushBuffer();
                }
                r1 = this._outputBuffer;
                r2 = this._outputTail;
                this._outputTail = r2 + 1;
                r1[r2] = TOKEN_BYTE_LONG_STRING_UNICODE;
                System.arraycopy(text, offset, this._outputBuffer, this._outputTail, len);
                this._outputTail += len;
                r1 = this._outputBuffer;
                r2 = this._outputTail;
                this._outputTail = r2 + 1;
                r1[r2] = (byte) -4;
                return;
            }
            _writeByte(TOKEN_BYTE_LONG_STRING_UNICODE);
            _writeBytes(text, offset, len);
            _writeByte((byte) -4);
        }
    }

    public final void writeUTF8String(byte[] text, int offset, int len) throws IOException, JsonGenerationException {
        writeRawUTF8String(text, offset, len);
    }

    public void writeRaw(String text) throws IOException, JsonGenerationException {
        throw _notSupported();
    }

    public void writeRaw(String text, int offset, int len) throws IOException, JsonGenerationException {
        throw _notSupported();
    }

    public void writeRaw(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        throw _notSupported();
    }

    public void writeRaw(char c) throws IOException, JsonGenerationException {
        throw _notSupported();
    }

    public void writeRawValue(String text) throws IOException, JsonGenerationException {
        throw _notSupported();
    }

    public void writeRawValue(String text, int offset, int len) throws IOException, JsonGenerationException {
        throw _notSupported();
    }

    public void writeRawValue(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        throw _notSupported();
    }

    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException, JsonGenerationException {
        if (data == null) {
            writeNull();
            return;
        }
        _verifyValueWrite("write Binary value");
        if (isEnabled(Feature.ENCODE_BINARY_AS_7BIT)) {
            _writeByte((byte) -24);
            _write7BitBinaryWithLength(data, offset, len);
            return;
        }
        _writeByte((byte) -3);
        _writePositiveVInt(len);
        _writeBytes(data, offset, len);
    }

    public void writeBoolean(boolean state) throws IOException, JsonGenerationException {
        _verifyValueWrite("write boolean value");
        if (state) {
            _writeByte(SmileConstants.TOKEN_LITERAL_TRUE);
        } else {
            _writeByte(SmileConstants.TOKEN_LITERAL_FALSE);
        }
    }

    public void writeNull() throws IOException, JsonGenerationException {
        _verifyValueWrite("write null value");
        _writeByte(SmileConstants.TOKEN_LITERAL_NULL);
    }

    public void writeNumber(int i) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        i = SmileUtil.zigzagEncode(i);
        if (i > 63 || i < 0) {
            byte b0 = (byte) ((i & 63) + Flags.FLAG8);
            i >>>= 6;
            if (i <= Service.LOCUS_CON) {
                _writeBytes((byte) TOKEN_BYTE_INT_32, (byte) i, b0);
                return;
            }
            byte b1 = (byte) (i & Service.LOCUS_CON);
            i >>= 7;
            if (i <= Service.LOCUS_CON) {
                _writeBytes(TOKEN_BYTE_INT_32, (byte) i, b1, b0);
                return;
            }
            byte b2 = (byte) (i & Service.LOCUS_CON);
            i >>= 7;
            if (i <= Service.LOCUS_CON) {
                _writeBytes(TOKEN_BYTE_INT_32, (byte) i, b2, b1, b0);
                return;
            }
            byte b = TOKEN_BYTE_INT_32;
            _writeBytes(b, (byte) (i >> 7), (byte) (i & Service.LOCUS_CON), b2, b1, b0);
        } else if (i <= 31) {
            _writeByte((byte) (i + Wbxml.EXT_0));
        } else {
            _writeBytes(TOKEN_BYTE_INT_32, (byte) (i + Flags.FLAG8));
        }
    }

    public void writeNumber(long l) throws IOException, JsonGenerationException {
        if (l > MAX_INT_AS_LONG || l < MIN_INT_AS_LONG) {
            _verifyValueWrite("write number");
            l = SmileUtil.zigzagEncode(l);
            int i = (int) l;
            byte b0 = (byte) ((i & 63) + Flags.FLAG8);
            byte b1 = (byte) ((i >> 6) & Service.LOCUS_CON);
            byte b2 = (byte) ((i >> 13) & Service.LOCUS_CON);
            byte b3 = (byte) ((i >> 20) & Service.LOCUS_CON);
            l >>>= 27;
            byte b4 = (byte) (((int) l) & Service.LOCUS_CON);
            i = (int) (l >> 7);
            if (i == 0) {
                _writeBytes(TOKEN_BYTE_INT_64, b4, b3, b2, b1, b0);
                return;
            } else if (i <= Service.LOCUS_CON) {
                _writeBytes(TOKEN_BYTE_INT_64, (byte) i);
                _writeBytes(b4, b3, b2, b1, b0);
                return;
            } else {
                byte b5 = (byte) (i & Service.LOCUS_CON);
                i >>= 7;
                if (i <= Service.LOCUS_CON) {
                    _writeBytes(TOKEN_BYTE_INT_64, (byte) i);
                    _writeBytes(b5, b4, b3, b2, b1, b0);
                    return;
                }
                byte b6 = (byte) (i & Service.LOCUS_CON);
                i >>= 7;
                if (i <= Service.LOCUS_CON) {
                    _writeBytes((byte) TOKEN_BYTE_INT_64, (byte) i, b6);
                    _writeBytes(b5, b4, b3, b2, b1, b0);
                    return;
                }
                byte b7 = (byte) (i & Service.LOCUS_CON);
                i >>= 7;
                if (i <= Service.LOCUS_CON) {
                    _writeBytes(TOKEN_BYTE_INT_64, (byte) i, b7, b6);
                    _writeBytes(b5, b4, b3, b2, b1, b0);
                    return;
                }
                _writeBytes(TOKEN_BYTE_INT_64, (byte) (i >> 7), (byte) (i & Service.LOCUS_CON), b7, b6);
                _writeBytes(b5, b4, b3, b2, b1, b0);
                return;
            }
        }
        writeNumber((int) l);
    }

    public void writeNumber(BigInteger v) throws IOException, JsonGenerationException {
        if (v == null) {
            writeNull();
            return;
        }
        _verifyValueWrite("write number");
        _writeByte(TOKEN_BYTE_BIG_INTEGER);
        byte[] data = v.toByteArray();
        _write7BitBinaryWithLength(data, 0, data.length);
    }

    public void writeNumber(double d) throws IOException, JsonGenerationException {
        _ensureRoomForOutput(11);
        _verifyValueWrite("write number");
        long l = Double.doubleToRawLongBits(d);
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = TOKEN_BYTE_FLOAT_64;
        int hi5 = (int) (l >>> 35);
        this._outputBuffer[this._outputTail + 4] = (byte) (hi5 & Service.LOCUS_CON);
        hi5 >>= 7;
        this._outputBuffer[this._outputTail + 3] = (byte) (hi5 & Service.LOCUS_CON);
        hi5 >>= 7;
        this._outputBuffer[this._outputTail + 2] = (byte) (hi5 & Service.LOCUS_CON);
        hi5 >>= 7;
        this._outputBuffer[this._outputTail + 1] = (byte) (hi5 & Service.LOCUS_CON);
        this._outputBuffer[this._outputTail] = (byte) (hi5 >> 7);
        this._outputTail += 5;
        int mid = (int) (l >> 28);
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = (byte) (mid & Service.LOCUS_CON);
        int lo4 = (int) l;
        this._outputBuffer[this._outputTail + 3] = (byte) (lo4 & Service.LOCUS_CON);
        lo4 >>= 7;
        this._outputBuffer[this._outputTail + 2] = (byte) (lo4 & Service.LOCUS_CON);
        lo4 >>= 7;
        this._outputBuffer[this._outputTail + 1] = (byte) (lo4 & Service.LOCUS_CON);
        this._outputBuffer[this._outputTail] = (byte) ((lo4 >> 7) & Service.LOCUS_CON);
        this._outputTail += 4;
    }

    public void writeNumber(float f) throws IOException, JsonGenerationException {
        _ensureRoomForOutput(6);
        _verifyValueWrite("write number");
        int i = Float.floatToRawIntBits(f);
        byte[] bArr = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = TOKEN_BYTE_FLOAT_32;
        this._outputBuffer[this._outputTail + 4] = (byte) (i & Service.LOCUS_CON);
        i >>= 7;
        this._outputBuffer[this._outputTail + 3] = (byte) (i & Service.LOCUS_CON);
        i >>= 7;
        this._outputBuffer[this._outputTail + 2] = (byte) (i & Service.LOCUS_CON);
        i >>= 7;
        this._outputBuffer[this._outputTail + 1] = (byte) (i & Service.LOCUS_CON);
        this._outputBuffer[this._outputTail] = (byte) ((i >> 7) & Service.LOCUS_CON);
        this._outputTail += 5;
    }

    public void writeNumber(BigDecimal dec) throws IOException, JsonGenerationException {
        if (dec == null) {
            writeNull();
            return;
        }
        _verifyValueWrite("write number");
        _writeByte(TOKEN_BYTE_BIG_DECIMAL);
        _writeSignedVInt(dec.scale());
        byte[] data = dec.unscaledValue().toByteArray();
        _write7BitBinaryWithLength(data, 0, data.length);
    }

    public void writeNumber(String encodedValue) throws IOException, JsonGenerationException, UnsupportedOperationException {
        throw _notSupported();
    }

    protected final void _verifyValueWrite(String typeMsg) throws IOException, JsonGenerationException {
        if (this._writeContext.writeValue() == 5) {
            _reportError("Can not " + typeMsg + ", expecting field name");
        }
    }

    public final void flush() throws IOException {
        _flushBuffer();
        if (isEnabled(org.codehaus.jackson.JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM)) {
            this._out.flush();
        }
    }

    public void close() throws IOException {
        boolean wasClosed = this._closed;
        super.close();
        if (this._outputBuffer != null && isEnabled(org.codehaus.jackson.JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT)) {
            while (true) {
                JsonStreamContext ctxt = getOutputContext();
                if (!ctxt.inArray()) {
                    if (!ctxt.inObject()) {
                        break;
                    }
                    writeEndObject();
                } else {
                    writeEndArray();
                }
            }
        }
        if (!wasClosed && isEnabled(Feature.WRITE_END_MARKER)) {
            _writeByte((byte) -1);
        }
        _flushBuffer();
        if (this._ioContext.isResourceManaged() || isEnabled(org.codehaus.jackson.JsonGenerator.Feature.AUTO_CLOSE_TARGET)) {
            this._out.close();
        } else {
            this._out.flush();
        }
        _releaseBuffers();
    }

    private final int _shortUTF8Encode(char[] str, int i, int end) {
        int ptr = this._outputTail;
        byte[] outBuf = this._outputBuffer;
        while (true) {
            int c = str[i];
            if (c > Service.LOCUS_CON) {
                return _shortUTF8Encode2(str, i, end, ptr);
            }
            int ptr2 = ptr + 1;
            outBuf[ptr] = (byte) c;
            i++;
            if (i >= end) {
                int codedLen = ptr2 - this._outputTail;
                this._outputTail = ptr2;
                ptr = ptr2;
                return codedLen;
            }
            ptr = ptr2;
        }
    }

    private final int _shortUTF8Encode2(char[] str, int i, int end, int outputPtr) {
        byte[] outBuf = this._outputBuffer;
        int outputPtr2 = outputPtr;
        int i2 = i;
        while (i2 < end) {
            i = i2 + 1;
            int c = str[i2];
            if (c <= Service.LOCUS_CON) {
                outputPtr = outputPtr2 + 1;
                outBuf[outputPtr2] = (byte) c;
                outputPtr2 = outputPtr;
                i2 = i;
            } else if (c < Flags.FLAG4) {
                outputPtr = outputPtr2 + 1;
                outBuf[outputPtr2] = (byte) ((c >> 6) | Wbxml.EXT_0);
                outputPtr2 = outputPtr + 1;
                outBuf[outputPtr] = (byte) ((c & 63) | Flags.FLAG8);
                i2 = i;
            } else if (c < SURR1_FIRST || c > SURR2_LAST) {
                outputPtr = outputPtr2 + 1;
                outBuf[outputPtr2] = (byte) ((c >> 12) | SmileConstants.TOKEN_PREFIX_MISC_OTHER);
                outputPtr2 = outputPtr + 1;
                outBuf[outputPtr] = (byte) (((c >> 6) & 63) | Flags.FLAG8);
                outputPtr = outputPtr2 + 1;
                outBuf[outputPtr2] = (byte) ((c & 63) | Flags.FLAG8);
                outputPtr2 = outputPtr;
                i2 = i;
            } else {
                if (c > SURR1_LAST) {
                    _throwIllegalSurrogate(c);
                }
                if (i >= end) {
                    _throwIllegalSurrogate(c);
                }
                i2 = i + 1;
                c = _convertSurrogate(c, str[i]);
                if (c > 1114111) {
                    _throwIllegalSurrogate(c);
                }
                outputPtr = outputPtr2 + 1;
                outBuf[outputPtr2] = (byte) ((c >> 18) | 240);
                outputPtr2 = outputPtr + 1;
                outBuf[outputPtr] = (byte) (((c >> 12) & 63) | Flags.FLAG8);
                outputPtr = outputPtr2 + 1;
                outBuf[outputPtr2] = (byte) (((c >> 6) & 63) | Flags.FLAG8);
                outputPtr2 = outputPtr + 1;
                outBuf[outputPtr] = (byte) ((c & 63) | Flags.FLAG8);
            }
        }
        int codedLen = outputPtr2 - this._outputTail;
        this._outputTail = outputPtr2;
        return codedLen;
    }

    private void _slowUTF8Encode(String str) throws IOException {
        int len = str.length();
        int bufferEnd = this._outputEnd - 4;
        int inputPtr = 0;
        while (inputPtr < len) {
            byte[] bArr;
            int i;
            if (this._outputTail >= bufferEnd) {
                _flushBuffer();
            }
            int inputPtr2 = inputPtr + 1;
            int c = str.charAt(inputPtr);
            if (c <= Service.LOCUS_CON) {
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) c;
                int maxInCount = len - inputPtr2;
                int maxOutCount = bufferEnd - this._outputTail;
                if (maxInCount > maxOutCount) {
                    maxInCount = maxOutCount;
                }
                maxInCount += inputPtr2;
                inputPtr = inputPtr2;
                while (inputPtr < maxInCount) {
                    inputPtr2 = inputPtr + 1;
                    c = str.charAt(inputPtr);
                    if (c > Service.LOCUS_CON) {
                        inputPtr = inputPtr2;
                    } else {
                        bArr = this._outputBuffer;
                        i = this._outputTail;
                        this._outputTail = i + 1;
                        bArr[i] = (byte) c;
                        inputPtr = inputPtr2;
                    }
                }
            } else {
                inputPtr = inputPtr2;
            }
            if (c < Flags.FLAG4) {
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((c >> 6) | Wbxml.EXT_0);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((c & 63) | Flags.FLAG8);
                inputPtr2 = inputPtr;
            } else if (c < SURR1_FIRST || c > SURR2_LAST) {
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((c >> 12) | SmileConstants.TOKEN_PREFIX_MISC_OTHER);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) (((c >> 6) & 63) | Flags.FLAG8);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((c & 63) | Flags.FLAG8);
            } else {
                if (c > SURR1_LAST) {
                    _throwIllegalSurrogate(c);
                }
                if (inputPtr >= len) {
                    _throwIllegalSurrogate(c);
                }
                inputPtr2 = inputPtr + 1;
                c = _convertSurrogate(c, str.charAt(inputPtr));
                if (c > 1114111) {
                    _throwIllegalSurrogate(c);
                }
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((c >> 18) | 240);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) (((c >> 12) & 63) | Flags.FLAG8);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) (((c >> 6) & 63) | Flags.FLAG8);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((c & 63) | Flags.FLAG8);
            }
            inputPtr = inputPtr2;
        }
    }

    private void _mediumUTF8Encode(char[] str, int inputPtr, int inputEnd) throws IOException {
        int bufferEnd = this._outputEnd - 4;
        int inputPtr2 = inputPtr;
        while (inputPtr2 < inputEnd) {
            byte[] bArr;
            int i;
            if (this._outputTail >= bufferEnd) {
                _flushBuffer();
            }
            inputPtr = inputPtr2 + 1;
            int c = str[inputPtr2];
            if (c <= Service.LOCUS_CON) {
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) c;
                int maxInCount = inputEnd - inputPtr;
                int maxOutCount = bufferEnd - this._outputTail;
                if (maxInCount > maxOutCount) {
                    maxInCount = maxOutCount;
                }
                maxInCount += inputPtr;
                inputPtr2 = inputPtr;
                while (inputPtr2 < maxInCount) {
                    inputPtr = inputPtr2 + 1;
                    c = str[inputPtr2];
                    if (c > Service.LOCUS_CON) {
                        inputPtr2 = inputPtr;
                    } else {
                        bArr = this._outputBuffer;
                        i = this._outputTail;
                        this._outputTail = i + 1;
                        bArr[i] = (byte) c;
                        inputPtr2 = inputPtr;
                    }
                }
            } else {
                inputPtr2 = inputPtr;
            }
            if (c < Flags.FLAG4) {
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((c >> 6) | Wbxml.EXT_0);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((c & 63) | Flags.FLAG8);
                inputPtr = inputPtr2;
            } else if (c < SURR1_FIRST || c > SURR2_LAST) {
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((c >> 12) | SmileConstants.TOKEN_PREFIX_MISC_OTHER);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) (((c >> 6) & 63) | Flags.FLAG8);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((c & 63) | Flags.FLAG8);
            } else {
                if (c > SURR1_LAST) {
                    _throwIllegalSurrogate(c);
                }
                if (inputPtr2 >= inputEnd) {
                    _throwIllegalSurrogate(c);
                }
                inputPtr = inputPtr2 + 1;
                c = _convertSurrogate(c, str[inputPtr2]);
                if (c > 1114111) {
                    _throwIllegalSurrogate(c);
                }
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((c >> 18) | 240);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) (((c >> 12) & 63) | Flags.FLAG8);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) (((c >> 6) & 63) | Flags.FLAG8);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((c & 63) | Flags.FLAG8);
            }
            inputPtr2 = inputPtr;
        }
    }

    private int _convertSurrogate(int firstPart, int secondPart) {
        if (secondPart >= SURR2_FIRST && secondPart <= SURR2_LAST) {
            return (AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED + ((firstPart - SURR1_FIRST) << 10)) + (secondPart - SURR2_FIRST);
        }
        throw new IllegalArgumentException("Broken surrogate pair: first char 0x" + Integer.toHexString(firstPart) + ", second 0x" + Integer.toHexString(secondPart) + "; illegal combination");
    }

    private void _throwIllegalSurrogate(int code) {
        if (code > 1114111) {
            throw new IllegalArgumentException("Illegal character point (0x" + Integer.toHexString(code) + ") to output; max is 0x10FFFF as per RFC 4627");
        } else if (code < SURR1_FIRST) {
            throw new IllegalArgumentException("Illegal character point (0x" + Integer.toHexString(code) + ") to output");
        } else if (code <= SURR1_LAST) {
            throw new IllegalArgumentException("Unmatched first part of surrogate pair (0x" + Integer.toHexString(code) + ")");
        } else {
            throw new IllegalArgumentException("Unmatched second part of surrogate pair (0x" + Integer.toHexString(code) + ")");
        }
    }

    private final void _ensureRoomForOutput(int needed) throws IOException {
        if (this._outputTail + needed >= this._outputEnd) {
            _flushBuffer();
        }
    }

    private final void _writeByte(byte b) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b;
    }

    private final void _writeBytes(byte b1, byte b2) throws IOException {
        if (this._outputTail + 1 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b1;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b2;
    }

    private final void _writeBytes(byte b1, byte b2, byte b3) throws IOException {
        if (this._outputTail + 2 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b1;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b2;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b3;
    }

    private final void _writeBytes(byte b1, byte b2, byte b3, byte b4) throws IOException {
        if (this._outputTail + 3 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b1;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b2;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b3;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b4;
    }

    private final void _writeBytes(byte b1, byte b2, byte b3, byte b4, byte b5) throws IOException {
        if (this._outputTail + 4 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b1;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b2;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b3;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b4;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b5;
    }

    private final void _writeBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6) throws IOException {
        if (this._outputTail + 5 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b1;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b2;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b3;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b4;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b5;
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = b6;
    }

    private final void _writeBytes(byte[] data, int offset, int len) throws IOException {
        if (len != 0) {
            if (this._outputTail + len >= this._outputEnd) {
                _writeBytesLong(data, offset, len);
                return;
            }
            System.arraycopy(data, offset, this._outputBuffer, this._outputTail, len);
            this._outputTail += len;
        }
    }

    private final void _writeBytesLong(byte[] data, int offset, int len) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        while (true) {
            int currLen = Math.min(len, this._outputEnd - this._outputTail);
            System.arraycopy(data, offset, this._outputBuffer, this._outputTail, currLen);
            this._outputTail += currLen;
            len -= currLen;
            if (len != 0) {
                offset += currLen;
                _flushBuffer();
            } else {
                return;
            }
        }
    }

    private void _writePositiveVInt(int i) throws IOException {
        _ensureRoomForOutput(5);
        byte b0 = (byte) ((i & 63) + Flags.FLAG8);
        i >>= 6;
        byte[] bArr;
        int i2;
        if (i <= Service.LOCUS_CON) {
            if (i > 0) {
                bArr = this._outputBuffer;
                i2 = this._outputTail;
                this._outputTail = i2 + 1;
                bArr[i2] = (byte) i;
            }
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = b0;
            return;
        }
        byte b1 = (byte) (i & Service.LOCUS_CON);
        i >>= 7;
        if (i <= Service.LOCUS_CON) {
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) i;
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = b1;
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = b0;
            return;
        }
        byte b2 = (byte) (i & Service.LOCUS_CON);
        i >>= 7;
        if (i <= Service.LOCUS_CON) {
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) i;
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = b2;
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = b1;
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = b0;
            return;
        }
        byte b3 = (byte) (i & Service.LOCUS_CON);
        bArr = this._outputBuffer;
        i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = (byte) (i >> 7);
        bArr = this._outputBuffer;
        i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = b3;
        bArr = this._outputBuffer;
        i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = b2;
        bArr = this._outputBuffer;
        i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = b1;
        bArr = this._outputBuffer;
        i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = b0;
    }

    private void _writeSignedVInt(int input) throws IOException {
        _writePositiveVInt(SmileUtil.zigzagEncode(input));
    }

    protected void _write7BitBinaryWithLength(byte[] data, int offset, int len) throws IOException {
        _writePositiveVInt(len);
        int offset2 = offset;
        while (len >= 7) {
            if (this._outputTail + 8 >= this._outputEnd) {
                _flushBuffer();
            }
            offset = offset2 + 1;
            int i = data[offset2];
            byte[] bArr = this._outputBuffer;
            int i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) ((i >> 1) & Service.LOCUS_CON);
            offset2 = offset + 1;
            i = (i << 8) | (data[offset] & KEYRecord.PROTOCOL_ANY);
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) ((i >> 2) & Service.LOCUS_CON);
            offset = offset2 + 1;
            i = (i << 8) | (data[offset2] & KEYRecord.PROTOCOL_ANY);
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) ((i >> 3) & Service.LOCUS_CON);
            offset2 = offset + 1;
            i = (i << 8) | (data[offset] & KEYRecord.PROTOCOL_ANY);
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) ((i >> 4) & Service.LOCUS_CON);
            offset = offset2 + 1;
            i = (i << 8) | (data[offset2] & KEYRecord.PROTOCOL_ANY);
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) ((i >> 5) & Service.LOCUS_CON);
            offset2 = offset + 1;
            i = (i << 8) | (data[offset] & KEYRecord.PROTOCOL_ANY);
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) ((i >> 6) & Service.LOCUS_CON);
            offset = offset2 + 1;
            i = (i << 8) | (data[offset2] & KEYRecord.PROTOCOL_ANY);
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) ((i >> 7) & Service.LOCUS_CON);
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) (i & Service.LOCUS_CON);
            len -= 7;
            offset2 = offset;
        }
        if (len > 0) {
            if (this._outputTail + 7 >= this._outputEnd) {
                _flushBuffer();
            }
            offset = offset2 + 1;
            i = data[offset2];
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) ((i >> 1) & Service.LOCUS_CON);
            if (len > 1) {
                offset2 = offset + 1;
                i = ((i & 1) << 8) | (data[offset] & KEYRecord.PROTOCOL_ANY);
                bArr = this._outputBuffer;
                i2 = this._outputTail;
                this._outputTail = i2 + 1;
                bArr[i2] = (byte) ((i >> 2) & Service.LOCUS_CON);
                if (len > 2) {
                    offset = offset2 + 1;
                    i = ((i & 3) << 8) | (data[offset2] & KEYRecord.PROTOCOL_ANY);
                    bArr = this._outputBuffer;
                    i2 = this._outputTail;
                    this._outputTail = i2 + 1;
                    bArr[i2] = (byte) ((i >> 3) & Service.LOCUS_CON);
                    if (len > 3) {
                        offset2 = offset + 1;
                        i = ((i & 7) << 8) | (data[offset] & KEYRecord.PROTOCOL_ANY);
                        bArr = this._outputBuffer;
                        i2 = this._outputTail;
                        this._outputTail = i2 + 1;
                        bArr[i2] = (byte) ((i >> 4) & Service.LOCUS_CON);
                        if (len > 4) {
                            offset = offset2 + 1;
                            i = ((i & 15) << 8) | (data[offset2] & KEYRecord.PROTOCOL_ANY);
                            bArr = this._outputBuffer;
                            i2 = this._outputTail;
                            this._outputTail = i2 + 1;
                            bArr[i2] = (byte) ((i >> 5) & Service.LOCUS_CON);
                            if (len > 5) {
                                offset2 = offset + 1;
                                i = ((i & 31) << 8) | (data[offset] & KEYRecord.PROTOCOL_ANY);
                                bArr = this._outputBuffer;
                                i2 = this._outputTail;
                                this._outputTail = i2 + 1;
                                bArr[i2] = (byte) ((i >> 6) & Service.LOCUS_CON);
                                bArr = this._outputBuffer;
                                i2 = this._outputTail;
                                this._outputTail = i2 + 1;
                                bArr[i2] = (byte) (i & 63);
                                offset = offset2;
                                return;
                            }
                            bArr = this._outputBuffer;
                            i2 = this._outputTail;
                            this._outputTail = i2 + 1;
                            bArr[i2] = (byte) (i & 31);
                            return;
                        }
                        bArr = this._outputBuffer;
                        i2 = this._outputTail;
                        this._outputTail = i2 + 1;
                        bArr[i2] = (byte) (i & 15);
                        offset = offset2;
                        return;
                    }
                    bArr = this._outputBuffer;
                    i2 = this._outputTail;
                    this._outputTail = i2 + 1;
                    bArr[i2] = (byte) (i & 7);
                    return;
                }
                bArr = this._outputBuffer;
                i2 = this._outputTail;
                this._outputTail = i2 + 1;
                bArr[i2] = (byte) (i & 3);
                offset = offset2;
                return;
            }
            bArr = this._outputBuffer;
            i2 = this._outputTail;
            this._outputTail = i2 + 1;
            bArr[i2] = (byte) (i & 1);
            return;
        }
    }

    protected void _releaseBuffers() {
        byte[] buf = this._outputBuffer;
        if (buf != null && this._bufferRecyclable) {
            this._outputBuffer = null;
            this._ioContext.releaseWriteEncodingBuffer(buf);
        }
        char[] cbuf = this._charBuffer;
        if (cbuf != null) {
            this._charBuffer = null;
            this._ioContext.releaseConcatBuffer(cbuf);
        }
        SharedStringNode[] nameBuf = this._seenNames;
        if (nameBuf != null && nameBuf.length == 64) {
            this._seenNames = null;
            this._smileBufferRecycler.releaseSeenNamesBuffer(nameBuf);
        }
        SharedStringNode[] valueBuf = this._seenStringValues;
        if (valueBuf != null && valueBuf.length == 64) {
            this._seenStringValues = null;
            this._smileBufferRecycler.releaseSeenStringValuesBuffer(valueBuf);
        }
    }

    protected final void _flushBuffer() throws IOException {
        if (this._outputTail > 0) {
            this._bytesWritten += this._outputTail;
            this._out.write(this._outputBuffer, 0, this._outputTail);
            this._outputTail = 0;
        }
    }

    private final int _findSeenName(String name) {
        int hash = name.hashCode();
        SharedStringNode head = this._seenNames[(this._seenNames.length - 1) & hash];
        if (head == null) {
            return -1;
        }
        SharedStringNode node = head;
        if (node.value == name) {
            return node.index;
        }
        do {
            node = node.next;
            if (node == null) {
                node = head;
                do {
                    String value = node.value;
                    if (value.hashCode() == hash && value.equals(name)) {
                        return node.index;
                    }
                    node = node.next;
                } while (node != null);
                return -1;
            }
        } while (node.value != name);
        return node.index;
    }

    private final void _addSeenName(String name) {
        int ix;
        if (this._seenNameCount == this._seenNames.length) {
            if (this._seenNameCount == Flags.FLAG5) {
                Arrays.fill(this._seenNames, null);
                this._seenNameCount = 0;
            } else {
                SharedStringNode[] old = this._seenNames;
                this._seenNames = new SharedStringNode[Flags.FLAG5];
                for (SharedStringNode node : old) {
                    for (SharedStringNode node2 = arr$[i$]; node2 != null; node2 = node2.next) {
                        ix = node2.value.hashCode() & 1023;
                        node2.next = this._seenNames[ix];
                        this._seenNames[ix] = node2;
                    }
                }
            }
        }
        ix = name.hashCode() & (this._seenNames.length - 1);
        this._seenNames[ix] = new SharedStringNode(name, this._seenNameCount, this._seenNames[ix]);
        this._seenNameCount++;
    }

    private final int _findSeenStringValue(String text) {
        int hash = text.hashCode();
        SharedStringNode head = this._seenStringValues[(this._seenStringValues.length - 1) & hash];
        if (head != null) {
            SharedStringNode node = head;
            while (node.value != text) {
                node = node.next;
                if (node == null) {
                    node = head;
                    do {
                        String value = node.value;
                        if (value.hashCode() == hash && value.equals(text)) {
                            return node.index;
                        }
                        node = node.next;
                    } while (node != null);
                }
            }
            return node.index;
        }
        return -1;
    }

    private final void _addSeenStringValue(String text) {
        int ix;
        if (this._seenStringValueCount == this._seenStringValues.length) {
            if (this._seenStringValueCount == Flags.FLAG5) {
                Arrays.fill(this._seenStringValues, null);
                this._seenStringValueCount = 0;
            } else {
                SharedStringNode[] old = this._seenStringValues;
                this._seenStringValues = new SharedStringNode[Flags.FLAG5];
                for (SharedStringNode node : old) {
                    for (SharedStringNode node2 = arr$[i$]; node2 != null; node2 = node2.next) {
                        ix = node2.value.hashCode() & 1023;
                        node2.next = this._seenStringValues[ix];
                        this._seenStringValues[ix] = node2;
                    }
                }
            }
        }
        ix = text.hashCode() & (this._seenStringValues.length - 1);
        this._seenStringValues[ix] = new SharedStringNode(text, this._seenStringValueCount, this._seenStringValues[ix]);
        this._seenStringValueCount++;
    }

    protected long outputOffset() {
        return (long) (this._bytesWritten + this._outputTail);
    }

    protected UnsupportedOperationException _notSupported() {
        return new UnsupportedOperationException();
    }
}
