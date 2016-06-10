package com.ifoer.webservice;

import android.text.TextUtils;
import com.car.result.WSResult;
import com.cnlaunch.framework.common.Constants;
import com.ifoer.entity.ConcernProductResult;
import com.ifoer.entity.ConcernedProductDTO;
import com.ifoer.entity.ConcernedProductInfoResult;
import com.ifoer.entity.ConcernedProductListResult;
import com.ifoer.entity.Constant;
import com.ifoer.entity.CustomerDTO;
import com.ifoer.entity.DrivingInfoResult;
import com.ifoer.entity.PagingHelper;
import com.ifoer.entity.PushMessageContentResult;
import com.ifoer.entity.PushMessageCountResult;
import com.ifoer.entity.PushMessageDTO;
import com.ifoer.entity.PushMessageListResult;
import com.ifoer.entity.RCUDataDTO;
import com.ifoer.md5.MD5;
import com.ifoer.mine.Contact;
import com.ifoer.util.MyAndroidHttpTransport;
import com.ifoer.util.MyHttpException;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.achartengine.ChartFactory;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

public class RegisteredProduct {
    private static final String TAG = "RegisteredProduct";
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

    public ConcernProductResult concernProduct(String cc, String serialNo, String password, String mobile, String customerName, String nextMaintanceMileage) throws NullPointerException, SocketTimeoutException {
        String methodName = "concernProduct";
        String serviceName = "concernProductService";
        ConcernProductResult result = new ConcernProductResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (cc != null) {
                request.addProperty(MultipleAddresses.CC, cc);
            }
            if (serialNo != null) {
                request.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, serialNo);
            }
            if (password != null) {
                request.addProperty("password", password);
            }
            if (mobile != null) {
                request.addProperty("mobile", mobile);
            }
            if (customerName != null) {
                request.addProperty("customerName", customerName);
            }
            if (nextMaintanceMileage != null) {
                request.addProperty("nextMaintanceMileage", nextMaintanceMileage);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(cc).append(serialNo).append(password).append(mobile).append(customerName).append(nextMaintanceMileage).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    result.setCode(((SoapObject) envelope.getResponse()).getPropertyAsString("code"));
                }
                return result;
            }
            for (Element element : headerIn) {
                result.setCode(((Element) element.getChild(0)).getChild(0).toString());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public ConcernedProductListResult getConcernedProductList(String CC) throws NullPointerException, SocketTimeoutException {
        String methodName = "getConcernedProductList";
        String serviceName = "concernProductService";
        ConcernedProductListResult result = new ConcernedProductListResult();
        List<ConcernedProductDTO> dtoList = new ArrayList();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (this.cc != null) {
                request.addProperty(MultipleAddresses.CC, this.cc);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(this.cc).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope, null);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                        result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                        SoapObject list = (SoapObject) soapObject.getProperty("concernedProductList");
                        if (list.getPropertyCount() > 0) {
                            for (int i = 0; i < list.getPropertyCount(); i++) {
                                ConcernedProductDTO concernedProductDTO = new ConcernedProductDTO();
                                SoapObject item = (SoapObject) list.getProperty(i);
                                concernedProductDTO.setSerialNo(item.getPropertyAsString(com.cnlaunch.x431pro.common.Constants.serialNo));
                                concernedProductDTO.setUnResolvedMessageCount(item.getPropertyAsString("unResolvedMessageCount"));
                                dtoList.add(concernedProductDTO);
                            }
                        }
                        result.setList(dtoList);
                    } else {
                        result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
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

    public ConcernedProductInfoResult getConcernedProductSNInfo(String CC, String concernedSN, String displayLan) throws NullPointerException, SocketTimeoutException {
        String methodName = "getConcernedProductSNInfo";
        String serviceName = "concernProductService";
        ConcernedProductInfoResult result = new ConcernedProductInfoResult();
        List<Map<String, String>> downloadFunMap = new ArrayList();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (CC != null) {
                request.addProperty("CC", CC);
            }
            if (concernedSN != null) {
                request.addProperty("concernedSN", concernedSN);
            }
            if (displayLan != null) {
                request.addProperty("displayLan", displayLan);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(CC).append(concernedSN).append(displayLan).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
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
                        String isConfigured = soapObject.getPropertyAsString("isConfigured");
                        if (isConfigured.equals(Contact.RELATION_ASK)) {
                            result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                            result.setSeriaNo(soapObject.getPropertyAsString(com.cnlaunch.x431pro.common.Constants.serialNo));
                            result.setIsConfigured(isConfigured);
                            result.setCustomerName(soapObject.getPropertyAsString("customerName").toString());
                            result.setCustomerMobile(soapObject.getPropertyAsString("customerMobile"));
                            try {
                                result.setCurrentMileage(soapObject.getPropertyAsString("currentMileage"));
                            } catch (RuntimeException e) {
                                result.setCurrentMileage(WEBSERVICE_SOAPACION);
                            }
                            result.setNextMaintanceMileage(soapObject.getPropertyAsString("nextMaintanceMileage"));
                            result.setConcernBeginTime(soapObject.getPropertyAsString("concernBeginTime"));
                        } else {
                            result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                            result.setSeriaNo(soapObject.getPropertyAsString(com.cnlaunch.x431pro.common.Constants.serialNo));
                            result.setIsConfigured(isConfigured);
                            result.setCustomerName(soapObject.getPropertyAsString("customerName").toString());
                            result.setCustomerMobile(soapObject.getPropertyAsString("customerMobile"));
                            result.setNextMaintanceMileage(soapObject.getPropertyAsString("nextMaintanceMileage"));
                            try {
                                result.setCurrentMileage(soapObject.getPropertyAsString("currentMileage"));
                            } catch (RuntimeException e2) {
                                result.setCurrentMileage(WEBSERVICE_SOAPACION);
                            }
                            result.setCarBrandId(soapObject.getPropertyAsString("carBrandId"));
                            result.setCarBrandName(soapObject.getPropertyAsString("carBrandName"));
                            result.setConcernBeginTime(soapObject.getPropertyAsString("concernBeginTime"));
                            result.setVIN(soapObject.getPropertyAsString("VIN"));
                            SoapObject dfm = (SoapObject) soapObject.getProperty("downloadFunMap");
                            if (dfm.getPropertyCount() > 0) {
                                for (int i = 0; i < dfm.getPropertyCount(); i++) {
                                    Map<String, String> map = new HashMap();
                                    SoapObject item = (SoapObject) dfm.getProperty(i);
                                    map.put(SharedPref.KEY, item.getPropertyAsString(SharedPref.KEY));
                                    map.put(SharedPref.VALUE, item.getPropertyAsString(SharedPref.VALUE));
                                    downloadFunMap.add(map);
                                }
                            }
                            result.setDownloadFunMap(downloadFunMap);
                        }
                    } else {
                        result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    }
                }
                return result;
            }
            for (Element element : headerIn) {
                result.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return result;
        } catch (IOException e3) {
            e3.printStackTrace();
        } catch (XmlPullParserException e4) {
            e4.printStackTrace();
        }
    }

    public WSResult updateCustomerInfo(String CC, String concernedSN, CustomerDTO dto) throws NullPointerException, SocketTimeoutException {
        String methodName = "updateCustomerInfo";
        String serviceName = "concernProductService";
        WSResult result = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (CC != null) {
                request.addProperty("CC", CC);
            }
            if (concernedSN != null) {
                request.addProperty("concernedSN", concernedSN);
            }
            SoapObject customerDTO = new SoapObject();
            customerDTO.addProperty("customerName", dto.getCustomerName());
            customerDTO.addProperty("customerMobile", dto.getCustomerMobile());
            request.addProperty("customerDTO", customerDTO);
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString());
            StringBuffer sb = new StringBuffer();
            sb.append(CC).append(concernedSN).append(dto.getCustomerName()).append(dto.getCustomerMobile()).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
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
                        result.setCode(Integer.parseInt(soapObject.getPropertyAsString("code")));
                    } else {
                        result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
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

    public WSResult updateMaintanceMillage(String CC, String concernedSN, String nextMaintanceMillage) throws NullPointerException, SocketTimeoutException {
        String methodName = "updateMaintanceMillage";
        String serviceName = "concernProductService";
        WSResult result = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (CC != null) {
                request.addProperty("CC", CC);
            }
            if (concernedSN != null) {
                request.addProperty("concernedSN", concernedSN);
            }
            if (nextMaintanceMillage != null) {
                request.addProperty("nextMaintanceMillage", nextMaintanceMillage);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(CC).append(concernedSN).append(nextMaintanceMillage).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
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
                        result.setCode(Integer.parseInt(soapObject.getPropertyAsString("code")));
                    } else {
                        result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
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

    public PushMessageCountResult getPushMessageListCount(String concernedSN, Integer messageType) {
        String methodName = "getPushMessageListCount";
        String serviceName = "iosPushService";
        PushMessageCountResult result = new PushMessageCountResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (concernedSN != null) {
                request.addProperty("concernedSN", concernedSN);
            }
            if (messageType != null) {
                request.addProperty("messageType", messageType);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(concernedSN).append(messageType).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                    result.setCode(Integer.parseInt(soapObject.getPropertyAsString("code")));
                    result.setAmount(Integer.valueOf(Integer.parseInt(soapObject.getPropertyAsString("amount"))));
                    result.setResolvedCount(Integer.valueOf(Integer.parseInt(soapObject.getPropertyAsString("resolvedCount"))));
                    result.setUnResolvedCount(Integer.valueOf(Integer.parseInt(soapObject.getPropertyAsString("unResolvedCount"))));
                } else {
                    result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return result;
    }

    public PushMessageListResult getPushMessageList(String concernedSN, Integer messageType, int pageNo, int pageSize) throws NullPointerException, SocketTimeoutException {
        String methodName = "getPushMessageList";
        String serviceName = "iosPushService";
        PushMessageListResult result = new PushMessageListResult();
        PagingHelper pageHelper = new PagingHelper();
        List<PushMessageDTO> dtoList = new ArrayList();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (concernedSN != null) {
                request.addProperty("concernedSN", concernedSN);
            }
            if (messageType != null) {
                request.addProperty("messageType", messageType);
            }
            if (pageNo != 0) {
                request.addProperty("pageNo", Integer.valueOf(pageNo));
            }
            if (pageSize != 0) {
                request.addProperty("pageSize", Integer.valueOf(pageSize));
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(concernedSN).append(messageType).append(pageNo).append(pageSize).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
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
                        result.setCode(Integer.parseInt(soapObject.getPropertyAsString("code")));
                        SoapObject pHelper = (SoapObject) soapObject.getProperty("pagingHelper");
                        pageHelper.setSize(Integer.parseInt(pHelper.getPropertyAsString("size")));
                        pageHelper.setPageNo(Integer.parseInt(pHelper.getPropertyAsString("pageNo")));
                        pageHelper.setPagesize(Integer.parseInt(pHelper.getPropertyAsString("pageSize")));
                        SoapObject dataList = (SoapObject) pHelper.getProperty("dataList");
                        if (dataList.getPropertyCount() > 0) {
                            for (int i = 0; i < dataList.getPropertyCount(); i++) {
                                PushMessageDTO dto = new PushMessageDTO();
                                SoapObject message = (SoapObject) dataList.getProperty(i);
                                dto.setMessageId(message.getPropertyAsString("messageId"));
                                dto.setMessageDesc(message.getPropertyAsString("messageDesc"));
                                dto.setPushTime(message.getPropertyAsString("pushTime"));
                                dto.setResolvedFlag(message.getPropertyAsString("resolvedFlag"));
                                dto.setConcernedSN(message.getPropertyAsString("concernedSN"));
                                dtoList.add(dto);
                            }
                        }
                        pageHelper.setDataList(dtoList);
                        result.setPagingHelper(pageHelper);
                    } else {
                        result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
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

    public PushMessageContentResult getPushMessageDetailContent(Integer messageId) throws NullPointerException, SocketTimeoutException {
        String methodName = "getPushMessageDetailContent";
        String serviceName = "iosPushService";
        PushMessageContentResult result = new PushMessageContentResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (messageId != null) {
                request.addProperty("messageId", messageId);
                MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
                StringBuffer sb = new StringBuffer();
                sb.append(messageId).append(this.token);
                String sgin = MD5.getMD5Str(sb.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.headerOut = createHead(sgin);
                envelope.bodyOut = request;
                envelope.setOutputSoapObject(request);
                ht.call(WEBSERVICE_SOAPACION, envelope);
                Element[] headerIn = envelope.headerIn;
                if (headerIn != null && headerIn.length > 0) {
                    for (Element element : headerIn) {
                        result.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
                    }
                } else if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                        result.setCode(Integer.parseInt(soapObject.getPropertyAsString("code")));
                        result.setDetailContent(soapObject.getPropertyAsString("detailContent"));
                    } else {
                        result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return result;
    }

    public WSResult setFaultResolvedFlag(Integer messageId, Integer flag) throws NullPointerException, SocketTimeoutException {
        String methodName = "setFaultResolvedFlag";
        String serviceName = "iosPushService";
        WSResult result = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (messageId != null) {
                request.addProperty("messageId", messageId);
            }
            if (flag != null) {
                request.addProperty("flag", flag);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(messageId).append(flag).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
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
                        result.setCode(Integer.parseInt(soapObject.getPropertyAsString("code")));
                    } else {
                        result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
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

    public WSResult pushMessageForSelectedUser(String serialNoList, Integer messageType, String title, String simpleContent) {
        String methodName = "pushMessageForSelectedUser";
        String serviceName = "iosPushService";
        WSResult result = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (!TextUtils.isEmpty(serialNoList)) {
                request.addProperty("serialNoList", serialNoList);
            }
            if (messageType.intValue() == 4) {
                request.addProperty("messageType", messageType);
            }
            if (!TextUtils.isEmpty(title)) {
                request.addProperty(ChartFactory.TITLE, title);
            }
            if (!TextUtils.isEmpty(simpleContent)) {
                request.addProperty("simpleContent", simpleContent);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(serialNoList).append(messageType).append(title).append(simpleContent).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (envelope.getResponse() != null) {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                    result.setCode(Integer.parseInt(soapObject.getPropertyAsString("code")));
                } else {
                    result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                }
            } else {
                result.setCode(MyHttpException.ERROR_SERVER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return result;
    }

    public WSResult pushMessageForAndroid(Integer cc, Integer messageType, String title, String content) throws NullPointerException, SocketTimeoutException {
        String methodName = "pushMessageForAndroid";
        String serviceName = "iosPushService";
        WSResult result = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (cc != null) {
                request.addProperty(MultipleAddresses.CC, cc);
            }
            if (messageType != null) {
                request.addProperty("messageType", messageType);
            }
            if (title != null) {
                request.addProperty(ChartFactory.TITLE, title);
            }
            if (content != null) {
                request.addProperty("content", content);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(cc).append(messageType).append(title).append(content).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
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
                        result.setCode(Integer.parseInt(soapObject.getPropertyAsString("code")));
                    } else {
                        result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
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

    public DrivingInfoResult getCurrentDrivingInfo(String serialNo) throws NullPointerException, SocketTimeoutException {
        String methodName = "getUploadedDFDataList";
        String serviceName = "drivingInfoService";
        DrivingInfoResult result = new DrivingInfoResult();
        List<RCUDataDTO> dtoList = new ArrayList();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (serialNo != null) {
                request.addProperty("productSerialNo", serialNo);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(serialNo).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
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
                        result.setCode(Integer.parseInt(soapObject.getPropertyAsString("code")));
                        if (soapObject.hasProperty("dataList")) {
                            SoapObject dataList = (SoapObject) soapObject.getProperty("dataList");
                            if (dataList.getPropertyCount() > 0) {
                                for (int i = 0; i < dataList.getPropertyCount(); i++) {
                                    RCUDataDTO dto = new RCUDataDTO();
                                    SoapObject message = (SoapObject) dataList.getProperty(i);
                                    dto.setDataId(message.getPropertyAsString("dataId"));
                                    dto.setDataName(message.getPropertyAsString("dataName"));
                                    dto.setDataUnitId(message.getPropertyAsString("dataUnitId"));
                                    dto.setDataUnitName(message.getPropertyAsString("dataUnitName"));
                                    dto.setDataValue(message.getPropertyAsString("dataValue"));
                                    dtoList.add(dto);
                                }
                            }
                            result.setDataList(dtoList);
                        }
                    } else {
                        result.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
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
}
