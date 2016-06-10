package com.ifoer.mine;

import com.cnlaunch.framework.common.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public class SignUtils {
    public static Map<String, String> paraFilter(Map<String, String> params) {
        Map<String, String> result = new HashMap();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                String value = (String) params.get(key);
                if (!(value == null || value.equals(XmlPullParser.NO_NAMESPACE) || key.equalsIgnoreCase(Constants.SIGN))) {
                    result.put(key, value);
                }
            }
        }
        return result;
    }

    public static String createLinkString(Map<String, String> params) {
        Map<String, String> filterParams = paraFilter(params);
        List<String> keys = new ArrayList(filterParams.keySet());
        Collections.sort(keys);
        String prestr = XmlPullParser.NO_NAMESPACE;
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) filterParams.get(key);
            if (i == keys.size() - 1) {
                prestr = new StringBuilder(String.valueOf(prestr)).append(key).append("=").append(value).toString();
            } else {
                prestr = new StringBuilder(String.valueOf(prestr)).append(key).append("=").append(value).append(AlixDefine.split).toString();
            }
        }
        return prestr;
    }

    public static String getSign(String token, Map<String, String> params) {
        return MD5Util.MD5(createLinkString(params) + token);
    }
}
