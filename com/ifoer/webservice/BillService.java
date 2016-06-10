package com.ifoer.webservice;

import com.car.result.WSResult;
import com.cnlaunch.framework.common.Constants;
import com.ifoer.entity.Constant;
import com.ifoer.entity.NormalBillInfoDTO;
import com.ifoer.entity.NormalBillInfoResult;
import com.ifoer.entity.OrderBillPostInfoResult;
import com.ifoer.entity.PostAddressInfoDTO;
import com.ifoer.entity.PostAddressInfoResult;
import com.ifoer.entity.SetNormalBillInfo;
import com.ifoer.entity.ValueAddedTaxBillDTO;
import com.ifoer.entity.ValueAddedTaxBillInfoDTO;
import com.ifoer.entity.ValueAddedTaxBillInfoResult;
import com.ifoer.md5.MD5;
import com.ifoer.util.MyAndroidHttpTransport;
import com.thoughtworks.xstream.XStream;
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

public class BillService {
    private static final String WEBSERVICE_NAMESPACE = "http://www.x431.com";
    private static final String WEBSERVICE_SOAPACION = "";
    private static final String WEBSERVICE_URL = "http://mycar.x431.com/services/";
    public static int timeout;
    private boolean f1316D;
    private String cc;
    private String token;

    public BillService() {
        this.f1316D = false;
    }

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

