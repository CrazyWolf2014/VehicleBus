package cn.sharesdk.framework.p000a;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/* renamed from: cn.sharesdk.framework.a.j */
class C0027j implements X509TrustManager {
    final /* synthetic */ C0026i f16a;

    C0027j(C0026i c0026i) {
        this.f16a = c0026i;
    }

    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
    }

    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
