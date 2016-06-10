package org.codehaus.jackson.impl;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.NumberOutput;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.smile.SmileConstants;
import org.codehaus.jackson.util.CharTypes;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public class Utf8Generator extends JsonGeneratorBase {
    private static final byte BYTE_0 = (byte) 48;
    private static final byte BYTE_BACKSLASH = (byte) 92;
    private static final byte BYTE_COLON = (byte) 58;
    private static final byte BYTE_COMMA = (byte) 44;
    private static final byte BYTE_LBRACKET = (byte) 91;
    private static final byte BYTE_LCURLY = (byte) 123;
    private static final byte BYTE_QUOTE = (byte) 34;
    private static final byte BYTE_RBRACKET = (byte) 93;
    private static final byte BYTE_RCURLY = (byte) 125;
    private static final byte BYTE_SPACE = (byte) 32;
    private static final byte BYTE_u = (byte) 117;
    private static final byte[] FALSE_BYTES;
    static final byte[] HEX_CHARS;
    private static final int MAX_BYTES_TO_BUFFER = 512;
    private static final byte[] NULL_BYTES;
    protected static final int SURR1_FIRST = 55296;
    protected static final int SURR1_LAST = 56319;
    protected static final int SURR2_FIRST = 56320;
    protected static final int SURR2_LAST = 57343;
    private static final byte[] TRUE_BYTES;
    private static final int[] sOutputEscapes;
    protected boolean _bufferRecyclable;
    protected char[] _charBuffer;
    protected final int _charBufferLength;
    protected byte[] _entityBuffer;
    protected final IOContext _ioContext;
    protected byte[] _outputBuffer;
    protected final int _outputEnd;
    protected final int _outputMaxContiguous;
    protected final OutputStream _outputStream;
    protected int _outputTail;

    static {
        HEX_CHARS = CharTypes.copyHexBytes();
        NULL_BYTES = new byte[]{(byte) 110, BYTE_u, (byte) 108, (byte) 108};
        TRUE_BYTES = new byte[]{(byte) 116, (byte) 114, BYTE_u, (byte) 101};
        FALSE_BYTES = new byte[]{(byte) 102, (byte) 97, (byte) 108, (byte) 115, (byte) 101};
        sOutputEscapes = CharTypes.getOutputEscapes();
    }

    public Utf8Generator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out) {
        super(features, codec);
        this._outputTail = 0;
        this._ioContext = ctxt;
        this._outputStream = out;
        this._bufferRecyclable = true;
        this._outputBuffer = ctxt.allocWriteEncodingBuffer();
        this._outputEnd = this._outputBuffer.length;
        this._outputMaxContiguous = this._outputEnd >> 3;
        this._charBuffer = ctxt.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
    }

    public Utf8Generator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out, byte[] outputBuffer, int outputOffset, boolean bufferRecyclable) {
        super(features, codec);
        this._outputTail = 0;
        this._ioContext = ctxt;
        this._outputStream = out;
        this._bufferRecyclable = bufferRecyclable;
        this._outputTail = outputOffset;
        this._outputBuffer = outputBuffer;
        this._outputEnd = this._outputBuffer.length;
        this._outputMaxContiguous = this._outputEnd >> 3;
        this._charBuffer = ctxt.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
    }

    public final void writeStringField(String fieldName, String value) throws IOException, JsonGenerationException {
        writeFieldName(fieldName);
        writeString(value);
    }

    public final void writeFieldName(String name) throws IOException, JsonGenerationException {
        boolean z = true;
        int status = this._writeContext.writeFieldName(name);
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (this._cfgPrettyPrinter != null) {
            if (status != 1) {
                z = false;
            }
            _writePPFieldName(name, z);
            return;
        }
        if (status == 1) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_COMMA;
        }
        _writeFieldName(name);
    }

    public final void writeFieldName(SerializedString name) throws IOException, JsonGenerationException {
        boolean z = true;
        int status = this._writeContext.writeFieldName(name.getValue());
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (this._cfgPrettyPrinter != null) {
            if (status != 1) {
                z = false;
            }
            _writePPFieldName((SerializableString) name, z);
            return;
        }
        if (status == 1) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_COMMA;
        }
        _writeFieldName((SerializableString) name);
    }

    public final void writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        boolean z = true;
        int status = this._writeContext.writeFieldName(name.getValue());
        if (status == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (this._cfgPrettyPrinter != null) {
            if (status != 1) {
                z = false;
            }
            _writePPFieldName(name, z);
            return;
        }
        if (status == 1) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_COMMA;
        }
        _writeFieldName(name);
    }

    public final void writeStartArray() throws IOException, JsonGenerationException {
        _verifyValueWrite("start an array");
        this._writeContext = this._writeContext.createChildArrayContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartArray(this);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_LBRACKET;
    }

    public final void writeEndArray() throws IOException, JsonGenerationException {
        if (!this._writeContext.inArray()) {
            _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        } else {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_RBRACKET;
        }
        this._writeContext = this._writeContext.getParent();
    }

    public final void writeStartObject() throws IOException, JsonGenerationException {
        _verifyValueWrite("start an object");
        this._writeContext = this._writeContext.createChildObjectContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartObject(this);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_LCURLY;
    }

    public final void writeEndObject() throws IOException, JsonGenerationException {
        if (!this._writeContext.inObject()) {
            _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
        }
        this._writeContext = this._writeContext.getParent();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_RCURLY;
    }

    protected final void _writeFieldName(String name) throws IOException, JsonGenerationException {
        if (isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_QUOTE;
            int len = name.length();
            if (len <= this._charBufferLength) {
                name.getChars(0, len, this._charBuffer, 0);
                if (len <= this._outputMaxContiguous) {
                    if (this._outputTail + len > this._outputEnd) {
                        _flushBuffer();
                    }
                    _writeStringSegment(this._charBuffer, 0, len);
                } else {
                    _writeStringSegments(this._charBuffer, 0, len);
                }
            } else {
                _writeStringSegments(name);
            }
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            bArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_QUOTE;
            return;
        }
        _writeStringSegments(name);
    }

    protected final void _writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        byte[] raw = name.asQuotedUTF8();
        if (isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_QUOTE;
            int len = raw.length;
            if ((this._outputTail + len) + 1 < this._outputEnd) {
                System.arraycopy(raw, 0, this._outputBuffer, this._outputTail, len);
                this._outputTail += len;
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = BYTE_QUOTE;
                return;
            }
            _writeBytes(raw);
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            bArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_QUOTE;
            return;
        }
        _writeBytes(raw);
    }

    protected final void _writePPFieldName(String name, boolean commaBefore) throws IOException, JsonGenerationException {
        if (commaBefore) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        } else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        if (isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_QUOTE;
            int len = name.length();
            if (len <= this._charBufferLength) {
                name.getChars(0, len, this._charBuffer, 0);
                if (len <= this._outputMaxContiguous) {
                    if (this._outputTail + len > this._outputEnd) {
                        _flushBuffer();
                    }
                    _writeStringSegment(this._charBuffer, 0, len);
                } else {
                    _writeStringSegments(this._charBuffer, 0, len);
                }
            } else {
                _writeStringSegments(name);
            }
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            bArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_QUOTE;
            return;
        }
        _writeStringSegments(name);
    }

    protected final void _writePPFieldName(SerializableString name, boolean commaBefore) throws IOException, JsonGenerationException {
        if (commaBefore) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        } else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        boolean addQuotes = isEnabled(Feature.QUOTE_FIELD_NAMES);
        if (addQuotes) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            byte[] bArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_QUOTE;
        }
        _writeBytes(name.asQuotedUTF8());
        if (addQuotes) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            bArr = this._outputBuffer;
            i = this._outputTail;
            this._outputTail = i + 1;
            bArr[i] = BYTE_QUOTE;
        }
    }

    public void writeString(String text) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (text == null) {
            _writeNull();
            return;
        }
        int len = text.length();
        if (len > this._charBufferLength) {
            _writeLongString(text);
            return;
        }
        text.getChars(0, len, this._charBuffer, 0);
        if (len > this._outputMaxContiguous) {
            _writeLongString(this._charBuffer, 0, len);
            return;
        }
        if ((this._outputTail + len) + 2 > this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        _writeStringSegment(this._charBuffer, 0, len);
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    private final void _writeLongString(String text) throws IOException, JsonGenerationException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        _writeStringSegments(text);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    private final void _writeLongString(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        _writeStringSegments(this._charBuffer, 0, len);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeString(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        if (len <= this._outputMaxContiguous) {
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(text, offset, len);
        } else {
            _writeStringSegments(text, offset, len);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public final void writeString(SerializableString text) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        _writeBytes(text.asQuotedUTF8());
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        _writeBytes(text, offset, length);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeUTF8String(byte[] text, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        if (len <= this._outputMaxContiguous) {
            _writeUTF8Segment(text, offset, len);
        } else {
            _writeUTF8Segments(text, offset, len);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeRaw(String text) throws IOException, JsonGenerationException {
        int start = 0;
        int len = text.length();
        while (len > 0) {
            int len2;
            char[] buf = this._charBuffer;
            int blen = buf.length;
            if (len < blen) {
                len2 = len;
            } else {
                len2 = blen;
            }
            text.getChars(start, start + len2, buf, 0);
            writeRaw(buf, 0, len2);
            start += len2;
            len -= len2;
        }
    }

    public void writeRaw(String text, int offset, int len) throws IOException, JsonGenerationException {
        while (len > 0) {
            int len2;
            char[] buf = this._charBuffer;
            int blen = buf.length;
            if (len < blen) {
                len2 = len;
            } else {
                len2 = blen;
            }
            text.getChars(offset, offset + len2, buf, 0);
            writeRaw(buf, 0, len2);
            offset += len2;
            len -= len2;
        }
    }

    public final void writeRaw(char[] cbuf, int offset, int len) throws IOException, JsonGenerationException {
        int len3 = (len + len) + len;
        if (this._outputTail + len3 > this._outputEnd) {
            if (this._outputEnd < len3) {
                _writeSegmentedRaw(cbuf, offset, len);
                return;
            }
            _flushBuffer();
        }
        len += offset;
        while (offset < len) {
            char ch;
            while (true) {
                ch = cbuf[offset];
                if (ch > '\u007f') {
                    break;
                }
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ch;
                offset++;
                if (offset >= len) {
                    return;
                }
            }
            int offset2 = offset + 1;
            ch = cbuf[offset];
            if (ch < '\u0800') {
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((ch >> 6) | Wbxml.EXT_0);
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) ((ch & 63) | Flags.FLAG8);
            } else {
                _outputRawMultiByteChar(ch, cbuf, offset2, len);
            }
            offset = offset2;
        }
    }

    public void writeRaw(char ch) throws IOException, JsonGenerationException {
        if (this._outputTail + 3 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bbuf = this._outputBuffer;
        int i;
        if (ch <= '\u007f') {
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ch;
        } else if (ch < '\u0800') {
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ((ch >> 6) | Wbxml.EXT_0);
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ((ch & 63) | Flags.FLAG8);
        } else {
            _outputRawMultiByteChar(ch, null, 0, 0);
        }
    }

    private final void _writeSegmentedRaw(char[] cbuf, int offset, int len) throws IOException, JsonGenerationException {
        int end = this._outputEnd;
        byte[] bbuf = this._outputBuffer;
        while (offset < len) {
            char ch;
            while (true) {
                ch = cbuf[offset];
                if (ch >= '\u0080') {
                    break;
                }
                if (this._outputTail >= end) {
                    _flushBuffer();
                }
                int i = this._outputTail;
                this._outputTail = i + 1;
                bbuf[i] = (byte) ch;
                offset++;
                if (offset >= len) {
                    return;
                }
            }
            if (this._outputTail + 3 >= this._outputEnd) {
                _flushBuffer();
            }
            int offset2 = offset + 1;
            ch = cbuf[offset];
            if (ch < '\u0800') {
                i = this._outputTail;
                this._outputTail = i + 1;
                bbuf[i] = (byte) ((ch >> 6) | Wbxml.EXT_0);
                i = this._outputTail;
                this._outputTail = i + 1;
                bbuf[i] = (byte) ((ch & 63) | Flags.FLAG8);
            } else {
                _outputRawMultiByteChar(ch, cbuf, offset2, len);
            }
            offset = offset2;
        }
    }

    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write binary value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        _writeBinary(b64variant, data, offset, offset + len);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeNumber(int i) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (this._outputTail + 11 >= this._outputEnd) {
            _flushBuffer();
        }
        if (this._cfgNumbersAsStrings) {
            _writeQuotedInt(i);
        } else {
            this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
        }
    }

    private final void _writeQuotedInt(int i) throws IOException {
        if (this._outputTail + 13 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = BYTE_QUOTE;
        this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
        bArr = this._outputBuffer;
        i2 = this._outputTail;
        this._outputTail = i2 + 1;
        bArr[i2] = BYTE_QUOTE;
    }

    public void writeNumber(long l) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (this._cfgNumbersAsStrings) {
            _writeQuotedLong(l);
            return;
        }
        if (this._outputTail + 21 >= this._outputEnd) {
            _flushBuffer();
        }
        this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
    }

    private final void _writeQuotedLong(long l) throws IOException {
        if (this._outputTail + 23 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeNumber(BigInteger value) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (value == null) {
            _writeNull();
        } else if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(value);
        } else {
            writeRaw(value.toString());
        }
    }

    public void writeNumber(double d) throws IOException, JsonGenerationException {
        if (this._cfgNumbersAsStrings || ((Double.isNaN(d) || Double.isInfinite(d)) && isEnabled(Feature.QUOTE_NON_NUMERIC_NUMBERS))) {
            writeString(String.valueOf(d));
            return;
        }
        _verifyValueWrite("write number");
        writeRaw(String.valueOf(d));
    }

    public void writeNumber(float f) throws IOException, JsonGenerationException {
        if (this._cfgNumbersAsStrings || ((Float.isNaN(f) || Float.isInfinite(f)) && isEnabled(Feature.QUOTE_NON_NUMERIC_NUMBERS))) {
            writeString(String.valueOf(f));
            return;
        }
        _verifyValueWrite("write number");
        writeRaw(String.valueOf(f));
    }

    public void writeNumber(BigDecimal value) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (value == null) {
            _writeNull();
        } else if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(value);
        } else {
            writeRaw(value.toString());
        }
    }

    public void writeNumber(String encodedValue) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(encodedValue);
        } else {
            writeRaw(encodedValue);
        }
    }

    private final void _writeQuotedRaw(Object value) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] bArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
        writeRaw(value.toString());
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        bArr = this._outputBuffer;
        i = this._outputTail;
        this._outputTail = i + 1;
        bArr[i] = BYTE_QUOTE;
    }

    public void writeBoolean(boolean state) throws IOException, JsonGenerationException {
        _verifyValueWrite("write boolean value");
        if (this._outputTail + 5 >= this._outputEnd) {
            _flushBuffer();
        }
        byte[] keyword = state ? TRUE_BYTES : FALSE_BYTES;
        int len = keyword.length;
        System.arraycopy(keyword, 0, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    public void writeNull() throws IOException, JsonGenerationException {
        _verifyValueWrite("write null value");
        _writeNull();
    }

    protected final void _verifyValueWrite(String typeMsg) throws IOException, JsonGenerationException {
        int status = this._writeContext.writeValue();
        if (status == 5) {
            _reportError("Can not " + typeMsg + ", expecting field name");
        }
        if (this._cfgPrettyPrinter == null) {
            byte b;
            switch (status) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    b = BYTE_COMMA;
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    b = BYTE_COLON;
                    break;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    b = BYTE_SPACE;
                    break;
                default:
                    return;
            }
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            this._outputBuffer[this._outputTail] = b;
            this._outputTail++;
            return;
        }
        _verifyPrettyValueWrite(typeMsg, status);
    }

    protected final void _verifyPrettyValueWrite(String typeMsg, int status) throws IOException, JsonGenerationException {
        switch (status) {
            case KEYRecord.OWNER_USER /*0*/:
                if (this._writeContext.inArray()) {
                    this._cfgPrettyPrinter.beforeArrayValues(this);
                } else if (this._writeContext.inObject()) {
                    this._cfgPrettyPrinter.beforeObjectEntries(this);
                }
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this._cfgPrettyPrinter.writeArrayValueSeparator(this);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                this._cfgPrettyPrinter.writeObjectFieldValueSeparator(this);
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                this._cfgPrettyPrinter.writeRootValueSeparator(this);
            default:
                _cantHappen();
        }
    }

    public final void flush() throws IOException {
        _flushBuffer();
        if (this._outputStream != null && isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
            this._outputStream.flush();
        }
    }

    public void close() throws IOException {
        super.close();
        if (this._outputBuffer != null && isEnabled(Feature.AUTO_CLOSE_JSON_CONTENT)) {
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
        _flushBuffer();
        if (this._ioContext.isResourceManaged() || isEnabled(Feature.AUTO_CLOSE_TARGET)) {
            this._outputStream.close();
        } else {
            this._outputStream.flush();
        }
        _releaseBuffers();
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
    }

    private final void _writeBytes(byte[] bytes) throws IOException {
        int len = bytes.length;
        if (this._outputTail + len > this._outputEnd) {
            _flushBuffer();
            if (len > MAX_BYTES_TO_BUFFER) {
                this._outputStream.write(bytes, 0, len);
                return;
            }
        }
        System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    private final void _writeBytes(byte[] bytes, int offset, int len) throws IOException {
        if (this._outputTail + len > this._outputEnd) {
            _flushBuffer();
            if (len > MAX_BYTES_TO_BUFFER) {
                this._outputStream.write(bytes, offset, len);
                return;
            }
        }
        System.arraycopy(bytes, offset, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
    }

    private final void _writeStringSegments(String text) throws IOException, JsonGenerationException {
        int left = text.length();
        int offset = 0;
        char[] cbuf = this._charBuffer;
        while (left > 0) {
            int len = Math.min(this._outputMaxContiguous, left);
            text.getChars(offset, offset + len, cbuf, 0);
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(cbuf, 0, len);
            offset += len;
            left -= len;
        }
    }

    private final void _writeStringSegments(char[] cbuf, int offset, int totalLen) throws IOException, JsonGenerationException {
        do {
            int len = Math.min(this._outputMaxContiguous, totalLen);
            if (this._outputTail + len > this._outputEnd) {
                _flushBuffer();
            }
            _writeStringSegment(cbuf, offset, len);
            offset += len;
            totalLen -= len;
        } while (totalLen > 0);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void _writeStringSegment(char[] r7, int r8, int r9) throws java.io.IOException, org.codehaus.jackson.JsonGenerationException {
        /*
        r6 = this;
        r9 = r9 + r8;
        r3 = r6._outputTail;
        r2 = r6._outputBuffer;
        r1 = sOutputEscapes;
        r4 = r3;
    L_0x0008:
        if (r8 >= r9) goto L_0x0014;
    L_0x000a:
        r0 = r7[r8];
        r5 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        if (r0 > r5) goto L_0x0014;
    L_0x0010:
        r5 = r1[r0];
        if (r5 == 0) goto L_0x001c;
    L_0x0014:
        r6._outputTail = r4;
        if (r8 >= r9) goto L_0x001b;
    L_0x0018:
        r6._writeStringSegment2(r7, r8, r9);
    L_0x001b:
        return;
    L_0x001c:
        r3 = r4 + 1;
        r5 = (byte) r0;
        r2[r4] = r5;
        r8 = r8 + 1;
        r4 = r3;
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.Utf8Generator._writeStringSegment(char[], int, int):void");
    }

    private final void _writeStringSegment2(char[] cbuf, int offset, int end) throws IOException, JsonGenerationException {
        if (this._outputTail + ((end - offset) * 6) > this._outputEnd) {
            _flushBuffer();
        }
        int outputPtr = this._outputTail;
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = sOutputEscapes;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < end) {
            offset = offset2 + 1;
            int ch = cbuf[offset2];
            if (ch > Service.LOCUS_CON) {
                if (ch <= 2047) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = (byte) ((ch >> 6) | Wbxml.EXT_0);
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) ((ch & 63) | Flags.FLAG8);
                    outputPtr = outputPtr2;
                } else {
                    outputPtr = _outputMultiByteChar(ch, outputPtr2);
                }
                outputPtr2 = outputPtr;
                offset2 = offset;
            } else if (escCodes[ch] == 0) {
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = (byte) ch;
                outputPtr2 = outputPtr;
                offset2 = offset;
            } else {
                int escape = escCodes[ch];
                if (escape > 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = BYTE_BACKSLASH;
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) escape;
                    offset2 = offset;
                } else {
                    outputPtr2 = _writeEscapedControlChar(escape, outputPtr2);
                    offset2 = offset;
                }
            }
        }
        this._outputTail = outputPtr2;
    }

    private final void _writeUTF8Segments(byte[] utf8, int offset, int totalLen) throws IOException, JsonGenerationException {
        do {
            int len = Math.min(this._outputMaxContiguous, totalLen);
            _writeUTF8Segment(utf8, offset, len);
            offset += len;
            totalLen -= len;
        } while (totalLen > 0);
    }

    private final void _writeUTF8Segment(byte[] utf8, int offset, int len) throws IOException, JsonGenerationException {
        int ptr;
        int[] escCodes = sOutputEscapes;
        int end = offset + len;
        int ptr2 = offset;
        while (ptr2 < end) {
            ptr = ptr2 + 1;
            if (escCodes[utf8[ptr2] & KEYRecord.PROTOCOL_ANY] != 0) {
                _writeUTFSegment2(utf8, offset, len);
                return;
            }
            ptr2 = ptr;
        }
        if (this._outputTail + len > this._outputEnd) {
            _flushBuffer();
        }
        System.arraycopy(utf8, offset, this._outputBuffer, this._outputTail, len);
        this._outputTail += len;
        ptr = ptr2;
    }

    private final void _writeUTFSegment2(byte[] utf8, int offset, int len) throws IOException, JsonGenerationException {
        int outputPtr = this._outputTail;
        if ((len * 6) + outputPtr > this._outputEnd) {
            _flushBuffer();
            outputPtr = this._outputTail;
        }
        byte[] outputBuffer = this._outputBuffer;
        int[] escCodes = sOutputEscapes;
        len += offset;
        int outputPtr2 = outputPtr;
        int offset2 = offset;
        while (offset2 < len) {
            offset = offset2 + 1;
            byte b = utf8[offset2];
            int ch = b & KEYRecord.PROTOCOL_ANY;
            if (escCodes[ch] == 0) {
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = b;
                outputPtr2 = outputPtr;
                offset2 = offset;
            } else {
                int escape = sOutputEscapes[ch];
                if (escape > 0) {
                    outputPtr = outputPtr2 + 1;
                    outputBuffer[outputPtr2] = BYTE_BACKSLASH;
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) escape;
                    outputPtr = outputPtr2;
                } else {
                    outputPtr = _writeEscapedControlChar(escape, outputPtr2);
                }
                outputPtr2 = outputPtr;
                offset2 = offset;
            }
        }
        this._outputTail = outputPtr2;
    }

    protected void _writeBinary(Base64Variant b64variant, byte[] input, int inputPtr, int inputEnd) throws IOException, JsonGenerationException {
        int safeInputEnd = inputEnd - 3;
        int safeOutputEnd = this._outputEnd - 6;
        int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
        int inputPtr2 = inputPtr;
        while (inputPtr2 <= safeInputEnd) {
            if (this._outputTail > safeOutputEnd) {
                _flushBuffer();
            }
            inputPtr = inputPtr2 + 1;
            inputPtr2 = inputPtr + 1;
            inputPtr = inputPtr2 + 1;
            this._outputTail = b64variant.encodeBase64Chunk((((input[inputPtr2] << 8) | (input[inputPtr] & KEYRecord.PROTOCOL_ANY)) << 8) | (input[inputPtr2] & KEYRecord.PROTOCOL_ANY), this._outputBuffer, this._outputTail);
            chunksBeforeLF--;
            if (chunksBeforeLF <= 0) {
                byte[] bArr = this._outputBuffer;
                int i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = BYTE_BACKSLASH;
                bArr = this._outputBuffer;
                i = this._outputTail;
                this._outputTail = i + 1;
                bArr[i] = (byte) 110;
                chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
            }
            inputPtr2 = inputPtr;
        }
        int inputLeft = inputEnd - inputPtr2;
        if (inputLeft > 0) {
            if (this._outputTail > safeOutputEnd) {
                _flushBuffer();
            }
            inputPtr = inputPtr2 + 1;
            int b24 = input[inputPtr2] << 16;
            if (inputLeft == 2) {
                b24 |= (input[inputPtr] & KEYRecord.PROTOCOL_ANY) << 8;
                inputPtr++;
            }
            this._outputTail = b64variant.encodeBase64Partial(b24, inputLeft, this._outputBuffer, this._outputTail);
            return;
        }
    }

    private final int _outputRawMultiByteChar(int ch, char[] cbuf, int inputOffset, int inputLen) throws IOException {
        if (ch < SURR1_FIRST || ch > SURR2_LAST) {
            byte[] bbuf = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ((ch >> 12) | SmileConstants.TOKEN_PREFIX_MISC_OTHER);
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) (((ch >> 6) & 63) | Flags.FLAG8);
            i = this._outputTail;
            this._outputTail = i + 1;
            bbuf[i] = (byte) ((ch & 63) | Flags.FLAG8);
            return inputOffset;
        }
        if (inputOffset >= inputLen) {
            _reportError("Split surrogate on writeRaw() input (last character)");
        }
        _outputSurrogates(ch, cbuf[inputOffset]);
        return inputOffset + 1;
    }

    protected final void _outputSurrogates(int surr1, int surr2) throws IOException {
        int c = _decodeSurrogate(surr1, surr2);
        if (this._outputTail + 4 > this._outputEnd) {
            _flushBuffer();
        }
        byte[] bbuf = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) ((c >> 18) | 240);
        i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) (((c >> 12) & 63) | Flags.FLAG8);
        i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) (((c >> 6) & 63) | Flags.FLAG8);
        i = this._outputTail;
        this._outputTail = i + 1;
        bbuf[i] = (byte) ((c & 63) | Flags.FLAG8);
    }

    private final int _outputMultiByteChar(int ch, int outputPtr) throws IOException {
        byte[] bbuf = this._outputBuffer;
        if (ch < SURR1_FIRST || ch > SURR2_LAST) {
            int i = outputPtr + 1;
            bbuf[outputPtr] = (byte) ((ch >> 12) | SmileConstants.TOKEN_PREFIX_MISC_OTHER);
            outputPtr = i + 1;
            bbuf[i] = (byte) (((ch >> 6) & 63) | Flags.FLAG8);
            i = outputPtr + 1;
            bbuf[outputPtr] = (byte) ((ch & 63) | Flags.FLAG8);
            return i;
        }
        i = outputPtr + 1;
        bbuf[outputPtr] = BYTE_BACKSLASH;
        outputPtr = i + 1;
        bbuf[i] = BYTE_u;
        i = outputPtr + 1;
        bbuf[outputPtr] = HEX_CHARS[(ch >> 12) & 15];
        outputPtr = i + 1;
        bbuf[i] = HEX_CHARS[(ch >> 8) & 15];
        i = outputPtr + 1;
        bbuf[outputPtr] = HEX_CHARS[(ch >> 4) & 15];
        outputPtr = i + 1;
        bbuf[i] = HEX_CHARS[ch & 15];
        return outputPtr;
    }

    protected final int _decodeSurrogate(int surr1, int surr2) throws IOException {
        if (surr2 < SURR2_FIRST || surr2 > SURR2_LAST) {
            _reportError("Incomplete surrogate pair: first char 0x" + Integer.toHexString(surr1) + ", second 0x" + Integer.toHexString(surr2));
        }
        return (AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED + ((surr1 - SURR1_FIRST) << 10)) + (surr2 - SURR2_FIRST);
    }

    private final void _writeNull() throws IOException {
        if (this._outputTail + 4 >= this._outputEnd) {
            _flushBuffer();
        }
        System.arraycopy(NULL_BYTES, 0, this._outputBuffer, this._outputTail, 4);
        this._outputTail += 4;
    }

    private int _writeEscapedControlChar(int escCode, int outputPtr) throws IOException {
        byte[] bbuf = this._outputBuffer;
        int i = outputPtr + 1;
        bbuf[outputPtr] = BYTE_BACKSLASH;
        int value = -(escCode + 1);
        outputPtr = i + 1;
        bbuf[i] = BYTE_u;
        i = outputPtr + 1;
        bbuf[outputPtr] = BYTE_0;
        outputPtr = i + 1;
        bbuf[i] = BYTE_0;
        i = outputPtr + 1;
        bbuf[outputPtr] = HEX_CHARS[value >> 4];
        outputPtr = i + 1;
        bbuf[i] = HEX_CHARS[value & 15];
        return outputPtr;
    }

    protected final void _flushBuffer() throws IOException {
        int len = this._outputTail;
        if (len > 0) {
            this._outputTail = 0;
            this._outputStream.write(this._outputBuffer, 0, len);
        }
    }
}
