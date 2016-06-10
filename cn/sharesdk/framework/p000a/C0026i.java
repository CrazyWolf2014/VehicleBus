package cn.sharesdk.framework.p000a;

import java.net.Socket;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

/* renamed from: cn.sharesdk.framework.a.i */
public class C0026i extends SSLSocketFactory {
    SSLContext f15a;

    public C0026i(KeyStore keyStore) {
        super(keyStore);
        this.f15a = SSLContext.getInstance("TLS");
        C0027j c0027j = new C0027j(this);
        this.f15a.init(null, new TrustManager[]{c0027j}, null);
    }

    public Socket createSocket() {
        return this.f15a.getSocketFactory().createSocket();
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) {
        return this.f15a.getSocketFactory().createSocket(socket, str, i, z);
    }
}
