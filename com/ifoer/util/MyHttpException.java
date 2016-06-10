package com.ifoer.util;

import android.content.Context;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.expeditionphone.MainActivity;
import java.util.Properties;
import org.xmlpull.v1.XmlPullParser;

public class MyHttpException extends Exception {
    public static final int ERROR_CANCELED_ORDER = 761;
    public static final int ERROR_CIRCLE_NOT_EXIST = 723;
    public static final int ERROR_CURRENCY_TYPE = 757;
    public static final int ERROR_DEALER_CODE = 656;
    public static final int ERROR_DONT_HAVE_ORDER = 753;
    public static final int ERROR_EMALI_OTHERSUAED = 382;
    public static final int ERROR_EMPTY_CIRCLE = 720;
    public static final int ERROR_EMPTY_CIRCLE_SORT = 722;
    public static final int ERROR_EMPTY_CONFING = 608;
    public static final int ERROR_EMPTY_ORDERNAME = 768;
    public static final int ERROR_EMPTY_SOFTLIST = 607;
    public static final int ERROR_EXCEED_TEST_TIME = 380;
    public static final int ERROR_FAIL_PASW_PROTECTION = 385;
    public static final int ERROR_INVALID_AUTH_CODE = 378;
    public static final int ERROR_INVALID_ORDER = 765;
    public static final int ERROR_INVALID_ORDER_PPAY = 754;
    public static final int ERROR_NETWORK = 501;
    public static final int ERROR_NOEXIST_PACKAGE = 773;
    public static final int ERROR_NOEXIST_SERIAL_NUB = 661;
    public static final int ERROR_NOTTHIS_LAN_SOFT = 606;
    public static final int ERROR_NOT_BELONG_CIRCLE = 724;
    public static final int ERROR_NOT_EXIST_AREAID = 609;
    public static final int ERROR_NOT_EXIST_DOC = 610;
    public static final int ERROR_NOT_HAVE_THIS_SOFT = 751;
    public static final int ERROR_NOT_REG_THIS = 662;
    public static final int ERROR_NOT_SELL = 650;
    public static final int ERROR_NOT_SET_EMAIL = 397;
    public static final int ERROR_NOT_SET_PHONE = 381;
    public static final int ERROR_NOT_SET_PRICE = 750;
    public static final int ERROR_NULLIFY = 652;
    public static final int ERROR_ORDER_CANCELLED = 755;
    public static final int ERROR_ORDER_OFFLINE_PAY = 764;
    public static final int ERROR_ORDER_PAID = 756;
    public static final int ERROR_OTHER_REGISTER = 659;
    public static final int ERROR_PAID_ORDER = 762;
    public static final int ERROR_PARAMETER = 400;
    public static final int ERROR_PARAMETER_EMPTY = 401;
    public static final int ERROR_PARAMETER_ILLEGAL = 402;
    public static final int ERROR_PASTDUE_AUTH_CODE = 379;
    public static final int ERROR_PASW = 383;
    public static final int ERROR_PASW_PD = 655;
    public static final int ERROR_PRODUCT_CONF_EMPTY = 660;
    public static final int ERROR_REFUNDED_ORDER = 763;
    public static final int ERROR_REGISTERED = 651;
    public static final int ERROR_RESULT_NOT_EXIST = 405;
    public static final int ERROR_SERIAL_NOEXIST = 658;
    public static final int ERROR_SERIAL_NUMBER = 759;
    public static final int ERROR_SERVER = 500;
    public static final int ERROR_SOFT_ERROR_ORDER = 766;
    public static final int ERROR_SOFT_PRICE = 758;
    public static final int ERROR_SOFT_PURCHASED = 760;
    public static final int ERROR_TOTAL_PRICE = 752;
    public static final int ERROR_TOTAL_PRICE_ZERO = 767;
    public static final int ERROR_UNLOGIN_USER = 772;
    public static final int ERROR_UNREGISTER = 653;
    public static final int ERROR_UNREGISTER_PRODUCT = 770;
    public static final int ERROR_UNSUPPORT_CURRENCY_TYPE = 769;
    public static final int ERROR_USERNAME_FORM = 393;
    public static final int ERROR_USERNAME_REPEAT = 396;
    public static final int ERROR_USERPASW_FORM = 392;
    public static final int ERROR_USERPHONE_FORM = 394;
    public static final int ERROR_USER_EMAIL = 395;
    public static final int ERROR_USER_NONENTITY = 384;
    public static final int ERROR_USER_PRODUCT_MISMATCHING = 771;
    public static final int ERROR_USER_STATE = 398;
    public static final int IDIAG_PACKAGE_EXISTS = 777;
    public static final int SUCCESS = 0;
    private static Properties codeProperties = null;
    private static final long serialVersionUID = 1;
    private int code;
    private String errormessage;

