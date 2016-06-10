package org.ksoap2.transport;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.http.entity.mime.MIME;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParserException;

public class HttpTransportSE extends Transport {
    private ServiceConnection serviceConnection;

    public HttpTransportSE(String url) {
        super(null, url);
    }

    public HttpTransportSE(Proxy proxy, String url) {
        super(proxy, url);
    }

    public HttpTransportSE(String url, int timeout) {
        super(url, timeout);
    }

    public HttpTransportSE(Proxy proxy, String url, int timeout) {
        super(proxy, url, timeout);
    }

    public HttpTransportSE(String url, int timeout, int contentLength) {
        super(url, timeout);
    }

    public HttpTransportSE(Proxy proxy, String url, int timeout, int contentLength) {
        super(proxy, url, timeout);
    }

    public void call(String soapAction, SoapEnvelope envelope) throws IOException, XmlPullParserException {
        call(soapAction, envelope, null);
    }

    public List call(String soapAction, SoapEnvelope envelope, List headers) throws IOException, XmlPullParserException {
        return call(soapAction, envelope, headers, null);
    }

    public List call(String soapAction, SoapEnvelope envelope, List headers, File outputFile) throws IOException, XmlPullParserException {
        String str;
        int i;
        InputStream is;
        if (soapAction == null) {
            soapAction = "\"\"";
        }
        byte[] requestData = createRequestData(envelope, AsyncHttpResponseHandler.DEFAULT_CHARSET);
        if (this.debug) {
            String str2 = new String(requestData);
        } else {
            str = null;
        }
        this.requestDump = str;
        this.responseDump = null;
        System.out.println("requestDump : " + new String(requestData));
        ServiceConnection connection = getServiceConnection();
        connection.setRequestProperty("User-Agent", "ksoap2-android/2.6.0+");
        int i2 = envelope.version;
        if (r0 != 120) {
            connection.setRequestProperty("SOAPAction", soapAction);
        }
        i2 = envelope.version;
        if (r0 == 120) {
            connection.setRequestProperty(MIME.CONTENT_TYPE, "application/soap+xml;charset=utf-8");
        } else {
            connection.setRequestProperty(MIME.CONTENT_TYPE, "text/xml;charset=utf-8");
        }
        connection.setRequestProperty("Connection", "close");
        connection.setRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestProperty("Content-Length", requestData.length);
        connection.setFixedLengthStreamingMode(requestData.length);
        if (headers != null) {
            for (i = 0; i < headers.size(); i++) {
                HeaderProperty hp = (HeaderProperty) headers.get(i);
                connection.setRequestProperty(hp.getKey(), hp.getValue());
            }
        }
        connection.setRequestMethod("POST");
        OutputStream os = connection.openOutputStream();
        os.write(requestData, 0, requestData.length);
        os.flush();
        os.close();
        requestData = null;
        List list = null;
        byte[] buf = null;
        int contentLength = Flags.FLAG2;
        boolean gZippedContent = false;
        list = connection.getResponseProperties();
        for (i = 0; i < list.size(); i++) {
            hp = (HeaderProperty) list.get(i);
            if (hp.getKey() != null) {
                if (hp.getKey().equalsIgnoreCase("content-length") && hp.getValue() != null) {
                    try {
                        contentLength = Integer.parseInt(hp.getValue());
                    } catch (NumberFormatException e) {
                        contentLength = Flags.FLAG2;
                    }
                }
                try {
                    if (hp.getKey().equalsIgnoreCase("Content-Encoding") && hp.getValue().equalsIgnoreCase("gzip")) {
                        gZippedContent = true;
                        break;
                    }
                } catch (IOException e2) {
                    if (null != null) {
                        is = getUnZippedInputStream(new BufferedInputStream(connection.getErrorStream(), contentLength));
                    } else {
                        is = new BufferedInputStream(connection.getErrorStream(), contentLength);
                    }
                    if (is == null) {
                        connection.disconnect();
                        throw e2;
                    }
                }
            }
        }
        if (gZippedContent) {
            is = getUnZippedInputStream(new BufferedInputStream(connection.openInputStream(), contentLength));
        } else {
            is = new BufferedInputStream(connection.openInputStream(), contentLength);
        }
        if (this.debug) {
            OutputStream bos;
            if (outputFile != null) {
                bos = new FileOutputStream(outputFile);
            } else {
                if (contentLength <= 0) {
                    contentLength = ServiceConnection.DEFAULT_BUFFER_SIZE;
                }
                bos = new ByteArrayOutputStream(contentLength);
            }
            buf = new byte[KEYRecord.OWNER_ZONE];
            while (true) {
                int rd = is.read(buf, 0, KEYRecord.OWNER_ZONE);
                if (rd == -1) {
                    break;
                }
                bos.write(buf, 0, rd);
            }
            bos.flush();
            if (bos instanceof ByteArrayOutputStream) {
                buf = ((ByteArrayOutputStream) bos).toByteArray();
            }
            this.responseDump = new String(buf);
            is.close();
            is = new ByteArrayInputStream(buf);
        }
        parseResponse(envelope, is);
        buf = null;
        return list;
    }

    private InputStream getUnZippedInputStream(InputStream inputStream) throws IOException {
        try {
            return (GZIPInputStream) inputStream;
        } catch (ClassCastException e) {
            return new GZIPInputStream(inputStream);
        }
    }

    public ServiceConnection getServiceConnection() throws IOException {
        if (this.serviceConnection == null) {
            this.serviceConnection = new ServiceConnectionSE(this.proxy, this.url, this.timeout);
        }
        return this.serviceConnection;
    }

    public String getHost() {
        String retVal = null;
        try {
            retVal = new URL(this.url).getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public int getPort() {
        int retVal = -1;
        try {
            retVal = new URL(this.url).getPort();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public String getPath() {
        String retVal = null;
        try {
            retVal = new URL(this.url).getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retVal;
    }
}
