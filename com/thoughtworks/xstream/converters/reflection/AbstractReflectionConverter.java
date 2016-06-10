package com.thoughtworks.xstream.converters.reflection;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider.Visitor;
import com.thoughtworks.xstream.core.Caching;
import com.thoughtworks.xstream.core.ReferencingMarshallingContext;
import com.thoughtworks.xstream.core.util.ArrayIterator;
import com.thoughtworks.xstream.core.util.FastField;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.Mapper.ImplicitCollectionMapping;
import com.thoughtworks.xstream.mapper.Mapper.Null;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class AbstractReflectionConverter implements Converter, Caching {
    protected final Mapper mapper;
    private transient ReflectionProvider pureJavaReflectionProvider;
    protected final ReflectionProvider reflectionProvider;
    protected transient SerializationMethodInvoker serializationMethodInvoker;

    /* renamed from: com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.2 */
    class C08842 {
        final /* synthetic */ MarshallingContext val$context;
        final /* synthetic */ Map val$defaultFieldDefinition;
        final /* synthetic */ List val$fields;
        final /* synthetic */ Object val$source;
        final /* synthetic */ HierarchicalStreamWriter val$writer;

        C08842(List list, Object obj, MarshallingContext marshallingContext, HierarchicalStreamWriter hierarchicalStreamWriter, Map map) {
            this.val$fields = list;
            this.val$source = obj;
            this.val$context = marshallingContext;
            this.val$writer = hierarchicalStreamWriter;
            this.val$defaultFieldDefinition = map;
            for (FieldInfo info : this.val$fields) {
                if (info.value != null) {
                    ImplicitCollectionMapping mapping = AbstractReflectionConverter.this.mapper.getImplicitCollectionDefForFieldName(this.val$source.getClass(), info.fieldName);
                    if (mapping != null) {
                        Iterator iter;
                        if (!(!(this.val$context instanceof ReferencingMarshallingContext) || info.value == Collections.EMPTY_LIST || info.value == Collections.EMPTY_SET || info.value == Collections.EMPTY_MAP)) {
                            ((ReferencingMarshallingContext) this.val$context).registerImplicit(info.value);
                        }
                        boolean isCollection = info.value instanceof Collection;
                        boolean isEntry = (info.value instanceof Map) && mapping.getKeyFieldName() == null;
                        if (info.value.getClass().isArray()) {
                            Iterator arrayIterator = new ArrayIterator(info.value);
                        } else {
                            iter = isCollection ? ((Collection) info.value).iterator() : isEntry ? ((Map) info.value).entrySet().iterator() : ((Map) info.value).values().iterator();
                        }
                        while (iter.hasNext()) {
                            Class itemType;
                            String itemName;
                            Entry obj2 = iter.next();
                            if (obj2 == null) {
                                itemType = Object.class;
                                itemName = AbstractReflectionConverter.this.mapper.serializedClass(null);
                            } else if (isEntry) {
                                Entry entry = obj2;
                                ExtendedHierarchicalStreamWriterHelper.startNode(this.val$writer, mapping.getItemFieldName() != null ? mapping.getItemFieldName() : AbstractReflectionConverter.this.mapper.serializedClass(Entry.class), entry.getClass());
                                writeItem(entry.getKey(), this.val$context, this.val$writer);
                                writeItem(entry.getValue(), this.val$context, this.val$writer);
                                this.val$writer.endNode();
                            } else if (mapping.getItemFieldName() != null) {
                                itemType = mapping.getItemType();
                                itemName = mapping.getItemFieldName();
                            } else {
                                itemType = obj2.getClass();
                                itemName = AbstractReflectionConverter.this.mapper.serializedClass(itemType);
                            }
                            writeField(info.fieldName, itemName, itemType, info.definedIn, obj2);
                        }
                    } else {
                        writeField(info.fieldName, null, info.type, info.definedIn, info.value);
                    }
                }
            }
        }

        void writeField(String fieldName, String aliasName, Class fieldType, Class definedIn, Object newObj) {
            Class actualType;
            if (newObj != null) {
                actualType = newObj.getClass();
            } else {
                actualType = fieldType;
            }
            HierarchicalStreamWriter hierarchicalStreamWriter = this.val$writer;
            if (aliasName == null) {
                aliasName = AbstractReflectionConverter.this.mapper.serializedMember(this.val$source.getClass(), fieldName);
            }
            ExtendedHierarchicalStreamWriterHelper.startNode(hierarchicalStreamWriter, aliasName, actualType);
            if (newObj != null) {
                String attributeName;
                Class defaultType = AbstractReflectionConverter.this.mapper.defaultImplementationOf(fieldType);
                if (!actualType.equals(defaultType)) {
                    String serializedClassName = AbstractReflectionConverter.this.mapper.serializedClass(actualType);
                    if (!serializedClassName.equals(AbstractReflectionConverter.this.mapper.serializedClass(defaultType))) {
                        attributeName = AbstractReflectionConverter.this.mapper.aliasForSystemAttribute("class");
                        if (attributeName != null) {
                            this.val$writer.addAttribute(attributeName, serializedClassName);
                        }
                    }
                }
                if (((Field) this.val$defaultFieldDefinition.get(fieldName)).getDeclaringClass() != definedIn) {
                    attributeName = AbstractReflectionConverter.this.mapper.aliasForSystemAttribute("defined-in");
                    if (attributeName != null) {
                        this.val$writer.addAttribute(attributeName, AbstractReflectionConverter.this.mapper.serializedClass(definedIn));
                    }
                }
                AbstractReflectionConverter.this.marshallField(this.val$context, newObj, AbstractReflectionConverter.this.reflectionProvider.getField(definedIn, fieldName));
            }
            this.val$writer.endNode();
        }

        void writeItem(Object item, MarshallingContext context, HierarchicalStreamWriter writer) {
            if (item == null) {
                ExtendedHierarchicalStreamWriterHelper.startNode(writer, AbstractReflectionConverter.this.mapper.serializedClass(null), Null.class);
                writer.endNode();
                return;
            }
            ExtendedHierarchicalStreamWriterHelper.startNode(writer, AbstractReflectionConverter.this.mapper.serializedClass(item.getClass()), item.getClass());
            context.convertAnother(item);
            writer.endNode();
        }
    }

    /* renamed from: com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.3 */
    class C08853 extends HashSet {
        C08853() {
        }

        public boolean add(Object e) {
            if (super.add(e)) {
                return true;
            }
            throw new DuplicateFieldException(((FastField) e).getName());
        }
    }

    private static class ArraysList extends ArrayList {
        final Class physicalFieldType;

        ArraysList(Class physicalFieldType) {
            this.physicalFieldType = physicalFieldType;
        }

        Object toPhysicalArray() {
            Object[] objects = toArray();
            Object array = Array.newInstance(this.physicalFieldType.getComponentType(), objects.length);
            if (this.physicalFieldType.getComponentType().isPrimitive()) {
                for (int i = 0; i < objects.length; i++) {
                    Array.set(array, i, Array.get(objects, i));
                }
            } else {
                System.arraycopy(objects, 0, array, 0, objects.length);
            }
            return array;
        }
    }

    private static class FieldInfo {
        final Class definedIn;
        final String fieldName;
        final Class type;
        final Object value;

        FieldInfo(String fieldName, Class type, Class definedIn, Object value) {
            this.fieldName = fieldName;
            this.type = type;
            this.definedIn = definedIn;
            this.value = value;
        }
    }

    private class MappingList extends AbstractList {
        private final Map fieldCache;
        private final String keyFieldName;
        private final Map map;

        public MappingList(Map map, String keyFieldName) {
            this.fieldCache = new HashMap();
            this.map = map;
            this.keyFieldName = keyFieldName;
        }

        public boolean add(Object object) {
            boolean containsNull = true;
            if (object == null) {
                if (this.map.containsKey(null)) {
                    containsNull = false;
                }
                this.map.put(null, null);
                return containsNull;
            }
            Class itemType = object.getClass();
            if (this.keyFieldName != null) {
                Field field = (Field) this.fieldCache.get(itemType);
                if (field == null) {
                    field = AbstractReflectionConverter.this.reflectionProvider.getField(itemType, this.keyFieldName);
                    this.fieldCache.put(itemType, field);
                }
                if (field != null) {
                    try {
                        if (this.map.put(field.get(object), object) != null) {
                            return false;
                        }
                        return true;
                    } catch (IllegalArgumentException e) {
                        throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e);
                    } catch (IllegalAccessException e2) {
                        throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e2);
                    }
                }
            } else if (object instanceof Entry) {
                Entry entry = (Entry) object;
                if (this.map.put(entry.getKey(), entry.getValue()) != null) {
                    return false;
                }
                return true;
            }
            throw new ConversionException("Element of type " + object.getClass().getName() + " is not defined as entry for map of type " + this.map.getClass().getName());
        }

        public Object get(int index) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return this.map.size();
        }
    }

    /* renamed from: com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.1 */
    class C11301 implements Visitor {
        final /* synthetic */ Map val$defaultFieldDefinition;
        final /* synthetic */ List val$fields;
        final /* synthetic */ Object val$source;
        final /* synthetic */ HierarchicalStreamWriter val$writer;
        final Set writtenAttributes;

        C11301(Map map, Object obj, HierarchicalStreamWriter hierarchicalStreamWriter, List list) {
            this.val$defaultFieldDefinition = map;
            this.val$source = obj;
            this.val$writer = hierarchicalStreamWriter;
            this.val$fields = list;
            this.writtenAttributes = new HashSet();
        }

        public void visit(String fieldName, Class type, Class definedIn, Object value) {
            if (AbstractReflectionConverter.this.mapper.shouldSerializeMember(definedIn, fieldName)) {
                if (!this.val$defaultFieldDefinition.containsKey(fieldName)) {
                    Class lookupType = this.val$source.getClass();
                    if (!(definedIn == this.val$source.getClass() || AbstractReflectionConverter.this.mapper.shouldSerializeMember(lookupType, fieldName))) {
                        lookupType = definedIn;
                    }
                    this.val$defaultFieldDefinition.put(fieldName, AbstractReflectionConverter.this.reflectionProvider.getField(lookupType, fieldName));
                }
                SingleValueConverter converter = AbstractReflectionConverter.this.mapper.getConverterFromItemType(fieldName, type, definedIn);
                if (converter != null) {
                    String attribute = AbstractReflectionConverter.this.mapper.aliasForAttribute(AbstractReflectionConverter.this.mapper.serializedMember(definedIn, fieldName));
                    if (value != null) {
                        if (this.writtenAttributes.contains(fieldName)) {
                            throw new ConversionException("Cannot write field with name '" + fieldName + "' twice as attribute for object of type " + this.val$source.getClass().getName());
                        }
                        String str = converter.toString(value);
                        if (str != null) {
                            this.val$writer.addAttribute(attribute, str);
                        }
                    }
                    this.writtenAttributes.add(fieldName);
                    return;
                }
                this.val$fields.add(new FieldInfo(fieldName, type, definedIn, value));
            }
        }
    }

    public static class DuplicateFieldException extends ConversionException {
        public DuplicateFieldException(String msg) {
            super("Duplicate field " + msg);
            add("field", msg);
        }
    }

    public static class UnknownFieldException extends ConversionException {
        public UnknownFieldException(String type, String field) {
            super("No such field " + type + "." + field);
            add("field", field);
        }
    }

    public AbstractReflectionConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
        this.mapper = mapper;
        this.reflectionProvider = reflectionProvider;
        this.serializationMethodInvoker = new SerializationMethodInvoker();
    }

    public void marshal(Object original, HierarchicalStreamWriter writer, MarshallingContext context) {
        Object source = this.serializationMethodInvoker.callWriteReplace(original);
        if (source != original && (context instanceof ReferencingMarshallingContext)) {
            ((ReferencingMarshallingContext) context).replace(original, source);
        }
        if (source.getClass() != original.getClass()) {
            String attributeName = this.mapper.aliasForSystemAttribute("resolves-to");
            if (attributeName != null) {
                writer.addAttribute(attributeName, this.mapper.serializedClass(source.getClass()));
            }
            context.convertAnother(source);
            return;
        }
        doMarshal(source, writer, context);
    }

    protected void doMarshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        List fields = new ArrayList();
        Map defaultFieldDefinition = new HashMap();
        this.reflectionProvider.visitSerializableFields(source, new C11301(defaultFieldDefinition, source, writer, fields));
        C08842 c08842 = new C08842(fields, source, context, writer, defaultFieldDefinition);
    }

    protected void marshallField(MarshallingContext context, Object newObj, Field field) {
        context.convertAnother(newObj, this.mapper.getLocalConverter(field.getDeclaringClass(), field.getName()));
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return this.serializationMethodInvoker.callReadResolve(doUnmarshal(instantiateNewInstance(reader, context), reader, context));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object doUnmarshal(java.lang.Object r35, com.thoughtworks.xstream.io.HierarchicalStreamReader r36, com.thoughtworks.xstream.converters.UnmarshallingContext r37) {
        /*
        r34 = this;
        r26 = r35.getClass();
        r27 = new com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter$3;
        r0 = r27;
        r1 = r34;
        r0.<init>();
        r21 = r36.getAttributeNames();
    L_0x0011:
        r31 = r21.hasNext();
        if (r31 == 0) goto L_0x00ef;
    L_0x0017:
        r6 = r21.next();
        r6 = (java.lang.String) r6;
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r0 = r34;
        r0 = r0.mapper;
        r32 = r0;
        r0 = r32;
        r32 = r0.attributeForAlias(r6);
        r0 = r31;
        r1 = r26;
        r2 = r32;
        r7 = r0.realMember(r1, r2);
        r0 = r34;
        r0 = r0.reflectionProvider;
        r31 = r0;
        r0 = r31;
        r1 = r26;
        r14 = r0.getFieldOrNull(r1, r7);
        if (r14 == 0) goto L_0x0011;
    L_0x0049:
        r0 = r34;
        r31 = r0.shouldUnmarshalField(r14);
        if (r31 == 0) goto L_0x0011;
    L_0x0051:
        r9 = r14.getDeclaringClass();
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r0 = r31;
        r31 = r0.shouldSerializeMember(r9, r7);
        if (r31 == 0) goto L_0x0011;
    L_0x0063:
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r32 = r14.getType();
        r0 = r31;
        r1 = r32;
        r10 = r0.getConverterFromAttribute(r9, r7, r1);
        r28 = r14.getType();
        if (r10 == 0) goto L_0x0011;
    L_0x007b:
        r0 = r36;
        r31 = r0.getAttribute(r6);
        r0 = r31;
        r30 = r10.fromString(r0);
        r31 = r28.isPrimitive();
        if (r31 == 0) goto L_0x0091;
    L_0x008d:
        r28 = com.thoughtworks.xstream.core.util.Primitives.box(r28);
    L_0x0091:
        if (r30 == 0) goto L_0x00d0;
    L_0x0093:
        r31 = r30.getClass();
        r0 = r28;
        r1 = r31;
        r31 = r0.isAssignableFrom(r1);
        if (r31 != 0) goto L_0x00d0;
    L_0x00a1:
        r31 = new com.thoughtworks.xstream.converters.ConversionException;
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "Cannot convert type ";
        r32 = r32.append(r33);
        r33 = r30.getClass();
        r33 = r33.getName();
        r32 = r32.append(r33);
        r33 = " to type ";
        r32 = r32.append(r33);
        r33 = r28.getName();
        r32 = r32.append(r33);
        r32 = r32.toString();
        r31.<init>(r32);
        throw r31;
    L_0x00d0:
        r31 = new com.thoughtworks.xstream.core.util.FastField;
        r0 = r31;
        r0.<init>(r9, r7);
        r0 = r27;
        r1 = r31;
        r0.add(r1);
        r0 = r34;
        r0 = r0.reflectionProvider;
        r31 = r0;
        r0 = r31;
        r1 = r35;
        r2 = r30;
        r0.writeField(r1, r7, r2, r9);
        goto L_0x0011;
    L_0x00ef:
        r19 = 0;
    L_0x00f1:
        r31 = r36.hasMoreChildren();
        if (r31 == 0) goto L_0x03ab;
    L_0x00f7:
        r36.moveDown();
        r25 = r36.getNodeName();
        r0 = r34;
        r1 = r36;
        r13 = r0.readDeclaringClass(r1);
        if (r13 != 0) goto L_0x01b4;
    L_0x0108:
        r16 = r26;
    L_0x010a:
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r0 = r31;
        r1 = r16;
        r2 = r25;
        r17 = r0.realMember(r1, r2);
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r0 = r31;
        r1 = r16;
        r2 = r17;
        r18 = r0.getImplicitCollectionDefForFieldName(r1, r2);
        r20 = 0;
        r14 = 0;
        r28 = 0;
        if (r18 != 0) goto L_0x030a;
    L_0x0131:
        r0 = r34;
        r0 = r0.reflectionProvider;
        r31 = r0;
        r0 = r31;
        r1 = r16;
        r2 = r17;
        r14 = r0.getFieldOrNull(r1, r2);
        if (r14 != 0) goto L_0x025e;
    L_0x0143:
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r0 = r31;
        r1 = r26;
        r2 = r17;
        r22 = r0.getItemTypeForItemFieldName(r1, r2);
        if (r22 == 0) goto L_0x01bb;
    L_0x0155:
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r0 = r36;
        r1 = r31;
        r8 = com.thoughtworks.xstream.core.util.HierarchicalStreams.readClassAttribute(r0, r1);
        if (r8 == 0) goto L_0x01b8;
    L_0x0165:
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r0 = r31;
        r28 = r0.realClass(r8);
    L_0x0171:
        if (r28 != 0) goto L_0x01f4;
    L_0x0173:
        r30 = 0;
    L_0x0175:
        if (r30 == 0) goto L_0x033f;
    L_0x0177:
        r31 = r30.getClass();
        r0 = r28;
        r1 = r31;
        r31 = r0.isAssignableFrom(r1);
        if (r31 != 0) goto L_0x033f;
    L_0x0185:
        r31 = new com.thoughtworks.xstream.converters.ConversionException;
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "Cannot convert type ";
        r32 = r32.append(r33);
        r33 = r30.getClass();
        r33 = r33.getName();
        r32 = r32.append(r33);
        r33 = " to type ";
        r32 = r32.append(r33);
        r33 = r28.getName();
        r32 = r32.append(r33);
        r32 = r32.toString();
        r31.<init>(r32);
        throw r31;
    L_0x01b4:
        r16 = r13;
        goto L_0x010a;
    L_0x01b8:
        r28 = r22;
        goto L_0x0171;
    L_0x01bb:
        r0 = r34;
        r0 = r0.mapper;	 Catch:{ CannotResolveClassException -> 0x03ee }
        r31 = r0;
        r0 = r31;
        r1 = r25;
        r28 = r0.realClass(r1);	 Catch:{ CannotResolveClassException -> 0x03ee }
        r0 = r34;
        r0 = r0.mapper;	 Catch:{ CannotResolveClassException -> 0x03ee }
        r31 = r0;
        r32 = r37.getRequiredType();	 Catch:{ CannotResolveClassException -> 0x03ee }
        r0 = r31;
        r1 = r32;
        r2 = r28;
        r3 = r25;
        r20 = r0.getFieldNameForItemTypeAndName(r1, r2, r3);	 Catch:{ CannotResolveClassException -> 0x03ee }
    L_0x01df:
        if (r28 == 0) goto L_0x01e5;
    L_0x01e1:
        if (r28 == 0) goto L_0x0171;
    L_0x01e3:
        if (r20 != 0) goto L_0x0171;
    L_0x01e5:
        r0 = r34;
        r1 = r17;
        r2 = r26;
        r3 = r25;
        r0.handleUnknownField(r13, r1, r2, r3);
        r28 = 0;
        goto L_0x0171;
    L_0x01f4:
        r31 = java.util.Map.Entry.class;
        r0 = r31;
        r1 = r28;
        r31 = r0.equals(r1);
        if (r31 == 0) goto L_0x0252;
    L_0x0200:
        r36.moveDown();
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r0 = r36;
        r1 = r31;
        r31 = com.thoughtworks.xstream.core.util.HierarchicalStreams.readClassType(r0, r1);
        r0 = r37;
        r1 = r35;
        r2 = r31;
        r24 = r0.convertAnother(r1, r2);
        r36.moveUp();
        r36.moveDown();
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r0 = r36;
        r1 = r31;
        r31 = com.thoughtworks.xstream.core.util.HierarchicalStreams.readClassType(r0, r1);
        r0 = r37;
        r1 = r35;
        r2 = r31;
        r29 = r0.convertAnother(r1, r2);
        r36.moveUp();
        r0 = r24;
        r1 = r29;
        r31 = java.util.Collections.singletonMap(r0, r1);
        r31 = r31.entrySet();
        r31 = r31.iterator();
        r30 = r31.next();
        goto L_0x0175;
    L_0x0252:
        r0 = r37;
        r1 = r35;
        r2 = r28;
        r30 = r0.convertAnother(r1, r2);
        goto L_0x0175;
    L_0x025e:
        r15 = 0;
        if (r13 != 0) goto L_0x029f;
    L_0x0261:
        if (r14 == 0) goto L_0x029f;
    L_0x0263:
        r0 = r34;
        r31 = r0.shouldUnmarshalField(r14);
        if (r31 == 0) goto L_0x029d;
    L_0x026b:
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r32 = r14.getDeclaringClass();
        r0 = r31;
        r1 = r32;
        r2 = r17;
        r31 = r0.shouldSerializeMember(r1, r2);
        if (r31 == 0) goto L_0x029d;
    L_0x0281:
        r15 = 1;
    L_0x0282:
        if (r15 != 0) goto L_0x029f;
    L_0x0284:
        r0 = r34;
        r0 = r0.reflectionProvider;
        r31 = r0;
        r32 = r14.getDeclaringClass();
        r32 = r32.getSuperclass();
        r0 = r31;
        r1 = r32;
        r2 = r17;
        r14 = r0.getFieldOrNull(r1, r2);
        goto L_0x0261;
    L_0x029d:
        r15 = 0;
        goto L_0x0282;
    L_0x029f:
        if (r14 == 0) goto L_0x0306;
    L_0x02a1:
        if (r15 != 0) goto L_0x02c1;
    L_0x02a3:
        r0 = r34;
        r31 = r0.shouldUnmarshalField(r14);
        if (r31 == 0) goto L_0x0306;
    L_0x02ab:
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r32 = r14.getDeclaringClass();
        r0 = r31;
        r1 = r32;
        r2 = r17;
        r31 = r0.shouldSerializeMember(r1, r2);
        if (r31 == 0) goto L_0x0306;
    L_0x02c1:
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r0 = r36;
        r1 = r31;
        r8 = com.thoughtworks.xstream.core.util.HierarchicalStreams.readClassAttribute(r0, r1);
        if (r8 == 0) goto L_0x02f7;
    L_0x02d1:
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r0 = r31;
        r28 = r0.realClass(r8);
    L_0x02dd:
        r0 = r34;
        r1 = r37;
        r2 = r35;
        r3 = r28;
        r30 = r0.unmarshallField(r1, r2, r3, r14);
        r11 = r14.getType();
        r31 = r11.isPrimitive();
        if (r31 != 0) goto L_0x0175;
    L_0x02f3:
        r28 = r11;
        goto L_0x0175;
    L_0x02f7:
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r32 = r14.getType();
        r28 = r31.defaultImplementationOf(r32);
        goto L_0x02dd;
    L_0x0306:
        r30 = 0;
        goto L_0x0175;
    L_0x030a:
        r20 = r18.getFieldName();
        r28 = r18.getItemType();
        if (r28 != 0) goto L_0x0330;
    L_0x0314:
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        r0 = r36;
        r1 = r31;
        r8 = com.thoughtworks.xstream.core.util.HierarchicalStreams.readClassAttribute(r0, r1);
        r0 = r34;
        r0 = r0.mapper;
        r31 = r0;
        if (r8 == 0) goto L_0x033c;
    L_0x032a:
        r0 = r31;
        r28 = r0.realClass(r8);
    L_0x0330:
        r0 = r37;
        r1 = r35;
        r2 = r28;
        r30 = r0.convertAnother(r1, r2);
        goto L_0x0175;
    L_0x033c:
        r8 = r25;
        goto L_0x032a;
    L_0x033f:
        if (r14 == 0) goto L_0x0373;
    L_0x0341:
        r0 = r34;
        r0 = r0.reflectionProvider;
        r31 = r0;
        r32 = r14.getDeclaringClass();
        r0 = r31;
        r1 = r35;
        r2 = r17;
        r3 = r30;
        r4 = r32;
        r0.writeField(r1, r2, r3, r4);
        r31 = new com.thoughtworks.xstream.core.util.FastField;
        r32 = r14.getDeclaringClass();
        r0 = r31;
        r1 = r32;
        r2 = r17;
        r0.<init>(r1, r2);
        r0 = r27;
        r1 = r31;
        r0.add(r1);
    L_0x036e:
        r36.moveUp();
        goto L_0x00f1;
    L_0x0373:
        if (r28 == 0) goto L_0x036e;
    L_0x0375:
        if (r20 != 0) goto L_0x0393;
    L_0x0377:
        r0 = r34;
        r0 = r0.mapper;
        r32 = r0;
        r33 = r37.getRequiredType();
        if (r30 == 0) goto L_0x03a8;
    L_0x0383:
        r31 = r30.getClass();
    L_0x0387:
        r0 = r32;
        r1 = r33;
        r2 = r31;
        r3 = r25;
        r20 = r0.getFieldNameForItemTypeAndName(r1, r2, r3);
    L_0x0393:
        if (r19 != 0) goto L_0x039a;
    L_0x0395:
        r19 = new java.util.HashMap;
        r19.<init>();
    L_0x039a:
        r0 = r34;
        r1 = r30;
        r2 = r19;
        r3 = r35;
        r4 = r20;
        r0.writeValueToImplicitCollection(r1, r2, r3, r4);
        goto L_0x036e;
    L_0x03a8:
        r31 = com.thoughtworks.xstream.mapper.Mapper.Null.class;
        goto L_0x0387;
    L_0x03ab:
        if (r19 == 0) goto L_0x03ed;
    L_0x03ad:
        r31 = r19.entrySet();
        r23 = r31.iterator();
    L_0x03b5:
        r31 = r23.hasNext();
        if (r31 == 0) goto L_0x03ed;
    L_0x03bb:
        r12 = r23.next();
        r12 = (java.util.Map.Entry) r12;
        r30 = r12.getValue();
        r0 = r30;
        r0 = r0 instanceof com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.ArraysList;
        r31 = r0;
        if (r31 == 0) goto L_0x03b5;
    L_0x03cd:
        r30 = (com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.ArraysList) r30;
        r5 = r30.toPhysicalArray();
        r0 = r34;
        r0 = r0.reflectionProvider;
        r32 = r0;
        r31 = r12.getKey();
        r31 = (java.lang.String) r31;
        r33 = 0;
        r0 = r32;
        r1 = r35;
        r2 = r31;
        r3 = r33;
        r0.writeField(r1, r2, r5, r3);
        goto L_0x03b5;
    L_0x03ed:
        return r35;
    L_0x03ee:
        r31 = move-exception;
        goto L_0x01df;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.doUnmarshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext):java.lang.Object");
    }

    protected Object unmarshallField(UnmarshallingContext context, Object result, Class type, Field field) {
        return context.convertAnother(result, type, this.mapper.getLocalConverter(field.getDeclaringClass(), field.getName()));
    }

    protected boolean shouldUnmarshalTransientFields() {
        return false;
    }

    protected boolean shouldUnmarshalField(Field field) {
        return !Modifier.isTransient(field.getModifiers()) || shouldUnmarshalTransientFields();
    }

    private void handleUnknownField(Class classDefiningField, String fieldName, Class resultType, String originalNodeName) {
        if (classDefiningField == null) {
            Class cls = resultType;
            while (cls != null) {
                if (this.mapper.shouldSerializeMember(cls, originalNodeName)) {
                    cls = cls.getSuperclass();
                } else {
                    return;
                }
            }
        }
        throw new UnknownFieldException(resultType.getName(), fieldName);
    }

    private void writeValueToImplicitCollection(Object value, Map implicitCollections, Object result, String implicitFieldName) {
        Collection collection = (Collection) implicitCollections.get(implicitFieldName);
        if (collection == null) {
            Class physicalFieldType = this.reflectionProvider.getFieldType(result, implicitFieldName, null);
            if (physicalFieldType.isArray()) {
                collection = new ArraysList(physicalFieldType);
            } else {
                Class fieldType = this.mapper.defaultImplementationOf(physicalFieldType);
                if (Collection.class.isAssignableFrom(fieldType) || Map.class.isAssignableFrom(fieldType)) {
                    if (this.pureJavaReflectionProvider == null) {
                        this.pureJavaReflectionProvider = new PureJavaReflectionProvider();
                    }
                    Collection instance = this.pureJavaReflectionProvider.newInstance(fieldType);
                    if (instance instanceof Collection) {
                        collection = instance;
                    } else {
                        collection = new MappingList((Map) instance, this.mapper.getImplicitCollectionDefForFieldName(result.getClass(), implicitFieldName).getKeyFieldName());
                    }
                    this.reflectionProvider.writeField(result, implicitFieldName, instance, null);
                } else {
                    throw new ObjectAccessException("Field " + implicitFieldName + " of " + result.getClass().getName() + " is configured for an implicit Collection or Map, but field is of type " + fieldType.getName());
                }
            }
            implicitCollections.put(implicitFieldName, collection);
        }
        collection.add(value);
    }

    private Class readDeclaringClass(HierarchicalStreamReader reader) {
        String attributeName = this.mapper.aliasForSystemAttribute("defined-in");
        String definedIn = attributeName == null ? null : reader.getAttribute(attributeName);
        if (definedIn == null) {
            return null;
        }
        return this.mapper.realClass(definedIn);
    }

    protected Object instantiateNewInstance(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String attributeName = this.mapper.aliasForSystemAttribute("resolves-to");
        String readResolveValue = attributeName == null ? null : reader.getAttribute(attributeName);
        Object currentObject = context.currentObject();
        if (currentObject != null) {
            return currentObject;
        }
        if (readResolveValue != null) {
            return this.reflectionProvider.newInstance(this.mapper.realClass(readResolveValue));
        }
        return this.reflectionProvider.newInstance(context.getRequiredType());
    }

    public void flushCache() {
        this.serializationMethodInvoker.flushCache();
    }

    private Object readResolve() {
        this.serializationMethodInvoker = new SerializationMethodInvoker();
        return this;
    }
}
