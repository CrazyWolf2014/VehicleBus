package org.codehaus.jackson.map;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.map.deser.StdDeserializationContext;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.VersionUtil;

public class ObjectReader implements Versioned {
    private static final JavaType JSON_NODE_TYPE;
    protected final DeserializationConfig _config;
    protected TypeResolverBuilder<?> _defaultTyper;
    protected final JsonFactory _jsonFactory;
    protected final DeserializerProvider _provider;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
    protected final SubtypeResolver _subtypeResolver;
    protected final Object _valueToUpdate;
    protected final JavaType _valueType;
    protected VisibilityChecker<?> _visibilityChecker;

    static {
        JSON_NODE_TYPE = TypeFactory.type((Type) JsonNode.class);
    }

    protected ObjectReader(ObjectMapper mapper, JavaType valueType, Object valueToUpdate) {
        this._rootDeserializers = mapper._rootDeserializers;
        this._defaultTyper = mapper._defaultTyper;
        this._visibilityChecker = mapper._visibilityChecker;
        this._subtypeResolver = mapper._subtypeResolver;
        this._provider = mapper._deserializerProvider;
        this._jsonFactory = mapper._jsonFactory;
        this._config = mapper._deserializationConfig.createUnshared(this._defaultTyper, this._visibilityChecker, this._subtypeResolver);
        this._valueType = valueType;
        this._valueToUpdate = valueToUpdate;
        if (valueToUpdate != null && valueType.isArrayType()) {
            throw new IllegalArgumentException("Can not update an array value");
        }
    }

    protected ObjectReader(ObjectReader base, DeserializationConfig config, JavaType valueType, Object valueToUpdate) {
        this._rootDeserializers = base._rootDeserializers;
        this._defaultTyper = base._defaultTyper;
        this._visibilityChecker = base._visibilityChecker;
        this._provider = base._provider;
        this._jsonFactory = base._jsonFactory;
        this._subtypeResolver = base._subtypeResolver;
        this._config = config;
        this._valueType = valueType;
        this._valueToUpdate = valueToUpdate;
        if (valueToUpdate != null && valueType.isArrayType()) {
            throw new IllegalArgumentException("Can not update an array value");
        }
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public ObjectReader withType(JavaType valueType) {
        return valueType == this._valueType ? this : new ObjectReader(this, this._config, valueType, this._valueToUpdate);
    }

    public ObjectReader withType(Class<?> valueType) {
        return withType(TypeFactory.type((Type) valueType));
    }

    public ObjectReader withType(Type valueType) {
        return withType(TypeFactory.type(valueType));
    }

    public ObjectReader withNodeFactory(JsonNodeFactory f) {
        return f == this._config.getNodeFactory() ? this : new ObjectReader(this, this._config.createUnshared(f), this._valueType, this._valueToUpdate);
    }

    public ObjectReader withValueToUpdate(Object value) {
        if (value == this._valueToUpdate) {
            return this;
        }
        if (value == null) {
            throw new IllegalArgumentException("cat not update null value");
        }
        return new ObjectReader(this, this._config, TypeFactory.type(value.getClass()), value);
    }

    public <T> T readValue(JsonParser jp) throws IOException, JsonProcessingException {
        return _bind(jp);
    }

    public JsonNode readTree(JsonParser jp) throws IOException, JsonProcessingException {
        return _bindAsTree(jp);
    }

    public <T> T readValue(InputStream src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(Reader src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(String src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(byte[] src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(byte[] src, int offset, int length) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src, offset, length));
    }

    public <T> T readValue(File src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(URL src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(JsonNode src) throws IOException, JsonProcessingException {
        return _bindAndClose(src.traverse());
    }

    public JsonNode readTree(InputStream in) throws IOException, JsonProcessingException {
        return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(in));
    }

    public JsonNode readTree(Reader r) throws IOException, JsonProcessingException {
        return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(r));
    }

    public JsonNode readTree(String content) throws IOException, JsonProcessingException {
        return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(content));
    }

    protected Object _bind(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        Object result;
        JsonToken t = _initForReading(jp);
        if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            result = this._valueToUpdate;
        } else {
            DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
            if (this._valueToUpdate == null) {
                result = _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt);
            } else {
                _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt, this._valueToUpdate);
                result = this._valueToUpdate;
            }
        }
        jp.clearCurrentToken();
        return result;
    }

    protected Object _bindAndClose(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        try {
            Object result;
            JsonToken t = _initForReading(jp);
            if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
                result = this._valueToUpdate;
            } else {
                DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
                if (this._valueToUpdate == null) {
                    result = _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt);
                } else {
                    _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt, this._valueToUpdate);
                    result = this._valueToUpdate;
                }
            }
            return result;
        } finally {
            try {
                jp.close();
            } catch (IOException e) {
            }
        }
    }

    protected JsonNode _bindAsTree(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        JsonNode result;
        JsonToken t = _initForReading(jp);
        if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            result = NullNode.instance;
        } else {
            result = (JsonNode) _findRootDeserializer(this._config, JSON_NODE_TYPE).deserialize(jp, _createDeserializationContext(jp, this._config));
        }
        jp.clearCurrentToken();
        return result;
    }

    protected JsonNode _bindAndCloseAsTree(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        try {
            JsonNode _bindAsTree = _bindAsTree(jp);
            return _bindAsTree;
        } finally {
            try {
                jp.close();
            } catch (IOException e) {
            }
        }
    }

    protected static JsonToken _initForReading(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        JsonToken t = jp.getCurrentToken();
        if (t == null) {
            t = jp.nextToken();
            if (t == null) {
                throw new EOFException("No content to map to Object due to end of input");
            }
        }
        return t;
    }

    protected JsonDeserializer<Object> _findRootDeserializer(DeserializationConfig cfg, JavaType valueType) throws JsonMappingException {
        JsonDeserializer<Object> deser = (JsonDeserializer) this._rootDeserializers.get(valueType);
        if (deser != null) {
            return deser;
        }
        deser = this._provider.findTypedValueDeserializer(cfg, valueType, null);
        if (deser == null) {
            throw new JsonMappingException("Can not find a deserializer for type " + valueType);
        }
        this._rootDeserializers.put(valueType, deser);
        return deser;
    }

    protected DeserializationContext _createDeserializationContext(JsonParser jp, DeserializationConfig cfg) {
        return new StdDeserializationContext(cfg, jp, this._provider);
    }
}
