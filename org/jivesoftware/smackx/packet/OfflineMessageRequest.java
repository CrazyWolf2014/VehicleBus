package org.jivesoftware.smackx.packet;

import com.ifoer.entity.Constant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class OfflineMessageRequest extends IQ {
    private boolean fetch;
    private List<Item> items;
    private boolean purge;

    public static class Item {
        private String action;
        private String jid;
        private String node;

        public Item(String str) {
            this.node = str;
        }

        public String getNode() {
            return this.node;
        }

        public String getAction() {
            return this.action;
        }

        public void setAction(String str) {
            this.action = str;
        }

        public String getJid() {
            return this.jid;
        }

        public void setJid(String str) {
            this.jid = str;
        }

        public String toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<item");
            if (getAction() != null) {
                stringBuilder.append(" action=\"").append(getAction()).append("\"");
            }
            if (getJid() != null) {
                stringBuilder.append(" jid=\"").append(getJid()).append("\"");
            }
            if (getNode() != null) {
                stringBuilder.append(" node=\"").append(getNode()).append("\"");
            }
            stringBuilder.append("/>");
            return stringBuilder.toString();
        }
    }

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            IQ offlineMessageRequest = new OfflineMessageRequest();
            boolean z = false;
            while (!z) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    if (xmlPullParser.getName().equals("item")) {
                        offlineMessageRequest.addItem(parseItem(xmlPullParser));
                    } else if (xmlPullParser.getName().equals("purge")) {
                        offlineMessageRequest.setPurge(true);
                    } else if (xmlPullParser.getName().equals("fetch")) {
                        offlineMessageRequest.setFetch(true);
                    }
                } else if (next == 3 && xmlPullParser.getName().equals(MessageEvent.OFFLINE)) {
                    z = true;
                }
            }
            return offlineMessageRequest;
        }

        private Item parseItem(XmlPullParser xmlPullParser) throws Exception {
            Object obj = null;
            Item item = new Item(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "node"));
            item.setAction(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, Constant.ACTION));
            item.setJid(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid"));
            while (obj == null) {
                if (xmlPullParser.next() == 3 && xmlPullParser.getName().equals("item")) {
                    obj = 1;
                }
            }
            return item;
        }
    }

    public OfflineMessageRequest() {
        this.items = new ArrayList();
        this.purge = false;
        this.fetch = false;
    }

    public Iterator<Item> getItems() {
        Iterator<Item> it;
        synchronized (this.items) {
            it = Collections.unmodifiableList(new ArrayList(this.items)).iterator();
        }
        return it;
    }

    public void addItem(Item item) {
        synchronized (this.items) {
            this.items.add(item);
        }
    }

    public boolean isPurge() {
        return this.purge;
    }

    public void setPurge(boolean z) {
        this.purge = z;
    }

    public boolean isFetch() {
        return this.fetch;
    }

    public void setFetch(boolean z) {
        this.fetch = z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getChildElementXML() {
        /*
        r4 = this;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = "<offline xmlns=\"http://jabber.org/protocol/offline\">";
        r2.append(r0);
        r3 = r4.items;
        monitor-enter(r3);
        r0 = 0;
        r1 = r0;
    L_0x000f:
        r0 = r4.items;	 Catch:{ all -> 0x004e }
        r0 = r0.size();	 Catch:{ all -> 0x004e }
        if (r1 >= r0) goto L_0x002a;
    L_0x0017:
        r0 = r4.items;	 Catch:{ all -> 0x004e }
        r0 = r0.get(r1);	 Catch:{ all -> 0x004e }
        r0 = (org.jivesoftware.smackx.packet.OfflineMessageRequest.Item) r0;	 Catch:{ all -> 0x004e }
        r0 = r0.toXML();	 Catch:{ all -> 0x004e }
        r2.append(r0);	 Catch:{ all -> 0x004e }
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x000f;
    L_0x002a:
        monitor-exit(r3);	 Catch:{ all -> 0x004e }
        r0 = r4.purge;
        if (r0 == 0) goto L_0x0034;
    L_0x002f:
        r0 = "<purge/>";
        r2.append(r0);
    L_0x0034:
        r0 = r4.fetch;
        if (r0 == 0) goto L_0x003d;
    L_0x0038:
        r0 = "<fetch/>";
        r2.append(r0);
    L_0x003d:
        r0 = r4.getExtensionsXML();
        r2.append(r0);
        r0 = "</offline>";
        r2.append(r0);
        r0 = r2.toString();
        return r0;
    L_0x004e:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x004e }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.packet.OfflineMessageRequest.getChildElementXML():java.lang.String");
    }
}
