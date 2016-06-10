package com.ifoer.webservice;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.car.result.DownloadBinResult;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.SoftMaxVersion;
import com.ifoer.ui.MoreActivity;
import com.ifoer.util.MyAndroidHttpTransport;
import com.launch.service.BundleBuilder;
import com.thoughtworks.xstream.XStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParserException;

public class UpdateSoftware {
    private static boolean f1317D = false;
    private static final String WEBSERVICE_NAMESPACE = "http://www.x431.com";
    private static final String WEBSERVICE_SOAPACION = "";
    public static final String WEBSERVICE_URL = "http://mycar.x431.com/services/";
    private static SoftMaxVersion rs;
    public static int timeout;

    static {
        f1317D = true;
        timeout = XStream.PRIORITY_VERY_HIGH;
    }

    public SoftMaxVersion getMobileAppSoftMaxVersion(Context context, String versionNo, int pdtType, int lanId) {
        String methodName = "getMobileAppSoftMaxVersion";
        String serviceName = "publicSoftService";
        SoftMaxVersion rs = new SoftMaxVersion();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (versionNo != null) {
                request.addProperty("versionNo", versionNo);
            }
            if (pdtType != 0) {
                request.addProperty("pdtType", Integer.valueOf(pdtType));
            }
            if (lanId != 0) {
                request.addProperty("lanId", Integer.valueOf(lanId));
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = false;
            if (ht == null) {
                return null;
            }
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                if (MoreActivity.binProgressDialogs != null) {
                    MoreActivity.binProgressDialogs.dismiss();
                }
                if (Integer.valueOf(soapObject.getProperty("code").toString()).intValue() != 0) {
                    return rs;
                }
                SoapObject softMaxVersion = (SoapObject) soapObject.getProperty("SoftMaxVersion");
                int forceUpgrade = new Integer(softMaxVersion.getPropertyAsString("forceUpgrade")).intValue();
                int versionDetailId = new Integer(softMaxVersion.getPropertyAsString("versionDetailId")).intValue();
                String newVersionNo = softMaxVersion.getPropertyAsString("versionNo");
                rs.setForceUpgrade(forceUpgrade);
                rs.setVersionDetailId(versionDetailId);
                rs.setVersionNo(newVersionNo);
                return rs;
            }
            rs.setMessage(context.getString(C0136R.string.ERROR_SERVER));
            return rs;
        } catch (IOException e) {
            if (MoreActivity.binProgressDialogs != null) {
                MoreActivity.binProgressDialogs.dismiss();
            }
            rs.setMessage(context.getString(C0136R.string.io_exception));
            e.printStackTrace();
            return rs;
        } catch (XmlPullParserException e2) {
            if (MoreActivity.binProgressDialogs != null) {
                MoreActivity.binProgressDialogs.dismiss();
            }
            rs.setMessage(context.getString(C0136R.string.ERROR_NETWORK));
            e2.printStackTrace();
            return rs;
        } catch (Exception e3) {
            if (MoreActivity.binProgressDialogs != null) {
                MoreActivity.binProgressDialogs.dismiss();
            }
            rs.setMessage(context.getString(C0136R.string.error_server));
            e3.printStackTrace();
            return rs;
        }
    }

    public SoftMaxVersion getMaxVersionAppForZY(String versionNo, int pdtTypeId, int configId, int lanId) {
        String methodName = "getMaxVersionAppForZY";
        String serviceName = "publicSoftService";
        SoftMaxVersion rs = new SoftMaxVersion();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (versionNo != null) {
                request.addProperty("versionNo", versionNo);
            }
            if (pdtTypeId != 0) {
                request.addProperty("pdtTypeId", Integer.valueOf(pdtTypeId));
            }
            if (configId != 0) {
                request.addProperty("configId", Integer.valueOf(configId));
            }
            if (lanId != 0) {
                request.addProperty("lanId", Integer.valueOf(lanId));
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = false;
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                if (Integer.valueOf(soapObject.getProperty("code").toString()).intValue() == 0) {
                    SoapObject softMaxVersion = (SoapObject) soapObject.getProperty("SoftMaxVersion");
                    int forceUpgrade = new Integer(softMaxVersion.getPropertyAsString("forceUpgrade")).intValue();
                    int versionDetailId = new Integer(softMaxVersion.getPropertyAsString("versionDetailId")).intValue();
                    String newVersionNo = softMaxVersion.getPropertyAsString("versionNo");
                    rs.setForceUpgrade(forceUpgrade);
                    rs.setVersionDetailId(versionDetailId);
                    rs.setVersionNo(newVersionNo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return rs;
    }

    public DownloadBinResult getBinFileMaxVersion(Context context, String cc, String productSerialNo, String versionNo, String displayLan) throws NullPointerException, SocketTimeoutException {
        String methodName = "getBinFileMaxVersion";
        String serviceName = "publicSoftService";
        DownloadBinResult bin = new DownloadBinResult();
        SoftMaxVersion rs = new SoftMaxVersion();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (cc != null) {
                request.addProperty(MultipleAddresses.CC, cc);
            }
            if (productSerialNo != null) {
                request.addProperty("productSerialNo", productSerialNo);
            }
            if (versionNo != null) {
                request.addProperty("versionNo", versionNo);
            }
            if (displayLan != null) {
                request.addProperty("displayLan", displayLan);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = false;
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                bin.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                if (Integer.valueOf(soapObject.getProperty("code").toString()).intValue() == 0) {
                    SoapObject softMaxVersion = (SoapObject) soapObject.getProperty("SoftMaxVersion");
                    int forceUpgrade = new Integer(softMaxVersion.getPropertyAsString("forceUpgrade")).intValue();
                    int versionDetailId = new Integer(softMaxVersion.getPropertyAsString("versionDetailId")).intValue();
                    String newVersionNo = softMaxVersion.getPropertyAsString("versionNo");
                    rs.setForceUpgrade(forceUpgrade);
                    rs.setVersionDetailId(versionDetailId);
                    rs.setVersionNo(newVersionNo);
                    bin.setVersioninfo(rs);
                } else {
                    if (MoreActivity.binProgressDialogs != null) {
                        MoreActivity.binProgressDialogs.dismiss();
                    }
                    if (TextUtils.isEmpty(soapObject.getProperty(BundleBuilder.AskFromMessage).toString())) {
                        bin.setMessage(context.getString(C0136R.string.ERROR_SERVER));
                    } else {
                        bin.setMessage(soapObject.getProperty(BundleBuilder.AskFromMessage).toString());
                        Log.i("\u5f02\u5e38", "\u6709\u6d88\u606f");
                    }
                    bin.setVersioninfo(rs);
                }
                return bin;
            }
            Log.i("\u5f02\u5e38", "\u6267\u884c\u6b64\u5904");
            rs.setMessage(context.getString(C0136R.string.ERROR_SERVER));
            return bin;
        } catch (IOException e) {
            rs.setMessage(context.getString(C0136R.string.io_exception));
            bin.setVersioninfo(rs);
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            rs.setMessage(context.getString(C0136R.string.ERROR_NETWORK));
            bin.setVersioninfo(rs);
            e2.printStackTrace();
        }
    }

    public static void download(String id, String filename) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL("http://mycar.x431.com/mobile/softCenter/downloadPhoneSoftWs.action").openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", AsyncHttpResponseHandler.DEFAULT_CHARSET);
        con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
        con.setDoInput(true);
        con.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
        out.write("versionDetailId=" + id);
        out.flush();
        out.close();
        int code = con.getResponseCode();
        InputStream is = con.getInputStream();
        int size = con.getContentLength();
        byte[] bs = new byte[Flags.FLAG5];
        OutputStream os = new FileOutputStream(filename);
        while (true) {
            int len = is.read(bs);
            if (len == -1) {
                os.close();
                is.close();
                return;
            }
            os.write(bs, 0, len);
        }
    }
}
