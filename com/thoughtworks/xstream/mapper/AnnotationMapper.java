package com.thoughtworks.xstream.mapper;

import com.thoughtworks.xstream.InitializationException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamConverters;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamImplicitCollection;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.ConverterMatcher;
import com.thoughtworks.xstream.converters.ConverterRegistry;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.SingleValueConverterWrapper;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.core.util.DependencyInjectionFactory;
import com.thoughtworks.xstream.core.util.TypedNull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;

public class AnnotationMapper extends MapperWrapper implements AnnotationConfiguration {
    private final Set<Class<?>> annotatedTypes;
    private transient Object[] arguments;
    private transient AttributeMapper attributeMapper;
    private transient ClassAliasingMapper classAliasingMapper;
    private final Map<Class<?>, Map<List<Object>, Converter>> converterCache;
    private final ConverterRegistry converterRegistry;
    private transient DefaultImplementationsMapper defaultImplementationsMapper;
    private transient FieldAliasingMapper fieldAliasingMapper;
    private transient ImplicitCollectionMapper implicitCollectionMapper;
    private transient LocalConversionMapper localConversionMapper;
    private boolean locked;

    /* renamed from: com.thoughtworks.xstream.mapper.AnnotationMapper.1 */
    class C09041 extends LinkedHashSet<Type> {
        final /* synthetic */ Set val$processedTypes;
        final /* synthetic */ Set val$types;

        C09041(Set set, Set set2) {
            this.val$types = set;
            this.val$processedTypes = set2;
        }

        public boolean add(Type o) {
            if (o instanceof Class) {
                return this.val$types.add((Class) o);
            }
            return (o == null || this.val$processedTypes.contains(o)) ? false : super.add(o);
        }
    }

    private final class UnprocessedTypesSet extends LinkedHashSet<Class<?>> {
        private UnprocessedTypesSet() {
        }

        public boolean add(Class<?> type) {
            boolean ret = false;
            if (type != null) {
                while (type.isArray()) {
                    type = type.getComponentType();
                }
                String name = type.getName();
                if (!(name.startsWith("java.") || name.startsWith("javax."))) {
                    if (!AnnotationMapper.this.annotatedTypes.contains(type)) {
                        ret = super.add(type);
                    }
                    if (ret) {
                        XStreamInclude inc = (XStreamInclude) type.getAnnotation(XStreamInclude.class);
                        if (inc != null) {
                            Class<?>[] incTypes = inc.value();
                            if (incTypes != null) {
                                for (Class incType : incTypes) {
                                    add(incType);
                                }
                            }
                        }
                    }
                }
            }
            return ret;
        }
    }

    public AnnotationMapper(Mapper wrapped, ConverterRegistry converterRegistry, ConverterLookup converterLookup, ClassLoaderReference classLoaderReference, ReflectionProvider reflectionProvider) {
        super(wrapped);
        this.converterCache = new HashMap();
        this.annotatedTypes = Collections.synchronizedSet(new HashSet());
        this.converterRegistry = converterRegistry;
        this.annotatedTypes.add(Object.class);
        setupMappers();
        this.locked = true;
        ClassLoader classLoader = classLoaderReference.getReference();
        Object[] objArr = new Object[6];
        objArr[0] = this;
        objArr[1] = classLoaderReference;
        objArr[2] = reflectionProvider;
        objArr[3] = converterLookup;
        objArr[4] = new JVM();
        if (classLoader == null) {
            classLoader = new TypedNull(ClassLoader.class);
        }
        objArr[5] = classLoader;
        this.arguments = objArr;
    }

    public AnnotationMapper(Mapper wrapped, ConverterRegistry converterRegistry, ConverterLookup converterLookup, ClassLoader classLoader, ReflectionProvider reflectionProvider, JVM jvm) {
        this(wrapped, converterRegistry, converterLookup, new ClassLoaderReference(classLoader), reflectionProvider);
    }

    public String realMember(Class type, String serialized) {
        if (!this.locked) {
            processAnnotations(type);
        }
        return super.realMember(type, serialized);
    }

    public String serializedClass(Class type) {
        if (!this.locked) {
            processAnnotations(type);
        }
        return super.serializedClass(type);
    }

    public Class defaultImplementationOf(Class type) {
        if (!this.locked) {
            processAnnotations(type);
        }
        Class defaultImplementation = super.defaultImplementationOf(type);
        if (!this.locked) {
            processAnnotations(defaultImplementation);
        }
        return defaultImplementation;
    }

