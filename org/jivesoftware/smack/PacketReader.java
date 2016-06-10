package org.jivesoftware.smack;

import com.cnlaunch.framework.common.Constants;
import com.ifoer.entity.Constant;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.sasl.SASLMechanism.Challenge;
import org.jivesoftware.smack.sasl.SASLMechanism.Success;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.entitycaps.EntityCapsManager;
import org.jivesoftware.smackx.workgroup.packet.SessionID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

class PacketReader {
    private XMPPConnection connection;
    private String connectionID;
    private boolean done;
    private ExecutorService listenerExecutor;
    private XmlPullParser parser;
    private Thread readerThread;

    /* renamed from: org.jivesoftware.smack.PacketReader.1 */
    class C09561 extends Thread {
        C09561() {
        }

        public void run() {
            PacketReader.this.parsePackets(this);
        }
    }

    /* renamed from: org.jivesoftware.smack.PacketReader.2 */
    class C09572 implements ThreadFactory {
        C09572() {
        }

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "Smack Listener Processor (" + PacketReader.this.connection.connectionCounterValue + ")");
            thread.setDaemon(true);
            return thread;
        }
    }

    private class ListenerNotification implements Runnable {
        private Packet packet;

        public ListenerNotification(Packet packet) {
            this.packet = packet;
        }

        public void run() {
            for (ListenerWrapper notifyListener : PacketReader.this.connection.recvListeners.values()) {
                notifyListener.notifyListener(this.packet);
            }
        }
    }

    protected PacketReader(XMPPConnection xMPPConnection) {
        this.connectionID = null;
        this.connection = xMPPConnection;
        init();
    }

    protected void init() {
        this.done = false;
        this.connectionID = null;
        this.readerThread = new C09561();
        this.readerThread.setName("Smack Packet Reader (" + this.connection.connectionCounterValue + ")");
        this.readerThread.setDaemon(true);
        this.listenerExecutor = Executors.newSingleThreadExecutor(new C09572());
        resetParser();
    }

    public synchronized void startup() throws XMPPException {
        this.readerThread.start();
        try {
            wait((long) (SmackConfiguration.getPacketReplyTimeout() * 3));
        } catch (InterruptedException e) {
        }
        if (this.connectionID == null) {
            throw new XMPPException("Connection failed. No response from server.");
        }
        this.connection.connectionID = this.connectionID;
    }

    public void shutdown() {
        if (!this.done) {
            for (ConnectionListener connectionClosed : this.connection.getConnectionListeners()) {
                try {
                    connectionClosed.connectionClosed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.done = true;
        this.listenerExecutor.shutdown();
    }

    void cleanup() {
        this.connection.recvListeners.clear();
        this.connection.collectors.clear();
    }

    void notifyConnectionError(Exception exception) {
        this.done = true;
        this.connection.shutdown(new Presence(Type.unavailable));
        exception.printStackTrace();
        for (ConnectionListener connectionClosedOnError : this.connection.getConnectionListeners()) {
            try {
                connectionClosedOnError.connectionClosedOnError(exception);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void notifyReconnection() {
        for (ConnectionListener reconnectionSuccessful : this.connection.getConnectionListeners()) {
            try {
                reconnectionSuccessful.reconnectionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resetParser() {
        try {
            this.parser = XmlPullParserFactory.newInstance().newPullParser();
            this.parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            this.parser.setInput(this.connection.reader);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void parsePackets(Thread thread) {
        try {
            int eventType = this.parser.getEventType();
            do {
                if (eventType == 2) {
                    if (this.parser.getName().equals(BundleBuilder.AskFromMessage)) {
                        processPacket(PacketParserUtils.parseMessage(this.parser));
                    } else if (this.parser.getName().equals("iq")) {
                        processPacket(PacketParserUtils.parseIQ(this.parser, this.connection));
                    } else if (this.parser.getName().equals("presence")) {
                        processPacket(PacketParserUtils.parsePresence(this.parser));
                    } else if (this.parser.getName().equals("stream")) {
                        if ("jabber:client".equals(this.parser.getNamespace(null))) {
                            for (eventType = 0; eventType < this.parser.getAttributeCount(); eventType++) {
                                if (this.parser.getAttributeName(eventType).equals(LocaleUtil.INDONESIAN)) {
                                    this.connectionID = this.parser.getAttributeValue(eventType);
                                    if (!Constant.APP_VERSION.equals(this.parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, AlixDefine.VERSION))) {
                                        releaseConnectionIDLock();
                                    }
                                } else if (this.parser.getAttributeName(eventType).equals(PrivacyRule.SUBSCRIPTION_FROM)) {
                                    this.connection.config.setServiceName(this.parser.getAttributeValue(eventType));
                                }
                            }
                        }
                    } else if (this.parser.getName().equals("error")) {
                        throw new XMPPException(PacketParserUtils.parseStreamError(this.parser));
                    } else if (this.parser.getName().equals("features")) {
                        parseFeatures(this.parser);
                    } else if (this.parser.getName().equals("proceed")) {
                        this.connection.proceedTLSReceived();
                        resetParser();
                    } else if (this.parser.getName().equals("failure")) {
                        r0 = this.parser.getNamespace(null);
                        if ("urn:ietf:params:xml:ns:xmpp-tls".equals(r0)) {
                            throw new Exception("TLS negotiation has failed");
                        } else if ("http://jabber.org/protocol/compress".equals(r0)) {
                            this.connection.streamCompressionDenied();
                        } else {
                            processPacket(PacketParserUtils.parseSASLFailure(this.parser));
                            this.connection.getSASLAuthentication().authenticationFailed();
                        }
                    } else if (this.parser.getName().equals("challenge")) {
                        r0 = this.parser.nextText();
                        processPacket(new Challenge(r0));
                        this.connection.getSASLAuthentication().challengeReceived(r0);
                    } else if (this.parser.getName().equals("success")) {
                        processPacket(new Success(this.parser.nextText()));
                        this.connection.packetWriter.openStream();
                        resetParser();
                        this.connection.getSASLAuthentication().authenticated();
                    } else if (this.parser.getName().equals("compressed")) {
                        this.connection.startStreamCompression();
                        resetParser();
                    }
                } else if (eventType == 3 && this.parser.getName().equals("stream")) {
                    this.connection.disconnect();
                }
                eventType = this.parser.next();
                if (this.done || eventType == 1) {
                    return;
                }
            } while (thread == this.readerThread);
        } catch (Exception e) {
            if (!this.done && !this.connection.isSocketClosed()) {
                notifyConnectionError(e);
            }
        }
    }

    private synchronized void releaseConnectionIDLock() {
        notify();
    }

    private void processPacket(Packet packet) {
        if (packet != null) {
            for (PacketCollector processPacket : this.connection.getPacketCollectors()) {
                processPacket.processPacket(packet);
            }
            this.listenerExecutor.submit(new ListenerNotification(packet));
        }
    }

    private void parseFeatures(XmlPullParser xmlPullParser) throws Exception {
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("starttls")) {
                    z3 = true;
                } else if (xmlPullParser.getName().equals("mechanisms")) {
                    this.connection.getSASLAuthentication().setAvailableSASLMethods(PacketParserUtils.parseMechanisms(xmlPullParser));
                } else if (xmlPullParser.getName().equals("bind")) {
                    this.connection.getSASLAuthentication().bindingRequired();
                } else if (xmlPullParser.getName().equals(Constants.VER)) {
                    this.connection.getConfiguration().setRosterVersioningAvailable(true);
                } else if (xmlPullParser.getName().equals(EntityCapsManager.ELEMENT)) {
                    String attributeValue = xmlPullParser.getAttributeValue(null, "node");
                    String attributeValue2 = xmlPullParser.getAttributeValue(null, Constants.VER);
                    if (!(attributeValue2 == null || attributeValue == null)) {
                        this.connection.setServiceCapsNode(attributeValue + "#" + attributeValue2);
                    }
                } else if (xmlPullParser.getName().equals(SessionID.ELEMENT_NAME)) {
                    this.connection.getSASLAuthentication().sessionsSupported();
                } else if (xmlPullParser.getName().equals("compression")) {
                    this.connection.setAvailableCompressionMethods(PacketParserUtils.parseCompressionMethods(xmlPullParser));
                } else if (xmlPullParser.getName().equals("register")) {
                    this.connection.getAccountManager().setSupportsAccountCreation(true);
                }
            } else if (next == 3) {
                if (xmlPullParser.getName().equals("starttls")) {
                    this.connection.startTLSReceived(z2);
                } else if (xmlPullParser.getName().equals("required") && z3) {
                    z2 = true;
                } else if (xmlPullParser.getName().equals("features")) {
                    z = true;
                }
            }
        }
        if (!this.connection.isSecureConnection() && !z3 && this.connection.getConfiguration().getSecurityMode() == SecurityMode.required) {
            throw new XMPPException("Server does not support security (TLS), but security required by connection configuration.", new XMPPError(Condition.forbidden));
        } else if (!z3 || this.connection.getConfiguration().getSecurityMode() == SecurityMode.disabled) {
            releaseConnectionIDLock();
        }
    }
}
