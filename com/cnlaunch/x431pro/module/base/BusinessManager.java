package com.cnlaunch.x431pro.module.base;

import android.content.Context;
import android.text.TextUtils;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.framework.network.http.RequestParams;
import org.ksoap2.serialization.SoapObject;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParser;

public class BusinessManager extends BaseManager {
    protected final int Cache_1Day;
    protected final int Cache_1hour;
    protected final int Cache_1month;
    protected final int Cache_30min;
    protected final int Cache_3Day;
    protected final int Cache_3hour;
    protected final int Cache_5Day;
    protected final int Cache_7Day;

    public BusinessManager(Context context) {
        super(context);
        this.Cache_30min = 1800;
        this.Cache_1hour = 3600;
        this.Cache_3hour = 10800;
        this.Cache_1Day = 86400;
        this.Cache_3Day = 259200;
        this.Cache_5Day = 432000;
        this.Cache_7Day = 604800;
        this.Cache_1month = 2592000;
    }

    public CommonResponse getCommonResponse(String url) throws HttpException {
        return getCommonResponse(url, null);
    }

    public CommonResponse getCommonResponse(String url, RequestParams params) throws HttpException {
        String json = this.httpManager.get(url, params);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return (CommonResponse) jsonToBean(json, CommonResponse.class);
    }

    public CommonResponse postCommonResponse(String url) throws HttpException {
        return postCommonResponse(url, null);
    }

    public CommonResponse postCommonResponse(String url, RequestParams params) throws HttpException {
        String json = this.httpManager.post(url, params);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return (CommonResponse) jsonToBean(json, CommonResponse.class);
    }

    public CommonResponse callCommonResponse(String url, SoapObject requestObj, Element[] elements) throws HttpException {
        try {
            this.httpTransport = getHttpTransport(url);
            this.envelope = getSoapSerializationEnvelope(elements, requestObj);
            this.httpTransport.call(XmlPullParser.NO_NAMESPACE, this.envelope);
            if (this.envelope == null) {
                return null;
            }
            return (CommonResponse) soapToBean(CommonResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCacheKey(String url) {
        return getCacheKey(url, null);
    }

    public String getCacheKey(String url, RequestParams params) {
        StringBuilder key = new StringBuilder(url);
        if (params != null) {
            key.append(params.toString());
        }
        return String.valueOf(key.hashCode());
    }
}
