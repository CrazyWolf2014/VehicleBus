package com.iflytek.msc.p013f;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.Setting;
import com.iflytek.speech.SpeechError;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.msc.f.j */
public class C0282j {
    public static String m1262a(NetworkInfo networkInfo) {
        if (networkInfo == null) {
            return PrivacyRule.SUBSCRIPTION_NONE;
        }
        try {
            if (networkInfo.getType() == 1) {
                return "wifi";
            }
            String toLowerCase = networkInfo.getExtraInfo().toLowerCase();
            return TextUtils.isEmpty(toLowerCase) ? PrivacyRule.SUBSCRIPTION_NONE : (toLowerCase.startsWith("3gwap") || toLowerCase.startsWith("uniwap")) ? "uniwap" : toLowerCase.startsWith("cmwap") ? "cmwap" : toLowerCase.startsWith("ctwap") ? "ctwap" : toLowerCase;
        } catch (Exception e) {
            C0276e.m1220a(e.toString());
            return PrivacyRule.SUBSCRIPTION_NONE;
        }
    }

    public static void m1263a(Context context) throws SpeechError {
        if (Setting.f929b && context != null) {
            NetworkInfo[] allNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getAllNetworkInfo();
            if (allNetworkInfo != null && allNetworkInfo.length > 0) {
                int length = allNetworkInfo.length;
                int i = 0;
                while (i < length) {
                    if (!allNetworkInfo[i].isConnectedOrConnecting()) {
                        i++;
                    } else {
                        return;
                    }
                }
            }
            throw new SpeechError(1, SyncHttpTransportSE.DEFAULTTIMEOUT);
        }
    }

    public static String m1264b(NetworkInfo networkInfo) {
        if (networkInfo == null) {
            return PrivacyRule.SUBSCRIPTION_NONE;
        }
        try {
            if (networkInfo.getType() == 1) {
                return PrivacyRule.SUBSCRIPTION_NONE;
            }
            return (XmlPullParser.NO_NAMESPACE + networkInfo.getSubtypeName()) + ";" + networkInfo.getSubtype();
        } catch (Exception e) {
            C0276e.m1220a(e.toString());
            return PrivacyRule.SUBSCRIPTION_NONE;
        }
    }
}
