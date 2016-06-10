package org.codehaus.jackson.util;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.impl.JsonParserMinimalBase;
import org.codehaus.jackson.impl.JsonReadContext;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.io.SerializedString;

public class TokenBuffer extends JsonGenerator {
    protected static final int DEFAULT_PARSER_FEATURES;
    protected int _appendOffset;
    protected boolean _closed;
    protected Segment _first;
    protected int _generatorFeatures;
    protected Segment _last;
    protected ObjectCodec _objectCodec;
    protected JsonWriteContext _writeContext;

    /* renamed from: org.codehaus.jackson.util.TokenBuffer.1 */
    static /* synthetic */ class C09501 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonParser$NumberType;
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonParser$NumberType = new int[NumberType.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.INT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.BIG_INTEGER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.BIG_DECIMAL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.FLOAT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.LONG.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_OBJECT.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_OBJECT.ordinal()] = 2;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 3;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_ARRAY.ordinal()] = 4;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 5;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 6;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 7;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 8;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 9;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 10;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NULL.ordinal()] = 11;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_EMBEDDED_OBJECT.ordinal()] = 12;
            } catch (NoSuchFieldError e17) {
            }
        }
    }

    protected static final class Segment {
        public static final int TOKENS_PER_SEGMENT = 16;
        private static final JsonToken[] TOKEN_TYPES_BY_INDEX;
        protected Segment _next;
        protected long _tokenTypes;
        protected final Object[] _tokens;

        static {
            TOKEN_TYPES_BY_INDEX = new JsonToken[TOKENS_PER_SEGMENT];
            JsonToken[] t = JsonToken.values();
            System.arraycopy(t, 1, TOKEN_TYPES_BY_INDEX, 1, Math.min(15, t.length - 1));
        }

        public Segment() {
            this._tokens = new Object[TOKENS_PER_SEGMENT];
        }

        public JsonToken type(int index) {
            long l = this._tokenTypes;
            if (index > 0) {
                l >>= index << 2;
            }
            return TOKEN_TYPES_BY_INDEX[((int) l) & 15];
        }

        public Object get(int index) {
            return this._tokens[index];
        }

        public Segment next() {
            return this._next;
        }

        public Segment append(int index, JsonToken tokenType) {
            if (index < TOKENS_PER_SEGMENT) {
                set(index, tokenType);
                return null;
            }
            this._next = new Segment();
            this._next.set(0, tokenType);
            return this._next;
        }

        public Segment append(int index, JsonToken tokenType, Object value) {
            if (index < TOKENS_PER_SEGMENT) {
                set(index, tokenType, value);
                return null;
            }
            this._next = new Segment();
            this._next.set(0, tokenType, value);
            return this._next;
        }

        public void set(int index, JsonToken tokenType) {
            long typeCode = (long) tokenType.ordinal();
            if (index > 0) {
                typeCode <<= index << 2;
            }
            this._tokenTypes |= typeCode;
        }

        public void set(int index, JsonToken tokenType, Object value) {
            this._tokens[index] = value;
            long typeCode = (long) tokenType.ordinal();
            if (index > 0) {
                typeCode <<= index << 2;
            }
            this._tokenTypes |= typeCode;
        }
    }

    protected static final class Parser extends JsonParserMinimalBase {
        protected transient ByteArrayBuilder _byteBuilder;
        protected boolean _closed;
        protected ObjectCodec _codec;
        protected JsonLocation _location;
        protected JsonReadContext _parsingContext;
        protected Segment _segment;
        protected int _segmentPtr;

        public Parser(Segment firstSeg, ObjectCodec codec) {
            super(0);
            this._location = null;
            this._segment = firstSeg;
            this._segmentPtr = -1;
            this._codec = codec;
            this._parsingContext = JsonReadContext.createRootContext(-1, -1);
        }

        public void setLocation(JsonLocation l) {
            this._location = l;
        }

        public ObjectCodec getCodec() {
            return this._codec;
        }

        public void setCodec(ObjectCodec c) {
            this._codec = c;
        }

        public JsonToken peekNextToken() throws IOException, JsonParseException {
            if (this._closed) {
                return null;
            }
            Segment seg = this._segment;
            int ptr = this._segmentPtr + 1;
            if (ptr >= 16) {
                ptr = 0;
                seg = seg == null ? null : seg.next();
            }
            if (seg != null) {
                return seg.type(ptr);
            }
            return null;
        }

        public void close() throws IOException {
            if (!this._closed) {
                this._closed = true;
            }
        }

        public JsonToken nextToken() throws IOException, JsonParseException {
            if (this._closed || this._segment == null) {
                return null;
            }
            int i = this._segmentPtr + 1;
            this._segmentPtr = i;
            if (i >= 16) {
                this._segmentPtr = 0;
                this._segment = this._segment.next();
                if (this._segment == null) {
                    return null;
                }
            }
            this._currToken = this._segment.type(this._segmentPtr);
            if (this._currToken == JsonToken.FIELD_NAME) {
                Object ob = _currentObject();
                this._parsingContext.setCurrentName(ob instanceof String ? (String) ob : ob.toString());
            } else if (this._currToken == JsonToken.START_OBJECT) {
                this._parsingContext = this._parsingContext.createChildObjectContext(-1, -1);
            } else if (this._currToken == JsonToken.START_ARRAY) {
                this._parsingContext = this._parsingContext.createChildArrayContext(-1, -1);
            } else if (this._currToken == JsonToken.END_OBJECT || this._currToken == JsonToken.END_ARRAY) {
                this._parsingContext = this._parsingContext.getParent();
                if (this._parsingContext == null) {
                    this._parsingContext = JsonReadContext.createRootContext(-1, -1);
                }
            }
            return this._currToken;
        }

        public boolean isClosed() {
            return this._closed;
        }

        public JsonStreamContext getParsingContext() {
            return this._parsingContext;
        }

        public JsonLocation getTokenLocation() {
            return getCurrentLocation();
        }

        public JsonLocation getCurrentLocation() {
            return this._location == null ? JsonLocation.NA : this._location;
        }

        public String getCurrentName() {
            return this._parsingContext.getCurrentName();
        }

        public String getText() {
            Object ob;
            if (this._currToken == JsonToken.VALUE_STRING || this._currToken == JsonToken.FIELD_NAME) {
                ob = _currentObject();
                if (ob instanceof String) {
                    return (String) ob;
                }
                if (ob != null) {
                    return ob.toString();
                }
                return null;
            } else if (this._currToken == null) {
                return null;
            } else {
                switch (C09501.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
                    case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                    case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                        ob = _currentObject();
                        if (ob != null) {
                            return ob.toString();
                        }
                        return null;
                    default:
                        return this._currToken.asString();
                }
            }
        }

        public char[] getTextCharacters() {
            String str = getText();
            return str == null ? null : str.toCharArray();
        }

        public int getTextLength() {
            String str = getText();
            return str == null ? 0 : str.length();
        }

        public int getTextOffset() {
            return 0;
        }

        public boolean hasTextCharacters() {
            return false;
        }

        public BigInteger getBigIntegerValue() throws IOException, JsonParseException {
            Number n = getNumberValue();
            if (n instanceof BigInteger) {
                return (BigInteger) n;
            }
            switch (C09501.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[getNumberType().ordinal()]) {
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    return ((BigDecimal) n).toBigInteger();
                default:
                    return BigInteger.valueOf(n.longValue());
            }
        }

        public BigDecimal getDecimalValue() throws IOException, JsonParseException {
            Number n = getNumberValue();
            if (n instanceof BigDecimal) {
                return (BigDecimal) n;
            }
            switch (C09501.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[getNumberType().ordinal()]) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    return BigDecimal.valueOf(n.longValue());
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    return new BigDecimal((BigInteger) n);
                default:
                    return BigDecimal.valueOf(n.doubleValue());
            }
        }

        public double getDoubleValue() throws IOException, JsonParseException {
            return getNumberValue().doubleValue();
        }

        public float getFloatValue() throws IOException, JsonParseException {
            return getNumberValue().floatValue();
        }

        public int getIntValue() throws IOException, JsonParseException {
            if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
                return ((Number) _currentObject()).intValue();
            }
            return getNumberValue().intValue();
        }

        public long getLongValue() throws IOException, JsonParseException {
            return getNumberValue().longValue();
        }

        public NumberType getNumberType() throws IOException, JsonParseException {
            Number n = getNumberValue();
            if (n instanceof Integer) {
                return NumberType.INT;
            }
            if (n instanceof Long) {
                return NumberType.LONG;
            }
            if (n instanceof Double) {
                return NumberType.DOUBLE;
            }
            if (n instanceof BigDecimal) {
                return NumberType.BIG_DECIMAL;
            }
            if (n instanceof Float) {
                return NumberType.FLOAT;
            }
            if (n instanceof BigInteger) {
                return NumberType.BIG_INTEGER;
            }
            return null;
        }

        public final Number getNumberValue() throws IOException, JsonParseException {
            _checkIsNumber();
            return (Number) _currentObject();
        }

        public Object getEmbeddedObject() {
            if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
                return _currentObject();
            }
            return null;
        }

        public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException {
            if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
                Object ob = _currentObject();
                if (ob instanceof byte[]) {
                    return (byte[]) ob;
                }
            }
            if (this._currToken != JsonToken.VALUE_STRING) {
                throw _constructError("Current token (" + this._currToken + ") not VALUE_STRING (or VALUE_EMBEDDED_OBJECT with byte[]), can not access as binary");
            }
            String str = getText();
            if (str == null) {
                return null;
            }
            ByteArrayBuilder builder = this._byteBuilder;
            if (builder == null) {
                builder = new ByteArrayBuilder(100);
                this._byteBuilder = builder;
            }
            _decodeBase64(str, builder, b64variant);
            return builder.toByteArray();
        }

        protected void _decodeBase64(String str, ByteArrayBuilder builder, Base64Variant b64variant) throws IOException, JsonParseException {
            int ptr = 0;
            int len = str.length();
            while (ptr < len) {
                int ptr2;
                char ch;
                while (true) {
                    ptr2 = ptr + 1;
                    ch = str.charAt(ptr);
                    if (ptr2 >= len) {
                        ptr = ptr2;
                        return;
                    } else if (ch > ' ') {
                        break;
                    } else {
                        ptr = ptr2;
                    }
                }
                int bits = b64variant.decodeBase64Char(ch);
                if (bits < 0) {
                    _reportInvalidBase64(b64variant, ch, 0, null);
                }
                int decodedData = bits;
                if (ptr2 >= len) {
                    _reportBase64EOF();
                }
                ptr = ptr2 + 1;
                ch = str.charAt(ptr2);
                bits = b64variant.decodeBase64Char(ch);
                if (bits < 0) {
                    _reportInvalidBase64(b64variant, ch, 1, null);
                }
                decodedData = (decodedData << 6) | bits;
                if (ptr >= len) {
                    _reportBase64EOF();
                }
                ptr2 = ptr + 1;
                ch = str.charAt(ptr);
                bits = b64variant.decodeBase64Char(ch);
                if (bits < 0) {
                    if (bits != -2) {
                        _reportInvalidBase64(b64variant, ch, 2, null);
                    }
                    if (ptr2 >= len) {
                        _reportBase64EOF();
                    }
                    ptr = ptr2 + 1;
                    ch = str.charAt(ptr2);
                    if (!b64variant.usesPaddingChar(ch)) {
                        _reportInvalidBase64(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
                    }
                    builder.append(decodedData >> 4);
                } else {
                    decodedData = (decodedData << 6) | bits;
                    if (ptr2 >= len) {
                        _reportBase64EOF();
                    }
                    ptr = ptr2 + 1;
                    ch = str.charAt(ptr2);
                    bits = b64variant.decodeBase64Char(ch);
                    if (bits < 0) {
                        if (bits != -2) {
                            _reportInvalidBase64(b64variant, ch, 3, null);
                        }
                        builder.appendTwoBytes(decodedData >> 2);
                    } else {
                        builder.appendThreeBytes((decodedData << 6) | bits);
                    }
                }
            }
        }

        protected final Object _currentObject() {
            return this._segment.get(this._segmentPtr);
        }

        protected final void _checkIsNumber() throws JsonParseException {
            if (this._currToken == null || !this._currToken.isNumeric()) {
                throw _constructError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
            }
        }

        protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex, String msg) throws JsonParseException {
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
            throw _constructError(base);
        }

        protected void _reportBase64EOF() throws JsonParseException {
            throw _constructError("Unexpected end-of-String in base64 content");
        }

        protected void _handleEOF() throws JsonParseException {
            _throwInternal();
        }
    }

    static {
        DEFAULT_PARSER_FEATURES = Feature.collectDefaults();
    }

    public TokenBuffer(ObjectCodec codec) {
        this._objectCodec = codec;
        this._generatorFeatures = DEFAULT_PARSER_FEATURES;
        this._writeContext = JsonWriteContext.createRootContext();
        Segment segment = new Segment();
        this._last = segment;
        this._first = segment;
        this._appendOffset = 0;
    }

    public JsonParser asParser() {
        return asParser(this._objectCodec);
    }

    public JsonParser asParser(ObjectCodec codec) {
        return new Parser(this._first, codec);
    }

    public JsonParser asParser(JsonParser src) {
        Parser p = new Parser(this._first, src.getCodec());
        p.setLocation(src.getTokenLocation());
        return p;
    }

    public void serialize(JsonGenerator jgen) throws IOException, JsonGenerationException {
        Segment segment = this._first;
        int ptr = -1;
        while (true) {
            ptr++;
            if (ptr >= 16) {
                ptr = 0;
                segment = segment.next();
                if (segment == null) {
                    return;
                }
            }
            JsonToken t = segment.type(ptr);
            if (t != null) {
                Object ob;
                switch (C09501.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        jgen.writeStartObject();
                        break;
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        jgen.writeEndObject();
                        break;
                    case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                        jgen.writeStartArray();
                        break;
                    case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                        jgen.writeEndArray();
                        break;
                    case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                        ob = segment.get(ptr);
                        if (!(ob instanceof SerializableString)) {
                            jgen.writeFieldName((String) ob);
                            break;
                        } else {
                            jgen.writeFieldName((SerializableString) ob);
                            break;
                        }
                    case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                        ob = segment.get(ptr);
                        if (!(ob instanceof SerializableString)) {
                            jgen.writeString((String) ob);
                            break;
                        } else {
                            jgen.writeString((SerializableString) ob);
                            break;
                        }
                    case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                        Number n = (Number) segment.get(ptr);
                        if (!(n instanceof BigInteger)) {
                            if (!(n instanceof Long)) {
                                jgen.writeNumber(n.intValue());
                                break;
                            } else {
                                jgen.writeNumber(n.longValue());
                                break;
                            }
                        }
                        jgen.writeNumber((BigInteger) n);
                        break;
                    case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                        Object n2 = segment.get(ptr);
                        if (n2 instanceof BigDecimal) {
                            jgen.writeNumber((BigDecimal) n2);
                            break;
                        } else if (n2 instanceof Float) {
                            jgen.writeNumber(((Float) n2).floatValue());
                            break;
                        } else if (n2 instanceof Double) {
                            jgen.writeNumber(((Double) n2).doubleValue());
                            break;
                        } else if (n2 == null) {
                            jgen.writeNull();
                            break;
                        } else if (n2 instanceof String) {
                            jgen.writeNumber((String) n2);
                            break;
                        } else {
                            throw new JsonGenerationException("Unrecognized value type for VALUE_NUMBER_FLOAT: " + n2.getClass().getName() + ", can not serialize");
                        }
                    case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                        jgen.writeBoolean(true);
                        break;
                    case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                        jgen.writeBoolean(false);
                        break;
                    case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                        jgen.writeNull();
                        break;
                    case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                        jgen.writeObject(segment.get(ptr));
                        break;
                    default:
                        throw new RuntimeException("Internal error: should never end up through this code path");
                }
            }
            return;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[TokenBuffer: ");
        JsonParser jp = asParser();
        int count = 0;
        while (true) {
            try {
                JsonToken t = jp.nextToken();
                if (t == null) {
                    break;
                }
                if (count < 100) {
                    if (count > 0) {
                        sb.append(", ");
                    }
                    sb.append(t.toString());
                }
                count++;
            } catch (IOException ioe) {
                throw new IllegalStateException(ioe);
            }
        }
        if (count >= 100) {
            sb.append(" ... (truncated ").append(count - 100).append(" entries)");
        }
        sb.append(']');
        return sb.toString();
    }

    public JsonGenerator enable(JsonGenerator.Feature f) {
        this._generatorFeatures |= f.getMask();
        return this;
    }

    public JsonGenerator disable(JsonGenerator.Feature f) {
        this._generatorFeatures &= f.getMask() ^ -1;
        return this;
    }

    public boolean isEnabled(JsonGenerator.Feature f) {
        return (this._generatorFeatures & f.getMask()) != 0;
    }

    public JsonGenerator useDefaultPrettyPrinter() {
        return this;
    }

    public JsonGenerator setCodec(ObjectCodec oc) {
        this._objectCodec = oc;
        return this;
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public final JsonWriteContext getOutputContext() {
        return this._writeContext;
    }

    public void flush() throws IOException {
    }

    public void close() throws IOException {
        this._closed = true;
    }

    public boolean isClosed() {
        return this._closed;
    }

    public final void writeStartArray() throws IOException, JsonGenerationException {
        _append(JsonToken.START_ARRAY);
        this._writeContext = this._writeContext.createChildArrayContext();
    }

    public final void writeEndArray() throws IOException, JsonGenerationException {
        _append(JsonToken.END_ARRAY);
        JsonWriteContext c = this._writeContext.getParent();
        if (c != null) {
            this._writeContext = c;
        }
    }

    public final void writeStartObject() throws IOException, JsonGenerationException {
        _append(JsonToken.START_OBJECT);
        this._writeContext = this._writeContext.createChildObjectContext();
    }

    public final void writeEndObject() throws IOException, JsonGenerationException {
        _append(JsonToken.END_OBJECT);
        JsonWriteContext c = this._writeContext.getParent();
        if (c != null) {
            this._writeContext = c;
        }
    }

    public final void writeFieldName(String name) throws IOException, JsonGenerationException {
        _append(JsonToken.FIELD_NAME, name);
        this._writeContext.writeFieldName(name);
    }

    public void writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        _append(JsonToken.FIELD_NAME, name);
        this._writeContext.writeFieldName(name.getValue());
    }

    public void writeFieldName(SerializedString name) throws IOException, JsonGenerationException {
        _append(JsonToken.FIELD_NAME, name);
        this._writeContext.writeFieldName(name.getValue());
    }

    public void writeString(String text) throws IOException, JsonGenerationException {
        if (text == null) {
            writeNull();
        } else {
            _append(JsonToken.VALUE_STRING, text);
        }
    }

    public void writeString(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        writeString(new String(text, offset, len));
    }

    public void writeString(SerializableString text) throws IOException, JsonGenerationException {
        if (text == null) {
            writeNull();
        } else {
            _append(JsonToken.VALUE_STRING, text);
        }
    }

    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRaw(String text) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRaw(String text, int offset, int len) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRaw(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRaw(char c) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRawValue(String text) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRawValue(String text, int offset, int len) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRawValue(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeNumber(int i) throws IOException, JsonGenerationException {
        _append(JsonToken.VALUE_NUMBER_INT, Integer.valueOf(i));
    }

    public void writeNumber(long l) throws IOException, JsonGenerationException {
        _append(JsonToken.VALUE_NUMBER_INT, Long.valueOf(l));
    }

    public void writeNumber(double d) throws IOException, JsonGenerationException {
        _append(JsonToken.VALUE_NUMBER_FLOAT, Double.valueOf(d));
    }

    public void writeNumber(float f) throws IOException, JsonGenerationException {
        _append(JsonToken.VALUE_NUMBER_FLOAT, Float.valueOf(f));
    }

    public void writeNumber(BigDecimal dec) throws IOException, JsonGenerationException {
        if (dec == null) {
            writeNull();
        } else {
            _append(JsonToken.VALUE_NUMBER_FLOAT, dec);
        }
    }

    public void writeNumber(BigInteger v) throws IOException, JsonGenerationException {
        if (v == null) {
            writeNull();
        } else {
            _append(JsonToken.VALUE_NUMBER_INT, v);
        }
    }

    public void writeNumber(String encodedValue) throws IOException, JsonGenerationException {
        _append(JsonToken.VALUE_NUMBER_FLOAT, encodedValue);
    }

    public void writeBoolean(boolean state) throws IOException, JsonGenerationException {
        _append(state ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE);
    }

    public void writeNull() throws IOException, JsonGenerationException {
        _append(JsonToken.VALUE_NULL);
    }

    public void writeObject(Object value) throws IOException, JsonProcessingException {
        _append(JsonToken.VALUE_EMBEDDED_OBJECT, value);
    }

    public void writeTree(JsonNode rootNode) throws IOException, JsonProcessingException {
        _append(JsonToken.VALUE_EMBEDDED_OBJECT, rootNode);
    }

    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException, JsonGenerationException {
        byte[] copy = new byte[len];
        System.arraycopy(data, offset, copy, 0, len);
        writeObject(copy);
    }

    public void copyCurrentEvent(JsonParser jp) throws IOException, JsonProcessingException {
        switch (C09501.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                writeStartObject();
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                writeEndObject();
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                writeStartArray();
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                writeEndArray();
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                writeFieldName(jp.getCurrentName());
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                if (jp.hasTextCharacters()) {
                    writeString(jp.getTextCharacters(), jp.getTextOffset(), jp.getTextLength());
                } else {
                    writeString(jp.getText());
                }
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                switch (C09501.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        writeNumber(jp.getIntValue());
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        writeNumber(jp.getBigIntegerValue());
                    default:
                        writeNumber(jp.getLongValue());
                }
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                switch (C09501.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
                    case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                        writeNumber(jp.getDecimalValue());
                    case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                        writeNumber(jp.getFloatValue());
                    default:
                        writeNumber(jp.getDoubleValue());
                }
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                writeBoolean(true);
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                writeBoolean(false);
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                writeNull();
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                writeObject(jp.getEmbeddedObject());
            default:
                throw new RuntimeException("Internal error: should never end up through this code path");
        }
    }

    public void copyCurrentStructure(JsonParser jp) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.FIELD_NAME) {
            writeFieldName(jp.getCurrentName());
            t = jp.nextToken();
        }
        switch (C09501.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                writeStartObject();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    copyCurrentStructure(jp);
                }
                writeEndObject();
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                writeStartArray();
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    copyCurrentStructure(jp);
                }
                writeEndArray();
            default:
                copyCurrentEvent(jp);
        }
    }

    protected final void _append(JsonToken type) {
        Segment next = this._last.append(this._appendOffset, type);
        if (next == null) {
            this._appendOffset++;
            return;
        }
        this._last = next;
        this._appendOffset = 1;
    }

    protected final void _append(JsonToken type, Object value) {
        Segment next = this._last.append(this._appendOffset, type, value);
        if (next == null) {
            this._appendOffset++;
            return;
        }
        this._last = next;
        this._appendOffset = 1;
    }

    protected void _reportUnsupportedOperation() {
        throw new UnsupportedOperationException("Called operation not supported for TokenBuffer");
    }
}
