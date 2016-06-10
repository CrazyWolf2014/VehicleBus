package com.ifoer.webservice;

import android.os.Handler;
import com.car.result.X431PadSoftListResult;
import com.cnlaunch.framework.common.Constants;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.ifoer.entity.Constant;
import com.ifoer.entity.X431PadSoftDTO;
import com.ifoer.md5.MD5;
import com.ifoer.util.MyAndroidHttpTransport;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

public class X431PadDiagSoftService {
    private static final String WEBSERVICE_NAMESPACE = "http://www.x431.com";
    private static final String WEBSERVICE_SOAPACION = "";
    private static final String WEBSERVICE_URL = "http://mycar.x431.com/services/";
    public static int timeout;
    private String cc;
    private String token;

    static {
        timeout = 15000;
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

    private Element[] createHead(String sign, String cc) {
        Element[] header = new Element[]{new Element().createElement(WEBSERVICE_NAMESPACE, Constant.AUTHENTICATE)};
        Element ccElt = new Element().createElement(WEBSERVICE_NAMESPACE, MultipleAddresses.CC);
        ccElt.addChild(4, cc);
        header[0].addChild(2, ccElt);
        Element signElt = new Element().createElement(WEBSERVICE_NAMESPACE, Constants.SIGN);
        signElt.addChild(4, sign);
        header[0].addChild(2, signElt);
        return header;
    }

    public X431PadSoftListResult queryLatestDiagSoftsByArea(Integer areaId, Integer lanId, Integer defaultLanId) throws NullPointerException, SocketTimeoutException {
        IOException e;
        XmlPullParserException e2;
        String serviceName = "x431PadDiagSoftService";
        SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "queryLatestDiagSoftsByArea");
        X431PadSoftListResult result = null;
        List<X431PadSoftDTO> dtoList = new ArrayList();
        if (areaId != null) {
            request.addProperty("areaId", areaId);
        }
        if (lanId != null) {
            request.addProperty("lanId", lanId);
        }
        if (defaultLanId != null) {
            request.addProperty("defaultLanId", defaultLanId);
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(areaId).append(lanId).append(defaultLanId);
        String sgin = MD5.getMD5Str(buffer.toString() + this.token);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.headerOut = createHead(sgin, this.cc);
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
        try {
            ht.debug = true;
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() == null) {
                return null;
            }
            X431PadSoftListResult result2 = new X431PadSoftListResult();
            try {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                result2.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() != 0) {
                    return result2;
                }
                SoapObject softList = (SoapObject) soapObject.getProperty("x431PadSoftList");
                for (int i = 0; i < softList.getPropertyCount(); i++) {
                    SoapObject softInfo = (SoapObject) softList.getProperty(i);
                    X431PadSoftDTO padSoftDto = new X431PadSoftDTO();
                    padSoftDto.setDiagVehicleType(softInfo.getPropertyAsString("diagVehicleType"));
                    padSoftDto.setLanId(softInfo.getPropertyAsString("lanId"));
                    padSoftDto.setSoftApplicableArea(softInfo.getPropertyAsString("softApplicableArea"));
                    padSoftDto.setSoftId(softInfo.getPropertyAsString("softId"));
                    padSoftDto.setSoftName(softInfo.getPropertyAsString("softName"));
                    padSoftDto.setSoftPackageID(softInfo.getPropertyAsString("softPackageID"));
                    padSoftDto.setVersionDetailId(softInfo.getPropertyAsString("versionDetailId"));
                    padSoftDto.setVersionNo(softInfo.getPropertyAsString("versionNo"));
                    dtoList.add(padSoftDto);
                }
                result2.setDtoList(dtoList);
                return result2;
            } catch (IOException e3) {
                e = e3;
                result = result2;
                e.printStackTrace();
                return result;
            } catch (XmlPullParserException e4) {
                e2 = e4;
                result = result2;
                e2.printStackTrace();
                return result;
            }
        } catch (IOException e5) {
            e = e5;
            e.printStackTrace();
            return result;
        } catch (XmlPullParserException e6) {
            e2 = e6;
            e2.printStackTrace();
            return result;
        }
    }

    public X431PadSoftListResult queryLatestDiagSofts(String cc, String token, String serialNo, Integer lanId, Integer defaultLanId) throws NullPointerException, SocketTimeoutException {
        String serviceName = "x431PadDiagSoftService";
        X431PadSoftListResult result = new X431PadSoftListResult();
        List<X431PadSoftDTO> dtoList = new ArrayList();
        SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "queryLatestDiagSofts");
        if (serialNo != null) {
            request.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, serialNo);
        }
        if (lanId != null) {
            request.addProperty("lanId", lanId);
        }
        if (defaultLanId != null) {
            request.addProperty("defaultLanId", defaultLanId);
        }
        if (cc != null) {
            request.addProperty(MultipleAddresses.CC, cc);
        }
        String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(serialNo)).append(lanId).append(defaultLanId).append(cc).append(token).toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.headerOut = createHead(sgin, cc);
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        try {
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), SyncHttpTransportSE.DEFAULTTIMEOUT);
            ht.debug = true;
            ht.call(null, envelope, null);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    result.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                        SoapObject softList = (SoapObject) soapObject.getProperty("x431PadSoftList");
                        for (int i = 0; i < softList.getPropertyCount(); i++) {
                            SoapObject softInfo = (SoapObject) softList.getProperty(i);
                            X431PadSoftDTO padSoftDto = new X431PadSoftDTO();
                            padSoftDto.setDiagVehicleType(softInfo.getPropertyAsString("diagVehicleType"));
                            padSoftDto.setFileSize(softInfo.getPropertyAsString("fileSize"));
                            padSoftDto.setLanId(softInfo.getPropertyAsString("lanId"));
                            padSoftDto.setSoftApplicableArea(softInfo.getPropertyAsString("softApplicableArea"));
                            padSoftDto.setSoftId(softInfo.getPropertyAsString("softId"));
                            String tempSoftName = softInfo.getPropertyAsString("softName");
                            if (softInfo.getPropertyAsString("softName").equals("EOBD2")) {
                                tempSoftName = "EOBD";
                            }
                            padSoftDto.setSoftName(tempSoftName);
                            padSoftDto.setSoftPackageID(softInfo.getPropertyAsString("softPackageID"));
                            padSoftDto.setSoftUpdateTime(softInfo.getPropertyAsString("softUpdateTime"));
                            padSoftDto.setVersionDetailId(softInfo.getPropertyAsString("versionDetailId"));
                            padSoftDto.setVersionNo(softInfo.getPropertyAsString("versionNo"));
                            dtoList.add(padSoftDto);
                        }
                        result.setDtoList(dtoList);
                    }
                }
                return result;
            }
            for (Element element : headerIn) {
                result.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.car.result.DiagSoftIdListResult getDiagSoftIdList(java.lang.String r27) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r26 = this;
        r13 = "getDiagSoftIdList";
        r18 = "x431PadDiagSoftService";
        r16 = new com.car.result.DiagSoftIdListResult;
        r16.<init>();
        r3 = new java.util.ArrayList;
        r3.<init>();
        r5 = 0;
        r15 = new org.ksoap2.serialization.SoapObject;
        r22 = "http://www.x431.com";
        r0 = r22;
        r15.<init>(r0, r13);
        if (r27 == 0) goto L_0x0023;
    L_0x001a:
        r22 = "productTypeCode";
        r0 = r22;
        r1 = r27;
        r15.addProperty(r0, r1);
    L_0x0023:
        r22 = new java.lang.StringBuilder;
        r23 = java.lang.String.valueOf(r27);
        r22.<init>(r23);
        r0 = r26;
        r0 = r0.token;
        r23 = r0;
        r22 = r22.append(r23);
        r20 = r22.toString();
        r19 = com.ifoer.md5.MD5.getMD5Str(r20);
        r9 = new org.ksoap2.serialization.SoapSerializationEnvelope;
        r22 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r22;
        r9.<init>(r0);
        r0 = r26;
        r0 = r0.cc;
        r22 = r0;
        r0 = r26;
        r1 = r19;
        r2 = r22;
        r22 = r0.createHead(r1, r2);
        r0 = r22;
        r9.headerOut = r0;
        r9.bodyOut = r15;
        r9.setOutputSoapObject(r15);
        r11 = new com.ifoer.util.MyAndroidHttpTransport;
        r22 = new java.lang.StringBuilder;
        r23 = "http://mycar.x431.com/services/";
        r22.<init>(r23);
        r0 = r22;
        r1 = r18;
        r22 = r0.append(r1);
        r22 = r22.toString();
        r23 = timeout;
        r0 = r22;
        r1 = r23;
        r11.<init>(r0, r1);
        r22 = 1;
        r0 = r22;
        r11.debug = r0;	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = "";
        r0 = r22;
        r11.call(r0, r9);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r10 = r9.headerIn;	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        if (r10 == 0) goto L_0x00cc;
    L_0x008f:
        r0 = r10.length;	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = r0;
        if (r22 <= 0) goto L_0x00cc;
    L_0x0094:
        r0 = r10.length;	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r24 = r0;
        r22 = 0;
        r23 = r22;
    L_0x009b:
        r0 = r23;
        r1 = r24;
        if (r0 < r1) goto L_0x00a2;
    L_0x00a1:
        return r16;
    L_0x00a2:
        r8 = r10[r23];	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = 0;
        r0 = r22;
        r22 = r8.getChild(r0);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = (org.kxml2.kdom.Element) r22;	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r25 = 0;
        r0 = r22;
        r1 = r25;
        r22 = r0.getChild(r1);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r17 = r22.toString();	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = java.lang.Integer.parseInt(r17);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r0 = r16;
        r1 = r22;
        r0.setCode(r1);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = r23 + 1;
        r23 = r22;
        goto L_0x009b;
    L_0x00cc:
        r22 = r9.getResponse();	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        if (r22 == 0) goto L_0x00a1;
    L_0x00d2:
        r21 = r9.getResponse();	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r21 = (org.ksoap2.serialization.SoapObject) r21;	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = "code";
        r22 = r21.getPropertyAsString(r22);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        if (r22 != 0) goto L_0x00a1;
    L_0x00e8:
        r22 = "diagSoftIdList";
        r14 = r21.getProperty(r22);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r14 = (org.ksoap2.serialization.SoapObject) r14;	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = r14.getPropertyCount();	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        if (r22 <= 0) goto L_0x014c;
    L_0x00f6:
        r12 = 0;
        r6 = r5;
    L_0x00f8:
        r22 = r14.getPropertyCount();	 Catch:{ IOException -> 0x0172, XmlPullParserException -> 0x016f }
        r0 = r22;
        if (r12 < r0) goto L_0x011c;
    L_0x0100:
        r22 = "code";
        r22 = r21.getPropertyAsString(r22);	 Catch:{ IOException -> 0x0172, XmlPullParserException -> 0x016f }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0172, XmlPullParserException -> 0x016f }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0172, XmlPullParserException -> 0x016f }
        r0 = r16;
        r1 = r22;
        r0.setCode(r1);	 Catch:{ IOException -> 0x0172, XmlPullParserException -> 0x016f }
        r0 = r16;
        r0.setDiagSoftIdList(r3);	 Catch:{ IOException -> 0x0172, XmlPullParserException -> 0x016f }
        r5 = r6;
        goto L_0x00a1;
    L_0x011c:
        r4 = r14.getProperty(r12);	 Catch:{ IOException -> 0x0172, XmlPullParserException -> 0x016f }
        r4 = (org.ksoap2.serialization.SoapObject) r4;	 Catch:{ IOException -> 0x0172, XmlPullParserException -> 0x016f }
        r5 = new com.ifoer.entity.DiagSoftIdDTO;	 Catch:{ IOException -> 0x0172, XmlPullParserException -> 0x016f }
        r5.<init>();	 Catch:{ IOException -> 0x0172, XmlPullParserException -> 0x016f }
        r22 = "softId";
        r0 = r22;
        r22 = r4.getPrimitivePropertyAsString(r0);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r0 = r22;
        r5.setSoftId(r0);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = "softPackageId";
        r0 = r22;
        r22 = r4.getPrimitivePropertyAsString(r0);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r0 = r22;
        r5.setSoftPackageId(r0);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r3.add(r5);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r12 = r12 + 1;
        r6 = r5;
        goto L_0x00f8;
    L_0x014c:
        r22 = "code";
        r22 = r21.getPropertyAsString(r22);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        r0 = r16;
        r1 = r22;
        r0.setCode(r1);	 Catch:{ IOException -> 0x0163, XmlPullParserException -> 0x0169 }
        goto L_0x00a1;
    L_0x0163:
        r7 = move-exception;
    L_0x0164:
        r7.printStackTrace();
        goto L_0x00a1;
    L_0x0169:
        r7 = move-exception;
    L_0x016a:
        r7.printStackTrace();
        goto L_0x00a1;
    L_0x016f:
        r7 = move-exception;
        r5 = r6;
        goto L_0x016a;
    L_0x0172:
        r7 = move-exception;
        r5 = r6;
        goto L_0x0164;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.X431PadDiagSoftService.getDiagSoftIdList(java.lang.String):com.car.result.DiagSoftIdListResult");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.car.result.X431PadSoftListResult getDiagSoftInfoBySoftId(java.lang.String r27, java.lang.Integer r28, java.lang.Integer r29, java.lang.Integer r30, java.lang.String r31, java.lang.String r32) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r26 = this;
        r15 = new com.car.result.X431PadSoftListResult;
        r15.<init>();
        r6 = new java.util.ArrayList;
        r6.<init>();
        r4 = 0;
        r13 = "getDiagSoftInfoBySoftId";
        r18 = "x431PadDiagSoftService";
        r14 = new org.ksoap2.serialization.SoapObject;
        r22 = "http://www.x431.com";
        r0 = r22;
        r14.<init>(r0, r13);
        if (r27 == 0) goto L_0x0023;
    L_0x001a:
        r22 = "serialNo";
        r0 = r22;
        r1 = r27;
        r14.addProperty(r0, r1);
    L_0x0023:
        if (r28 == 0) goto L_0x0036;
    L_0x0025:
        r22 = "softId";
        r23 = r28.intValue();
        r23 = java.lang.Integer.valueOf(r23);
        r0 = r22;
        r1 = r23;
        r14.addProperty(r0, r1);
    L_0x0036:
        if (r29 == 0) goto L_0x0049;
    L_0x0038:
        r22 = "lanId";
        r23 = r29.intValue();
        r23 = java.lang.Integer.valueOf(r23);
        r0 = r22;
        r1 = r23;
        r14.addProperty(r0, r1);
    L_0x0049:
        if (r30 == 0) goto L_0x005c;
    L_0x004b:
        r22 = "defaultLanId";
        r23 = r30.intValue();
        r23 = java.lang.Integer.valueOf(r23);
        r0 = r22;
        r1 = r23;
        r14.addProperty(r0, r1);
    L_0x005c:
        r17 = new java.lang.StringBuffer;
        r17.<init>();
        r0 = r17;
        r1 = r27;
        r0.append(r1);
        r0 = r17;
        r1 = r28;
        r0.append(r1);
        r0 = r17;
        r1 = r29;
        r0.append(r1);
        r0 = r17;
        r1 = r30;
        r0.append(r1);
        r0 = r17;
        r1 = r32;
        r0.append(r1);
        r22 = r17.toString();
        r19 = com.ifoer.md5.MD5.getMD5Str(r22);
        r9 = new org.ksoap2.serialization.SoapSerializationEnvelope;
        r22 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r22;
        r9.<init>(r0);
        r0 = r26;
        r1 = r19;
        r2 = r31;
        r22 = r0.createHead(r1, r2);
        r0 = r22;
        r9.headerOut = r0;
        r9.bodyOut = r14;
        r9.setOutputSoapObject(r14);
        r11 = new com.ifoer.util.MyAndroidHttpTransport;
        r22 = new java.lang.StringBuilder;
        r23 = "http://mycar.x431.com/services/";
        r22.<init>(r23);
        r0 = r22;
        r1 = r18;
        r22 = r0.append(r1);
        r22 = r22.toString();
        r23 = timeout;
        r0 = r22;
        r1 = r23;
        r11.<init>(r0, r1);
        r22 = 1;
        r0 = r22;
        r11.debug = r0;	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = "";
        r0 = r22;
        r11.call(r0, r9);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r10 = r9.headerIn;	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        if (r10 == 0) goto L_0x0112;
    L_0x00d7:
        r0 = r10.length;	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = r0;
        if (r22 <= 0) goto L_0x0112;
    L_0x00dc:
        r0 = r10.length;	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r24 = r0;
        r22 = 0;
        r23 = r22;
    L_0x00e3:
        r0 = r23;
        r1 = r24;
        if (r0 < r1) goto L_0x00ea;
    L_0x00e9:
        return r15;
    L_0x00ea:
        r8 = r10[r23];	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = 0;
        r0 = r22;
        r22 = r8.getChild(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = (org.kxml2.kdom.Element) r22;	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r25 = 0;
        r0 = r22;
        r1 = r25;
        r22 = r0.getChild(r1);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r16 = r22.toString();	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = java.lang.Integer.parseInt(r16);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r15.setCode(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = r23 + 1;
        r23 = r22;
        goto L_0x00e3;
    L_0x0112:
        r22 = r9.getResponse();	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        if (r22 == 0) goto L_0x00e9;
    L_0x0118:
        r20 = r9.getResponse();	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r20 = (org.ksoap2.serialization.SoapObject) r20;	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = "code";
        r0 = r20;
        r1 = r22;
        r22 = r0.getPropertyAsString(r1);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        if (r22 != 0) goto L_0x0210;
    L_0x0132:
        r22 = "x431PadSoftList";
        r0 = r20;
        r1 = r22;
        r21 = r0.getProperty(r1);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r21 = (org.ksoap2.serialization.SoapObject) r21;	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r12 = 0;
        r5 = r4;
    L_0x0140:
        r22 = r21.getPropertyCount();	 Catch:{ IOException -> 0x0238, XmlPullParserException -> 0x0235 }
        r0 = r22;
        if (r12 < r0) goto L_0x0164;
    L_0x0148:
        r22 = "code";
        r0 = r20;
        r1 = r22;
        r22 = r0.getPropertyAsString(r1);	 Catch:{ IOException -> 0x0238, XmlPullParserException -> 0x0235 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0238, XmlPullParserException -> 0x0235 }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0238, XmlPullParserException -> 0x0235 }
        r0 = r22;
        r15.setCode(r0);	 Catch:{ IOException -> 0x0238, XmlPullParserException -> 0x0235 }
        r15.setDtoList(r6);	 Catch:{ IOException -> 0x0238, XmlPullParserException -> 0x0235 }
        r4 = r5;
        goto L_0x00e9;
    L_0x0164:
        r0 = r21;
        r3 = r0.getProperty(r12);	 Catch:{ IOException -> 0x0238, XmlPullParserException -> 0x0235 }
        r3 = (org.ksoap2.serialization.SoapObject) r3;	 Catch:{ IOException -> 0x0238, XmlPullParserException -> 0x0235 }
        r4 = new com.ifoer.entity.X431PadSoftDTO;	 Catch:{ IOException -> 0x0238, XmlPullParserException -> 0x0235 }
        r4.<init>();	 Catch:{ IOException -> 0x0238, XmlPullParserException -> 0x0235 }
        r22 = "diagVehicleType";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r4.setDiagVehicleType(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = "fileSize";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r4.setFileSize(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = "lanId";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r4.setLanId(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = "softApplicableArea";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r4.setSoftApplicableArea(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = "softId";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r4.setSoftId(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = "softName";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r4.setSoftName(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = "softPackageID";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r4.setSoftPackageID(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = "softUpdateTime";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r4.setSoftUpdateTime(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = "versionDetailId";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r4.setVersionDetailId(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = "versionNo";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r4.setVersionNo(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = "purchased";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r4.setPurchased(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r6.add(r4);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r12 = r12 + 1;
        r5 = r4;
        goto L_0x0140;
    L_0x0210:
        r22 = "code";
        r0 = r20;
        r1 = r22;
        r22 = r0.getPropertyAsString(r1);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        r0 = r22;
        r15.setCode(r0);	 Catch:{ IOException -> 0x0229, XmlPullParserException -> 0x022f }
        goto L_0x00e9;
    L_0x0229:
        r7 = move-exception;
    L_0x022a:
        r7.printStackTrace();
        goto L_0x00e9;
    L_0x022f:
        r7 = move-exception;
    L_0x0230:
        r7.printStackTrace();
        goto L_0x00e9;
    L_0x0235:
        r7 = move-exception;
        r4 = r5;
        goto L_0x0230;
    L_0x0238:
        r7 = move-exception;
        r4 = r5;
        goto L_0x022a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.X431PadDiagSoftService.getDiagSoftInfoBySoftId(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String):com.car.result.X431PadSoftListResult");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.ifoer.entity.DiagSoftLanListResult getDiagSoftLanList(int r27) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r26 = this;
        r13 = "getDiagSoftLanList";
        r18 = "diagSoftService";
        r16 = new com.ifoer.entity.DiagSoftLanListResult;
        r16.<init>();
        r3 = new java.util.ArrayList;
        r3.<init>();
        r5 = 0;
        r15 = new org.ksoap2.serialization.SoapObject;
        r22 = "http://www.x431.com";
        r0 = r22;
        r15.<init>(r0, r13);
        r22 = "versionDetailId";
        r23 = java.lang.Integer.valueOf(r27);
        r0 = r22;
        r1 = r23;
        r15.addProperty(r0, r1);
        r22 = new java.lang.StringBuilder;
        r23 = java.lang.String.valueOf(r27);
        r22.<init>(r23);
        r0 = r26;
        r0 = r0.token;
        r23 = r0;
        r22 = r22.append(r23);
        r20 = r22.toString();
        r19 = com.ifoer.md5.MD5.getMD5Str(r20);
        r9 = new org.ksoap2.serialization.SoapSerializationEnvelope;
        r22 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r22;
        r9.<init>(r0);
        r0 = r26;
        r0 = r0.cc;
        r22 = r0;
        r0 = r26;
        r1 = r19;
        r2 = r22;
        r22 = r0.createHead(r1, r2);
        r0 = r22;
        r9.headerOut = r0;
        r9.bodyOut = r15;
        r9.setOutputSoapObject(r15);
        r11 = new com.ifoer.util.MyAndroidHttpTransport;
        r22 = new java.lang.StringBuilder;
        r23 = "http://mycar.x431.com/services/";
        r22.<init>(r23);
        r0 = r22;
        r1 = r18;
        r22 = r0.append(r1);
        r22 = r22.toString();
        r23 = timeout;
        r0 = r22;
        r1 = r23;
        r11.<init>(r0, r1);
        r22 = 1;
        r0 = r22;
        r11.debug = r0;	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = "";
        r0 = r22;
        r11.call(r0, r9);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r10 = r9.headerIn;	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        if (r10 == 0) goto L_0x00ce;
    L_0x0091:
        r0 = r10.length;	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = r0;
        if (r22 <= 0) goto L_0x00ce;
    L_0x0096:
        r0 = r10.length;	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r24 = r0;
        r22 = 0;
        r23 = r22;
    L_0x009d:
        r0 = r23;
        r1 = r24;
        if (r0 < r1) goto L_0x00a4;
    L_0x00a3:
        return r16;
    L_0x00a4:
        r8 = r10[r23];	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = 0;
        r0 = r22;
        r22 = r8.getChild(r0);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = (org.kxml2.kdom.Element) r22;	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r25 = 0;
        r0 = r22;
        r1 = r25;
        r22 = r0.getChild(r1);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r17 = r22.toString();	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = java.lang.Integer.parseInt(r17);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r0 = r16;
        r1 = r22;
        r0.setCode(r1);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = r23 + 1;
        r23 = r22;
        goto L_0x009d;
    L_0x00ce:
        r22 = r9.getResponse();	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        if (r22 == 0) goto L_0x00a3;
    L_0x00d4:
        r21 = r9.getResponse();	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r21 = (org.ksoap2.serialization.SoapObject) r21;	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = "code";
        r22 = r21.getPropertyAsString(r22);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        if (r22 != 0) goto L_0x00a3;
    L_0x00ea:
        r22 = "lanList";
        r14 = r21.getProperty(r22);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r14 = (org.ksoap2.serialization.SoapObject) r14;	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = r14.getPropertyCount();	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        if (r22 <= 0) goto L_0x0152;
    L_0x00f8:
        r12 = 0;
        r6 = r5;
    L_0x00fa:
        r22 = r14.getPropertyCount();	 Catch:{ IOException -> 0x0178, XmlPullParserException -> 0x0175 }
        r0 = r22;
        if (r12 < r0) goto L_0x011e;
    L_0x0102:
        r22 = "code";
        r22 = r21.getPropertyAsString(r22);	 Catch:{ IOException -> 0x0178, XmlPullParserException -> 0x0175 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0178, XmlPullParserException -> 0x0175 }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0178, XmlPullParserException -> 0x0175 }
        r0 = r16;
        r1 = r22;
        r0.setCode(r1);	 Catch:{ IOException -> 0x0178, XmlPullParserException -> 0x0175 }
        r0 = r16;
        r0.setLanList(r3);	 Catch:{ IOException -> 0x0178, XmlPullParserException -> 0x0175 }
        r5 = r6;
        goto L_0x00a3;
    L_0x011e:
        r4 = r14.getProperty(r12);	 Catch:{ IOException -> 0x0178, XmlPullParserException -> 0x0175 }
        r4 = (org.ksoap2.serialization.SoapObject) r4;	 Catch:{ IOException -> 0x0178, XmlPullParserException -> 0x0175 }
        r5 = new com.ifoer.entity.DiagSoftLanDTO;	 Catch:{ IOException -> 0x0178, XmlPullParserException -> 0x0175 }
        r5.<init>();	 Catch:{ IOException -> 0x0178, XmlPullParserException -> 0x0175 }
        r22 = "lanId";
        r0 = r22;
        r22 = r4.getPrimitivePropertyAsString(r0);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r0 = r22;
        r5.setLanId(r0);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = "lanCode";
        r0 = r22;
        r22 = r4.getPrimitivePropertyAsString(r0);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r0 = r22;
        r5.setLanCode(r0);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r3.add(r5);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r12 = r12 + 1;
        r6 = r5;
        goto L_0x00fa;
    L_0x0152:
        r22 = "code";
        r22 = r21.getPropertyAsString(r22);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        r0 = r16;
        r1 = r22;
        r0.setCode(r1);	 Catch:{ IOException -> 0x0169, XmlPullParserException -> 0x016f }
        goto L_0x00a3;
    L_0x0169:
        r7 = move-exception;
    L_0x016a:
        r7.printStackTrace();
        goto L_0x00a3;
    L_0x016f:
        r7 = move-exception;
    L_0x0170:
        r7.printStackTrace();
        goto L_0x00a3;
    L_0x0175:
        r7 = move-exception;
        r5 = r6;
        goto L_0x0170;
    L_0x0178:
        r7 = move-exception;
        r5 = r6;
        goto L_0x016a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.X431PadDiagSoftService.getDiagSoftLanList(int):com.ifoer.entity.DiagSoftLanListResult");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.car.result.X431PadSoftListResult queryHistoryDiagSofts(java.lang.String r27, java.lang.String r28, java.lang.String r29, java.lang.Integer r30, java.lang.Integer r31, java.lang.Integer r32) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r26 = this;
        r15 = new com.car.result.X431PadSoftListResult;
        r15.<init>();
        r6 = new java.util.ArrayList;
        r6.<init>();
        r4 = 0;
        r13 = "queryHistoryDiagSofts";
        r17 = "x431PadDiagSoftService";
        r14 = new org.ksoap2.serialization.SoapObject;
        r22 = "http://www.x431.com";
        r0 = r22;
        r14.<init>(r0, r13);
        if (r29 == 0) goto L_0x0023;
    L_0x001a:
        r22 = "serialNo";
        r0 = r22;
        r1 = r29;
        r14.addProperty(r0, r1);
    L_0x0023:
        if (r30 == 0) goto L_0x002e;
    L_0x0025:
        r22 = "softId";
        r0 = r22;
        r1 = r30;
        r14.addProperty(r0, r1);
    L_0x002e:
        if (r31 == 0) goto L_0x0039;
    L_0x0030:
        r22 = "lanId";
        r0 = r22;
        r1 = r31;
        r14.addProperty(r0, r1);
    L_0x0039:
        if (r32 == 0) goto L_0x0044;
    L_0x003b:
        r22 = "defaultLanId";
        r0 = r22;
        r1 = r32;
        r14.addProperty(r0, r1);
    L_0x0044:
        r22 = new java.lang.StringBuilder;
        r23 = java.lang.String.valueOf(r29);
        r22.<init>(r23);
        r0 = r22;
        r1 = r30;
        r22 = r0.append(r1);
        r0 = r22;
        r1 = r31;
        r22 = r0.append(r1);
        r0 = r22;
        r1 = r32;
        r22 = r0.append(r1);
        r0 = r22;
        r1 = r28;
        r22 = r0.append(r1);
        r19 = r22.toString();
        r18 = com.ifoer.md5.MD5.getMD5Str(r19);
        r9 = new org.ksoap2.serialization.SoapSerializationEnvelope;
        r22 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r22;
        r9.<init>(r0);
        r0 = r26;
        r1 = r18;
        r2 = r27;
        r22 = r0.createHead(r1, r2);
        r0 = r22;
        r9.headerOut = r0;
        r9.bodyOut = r14;
        r9.setOutputSoapObject(r14);
        r11 = new com.ifoer.util.MyAndroidHttpTransport;	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r23 = "http://mycar.x431.com/services/";
        r22.<init>(r23);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r0 = r22;
        r1 = r17;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = r22.toString();	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r23 = timeout;	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r0 = r22;
        r1 = r23;
        r11.<init>(r0, r1);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = 1;
        r0 = r22;
        r11.debug = r0;	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = "";
        r0 = r22;
        r11.call(r0, r9);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r10 = r9.headerIn;	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        if (r10 == 0) goto L_0x00fb;
    L_0x00c0:
        r0 = r10.length;	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = r0;
        if (r22 <= 0) goto L_0x00fb;
    L_0x00c5:
        r0 = r10.length;	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r24 = r0;
        r22 = 0;
        r23 = r22;
    L_0x00cc:
        r0 = r23;
        r1 = r24;
        if (r0 < r1) goto L_0x00d3;
    L_0x00d2:
        return r15;
    L_0x00d3:
        r8 = r10[r23];	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = 0;
        r0 = r22;
        r22 = r8.getChild(r0);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = (org.kxml2.kdom.Element) r22;	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r25 = 0;
        r0 = r22;
        r1 = r25;
        r22 = r0.getChild(r1);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r16 = r22.toString();	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = java.lang.Integer.parseInt(r16);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r0 = r22;
        r15.setCode(r0);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = r23 + 1;
        r23 = r22;
        goto L_0x00cc;
    L_0x00fb:
        r22 = r9.getResponse();	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        if (r22 == 0) goto L_0x00d2;
    L_0x0101:
        r20 = r9.getResponse();	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r20 = (org.ksoap2.serialization.SoapObject) r20;	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = "code";
        r0 = r20;
        r1 = r22;
        r22 = r0.getPropertyAsString(r1);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        if (r22 != 0) goto L_0x0195;
    L_0x011b:
        r22 = "x431PadSoftList";
        r0 = r20;
        r1 = r22;
        r21 = r0.getProperty(r1);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r21 = (org.ksoap2.serialization.SoapObject) r21;	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r12 = 0;
        r5 = r4;
    L_0x0129:
        r22 = r21.getPropertyCount();	 Catch:{ IOException -> 0x01bd, XmlPullParserException -> 0x01ba }
        r0 = r22;
        if (r12 < r0) goto L_0x014d;
    L_0x0131:
        r22 = "code";
        r0 = r20;
        r1 = r22;
        r22 = r0.getPropertyAsString(r1);	 Catch:{ IOException -> 0x01bd, XmlPullParserException -> 0x01ba }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x01bd, XmlPullParserException -> 0x01ba }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x01bd, XmlPullParserException -> 0x01ba }
        r0 = r22;
        r15.setCode(r0);	 Catch:{ IOException -> 0x01bd, XmlPullParserException -> 0x01ba }
        r15.setDtoList(r6);	 Catch:{ IOException -> 0x01bd, XmlPullParserException -> 0x01ba }
        r4 = r5;
        goto L_0x00d2;
    L_0x014d:
        r0 = r21;
        r3 = r0.getProperty(r12);	 Catch:{ IOException -> 0x01bd, XmlPullParserException -> 0x01ba }
        r3 = (org.ksoap2.serialization.SoapObject) r3;	 Catch:{ IOException -> 0x01bd, XmlPullParserException -> 0x01ba }
        r4 = new com.ifoer.entity.X431PadSoftDTO;	 Catch:{ IOException -> 0x01bd, XmlPullParserException -> 0x01ba }
        r4.<init>();	 Catch:{ IOException -> 0x01bd, XmlPullParserException -> 0x01ba }
        r22 = "softId";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r0 = r22;
        r4.setSoftId(r0);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = "softName";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r0 = r22;
        r4.setSoftName(r0);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = "versionDetailId";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r0 = r22;
        r4.setVersionDetailId(r0);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = "versionNo";
        r0 = r22;
        r22 = r3.getPropertyAsString(r0);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r0 = r22;
        r4.setVersionNo(r0);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r6.add(r4);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r12 = r12 + 1;
        r5 = r4;
        goto L_0x0129;
    L_0x0195:
        r22 = "code";
        r0 = r20;
        r1 = r22;
        r22 = r0.getPropertyAsString(r1);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r22 = r22.intValue();	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        r0 = r22;
        r15.setCode(r0);	 Catch:{ IOException -> 0x01ae, XmlPullParserException -> 0x01b4 }
        goto L_0x00d2;
    L_0x01ae:
        r7 = move-exception;
    L_0x01af:
        r7.printStackTrace();
        goto L_0x00d2;
    L_0x01b4:
        r7 = move-exception;
    L_0x01b5:
        r7.printStackTrace();
        goto L_0x00d2;
    L_0x01ba:
        r7 = move-exception;
        r4 = r5;
        goto L_0x01b5;
    L_0x01bd:
        r7 = move-exception;
        r4 = r5;
        goto L_0x01af;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.X431PadDiagSoftService.queryHistoryDiagSofts(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer):com.car.result.X431PadSoftListResult");
    }

    public X431PadSoftListResult getSpecifiedDiagSoftLatestInfo(String serialNo, String diagSoftId, Integer lanId, Integer defaultLanId) throws NullPointerException, SocketTimeoutException {
        IOException e;
        XmlPullParserException e2;
        String serviceName = "x431PadDiagSoftService";
        SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "getSpecifiedDiagSoftLatestInfo");
        X431PadSoftListResult result = null;
        List<X431PadSoftDTO> dtoList = new ArrayList();
        if (serialNo != null) {
            request.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, serialNo);
        }
        if (diagSoftId != null) {
            request.addProperty("diagSoftId", diagSoftId);
        }
        if (lanId != null) {
            request.addProperty("lanId", lanId);
        }
        if (defaultLanId != null) {
            request.addProperty("defaultLanId", defaultLanId);
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(serialNo).append(diagSoftId).append(lanId).append(defaultLanId);
        String sgin = MD5.getMD5Str(buffer.toString() + this.token);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.headerOut = createHead(sgin, this.cc);
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
        try {
            ht.debug = true;
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() == null) {
                return null;
            }
            X431PadSoftListResult result2 = new X431PadSoftListResult();
            try {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                result2.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() != 0) {
                    return result2;
                }
                SoapObject softList = (SoapObject) soapObject.getProperty("x431PadSoftList");
                for (int i = 0; i < softList.getPropertyCount(); i++) {
                    SoapObject softInfo = (SoapObject) softList.getProperty(i);
                    X431PadSoftDTO padSoftDto = new X431PadSoftDTO();
                    padSoftDto.setDiagVehicleType(softInfo.getPropertyAsString("diagVehicleType"));
                    padSoftDto.setLanId(softInfo.getPropertyAsString("lanId"));
                    padSoftDto.setSoftApplicableArea(softInfo.getPropertyAsString("softApplicableArea"));
                    padSoftDto.setSoftId(softInfo.getPropertyAsString("softId"));
                    padSoftDto.setSoftName(softInfo.getPropertyAsString("softName"));
                    padSoftDto.setSoftPackageID(softInfo.getPropertyAsString("softPackageID"));
                    padSoftDto.setVersionDetailId(softInfo.getPropertyAsString("versionDetailId"));
                    padSoftDto.setVersionNo(softInfo.getPropertyAsString("versionNo"));
                    dtoList.add(padSoftDto);
                }
                result2.setDtoList(dtoList);
                return result2;
            } catch (IOException e3) {
                e = e3;
                result = result2;
                e.printStackTrace();
                return result;
            } catch (XmlPullParserException e4) {
                e2 = e4;
                result = result2;
                e2.printStackTrace();
                return result;
            }
        } catch (IOException e5) {
            e = e5;
            e.printStackTrace();
            return result;
        } catch (XmlPullParserException e6) {
            e2 = e6;
            e2.printStackTrace();
            return result;
        }
    }

    public X431PadDtoSoft getSpecifiedDiagSoftLatestInfo1(String serialNo, String diagSoftId, Integer lanId, Integer defaultLanId, Handler handler) throws NullPointerException, SocketTimeoutException {
        IOException e;
        XmlPullParserException e2;
        String serviceName = "x431PadDiagSoftService";
        SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "getSpecifiedDiagSoftLatestInfo");
        List<X431PadDtoSoft> dtoList = new ArrayList();
        X431PadDtoSoft padSoftDto = null;
        if (serialNo != null) {
            request.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, serialNo);
        }
        if (diagSoftId != null) {
            request.addProperty("diagSoftId", diagSoftId);
        }
        if (lanId != null) {
            request.addProperty("lanId", lanId);
        }
        if (defaultLanId != null) {
            request.addProperty("defaultLanId", defaultLanId);
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(serialNo).append(diagSoftId).append(lanId).append(defaultLanId);
        String sgin = MD5.getMD5Str(buffer.toString() + this.token);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.headerOut = createHead(sgin, this.cc);
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
        try {
            ht.debug = true;
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() == null) {
                return null;
            }
            X431PadDtoSoft padSoftDto2 = new X431PadDtoSoft();
            try {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                padSoftDto2.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 500) {
                    handler.sendEmptyMessage(6);
                }
                if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() != 0) {
                    return padSoftDto2;
                }
                SoapObject softList = (SoapObject) soapObject.getProperty("x431PadSoftList");
                for (int i = 0; i < softList.getPropertyCount(); i++) {
                    SoapObject softInfo = (SoapObject) softList.getProperty(i);
                    padSoftDto2.setDiagVehicleType(softInfo.getPropertyAsString("diagVehicleType"));
                    padSoftDto2.setLanId(softInfo.getPropertyAsString("lanId"));
                    padSoftDto2.setSoftApplicableArea(softInfo.getPropertyAsString("softApplicableArea"));
                    padSoftDto2.setSoftId(softInfo.getPropertyAsString("softId"));
                    padSoftDto2.setSoftName(softInfo.getPropertyAsString("softName"));
                    padSoftDto2.setSoftPackageID(softInfo.getPropertyAsString("softPackageID"));
                    padSoftDto2.setVersionDetailId(softInfo.getPropertyAsString("versionDetailId"));
                    padSoftDto2.setVersionNo(softInfo.getPropertyAsString("versionNo"));
                }
                return padSoftDto2;
            } catch (IOException e3) {
                e = e3;
                padSoftDto = padSoftDto2;
                e.printStackTrace();
                return padSoftDto;
            } catch (XmlPullParserException e4) {
                e2 = e4;
                padSoftDto = padSoftDto2;
                e2.printStackTrace();
                return padSoftDto;
            }
        } catch (IOException e5) {
            e = e5;
            e.printStackTrace();
            return padSoftDto;
        } catch (XmlPullParserException e6) {
            e2 = e6;
            e2.printStackTrace();
            return padSoftDto;
        }
    }
}
