package com.cnlaunch.x431pro.module.crash.action;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.cnlaunch.framework.common.KeyConstant;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.x431pro.module.base.BaseAction;
import com.cnmobi.im.dto.MessageVo;
import java.io.File;
import java.io.FileNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

public class CrashAction extends BaseAction {
    private static final String TAG = "CrashAction";

    public CrashAction(Context mContext) {
        super(mContext);
    }

    public boolean upload(File file) throws HttpException, FileNotFoundException {
        boolean result = false;
        String url = getUrlByKey(KeyConstant.log_upload);
        this.params = getRequestParams();
        this.params.put(MessageVo.TYPE_FILE, file, XmlPullParser.NO_NAMESPACE);
        String json = this.httpManager.post(getSignUrl(url, this.params, false), this.params);
        if (!TextUtils.isEmpty(json)) {
            try {
                if (new JSONObject(json).getInt("code") == 0) {
                    result = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "result:" + result + ",json:" + json);
        return result;
    }
}
