package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;

public class MUCAdmin extends IQ {
    private List<Item> items;

    public static class Item {
        private String actor;
        private String affiliation;
        private String jid;
        private String nick;
        private String reason;
        private String role;

        public Item(String str, String str2) {
            this.affiliation = str;
            this.role = str2;
        }

        public String getActor() {
            return this.actor;
        }

        public String getReason() {
            return this.reason;
        }

        public String getAffiliation() {
            return this.affiliation;
        }

        public String getJid() {
            return this.jid;
        }

        public String getNick() {
            return this.nick;
        }

        public String getRole() {
            return this.role;
        }

        public void setActor(String str) {
            this.actor = str;
        }

        public void setReason(String str) {
            this.reason = str;
        }

        public void setJid(String str) {
            this.jid = str;
        }

        public void setNick(String str) {
            this.nick = str;
        }

        public String toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<item");
            if (getAffiliation() != null) {
                stringBuilder.append(" affiliation=\"").append(getAffiliation()).append("\"");
            }
            if (getJid() != null) {
                stringBuilder.append(" jid=\"").append(getJid()).append("\"");
            }
            if (getNick() != null) {
                stringBuilder.append(" nick=\"").append(getNick()).append("\"");
            }
            if (getRole() != null) {
                stringBuilder.append(" role=\"").append(getRole()).append("\"");
            }
            if (getReason() == null && getActor() == null) {
                stringBuilder.append("/>");
            } else {
                stringBuilder.append(">");
                if (getReason() != null) {
                    stringBuilder.append("<reason>").append(getReason()).append("</reason>");
                }
                if (getActor() != null) {
                    stringBuilder.append("<actor jid=\"").append(getActor()).append("\"/>");
                }
                stringBuilder.append("</item>");
            }
            return stringBuilder.toString();
        }
    }

    public MUCAdmin() {
        this.items = new ArrayList();
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getChildElementXML() {
        /*
        r4 = this;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = "<query xmlns=\"http://jabber.org/protocol/muc#admin\">";
        r2.append(r0);
        r3 = r4.items;
        monitor-enter(r3);
        r0 = 0;
        r1 = r0;
    L_0x000f:
        r0 = r4.items;	 Catch:{ all -> 0x003c }
        r0 = r0.size();	 Catch:{ all -> 0x003c }
        if (r1 >= r0) goto L_0x002a;
    L_0x0017:
        r0 = r4.items;	 Catch:{ all -> 0x003c }
        r0 = r0.get(r1);	 Catch:{ all -> 0x003c }
        r0 = (org.jivesoftware.smackx.packet.MUCAdmin.Item) r0;	 Catch:{ all -> 0x003c }
        r0 = r0.toXML();	 Catch:{ all -> 0x003c }
        r2.append(r0);	 Catch:{ all -> 0x003c }
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x000f;
    L_0x002a:
        monitor-exit(r3);	 Catch:{ all -> 0x003c }
        r0 = r4.getExtensionsXML();
        r2.append(r0);
        r0 = "</query>";
        r2.append(r0);
        r0 = r2.toString();
        return r0;
    L_0x003c:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x003c }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.packet.MUCAdmin.getChildElementXML():java.lang.String");
    }
}
