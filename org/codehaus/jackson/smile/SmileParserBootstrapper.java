package org.codehaus.jackson.smile;

import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.xbill.DNS.KEYRecord;

public class SmileParserBootstrapper {
    private final boolean _bufferRecyclable;
    final IOContext _context;
    final InputStream _in;
    final byte[] _inputBuffer;
    private int _inputEnd;
    protected int _inputProcessed;
    private int _inputPtr;

    public SmileParserBootstrapper(IOContext ctxt, InputStream in) {
        this._context = ctxt;
        this._in = in;
        this._inputBuffer = ctxt.allocReadIOBuffer();
        this._inputPtr = 0;
        this._inputEnd = 0;
        this._inputProcessed = 0;
        this._bufferRecyclable = true;
    }

    public SmileParserBootstrapper(IOContext ctxt, byte[] inputBuffer, int inputStart, int inputLen) {
        this._context = ctxt;
        this._in = null;
        this._inputBuffer = inputBuffer;
        this._inputPtr = inputStart;
        this._inputEnd = inputStart + inputLen;
        this._inputProcessed = -inputStart;
        this._bufferRecyclable = false;
    }

    public SmileParser constructParser(int generalParserFeatures, int smileFeatures, ObjectCodec codec, BytesToNameCanonicalizer rootByteSymbols) throws IOException, JsonParseException {
        BytesToNameCanonicalizer can = rootByteSymbols.makeChild(true, Feature.INTERN_FIELD_NAMES.enabledIn(generalParserFeatures));
        ensureLoaded(1);
        SmileParser p = new SmileParser(this._context, generalParserFeatures, smileFeatures, codec, can, this._in, this._inputBuffer, this._inputPtr, this._inputEnd, this._bufferRecyclable);
        boolean hadSig = false;
        if (this._inputPtr < this._inputEnd && this._inputBuffer[this._inputPtr] == 58) {
            hadSig = p.handleSignature(true, true);
        }
        if (hadSig || (SmileParser.Feature.REQUIRE_HEADER.getMask() & smileFeatures) == 0) {
            return p;
        }
        String msg;
        byte firstByte = this._inputPtr < this._inputEnd ? this._inputBuffer[this._inputPtr] : (byte) 0;
        if (firstByte == 123 || firstByte == 91) {
            msg = "Input does not start with Smile format header (first byte = 0x" + Integer.toHexString(firstByte & KEYRecord.PROTOCOL_ANY) + ") -- rather, it starts with '" + ((char) firstByte) + "' (plain JSON input?) -- can not parse";
        } else {
            msg = "Input does not start with Smile format header (first byte = 0x" + Integer.toHexString(firstByte & KEYRecord.PROTOCOL_ANY) + ") and parser has REQUIRE_HEADER enabled: can not parse";
        }
        throw new JsonParseException(msg, JsonLocation.NA);
    }

    protected boolean ensureLoaded(int minimum) throws IOException {
        if (this._in == null) {
            return false;
        }
        int gotten = this._inputEnd - this._inputPtr;
        while (gotten < minimum) {
            int count = this._in.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
            if (count < 1) {
                return false;
            }
            this._inputEnd += count;
            gotten += count;
        }
        return true;
    }
}
