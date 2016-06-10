package org.jivesoftware.smackx.workgroup.agent;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

public class Offer {
    private boolean accepted;
    private Connection connection;
    private OfferContent content;
    private Date expiresDate;
    private Map<String, List<String>> metaData;
    private boolean rejected;
    private AgentSession session;
    private String sessionID;
    private String userID;
    private String userJID;
    private String workgroupName;

    private class AcceptPacket extends IQ {
        AcceptPacket(String str) {
            setTo(str);
            setType(Type.SET);
        }

        public String getChildElementXML() {
            return "<offer-accept id=\"" + Offer.this.getSessionID() + "\" xmlns=\"http://jabber.org/protocol/workgroup" + "\"/>";
        }
    }

    private class RejectPacket extends IQ {
        RejectPacket(String str) {
            setTo(str);
            setType(Type.SET);
        }

        public String getChildElementXML() {
            return "<offer-reject id=\"" + Offer.this.getSessionID() + "\" xmlns=\"http://jabber.org/protocol/workgroup" + "\"/>";
        }
    }

    Offer(Connection connection, AgentSession agentSession, String str, String str2, String str3, Date date, String str4, Map<String, List<String>> map, OfferContent offerContent) {
        this.accepted = false;
        this.rejected = false;
        this.connection = connection;
        this.session = agentSession;
        this.userID = str;
        this.userJID = str2;
        this.workgroupName = str3;
        this.expiresDate = date;
        this.sessionID = str4;
        this.metaData = map;
        this.content = offerContent;
    }

    public void accept() {
        this.connection.sendPacket(new AcceptPacket(this.session.getWorkgroupJID()));
        this.accepted = true;
    }

    public void reject() {
        this.connection.sendPacket(new RejectPacket(this.session.getWorkgroupJID()));
        this.rejected = true;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getUserJID() {
        return this.userJID;
    }

    public String getWorkgroupName() {
        return this.workgroupName;
    }

    public Date getExpiresDate() {
        return this.expiresDate;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public Map<String, List<String>> getMetaData() {
        return this.metaData;
    }

    public OfferContent getContent() {
        return this.content;
    }

    public boolean isAccepted() {
        return this.accepted;
    }

    public boolean isRejected() {
        return this.rejected;
    }
}
