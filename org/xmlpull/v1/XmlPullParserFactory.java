package org.xmlpull.v1;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class XmlPullParserFactory {
    public static final String PROPERTY_NAME = "org.xmlpull.v1.XmlPullParserFactory";
    private static final String RESOURCE_NAME = "/META-INF/services/org.xmlpull.v1.XmlPullParserFactory";
    static final Class referenceContextClass;
    protected String classNamesLocation;
    protected Hashtable features;
    protected Vector parserClasses;
    protected Vector serializerClasses;

    static {
        referenceContextClass = new XmlPullParserFactory().getClass();
    }

    protected XmlPullParserFactory() {
        this.features = new Hashtable();
    }

    public void setFeature(String name, boolean state) throws XmlPullParserException {
        this.features.put(name, new Boolean(state));
    }

    public boolean getFeature(String name) {
        Boolean value = (Boolean) this.features.get(name);
        return value != null ? value.booleanValue() : false;
    }

    public void setNamespaceAware(boolean awareness) {
        this.features.put(XmlPullParser.FEATURE_PROCESS_NAMESPACES, new Boolean(awareness));
    }

    public boolean isNamespaceAware() {
        return getFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES);
    }

    public void setValidating(boolean validating) {
        this.features.put(XmlPullParser.FEATURE_VALIDATION, new Boolean(validating));
    }

    public boolean isValidating() {
        return getFeature(XmlPullParser.FEATURE_VALIDATION);
    }

    public XmlPullParser newPullParser() throws XmlPullParserException {
        if (this.parserClasses == null) {
            throw new XmlPullParserException(new StringBuffer().append("Factory initialization was incomplete - has not tried ").append(this.classNamesLocation).toString());
        } else if (this.parserClasses.size() == 0) {
            throw new XmlPullParserException(new StringBuffer().append("No valid parser classes found in ").append(this.classNamesLocation).toString());
        } else {
            StringBuffer issues = new StringBuffer();
            int i = 0;
            while (i < this.parserClasses.size()) {
                Class ppClass = (Class) this.parserClasses.elementAt(i);
                try {
                    XmlPullParser pp = (XmlPullParser) ppClass.newInstance();
                    Enumeration e = this.features.keys();
                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        Boolean value = (Boolean) this.features.get(key);
                        if (value != null && value.booleanValue()) {
                            pp.setFeature(key, true);
                        }
                    }
                    return pp;
                } catch (Exception ex) {
                    issues.append(new StringBuffer().append(ppClass.getName()).append(": ").append(ex.toString()).append("; ").toString());
                    i++;
                }
            }
            throw new XmlPullParserException(new StringBuffer().append("could not create parser: ").append(issues).toString());
        }
    }

    public XmlSerializer newSerializer() throws XmlPullParserException {
        if (this.serializerClasses == null) {
            throw new XmlPullParserException(new StringBuffer().append("Factory initialization incomplete - has not tried ").append(this.classNamesLocation).toString());
        } else if (this.serializerClasses.size() == 0) {
            throw new XmlPullParserException(new StringBuffer().append("No valid serializer classes found in ").append(this.classNamesLocation).toString());
        } else {
            StringBuffer issues = new StringBuffer();
            int i = 0;
            while (i < this.serializerClasses.size()) {
                Class ppClass = (Class) this.serializerClasses.elementAt(i);
                try {
                    return (XmlSerializer) ppClass.newInstance();
                } catch (Exception ex) {
                    issues.append(new StringBuffer().append(ppClass.getName()).append(": ").append(ex.toString()).append("; ").toString());
                    i++;
                }
            }
            throw new XmlPullParserException(new StringBuffer().append("could not create serializer: ").append(issues).toString());
        }
    }

    public static XmlPullParserFactory newInstance() throws XmlPullParserException {
        return newInstance(null, null);
    }

    public static XmlPullParserFactory newInstance(String classNames, Class context) throws XmlPullParserException {
        XmlPullParserFactory factory;
        Vector parserClasses;
        Vector serializerClasses;
        int pos;
        int cut;
        String name;
        Class candidate;
        XmlPullParserFactory instance;
        boolean recognized;
        if (context == null) {
            context = referenceContextClass;
        }
        if (!(classNames == null || classNames.length() == 0)) {
            if (!"DEFAULT".equals(classNames)) {
                String classNamesLocation = new StringBuffer().append("parameter classNames to newInstance() that contained '").append(classNames).append("'").toString();
                factory = null;
                parserClasses = new Vector();
                serializerClasses = new Vector();
                pos = 0;
                while (pos < classNames.length()) {
                    cut = classNames.indexOf(44, pos);
                    if (cut == -1) {
                        cut = classNames.length();
                    }
                    name = classNames.substring(pos, cut);
                    candidate = null;
                    instance = null;
                    try {
                        candidate = Class.forName(name);
                        instance = candidate.newInstance();
                    } catch (Exception e) {
                    }
                    if (candidate != null) {
                        recognized = false;
                        if (instance instanceof XmlPullParser) {
                            parserClasses.addElement(candidate);
                            recognized = true;
                        }
                        if (instance instanceof XmlSerializer) {
                            serializerClasses.addElement(candidate);
                            recognized = true;
                        }
                        if (instance instanceof XmlPullParserFactory) {
                            if (factory == null) {
                                factory = instance;
                            }
                            recognized = true;
                        }
                        if (!recognized) {
                            throw new XmlPullParserException(new StringBuffer().append("incompatible class: ").append(name).toString());
                        }
                    }
                    pos = cut + 1;
                }
                if (factory == null) {
                    factory = new XmlPullParserFactory();
                }
                factory.parserClasses = parserClasses;
                factory.serializerClasses = serializerClasses;
                factory.classNamesLocation = classNamesLocation;
                return factory;
            }
        }
        try {
            InputStream is = context.getResourceAsStream(RESOURCE_NAME);
            if (is == null) {
                throw new XmlPullParserException("resource not found: /META-INF/services/org.xmlpull.v1.XmlPullParserFactory make sure that parser implementing XmlPull API is available");
            }
            StringBuffer sb = new StringBuffer();
            while (true) {
                int ch = is.read();
                if (ch < 0) {
                    break;
                } else if (ch > 32) {
                    sb.append((char) ch);
                }
            }
            is.close();
            classNames = sb.toString();
            classNamesLocation = new StringBuffer().append("resource /META-INF/services/org.xmlpull.v1.XmlPullParserFactory that contained '").append(classNames).append("'").toString();
            factory = null;
            parserClasses = new Vector();
            serializerClasses = new Vector();
            pos = 0;
            while (pos < classNames.length()) {
                cut = classNames.indexOf(44, pos);
                if (cut == -1) {
                    cut = classNames.length();
                }
                name = classNames.substring(pos, cut);
                candidate = null;
                instance = null;
                candidate = Class.forName(name);
                instance = candidate.newInstance();
                if (candidate != null) {
                    recognized = false;
                    if (instance instanceof XmlPullParser) {
                        parserClasses.addElement(candidate);
                        recognized = true;
                    }
                    if (instance instanceof XmlSerializer) {
                        serializerClasses.addElement(candidate);
                        recognized = true;
                    }
                    if (instance instanceof XmlPullParserFactory) {
                        if (factory == null) {
                            factory = instance;
                        }
                        recognized = true;
                    }
                    if (!recognized) {
                        throw new XmlPullParserException(new StringBuffer().append("incompatible class: ").append(name).toString());
                    }
                }
                pos = cut + 1;
            }
            if (factory == null) {
                factory = new XmlPullParserFactory();
            }
            factory.parserClasses = parserClasses;
            factory.serializerClasses = serializerClasses;
            factory.classNamesLocation = classNamesLocation;
            return factory;
        } catch (Exception e2) {
            throw new XmlPullParserException(null, null, e2);
        }
    }
}