    public Converter getLocalConverter(Class definedIn, String fieldName) {
        if (!this.locked) {
            processAnnotations(definedIn);
        }
        return super.getLocalConverter(definedIn, fieldName);
    }

    public void autodetectAnnotations(boolean mode) {
        this.locked = !mode;
    }

    public void processAnnotations(Class[] initialTypes) {
        if (initialTypes != null && initialTypes.length != 0) {
            this.locked = true;
            Set<Class<?>> types = new UnprocessedTypesSet();
            for (Class initialType : initialTypes) {
                types.add(initialType);
            }
            processTypes(types);
        }
    }

    private void processAnnotations(Class initialType) {
        if (initialType != null) {
            Set<Class<?>> types = new UnprocessedTypesSet();
            types.add(initialType);
            processTypes(types);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void processTypes(java.util.Set<java.lang.Class<?>> r8) {
        /*
        r7 = this;
    L_0x0000:
        r5 = r8.isEmpty();
        if (r5 != 0) goto L_0x0091;
    L_0x0006:
        r3 = r8.iterator();
        r4 = r3.next();
        r4 = (java.lang.Class) r4;
        r3.remove();
        monitor-enter(r4);
        r5 = r7.annotatedTypes;	 Catch:{ all -> 0x001e }
        r5 = r5.contains(r4);	 Catch:{ all -> 0x001e }
        if (r5 == 0) goto L_0x0021;
    L_0x001c:
        monitor-exit(r4);	 Catch:{ all -> 0x001e }
        goto L_0x0000;
    L_0x001e:
        r5 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x001e }
        throw r5;
    L_0x0021:
        r5 = r4.isPrimitive();	 Catch:{ all -> 0x0082 }
        if (r5 == 0) goto L_0x002e;
    L_0x0027:
        r5 = r7.annotatedTypes;	 Catch:{ all -> 0x001e }
        r5.add(r4);	 Catch:{ all -> 0x001e }
        monitor-exit(r4);	 Catch:{ all -> 0x001e }
        goto L_0x0000;
    L_0x002e:
        r7.addParametrizedTypes(r4, r8);	 Catch:{ all -> 0x0082 }
        r7.processConverterAnnotations(r4);	 Catch:{ all -> 0x0082 }
        r7.processAliasAnnotation(r4, r8);	 Catch:{ all -> 0x0082 }
        r7.processAliasTypeAnnotation(r4);	 Catch:{ all -> 0x0082 }
        r5 = r4.isInterface();	 Catch:{ all -> 0x0082 }
        if (r5 == 0) goto L_0x0047;
    L_0x0040:
        r5 = r7.annotatedTypes;	 Catch:{ all -> 0x001e }
        r5.add(r4);	 Catch:{ all -> 0x001e }
        monitor-exit(r4);	 Catch:{ all -> 0x001e }
        goto L_0x0000;
    L_0x0047:
        r7.processImplicitCollectionAnnotation(r4);	 Catch:{ all -> 0x0082 }
        r1 = r4.getDeclaredFields();	 Catch:{ all -> 0x0082 }
        r2 = 0;
    L_0x004f:
        r5 = r1.length;	 Catch:{ all -> 0x0082 }
        if (r2 >= r5) goto L_0x0089;
    L_0x0052:
        r0 = r1[r2];	 Catch:{ all -> 0x0082 }
        r5 = r0.isEnumConstant();	 Catch:{ all -> 0x0082 }
        if (r5 != 0) goto L_0x0062;
    L_0x005a:
        r5 = r0.getModifiers();	 Catch:{ all -> 0x0082 }
        r5 = r5 & 136;
        if (r5 <= 0) goto L_0x0065;
    L_0x0062:
        r2 = r2 + 1;
        goto L_0x004f;
    L_0x0065:
        r5 = r0.getGenericType();	 Catch:{ all -> 0x0082 }
        r7.addParametrizedTypes(r5, r8);	 Catch:{ all -> 0x0082 }
        r5 = r0.isSynthetic();	 Catch:{ all -> 0x0082 }
        if (r5 != 0) goto L_0x0062;
    L_0x0072:
        r7.processFieldAliasAnnotation(r0);	 Catch:{ all -> 0x0082 }
        r7.processAsAttributeAnnotation(r0);	 Catch:{ all -> 0x0082 }
        r7.processImplicitAnnotation(r0);	 Catch:{ all -> 0x0082 }
        r7.processOmitFieldAnnotation(r0);	 Catch:{ all -> 0x0082 }
        r7.processLocalConverterAnnotation(r0);	 Catch:{ all -> 0x0082 }
        goto L_0x0062;
    L_0x0082:
        r5 = move-exception;
        r6 = r7.annotatedTypes;	 Catch:{ all -> 0x001e }
        r6.add(r4);	 Catch:{ all -> 0x001e }
        throw r5;	 Catch:{ all -> 0x001e }
    L_0x0089:
        r5 = r7.annotatedTypes;	 Catch:{ all -> 0x001e }
        r5.add(r4);	 Catch:{ all -> 0x001e }
        monitor-exit(r4);	 Catch:{ all -> 0x001e }
        goto L_0x0000;
    L_0x0091:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.thoughtworks.xstream.mapper.AnnotationMapper.processTypes(java.util.Set):void");
    }

    private void addParametrizedTypes(Type type, Set<Class<?>> types) {
        Set<Type> processedTypes = new HashSet();
        Set<Type> localTypes = new C09041(types, processedTypes);
        while (type != null) {
            processedTypes.add(type);
            if (type instanceof Class) {
                Class<?> clazz = (Class) type;
                types.add(clazz);
                if (!clazz.isPrimitive()) {
                    for (TypeVariable<?> add : clazz.getTypeParameters()) {
                        localTypes.add(add);
                    }
                    localTypes.add(clazz.getGenericSuperclass());
                    for (Type iface : clazz.getGenericInterfaces()) {
                        localTypes.add(iface);
                    }
                }
            } else if (type instanceof TypeVariable) {
                for (Type bound : ((TypeVariable) type).getBounds()) {
                    localTypes.add(bound);
                }
            } else if (type instanceof ParameterizedType) {
                ParameterizedType parametrizedType = (ParameterizedType) type;
                localTypes.add(parametrizedType.getRawType());
                for (Type actualArgument : parametrizedType.getActualTypeArguments()) {
                    localTypes.add(actualArgument);
                }
            } else if (type instanceof GenericArrayType) {
                localTypes.add(((GenericArrayType) type).getGenericComponentType());
            }
            if (localTypes.isEmpty()) {
                type = null;
            } else {
                Iterator<Type> iter = localTypes.iterator();
                type = (Type) iter.next();
                iter.remove();
            }
        }
    }

    private void processConverterAnnotations(Class<?> type) {
        if (this.converterRegistry != null) {
            XStreamConverters convertersAnnotation = (XStreamConverters) type.getAnnotation(XStreamConverters.class);
            XStreamConverter converterAnnotation = (XStreamConverter) type.getAnnotation(XStreamConverter.class);
            List<XStreamConverter> annotations = convertersAnnotation != null ? new ArrayList(Arrays.asList(convertersAnnotation.value())) : new ArrayList();
            if (converterAnnotation != null) {
                annotations.add(converterAnnotation);
            }
            for (XStreamConverter annotation : annotations) {
                Converter converter = cacheConverter(annotation, converterAnnotation != null ? type : null);
                if (converter != null) {
                    if (converterAnnotation != null || converter.canConvert(type)) {
                        this.converterRegistry.registerConverter(converter, annotation.priority());
                    } else {
                        throw new InitializationException("Converter " + annotation.value().getName() + " cannot handle annotated class " + type.getName());
                    }
                }
            }
        }
    }

    private void processAliasAnnotation(Class<?> type, Set<Class<?>> types) {
        XStreamAlias aliasAnnotation = (XStreamAlias) type.getAnnotation(XStreamAlias.class);
        if (aliasAnnotation == null) {
            return;
        }
        if (this.classAliasingMapper == null) {
            throw new InitializationException("No " + ClassAliasingMapper.class.getName() + " available");
        }
        this.classAliasingMapper.addClassAlias(aliasAnnotation.value(), type);
        if (aliasAnnotation.impl() != Void.class) {
            this.defaultImplementationsMapper.addDefaultImplementation(aliasAnnotation.impl(), type);
            if (type.isInterface()) {
                types.add(aliasAnnotation.impl());
            }
        }
    }

    private void processAliasTypeAnnotation(Class<?> type) {
        XStreamAliasType aliasAnnotation = (XStreamAliasType) type.getAnnotation(XStreamAliasType.class);
        if (aliasAnnotation == null) {
            return;
        }
        if (this.classAliasingMapper == null) {
            throw new InitializationException("No " + ClassAliasingMapper.class.getName() + " available");
        }
        this.classAliasingMapper.addTypeAlias(aliasAnnotation.value(), type);
    }

    @Deprecated
    private void processImplicitCollectionAnnotation(Class<?> type) {
        XStreamImplicitCollection implicitColAnnotation = (XStreamImplicitCollection) type.getAnnotation(XStreamImplicitCollection.class);
        if (implicitColAnnotation == null) {
            return;
        }
        if (this.implicitCollectionMapper == null) {
            throw new InitializationException("No " + ImplicitCollectionMapper.class.getName() + " available");
        }
        String fieldName = implicitColAnnotation.value();
        String itemFieldName = implicitColAnnotation.item();
        try {
            Class itemType = null;
            Type genericType = type.getDeclaredField(fieldName).getGenericType();
            if (genericType instanceof ParameterizedType) {
                itemType = getClass(((ParameterizedType) genericType).getActualTypeArguments()[0]);
            }
            if (itemType == null) {
                this.implicitCollectionMapper.add(type, fieldName, null, Object.class);
            } else if (itemFieldName.equals(XmlPullParser.NO_NAMESPACE)) {
                this.implicitCollectionMapper.add(type, fieldName, null, itemType);
            } else {
                this.implicitCollectionMapper.add(type, fieldName, itemFieldName, itemType);
            }
        } catch (NoSuchFieldException e) {
            throw new InitializationException(type.getName() + " does not have a field named '" + fieldName + "' as required by " + XStreamImplicitCollection.class.getName());
        }
    }

    private void processFieldAliasAnnotation(Field field) {
        XStreamAlias aliasAnnotation = (XStreamAlias) field.getAnnotation(XStreamAlias.class);
        if (aliasAnnotation == null) {
            return;
        }
        if (this.fieldAliasingMapper == null) {
            throw new InitializationException("No " + FieldAliasingMapper.class.getName() + " available");
        }
        this.fieldAliasingMapper.addFieldAlias(aliasAnnotation.value(), field.getDeclaringClass(), field.getName());
    }

    private void processAsAttributeAnnotation(Field field) {
        if (((XStreamAsAttribute) field.getAnnotation(XStreamAsAttribute.class)) == null) {
            return;
        }
        if (this.attributeMapper == null) {
            throw new InitializationException("No " + AttributeMapper.class.getName() + " available");
        }
        this.attributeMapper.addAttributeFor(field);
    }

    private void processImplicitAnnotation(Field field) {
        String str = null;
        XStreamImplicit implicitAnnotation = (XStreamImplicit) field.getAnnotation(XStreamImplicit.class);
        if (implicitAnnotation == null) {
            return;
        }
        if (this.implicitCollectionMapper == null) {
            throw new InitializationException("No " + ImplicitCollectionMapper.class.getName() + " available");
        }
        String fieldName = field.getName();
        String itemFieldName = implicitAnnotation.itemFieldName();
        String keyFieldName = implicitAnnotation.keyFieldName();
        boolean isMap = Map.class.isAssignableFrom(field.getType());
        Class itemType = null;
        if (!field.getType().isArray()) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                itemType = getClass(((ParameterizedType) genericType).getActualTypeArguments()[isMap ? 1 : 0]);
            }
        }
        if (isMap) {
            String str2;
            ImplicitCollectionMapper implicitCollectionMapper = this.implicitCollectionMapper;
            Class declaringClass = field.getDeclaringClass();
            if (itemFieldName == null || XmlPullParser.NO_NAMESPACE.equals(itemFieldName)) {
                str2 = null;
            } else {
                str2 = itemFieldName;
            }
            if (!(keyFieldName == null || XmlPullParser.NO_NAMESPACE.equals(keyFieldName))) {
                str = keyFieldName;
            }
            implicitCollectionMapper.add(declaringClass, fieldName, str2, itemType, str);
        } else if (itemFieldName == null || XmlPullParser.NO_NAMESPACE.equals(itemFieldName)) {
            this.implicitCollectionMapper.add(field.getDeclaringClass(), fieldName, itemType);
        } else {
            this.implicitCollectionMapper.add(field.getDeclaringClass(), fieldName, itemFieldName, itemType);
        }
    }

    private void processOmitFieldAnnotation(Field field) {
        if (((XStreamOmitField) field.getAnnotation(XStreamOmitField.class)) == null) {
            return;
        }
        if (this.fieldAliasingMapper == null) {
            throw new InitializationException("No " + FieldAliasingMapper.class.getName() + " available");
        }
        this.fieldAliasingMapper.omitField(field.getDeclaringClass(), field.getName());
    }

    private void processLocalConverterAnnotation(Field field) {
        XStreamConverter annotation = (XStreamConverter) field.getAnnotation(XStreamConverter.class);
        if (annotation != null) {
            Converter converter = cacheConverter(annotation, field.getType());
            if (converter == null) {
                return;
            }
            if (this.localConversionMapper == null) {
                throw new InitializationException("No " + LocalConversionMapper.class.getName() + " available");
            }
            this.localConversionMapper.registerLocalConverter(field.getDeclaringClass(), field.getName(), converter);
        }
    }

    private Converter cacheConverter(XStreamConverter annotation, Class targetType) {
        Converter result = null;
        List<Object> parameter = new ArrayList();
        if (targetType != null && annotation.useImplicitType()) {
            parameter.add(targetType);
        }
        List<Object> arrays = new ArrayList();
        arrays.add(annotation.booleans());
        arrays.add(annotation.bytes());
        arrays.add(annotation.chars());
        arrays.add(annotation.doubles());
        arrays.add(annotation.floats());
        arrays.add(annotation.ints());
        arrays.add(annotation.longs());
        arrays.add(annotation.shorts());
        arrays.add(annotation.strings());
        arrays.add(annotation.types());
        for (Object array : arrays) {
            if (array != null) {
                int length = Array.getLength(array);
                for (int i = 0; i < length; i++) {
                    Object object = Array.get(array, i);
                    if (!parameter.contains(object)) {
                        parameter.add(object);
                    }
                }
            }
        }
        Class<? extends ConverterMatcher> converterType = annotation.value();
        Map<List<Object>, Converter> converterMapping = (Map) this.converterCache.get(converterType);
        if (converterMapping != null) {
            result = (Converter) converterMapping.get(parameter);
        }
        if (result != null) {
            return result;
        }
        Object[] args;
        int size = parameter.size();
        if (size > 0) {
            args = new Object[(this.arguments.length + size)];
            System.arraycopy(this.arguments, 0, args, size, this.arguments.length);
            System.arraycopy(parameter.toArray(new Object[size]), 0, args, 0, size);
        } else {
            args = this.arguments;
        }
        try {
            Converter converter;
            if (SingleValueConverter.class.isAssignableFrom(converterType)) {
                if (!Converter.class.isAssignableFrom(converterType)) {
                    converter = new SingleValueConverterWrapper((SingleValueConverter) DependencyInjectionFactory.newInstance(converterType, args));
                    if (converterMapping == null) {
                        converterMapping = new HashMap();
                        this.converterCache.put(converterType, converterMapping);
                    }
                    converterMapping.put(parameter, converter);
                    return converter;
                }
            }
            converter = (Converter) DependencyInjectionFactory.newInstance(converterType, args);
            if (converterMapping == null) {
                converterMapping = new HashMap();
                this.converterCache.put(converterType, converterMapping);
            }
            converterMapping.put(parameter, converter);
            return converter;
        } catch (Exception e) {
            String str;
            StringBuilder append = new StringBuilder().append("Cannot instantiate converter ").append(converterType.getName());
            if (targetType != null) {
                str = " for type " + targetType.getName();
            } else {
                str = XmlPullParser.NO_NAMESPACE;
            }
            throw new InitializationException(append.append(str).toString(), e);
        }
    }

    private Class<?> getClass(Type typeArgument) {
        if (typeArgument instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) typeArgument).getRawType();
        }
        if (typeArgument instanceof Class) {
            return (Class) typeArgument;
        }
        return null;
    }

    private void setupMappers() {
        this.classAliasingMapper = (ClassAliasingMapper) lookupMapperOfType(ClassAliasingMapper.class);
        this.defaultImplementationsMapper = (DefaultImplementationsMapper) lookupMapperOfType(DefaultImplementationsMapper.class);
        this.implicitCollectionMapper = (ImplicitCollectionMapper) lookupMapperOfType(ImplicitCollectionMapper.class);
        this.fieldAliasingMapper = (FieldAliasingMapper) lookupMapperOfType(FieldAliasingMapper.class);
        this.attributeMapper = (AttributeMapper) lookupMapperOfType(AttributeMapper.class);
        this.localConversionMapper = (LocalConversionMapper) lookupMapperOfType(LocalConversionMapper.class);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        int max = this.arguments.length - 2;
        out.writeInt(max);
        for (int i = 0; i < max; i++) {
            out.writeObject(this.arguments[i]);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setupMappers();
        int max = in.readInt();
        this.arguments = new Object[(max + 2)];
        for (int i = 0; i < max; i++) {
            this.arguments[i] = in.readObject();
            if (this.arguments[i] instanceof ClassLoaderReference) {
                this.arguments[max + 1] = ((ClassLoaderReference) this.arguments[i]).getReference();
            }
        }
        this.arguments[max] = new JVM();
    }
}
