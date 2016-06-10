package com.ifoer.webservice;

import android.text.TextUtils;
import com.car.result.GetEndUserFullResult;
import com.car.result.GetNationListResult;
import com.car.result.RetrievePasswordResult;
import com.car.result.WSResult;
import com.cnlaunch.framework.common.Constants;
import com.ifoer.entity.CardConsumeDTO;
import com.ifoer.entity.CardConsumeListResult;
import com.ifoer.entity.CardInfo;
import com.ifoer.entity.Constant;
import com.ifoer.entity.EndUserFullDTO;
import com.ifoer.entity.InterfaceDao;
import com.ifoer.entity.ProductToUpgradeToInfo;
import com.ifoer.entity.SecurityAnswerDTO;
import com.ifoer.entity.UpgradeProductResult;
import com.ifoer.entity.UserSerial;
import com.ifoer.http.HttpInfoProvider;
import com.ifoer.md5.MD5;
import com.ifoer.mine.model.UserData;
import com.ifoer.mine.model.UserDataResult;
import com.ifoer.util.MyAndroidHttpTransport;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.plugin.BaseProfile;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

public class UserServiceClient {
    private static final String WEBSERVICE_NAMESPACE = "http://www.x431.com";
    private static final String WEBSERVICE_SOAPACION = "";
    private static final String WEBSERVICE_URL = "http://uc.x431.com/services/";
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

