package com.alipay.android.appDemo4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.ArrayList;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.xbill.DNS.KEYRecord.Flags;

public class NetworkManager {
    static final String TAG = "NetworkManager";
    private int connectTimeout;
    Context mContext;
    Proxy mProxy;
    private int readTimeout;

    /* renamed from: com.alipay.android.appDemo4.NetworkManager.1 */
    class C00841 implements HostnameVerifier {
        C00841() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public NetworkManager(Context context) {
        this.connectTimeout = SyncHttpTransportSE.DEFAULTTIMEOUT;
        this.readTimeout = SyncHttpTransportSE.DEFAULTTIMEOUT;
        this.mProxy = null;
        this.mContext = context;
        setDefaultHostnameVerifier();
    }

    private void detectProxy() {
        NetworkInfo ni = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (ni != null && ni.isAvailable() && ni.getType() == 0) {
            String proxyHost = android.net.Proxy.getDefaultHost();
            int port = android.net.Proxy.getDefaultPort();
            if (proxyHost != null) {
                this.mProxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, port));
            }
        }
    }

    private void setDefaultHostnameVerifier() {
        HttpsURLConnection.setDefaultHostnameVerifier(new C00841());
    }

    public String SendAndWaitResponse(String strReqData, String strUrl) {
        detectProxy();
        String strResponse = null;
        ArrayList<BasicNameValuePair> pairs = new ArrayList();
        pairs.add(new BasicNameValuePair("requestData", strReqData));
        HttpURLConnection httpConnect = null;
        try {
            UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "utf-8");
            URL url = new URL(strUrl);
            if (this.mProxy != null) {
                httpConnect = (HttpURLConnection) url.openConnection(this.mProxy);
            } else {
                httpConnect = (HttpURLConnection) url.openConnection();
            }
            httpConnect.setConnectTimeout(this.connectTimeout);
            httpConnect.setReadTimeout(this.readTimeout);
            httpConnect.setDoOutput(true);
            httpConnect.addRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            httpConnect.connect();
            OutputStream os = httpConnect.getOutputStream();
            p_entity.writeTo(os);
            os.flush();
            strResponse = BaseHelper.convertStreamToString(httpConnect.getInputStream());
            BaseHelper.log(TAG, "response " + strResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpConnect.disconnect();
        }
        return strResponse;
    }

    public boolean urlDownloadToFile(Context context, String strurl, String path) {
        detectProxy();
        try {
            HttpURLConnection conn;
            URL url = new URL(strurl);
            if (this.mProxy != null) {
                conn = (HttpURLConnection) url.openConnection(this.mProxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setConnectTimeout(this.connectTimeout);
            conn.setReadTimeout(this.readTimeout);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[Flags.FLAG5];
            while (true) {
                int i = is.read(temp);
                if (i <= 0) {
                    fos.close();
                    is.close();
                    return true;
                }
                fos.write(temp, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
