package com.ifoer.util;

import android.content.Context;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.expeditionphone.MainActivity;
import java.util.Properties;
import org.xmlpull.v1.XmlPullParser;

public class BillServiceException {
    public static final int CONNECT_ERROR = -1;
    public static final int PARAM_EMPTY = 401;
    public static final int PRO_UNREGISTER = 653;
    public static final int SERVICE_EXCEPTION = 500;
    private static Properties codeProperties;
    private int code;

    static {
        codeProperties = new Properties();
        codeProperties.put(Integer.valueOf(PARAM_EMPTY), MainActivity.contexts.getResources().getString(C0136R.string.notic_null));
        codeProperties.put(Integer.valueOf(PRO_UNREGISTER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_UNREGISTER_PRODUCT));
        codeProperties.put(Integer.valueOf(PRO_UNREGISTER), MainActivity.contexts.getResources().getString(C0136R.string.ERROR_SERVER));
        codeProperties.put(Integer.valueOf(CONNECT_ERROR), MainActivity.contexts.getResources().getString(C0136R.string.connect_server_error));
    }

    public BillServiceException(int code) {
        this.code = 0;
        this.code = code;
    }

    public String getMessage() {
        String str1 = XmlPullParser.NO_NAMESPACE;
        if (this.code != 0) {
            return (String) codeProperties.get(Integer.valueOf(this.code));
        }
        return str1;
    }

    public void showToast(Context context) {
        Toast.makeText(context, getMessage(), 0).show();
    }
}
