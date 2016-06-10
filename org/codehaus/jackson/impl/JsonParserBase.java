package org.codehaus.jackson.impl;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.io.IOException;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.TextBuffer;
import org.codehaus.jackson.util.VersionUtil;
import org.xmlpull.v1.XmlPullParser;

public abstract class JsonParserBase extends JsonParserMinimalBase {
    protected byte[] _binaryValue;
    protected ByteArrayBuilder _byteArrayBuilder;
    protected boolean _closed;
    protected long _currInputProcessed;
    protected int _currInputRow;
    protected int _currInputRowStart;
    protected int _inputEnd;
    protected int _inputPtr;
    protected final IOContext _ioContext;
    protected boolean _nameCopied;
    protected char[] _nameCopyBuffer;
    protected JsonToken _nextToken;
    protected JsonReadContext _parsingContext;
    protected final TextBuffer _textBuffer;
    protected int _tokenInputCol;
    protected int _tokenInputRow;
    protected long _tokenInputTotal;

    /* renamed from: org.codehaus.jackson.impl.JsonParserBase.1 */
    static /* synthetic */ class C09341 {
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
        }
    }

    protected abstract void _closeInput() throws IOException;

    protected abstract byte[] _decodeBase64(Base64Variant base64Variant) throws IOException, JsonParseException;

    protected abstract void _finishString() throws IOException, JsonParseException;

    protected abstract boolean loadMore() throws IOException;

    protected JsonParserBase(IOContext ctxt, int features) {
        this._inputPtr = 0;
        this._inputEnd = 0;
        this._currInputProcessed = 0;
        this._currInputRow = 1;
        this._currInputRowStart = 0;
        this._tokenInputTotal = 0;
        this._tokenInputRow = 1;
        this._tokenInputCol = 0;
        this._nameCopyBuffer = null;
        this._nameCopied = false;
        this._byteArrayBuilder = null;
        this._features = features;
        this._ioContext = ctxt;
        this._textBuffer = ctxt.constructTextBuffer();
        this._parsingContext = JsonReadContext.createRootContext(this._tokenInputRow, this._tokenInputCol);
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public String getCurrentName() throws IOException, JsonParseException {
        if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
            return this._parsingContext.getParent().getCurrentName();
        }
        return this._parsingContext.getCurrentName();
    }

    public void close() throws IOException {
        if (!this._closed) {
            this._closed = true;
            try {
                _closeInput();
            } finally {
                _releaseBuffers();
            }
        }
    }

    public boolean isClosed() {
        return this._closed;
    }

    public JsonReadContext getParsingContext() {
        return this._parsingContext;
    }

    public JsonLocation getTokenLocation() {
        return new JsonLocation(this._ioContext.getSourceReference(), getTokenCharacterOffset(), getTokenLineNr(), getTokenColumnNr());
    }

    public JsonLocation getCurrentLocation() {
        return new JsonLocation(this._ioContext.getSourceReference(), (this._currInputProcessed + ((long) this._inputPtr)) - 1, this._currInputRow, (this._inputPtr - this._currInputRowStart) + 1);
    }

    public boolean hasTextCharacters() {
        if (this._currToken != null) {
            switch (C09341.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    return this._nameCopied;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    return true;
            }
        }
        return false;
    }

    public final long getTokenCharacterOffset() {
        return this._tokenInputTotal;
    }

    public final int getTokenLineNr() {
        return this._tokenInputRow;
    }

    public final int getTokenColumnNr() {
        return this._tokenInputCol + 1;
    }

    protected final void loadMoreGuaranteed() throws IOException {
        if (!loadMore()) {
            _reportInvalidEOF();
        }
    }

    protected void _releaseBuffers() throws IOException {
        this._textBuffer.releaseBuffers();
        char[] buf = this._nameCopyBuffer;
        if (buf != null) {
            this._nameCopyBuffer = null;
            this._ioContext.releaseNameCopyBuffer(buf);
        }
    }

    protected void _handleEOF() throws JsonParseException {
        if (!this._parsingContext.inRoot()) {
            _reportInvalidEOF(": expected close marker for " + this._parsingContext.getTypeDesc() + " (from " + this._parsingContext.getStartLocation(this._ioContext.getSourceReference()) + ")");
        }
    }

    protected void _reportMismatchedEndMarker(int actCh, char expCh) throws JsonParseException {
        _reportError("Unexpected close marker '" + ((char) actCh) + "': expected '" + expCh + "' (for " + this._parsingContext.getTypeDesc() + " starting at " + (XmlPullParser.NO_NAMESPACE + this._parsingContext.getStartLocation(this._ioContext.getSourceReference())) + ")");
    }

    public ByteArrayBuilder _getByteArrayBuilder() {
        if (this._byteArrayBuilder == null) {
            this._byteArrayBuilder = new ByteArrayBuilder();
        } else {
            this._byteArrayBuilder.reset();
        }
        return this._byteArrayBuilder;
    }
}
