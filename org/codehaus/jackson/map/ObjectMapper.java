package org.codehaus.jackson.map;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.io.SegmentedStringWriter;
import org.codehaus.jackson.map.Module.SetupContext;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
import org.codehaus.jackson.map.deser.StdDeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializerProvider;
import org.codehaus.jackson.map.introspect.BasicClassIntrospector;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.introspect.VisibilityChecker.Std;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver;
import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
import org.codehaus.jackson.map.ser.BeanSerializerFactory;
import org.codehaus.jackson.map.ser.BeanSerializerModifier;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TreeTraversingParser;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.util.TokenBuffer;
import org.codehaus.jackson.util.VersionUtil;

public class ObjectMapper extends ObjectCodec implements Versioned {
    protected static final AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR;
    protected static final ClassIntrospector<? extends BeanDescription> DEFAULT_INTROSPECTOR;
    private static final JavaType JSON_NODE_TYPE;
    protected static final VisibilityChecker<?> STD_VISIBILITY_CHECKER;
    protected TypeResolverBuilder<?> _defaultTyper;
    protected DeserializationConfig _deserializationConfig;
    protected DeserializerProvider _deserializerProvider;
    protected final JsonFactory _jsonFactory;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
    protected SerializationConfig _serializationConfig;
    protected SerializerFactory _serializerFactory;
    protected SerializerProvider _serializerProvider;
    protected SubtypeResolver _subtypeResolver;
    protected ClassLoader _valueClassLoader;
    protected VisibilityChecker<?> _visibilityChecker;

    /* renamed from: org.codehaus.jackson.map.ObjectMapper.2 */
    static /* synthetic */ class C09392 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping;

