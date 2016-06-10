package com.cnlaunch.framework.common;

import android.os.Environment;

public abstract class Constants {
    public static final String ACTION = "action";
    public static final String ACTION_REGEX = "action=";
    public static final String ALIPAY_WEBSITE_PATH = "http://mycar.x431.com/services/alipay/enterMobileAlipay.action?orderSn=";
    public static final String APP_ID = "app_id";
    public static final String APP_ID_VALUE = "921";
    public static final String AUTHENTICATE = "authenticate";
    public static final String CC = "cc";
    public static final String EXPIRED_REMIND = "expired_remind";
    public static final String GOOGLE_GEOCODE_PATH = "http://maps.google.com/maps/api/geocode/json";
    public static final String LANG = "Lang";
    public static String LOCAL_BASE_PATH = null;
    public static final String MYCAR_WEBSERVICE_URL = "http://mycar.x431.com/services/";
    public static final int PAYPAL_INIT_SUCCESS = 2000;
    public static final String PAYPAL_WEBSITE_PATH = "http://mycar.x431.com/services/paypal/enterMobilePaypal.action?orderSn=";
    public static final String SIGN = "sign";
    public static final String THEME = "Theme";
    public static final String TOKEN = "token";
    public static final String UC_WEBSERVICE_URL = "http://uc.x431.com/services/";
    public static final String USER_ID = "user_id";
    public static final String USER_PUBLIC_ID = "USER_PUBLIC_ID";
    public static final String USER_PUBLIC_NAME = "USER_PUBLIC_NAME";
    public static final String VER = "ver";
    public static final String VER_VALUE = "1.0";
    public static final String WEBSERVICE_NAMESPACE = "http://www.x431.com";
    public static final String WEBSERVICE_SOAPACION = "";

    static {
        LOCAL_BASE_PATH = Environment.getExternalStorageDirectory() + "/cnlaunch/";
    }
}
