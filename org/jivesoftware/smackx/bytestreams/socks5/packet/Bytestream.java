package org.jivesoftware.smackx.bytestreams.socks5.packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.PacketExtension;
import org.xmlpull.v1.XmlPullParser;

public class Bytestream extends IQ {
    private Mode mode;
    private String sessionID;
    private final List<StreamHost> streamHosts;
    private Activate toActivate;
    private StreamHostUsed usedHost;

    public enum Mode {
        tcp,
        udp;

        public static Mode fromName(String str) {
            try {
                return valueOf(str);
            } catch (Exception e) {
                return tcp;
            }
        }
    }

    public static class Activate implements PacketExtension {
        public static String ELEMENTNAME;
        public String NAMESPACE;
        private final String target;

        static {
            ELEMENTNAME = "activate";
        }

        public Activate(String str) {
            this.NAMESPACE = XmlPullParser.NO_NAMESPACE;
            this.target = str;
        }

        public String getTarget() {
            return this.target;
        }

        public String getNamespace() {
            return this.NAMESPACE;
        }

        public String getElementName() {
            return ELEMENTNAME;
        }

        public String toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<").append(getElementName()).append(">");
            stringBuilder.append(getTarget());
            stringBuilder.append("</").append(getElementName()).append(">");
            return stringBuilder.toString();
        }
    }

    public static class StreamHost implements PacketExtension {
        public static String ELEMENTNAME;
        public static String NAMESPACE;
        private final String JID;
        private final String addy;
        private int port;

        static {
            NAMESPACE = XmlPullParser.NO_NAMESPACE;
            ELEMENTNAME = "streamhost";
        }

        public StreamHost(String str, String str2) {
            this.port = 0;
            this.JID = str;
            this.addy = str2;
        }

        public String getJID() {
            return this.JID;
        }

        public String getAddress() {
            return this.addy;
        }

        public void setPort(int i) {
            this.port = i;
        }

        public int getPort() {
            return this.port;
        }

        public String getNamespace() {
            return NAMESPACE;
        }

        public String getElementName() {
            return ELEMENTNAME;
        }

        public String toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<").append(getElementName()).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            stringBuilder.append("jid=\"").append(getJID()).append("\" ");
            stringBuilder.append("host=\"").append(getAddress()).append("\" ");
            if (getPort() != 0) {
                stringBuilder.append("port=\"").append(getPort()).append("\"");
            } else {
                stringBuilder.append("zeroconf=\"_jabber.bytestreams\"");
            }
            stringBuilder.append("/>");
            return stringBuilder.toString();
        }
    }

    public static class StreamHostUsed implements PacketExtension {
        public static String ELEMENTNAME;
        private final String JID;
        public String NAMESPACE;

        static {
            ELEMENTNAME = "streamhost-used";
        }

        public StreamHostUsed(String str) {
            this.NAMESPACE = XmlPullParser.NO_NAMESPACE;
            this.JID = str;
        }

        public String getJID() {
            return this.JID;
        }

        public String getNamespace() {
            return this.NAMESPACE;
        }

        public String getElementName() {
            return ELEMENTNAME;
        }

        public String toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<").append(getElementName()).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            stringBuilder.append("jid=\"").append(getJID()).append("\" ");
            stringBuilder.append("/>");
            return stringBuilder.toString();
        }
    }

    public Bytestream() {
        this.mode = Mode.tcp;
        this.streamHosts = new ArrayList();
    }

    public Bytestream(String str) {
        this.mode = Mode.tcp;
        this.streamHosts = new ArrayList();
        setSessionID(str);
    }

    public void setSessionID(String str) {
        this.sessionID = str;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return this.mode;
    }

    public StreamHost addStreamHost(String str, String str2) {
        return addStreamHost(str, str2, 0);
    }

    public StreamHost addStreamHost(String str, String str2, int i) {
        StreamHost streamHost = new StreamHost(str, str2);
        streamHost.setPort(i);
        addStreamHost(streamHost);
        return streamHost;
    }

    public void addStreamHost(StreamHost streamHost) {
        this.streamHosts.add(streamHost);
    }

    public Collection<StreamHost> getStreamHosts() {
        return Collections.unmodifiableCollection(this.streamHosts);
    }

    public StreamHost getStreamHost(String str) {
        if (str == null) {
            return null;
        }
        for (StreamHost streamHost : this.streamHosts) {
            if (streamHost.getJID().equals(str)) {
                return streamHost;
            }
        }
        return null;
    }

    public int countStreamHosts() {
        return this.streamHosts.size();
    }

    public void setUsedHost(String str) {
        this.usedHost = new StreamHostUsed(str);
    }

    public StreamHostUsed getUsedHost() {
        return this.usedHost;
    }

    public Activate getToActivate() {
        return this.toActivate;
    }

    public void setToActivate(String str) {
        this.toActivate = new Activate(str);
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<query xmlns=\"http://jabber.org/protocol/bytestreams\"");
        if (getType().equals(Type.SET)) {
            if (getSessionID() != null) {
                stringBuilder.append(" sid=\"").append(getSessionID()).append("\"");
            }
            if (getMode() != null) {
                stringBuilder.append(" mode = \"").append(getMode()).append("\"");
            }
            stringBuilder.append(">");
            if (getToActivate() == null) {
                for (StreamHost toXML : getStreamHosts()) {
                    stringBuilder.append(toXML.toXML());
                }
            } else {
                stringBuilder.append(getToActivate().toXML());
            }
        } else if (getType().equals(Type.RESULT)) {
            stringBuilder.append(">");
            if (getUsedHost() != null) {
                stringBuilder.append(getUsedHost().toXML());
            } else if (countStreamHosts() > 0) {
                for (StreamHost toXML2 : this.streamHosts) {
                    stringBuilder.append(toXML2.toXML());
                }
            }
        } else if (getType().equals(Type.GET)) {
            return stringBuilder.append("/>").toString();
        } else {
            return null;
        }
        stringBuilder.append("</query>");
        return stringBuilder.toString();
    }
}
