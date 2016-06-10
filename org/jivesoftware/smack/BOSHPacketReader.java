package org.jivesoftware.smack;

import com.kenai.jbosh.AbstractBody;
import com.kenai.jbosh.BOSHClientResponseListener;
import com.kenai.jbosh.BOSHMessageEvent;
import com.kenai.jbosh.BodyQName;
import com.kenai.jbosh.ComposableBody;
import com.launch.service.BundleBuilder;
import java.io.StringReader;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.sasl.SASLMechanism.Challenge;
import org.jivesoftware.smack.sasl.SASLMechanism.Success;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.jivesoftware.smackx.workgroup.packet.SessionID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class BOSHPacketReader implements BOSHClientResponseListener {
    private BOSHConnection connection;

    public BOSHPacketReader(BOSHConnection bOSHConnection) {
        this.connection = bOSHConnection;
    }

    public void responseReceived(BOSHMessageEvent bOSHMessageEvent) {
        AbstractBody body = bOSHMessageEvent.getBody();
        if (body != null) {
            try {
                if (this.connection.sessionID == null) {
                    this.connection.sessionID = body.getAttribute(BodyQName.create(BOSHConnection.BOSH_URI, AlixDefine.SID));
                }
                if (this.connection.authID == null) {
                    this.connection.authID = body.getAttribute(BodyQName.create(BOSHConnection.BOSH_URI, "authid"));
                }
                XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
                newPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                newPullParser.setInput(new StringReader(body.toXML()));
                newPullParser.getEventType();
                int next;
                do {
                    next = newPullParser.next();
                    if (next == 2 && !newPullParser.getName().equals("body")) {
                        if (newPullParser.getName().equals(BundleBuilder.AskFromMessage)) {
                            this.connection.processPacket(PacketParserUtils.parseMessage(newPullParser));
                            continue;
                        } else if (newPullParser.getName().equals("iq")) {
                            this.connection.processPacket(PacketParserUtils.parseIQ(newPullParser, this.connection));
                            continue;
                        } else if (newPullParser.getName().equals("presence")) {
                            this.connection.processPacket(PacketParserUtils.parsePresence(newPullParser));
                            continue;
                        } else if (newPullParser.getName().equals("challenge")) {
                            String nextText = newPullParser.nextText();
                            this.connection.getSASLAuthentication().challengeReceived(nextText);
                            this.connection.processPacket(new Challenge(nextText));
                            continue;
                        } else if (newPullParser.getName().equals("success")) {
                            this.connection.send(ComposableBody.builder().setNamespaceDefinition("xmpp", BOSHConnection.XMPP_BOSH_NS).setAttribute(BodyQName.createWithPrefix(BOSHConnection.XMPP_BOSH_NS, "restart", "xmpp"), "true").setAttribute(BodyQName.create(BOSHConnection.BOSH_URI, MultipleAddresses.TO), this.connection.getServiceName()).build());
                            this.connection.getSASLAuthentication().authenticated();
                            this.connection.processPacket(new Success(newPullParser.nextText()));
                            continue;
                        } else if (newPullParser.getName().equals("features")) {
                            parseFeatures(newPullParser);
                            continue;
                        } else if (newPullParser.getName().equals("failure")) {
                            if ("urn:ietf:params:xml:ns:xmpp-sasl".equals(newPullParser.getNamespace(null))) {
                                Packet parseSASLFailure = PacketParserUtils.parseSASLFailure(newPullParser);
                                this.connection.getSASLAuthentication().authenticationFailed();
                                this.connection.processPacket(parseSASLFailure);
                                continue;
                            } else {
                                continue;
                            }
                        } else if (newPullParser.getName().equals("error")) {
                            throw new XMPPException(PacketParserUtils.parseStreamError(newPullParser));
                        }
                    }
                } while (next != 1);
            } catch (Exception e) {
                if (this.connection.isConnected()) {
                    this.connection.notifyConnectionError(e);
                }
            }
        }
    }

    private void parseFeatures(XmlPullParser xmlPullParser) throws Exception {
        boolean z = false;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("mechanisms")) {
                    this.connection.getSASLAuthentication().setAvailableSASLMethods(PacketParserUtils.parseMechanisms(xmlPullParser));
                } else if (xmlPullParser.getName().equals("bind")) {
                    this.connection.getSASLAuthentication().bindingRequired();
                } else if (xmlPullParser.getName().equals(SessionID.ELEMENT_NAME)) {
                    this.connection.getSASLAuthentication().sessionsSupported();
                } else if (xmlPullParser.getName().equals("register")) {
                    this.connection.getAccountManager().setSupportsAccountCreation(true);
                }
            } else if (next == 3 && xmlPullParser.getName().equals("features")) {
                z = true;
            }
        }
    }
}
