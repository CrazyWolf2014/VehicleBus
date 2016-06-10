package com.ifoer.webservice;

import com.cnlaunch.framework.common.Constants;
import com.ifoer.entity.Constant;
import com.ifoer.entity.DiagSoftDocResult;
import com.ifoer.md5.MD5;
import com.ifoer.util.MyAndroidHttpTransport;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

public class DocumentService {
    private static final String WEBSERVICE_NAMESPACE = "http://www.x431.com";
    private static final String WEBSERVICE_SOAPACION = "";
    private static final String WEBSERVICE_URL = "http://mycar.x431.com/services/";
    private String cc;
    private String serviceName;
    private int timeout;
    private String token;

    public DocumentService() {
        this.serviceName = "diagSoftService";
        this.timeout = XStream.PRIORITY_VERY_HIGH;
    }

    public String getCc() {
        return this.cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private Element[] createHead(String sign) {
        Element[] header = new Element[]{new Element().createElement(WEBSERVICE_NAMESPACE, Constant.AUTHENTICATE)};
        Element ccElt = new Element().createElement(WEBSERVICE_NAMESPACE, MultipleAddresses.CC);
        ccElt.addChild(4, this.cc);
        header[0].addChild(2, ccElt);
        Element signElt = new Element().createElement(WEBSERVICE_NAMESPACE, Constants.SIGN);
        signElt.addChild(4, sign);
        header[0].addChild(2, signElt);
        return header;
    }

    public DiagSoftDocResult getDiagSoftDoc(String versionDetailId, String defaultLanId) throws NullPointerException, SocketTimeoutException {
        DiagSoftDocResult res = new DiagSoftDocResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "getDiagSoftDoc");
            if (versionDetailId != null) {
                request.addProperty("versionDetailId", versionDetailId);
            }
            if (defaultLanId != null) {
                request.addProperty("defaultLanId", defaultLanId);
            }
            StringBuffer sbf = new StringBuffer();
            sbf.append(versionDetailId);
            sbf.append(defaultLanId);
            sbf.append(this.token);
            String sgin = MD5.getMD5Str(sbf.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(this.serviceName).toString(), this.timeout).call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (!(envelope == null || envelope.getResponse() == null)) {
                    System.out.println(envelope.getResponse().toString());
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    res.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    if (Integer.valueOf(soapObject.getProperty("code").toString()).intValue() == 0) {
                        res.setDocId(Integer.valueOf(soapObject.getProperty("docId").toString()).intValue());
                    }
                }
                return res;
            }
            for (Element element : headerIn) {
                res.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public DiagSoftDocResult getProductDoc(String pdtType, int docType, int lanId, int defaultLanId) throws NullPointerException, SocketTimeoutException {
        DiagSoftDocResult res = new DiagSoftDocResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "getProductDoc");
            request.addProperty("pdtType", pdtType);
            request.addProperty("docType", Integer.valueOf(docType));
            request.addProperty("lanId", Integer.valueOf(lanId));
            request.addProperty("defaultLanId", Integer.valueOf(defaultLanId));
            StringBuffer sbf = new StringBuffer();
            sbf.append(pdtType);
            sbf.append(docType);
            sbf.append(lanId);
            sbf.append(defaultLanId);
            sbf.append(this.token);
            String sgin = MD5.getMD5Str(sbf.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(this.serviceName).toString(), this.timeout).call(WEBSERVICE_SOAPACION, envelope);
            if (!(envelope == null || envelope.getResponse() == null)) {
                System.out.println(envelope.getResponse().toString());
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                res.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                if (Integer.valueOf(soapObject.getProperty("code").toString()).intValue() == 0) {
                    res.setDocId(Integer.valueOf(soapObject.getProperty("docId").toString()).intValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return res;
    }
}
