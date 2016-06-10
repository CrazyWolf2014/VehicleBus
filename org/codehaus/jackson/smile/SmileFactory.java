package org.codehaus.jackson.smile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.smile.SmileParser.Feature;

public class SmileFactory extends JsonFactory {
    static final int DEFAULT_SMILE_GENERATOR_FEATURE_FLAGS;
    static final int DEFAULT_SMILE_PARSER_FEATURE_FLAGS;
    protected boolean _cfgDelegateToTextual;
    protected int _smileGeneratorFeatures;
    protected int _smileParserFeatures;

    static {
        DEFAULT_SMILE_PARSER_FEATURE_FLAGS = Feature.collectDefaults();
        DEFAULT_SMILE_GENERATOR_FEATURE_FLAGS = SmileGenerator.Feature.collectDefaults();
    }

    public SmileFactory() {
        this(null);
    }

    public SmileFactory(ObjectCodec oc) {
        super(oc);
        this._smileParserFeatures = DEFAULT_SMILE_PARSER_FEATURE_FLAGS;
        this._smileGeneratorFeatures = DEFAULT_SMILE_GENERATOR_FEATURE_FLAGS;
    }

    public void delegateToTextual(boolean state) {
        this._cfgDelegateToTextual = state;
    }

    public final SmileFactory configure(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public SmileFactory enable(Feature f) {
        this._smileParserFeatures |= f.getMask();
        return this;
    }

    public SmileFactory disable(Feature f) {
        this._smileParserFeatures &= f.getMask() ^ -1;
        return this;
    }

    public final boolean isEnabled(Feature f) {
        return (this._smileParserFeatures & f.getMask()) != 0;
    }

    public final SmileFactory configure(SmileGenerator.Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public SmileFactory enable(SmileGenerator.Feature f) {
        this._smileGeneratorFeatures |= f.getMask();
        return this;
    }

    public SmileFactory disable(SmileGenerator.Feature f) {
        this._smileGeneratorFeatures &= f.getMask() ^ -1;
        return this;
    }

    public final boolean isEnabled(SmileGenerator.Feature f) {
        return (this._smileGeneratorFeatures & f.getMask()) != 0;
    }

    public SmileParser createJsonParser(File f) throws IOException, JsonParseException {
        return _createJsonParser(new FileInputStream(f), _createContext(f, true));
    }

    public SmileParser createJsonParser(URL url) throws IOException, JsonParseException {
        return _createJsonParser(_optimizedStreamFromURL(url), _createContext(url, true));
    }

    public SmileParser createJsonParser(InputStream in) throws IOException, JsonParseException {
        return _createJsonParser(in, _createContext(in, false));
    }

    public SmileParser createJsonParser(byte[] data) throws IOException, JsonParseException {
        return _createJsonParser(data, 0, data.length, _createContext(data, true));
    }

    public SmileParser createJsonParser(byte[] data, int offset, int len) throws IOException, JsonParseException {
        return _createJsonParser(data, offset, len, _createContext(data, true));
    }

    public SmileGenerator createJsonGenerator(OutputStream out, JsonEncoding enc) throws IOException {
        return createJsonGenerator(out);
    }

    public SmileGenerator createJsonGenerator(OutputStream out) throws IOException {
        return _createJsonGenerator(out, _createContext(out, false));
    }

    protected SmileParser _createJsonParser(InputStream in, IOContext ctxt) throws IOException, JsonParseException {
        return new SmileParserBootstrapper(ctxt, in).constructParser(this._parserFeatures, this._smileParserFeatures, this._objectCodec, this._rootByteSymbols);
    }

    protected JsonParser _createJsonParser(Reader r, IOContext ctxt) throws IOException, JsonParseException {
        if (this._cfgDelegateToTextual) {
            return super._createJsonParser(r, ctxt);
        }
        throw new UnsupportedOperationException("Can not create generator for non-byte-based target");
    }

    protected SmileParser _createJsonParser(byte[] data, int offset, int len, IOContext ctxt) throws IOException, JsonParseException {
        return new SmileParserBootstrapper(ctxt, data, offset, len).constructParser(this._parserFeatures, this._smileParserFeatures, this._objectCodec, this._rootByteSymbols);
    }

    protected JsonGenerator _createJsonGenerator(Writer out, IOContext ctxt) throws IOException {
        if (this._cfgDelegateToTextual) {
            return super._createJsonGenerator(out, ctxt);
        }
        throw new UnsupportedOperationException("Can not create generator for non-byte-based target");
    }

    protected Writer _createWriter(OutputStream out, JsonEncoding enc, IOContext ctxt) throws IOException {
        if (this._cfgDelegateToTextual) {
            return super._createWriter(out, enc, ctxt);
        }
        throw new UnsupportedOperationException("Can not create generator for non-byte-based target");
    }

    protected SmileGenerator _createJsonGenerator(OutputStream out, IOContext ctxt) throws IOException {
        int feats = this._smileGeneratorFeatures;
        SmileGenerator gen = new SmileGenerator(ctxt, this._generatorFeatures, feats, this._objectCodec, out);
        if ((SmileGenerator.Feature.WRITE_HEADER.getMask() & feats) != 0) {
            gen.writeHeader();
        } else if ((SmileGenerator.Feature.CHECK_SHARED_STRING_VALUES.getMask() & feats) != 0) {
            throw new JsonGenerationException("Inconsistent settings: WRITE_HEADER disabled, but CHECK_SHARED_STRING_VALUES enabled; can not construct generator due to possible data loss (either enable WRITE_HEADER, or disable CHECK_SHARED_STRING_VALUES to resolve)");
        } else if ((SmileGenerator.Feature.ENCODE_BINARY_AS_7BIT.getMask() & feats) == 0) {
            throw new JsonGenerationException("Inconsistent settings: WRITE_HEADER disabled, but ENCODE_BINARY_AS_7BIT disabled; can not construct generator due to possible data loss (either enable WRITE_HEADER, or ENCODE_BINARY_AS_7BIT to resolve)");
        }
        return gen;
    }
}
