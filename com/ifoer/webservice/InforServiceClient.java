package com.ifoer.webservice;

import com.car.result.CntNewsDescResult;
import com.cnlaunch.framework.common.Constants;
import com.ifoer.entity.CntSynNews;
import com.ifoer.entity.CntSynUpdateInfo;
import com.ifoer.entity.CntSynUpdateInfoResult;
import com.ifoer.entity.Constant;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

public class InforServiceClient {
    private static final String WEBSERVICE_NAMESPACE = "http://www.x431.com";
    private static final String WEBSERVICE_SOAPACION = "";
    private static final String WEBSERVICE_URL = "http://mycar.x431.com/services/";
    private String cc;
    private int timeout;
    private String token;

    public InforServiceClient() {
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.car.result.CntNewsPageResult queryCntSynNews(int r22, int r23, com.ifoer.entity.CntNewsCondition r24) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r21 = this;
        r9 = "queryCntSynNews";
        r13 = "cntNewsService";
        r12 = new com.car.result.CntNewsPageResult;
        r12.<init>();
        r10 = new com.ifoer.entity.CntNewsPage;
        r10.<init>();
        r2 = new java.util.ArrayList;
        r2.<init>();
        r3 = 0;
        r11 = new org.ksoap2.serialization.SoapObject;	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "http://www.x431.com";
        r0 = r18;
        r11.<init>(r0, r9);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        if (r22 == 0) goto L_0x002c;
    L_0x001f:
        r18 = "pageNo";
        r19 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r1 = r19;
        r11.addProperty(r0, r1);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
    L_0x002c:
        if (r23 == 0) goto L_0x003b;
    L_0x002e:
        r18 = "pageSize";
        r19 = java.lang.Integer.valueOf(r23);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r1 = r19;
        r11.addProperty(r0, r1);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
    L_0x003b:
        if (r24 == 0) goto L_0x0046;
    L_0x003d:
        r18 = "cntNewsCondition";
        r0 = r18;
        r1 = r24;
        r11.addProperty(r0, r1);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
    L_0x0046:
        r7 = new com.ifoer.util.MyAndroidHttpTransport;	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r19 = "http://mycar.x431.com/services/";
        r18.<init>(r19);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r18 = r0.append(r13);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = r18.toString();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r7.<init>(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r6 = new org.ksoap2.serialization.SoapSerializationEnvelope;	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r18;
        r6.<init>(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r6.setOutputSoapObject(r11);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = 0;
        r0 = r18;
        r6.dotNet = r0;	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "";
        r0 = r18;
        r7.call(r0, r6);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = r6.getResponse();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        if (r18 == 0) goto L_0x014e;
    L_0x007d:
        r18 = java.lang.System.out;	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r19 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r20 = "queryCntSynNews==";
        r19.<init>(r20);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r20 = r6.getResponse();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r20 = r20.toString();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r19 = r19.append(r20);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r19 = r19.toString();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18.println(r19);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r16 = r6.getResponse();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r16 = (org.ksoap2.serialization.SoapObject) r16;	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "code";
        r0 = r16;
        r1 = r18;
        r18 = r0.getProperty(r1);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = r18.toString();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = java.lang.Integer.valueOf(r18);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = r18.intValue();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        if (r18 != 0) goto L_0x01c5;
    L_0x00b7:
        r18 = "code";
        r0 = r16;
        r1 = r18;
        r18 = r0.getProperty(r1);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = r18.toString();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = java.lang.Integer.valueOf(r18);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = r18.intValue();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r12.setCode(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "page";
        r0 = r16;
        r1 = r18;
        r17 = r0.getProperty(r1);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r17 = (org.ksoap2.serialization.SoapObject) r17;	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "pageNo";
        r18 = r17.getPropertyAsString(r18);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = java.lang.Integer.valueOf(r18);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = r18.intValue();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r10.setPageNo(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "size";
        r18 = r17.getPropertyAsString(r18);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = java.lang.Integer.valueOf(r18);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = r18.intValue();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r10.setSize(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "pageSize";
        r18 = r17.getPropertyAsString(r18);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = java.lang.Integer.valueOf(r18);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = r18.intValue();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r10.setPageSize(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "dataList";
        r14 = r17.getProperty(r18);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r14 = (org.ksoap2.serialization.SoapObject) r14;	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = java.lang.System.out;	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r19 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r20 = "hhhhhhhhhhhhhhhhhhhhhhhhh";
        r19.<init>(r20);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r20 = r14.getPropertyCount();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r19 = r19.append(r20);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r19 = r19.toString();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18.println(r19);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = r14.getPropertyCount();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        if (r18 <= 0) goto L_0x014e;
    L_0x013d:
        r8 = 0;
        r4 = r3;
    L_0x013f:
        r18 = r14.getPropertyCount();	 Catch:{ IOException -> 0x01f1, XmlPullParserException -> 0x01ee }
        r0 = r18;
        if (r8 < r0) goto L_0x014f;
    L_0x0147:
        r10.setDataList(r2);	 Catch:{ IOException -> 0x01f1, XmlPullParserException -> 0x01ee }
        r12.setPage(r10);	 Catch:{ IOException -> 0x01f1, XmlPullParserException -> 0x01ee }
        r3 = r4;
    L_0x014e:
        return r12;
    L_0x014f:
        r15 = r14.getProperty(r8);	 Catch:{ IOException -> 0x01f1, XmlPullParserException -> 0x01ee }
        r15 = (org.ksoap2.serialization.SoapObject) r15;	 Catch:{ IOException -> 0x01f1, XmlPullParserException -> 0x01ee }
        r3 = new com.ifoer.entity.CntSynNews;	 Catch:{ IOException -> 0x01f1, XmlPullParserException -> 0x01ee }
        r3.<init>();	 Catch:{ IOException -> 0x01f1, XmlPullParserException -> 0x01ee }
        r18 = "author";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r3.setAuthor(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "id";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = java.lang.Integer.parseInt(r18);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r3.setId(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "publishDate";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r3.setPublishDate(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "publisher";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r3.setPublisher(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "source";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r3.setSource(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "subject";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r3.setSubject(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = "type";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = java.lang.Integer.parseInt(r18);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r3.setType(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r2.add(r3);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r8 = r8 + 1;
        r4 = r3;
        goto L_0x013f;
    L_0x01c5:
        r18 = "code";
        r0 = r16;
        r1 = r18;
        r18 = r0.getProperty(r1);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = r18.toString();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = java.lang.Integer.valueOf(r18);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r18 = r18.intValue();	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        r0 = r18;
        r12.setCode(r0);	 Catch:{ IOException -> 0x01e2, XmlPullParserException -> 0x01e8 }
        goto L_0x014e;
    L_0x01e2:
        r5 = move-exception;
    L_0x01e3:
        r5.printStackTrace();
        goto L_0x014e;
    L_0x01e8:
        r5 = move-exception;
    L_0x01e9:
        r5.printStackTrace();
        goto L_0x014e;
    L_0x01ee:
        r5 = move-exception;
        r3 = r4;
        goto L_0x01e9;
    L_0x01f1:
        r5 = move-exception;
        r3 = r4;
        goto L_0x01e3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.InforServiceClient.queryCntSynNews(int, int, com.ifoer.entity.CntNewsCondition):com.car.result.CntNewsPageResult");
    }

    public CntNewsDescResult getCntSynNews(int newsId) throws NullPointerException, SocketTimeoutException {
        IOException e;
        String methodName = "getCntSynNews";
        String serviceName = "cntNewsService";
        CntNewsDescResult result = new CntNewsDescResult();
        List<String> imagPaths = new ArrayList();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (newsId != 0) {
                request.addProperty("newsId", Integer.valueOf(newsId));
            }
            HttpTransportSE ht = new HttpTransportSE(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = false;
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                System.out.println("getCntSynNews==" + envelope.getResponse().toString());
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                    CntSynNews cnt = new CntSynNews();
                    try {
                        SoapObject soapCnt = (SoapObject) soapObject.getProperty("CntSynNews");
                        if (soapCnt.hasProperty("content")) {
                            cnt.setContent(soapCnt.getPropertyAsString("content"));
                        }
                        for (int i = 0; i < soapCnt.getPropertyCount(); i++) {
                            PropertyInfo propertyInfo = new PropertyInfo();
                            soapCnt.getPropertyInfo(i, propertyInfo);
                            if (propertyInfo.getName().equals("imagPath")) {
                                StringBuffer sb = new StringBuffer();
                                sb.append(Constant.urlBusiness).append(soapCnt.getPropertyAsString(i).toString());
                                imagPaths.add(sb.toString());
                            }
                        }
                        result.setImagPaths(imagPaths);
                        result.setCntNewsDesc(cnt);
                        CntSynNews cntSynNews = cnt;
                    } catch (IOException e2) {
                        e = e2;
                        cntSynNews = cnt;
                        e.printStackTrace();
                        return result;
                    } catch (XmlPullParserException e3) {
                        e = e3;
                        cntSynNews = cnt;
                        e.printStackTrace();
                        return result;
                    }
                }
            }
        } catch (IOException e4) {
            e = e4;
            e.printStackTrace();
            return result;
        } catch (XmlPullParserException e5) {
            XmlPullParserException e6;
            e6 = e5;
            e6.printStackTrace();
            return result;
        }
        return result;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.ifoer.entity.CntSynUpdateInfoPageResult queryCntSynUpdateInfo(int r22, int r23, int r24) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r21 = this;
        r9 = "queryCntSynUpdateInfo";
        r13 = "cntNewsService";
        r12 = new com.ifoer.entity.CntSynUpdateInfoPageResult;
        r12.<init>();
        r10 = new com.ifoer.entity.CntSynUpdateInfoPage;
        r10.<init>();
        r2 = new java.util.ArrayList;
        r2.<init>();
        r3 = 0;
        r11 = new org.ksoap2.serialization.SoapObject;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "http://www.x431.com";
        r0 = r18;
        r11.<init>(r0, r9);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        if (r22 == 0) goto L_0x002c;
    L_0x001f:
        r18 = "pageNo";
        r19 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r1 = r19;
        r11.addProperty(r0, r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
    L_0x002c:
        if (r23 == 0) goto L_0x003b;
    L_0x002e:
        r18 = "pageSize";
        r19 = java.lang.Integer.valueOf(r23);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r1 = r19;
        r11.addProperty(r0, r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
    L_0x003b:
        if (r24 == 0) goto L_0x004a;
    L_0x003d:
        r18 = "lanId";
        r19 = java.lang.Integer.valueOf(r24);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r1 = r19;
        r11.addProperty(r0, r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
    L_0x004a:
        r7 = new org.ksoap2.transport.HttpTransportSE;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r19 = "http://mycar.x431.com/services/";
        r18.<init>(r19);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r18 = r0.append(r13);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = r18.toString();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r7.<init>(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r6 = new org.ksoap2.serialization.SoapSerializationEnvelope;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r18;
        r6.<init>(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r6.setOutputSoapObject(r11);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = 0;
        r0 = r18;
        r6.dotNet = r0;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "";
        r0 = r18;
        r7.call(r0, r6);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = r6.getResponse();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        if (r18 == 0) goto L_0x0134;
    L_0x0081:
        r18 = java.lang.System.out;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r19 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = "queryCntSynUpdateInfo==";
        r19.<init>(r20);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = r6.getResponse();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r20 = r20.toString();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r19 = r19.append(r20);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r19 = r19.toString();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18.println(r19);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r16 = r6.getResponse();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r16 = (org.ksoap2.serialization.SoapObject) r16;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "code";
        r0 = r16;
        r1 = r18;
        r18 = r0.getProperty(r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = r18.toString();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = java.lang.Integer.valueOf(r18);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = r18.intValue();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        if (r18 != 0) goto L_0x0134;
    L_0x00bb:
        r18 = "code";
        r0 = r16;
        r1 = r18;
        r18 = r0.getProperty(r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = r18.toString();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = java.lang.Integer.valueOf(r18);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = r18.intValue();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r12.setCode(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "page";
        r0 = r16;
        r1 = r18;
        r17 = r0.getProperty(r1);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r17 = (org.ksoap2.serialization.SoapObject) r17;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "pageNo";
        r18 = r17.getPropertyAsString(r18);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = java.lang.Integer.valueOf(r18);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = r18.intValue();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r10.setPageNo(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "size";
        r18 = r17.getPropertyAsString(r18);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = java.lang.Integer.valueOf(r18);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = r18.intValue();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r10.setSize(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "pageSize";
        r18 = r17.getPropertyAsString(r18);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = java.lang.Integer.valueOf(r18);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = r18.intValue();	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r10.setPageSize(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "dataList";
        r14 = r17.getProperty(r18);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r14 = (org.ksoap2.serialization.SoapObject) r14;	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r8 = 0;
        r4 = r3;
    L_0x0125:
        r18 = r14.getPropertyCount();	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r0 = r18;
        if (r8 < r0) goto L_0x0135;
    L_0x012d:
        r10.setDataList(r2);	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r12.setPage(r10);	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r3 = r4;
    L_0x0134:
        return r12;
    L_0x0135:
        r15 = r14.getProperty(r8);	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r15 = (org.ksoap2.serialization.SoapObject) r15;	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r3 = new com.ifoer.entity.CntSynUpdateInfo;	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r3.<init>();	 Catch:{ IOException -> 0x01aa, XmlPullParserException -> 0x01a7 }
        r18 = "fstrUser";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r3.setFstrUser(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "id";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = java.lang.Integer.parseInt(r18);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r3.setId(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "lastUpdateDate";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r3.setLastUpdateDate(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "name";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r3.setName(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "oaAuditNo";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r3.setOaAuditNo(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = "synId";
        r0 = r18;
        r18 = r15.getPropertyAsString(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r18 = java.lang.Integer.parseInt(r18);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r0 = r18;
        r3.setSynId(r0);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r2.add(r3);	 Catch:{ IOException -> 0x019d, XmlPullParserException -> 0x01a2 }
        r8 = r8 + 1;
        r4 = r3;
        goto L_0x0125;
    L_0x019d:
        r5 = move-exception;
    L_0x019e:
        r5.printStackTrace();
        goto L_0x0134;
    L_0x01a2:
        r5 = move-exception;
    L_0x01a3:
        r5.printStackTrace();
        goto L_0x0134;
    L_0x01a7:
        r5 = move-exception;
        r3 = r4;
        goto L_0x01a3;
    L_0x01aa:
        r5 = move-exception;
        r3 = r4;
        goto L_0x019e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.InforServiceClient.queryCntSynUpdateInfo(int, int, int):com.ifoer.entity.CntSynUpdateInfoPageResult");
    }

    public CntSynUpdateInfoResult getCntSynUpdateInfo(int id) {
        String methodName = "getCntSynUpdateInfo";
        String serviceName = "cntNewsService";
        CntSynUpdateInfoResult result = new CntSynUpdateInfoResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (id != 0) {
                request.addProperty(LocaleUtil.INDONESIAN, Integer.valueOf(id));
            }
            HttpTransportSE ht = new HttpTransportSE(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = false;
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                System.out.println("getCntSynUpdateInfo==" + envelope.getResponse().toString());
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                    CntSynUpdateInfo cntSynUpdateInfo = new CntSynUpdateInfo();
                    SoapObject soapCnt = (SoapObject) soapObject.getProperty("CntSynUpdateInfo");
                    cntSynUpdateInfo.setContent(soapCnt.getPropertyAsString("content"));
                    cntSynUpdateInfo.setId(Integer.parseInt(soapCnt.getPropertyAsString(LocaleUtil.INDONESIAN)));
                    cntSynUpdateInfo.setName(soapCnt.getPropertyAsString("name"));
                    result.setCntSynUpdateInfo(cntSynUpdateInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return result;
    }
}
