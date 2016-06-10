package com.cnlaunch.x431pro.module.base;

import android.content.Context;
import android.text.TextUtils;
import com.cnlaunch.framework.common.Constants;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.framework.network.http.RequestParams;
import com.cnlaunch.framework.utils.MD5Utils;
import com.cnlaunch.framework.utils.NLog;
import com.cnlaunch.x431pro.module.config.ConfigDBManager;
import com.cnmobi.im.bo.ImConstants;
import com.ifoer.entity.Constant;
import com.ifoer.util.MySharedPreferences;
import java.io.File;
import java.util.Map;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.ksoap2.serialization.SoapObject;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParser;

public class BaseAction extends BusinessManager {
    protected ConfigDBManager configDB;
    protected Context mContext;
    protected RequestParams params;
    protected SoapObjectParams requestObj;
    private final String tag;

    public BaseAction(Context mContext) {
        super(mContext);
        this.tag = BaseAction.class.getSimpleName();
        this.mContext = mContext;
        this.configDB = ConfigDBManager.getInstance(mContext);
    }

    protected String getUrlByKey(String key) throws HttpException {
        return this.configDB.getUrlByKey(key);
    }

    protected RequestParams getRequestParams() {
        RequestParams params = new RequestParams();
        params.put(Constants.APP_ID, Constant.APP_ID);
        return params;
    }

    protected String getSignUrl(String url, RequestParams params) throws HttpException {
        StringBuilder urlBuilder = new StringBuilder(url);
        RequestParams signParams = new RequestParams();
        String user_id = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.CCKey);
        String token = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.TokenKey);
        if (TextUtils.isEmpty(user_id) || TextUtils.isEmpty(token)) {
            throw new HttpException("BaseManager getSignUrl method IllegalArgumentException.");
        }
        params.put(Constants.APP_ID, Constant.APP_ID);
        params.put(Constant.ACTION, getAction(url));
        params.put(ImConstants.USER_ID, user_id);
        params.put(Constants.VER, Constant.APP_VERSION);
        signParams.put(Constants.SIGN, MD5Utils.encrypt(params.getParamString() + token));
        signParams.put(ImConstants.USER_ID, user_id);
        signParams.put(Constants.APP_ID, Constant.APP_ID);
        signParams.put(Constants.VER, Constant.APP_VERSION);
        urlBuilder.append(AlixDefine.split).append(signParams.getParamString());
        params.remove(Constants.APP_ID);
        params.remove(Constant.ACTION);
        params.remove(Constants.VER);
        params.remove(ImConstants.USER_ID);
        NLog.m916d("Action", "Url=" + urlBuilder.toString());
        return urlBuilder.toString();
    }

    protected String getSignUrl(String url, RequestParams params, boolean flag) throws HttpException {
        StringBuilder urlBuilder = new StringBuilder(url);
        RequestParams signParams = new RequestParams();
        String user_id = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.CCKey);
        String token = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.TokenKey);
        if (TextUtils.isEmpty(user_id) || TextUtils.isEmpty(token)) {
            throw new HttpException("BaseManager getSignUrl method IllegalArgumentException.");
        }
        params.put(Constants.APP_ID, Constant.APP_ID);
        params.put(Constant.ACTION, getAction(url));
        params.put(ImConstants.USER_ID, user_id);
        params.put(Constants.VER, Constant.APP_VERSION);
        signParams.put(Constants.SIGN, MD5Utils.encrypt(params.getParamString() + token));
        signParams.put(ImConstants.USER_ID, user_id);
        signParams.put(Constants.APP_ID, Constant.APP_ID);
        signParams.put(Constants.VER, Constant.APP_VERSION);
        urlBuilder.append(AlixDefine.split).append(signParams.getParamString());
        params.remove(Constant.ACTION);
        params.remove(Constants.VER);
        return urlBuilder.toString();
    }

    private String getAction(String url) {
        String action = XmlPullParser.NO_NAMESPACE;
        if (!TextUtils.isEmpty(url)) {
            String[] urlArray = url.split(Constant.ACTION_REGEX);
            if (urlArray != null) {
                action = urlArray[1];
            }
        }
        NLog.m917e(this.tag, "getAction: " + action);
        return action;
    }

    protected SoapObjectParams getSoapObjectParams(String methodName) {
        return new SoapObjectParams(Constant.WEBSERVICE_NAMESPACE, methodName);
    }

    public Element[] createHead(SoapObject soapObject) {
        return createHead(true, soapObject);
    }

    public Element[] createHead(boolean needSign, SoapObject soapObject) {
        String user_id = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.CCKey);
        String token = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.TokenKey);
        Element[] header = new Element[]{new Element().createElement(Constant.WEBSERVICE_NAMESPACE, Constant.AUTHENTICATE)};
        Element ccElt = new Element().createElement(Constant.WEBSERVICE_NAMESPACE, MultipleAddresses.CC);
        ccElt.addChild(4, user_id);
        header[0].addChild(2, ccElt);
        if (needSign) {
            StringBuilder sign = new StringBuilder();
            if (soapObject != null) {
                int paramsLength = soapObject.getPropertyCount();
                for (int i = 0; i < paramsLength; i++) {
                    sign.append(String.valueOf(soapObject.getProperty(i)));
                }
            }
            sign.append(token);
            NLog.m917e(this.tag, "sign: " + sign);
            Element signElt = new Element().createElement(Constant.WEBSERVICE_NAMESPACE, Constants.SIGN);
            signElt.addChild(4, MD5Utils.encrypt(sign.toString()));
            header[0].addChild(2, signElt);
        }
        return header;
    }

    public String getSign(String logFilePath) {
        return MD5Utils.encrypt(new StringBuilder(String.valueOf(new File(logFilePath).getName().split("\\.")[0])).append("dbh23foup88lo56ad7865log46ke89y").toString());
    }

    public String uploadFile(String requestUrl, Map<String, String> map, String logFilePath) throws HttpException {
        return null;
    }
}
