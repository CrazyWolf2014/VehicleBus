package com.ifoer.webservice;

import android.content.Context;
import com.car.result.AlipayRSATradeDTOResult;
import com.car.result.DiagSoftOrderResult;
import com.car.result.StateResult;
import com.car.result.WSResult;
import com.cnlaunch.framework.common.Constants;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.AlipayRSATradeDTO;
import com.ifoer.entity.CompletePay;
import com.ifoer.entity.Constant;
import com.ifoer.entity.DiagSoftOrder;
import com.ifoer.entity.DiagSoftOrderInfo;
import com.ifoer.entity.InterfaceDao;
import com.ifoer.entity.QueryAlipayRSATrade;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.md5.MD5;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.MyAndroidHttpTransport;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Locale;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

public class ProductService {
    private static final String WEBSERVICE_NAMESPACE = "http://www.x431.com";
    private static final String WEBSERVICE_SOAPACION = "";
    private static final String WEBSERVICE_URL = "http://mycar.x431.com/services/";
    public static int timeout;
    private String cc;
    private String token;

    static {
        timeout = XStream.PRIORITY_VERY_HIGH;
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

    public StateResult checkPadIIStatus(String serialNo) throws SocketTimeoutException {
        StateResult res = new StateResult();
        String serviceName = "productService";
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "checkPadIIStatus");
            if (serialNo != null) {
                request.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, serialNo);
            }
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(serialNo)).append(this.token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout).call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                res.setCode(Integer.valueOf(((SoapObject) envelope.getResponse()).getProperty("code").toString()).intValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return res;
    }

    public StateResult registerProductForPad(Context context, String serialNo, String venderCode, String password) throws NullPointerException, SocketTimeoutException {
        StateResult res = new StateResult();
        String serviceName = "productService.*";
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "registerProductForPad");
            if (serialNo != null) {
                request.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, serialNo);
            }
            if (venderCode != null) {
                request.addProperty(com.cnlaunch.x431pro.common.Constants.VENDERCODE_KEY, venderCode);
            }
            if (password != null) {
                request.addProperty("password", password);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(InterfaceDao.search(serviceName), timeout);
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(serialNo)).append(venderCode).append(password).append(this.token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                        res.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    } else {
                        res.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    }
                }
                return res;
            }
            for (Element element : headerIn) {
                res.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return res;
        } catch (IOException e) {
            res.setMessage(context.getResources().getString(C0136R.string.io_exception));
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            res.setMessage(context.getResources().getString(C0136R.string.ERROR_NETWORK));
            e2.printStackTrace();
        } catch (Exception e3) {
            res.setMessage(context.getResources().getString(C0136R.string.ERROR_SERVER));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.car.result.ProductDTOResult getRegisteredProductsForPad(java.lang.String r25) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r24 = this;
        r13 = new com.car.result.ProductDTOResult;
        r13.<init>();
        r11 = new java.util.ArrayList;
        r11.<init>();
        r9 = 0;
        r8 = "getRegisteredProductsForPad";
        r15 = "productService";
        r12 = new org.ksoap2.serialization.SoapObject;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = "http://www.x431.com";
        r0 = r20;
        r12.<init>(r0, r8);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        if (r25 == 0) goto L_0x0023;
    L_0x001a:
        r20 = "productType";
        r0 = r20;
        r1 = r25;
        r12.addProperty(r0, r1);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
    L_0x0023:
        r6 = new com.ifoer.util.MyAndroidHttpTransport;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r21 = "http://mycar.x431.com/services/";
        r20.<init>(r21);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r0 = r20;
        r20 = r0.append(r15);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = r20.toString();	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r21 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r0 = r20;
        r1 = r21;
        r6.<init>(r0, r1);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r21 = java.lang.String.valueOf(r25);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20.<init>(r21);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r0 = r24;
        r0 = r0.token;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r21 = r0;
        r20 = r20.append(r21);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r17 = r20.toString();	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r16 = com.ifoer.md5.MD5.getMD5Str(r17);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r4 = new org.ksoap2.serialization.SoapSerializationEnvelope;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r20;
        r4.<init>(r0);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r0 = r24;
        r1 = r16;
        r20 = r0.createHead(r1);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r0 = r20;
        r4.headerOut = r0;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r4.bodyOut = r12;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r4.setOutputSoapObject(r12);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = "";
        r0 = r20;
        r6.call(r0, r4);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r5 = r4.headerIn;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        if (r5 == 0) goto L_0x00ba;
    L_0x007f:
        r0 = r5.length;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = r0;
        if (r20 <= 0) goto L_0x00ba;
    L_0x0084:
        r0 = r5.length;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r22 = r0;
        r20 = 0;
        r21 = r20;
    L_0x008b:
        r0 = r21;
        r1 = r22;
        if (r0 < r1) goto L_0x0092;
    L_0x0091:
        return r13;
    L_0x0092:
        r3 = r5[r21];	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = 0;
        r0 = r20;
        r20 = r3.getChild(r0);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = (org.kxml2.kdom.Element) r20;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r23 = 0;
        r0 = r20;
        r1 = r23;
        r20 = r0.getChild(r1);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r14 = r20.toString();	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = java.lang.Integer.parseInt(r14);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r0 = r20;
        r13.setCode(r0);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = r21 + 1;
        r21 = r20;
        goto L_0x008b;
    L_0x00ba:
        r20 = r4.getResponse();	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        if (r20 == 0) goto L_0x0091;
    L_0x00c0:
        r18 = r4.getResponse();	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r18 = (org.ksoap2.serialization.SoapObject) r18;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = "code";
        r0 = r18;
        r1 = r20;
        r20 = r0.getProperty(r1);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = r20.toString();	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = java.lang.Integer.valueOf(r20);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = r20.intValue();	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        if (r20 != 0) goto L_0x0131;
    L_0x00de:
        r7 = 0;
        r10 = r9;
    L_0x00e0:
        r20 = r18.getPropertyCount();	 Catch:{ IOException -> 0x0157, XmlPullParserException -> 0x0154 }
        r0 = r20;
        if (r7 < r0) goto L_0x00ed;
    L_0x00e8:
        r13.setProductDTOs(r11);	 Catch:{ IOException -> 0x0157, XmlPullParserException -> 0x0154 }
        r9 = r10;
        goto L_0x0091;
    L_0x00ed:
        if (r7 != 0) goto L_0x010b;
    L_0x00ef:
        r0 = r18;
        r20 = r0.getProperty(r7);	 Catch:{ IOException -> 0x0157, XmlPullParserException -> 0x0154 }
        r20 = r20.toString();	 Catch:{ IOException -> 0x0157, XmlPullParserException -> 0x0154 }
        r20 = java.lang.Integer.valueOf(r20);	 Catch:{ IOException -> 0x0157, XmlPullParserException -> 0x0154 }
        r20 = r20.intValue();	 Catch:{ IOException -> 0x0157, XmlPullParserException -> 0x0154 }
        r0 = r20;
        r13.setCode(r0);	 Catch:{ IOException -> 0x0157, XmlPullParserException -> 0x0154 }
        r9 = r10;
    L_0x0107:
        r7 = r7 + 1;
        r10 = r9;
        goto L_0x00e0;
    L_0x010b:
        r9 = new com.ifoer.entity.ProductDTO;	 Catch:{ IOException -> 0x0157, XmlPullParserException -> 0x0154 }
        r9.<init>();	 Catch:{ IOException -> 0x0157, XmlPullParserException -> 0x0154 }
        r0 = r18;
        r19 = r0.getProperty(r7);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r19 = (org.ksoap2.serialization.SoapObject) r19;	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = "serialNo";
        r20 = r19.getProperty(r20);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = r20.toString();	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r0 = r20;
        r9.setSerialNo(r0);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r11.add(r9);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        goto L_0x0107;
    L_0x012b:
        r2 = move-exception;
    L_0x012c:
        r2.printStackTrace();
        goto L_0x0091;
    L_0x0131:
        r20 = "code";
        r0 = r18;
        r1 = r20;
        r20 = r0.getProperty(r1);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = r20.toString();	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = java.lang.Integer.valueOf(r20);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r20 = r20.intValue();	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        r0 = r20;
        r13.setCode(r0);	 Catch:{ IOException -> 0x012b, XmlPullParserException -> 0x014e }
        goto L_0x0091;
    L_0x014e:
        r2 = move-exception;
    L_0x014f:
        r2.printStackTrace();
        goto L_0x0091;
    L_0x0154:
        r2 = move-exception;
        r9 = r10;
        goto L_0x014f;
    L_0x0157:
        r2 = move-exception;
        r9 = r10;
        goto L_0x012c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.ProductService.getRegisteredProductsForPad(java.lang.String):com.car.result.ProductDTOResult");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.car.result.DiagSoftPriceResult getSoftPrice(java.lang.String r27, java.lang.Integer r28, java.lang.Integer r29) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r26 = this;
        r15 = new com.car.result.DiagSoftPriceResult;
        r15.<init>();
        r6 = new java.util.ArrayList;
        r6.<init>();
        r2 = 0;
        r13 = "getSoftPrice";
        r18 = "userOrderService";
        r14 = new org.ksoap2.serialization.SoapObject;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = "http://www.x431.com";
        r0 = r22;
        r14.<init>(r0, r13);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = android.text.TextUtils.isEmpty(r27);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        if (r22 != 0) goto L_0x0027;
    L_0x001e:
        r22 = "serialNo";
        r0 = r22;
        r1 = r27;
        r14.addProperty(r0, r1);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
    L_0x0027:
        if (r28 == 0) goto L_0x003a;
    L_0x0029:
        r22 = "softId";
        r23 = r28.intValue();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r23 = java.lang.Integer.valueOf(r23);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r22;
        r1 = r23;
        r14.addProperty(r0, r1);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
    L_0x003a:
        if (r29 == 0) goto L_0x004d;
    L_0x003c:
        r22 = "lanId";
        r23 = r29.intValue();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r23 = java.lang.Integer.valueOf(r23);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r22;
        r1 = r23;
        r14.addProperty(r0, r1);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
    L_0x004d:
        r17 = new java.lang.StringBuffer;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r17.<init>();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r17;
        r1 = r27;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r22;
        r1 = r28;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r22;
        r1 = r29;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r26;
        r0 = r0.token;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r23 = r0;
        r22.append(r23);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r20 = r17.toString();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r19 = com.ifoer.md5.MD5.getMD5Str(r20);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r9 = new org.ksoap2.serialization.SoapSerializationEnvelope;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r22;
        r9.<init>(r0);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r26;
        r1 = r19;
        r22 = r0.createHead(r1);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r22;
        r9.headerOut = r0;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r9.bodyOut = r14;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r9.setOutputSoapObject(r14);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r11 = new com.ifoer.util.MyAndroidHttpTransport;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r23 = "http://mycar.x431.com/services/";
        r22.<init>(r23);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r22;
        r1 = r18;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = r22.toString();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r23 = timeout;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r22;
        r1 = r23;
        r11.<init>(r0, r1);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = "";
        r0 = r22;
        r11.call(r0, r9);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r10 = r9.headerIn;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        if (r10 == 0) goto L_0x00f9;
    L_0x00be:
        r0 = r10.length;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = r0;
        if (r22 <= 0) goto L_0x00f9;
    L_0x00c3:
        r0 = r10.length;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r24 = r0;
        r22 = 0;
        r23 = r22;
    L_0x00ca:
        r0 = r23;
        r1 = r24;
        if (r0 < r1) goto L_0x00d1;
    L_0x00d0:
        return r15;
    L_0x00d1:
        r8 = r10[r23];	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = 0;
        r0 = r22;
        r22 = r8.getChild(r0);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = (org.kxml2.kdom.Element) r22;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r25 = 0;
        r0 = r22;
        r1 = r25;
        r22 = r0.getChild(r1);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r16 = r22.toString();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = java.lang.Integer.parseInt(r16);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r22;
        r15.setCode(r0);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = r23 + 1;
        r23 = r22;
        goto L_0x00ca;
    L_0x00f9:
        r22 = r9.getResponse();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        if (r22 == 0) goto L_0x00d0;
    L_0x00ff:
        r21 = r9.getResponse();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r21 = (org.ksoap2.serialization.SoapObject) r21;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = "code";
        r22 = r21.getPropertyAsString(r22);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        if (r22 != 0) goto L_0x017b;
    L_0x0115:
        r22 = "diagSoftPriceList";
        r4 = r21.getProperty(r22);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r4 = (org.ksoap2.serialization.SoapObject) r4;	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = "code";
        r22 = r21.getPropertyAsString(r22);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r22;
        r15.setCode(r0);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r12 = 0;
        r3 = r2;
    L_0x0132:
        r22 = r4.getPropertyCount();	 Catch:{ IOException -> 0x019f, XmlPullParserException -> 0x019c }
        r0 = r22;
        if (r12 < r0) goto L_0x013f;
    L_0x013a:
        r15.setDiagSoftPriceList(r6);	 Catch:{ IOException -> 0x019f, XmlPullParserException -> 0x019c }
        r2 = r3;
        goto L_0x00d0;
    L_0x013f:
        r5 = r4.getProperty(r12);	 Catch:{ IOException -> 0x019f, XmlPullParserException -> 0x019c }
        r5 = (org.ksoap2.serialization.SoapObject) r5;	 Catch:{ IOException -> 0x019f, XmlPullParserException -> 0x019c }
        r2 = new com.ifoer.entity.DiagSoftPrice;	 Catch:{ IOException -> 0x019f, XmlPullParserException -> 0x019c }
        r2.<init>();	 Catch:{ IOException -> 0x019f, XmlPullParserException -> 0x019c }
        r22 = "currencyId";
        r0 = r22;
        r22 = r5.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r22;
        r2.setCurrencyId(r0);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = "price";
        r0 = r22;
        r22 = r5.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = java.lang.Double.valueOf(r22);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = r22.doubleValue();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r22;
        r2.setPrice(r0);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r6.add(r2);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r12 = r12 + 1;
        r3 = r2;
        goto L_0x0132;
    L_0x017b:
        r22 = "code";
        r22 = r21.getPropertyAsString(r22);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        r0 = r22;
        r15.setCode(r0);	 Catch:{ IOException -> 0x0190, XmlPullParserException -> 0x0196 }
        goto L_0x00d0;
    L_0x0190:
        r7 = move-exception;
    L_0x0191:
        r7.printStackTrace();
        goto L_0x00d0;
    L_0x0196:
        r7 = move-exception;
    L_0x0197:
        r7.printStackTrace();
        goto L_0x00d0;
    L_0x019c:
        r7 = move-exception;
        r2 = r3;
        goto L_0x0197;
    L_0x019f:
        r7 = move-exception;
        r2 = r3;
        goto L_0x0191;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.ProductService.getSoftPrice(java.lang.String, java.lang.Integer, java.lang.Integer):com.car.result.DiagSoftPriceResult");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.car.result.DiagSoftPriceResult getDiagSoftPrice(java.lang.Integer r27, java.lang.Integer r28) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r26 = this;
        r15 = new com.car.result.DiagSoftPriceResult;
        r15.<init>();
        r6 = new java.util.ArrayList;
        r6.<init>();
        r2 = 0;
        r13 = "getDiagSoftPrice";
        r18 = "userOrderService";
        r14 = new org.ksoap2.serialization.SoapObject;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = "http://www.x431.com";
        r0 = r22;
        r14.<init>(r0, r13);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        if (r27 == 0) goto L_0x002b;
    L_0x001a:
        r22 = "softId";
        r23 = r27.intValue();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r23 = java.lang.Integer.valueOf(r23);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r22;
        r1 = r23;
        r14.addProperty(r0, r1);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
    L_0x002b:
        if (r28 == 0) goto L_0x003e;
    L_0x002d:
        r22 = "lanId";
        r23 = r28.intValue();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r23 = java.lang.Integer.valueOf(r23);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r22;
        r1 = r23;
        r14.addProperty(r0, r1);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
    L_0x003e:
        r17 = new java.lang.StringBuffer;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r17.<init>();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r17;
        r1 = r27;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r22;
        r1 = r28;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r26;
        r0 = r0.token;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r23 = r0;
        r22.append(r23);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r20 = r17.toString();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r19 = com.ifoer.md5.MD5.getMD5Str(r20);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r9 = new org.ksoap2.serialization.SoapSerializationEnvelope;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r22;
        r9.<init>(r0);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r26;
        r1 = r19;
        r22 = r0.createHead(r1);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r22;
        r9.headerOut = r0;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r9.bodyOut = r14;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r9.setOutputSoapObject(r14);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r11 = new com.ifoer.util.MyAndroidHttpTransport;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r23 = "http://mycar.x431.com/services/";
        r22.<init>(r23);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r22;
        r1 = r18;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = r22.toString();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r23 = timeout;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r22;
        r1 = r23;
        r11.<init>(r0, r1);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = "";
        r0 = r22;
        r11.call(r0, r9);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r10 = r9.headerIn;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        if (r10 == 0) goto L_0x00e2;
    L_0x00a7:
        r0 = r10.length;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = r0;
        if (r22 <= 0) goto L_0x00e2;
    L_0x00ac:
        r0 = r10.length;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r24 = r0;
        r22 = 0;
        r23 = r22;
    L_0x00b3:
        r0 = r23;
        r1 = r24;
        if (r0 < r1) goto L_0x00ba;
    L_0x00b9:
        return r15;
    L_0x00ba:
        r8 = r10[r23];	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = 0;
        r0 = r22;
        r22 = r8.getChild(r0);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = (org.kxml2.kdom.Element) r22;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r25 = 0;
        r0 = r22;
        r1 = r25;
        r22 = r0.getChild(r1);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r16 = r22.toString();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = java.lang.Integer.parseInt(r16);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r22;
        r15.setCode(r0);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = r23 + 1;
        r23 = r22;
        goto L_0x00b3;
    L_0x00e2:
        r22 = r9.getResponse();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        if (r22 == 0) goto L_0x00b9;
    L_0x00e8:
        r21 = r9.getResponse();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r21 = (org.ksoap2.serialization.SoapObject) r21;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = "code";
        r22 = r21.getPropertyAsString(r22);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        if (r22 != 0) goto L_0x0164;
    L_0x00fe:
        r22 = "diagSoftPriceList";
        r4 = r21.getProperty(r22);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r4 = (org.ksoap2.serialization.SoapObject) r4;	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = "code";
        r22 = r21.getPropertyAsString(r22);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r22;
        r15.setCode(r0);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r12 = 0;
        r3 = r2;
    L_0x011b:
        r22 = r4.getPropertyCount();	 Catch:{ IOException -> 0x0188, XmlPullParserException -> 0x0185 }
        r0 = r22;
        if (r12 < r0) goto L_0x0128;
    L_0x0123:
        r15.setDiagSoftPriceList(r6);	 Catch:{ IOException -> 0x0188, XmlPullParserException -> 0x0185 }
        r2 = r3;
        goto L_0x00b9;
    L_0x0128:
        r5 = r4.getProperty(r12);	 Catch:{ IOException -> 0x0188, XmlPullParserException -> 0x0185 }
        r5 = (org.ksoap2.serialization.SoapObject) r5;	 Catch:{ IOException -> 0x0188, XmlPullParserException -> 0x0185 }
        r2 = new com.ifoer.entity.DiagSoftPrice;	 Catch:{ IOException -> 0x0188, XmlPullParserException -> 0x0185 }
        r2.<init>();	 Catch:{ IOException -> 0x0188, XmlPullParserException -> 0x0185 }
        r22 = "currencyId";
        r0 = r22;
        r22 = r5.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r22;
        r2.setCurrencyId(r0);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = "price";
        r0 = r22;
        r22 = r5.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = java.lang.Double.valueOf(r22);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = r22.doubleValue();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r22;
        r2.setPrice(r0);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r6.add(r2);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r12 = r12 + 1;
        r3 = r2;
        goto L_0x011b;
    L_0x0164:
        r22 = "code";
        r22 = r21.getPropertyAsString(r22);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        r0 = r22;
        r15.setCode(r0);	 Catch:{ IOException -> 0x0179, XmlPullParserException -> 0x017f }
        goto L_0x00b9;
    L_0x0179:
        r7 = move-exception;
    L_0x017a:
        r7.printStackTrace();
        goto L_0x00b9;
    L_0x017f:
        r7 = move-exception;
    L_0x0180:
        r7.printStackTrace();
        goto L_0x00b9;
    L_0x0185:
        r7 = move-exception;
        r2 = r3;
        goto L_0x0180;
    L_0x0188:
        r7 = move-exception;
        r2 = r3;
        goto L_0x017a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.ProductService.getDiagSoftPrice(java.lang.Integer, java.lang.Integer):com.car.result.DiagSoftPriceResult");
    }

    public DiagSoftOrderResult createDiagSoftOrders(DiagSoftOrder orderInfo) throws NullPointerException, SocketTimeoutException {
        DiagSoftOrderResult res = new DiagSoftOrderResult();
        if (orderInfo != null) {
            orderInfo.getSoftOrderList();
            String serviceName = "userOrderService";
            try {
                SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "createDiagSoftOrder");
                SoapObject packageOrderDTO = new SoapObject();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < orderInfo.getSoftOrderList().size(); i++) {
                    SoapObject softOrderList = new SoapObject();
                    softOrderList.addProperty("softName", ((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getSoftName());
                    sb.append(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getSoftName());
                    softOrderList.addProperty("softId", Integer.valueOf(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getSoftId()));
                    sb.append(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getSoftId());
                    softOrderList.addProperty(AlixDefine.VERSION, ((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getVersion());
                    sb.append(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getVersion());
                    softOrderList.addProperty("price", ((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getPrice());
                    sb.append(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getPrice());
                    softOrderList.addProperty("currencyId", Integer.valueOf(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getCurrencyId()));
                    sb.append(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getCurrencyId());
                    softOrderList.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, ((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getSerialNo());
                    sb.append(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getSerialNo());
                    packageOrderDTO.addProperty("softOrderList", softOrderList);
                }
                packageOrderDTO.addProperty("buyType", Integer.valueOf(orderInfo.getBuyType()));
                sb.append(orderInfo.getBuyType());
                packageOrderDTO.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, orderInfo.getSerialNo());
                sb.append(orderInfo.getSerialNo());
                packageOrderDTO.addProperty("packageId", Integer.valueOf(orderInfo.getPackageId()));
                sb.append(orderInfo.getPackageId());
                if (MainActivity.country == null || MainActivity.country.length() <= 0) {
                    MainActivity.country = Locale.getDefault().getCountry();
                }
                int lanId = AndroidToLan.getLanId(MainActivity.country);
                packageOrderDTO.addProperty("lanId", new StringBuilder(String.valueOf(lanId)).toString());
                sb.append(lanId);
                request.addProperty("packageOrderDTO", packageOrderDTO);
                sb.append(this.token);
                MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
                String sgin = MD5.getMD5Str(sb.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.headerOut = createHead(sgin);
                envelope.bodyOut = request;
                envelope.setOutputSoapObject(request);
                envelope.dotNet = false;
                ht.call(WEBSERVICE_SOAPACION, envelope);
                Element[] headerIn = envelope.headerIn;
                if (headerIn != null && headerIn.length > 0) {
                    for (Element element : headerIn) {
                        res.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
                    }
                } else if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    if (Integer.valueOf(soapObject.getProperty("code").toString()).intValue() == 0) {
                        res.setOrderSn(soapObject.getProperty("orderSn").toString());
                        res.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    } else {
                        res.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e2) {
                e2.printStackTrace();
            }
        }
        return res;
    }

    public DiagSoftOrderResult createDiagSoftOrder(DiagSoftOrder orderInfo) throws NullPointerException, SocketTimeoutException {
        DiagSoftOrderResult res = new DiagSoftOrderResult();
        if (orderInfo != null) {
            orderInfo.getSoftOrderList();
            String serviceName = "userOrderService";
            try {
                SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "createDiagSoftOrder");
                SoapObject orderInfoDTO = new SoapObject();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < orderInfo.getSoftOrderList().size(); i++) {
                    SoapObject softOrderList = new SoapObject();
                    softOrderList.addProperty("softName", ((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getSoftName());
                    sb.append(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getSoftName());
                    softOrderList.addProperty("softId", Integer.valueOf(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getSoftId()));
                    sb.append(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getSoftId());
                    softOrderList.addProperty(AlixDefine.VERSION, ((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getVersion());
                    sb.append(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getVersion());
                    softOrderList.addProperty("price", ((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getPrice());
                    sb.append(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getPrice());
                    softOrderList.addProperty("currencyId", Integer.valueOf(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getCurrencyId()));
                    sb.append(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getCurrencyId());
                    softOrderList.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, ((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getSerialNo());
                    sb.append(((DiagSoftOrderInfo) orderInfo.getSoftOrderList().get(i)).getSerialNo());
                    orderInfoDTO.addProperty("softOrderList", softOrderList);
                }
                orderInfoDTO.addProperty("buyType", Integer.valueOf(orderInfo.getBuyType()));
                sb.append(orderInfo.getBuyType());
                orderInfoDTO.addProperty("currencyId", Integer.valueOf(orderInfo.getCurrencyId()));
                sb.append(orderInfo.getCurrencyId());
                orderInfoDTO.addProperty("totalPrice", orderInfo.getTotalPrice());
                sb.append(orderInfo.getTotalPrice());
                orderInfoDTO.addProperty(MultipleAddresses.CC, orderInfo.getCc());
                sb.append(orderInfo.getCc());
                orderInfoDTO.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, orderInfo.getSerialNo());
                sb.append(orderInfo.getSerialNo());
                request.addProperty("orderInfoDTO", orderInfoDTO);
                sb.append(this.token);
                MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
                String sgin = MD5.getMD5Str(sb.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.headerOut = createHead(sgin);
                envelope.bodyOut = request;
                envelope.setOutputSoapObject(request);
                envelope.dotNet = false;
                ht.call(WEBSERVICE_SOAPACION, envelope);
                Element[] headerIn = envelope.headerIn;
                if (headerIn != null && headerIn.length > 0) {
                    for (Element element : headerIn) {
                        res.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
                    }
                } else if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    if (Integer.valueOf(soapObject.getProperty("code").toString()).intValue() == 0) {
                        res.setOrderSn(soapObject.getProperty("orderSn").toString());
                        res.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    } else {
                        res.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e2) {
                e2.printStackTrace();
            }
        }
        return res;
    }

    public WSResult cancelOrder(Integer orderId) throws NullPointerException, SocketTimeoutException {
        WSResult res = new WSResult();
        String serviceName = "userOrderService";
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "cancelOrder");
            if (orderId != null) {
                request.addProperty("orderId", Integer.valueOf(orderId.intValue()));
            }
            String sgin = MD5.getMD5Str(orderId + this.token);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout).call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    res.setCode(Integer.valueOf(((SoapObject) envelope.getResponse()).getPropertyAsString("code")).intValue());
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.car.result.UserOrderListResult getUserOrderList(java.lang.String r26, java.lang.String r27) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r25 = this;
        r12 = new com.car.result.UserOrderListResult;
        r12.<init>();
        r19 = new java.util.ArrayList;
        r19.<init>();
        r9 = 0;
        r8 = "getUserOrderList";
        r14 = "userOrderService";
        r11 = new org.ksoap2.serialization.SoapObject;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = "http://www.x431.com";
        r0 = r21;
        r11.<init>(r0, r8);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        if (r26 == 0) goto L_0x0023;
    L_0x001a:
        r21 = "CC";
        r0 = r21;
        r1 = r26;
        r11.addProperty(r0, r1);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
    L_0x0023:
        if (r27 == 0) goto L_0x002e;
    L_0x0025:
        r21 = "serialNo";
        r0 = r21;
        r1 = r27;
        r11.addProperty(r0, r1);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
    L_0x002e:
        r21 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r22 = java.lang.String.valueOf(r26);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21.<init>(r22);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r1 = r27;
        r21 = r0.append(r1);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r25;
        r0 = r0.token;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r22 = r0;
        r21 = r21.append(r22);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r16 = r21.toString();	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r15 = com.ifoer.md5.MD5.getMD5Str(r16);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r4 = new org.ksoap2.serialization.SoapSerializationEnvelope;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r21;
        r4.<init>(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r25;
        r21 = r0.createHead(r15);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r4.headerOut = r0;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r4.bodyOut = r11;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r4.setOutputSoapObject(r11);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r6 = new com.ifoer.util.MyAndroidHttpTransport;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r22 = "http://mycar.x431.com/services/";
        r21.<init>(r22);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r21 = r0.append(r14);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r22 = timeout;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r1 = r22;
        r6.<init>(r0, r1);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = "";
        r0 = r21;
        r6.call(r0, r4);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r5 = r4.headerIn;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        if (r5 == 0) goto L_0x00cb;
    L_0x0090:
        r0 = r5.length;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = r0;
        if (r21 <= 0) goto L_0x00cb;
    L_0x0095:
        r0 = r5.length;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r23 = r0;
        r21 = 0;
        r22 = r21;
    L_0x009c:
        r0 = r22;
        r1 = r23;
        if (r0 < r1) goto L_0x00a3;
    L_0x00a2:
        return r12;
    L_0x00a3:
        r3 = r5[r22];	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = 0;
        r0 = r21;
        r21 = r3.getChild(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = (org.kxml2.kdom.Element) r21;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r24 = 0;
        r0 = r21;
        r1 = r24;
        r21 = r0.getChild(r1);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r13 = r21.toString();	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = java.lang.Integer.parseInt(r13);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r12.setCode(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = r22 + 1;
        r22 = r21;
        goto L_0x009c;
    L_0x00cb:
        r21 = r4.getResponse();	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        if (r21 == 0) goto L_0x00a2;
    L_0x00d1:
        r17 = r4.getResponse();	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r17 = (org.ksoap2.serialization.SoapObject) r17;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = "code";
        r0 = r17;
        r1 = r21;
        r21 = r0.getPropertyAsString(r1);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = r21.intValue();	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        if (r21 != 0) goto L_0x01aa;
    L_0x00eb:
        r21 = "orderList";
        r0 = r17;
        r1 = r21;
        r18 = r0.getProperty(r1);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r18 = (org.ksoap2.serialization.SoapObject) r18;	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r7 = 0;
        r10 = r9;
    L_0x00f9:
        r21 = r18.getPropertyCount();	 Catch:{ IOException -> 0x01d2, XmlPullParserException -> 0x01cf }
        r0 = r21;
        if (r7 < r0) goto L_0x0108;
    L_0x0101:
        r0 = r19;
        r12.setUserOrder(r0);	 Catch:{ IOException -> 0x01d2, XmlPullParserException -> 0x01cf }
        r9 = r10;
        goto L_0x00a2;
    L_0x0108:
        r0 = r18;
        r20 = r0.getProperty(r7);	 Catch:{ IOException -> 0x01d2, XmlPullParserException -> 0x01cf }
        r20 = (org.ksoap2.serialization.SoapObject) r20;	 Catch:{ IOException -> 0x01d2, XmlPullParserException -> 0x01cf }
        r9 = new com.ifoer.entity.UserOrder;	 Catch:{ IOException -> 0x01d2, XmlPullParserException -> 0x01cf }
        r9.<init>();	 Catch:{ IOException -> 0x01d2, XmlPullParserException -> 0x01cf }
        r21 = "currencyId";
        r21 = r20.getPropertyAsString(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = r21.intValue();	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r9.setCurrencyId(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = "orderId";
        r21 = r20.getPropertyAsString(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = r21.intValue();	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r9.setOrderId(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = "orderName";
        r21 = r20.getPropertyAsString(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r9.setOrderName(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = "orderSN";
        r21 = r20.getPropertyAsString(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r9.setOrderSN(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = "orderTime";
        r21 = r20.getPropertyAsString(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r9.setOrderTime(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = "payType";
        r21 = r20.getPropertyAsString(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = r21.intValue();	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r9.setPayType(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = "serialNo";
        r21 = r20.getPropertyAsString(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r9.setSerialNo(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = "status";
        r21 = r20.getPropertyAsString(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = r21.intValue();	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r9.setStatus(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = "totalPrice";
        r21 = r20.getPropertyAsString(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = java.lang.Double.valueOf(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = r21.doubleValue();	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r9.setTotalPrice(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r19;
        r0.add(r9);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r7 = r7 + 1;
        r10 = r9;
        goto L_0x00f9;
    L_0x01aa:
        r21 = "code";
        r0 = r17;
        r1 = r21;
        r21 = r0.getPropertyAsString(r1);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r21 = r21.intValue();	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        r0 = r21;
        r12.setCode(r0);	 Catch:{ IOException -> 0x01c3, XmlPullParserException -> 0x01c9 }
        goto L_0x00a2;
    L_0x01c3:
        r2 = move-exception;
    L_0x01c4:
        r2.printStackTrace();
        goto L_0x00a2;
    L_0x01c9:
        r2 = move-exception;
    L_0x01ca:
        r2.printStackTrace();
        goto L_0x00a2;
    L_0x01cf:
        r2 = move-exception;
        r9 = r10;
        goto L_0x01ca;
    L_0x01d2:
        r2 = move-exception;
        r9 = r10;
        goto L_0x01c4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.ProductService.getUserOrderList(java.lang.String, java.lang.String):com.car.result.UserOrderListResult");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.car.result.OrderDetailInfoResult getUserOrderDetailInfo(java.lang.Integer r26) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r25 = this;
        r15 = new com.car.result.OrderDetailInfoResult;
        r15.<init>();
        r11 = new java.util.ArrayList;
        r11.<init>();
        r2 = 0;
        r10 = "getUserOrderDetailInfo";
        r17 = "userOrderService";
        r14 = new org.ksoap2.serialization.SoapObject;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = "http://www.x431.com";
        r0 = r21;
        r14.<init>(r0, r10);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        if (r26 == 0) goto L_0x002b;
    L_0x001a:
        r21 = "orderId";
        r22 = r26.intValue();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r1 = r22;
        r14.addProperty(r0, r1);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
    L_0x002b:
        r21 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21.<init>();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r1 = r26;
        r21 = r0.append(r1);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r25;
        r0 = r0.token;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r22 = r0;
        r21 = r21.append(r22);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r19 = r21.toString();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r18 = com.ifoer.md5.MD5.getMD5Str(r19);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r6 = new org.ksoap2.serialization.SoapSerializationEnvelope;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r21;
        r6.<init>(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r25;
        r1 = r18;
        r21 = r0.createHead(r1);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r6.headerOut = r0;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r6.bodyOut = r14;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r6.setOutputSoapObject(r14);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r8 = new com.ifoer.util.MyAndroidHttpTransport;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r22 = "http://mycar.x431.com/services/";
        r21.<init>(r22);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r1 = r17;
        r21 = r0.append(r1);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r22 = timeout;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r1 = r22;
        r8.<init>(r0, r1);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = "";
        r0 = r21;
        r8.call(r0, r6);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r7 = r6.headerIn;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        if (r7 == 0) goto L_0x00c8;
    L_0x008d:
        r0 = r7.length;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r0;
        if (r21 <= 0) goto L_0x00c8;
    L_0x0092:
        r0 = r7.length;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r23 = r0;
        r21 = 0;
        r22 = r21;
    L_0x0099:
        r0 = r22;
        r1 = r23;
        if (r0 < r1) goto L_0x00a0;
    L_0x009f:
        return r15;
    L_0x00a0:
        r5 = r7[r22];	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = 0;
        r0 = r21;
        r21 = r5.getChild(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = (org.kxml2.kdom.Element) r21;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r24 = 0;
        r0 = r21;
        r1 = r24;
        r21 = r0.getChild(r1);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r16 = r21.toString();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = java.lang.Integer.parseInt(r16);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r15.setCode(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r22 + 1;
        r22 = r21;
        goto L_0x0099;
    L_0x00c8:
        r21 = r6.getResponse();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        if (r21 == 0) goto L_0x009f;
    L_0x00ce:
        r20 = r6.getResponse();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r20 = (org.ksoap2.serialization.SoapObject) r20;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = "code";
        r21 = r20.getProperty(r21);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.intValue();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        if (r21 != 0) goto L_0x01af;
    L_0x00e8:
        r21 = "orderDetailList";
        r13 = r20.getProperty(r21);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r13 = (org.ksoap2.serialization.SoapObject) r13;	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r9 = 0;
        r3 = r2;
    L_0x00f2:
        r21 = r13.getPropertyCount();	 Catch:{ IOException -> 0x01d7, XmlPullParserException -> 0x01d4 }
        r0 = r21;
        if (r9 < r0) goto L_0x0116;
    L_0x00fa:
        r15.setOrderDetailList(r11);	 Catch:{ IOException -> 0x01d7, XmlPullParserException -> 0x01d4 }
        r21 = "code";
        r21 = r20.getProperty(r21);	 Catch:{ IOException -> 0x01d7, XmlPullParserException -> 0x01d4 }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01d7, XmlPullParserException -> 0x01d4 }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01d7, XmlPullParserException -> 0x01d4 }
        r21 = r21.intValue();	 Catch:{ IOException -> 0x01d7, XmlPullParserException -> 0x01d4 }
        r0 = r21;
        r15.setCode(r0);	 Catch:{ IOException -> 0x01d7, XmlPullParserException -> 0x01d4 }
        r2 = r3;
        goto L_0x009f;
    L_0x0116:
        r12 = r13.getProperty(r9);	 Catch:{ IOException -> 0x01d7, XmlPullParserException -> 0x01d4 }
        r12 = (org.ksoap2.serialization.SoapObject) r12;	 Catch:{ IOException -> 0x01d7, XmlPullParserException -> 0x01d4 }
        r2 = new com.ifoer.entity.OrderDetail;	 Catch:{ IOException -> 0x01d7, XmlPullParserException -> 0x01d4 }
        r2.<init>();	 Catch:{ IOException -> 0x01d7, XmlPullParserException -> 0x01d4 }
        r21 = "currencyId";
        r0 = r21;
        r21 = r12.getProperty(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.intValue();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r2.setCurrencyId(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = "orderDetailId";
        r0 = r21;
        r21 = r12.getProperty(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.intValue();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r2.setOrderDetailId(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = "price";
        r0 = r21;
        r21 = r12.getProperty(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = java.lang.Double.valueOf(r21);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.doubleValue();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r2.setPrice(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = "softId";
        r0 = r21;
        r21 = r12.getProperty(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.intValue();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r2.setSoftId(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = "softName";
        r0 = r21;
        r21 = r12.getProperty(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r2.setSoftName(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = "version";
        r0 = r21;
        r21 = r12.getProperty(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r2.setVersion(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r11.add(r2);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r9 = r9 + 1;
        r3 = r2;
        goto L_0x00f2;
    L_0x01af:
        r21 = "code";
        r21 = r20.getProperty(r21);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r21 = r21.intValue();	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        r0 = r21;
        r15.setCode(r0);	 Catch:{ IOException -> 0x01c8, XmlPullParserException -> 0x01ce }
        goto L_0x009f;
    L_0x01c8:
        r4 = move-exception;
    L_0x01c9:
        r4.printStackTrace();
        goto L_0x009f;
    L_0x01ce:
        r4 = move-exception;
    L_0x01cf:
        r4.printStackTrace();
        goto L_0x009f;
    L_0x01d4:
        r4 = move-exception;
        r2 = r3;
        goto L_0x01cf;
    L_0x01d7:
        r4 = move-exception;
        r2 = r3;
        goto L_0x01c9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.ProductService.getUserOrderDetailInfo(java.lang.Integer):com.car.result.OrderDetailInfoResult");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.car.result.SoftPackageInfoResult getSoftPackageInfo(java.lang.Integer r32, java.lang.String r33) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r31 = this;
        r13 = "getSoftPackageInfo";
        r20 = "userOrderService";
        r18 = new com.car.result.SoftPackageInfoResult;
        r18.<init>();
        r26 = new java.util.ArrayList;
        r26.<init>();
        r24 = 0;
        r17 = new org.ksoap2.serialization.SoapObject;	 Catch:{ Exception -> 0x0325 }
        r27 = "http://www.x431.com";
        r0 = r17;
        r1 = r27;
        r0.<init>(r1, r13);	 Catch:{ Exception -> 0x0325 }
        if (r32 == 0) goto L_0x0028;
    L_0x001d:
        r27 = "lanId";
        r0 = r17;
        r1 = r27;
        r2 = r32;
        r0.addProperty(r1, r2);	 Catch:{ Exception -> 0x0325 }
    L_0x0028:
        if (r33 == 0) goto L_0x0035;
    L_0x002a:
        r27 = "serialNo";
        r0 = r17;
        r1 = r27;
        r2 = r33;
        r0.addProperty(r1, r2);	 Catch:{ Exception -> 0x0325 }
    L_0x0035:
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0325 }
        r27.<init>();	 Catch:{ Exception -> 0x0325 }
        r0 = r27;
        r1 = r32;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0325 }
        r0 = r27;
        r1 = r33;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0325 }
        r0 = r31;
        r0 = r0.token;	 Catch:{ Exception -> 0x0325 }
        r28 = r0;
        r27 = r27.append(r28);	 Catch:{ Exception -> 0x0325 }
        r22 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r21 = com.ifoer.md5.MD5.getMD5Str(r22);	 Catch:{ Exception -> 0x0325 }
        r8 = new org.ksoap2.serialization.SoapSerializationEnvelope;	 Catch:{ Exception -> 0x0325 }
        r27 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r27;
        r8.<init>(r0);	 Catch:{ Exception -> 0x0325 }
        r0 = r31;
        r1 = r21;
        r27 = r0.createHead(r1);	 Catch:{ Exception -> 0x0325 }
        r0 = r27;
        r8.headerOut = r0;	 Catch:{ Exception -> 0x0325 }
        r0 = r17;
        r8.bodyOut = r0;	 Catch:{ Exception -> 0x0325 }
        r0 = r17;
        r8.setOutputSoapObject(r0);	 Catch:{ Exception -> 0x0325 }
        r10 = new com.ifoer.util.MyAndroidHttpTransport;	 Catch:{ Exception -> 0x0325 }
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0325 }
        r28 = "http://mycar.x431.com/services/";
        r27.<init>(r28);	 Catch:{ Exception -> 0x0325 }
        r0 = r27;
        r1 = r20;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r28 = timeout;	 Catch:{ Exception -> 0x0325 }
        r0 = r27;
        r1 = r28;
        r10.<init>(r0, r1);	 Catch:{ Exception -> 0x0325 }
        r27 = "";
        r0 = r27;
        r10.call(r0, r8);	 Catch:{ Exception -> 0x0325 }
        r9 = r8.headerIn;	 Catch:{ Exception -> 0x0325 }
        if (r9 == 0) goto L_0x00e0;
    L_0x00a3:
        r0 = r9.length;	 Catch:{ Exception -> 0x0325 }
        r27 = r0;
        if (r27 <= 0) goto L_0x00e0;
    L_0x00a8:
        r0 = r9.length;	 Catch:{ Exception -> 0x0325 }
        r29 = r0;
        r27 = 0;
        r28 = r27;
    L_0x00af:
        r0 = r28;
        r1 = r29;
        if (r0 < r1) goto L_0x00b6;
    L_0x00b5:
        return r18;
    L_0x00b6:
        r7 = r9[r28];	 Catch:{ Exception -> 0x0325 }
        r27 = 0;
        r0 = r27;
        r27 = r7.getChild(r0);	 Catch:{ Exception -> 0x0325 }
        r27 = (org.kxml2.kdom.Element) r27;	 Catch:{ Exception -> 0x0325 }
        r30 = 0;
        r0 = r27;
        r1 = r30;
        r27 = r0.getChild(r1);	 Catch:{ Exception -> 0x0325 }
        r19 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Integer.parseInt(r19);	 Catch:{ Exception -> 0x0325 }
        r0 = r18;
        r1 = r27;
        r0.setCode(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r28 + 1;
        r28 = r27;
        goto L_0x00af;
    L_0x00e0:
        r27 = r8.getResponse();	 Catch:{ Exception -> 0x0325 }
        if (r27 == 0) goto L_0x00b5;
    L_0x00e6:
        r23 = r8.getResponse();	 Catch:{ Exception -> 0x0325 }
        r23 = (org.ksoap2.serialization.SoapObject) r23;	 Catch:{ Exception -> 0x0325 }
        r27 = "code";
        r0 = r23;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.intValue();	 Catch:{ Exception -> 0x0325 }
        if (r27 != 0) goto L_0x02f1;
    L_0x0104:
        r27 = "code";
        r0 = r23;
        r1 = r27;
        r27 = r0.getPropertyAsString(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.intValue();	 Catch:{ Exception -> 0x0325 }
        r0 = r18;
        r1 = r27;
        r0.setCode(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = "message";
        r0 = r23;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r0 = r18;
        r1 = r27;
        r0.setMessage(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = "softPackageList";
        r0 = r23;
        r1 = r27;
        r14 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r14 = (org.ksoap2.serialization.SoapObject) r14;	 Catch:{ Exception -> 0x0325 }
        r11 = 0;
        r25 = r24;
    L_0x0141:
        r27 = r14.getPropertyCount();	 Catch:{ Exception -> 0x032b }
        r0 = r27;
        if (r11 < r0) goto L_0x0154;
    L_0x0149:
        r0 = r18;
        r1 = r26;
        r0.setSoftPackageList(r1);	 Catch:{ Exception -> 0x032b }
        r24 = r25;
        goto L_0x00b5;
    L_0x0154:
        r16 = r14.getProperty(r11);	 Catch:{ Exception -> 0x032b }
        r16 = (org.ksoap2.serialization.SoapObject) r16;	 Catch:{ Exception -> 0x032b }
        r27 = "packageIsBuyed";
        r0 = r16;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x032b }
        r27 = r27.toString();	 Catch:{ Exception -> 0x032b }
        r28 = "0";
        r27 = r27.equals(r28);	 Catch:{ Exception -> 0x032b }
        if (r27 == 0) goto L_0x032f;
    L_0x0170:
        r24 = new com.ifoer.entity.SoftPackageDto;	 Catch:{ Exception -> 0x032b }
        r24.<init>();	 Catch:{ Exception -> 0x032b }
        r27 = "amount";
        r0 = r16;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.intValue();	 Catch:{ Exception -> 0x0325 }
        r0 = r24;
        r1 = r27;
        r0.setAmount(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = "currencyId";
        r0 = r16;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.intValue();	 Catch:{ Exception -> 0x0325 }
        r0 = r24;
        r1 = r27;
        r0.setCurrencyId(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = "packageFlag";
        r0 = r16;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.intValue();	 Catch:{ Exception -> 0x0325 }
        r0 = r24;
        r1 = r27;
        r0.setPackageFlag(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = "packageIsBuyed";
        r0 = r16;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.intValue();	 Catch:{ Exception -> 0x0325 }
        r0 = r24;
        r1 = r27;
        r0.setPackageIsBuyed(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = "softPackageDesc";
        r0 = r16;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r0 = r24;
        r1 = r27;
        r0.setSoftPackageDesc(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = "softPackageId";
        r0 = r16;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.intValue();	 Catch:{ Exception -> 0x0325 }
        r0 = r24;
        r1 = r27;
        r0.setSoftPackageId(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = "softPackageName";
        r0 = r16;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r0 = r24;
        r1 = r27;
        r0.setSoftPackageName(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = "totalPrice";
        r0 = r16;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Double.parseDouble(r27);	 Catch:{ Exception -> 0x0325 }
        r0 = r24;
        r1 = r27;
        r0.setTotalPrice(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = "packageDetailList";
        r0 = r16;
        r1 = r27;
        r4 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r4 = (org.ksoap2.serialization.SoapObject) r4;	 Catch:{ Exception -> 0x0325 }
        r15 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0325 }
        r15.<init>();	 Catch:{ Exception -> 0x0325 }
        r12 = 0;
    L_0x025b:
        r27 = r4.getPropertyCount();	 Catch:{ Exception -> 0x0325 }
        r0 = r27;
        if (r12 < r0) goto L_0x0275;
    L_0x0263:
        r0 = r24;
        r0.setPackageDetailList(r15);	 Catch:{ Exception -> 0x0325 }
        r0 = r26;
        r1 = r24;
        r0.add(r1);	 Catch:{ Exception -> 0x0325 }
    L_0x026f:
        r11 = r11 + 1;
        r25 = r24;
        goto L_0x0141;
    L_0x0275:
        r5 = r4.getProperty(r12);	 Catch:{ Exception -> 0x0325 }
        r5 = (org.ksoap2.serialization.SoapObject) r5;	 Catch:{ Exception -> 0x0325 }
        r3 = new com.ifoer.entity.PackageDetailDto;	 Catch:{ Exception -> 0x0325 }
        r3.<init>();	 Catch:{ Exception -> 0x0325 }
        r27 = "buyed";
        r0 = r27;
        r27 = r5.getPropertyAsString(r0);	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.intValue();	 Catch:{ Exception -> 0x0325 }
        r0 = r27;
        r3.setBuyed(r0);	 Catch:{ Exception -> 0x0325 }
        r27 = "currencyId";
        r0 = r27;
        r27 = r5.getPropertyAsString(r0);	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.intValue();	 Catch:{ Exception -> 0x0325 }
        r0 = r27;
        r3.setCurrencyId(r0);	 Catch:{ Exception -> 0x0325 }
        r27 = "price";
        r0 = r27;
        r27 = r5.getPropertyAsString(r0);	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Double.parseDouble(r27);	 Catch:{ Exception -> 0x0325 }
        r0 = r27;
        r3.setPrice(r0);	 Catch:{ Exception -> 0x0325 }
        r27 = "softId";
        r0 = r27;
        r27 = r5.getPropertyAsString(r0);	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.intValue();	 Catch:{ Exception -> 0x0325 }
        r0 = r27;
        r3.setSoftId(r0);	 Catch:{ Exception -> 0x0325 }
        r27 = "softName";
        r0 = r27;
        r27 = r5.getPropertyAsString(r0);	 Catch:{ Exception -> 0x0325 }
        r0 = r27;
        r3.setSoftName(r0);	 Catch:{ Exception -> 0x0325 }
        r27 = "version";
        r0 = r27;
        r27 = r5.getPropertyAsString(r0);	 Catch:{ Exception -> 0x0325 }
        r0 = r27;
        r3.setVersion(r0);	 Catch:{ Exception -> 0x0325 }
        r15.add(r3);	 Catch:{ Exception -> 0x0325 }
        r12 = r12 + 1;
        goto L_0x025b;
    L_0x02f1:
        r27 = "code";
        r0 = r23;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r27 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.intValue();	 Catch:{ Exception -> 0x0325 }
        r0 = r18;
        r1 = r27;
        r0.setCode(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = "message";
        r0 = r23;
        r1 = r27;
        r27 = r0.getProperty(r1);	 Catch:{ Exception -> 0x0325 }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0325 }
        r0 = r18;
        r1 = r27;
        r0.setMessage(r1);	 Catch:{ Exception -> 0x0325 }
        goto L_0x00b5;
    L_0x0325:
        r6 = move-exception;
    L_0x0326:
        r6.printStackTrace();
        goto L_0x00b5;
    L_0x032b:
        r6 = move-exception;
        r24 = r25;
        goto L_0x0326;
    L_0x032f:
        r24 = r25;
        goto L_0x026f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.ProductService.getSoftPackageInfo(java.lang.Integer, java.lang.String):com.car.result.SoftPackageInfoResult");
    }

    public AlipayRSATradeDTOResult queryAlipayRSATrade(QueryAlipayRSATrade queryAlipayRSATrade) throws NullPointerException, SocketTimeoutException {
        AlipayRSATradeDTO alipayRSATradeDTO;
        IOException e;
        XmlPullParserException e2;
        AlipayRSATradeDTOResult res = new AlipayRSATradeDTOResult();
        String serviceName = "alipayService";
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "queryAlipayRSATrade");
            if (queryAlipayRSATrade != null) {
                request.addProperty("orderId", Integer.valueOf(queryAlipayRSATrade.getOrderId()));
                request.addProperty("basePath", queryAlipayRSATrade.getBasePath());
            }
            String sgin = MD5.getMD5Str(queryAlipayRSATrade.getOrderId() + queryAlipayRSATrade.getBasePath() + this.token);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout).call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    if (Integer.valueOf(soapObject.getProperty("code").toString()).intValue() == 0) {
                        AlipayRSATradeDTO dto = new AlipayRSATradeDTO();
                        try {
                            SoapObject dtoObj = (SoapObject) soapObject.getProperty("alipayRSATradeDTO");
                            dto.setSign(dtoObj.getProperty(Constants.SIGN).toString());
                            dto.setSignData(dtoObj.getProperty("signData").toString());
                            dto.setSignType(dtoObj.getPropertyAsString("signType"));
                            res.setAlipayRSATradeDTO(dto);
                            res.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                            alipayRSATradeDTO = dto;
                        } catch (IOException e3) {
                            e = e3;
                            alipayRSATradeDTO = dto;
                            e.printStackTrace();
                            return res;
                        } catch (XmlPullParserException e4) {
                            e2 = e4;
                            alipayRSATradeDTO = dto;
                            e2.printStackTrace();
                            return res;
                        }
                    }
                    res.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                }
                return res;
            }
            for (Element element : headerIn) {
                res.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return res;
        } catch (IOException e5) {
            e = e5;
            e.printStackTrace();
            return res;
        } catch (XmlPullParserException e6) {
            e2 = e6;
            e2.printStackTrace();
            return res;
        }
    }

    public CompletePay completePay(Integer orderId, Integer payType, Context context) throws NullPointerException, SocketTimeoutException {
        CompletePay completePay = new CompletePay();
        String serviceName = "userOrderService";
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "completePay");
            if (orderId != null) {
                request.addProperty("orderId", orderId);
            }
            if (payType != null) {
                request.addProperty("payType", payType);
            }
            StringBuffer sb = new StringBuffer();
            sb.append(orderId).append(payType);
            String sgin = MD5.getMD5Str(sb.toString() + this.token);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout).call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    int code = Integer.valueOf(soapObject.getProperty("code").toString()).intValue();
                    if (code == 0) {
                        completePay.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    } else {
                        completePay.setCode(code);
                        completePay.setMessage(new CompleteCodes().completeCode(code, context));
                    }
                }
                return completePay;
            }
            for (Element element : headerIn) {
                completePay.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return completePay;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.ifoer.entity.CntSynUpdateInfoPageResult getPushMessageList(java.lang.String r23, int r24, int r25, int r26) {
        /*
        r22 = this;
        r9 = "getPushMessageList";
        r14 = "iosPushService";
        r12 = new com.ifoer.entity.CntSynUpdateInfoPageResult;
        r12.<init>();
        r10 = new com.ifoer.entity.CntSynUpdateInfoPage;
        r10.<init>();
        r2 = new java.util.ArrayList;
        r2.<init>();
        r3 = 0;
        r11 = new org.ksoap2.serialization.SoapObject;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = "http://www.x431.com";
        r0 = r20;
        r11.<init>(r0, r9);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = android.text.TextUtils.isEmpty(r23);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        if (r20 != 0) goto L_0x002c;
    L_0x0023:
        r20 = "concernedSN";
        r0 = r20;
        r1 = r23;
        r11.addProperty(r0, r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
    L_0x002c:
        r20 = "messageType";
        r21 = java.lang.Integer.valueOf(r24);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r1 = r21;
        r11.addProperty(r0, r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        if (r25 == 0) goto L_0x0048;
    L_0x003b:
        r20 = "pageNo";
        r21 = java.lang.Integer.valueOf(r25);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r1 = r21;
        r11.addProperty(r0, r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
    L_0x0048:
        if (r26 == 0) goto L_0x0057;
    L_0x004a:
        r20 = "pageSize";
        r21 = java.lang.Integer.valueOf(r26);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r1 = r21;
        r11.addProperty(r0, r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
    L_0x0057:
        r7 = new org.ksoap2.transport.HttpTransportSE;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r21 = "http://mycar.x431.com/services/";
        r20.<init>(r21);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r20 = r0.append(r14);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = r20.toString();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r7.<init>(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r6 = new org.ksoap2.serialization.SoapSerializationEnvelope;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r20;
        r6.<init>(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r13 = new java.lang.StringBuffer;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r13.<init>();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r23;
        r20 = r13.append(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r1 = r24;
        r20 = r0.append(r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r1 = r25;
        r20 = r0.append(r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r1 = r26;
        r0.append(r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r21 = r13.toString();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r21 = java.lang.String.valueOf(r21);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20.<init>(r21);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r22;
        r0 = r0.token;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r21 = r0;
        r20 = r20.append(r21);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = r20.toString();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r15 = com.ifoer.md5.MD5.getMD5Str(r20);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r22;
        r20 = r0.createHead(r15);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r6.headerOut = r0;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r6.bodyOut = r11;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = 0;
        r0 = r20;
        r6.dotNet = r0;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = "";
        r0 = r20;
        r7.call(r0, r6);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = r6.getResponse();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        if (r20 == 0) goto L_0x0157;
    L_0x00d8:
        r18 = r6.getResponse();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = (org.ksoap2.serialization.SoapObject) r18;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = "code";
        r0 = r18;
        r1 = r20;
        r20 = r0.getProperty(r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = r20.toString();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = java.lang.Integer.valueOf(r20);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = r20.intValue();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r12.setCode(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = "pagingHelper";
        r0 = r18;
        r1 = r20;
        r19 = r0.getProperty(r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r19 = (org.ksoap2.serialization.SoapObject) r19;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = "pageNo";
        r20 = r19.getPropertyAsString(r20);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = java.lang.Integer.valueOf(r20);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = r20.intValue();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r10.setPageNo(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = "size";
        r20 = r19.getPropertyAsString(r20);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = java.lang.Integer.valueOf(r20);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = r20.intValue();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r10.setSize(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = "pageSize";
        r20 = r19.getPropertyAsString(r20);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = java.lang.Integer.valueOf(r20);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = r20.intValue();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r10.setPageSize(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = "dataList";
        r16 = r19.getProperty(r20);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r16 = (org.ksoap2.serialization.SoapObject) r16;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r8 = 0;
        r4 = r3;
    L_0x0148:
        r20 = r16.getPropertyCount();	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r0 = r20;
        if (r8 < r0) goto L_0x0158;
    L_0x0150:
        r10.setDataList(r2);	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r12.setPage(r10);	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r3 = r4;
    L_0x0157:
        return r12;
    L_0x0158:
        r0 = r16;
        r17 = r0.getProperty(r8);	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r17 = (org.ksoap2.serialization.SoapObject) r17;	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r3 = new com.ifoer.entity.CntSynUpdateInfo;	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r3.<init>();	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r20 = "messageDesc";
        r0 = r17;
        r1 = r20;
        r20 = r0.getPropertyAsString(r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r3.setName(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = "messageId";
        r0 = r17;
        r1 = r20;
        r20 = r0.getPropertyAsString(r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = java.lang.Integer.parseInt(r20);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r3.setId(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = "pushTime";
        r0 = r17;
        r1 = r20;
        r20 = r0.getPropertyAsString(r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r20;
        r3.setLastUpdateDate(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r2.add(r3);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r8 = r8 + 1;
        r4 = r3;
        goto L_0x0148;
    L_0x019d:
        r5 = move-exception;
    L_0x019e:
        r5.printStackTrace();
        goto L_0x0157;
    L_0x01a2:
        r5 = move-exception;
    L_0x01a3:
        r5.printStackTrace();
        goto L_0x0157;
    L_0x01a7:
        r5 = move-exception;
        r3 = r4;
        goto L_0x01a3;
    L_0x01aa:
        r5 = move-exception;
        r3 = r4;
        goto L_0x019e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.ProductService.getPushMessageList(java.lang.String, int, int, int):com.ifoer.entity.CntSynUpdateInfoPageResult");
    }
}