    static {
        codeProperties = new Properties();
        codeProperties.put(Integer.valueOf(IDIAG_PACKAGE_EXISTS), MainActivity.contexts.getResources().getString(C0136R.string.not_need_repeated));
        codeProperties.put(Integer.valueOf(ERROR_NOEXIST_PACKAGE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NOEXIST_PACKAGE));
        codeProperties.put(Integer.valueOf(ERROR_UNLOGIN_USER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_USER_PRODUCT_MISMATCHING));
        codeProperties.put(Integer.valueOf(ERROR_USER_PRODUCT_MISMATCHING), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_USER_PRODUCT_MISMATCHING));
        codeProperties.put(Integer.valueOf(ERROR_UNREGISTER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_USER_PRODUCT_MISMATCHING));
        codeProperties.put(Integer.valueOf(ERROR_UNREGISTER_PRODUCT), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_UNREGISTER_PRODUCT));
        codeProperties.put(Integer.valueOf(ERROR_UNSUPPORT_CURRENCY_TYPE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_UNSUPPORT_CURRENCY_TYPE));
        codeProperties.put(Integer.valueOf(ERROR_EMPTY_ORDERNAME), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_EMPTY_ORDERNAME));
        codeProperties.put(Integer.valueOf(ERROR_TOTAL_PRICE_ZERO), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_TOTAL_PRICE_ZERO));
        codeProperties.put(Integer.valueOf(ERROR_SOFT_ERROR_ORDER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_SOFT_ERROR_ORDER));
        codeProperties.put(Integer.valueOf(ERROR_INVALID_ORDER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_INVALID_ORDER));
        codeProperties.put(Integer.valueOf(ERROR_ORDER_OFFLINE_PAY), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_ORDER_OFFLINE_PAY));
        codeProperties.put(Integer.valueOf(ERROR_REFUNDED_ORDER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_REFUNDED_ORDER));
        codeProperties.put(Integer.valueOf(ERROR_PAID_ORDER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_PAID_ORDER));
        codeProperties.put(Integer.valueOf(ERROR_CANCELED_ORDER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_CANCELED_ORDER));
        codeProperties.put(Integer.valueOf(ERROR_SOFT_PURCHASED), MainActivity.contexts.getResources().getString(C0136R.string.not_need_repeated));
        codeProperties.put(Integer.valueOf(ERROR_SERIAL_NUMBER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_SERIAL_NUMBER));
        codeProperties.put(Integer.valueOf(ERROR_SOFT_PRICE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_SOFT_PRICE));
        codeProperties.put(Integer.valueOf(ERROR_CURRENCY_TYPE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_CURRENCY_TYPE));
        codeProperties.put(Integer.valueOf(ERROR_TOTAL_PRICE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_TOTAL_PRICE));
        codeProperties.put(Integer.valueOf(ERROR_NOT_HAVE_THIS_SOFT), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NOT_HAVE_THIS_SOFT));
        codeProperties.put(Integer.valueOf(ERROR_NOT_SET_PRICE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NOT_SET_PRICE));
        codeProperties.put(Integer.valueOf(ERROR_SERIAL_NOEXIST), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_SERIAL_NOEXIST));
        codeProperties.put(Integer.valueOf(ERROR_OTHER_REGISTER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_OTHER_REGISTER));
        codeProperties.put(Integer.valueOf(ERROR_PRODUCT_CONF_EMPTY), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_PRODUCT_CONF_EMPTY));
        codeProperties.put(Integer.valueOf(ERROR_NOEXIST_SERIAL_NUB), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NOEXIST_SERIAL_NUB));
        codeProperties.put(Integer.valueOf(ERROR_NOT_REG_THIS), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NOT_REG_THIS));
        codeProperties.put(Integer.valueOf(ERROR_NOTTHIS_LAN_SOFT), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NOTTHIS_LAN_SOFT));
        codeProperties.put(Integer.valueOf(ERROR_EMPTY_SOFTLIST), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_EMPTY_SOFTLIST));
        codeProperties.put(Integer.valueOf(ERROR_EMPTY_CONFING), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_EMPTY_CONFING));
        codeProperties.put(Integer.valueOf(ERROR_NOT_EXIST_AREAID), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NOT_EXIST_AREAID));
        codeProperties.put(Integer.valueOf(ERROR_NOT_EXIST_DOC), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NOT_EXIST_DOC));
        codeProperties.put(Integer.valueOf(ERROR_NOT_SELL), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NOT_SELL));
        codeProperties.put(Integer.valueOf(ERROR_REGISTERED), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_REGISTERED));
        codeProperties.put(Integer.valueOf(ERROR_NULLIFY), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NULLIFY));
        codeProperties.put(Integer.valueOf(ERROR_PASW_PD), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_PASW_PD));
        codeProperties.put(Integer.valueOf(ERROR_DEALER_CODE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_DEALER_CODE));
        codeProperties.put(Integer.valueOf(ERROR_PARAMETER_ILLEGAL), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_PARAMETER_ILLEGAL));
        codeProperties.put(Integer.valueOf(ERROR_RESULT_NOT_EXIST), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_RESULT_NOT_EXIST));
        codeProperties.put(Integer.valueOf(ERROR_SERVER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_SERVER));
        codeProperties.put(Integer.valueOf(ERROR_NETWORK), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NETWORK));
        codeProperties.put(Integer.valueOf(ERROR_PARAMETER_EMPTY), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_PARAMETER_EMPTY));
        codeProperties.put(Integer.valueOf(ERROR_PARAMETER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_PARAMETER));
        codeProperties.put(Integer.valueOf(ERROR_USER_STATE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_USER_STATE));
        codeProperties.put(Integer.valueOf(ERROR_NOT_SET_EMAIL), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NOT_SET_EMAIL));
        codeProperties.put(Integer.valueOf(SUCCESS), MainActivity.contexts.getResources().getString(C0136R.string.SUCCESS));
        codeProperties.put(Integer.valueOf(ERROR_INVALID_AUTH_CODE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_INVALID_AUTH_CODE));
        codeProperties.put(Integer.valueOf(ERROR_PASTDUE_AUTH_CODE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_PASTDUE_AUTH_CODE));
        codeProperties.put(Integer.valueOf(ERROR_EXCEED_TEST_TIME), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_EXCEED_TEST_TIME));
        codeProperties.put(Integer.valueOf(ERROR_NOT_SET_PHONE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NOT_SET_PHONE));
        codeProperties.put(Integer.valueOf(ERROR_PASW), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_PASW));
        codeProperties.put(Integer.valueOf(ERROR_USER_NONENTITY), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_USER_NONENTITY));
        codeProperties.put(Integer.valueOf(ERROR_FAIL_PASW_PROTECTION), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_FAIL_PASW_PROTECTION));
        codeProperties.put(Integer.valueOf(ERROR_USERPASW_FORM), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_USERPASW_FORM));
        codeProperties.put(Integer.valueOf(ERROR_USERNAME_FORM), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_USERNAME_FORM));
        codeProperties.put(Integer.valueOf(ERROR_USERPHONE_FORM), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_USERPHONE_FORM));
        codeProperties.put(Integer.valueOf(ERROR_USER_EMAIL), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_USER_EMAIL));
        codeProperties.put(Integer.valueOf(ERROR_USERNAME_REPEAT), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_USERNAME_REPEAT));
        codeProperties.put(Integer.valueOf(ERROR_DONT_HAVE_ORDER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_DONT_HAVE_ORDER));
        codeProperties.put(Integer.valueOf(ERROR_INVALID_ORDER_PPAY), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_INVALID_ORDER_PPAY));
        codeProperties.put(Integer.valueOf(ERROR_ORDER_CANCELLED), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_ORDER_CANCELLED));
        codeProperties.put(Integer.valueOf(ERROR_ORDER_PAID), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_ORDER_PAID));
        codeProperties.put(Integer.valueOf(ERROR_EMPTY_CIRCLE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_EMPTY_CIRCLE));
        codeProperties.put(Integer.valueOf(ERROR_EMPTY_CIRCLE_SORT), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_EMPTY_CIRCLE_SORT));
        codeProperties.put(Integer.valueOf(ERROR_CIRCLE_NOT_EXIST), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_CIRCLE_NOT_EXIST));
        codeProperties.put(Integer.valueOf(ERROR_NOT_BELONG_CIRCLE), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_NOT_BELONG_CIRCLE));
    }

    public MyHttpException(int code) {
        this.code = SUCCESS;
        this.code = code;
    }

    public MyHttpException(String errormessage) {
        this.code = SUCCESS;
        this.errormessage = errormessage;
    }

    public MyHttpException(int code, String errormessage) {
        this.code = SUCCESS;
        this.errormessage = errormessage;
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        if (this.code != 0) {
            return (String) codeProperties.get(Integer.valueOf(this.code));
        }
        return this.errormessage;
    }

    public void showToast(Context context) {
        String message = getMessage();
        if (message == null || message.equals(XmlPullParser.NO_NAMESPACE)) {
            message = context.getResources().getString(C0136R.string.notic_serv);
        }
        Toast.makeText(context, message, SUCCESS).show();
    }
}
