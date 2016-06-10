package com.kenai.jbosh;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public final class ComposableBody extends AbstractBody {
    private static final Pattern BOSH_START;
    private final Map<BodyQName, String> attrs;
    private final AtomicReference<String> computed;
    private final String payload;

    public static final class Builder {
        private boolean doMapCopy;
        private Map<BodyQName, String> map;
        private String payloadXML;

        private Builder() {
        }

        private static Builder fromBody(ComposableBody composableBody) {
            Builder builder = new Builder();
            builder.map = composableBody.getAttributes();
            builder.doMapCopy = true;
            builder.payloadXML = composableBody.payload;
            return builder;
        }

        public Builder setPayloadXML(String str) {
            if (str == null) {
                throw new IllegalArgumentException("payload XML argument cannot be null");
            }
            this.payloadXML = str;
            return this;
        }

        public Builder setAttribute(BodyQName bodyQName, String str) {
            if (this.map == null) {
                this.map = new HashMap();
            } else if (this.doMapCopy) {
                this.map = new HashMap(this.map);
                this.doMapCopy = false;
            }
            if (str == null) {
                this.map.remove(bodyQName);
            } else {
                this.map.put(bodyQName, str);
            }
            return this;
        }

        public Builder setNamespaceDefinition(String str, String str2) {
            return setAttribute(BodyQName.createWithPrefix("http://www.w3.org/XML/1998/namespace", str, "xmlns"), str2);
        }

        public ComposableBody build() {
            if (this.map == null) {
                this.map = new HashMap();
            }
            if (this.payloadXML == null) {
                this.payloadXML = XmlPullParser.NO_NAMESPACE;
            }
            return new ComposableBody(this.payloadXML, null);
        }
    }

    static {
        BOSH_START = Pattern.compile("<(?:(?:[^:\t\n\r >]+:)|(?:\\{[^\\}>]*?}))?body(?:[\t\n\r ][^>]*?)?(/>|>)");
    }

    private ComposableBody(Map<BodyQName, String> map, String str) {
        this.computed = new AtomicReference();
        this.attrs = map;
        this.payload = str;
    }

    static ComposableBody fromStaticBody(StaticBody staticBody) throws BOSHException {
        String toXML = staticBody.toXML();
        Matcher matcher = BOSH_START.matcher(toXML);
        if (matcher.find()) {
            String substring;
            if (">".equals(matcher.group(1))) {
                int end = matcher.end();
                int lastIndexOf = toXML.lastIndexOf("</");
                if (lastIndexOf < end) {
                    lastIndexOf = end;
                }
                substring = toXML.substring(end, lastIndexOf);
            } else {
                substring = XmlPullParser.NO_NAMESPACE;
            }
            return new ComposableBody(staticBody.getAttributes(), substring);
        }
        throw new BOSHException("Could not locate 'body' element in XML.  The raw XML did not match the pattern: " + BOSH_START);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder rebuild() {
        return Builder.fromBody(this);
    }

    public Map<BodyQName, String> getAttributes() {
        return Collections.unmodifiableMap(this.attrs);
    }

    public String toXML() {
        String str = (String) this.computed.get();
        if (str != null) {
            return str;
        }
        str = computeXML();
        this.computed.set(str);
        return str;
    }

    public String getPayloadXML() {
        return this.payload;
    }

    private String escape(String str) {
        return str.replace("'", "&apos;");
    }

    private String computeXML() {
        BodyQName bodyQName = AbstractBody.getBodyQName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<");
        stringBuilder.append(bodyQName.getLocalPart());
        for (Entry entry : this.attrs.entrySet()) {
            stringBuilder.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            BodyQName bodyQName2 = (BodyQName) entry.getKey();
            String prefix = bodyQName2.getPrefix();
            if (prefix != null && prefix.length() > 0) {
                stringBuilder.append(prefix);
                stringBuilder.append(":");
            }
            stringBuilder.append(bodyQName2.getLocalPart());
            stringBuilder.append("='");
            stringBuilder.append(escape((String) entry.getValue()));
            stringBuilder.append("'");
        }
        stringBuilder.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuilder.append("xmlns");
        stringBuilder.append("='");
        stringBuilder.append(bodyQName.getNamespaceURI());
        stringBuilder.append("'>");
        if (this.payload != null) {
            stringBuilder.append(this.payload);
        }
        stringBuilder.append("</body>");
        return stringBuilder.toString();
    }
}
