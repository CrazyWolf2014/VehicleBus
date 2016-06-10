package com.amap.mapapi.core;

import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import org.apache.http.entity.mime.MIME;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.amap.mapapi.core.f */
public class HttpTool {
    public static HttpURLConnection m499a(String str, Proxy proxy) throws AMapException {
        if (str == null) {
            throw new AMapException(AMapException.ERROR_INVALID_PARAMETER);
        }
        try {
            HttpURLConnection httpURLConnection;
            URL url = new URL(str);
            if (proxy == null || ConfigableConst.f345o) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
            }
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(XStream.PRIORITY_VERY_HIGH);
            httpURLConnection.setReadTimeout(BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == AsyncTaskManager.REQUEST_SUCCESS_CODE) {
                return httpURLConnection;
            }
            throw new AMapException(AMapException.ERROR_CONNECTION);
        } catch (UnknownHostException e) {
            throw new AMapException(AMapException.ERROR_UNKNOW_HOST);
        } catch (MalformedURLException e2) {
            throw new AMapException(AMapException.ERROR_URL);
        } catch (ProtocolException e3) {
            throw new AMapException(AMapException.ERROR_PROTOCOL);
        } catch (SocketTimeoutException e4) {
            throw new AMapException(AMapException.ERROR_SOCKE_TIME_OUT);
        } catch (IOException e5) {
            throw new AMapException(AMapException.ERROR_IO);
        }
    }

    public static HttpURLConnection m500a(String str, byte[] bArr, Proxy proxy) throws AMapException {
        if (str == null) {
            throw new AMapException(AMapException.ERROR_INVALID_PARAMETER);
        }
        try {
            HttpURLConnection httpURLConnection;
            URL url = new URL(str);
            if (proxy == null || ConfigableConst.f345o) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
            }
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setConnectTimeout(XStream.PRIORITY_VERY_HIGH);
            httpURLConnection.setReadTimeout(BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty(MIME.CONTENT_TYPE, "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(bArr.length));
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(bArr);
            outputStream.flush();
            outputStream.close();
            if (httpURLConnection.getResponseCode() == AsyncTaskManager.REQUEST_SUCCESS_CODE) {
                return httpURLConnection;
            }
            throw new AMapException(AMapException.ERROR_CONNECTION);
        } catch (UnknownHostException e) {
            throw new AMapException(AMapException.ERROR_UNKNOW_HOST);
        } catch (MalformedURLException e2) {
            throw new AMapException(AMapException.ERROR_URL);
        } catch (ProtocolException e3) {
            throw new AMapException(AMapException.ERROR_PROTOCOL);
        } catch (SocketTimeoutException e4) {
            throw new AMapException(AMapException.ERROR_SOCKE_TIME_OUT);
        } catch (IOException e5) {
            throw new AMapException(AMapException.ERROR_IO);
        }
    }

    public static byte[] m502a(InputStream inputStream) throws AMapException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bArr = new byte[Flags.FLAG5];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
                byteArrayOutputStream.flush();
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    throw new AMapException(AMapException.ERROR_IO);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    throw new AMapException(AMapException.ERROR_IO);
                }
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e3) {
            throw new AMapException(AMapException.ERROR_IO);
        } catch (Throwable th) {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e4) {
                    throw new AMapException(AMapException.ERROR_IO);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e5) {
                    throw new AMapException(AMapException.ERROR_IO);
                }
            }
        }
    }

    public static short m501a(byte[] bArr) {
        return (short) (((short) (((bArr[0] & KEYRecord.PROTOCOL_ANY) << 8) + 0)) + (bArr[1] & KEYRecord.PROTOCOL_ANY));
    }
}
