package org.jivesoftware.smack;

import java.io.File;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.util.DNSUtil;
import org.jivesoftware.smack.util.DNSUtil.HostAddress;

public class ConnectionConfiguration implements Cloneable {
    private CallbackHandler callbackHandler;
    private String capsNode;
    private boolean compressionEnabled;
    private SSLContext customSSLContext;
    private boolean debuggerEnabled;
    private boolean enableEntityCaps;
    private boolean expiredCertificatesCheckEnabled;
    private String host;
    private boolean isRosterVersioningAvailable;
    private String keystorePath;
    private String keystoreType;
    private boolean notMatchingDomainCheckEnabled;
    private String password;
    private String pkcs11Library;
    private int port;
    protected ProxyInfo proxy;
    private boolean reconnectionAllowed;
    private String resource;
    private boolean rosterLoadedAtLogin;
    private boolean saslAuthenticationEnabled;
    private SecurityMode securityMode;
    private boolean selfSignedCertificateEnabled;
    private boolean sendPresence;
    private String serviceName;
    private SocketFactory socketFactory;
    private String truststorePassword;
    private String truststorePath;
    private String truststoreType;
    private String username;
    private boolean verifyChainEnabled;
    private boolean verifyRootCAEnabled;

    public enum SecurityMode {
        required,
        enabled,
        disabled
    }

    public ConnectionConfiguration(String str) {
        this.verifyChainEnabled = false;
        this.verifyRootCAEnabled = false;
        this.selfSignedCertificateEnabled = false;
        this.expiredCertificatesCheckEnabled = false;
        this.notMatchingDomainCheckEnabled = false;
        this.isRosterVersioningAvailable = false;
        this.enableEntityCaps = true;
        this.capsNode = null;
        this.compressionEnabled = false;
        this.saslAuthenticationEnabled = true;
        this.debuggerEnabled = Connection.DEBUG_ENABLED;
        this.reconnectionAllowed = true;
        this.sendPresence = true;
        this.rosterLoadedAtLogin = true;
        this.securityMode = SecurityMode.enabled;
        HostAddress resolveXMPPDomain = DNSUtil.resolveXMPPDomain(str);
        init(resolveXMPPDomain.getHost(), resolveXMPPDomain.getPort(), str, ProxyInfo.forDefaultProxy());
    }

    protected ConnectionConfiguration() {
        this.verifyChainEnabled = false;
        this.verifyRootCAEnabled = false;
        this.selfSignedCertificateEnabled = false;
        this.expiredCertificatesCheckEnabled = false;
        this.notMatchingDomainCheckEnabled = false;
        this.isRosterVersioningAvailable = false;
        this.enableEntityCaps = true;
        this.capsNode = null;
        this.compressionEnabled = false;
        this.saslAuthenticationEnabled = true;
        this.debuggerEnabled = Connection.DEBUG_ENABLED;
        this.reconnectionAllowed = true;
        this.sendPresence = true;
        this.rosterLoadedAtLogin = true;
        this.securityMode = SecurityMode.enabled;
    }

    public ConnectionConfiguration(String str, ProxyInfo proxyInfo) {
        this.verifyChainEnabled = false;
        this.verifyRootCAEnabled = false;
        this.selfSignedCertificateEnabled = false;
        this.expiredCertificatesCheckEnabled = false;
        this.notMatchingDomainCheckEnabled = false;
        this.isRosterVersioningAvailable = false;
        this.enableEntityCaps = true;
        this.capsNode = null;
        this.compressionEnabled = false;
        this.saslAuthenticationEnabled = true;
        this.debuggerEnabled = Connection.DEBUG_ENABLED;
        this.reconnectionAllowed = true;
        this.sendPresence = true;
        this.rosterLoadedAtLogin = true;
        this.securityMode = SecurityMode.enabled;
        HostAddress resolveXMPPDomain = DNSUtil.resolveXMPPDomain(str);
        init(resolveXMPPDomain.getHost(), resolveXMPPDomain.getPort(), str, proxyInfo);
    }

