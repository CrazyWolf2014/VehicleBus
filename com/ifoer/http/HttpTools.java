package com.ifoer.http;

import com.cnlaunch.framework.common.Constants;
import com.cnmobi.im.bo.ImConstants;
import com.ifoer.entity.Constant;
import com.ifoer.md5.MD5;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public class HttpTools {
    public static String getAction(String url) {
        return url.substring(url.indexOf("?") + 8);
    }

    public static String getSign(String token, Map<String, String> params) {
        return MD5.getMD5Str(createLinkString(params) + token);
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

    public static StringBuilder getAllUrl(String url, String user_id, String sign) {
        StringBuilder allUrl = new StringBuilder(url);
        Map<String, String> map = new HashMap();
        map.put(Constants.APP_ID, Constant.APP_ID);
        map.put(Constants.SIGN, sign);
        map.put(ImConstants.USER_ID, user_id);
        map.put(Constants.VER, Constant.APP_VERSION);
        List<String> keys = new ArrayList(map.keySet());
        allUrl.append(AlixDefine.split);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) map.get(key);
            if (i == keys.size() - 1) {
                allUrl = allUrl.append(new StringBuilder(String.valueOf(key)).append("=").append(value).toString());
            } else {
                allUrl = allUrl.append(new StringBuilder(String.valueOf(key)).append("=").append(value).append(AlixDefine.split).toString());
            }
        }
        return allUrl;
    }

    public static String getUrlTool(StringBuilder sb, Map<String, String> params) {
        List<String> keys = new ArrayList(params.keySet());
        String url = sb.toString() + AlixDefine.split;
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) params.get(key);
            if (i == keys.size() - 1) {
                url = new StringBuilder(String.valueOf(url)).append(key).append("=").append(value).toString();
            } else {
                url = new StringBuilder(String.valueOf(url)).append(key).append("=").append(value).append(AlixDefine.split).toString();
            }
        }
        return url;
    }
}
