package com.cnmobi.im.dto;

import android.widget.ImageView;
import com.ifoer.mine.Contact;
import com.launch.rcu.socket.SocketCode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

public class Msg {
    public static final String DATE = "date";
    public static final String FIL_PAHT = "filePath";
    public static final String FROM = "from";
    public static final String[] FROM_TYPE;
    public static final String MSG_CONTENT = "msg";
    public static final String MSG_TYPE = "type";
    public static final String RECEIVE_STAUTS = "receive";
    public static final String[] STATUS;
    public static final String TIME_REDIO = "time";
    public static final String[] TYPE;
    public static final String USERID = "userid";
    String date;
    String filePath;
    String from;
    public String jid;
    public ImageView logoView;
    String msg;
    String receive;
    String time;
    String type;
    String userid;

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    static {
        STATUS = new String[]{"success", "refused", "fail", "wait"};
        TYPE = new String[]{SocketCode.REMOTE_RECORD_BUTTON, "photo", "normal"};
        FROM_TYPE = new String[]{MessageVo.DIRECTION_IN, MessageVo.DIRECTION_OUT};
    }

    public Msg() {
        this.type = TYPE[2];
    }

    public Msg(String userid, String msg, String date, String from) {
        this.type = TYPE[2];
        this.userid = userid;
        this.msg = msg;
        this.date = date;
        this.from = from;
    }

    public Msg(String userid, String msg, String date, String from, String type, String receive, String time, String filePath) {
        this.type = TYPE[2];
        this.userid = userid;
        this.msg = msg;
        this.date = date;
        this.from = from;
        this.type = type;
        this.receive = receive;
        this.time = time;
        this.filePath = filePath;
    }

    public String getType() {
        return this.type;
    }

    public String toString() {
        return "Msg [userid=" + this.userid + ", msg=" + this.msg + ", date=" + this.date + ", from=" + this.from + ", type=" + this.type + ", receive=" + this.receive + ", time=" + this.time + ", filePath=" + this.filePath + "]";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceive() {
        return this.receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public static String[] getStatus() {
        return STATUS;
    }

    public Msg(String userid, String msg, String date, String from, String type, String receive) {
        this.type = TYPE[2];
        this.userid = userid;
        this.msg = msg;
        this.date = date;
        this.from = from;
        this.type = type;
        this.receive = receive;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public ImageView getLogoView() {
        return this.logoView;
    }

    public void setLogoView(ImageView logoView) {
        this.logoView = logoView;
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public static Msg analyseMsgBody(String jsonStr) throws Exception {
        Msg msg = new Msg();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            msg.setUserid(jsonObject.getString(USERID));
            msg.setFrom(jsonObject.getString(FROM));
            msg.setMsg(jsonObject.getString(MSG_CONTENT));
            msg.setDate(jsonObject.getString(DATE));
            msg.setType(jsonObject.getString(MSG_TYPE));
            msg.setReceive(jsonObject.getString(RECEIVE_STAUTS));
            msg.setTime(jsonObject.getString(TIME_REDIO));
            msg.setFilePath(jsonObject.getString(FIL_PAHT));
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (Throwable th) {
        }
        return msg;
    }

    public static String toJson(Msg msg) {
        JSONObject jsonObject = new JSONObject();
        String jsonStr = XmlPullParser.NO_NAMESPACE;
        if (msg.getTime() == null) {
            msg.setTime(Contact.RELATION_ASK);
        }
        if (msg.getFilePath() == null) {
            msg.setFilePath(XmlPullParser.NO_NAMESPACE);
        }
        try {
            jsonObject.put(USERID, new StringBuilder(String.valueOf(msg.getUserid())).toString());
            jsonObject.put(MSG_CONTENT, new StringBuilder(String.valueOf(msg.getMsg())).toString());
            jsonObject.put(DATE, new StringBuilder(String.valueOf(msg.getDate())).toString());
            jsonObject.put(FROM, new StringBuilder(String.valueOf(msg.getFrom())).toString());
            jsonObject.put(MSG_TYPE, new StringBuilder(String.valueOf(msg.getType())).toString());
            jsonObject.put(RECEIVE_STAUTS, new StringBuilder(String.valueOf(msg.getReceive())).toString());
            jsonObject.put(TIME_REDIO, msg.getTime());
            jsonObject.put(FIL_PAHT, msg.getFilePath());
            jsonStr = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Throwable th) {
        }
        return jsonStr;
    }
}
