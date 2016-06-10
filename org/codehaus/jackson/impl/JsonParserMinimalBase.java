package org.codehaus.jackson.impl;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.io.IOException;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberInput;
import org.xbill.DNS.KEYRecord;

public abstract class JsonParserMinimalBase extends JsonParser {
    protected static final int INT_APOSTROPHE = 39;
    protected static final int INT_ASTERISK = 42;
    protected static final int INT_BACKSLASH = 92;
    protected static final int INT_COLON = 58;
    protected static final int INT_COMMA = 44;
    protected static final int INT_CR = 13;
    protected static final int INT_LBRACKET = 91;
    protected static final int INT_LCURLY = 123;
    protected static final int INT_LF = 10;
    protected static final int INT_QUOTE = 34;
    protected static final int INT_RBRACKET = 93;
    protected static final int INT_RCURLY = 125;
    protected static final int INT_SLASH = 47;
    protected static final int INT_SPACE = 32;
    protected static final int INT_TAB = 9;
    protected static final int INT_b = 98;
    protected static final int INT_f = 102;
    protected static final int INT_n = 110;
    protected static final int INT_r = 114;
    protected static final int INT_t = 116;
    protected static final int INT_u = 117;

    /* renamed from: org.codehaus.jackson.impl.JsonParserMinimalBase.1 */
    static /* synthetic */ class C09351 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_OBJECT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_OBJECT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_ARRAY.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NULL.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_EMBEDDED_OBJECT.ordinal()] = JsonParserMinimalBase.INT_TAB;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = JsonParserMinimalBase.INT_LF;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    protected abstract void _handleEOF() throws JsonParseException;

    public abstract void close() throws IOException;

    public abstract byte[] getBinaryValue(Base64Variant base64Variant) throws IOException, JsonParseException;

    public abstract String getCurrentName() throws IOException, JsonParseException;

    public abstract JsonStreamContext getParsingContext();

    public abstract String getText() throws IOException, JsonParseException;

    public abstract char[] getTextCharacters() throws IOException, JsonParseException;

    public abstract int getTextLength() throws IOException, JsonParseException;

    public abstract int getTextOffset() throws IOException, JsonParseException;

    public abstract boolean hasTextCharacters();

    public abstract boolean isClosed();

    public abstract JsonToken nextToken() throws IOException, JsonParseException;

    protected JsonParserMinimalBase() {
    }

    protected JsonParserMinimalBase(int features) {
        super(features);
    }

    public JsonParser skipChildren() throws IOException, JsonParseException {
        if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
            int open = 1;
            while (true) {
                JsonToken t = nextToken();
                if (t == null) {
                    _handleEOF();
                } else {
                    switch (C09351.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
                        case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                            open++;
                            continue;
                        case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                        case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                            open--;
                            if (open == 0) {
                                break;
                            }
                            continue;
                        default:
                            continue;
                    }
                }
            }
        }
        return this;
    }

    public boolean getValueAsBoolean(boolean defaultValue) throws IOException, JsonParseException {
        if (this._currToken != null) {
            switch (C09351.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    if (getIntValue() == 0) {
                        return false;
                    }
                    return true;
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    return true;
                case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    return false;
                case INT_TAB /*9*/:
                    Object value = getEmbeddedObject();
                    if (value instanceof Boolean) {
                        return ((Boolean) value).booleanValue();
                    }
                    break;
                case INT_LF /*10*/:
                    break;
            }
            if ("true".equals(getText().trim())) {
                return true;
            }
        }
        return defaultValue;
    }

    public int getValueAsInt(int defaultValue) throws IOException, JsonParseException {
        if (this._currToken == null) {
            return defaultValue;
        }
        switch (C09351.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                return getIntValue();
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return 1;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return 0;
            case INT_TAB /*9*/:
                Object value = getEmbeddedObject();
                if (value instanceof Number) {
                    return ((Number) value).intValue();
                }
                return defaultValue;
            case INT_LF /*10*/:
                return NumberInput.parseAsInt(getText(), defaultValue);
            default:
                return defaultValue;
        }
    }

    public long getValueAsLong(long defaultValue) throws IOException, JsonParseException {
        if (this._currToken == null) {
            return defaultValue;
        }
        switch (C09351.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                return getLongValue();
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return 1;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return 0;
            case INT_TAB /*9*/:
                Object value = getEmbeddedObject();
                if (value instanceof Number) {
                    return ((Number) value).longValue();
                }
                return defaultValue;
            case INT_LF /*10*/:
                return NumberInput.parseAsLong(getText(), defaultValue);
            default:
                return defaultValue;
        }
    }

    public double getValueAsDouble(double defaultValue) throws IOException, JsonParseException {
        if (this._currToken == null) {
            return defaultValue;
        }
        switch (C09351.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                return getDoubleValue();
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return 1.0d;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return 0.0d;
            case INT_TAB /*9*/:
                Object value = getEmbeddedObject();
                if (value instanceof Number) {
                    return ((Number) value).doubleValue();
                }
                return defaultValue;
            case INT_LF /*10*/:
                return NumberInput.parseAsDouble(getText(), defaultValue);
            default:
                return defaultValue;
        }
    }

    protected void _reportUnexpectedChar(int ch, String comment) throws JsonParseException {
        String msg = "Unexpected character (" + _getCharDesc(ch) + ")";
        if (comment != null) {
            msg = msg + ": " + comment;
        }
        _reportError(msg);
    }

    protected void _reportInvalidEOF() throws JsonParseException {
        _reportInvalidEOF(" in " + this._currToken);
    }

    protected void _reportInvalidEOF(String msg) throws JsonParseException {
        _reportError("Unexpected end-of-input" + msg);
    }

    protected void _throwInvalidSpace(int i) throws JsonParseException {
        _reportError("Illegal character (" + _getCharDesc((char) i) + "): only regular white space (\\r, \\n, \\t) is allowed between tokens");
    }

    protected void _throwUnquotedSpace(int i, String ctxtDesc) throws JsonParseException {
        if (!isEnabled(Feature.ALLOW_UNQUOTED_CONTROL_CHARS) || i >= INT_SPACE) {
            _reportError("Illegal unquoted character (" + _getCharDesc((char) i) + "): has to be escaped using backslash to be included in " + ctxtDesc);
        }
    }

    protected char _handleUnrecognizedCharacterEscape(char ch) throws JsonProcessingException {
        if (!isEnabled(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)) {
            _reportError("Unrecognized character escape " + _getCharDesc(ch));
        }
        return ch;
    }

    protected static final String _getCharDesc(int ch) {
        char c = (char) ch;
        if (Character.isISOControl(c)) {
            return "(CTRL-CHAR, code " + ch + ")";
        }
        if (ch > KEYRecord.PROTOCOL_ANY) {
            return "'" + c + "' (code " + ch + " / 0x" + Integer.toHexString(ch) + ")";
        }
        return "'" + c + "' (code " + ch + ")";
    }

    protected final void _reportError(String msg) throws JsonParseException {
        throw _constructError(msg);
    }

    protected final void _wrapError(String msg, Throwable t) throws JsonParseException {
        throw _constructError(msg, t);
    }

    protected final void _throwInternal() {
        throw new RuntimeException("Internal error: this code path should never get executed");
    }

    protected final JsonParseException _constructError(String msg, Throwable t) {
        return new JsonParseException(msg, getCurrentLocation(), t);
    }
}
