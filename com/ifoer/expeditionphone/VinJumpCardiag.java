package com.ifoer.expeditionphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;

public class VinJumpCardiag extends BroadcastReceiver {
    public String mDataDir;
    public String mPaths;
    private String mSdPaths;

    public VinJumpCardiag() {
        this.mSdPaths = XmlPullParser.NO_NAMESPACE;
        this.mDataDir = XmlPullParser.NO_NAMESPACE;
        this.mPaths = XmlPullParser.NO_NAMESPACE;
    }

    public void onReceive(Context context, Intent intent) {
        Log.e("bcf", "VinJumpCardiag: " + intent.getAction());
        intent.getAction().equals("X431.FRAMEWORK.VIN.JUMP.DIAG");
    }
}
