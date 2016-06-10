package com.ifoer.expedition.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.OAuth;
import java.util.Properties;
import org.xmlpull.v1.XmlPullParser;

public final class ServiceManager {
    private static final String LOGTAG;
    private String apiKey;
    private String callbackActivityClassName;
    private String callbackActivityPackageName;
    private Context context;
    private Properties props;
    private SharedPreferences sharedPrefs;
    private String version;
    private String xmppHost;
    private String xmppPort;

    /* renamed from: com.ifoer.expedition.client.ServiceManager.1 */
    class C04991 implements Runnable {
        C04991() {
        }

        public void run() {
            ServiceManager.this.context.startService(NotificationService.getIntent());
        }
    }

    static {
        LOGTAG = LogUtil.makeLogTag(ServiceManager.class);
    }

    public ServiceManager(Context context) {
        this.version = "0.5.0";
        this.context = context;
        if (context instanceof Activity) {
            Log.i(LOGTAG, "Callback Activity...");
            Activity callbackActivity = (Activity) context;
            this.callbackActivityPackageName = callbackActivity.getPackageName();
            this.callbackActivityClassName = callbackActivity.getClass().getName();
        }
        this.props = loadProperties();
        this.apiKey = this.props.getProperty(OAuth.API_KEY, XmlPullParser.NO_NAMESPACE);
        this.xmppHost = this.props.getProperty("xmppHost", "127.0.0.1");
        this.xmppPort = this.props.getProperty("xmppPort", "5222");
        Log.i(LOGTAG, "apiKey=" + this.apiKey);
        Log.i(LOGTAG, "xmppHost=" + this.xmppHost);
        Log.i(LOGTAG, "xmppPort=" + this.xmppPort);
        this.sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, 0);
        Editor editor = this.sharedPrefs.edit();
        editor.putString(Constants.API_KEY, this.apiKey);
        editor.putString(Constants.VERSION, this.version);
        editor.putString(Constants.XMPP_HOST, this.xmppHost);
        editor.putInt(Constants.XMPP_PORT, Integer.parseInt(this.xmppPort));
        editor.putString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME, this.callbackActivityPackageName);
        editor.putString(Constants.CALLBACK_ACTIVITY_CLASS_NAME, this.callbackActivityClassName);
        editor.commit();
    }

    public void startService() {
        new Thread(new C04991()).start();
    }

    public void stopService() {
        this.context.stopService(NotificationService.getIntent());
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try {
            props.load(this.context.getResources().openRawResource(this.context.getResources().getIdentifier("androidpn", "raw", this.context.getPackageName())));
        } catch (Exception e) {
            Log.e(LOGTAG, "Could not find the properties file.", e);
        }
        return props;
    }

    public void setNotificationIcon(int iconId) {
        Editor editor = this.sharedPrefs.edit();
        editor.putInt(Constants.NOTIFICATION_ICON, iconId);
        editor.commit();
    }

    public static void viewNotificationSettings(Context context) {
        context.startActivity(new Intent().setClass(context, NotificationSettingsActivity.class));
    }
}
