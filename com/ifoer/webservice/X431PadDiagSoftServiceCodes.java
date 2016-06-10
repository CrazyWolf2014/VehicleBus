package com.ifoer.webservice;

import android.content.Context;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.util.MyHttpException;
import org.xmlpull.v1.XmlPullParser;

public class X431PadDiagSoftServiceCodes {
    public String queryLatestDiagSoftsCode(int code, Context context) {
        String message = XmlPullParser.NO_NAMESPACE;
        switch (code) {
            case MyHttpException.ERROR_PARAMETER_EMPTY /*401*/:
                return context.getResources().getString(C0136R.string.notic_null);
            case MyHttpException.ERROR_SERVER /*500*/:
                return context.getResources().getString(C0136R.string.ERROR_SERVER);
            case MyHttpException.ERROR_NOTTHIS_LAN_SOFT /*606*/:
                return context.getResources().getString(C0136R.string.ERROR_NOTTHIS_LAN_SOFT);
            case MyHttpException.ERROR_NOT_REG_THIS /*662*/:
                return context.getResources().getString(C0136R.string.ERROR_NOT_REG_THIS);
            case 795:
                return context.getResources().getString(C0136R.string.package_null);
            default:
                return message;
        }
    }

    public String queryHistoryDiagSoftsCode(int code, Context context) {
        String message = XmlPullParser.NO_NAMESPACE;
        switch (code) {
            case MyHttpException.ERROR_PARAMETER_EMPTY /*401*/:
                return context.getResources().getString(C0136R.string.notic_null);
            case MyHttpException.ERROR_SERVER /*500*/:
                return context.getResources().getString(C0136R.string.ERROR_SERVER);
            case MyHttpException.ERROR_EMPTY_SOFTLIST /*607*/:
                return context.getResources().getString(C0136R.string.ERROR_EMPTY_SOFTLIST);
            default:
                return message;
        }
    }
}
