package com.cnlaunch.x431pro.module.user.action;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.cnlaunch.framework.common.CacheManager;
import com.cnlaunch.framework.common.Constants;
import com.cnlaunch.framework.common.KeyConstant;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.x431pro.module.base.BaseAction;
import com.cnlaunch.x431pro.module.base.CommonResponse;
import com.cnlaunch.x431pro.module.user.model.CityListResponse;
import com.cnlaunch.x431pro.module.user.model.ConfParam;
import com.cnlaunch.x431pro.module.user.model.ConfResponse;
import com.cnlaunch.x431pro.module.user.model.ContactResponse;
import com.cnlaunch.x431pro.module.user.model.Country;
import com.cnlaunch.x431pro.module.user.model.CountryListResponse;
import com.cnlaunch.x431pro.module.user.model.ExtParam;
import com.cnlaunch.x431pro.module.user.model.LoginResponse;
import com.cnlaunch.x431pro.module.user.model.PriconfResponse;
import com.cnlaunch.x431pro.module.user.model.ProvinceListResponse;
import com.cnlaunch.x431pro.module.user.model.RegistResponse;
import com.cnlaunch.x431pro.module.user.model.SendClientVersionResponse;
import com.cnlaunch.x431pro.module.user.model.UserBaseInfoResponse;
import com.cnlaunch.x431pro.module.user.model.UserParam;
import com.cnlaunch.x431pro.module.user.model.UserSettingInfo;
import com.cnmobi.im.dto.Msg;
import com.tencent.mm.sdk.plugin.BaseProfile;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class UserAction extends BaseAction {
    public UserAction(Context mContext) {
        super(mContext);
    }

    public CommonResponse verifyReqSendCode(String req_info, String lan, String isres) throws HttpException {
        String url = getUrlByKey(KeyConstant.verify_req_send_code);
        this.params = getRequestParams();
        this.params.put("req_info", req_info);
        this.params.put("lan", lan);
        this.params.put("isres", isres);
        return postCommonResponse(url, this.params);
    }

    public CommonResponse verifyReqConfirmCode(String req_info, String code) throws HttpException {
        String url = getUrlByKey(KeyConstant.verify_verify_code);
        this.params = getRequestParams();
        this.params.put("req_info", req_info);
        this.params.put("verify_code", code);
        return postCommonResponse(url, this.params);
    }

    public LoginResponse login(String login_key, String password, String time, String type, String device_token) throws HttpException {
        String url = getUrlByKey(KeyConstant.login);
        this.params = getRequestParams();
        this.params.put("login_key", login_key);
        this.params.put("password", password);
        this.params.put(Msg.TIME_REDIO, time);
        this.params.put(SharedPref.TYPE, type);
        this.params.put("device_token", device_token);
        String json = this.httpManager.post(url, this.params);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return (LoginResponse) jsonToBean(json, LoginResponse.class);
    }

    public RegistResponse regist(String nation_id, String loginKey, String verify_code, String password, String nick_name) throws HttpException {
        String url = getUrlByKey(KeyConstant.register);
        this.params = getRequestParams();
        this.params.put("nation_id", nation_id);
        this.params.put("loginKey", loginKey);
        this.params.put("verify_code", verify_code);
        this.params.put("password", password);
        this.params.put("nick_name", nick_name);
        String json = this.httpManager.post(url, this.params);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return (RegistResponse) jsonToBean(json, RegistResponse.class);
    }

    public RegistResponse regist(String nation_id, String loginKey, String verify_code, String password, String app_id, String nick_name, String email) throws HttpException {
        String url = getUrlByKey(KeyConstant.register);
        this.params = getRequestParams();
        this.params.put("nation_id", nation_id);
        this.params.put("loginKey", loginKey);
        this.params.put("verify_code", verify_code);
        this.params.put("password", password);
        this.params.put(Constants.APP_ID, app_id);
        this.params.put("nick_name", nick_name);
        this.params.put("email", email);
        String json = this.httpManager.post(url, this.params);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return (RegistResponse) jsonToBean(json, RegistResponse.class);
    }

    public RegistResponse regist(String nation_id, String loginKey, String verify_code, String password, String app_id, String nick_name, String email, int reg_source, String zipcode) throws HttpException {
        String url = getUrlByKey(KeyConstant.register);
        this.params = getRequestParams();
        this.params.put("nation_id", nation_id);
        this.params.put("loginKey", loginKey);
        this.params.put("verify_code", verify_code);
        this.params.put("password", password);
        this.params.put(Constants.APP_ID, app_id);
        this.params.put("nick_name", nick_name);
        this.params.put("email", email);
        this.params.put("reg_source", Integer.valueOf(reg_source));
        this.params.put("zipcode", zipcode);
        String json = this.httpManager.post(url, this.params);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return (RegistResponse) jsonToBean(json, RegistResponse.class);
    }

    public CommonResponse verifyResetPass(String req, String pass, String confirm_pass, String verify_code) throws HttpException {
        String url = getUrlByKey(KeyConstant.verify_reset_pass);
        this.params = getRequestParams();
        this.params.put("req", req);
        this.params.put("pass", pass);
        this.params.put("confirm_pass", confirm_pass);
        this.params.put("verify_code", verify_code);
        return postCommonResponse(url, this.params);
    }

    public CommonResponse modifyPassWord(String oldPwd, String newPwd) throws HttpException {
        String url = getUrlByKey(KeyConstant.userinfo_set_password);
        this.params = getRequestParams();
        this.params.put("pw", oldPwd);
        this.params.put("chpw", newPwd);
        return postCommonResponse(getSignUrl(url, this.params), this.params);
    }

    public CountryListResponse getCountryList(String lan) throws HttpException {
        List<Country> list;
        String url = getUrlByKey(KeyConstant.area_get_country_list);
        this.params = getRequestParams();
        this.params.put("lan", lan);
        CountryListResponse countryListResponse = null;
        String key = getCacheKey(url, this.params);
        if (CacheManager.isInvalidCache(key, 2592000)) {
            countryListResponse = (CountryListResponse) CacheManager.readObject(key);
            if (countryListResponse != null && countryListResponse.getCode() == 0) {
                list = countryListResponse.getData();
                if (list != null && list.size() > 0) {
                    return countryListResponse;
                }
            }
        }
        String json = this.httpManager.post(url, this.params);
        if (!TextUtils.isEmpty(json)) {
            countryListResponse = (CountryListResponse) jsonToBean(json, CountryListResponse.class);
            if (countryListResponse != null && countryListResponse.getCode() == 0) {
                list = countryListResponse.getData();
                if (list != null && list.size() > 0) {
                    CacheManager.writeObject(countryListResponse, key);
                }
            }
        }
        return countryListResponse;
    }

    public CommonResponse logout() throws HttpException {
        String url = getUrlByKey(KeyConstant.logout);
        this.params = getRequestParams();
        return postCommonResponse(getSignUrl(url, this.params), this.params);
    }

    public UserBaseInfoResponse getBaseInfo(String lan, String target_id) throws HttpException {
        String url = getUrlByKey(KeyConstant.user_get_base_info_car_logo);
        this.params = getRequestParams();
        this.params.put("lan", lan);
        if (target_id != null) {
            this.params.put("target_id", target_id);
        }
        UserBaseInfoResponse response = null;
        String json = this.httpManager.post(getSignUrl(url, this.params), this.params);
        if (!TextUtils.isEmpty(json)) {
            response = (UserBaseInfoResponse) jsonToBean(json, UserBaseInfoResponse.class);
        }
        Log.d("Sanda", "getBaseInfo=" + json);
        return response;
    }

    public CommonResponse setBase(UserParam userbase) throws HttpException {
        String url = getUrlByKey(KeyConstant.user_set_base);
        this.params = getRequestParams();
        this.params.put("email", userbase.getEmail());
        this.params.put("mobile", userbase.getMobile());
        this.params.put("vcode", userbase.getVcode());
        this.params.put("name", userbase.getName());
        this.params.put("sex", userbase.getSex());
        this.params.put(BaseProfile.COL_SIGNATURE, userbase.getSignature());
        this.params.put("uname", userbase.getUname());
        this.params.put("identity_tag", userbase.getIdentity_tag());
        this.params.put("lang", userbase.getLang());
        return postCommonResponse(getSignUrl(url, this.params), this.params);
    }

    public CommonResponse setArea(String ncode, String pcode, String ccode) throws HttpException {
        String url = getUrlByKey(KeyConstant.user_set_area);
        this.params = getRequestParams();
        this.params.put("ncode", ncode);
        this.params.put("pcode", pcode);
        this.params.put("ccode", ccode);
        return postCommonResponse(getSignUrl(url, this.params), this.params);
    }

    public CommonResponse unbindTel() throws HttpException {
        String url = getUrlByKey(KeyConstant.user_unbind_tel);
        this.params = getRequestParams();
        return postCommonResponse(getSignUrl(url, this.params), this.params);
    }

    public CommonResponse unbindEmail() throws HttpException {
        String url = getUrlByKey(KeyConstant.user_unbind_email);
        this.params = getRequestParams();
        return postCommonResponse(getSignUrl(url, this.params), this.params);
    }

    public ContactResponse getContact() throws HttpException {
        String url = getUrlByKey(KeyConstant.user_get_contact);
        this.params = getRequestParams();
        String json = this.httpManager.post(getSignUrl(url, this.params), this.params);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return (ContactResponse) jsonToBean(json, ContactResponse.class);
    }

    public CommonResponse setExt(ExtParam extinfo) throws HttpException {
        String url = getUrlByKey(KeyConstant.user_set_ext);
        this.params = getRequestParams();
        this.params.put("qq", extinfo.getQq());
        this.params.put(BaseProfile.COL_WEIBO, extinfo.getWeibo());
        this.params.put("facebook", extinfo.getFacebook());
        this.params.put("twitter", extinfo.getTwitter());
        this.params.put("hobby", extinfo.getHobby());
        return postCommonResponse(getSignUrl(url, this.params), this.params);
    }

    public PriconfResponse getPriconf() throws HttpException {
        String url = getUrlByKey(KeyConstant.user_get_priconf);
        this.params = getRequestParams();
        String json = this.httpManager.post(getSignUrl(url, this.params), this.params);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return (PriconfResponse) jsonToBean(json, PriconfResponse.class);
    }

    public CommonResponse setConf(ConfParam confinfo) throws HttpException {
        String url = getUrlByKey(KeyConstant.user_set_conf);
        this.params = getRequestParams();
        this.params.put("is_verify", confinfo.getIs_verify());
        this.params.put("find_by_tel", confinfo.getFind_by_tel());
        this.params.put("fontsize", confinfo.getFontsize());
        this.params.put("is_shock", confinfo.getIs_shock());
        this.params.put("open_space", confinfo.getOpen_space());
        this.params.put("recommend", confinfo.getRecommend());
        this.params.put("sound", confinfo.getSound());
        this.params.put("visible", confinfo.getVisible());
        this.params.put("allow_vmt_find", confinfo.getAllow_vmt_find());
        return postCommonResponse(getSignUrl(url, this.params), this.params);
    }

    public ConfResponse getConf() throws HttpException {
        String url = getUrlByKey(KeyConstant.user_get_common);
        this.params = getRequestParams();
        String json = this.httpManager.post(getSignUrl(url, this.params), this.params);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return (ConfResponse) jsonToBean(json, ConfResponse.class);
    }

    public CommonResponse getRandHobby() throws HttpException {
        String url = getUrlByKey(KeyConstant.user_get_hobby);
        this.params = getRequestParams();
        return getCommonResponse(getSignUrl(url, this.params), this.params);
    }

    public CommonResponse getHobby() throws HttpException {
        String url = getUrlByKey(KeyConstant.user_get_rand_hobby);
        this.params = getRequestParams();
        return getCommonResponse(getSignUrl(url, this.params), this.params);
    }

    public CommonResponse setSex(String sex) throws HttpException {
        String url = getUrlByKey(KeyConstant.user_set_base);
        this.params = getRequestParams();
        this.params.put("sex", sex);
        return postCommonResponse(getSignUrl(url, this.params), this.params);
    }

    public ProvinceListResponse getProvinceList(String lan, String ncode) throws HttpException {
        String url = getUrlByKey(KeyConstant.area_get_province);
        this.params = getRequestParams();
        this.params.put("lan", lan);
        this.params.put("ncode", ncode);
        String json = this.httpManager.post(getSignUrl(url, this.params), this.params);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return (ProvinceListResponse) jsonToBean(json, ProvinceListResponse.class);
    }

    public CityListResponse getCityList(String lan, String pcode) throws HttpException {
        String url = getUrlByKey(KeyConstant.area_get_city);
        this.params = getRequestParams();
        this.params.put("lan", lan);
        this.params.put("pcode", pcode);
        String json = this.httpManager.post(getSignUrl(url, this.params), this.params);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return (CityListResponse) jsonToBean(json, CityListResponse.class);
    }

    public CommonResponse modifyPhoneNumber(String phonenumber) throws HttpException {
        String url = getUrlByKey(KeyConstant.userinfo_update_user);
        this.params = getRequestParams();
        this.params.put("mobile", phonenumber);
        return postCommonResponse(getSignUrl(url, this.params), this.params);
    }

    public SendClientVersionResponse sendClientVersion(String serialNo, String softType, String softName, String version) throws HttpException {
        String url = getUrlByKey(KeyConstant.send_Client_Version);
        this.requestObj = getSoapObjectParams("sendClientVersion");
        this.requestObj.addProperty(com.cnlaunch.x431pro.common.Constants.serialNo, serialNo);
        this.requestObj.addProperty("softType", softType);
        this.requestObj.addProperty("softName", softName);
        this.requestObj.addProperty("verson", version);
        try {
            this.httpTransport = getHttpTransport(url);
            this.envelope = getSoapSerializationEnvelope(createHead(this.requestObj), this.requestObj);
            this.httpTransport.call(XmlPullParser.NO_NAMESPACE, this.envelope);
            if (this.envelope != null) {
                return (SendClientVersionResponse) soapToBean(SendClientVersionResponse.class);
            }
            return null;
        } catch (Throwable e) {
            throw new HttpException(e);
        } catch (Throwable e2) {
            throw new HttpException(e2);
        }
    }

    public CommonResponse setUserInfo(UserSettingInfo info) throws HttpException {
        String url = getUrlByKey(KeyConstant.user_set_base);
        this.params = getRequestParams();
        if (info.getNickname() != null) {
            this.params.put("name", info.getNickname());
        }
        if (info.getSex() != -1) {
            this.params.put("sex", new StringBuilder(String.valueOf(info.getSex())).toString());
        }
        if (info.getSignature() != null) {
            this.params.put(BaseProfile.COL_SIGNATURE, info.getSignature());
        }
        if (info.getTag() != null) {
            this.params.put("identity_tag", info.getTag());
        }
        return postCommonResponse(getSignUrl(url, this.params), this.params);
    }
}
