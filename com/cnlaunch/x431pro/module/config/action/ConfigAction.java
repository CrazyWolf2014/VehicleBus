package com.cnlaunch.x431pro.module.config.action;

import android.content.Context;
import android.text.TextUtils;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.framework.network.http.RequestParams;
import com.cnlaunch.framework.utils.NLog;
import com.cnlaunch.x431pro.module.base.BaseAction;
import com.cnlaunch.x431pro.module.config.model.ConfigRespose;
import com.cnlaunch.x431pro.module.config.model.GetIpAreaResponse;
import com.cnlaunch.x431pro.utils.Tools;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

public class ConfigAction extends BaseAction {
    private final String tag;

    public ConfigAction(Context mContext) {
        super(mContext);
        this.tag = ConfigAction.class.getSimpleName();
    }

    public GetIpAreaResponse getIpArea(String ip) throws HttpException {
        try {
            this.params = new RequestParams();
            this.params.put("ip", ip);
            String json = this.httpManager.get("http://ip.taobao.com/service/getIpInfo.php", this.params);
            NLog.m916d("Config", "json" + json.toString());
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            return (GetIpAreaResponse) jsonToBean(json, GetIpAreaResponse.class);
        } catch (Exception e) {
            return null;
        }
    }

    public ConfigRespose getConfigService(String url, String config_no, String area) throws HttpException {
        if (TextUtils.isEmpty(url)) {
            throw new HttpException("getConfigService url is not null.");
        }
        this.params = getRequestParams();
        this.params.put("config_no", config_no);
        this.params.put("area", area);
        String json = this.httpManager.get(url, this.params);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return (ConfigRespose) jsonToBean(json, ConfigRespose.class);
    }

    public String getIp() {
        int start;
        int end;
        String ip = XmlPullParser.NO_NAMESPACE;
        try {
            String returnSN = this.httpManager.get("http://pv.sohu.com/cityjson?ie=utf-8");
            if (!TextUtils.isEmpty(returnSN)) {
                start = returnSN.indexOf("{");
                end = returnSN.indexOf("}");
                if (start == -1 || end == -1) {
                    ip = XmlPullParser.NO_NAMESPACE;
                } else {
                    NLog.m917e("Sanda", "sohujsonStr=" + returnSN.substring(start, end + 1));
                    ip = new JSONObject(jsonStr).getString("cip");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ip = XmlPullParser.NO_NAMESPACE;
        } catch (Exception e2) {
            ip = XmlPullParser.NO_NAMESPACE;
        }
        if (Tools.checkIpSuccess(ip)) {
            return ip;
        }
        String json;
        try {
            json = this.httpManager.get("http://city.ip138.com/ip2city.asp");
            if (!TextUtils.isEmpty(json)) {
                start = json.indexOf("[");
                end = json.indexOf("]");
                if (start == -1 || end == -1) {
                    ip = XmlPullParser.NO_NAMESPACE;
                }
                ip = json.replace("[", XmlPullParser.NO_NAMESPACE).replace("]", XmlPullParser.NO_NAMESPACE).substring(start, end).trim();
                NLog.m917e("Sanda", "city.ip138 ip : " + ip);
            }
        } catch (Exception e3) {
            ip = XmlPullParser.NO_NAMESPACE;
        }
        if (Tools.checkIpSuccess(ip)) {
            return ip;
        }
        try {
            json = this.httpManager.get("http://20140507.ip138.com/ic.asp");
            if (!TextUtils.isEmpty(json)) {
                start = json.indexOf("[");
                end = json.indexOf("]");
                if (start == -1 || end == -1) {
                    return XmlPullParser.NO_NAMESPACE;
                }
                ip = json.replace("[", XmlPullParser.NO_NAMESPACE).replace("]", XmlPullParser.NO_NAMESPACE).substring(start, end).trim();
                NLog.m917e("Sanda", "ip : " + ip);
            }
            return ip;
        } catch (Exception e4) {
            return XmlPullParser.NO_NAMESPACE;
        }
    }
}
