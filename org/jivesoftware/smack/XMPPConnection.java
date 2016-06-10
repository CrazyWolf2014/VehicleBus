package org.jivesoftware.smack;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnmobi.im.util.XmppConnection;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Collection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import org.apache.harmony.javax.security.auth.callback.Callback;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.apache.harmony.javax.security.auth.callback.PasswordCallback;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;

public class XMPPConnection extends Connection {
    private boolean anonymous;
    private boolean authenticated;
    private Collection<String> compressionMethods;
    private boolean connected;
    String connectionID;
    PacketReader packetReader;
    PacketWriter packetWriter;
    Roster roster;
    Socket socket;
    private volatile boolean socketClosed;
    private String user;
    private boolean usingCompression;
    private boolean usingTLS;
    private boolean wasAuthenticated;

    public XMPPConnection(String str, CallbackHandler callbackHandler) {
        super(new ConnectionConfiguration(str));
        this.connectionID = null;
        this.user = null;
        this.connected = false;
        this.socketClosed = false;
        this.authenticated = false;
        this.wasAuthenticated = false;
        this.anonymous = false;
        this.usingTLS = false;
        this.roster = null;
        this.config.setCompressionEnabled(false);
        this.config.setSASLAuthenticationEnabled(true);
        this.config.setDebuggerEnabled(DEBUG_ENABLED);
        this.config.setCallbackHandler(callbackHandler);
    }

    public XMPPConnection(String str) {
        super(new ConnectionConfiguration(str));
        this.connectionID = null;
        this.user = null;
        this.connected = false;
        this.socketClosed = false;
        this.authenticated = false;
        this.wasAuthenticated = false;
        this.anonymous = false;
        this.usingTLS = false;
        this.roster = null;
        this.config.setCompressionEnabled(false);
        this.config.setSASLAuthenticationEnabled(true);
        this.config.setDebuggerEnabled(DEBUG_ENABLED);
    }

    public XMPPConnection(ConnectionConfiguration connectionConfiguration) {
        super(connectionConfiguration);
        this.connectionID = null;
        this.user = null;
        this.connected = false;
        this.socketClosed = false;
        this.authenticated = false;
        this.wasAuthenticated = false;
        this.anonymous = false;
        this.usingTLS = false;
        this.roster = null;
    }

    public XMPPConnection(ConnectionConfiguration connectionConfiguration, CallbackHandler callbackHandler) {
        super(connectionConfiguration);
        this.connectionID = null;
        this.user = null;
        this.connected = false;
        this.socketClosed = false;
        this.authenticated = false;
        this.wasAuthenticated = false;
        this.anonymous = false;
        this.usingTLS = false;
        this.roster = null;
        connectionConfiguration.setCallbackHandler(callbackHandler);
    }

    public String getConnectionID() {
        if (isConnected()) {
            return this.connectionID;
        }
        return null;
    }

    public String getUser() {
        if (isAuthenticated()) {
            return this.user;
        }
        return null;
    }

