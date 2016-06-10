package com.car.result;

import android.content.Context;
import com.cnlaunch.framework.common.Constants;
import com.cnmobi.im.bo.ImConstants;
import com.cnmobi.im.dto.Msg;
import com.ifoer.entity.Constant;
import com.tencent.mm.sdk.plugin.BaseProfile;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.jivesoftware.smackx.workgroup.packet.UserID;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginResult extends WSResult {
    protected String cc;
    private String city;
    public String domain;
    public String email;
    public String face_url;
    public String ip;
    private Context mContext;
    private String message;
    public String mobile;
    private String name;
    public String nick_name;
    private String password;
    public int port;
    protected Long serverSystemTime;
    public int set_face_time;
    public String signature;
    protected String token;
    protected String userName;
    public String user_id;
    private String user_json;

    public LoginResult(Context context, String name, String password) {
        this.name = name;
        this.password = password;
        this.mContext = context;
    }

    public void decode(String content) throws Exception {
        try {
            JSONObject json = new JSONObject(content);
            if (json.has(Msg.MSG_CONTENT)) {
                this.message = json.getString(Msg.MSG_CONTENT);
            }
            if (json.has("code")) {
                setCode(json.getInt("code"));
                if (json.getInt("code") == 0) {
                    JSONObject data = json.getJSONObject(DataPacketExtension.ELEMENT_NAME);
                    JSONObject user = data.getJSONObject(UserID.ELEMENT_NAME);
                    JSONObject xmpp = data.getJSONObject("xmpp");
                    this.token = getString(data, Constants.TOKEN);
                    this.cc = getString(user, ImConstants.USER_ID);
                    this.nick_name = getString(user, "nick_name");
                    this.userName = getString(user, "user_name");
                    this.city = getString(user, BaseProfile.COL_CITY);
                    this.email = getString(user, "email");
                    this.face_url = getString(user, "face_url");
                    this.user_id = getString(user, ImConstants.USER_ID);
                    this.signature = getString(user, BaseProfile.COL_SIGNATURE);
                    this.mobile = getString(user, "mobile");
                    this.set_face_time = getInt(user, "set_face_time");
                    this.ip = getString(xmpp, "ip");
                    this.port = getInt(xmpp, "port");
                    this.domain = getString(xmpp, "domain");
                }
            }
        } catch (JSONException e) {
            setCode(Constant.ERROR_NETWORK);
        } catch (Exception e2) {
            setCode(Constant.ERROR_SERVER);
        }
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCc() {
        return this.cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public Long getServerSystemTime() {
        return this.serverSystemTime;
    }

    public void setServerSystemTime(Long serverSystemTime) {
        this.serverSystemTime = serverSystemTime;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNick_name() {
        return this.nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getSet_face_time() {
        return this.set_face_time;
    }

    public void setSet_face_time(int set_face_time) {
        this.set_face_time = set_face_time;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFace_url() {
        return this.face_url;
    }

    public void setFace_url(String face_url) {
        this.face_url = face_url;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_json() {
        return this.user_json;
    }

    public void setUser_json(String user_json) {
        this.user_json = user_json;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Context getmContext() {
        return this.mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
