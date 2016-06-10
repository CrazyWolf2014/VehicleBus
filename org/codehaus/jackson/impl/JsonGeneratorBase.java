package org.codehaus.jackson.impl;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.util.VersionUtil;

public abstract class JsonGeneratorBase extends JsonGenerator {
    protected boolean _cfgNumbersAsStrings;
    protected boolean _closed;
    protected int _features;
    protected ObjectCodec _objectCodec;
    protected JsonWriteContext _writeContext;

    /* renamed from: org.codehaus.jackson.impl.JsonGeneratorBase.1 */
    static /* synthetic */ class C09331 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonParser$NumberType;
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_OBJECT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_OBJECT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_ARRAY.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NULL.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_EMBEDDED_OBJECT.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            $SwitchMap$org$codehaus$jackson$JsonParser$NumberType = new int[NumberType.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.INT.ordinal()] = 1;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.BIG_INTEGER.ordinal()] = 2;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.BIG_DECIMAL.ordinal()] = 3;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.FLOAT.ordinal()] = 4;
            } catch (NoSuchFieldError e16) {
            }
        }
    }

    protected abstract void _releaseBuffers();

    protected abstract void _verifyValueWrite(String str) throws IOException, JsonGenerationException;

    public abstract void flush() throws IOException;

    protected JsonGeneratorBase(int features, ObjectCodec codec) {
        this._features = features;
        this._writeContext = JsonWriteContext.createRootContext();
        this._objectCodec = codec;
        this._cfgNumbersAsStrings = isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS);
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public JsonGenerator enable(Feature f) {
        this._features |= f.getMask();
        if (f == Feature.WRITE_NUMBERS_AS_STRINGS) {
            this._cfgNumbersAsStrings = true;
        }
        return this;
    }

    public JsonGenerator disable(Feature f) {
        this._features &= f.getMask() ^ -1;
        if (f == Feature.WRITE_NUMBERS_AS_STRINGS) {
            this._cfgNumbersAsStrings = false;
        }
        return this;
    }

    public final boolean isEnabled(Feature f) {
        return (this._features & f.getMask()) != 0;
    }

    public JsonGenerator useDefaultPrettyPrinter() {
        return setPrettyPrinter(new DefaultPrettyPrinter());
    }

    public JsonGenerator setCodec(ObjectCodec oc) {
        this._objectCodec = oc;
        return this;
    }

    public final ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public final JsonWriteContext getOutputContext() {
        return this._writeContext;
    }

    public void writeStartArray() throws IOException, JsonGenerationException {
        _verifyValueWrite("start an array");
        this._writeContext = this._writeContext.createChildArrayContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartArray(this);
        } else {
            _writeStartArray();
        }
    }

    @Deprecated
    protected void _writeStartArray() throws IOException, JsonGenerationException {
    }

    public void writeEndArray() throws IOException, JsonGenerationException {
        if (!this._writeContext.inArray()) {
            _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        } else {
            _writeEndArray();
        }
        this._writeContext = this._writeContext.getParent();
    }

    @Deprecated
    protected void _writeEndArray() throws IOException, JsonGenerationException {
    }

    public void writeStartObject() throws IOException, JsonGenerationException {
        _verifyValueWrite("start an object");
        this._writeContext = this._writeContext.createChildObjectContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartObject(this);
        } else {
            _writeStartObject();
        }
    }

    @Deprecated
    protected void _writeStartObject() throws IOException, JsonGenerationException {
    }

    public void writeEndObject() throws IOException, JsonGenerationException {
        if (!this._writeContext.inObject()) {
            _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
        }
        this._writeContext = this._writeContext.getParent();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
        } else {
            _writeEndObject();
        }
    }

    @Deprecated
    protected void _writeEndObject() throws IOException, JsonGenerationException {
    }

    public void writeRawValue(String text) throws IOException, JsonGenerationException {
        _verifyValueWrite("write raw value");
        writeRaw(text);
    }

    public void writeRawValue(String text, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write raw value");
        writeRaw(text, offset, len);
    }

    public void writeRawValue(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        _verifyValueWrite("write raw value");
        writeRaw(text, offset, len);
    }

    public void writeObject(Object value) throws IOException, JsonProcessingException {
        if (value == null) {
            writeNull();
        } else if (this._objectCodec != null) {
            this._objectCodec.writeValue(this, value);
        } else {
            _writeSimpleObject(value);
        }
    }

    public void writeTree(JsonNode rootNode) throws IOException, JsonProcessingException {
        if (rootNode == null) {
            writeNull();
        } else if (this._objectCodec == null) {
            throw new IllegalStateException("No ObjectCodec defined for the generator, can not serialize JsonNode-based trees");
        } else {
            this._objectCodec.writeTree(this, rootNode);
        }
    }

    public void close() throws IOException {
        this._closed = true;
    }

    public boolean isClosed() {
        return this._closed;
    }

    public final void copyCurrentEvent(JsonParser jp) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == null) {
            _reportError("No current event to copy");
        }
        switch (C09331.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
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
                switch (C09331.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        writeNumber(jp.getIntValue());
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        writeNumber(jp.getBigIntegerValue());
                    default:
                        writeNumber(jp.getLongValue());
                }
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                switch (C09331.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
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
                _cantHappen();
        }
    }

    public final void copyCurrentStructure(JsonParser jp) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.FIELD_NAME) {
            writeFieldName(jp.getCurrentName());
            t = jp.nextToken();
        }
        switch (C09331.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
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

    protected void _reportError(String msg) throws JsonGenerationException {
        throw new JsonGenerationException(msg);
    }

    protected void _cantHappen() {
        throw new RuntimeException("Internal error: should never end up through this code path");
    }

    protected void _writeSimpleObject(Object value) throws IOException, JsonGenerationException {
        if (value == null) {
            writeNull();
        } else if (value instanceof String) {
            writeString((String) value);
        } else {
            if (value instanceof Number) {
                Number n = (Number) value;
                if (n instanceof Integer) {
                    writeNumber(n.intValue());
                    return;
                } else if (n instanceof Long) {
                    writeNumber(n.longValue());
                    return;
                } else if (n instanceof Double) {
                    writeNumber(n.doubleValue());
                    return;
                } else if (n instanceof Float) {
                    writeNumber(n.floatValue());
                    return;
                } else if (n instanceof Short) {
                    writeNumber((int) n.shortValue());
                    return;
                } else if (n instanceof Byte) {
                    writeNumber((int) n.byteValue());
                    return;
                } else if (n instanceof BigInteger) {
                    writeNumber((BigInteger) n);
                    return;
                } else if (n instanceof BigDecimal) {
                    writeNumber((BigDecimal) n);
                    return;
                } else if (n instanceof AtomicInteger) {
                    writeNumber(((AtomicInteger) n).get());
                    return;
                } else if (n instanceof AtomicLong) {
                    writeNumber(((AtomicLong) n).get());
                    return;
                }
            } else if (value instanceof byte[]) {
                writeBinary((byte[]) value);
                return;
            } else if (value instanceof Boolean) {
                writeBoolean(((Boolean) value).booleanValue());
                return;
            } else if (value instanceof AtomicBoolean) {
                writeBoolean(((AtomicBoolean) value).get());
                return;
            }
            throw new IllegalStateException("No ObjectCodec defined for the generator, can only serialize simple wrapper types (type passed " + value.getClass().getName() + ")");
        }
    }

    protected final void _throwInternal() {
        throw new RuntimeException("Internal error: this code path should never get executed");
    }

    protected void _reportUnsupportedOperation() {
        throw new UnsupportedOperationException("Operation not supported by generator of type " + getClass().getName());
    }
}
