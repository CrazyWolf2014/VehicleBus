package com.ifoer.expeditionphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.ifoer.entity.Constant;

public class UsbBroadCastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ((action.equals("android.intent.action.MEDIA_EJECT") || action.equals("android.intent.action.MEDIA_MOUNTED")) && BaseActivity.mContexts != null) {
            Constant.referValue();
            context.sendBroadcast(new Intent("CLOSE_DATASTREAM_ACTIVITY"));
        }
    }
}