    public ConnectionConfiguration(String str, int i, String str2) {
        this.verifyChainEnabled = false;
        this.verifyRootCAEnabled = false;
        this.selfSignedCertificateEnabled = false;
        this.expiredCertificatesCheckEnabled = false;
        this.notMatchingDomainCheckEnabled = false;
        this.isRosterVersioningAvailable = false;
        this.enableEntityCaps = true;
        this.capsNode = null;
        this.compressionEnabled = false;
        this.saslAuthenticationEnabled = true;
        this.debuggerEnabled = Connection.DEBUG_ENABLED;
        this.reconnectionAllowed = true;
        this.sendPresence = true;
        this.rosterLoadedAtLogin = true;
        this.securityMode = SecurityMode.enabled;
        init(str, i, str2, ProxyInfo.forDefaultProxy());
    }

    public ConnectionConfiguration(String str, int i, String str2, ProxyInfo proxyInfo) {
        this.verifyChainEnabled = false;
        this.verifyRootCAEnabled = false;
        this.selfSignedCertificateEnabled = false;
        this.expiredCertificatesCheckEnabled = false;
        this.notMatchingDomainCheckEnabled = false;
        this.isRosterVersioningAvailable = false;
        this.enableEntityCaps = true;
        this.capsNode = null;
        this.compressionEnabled = false;
        this.saslAuthenticationEnabled = true;
        this.debuggerEnabled = Connection.DEBUG_ENABLED;
        this.reconnectionAllowed = true;
        this.sendPresence = true;
        this.rosterLoadedAtLogin = true;
        this.securityMode = SecurityMode.enabled;
        init(str, i, str2, proxyInfo);
    }

    public ConnectionConfiguration(String str, int i) {
        this.verifyChainEnabled = false;
        this.verifyRootCAEnabled = false;
        this.selfSignedCertificateEnabled = false;
        this.expiredCertificatesCheckEnabled = false;
        this.notMatchingDomainCheckEnabled = false;
        this.isRosterVersioningAvailable = false;
        this.enableEntityCaps = true;
        this.capsNode = null;
        this.compressionEnabled = false;
        this.saslAuthenticationEnabled = true;
        this.debuggerEnabled = Connection.DEBUG_ENABLED;
        this.reconnectionAllowed = true;
        this.sendPresence = true;
        this.rosterLoadedAtLogin = true;
        this.securityMode = SecurityMode.enabled;
        init(str, i, str, ProxyInfo.forDefaultProxy());
    }

    public ConnectionConfiguration(String str, int i, ProxyInfo proxyInfo) {
        this.verifyChainEnabled = false;
        this.verifyRootCAEnabled = false;
        this.selfSignedCertificateEnabled = false;
        this.expiredCertificatesCheckEnabled = false;
        this.notMatchingDomainCheckEnabled = false;
        this.isRosterVersioningAvailable = false;
        this.enableEntityCaps = true;
        this.capsNode = null;
        this.compressionEnabled = false;
        this.saslAuthenticationEnabled = true;
        this.debuggerEnabled = Connection.DEBUG_ENABLED;
        this.reconnectionAllowed = true;
        this.sendPresence = true;
        this.rosterLoadedAtLogin = true;
        this.securityMode = SecurityMode.enabled;
        init(str, i, str, proxyInfo);
    }

    protected void init(String str, int i, String str2, ProxyInfo proxyInfo) {
        this.host = str;
        this.port = i;
        this.serviceName = str2;
        this.proxy = proxyInfo;
        String property = System.getProperty("java.home");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(property).append(File.separator).append("lib");
        stringBuilder.append(File.separator).append("security");
        stringBuilder.append(File.separator).append("cacerts");
        this.truststorePath = stringBuilder.toString();
        this.truststoreType = "jks";
        this.truststorePassword = "changeit";
        this.keystorePath = System.getProperty("javax.net.ssl.keyStore");
        this.keystoreType = "jks";
        this.pkcs11Library = "pkcs11.config";
        this.socketFactory = proxyInfo.getSocketFactory();
    }