        static {
            $SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping = new int[DefaultTyping.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping[DefaultTyping.NON_CONCRETE_AND_ARRAYS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping[DefaultTyping.OBJECT_AND_NON_CONCRETE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping[DefaultTyping.NON_FINAL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public enum DefaultTyping {
        JAVA_LANG_OBJECT,
        OBJECT_AND_NON_CONCRETE,
        NON_CONCRETE_AND_ARRAYS,
        NON_FINAL
    }

    /* renamed from: org.codehaus.jackson.map.ObjectMapper.1 */
    class C11491 implements SetupContext {
        final /* synthetic */ ObjectMapper val$mapper;

        C11491(ObjectMapper objectMapper) {
            this.val$mapper = objectMapper;
        }

        public Version getMapperVersion() {
            return ObjectMapper.this.version();
        }

        public DeserializationConfig getDeserializationConfig() {
            return this.val$mapper.getDeserializationConfig();
        }

        public SerializationConfig getSerializationConfig() {
            return this.val$mapper.getSerializationConfig();
        }

        public SerializationConfig getSeserializationConfig() {
            return getSerializationConfig();
        }

        public void addSerializers(Serializers s) {
            this.val$mapper._serializerFactory = this.val$mapper._serializerFactory.withAdditionalSerializers(s);
        }

        public void addBeanSerializerModifier(BeanSerializerModifier modifier) {
            this.val$mapper._serializerFactory = this.val$mapper._serializerFactory.withSerializerModifier(modifier);
        }

        public void addBeanDeserializerModifier(BeanDeserializerModifier modifier) {
            this.val$mapper._deserializerProvider = this.val$mapper._deserializerProvider.withDeserializerModifier(modifier);
        }

        public void addDeserializers(Deserializers d) {
            this.val$mapper._deserializerProvider = this.val$mapper._deserializerProvider.withAdditionalDeserializers(d);
        }

        public void insertAnnotationIntrospector(AnnotationIntrospector ai) {
            this.val$mapper._deserializationConfig.insertAnnotationIntrospector(ai);
            this.val$mapper._serializationConfig.insertAnnotationIntrospector(ai);
        }

        public void appendAnnotationIntrospector(AnnotationIntrospector ai) {
            this.val$mapper._deserializationConfig.appendAnnotationIntrospector(ai);
            this.val$mapper._serializationConfig.appendAnnotationIntrospector(ai);
        }

        public void setMixInAnnotations(Class<?> target, Class<?> mixinSource) {
            this.val$mapper._deserializationConfig.addMixInAnnotations(target, mixinSource);
            this.val$mapper._serializationConfig.addMixInAnnotations(target, mixinSource);
        }
    }

    public static class DefaultTypeResolverBuilder extends StdTypeResolverBuilder {
        protected final DefaultTyping _appliesFor;

        public DefaultTypeResolverBuilder(DefaultTyping t) {
            this._appliesFor = t;
        }

        public TypeDeserializer buildTypeDeserializer(JavaType baseType, Collection<NamedType> subtypes, BeanProperty property) {
            return useForType(baseType) ? super.buildTypeDeserializer(baseType, subtypes, property) : null;
        }

        public TypeSerializer buildTypeSerializer(JavaType baseType, Collection<NamedType> subtypes, BeanProperty property) {
            return useForType(baseType) ? super.buildTypeSerializer(baseType, subtypes, property) : null;
        }

        public boolean useForType(JavaType t) {
            boolean z = false;
            switch (C09392.$SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping[this._appliesFor.ordinal()]) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    if (t.isArrayType()) {
                        t = t.getContentType();
                        break;
                    }
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    break;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    if (t.isArrayType()) {
                        t = t.getContentType();
                    }
                    if (t.isFinal()) {
                        return false;
                    }
                    return true;
                default:
                    if (t.getRawClass() == Object.class) {
                        return true;
                    }
                    return false;
            }
            if (t.getRawClass() == Object.class || !t.isConcrete()) {
                z = true;
            }
            return z;
        }
    }

    static {
        JSON_NODE_TYPE = TypeFactory.type((Type) JsonNode.class);
        DEFAULT_INTROSPECTOR = BasicClassIntrospector.instance;
        DEFAULT_ANNOTATION_INTROSPECTOR = new JacksonAnnotationIntrospector();
        STD_VISIBILITY_CHECKER = Std.defaultInstance();
    }

    public ObjectMapper() {
        this(null, null, null);
    }

    public ObjectMapper(JsonFactory jf) {
        this(jf, null, null);
    }

    @Deprecated
    public ObjectMapper(SerializerFactory sf) {
        this(null, null, null);
        setSerializerFactory(sf);
    }

    public ObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp) {
        this(jf, sp, dp, null, null);
    }

    public ObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp, SerializationConfig sconfig, DeserializationConfig dconfig) {
        this._rootDeserializers = new ConcurrentHashMap(64, 0.6f, 2);
        if (jf == null) {
            jf = new MappingJsonFactory(this);
        }
        this._jsonFactory = jf;
        this._visibilityChecker = STD_VISIBILITY_CHECKER;
        if (sconfig == null) {
            sconfig = new SerializationConfig(DEFAULT_INTROSPECTOR, DEFAULT_ANNOTATION_INTROSPECTOR, this._visibilityChecker, null);
        }
        this._serializationConfig = sconfig;
        if (dconfig == null) {
            dconfig = new DeserializationConfig(DEFAULT_INTROSPECTOR, DEFAULT_ANNOTATION_INTROSPECTOR, this._visibilityChecker, null);
        }
        this._deserializationConfig = dconfig;
        if (sp == null) {
            sp = new StdSerializerProvider();
        }
        this._serializerProvider = sp;
        if (dp == null) {
            dp = new StdDeserializerProvider();
        }
        this._deserializerProvider = dp;
        this._serializerFactory = BeanSerializerFactory.instance;
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public ObjectMapper setSerializerFactory(SerializerFactory f) {
        this._serializerFactory = f;
        return this;
    }

    public ObjectMapper setSerializerProvider(SerializerProvider p) {
        this._serializerProvider = p;
        return this;
    }

    public SerializerProvider getSerializerProvider() {
        return this._serializerProvider;
    }

    public ObjectMapper setDeserializerProvider(DeserializerProvider p) {
        this._deserializerProvider = p;
        return this;
    }

    public DeserializerProvider getDeserializerProvider() {
        return this._deserializerProvider;
    }

    public ObjectMapper setNodeFactory(JsonNodeFactory f) {
        this._deserializationConfig.setNodeFactory(f);
        return this;
    }

    public VisibilityChecker<?> getVisibilityChecker() {
        return this._visibilityChecker;
    }

    public void setVisibilityChecker(VisibilityChecker<?> vc) {
        this._visibilityChecker = vc;
    }

    public SubtypeResolver getSubtypeResolver() {
        if (this._subtypeResolver == null) {
            this._subtypeResolver = new StdSubtypeResolver();
        }
        return this._subtypeResolver;
    }

    public void setSubtypeResolver(SubtypeResolver r) {
        this._subtypeResolver = r;
    }

    public void registerSubtypes(Class<?>... classes) {
        getSubtypeResolver().registerSubtypes((Class[]) classes);
    }

    public void registerSubtypes(NamedType... types) {
        getSubtypeResolver().registerSubtypes(types);
    }

    public void registerModule(Module module) {
        if (module.getModuleName() == null) {
            throw new IllegalArgumentException("Module without defined name");
        } else if (module.version() == null) {
            throw new IllegalArgumentException("Module without defined version");
        } else {
            module.setupModule(new C11491(this));
        }
    }

    public SerializationConfig getSerializationConfig() {
        return this._serializationConfig;
    }

    public SerializationConfig copySerializationConfig() {
        return this._serializationConfig.createUnshared(this._defaultTyper, this._visibilityChecker, this._subtypeResolver);
    }

    public ObjectMapper setSerializationConfig(SerializationConfig cfg) {
        this._serializationConfig = cfg;
        return this;
    }

    public ObjectMapper configure(Feature f, boolean state) {
        this._serializationConfig.set(f, state);
        return this;
    }

    public DeserializationConfig getDeserializationConfig() {
        return this._deserializationConfig;
    }

    public DeserializationConfig copyDeserializationConfig() {
        return this._deserializationConfig.createUnshared(this._defaultTyper, this._visibilityChecker, this._subtypeResolver);
    }

    public ObjectMapper setDeserializationConfig(DeserializationConfig cfg) {
        this._deserializationConfig = cfg;
        return this;
    }

    public ObjectMapper configure(DeserializationConfig.Feature f, boolean state) {
        this._deserializationConfig.set(f, state);
        return this;
    }

    public JsonFactory getJsonFactory() {
        return this._jsonFactory;
    }

    public ObjectMapper configure(JsonParser.Feature f, boolean state) {
        this._jsonFactory.configure(f, state);
        return this;
    }

    public ObjectMapper configure(JsonGenerator.Feature f, boolean state) {
        this._jsonFactory.configure(f, state);
        return this;
    }

    public JsonNodeFactory getNodeFactory() {
        return this._deserializationConfig.getNodeFactory();
    }

    public ObjectMapper enableDefaultTyping() {
        return enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE);
    }

    public ObjectMapper enableDefaultTyping(DefaultTyping dti) {
        return enableDefaultTyping(dti, As.WRAPPER_ARRAY);
    }

    public ObjectMapper enableDefaultTyping(DefaultTyping applicability, As includeAs) {
        return setDefaultTyping(new DefaultTypeResolverBuilder(applicability).init(Id.CLASS, null).inclusion(includeAs));
    }

    public ObjectMapper enableDefaultTypingAsProperty(DefaultTyping applicability, String propertyName) {
        return setDefaultTyping(new DefaultTypeResolverBuilder(applicability).init(Id.CLASS, null).inclusion(As.PROPERTY).typeProperty(propertyName));
    }

    public ObjectMapper disableDefaultTyping() {
        return setDefaultTyping(null);
    }

    @Deprecated
    public ObjectMapper setDefaltTyping(TypeResolverBuilder<?> typer) {
        this._defaultTyper = typer;
        return this;
    }

    public ObjectMapper setDefaultTyping(TypeResolverBuilder<?> typer) {
        this._defaultTyper = typer;
        return this;
    }

    public <T> T readValue(JsonParser jp, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), jp, TypeFactory.type((Type) valueType));
    }

