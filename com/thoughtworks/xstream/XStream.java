package com.thoughtworks.xstream;

import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.dto.Msg;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.ConverterRegistry;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.SingleValueConverterWrapper;
import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;
import com.thoughtworks.xstream.converters.basic.BigIntegerConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.converters.basic.ByteConverter;
import com.thoughtworks.xstream.converters.basic.CharConverter;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.converters.basic.NullConverter;
import com.thoughtworks.xstream.converters.basic.ShortConverter;
import com.thoughtworks.xstream.converters.basic.StringBufferConverter;
import com.thoughtworks.xstream.converters.basic.StringConverter;
import com.thoughtworks.xstream.converters.basic.URIConverter;
import com.thoughtworks.xstream.converters.basic.URLConverter;
import com.thoughtworks.xstream.converters.collections.ArrayConverter;
import com.thoughtworks.xstream.converters.collections.BitSetConverter;
import com.thoughtworks.xstream.converters.collections.CharArrayConverter;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.converters.collections.PropertiesConverter;
import com.thoughtworks.xstream.converters.collections.SingletonCollectionConverter;
import com.thoughtworks.xstream.converters.collections.SingletonMapConverter;
import com.thoughtworks.xstream.converters.collections.TreeMapConverter;
import com.thoughtworks.xstream.converters.collections.TreeSetConverter;
import com.thoughtworks.xstream.converters.extended.ColorConverter;
import com.thoughtworks.xstream.converters.extended.DynamicProxyConverter;
import com.thoughtworks.xstream.converters.extended.EncodedByteArrayConverter;
import com.thoughtworks.xstream.converters.extended.FileConverter;
import com.thoughtworks.xstream.converters.extended.FontConverter;
import com.thoughtworks.xstream.converters.extended.GregorianCalendarConverter;
import com.thoughtworks.xstream.converters.extended.JavaClassConverter;
import com.thoughtworks.xstream.converters.extended.JavaFieldConverter;
import com.thoughtworks.xstream.converters.extended.JavaMethodConverter;
import com.thoughtworks.xstream.converters.extended.LocaleConverter;
import com.thoughtworks.xstream.converters.extended.LookAndFeelConverter;
import com.thoughtworks.xstream.converters.extended.SqlDateConverter;
import com.thoughtworks.xstream.converters.extended.SqlTimeConverter;
import com.thoughtworks.xstream.converters.extended.SqlTimestampConverter;
import com.thoughtworks.xstream.converters.extended.TextAttributeConverter;
import com.thoughtworks.xstream.converters.reflection.ExternalizableConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.SerializableConverter;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.core.DefaultConverterLookup;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.core.MapBackedDataHolder;
import com.thoughtworks.xstream.core.ReferenceByIdMarshallingStrategy;
import com.thoughtworks.xstream.core.ReferenceByXPathMarshallingStrategy;
import com.thoughtworks.xstream.core.TreeMarshallingStrategy;
import com.thoughtworks.xstream.core.util.CompositeClassLoader;
import com.thoughtworks.xstream.core.util.CustomObjectInputStream;
import com.thoughtworks.xstream.core.util.CustomObjectOutputStream;
import com.thoughtworks.xstream.core.util.CustomObjectOutputStream.StreamCallback;
import com.thoughtworks.xstream.core.util.SelfStreamingInstanceChecker;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StatefulWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.AnnotationConfiguration;
import com.thoughtworks.xstream.mapper.ArrayMapper;
import com.thoughtworks.xstream.mapper.AttributeAliasingMapper;
import com.thoughtworks.xstream.mapper.AttributeMapper;
import com.thoughtworks.xstream.mapper.CachingMapper;
import com.thoughtworks.xstream.mapper.ClassAliasingMapper;
import com.thoughtworks.xstream.mapper.DefaultImplementationsMapper;
import com.thoughtworks.xstream.mapper.DefaultMapper;
import com.thoughtworks.xstream.mapper.DynamicProxyMapper;
import com.thoughtworks.xstream.mapper.FieldAliasingMapper;
import com.thoughtworks.xstream.mapper.ImmutableTypesMapper;
import com.thoughtworks.xstream.mapper.ImplicitCollectionMapper;
import com.thoughtworks.xstream.mapper.LocalConversionMapper;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.Mapper.Null;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import com.thoughtworks.xstream.mapper.OuterClassMapper;
import com.thoughtworks.xstream.mapper.PackageAliasingMapper;
import com.thoughtworks.xstream.mapper.SystemAttributeAliasingMapper;
import com.thoughtworks.xstream.mapper.XStream11XmlFriendlyMapper;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Pattern;
import org.jivesoftware.smackx.FormField;

