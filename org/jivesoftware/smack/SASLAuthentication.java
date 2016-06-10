package org.jivesoftware.smack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.apache.qpid.management.common.sasl.Constants;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.Bind;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Session;
import org.jivesoftware.smack.sasl.SASLAnonymous;
import org.jivesoftware.smack.sasl.SASLCramMD5Mechanism;
import org.jivesoftware.smack.sasl.SASLDigestMD5Mechanism;
import org.jivesoftware.smack.sasl.SASLExternalMechanism;
import org.jivesoftware.smack.sasl.SASLGSSAPIMechanism;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.sasl.SASLPlainMechanism;
import org.xmlpull.v1.XmlPullParser;

public class SASLAuthentication implements UserAuthentication {
    private static Map<String, Class<? extends SASLMechanism>> implementedMechanisms;
    private static List<String> mechanismsPreferences;
    private Connection connection;
    private SASLMechanism currentMechanism;
    private String errorCondition;
    private boolean resourceBinded;
    private boolean saslFailed;
    private boolean saslNegotiated;
    private Collection<String> serverMechanisms;
    private boolean sessionSupported;

    static {
        implementedMechanisms = new HashMap();
        mechanismsPreferences = new ArrayList();
        registerSASLMechanism("EXTERNAL", SASLExternalMechanism.class);
        registerSASLMechanism("GSSAPI", SASLGSSAPIMechanism.class);
        registerSASLMechanism("DIGEST-MD5", SASLDigestMD5Mechanism.class);
        registerSASLMechanism(Constants.MECH_CRAMMD5, SASLCramMD5Mechanism.class);
        registerSASLMechanism(Constants.MECH_PLAIN, SASLPlainMechanism.class);
        registerSASLMechanism("ANONYMOUS", SASLAnonymous.class);
        supportSASLMechanism("DIGEST-MD5", 0);
        supportSASLMechanism(Constants.MECH_PLAIN, 1);
        supportSASLMechanism("ANONYMOUS", 2);
    }

    public static void registerSASLMechanism(String str, Class<? extends SASLMechanism> cls) {
        implementedMechanisms.put(str, cls);
    }

    public static void unregisterSASLMechanism(String str) {
        implementedMechanisms.remove(str);
        mechanismsPreferences.remove(str);
    }

    public static void supportSASLMechanism(String str) {
        mechanismsPreferences.add(0, str);
    }

    public static void supportSASLMechanism(String str, int i) {
        mechanismsPreferences.add(i, str);
    }

    public static void unsupportSASLMechanism(String str) {
        mechanismsPreferences.remove(str);
    }

    public static List<Class<? extends SASLMechanism>> getRegisterSASLMechanisms() {
        List<Class<? extends SASLMechanism>> arrayList = new ArrayList();
        for (String str : mechanismsPreferences) {
            arrayList.add(implementedMechanisms.get(str));
        }
        return arrayList;
    }

    SASLAuthentication(Connection connection) {
        this.serverMechanisms = new ArrayList();
        this.currentMechanism = null;
        this.connection = connection;
        init();
    }

    public boolean hasAnonymousAuthentication() {
        return this.serverMechanisms.contains("ANONYMOUS");
    }

    public boolean hasNonAnonymousAuthentication() {
        return (this.serverMechanisms.isEmpty() || (this.serverMechanisms.size() == 1 && hasAnonymousAuthentication())) ? false : true;
    }

