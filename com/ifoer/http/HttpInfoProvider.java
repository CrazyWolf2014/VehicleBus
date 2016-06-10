package com.ifoer.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.car.result.LoginResult;
import com.car.result.RegisterResult;
import com.cnlaunch.framework.common.Constants;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnmobi.im.bo.ImConstants;
import com.cnmobi.im.dto.Msg;
import com.ifoer.entity.Constant;
import com.ifoer.entity.CountryInfo;
import com.ifoer.entity.CountryListResult;
import com.ifoer.entity.InterfaceConfig;
import com.ifoer.entity.InterfaceDao;
import com.ifoer.entity.InterfaceUrl;
import com.ifoer.entity.RemindBean;
import com.ifoer.entity.RemindDetailBean;
import com.ifoer.entity.VIPUserInfoDTO;
import com.ifoer.entity.VIPWarnReport;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.mine.BaseInfoDto;
import com.ifoer.mine.Contact;
import com.ifoer.mine.DecodeException;
import com.ifoer.mine.SignUtils;
import com.ifoer.mine.model.BaseCode;
import com.ifoer.mine.model.GetCarsResult;
import com.ifoer.mine.model.GetTechInfoResult;
import com.ifoer.mine.model.UserData;
import com.ifoer.mine.model.UserDataResult;
import com.ifoer.mine.model.getClassificationResult;
import com.ifoer.util.CacheManager;
import com.ifoer.util.MySharedPreferences;
import com.launch.service.CodeUtil;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.plugin.BaseProfile;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

public class HttpInfoProvider {
    private static String cc;
    private static HttpClient client;
    private static HttpInfoProvider instance;
    private static ObjectMapper jsonMapper;
    private static String mToken;
    public InterfaceDao dao;

    static {
        instance = new HttpInfoProvider();
        client = new DefaultHttpClient();
    }

    private HttpInfoProvider() {
        if (this.dao == null) {
            this.dao = InterfaceDao.getInstance();
        }
    }

