package com.ifoer.webservice;

import android.content.Context;
import com.car.result.LoginResult;
import com.car.result.RegisterResult;
import com.car.result.WSResult;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.ifoer.entity.Constant;
import com.ifoer.entity.CountryListResult;
import com.ifoer.entity.InterfaceDao;
import com.ifoer.entity.UserInfoResult;
import com.ifoer.entity.X431UserDTO;
import com.ifoer.http.HttpInfoProvider;
import com.ifoer.mine.DecodeException;
import com.ifoer.mine.model.BaseCode;
import com.ifoer.util.MyAndroidHttpTransport;
import com.launch.service.BundleBuilder;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

public class WebServiceClient {
    public static final boolean f1318D = true;
    private static final String WEBSERVICE_NAMESPACE = "http://www.x431.com";
    private static final String WEBSERVICE_SOAPACION = "";
    private static final String WEBSERVICE_URL = "http://uc.x431.com/services/";
    public static int timeout;

    static {
        timeout = XStream.PRIORITY_VERY_HIGH;
    }

    public RegisterResult registe4X431(X431UserDTO x431UserDTO, String password) throws NullPointerException, SocketTimeoutException {
        String methodName = "registe4X431";
        String serviceName = "loginservice";
        RegisterResult rs = new RegisterResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (x431UserDTO != null) {
                SoapObject endUserFullDTO = new SoapObject();
                endUserFullDTO.addProperty("userName", x431UserDTO.getUserName());
                endUserFullDTO.addProperty("email", x431UserDTO.getEmail());
                endUserFullDTO.addProperty("mobile", x431UserDTO.getMobile());
                endUserFullDTO.addProperty("firstName", x431UserDTO.getFirstName());
                endUserFullDTO.addProperty("lastName", x431UserDTO.getLastName());
                endUserFullDTO.addProperty("sex", Integer.valueOf(x431UserDTO.getSex()));
                endUserFullDTO.addProperty("birthdays", Long.valueOf(x431UserDTO.getBirthdays()));
                endUserFullDTO.addProperty("officePhone", x431UserDTO.getOfficePhone());
                endUserFullDTO.addProperty("fax", x431UserDTO.getFax());
                endUserFullDTO.addProperty(Constants.COMPANYNAME, x431UserDTO.getCompanyName());
                endUserFullDTO.addProperty("address", x431UserDTO.getAddress());
                endUserFullDTO.addProperty("zipCode", x431UserDTO.getZipCode());
                request.addProperty("x431UserDTO", endUserFullDTO);
            }
            if (password != null) {
                request.addProperty("password", password);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(WEBSERVICE_URL).append(serviceName).toString(), timeout);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = false;
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                rs.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                if (rs.getCode() == 0) {
                    rs.setCc(soapObject.getProperty(MultipleAddresses.CC).toString());
                    rs.setInitPassword(soapObject.getProperty("initPassword").toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return rs;
    }

    public RegisterResult registeUser(Context context, String userName, String password, String email, String mobile) throws NullPointerException, SocketTimeoutException {
        String methodName = "registeUser";
        String serviceName = "loginservice.*";
        RegisterResult rs = new RegisterResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (userName != null) {
                request.addProperty("userName", userName);
            }
            if (password != null) {
                request.addProperty("password", password);
            }
            if (email != null) {
                request.addProperty("email", email);
            }
            if (mobile != null) {
                request.addProperty("mobile", mobile);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(InterfaceDao.search(serviceName), timeout);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = false;
            ht.call(WEBSERVICE_SOAPACION, envelope);
            if (envelope.getResponse() != null) {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                rs.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                if (rs.getCode() == 0) {
                    rs.setCc(soapObject.getProperty(MultipleAddresses.CC).toString());
                    rs.setInitPassword(soapObject.getProperty("initPassword").toString());
                }
                if (!soapObject.getProperty(BundleBuilder.AskFromMessage).toString().equals(null)) {
                    rs.setMessage(soapObject.getProperty(BundleBuilder.AskFromMessage).toString());
                }
            }
        } catch (IOException e) {
            rs.setMessage(context.getResources().getString(C0136R.string.io_exception));
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            rs.setMessage(context.getResources().getString(C0136R.string.ERROR_NETWORK));
            e2.printStackTrace();
        } catch (Exception e3) {
            rs.setMessage(context.getResources().getString(C0136R.string.error_server));
            e3.printStackTrace();
        }
        return rs;
    }

    public WSResult checkForCRP(String sn, String psw, Context context) throws SocketTimeoutException {
        String methodName = "checkForCRP";
        String serviceName = "sysProductService";
        WSResult ws = new WSResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (!(sn == null || psw == null)) {
                request.addProperty(Constants.serialNo, sn);
                request.addProperty("password", psw);
            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;
            envelope.setOutputSoapObject(request);
            new MyAndroidHttpTransport(new StringBuilder(UpdateSoftware.WEBSERVICE_URL).append(serviceName).toString(), timeout).call(null, envelope, null);
            Element[] headerIn = envelope.headerIn;
            if (headerIn == null || headerIn.length <= 0) {
                if (envelope.getResponse() != null) {
                    SoapObject soapObject = (SoapObject) envelope.getResponse();
                    ws.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                    if (!soapObject.getProperty(BundleBuilder.AskFromMessage).toString().equals(null)) {
                        ws.setMessage(soapObject.getProperty(BundleBuilder.AskFromMessage).toString());
                    }
                } else {
                    ws.setMessage(context.getResources().getString(C0136R.string.notic_serv));
                }
                return ws;
            }
            for (Element element : headerIn) {
                ws.setCode(Integer.parseInt(((Element) element.getChild(0)).getChild(0).toString()));
            }
            return ws;
        } catch (IOException e) {
            ws.setMessage(context.getResources().getString(C0136R.string.error_server));
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            ws.setMessage(context.getResources().getString(C0136R.string.ERROR_NETWORK));
            e2.printStackTrace();
        } catch (Exception e3) {
            ws.setMessage(context.getResources().getString(C0136R.string.error_server));
            e3.printStackTrace();
        }
    }

    public UserInfoResult getUserInfoBySerialNo(String serialNo, Context context) throws NullPointerException, SocketTimeoutException {
        String methodName = "getUserInfoBySerialNo";
        String serviceName = "sysUserService";
        UserInfoResult logReg = new UserInfoResult();
        try {
            SoapObject request = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
            if (serialNo != null) {
                request.addProperty(Constants.serialNo, serialNo);
            }
            MyAndroidHttpTransport ht = new MyAndroidHttpTransport(new StringBuilder(UpdateSoftware.WEBSERVICE_URL).append(serviceName).toString(), timeout);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.bodyOut = request;
            envelope.dotNet = false;
            ht.call(WEBSERVICE_SOAPACION, envelope, null);
            if (envelope.getResponse() != null) {
                SoapObject soapObject = (SoapObject) envelope.getResponse();
                logReg.setCode(Integer.valueOf(soapObject.getProperty("code").toString()).intValue());
                if (logReg.getCode() == 0) {
                    logReg.setCc(soapObject.getProperty(MultipleAddresses.CC).toString());
                    logReg.setEmail(soapObject.getProperty("email").toString());
                    logReg.setUserName(soapObject.getProperty("userName").toString());
                } else if (!soapObject.getProperty(BundleBuilder.AskFromMessage).toString().equals(null)) {
                    logReg.setMessage(soapObject.getProperty(BundleBuilder.AskFromMessage).toString());
                }
            } else {
                logReg.setCode(Constant.ERROR_CODE);
                logReg.setMessage(context.getString(C0136R.string.In_response_to_failure));
            }
        } catch (IOException e) {
            logReg.setCode(Constant.ERROR_CODE);
            logReg.setMessage(context.getString(C0136R.string.ERROR_NETWORK));
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            logReg.setCode(Constant.ERROR_CODE);
            logReg.setMessage(context.getString(C0136R.string.ERROR_NETWORK));
            e2.printStackTrace();
        } catch (Exception e3) {
            logReg.setCode(Constant.ERROR_CODE);
            logReg.setMessage(context.getString(C0136R.string.ERROR_SERVER));
            e3.printStackTrace();
        }
        return logReg;
    }

    public LoginResult userLogin(Context context, String loginKey, String mobileAppVersion, String password) throws Exception {
        LoginResult loginResult = new LoginResult(context, loginKey, password);
        try {
            loginResult = HttpInfoProvider.getInstance().getLoginUser(context, loginKey, password, Constant.APP_ID, new StringBuilder(String.valueOf(System.currentTimeMillis())).toString(), 0);
            Constant.loginResult = loginResult;
            return loginResult;
        } catch (Exception e) {
            e.printStackTrace();
            return loginResult;
        }
    }

    public CountryListResult getAreaCountry(String lan) {
        CountryListResult baseCode = null;
        try {
            baseCode = HttpInfoProvider.getInstance().getAreaCountry(lan);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseCode;
    }

    public BaseCode getVerifyCode(String req_info, String lan, String isres) throws Exception {
        BaseCode code = null;
        try {
            code = HttpInfoProvider.getInstance().getVerifyCode(req_info, lan, isres);
        } catch (DecodeException e) {
            e.printStackTrace();
        }
        return code;
    }

    public BaseCode verifyVerifyCode(String userName, String verifhCode) {
        BaseCode code = null;
        try {
            code = HttpInfoProvider.getInstance().verifyVerifyCode(userName, verifhCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public BaseCode findPsw(String userName, String psw, String confirmPsw, String verifyCode) throws Exception {
        BaseCode code = null;
        try {
            code = HttpInfoProvider.getInstance().findPsw(userName, psw, confirmPsw, verifyCode);
        } catch (DecodeException e) {
            e.printStackTrace();
        }
        return code;
    }

    public BaseCode modiPsw(String cc, String oldPsw, String newPsw) {
        BaseCode code = null;
        try {
            code = HttpInfoProvider.getInstance().modiPsw(cc, oldPsw, newPsw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public BaseCode setPhone(String mobile, String cc) {
        BaseCode code = null;
        try {
            code = HttpInfoProvider.getInstance().setPhone(mobile, cc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public BaseCode setName(String name, String cc) {
        BaseCode code = null;
        try {
            code = HttpInfoProvider.getInstance().setName(name, cc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public RegisterResult userRegist(String nationId, String loginKey, String nickName, String psw, String zipCode, String verifyCode, String email) {
        return HttpInfoProvider.getInstance().registeUser(nationId, loginKey, nickName, psw, zipCode, verifyCode, email);
    }
}
