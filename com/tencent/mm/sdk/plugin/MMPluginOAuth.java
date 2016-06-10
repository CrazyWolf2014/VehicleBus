package com.tencent.mm.sdk.plugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.tencent.mm.sdk.platformtools.Log;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.OAuth;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.PluginIntent;
import java.util.HashMap;
import java.util.Map;

public class MMPluginOAuth {
    private final IResult bG;
    private String bH;
    private String bI;
    private Handler handler;
    private final Context f1660q;

    /* renamed from: com.tencent.mm.sdk.plugin.MMPluginOAuth.1 */
    class C08761 implements Runnable {
        final /* synthetic */ MMPluginOAuth bJ;

        C08761(MMPluginOAuth mMPluginOAuth) {
            this.bJ = mMPluginOAuth;
        }

        public void run() {
            if (this.bJ.bG != null) {
                this.bJ.bG.onResult(this.bJ);
            }
        }
    }

    public interface IResult {
        void onResult(MMPluginOAuth mMPluginOAuth);

        void onSessionTimeOut();
    }

    public static class Receiver extends BroadcastReceiver {
        private static final Map<String, MMPluginOAuth> ah;
        private final MMPluginOAuth bK;

        /* renamed from: com.tencent.mm.sdk.plugin.MMPluginOAuth.Receiver.1 */
        class C08771 implements Runnable {
            final /* synthetic */ MMPluginOAuth bL;
            final /* synthetic */ String bM;
            final /* synthetic */ Receiver bN;

            C08771(Receiver receiver, MMPluginOAuth mMPluginOAuth, String str) {
                this.bN = receiver;
                this.bL = mMPluginOAuth;
                this.bM = str;
            }

            public void run() {
                MMPluginOAuth.m1695a(this.bL, this.bM);
            }
        }

        static {
            ah = new HashMap();
        }

        public Receiver() {
            this(null);
        }

        public Receiver(MMPluginOAuth mMPluginOAuth) {
            this.bK = mMPluginOAuth;
        }

        public static void register(String str, MMPluginOAuth mMPluginOAuth) {
            ah.put(str, mMPluginOAuth);
        }

        public static void unregister(String str) {
            ah.remove(str);
        }

        public void onReceive(Context context, Intent intent) {
            MMPluginOAuth mMPluginOAuth;
            Log.m1655d("MicroMsg.SDK.MMPluginOAuth", "receive oauth result");
            String stringExtra = intent.getStringExtra(PluginIntent.REQUEST_TOKEN);
            String stringExtra2 = intent.getStringExtra(PluginIntent.ACCESS_TOKEN);
            if (this.bK != null) {
                mMPluginOAuth = this.bK;
            } else {
                mMPluginOAuth = (MMPluginOAuth) ah.get(stringExtra);
                if (mMPluginOAuth == null) {
                    Log.m1657e("MicroMsg.SDK.MMPluginOAuth", "oauth unregistered, request token = " + stringExtra);
                    return;
                }
                unregister(mMPluginOAuth.bI);
            }
            new Handler().post(new C08771(this, mMPluginOAuth, stringExtra2));
        }
    }

    public MMPluginOAuth(Context context, IResult iResult) {
        this.f1660q = context;
        this.bG = iResult;
    }

    static /* synthetic */ void m1695a(MMPluginOAuth mMPluginOAuth, String str) {
        Receiver.unregister(mMPluginOAuth.bI);
        mMPluginOAuth.bH = str;
        Log.m1661i("MicroMsg.SDK.MMPluginOAuth", "access token: " + str);
        if (mMPluginOAuth.bG != null) {
            mMPluginOAuth.bG.onResult(mMPluginOAuth);
        }
    }

    public String getAccessToken() {
        return this.bH;
    }

    public String getRequestToken() {
        return this.bI;
    }

    public void start() {
        start(null);
    }

    public boolean start(Handler handler) {
        if (handler == null) {
            handler = new Handler();
        }
        this.handler = handler;
        Cursor query = this.f1660q.getContentResolver().query(OAuth.CONTENT_URI, null, null, new String[]{this.f1660q.getPackageName(), OAuth.ACTION_REQUEST_TOKEN}, null);
        if (query != null) {
            if (query.moveToFirst() && query.getColumnCount() >= 2) {
                this.bI = query.getString(0);
                this.bH = query.getString(1);
            }
            query.close();
        }
        Log.m1661i("MicroMsg.SDK.MMPluginOAuth", "request token = " + this.bI);
        if (this.bI == null) {
            Log.m1657e("MicroMsg.SDK.MMPluginOAuth", "request token failed");
            return false;
        } else if (this.bH != null) {
            this.handler.post(new C08761(this));
            return true;
        } else {
            int i;
            Log.m1655d("MicroMsg.SDK.MMPluginOAuth", "begin to show user oauth page");
            Intent intent = new Intent();
            intent.setClassName(PluginIntent.APP_PACKAGE_PATTERN, "com.tencent.mm.plugin.PluginOAuthUI");
            intent.putExtra(PluginIntent.REQUEST_TOKEN, this.bI);
            intent.putExtra(PluginIntent.PACKAGE, this.f1660q.getPackageName());
            if (this.f1660q.getPackageManager().resolveActivity(intent, AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) == null) {
                Log.m1657e("MicroMsg.SDK.MMPluginOAuth", "show oauth page failed, activity not found");
                i = 0;
            } else {
                if (!(this.f1660q instanceof Activity)) {
                    intent.setFlags(268435456);
                }
                this.f1660q.startActivity(intent);
                i = 1;
            }
            if (i == 0) {
                return false;
            }
            Receiver.register(this.bI, this);
            return true;
        }
    }
}
