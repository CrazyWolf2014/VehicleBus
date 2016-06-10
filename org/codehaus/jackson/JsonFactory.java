package org.codehaus.jackson;

import com.cnmobi.im.dto.MessageVo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.ref.SoftReference;
import java.net.URL;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.impl.ByteSourceBootstrapper;
import org.codehaus.jackson.impl.ReaderBasedParser;
import org.codehaus.jackson.impl.Utf8Generator;
import org.codehaus.jackson.impl.WriterBasedGenerator;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.UTF8Writer;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.CharsToNameCanonicalizer;
import org.codehaus.jackson.util.BufferRecycler;
import org.codehaus.jackson.util.VersionUtil;

public class JsonFactory implements Versioned {
    static final int DEFAULT_GENERATOR_FEATURE_FLAGS;
    static final int DEFAULT_PARSER_FEATURE_FLAGS;
    protected static final ThreadLocal<SoftReference<BufferRecycler>> _recyclerRef;
    protected int _generatorFeatures;
    protected ObjectCodec _objectCodec;
    protected int _parserFeatures;
    protected BytesToNameCanonicalizer _rootByteSymbols;
    protected CharsToNameCanonicalizer _rootCharSymbols;

    static {
        DEFAULT_PARSER_FEATURE_FLAGS = Feature.collectDefaults();
        DEFAULT_GENERATOR_FEATURE_FLAGS = JsonGenerator.Feature.collectDefaults();
        _recyclerRef = new ThreadLocal();
    }

    public JsonFactory() {
        this(null);
    }

    public JsonFactory(ObjectCodec oc) {
        this._rootCharSymbols = CharsToNameCanonicalizer.createRoot();
        this._rootByteSymbols = BytesToNameCanonicalizer.createRoot();
        this._parserFeatures = DEFAULT_PARSER_FEATURE_FLAGS;
        this._generatorFeatures = DEFAULT_GENERATOR_FEATURE_FLAGS;
        this._objectCodec = oc;
    }

    public Version version() {
        return VersionUtil.versionFor(Utf8Generator.class);
    }

