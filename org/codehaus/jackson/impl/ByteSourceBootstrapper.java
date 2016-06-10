package org.codehaus.jackson.impl;

import android.support.v4.view.MotionEventCompat;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.io.ByteArrayInputStream;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.MergedStream;
import org.codehaus.jackson.io.UTF32Reader;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.CharsToNameCanonicalizer;
import org.xbill.DNS.KEYRecord;

public final class ByteSourceBootstrapper {
    boolean _bigEndian;
    private final boolean _bufferRecyclable;
    int _bytesPerChar;
    final IOContext _context;
    final InputStream _in;
    final byte[] _inputBuffer;
    private int _inputEnd;
    protected int _inputProcessed;
    private int _inputPtr;

    /* renamed from: org.codehaus.jackson.impl.ByteSourceBootstrapper.1 */
    static /* synthetic */ class C09321 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonEncoding;

        static {
            $SwitchMap$org$codehaus$jackson$JsonEncoding = new int[JsonEncoding.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonEncoding[JsonEncoding.UTF32_BE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonEncoding[JsonEncoding.UTF32_LE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonEncoding[JsonEncoding.UTF16_BE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonEncoding[JsonEncoding.UTF16_LE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonEncoding[JsonEncoding.UTF8.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public ByteSourceBootstrapper(IOContext ctxt, InputStream in) {
        this._bigEndian = true;
        this._bytesPerChar = 0;
        this._context = ctxt;
        this._in = in;
        this._inputBuffer = ctxt.allocReadIOBuffer();
        this._inputPtr = 0;
        this._inputEnd = 0;
        this._inputProcessed = 0;
        this._bufferRecyclable = true;
    }

    public ByteSourceBootstrapper(IOContext ctxt, byte[] inputBuffer, int inputStart, int inputLen) {
        this._bigEndian = true;
        this._bytesPerChar = 0;
        this._context = ctxt;
        this._in = null;
        this._inputBuffer = inputBuffer;
        this._inputPtr = inputStart;
        this._inputEnd = inputStart + inputLen;
        this._inputProcessed = -inputStart;
        this._bufferRecyclable = false;
    }

    public JsonEncoding detectEncoding() throws IOException, JsonParseException {
        JsonEncoding enc;
        boolean foundEncoding = false;
        if (ensureLoaded(4)) {
            int quad = (((this._inputBuffer[this._inputPtr] << 24) | ((this._inputBuffer[this._inputPtr + 1] & KEYRecord.PROTOCOL_ANY) << 16)) | ((this._inputBuffer[this._inputPtr + 2] & KEYRecord.PROTOCOL_ANY) << 8)) | (this._inputBuffer[this._inputPtr + 3] & KEYRecord.PROTOCOL_ANY);
            if (handleBOM(quad)) {
                foundEncoding = true;
            } else if (checkUTF32(quad)) {
                foundEncoding = true;
            } else if (checkUTF16(quad >>> 16)) {
                foundEncoding = true;
            }
        } else if (ensureLoaded(2) && checkUTF16(((this._inputBuffer[this._inputPtr] & KEYRecord.PROTOCOL_ANY) << 8) | (this._inputBuffer[this._inputPtr + 1] & KEYRecord.PROTOCOL_ANY))) {
            foundEncoding = true;
        }
        if (!foundEncoding) {
            enc = JsonEncoding.UTF8;
        } else if (this._bytesPerChar == 2) {
            enc = this._bigEndian ? JsonEncoding.UTF16_BE : JsonEncoding.UTF16_LE;
        } else if (this._bytesPerChar == 4) {
            enc = this._bigEndian ? JsonEncoding.UTF32_BE : JsonEncoding.UTF32_LE;
        } else {
            throw new RuntimeException("Internal error");
        }
        this._context.setEncoding(enc);
        return enc;
    }

    public Reader constructReader() throws IOException {
        JsonEncoding enc = this._context.getEncoding();
        switch (C09321.$SwitchMap$org$codehaus$jackson$JsonEncoding[enc.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return new UTF32Reader(this._context, this._in, this._inputBuffer, this._inputPtr, this._inputEnd, this._context.getEncoding().isBigEndian());
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                InputStream in = this._in;
                InputStream in2 = in == null ? new ByteArrayInputStream(this._inputBuffer, this._inputPtr, this._inputEnd) : this._inputPtr < this._inputEnd ? new MergedStream(this._context, in, this._inputBuffer, this._inputPtr, this._inputEnd) : in;
                return new InputStreamReader(in2, enc.getJavaName());
            default:
                throw new RuntimeException("Internal error");
        }
    }

    public JsonParser constructParser(int features, ObjectCodec codec, BytesToNameCanonicalizer rootByteSymbols, CharsToNameCanonicalizer rootCharSymbols) throws IOException, JsonParseException {
        JsonEncoding enc = detectEncoding();
        boolean canonicalize = Feature.CANONICALIZE_FIELD_NAMES.enabledIn(features);
        boolean intern = Feature.INTERN_FIELD_NAMES.enabledIn(features);
        if (enc == JsonEncoding.UTF8 && canonicalize) {
            return new Utf8StreamParser(this._context, features, this._in, codec, rootByteSymbols.makeChild(canonicalize, intern), this._inputBuffer, this._inputPtr, this._inputEnd, this._bufferRecyclable);
        }
        return new ReaderBasedParser(this._context, features, constructReader(), codec, rootCharSymbols.makeChild(canonicalize, intern));
    }

    private boolean handleBOM(int quad) throws IOException {
        switch (quad) {
            case -16842752:
                break;
            case -131072:
                this._inputPtr += 4;
                this._bytesPerChar = 4;
                this._bigEndian = false;
                return true;
            case 65279:
                this._bigEndian = true;
                this._inputPtr += 4;
                this._bytesPerChar = 4;
                return true;
            case 65534:
                reportWeirdUCS4("2143");
                break;
        }
        reportWeirdUCS4("3412");
        int msw = quad >>> 16;
        if (msw == 65279) {
            this._inputPtr += 2;
            this._bytesPerChar = 2;
            this._bigEndian = true;
            return true;
        } else if (msw == 65534) {
            this._inputPtr += 2;
            this._bytesPerChar = 2;
            this._bigEndian = false;
            return true;
        } else if ((quad >>> 8) != 15711167) {
            return false;
        } else {
            this._inputPtr += 3;
            this._bytesPerChar = 1;
            this._bigEndian = true;
            return true;
        }
    }

    private boolean checkUTF32(int quad) throws IOException {
        if ((quad >> 8) == 0) {
            this._bigEndian = true;
        } else if ((16777215 & quad) == 0) {
            this._bigEndian = false;
        } else if ((-16711681 & quad) == 0) {
            reportWeirdUCS4("3412");
        } else if ((-65281 & quad) != 0) {
            return false;
        } else {
            reportWeirdUCS4("2143");
        }
        this._bytesPerChar = 4;
        return true;
    }

    private boolean checkUTF16(int i16) {
        if ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i16) == 0) {
            this._bigEndian = true;
        } else if ((i16 & KEYRecord.PROTOCOL_ANY) != 0) {
            return false;
        } else {
            this._bigEndian = false;
        }
        this._bytesPerChar = 2;
        return true;
    }

    private void reportWeirdUCS4(String type) throws IOException {
        throw new CharConversionException("Unsupported UCS-4 endianness (" + type + ") detected");
    }

    protected boolean ensureLoaded(int minimum) throws IOException {
        int gotten = this._inputEnd - this._inputPtr;
        while (gotten < minimum) {
            int count;
            if (this._in == null) {
                count = -1;
            } else {
                count = this._in.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
            }
            if (count < 1) {
                return false;
            }
            this._inputEnd += count;
            gotten += count;
        }
        return true;
    }
}
