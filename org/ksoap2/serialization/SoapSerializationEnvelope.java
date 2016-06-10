package org.ksoap2.serialization;

import com.ifoer.mine.Contact;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.SoapFault12;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class SoapSerializationEnvelope extends SoapEnvelope {
    private static final String ANY_TYPE_LABEL = "anyType";
    private static final String ARRAY_MAPPING_NAME = "Array";
    private static final String ARRAY_TYPE_LABEL = "arrayType";
    static final Marshal DEFAULT_MARSHAL;
    private static final String HREF_LABEL = "href";
    private static final String ID_LABEL = "id";
    private static final String ITEM_LABEL = "item";
    private static final String NIL_LABEL = "nil";
    private static final String NULL_LABEL = "null";
    protected static final int QNAME_MARSHAL = 3;
    protected static final int QNAME_NAMESPACE = 0;
    protected static final int QNAME_TYPE = 1;
    private static final String ROOT_LABEL = "root";
    private static final String TYPE_LABEL = "type";
    protected boolean addAdornments;
    public boolean avoidExceptionForUnknownProperty;
    protected Hashtable classToQName;
    public boolean dotNet;
    Hashtable idMap;
    public boolean implicitTypes;
    Vector multiRef;
    public Hashtable properties;
    protected Hashtable qNameToClass;

    static {
        DEFAULT_MARSHAL = new DM();
    }

    public SoapSerializationEnvelope(int version) {
        super(version);
        this.properties = new Hashtable();
        this.idMap = new Hashtable();
        this.qNameToClass = new Hashtable();
        this.classToQName = new Hashtable();
        this.addAdornments = true;
        addMapping(this.enc, ARRAY_MAPPING_NAME, PropertyInfo.VECTOR_CLASS);
        DEFAULT_MARSHAL.register(this);
    }

    public boolean isAddAdornments() {
        return this.addAdornments;
    }

    public void setAddAdornments(boolean addAdornments) {
        this.addAdornments = addAdornments;
    }

    public void setBodyOutEmpty(boolean emptyBody) {
        if (emptyBody) {
            this.bodyOut = null;
        }
    }

    public void parseBody(XmlPullParser parser) throws IOException, XmlPullParserException {
        this.bodyIn = null;
        parser.nextTag();
        if (parser.getEventType() == 2 && parser.getNamespace().equals(this.env) && parser.getName().equals("Fault")) {
            SoapFault fault;
            if (this.version < SoapEnvelope.VER12) {
                fault = new SoapFault(this.version);
            } else {
                fault = new SoapFault12(this.version);
            }
            fault.parse(parser);
            this.bodyIn = fault;
            return;
        }
        while (parser.getEventType() == 2) {
            String rootAttr = parser.getAttributeValue(this.enc, ROOT_LABEL);
            Object o = read(parser, null, -1, parser.getNamespace(), parser.getName(), PropertyInfo.OBJECT_TYPE);
            if (Contact.RELATION_FRIEND.equals(rootAttr) || this.bodyIn == null) {
                this.bodyIn = o;
            }
            parser.nextTag();
        }
    }

    protected void readSerializable(XmlPullParser parser, SoapObject obj) throws IOException, XmlPullParserException {
        for (int counter = QNAME_NAMESPACE; counter < parser.getAttributeCount(); counter += QNAME_TYPE) {
            obj.addAttribute(parser.getAttributeName(counter), parser.getAttributeValue(counter));
        }
        readSerializable(parser, (KvmSerializable) obj);
    }

    protected void readSerializable(XmlPullParser parser, KvmSerializable obj) throws IOException, XmlPullParserException {
        while (parser.nextTag() != QNAME_MARSHAL) {
            String name = parser.getName();
            if (this.implicitTypes && (obj instanceof SoapObject)) {
                ((SoapObject) obj).addProperty(parser.getName(), read(parser, obj, obj.getPropertyCount(), ((SoapObject) obj).getNamespace(), name, PropertyInfo.OBJECT_TYPE));
            } else {
                PropertyInfo info = new PropertyInfo();
                int propertyCount = obj.getPropertyCount();
                boolean propertyFound = false;
                for (int i = QNAME_NAMESPACE; i < propertyCount && !propertyFound; i += QNAME_TYPE) {
                    info.clear();
                    obj.getPropertyInfo(i, this.properties, info);
                    if ((name.equals(info.name) && info.namespace == null) || (name.equals(info.name) && parser.getNamespace().equals(info.namespace))) {
                        propertyFound = true;
                        obj.setProperty(i, read(parser, obj, i, null, null, info));
                    }
                }
                if (propertyFound) {
                    continue;
                } else if (this.avoidExceptionForUnknownProperty) {
                    while (true) {
                        if (parser.next() == QNAME_MARSHAL && name.equals(parser.getName())) {
                            break;
                        }
                    }
                } else {
                    throw new RuntimeException("Unknown Property: " + name);
                }
            }
        }
        parser.require(QNAME_MARSHAL, null, null);
    }

    protected Object readUnknown(XmlPullParser parser, String typeNamespace, String typeName) throws IOException, XmlPullParserException {
        int i;
        SoapObject so;
        String name = parser.getName();
        String namespace = parser.getNamespace();
        Vector attributeInfoVector = new Vector();
        for (int attributeCount = QNAME_NAMESPACE; attributeCount < parser.getAttributeCount(); attributeCount += QNAME_TYPE) {
            AttributeInfo attributeInfo = new AttributeInfo();
            attributeInfo.setName(parser.getAttributeName(attributeCount));
            attributeInfo.setValue(parser.getAttributeValue(attributeCount));
            attributeInfo.setNamespace(parser.getAttributeNamespace(attributeCount));
            attributeInfo.setType(parser.getAttributeType(attributeCount));
            attributeInfoVector.addElement(attributeInfo);
        }
        parser.next();
        Object obj = null;
        String text = null;
        if (parser.getEventType() == 4) {
            text = parser.getText();
            SoapPrimitive soapPrimitive = new SoapPrimitive(typeNamespace, typeName, text);
            obj = soapPrimitive;
            for (i = QNAME_NAMESPACE; i < attributeInfoVector.size(); i += QNAME_TYPE) {
                soapPrimitive.addAttribute((AttributeInfo) attributeInfoVector.elementAt(i));
            }
            parser.next();
        } else if (parser.getEventType() == QNAME_MARSHAL) {
            so = new SoapObject(typeNamespace, typeName);
            for (i = QNAME_NAMESPACE; i < attributeInfoVector.size(); i += QNAME_TYPE) {
                so.addAttribute((AttributeInfo) attributeInfoVector.elementAt(i));
            }
            SoapObject result = so;
        }
        if (parser.getEventType() == 2) {
            if (text == null || text.trim().length() == 0) {
                so = new SoapObject(typeNamespace, typeName);
                for (i = QNAME_NAMESPACE; i < attributeInfoVector.size(); i += QNAME_TYPE) {
                    so.addAttribute((AttributeInfo) attributeInfoVector.elementAt(i));
                }
                while (parser.getEventType() != QNAME_MARSHAL) {
                    String name2 = parser.getName();
                    so.addProperty(name2, read(parser, so, so.getPropertyCount(), null, null, PropertyInfo.OBJECT_TYPE));
                    parser.nextTag();
                }
                obj = so;
            } else {
                throw new RuntimeException("Malformed input: Mixed content");
            }
        }
        parser.require(QNAME_MARSHAL, namespace, name);
        return obj;
    }

    private int getIndex(String value, int start, int dflt) {
        return (value != null && value.length() - start >= QNAME_MARSHAL) ? Integer.parseInt(value.substring(start + QNAME_TYPE, value.length() - 1)) : dflt;
    }

    protected void readVector(XmlPullParser parser, Vector v, PropertyInfo elementType) throws IOException, XmlPullParserException {
        String namespace = null;
        String name = null;
        int size = v.size();
        boolean dynamic = true;
        String type = parser.getAttributeValue(this.enc, ARRAY_TYPE_LABEL);
        if (type != null) {
            String prefix;
            int cut0 = type.indexOf(58);
            int cut1 = type.indexOf("[", cut0);
            name = type.substring(cut0 + QNAME_TYPE, cut1);
            if (cut0 == -1) {
                prefix = XmlPullParser.NO_NAMESPACE;
            } else {
                prefix = type.substring(QNAME_NAMESPACE, cut0);
            }
            namespace = parser.getNamespace(prefix);
            size = getIndex(type, cut1, -1);
            if (size != -1) {
                v.setSize(size);
                dynamic = false;
            }
        }
        if (elementType == null) {
            elementType = PropertyInfo.OBJECT_TYPE;
        }
        parser.nextTag();
        int position = getIndex(parser.getAttributeValue(this.enc, "offset"), QNAME_NAMESPACE, QNAME_NAMESPACE);
        while (parser.getEventType() != QNAME_MARSHAL) {
            position = getIndex(parser.getAttributeValue(this.enc, "position"), QNAME_NAMESPACE, position);
            if (dynamic && position >= size) {
                size = position + QNAME_TYPE;
                v.setSize(size);
            }
            v.setElementAt(read(parser, v, position, namespace, name, elementType), position);
            position += QNAME_TYPE;
            parser.nextTag();
        }
        parser.require(QNAME_MARSHAL, null, null);
    }

    public Object read(XmlPullParser parser, Object owner, int index, String namespace, String name, PropertyInfo expected) throws IOException, XmlPullParserException {
        Object obj;
        String elementName = parser.getName();
        String href = parser.getAttributeValue(null, HREF_LABEL);
        FwdRef f;
        if (href == null) {
            String nullAttr = parser.getAttributeValue(this.xsi, NIL_LABEL);
            String id = parser.getAttributeValue(null, ID_LABEL);
            if (nullAttr == null) {
                nullAttr = parser.getAttributeValue(this.xsi, NULL_LABEL);
            }
            if (nullAttr == null || !SoapEnvelope.stringToBoolean(nullAttr)) {
                String type = parser.getAttributeValue(this.xsi, TYPE_LABEL);
                if (type != null) {
                    int cut = type.indexOf(58);
                    name = type.substring(cut + QNAME_TYPE);
                    namespace = parser.getNamespace(cut == -1 ? XmlPullParser.NO_NAMESPACE : type.substring(QNAME_NAMESPACE, cut));
                } else if (name == null && namespace == null) {
                    if (parser.getAttributeValue(this.enc, ARRAY_TYPE_LABEL) != null) {
                        namespace = this.enc;
                        name = ARRAY_MAPPING_NAME;
                    } else {
                        Object[] names = getInfo(expected.type, null);
                        namespace = names[QNAME_NAMESPACE];
                        name = names[QNAME_TYPE];
                    }
                }
                if (type == null) {
                    this.implicitTypes = true;
                }
                obj = readInstance(parser, namespace, name, expected);
                if (obj == null) {
                    obj = readUnknown(parser, namespace, name);
                }
            } else {
                obj = null;
                parser.nextTag();
                parser.require(QNAME_MARSHAL, null, elementName);
            }
            if (id != null) {
                Object hlp = this.idMap.get(id);
                if (hlp instanceof FwdRef) {
                    f = (FwdRef) hlp;
                    do {
                        if (f.obj instanceof KvmSerializable) {
                            ((KvmSerializable) f.obj).setProperty(f.index, obj);
                        } else {
                            ((Vector) f.obj).setElementAt(obj, f.index);
                        }
                        f = f.next;
                    } while (f != null);
                } else if (hlp != null) {
                    throw new RuntimeException("double ID");
                }
                this.idMap.put(id, obj);
            }
        } else if (owner == null) {
            throw new RuntimeException("href at root level?!?");
        } else {
            href = href.substring(QNAME_TYPE);
            obj = this.idMap.get(href);
            if (obj == null || (obj instanceof FwdRef)) {
                f = new FwdRef();
                f.next = (FwdRef) obj;
                f.obj = owner;
                f.index = index;
                this.idMap.put(href, f);
                obj = null;
            }
            parser.nextTag();
            parser.require(QNAME_MARSHAL, null, elementName);
        }
        parser.require(QNAME_MARSHAL, null, elementName);
        return obj;
    }

    public Object readInstance(XmlPullParser parser, String namespace, String name, PropertyInfo expected) throws IOException, XmlPullParserException {
        Class obj = this.qNameToClass.get(new SoapPrimitive(namespace, name, null));
        if (obj == null) {
            return null;
        }
        if (obj instanceof Marshal) {
            return ((Marshal) obj).readInstance(parser, namespace, name, expected);
        }
        Object obj2;
        if (obj instanceof SoapObject) {
            obj2 = ((SoapObject) obj).newInstance();
        } else if (obj == SoapObject.class) {
            obj2 = new SoapObject(namespace, name);
        } else {
            try {
                obj2 = obj.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
        }
        if (obj2 instanceof SoapObject) {
            readSerializable(parser, (SoapObject) obj2);
            return obj2;
        } else if (obj2 instanceof KvmSerializable) {
            readSerializable(parser, (KvmSerializable) obj2);
            return obj2;
        } else if (obj2 instanceof Vector) {
            readVector(parser, (Vector) obj2, expected.elementType);
            return obj2;
        } else {
            throw new RuntimeException("no deserializer for " + obj2.getClass());
        }
    }

    public Object[] getInfo(Object type, Object instance) {
        AttributeContainer type2;
        if (type == null) {
            if ((instance instanceof SoapObject) || (instance instanceof SoapPrimitive)) {
                type2 = instance;
            } else {
                type2 = instance.getClass();
            }
        }
        Object[] objArr;
        if (type2 instanceof SoapObject) {
            SoapObject so = (SoapObject) type2;
            objArr = new Object[4];
            objArr[QNAME_NAMESPACE] = so.getNamespace();
            objArr[QNAME_TYPE] = so.getName();
            return objArr;
        } else if (type2 instanceof SoapPrimitive) {
            SoapPrimitive sp = (SoapPrimitive) type2;
            objArr = new Object[4];
            objArr[QNAME_NAMESPACE] = sp.getNamespace();
            objArr[QNAME_TYPE] = sp.getName();
            objArr[QNAME_MARSHAL] = DEFAULT_MARSHAL;
            return objArr;
        } else {
            if ((type2 instanceof Class) && type2 != PropertyInfo.OBJECT_CLASS) {
                objArr = (Object[]) this.classToQName.get(((Class) type2).getName());
                if (objArr != null) {
                    return objArr;
                }
            }
            objArr = new Object[4];
            objArr[QNAME_NAMESPACE] = this.xsd;
            objArr[QNAME_TYPE] = ANY_TYPE_LABEL;
            return objArr;
        }
    }

    public void addMapping(String namespace, String name, Class clazz, Marshal marshal) {
        Object obj;
        Hashtable hashtable = this.qNameToClass;
        SoapPrimitive soapPrimitive = new SoapPrimitive(namespace, name, null);
        if (marshal == null) {
            obj = clazz;
        } else {
            Marshal marshal2 = marshal;
        }
        hashtable.put(soapPrimitive, obj);
        Hashtable hashtable2 = this.classToQName;
        String name2 = clazz.getName();
        Object obj2 = new Object[4];
        obj2[QNAME_NAMESPACE] = namespace;
        obj2[QNAME_TYPE] = name;
        obj2[QNAME_MARSHAL] = marshal;
        hashtable2.put(name2, obj2);
    }

    public void addMapping(String namespace, String name, Class clazz) {
        addMapping(namespace, name, clazz, null);
    }

    public void addTemplate(SoapObject so) {
        this.qNameToClass.put(new SoapPrimitive(so.namespace, so.name, null), so);
    }

    public Object getResponse() throws SoapFault {
        if (this.bodyIn instanceof SoapFault) {
            throw ((SoapFault) this.bodyIn);
        }
        KvmSerializable ks = this.bodyIn;
        if (ks.getPropertyCount() == 0) {
            return null;
        }
        if (ks.getPropertyCount() == QNAME_TYPE) {
            return ks.getProperty(QNAME_NAMESPACE);
        }
        Object ret = new Vector();
        for (int i = QNAME_NAMESPACE; i < ks.getPropertyCount(); i += QNAME_TYPE) {
            ret.add(ks.getProperty(i));
        }
        return ret;
    }

    public void writeBody(XmlSerializer writer) throws IOException {
        if (this.bodyOut != null) {
            this.multiRef = new Vector();
            this.multiRef.addElement(this.bodyOut);
            Object[] qName = getInfo(null, this.bodyOut);
            writer.startTag(this.dotNet ? XmlPullParser.NO_NAMESPACE : (String) qName[QNAME_NAMESPACE], (String) qName[QNAME_TYPE]);
            if (this.dotNet) {
                writer.attribute(null, "xmlns", (String) qName[QNAME_NAMESPACE]);
            }
            if (this.addAdornments) {
                writer.attribute(null, ID_LABEL, qName[2] == null ? "o0" : (String) qName[2]);
                writer.attribute(this.enc, ROOT_LABEL, Contact.RELATION_FRIEND);
            }
            writeElement(writer, this.bodyOut, null, qName[QNAME_MARSHAL]);
            writer.endTag(this.dotNet ? XmlPullParser.NO_NAMESPACE : (String) qName[QNAME_NAMESPACE], (String) qName[QNAME_TYPE]);
        }
    }

    public void writeObjectBody(XmlSerializer writer, SoapObject obj) throws IOException {
        SoapObject soapObject = obj;
        int cnt = soapObject.getAttributeCount();
        for (int counter = QNAME_NAMESPACE; counter < cnt; counter += QNAME_TYPE) {
            AttributeInfo attributeInfo = new AttributeInfo();
            soapObject.getAttributeInfo(counter, attributeInfo);
            writer.attribute(attributeInfo.getNamespace(), attributeInfo.getName(), attributeInfo.getValue().toString());
        }
        writeObjectBody(writer, (KvmSerializable) obj);
    }

    public void writeObjectBody(XmlSerializer writer, KvmSerializable obj) throws IOException {
        int cnt = obj.getPropertyCount();
        PropertyInfo propertyInfo = new PropertyInfo();
        for (int i = QNAME_NAMESPACE; i < cnt; i += QNAME_TYPE) {
            SoapObject prop = obj.getProperty(i);
            obj.getPropertyInfo(i, this.properties, propertyInfo);
            if (prop instanceof SoapObject) {
                String name;
                String str;
                SoapObject nestedSoap = prop;
                Object[] qName = getInfo(null, nestedSoap);
                String namespace = qName[QNAME_NAMESPACE];
                String type = qName[QNAME_TYPE];
                if (propertyInfo.name == null || propertyInfo.name.length() <= 0) {
                    name = (String) qName[QNAME_TYPE];
                } else {
                    name = propertyInfo.name;
                }
                if (this.dotNet) {
                    str = XmlPullParser.NO_NAMESPACE;
                } else {
                    str = namespace;
                }
                writer.startTag(str, name);
                if (!this.implicitTypes) {
                    XmlSerializer xmlSerializer = writer;
                    xmlSerializer.attribute(this.xsi, TYPE_LABEL, writer.getPrefix(namespace, true) + ":" + type);
                }
                writeObjectBody(writer, nestedSoap);
                if (this.dotNet) {
                    namespace = XmlPullParser.NO_NAMESPACE;
                }
                writer.endTag(namespace, name);
            } else if ((propertyInfo.flags & QNAME_TYPE) == 0) {
                writer.startTag(propertyInfo.namespace, propertyInfo.name);
                writeProperty(writer, obj.getProperty(i), propertyInfo);
                writer.endTag(propertyInfo.namespace, propertyInfo.name);
            }
        }
    }

    protected void writeProperty(XmlSerializer writer, Object obj, PropertyInfo type) throws IOException {
        if (obj == null) {
            String str;
            String str2 = this.xsi;
            if (this.version >= SoapEnvelope.VER12) {
                str = NIL_LABEL;
            } else {
                str = NULL_LABEL;
            }
            writer.attribute(str2, str, "true");
            return;
        }
        Object[] qName = getInfo(null, obj);
        if (type.multiRef || qName[2] != null) {
            int i = this.multiRef.indexOf(obj);
            if (i == -1) {
                i = this.multiRef.size();
                this.multiRef.addElement(obj);
            }
            writer.attribute(null, HREF_LABEL, qName[2] == null ? "#o" + i : "#" + qName[2]);
            return;
        }
        if (!(this.implicitTypes && obj.getClass() == type.type)) {
            writer.attribute(this.xsi, TYPE_LABEL, writer.getPrefix((String) qName[QNAME_NAMESPACE], true) + ":" + qName[QNAME_TYPE]);
        }
        writeElement(writer, obj, type, qName[QNAME_MARSHAL]);
    }

    private void writeElement(XmlSerializer writer, Object element, PropertyInfo type, Object marshal) throws IOException {
        if (marshal != null) {
            ((Marshal) marshal).writeInstance(writer, element);
        } else if (element instanceof SoapObject) {
            writeObjectBody(writer, (SoapObject) element);
        } else if (element instanceof KvmSerializable) {
            writeObjectBody(writer, (KvmSerializable) element);
        } else if (element instanceof Vector) {
            writeVectorBody(writer, (Vector) element, type.elementType);
        } else {
            throw new RuntimeException("Cannot serialize: " + element);
        }
    }

    protected void writeVectorBody(XmlSerializer writer, Vector vector, PropertyInfo elementType) throws IOException {
        String itemsTagName = ITEM_LABEL;
        String itemsNamespace = null;
        if (elementType == null) {
            elementType = PropertyInfo.OBJECT_TYPE;
        } else if ((elementType instanceof PropertyInfo) && elementType.name != null) {
            itemsTagName = elementType.name;
            itemsNamespace = elementType.namespace;
        }
        int cnt = vector.size();
        Object[] arrType = getInfo(elementType.type, null);
        if (!this.implicitTypes) {
            writer.attribute(this.enc, ARRAY_TYPE_LABEL, writer.getPrefix((String) arrType[QNAME_NAMESPACE], false) + ":" + arrType[QNAME_TYPE] + "[" + cnt + "]");
        }
        boolean skipped = false;
        for (int i = QNAME_NAMESPACE; i < cnt; i += QNAME_TYPE) {
            if (vector.elementAt(i) == null) {
                skipped = true;
            } else {
                writer.startTag(itemsNamespace, itemsTagName);
                if (skipped) {
                    writer.attribute(this.enc, "position", "[" + i + "]");
                    skipped = false;
                }
                writeProperty(writer, vector.elementAt(i), elementType);
                writer.endTag(itemsNamespace, itemsTagName);
            }
        }
    }
}
