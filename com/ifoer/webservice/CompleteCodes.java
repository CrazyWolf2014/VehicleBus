package com.ifoer.webservice;

import android.content.Context;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.util.MyHttpException;
import org.xmlpull.v1.XmlPullParser;

public class CompleteCodes {
    public String completeCode(int code, Context context) {
        String msg = XmlPullParser.NO_NAMESPACE;
        switch (code) {
            case MyHttpException.ERROR_PARAMETER_EMPTY /*401*/:
                return context.getResources().getText(C0136R.string.notic_null).toString();
            case MyHttpException.ERROR_SERVER /*500*/:
                return context.getResources().getText(C0136R.string.the_service_side_abnormal).toString();
            case MyHttpException.ERROR_DONT_HAVE_ORDER /*753*/:
                return context.getResources().getText(C0136R.string.ERROR_DONT_HAVE_ORDER).toString();
            case MyHttpException.ERROR_INVALID_ORDER_PPAY /*754*/:
                return context.getResources().getText(C0136R.string.ERROR_INVALID_ORDER_PPAY).toString();
            case MyHttpException.ERROR_ORDER_PAID /*756*/:
                return context.getResources().getText(C0136R.string.ERROR_ORDER_PAID).toString();
            default:
                return msg;
        }
    }
}
