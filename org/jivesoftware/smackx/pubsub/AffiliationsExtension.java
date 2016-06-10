package org.jivesoftware.smackx.pubsub;

import java.util.Collections;
import java.util.List;

public class AffiliationsExtension extends NodeExtension {
    protected List<Affiliation> items;

    public AffiliationsExtension() {
        super(PubSubElementType.AFFILIATIONS);
        this.items = Collections.EMPTY_LIST;
    }

    public AffiliationsExtension(List<Affiliation> list) {
        super(PubSubElementType.AFFILIATIONS);
        this.items = Collections.EMPTY_LIST;
        if (list != null) {
            this.items = list;
        }
    }

    public AffiliationsExtension(String str, List<Affiliation> list) {
        super(PubSubElementType.AFFILIATIONS, str);
        this.items = Collections.EMPTY_LIST;
        if (list != null) {
            this.items = list;
        }
    }

    public List<Affiliation> getAffiliations() {
        return this.items;
    }

    public String toXML() {
        if (this.items == null || this.items.size() == 0) {
            return super.toXML();
        }
        StringBuilder stringBuilder = new StringBuilder("<");
        stringBuilder.append(getElementName());
        if (getNode() != null) {
            stringBuilder.append(" node='");
            stringBuilder.append(getNode());
            stringBuilder.append("'");
        }
        stringBuilder.append(">");
        for (Affiliation toXML : this.items) {
            stringBuilder.append(toXML.toXML());
        }
        stringBuilder.append("</");
        stringBuilder.append(getElementName());
        stringBuilder.append(">");
        return stringBuilder.toString();
    }
}
