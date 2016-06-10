package org.jivesoftware.smackx.packet;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

public class DiscoverItems extends IQ {
    public static final String NAMESPACE = "http://jabber.org/protocol/disco#items";
    private final List<Item> items;
    private String node;

    public static class Item {
        public static final String REMOVE_ACTION = "remove";
        public static final String UPDATE_ACTION = "update";
        private String action;
        private String entityID;
        private String name;
        private String node;

        public Item(String str) {
            this.entityID = str;
        }

        public String getEntityID() {
            return this.entityID;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }

        public String getNode() {
            return this.node;
        }

        public void setNode(String str) {
            this.node = str;
        }

        public String getAction() {
            return this.action;
        }

        public void setAction(String str) {
            this.action = str;
        }

        public String toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<item jid=\"").append(this.entityID).append("\"");
            if (this.name != null) {
                stringBuilder.append(" name=\"").append(StringUtils.escapeForXML(this.name)).append("\"");
            }
            if (this.node != null) {
                stringBuilder.append(" node=\"").append(StringUtils.escapeForXML(this.node)).append("\"");
            }
            if (this.action != null) {
                stringBuilder.append(" action=\"").append(StringUtils.escapeForXML(this.action)).append("\"");
            }
            stringBuilder.append("/>");
            return stringBuilder.toString();
        }
    }

    public DiscoverItems() {
        this.items = new CopyOnWriteArrayList();
    }

    public void addItem(Item item) {
        synchronized (this.items) {
            this.items.add(item);
        }
    }

    public void addItems(Collection<Item> collection) {
        if (collection != null) {
            for (Item addItem : collection) {
                addItem(addItem);
            }
        }
    }

    public Iterator<Item> getItems() {
        Iterator<Item> it;
        synchronized (this.items) {
            it = Collections.unmodifiableList(this.items).iterator();
        }
        return it;
    }

    public String getNode() {
        return this.node;
    }

    public void setNode(String str) {
        this.node = str;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<query xmlns=\"http://jabber.org/protocol/disco#items\"");
        if (getNode() != null) {
            stringBuilder.append(" node=\"");
            stringBuilder.append(StringUtils.escapeForXML(getNode()));
            stringBuilder.append("\"");
        }
        stringBuilder.append(">");
        synchronized (this.items) {
            for (Item toXML : this.items) {
                stringBuilder.append(toXML.toXML());
            }
        }
        stringBuilder.append("</query>");
        return stringBuilder.toString();
    }
}