    public String authenticate(String str, String str2, CallbackHandler callbackHandler) throws XMPPException {
        String str3 = null;
        for (String str4 : mechanismsPreferences) {
            if (implementedMechanisms.containsKey(str4) && this.serverMechanisms.contains(str4)) {
                str3 = str4;
                break;
            }
        }
        if (str3 != null) {
            try {
                this.currentMechanism = (SASLMechanism) ((Class) implementedMechanisms.get(str3)).getConstructor(new Class[]{SASLAuthentication.class}).newInstance(new Object[]{this});
                this.currentMechanism.authenticate(str, this.connection.getHost(), callbackHandler);
                synchronized (this) {
                    if (!(this.saslNegotiated || this.saslFailed)) {
                        try {
                            wait(30000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
                if (!this.saslFailed) {
                    if (this.saslNegotiated) {
                        return bindResourceAndEstablishSession(str2);
                    }
                    throw new XMPPException("SASL authentication failed");
                } else if (this.errorCondition != null) {
                    throw new XMPPException("SASL authentication " + str3 + " failed: " + this.errorCondition);
                } else {
                    throw new XMPPException("SASL authentication failed using mechanism " + str3);
                }
            } catch (XMPPException e2) {
                throw e2;
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else {
            throw new XMPPException("SASL Authentication failed. No known authentication mechanisims.");
        }
    }

    public String authenticate(String str, String str2, String str3) throws XMPPException {
        String str4 = null;
        for (String str5 : mechanismsPreferences) {
            if (implementedMechanisms.containsKey(str5) && this.serverMechanisms.contains(str5)) {
                str4 = str5;
                break;
            }
        }
        if (str4 == null) {
            return new NonSASLAuthentication(this.connection).authenticate(str, str2, str3);
        }
        try {
            this.currentMechanism = (SASLMechanism) ((Class) implementedMechanisms.get(str4)).getConstructor(new Class[]{SASLAuthentication.class}).newInstance(new Object[]{this});
            this.currentMechanism.authenticate(str, this.connection.getServiceName(), str2);
            synchronized (this) {
                if (!(this.saslNegotiated || this.saslFailed)) {
                    try {
                        wait(30000);
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (this.saslFailed) {
                if (this.errorCondition != null) {
                    throw new XMPPException("SASL authentication " + str4 + " failed: " + this.errorCondition);
                }
                throw new XMPPException("SASL authentication failed using mechanism " + str4);
            } else if (this.saslNegotiated) {
                return bindResourceAndEstablishSession(str3);
            } else {
                return new NonSASLAuthentication(this.connection).authenticate(str, str2, str3);
            }
        } catch (XMPPException e2) {
            throw e2;
        } catch (Exception e3) {
            e3.printStackTrace();
            return new NonSASLAuthentication(this.connection).authenticate(str, str2, str3);
        }
    }

    public String authenticateAnonymously() throws XMPPException {
        try {
            this.currentMechanism = new SASLAnonymous(this);
            this.currentMechanism.authenticate(null, null, XmlPullParser.NO_NAMESPACE);
            synchronized (this) {
                if (!(this.saslNegotiated || this.saslFailed)) {
                    try {
                        wait(5000);
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (this.saslFailed) {
                if (this.errorCondition != null) {
                    throw new XMPPException("SASL authentication failed: " + this.errorCondition);
                }
                throw new XMPPException("SASL authentication failed");
            } else if (this.saslNegotiated) {
                return bindResourceAndEstablishSession(null);
            } else {
                return new NonSASLAuthentication(this.connection).authenticateAnonymously();
            }
        } catch (IOException e2) {
            return new NonSASLAuthentication(this.connection).authenticateAnonymously();
        }
    }

    private String bindResourceAndEstablishSession(String str) throws XMPPException {
        synchronized (this) {
            if (!this.resourceBinded) {
                try {
                    wait(30000);
                } catch (InterruptedException e) {
                }
            }
        }
        if (this.resourceBinded) {
            Packet bind = new Bind();
            bind.setResource(str);
            PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(bind.getPacketID()));
            this.connection.sendPacket(bind);
            Bind bind2 = (Bind) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
            createPacketCollector.cancel();
            if (bind2 == null) {
                throw new XMPPException("No response from the server.");
            } else if (bind2.getType() == Type.ERROR) {
                throw new XMPPException(bind2.getError());
            } else {
                String jid = bind2.getJid();
                if (this.sessionSupported) {
                    bind = new Session();
                    PacketCollector createPacketCollector2 = this.connection.createPacketCollector(new PacketIDFilter(bind.getPacketID()));
                    this.connection.sendPacket(bind);
                    IQ iq = (IQ) createPacketCollector2.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
                    createPacketCollector2.cancel();
                    if (iq == null) {
                        throw new XMPPException("No response from the server.");
                    } else if (iq.getType() != Type.ERROR) {
                        return jid;
                    } else {
                        throw new XMPPException(iq.getError());
                    }
                }
                throw new XMPPException("Session establishment not offered by server");
            }
        }
        throw new XMPPException("Resource binding not offered by server");
    }

    void setAvailableSASLMethods(Collection<String> collection) {
        this.serverMechanisms = collection;
    }

    public boolean isAuthenticated() {
        return this.saslNegotiated;
    }

    void challengeReceived(String str) throws IOException {
        this.currentMechanism.challengeReceived(str);
    }

    void authenticated() {
        synchronized (this) {
            this.saslNegotiated = true;
            notify();
        }
    }

    void authenticationFailed() {
        authenticationFailed(null);
    }

    void authenticationFailed(String str) {
        synchronized (this) {
            this.saslFailed = true;
            this.errorCondition = str;
            notify();
        }
    }

    void bindingRequired() {
        synchronized (this) {
            this.resourceBinded = true;
            notify();
        }
    }

    public void send(Packet packet) {
        this.connection.sendPacket(packet);
    }

    void sessionsSupported() {
        this.sessionSupported = true;
    }

    protected void init() {
        this.saslNegotiated = false;
        this.saslFailed = false;
        this.resourceBinded = false;
        this.sessionSupported = false;
    }
}
