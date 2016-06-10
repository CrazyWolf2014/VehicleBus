package com.thoughtworks.xstream.converters.extended;

import com.tencent.mm.sdk.plugin.BaseProfile;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.ConverterMatcher;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.DuplicateFieldException;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider.Visitor;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.core.util.FastField;
import com.thoughtworks.xstream.core.util.HierarchicalStreams;
import com.thoughtworks.xstream.core.util.Primitives;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ToAttributedValueConverter implements Converter {
    private static final String STRUCTURE_MARKER = "";
    private final Mapper enumMapper;
    private final ConverterLookup lookup;
    private final Mapper mapper;
    private final ReflectionProvider reflectionProvider;
    private final Class type;
    private final Field valueField;

    /* renamed from: com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter.1 */
    class C11281 implements Visitor {
        final /* synthetic */ Map val$defaultFieldDefinition;
        final /* synthetic */ Class[] val$definingType;
        final /* synthetic */ Class[] val$fieldType;
        final /* synthetic */ Object[] val$realValue;
        final /* synthetic */ Class val$sourceType;
        final /* synthetic */ String[] val$tagValue;
        final /* synthetic */ HierarchicalStreamWriter val$writer;

        C11281(Map map, Class cls, Class[] clsArr, Class[] clsArr2, Object[] objArr, String[] strArr, HierarchicalStreamWriter hierarchicalStreamWriter) {
            this.val$defaultFieldDefinition = map;
            this.val$sourceType = cls;
            this.val$definingType = clsArr;
            this.val$fieldType = clsArr2;
            this.val$realValue = objArr;
            this.val$tagValue = strArr;
            this.val$writer = hierarchicalStreamWriter;
        }

        public void visit(String fieldName, Class type, Class definedIn, Object value) {
            if (ToAttributedValueConverter.this.mapper.shouldSerializeMember(definedIn, fieldName)) {
                ConversionException exception;
                FastField field = new FastField(definedIn, fieldName);
                String alias = ToAttributedValueConverter.this.mapper.serializedMember(definedIn, fieldName);
                if (!this.val$defaultFieldDefinition.containsKey(alias)) {
                    this.val$defaultFieldDefinition.put(alias, ToAttributedValueConverter.this.reflectionProvider.getField(this.val$sourceType, fieldName));
                } else if (!ToAttributedValueConverter.this.fieldIsEqual(field)) {
                    exception = new ConversionException("Cannot write attribute twice for object");
                    exception.add(BaseProfile.COL_ALIAS, alias);
                    exception.add(SharedPref.TYPE, this.val$sourceType.getName());
                    throw exception;
                }
                ConverterMatcher converter = UseAttributeForEnumMapper.isEnum(type) ? ToAttributedValueConverter.this.enumMapper.getConverterFromItemType(null, type, null) : ToAttributedValueConverter.this.mapper.getLocalConverter(definedIn, fieldName);
                if (converter == null) {
                    converter = ToAttributedValueConverter.this.lookup.lookupConverterForType(type);
                }
                if (value != null) {
                    boolean isValueField;
                    if (ToAttributedValueConverter.this.valueField == null || !ToAttributedValueConverter.this.fieldIsEqual(field)) {
                        isValueField = false;
                    } else {
                        isValueField = true;
                    }
                    if (isValueField) {
                        this.val$definingType[0] = definedIn;
                        this.val$fieldType[0] = type;
                        this.val$realValue[0] = value;
                        this.val$tagValue[0] = ToAttributedValueConverter.STRUCTURE_MARKER;
                    }
                    if (converter instanceof SingleValueConverter) {
                        String str = ((SingleValueConverter) converter).toString(value);
                        if (isValueField) {
                            this.val$tagValue[0] = str;
                        } else if (str != null) {
                            this.val$writer.addAttribute(alias, str);
                        }
                    } else if (!isValueField) {
                        exception = new ConversionException("Cannot write element as attribute");
                        exception.add(BaseProfile.COL_ALIAS, alias);
                        exception.add(SharedPref.TYPE, this.val$sourceType.getName());
                        throw exception;
                    }
                }
            }
        }
    }

    public ToAttributedValueConverter(Class type, Mapper mapper, ReflectionProvider reflectionProvider, ConverterLookup lookup, String valueFieldName) {
        this(type, mapper, reflectionProvider, lookup, valueFieldName, null);
    }

    public ToAttributedValueConverter(Class type, Mapper mapper, ReflectionProvider reflectionProvider, ConverterLookup lookup, String valueFieldName, Class valueDefinedIn) {
        Mapper mapper2 = null;
        this.type = type;
        this.mapper = mapper;
        this.reflectionProvider = reflectionProvider;
        this.lookup = lookup;
        if (valueFieldName == null) {
            this.valueField = null;
        } else {
            if (valueDefinedIn == null) {
                valueDefinedIn = type;
            }
            try {
                Field field = valueDefinedIn.getDeclaredField(valueFieldName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                this.valueField = field;
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException(e.getMessage() + ": " + valueFieldName);
            }
        }
        if (JVM.is15()) {
            mapper2 = UseAttributeForEnumMapper.createEnumMapper(mapper);
        }
        this.enumMapper = mapper2;
    }

    public boolean canConvert(Class type) {
        return this.type == type;
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Class sourceType = source.getClass();
        String[] tagValue = new String[1];
        Object[] realValue = new Object[1];
        Class[] fieldType = new Class[1];
        Object obj = source;
        this.reflectionProvider.visitSerializableFields(obj, new C11281(new HashMap(), sourceType, new Class[1], fieldType, realValue, tagValue, writer));
        if (tagValue[0] != null) {
            Class actualType = realValue[0].getClass();
            Class defaultType = this.mapper.defaultImplementationOf(fieldType[0]);
            if (!actualType.equals(defaultType)) {
                String serializedClassName = this.mapper.serializedClass(actualType);
                if (!serializedClassName.equals(this.mapper.serializedClass(defaultType))) {
                    String attributeName = this.mapper.aliasForSystemAttribute("class");
                    if (attributeName != null) {
                        writer.addAttribute(attributeName, serializedClassName);
                    }
                }
            }
            if (tagValue[0] == STRUCTURE_MARKER) {
                context.convertAnother(realValue[0]);
                return;
            }
            writer.setValue(tagValue[0]);
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String fieldName;
        Object result = this.reflectionProvider.newInstance(context.getRequiredType());
        Class resultType = result.getClass();
        Set seenFields = new HashSet();
        Iterator it = reader.getAttributeNames();
        Set systemAttributes = new HashSet();
        systemAttributes.add(this.mapper.aliasForSystemAttribute("class"));
        while (it.hasNext()) {
            Field field;
            Class type;
            ConversionException exception;
            Object value;
            String attrName = (String) it.next();
            if (!systemAttributes.contains(attrName)) {
                fieldName = this.mapper.realMember(resultType, attrName);
                field = this.reflectionProvider.getFieldOrNull(resultType, fieldName);
                if (!(field == null || Modifier.isTransient(field.getModifiers()))) {
                    ConverterMatcher converter;
                    type = field.getType();
                    Class declaringClass = field.getDeclaringClass();
                    if (UseAttributeForEnumMapper.isEnum(type)) {
                        converter = this.enumMapper.getConverterFromItemType(null, type, null);
                    } else {
                        converter = this.mapper.getLocalConverter(declaringClass, fieldName);
                    }
                    if (converter == null) {
                        converter = this.lookup.lookupConverterForType(type);
                    }
                    if (!(converter instanceof SingleValueConverter)) {
                        exception = new ConversionException("Cannot read field as a single value for object");
                        exception.add("field", fieldName);
                        exception.add(SharedPref.TYPE, resultType.getName());
                        throw exception;
                    } else if (converter != null) {
                        value = ((SingleValueConverter) converter).fromString(reader.getAttribute(attrName));
                        if (type.isPrimitive()) {
                            type = Primitives.box(type);
                        }
                        if (value != null) {
                            if (!type.isAssignableFrom(value.getClass())) {
                                exception = new ConversionException("Cannot assign object to type");
                                exception.add("object type", value.getClass().getName());
                                exception.add("target type", type.getName());
                                throw exception;
                            }
                        }
                        this.reflectionProvider.writeField(result, fieldName, value, declaringClass);
                        if (!seenFields.add(new FastField(declaringClass, fieldName))) {
                            throw new DuplicateFieldException(fieldName + " [" + declaringClass.getName() + "]");
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        if (this.valueField != null) {
            Class classDefiningField = this.valueField.getDeclaringClass();
            fieldName = this.valueField.getName();
            if (fieldName == null) {
                field = null;
            } else {
                field = this.reflectionProvider.getField(classDefiningField, fieldName);
            }
            if (fieldName == null || field == null) {
                exception = new ConversionException("Cannot assign value to field of type");
                exception.add("element", reader.getNodeName());
                exception.add("field", fieldName);
                exception.add("target type", context.getRequiredType().getName());
                throw exception;
            }
            String classAttribute = HierarchicalStreams.readClassAttribute(reader, this.mapper);
            if (classAttribute != null) {
                type = this.mapper.realClass(classAttribute);
            } else {
                type = this.mapper.defaultImplementationOf(this.reflectionProvider.getFieldType(result, fieldName, classDefiningField));
            }
            value = context.convertAnother(result, type, this.mapper.getLocalConverter(field.getDeclaringClass(), field.getName()));
            Class definedType = this.reflectionProvider.getFieldType(result, fieldName, classDefiningField);
            if (!definedType.isPrimitive()) {
                type = definedType;
            }
            if (value != null) {
                if (!type.isAssignableFrom(value.getClass())) {
                    exception = new ConversionException("Cannot assign object to type");
                    exception.add("object type", value.getClass().getName());
                    exception.add("target type", type.getName());
                    throw exception;
                }
            }
            this.reflectionProvider.writeField(result, fieldName, value, classDefiningField);
            if (!seenFields.add(new FastField(classDefiningField, fieldName))) {
                throw new DuplicateFieldException(fieldName + " [" + classDefiningField.getName() + "]");
            }
        }
        return result;
    }

    private boolean fieldIsEqual(FastField field) {
        return this.valueField.getName().equals(field.getName()) && this.valueField.getDeclaringClass().getName().equals(field.getDeclaringClass());
    }
}