public class XStream {
    private static final String ANNOTATION_MAPPER_TYPE = "com.thoughtworks.xstream.mapper.AnnotationMapper";
    public static final int ID_REFERENCES = 1002;
    private static final Pattern IGNORE_ALL;
    public static final int NO_REFERENCES = 1001;
    public static final int PRIORITY_LOW = -10;
    public static final int PRIORITY_NORMAL = 0;
    public static final int PRIORITY_VERY_HIGH = 10000;
    public static final int PRIORITY_VERY_LOW = -20;
    public static final int SINGLE_NODE_XPATH_ABSOLUTE_REFERENCES = 1006;
    public static final int SINGLE_NODE_XPATH_RELATIVE_REFERENCES = 1005;
    public static final int XPATH_ABSOLUTE_REFERENCES = 1004;
    public static final int XPATH_RELATIVE_REFERENCES = 1003;
    private AnnotationConfiguration annotationConfiguration;
    private AttributeAliasingMapper attributeAliasingMapper;
    private AttributeMapper attributeMapper;
    private ClassAliasingMapper classAliasingMapper;
    private ClassLoaderReference classLoaderReference;
    private ConverterLookup converterLookup;
    private ConverterRegistry converterRegistry;
    private DefaultImplementationsMapper defaultImplementationsMapper;
    private FieldAliasingMapper fieldAliasingMapper;
    private HierarchicalStreamDriver hierarchicalStreamDriver;
    private ImmutableTypesMapper immutableTypesMapper;
    private ImplicitCollectionMapper implicitCollectionMapper;
    private LocalConversionMapper localConversionMapper;
    private Mapper mapper;
    private MarshallingStrategy marshallingStrategy;
    private PackageAliasingMapper packageAliasingMapper;
    private ReflectionProvider reflectionProvider;
    private SystemAttributeAliasingMapper systemAttributeAliasingMapper;

    /* renamed from: com.thoughtworks.xstream.XStream.1 */
    class C11261 implements StreamCallback {
        final /* synthetic */ StatefulWriter val$statefulWriter;

        C11261(StatefulWriter statefulWriter) {
            this.val$statefulWriter = statefulWriter;
        }

        public void writeToStream(Object object) {
            XStream.this.marshal(object, this.val$statefulWriter);
        }

        public void writeFieldsToStream(Map fields) throws NotActiveException {
            throw new NotActiveException("not in call to writeObject");
        }

        public void defaultWriteObject() throws NotActiveException {
            throw new NotActiveException("not in call to writeObject");
        }

        public void flush() {
            this.val$statefulWriter.flush();
        }

        public void close() {
            if (this.val$statefulWriter.state() != StatefulWriter.STATE_CLOSED) {
                this.val$statefulWriter.endNode();
                this.val$statefulWriter.close();
            }
        }
    }

    /* renamed from: com.thoughtworks.xstream.XStream.2 */
    class C11272 implements CustomObjectInputStream.StreamCallback {
        final /* synthetic */ HierarchicalStreamReader val$reader;

        C11272(HierarchicalStreamReader hierarchicalStreamReader) {
            this.val$reader = hierarchicalStreamReader;
        }

        public Object readFromStream() throws EOFException {
            if (this.val$reader.hasMoreChildren()) {
                this.val$reader.moveDown();
                Object result = XStream.this.unmarshal(this.val$reader);
                this.val$reader.moveUp();
                return result;
            }
            throw new EOFException();
        }

        public Map readFieldsFromStream() throws IOException {
            throw new NotActiveException("not in call to readObject");
        }

        public void defaultReadObject() throws NotActiveException {
            throw new NotActiveException("not in call to readObject");
        }

        public void registerValidation(ObjectInputValidation validation, int priority) throws NotActiveException {
            throw new NotActiveException("stream inactive");
        }

        public void close() {
            this.val$reader.close();
        }
    }

    public static class InitializationException extends XStreamException {
        public InitializationException(String message, Throwable cause) {
            super(message, cause);
        }

        public InitializationException(String message) {
            super(message);
        }
    }

    static {
        IGNORE_ALL = Pattern.compile(".*");
    }

    public XStream() {
        this(null, (Mapper) null, new XppDriver());
    }

    public XStream(ReflectionProvider reflectionProvider) {
        this(reflectionProvider, (Mapper) null, new XppDriver());
    }

    public XStream(HierarchicalStreamDriver hierarchicalStreamDriver) {
        this(null, (Mapper) null, hierarchicalStreamDriver);
    }