    public SetNormalBillInfo setNormalBillInfo(String serailNo, Integer billType, String invoiceTitle, String billContent) throws NullPointerException, SocketTimeoutException {
        SetNormalBillInfo setNormalBillInfo = new SetNormalBillInfo();
        String serviceName = "billService";
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, "setNormalBillInfo");
            if (serailNo != null) {
                request.addProperty("serailNo", serailNo);
            }
            if (billType != null) {
                request.addProperty("billType", billType);
            }
            if (invoiceTitle != null) {
                request.addProperty("invoiceTitle", invoiceTitle);
            }
            if (billContent != null) {
                request.addProperty("billContent", billContent);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(serailNo).append(billType).append(invoiceTitle).append(billContent).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    if (this.f1316D) {
                        System.out.println("setNormalBillInfo==" + envelope.getResponse().toString());
                    }
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                        setNormalBillInfo.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    } else {
                        setNormalBillInfo.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    }
                }
                return setNormalBillInfo;
            }
            for (Element element : headerIn) {
                setNormalBillInfo.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return setNormalBillInfo;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public SetNormalBillInfo setValueAddedTaxBillInfo(String serailNo, ValueAddedTaxBillDTO info) throws NullPointerException, SocketTimeoutException {
        String methodName = "setValueAddedTaxBillInfo";
        String serviceName = "billService";
        SetNormalBillInfo bInfo = new SetNormalBillInfo();
        try {
            SoapObject soapObject = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (serailNo != null) {
                soapObject.addProperty("serailNo", serailNo);
            }
            String billType = new StringBuilder(String.valueOf(info.getBillType())).toString();
            String invoiceTitle = info.getInvoiceTtile();
            String billContent = info.getBillContent();
            String taxCode = info.getTaxCode();
            String regAddress = info.getRegAddress();
            String regTelephone = info.getRegTelephone();
            String bankName = info.getBankName();
            String bankAccountNum = info.getBankAccountNum();
            SoapObject billInfo = new SoapObject();
            billInfo.addProperty("bankAccountNum", bankAccountNum);
            billInfo.addProperty("bankName", bankName);
            billInfo.addProperty("billContent", billContent);
            billInfo.addProperty("billType", billType);
            billInfo.addProperty("invoiceTitle", invoiceTitle);
            billInfo.addProperty("regAddress", regAddress);
            billInfo.addProperty("regTelephone", regTelephone);
            billInfo.addProperty("taxCode", taxCode);
            soapObject.addProperty("billInfo", billInfo);
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(serailNo).append(bankAccountNum).append(bankName).append(billContent).append(billType).append(invoiceTitle).append(regAddress).append(regTelephone).append(taxCode).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = soapObject;
            envelope.setOutputSoapObject(soapObject);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    if (this.f1316D) {
                        System.out.println("setValueAddedTaxBillInfo==" + envelope.getResponse().toString());
                    }
                    SoapObject soapObject2 = (SoapObject) envelope.getResponse();
                    if (Integer.valueOf(soapObject2.getPropertyAsString("code")).intValue() == 0) {
                        bInfo.setCode(Integer.valueOf(soapObject2.getPropertyAsString("code")).intValue());
                    } else {
                        bInfo.setCode(Integer.valueOf(soapObject2.getPropertyAsString("code")).intValue());
                    }
                }
                return bInfo;
            }
            for (Element element : headerIn) {
                bInfo.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return bInfo;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public SetNormalBillInfo setBillPostInfo(String serailNo, String postAddress, String receiver, String zipCode, String telephone) throws NullPointerException, SocketTimeoutException {
        String methodName = "setBillPostInfo";
        String serviceName = "billService";
        SetNormalBillInfo billInfo = new SetNormalBillInfo();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (serailNo != null) {
                request.addProperty("serailNo", serailNo);
            }
            if (postAddress != null) {
                request.addProperty("postAddress", postAddress);
            }
            if (receiver != null) {
                request.addProperty("receiver", receiver);
            }
            if (zipCode != null) {
                request.addProperty("zipCode", zipCode);
            }
            if (telephone != null) {
                request.addProperty("telephone", telephone);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(serailNo).append(postAddress).append(receiver).append(zipCode).append(telephone).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    if (this.f1316D) {
                        System.out.println("setBillPostInfo==" + envelope.getResponse().toString());
                    }
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                        billInfo.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    } else {
                        billInfo.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    }
                }
                return billInfo;
            }
            for (Element element : headerIn) {
                billInfo.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return billInfo;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public SetNormalBillInfo setOraderBillPostInfo(String serailNo, String orderSn, Integer billId, Integer addressId) throws NullPointerException, SocketTimeoutException {
        String methodName = "setOraderBillPostInfo";
        String serviceName = "billService";
        SetNormalBillInfo billInfo = new SetNormalBillInfo();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (serailNo != null) {
                request.addProperty("serailNo", serailNo);
            }
            if (orderSn != null) {
                request.addProperty("orderSn", orderSn);
            }
            if (billId != null) {
                request.addProperty("billId", billId);
            }
            if (addressId != null) {
                request.addProperty("addressId", addressId);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(serailNo).append(orderSn).append(billId).append(addressId).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    if (this.f1316D) {
                        System.out.println("setOraderBillPostInfo==" + envelope.getResponse().toString());
                    }
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                        billInfo.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    } else {
                        billInfo.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    }
                }
                return billInfo;
            }
            for (Element element : headerIn) {
                billInfo.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return billInfo;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public NormalBillInfoResult getNormalBillInfoList(String serailNo) throws NullPointerException, SocketTimeoutException {
        String methodName = "getNormalBillInfoList";
        String serviceName = "billService";
        NormalBillInfoResult normalBillInfoResult = new NormalBillInfoResult();
        List<NormalBillInfoDTO> normalBillInfoDTOList = new ArrayList();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (serailNo != null) {
                request.addProperty("serailNo", serailNo);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(serailNo).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                if (this.f1316D) {
                    System.out.println("getNormalBillInfoList===" + envelope.getResponse().toString());
                }
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                Element[] headerIn = envelope.headerIn;
                if (headerIn != null && headerIn.length > 0) {
                    for (Element element : headerIn) {
                        normalBillInfoResult.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
                    }
                } else if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                    normalBillInfoResult.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    SoapObject list = (SoapObject) soapObject.getProperty("list");
                    if (list.getPropertyCount() > 0) {
                        for (int i = 0; i < list.getPropertyCount(); i++) {
                            NormalBillInfoDTO dto = new NormalBillInfoDTO();
                            SoapObject item = (SoapObject) list.getProperty(i);
                            dto.setBillContent(item.getPropertyAsString("billContent"));
                            dto.setBillId(Integer.parseInt(item.getPropertyAsString("billId")));
                            dto.setBillType(Integer.parseInt(item.getPropertyAsString("billType")));
                            dto.setInvoiceTtile(item.getPropertyAsString("invoiceTitle"));
                            normalBillInfoDTOList.add(dto);
                        }
                        normalBillInfoResult.setNormalBillInfoDTO(normalBillInfoDTOList);
                    }
                } else {
                    normalBillInfoResult.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return normalBillInfoResult;
    }

    public ValueAddedTaxBillInfoResult getValueAddedTaxBillInfoList(String serailNo) throws NullPointerException, SocketTimeoutException {
        String methodName = "getValueAddedTaxBillInfoList";
        String serviceName = "billService";
        ValueAddedTaxBillInfoResult valueAddedTaxBillInfoResult = new ValueAddedTaxBillInfoResult();
        List<ValueAddedTaxBillInfoDTO> valueAddedTaxBillInfoDTOList = new ArrayList();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (serailNo != null) {
                request.addProperty("serailNo", serailNo);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(serailNo).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    if (this.f1316D) {
                        System.out.println("getValueAddedTaxBillInfoList==" + envelope.getResponse().toString());
                    }
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                        valueAddedTaxBillInfoResult.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                        SoapObject list = (SoapObject) soapObject.getProperty("list");
                        if (list.getPropertyCount() > 0) {
                            for (int i = 0; i < list.getPropertyCount(); i++) {
                                ValueAddedTaxBillInfoDTO valueAddedTaxBillInfoDTO = new ValueAddedTaxBillInfoDTO();
                                SoapObject item = (SoapObject) list.getProperty(i);
                                valueAddedTaxBillInfoDTO.setBillId(Integer.parseInt(item.getPropertyAsString("billId")));
                                valueAddedTaxBillInfoDTO.setInvoiceTitle(item.getPropertyAsString("invoiceTitle"));
                                valueAddedTaxBillInfoDTO.setBillContent(item.getPropertyAsString("billContent"));
                                valueAddedTaxBillInfoDTO.setTaxCode(item.getPropertyAsString("taxCode"));
                                valueAddedTaxBillInfoDTO.setRegAddress(item.getPropertyAsString("regAddress"));
                                valueAddedTaxBillInfoDTO.setRegTelephone(item.getPropertyAsString("regTelephone"));
                                valueAddedTaxBillInfoDTO.setBankName(item.getPropertyAsString("bankName"));
                                valueAddedTaxBillInfoDTO.setBankAccountNum(item.getPropertyAsString("bankAccountNum"));
                                valueAddedTaxBillInfoDTOList.add(valueAddedTaxBillInfoDTO);
                            }
                            valueAddedTaxBillInfoResult.setValueAddedTaxBillInfoDTO(valueAddedTaxBillInfoDTOList);
                        }
                    } else {
                        valueAddedTaxBillInfoResult.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    }
                }
                return valueAddedTaxBillInfoResult;
            }
            for (Element element : headerIn) {
                valueAddedTaxBillInfoResult.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return valueAddedTaxBillInfoResult;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public PostAddressInfoResult getPostAddressInfoList(String serailNo) throws NullPointerException, SocketTimeoutException {
        String methodName = "getPostAddressInfoList";
        String serviceName = "billService";
        PostAddressInfoResult postAddressInfoResult = new PostAddressInfoResult();
        List<PostAddressInfoDTO> postAddressInfoDTOList = new ArrayList();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (serailNo != null) {
                request.addProperty("serailNo", serailNo);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            StringBuffer sb = new StringBuffer();
            sb.append(serailNo).append(this.token);
            String sgin = MD5.getMD5Str(sb.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    if (this.f1316D) {
                        System.out.println("getPostAddressInfoList===" + envelope.getResponse().toString());
                    }
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    if (Integer.valueOf(soapObject.getPropertyAsString("code")).intValue() == 0) {
                        postAddressInfoResult.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                        SoapObject list = (SoapObject) soapObject.getProperty("list");
                        if (list.getPropertyCount() > 0) {
                            for (int i = 0; i < list.getPropertyCount(); i++) {
                                SoapObject item = (SoapObject) list.getProperty(i);
                                PostAddressInfoDTO postAddressInfo = new PostAddressInfoDTO();
                                postAddressInfo.setAddressId(Integer.parseInt(item.getPropertyAsString("addressId")));
                                postAddressInfo.setPostAddress(item.getPropertyAsString("postAddress"));
                                postAddressInfo.setReceiver(item.getPropertyAsString("receiver"));
                                postAddressInfo.setTelephone(item.getPropertyAsString("telephone"));
                                postAddressInfo.setZipCode(item.getPropertyAsString("zipCode"));
                                postAddressInfoDTOList.add(postAddressInfo);
                            }
                            postAddressInfoResult.setPostAddressInfoDTO(postAddressInfoDTOList);
                        }
                    } else {
                        postAddressInfoResult.setCode(Integer.valueOf(soapObject.getPropertyAsString("code")).intValue());
                    }
                }
                return postAddressInfoResult;
            }
            for (Element element : headerIn) {
                postAddressInfoResult.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return postAddressInfoResult;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public OrderBillPostInfoResult getOrderBillPostInfo(String orderSn) throws NullPointerException, SocketTimeoutException {
        String methodName = "getOrderBillPostInfo";
        String serviceName = "billService";
        OrderBillPostInfoResult result = new OrderBillPostInfoResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (orderSn != null) {
                request.addProperty("orderSn", orderSn);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(orderSn)).append(this.token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    if (this.f1316D) {
                        System.out.println("getOrderBillPostInfo==" + envelope.getResponse().toString());
                    }
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    result.setCode(Integer.parseInt(soapObject.getPropertyAsString("code")));
                    result.setAddressId(soapObject.getPropertyAsString("addressId"));
                    result.setBillId(soapObject.getPropertyAsString("billId"));
                    result.setIsBilled(soapObject.getPropertyAsString("isBilled"));
                    result.setIsPosted(soapObject.getPropertyAsString("isPosted"));
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

    public WSResult checkMobilePaypalPayment(String payKey) throws NullPointerException, SocketTimeoutException {
        String methodName = "checkMobilePaypalPayment";
        String serviceName = "paypalService";
        WSResult result = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (payKey != null) {
                request.addProperty("payKey", payKey);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(payKey)).append(this.token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    if (this.f1316D) {
                        System.out.println("getOrderBillPostInfo==" + envelope.getResponse().toString());
                    }
                    result.setCode(Integer.parseInt(((SoapObject) envelope.getResponse()).getPropertyAsString("code")));
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

    public WSResult checkMobilePaypalPaymentTest(String payKey) throws NullPointerException, SocketTimeoutException {
        String methodName = "checkMobilePaypalPaymentTest";
        String serviceName = "paypalService";
        WSResult result = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (payKey != null) {
                request.addProperty("payKey", payKey);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport("http://mycar.cnlaunch.com:8080/services/" + serviceName, timeout);
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(payKey)).append(this.token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                if (this.f1316D) {
                    System.out.println("checkMobilePaypalPaymentTest==" + envelope.getResponse().toString());
                }
                result.setCode(Integer.parseInt(((SoapObject) envelope.getResponse()).getPropertyAsString("code")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return result;
    }
}
