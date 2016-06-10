package com.thoughtworks.xstream.io.xml.xppdom;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XppDom implements Serializable {
    private static final long serialVersionUID = 1;
    private Map attributes;
    private List childList;
    private transient Map childMap;
    private String name;
    private XppDom parent;
    private String value;

    public XppDom(String name) {
        this.name = name;
        this.childList = new ArrayList();
        this.childMap = new HashMap();
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getAttributeNames() {
        if (this.attributes == null) {
            return new String[0];
        }
        return (String[]) this.attributes.keySet().toArray(new String[0]);
    }

    public String getAttribute(String name) {
        return this.attributes != null ? (String) this.attributes.get(name) : null;
    }

    public void setAttribute(String name, String value) {
        if (this.attributes == null) {
            this.attributes = new HashMap();
        }
        this.attributes.put(name, value);
    }

    public XppDom getChild(int i) {
        return (XppDom) this.childList.get(i);
    }

    public XppDom getChild(String name) {
        return (XppDom) this.childMap.get(name);
    }

    public void addChild(XppDom xpp3Dom) {
        xpp3Dom.setParent(this);
        this.childList.add(xpp3Dom);
        this.childMap.put(xpp3Dom.getName(), xpp3Dom);
    }

    public XppDom[] getChildren() {
        if (this.childList == null) {
            return new XppDom[0];
        }
        return (XppDom[]) this.childList.toArray(new XppDom[0]);
    }

    public XppDom[] getChildren(String name) {
        if (this.childList == null) {
            return new XppDom[0];
        }
        ArrayList children = new ArrayList();
        int size = this.childList.size();
        for (int i = 0; i < size; i++) {
            XppDom configuration = (XppDom) this.childList.get(i);
            if (name.equals(configuration.getName())) {
                children.add(configuration);
            }
        }
        return (XppDom[]) children.toArray(new XppDom[0]);
    }

    public int getChildCount() {
        if (this.childList == null) {
            return 0;
        }
        return this.childList.size();
    }

    public XppDom getParent() {
        return this.parent;
    }

    public void setParent(XppDom parent) {
        this.parent = parent;
    }

    Object readResolve() {
        this.childMap = new HashMap();
        for (XppDom element : this.childList) {
            this.childMap.put(element.getName(), element);
        }
        return this;
    }

    public static XppDom build(XmlPullParser parser) throws XmlPullParserException, IOException {
        List elements = new ArrayList();
        List values = new ArrayList();
        XppDom node = null;
        int eventType = parser.getEventType();
        while (eventType != 1) {
            int depth;
            if (eventType == 2) {
                XppDom child = new Xpp3Dom(parser.getName());
                depth = elements.size();
                if (depth > 0) {
                    ((XppDom) elements.get(depth - 1)).addChild(child);
                }
                elements.add(child);
                values.add(new StringBuffer());
                int attributesSize = parser.getAttributeCount();
                for (int i = 0; i < attributesSize; i++) {
                    child.setAttribute(parser.getAttributeName(i), parser.getAttributeValue(i));
                }
            } else if (eventType == 4) {
                List list = values;
                ((StringBuffer) list.get(values.size() - 1)).append(parser.getText());
            } else if (eventType == 3) {
                String finishedValue;
                depth = elements.size() - 1;
                XppDom finalNode = (XppDom) elements.remove(depth);
                String accumulatedValue = values.remove(depth).toString();
                if (accumulatedValue.length() == 0) {
                    finishedValue = null;
                } else {
                    finishedValue = accumulatedValue;
                }
                finalNode.setValue(finishedValue);
                if (depth == 0) {
                    node = finalNode;
                }
            }
            eventType = parser.next();
        }
        return node;
    }
}
