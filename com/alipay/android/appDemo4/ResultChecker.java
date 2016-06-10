package com.alipay.android.appDemo4;

import com.cnlaunch.framework.common.Constants;
import org.jivesoftware.smackx.Form;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

public class ResultChecker {
    public static final int RESULT_CHECK_SIGN_FAILED = 1;
    public static final int RESULT_CHECK_SIGN_SUCCEED = 2;
    public static final int RESULT_INVALID_PARAM = 0;
    String mContent;

    public ResultChecker(String content) {
        this.mContent = content;
    }

    public int checkSign() {
        try {
            String result = BaseHelper.string2JSON(this.mContent, ";").getString(Form.TYPE_RESULT);
            result = result.substring(RESULT_CHECK_SIGN_FAILED, result.length() - 1);
            String signContent = result.substring(0, result.indexOf("&sign_type="));
            JSONObject objResult = BaseHelper.string2JSON(result, AlixDefine.split);
            String signType = objResult.getString(AlixDefine.sign_type).replace("\"", XmlPullParser.NO_NAMESPACE);
            String sign = objResult.getString(Constants.SIGN).replace("\"", XmlPullParser.NO_NAMESPACE);
            if (!signType.equalsIgnoreCase("RSA") || Rsa.doCheck(signContent, sign, PartnerConfig.RSA_PRIVATE)) {
                return RESULT_CHECK_SIGN_SUCCEED;
            }
            return RESULT_CHECK_SIGN_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