    public WSResult modifyPassWord(String cc, String oldPwd, String newPwd) throws NullPointerException, SocketTimeoutException {
        String methodName = "modifyPassWord";
        String serviceName = "userservice.*";
        WSResult rs = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (cc != null) {
                request.addProperty(MultipleAddresses.CC, cc);
            }
            if (oldPwd != null) {
                request.addProperty("oldPwd", MD5.getMD5Str(oldPwd));
            }
            if (newPwd != null) {
                request.addProperty("newPwd", MD5.getMD5Str(newPwd));
            }
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(cc)).append(MD5.getMD5Str(oldPwd)).append(MD5.getMD5Str(newPwd)).append(this.token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(InterfaceDao.search(serviceName), timeout).call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    rs.setCode(Integer.valueOf(((SoapObject) envelope.getResponse()).getProperty("code").toString()).intValue());
                }
                return rs;
            }
            for (Element element : headerIn) {
                rs.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return rs;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public WSResult sendBindingEail(String cc, String password, String email) throws SocketTimeoutException {
        String methodName = "sendBindingEail";
        String serviceName = "bindingservice";
        WSResult ws = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (cc != null) {
                request.addProperty(MultipleAddresses.CC, cc);
            }
            if (password != null) {
                password = MD5.getMD5Str(password);
                request.addProperty("password", password);
            }
            if (email != null) {
                request.addProperty("email", email);
            }
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(cc)).append(password).append(email).append(this.token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout).call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                ws.setCode(Integer.valueOf(((SoapObject) envelope.getResponse()).getProperty("code").toString()).intValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return ws;
    }

    public ProductToUpgradeToInfo checkProductToUpgrade(String serialNo, String token) throws SocketTimeoutException {
        String methodName = "checkProductToUpgrade";
        String serviceName = "productUpgradeService";
        ProductToUpgradeToInfo ws = new ProductToUpgradeToInfo();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (serialNo != null) {
                request.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, serialNo);
            }
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(serialNo)).append(token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(UpdateSoftware.WEBSERVICE_URL).append(serviceName).toString(), timeout).call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                ws.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                if (Integer.valueOf(soapObject.getProperty("code").toString()).intValue() == 0 && soapObject.getProperty("productUpgradeInfo") != null) {
                    SoapObject infoObject = (SoapObject) soapObject.getProperty("productUpgradeInfo");
                    ws.setFreeTime(infoObject.getProperty("freeEndTime").toString());
                    ws.setNeedRenew(Integer.valueOf(infoObject.getProperty("isNeedToRenew").toString()).intValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return ws;
    }

    public CardInfo getSysCardInfoByCardNo(String carNo, String token) throws SocketTimeoutException {
        String methodName = "getSysCardInfoByCardNo";
        String serviceName = "productUpgradeService";
        CardInfo ws = new CardInfo();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (carNo != null) {
                request.addProperty("cardNo", carNo);
            }
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(carNo)).append(token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(UpdateSoftware.WEBSERVICE_URL).append(serviceName).toString(), timeout).call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                ws.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                if (Integer.valueOf(soapObject.getProperty("code").toString()).intValue() == 0) {
                    if (soapObject.getProperty("sysCardInfo") != null) {
                        SoapObject infoObject = (SoapObject) soapObject.getProperty("sysCardInfo");
                        ws.setCarNo(infoObject.getProperty("cardNo").toString());
                        ws.setCardName(infoObject.getProperty("cardConfName").toString());
                        ws.setSoftConfName(infoObject.getProperty("softConfName").toString());
                        ws.setCardRechargeYear(Integer.valueOf(infoObject.getProperty("cardRechargeYear").toString()).intValue());
                        ws.setCardState(Integer.valueOf(infoObject.getProperty("cardState").toString()).intValue());
                        ws.setDtoisNull(false);
                        if (ws.getCardState() == 3) {
                            ws.setSerialNo(infoObject.getProperty(com.cnlaunch.x431pro.common.Constants.serialNo).toString());
                            ws.setCardConsumeDate(infoObject.getProperty("cardConsumeDate").toString());
                        }
                    } else {
                        ws.setDtoisNull(true);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return ws;
    }

    public CardConsumeListResult getProductUpgradeRecord(String serialNo, String token) throws SocketTimeoutException {
        String methodName = "getProductUpgradeRecord";
        String serviceName = "productUpgradeService";
        CardConsumeListResult ws = new CardConsumeListResult();
        System.out.println(" getProductUpgradeRecord");
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (serialNo != null) {
                request.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, serialNo);
            }
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(serialNo)).append(token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            String stringBuilder = new StringBuilder(UpdateSoftware.WEBSERVICE_URL).append(serviceName).toString();
            int i = timeout;
            new MyAndroidHttpTransport(stringBuilder, r18).call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                ws.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                if (Integer.valueOf(soapObject.getProperty("code").toString()).intValue() == 0) {
                    if (soapObject.getProperty("cardConsumeRecordList") != null) {
                        List<CardConsumeDTO> soapList = new ArrayList();
                        SoapObject infoObject = (SoapObject) soapObject.getProperty("cardConsumeRecordList");
                        for (int i2 = 0; i2 < infoObject.getPropertyCount(); i2++) {
                            SoapObject obj = (SoapObject) infoObject.getProperty(i2);
                            CardConsumeDTO dto = new CardConsumeDTO();
                            dto.setCardNo(obj.getProperty("cardNo").toString());
                            dto.setYears(Integer.valueOf(obj.getProperty("cardRechargeYear").toString()).intValue());
                            dto.setDate(obj.getProperty("updateDate").toString());
                            dto.setFreeEndTime(obj.getProperty("freeEndTime").toString());
                            dto.setOldFreeEndTime(obj.getProperty("oldFreeEndTime").toString());
                            soapList.add(dto);
                        }
                        ws.setCardListReust(soapList);
                    }
                }
            } else {
                System.out.println(" \u67e5\u8be2\u5e8f\u5217\u53f7\u7684\u70b9\u5361\u6d88\u8d39\u8bb0\u5f55 null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return ws;
    }

    public UpgradeProductResult upgradeProduct(String serialNo, String psw) throws SocketTimeoutException {
        String methodName = "upgradeProduct";
        String serviceName = "productUpgradeService";
        UpgradeProductResult ws = new UpgradeProductResult();
        System.out.println(" getProductUpgradeRecord");
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (serialNo != null) {
                request.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, serialNo);
            }
            if (psw != null) {
                request.addProperty("password", psw);
            }
            request.addProperty(SharedPref.TYPE, Integer.valueOf(1));
            String sginStr = new StringBuilder(String.valueOf(serialNo)).append(psw).append(1).append(this.token).toString();
            System.out.println(" token" + this.token);
            String sgin = MD5.getMD5Str(sginStr);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(UpdateSoftware.WEBSERVICE_URL).append(serviceName).toString(), timeout).call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                ws.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                if (Integer.valueOf(soapObject.getProperty("code").toString()).intValue() == 0 && soapObject.getProperty("productUpgradeInfo") != null) {
                    SoapObject infoObject = (SoapObject) soapObject.getProperty("productUpgradeInfo");
                    ws.setSerial(infoObject.getProperty(com.cnlaunch.x431pro.common.Constants.serialNo).toString());
                    ws.setUpdateDate(infoObject.getProperty("updateDate").toString());
                    ws.setOldFreeEndTime(infoObject.getProperty("oldFreeEndTime").toString());
                    ws.setFreeEndTime(infoObject.getProperty("freeEndTime").toString());
                }
            } else {
                System.out.println("\u5145\u503c\u4fe1\u606f null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return ws;
    }

    public WSResult updateEndUserFull(EndUserFullDTO userFullDTO) throws SocketTimeoutException {
        String methodName = "updateEndUserFull";
        String serviceName = "userservice.*";
        WSResult ws = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            StringBuffer sb = new StringBuffer();
            if (userFullDTO != null) {
                SoapObject endUserFullDTO = new SoapObject();
                if (userFullDTO.getUserId() != null) {
                    endUserFullDTO.addProperty("userId", userFullDTO.getUserId());
                    sb.append(userFullDTO.getUserId());
                }
                if (userFullDTO.getUserTypeId() != null) {
                    endUserFullDTO.addProperty("userTypeId", userFullDTO.getUserTypeId());
                    sb.append(userFullDTO.getUserTypeId());
                }
                if (!(userFullDTO.getUserName() == null || TextUtils.isEmpty(userFullDTO.getUserName()))) {
                    endUserFullDTO.addProperty("userName", userFullDTO.getUserName());
                    sb.append(userFullDTO.getUserName());
                }
                if (!(userFullDTO.getNickName() == null || TextUtils.isEmpty(userFullDTO.getNickName()))) {
                    endUserFullDTO.addProperty("nickName", userFullDTO.getNickName());
                    sb.append(userFullDTO.getNickName());
                }
                if (!(userFullDTO.getMobile() == null || TextUtils.isEmpty(userFullDTO.getMobile()))) {
                    endUserFullDTO.addProperty("mobile", userFullDTO.getMobile());
                    sb.append(userFullDTO.getMobile());
                }
                if (userFullDTO.getSex() != null) {
                    endUserFullDTO.addProperty("sex", userFullDTO.getSex());
                    sb.append(userFullDTO.getSex());
                }
                if (userFullDTO.getBirthdays() != 0) {
                    endUserFullDTO.addProperty("birthdays", Long.valueOf(userFullDTO.getBirthdays()));
                    sb.append(userFullDTO.getBirthdays());
                }
                if (!(userFullDTO.getCompanyName() == null || TextUtils.isEmpty(userFullDTO.getCompanyName()))) {
                    endUserFullDTO.addProperty(com.cnlaunch.x431pro.common.Constants.COMPANYNAME, userFullDTO.getCompanyName());
                    sb.append(userFullDTO.getCompanyName());
                }
                if (!(userFullDTO.getFirstName() == null || TextUtils.isEmpty(userFullDTO.getFirstName()))) {
                    endUserFullDTO.addProperty("firstName", userFullDTO.getFirstName());
                    sb.append(userFullDTO.getFirstName());
                }
                if (!(userFullDTO.getLastName() == null || TextUtils.isEmpty(userFullDTO.getLastName()))) {
                    endUserFullDTO.addProperty("lastName", userFullDTO.getLastName());
                    sb.append(userFullDTO.getLastName());
                }
                if (!(userFullDTO.getFamilyPhone() == null || TextUtils.isEmpty(userFullDTO.getFamilyPhone()))) {
                    endUserFullDTO.addProperty("familyPhone", userFullDTO.getFamilyPhone());
                    sb.append(userFullDTO.getFamilyPhone());
                }
                if (!(userFullDTO.getOfficePhone() == null || TextUtils.isEmpty(userFullDTO.getOfficePhone()))) {
                    endUserFullDTO.addProperty("officePhone", userFullDTO.getOfficePhone());
                    sb.append(userFullDTO.getOfficePhone());
                }
                if (!(userFullDTO.getContinent() == null || TextUtils.isEmpty(userFullDTO.getContinent()))) {
                    endUserFullDTO.addProperty("continent", userFullDTO.getContinent());
                    sb.append(userFullDTO.getContinent());
                }
                if (!(userFullDTO.getCountry() == null || TextUtils.isEmpty(userFullDTO.getCountry()))) {
                    endUserFullDTO.addProperty("country", userFullDTO.getCountry());
                    sb.append(userFullDTO.getCountry());
                }
                if (!(userFullDTO.getProvince() == null || TextUtils.isEmpty(userFullDTO.getProvince()))) {
                    endUserFullDTO.addProperty(BaseProfile.COL_PROVINCE, userFullDTO.getProvince());
                    sb.append(userFullDTO.getProvince());
                }
                if (!(userFullDTO.getCity() == null || TextUtils.isEmpty(userFullDTO.getCity()))) {
                    endUserFullDTO.addProperty(BaseProfile.COL_CITY, userFullDTO.getCity());
                    sb.append(userFullDTO.getCity());
                }
                if (!(userFullDTO.getAddress() == null || TextUtils.isEmpty(userFullDTO.getAddress()))) {
                    endUserFullDTO.addProperty("address", userFullDTO.getAddress());
                    sb.append(userFullDTO.getAddress());
                }
                if (!(userFullDTO.getEmail() == null || TextUtils.isEmpty(userFullDTO.getEmail()))) {
                    endUserFullDTO.addProperty("email", userFullDTO.getEmail());
                    sb.append(userFullDTO.getEmail());
                }
                if (!(userFullDTO.getZipCode() == null || TextUtils.isEmpty(userFullDTO.getZipCode()))) {
                    endUserFullDTO.addProperty("zipCode", userFullDTO.getZipCode());
                    sb.append(userFullDTO.getZipCode());
                }
                if (!(userFullDTO.getLongitude() == null || TextUtils.isEmpty(userFullDTO.getLongitude()))) {
                    endUserFullDTO.addProperty("longitude", new StringBuilder(String.valueOf(userFullDTO.getLongitude())).toString());
                    sb.append(new StringBuilder(String.valueOf(userFullDTO.getLongitude())).toString());
                }
                if (!(userFullDTO.getLatitude() == null || TextUtils.isEmpty(userFullDTO.getLatitude()))) {
                    endUserFullDTO.addProperty("latitude", new StringBuilder(String.valueOf(userFullDTO.getLatitude())).toString());
                    sb.append(new StringBuilder(String.valueOf(userFullDTO.getLatitude())).toString());
                }
                if (!(userFullDTO.getMarkAddress() == null || TextUtils.isEmpty(userFullDTO.getMarkAddress()))) {
                    endUserFullDTO.addProperty("markAddress", userFullDTO.getMarkAddress());
                    sb.append(userFullDTO.getMarkAddress());
                }
                if (!(userFullDTO.getDrivingLicense() == null || TextUtils.isEmpty(userFullDTO.getDrivingLicense()))) {
                    endUserFullDTO.addProperty("drivingLicense", userFullDTO.getDrivingLicense());
                    sb.append(userFullDTO.getDrivingLicense());
                }
                if (!(userFullDTO.getIssueDate() == null || TextUtils.isEmpty(userFullDTO.getIssueDate()))) {
                    endUserFullDTO.addProperty("issueDate", userFullDTO.getIssueDate());
                    sb.append(userFullDTO.getIssueDate());
                }
                if (endUserFullDTO != null) {
                    request.addProperty("endUserFullDTO", endUserFullDTO);
                }
                sb.append(this.token);
            }
            String sgin = MD5.getMD5Str(sb.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(InterfaceDao.search(serviceName), timeout).call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    ws.setCode(Integer.valueOf(((SoapObject) envelope.getResponse()).getProperty("code").toString()).intValue());
                }
                return ws;
            }
            for (Element element : headerIn) {
                ws.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return ws;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public UserData getData(String cc) {
        UserData code = null;
        try {
            code = HttpInfoProvider.getInstance().getUserData(cc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public UserDataResult getContactData(String cc) {
        UserDataResult code = null;
        try {
            code = HttpInfoProvider.getInstance().getContackData(cc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public GetEndUserFullResult getEndUserFull(String cc) throws NullPointerException, SocketTimeoutException {
        String methodName = "getEndUserFull";
        String serviceName = "userservice.*";
        GetEndUserFullResult full = new GetEndUserFullResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (cc != null) {
                request.addProperty(MultipleAddresses.CC, cc);
            }
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(cc)).append(this.token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(InterfaceDao.search(serviceName), timeout).call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    full.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    if (full.getCode() == 0) {
                        if (soapObject.hasProperty(BundleBuilder.AskFromMessage)) {
                            full.setMessage(soapObject.getProperty(BundleBuilder.AskFromMessage).toString());
                        } else {
                            full.setMessage(WEBSERVICE_SOAPACION);
                        }
                        if (soapObject.hasProperty("address")) {
                            full.setAddress(soapObject.getProperty("address").toString());
                        } else {
                            full.setAddress(WEBSERVICE_SOAPACION);
                        }
                        if (soapObject.hasProperty("birthday")) {
                            full.setBirthday(soapObject.getProperty("birthday").toString());
                        } else {
                            full.setBirthday(WEBSERVICE_SOAPACION);
                        }
                        if (soapObject.hasProperty("email")) {
                            full.setEmail(soapObject.getProperty("email").toString());
                        } else {
                            full.setEmail(WEBSERVICE_SOAPACION);
                        }
                        if (soapObject.hasProperty("isBindEmail")) {
                            full.setIsBindEmail(Integer.valueOf(soapObject.getProperty("isBindEmail").toString()));
                        } else {
                            full.setIsBindEmail(Integer.valueOf(0));
                        }
                        if (soapObject.hasProperty("isBindMobile")) {
                            full.setIsBindMobile(Integer.valueOf(soapObject.getProperty("isBindMobile").toString()));
                        } else {
                            full.setIsBindMobile(Integer.valueOf(0));
                        }
                        if (soapObject.hasProperty("isSecurity")) {
                            full.setIsSecurity(Integer.valueOf(soapObject.getProperty("isSecurity").toString()));
                        } else {
                            full.setIsSecurity(Integer.valueOf(0));
                        }
                        if (soapObject.hasProperty("mobile")) {
                            full.setMobile(soapObject.getProperty("mobile").toString());
                        } else {
                            full.setMobile(WEBSERVICE_SOAPACION);
                        }
                        if (soapObject.hasProperty("sex")) {
                            full.setSex(Integer.valueOf(soapObject.getProperty("sex").toString()));
                        } else {
                            full.setSex(Integer.valueOf(0));
                        }
                        if (soapObject.hasProperty("userId")) {
                            full.setUserId(Integer.valueOf(soapObject.getProperty("userId").toString()));
                        } else {
                            full.setUserId(Integer.valueOf(0));
                        }
                        if (soapObject.hasProperty("userName")) {
                            full.setUserName(soapObject.getProperty("userName").toString());
                        } else {
                            full.setUserName(WEBSERVICE_SOAPACION);
                        }
                        if (soapObject.hasProperty("userTypeId")) {
                            full.setUserTypeId(Integer.valueOf(soapObject.getProperty("userTypeId").toString()));
                        } else {
                            full.setUserTypeId(Integer.valueOf(0));
                        }
                        String zipcode = WEBSERVICE_SOAPACION;
                        if (soapObject.hasProperty("zipCode")) {
                            zipcode = soapObject.getProperty("zipCode").toString();
                            if (zipcode.equals("anyType{}")) {
                                zipcode = WEBSERVICE_SOAPACION;
                            }
                        }
                        full.setZipCode(zipcode);
                    }
                }
                return full;
            }
            for (Element element : headerIn) {
                full.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return full;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (SecurityException e4) {
            e4.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.car.result.GetSecurityQuestionListResult getSecurityQuestionListByLang(java.lang.String r18) throws java.lang.NullPointerException, java.net.SocketTimeoutException {
        /*
        r17 = this;
        r6 = "getSecurityQuestionListByLang";
        r9 = "usersecurityservice";
        r8 = new com.car.result.GetSecurityQuestionListResult;
        r8.<init>();
        r13 = 0;
        r5 = new java.util.ArrayList;
        r5.<init>();
        r7 = new org.ksoap2.serialization.SoapObject;	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = "http://www.x431.com";
        r7.<init>(r15, r6);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        if (r18 == 0) goto L_0x001f;
    L_0x0018:
        r15 = "language";
        r0 = r18;
        r7.addProperty(r15, r0);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
    L_0x001f:
        r3 = new com.ifoer.util.MyAndroidHttpTransport;	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r16 = "http://uc.x431.com/services/";
        r15.<init>(r16);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = r15.append(r9);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = r15.toString();	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r16 = timeout;	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r0 = r16;
        r3.<init>(r15, r0);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r2 = new org.ksoap2.serialization.SoapSerializationEnvelope;	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r2.<init>(r15);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r2.setOutputSoapObject(r7);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = 0;
        r2.dotNet = r15;	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = "";
        r3.call(r15, r2);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = r2.getResponse();	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        if (r15 == 0) goto L_0x0084;
    L_0x004f:
        r11 = r2.getResponse();	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r11 = (org.ksoap2.serialization.SoapObject) r11;	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = "code";
        r15 = r11.getProperty(r15);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = r15.toString();	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = java.lang.Integer.valueOf(r15);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = r15.intValue();	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r8.setCode(r15);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = r8.getCode();	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        if (r15 != 0) goto L_0x0084;
    L_0x0070:
        r15 = "securityQuestionList";
        r12 = r11.getProperty(r15);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r12 = (org.ksoap2.serialization.SoapObject) r12;	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r4 = 0;
        r14 = r13;
    L_0x007a:
        r15 = r11.getPropertyCount();	 Catch:{ IOException -> 0x00d3, XmlPullParserException -> 0x00d0 }
        if (r4 < r15) goto L_0x0085;
    L_0x0080:
        r8.setSecurityQuestionList(r5);	 Catch:{ IOException -> 0x00d3, XmlPullParserException -> 0x00d0 }
        r13 = r14;
    L_0x0084:
        return r8;
    L_0x0085:
        if (r4 != 0) goto L_0x00a1;
    L_0x0087:
        r15 = "code";
        r15 = r11.getProperty(r15);	 Catch:{ IOException -> 0x00d3, XmlPullParserException -> 0x00d0 }
        r15 = r15.toString();	 Catch:{ IOException -> 0x00d3, XmlPullParserException -> 0x00d0 }
        r15 = java.lang.Integer.valueOf(r15);	 Catch:{ IOException -> 0x00d3, XmlPullParserException -> 0x00d0 }
        r15 = r15.intValue();	 Catch:{ IOException -> 0x00d3, XmlPullParserException -> 0x00d0 }
        r8.setCode(r15);	 Catch:{ IOException -> 0x00d3, XmlPullParserException -> 0x00d0 }
        r13 = r14;
    L_0x009d:
        r4 = r4 + 1;
        r14 = r13;
        goto L_0x007a;
    L_0x00a1:
        r10 = r11.getProperty(r4);	 Catch:{ IOException -> 0x00d3, XmlPullParserException -> 0x00d0 }
        r10 = (org.ksoap2.serialization.SoapObject) r10;	 Catch:{ IOException -> 0x00d3, XmlPullParserException -> 0x00d0 }
        r13 = new com.ifoer.entity.SecurityQuestionDTO;	 Catch:{ IOException -> 0x00d3, XmlPullParserException -> 0x00d0 }
        r13.<init>();	 Catch:{ IOException -> 0x00d3, XmlPullParserException -> 0x00d0 }
        r15 = "questionId";
        r15 = r10.getPropertyAsString(r15);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = java.lang.Integer.valueOf(r15);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r13.setQuestionId(r15);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r15 = "questionDesc";
        r15 = r10.getPropertyAsString(r15);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r13.setQuestionDesc(r15);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        r5.add(r13);	 Catch:{ IOException -> 0x00c6, XmlPullParserException -> 0x00cb }
        goto L_0x009d;
    L_0x00c6:
        r1 = move-exception;
    L_0x00c7:
        r1.printStackTrace();
        goto L_0x0084;
    L_0x00cb:
        r1 = move-exception;
    L_0x00cc:
        r1.printStackTrace();
        goto L_0x0084;
    L_0x00d0:
        r1 = move-exception;
        r13 = r14;
        goto L_0x00cc;
    L_0x00d3:
        r1 = move-exception;
        r13 = r14;
        goto L_0x00c7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.UserServiceClient.getSecurityQuestionListByLang(java.lang.String):com.car.result.GetSecurityQuestionListResult");
    }

    public WSResult setupSecurityInfos(String cc, List<SecurityAnswerDTO> securityAnswerList) throws SocketTimeoutException {
        String methodName = "setupSecurityInfos";
        String serviceName = "userservice";
        WSResult ws = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (cc != null) {
                request.addProperty(MultipleAddresses.CC, cc);
            }
            StringBuffer sginStr = new StringBuffer();
            sginStr.append(cc);
            if (securityAnswerList != null && securityAnswerList.size() > 0) {
                for (SecurityAnswerDTO securityAnswerDTO : securityAnswerList) {
                    request.addProperty("securityAnswerList", securityAnswerDTO);
                    sginStr.append(securityAnswerDTO.getQuestionId() + securityAnswerDTO.getAnswer());
                }
            }
            sginStr.append(this.token);
            String sgin = MD5.getMD5Str(sginStr.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new HttpTransportSE(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString()).call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    ws.setCode(Integer.valueOf(((SoapObject) envelope.getResponse()).getProperty("code").toString()).intValue());
                    ws.getCode();
                }
                return ws;
            }
            for (Element element : headerIn) {
                ws.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return ws;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (SecurityException e4) {
            e4.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.car.result.GetSecurityQuestionListResult getSecurityQuestionListByLoginKeyAndLang(java.lang.String r17, java.lang.String r18) throws java.net.SocketTimeoutException {
        /*
        r16 = this;
        r6 = "getSecurityQuestionListByLoginKeyAndLang";
        r9 = "usersecurityservice";
        r3 = new com.car.result.GetSecurityQuestionListResult;
        r3.<init>();
        r8 = new java.util.ArrayList;
        r8.<init>();
        r12 = 0;
        r7 = new org.ksoap2.serialization.SoapObject;	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = "http://www.x431.com";
        r7.<init>(r14, r6);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        if (r17 == 0) goto L_0x001f;
    L_0x0018:
        r14 = "loginKey";
        r0 = r17;
        r7.addProperty(r14, r0);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
    L_0x001f:
        if (r18 == 0) goto L_0x0028;
    L_0x0021:
        r14 = "language";
        r0 = r18;
        r7.addProperty(r14, r0);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
    L_0x0028:
        r4 = new com.ifoer.util.MyAndroidHttpTransport;	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r15 = "http://uc.x431.com/services/";
        r14.<init>(r15);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = r14.append(r9);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = r14.toString();	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r15 = timeout;	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r4.<init>(r14, r15);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r2 = new org.ksoap2.serialization.SoapSerializationEnvelope;	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r2.<init>(r14);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r2.setOutputSoapObject(r7);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = 0;
        r2.dotNet = r14;	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = "";
        r4.call(r14, r2);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = r2.getResponse();	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        if (r14 == 0) goto L_0x007c;
    L_0x0056:
        r11 = r2.getResponse();	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r11 = (org.ksoap2.serialization.SoapObject) r11;	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = "code";
        r14 = r11.getProperty(r14);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = r14.toString();	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = r14.intValue();	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        if (r14 != 0) goto L_0x00c3;
    L_0x0070:
        r5 = 0;
        r13 = r12;
    L_0x0072:
        r14 = r11.getPropertyCount();	 Catch:{ IOException -> 0x00e1, XmlPullParserException -> 0x00de }
        if (r5 < r14) goto L_0x007d;
    L_0x0078:
        r3.setSecurityQuestionList(r8);	 Catch:{ IOException -> 0x00e1, XmlPullParserException -> 0x00de }
        r12 = r13;
    L_0x007c:
        return r3;
    L_0x007d:
        if (r5 != 0) goto L_0x0099;
    L_0x007f:
        r14 = "code";
        r14 = r11.getProperty(r14);	 Catch:{ IOException -> 0x00e1, XmlPullParserException -> 0x00de }
        r14 = r14.toString();	 Catch:{ IOException -> 0x00e1, XmlPullParserException -> 0x00de }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ IOException -> 0x00e1, XmlPullParserException -> 0x00de }
        r14 = r14.intValue();	 Catch:{ IOException -> 0x00e1, XmlPullParserException -> 0x00de }
        r3.setCode(r14);	 Catch:{ IOException -> 0x00e1, XmlPullParserException -> 0x00de }
        r12 = r13;
    L_0x0095:
        r5 = r5 + 1;
        r13 = r12;
        goto L_0x0072;
    L_0x0099:
        r10 = r11.getProperty(r5);	 Catch:{ IOException -> 0x00e1, XmlPullParserException -> 0x00de }
        r10 = (org.ksoap2.serialization.SoapObject) r10;	 Catch:{ IOException -> 0x00e1, XmlPullParserException -> 0x00de }
        r12 = new com.ifoer.entity.SecurityQuestionDTO;	 Catch:{ IOException -> 0x00e1, XmlPullParserException -> 0x00de }
        r12.<init>();	 Catch:{ IOException -> 0x00e1, XmlPullParserException -> 0x00de }
        r14 = "questionId";
        r14 = r10.getPropertyAsString(r14);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r12.setQuestionId(r14);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = "questionDesc";
        r14 = r10.getPropertyAsString(r14);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r12.setQuestionDesc(r14);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r8.add(r12);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        goto L_0x0095;
    L_0x00be:
        r1 = move-exception;
    L_0x00bf:
        r1.printStackTrace();
        goto L_0x007c;
    L_0x00c3:
        r14 = "code";
        r14 = r11.getProperty(r14);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = r14.toString();	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r14 = r14.intValue();	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        r3.setCode(r14);	 Catch:{ IOException -> 0x00be, XmlPullParserException -> 0x00d9 }
        goto L_0x007c;
    L_0x00d9:
        r1 = move-exception;
    L_0x00da:
        r1.printStackTrace();
        goto L_0x007c;
    L_0x00de:
        r1 = move-exception;
        r12 = r13;
        goto L_0x00da;
    L_0x00e1:
        r1 = move-exception;
        r12 = r13;
        goto L_0x00bf;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.webservice.UserServiceClient.getSecurityQuestionListByLoginKeyAndLang(java.lang.String, java.lang.String):com.car.result.GetSecurityQuestionListResult");
    }

    public RetrievePasswordResult retrievePassword(String loginKey, List<SecurityAnswerDTO> securityAnswerList) throws SocketTimeoutException {
        String methodName = "retrievePassword";
        String serviceName = "usersecurityservice";
        RetrievePasswordResult rs = new RetrievePasswordResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (loginKey != null) {
                request.addProperty("loginKey", loginKey);
            }
            StringBuffer sginStr = new StringBuffer();
            sginStr.append(this.cc);
            if (securityAnswerList != null && securityAnswerList.size() > 0) {
                for (SecurityAnswerDTO securityAnswerDTO : securityAnswerList) {
                    request.addProperty("securityAnswerList", securityAnswerDTO);
                    sginStr.append(securityAnswerDTO.getQuestionId() + securityAnswerDTO.getAnswer());
                }
            }
            String sgin = MD5.getMD5Str(sginStr.toString());
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(WEBSERVICE_NAMESPACE, "SecurityAnswerDTO", SecurityAnswerDTO.class);
            envelope.dotNet = false;
            ht.call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    rs.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    if (rs.getCode() == 0) {
                        if (soapObject.hasProperty(MultipleAddresses.CC)) {
                            rs.setCc(soapObject.getProperty(MultipleAddresses.CC).toString());
                        } else {
                            rs.setCc(WEBSERVICE_SOAPACION);
                        }
                        if (soapObject.hasProperty("password")) {
                            rs.setPassword(soapObject.getProperty("password").toString());
                        } else {
                            rs.setPassword(WEBSERVICE_SOAPACION);
                        }
                    }
                }
                return rs;
            }
            for (Element element : headerIn) {
                rs.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return rs;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public WSResult sendSms4RetrievePassWordByLoginKey(String loginKey) throws SocketTimeoutException {
        String methodName = "sendSms4RetrievePassWordByLoginKey";
        String serviceName = "usersecurityservice";
        WSResult rs = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (loginKey != null) {
                request.addProperty("loginKey", loginKey);
            }
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(loginKey)).append(this.token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout).call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                rs.setCode(Integer.valueOf(((SoapObject) envelope.getResponse()).getProperty("code").toString()).intValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return rs;
    }

    public RetrievePasswordResult retrievePasswordByVarifyCode(String loginKey, String validateCode) throws SocketTimeoutException {
        String methodName = "retrievePasswordByVarifyCode";
        String serviceName = "usersecurityservice";
        RetrievePasswordResult result = new RetrievePasswordResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (loginKey != null) {
                request.addProperty("loginKey", loginKey);
            }
            if (validateCode != null) {
                request.addProperty("validateCode", validateCode);
            }
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(loginKey)).append(validateCode).append(this.token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout).call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                result.setCode(Integer.valueOf(((SoapObject) envelope.getResponse()).getProperty("code").toString()).intValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return result;
    }

    public WSResult sendRetrievePasswodUrlByLoginKey(String loginKey) throws NullPointerException, SocketTimeoutException {
        String methodName = "sendRetrievePasswodUrlByLoginKey";
        String serviceName = "usersecurityservice.*";
        WSResult result = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (loginKey != null) {
                request.addProperty("loginKey", loginKey);
            }
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(loginKey)).append(this.token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(InterfaceDao.search(serviceName), timeout).call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    result.setCode(Integer.valueOf(((SoapObject) envelope.getResponse()).getProperty("code").toString()).intValue());
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

    public GetNationListResult getNationByCidAndLang(String cid, String language) throws SocketTimeoutException {
        String methodName = "getNationByCidAndLang";
        String serviceName = "locationservice";
        GetNationListResult rs = new GetNationListResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (cid != null) {
                request.addProperty("cid", cid);
            }
            if (language != null) {
                request.addProperty("language", language);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = false;
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                rs.setCode(Integer.valueOf(((SoapObject) envelope.getResponse()).getProperty("code").toString()).intValue());
                rs.getCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return rs;
    }

    public GetEndUserFullResult getEndUserFullOne(String cc) throws NullPointerException, SocketTimeoutException {
        String methodName = "getEndUserFull";
        String serviceName = "userservice.*";
        GetEndUserFullResult full = new GetEndUserFullResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (cc != null) {
                request.addProperty(MultipleAddresses.CC, cc);
            }
            String sgin = MD5.getMD5Str(new StringBuilder(String.valueOf(cc)).append(this.token).toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut = createHead(sgin);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(InterfaceDao.search(serviceName), timeout).call(WEBSERVICE_SOAPACION, envelope);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    full.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    if (full.getCode() == 0) {
                        full.setMessage(soapObject.getProperty(BundleBuilder.AskFromMessage).toString());
                        if (soapObject.getPropertyAsString("address").equals(WEBSERVICE_SOAPACION) || soapObject.getPropertyAsString("address") == null) {
                            full.setAddress(WEBSERVICE_SOAPACION);
                        } else {
                            full.setAddress(soapObject.getPropertyAsString("address"));
                        }
                    }
                }
                return full;
            }
            for (Element element : headerIn) {
                full.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return full;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (SecurityException e4) {
            e4.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public UserSerial getRegisteredProductList(String cc) throws SocketTimeoutException {
        String methodName = "getRegisteredProductList";
        String serviceName = "sysProductService.*";
        UserSerial ws = new UserSerial();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (cc != null) {
                request.addProperty(MultipleAddresses.CC, cc);
            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(InterfaceDao.search(serviceName), timeout).call(null, envelope, null);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    ws.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    if (soapObject.hasProperty("productPdtTypeDTOList")) {
                        SoapObject soapQuestion = (SoapObject) soapObject.getProperty("productPdtTypeDTOList");
                        for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                            if (i == 0) {
                                ws.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                            } else {
                                SoapObject soapList = (SoapObject) soapObject.getProperty(i);
                                if (ws.getCode() == 0) {
                                    ws.setSerailNo(soapList.getProperty(com.cnlaunch.x431pro.common.Constants.serialNo).toString());
                                } else {
                                    if (!soapObject.getProperty(BundleBuilder.AskFromMessage).toString().equals(null)) {
                                        ws.setMessage(soapObject.getProperty(BundleBuilder.AskFromMessage).toString());
                                    }
                                }
                            }
                        }
                    } else {
                        ws.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    }
                }
                return ws;
            }
            for (Element element : headerIn) {
                ws.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return ws;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }
}
