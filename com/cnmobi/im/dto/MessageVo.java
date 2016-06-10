package com.cnmobi.im.dto;

import android.widget.ImageView;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageVo {
    public static final String DIRECTION_IN = "IN";
    public static final String DIRECTION_OUT = "OUT";
    public static final String TYPE_AUDIO = "record";
    public static final String TYPE_FILE = "file";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_VIDEO = "video";
    public String content;
    public String direction;
    public int duration;
    public String filePath;
    public String jId;
    public ImageView logoView;
    public String ownerJid;
    public int readStatus;
    public String time;
    public String type;

    public MessageVo(String jId, String time, String content, String direction) {
        this.type = TYPE_TEXT;
        this.jId = jId;
        this.time = time;
        this.content = content;
        this.direction = direction;
    }

    public MessageVo(String jsonStr) {
        this.type = TYPE_TEXT;
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            this.jId = jsonObject.getString(Msg.USERID);
            this.content = jsonObject.getString(Msg.MSG_CONTENT);
            this.time = jsonObject.getString(Msg.DATE);
            this.type = jsonObject.getString(SharedPref.TYPE);
            this.duration = jsonObject.getInt(Msg.TIME_REDIO);
            this.filePath = jsonObject.getString(Msg.FIL_PAHT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