    public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver hierarchicalStreamDriver) {
        this(reflectionProvider, (Mapper) null, hierarchicalStreamDriver);
    }

    public XStream(ReflectionProvider reflectionProvider, Mapper mapper, HierarchicalStreamDriver driver) {
        this(reflectionProvider, driver, new CompositeClassLoader(), mapper);
    }

    public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoaderReference classLoaderReference) {
        this(reflectionProvider, driver, classLoaderReference, null);
    }

    public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader) {
        this(reflectionProvider, driver, classLoader, null);
    }

    public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader, Mapper mapper) {
        this(reflectionProvider, driver, new ClassLoaderReference(classLoader), mapper, new DefaultConverterLookup());
    }

    public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoaderReference classLoaderReference, Mapper mapper) {
        this(reflectionProvider, driver, classLoaderReference, mapper, new DefaultConverterLookup());
    }

    private XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoaderReference classLoader, Mapper mapper, DefaultConverterLookup defaultConverterLookup) {
        this(reflectionProvider, driver, classLoader, mapper, (ConverterLookup) defaultConverterLookup, (ConverterRegistry) defaultConverterLookup);
    }

    public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader, Mapper mapper, ConverterLookup converterLookup, ConverterRegistry converterRegistry) {
        this(reflectionProvider, driver, new ClassLoaderReference(classLoader), mapper, converterLookup, converterRegistry);
    }

    public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoaderReference classLoaderReference, Mapper mapper, ConverterLookup converterLookup, ConverterRegistry converterRegistry) {
        if (reflectionProvider == null) {
            reflectionProvider = JVM.newReflectionProvider();
        }
        this.reflectionProvider = reflectionProvider;
        this.hierarchicalStreamDriver = driver;
        this.classLoaderReference = classLoaderReference;
        this.converterLookup = converterLookup;
        this.converterRegistry = converterRegistry;
        if (mapper == null) {
            mapper = buildMapper();
        }
        this.mapper = mapper;
        setupMappers();
        setupAliases();
        setupDefaultImplementations();
        setupConverters();
        setupImmutableTypes();
        setMode(XPATH_RELATIVE_REFERENCES);
    }

    private Mapper buildMapper() {
        Mapper mapper = new DefaultMapper(this.classLoaderReference);
        if (useXStream11XmlFriendlyMapper()) {
            mapper = new XStream11XmlFriendlyMapper(mapper);
        }
        Mapper mapper2 = new AttributeMapper(new DefaultImplementationsMapper(new ArrayMapper(new OuterClassMapper(new ImplicitCollectionMapper(new SystemAttributeAliasingMapper(new AttributeAliasingMapper(new FieldAliasingMapper(new ClassAliasingMapper(new PackageAliasingMapper(new DynamicProxyMapper(mapper)))))))))), this.converterLookup, this.reflectionProvider);
        if (JVM.is15()) {
            mapper = buildMapperDynamically("com.thoughtworks.xstream.mapper.EnumMapper", new Class[]{Mapper.class}, new Object[]{mapper2});
        } else {
            mapper = mapper2;
        }
        mapper = new ImmutableTypesMapper(new LocalConversionMapper(mapper));
        if (JVM.is15()) {
            mapper = buildMapperDynamically(ANNOTATION_MAPPER_TYPE, new Class[]{Mapper.class, ConverterRegistry.class, ConverterLookup.class, ClassLoaderReference.class, ReflectionProvider.class}, new Object[]{mapper, this.converterLookup, this.converterLookup, this.classLoaderReference, this.reflectionProvider});
        }
        return new CachingMapper(wrapMapper((MapperWrapper) mapper));
    }

    private Mapper buildMapperDynamically(String className, Class[] constructorParamTypes, Object[] constructorParamValues) {
        try {
            return (Mapper) Class.forName(className, false, this.classLoaderReference.getReference()).getConstructor(constructorParamTypes).newInstance(constructorParamValues);
        } catch (Exception e) {
            throw new InitializationException("Could not instantiate mapper : " + className, e);
        }
    }

    protected MapperWrapper wrapMapper(MapperWrapper next) {
        return next;
    }

    protected boolean useXStream11XmlFriendlyMapper() {
        return false;
    }

    private void setupMappers() {
        this.packageAliasingMapper = (PackageAliasingMapper) this.mapper.lookupMapperOfType(PackageAliasingMapper.class);
        this.classAliasingMapper = (ClassAliasingMapper) this.mapper.lookupMapperOfType(ClassAliasingMapper.class);
        this.fieldAliasingMapper = (FieldAliasingMapper) this.mapper.lookupMapperOfType(FieldAliasingMapper.class);
        this.attributeMapper = (AttributeMapper) this.mapper.lookupMapperOfType(AttributeMapper.class);
        this.attributeAliasingMapper = (AttributeAliasingMapper) this.mapper.lookupMapperOfType(AttributeAliasingMapper.class);
        this.systemAttributeAliasingMapper = (SystemAttributeAliasingMapper) this.mapper.lookupMapperOfType(SystemAttributeAliasingMapper.class);
        this.implicitCollectionMapper = (ImplicitCollectionMapper) this.mapper.lookupMapperOfType(ImplicitCollectionMapper.class);
        this.defaultImplementationsMapper = (DefaultImplementationsMapper) this.mapper.lookupMapperOfType(DefaultImplementationsMapper.class);
        this.immutableTypesMapper = (ImmutableTypesMapper) this.mapper.lookupMapperOfType(ImmutableTypesMapper.class);
        this.localConversionMapper = (LocalConversionMapper) this.mapper.lookupMapperOfType(LocalConversionMapper.class);
        this.annotationConfiguration = (AnnotationConfiguration) this.mapper.lookupMapperOfType(AnnotationConfiguration.class);
    }

    protected void setupAliases() {
        if (this.classAliasingMapper != null) {
            alias("null", Null.class);
            alias("int", Integer.class);
            alias("float", Float.class);
            alias("double", Double.class);
            alias("long", Long.class);
            alias("short", Short.class);
            alias("char", Character.class);
            alias("byte", Byte.class);
            alias(FormField.TYPE_BOOLEAN, Boolean.class);
            alias("number", Number.class);
            alias("object", Object.class);
            alias("big-int", BigInteger.class);
            alias("big-decimal", BigDecimal.class);
            alias("string-buffer", StringBuffer.class);
            alias("string", String.class);
            alias("java-class", Class.class);
            alias("method", Method.class);
            alias("constructor", Constructor.class);
            alias("field", Field.class);
            alias(Msg.DATE, Date.class);
            alias("uri", URI.class);
            alias("url", URL.class);
            alias("bit-set", BitSet.class);
            alias("map", Map.class);
            alias("entry", Entry.class);
            alias("properties", Properties.class);
            alias("list", List.class);
            alias("set", Set.class);
            alias("sorted-set", SortedSet.class);
            alias("linked-list", LinkedList.class);
            alias("vector", Vector.class);
            alias("tree-map", TreeMap.class);
            alias("tree-set", TreeSet.class);
            alias("hashtable", Hashtable.class);
            alias("empty-list", Collections.EMPTY_LIST.getClass());
            alias("empty-map", Collections.EMPTY_MAP.getClass());
            alias("empty-set", Collections.EMPTY_SET.getClass());
            alias("singleton-list", Collections.singletonList(this).getClass());
            alias("singleton-map", Collections.singletonMap(this, null).getClass());
            alias("singleton-set", Collections.singleton(this).getClass());
            if (JVM.isAWTAvailable()) {
                alias("awt-color", JVM.loadClassForName("java.awt.Color", false));
                alias("awt-font", JVM.loadClassForName("java.awt.Font", false));
                alias("awt-text-attribute", JVM.loadClassForName("java.awt.font.TextAttribute"));
            }
            if (JVM.isSQLAvailable()) {
                alias("sql-timestamp", JVM.loadClassForName("java.sql.Timestamp"));
                alias("sql-time", JVM.loadClassForName("java.sql.Time"));
                alias("sql-date", JVM.loadClassForName("java.sql.Date"));
            }
            alias(MessageVo.TYPE_FILE, File.class);
            alias("locale", Locale.class);
            alias("gregorian-calendar", Calendar.class);
            if (JVM.is14()) {
                aliasDynamically("auth-subject", "javax.security.auth.Subject");
                alias("linked-hash-map", JVM.loadClassForName("java.util.LinkedHashMap"));
                alias("linked-hash-set", JVM.loadClassForName("java.util.LinkedHashSet"));
                alias("trace", JVM.loadClassForName("java.lang.StackTraceElement"));
                alias("currency", JVM.loadClassForName("java.util.Currency"));
                aliasType(AlixDefine.charset, JVM.loadClassForName("java.nio.charset.Charset"));
            }
            if (JVM.is15()) {
                aliasDynamically("duration", "javax.xml.datatype.Duration");
                alias("concurrent-hash-map", JVM.loadClassForName("java.util.concurrent.ConcurrentHashMap"));
                alias("enum-set", JVM.loadClassForName("java.util.EnumSet"));
                alias("enum-map", JVM.loadClassForName("java.util.EnumMap"));
                alias("string-builder", JVM.loadClassForName("java.lang.StringBuilder"));
                alias("uuid", JVM.loadClassForName("java.util.UUID"));
            }
        }
    }

    private void aliasDynamically(String alias, String className) {
        Class type = JVM.loadClassForName(className);
        if (type != null) {
            alias(alias, type);
        }
    }

    protected void setupDefaultImplementations() {
        if (this.defaultImplementationsMapper != null) {
            addDefaultImplementation(HashMap.class, Map.class);
            addDefaultImplementation(ArrayList.class, List.class);
            addDefaultImplementation(HashSet.class, Set.class);
            addDefaultImplementation(TreeSet.class, SortedSet.class);
            addDefaultImplementation(GregorianCalendar.class, Calendar.class);
        }
    }

    protected void setupConverters() {
        registerConverter(new ReflectionConverter(this.mapper, this.reflectionProvider), (int) PRIORITY_VERY_LOW);
        registerConverter(new SerializableConverter(this.mapper, this.reflectionProvider, this.classLoaderReference), (int) PRIORITY_LOW);
        registerConverter(new ExternalizableConverter(this.mapper, this.classLoaderReference), (int) PRIORITY_LOW);
        registerConverter(new NullConverter(), (int) PRIORITY_VERY_HIGH);
        registerConverter(new IntConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new FloatConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new DoubleConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new LongConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new ShortConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new CharConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new BooleanConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new ByteConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new StringConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new StringBufferConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new DateConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new BitSetConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new URIConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new URLConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new BigIntegerConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new BigDecimalConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new ArrayConverter(this.mapper), (int) PRIORITY_NORMAL);
        registerConverter(new CharArrayConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new CollectionConverter(this.mapper), (int) PRIORITY_NORMAL);
        registerConverter(new MapConverter(this.mapper), (int) PRIORITY_NORMAL);
        registerConverter(new TreeMapConverter(this.mapper), (int) PRIORITY_NORMAL);
        registerConverter(new TreeSetConverter(this.mapper), (int) PRIORITY_NORMAL);
        registerConverter(new SingletonCollectionConverter(this.mapper), (int) PRIORITY_NORMAL);
        registerConverter(new SingletonMapConverter(this.mapper), (int) PRIORITY_NORMAL);
        registerConverter(new PropertiesConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new EncodedByteArrayConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new FileConverter(), (int) PRIORITY_NORMAL);
        if (JVM.isSQLAvailable()) {
            registerConverter(new SqlTimestampConverter(), (int) PRIORITY_NORMAL);
            registerConverter(new SqlTimeConverter(), (int) PRIORITY_NORMAL);
            registerConverter(new SqlDateConverter(), (int) PRIORITY_NORMAL);
        }
        registerConverter(new DynamicProxyConverter(this.mapper, this.classLoaderReference), (int) PRIORITY_NORMAL);
        registerConverter(new JavaClassConverter(this.classLoaderReference), (int) PRIORITY_NORMAL);
        registerConverter(new JavaMethodConverter(this.classLoaderReference), (int) PRIORITY_NORMAL);
        registerConverter(new JavaFieldConverter(this.classLoaderReference), (int) PRIORITY_NORMAL);
        if (JVM.isAWTAvailable()) {
            registerConverter(new FontConverter(this.mapper), (int) PRIORITY_NORMAL);
            registerConverter(new ColorConverter(), (int) PRIORITY_NORMAL);
            registerConverter(new TextAttributeConverter(), (int) PRIORITY_NORMAL);
        }
        if (JVM.isSwingAvailable()) {
            registerConverter(new LookAndFeelConverter(this.mapper, this.reflectionProvider), (int) PRIORITY_NORMAL);
        }
        registerConverter(new LocaleConverter(), (int) PRIORITY_NORMAL);
        registerConverter(new GregorianCalendarConverter(), (int) PRIORITY_NORMAL);
        if (JVM.is14()) {
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.SubjectConverter", PRIORITY_NORMAL, new Class[]{Mapper.class}, new Object[]{this.mapper});
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.ThrowableConverter", PRIORITY_NORMAL, new Class[]{ConverterLookup.class}, new Object[]{this.converterLookup});
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.StackTraceElementConverter", PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.CurrencyConverter", PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.RegexPatternConverter", PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.extended.CharsetConverter", PRIORITY_NORMAL, null, null);
        }
        if (JVM.is15()) {
            if (JVM.loadClassForName("javax.xml.datatype.Duration") != null) {
                registerConverterDynamically("com.thoughtworks.xstream.converters.extended.DurationConverter", PRIORITY_NORMAL, null, null);
            }
            registerConverterDynamically("com.thoughtworks.xstream.converters.enums.EnumConverter", PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.enums.EnumSetConverter", PRIORITY_NORMAL, new Class[]{Mapper.class}, new Object[]{this.mapper});
            registerConverterDynamically("com.thoughtworks.xstream.converters.enums.EnumMapConverter", PRIORITY_NORMAL, new Class[]{Mapper.class}, new Object[]{this.mapper});
            registerConverterDynamically("com.thoughtworks.xstream.converters.basic.StringBuilderConverter", PRIORITY_NORMAL, null, null);
            registerConverterDynamically("com.thoughtworks.xstream.converters.basic.UUIDConverter", PRIORITY_NORMAL, null, null);
        }
        registerConverter(new SelfStreamingInstanceChecker(this.converterLookup, (Object) this), (int) PRIORITY_NORMAL);
    }

    private void registerConverterDynamically(String className, int priority, Class[] constructorParamTypes, Object[] constructorParamValues) {
        try {
            Object instance = Class.forName(className, false, this.classLoaderReference.getReference()).getConstructor(constructorParamTypes).newInstance(constructorParamValues);
            if (instance instanceof Converter) {
                registerConverter((Converter) instance, priority);
            } else if (instance instanceof SingleValueConverter) {
                registerConverter((SingleValueConverter) instance, priority);
            }
        } catch (Exception e) {
            throw new InitializationException("Could not instantiate converter : " + className, e);
        }
    }

    protected void setupImmutableTypes() {
        if (this.immutableTypesMapper != null) {
            addImmutableType(Boolean.TYPE);
            addImmutableType(Boolean.class);
            addImmutableType(Byte.TYPE);
            addImmutableType(Byte.class);
            addImmutableType(Character.TYPE);
            addImmutableType(Character.class);
            addImmutableType(Double.TYPE);
            addImmutableType(Double.class);
            addImmutableType(Float.TYPE);
            addImmutableType(Float.class);
            addImmutableType(Integer.TYPE);
            addImmutableType(Integer.class);
            addImmutableType(Long.TYPE);
            addImmutableType(Long.class);
            addImmutableType(Short.TYPE);
            addImmutableType(Short.class);
            addImmutableType(Null.class);
            addImmutableType(BigDecimal.class);
            addImmutableType(BigInteger.class);
            addImmutableType(String.class);
            addImmutableType(URI.class);
            addImmutableType(URL.class);
            addImmutableType(File.class);
            addImmutableType(Class.class);
            addImmutableType(Collections.EMPTY_LIST.getClass());
            addImmutableType(Collections.EMPTY_SET.getClass());
            addImmutableType(Collections.EMPTY_MAP.getClass());
            if (JVM.isAWTAvailable()) {
                addImmutableTypeDynamically("java.awt.font.TextAttribute");
            }
            if (JVM.is14()) {
                addImmutableTypeDynamically("java.nio.charset.Charset");
                addImmutableTypeDynamically("java.util.Currency");
            }
        }
    }

    private void addImmutableTypeDynamically(String className) {
        Class type = JVM.loadClassForName(className);
        if (type != null) {
            addImmutableType(type);
        }
    }

    public void setMarshallingStrategy(MarshallingStrategy marshallingStrategy) {
        this.marshallingStrategy = marshallingStrategy;
    }

    public String toXML(Object obj) {
        Writer writer = new StringWriter();
        toXML(obj, writer);
        return writer.toString();
    }

    public void toXML(Object obj, Writer out) {
        HierarchicalStreamWriter writer = this.hierarchicalStreamDriver.createWriter(out);
        try {
            marshal(obj, writer);
        } finally {
            writer.flush();
        }
    }

    public void toXML(Object obj, OutputStream out) {
        HierarchicalStreamWriter writer = this.hierarchicalStreamDriver.createWriter(out);
        try {
            marshal(obj, writer);
        } finally {
            writer.flush();
        }
    }

    public void marshal(Object obj, HierarchicalStreamWriter writer) {
        marshal(obj, writer, null);
    }

    public void marshal(Object obj, HierarchicalStreamWriter writer, DataHolder dataHolder) {
        this.marshallingStrategy.marshal(writer, obj, this.converterLookup, this.mapper, dataHolder);
    }

    public Object fromXML(String xml) {
        return fromXML(new StringReader(xml));
    }

    public Object fromXML(Reader reader) {
        return unmarshal(this.hierarchicalStreamDriver.createReader(reader), null);
    }

    public Object fromXML(InputStream input) {
        return unmarshal(this.hierarchicalStreamDriver.createReader(input), null);
    }

    public Object fromXML(URL url) {
        return fromXML(url, null);
    }

    public Object fromXML(File file) {
        return fromXML(file, null);
    }

    public Object fromXML(String xml, Object root) {
        return fromXML(new StringReader(xml), root);
    }

    public Object fromXML(Reader xml, Object root) {
        return unmarshal(this.hierarchicalStreamDriver.createReader(xml), root);
    }

    public Object fromXML(URL url, Object root) {
        return unmarshal(this.hierarchicalStreamDriver.createReader(url), root);
    }

    public Object fromXML(File file, Object root) {
        HierarchicalStreamReader reader = this.hierarchicalStreamDriver.createReader(file);
        try {
            Object unmarshal = unmarshal(reader, root);
            return unmarshal;
        } finally {
            reader.close();
        }
    }

    public Object fromXML(InputStream input, Object root) {
        return unmarshal(this.hierarchicalStreamDriver.createReader(input), root);
    }

    public Object unmarshal(HierarchicalStreamReader reader) {
        return unmarshal(reader, null, null);
    }

    public Object unmarshal(HierarchicalStreamReader reader, Object root) {
        return unmarshal(reader, root, null);
    }

    public Object unmarshal(HierarchicalStreamReader reader, Object root, DataHolder dataHolder) {
        try {
            return this.marshallingStrategy.unmarshal(root, reader, dataHolder, this.converterLookup, this.mapper);
        } catch (ConversionException e) {
            Package pkg = getClass().getPackage();
            String version = pkg != null ? pkg.getImplementationVersion() : null;
            String str = AlixDefine.VERSION;
            if (version == null) {
                version = "not available";
            }
            e.add(str, version);
            throw e;
        }
    }

    public void alias(String name, Class type) {
        if (this.classAliasingMapper == null) {
            throw new InitializationException("No " + ClassAliasingMapper.class.getName() + " available");
        }
        this.classAliasingMapper.addClassAlias(name, type);
    }

    public void aliasType(String name, Class type) {
        if (this.classAliasingMapper == null) {
            throw new InitializationException("No " + ClassAliasingMapper.class.getName() + " available");
        }
        this.classAliasingMapper.addTypeAlias(name, type);
    }

    public void alias(String name, Class type, Class defaultImplementation) {
        alias(name, type);
        addDefaultImplementation(defaultImplementation, type);
    }

    public void aliasPackage(String name, String pkgName) {
        if (this.packageAliasingMapper == null) {
            throw new InitializationException("No " + PackageAliasingMapper.class.getName() + " available");
        }
        this.packageAliasingMapper.addPackageAlias(name, pkgName);
    }

    public void aliasField(String alias, Class definedIn, String fieldName) {
        if (this.fieldAliasingMapper == null) {
            throw new InitializationException("No " + FieldAliasingMapper.class.getName() + " available");
        }
        this.fieldAliasingMapper.addFieldAlias(alias, definedIn, fieldName);
    }

    public void aliasAttribute(String alias, String attributeName) {
        if (this.attributeAliasingMapper == null) {
            throw new InitializationException("No " + AttributeAliasingMapper.class.getName() + " available");
        }
        this.attributeAliasingMapper.addAliasFor(attributeName, alias);
    }

    public void aliasSystemAttribute(String alias, String systemAttributeName) {
        if (this.systemAttributeAliasingMapper == null) {
            throw new InitializationException("No " + SystemAttributeAliasingMapper.class.getName() + " available");
        }
        this.systemAttributeAliasingMapper.addAliasFor(systemAttributeName, alias);
    }

    public void aliasAttribute(Class definedIn, String attributeName, String alias) {
        aliasField(alias, definedIn, attributeName);
        useAttributeFor(definedIn, attributeName);
    }

    public void useAttributeFor(String fieldName, Class type) {
        if (this.attributeMapper == null) {
            throw new InitializationException("No " + AttributeMapper.class.getName() + " available");
        }
        this.attributeMapper.addAttributeFor(fieldName, type);
    }

    public void useAttributeFor(Class definedIn, String fieldName) {
        if (this.attributeMapper == null) {
            throw new InitializationException("No " + AttributeMapper.class.getName() + " available");
        }
        this.attributeMapper.addAttributeFor(definedIn, fieldName);
    }

    public void useAttributeFor(Class type) {
        if (this.attributeMapper == null) {
            throw new InitializationException("No " + AttributeMapper.class.getName() + " available");
        }
        this.attributeMapper.addAttributeFor(type);
    }

    public void addDefaultImplementation(Class defaultImplementation, Class ofType) {
        if (this.defaultImplementationsMapper == null) {
            throw new InitializationException("No " + DefaultImplementationsMapper.class.getName() + " available");
        }
        this.defaultImplementationsMapper.addDefaultImplementation(defaultImplementation, ofType);
    }

    public void addImmutableType(Class type) {
        if (this.immutableTypesMapper == null) {
            throw new InitializationException("No " + ImmutableTypesMapper.class.getName() + " available");
        }
        this.immutableTypesMapper.addImmutableType(type);
    }

    public void registerConverter(Converter converter) {
        registerConverter(converter, (int) PRIORITY_NORMAL);
    }

    public void registerConverter(Converter converter, int priority) {
        if (this.converterRegistry != null) {
            this.converterRegistry.registerConverter(converter, priority);
        }
    }

    public void registerConverter(SingleValueConverter converter) {
        registerConverter(converter, (int) PRIORITY_NORMAL);
    }

    public void registerConverter(SingleValueConverter converter, int priority) {
        if (this.converterRegistry != null) {
            this.converterRegistry.registerConverter(new SingleValueConverterWrapper(converter), priority);
        }
    }

    public void registerLocalConverter(Class definedIn, String fieldName, Converter converter) {
        if (this.localConversionMapper == null) {
            throw new InitializationException("No " + LocalConversionMapper.class.getName() + " available");
        }
        this.localConversionMapper.registerLocalConverter(definedIn, fieldName, converter);
    }

    public void registerLocalConverter(Class definedIn, String fieldName, SingleValueConverter converter) {
        registerLocalConverter(definedIn, fieldName, new SingleValueConverterWrapper(converter));
    }

    public Mapper getMapper() {
        return this.mapper;
    }

    public ReflectionProvider getReflectionProvider() {
        return this.reflectionProvider;
    }

    public ConverterLookup getConverterLookup() {
        return this.converterLookup;
    }

    public void setMode(int mode) {
        switch (mode) {
            case NO_REFERENCES /*1001*/:
                setMarshallingStrategy(new TreeMarshallingStrategy());
            case ID_REFERENCES /*1002*/:
                setMarshallingStrategy(new ReferenceByIdMarshallingStrategy());
            case XPATH_RELATIVE_REFERENCES /*1003*/:
                setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.RELATIVE));
            case XPATH_ABSOLUTE_REFERENCES /*1004*/:
                setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.ABSOLUTE));
            case SINGLE_NODE_XPATH_RELATIVE_REFERENCES /*1005*/:
                setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.RELATIVE | ReferenceByXPathMarshallingStrategy.SINGLE_NODE));
            case SINGLE_NODE_XPATH_ABSOLUTE_REFERENCES /*1006*/:
                setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.ABSOLUTE | ReferenceByXPathMarshallingStrategy.SINGLE_NODE));
            default:
                throw new IllegalArgumentException("Unknown mode : " + mode);
        }
    }

    public void addImplicitCollection(Class ownerType, String fieldName) {
        addImplicitCollection(ownerType, fieldName, null, null);
    }

    public void addImplicitCollection(Class ownerType, String fieldName, Class itemType) {
        addImplicitCollection(ownerType, fieldName, null, itemType);
    }

    public void addImplicitCollection(Class ownerType, String fieldName, String itemFieldName, Class itemType) {
        addImplicitMap(ownerType, fieldName, itemFieldName, itemType, null);
    }

    public void addImplicitArray(Class ownerType, String fieldName) {
        addImplicitCollection(ownerType, fieldName);
    }

    public void addImplicitArray(Class ownerType, String fieldName, Class itemType) {
        addImplicitCollection(ownerType, fieldName, itemType);
    }

    public void addImplicitArray(Class ownerType, String fieldName, String itemName) {
        addImplicitCollection(ownerType, fieldName, itemName, null);
    }

    public void addImplicitMap(Class ownerType, String fieldName, Class itemType, String keyFieldName) {
        addImplicitMap(ownerType, fieldName, null, itemType, keyFieldName);
    }

    public void addImplicitMap(Class ownerType, String fieldName, String itemName, Class itemType, String keyFieldName) {
        if (this.implicitCollectionMapper == null) {
            throw new InitializationException("No " + ImplicitCollectionMapper.class.getName() + " available");
        }
        this.implicitCollectionMapper.add(ownerType, fieldName, itemName, itemType, keyFieldName);
    }

    public DataHolder newDataHolder() {
        return new MapBackedDataHolder();
    }

    public ObjectOutputStream createObjectOutputStream(Writer writer) throws IOException {
        return createObjectOutputStream(this.hierarchicalStreamDriver.createWriter(writer), "object-stream");
    }

    public ObjectOutputStream createObjectOutputStream(HierarchicalStreamWriter writer) throws IOException {
        return createObjectOutputStream(writer, "object-stream");
    }

    public ObjectOutputStream createObjectOutputStream(Writer writer, String rootNodeName) throws IOException {
        return createObjectOutputStream(this.hierarchicalStreamDriver.createWriter(writer), rootNodeName);
    }

    public ObjectOutputStream createObjectOutputStream(OutputStream out) throws IOException {
        return createObjectOutputStream(this.hierarchicalStreamDriver.createWriter(out), "object-stream");
    }

    public ObjectOutputStream createObjectOutputStream(OutputStream out, String rootNodeName) throws IOException {
        return createObjectOutputStream(this.hierarchicalStreamDriver.createWriter(out), rootNodeName);
    }

    public ObjectOutputStream createObjectOutputStream(HierarchicalStreamWriter writer, String rootNodeName) throws IOException {
        StatefulWriter statefulWriter = new StatefulWriter(writer);
        statefulWriter.startNode(rootNodeName, null);
        return new CustomObjectOutputStream(new C11261(statefulWriter));
    }

    public ObjectInputStream createObjectInputStream(Reader xmlReader) throws IOException {
        return createObjectInputStream(this.hierarchicalStreamDriver.createReader(xmlReader));
    }

    public ObjectInputStream createObjectInputStream(InputStream in) throws IOException {
        return createObjectInputStream(this.hierarchicalStreamDriver.createReader(in));
    }

    public ObjectInputStream createObjectInputStream(HierarchicalStreamReader reader) throws IOException {
        return new CustomObjectInputStream(new C11272(reader), this.classLoaderReference);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoaderReference.setReference(classLoader);
    }

    public ClassLoader getClassLoader() {
        return this.classLoaderReference.getReference();
    }

    public ClassLoaderReference getClassLoaderReference() {
        return this.classLoaderReference;
    }

    public void omitField(Class definedIn, String fieldName) {
        if (this.fieldAliasingMapper == null) {
            throw new InitializationException("No " + FieldAliasingMapper.class.getName() + " available");
        }
        this.fieldAliasingMapper.omitField(definedIn, fieldName);
    }

    public void ignoreUnknownElements() {
        ignoreUnknownElements(IGNORE_ALL);
    }

    public void ignoreUnknownElements(String pattern) {
        ignoreUnknownElements(Pattern.compile(pattern));
    }

    private void ignoreUnknownElements(Pattern pattern) {
        if (this.fieldAliasingMapper == null) {
            throw new InitializationException("No " + FieldAliasingMapper.class.getName() + " available");
        }
        this.fieldAliasingMapper.addFieldsToIgnore(pattern);
    }

    public void processAnnotations(Class[] types) {
        if (this.annotationConfiguration == null) {
            throw new InitializationException("No com.thoughtworks.xstream.mapper.AnnotationMapper available");
        }
        this.annotationConfiguration.processAnnotations(types);
    }

    public void processAnnotations(Class type) {
        processAnnotations(new Class[]{type});
    }

    public void autodetectAnnotations(boolean mode) {
        if (this.annotationConfiguration != null) {
            this.annotationConfiguration.autodetectAnnotations(mode);
        }
    }
}