    public final JsonFactory configure(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public JsonFactory enable(Feature f) {
        this._parserFeatures |= f.getMask();
        return this;
    }

    public JsonFactory disable(Feature f) {
        this._parserFeatures &= f.getMask() ^ -1;
        return this;
    }

    public final boolean isEnabled(Feature f) {
        return (this._parserFeatures & f.getMask()) != 0;
    }

    public final void enableParserFeature(Feature f) {
        enable(f);
    }

    public final void disableParserFeature(Feature f) {
        disable(f);
    }

    public final void setParserFeature(Feature f, boolean state) {
        configure(f, state);
    }

    public final boolean isParserFeatureEnabled(Feature f) {
        return (this._parserFeatures & f.getMask()) != 0;
    }

    public final JsonFactory configure(JsonGenerator.Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public JsonFactory enable(JsonGenerator.Feature f) {
        this._generatorFeatures |= f.getMask();
        return this;
    }

    public JsonFactory disable(JsonGenerator.Feature f) {
        this._generatorFeatures &= f.getMask() ^ -1;
        return this;
    }

    public final boolean isEnabled(JsonGenerator.Feature f) {
        return (this._generatorFeatures & f.getMask()) != 0;
    }

    @Deprecated
    public final void enableGeneratorFeature(JsonGenerator.Feature f) {
        enable(f);
    }

    @Deprecated
    public final void disableGeneratorFeature(JsonGenerator.Feature f) {
        disable(f);
    }

    @Deprecated
    public final void setGeneratorFeature(JsonGenerator.Feature f, boolean state) {
        configure(f, state);
    }

    @Deprecated
    public final boolean isGeneratorFeatureEnabled(JsonGenerator.Feature f) {
        return isEnabled(f);
    }

    public JsonFactory setCodec(ObjectCodec oc) {
        this._objectCodec = oc;
        return this;
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public JsonParser createJsonParser(File f) throws IOException, JsonParseException {
        return _createJsonParser(new FileInputStream(f), _createContext(f, true));
    }

    public JsonParser createJsonParser(URL url) throws IOException, JsonParseException {
        return _createJsonParser(_optimizedStreamFromURL(url), _createContext(url, true));
    }

    public JsonParser createJsonParser(InputStream in) throws IOException, JsonParseException {
        return _createJsonParser(in, _createContext(in, false));
    }

    public JsonParser createJsonParser(Reader r) throws IOException, JsonParseException {
        return _createJsonParser(r, _createContext(r, false));
    }

    public JsonParser createJsonParser(byte[] data) throws IOException, JsonParseException {
        return _createJsonParser(data, 0, data.length, _createContext(data, true));
    }

    public JsonParser createJsonParser(byte[] data, int offset, int len) throws IOException, JsonParseException {
        return _createJsonParser(data, offset, len, _createContext(data, true));
    }

    public JsonParser createJsonParser(String content) throws IOException, JsonParseException {
        Reader r = new StringReader(content);
        return _createJsonParser(r, _createContext(r, true));
    }

    public JsonGenerator createJsonGenerator(OutputStream out, JsonEncoding enc) throws IOException {
        IOContext ctxt = _createContext(out, false);
        ctxt.setEncoding(enc);
        if (enc == JsonEncoding.UTF8) {
            return _createUTF8JsonGenerator(out, ctxt);
        }
        return _createJsonGenerator(_createWriter(out, enc, ctxt), ctxt);
    }

    public JsonGenerator createJsonGenerator(Writer out) throws IOException {
        return _createJsonGenerator(out, _createContext(out, false));
    }

    public JsonGenerator createJsonGenerator(File f, JsonEncoding enc) throws IOException {
        OutputStream out = new FileOutputStream(f);
        IOContext ctxt = _createContext(out, true);
        ctxt.setEncoding(enc);
        if (enc == JsonEncoding.UTF8) {
            return _createUTF8JsonGenerator(out, ctxt);
        }
        return _createJsonGenerator(_createWriter(out, enc, ctxt), ctxt);
    }

    protected IOContext _createContext(Object srcRef, boolean resourceManaged) {
        return new IOContext(_getBufferRecycler(), srcRef, resourceManaged);
    }

    protected JsonParser _createJsonParser(InputStream in, IOContext ctxt) throws IOException, JsonParseException {
        return new ByteSourceBootstrapper(ctxt, in).constructParser(this._parserFeatures, this._objectCodec, this._rootByteSymbols, this._rootCharSymbols);
    }

    protected JsonParser _createJsonParser(Reader r, IOContext ctxt) throws IOException, JsonParseException {
        return new ReaderBasedParser(ctxt, this._parserFeatures, r, this._objectCodec, this._rootCharSymbols.makeChild(isEnabled(Feature.CANONICALIZE_FIELD_NAMES), isEnabled(Feature.INTERN_FIELD_NAMES)));
    }

    protected JsonParser _createJsonParser(byte[] data, int offset, int len, IOContext ctxt) throws IOException, JsonParseException {
        return new ByteSourceBootstrapper(ctxt, data, offset, len).constructParser(this._parserFeatures, this._objectCodec, this._rootByteSymbols, this._rootCharSymbols);
    }

    protected JsonGenerator _createJsonGenerator(Writer out, IOContext ctxt) throws IOException {
        return new WriterBasedGenerator(ctxt, this._generatorFeatures, this._objectCodec, out);
    }

    protected JsonGenerator _createUTF8JsonGenerator(OutputStream out, IOContext ctxt) throws IOException {
        return new Utf8Generator(ctxt, this._generatorFeatures, this._objectCodec, out);
    }

    public BufferRecycler _getBufferRecycler() {
        SoftReference<BufferRecycler> ref = (SoftReference) _recyclerRef.get();
        BufferRecycler br = ref == null ? null : (BufferRecycler) ref.get();
        if (br != null) {
            return br;
        }
        br = new BufferRecycler();
        _recyclerRef.set(new SoftReference(br));
        return br;
    }

    protected Writer _createWriter(OutputStream out, JsonEncoding enc, IOContext ctxt) throws IOException {
        if (enc == JsonEncoding.UTF8) {
            return new UTF8Writer(ctxt, out);
        }
        return new OutputStreamWriter(out, enc.getJavaName());
    }

    protected InputStream _optimizedStreamFromURL(URL url) throws IOException {
        if (MessageVo.TYPE_FILE.equals(url.getProtocol())) {
            String host = url.getHost();
            if (host == null || host.length() == 0) {
                return new FileInputStream(url.getPath());
            }
        }
        return url.openStream();
    }
}