    public static HttpInfoProvider getInstance() {
        cc = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.CCKey);
        mToken = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.TokenKey);
        getJSONMapper();
        return instance;
    }

    public InterfaceConfig getConfigUrls(String app_id, String config_no, String area) {
        int i;
        Map<String, String> params = new HashMap();
        params.put(Constants.APP_ID, Constant.APP_ID);
        params.put("config_no", config_no);
        params.put("area", area);
        String sb = Constant.CONFIG_WEBSITE_PATH + "/?action=config_service.urls";
        List<String> keys = new ArrayList(params.keySet());
        sb = new StringBuilder(String.valueOf(sb)).append(AlixDefine.split).toString();
        for (i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) params.get(key);
            if (i == keys.size() - 1) {
                sb = new StringBuilder(String.valueOf(sb)).append(key).append("=").append(value).toString();
            } else {
                sb = new StringBuilder(String.valueOf(sb)).append(key).append("=").append(value).append(AlixDefine.split).toString();
            }
        }
        String sendGet = null;
        try {
            sendGet = HttpInfoClient.getInstance().sendGet(sb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InterfaceConfig config = new InterfaceConfig();
        try {
            if (!TextUtils.isEmpty(sendGet)) {
                JSONObject object = new JSONObject(sendGet);
                config.setCode(object.getString("code"));
                config.setMsg(object.getString(Msg.MSG_CONTENT));
                if (Contact.RELATION_ASK.equals(object.getString("code"))) {
                    if (object.has(DataPacketExtension.ELEMENT_NAME)) {
                        if (object.getString(DataPacketExtension.ELEMENT_NAME).equals("null")) {
                            config.setUrlList(null);
                        } else {
                            List<InterfaceUrl> list = new ArrayList();
                            JSONObject dataObject = object.getJSONObject(DataPacketExtension.ELEMENT_NAME);
                            JSONArray urlsArray = dataObject.getJSONArray("urls");
                            for (i = 0; i < urlsArray.length(); i++) {
                                JSONObject valueObject = urlsArray.getJSONObject(i);
                                InterfaceUrl url = new InterfaceUrl();
                                url.setKey(valueObject.getString(SharedPref.KEY));
                                url.setValue(valueObject.getString(SharedPref.VALUE));
                                list.add(url);
                            }
                            config.setUrlList(list);
                            config.setVersion(dataObject.getString(AlixDefine.VERSION));
                            config.setArea(dataObject.getString("area"));
                        }
                    }
                }
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return config;
    }

    public String getConfigUrl(String key) {
        return InterfaceDao.search(key);
    }

    public RegisterResult registeUser(String nationId, String loginKey, String nickName, String password, String zipCode, String verifyCode, String email) {
        RegisterResult registerResult = new RegisterResult();
        String path = InterfaceDao.search(InterfaceConfig.REGISTER);
        Map<String, String> params = new HashMap();
        params.put("nation_id", nationId);
        params.put("loginKey", loginKey);
        params.put("verify_code", verifyCode);
        params.put("password", password);
        params.put("nick_name", nickName);
        params.put("email", email);
        params.put("zipcode", zipCode);
        params.put(Constants.APP_ID, Constant.APP_ID);
        BaseInfoDto info = HttpInfoClient.getInstance().getBaseInfoDtoByPost(path, params);
        if (info != null) {
            registerResult.setCode(Integer.parseInt(info.getCode()));
            registerResult.setMessage(info.getMsg());
            try {
                JSONObject obj = info.getJsonObject();
                if (obj != null) {
                    registerResult.setCc(obj.getString(ImConstants.USER_ID));
                    registerResult.setInitPassword(obj.getString("user_pass"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return registerResult;
    }

    public LoginResult getLoginUser(Context context, String login_key, String password, String app_id, String time, int type) throws Exception {
        LoginResult loginResult = new LoginResult(context, login_key, password);
        String path = InterfaceDao.search(InterfaceConfig.LOGIN);
        if (path != null && path.equals(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        HttpPost httpPost = new HttpPost(path);
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("login_key", login_key));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair(Constants.APP_ID, app_id));
        params.add(new BasicNameValuePair(Msg.TIME_REDIO, time));
        params.add(new BasicNameValuePair(SharedPref.TYPE, new StringBuilder(String.valueOf(type)).toString()));
        params.add(new BasicNameValuePair("lon", new StringBuilder(String.valueOf(Constant.mLon)).toString()));
        params.add(new BasicNameValuePair("lat", new StringBuilder(String.valueOf(Constant.mLat)).toString()));
        httpPost.setEntity(new UrlEncodedFormEntity(params, AsyncHttpResponseHandler.DEFAULT_CHARSET));
        HttpResponse httpResp = new DefaultHttpClient().execute(httpPost);
        if (httpResp.getStatusLine().getStatusCode() != AsyncTaskManager.REQUEST_SUCCESS_CODE) {
            return null;
        }
        String content = EntityUtils.toString(httpResp.getEntity(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
        try {
            loginResult.decode(content);
            loginResult.setUser_json(content);
            return loginResult;
        } catch (Exception e) {
            e.printStackTrace();
            return loginResult;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<com.ifoer.entity.VIPUser> getVIPList(android.os.Handler r34, java.lang.String r35, android.content.Context r36) {
        /*
        r33 = this;
        r29 = new java.util.ArrayList;
        r29.<init>();
        r31 = com.ifoer.entity.InterfaceConfig.MY_VIP_LIST;
        r28 = com.ifoer.entity.InterfaceDao.search(r31);
        if (r28 == 0) goto L_0x00cd;
    L_0x000d:
        r31 = r35.length();
        if (r31 != 0) goto L_0x010c;
    L_0x0013:
        r31 = com.ifoer.entity.InterfaceConfig.MY_TECH_ID;
        r27 = com.ifoer.entity.InterfaceDao.search(r31);
        r18 = "";
        if (r27 == 0) goto L_0x010c;
    L_0x001d:
        r22 = new java.util.HashMap;
        r22.<init>();
        r31 = "action";
        r32 = com.ifoer.http.HttpTools.getAction(r27);
        r0 = r22;
        r1 = r31;
        r2 = r32;
        r0.put(r1, r2);
        r31 = "user_id";
        r32 = cc;
        r0 = r22;
        r1 = r31;
        r2 = r32;
        r0.put(r1, r2);
        r31 = "app_id";
        r32 = "921";
        r0 = r22;
        r1 = r31;
        r2 = r32;
        r0.put(r1, r2);
        r31 = "ver";
        r32 = "1.0";
        r0 = r22;
        r1 = r31;
        r2 = r32;
        r0.put(r1, r2);
        r31 = "tech_id";
        r32 = cc;
        r0 = r22;
        r1 = r31;
        r2 = r32;
        r0.put(r1, r2);
        r31 = mToken;
        r0 = r31;
        r1 = r22;
        r24 = com.ifoer.http.HttpTools.getSign(r0, r1);
        r31 = cc;
        r0 = r27;
        r1 = r31;
        r2 = r24;
        r20 = com.ifoer.http.HttpTools.getAllUrl(r0, r1, r2);
        r15 = new java.util.HashMap;
        r15.<init>();
        r31 = "tech_id";
        r32 = cc;
        r0 = r31;
        r1 = r32;
        r15.put(r0, r1);
        r31 = com.ifoer.http.HttpInfoClient.getInstance();
        r32 = r20.toString();
        r0 = r31;
        r1 = r32;
        r18 = r0.sendPost(r1, r15);
        r31 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x01ad }
        r0 = r31;
        r1 = r18;
        r0.<init>(r1);	 Catch:{ JSONException -> 0x01ad }
        r32 = "code";
        r4 = r31.getString(r32);	 Catch:{ JSONException -> 0x01ad }
        r31 = "0";
        r0 = r31;
        r31 = r4.equals(r0);	 Catch:{ JSONException -> 0x01ad }
        if (r31 == 0) goto L_0x00ce;
    L_0x00b4:
        if (r18 == 0) goto L_0x00c2;
    L_0x00b6:
        r31 = "NULL";
        r0 = r18;
        r1 = r31;
        r31 = r0.equalsIgnoreCase(r1);	 Catch:{ JSONException -> 0x01ad }
        if (r31 == 0) goto L_0x00ce;
    L_0x00c2:
        r31 = 4;
        r0 = r34;
        r1 = r31;
        r0.sendEmptyMessage(r1);	 Catch:{ JSONException -> 0x01ad }
        r29 = 0;
    L_0x00cd:
        return r29;
    L_0x00ce:
        r31 = "0";
        r0 = r31;
        r31 = r4.equals(r0);	 Catch:{ JSONException -> 0x01ad }
        if (r31 != 0) goto L_0x00e4;
    L_0x00d8:
        r31 = 1;
        r0 = r34;
        r1 = r31;
        r0.sendEmptyMessage(r1);	 Catch:{ JSONException -> 0x01ad }
        r29 = 0;
        goto L_0x00cd;
    L_0x00e4:
        r31 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x01ad }
        r0 = r31;
        r1 = r18;
        r0.<init>(r1);	 Catch:{ JSONException -> 0x01ad }
        r32 = "data";
        r11 = r31.getJSONObject(r32);	 Catch:{ JSONException -> 0x01ad }
        r31 = "pub_id";
        r0 = r31;
        r35 = r11.getString(r0);	 Catch:{ JSONException -> 0x01ad }
        r31 = r35.length();	 Catch:{ JSONException -> 0x01ad }
        if (r31 <= 0) goto L_0x019e;
    L_0x0101:
        r31 = "USER_PUBLIC_ID";
        r0 = r36;
        r1 = r31;
        r2 = r18;
        com.ifoer.util.MySharedPreferences.setString(r0, r1, r2);	 Catch:{ JSONException -> 0x01ad }
    L_0x010c:
        r23 = new java.util.HashMap;	 Catch:{ Exception -> 0x01e3 }
        r23.<init>();	 Catch:{ Exception -> 0x01e3 }
        r31 = "action";
        r32 = com.ifoer.http.HttpTools.getAction(r28);	 Catch:{ Exception -> 0x01e3 }
        r0 = r23;
        r1 = r31;
        r2 = r32;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x01e3 }
        r31 = "user_id";
        r32 = cc;	 Catch:{ Exception -> 0x01e3 }
        r0 = r23;
        r1 = r31;
        r2 = r32;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x01e3 }
        r31 = "app_id";
        r32 = "921";
        r0 = r23;
        r1 = r31;
        r2 = r32;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x01e3 }
        r31 = "ver";
        r32 = "1.0";
        r0 = r23;
        r1 = r31;
        r2 = r32;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x01e3 }
        r31 = "pub_id";
        r0 = r23;
        r1 = r31;
        r2 = r35;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x01e3 }
        r31 = mToken;	 Catch:{ Exception -> 0x01e3 }
        r0 = r31;
        r1 = r23;
        r25 = com.ifoer.http.HttpTools.getSign(r0, r1);	 Catch:{ Exception -> 0x01e3 }
        r31 = cc;	 Catch:{ Exception -> 0x01e3 }
        r0 = r28;
        r1 = r31;
        r2 = r25;
        r21 = com.ifoer.http.HttpTools.getAllUrl(r0, r1, r2);	 Catch:{ Exception -> 0x01e3 }
        r16 = new java.util.HashMap;	 Catch:{ Exception -> 0x01e3 }
        r16.<init>();	 Catch:{ Exception -> 0x01e3 }
        r31 = "pub_id";
        r0 = r16;
        r1 = r31;
        r2 = r35;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x01e3 }
        r0 = r21;
        r1 = r16;
        r17 = com.ifoer.http.HttpTools.getUrlTool(r0, r1);	 Catch:{ Exception -> 0x01e3 }
        r31 = com.ifoer.http.HttpInfoClient.getInstance();	 Catch:{ Exception -> 0x01e3 }
        r0 = r31;
        r1 = r17;
        r19 = r0.sendGet(r1);	 Catch:{ Exception -> 0x01e3 }
        if (r19 == 0) goto L_0x019a;
    L_0x018e:
        r31 = "NULL";
        r0 = r19;
        r1 = r31;
        r31 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x01e3 }
        if (r31 == 0) goto L_0x01b5;
    L_0x019a:
        r29 = 0;
        goto L_0x00cd;
    L_0x019e:
        r31 = "USER_PUBLIC_ID";
        r32 = "";
        r0 = r36;
        r1 = r31;
        r2 = r32;
        com.ifoer.util.MySharedPreferences.setString(r0, r1, r2);	 Catch:{ JSONException -> 0x01ad }
        goto L_0x010c;
    L_0x01ad:
        r6 = move-exception;
        r6.printStackTrace();
        r29 = 0;
        goto L_0x00cd;
    L_0x01b5:
        r9 = new org.json.JSONObject;	 Catch:{ Exception -> 0x01e3 }
        r0 = r19;
        r9.<init>(r0);	 Catch:{ Exception -> 0x01e3 }
        r31 = "data";
        r0 = r31;
        r8 = r9.getJSONArray(r0);	 Catch:{ Exception -> 0x01e3 }
        r7 = 0;
    L_0x01c5:
        r31 = r8.length();	 Catch:{ Exception -> 0x01e3 }
        r0 = r31;
        if (r7 < r0) goto L_0x01eb;
    L_0x01cd:
        r12 = new android.os.Message;	 Catch:{ Exception -> 0x01e3 }
        r12.<init>();	 Catch:{ Exception -> 0x01e3 }
        r31 = 556; // 0x22c float:7.79E-43 double:2.747E-321;
        r0 = r31;
        r12.what = r0;	 Catch:{ Exception -> 0x01e3 }
        r0 = r35;
        r12.obj = r0;	 Catch:{ Exception -> 0x01e3 }
        r0 = r34;
        r0.sendMessage(r12);	 Catch:{ Exception -> 0x01e3 }
        goto L_0x00cd;
    L_0x01e3:
        r5 = move-exception;
        r5.printStackTrace();
        r29 = 0;
        goto L_0x00cd;
    L_0x01eb:
        r10 = r8.opt(r7);	 Catch:{ Exception -> 0x01e3 }
        r10 = (org.json.JSONObject) r10;	 Catch:{ Exception -> 0x01e3 }
        r30 = new com.ifoer.entity.VIPUser;	 Catch:{ Exception -> 0x01e3 }
        r30.<init>();	 Catch:{ Exception -> 0x01e3 }
        r31 = "serial_no";
        r0 = r31;
        r31 = r10.getString(r0);	 Catch:{ Exception -> 0x01e3 }
        r30.setSerNo(r31);	 Catch:{ Exception -> 0x01e3 }
        r31 = "id";
        r0 = r31;
        r31 = r10.getString(r0);	 Catch:{ Exception -> 0x01e3 }
        r30.setId(r31);	 Catch:{ Exception -> 0x01e3 }
        r31 = "nick_name";
        r0 = r31;
        r31 = r10.getString(r0);	 Catch:{ Exception -> 0x01e3 }
        r30.setName(r31);	 Catch:{ Exception -> 0x01e3 }
        r31 = "user_id";
        r0 = r31;
        r31 = r10.getString(r0);	 Catch:{ Exception -> 0x01e3 }
        r30.setUser_id(r31);	 Catch:{ Exception -> 0x01e3 }
        r31 = "mine_car_plate_num";
        r0 = r31;
        r31 = r10.getString(r0);	 Catch:{ Exception -> 0x01e3 }
        r30.setCar_no(r31);	 Catch:{ Exception -> 0x01e3 }
        r31 = "if_pass";
        r0 = r31;
        r31 = r10.getString(r0);	 Catch:{ Exception -> 0x01e3 }
        r30.setIf_pass(r31);	 Catch:{ Exception -> 0x01e3 }
        r31 = "auto_logos";
        r0 = r31;
        r31 = r10.getString(r0);	 Catch:{ Exception -> 0x01e3 }
        r30.setCarUrl(r31);	 Catch:{ Exception -> 0x01e3 }
        r3 = "";
        r26 = com.cnlaunch.x431frame.C0136R.string.class;
        r13 = "";
        r0 = r26;
        r31 = r0.getDeclaredField(r13);	 Catch:{ Exception -> 0x0275 }
        r32 = 0;
        r14 = r31.getInt(r32);	 Catch:{ Exception -> 0x0275 }
        r31 = r36.getResources();	 Catch:{ Exception -> 0x0275 }
        r0 = r31;
        r31 = r0.getText(r14);	 Catch:{ Exception -> 0x0275 }
        r3 = r31.toString();	 Catch:{ Exception -> 0x0275 }
    L_0x0263:
        r31 = r3.length();	 Catch:{ Exception -> 0x01e3 }
        if (r31 <= 0) goto L_0x027a;
    L_0x0269:
        r0 = r30;
        r0.setCarType(r3);	 Catch:{ Exception -> 0x01e3 }
    L_0x026e:
        r29.add(r30);	 Catch:{ Exception -> 0x01e3 }
        r7 = r7 + 1;
        goto L_0x01c5;
    L_0x0275:
        r5 = move-exception;
        r5.printStackTrace();	 Catch:{ Exception -> 0x01e3 }
        goto L_0x0263;
    L_0x027a:
        r31 = "auto_code";
        r0 = r31;
        r31 = r10.getString(r0);	 Catch:{ Exception -> 0x01e3 }
        r30.setCarType(r31);	 Catch:{ Exception -> 0x01e3 }
        goto L_0x026e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.http.HttpInfoProvider.getVIPList(android.os.Handler, java.lang.String, android.content.Context):java.util.List<com.ifoer.entity.VIPUser>");
    }

    public VIPUserInfoDTO getVIPUserInfo(String serial_no, String pub_id, Context mContext) {
        if (pub_id.length() == 0) {
            String techPath = InterfaceDao.search(InterfaceConfig.MY_TECH_ID);
            String result = XmlPullParser.NO_NAMESPACE;
            if (techPath != null) {
                Map<String, String> signMap = new HashMap();
                signMap.put(Constant.ACTION, HttpTools.getAction(techPath));
                signMap.put(ImConstants.USER_ID, cc);
                signMap.put(Constants.APP_ID, Constant.APP_ID);
                signMap.put(Constants.VER, Constant.APP_VERSION);
                signMap.put("tech_id", cc);
                String sign_u = HttpTools.getSign(mToken, signMap);
                StringBuilder sb = HttpTools.getAllUrl(techPath, cc, sign_u);
                Map<String, String> params = new HashMap();
                params.put("tech_id", cc);
                result = HttpInfoClient.getInstance().sendPost(sb.toString(), params);
                if (result != null) {
                    if (result.equalsIgnoreCase("null")) {
                        return null;
                    }
                }
                try {
                    pub_id = new JSONObject(result).getJSONObject(DataPacketExtension.ELEMENT_NAME).getString("pub_id");
                    if (pub_id.length() > 0) {
                        MySharedPreferences.setString(mContext, MySharedPreferences.USER_PUBLIC_ID, result);
                    } else {
                        MySharedPreferences.setString(mContext, MySharedPreferences.USER_PUBLIC_ID, XmlPullParser.NO_NAMESPACE);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    return null;
                }
            }
        }
        VIPUserInfoDTO vipUserInfo = new VIPUserInfoDTO();
        String cs_info = InterfaceDao.search(InterfaceConfig.MY_VIP_USER_INFO);
        if (cs_info == null) {
            return null;
        }
        Map<String, String> signMap_vipinfo = new HashMap();
        signMap_vipinfo.put(Constant.ACTION, HttpTools.getAction(cs_info));
        signMap_vipinfo.put(ImConstants.USER_ID, cc);
        signMap_vipinfo.put(Constants.APP_ID, Constant.APP_ID);
        signMap_vipinfo.put(Constants.VER, Constant.APP_VERSION);
        signMap_vipinfo.put("serial_no", serial_no);
        signMap_vipinfo.put("pub_id", pub_id);
        try {
            String sign_v_info = SignUtils.getSign(mToken, signMap_vipinfo);
            StringBuilder sb_v_info = HttpTools.getAllUrl(cs_info, cc, sign_v_info);
            Map<String, String> params_v_info = new HashMap();
            params_v_info.put("pub_id", pub_id);
            params_v_info.put("serial_no", serial_no);
            String request_v_info = HttpTools.getUrlTool(sb_v_info, params_v_info);
            String result_v_info = HttpInfoClient.getInstance().sendGet(request_v_info);
            if (result_v_info != null) {
                if (result_v_info.equalsIgnoreCase("null")) {
                    return null;
                }
            }
            JSONObject jsonObject = new JSONObject(result_v_info).getJSONObject(DataPacketExtension.ELEMENT_NAME);
            if (jsonObject.getString("mine_car_name").equalsIgnoreCase("null")) {
                vipUserInfo.setMine_car_name(XmlPullParser.NO_NAMESPACE);
            } else {
                vipUserInfo.setMine_car_name(jsonObject.getString("mine_car_name"));
            }
            if (jsonObject.getString("car_type_id").equalsIgnoreCase("null")) {
                vipUserInfo.setCar_type_id(XmlPullParser.NO_NAMESPACE);
            } else {
                vipUserInfo.setCar_type_id(jsonObject.getString("car_type_id"));
            }
            if (jsonObject.getString("car_brand_vin").equalsIgnoreCase("null")) {
                vipUserInfo.setCar_brand_vin(XmlPullParser.NO_NAMESPACE);
            } else {
                vipUserInfo.setCar_brand_vin(jsonObject.getString("car_brand_vin"));
            }
            if (jsonObject.getString("mine_car_plate_num").equalsIgnoreCase("null")) {
                vipUserInfo.setMine_car_plate_num(XmlPullParser.NO_NAMESPACE);
            } else {
                vipUserInfo.setMine_car_plate_num(jsonObject.getString("mine_car_plate_num"));
            }
            if (jsonObject.getString("auto_code").equalsIgnoreCase("null")) {
                vipUserInfo.setAuto_code(XmlPullParser.NO_NAMESPACE);
            } else {
                vipUserInfo.setAuto_code(jsonObject.getString("auto_code"));
            }
            if (jsonObject.getString("car_producing_year").equalsIgnoreCase("null")) {
                vipUserInfo.setCar_producing_year(XmlPullParser.NO_NAMESPACE);
            } else {
                vipUserInfo.setCar_producing_year(jsonObject.getString("car_producing_year"));
            }
            if (jsonObject.getString("car_displacement").equalsIgnoreCase("null")) {
                vipUserInfo.setCar_displacement(XmlPullParser.NO_NAMESPACE);
            } else {
                vipUserInfo.setCar_displacement(jsonObject.getString("car_displacement"));
            }
            if (jsonObject.getString("car_gearbox_type").equalsIgnoreCase("null")) {
                vipUserInfo.setCar_gearbox_type(XmlPullParser.NO_NAMESPACE);
            } else {
                vipUserInfo.setCar_gearbox_type(jsonObject.getString("car_gearbox_type"));
            }
            if (jsonObject.getString("car_engine_num").equalsIgnoreCase("null")) {
                vipUserInfo.setCar_engine_num(XmlPullParser.NO_NAMESPACE);
            } else {
                vipUserInfo.setCar_engine_num(jsonObject.getString("car_engine_num"));
            }
            if (jsonObject.getString("note").equalsIgnoreCase("null")) {
                vipUserInfo.setNote(XmlPullParser.NO_NAMESPACE);
            } else {
                vipUserInfo.setNote(jsonObject.getString("note"));
            }
            if (jsonObject.getString("mileage").equalsIgnoreCase("null")) {
                vipUserInfo.setMileage(XmlPullParser.NO_NAMESPACE);
                return vipUserInfo;
            }
            vipUserInfo.setMileage(jsonObject.getString("mileage"));
            return vipUserInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<VIPWarnReport> getWarnInfo(String serial_no, String client_id) {
        ArrayList<VIPWarnReport> warn_list = new ArrayList();
        String cs_warn_url = "http://192.168.85.212:8081/application/?action=client_service.get_history";
        cs_warn_url = InterfaceDao.search(InterfaceConfig.WARNING_INFO);
        Map<String, String> signMap_warninfo = new HashMap();
        signMap_warninfo.put(Constant.ACTION, HttpTools.getAction(cs_warn_url));
        signMap_warninfo.put(ImConstants.USER_ID, cc);
        signMap_warninfo.put(Constants.APP_ID, Constant.APP_ID);
        signMap_warninfo.put(Constants.VER, Constant.APP_VERSION);
        signMap_warninfo.put("serial_no", serial_no);
        signMap_warninfo.put("client_id", client_id);
        try {
            StringBuilder sb_warn_info = HttpTools.getAllUrl(cs_warn_url, cc, SignUtils.getSign(mToken, signMap_warninfo));
            Map<String, String> params_warn_info = new HashMap();
            params_warn_info.put("client_id", client_id);
            params_warn_info.put("serial_no", serial_no);
            String result_warn_info = HttpInfoClient.getInstance().sendGet(HttpTools.getUrlTool(sb_warn_info, params_warn_info));
            if (result_warn_info != null && result_warn_info.equalsIgnoreCase("null")) {
                return null;
            }
            JSONArray jsonArray = new JSONObject(result_warn_info).getJSONObject(DataPacketExtension.ELEMENT_NAME).getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                VIPWarnReport warn = new VIPWarnReport();
                warn.setId(jsonObject2.getString(LocaleUtil.INDONESIAN));
                warn.setItem_id(jsonObject2.getString("item_id"));
                warn.setAlarm_type(jsonObject2.getString("alarm_type"));
                warn.setMsg(jsonObject2.getString(Msg.MSG_CONTENT));
                warn.setContent(jsonObject2.getString("content"));
                warn.setSerial_no(jsonObject2.getString("serial_no"));
                warn.setTechnician_id(jsonObject2.getString("technician_id"));
                warn.setTime(jsonObject2.getString(Msg.TIME_REDIO));
                warn.setIs_read(Integer.parseInt(jsonObject2.getString("is_read")));
                warn_list.add(warn);
            }
            return warn_list;
        } catch (Exception e) {
            e.printStackTrace();
            return warn_list;
        }
    }

    public JSONObject getMyCarCord(String user_id, String mine_car_id, String serial_no, String token) throws DecodeException {
        String url = InterfaceDao.search(InterfaceConfig.MINE_CAR_QUERY_MINE_CAR_INFO);
        String action = HttpTools.getAction(url);
        Map<String, String> signMap = new HashMap();
        signMap.put(Constant.ACTION, action);
        signMap.put(ImConstants.USER_ID, user_id);
        signMap.put(Constants.APP_ID, Constant.APP_ID);
        signMap.put(Constants.VER, Constant.APP_VERSION);
        if (mine_car_id != null) {
            signMap.put("mine_car_id", mine_car_id);
        }
        if (serial_no != null) {
            signMap.put("serial_no", serial_no);
        }
        String sign = SignUtils.getSign(token, signMap);
        Map<String, String> params = new HashMap();
        if (mine_car_id != null) {
            params.put("mine_car_id", mine_car_id);
        }
        if (serial_no != null) {
            params.put("serial_no", serial_no);
        }
        String request = HttpTools.getUrlTool(HttpTools.getAllUrl(url, user_id, sign), params);
        CodeUtil.m1469d("URL=" + request);
        try {
            return new JSONObject(HttpInfoClient.getInstance().sendGet(request));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public getClassificationResult getClassification(String user_id, String token, String lan_id) throws DecodeException {
        String url = InterfaceDao.search(InterfaceConfig.EXPERT_GET_CAR_CLASSIFICATION);
        String key = new StringBuilder(String.valueOf(getClassificationResult.class.getSimpleName())).append(lan_id).toString();
        try {
            getClassificationResult result = (getClassificationResult) CacheManager.readObject(key);
            if (result != null && result.getCode() == 0 && result.getData() != null) {
                return result;
            }
            Map<String, String> params = new HashMap();
            params.put(Constant.ACTION, HttpTools.getAction(url));
            params.put(ImConstants.USER_ID, user_id);
            params.put(Constants.APP_ID, Constant.APP_ID);
            params.put(Constants.VER, Constant.APP_VERSION);
            params.put("lan_id", lan_id);
            String json = HttpInfoClient.getInstance().sendGet(HttpTools.getUrlTool(HttpTools.getAllUrl(url, user_id, SignUtils.getSign(token, params)), params));
            if (json != null) {
                result = (getClassificationResult) jsonToBean(json, getClassificationResult.class);
                if (!(result == null || result.getCode() != 0 || result.getData() == null)) {
                    CacheManager.writeObject(result, key);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecodeException(-2);
        }
    }

    public GetCarsResult getCars(String user_id, String token, String lan_id, String class_id) throws DecodeException {
        String url = InterfaceDao.search(InterfaceConfig.EXPERT_GET_CARS);
        String key = new StringBuilder(String.valueOf(GetCarsResult.class.getSimpleName())).append(lan_id).append(class_id).toString();
        try {
            GetCarsResult result = (GetCarsResult) CacheManager.readObject(key);
            if (result != null && result.getCode() == 0 && result.getData() != null) {
                return result;
            }
            Map<String, String> params = new HashMap();
            params.put(Constant.ACTION, HttpTools.getAction(url));
            params.put(ImConstants.USER_ID, user_id);
            params.put(Constants.APP_ID, Constant.APP_ID);
            params.put(Constants.VER, Constant.APP_VERSION);
            params.put("lan_id", lan_id);
            params.put("class_id", class_id);
            String json = HttpInfoClient.getInstance().sendGet(HttpTools.getUrlTool(HttpTools.getAllUrl(url, user_id, SignUtils.getSign(token, params)), params));
            if (json != null) {
                result = (GetCarsResult) jsonToBean(json, GetCarsResult.class);
                if (!(result == null || result.getCode() != 0 || result.getData() == null)) {
                    CacheManager.writeObject(result, key);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecodeException(-2);
        }
    }

    public GetTechInfoResult getTechInfo(String user_id, String token, String lan_id) throws DecodeException {
        String url = InterfaceDao.search(InterfaceConfig.EXPERT_GET_TECH_INFO);
        try {
            Map<String, String> params = new HashMap();
            params.put(Constant.ACTION, HttpTools.getAction(url));
            params.put(ImConstants.USER_ID, user_id);
            params.put(Constants.APP_ID, Constant.APP_ID);
            params.put(Constants.VER, Constant.APP_VERSION);
            params.put("lan_id", lan_id);
            String json = HttpInfoClient.getInstance().sendGet(HttpTools.getUrlTool(HttpTools.getAllUrl(url, user_id, SignUtils.getSign(token, params)), params));
            if (json != null) {
                return (GetTechInfoResult) jsonToBean(json, GetTechInfoResult.class);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecodeException(-2);
        }
    }

    public BaseCode modifyTechInfo(String token, String user_id, String introduce, String work_unit, String auto_info, String maintenance_leve) throws DecodeException {
        String url = InterfaceDao.search(InterfaceConfig.EXPERT_MODIFY_TECH_INFO);
        try {
            Map<String, String> params = new HashMap();
            params.put(Constant.ACTION, HttpTools.getAction(url));
            params.put(ImConstants.USER_ID, user_id);
            params.put(Constants.APP_ID, Constant.APP_ID);
            params.put(Constants.VER, Constant.APP_VERSION);
            params.put("maintenance_leve", maintenance_leve);
            params.put("introduce", introduce);
            params.put("work_unit", work_unit);
            params.put("auto_info", auto_info);
            StringBuilder sb = HttpTools.getAllUrl(url, user_id, SignUtils.getSign(token, params));
            Map<String, String> params1 = new HashMap();
            params1.put("introduce", introduce);
            params1.put("work_unit", work_unit);
            params1.put("auto_info", auto_info);
            params1.put("maintenance_leve", maintenance_leve);
            params1.put(ImConstants.USER_ID, user_id);
            String json = HttpInfoClient.getInstance().sendPost(sb.toString(), params1);
            if (json != null) {
                return (BaseCode) jsonToBean(json, BaseCode.class);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecodeException(-2);
        }
    }

    public BaseCode setExpertInfo(String token, String user_id, String introduce, String maintenance_leve, String work_unit, String auto_info) throws DecodeException {
        String url = InterfaceDao.search(InterfaceConfig.EXPERT_SET_EXPERT_INFO);
        try {
            Map<String, String> signMap = new HashMap();
            signMap.put(Constant.ACTION, HttpTools.getAction(url));
            signMap.put(ImConstants.USER_ID, user_id);
            signMap.put(Constants.APP_ID, Constant.APP_ID);
            signMap.put(Constants.VER, Constant.APP_VERSION);
            signMap.put("maintenance_leve", maintenance_leve);
            signMap.put("introduce", introduce);
            signMap.put("work_unit", work_unit);
            signMap.put("auto_info", auto_info);
            StringBuilder sb = HttpTools.getAllUrl(url, user_id, SignUtils.getSign(token, signMap));
            Map<String, String> params = new HashMap();
            params.put(ImConstants.USER_ID, user_id);
            params.put("maintenance_leve", maintenance_leve);
            params.put("introduce", introduce);
            params.put("work_unit", work_unit);
            params.put("auto_info", auto_info);
            String json = HttpInfoClient.getInstance().sendPost(sb.toString(), params);
            if (json != null) {
                return (BaseCode) jsonToBean(json, BaseCode.class);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecodeException(-2);
        }
    }

    public BaseCode unbindEmail(String user_id, String token) throws DecodeException {
        String url = InterfaceDao.search(InterfaceConfig.USER_UNBIND_EMAIL);
        Map<String, String> signMap = new HashMap();
        signMap.put(Constant.ACTION, HttpTools.getAction(url));
        signMap.put(ImConstants.USER_ID, user_id);
        signMap.put(Constants.APP_ID, Constant.APP_ID);
        signMap.put(Constants.VER, Constant.APP_VERSION);
        try {
            String json = HttpInfoClient.getInstance().sendPost(HttpTools.getUrlTool(HttpTools.getAllUrl(url, user_id, SignUtils.getSign(token, signMap)), new HashMap()).toString(), null);
            if (json != null) {
                return (BaseCode) jsonToBean(json, BaseCode.class);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecodeException(-2);
        }
    }

    public BaseCode unbindTel(String user_id, String token) throws DecodeException {
        String url = InterfaceDao.search(InterfaceConfig.USER_UNBIND_TEL);
        Map<String, String> signMap = new HashMap();
        signMap.put(Constant.ACTION, HttpTools.getAction(url));
        signMap.put(ImConstants.USER_ID, user_id);
        signMap.put(Constants.APP_ID, Constant.APP_ID);
        signMap.put(Constants.VER, Constant.APP_VERSION);
        try {
            String json = HttpInfoClient.getInstance().sendPost(HttpTools.getUrlTool(HttpTools.getAllUrl(url, user_id, SignUtils.getSign(token, signMap)), new HashMap()).toString(), null);
            if (json != null) {
                return (BaseCode) jsonToBean(json, BaseCode.class);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecodeException(-2);
        }
    }

    public BaseCode bindEmailOrMobile(String user_id, String token, String email, String mobile, String vcode) throws DecodeException {
        String url = InterfaceDao.search(InterfaceConfig.USER_SET_BASE);
        Map<String, String> signMap = new HashMap();
        signMap.put(Constant.ACTION, HttpTools.getAction(url));
        signMap.put(ImConstants.USER_ID, user_id);
        signMap.put(Constants.APP_ID, Constant.APP_ID);
        signMap.put(Constants.VER, Constant.APP_VERSION);
        signMap.put("vcode", vcode);
        if (!TextUtils.isEmpty(mobile)) {
            signMap.put("mobile", mobile);
        }
        if (!TextUtils.isEmpty(email)) {
            signMap.put("email", email);
        }
        StringBuilder sb = HttpTools.getAllUrl(url, user_id, SignUtils.getSign(token, signMap));
        Map<String, String> params = new HashMap();
        params.put("vcode", vcode);
        if (!TextUtils.isEmpty(mobile)) {
            params.put("mobile", mobile);
        }
        if (!TextUtils.isEmpty(email)) {
            params.put("email", email);
        }
        try {
            String json = HttpInfoClient.getInstance().sendPost(sb.toString(), params);
            if (json != null) {
                return (BaseCode) jsonToBean(json, BaseCode.class);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecodeException(-2);
        }
    }

    public BaseCode setIdentityTag(String user_id, String token, String identity_tag) throws DecodeException {
        String url = InterfaceDao.search(InterfaceConfig.USER_SET_BASE);
        Map<String, String> signMap = new HashMap();
        signMap.put(Constant.ACTION, HttpTools.getAction(url));
        signMap.put(ImConstants.USER_ID, user_id);
        signMap.put(Constants.APP_ID, Constant.APP_ID);
        signMap.put(Constants.VER, Constant.APP_VERSION);
        signMap.put("identity_tag", identity_tag);
        StringBuilder sb = HttpTools.getAllUrl(url, user_id, SignUtils.getSign(token, signMap));
        Map<String, String> params = new HashMap();
        params.put("identity_tag", identity_tag);
        try {
            String json = HttpInfoClient.getInstance().sendPost(sb.toString(), params);
            if (json != null) {
                return (BaseCode) jsonToBean(json, BaseCode.class);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecodeException(-2);
        }
    }

    public BaseCode sendHelpInfo(String user_id, String app_id, String data, String token) throws DecodeException {
        String url = InterfaceDao.search(InterfaceConfig.HELP_FEEDBACK);
        Map<String, String> signMap = new HashMap();
        signMap.put(Constant.ACTION, HttpTools.getAction(url));
        signMap.put(ImConstants.USER_ID, user_id);
        signMap.put(Constants.APP_ID, Constant.APP_ID);
        signMap.put(Constants.VER, Constant.APP_VERSION);
        signMap.put(DataPacketExtension.ELEMENT_NAME, data);
        StringBuilder sb = HttpTools.getAllUrl(url, user_id, SignUtils.getSign(token, signMap));
        Map<String, String> params = new HashMap();
        params.put(DataPacketExtension.ELEMENT_NAME, data);
        try {
            String json = HttpInfoClient.getInstance().sendPost(sb.toString(), params);
            if (json != null) {
                return (BaseCode) jsonToBean(json, BaseCode.class);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecodeException(-2);
        }
    }

    public BaseCode getCountryList(String lan) throws Exception {
        BaseCode result = null;
        String url = InterfaceDao.search(InterfaceConfig.VERIFY_REQ_SEND_CODE);
        if (url != null && url.equals(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("lan", lan));
        Log.i("getCountryList", "getCountryList");
        httpPost.setEntity(new UrlEncodedFormEntity(params, AsyncHttpResponseHandler.DEFAULT_CHARSET));
        HttpResponse httpResp = new DefaultHttpClient().execute(httpPost);
        if (httpResp.getStatusLine().getStatusCode() != AsyncTaskManager.REQUEST_SUCCESS_CODE) {
            return null;
        }
        String content = EntityUtils.toString(httpResp.getEntity(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
        if (content != null) {
            try {
                result = (BaseCode) jsonToBean(content, BaseCode.class);
            } catch (Exception e) {
            }
        } else {
            Log.i("getCountryList", "content" + content);
        }
        return result;
    }

    public BaseCode getVerifyCode(String req_info, String lan, String isres) throws Exception {
        BaseCode result = null;
        String url = InterfaceDao.search(InterfaceConfig.VERIFY_REQ_SEND_CODE);
        if (url != null && url.equals(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("req_info", req_info));
        params.add(new BasicNameValuePair("lan", lan));
        params.add(new BasicNameValuePair("isres", isres));
        httpPost.setEntity(new UrlEncodedFormEntity(params, AsyncHttpResponseHandler.DEFAULT_CHARSET));
        HttpResponse httpResp = new DefaultHttpClient().execute(httpPost);
        if (httpResp.getStatusLine().getStatusCode() != AsyncTaskManager.REQUEST_SUCCESS_CODE) {
            return null;
        }
        String content = EntityUtils.toString(httpResp.getEntity(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
        if (content != null) {
            try {
                Log.i("getVerifyCode", "content" + content);
                result = (BaseCode) jsonToBean(content, BaseCode.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i("getVerifyCode", "content" + content);
        }
        return result;
    }

    public BaseCode verifyVerifyCode(String req_info, String code) throws Exception {
        BaseCode result = null;
        String url = InterfaceDao.search(InterfaceConfig.VERIFY_VERIFY_CODE);
        if (url != null && url.equals(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("req_info", req_info));
        params.add(new BasicNameValuePair("verify_code", code));
        httpPost.setEntity(new UrlEncodedFormEntity(params, AsyncHttpResponseHandler.DEFAULT_CHARSET));
        HttpResponse httpResp = new DefaultHttpClient().execute(httpPost);
        if (httpResp.getStatusLine().getStatusCode() != AsyncTaskManager.REQUEST_SUCCESS_CODE) {
            return null;
        }
        String content = EntityUtils.toString(httpResp.getEntity(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
        if (content != null) {
            try {
                result = (BaseCode) jsonToBean(content, BaseCode.class);
            } catch (Exception e) {
            }
        }
        return result;
    }

    public BaseCode setPhone(String mobile, String cc) throws Exception {
        BaseCode baseCode = new BaseCode();
        String url = InterfaceDao.search(InterfaceConfig.SET_PHONE);
        if (url != null && url.equals(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        if (url == null) {
            return baseCode;
        }
        Map<String, String> signMap = new HashMap();
        signMap.put(Constant.ACTION, HttpTools.getAction(url));
        signMap.put(ImConstants.USER_ID, cc);
        signMap.put(Constants.APP_ID, Constant.APP_ID);
        signMap.put(Constants.VER, Constant.APP_VERSION);
        signMap.put("mobile", mobile);
        StringBuilder sb = HttpTools.getAllUrl(url, cc, HttpTools.getSign(mToken, signMap));
        Map<String, String> params = new HashMap();
        params.put("mobile", mobile);
        String result = HttpInfoClient.getInstance().sendPost(sb.toString(), params);
        if (result != null) {
            try {
                if (!result.equalsIgnoreCase("NULL")) {
                    JSONObject jsonObject_u = new JSONObject(result);
                    if (!jsonObject_u.has("code")) {
                        return baseCode;
                    }
                    baseCode.setCode(jsonObject_u.getInt("code"));
                    return baseCode;
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public BaseCode setName(String name, String cc) throws Exception {
        BaseCode baseCode = new BaseCode();
        String url = InterfaceDao.search(InterfaceConfig.SET_PHONE);
        if (url != null && url.equals(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        if (url == null) {
            return baseCode;
        }
        Map<String, String> signMap = new HashMap();
        signMap.put(Constant.ACTION, HttpTools.getAction(url));
        signMap.put(ImConstants.USER_ID, cc);
        signMap.put(Constants.APP_ID, Constant.APP_ID);
        signMap.put(Constants.VER, Constant.APP_VERSION);
        signMap.put("uname", name);
        StringBuilder sb = HttpTools.getAllUrl(url, cc, HttpTools.getSign(mToken, signMap));
        Map<String, String> params = new HashMap();
        params.put("uname", name);
        String result = HttpInfoClient.getInstance().sendPost(sb.toString(), params);
        if (result != null) {
            try {
                if (!result.equalsIgnoreCase("NULL")) {
                    JSONObject jsonObject_u = new JSONObject(result);
                    if (!jsonObject_u.has("code")) {
                        return baseCode;
                    }
                    baseCode.setCode(jsonObject_u.getInt("code"));
                    return baseCode;
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public BaseCode modiPsw(String cc, String oldPsw, String newPsw) throws Exception {
        BaseCode baseCode = new BaseCode();
        String url = InterfaceDao.search(InterfaceConfig.MODI_PASS);
        if (url != null && url.equals(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        if (url == null) {
            return baseCode;
        }
        Map<String, String> signMap = new HashMap();
        signMap.put(Constant.ACTION, HttpTools.getAction(url));
        signMap.put(ImConstants.USER_ID, cc);
        signMap.put(Constants.APP_ID, Constant.APP_ID);
        signMap.put(Constants.VER, Constant.APP_VERSION);
        signMap.put("pw", oldPsw);
        signMap.put("chpw", newPsw);
        StringBuilder sb = HttpTools.getAllUrl(url, cc, HttpTools.getSign(mToken, signMap));
        Map<String, String> params = new HashMap();
        params.put("pw", oldPsw);
        params.put("chpw", newPsw);
        String result = HttpInfoClient.getInstance().sendPost(sb.toString(), params);
        if (result != null) {
            try {
                if (!result.equalsIgnoreCase("NULL")) {
                    JSONObject jsonObject_u = new JSONObject(result);
                    if (!jsonObject_u.has("code")) {
                        return baseCode;
                    }
                    baseCode.setCode(jsonObject_u.getInt("code"));
                    return baseCode;
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public CountryListResult getAreaCountry(String lan) throws Exception {
        CountryListResult result = new CountryListResult();
        ArrayList<CountryInfo> datas = new ArrayList();
        String url = InterfaceDao.search(InterfaceConfig.AREA_GET_COUNTRY_LIST);
        if (url != null && url.equals(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("lan", lan));
        params.add(new BasicNameValuePair(Constants.APP_ID, Constant.APP_ID));
        httpPost.setEntity(new UrlEncodedFormEntity(params, AsyncHttpResponseHandler.DEFAULT_CHARSET));
        HttpResponse httpResp = new DefaultHttpClient().execute(httpPost);
        if (httpResp.getStatusLine().getStatusCode() != 200) {
            return null;
        }
        String content = EntityUtils.toString(httpResp.getEntity(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
        if (content == null) {
            return result;
        }
        try {
            Log.i("getAreaCountry", "getAreaCountry" + content);
            JSONObject data = new JSONObject(content);
            if (data.has("code")) {
                result.setCode(data.getInt("code"));
            }
            if (!data.has(DataPacketExtension.ELEMENT_NAME)) {
                return result;
            }
            JSONArray jsonArray = data.getJSONArray(DataPacketExtension.ELEMENT_NAME);
            for (int i = 0; i < jsonArray.length(); i++) {
                CountryInfo info = new CountryInfo();
                JSONObject infoObj = jsonArray.getJSONObject(i);
                if (infoObj.has("ncode")) {
                    info.setnCode(infoObj.getInt("ncode"));
                }
                if (infoObj.has("display")) {
                    info.setCountry(infoObj.getString("display"));
                }
                if (infoObj.has("is_sms")) {
                    info.setiSsms(infoObj.getInt("is_sms"));
                }
                if (infoObj.has("pre_code")) {
                    info.setPreCode(infoObj.getString("pre_code"));
                }
                datas.add(info);
            }
            result.setDatas(datas);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setError(true);
            return result;
        }
    }

    public BaseCode updateData(String cc, String oldPsw, String newPsw) throws Exception {
        BaseCode baseCode = new BaseCode();
        String url = InterfaceDao.search(InterfaceConfig.MODI_PASS);
        if (url != null && url.equals(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        if (url == null) {
            return baseCode;
        }
        Map<String, String> signMap = new HashMap();
        signMap.put(Constant.ACTION, HttpTools.getAction(url));
        signMap.put(ImConstants.USER_ID, cc);
        signMap.put(Constants.APP_ID, Constant.APP_ID);
        signMap.put(Constants.VER, Constant.APP_VERSION);
        signMap.put("pw", oldPsw);
        signMap.put("chpw", newPsw);
        StringBuilder sb = HttpTools.getAllUrl(url, cc, HttpTools.getSign(mToken, signMap));
        Map<String, String> params = new HashMap();
        params.put("pw", oldPsw);
        params.put("chpw", newPsw);
        String result = HttpInfoClient.getInstance().sendPost(sb.toString(), params);
        if (result != null) {
            try {
                if (!result.equalsIgnoreCase("NULL")) {
                    JSONObject jsonObject_u = new JSONObject(result);
                    if (!jsonObject_u.has("code")) {
                        return baseCode;
                    }
                    baseCode.setCode(jsonObject_u.getInt("code"));
                    return baseCode;
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public UserDataResult getContackData(String cc) throws Exception {
        UserDataResult baseCode = new UserDataResult();
        String url = InterfaceDao.search(InterfaceConfig.GET_CONTACT_DATA);
        if (url != null && url.equals(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        if (url == null) {
            return baseCode;
        }
        Map<String, String> signMap = new HashMap();
        signMap.put(Constant.ACTION, HttpTools.getAction(url));
        signMap.put(ImConstants.USER_ID, cc);
        signMap.put(Constants.APP_ID, Constant.APP_ID);
        signMap.put(Constants.VER, Constant.APP_VERSION);
        StringBuilder sb = HttpTools.getAllUrl(url, cc, HttpTools.getSign(mToken, signMap));
        String result = HttpInfoClient.getInstance().sendPost(sb.toString(), new HashMap());
        if (result != null) {
            try {
                if (!result.equalsIgnoreCase("NULL")) {
                    JSONObject jsonObject_u = new JSONObject(result);
                    if (jsonObject_u.has("code")) {
                        baseCode.setCode(jsonObject_u.getInt("code"));
                    }
                    if (!jsonObject_u.has(DataPacketExtension.ELEMENT_NAME)) {
                        return baseCode;
                    }
                    JSONObject data = jsonObject_u.getJSONObject(DataPacketExtension.ELEMENT_NAME);
                    if (data.has("email")) {
                        baseCode.setEmail(data.getString("email"));
                    }
                    if (data.has("mobile")) {
                        baseCode.setMobile(data.getString("mobile"));
                    }
                    if (data.has("u_email")) {
                        baseCode.setuEmail(data.getString("u_email"));
                    }
                    if (data.has("u_mobile")) {
                        baseCode.setuMobile(data.getString("u_mobile"));
                    }
                    if (data.has("is_bind_email")) {
                        baseCode.setIsBindEmali(data.getString("is_bind_email"));
                    }
                    if (data.has("is_bind_mobile")) {
                        baseCode.setIsBindMobile(data.getString("is_bind_mobile"));
                    }
                    if (data.has("qq")) {
                        baseCode.setQq(data.getString("qq"));
                    }
                    if (data.has(BaseProfile.COL_WEIBO)) {
                        baseCode.setWeibo(data.getString(BaseProfile.COL_WEIBO));
                    }
                    if (data.has("facebook")) {
                        baseCode.setFacebook(data.getString("facebook"));
                    }
                    if (!data.has("twitter")) {
                        return baseCode;
                    }
                    baseCode.setTwitter(data.getString("twitter"));
                    return baseCode;
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public UserData getUserData(String cc) throws Exception {
        UserData baseCode = new UserData();
        String url = InterfaceDao.search(InterfaceConfig.GET_USER_DATA);
        if (url != null && url.equals(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        if (url == null) {
            return baseCode;
        }
        Map<String, String> signMap = new HashMap();
        signMap.put(Constant.ACTION, HttpTools.getAction(url));
        signMap.put(ImConstants.USER_ID, cc);
        signMap.put(Constants.APP_ID, Constant.APP_ID);
        signMap.put(Constants.VER, Constant.APP_VERSION);
        StringBuilder sb = HttpTools.getAllUrl(url, cc, HttpTools.getSign(mToken, signMap));
        String result = HttpInfoClient.getInstance().sendPost(sb.toString(), new HashMap());
        if (result != null) {
            try {
                if (!result.equalsIgnoreCase("NULL")) {
                    JSONObject jsonObject_u = new JSONObject(result);
                    if (jsonObject_u.has("code")) {
                        baseCode.setCode(jsonObject_u.getInt("code"));
                    }
                    if (!jsonObject_u.has(DataPacketExtension.ELEMENT_NAME)) {
                        return baseCode;
                    }
                    JSONObject data = jsonObject_u.getJSONObject(DataPacketExtension.ELEMENT_NAME);
                    if (data.has(ImConstants.USER_ID)) {
                        baseCode.setUserId(data.getInt(ImConstants.USER_ID));
                    }
                    if (data.has("zipcode")) {
                        baseCode.setZipCode(data.getString("zipcode"));
                    }
                    if (data.has("country")) {
                        baseCode.setCountry(data.getString("country"));
                    }
                    if (data.has(BaseProfile.COL_PROVINCE)) {
                        baseCode.setProvince(data.getString(BaseProfile.COL_PROVINCE));
                    }
                    if (!data.has(BaseProfile.COL_CITY)) {
                        return baseCode;
                    }
                    baseCode.setCity(data.getString(BaseProfile.COL_CITY));
                    return baseCode;
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public BaseCode findPsw(String userName, String psw, String comfirPsw, String verifyCode) throws Exception {
        BaseCode result = null;
        String url = InterfaceDao.search(InterfaceConfig.VERIFY_RESET_PASS);
        if (url != null && url.equals(XmlPullParser.NO_NAMESPACE)) {
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("req", userName));
        params.add(new BasicNameValuePair("pass", psw));
        params.add(new BasicNameValuePair("confirm_pass", comfirPsw));
        params.add(new BasicNameValuePair("verify_code", verifyCode));
        httpPost.setEntity(new UrlEncodedFormEntity(params, AsyncHttpResponseHandler.DEFAULT_CHARSET));
        HttpResponse httpResp = new DefaultHttpClient().execute(httpPost);
        if (httpResp.getStatusLine().getStatusCode() != AsyncTaskManager.REQUEST_SUCCESS_CODE) {
            return null;
        }
        String content = EntityUtils.toString(httpResp.getEntity(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
        if (content != null) {
            try {
                result = (BaseCode) jsonToBean(content, BaseCode.class);
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static <T> T jsonToBean(String json, Class<T> cls) throws DecodeException {
        try {
            return jsonMapper.readValue(json, (Class) cls);
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new DecodeException(-2);
        } catch (JsonMappingException e2) {
            e2.printStackTrace();
            throw new DecodeException(-2);
        } catch (IOException e3) {
            e3.printStackTrace();
            throw new DecodeException(-2);
        }
    }

    public static ObjectMapper getJSONMapper() {
        if (jsonMapper == null) {
            jsonMapper = new ObjectMapper();
            jsonMapper.getSerializationConfig().setSerializationInclusion(Inclusion.ALWAYS);
            jsonMapper.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return jsonMapper;
    }

    public RemindBean getWarnInfoList(String client_id, String serial_no, String size, String page) {
        RemindBean remindBean = new RemindBean();
        ArrayList<RemindDetailBean> datas = new ArrayList();
        String cs_warn_url = "http://192.168.85.212:8081/application/?action=client_service.get_history";
        cs_warn_url = InterfaceDao.search(InterfaceConfig.WARNING_INFO);
        if (cs_warn_url == null) {
            return remindBean;
        }
        Map<String, String> signMap_warninfo = new HashMap();
        signMap_warninfo.put(Constant.ACTION, HttpTools.getAction(cs_warn_url));
        signMap_warninfo.put("client_id", cc);
        if (isNull(serial_no)) {
            signMap_warninfo.put("serial_no", serial_no);
        }
        if (isNull(size)) {
            signMap_warninfo.put("size", size);
        }
        if (isNull(page)) {
            signMap_warninfo.put("page", page);
        }
        try {
            StringBuilder sb_warn_info = HttpTools.getAllUrl(cs_warn_url, cc, SignUtils.getSign(mToken, signMap_warninfo));
            Map<String, String> params_warn_info = new HashMap();
            params_warn_info.put("client_id", cc);
            if (isNull(serial_no)) {
                params_warn_info.put("serial_no", serial_no);
            }
            if (isNull(serial_no)) {
                params_warn_info.put("size", size);
            }
            if (isNull(page)) {
                params_warn_info.put("page", page);
            }
            String result_warn_info = HttpInfoClient.getInstance().sendGet(HttpTools.getUrlTool(sb_warn_info, params_warn_info));
            if (result_warn_info != null) {
                if (!result_warn_info.equalsIgnoreCase("NULL")) {
                    JSONObject jSONObject = new JSONObject(result_warn_info);
                    remindBean.setCode(jSONObject.getInt("code"));
                    remindBean.setMsg(jSONObject.getString(Msg.MSG_CONTENT));
                    JSONObject jsonObject1 = jSONObject.getJSONObject(DataPacketExtension.ELEMENT_NAME);
                    remindBean.setTotalSize(jsonObject1.getInt("size"));
                    remindBean.setUnReadSize(jsonObject1.getInt("unreadsize"));
                    JSONArray jsonArray = jsonObject1.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                        datas.add(new RemindDetailBean(jsonObject2.getString(LocaleUtil.INDONESIAN), jsonObject2.getString("item_id"), jsonObject2.getString("alarm_type"), jsonObject2.getString(Msg.MSG_CONTENT), jsonObject2.getString("content"), jsonObject2.getString("serial_no"), jsonObject2.getString("technician_id"), Integer.parseInt(jsonObject2.getString("is_read")), jsonObject2.getString(Msg.TIME_REDIO)));
                    }
                    remindBean.setDatas(datas);
                    return remindBean;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return remindBean;
        }
    }

    private boolean isNull(String params) {
        if (params.length() <= 0 || params.equalsIgnoreCase("null")) {
            return false;
        }
        return true;
    }

    public String modifyWarnInfoState(String id, String client_id) {
        String cs_warn_url = "http://192.168.85.212:8081/application/?action=client_service.read";
        cs_warn_url = InterfaceDao.search(InterfaceConfig.WARNING_STATE);
        Map<String, String> signMap_warninfo = new HashMap();
        signMap_warninfo.put(Constant.ACTION, "client_service.read");
        signMap_warninfo.put("client_id", cc);
        signMap_warninfo.put(LocaleUtil.INDONESIAN, id);
        try {
            StringBuilder sb_warn_info = HttpTools.getAllUrl(cs_warn_url, cc, SignUtils.getSign(mToken, signMap_warninfo));
            Map<String, String> params_warn_info = new HashMap();
            params_warn_info.put("client_id", cc);
            params_warn_info.put(LocaleUtil.INDONESIAN, id);
            String result_warn_info = HttpInfoClient.getInstance().sendGet(HttpTools.getUrlTool(sb_warn_info, params_warn_info));
            if (result_warn_info == null || result_warn_info.equalsIgnoreCase("NULL")) {
                return XmlPullParser.NO_NAMESPACE;
            }
            return new StringBuilder(String.valueOf(new JSONObject(result_warn_info).getInt("code"))).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return XmlPullParser.NO_NAMESPACE;
        }
    }

    public int getWarnInfoSize(String client_id) {
        String cs_warn_url = "http://192.168.85.212:8081/application/?action=client_service.get_history";
        cs_warn_url = InterfaceDao.search(InterfaceConfig.WARNING_INFO);
        Map<String, String> signMap_warninfo = new HashMap();
        signMap_warninfo.put(Constant.ACTION, HttpTools.getAction(cs_warn_url));
        signMap_warninfo.put("client_id", cc);
        try {
            StringBuilder sb_warn_info = HttpTools.getAllUrl(cs_warn_url, cc, SignUtils.getSign(mToken, signMap_warninfo));
            Map<String, String> params_warn_info = new HashMap();
            params_warn_info.put("client_id", cc);
            return new JSONObject(HttpInfoClient.getInstance().sendGet(HttpTools.getUrlTool(sb_warn_info, params_warn_info))).getJSONObject(DataPacketExtension.ELEMENT_NAME).getInt("unreadsize");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getPubId() {
        String pub_id = XmlPullParser.NO_NAMESPACE;
        String techPath = InterfaceDao.search(InterfaceConfig.MY_TECH_ID);
        String result = XmlPullParser.NO_NAMESPACE;
        if (techPath != null) {
            Map<String, String> signMap = new HashMap();
            signMap.put(Constant.ACTION, HttpTools.getAction(techPath));
            signMap.put(ImConstants.USER_ID, cc);
            signMap.put(Constants.APP_ID, Constant.APP_ID);
            signMap.put(Constants.VER, Constant.APP_VERSION);
            signMap.put("tech_id", cc);
            StringBuilder sb = HttpTools.getAllUrl(techPath, cc, HttpTools.getSign(mToken, signMap));
            Map<String, String> params = new HashMap();
            params.put("tech_id", cc);
            result = HttpInfoClient.getInstance().sendPost(sb.toString(), params);
            if (result != null) {
                try {
                    if (!result.equalsIgnoreCase("NULL")) {
                        JSONObject jsonObject_u = new JSONObject(result);
                        if (jsonObject_u.getInt("code") == 0) {
                            pub_id = jsonObject_u.getJSONObject(DataPacketExtension.ELEMENT_NAME).getString("pub_id");
                        }
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    return XmlPullParser.NO_NAMESPACE;
                }
            }
            return XmlPullParser.NO_NAMESPACE;
        }
        return pub_id;
    }
}
