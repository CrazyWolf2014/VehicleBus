package org.jivesoftware.smackx.packet;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;

public class DiscoverInfo extends IQ {
    public static final String NAMESPACE = "http://jabber.org/protocol/disco#info";
    private final List<Feature> features;
    private final List<Identity> identities;
    private String node;

    public static class Feature {
        private String variable;

        public Feature(String str) {
            this.variable = str;
        }

        public String getVar() {
            return this.variable;
        }

        public String toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<feature var=\"").append(StringUtils.escapeForXML(this.variable)).append("\"/>");
            return stringBuilder.toString();
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            return this.variable.equals(((Feature) obj).variable);
        }
    }

    public static class Identity implements Comparable<Object> {
        private String category;
        private String lang;
        private String name;
        private String type;

        public Identity(String str, String str2, String str3) {
            this.category = str;
            this.name = str2;
            this.type = str3;
        }

        public String getCategory() {
            return this.category;
        }

        public String getName() {
            return this.name;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public void setLanguage(String str) {
            this.lang = str;
        }

        public String getLanguage() {
            return this.lang;
        }

        public String toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<identity");
            if (this.lang != null) {
                stringBuilder.append(" xml:lang=\"").append(StringUtils.escapeForXML(this.lang)).append("\"");
            }
            stringBuilder.append(" category=\"").append(StringUtils.escapeForXML(this.category)).append("\"");
            stringBuilder.append(" name=\"").append(StringUtils.escapeForXML(this.name)).append("\"");
            if (this.type != null) {
                stringBuilder.append(" type=\"").append(StringUtils.escapeForXML(this.type)).append("\"");
            }
            stringBuilder.append("/>");
            return stringBuilder.toString();
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            Identity identity = (Identity) obj;
            if (!this.category.equals(identity.category)) {
                return false;
            }
            String str = identity.lang == null ? XmlPullParser.NO_NAMESPACE : identity.lang;
            Object obj2 = this.lang == null ? XmlPullParser.NO_NAMESPACE : this.lang;
            if (!identity.type.equals(this.type)) {
                return false;
            }
            if (!str.equals(obj2)) {
                return false;
            }
            if ((this.name == null ? XmlPullParser.NO_NAMESPACE : identity.name).equals(identity.name == null ? XmlPullParser.NO_NAMESPACE : identity.name)) {
                return true;
            }
            return false;
        }

        public int compareTo(Object obj) {
            Identity identity = (Identity) obj;
            String str = identity.lang == null ? XmlPullParser.NO_NAMESPACE : identity.lang;
            String str2 = this.lang == null ? XmlPullParser.NO_NAMESPACE : this.lang;
            if (!this.category.equals(identity.category)) {
                return this.category.compareTo(identity.category);
            }
            if (!this.type.equals(identity.type)) {
                return this.type.compareTo(identity.type);
            }
            if (str2.equals(str)) {
                return 0;
            }
            return str2.compareTo(str);
        }
    }

    public DiscoverInfo() {
        this.features = new CopyOnWriteArrayList();
        this.identities = new CopyOnWriteArrayList();
    }

    public DiscoverInfo(DiscoverInfo discoverInfo) {
        super(discoverInfo);
        this.features = new CopyOnWriteArrayList();
        this.identities = new CopyOnWriteArrayList();
        setNode(discoverInfo.getNode());
        synchronized (discoverInfo.features) {
            for (Feature addFeature : discoverInfo.features) {
                addFeature(addFeature);
            }
        }
        synchronized (discoverInfo.identities) {
            for (Identity addIdentity : discoverInfo.identities) {
                addIdentity(addIdentity);
            }
        }
    }

    public void addFeature(String str) {
        addFeature(new Feature(str));
    }

    public void addFeatures(Collection<String> collection) {
        if (collection != null) {
            for (String addFeature : collection) {
                addFeature(addFeature);
            }
        }
    }

    private void addFeature(Feature feature) {
        synchronized (this.features) {
            this.features.add(feature);
        }
    }

    public Iterator<Feature> getFeatures() {
        Iterator<Feature> it;
        synchronized (this.features) {
            it = Collections.unmodifiableList(this.features).iterator();
        }
        return it;
    }

    public void addIdentity(Identity identity) {
        synchronized (this.identities) {
            this.identities.add(identity);
        }
    }

    public void addIdentities(Collection<Identity> collection) {
        if (collection != null) {
            synchronized (this.identities) {
                this.identities.addAll(collection);
            }
        }
    }

    public Iterator<Identity> getIdentities() {
        Iterator<Identity> it;
        synchronized (this.identities) {
            it = Collections.unmodifiableList(this.identities).iterator();
        }
        return it;
    }

    public String getNode() {
        return this.node;
    }

    public void setNode(String str) {
        this.node = str;
    }

    public boolean containsFeature(String str) {
        Iterator features = getFeatures();
        while (features.hasNext()) {
            if (str.equals(((Feature) features.next()).getVar())) {
                return true;
            }
        }
        return false;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<query xmlns=\"http://jabber.org/protocol/disco#info\"");
        if (getNode() != null) {
            stringBuilder.append(" node=\"");
            stringBuilder.append(StringUtils.escapeForXML(getNode()));
            stringBuilder.append("\"");
        }
        stringBuilder.append(">");
        synchronized (this.identities) {
            for (Identity toXML : this.identities) {
                stringBuilder.append(toXML.toXML());
            }
        }
        synchronized (this.features) {
            for (Feature toXML2 : this.features) {
                stringBuilder.append(toXML2.toXML());
            }
        }
        stringBuilder.append(getExtensionsXML());
        stringBuilder.append("</query>");
        return stringBuilder.toString();
    }

    public boolean containsDuplicateIdentities() {
        List<Identity> linkedList = new LinkedList();
        for (Identity identity : this.identities) {
            for (Identity equals : linkedList) {
                if (identity.equals(equals)) {
                    return true;
                }
            }
            linkedList.add(identity);
        }
        return false;
    }

    public boolean containsDuplicateFeatures() {
        List<Feature> linkedList = new LinkedList();
        for (Feature feature : this.features) {
            for (Feature equals : linkedList) {
                if (feature.equals(equals)) {
                    return true;
                }
            }
            linkedList.add(feature);
        }
        return false;
    }
}
