package com.cnmobi.im.cnlaunch.net;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.car.result.GetEndUserFullResult;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.dto.MeVo;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.webservice.UserServiceClient;
import java.net.SocketTimeoutException;
import org.xmlpull.v1.XmlPullParser;

public class CnlaunchDataManager {
    private static CnlaunchDataManager instance;
    GetEndUserFullResult getEndUserFullResult;
    String token;

    static {
        instance = new CnlaunchDataManager();
    }

    private CnlaunchDataManager() {
    }

    public static CnlaunchDataManager getInstance() {
        return instance;
    }

    public MeVo getMeInfo(String cc) {
        UserServiceClient client = new UserServiceClient();
        cc = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.CCKey);
        this.token = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.TokenKey);
        client.setCc(cc);
        client.setToken(this.token);
        MeVo me = new MeVo();
        try {
            this.getEndUserFullResult = client.getEndUserFull(cc);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        if (this.getEndUserFullResult == null) {
            Log.d("wzw", XmlPullParser.NO_NAMESPACE);
        } else if (this.getEndUserFullResult.getCode() == -1) {
            Looper.prepare();
            SimpleDialog.validTokenDialog(MainActivity.contexts);
            Looper.loop();
        } else if (this.getEndUserFullResult.getCode() == 0) {
            if (!(XmlPullParser.NO_NAMESPACE.equals(this.getEndUserFullResult.getAddress()) || this.getEndUserFullResult.getAddress() == null)) {
                me.setArea(this.getEndUserFullResult.getAddress().toString());
            }
            if (!(XmlPullParser.NO_NAMESPACE.equals(this.getEndUserFullResult.getMobile()) || this.getEndUserFullResult.getMobile() == null)) {
                me.setPhone(this.getEndUserFullResult.getMobile().toString());
            }
            if (!(XmlPullParser.NO_NAMESPACE.equals(this.getEndUserFullResult.getEmail()) || this.getEndUserFullResult.getEmail() == null)) {
                me.setEmail(this.getEndUserFullResult.getEmail().toString());
            }
            if (!(XmlPullParser.NO_NAMESPACE.equals(this.getEndUserFullResult.getSex()) || this.getEndUserFullResult.getSex() == null)) {
                if (this.getEndUserFullResult.getSex().intValue() == 0) {
                    me.setSex("\u7537");
                } else if (this.getEndUserFullResult.getSex().intValue() == 1) {
                    me.setSex("\u5973");
                }
            }
            if (!(XmlPullParser.NO_NAMESPACE.equals(this.getEndUserFullResult.getUserName()) || this.getEndUserFullResult.getUserName() == null)) {
                me.setAccount(this.getEndUserFullResult.getUserName().toString());
            }
        } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_PARAMETER) {
            Toast.makeText(MainActivity.contexts, MainActivity.contexts.getResources().getString(C0136R.string.input_wrong), 1).show();
        } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_PARAMETER_EMPTY) {
            Toast.makeText(MainActivity.contexts, MainActivity.contexts.getResources().getString(C0136R.string.request_wrong), 1).show();
        } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_PARAMETER_ILLEGAL) {
            Toast.makeText(MainActivity.contexts, MainActivity.contexts.getResources().getString(C0136R.string.request_legal), 1).show();
        } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_RESULT_NOT_EXIST) {
            Toast.makeText(MainActivity.contexts, MainActivity.contexts.getResources().getString(C0136R.string.request_result_null), 1).show();
        } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_SERVER) {
            Toast.makeText(MainActivity.contexts, MainActivity.contexts.getResources().getString(C0136R.string.notic_serv), 1).show();
        } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_NETWORK) {
            Toast.makeText(MainActivity.contexts, MainActivity.contexts.getResources().getString(C0136R.string.network), 1).show();
        }
        return me;
    }
}
