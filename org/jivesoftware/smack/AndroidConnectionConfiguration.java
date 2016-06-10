package org.jivesoftware.smack;

import android.os.Build.VERSION;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.io.File;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.util.DNSUtil;
import org.jivesoftware.smack.util.DNSUtil.HostAddress;

public class AndroidConnectionConfiguration extends ConnectionConfiguration {
    private static final int DEFAULT_TIMEOUT = 1000;

    /* renamed from: org.jivesoftware.smack.AndroidConnectionConfiguration.1DnsSrvLookupRunnable */
    class AnonymousClass1DnsSrvLookupRunnable implements Runnable {
        volatile HostAddress address;
        String serviceName;

        public AnonymousClass1DnsSrvLookupRunnable(String str) {
            this.serviceName = str;
        }

        public void run() {
            this.address = DNSUtil.resolveXMPPDomain(this.serviceName);
        }

        public HostAddress getHostAddress() {
            return this.address;
        }
    }

    public AndroidConnectionConfiguration(String str) throws XMPPException {
        AndroidInit(str, DEFAULT_TIMEOUT);
    }

    public AndroidConnectionConfiguration(String str, int i) throws XMPPException {
        AndroidInit(str, i);
    }

    public AndroidConnectionConfiguration(String str, int i, String str2) {
        super(str, i, str2);
        AndroidInit();
    }

    private void AndroidInit() {
        if (VERSION.SDK_INT >= 14) {
            setTruststoreType("AndroidCAStore");
            setTruststorePassword(null);
            setTruststorePath(null);
            return;
        }
        setTruststoreType("BKS");
        String property = System.getProperty("javax.net.ssl.trustStore");
        if (property == null) {
            property = System.getProperty("java.home") + File.separator + "etc" + File.separator + "security" + File.separator + "cacerts.bks";
        }
        setTruststorePath(property);
    }

    private void AndroidInit(String str, int i) throws XMPPException {
        AndroidInit();
        Object anonymousClass1DnsSrvLookupRunnable = new AnonymousClass1DnsSrvLookupRunnable(str);
        Thread thread = new Thread(anonymousClass1DnsSrvLookupRunnable, "dns-srv-lookup");
        thread.start();
        try {
            thread.join((long) i);
            HostAddress hostAddress = anonymousClass1DnsSrvLookupRunnable.getHostAddress();
            if (hostAddress == null) {
                throw new XMPPException("DNS lookup failure");
            }
            init(hostAddress.getHost(), hostAddress.getPort(), str, ProxyInfo.forDefaultProxy());
        } catch (Throwable e) {
            throw new XMPPException("DNS lookup timeout after " + i + LocaleUtil.MALAY, e);
        }
    }
}