    public <T> T readValue(JsonParser jp, Class<T> valueType, DeserializationConfig cfg) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(cfg, jp, TypeFactory.type((Type) valueType));
    }

    public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), jp, TypeFactory.type((TypeReference) valueTypeRef));
    }

    public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef, DeserializationConfig cfg) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(cfg, jp, TypeFactory.type((TypeReference) valueTypeRef));
    }

    public <T> T readValue(JsonParser jp, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), jp, valueType);
    }

    public <T> T readValue(JsonParser jp, JavaType valueType, DeserializationConfig cfg) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(cfg, jp, valueType);
    }

    public JsonNode readTree(JsonParser jp) throws IOException, JsonProcessingException {
        return readTree(jp, copyDeserializationConfig());
    }

    public JsonNode readTree(JsonParser jp, DeserializationConfig cfg) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readValue(cfg, jp, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(InputStream in) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) readValue(in, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(Reader r) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) readValue(r, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(String content) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) readValue(content, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public void writeValue(JsonGenerator jgen, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        SerializationConfig config = copySerializationConfig();
        if (config.isEnabled(Feature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _writeCloseableValue(jgen, value, config);
            return;
        }
        this._serializerProvider.serializeValue(config, jgen, value, this._serializerFactory);
        if (config.isEnabled(Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public void writeValue(JsonGenerator jgen, Object value, SerializationConfig config) throws IOException, JsonGenerationException, JsonMappingException {
        if (config.isEnabled(Feature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _writeCloseableValue(jgen, value, config);
            return;
        }
        this._serializerProvider.serializeValue(config, jgen, value, this._serializerFactory);
        if (config.isEnabled(Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public void writeTree(JsonGenerator jgen, JsonNode rootNode) throws IOException, JsonProcessingException {
        SerializationConfig config = copySerializationConfig();
        this._serializerProvider.serializeValue(config, jgen, rootNode, this._serializerFactory);
        if (config.isEnabled(Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public void writeTree(JsonGenerator jgen, JsonNode rootNode, SerializationConfig cfg) throws IOException, JsonProcessingException {
        this._serializerProvider.serializeValue(cfg, jgen, rootNode, this._serializerFactory);
        if (cfg.isEnabled(Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public ObjectNode createObjectNode() {
        return this._deserializationConfig.getNodeFactory().objectNode();
    }

    public ArrayNode createArrayNode() {
        return this._deserializationConfig.getNodeFactory().arrayNode();
    }

    public JsonParser treeAsTokens(JsonNode n) {
        return new TreeTraversingParser(n, this);
    }

    public <T> T treeToValue(JsonNode n, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return readValue(treeAsTokens(n), (Class) valueType);
    }

    public <T extends JsonNode> T valueToTree(Object fromValue) throws IllegalArgumentException {
        if (fromValue == null) {
            return null;
        }
        JsonGenerator buf = new TokenBuffer(this);
        try {
            writeValue(buf, fromValue);
            JsonParser jp = buf.asParser();
            T result = readTree(jp);
            jp.close();
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public boolean canSerialize(Class<?> type) {
        return this._serializerProvider.hasSerializerFor(copySerializationConfig(), type, this._serializerFactory);
    }

    public boolean canDeserialize(JavaType type) {
        return this._deserializerProvider.hasValueDeserializerFor(copyDeserializationConfig(), type);
    }

    public <T> T readValue(File src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type((Type) valueType));
    }

    public <T> T readValue(File src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type(valueTypeRef));
    }

    public <T> T readValue(File src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(URL src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type((Type) valueType));
    }

    public <T> T readValue(URL src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type(valueTypeRef));
    }

    public <T> T readValue(URL src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(String content, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(content), TypeFactory.type((Type) valueType));
    }

    public <T> T readValue(String content, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(content), TypeFactory.type(valueTypeRef));
    }

    public <T> T readValue(String content, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(content), valueType);
    }

    public <T> T readValue(Reader src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type((Type) valueType));
    }

    public <T> T readValue(Reader src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type(valueTypeRef));
    }

    public <T> T readValue(Reader src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(InputStream src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type((Type) valueType));
    }

    public <T> T readValue(InputStream src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), TypeFactory.type(valueTypeRef));
    }

    public <T> T readValue(InputStream src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(byte[] src, int offset, int len, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src, offset, len), TypeFactory.type((Type) valueType));
    }

    public <T> T readValue(byte[] src, int offset, int len, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src, offset, len), TypeFactory.type(valueTypeRef));
    }

    public <T> T readValue(byte[] src, int offset, int len, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src, offset, len), valueType);
    }

    public <T> T readValue(JsonNode root, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), root.traverse(), TypeFactory.type((Type) valueType));
    }

    public <T> T readValue(JsonNode root, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), root.traverse(), TypeFactory.type(valueTypeRef));
    }

    public <T> T readValue(JsonNode root, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), root.traverse(), valueType);
    }

    public void writeValue(File resultFile, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(resultFile, JsonEncoding.UTF8), value);
    }

    public void writeValue(OutputStream out, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8), value);
    }

    public void writeValue(Writer w, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(w), value);
    }

    public String writeValueAsString(Object value) throws IOException, JsonGenerationException, JsonMappingException {
        SegmentedStringWriter sw = new SegmentedStringWriter(this._jsonFactory._getBufferRecycler());
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(sw), value);
        return sw.getAndClear();
    }

    public byte[] writeValueAsBytes(Object value) throws IOException, JsonGenerationException, JsonMappingException {
        OutputStream bb = new ByteArrayBuilder(this._jsonFactory._getBufferRecycler());
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(bb, JsonEncoding.UTF8), value);
        byte[] result = bb.toByteArray();
        bb.release();
        return result;
    }

    @Deprecated
    public void writeValueUsingView(JsonGenerator jgen, Object value, Class<?> viewClass) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(jgen, value, viewClass);
    }

    @Deprecated
    public void writeValueUsingView(Writer w, Object value, Class<?> viewClass) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(w), value, viewClass);
    }

    @Deprecated
    public void writeValueUsingView(OutputStream out, Object value, Class<?> viewClass) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8), value, viewClass);
    }

    public ObjectWriter writer() {
        return new ObjectWriter(this, null, null, null);
    }

    public ObjectWriter viewWriter(Class<?> serializationView) {
        return new ObjectWriter(this, serializationView, null, null);
    }

    public ObjectWriter typedWriter(Class<?> rootType) {
        return new ObjectWriter(this, null, rootType == null ? null : TypeFactory.type((Type) rootType), null);
    }

    public ObjectWriter typedWriter(JavaType rootType) {
        return new ObjectWriter(this, null, rootType, null);
    }

    public ObjectWriter typedWriter(TypeReference<?> rootType) {
        return new ObjectWriter(this, null, rootType == null ? null : TypeFactory.type((TypeReference) rootType), null);
    }

    public ObjectWriter prettyPrintingWriter(PrettyPrinter pp) {
        if (pp == null) {
            pp = ObjectWriter.NULL_PRETTY_PRINTER;
        }
        return new ObjectWriter(this, null, null, pp);
    }

    public ObjectWriter defaultPrettyPrintingWriter() {
        return new ObjectWriter(this, null, null, _defaultPrettyPrinter());
    }

    public ObjectWriter filteredWriter(FilterProvider filterProvider) {
        return new ObjectWriter(this, filterProvider);
    }

    public ObjectReader reader() {
        return new ObjectReader(this, null, null);
    }

    public ObjectReader updatingReader(Object valueToUpdate) {
        return new ObjectReader(this, TypeFactory.type(valueToUpdate.getClass()), valueToUpdate);
    }

    public ObjectReader reader(JavaType type) {
        return new ObjectReader(this, type, null);
    }

    public ObjectReader reader(Class<?> type) {
        return reader(TypeFactory.type((Type) type));
    }

    public ObjectReader reader(TypeReference<?> type) {
        return reader(TypeFactory.type((TypeReference) type));
    }

    public ObjectReader reader(JsonNodeFactory f) {
        return new ObjectReader(this, null, null).withNodeFactory(f);
    }

    public <T> T convertValue(Object fromValue, Class<T> toValueType) throws IllegalArgumentException {
        return _convert(fromValue, TypeFactory.type((Type) toValueType));
    }

    public <T> T convertValue(Object fromValue, TypeReference toValueTypeRef) throws IllegalArgumentException {
        return _convert(fromValue, TypeFactory.type(toValueTypeRef));
    }

    public <T> T convertValue(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
        return _convert(fromValue, toValueType);
    }

    protected Object _convert(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
        if (fromValue == null) {
            return null;
        }
        JsonGenerator buf = new TokenBuffer(this);
        try {
            writeValue(buf, fromValue);
            JsonParser jp = buf.asParser();
            Object result = readValue(jp, toValueType);
            jp.close();
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public JsonSchema generateJsonSchema(Class<?> t) throws JsonMappingException {
        return generateJsonSchema(t, copySerializationConfig());
    }

    public JsonSchema generateJsonSchema(Class<?> t, SerializationConfig cfg) throws JsonMappingException {
        return this._serializerProvider.generateJsonSchema(t, cfg, this._serializerFactory);
    }

    protected PrettyPrinter _defaultPrettyPrinter() {
        return new DefaultPrettyPrinter();
    }

    protected final void _configAndWriteValue(JsonGenerator jgen, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        SerializationConfig cfg = copySerializationConfig();
        if (cfg.isEnabled(Feature.INDENT_OUTPUT)) {
            jgen.useDefaultPrettyPrinter();
        }
        if (cfg.isEnabled(Feature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _configAndWriteCloseable(jgen, value, cfg);
            return;
        }
        boolean closed = false;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            closed = true;
            jgen.close();
            if (1 == null) {
                try {
                    jgen.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (!closed) {
                try {
                    jgen.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    protected final void _configAndWriteValue(JsonGenerator jgen, Object value, Class<?> viewClass) throws IOException, JsonGenerationException, JsonMappingException {
        SerializationConfig cfg = copySerializationConfig();
        if (cfg.isEnabled(Feature.INDENT_OUTPUT)) {
            jgen.useDefaultPrettyPrinter();
        }
        cfg.setSerializationView(viewClass);
        if (cfg.isEnabled(Feature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _configAndWriteCloseable(jgen, value, cfg);
            return;
        }
        boolean closed = false;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            closed = true;
            jgen.close();
            if (1 == null) {
                try {
                    jgen.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (!closed) {
                try {
                    jgen.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    private final void _configAndWriteCloseable(JsonGenerator jgen, Object value, SerializationConfig cfg) throws IOException, JsonGenerationException, JsonMappingException {
        Closeable toClose = (Closeable) value;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            JsonGenerator tmpJgen = jgen;
            jgen = null;
            tmpJgen.close();
            Closeable tmpToClose = toClose;
            toClose = null;
            tmpToClose.close();
            if (jgen != null) {
                try {
                    jgen.close();
                } catch (IOException e) {
                }
            }
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e2) {
                }
            }
        } catch (Throwable th) {
            if (jgen != null) {
                try {
                    jgen.close();
                } catch (IOException e3) {
                }
            }
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e4) {
                }
            }
        }
    }

    private final void _writeCloseableValue(JsonGenerator jgen, Object value, SerializationConfig cfg) throws IOException, JsonGenerationException, JsonMappingException {
        Closeable toClose = (Closeable) value;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            if (cfg.isEnabled(Feature.FLUSH_AFTER_WRITE_VALUE)) {
                jgen.flush();
            }
            Closeable tmpToClose = toClose;
            toClose = null;
            tmpToClose.close();
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    protected Object _readValue(DeserializationConfig cfg, JsonParser jp, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        Object obj;
        JsonToken t = _initForReading(jp);
        if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            obj = null;
        } else {
            obj = _findRootDeserializer(cfg, valueType).deserialize(jp, _createDeserializationContext(jp, cfg));
        }
        jp.clearCurrentToken();
        return obj;
    }

    protected Object _readMapAndClose(JsonParser jp, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        try {
            Object obj;
            JsonToken t = _initForReading(jp);
            if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
                obj = null;
            } else {
                DeserializationConfig cfg = copyDeserializationConfig();
                obj = _findRootDeserializer(cfg, valueType).deserialize(jp, _createDeserializationContext(jp, cfg));
            }
            jp.clearCurrentToken();
            return obj;
        } finally {
            try {
                jp.close();
            } catch (IOException e) {
            }
        }
    }

    protected JsonToken _initForReading(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
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
        deser = this._deserializerProvider.findTypedValueDeserializer(cfg, valueType, null);
        if (deser == null) {
            throw new JsonMappingException("Can not find a deserializer for type " + valueType);
        }
        this._rootDeserializers.put(valueType, deser);
        return deser;
    }

    protected DeserializationContext _createDeserializationContext(JsonParser jp, DeserializationConfig cfg) {
        return new StdDeserializationContext(cfg, jp, this._deserializerProvider);
    }
}