    public void setServiceName(String str) {
        this.serviceName = str;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public SecurityMode getSecurityMode() {
        return this.securityMode;
    }

    public void setSecurityMode(SecurityMode securityMode) {
        this.securityMode = securityMode;
    }

    public String getTruststorePath() {
        return this.truststorePath;
    }

    public void setTruststorePath(String str) {
        this.truststorePath = str;
    }

    public String getTruststoreType() {
        return this.truststoreType;
    }

    public void setTruststoreType(String str) {
        this.truststoreType = str;
    }

    public String getTruststorePassword() {
        return this.truststorePassword;
    }

    public void setTruststorePassword(String str) {
        this.truststorePassword = str;
    }

    public String getKeystorePath() {
        return this.keystorePath;
    }

    public void setKeystorePath(String str) {
        this.keystorePath = str;
    }

    public String getKeystoreType() {
        return this.keystoreType;
    }

    public void setKeystoreType(String str) {
        this.keystoreType = str;
    }

    public String getPKCS11Library() {
        return this.pkcs11Library;
    }

    public void setPKCS11Library(String str) {
        this.pkcs11Library = str;
    }

    public boolean isVerifyChainEnabled() {
        return this.verifyChainEnabled;
    }

    public void setVerifyChainEnabled(boolean z) {
        this.verifyChainEnabled = z;
    }

    public boolean isVerifyRootCAEnabled() {
        return this.verifyRootCAEnabled;
    }

    public void setVerifyRootCAEnabled(boolean z) {
        this.verifyRootCAEnabled = z;
    }

    public boolean isSelfSignedCertificateEnabled() {
        return this.selfSignedCertificateEnabled;
    }

    public void setSelfSignedCertificateEnabled(boolean z) {
        this.selfSignedCertificateEnabled = z;
    }

    public boolean isExpiredCertificatesCheckEnabled() {
        return this.expiredCertificatesCheckEnabled;
    }

    public void setExpiredCertificatesCheckEnabled(boolean z) {
        this.expiredCertificatesCheckEnabled = z;
    }

    public boolean isNotMatchingDomainCheckEnabled() {
        return this.notMatchingDomainCheckEnabled;
    }

    public void setNotMatchingDomainCheckEnabled(boolean z) {
        this.notMatchingDomainCheckEnabled = z;
    }

    public SSLContext getCustomSSLContext() {
        return this.customSSLContext;
    }

    public void setCustomSSLContext(SSLContext sSLContext) {
        this.customSSLContext = sSLContext;
    }

    public boolean isCompressionEnabled() {
        return this.compressionEnabled;
    }

    public void setCompressionEnabled(boolean z) {
        this.compressionEnabled = z;
    }

    public boolean isSASLAuthenticationEnabled() {
        return this.saslAuthenticationEnabled;
    }

    public void setSASLAuthenticationEnabled(boolean z) {
        this.saslAuthenticationEnabled = z;
    }

    public boolean isDebuggerEnabled() {
        return this.debuggerEnabled;
    }

    public void setDebuggerEnabled(boolean z) {
        this.debuggerEnabled = z;
    }

    public void setReconnectionAllowed(boolean z) {
        this.reconnectionAllowed = z;
    }

    public boolean isReconnectionAllowed() {
        return this.reconnectionAllowed;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public void setSendPresence(boolean z) {
        this.sendPresence = z;
    }

    public boolean isRosterLoadedAtLogin() {
        return this.rosterLoadedAtLogin;
    }

    public void setRosterLoadedAtLogin(boolean z) {
        this.rosterLoadedAtLogin = z;
    }

    public CallbackHandler getCallbackHandler() {
        return this.callbackHandler;
    }

    public void setCallbackHandler(CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    String getUsername() {
        return this.username;
    }

    String getPassword() {
        return this.password;
    }

    String getResource() {
        return this.resource;
    }

    boolean isRosterVersioningAvailable() {
        return this.isRosterVersioningAvailable;
    }

    void setRosterVersioningAvailable(boolean z) {
        this.isRosterVersioningAvailable = z;
    }

    boolean isSendPresence() {
        return this.sendPresence;
    }

    void setLoginInfo(String str, String str2, String str3) {
        this.username = str;
        this.password = str2;
        this.resource = str3;
    }
}
