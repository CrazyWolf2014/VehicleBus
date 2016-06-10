package com.ifoer.pro.expeditionphone;

import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import com.ifoer.expedition.client.Constants;
import com.ifoer.expedition.crp229.C0501R;
import com.ifoer.expeditionphone.inteface.IWelcomeActivityInterface;
import com.ifoer.mine.Contact;
import com.ifoer.ui.MainMenuActivity;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import java.util.Timer;
import java.util.TimerTask;
import org.xbill.DNS.KEYRecord.Flags;

public class WelcomeActivity extends com.ifoer.expeditionphone.WelcomeActivity implements IWelcomeActivityInterface {

    /* renamed from: com.ifoer.pro.expeditionphone.WelcomeActivity.1 */
    class C06841 extends TimerTask {
        C06841() {
        }

        public void run() {
            Intent intent = new Intent(WelcomeActivity.this, MainMenuActivity.class);
            intent.putExtra(MainMenuActivity.IfShowDialog, 2);
            intent.putExtra("fromWelcome", true);
            WelcomeActivity.this.startActivity(intent);
            WelcomeActivity.this.overridePendingTransition(0, 0);
            WelcomeActivity.this.finish();
            WelcomeActivity.this.overridePendingTransition(0, 0);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(4194304, 4194304);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        getWindow().addFlags(6815744);
        setContentView(C0501R.layout.main);
        MySharedPreferences.setString(this, "PORT_START", "96849");
        MySharedPreferences.setInt(this, "PDT_TYPE", MyHttpException.ERROR_USERNAME_FORM);
        MySharedPreferences.setString(this, "PRODUCT_TYPE", "X431Pro");
        MySharedPreferences.setString(this, "SERIA_NO_PRODUCT_TYPE", "X431 Pro");
        MySharedPreferences.setString(this, "whoCountry", Contact.RELATION_FRIEND);
        MySharedPreferences.setString(this, Constants.NOTIFICATION_TYPE, Contact.RELATION_BACKNAME);
        MySharedPreferences.setString(this, "PDY_GRID_TYPE", "NORMAL");
        MySharedPreferences.setInt(this, "WORK_COUNT", 4);
        MySharedPreferences.setInt(this, "GUIDE_COUNT", 3);
        MySharedPreferences.setBoolean(this, "LOAD_CN_CAR", true);
        MySharedPreferences.setBoolean(this, "LOAD_AS_CAR", true);
        MySharedPreferences.setBoolean(this, "LOAD_EU_CAR", true);
        MySharedPreferences.setBoolean(this, "LOAD_AM_CAR", true);
        MySharedPreferences.setString(this, "LOCAL_UPGRADE_APK", "/cnlaunch/CRP229/CRP299.apk");
        MySharedPreferences.setString(this, "SOFT_PRODUCT_TYPE", "CRP229");
        MySharedPreferences.setString(this, "SERVICE_NAME", "com.ifoer.expedition.client.NotificationServicePro");
        MySharedPreferences.setString(this, "CRP229_MANUAL_EN", "CRP229 English user manual.pdf");
        MySharedPreferences.setString(this, "CRP229_GUIDE_EN", "CRP229 Quick Start Guide.pdf");
        MySharedPreferences.setString(this, "STD", "StdCfg.ini");
        MySharedPreferences.setString(this, "MANUAL_CN", "manual_cn.pdf");
        MySharedPreferences.setString(this, "MANUAL_EN", "manual_en.pdf");
        MySharedPreferences.setString(this, "DOWNLOADINFO_EN", "nopdf.pdf");
        MySharedPreferences.setString(this, "DOWNLOADINFO_DE", "nopdf.pdf");
        MySharedPreferences.setString(this, "DOWNLOADINFO_FR", "nopdf.pdf");
        MySharedPreferences.setString(this, "DOWNLOADINFO_IT", "nopdf.pdf");
        MySharedPreferences.setString(this, "DOWNLOADINFO_JA", "nopdf.pdf");
        MySharedPreferences.setString(this, "DOWNLOADINFO_RU", "nopdf.pdf");
        MySharedPreferences.setString(this, "DOWNLOADINFO_CN", "nopdf.pdf");
        MySharedPreferences.setString(this, "USER_MANUAL_CN", "X431 Pro user's manual(2nd edition)_cn.pdf");
        MySharedPreferences.setString(this, "USER_MANUAL_EN", "X431 Pro user's manual(2nd edition)_en.pdf");
        MySharedPreferences.setString(this, "USER_MANUAL_JA", "X431 Pro user's manual(2nd edition)_ja.pdf");
        initView();
        if (!hasShortcut()) {
            addShortcut();
        }
    }

    public void initView() {
        super.initView();
    }

    private void addShortcut() {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra("android.intent.extra.shortcut.NAME", getString(C0501R.string.app_name));
        shortcut.putExtra("duplicate", false);
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setAction("android.intent.action.MAIN");
        shortcut.putExtra("android.intent.extra.shortcut.INTENT", intent);
        shortcut.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(this, C0501R.drawable.ic_launcher));
        sendBroadcast(shortcut);
    }

    private boolean hasShortcut() {
        String uriStr;
        String title = null;
        try {
            PackageManager pm = getPackageManager();
            title = pm.getApplicationLabel(pm.getApplicationInfo(getPackageName(), Flags.FLAG8)).toString();
        } catch (Exception e) {
        }
        if (VERSION.SDK_INT < 8) {
            uriStr = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        Cursor c = getContentResolver().query(Uri.parse(uriStr), null, "title=?", new String[]{title}, null);
        if (c == null || c.getCount() <= 0) {
            return false;
        }
        return true;
    }

    public void jump() {
        new Timer().schedule(new C06841(), 500);
    }

    public void onStart() {
        super.onStart();
    }
}