    public synchronized void login(String str, String str2, String str3) throws XMPPException {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to server.");
        } else if (this.authenticated) {
            throw new IllegalStateException("Already logged in to server.");
        } else {
            String authenticate;
            String trim = str.toLowerCase().trim();
            if (!this.config.isSASLAuthenticationEnabled() || !this.saslAuthentication.hasNonAnonymousAuthentication()) {
                authenticate = new NonSASLAuthentication(this).authenticate(trim, str2, str3);
            } else if (str2 != null) {
                authenticate = this.saslAuthentication.authenticate(trim, str2, str3);
            } else {
                authenticate = this.saslAuthentication.authenticate(trim, str3, this.config.getCallbackHandler());
            }
            if (authenticate != null) {
                this.user = authenticate;
                this.config.setServiceName(StringUtils.parseServer(authenticate));
            } else {
                this.user = trim + XmppConnection.JID_SEPARATOR + getServiceName();
                if (str3 != null) {
                    this.user += FilePathGenerator.ANDROID_DIR_SEP + str3;
                }
            }
            if (this.config.isCompressionEnabled()) {
                useCompression();
            }
            this.authenticated = true;
            this.anonymous = false;
            if (this.roster == null) {
                if (this.rosterStorage == null) {
                    this.roster = new Roster(this);
                } else {
                    this.roster = new Roster(this, this.rosterStorage);
                }
            }
            if (this.config.isRosterLoadedAtLogin()) {
                this.roster.reload();
            }
            if (this.config.isSendPresence()) {
                this.packetWriter.sendPacket(new Presence(Type.available));
            }
            this.config.setLoginInfo(trim, str2, str3);
            if (this.config.isDebuggerEnabled() && this.debugger != null) {
                this.debugger.userHasLogged(this.user);
            }
        }
    }

    public synchronized void loginAnonymously() throws XMPPException {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to server.");
        } else if (this.authenticated) {
            throw new IllegalStateException("Already logged in to server.");
        } else {
            String authenticateAnonymously;
            if (this.config.isSASLAuthenticationEnabled() && this.saslAuthentication.hasAnonymousAuthentication()) {
                authenticateAnonymously = this.saslAuthentication.authenticateAnonymously();
            } else {
                authenticateAnonymously = new NonSASLAuthentication(this).authenticateAnonymously();
            }
            this.user = authenticateAnonymously;
            this.config.setServiceName(StringUtils.parseServer(authenticateAnonymously));
            if (this.config.isCompressionEnabled()) {
                useCompression();
            }
            this.packetWriter.sendPacket(new Presence(Type.available));
            this.authenticated = true;
            this.anonymous = true;
            if (this.config.isDebuggerEnabled() && this.debugger != null) {
                this.debugger.userHasLogged(this.user);
            }
        }
    }

    public Roster getRoster() {
        synchronized (this) {
            if (!isAuthenticated() || isAnonymous()) {
                if (this.roster == null) {
                    this.roster = new Roster(this);
                }
                Roster roster = this.roster;
                return roster;
            }
            if (!this.config.isRosterLoadedAtLogin()) {
                this.roster.reload();
            }
            if (!this.roster.rosterInitialized) {
                try {
                    synchronized (this.roster) {
                        long packetReplyTimeout = (long) SmackConfiguration.getPacketReplyTimeout();
                        long currentTimeMillis = System.currentTimeMillis();
                        long j = packetReplyTimeout;
                        while (!this.roster.rosterInitialized && j > 0) {
                            this.roster.wait(j);
                            packetReplyTimeout = System.currentTimeMillis();
                            j -= packetReplyTimeout - currentTimeMillis;
                            currentTimeMillis = packetReplyTimeout;
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
            return this.roster;
        }
    }

    public boolean isConnected() {
        return this.connected;
    }

    public boolean isSecureConnection() {
        return isUsingTLS();
    }

    public boolean isSocketClosed() {
        return this.socketClosed;
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public boolean isAnonymous() {
        return this.anonymous;
    }

    protected void shutdown(Presence presence) {
        if (this.packetWriter != null) {
            this.packetWriter.sendPacket(presence);
        }
        setWasAuthenticated(this.authenticated);
        this.authenticated = false;
        if (this.packetReader != null) {
            this.packetReader.shutdown();
        }
        if (this.packetWriter != null) {
            this.packetWriter.shutdown();
        }
        try {
            Thread.sleep(150);
        } catch (Exception e) {
        }
        this.socketClosed = true;
        try {
            this.socket.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.connected = false;
        if (this.reader != null) {
            try {
                this.reader.close();
            } catch (Throwable th) {
            }
            this.reader = null;
        }
        if (this.writer != null) {
            try {
                this.writer.close();
            } catch (Throwable th2) {
            }
            this.writer = null;
        }
        try {
            this.socket.close();
        } catch (Exception e3) {
        }
        this.saslAuthentication.init();
    }

    public synchronized void disconnect(Presence presence) {
        PacketReader packetReader = this.packetReader;
        PacketWriter packetWriter = this.packetWriter;
        if (!(packetReader == null || packetWriter == null)) {
            shutdown(presence);
            if (this.roster != null) {
                this.roster.cleanup();
                this.roster = null;
            }
            this.chatManager = null;
            this.wasAuthenticated = false;
            packetWriter.cleanup();
            this.packetWriter = null;
            packetReader.cleanup();
            this.packetReader = null;
        }
    }

    public void sendPacket(Packet packet) {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to server.");
        } else if (packet == null) {
            throw new NullPointerException("Packet is null.");
        } else {
            this.packetWriter.sendPacket(packet);
        }
    }

    public void addPacketWriterInterceptor(PacketInterceptor packetInterceptor, PacketFilter packetFilter) {
        addPacketInterceptor(packetInterceptor, packetFilter);
    }

    public void removePacketWriterInterceptor(PacketInterceptor packetInterceptor) {
        removePacketInterceptor(packetInterceptor);
    }

    public void addPacketWriterListener(PacketListener packetListener, PacketFilter packetFilter) {
        addPacketSendingListener(packetListener, packetFilter);
    }

    public void removePacketWriterListener(PacketListener packetListener) {
        removePacketSendingListener(packetListener);
    }

    private void connectUsingConfiguration(ConnectionConfiguration connectionConfiguration) throws XMPPException {
        String host = connectionConfiguration.getHost();
        int port = connectionConfiguration.getPort();
        try {
            if (connectionConfiguration.getSocketFactory() == null) {
                this.socket = new Socket(host, port);
            } else {
                this.socket = connectionConfiguration.getSocketFactory().createSocket(host, port);
            }
            this.socketClosed = false;
            initConnection();
        } catch (Throwable e) {
            host = "Could not connect to " + host + ":" + port + ".";
            throw new XMPPException(host, new XMPPError(Condition.remote_server_timeout, host), e);
        } catch (Throwable e2) {
            host = "XMPPError connecting to " + host + ":" + port + ".";
            throw new XMPPException(host, new XMPPError(Condition.remote_server_error, host), e2);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initConnection() throws org.jivesoftware.smack.XMPPException {
        /*
        r5 = this;
        r0 = 1;
        r1 = 0;
        r4 = 0;
        r2 = r5.packetReader;
        if (r2 == 0) goto L_0x000b;
    L_0x0007:
        r2 = r5.packetWriter;
        if (r2 != 0) goto L_0x00b4;
    L_0x000b:
        r5.usingCompression = r1;
        r5.initReaderAndWriter();
        if (r0 == 0) goto L_0x00b7;
    L_0x0012:
        r2 = new org.jivesoftware.smack.PacketWriter;	 Catch:{ XMPPException -> 0x0070 }
        r2.<init>(r5);	 Catch:{ XMPPException -> 0x0070 }
        r5.packetWriter = r2;	 Catch:{ XMPPException -> 0x0070 }
        r2 = new org.jivesoftware.smack.PacketReader;	 Catch:{ XMPPException -> 0x0070 }
        r2.<init>(r5);	 Catch:{ XMPPException -> 0x0070 }
        r5.packetReader = r2;	 Catch:{ XMPPException -> 0x0070 }
        r2 = r5.config;	 Catch:{ XMPPException -> 0x0070 }
        r2 = r2.isDebuggerEnabled();	 Catch:{ XMPPException -> 0x0070 }
        if (r2 == 0) goto L_0x0044;
    L_0x0028:
        r2 = r5.debugger;	 Catch:{ XMPPException -> 0x0070 }
        r2 = r2.getReaderListener();	 Catch:{ XMPPException -> 0x0070 }
        r3 = 0;
        r5.addPacketListener(r2, r3);	 Catch:{ XMPPException -> 0x0070 }
        r2 = r5.debugger;	 Catch:{ XMPPException -> 0x0070 }
        r2 = r2.getWriterListener();	 Catch:{ XMPPException -> 0x0070 }
        if (r2 == 0) goto L_0x0044;
    L_0x003a:
        r2 = r5.debugger;	 Catch:{ XMPPException -> 0x0070 }
        r2 = r2.getWriterListener();	 Catch:{ XMPPException -> 0x0070 }
        r3 = 0;
        r5.addPacketSendingListener(r2, r3);	 Catch:{ XMPPException -> 0x0070 }
    L_0x0044:
        r2 = r5.packetWriter;	 Catch:{ XMPPException -> 0x0070 }
        r2.startup();	 Catch:{ XMPPException -> 0x0070 }
        r2 = r5.packetReader;	 Catch:{ XMPPException -> 0x0070 }
        r2.startup();	 Catch:{ XMPPException -> 0x0070 }
        r2 = 1;
        r5.connected = r2;	 Catch:{ XMPPException -> 0x0070 }
        r2 = r5.packetWriter;	 Catch:{ XMPPException -> 0x0070 }
        r2.startKeepAliveProcess();	 Catch:{ XMPPException -> 0x0070 }
        if (r0 == 0) goto L_0x00c2;
    L_0x0058:
        r0 = org.jivesoftware.smack.Connection.getConnectionCreationListeners();	 Catch:{ XMPPException -> 0x0070 }
        r2 = r0.iterator();	 Catch:{ XMPPException -> 0x0070 }
    L_0x0060:
        r0 = r2.hasNext();	 Catch:{ XMPPException -> 0x0070 }
        if (r0 == 0) goto L_0x00cb;
    L_0x0066:
        r0 = r2.next();	 Catch:{ XMPPException -> 0x0070 }
        r0 = (org.jivesoftware.smack.ConnectionCreationListener) r0;	 Catch:{ XMPPException -> 0x0070 }
        r0.connectionCreated(r5);	 Catch:{ XMPPException -> 0x0070 }
        goto L_0x0060;
    L_0x0070:
        r0 = move-exception;
        r2 = r5.packetWriter;
        if (r2 == 0) goto L_0x007c;
    L_0x0075:
        r2 = r5.packetWriter;	 Catch:{ Throwable -> 0x00d4 }
        r2.shutdown();	 Catch:{ Throwable -> 0x00d4 }
    L_0x007a:
        r5.packetWriter = r4;
    L_0x007c:
        r2 = r5.packetReader;
        if (r2 == 0) goto L_0x0087;
    L_0x0080:
        r2 = r5.packetReader;	 Catch:{ Throwable -> 0x00d2 }
        r2.shutdown();	 Catch:{ Throwable -> 0x00d2 }
    L_0x0085:
        r5.packetReader = r4;
    L_0x0087:
        r2 = r5.reader;
        if (r2 == 0) goto L_0x0092;
    L_0x008b:
        r2 = r5.reader;	 Catch:{ Throwable -> 0x00d0 }
        r2.close();	 Catch:{ Throwable -> 0x00d0 }
    L_0x0090:
        r5.reader = r4;
    L_0x0092:
        r2 = r5.writer;
        if (r2 == 0) goto L_0x009d;
    L_0x0096:
        r2 = r5.writer;	 Catch:{ Throwable -> 0x00ce }
        r2.close();	 Catch:{ Throwable -> 0x00ce }
    L_0x009b:
        r5.writer = r4;
    L_0x009d:
        r2 = r5.socket;
        if (r2 == 0) goto L_0x00a8;
    L_0x00a1:
        r2 = r5.socket;	 Catch:{ Exception -> 0x00cc }
        r2.close();	 Catch:{ Exception -> 0x00cc }
    L_0x00a6:
        r5.socket = r4;
    L_0x00a8:
        r2 = r5.authenticated;
        r5.setWasAuthenticated(r2);
        r5.chatManager = r4;
        r5.authenticated = r1;
        r5.connected = r1;
        throw r0;
    L_0x00b4:
        r0 = r1;
        goto L_0x000b;
    L_0x00b7:
        r2 = r5.packetWriter;	 Catch:{ XMPPException -> 0x0070 }
        r2.init();	 Catch:{ XMPPException -> 0x0070 }
        r2 = r5.packetReader;	 Catch:{ XMPPException -> 0x0070 }
        r2.init();	 Catch:{ XMPPException -> 0x0070 }
        goto L_0x0044;
    L_0x00c2:
        r0 = r5.wasAuthenticated;	 Catch:{ XMPPException -> 0x0070 }
        if (r0 != 0) goto L_0x00cb;
    L_0x00c6:
        r0 = r5.packetReader;	 Catch:{ XMPPException -> 0x0070 }
        r0.notifyReconnection();	 Catch:{ XMPPException -> 0x0070 }
    L_0x00cb:
        return;
    L_0x00cc:
        r2 = move-exception;
        goto L_0x00a6;
    L_0x00ce:
        r2 = move-exception;
        goto L_0x009b;
    L_0x00d0:
        r2 = move-exception;
        goto L_0x0090;
    L_0x00d2:
        r2 = move-exception;
        goto L_0x0085;
    L_0x00d4:
        r2 = move-exception;
        goto L_0x007a;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.XMPPConnection.initConnection():void");
    }

    private void initReaderAndWriter() throws XMPPException {
        try {
            if (this.usingCompression) {
                try {
                    Class cls = Class.forName("com.jcraft.jzlib.ZOutputStream");
                    Object newInstance = cls.getConstructor(new Class[]{OutputStream.class, Integer.TYPE}).newInstance(new Object[]{this.socket.getOutputStream(), Integer.valueOf(9)});
                    cls.getMethod("setFlushMode", new Class[]{Integer.TYPE}).invoke(newInstance, new Object[]{Integer.valueOf(2)});
                    this.writer = new BufferedWriter(new OutputStreamWriter((OutputStream) newInstance, AsyncHttpResponseHandler.DEFAULT_CHARSET));
                    cls = Class.forName("com.jcraft.jzlib.ZInputStream");
                    newInstance = cls.getConstructor(new Class[]{InputStream.class}).newInstance(new Object[]{this.socket.getInputStream()});
                    cls.getMethod("setFlushMode", new Class[]{Integer.TYPE}).invoke(newInstance, new Object[]{Integer.valueOf(2)});
                    this.reader = new BufferedReader(new InputStreamReader((InputStream) newInstance, AsyncHttpResponseHandler.DEFAULT_CHARSET));
                } catch (Exception e) {
                    e.printStackTrace();
                    this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), AsyncHttpResponseHandler.DEFAULT_CHARSET));
                    this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), AsyncHttpResponseHandler.DEFAULT_CHARSET));
                }
            } else {
                this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), AsyncHttpResponseHandler.DEFAULT_CHARSET));
                this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), AsyncHttpResponseHandler.DEFAULT_CHARSET));
            }
            initDebugger();
        } catch (Throwable e2) {
            throw new XMPPException("XMPPError establishing connection with server.", new XMPPError(Condition.remote_server_error, "XMPPError establishing connection with server."), e2);
        }
    }

    public boolean isUsingTLS() {
        return this.usingTLS;
    }

    void startTLSReceived(boolean z) {
        if (z && this.config.getSecurityMode() == SecurityMode.disabled) {
            this.packetReader.notifyConnectionError(new IllegalStateException("TLS required by server but not allowed by connection configuration"));
        } else if (this.config.getSecurityMode() != SecurityMode.disabled) {
            try {
                this.writer.write("<starttls xmlns=\"urn:ietf:params:xml:ns:xmpp-tls\"/>");
                this.writer.flush();
            } catch (Exception e) {
                this.packetReader.notifyConnectionError(e);
            }
        }
    }

    void proceedTLSReceived() throws Exception {
        SSLContext instance;
        KeyManager[] keyManagerArr = null;
        SSLContext customSSLContext = this.config.getCustomSSLContext();
        if (this.config.getCallbackHandler() != null && customSSLContext == null) {
            PasswordCallback passwordCallback;
            KeyStore keyStore;
            if (this.config.getKeystoreType().equals("NONE")) {
                passwordCallback = null;
                keyStore = null;
            } else if (this.config.getKeystoreType().equals("PKCS11")) {
                try {
                    Constructor constructor = Class.forName("sun.security.pkcs11.SunPKCS11").getConstructor(new Class[]{InputStream.class});
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(("name = SmartCard\nlibrary = " + this.config.getPKCS11Library()).getBytes());
                    Provider provider = (Provider) constructor.newInstance(new Object[]{byteArrayInputStream});
                    Security.addProvider(provider);
                    keyStore = KeyStore.getInstance("PKCS11", provider);
                    passwordCallback = new PasswordCallback("PKCS11 Password: ", false);
                    this.config.getCallbackHandler().handle(new Callback[]{passwordCallback});
                    keyStore.load(null, passwordCallback.getPassword());
                } catch (Exception e) {
                    passwordCallback = null;
                    keyStore = null;
                }
            } else if (this.config.getKeystoreType().equals("Apple")) {
                KeyStore instance2 = KeyStore.getInstance("KeychainStore", "Apple");
                instance2.load(null, null);
                keyStore = instance2;
                passwordCallback = null;
            } else {
                keyStore = KeyStore.getInstance(this.config.getKeystoreType());
                try {
                    passwordCallback = new PasswordCallback("Keystore Password: ", false);
                    this.config.getCallbackHandler().handle(new Callback[]{passwordCallback});
                    keyStore.load(new FileInputStream(this.config.getKeystorePath()), passwordCallback.getPassword());
                } catch (Exception e2) {
                    passwordCallback = null;
                    keyStore = null;
                }
            }
            KeyManagerFactory instance3 = KeyManagerFactory.getInstance("SunX509");
            if (passwordCallback == null) {
                try {
                    instance3.init(keyStore, null);
                } catch (NullPointerException e3) {
                }
            } else {
                instance3.init(keyStore, passwordCallback.getPassword());
                passwordCallback.clearPassword();
            }
            keyManagerArr = instance3.getKeyManagers();
        }
        if (customSSLContext == null) {
            instance = SSLContext.getInstance("TLS");
            instance.init(keyManagerArr, new TrustManager[]{new ServerTrustManager(getServiceName(), this.config)}, new SecureRandom());
        } else {
            instance = customSSLContext;
        }
        Socket socket = this.socket;
        this.socket = instance.getSocketFactory().createSocket(socket, socket.getInetAddress().getHostAddress(), socket.getPort(), true);
        this.socket.setSoTimeout(0);
        this.socket.setKeepAlive(true);
        initReaderAndWriter();
        ((SSLSocket) this.socket).startHandshake();
        this.usingTLS = true;
        this.packetWriter.setWriter(this.writer);
        this.packetWriter.openStream();
    }

    void setAvailableCompressionMethods(Collection<String> collection) {
        this.compressionMethods = collection;
    }

    private boolean hasAvailableCompressionMethod(String str) {
        return this.compressionMethods != null && this.compressionMethods.contains(str);
    }

    public boolean isUsingCompression() {
        return this.usingCompression;
    }

    private boolean useCompression() {
        if (this.authenticated) {
            throw new IllegalStateException("Compression should be negotiated before authentication.");
        }
        try {
            Class.forName("com.jcraft.jzlib.ZOutputStream");
            if (!hasAvailableCompressionMethod("zlib")) {
                return false;
            }
            requestStreamCompression();
            synchronized (this) {
                try {
                    wait((long) (SmackConfiguration.getPacketReplyTimeout() * 5));
                } catch (InterruptedException e) {
                }
            }
            return this.usingCompression;
        } catch (ClassNotFoundException e2) {
            throw new IllegalStateException("Cannot use compression. Add smackx.jar to the classpath");
        }
    }

    private void requestStreamCompression() {
        try {
            this.writer.write("<compress xmlns='http://jabber.org/protocol/compress'>");
            this.writer.write("<method>zlib</method></compress>");
            this.writer.flush();
        } catch (Exception e) {
            this.packetReader.notifyConnectionError(e);
        }
    }

    void startStreamCompression() throws Exception {
        this.usingCompression = true;
        initReaderAndWriter();
        this.packetWriter.setWriter(this.writer);
        this.packetWriter.openStream();
        synchronized (this) {
            notify();
        }
    }

    void streamCompressionDenied() {
        synchronized (this) {
            notify();
        }
    }

    public void connect() throws XMPPException {
        connectUsingConfiguration(this.config);
        if (this.connected && this.wasAuthenticated) {
            try {
                if (isAnonymous()) {
                    loginAnonymously();
                } else {
                    login(this.config.getUsername(), this.config.getPassword(), this.config.getResource());
                }
                this.packetReader.notifyReconnection();
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }
    }

    private void setWasAuthenticated(boolean z) {
        if (!this.wasAuthenticated) {
            this.wasAuthenticated = z;
        }
    }

    public void setRosterStorage(RosterStorage rosterStorage) throws IllegalStateException {
        if (this.roster != null) {
            throw new IllegalStateException("Roster is already initialized");
        }
        this.rosterStorage = rosterStorage;
    }
}
