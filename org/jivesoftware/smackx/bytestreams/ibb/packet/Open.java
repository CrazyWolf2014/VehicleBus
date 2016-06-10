package org.jivesoftware.smackx.bytestreams.ibb.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager.StanzaType;
import org.xmlpull.v1.XmlPullParser;

public class Open extends IQ {
    private final int blockSize;
    private final String sessionID;
    private final StanzaType stanza;

    public Open(String str, int i, StanzaType stanzaType) {
        if (str == null || XmlPullParser.NO_NAMESPACE.equals(str)) {
            throw new IllegalArgumentException("Session ID must not be null or empty");
        } else if (i <= 0) {
            throw new IllegalArgumentException("Block size must be greater than zero");
        } else {
            this.sessionID = str;
            this.blockSize = i;
            this.stanza = stanzaType;
            setType(Type.SET);
        }
    }

    public Open(String str, int i) {
        this(str, i, StanzaType.IQ);
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public StanzaType getStanza() {
        return this.stanza;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<open ");
        stringBuilder.append("xmlns=\"");
        stringBuilder.append(InBandBytestreamManager.NAMESPACE);
        stringBuilder.append("\" ");
        stringBuilder.append("block-size=\"");
        stringBuilder.append(this.blockSize);
        stringBuilder.append("\" ");
        stringBuilder.append("sid=\"");
        stringBuilder.append(this.sessionID);
        stringBuilder.append("\" ");
        stringBuilder.append("stanza=\"");
        stringBuilder.append(this.stanza.toString().toLowerCase());
        stringBuilder.append("\"");
        stringBuilder.append("/>");
        return stringBuilder.toString();
    }
}
