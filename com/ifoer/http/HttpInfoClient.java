package com.ifoer.http;

import android.util.Log;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.ifoer.mine.BaseInfoDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

public class HttpInfoClient {
    private static final String RESPONSE_CODE = "code";
    private static final String RESPONSE_DATA = "data";
    private static final String RESPONSE_MSG = "msg";
    private static HttpClient client;
    private static HttpInfoClient instance;

    static {
        instance = new HttpInfoClient();
    }

    public static HttpInfoClient getInstance() {
        if (client == null) {
            client = HttpClientManager.getHttpClient();
        }
        return instance;
    }

    public String sendPost(String url, Map<String, String> params) {
        try {
            HttpPost post = new HttpPost(url);
            List<BasicNameValuePair> parameters = new ArrayList();
            if (params != null) {
                for (Entry<String, String> item : params.entrySet()) {
                    parameters.add(new BasicNameValuePair((String) item.getKey(), (String) item.getValue()));
                }
                post.setEntity(new UrlEncodedFormEntity(parameters, AsyncHttpResponseHandler.DEFAULT_CHARSET));
            }
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == AsyncTaskManager.REQUEST_SUCCESS_CODE) {
                return EntityUtils.toString(response.getEntity(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String sendGet(String url) {
        try {
            HttpResponse response = client.execute(new HttpGet(url));
            if (response.getStatusLine().getStatusCode() == AsyncTaskManager.REQUEST_SUCCESS_CODE) {
                String result = EntityUtils.toString(response.getEntity(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
                Log.i("bcf", result);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseInfoDto getBaseInfoDtoByPost(String url, Map<String, String> params) {
        String result = XmlPullParser.NO_NAMESPACE;
        try {
            result = sendPost(url, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject object = new JSONObject(result);
            if (object != null) {
                BaseInfoDto info = new BaseInfoDto();
                info.setCode(object.getString(RESPONSE_CODE));
                if (object.has(RESPONSE_MSG)) {
                    if (object.getString(RESPONSE_MSG).equals("null") || object.getString(RESPONSE_MSG).equals(XmlPullParser.NO_NAMESPACE)) {
                        info.setMsg(null);
                    } else {
                        info.setMsg(object.getString(RESPONSE_MSG));
                    }
                }
                if (!object.has(RESPONSE_DATA)) {
                    return info;
                }
                if (object.getString(RESPONSE_DATA).equals("null") || object.getString(RESPONSE_DATA).equals(XmlPullParser.NO_NAMESPACE) || object.getString(RESPONSE_DATA).equals("[]")) {
                    info.setJsonObject(null);
                    return info;
                }
                info.setJsonObject(object.getJSONObject(RESPONSE_DATA));
                return info;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }
}
