package com.car.result;

import android.content.Context;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

public class WSResult implements Serializable {
    public static final int DONT_HAVE_THIS_USER = 384;
    public static final int ERROR_PASWD = 383;
    public static final int NETWORK_ERROR = 501;
    public static final int PARAMETER_ERROR = 400;
    public static final int REQUEST_NOT_EXIST = 405;
    public static final int REQUEST_PARAMETER_EMPTY = 401;
    public static final int REQUEST_PARAMETER_ILLEGAL = 402;
    public static final int SUCCESS = 0;
    public static final int SYSTEM_ERROR = 500;
    public static final int UNUSUAL_STATER_OF_USER = 398;
    private int code;
    private String message;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void failToast(Context context, int code) {
        switch (code) {
            case ERROR_PASWD /*383*/:
                Toast.makeText(context, context.getResources().getString(C0136R.string.ERROR_PASW), 1).show();
                return;
            case DONT_HAVE_THIS_USER /*384*/:
                Toast.makeText(context, context.getResources().getString(C0136R.string.ERROR_USER_NONENTITY), 1).show();
                return;
            case UNUSUAL_STATER_OF_USER /*398*/:
                Toast.makeText(context, context.getResources().getString(C0136R.string.ERROR_USER_STATE), 1).show();
                return;
            case PARAMETER_ERROR /*400*/:
                Toast.makeText(context, context.getResources().getString(C0136R.string.ERROR_PARAMETER), 1).show();
                return;
            case REQUEST_PARAMETER_EMPTY /*401*/:
                Toast.makeText(context, context.getResources().getString(C0136R.string.ERROR_PARAMETER_EMPTY), 1).show();
                return;
            case REQUEST_PARAMETER_ILLEGAL /*402*/:
                Toast.makeText(context, context.getResources().getString(C0136R.string.ERROR_PARAMETER_ILLEGAL), 1).show();
                return;
            case REQUEST_NOT_EXIST /*405*/:
                Toast.makeText(context, context.getResources().getString(C0136R.string.ERROR_RESULT_NOT_EXIST), 1).show();
                return;
            case SYSTEM_ERROR /*500*/:
                Toast.makeText(context, context.getResources().getString(C0136R.string.ERROR_SERVER), 1).show();
                break;
            case NETWORK_ERROR /*501*/:
                break;
            default:
                return;
        }
        Toast.makeText(context, context.getResources().getString(C0136R.string.ERROR_SERVER), 1).show();
    }

    protected String getString(JSONObject json, String tag) {
        try {
            return json.getString(tag);
        } catch (Exception e) {
            return XmlPullParser.NO_NAMESPACE;
        }
    }

    protected int getInt(JSONObject json, String tag) {
        try {
            return json.getInt(tag);
        } catch (Exception e) {
            return SUCCESS;
        }
    }

    protected void put(JSONObject json, String tag, Object value) {
        try {
            json.put(tag, value);
        } catch (Exception e) {
            try {
                json.put(tag, XmlPullParser.NO_NAMESPACE);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
}
