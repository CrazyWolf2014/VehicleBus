package com.ifoer.mine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smackx.Form;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class HttpInfoClient {
    private static final String RESPONSE_CODE = "code";
    private static final String RESPONSE_DATA = "data";
    private static final String RESPONSE_MSG = "msg";
    private static final String TAG = "HttpInfoClient";
    private static final String UA = "Android_ChatApp_1.0";
    private static HttpClient client;
    private static HttpInfoClient instance;
    private List<Cookie> cookies;
    private String targerServerUrl;

    static {
        instance = new HttpInfoClient();
    }

    private HttpInfoClient() {
    }

    public static HttpInfoClient getInstance() {
        if (client == null) {
            client = HttpClientManager.getHttpClient();
        }
        return instance;
    }

    public String getServerUrl() {
        return this.targerServerUrl;
    }

    private Bitmap inputStream2Bitmap(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[Flags.FLAG5];
        while (true) {
            int len = is.read(b, 0, Flags.FLAG5);
            if (len == -1) {
                break;
            }
            baos.write(b, 0, len);
            baos.flush();
        }
        byte[] bytes = baos.toByteArray();
        baos.close();
        Options opts = new Options();
        int length = bytes.length;
        System.out.println("Length---->" + length);
        if (length > 204800) {
            opts.inSampleSize = 1;
        } else {
            opts.inSampleSize = 1;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
    }

    private JSONObject getJsonObjectByUrl(String url) throws Exception {
        return getJsonObjectByUrl(url, 0, null);
    }

    private JSONObject getJsonObjectByUrlForLogin(String url) throws Exception {
        HttpResponse response = client.execute(getHttpPost(url, null));
        this.cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
        return new JSONObject(EntityUtils.toString(response.getEntity()));
    }

    private JSONObject getJsonObjectByUrl(String url, List<NameValuePair> params) throws Exception {
        return getJsonObjectByUrl(url, 0, params);
    }

    private JSONObject getJsonObjectByUrl(String url, int times, List<NameValuePair> params) throws Exception {
        if (times > 2) {
            return null;
        }
        JSONObject object = new JSONObject(EntityUtils.toString(client.execute(getHttpPost(url, params)).getEntity()));
        if (object.getInt(RESPONSE_CODE) == 10) {
            return getJsonObjectByUrl(url, times + 1, params);
        }
        return object;
    }

    private HttpPost getHttpPost(String url, List<NameValuePair> params) throws Exception {
        HttpPost request = new HttpPost(url);
        if (!(this.cookies == null || this.cookies.isEmpty())) {
            for (int i = 0; i < this.cookies.size(); i++) {
                Cookie cookie = (Cookie) this.cookies.get(i);
                request.setHeader("cookie", cookie.getName() + "=" + cookie.getValue());
            }
        }
        request.addHeader("User-Agent", UA);
        if (params != null) {
            request.setEntity(new UrlEncodedFormEntity(params, AsyncHttpResponseHandler.DEFAULT_CHARSET));
        }
        return request;
    }

    public InfoDto getInfoByUrl(String url) throws Exception {
        return getInfoByUrl(url, null);
    }

    public AreaInfoDto getAreaInfoByUrl(String url) throws Exception {
        return getAreaInfoByUrl(url, null);
    }

    public InfoDto getInfoByUrl(String url, List<NameValuePair> params) throws Exception {
        String result = EntityUtils.toString(client.execute(getHttpPost(url, params)).getEntity());
        Log.e(Form.TYPE_RESULT, result);
        JSONObject object = new JSONObject(result);
        Log.d(TAG, "response json:" + object.toString());
        InfoDto info = new InfoDto();
        info.setCode(object.getInt(RESPONSE_CODE));
        info.setMsg(object.getString(RESPONSE_MSG));
        if (!(!object.has(RESPONSE_DATA) || "null".equals(object.getString(RESPONSE_DATA)) || XmlPullParser.NO_NAMESPACE.equals(object.getString(RESPONSE_DATA)))) {
            info.setJsonObject(object.getJSONObject(RESPONSE_DATA));
        }
        return info;
    }

    public AreaInfoDto getAreaInfoByUrl(String url, List<NameValuePair> params) throws Exception {
        JSONObject object = new JSONObject(EntityUtils.toString(client.execute(getHttpPost(url, params)).getEntity()));
        AreaInfoDto info = new AreaInfoDto();
        info.setCode(object.getInt(RESPONSE_CODE));
        info.setMsg(object.getString(RESPONSE_MSG));
        if (object.has(RESPONSE_DATA)) {
            info.setJsonArray(object.getJSONArray(RESPONSE_DATA));
        }
        return info;
    }

    public String sendPost(String url, Map<String, String> params) throws HttpException {
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
                String result = EntityUtils.toString(response.getEntity(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
                Log.i("yb", result);
                return result;
            }
            throw new HttpException(response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            throw new HttpException(-1);
        }
    }

    public NationInfoDto getNationInfoDtoByPost(String url, Map<String, String> params) throws DecodeException {
        try {
            try {
                JSONObject object = new JSONObject(sendPost(url, params));
                if (object != null) {
                    NationInfoDto info = new NationInfoDto();
                    info.setCode(object.getString(RESPONSE_CODE));
                    if (object.has(RESPONSE_MSG)) {
                        if (object.getString(RESPONSE_MSG) == "null" || object.getString(RESPONSE_MSG).equals(XmlPullParser.NO_NAMESPACE)) {
                            info.setMsg(null);
                        } else {
                            info.setMsg(object.getString(RESPONSE_MSG));
                        }
                    }
                    if (object.has(RESPONSE_DATA)) {
                        if (object.getString(RESPONSE_DATA) == "null" || object.getString(RESPONSE_DATA).equals(XmlPullParser.NO_NAMESPACE)) {
                            info.setJsonArray(null);
                        } else {
                            info.setJsonArray(object.getJSONArray(RESPONSE_DATA));
                        }
                    }
                    return info;
                }
                throw new DecodeException(-2);
            } catch (JSONException e) {
                throw new DecodeException(-2);
            }
        } catch (HttpException e2) {
            throw new DecodeException(e2.getCode());
        }
    }

    public BaseInfoDto getBaseInfoDtoByPost(String url, Map<String, String> params) throws DecodeException {
        try {
            try {
                JSONObject object = new JSONObject(sendPost(url, params));
                if (object != null) {
                    BaseInfoDto info = new BaseInfoDto();
                    info.setCode(object.getString(RESPONSE_CODE));
                    if (object.has(RESPONSE_MSG)) {
                        if (object.getString(RESPONSE_MSG) == "null" || object.getString(RESPONSE_MSG).equals(XmlPullParser.NO_NAMESPACE)) {
                            info.setMsg(null);
                        } else {
                            info.setMsg(object.getString(RESPONSE_MSG));
                        }
                    }
                    if (object.has(RESPONSE_DATA)) {
                        if (object.getString(RESPONSE_DATA) == "null" || object.getString(RESPONSE_DATA).equals(XmlPullParser.NO_NAMESPACE)) {
                            info.setJsonObject(null);
                        } else {
                            info.setJsonObject(object.getJSONObject(RESPONSE_DATA));
                        }
                    }
                    return info;
                }
                throw new DecodeException(-2);
            } catch (JSONException e) {
                throw new DecodeException(-2);
            }
        } catch (HttpException e2) {
            throw new DecodeException(e2.getCode());
        }
    }
}
