package org.codehaus.jackson.map.deser;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.io.IOException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

/* compiled from: JsonNodeDeserializer */
abstract class BaseNodeDeserializer<N extends JsonNode> extends StdDeserializer<N> {

    /* renamed from: org.codehaus.jackson.map.deser.BaseNodeDeserializer.1 */
    static /* synthetic */ class JsonNodeDeserializer {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_OBJECT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NULL.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_OBJECT.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_ARRAY.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    public BaseNodeDeserializer(Class<N> nodeClass) {
        super((Class) nodeClass);
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
    }

    protected void _reportProblem(JsonParser jp, String msg) throws JsonMappingException {
        throw new JsonMappingException(msg, jp.getTokenLocation());
    }

    protected void _handleDuplicateField(String fieldName, ObjectNode objectNode, JsonNode oldValue, JsonNode newValue) throws JsonProcessingException {
    }

    protected final ObjectNode deserializeObject(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectNode node = ctxt.getNodeFactory().objectNode();
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        while (t == JsonToken.FIELD_NAME) {
            String fieldName = jp.getCurrentName();
            jp.nextToken();
            JsonNode value = deserializeAny(jp, ctxt);
            JsonNode old = node.put(fieldName, value);
            if (old != null) {
                _handleDuplicateField(fieldName, node, old, value);
            }
            t = jp.nextToken();
        }
        return node;
    }

    protected final ArrayNode deserializeArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ArrayNode node = ctxt.getNodeFactory().arrayNode();
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            node.add(deserializeAny(jp, ctxt));
        }
        return node;
    }

    protected final JsonNode deserializeAny(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNodeFactory nodeFactory = ctxt.getNodeFactory();
        switch (JsonNodeDeserializer.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return deserializeObject(jp, ctxt);
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return deserializeArray(jp, ctxt);
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return nodeFactory.textNode(jp.getText());
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                NumberType nt = jp.getNumberType();
                if (nt == NumberType.BIG_INTEGER || ctxt.isEnabled(Feature.USE_BIG_INTEGER_FOR_INTS)) {
                    return nodeFactory.numberNode(jp.getBigIntegerValue());
                }
                if (nt == NumberType.INT) {
                    return nodeFactory.numberNode(jp.getIntValue());
                }
                return nodeFactory.numberNode(jp.getLongValue());
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                if (jp.getNumberType() == NumberType.BIG_DECIMAL || ctxt.isEnabled(Feature.USE_BIG_DECIMAL_FOR_FLOATS)) {
                    return nodeFactory.numberNode(jp.getDecimalValue());
                }
                return nodeFactory.numberNode(jp.getDoubleValue());
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                return nodeFactory.booleanNode(true);
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return nodeFactory.booleanNode(false);
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                return nodeFactory.nullNode();
            default:
                throw ctxt.mappingException(getValueClass());
        }
    }
}
