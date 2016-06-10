package com.ifoer.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.util.XmppConnection;
import com.ifoer.entity.Constant;
import com.ifoer.entity.UpgradeProductResult;
import com.ifoer.expedition.BluetoothChat.DataStreamChartTabActivity;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.freememory.FreeMemory;
import com.ifoer.image.AsyncImageView;
import com.ifoer.ui.LoginActivity3;
import com.ifoer.ui.MainMenuActivity;
import com.ifoer.webservice.KeyToLogin;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.File;
import org.xmlpull.v1.XmlPullParser;

public class SimpleDialog {
    private static final boolean f1313D = false;
    private static boolean hasShow;
    private static Builder sptInputNumericDialog;
    public static Dialog unifyDialog;

    /* renamed from: com.ifoer.util.SimpleDialog.10 */
    class AnonymousClass10 implements OnClickListener {
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ Dialog val$dialog;
        private final /* synthetic */ EditText val$infos;

        AnonymousClass10(EditText editText, Context context, Dialog dialog) {
            this.val$infos = editText;
            this.val$context = context;
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            String order = "01" + ByteHexHelper.bytesToHexString(new StringBuilder(String.valueOf(this.val$infos.getText().toString())).append("\u0000").toString().getBytes());
            Constant.feedback = null;
            Constant.feedback = ByteHexHelper.hexStringToBytes(order);
            CToJava.haveData = Boolean.valueOf(true);
            Toast.makeText(this.val$context, this.val$infos.getText().toString(), 0).show();
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.11 */
    class AnonymousClass11 implements OnClickListener {
        private final /* synthetic */ Dialog val$dialog;
        private final /* synthetic */ EditText val$infos;

        AnonymousClass11(EditText editText, Dialog dialog) {
            this.val$infos = editText;
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            String order = "00" + ByteHexHelper.bytesToHexString(new StringBuilder(String.valueOf(this.val$infos.getText().toString())).append("\u0000").toString().getBytes());
            Constant.feedback = null;
            Constant.feedback = ByteHexHelper.hexStringToBytes(order);
            CToJava.haveData = Boolean.valueOf(true);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.14 */
    class AnonymousClass14 implements DialogInterface.OnClickListener {
        private final /* synthetic */ String val$content;
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ EditText val$infos;
        private final /* synthetic */ String val$title;

        AnonymousClass14(EditText editText, Context context, String str, String str2) {
            this.val$infos = editText;
            this.val$context = context;
            this.val$title = str;
            this.val$content = str2;
        }

        public void onClick(DialogInterface arg0, int arg1) {
            if (this.val$infos.getText().toString().length() > 0) {
                byte[] str = ByteHexHelper.intToFourHexBytesTwo(Integer.valueOf(this.val$infos.getText().toString()).intValue());
                Constant.feedback = null;
                Constant.feedback = str;
                CToJava.haveData = Boolean.valueOf(true);
                CToJava.inputBox = Boolean.valueOf(true);
                arg0.dismiss();
                return;
            }
            Toast.makeText(this.val$context, this.val$context.getResources().getString(C0136R.string.request_parameter_is_illegal), 0).show();
            SimpleDialog.sptInputBoxTextDiagnose(this.val$context, this.val$title, this.val$content);
            arg0.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.15 */
    class AnonymousClass15 implements DialogInterface.OnClickListener {
        private final /* synthetic */ EditText val$infos;

        AnonymousClass15(EditText editText) {
            this.val$infos = editText;
        }

        public void onClick(DialogInterface arg0, int arg1) {
            String order = "01" + ByteHexHelper.bytesToHexString(new StringBuilder(String.valueOf(this.val$infos.getText().toString())).append("\u0000").toString().getBytes());
            Constant.feedback = null;
            Constant.feedback = ByteHexHelper.hexStringToBytes(order);
            CToJava.haveData = Boolean.valueOf(true);
            arg0.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.16 */
    class AnonymousClass16 implements DialogInterface.OnClickListener {
        private final /* synthetic */ EditText val$infos;

        AnonymousClass16(EditText editText) {
            this.val$infos = editText;
        }

        public void onClick(DialogInterface dialog, int which) {
            String order = "00" + ByteHexHelper.bytesToHexString(new StringBuilder(String.valueOf(this.val$infos.getText().toString())).append("\u0000").toString().getBytes());
            Constant.feedback = null;
            Constant.feedback = ByteHexHelper.hexStringToBytes(order);
            CToJava.haveData = Boolean.valueOf(true);
            dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.17 */
    class AnonymousClass17 implements DialogInterface.OnClickListener {
        private final /* synthetic */ EditText val$infos;

        AnonymousClass17(EditText editText) {
            this.val$infos = editText;
        }

        public void onClick(DialogInterface arg0, int arg1) {
            byte[] str = new StringBuilder(String.valueOf(this.val$infos.getText().toString())).append("\u0000").toString().getBytes();
            Constant.feedback = null;
            Constant.feedback = str;
            CToJava.haveData = Boolean.valueOf(true);
            arg0.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.19 */
    class AnonymousClass19 implements DialogInterface.OnClickListener {
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ Handler val$handler;
        private final /* synthetic */ ProgressDialog val$progressDialogs;

        AnonymousClass19(Context context, Handler handler, ProgressDialog progressDialog) {
            this.val$context = context;
            this.val$handler = handler;
            this.val$progressDialogs = progressDialog;
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            new KeyToLogin(this.val$context, this.val$handler, this.val$progressDialogs).login();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.1 */
    class C07561 implements DialogInterface.OnClickListener {
        C07561() {
        }

        public void onClick(DialogInterface dialog, int which) {
            byte[] ok = new byte[]{(byte) 0};
            Constant.feedback = null;
            Constant.feedback = ok;
            CToJava.haveData = Boolean.valueOf(true);
            CToJava.inputBox = Boolean.valueOf(true);
            dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.20 */
    class AnonymousClass20 implements DialogInterface.OnClickListener {
        private final /* synthetic */ Context val$context;

        AnonymousClass20(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (SimpleDialog.getActivityName(((RunningTaskInfo) ((ActivityManager) this.val$context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName()).equalsIgnoreCase("KeyToUpgradeActivity")) {
                this.val$context.finish();
            }
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.21 */
    class AnonymousClass21 implements DialogInterface.OnClickListener {
        private final /* synthetic */ ProgressDialog val$binProgressDialogs;
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ EditText val$input;
        private final /* synthetic */ Handler val$mHandler;

        AnonymousClass21(EditText editText, Context context, ProgressDialog progressDialog, Handler handler) {
            this.val$input = editText;
            this.val$context = context;
            this.val$binProgressDialogs = progressDialog;
            this.val$mHandler = handler;
        }

        public void onClick(DialogInterface dialog, int which) {
            Log.i("useractivity", "\u786e\u5b9a\u5145\u503c");
            if (TextUtils.isEmpty(this.val$input.getText().toString()) || !Tools.isNumeric(this.val$input.getText().toString())) {
                Toast.makeText(this.val$context, this.val$context.getString(C0136R.string.ERROR_USERPASW_FORM), 0).show();
            } else {
                new UpgradeProduct(this.val$context, this.val$binProgressDialogs, this.val$mHandler, this.val$input.getText().toString()).execute(new String[0]);
            }
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.22 */
    class AnonymousClass22 implements DialogInterface.OnClickListener {
        private final /* synthetic */ ProgressDialog val$binProgressDialogs;
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ Handler val$mHandler;

        AnonymousClass22(Context context, ProgressDialog progressDialog, Handler handler) {
            this.val$context = context;
            this.val$binProgressDialogs = progressDialog;
            this.val$mHandler = handler;
        }

        public void onClick(DialogInterface dialog, int which) {
            SimpleDialog.buyDialog(this.val$context, this.val$binProgressDialogs, this.val$mHandler);
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.23 */
    class AnonymousClass23 implements DialogInterface.OnClickListener {
        private final /* synthetic */ ProgressDialog val$binProgressDialogs;
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ Handler val$mHandler;

        AnonymousClass23(Context context, ProgressDialog progressDialog, Handler handler) {
            this.val$context = context;
            this.val$binProgressDialogs = progressDialog;
            this.val$mHandler = handler;
        }

        public void onClick(DialogInterface arg0, int arg1) {
            new GetPublicSofl(this.val$context, this.val$binProgressDialogs, this.val$mHandler).execute(new String[0]);
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.24 */
    class AnonymousClass24 implements DialogInterface.OnClickListener {
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ boolean val$isKeyToUpgrade;
        private final /* synthetic */ Handler val$mHanlder;
        private final /* synthetic */ ProgressDialog val$progressDialogs;

        AnonymousClass24(boolean z, Context context, ProgressDialog progressDialog, Handler handler) {
            this.val$isKeyToUpgrade = z;
            this.val$context = context;
            this.val$progressDialogs = progressDialog;
            this.val$mHanlder = handler;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (this.val$isKeyToUpgrade) {
                new GetPublicSofl(this.val$context, this.val$progressDialogs, this.val$mHanlder).execute(new String[0]);
            }
            dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.25 */
    class AnonymousClass25 implements DialogInterface.OnClickListener {
        private final /* synthetic */ Context val$context;

        AnonymousClass25(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Intent intent = new Intent(this.val$context, LoginActivity3.class);
            intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            ((Activity) this.val$context).startActivityForResult(intent, 11);
            ((Activity) this.val$context).overridePendingTransition(0, 0);
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.26 */
    class AnonymousClass26 implements DialogInterface.OnClickListener {
        private final /* synthetic */ Context val$context;

        AnonymousClass26(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (SimpleDialog.getActivityName(((RunningTaskInfo) ((ActivityManager) this.val$context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName()).equalsIgnoreCase("KeyToUpgradeActivity")) {
                this.val$context.finish();
            }
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.27 */
    class AnonymousClass27 implements DialogInterface.OnClickListener {
        private final /* synthetic */ Context val$context;

        AnonymousClass27(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Intent intent = new Intent(this.val$context, LoginActivity3.class);
            intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            ((Activity) this.val$context).startActivityForResult(intent, 11);
            ((Activity) this.val$context).overridePendingTransition(0, 0);
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.28 */
    class AnonymousClass28 implements DialogInterface.OnClickListener {
        private final /* synthetic */ Context val$context;

        AnonymousClass28(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (SimpleDialog.getActivityName(((RunningTaskInfo) ((ActivityManager) this.val$context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName()).equalsIgnoreCase("KeyToUpgradeActivity")) {
                this.val$context.finish();
            }
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.29 */
    class AnonymousClass29 implements DialogInterface.OnClickListener {
        private final /* synthetic */ Context val$context;

        AnonymousClass29(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            FreeMemory.getInstance(this.val$context).freeMemory();
            Process.killProcess(Process.myPid());
            SimpleDialog.hasShow = false;
            System.exit(0);
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.2 */
    class C07572 implements DialogInterface.OnClickListener {
        C07572() {
        }

        public void onClick(DialogInterface dialog, int which) {
            byte[] ok = new byte[]{(byte) 0};
            Constant.feedback = null;
            Constant.feedback = ok;
            CToJava.haveData = Boolean.valueOf(true);
            dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.30 */
    class AnonymousClass30 implements DialogInterface.OnClickListener {
        private final /* synthetic */ Context val$context;

        AnonymousClass30(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (MainActivity.database != null) {
                MainActivity.database.close();
            }
            if (MainMenuActivity.database != null) {
                MainMenuActivity.database.close();
            }
            if (Constant.mChatService != null) {
                Constant.mChatService.stop();
            }
            if (MainActivity.serviceManager != null) {
                MainActivity.serviceManager.stopService();
            }
            DataCleanManager.cleanLibs(this.val$context);
            dialog.dismiss();
            MySharedPreferences.getSharedPref(this.val$context).edit().putString(MySharedPreferences.CCKey, XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(this.val$context).edit().putString(MySharedPreferences.TokenKey, XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(this.val$context).edit().putString("BluetoothDeviceAddress", XmlPullParser.NO_NAMESPACE).commit();
            try {
                XmppConnection.closeConnection();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            FreeMemory.getInstance(this.val$context).freeMemory();
            try {
                SimpleDialog.openApp(this.val$context);
            } catch (NameNotFoundException e2) {
                e2.printStackTrace();
            }
            MyApplication.getInstance().exit();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.31 */
    class AnonymousClass31 implements DialogInterface.OnClickListener {
        private final /* synthetic */ Context val$context;

        AnonymousClass31(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (MainActivity.database != null) {
                MainActivity.database.close();
            }
            if (MainMenuActivity.database != null) {
                MainMenuActivity.database.close();
            }
            if (Constant.mChatService != null) {
                Constant.mChatService.stop();
            }
            if (MainActivity.serviceManager != null) {
                MainActivity.serviceManager.stopService();
            }
            DataCleanManager.cleanLibs(this.val$context);
            dialog.dismiss();
            MySharedPreferences.getSharedPref(this.val$context).edit().putString(MySharedPreferences.CCKey, XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(this.val$context).edit().putString(MySharedPreferences.TokenKey, XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(this.val$context).edit().putString("BluetoothDeviceAddress", XmlPullParser.NO_NAMESPACE).commit();
            try {
                XmppConnection.closeConnection();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            FreeMemory.getInstance(this.val$context).freeMemory();
            try {
                SimpleDialog.openApp(this.val$context);
            } catch (NameNotFoundException e2) {
                e2.printStackTrace();
            }
            MyApplication.getInstance().exit();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.32 */
    class AnonymousClass32 implements DialogInterface.OnClickListener {
        private final /* synthetic */ Context val$context;

        AnonymousClass32(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (MainActivity.database != null) {
                MainActivity.database.close();
            }
            if (MainMenuActivity.database != null) {
                MainMenuActivity.database.close();
            }
            if (Constant.mChatService != null) {
                Constant.mChatService.stop();
            }
            if (MainActivity.serviceManager != null) {
                MainActivity.serviceManager.stopService();
            }
            DataCleanManager.cleanLibs(this.val$context);
            dialog.dismiss();
            MySharedPreferences.getSharedPref(this.val$context).edit().putString(MySharedPreferences.CCKey, XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(this.val$context).edit().putString(MySharedPreferences.TokenKey, XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(this.val$context).edit().putString("BluetoothDeviceAddress", XmlPullParser.NO_NAMESPACE).commit();
            try {
                XmppConnection.closeConnection();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            FreeMemory.getInstance(this.val$context).freeMemory();
            try {
                SimpleDialog.openApp(this.val$context);
            } catch (NameNotFoundException e2) {
                e2.printStackTrace();
            }
            MyApplication.getInstance().exit();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.3 */
    class C07583 implements DialogInterface.OnClickListener {
        C07583() {
        }

        public void onClick(DialogInterface dialog, int which) {
            byte[] cancel = new byte[]{(byte) 1};
            Constant.feedback = null;
            Constant.feedback = cancel;
            CToJava.haveData = Boolean.valueOf(true);
            dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.4 */
    class C07594 implements DialogInterface.OnClickListener {
        C07594() {
        }

        public void onClick(DialogInterface dialog, int which) {
            byte[] yes = new byte[]{(byte) 2};
            Constant.feedback = null;
            Constant.feedback = yes;
            CToJava.haveData = Boolean.valueOf(true);
            dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.5 */
    class C07605 implements DialogInterface.OnClickListener {
        C07605() {
        }

        public void onClick(DialogInterface dialog, int which) {
            byte[] no = new byte[]{(byte) 3};
            Constant.feedback = null;
            Constant.feedback = no;
            CToJava.haveData = Boolean.valueOf(true);
            dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.6 */
    class C07616 implements DialogInterface.OnClickListener {
        C07616() {
        }

        public void onClick(DialogInterface dialog, int which) {
            byte[] retry = new byte[]{(byte) 4};
            Constant.feedback = null;
            Constant.feedback = retry;
            CToJava.haveData = Boolean.valueOf(true);
            dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.7 */
    class C07627 implements DialogInterface.OnClickListener {
        C07627() {
        }

        public void onClick(DialogInterface dialog, int which) {
            byte[] cancel = new byte[]{(byte) 1};
            Constant.feedback = null;
            Constant.feedback = cancel;
            CToJava.haveData = Boolean.valueOf(true);
            dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.8 */
    class C07638 implements DialogInterface.OnClickListener {
        C07638() {
        }

        public void onClick(DialogInterface dialog, int which) {
            byte[] ok = new byte[]{(byte) 0};
            Constant.feedback = null;
            Constant.feedback = ok;
            CToJava.haveData = Boolean.valueOf(true);
            dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.SimpleDialog.9 */
    class C07649 implements DialogInterface.OnClickListener {
        C07649() {
        }

        public void onClick(DialogInterface dialog, int which) {
            byte[] ok = new byte[]{(byte) 0};
            Constant.feedback = null;
            Constant.feedback = ok;
            CToJava.haveData = Boolean.valueOf(true);
            dialog.dismiss();
            if (DataStreamChartTabActivity.instance != null && DataStreamChartTabActivity.needStop) {
                DataStreamChartTabActivity.needStop = false;
                CToJava.streamFlag = Boolean.valueOf(false);
                Constant.hasDestroy = true;
                DataStreamChartTabActivity.instance.finish();
            }
        }
    }

    static {
        hasShow = false;
    }

    public static void okDialog(Context context, String title, String message) {
        if (unifyDialog != null && unifyDialog.isShowing()) {
            unifyDialog.dismiss();
        }
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getText(C0136R.string.Ok), new C07561());
        unifyDialog = builder.show();
        unifyDialog.show();
    }

    public static void okCancelDialog(Context context, String title, String message) {
        if (unifyDialog != null && unifyDialog.isShowing()) {
            unifyDialog.dismiss();
        }
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getText(C0136R.string.Ok), new C07572());
        builder.setNegativeButton(context.getResources().getText(C0136R.string.Cancel), new C07583());
        unifyDialog = builder.show();
        unifyDialog.show();
    }

    public static void yesNoDialog(Context context, String title, String message) {
        if (unifyDialog != null && unifyDialog.isShowing()) {
            unifyDialog.dismiss();
        }
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getText(C0136R.string.Yes), new C07594());
        builder.setNegativeButton(context.getResources().getText(C0136R.string.No), new C07605());
        unifyDialog = builder.show();
        unifyDialog.show();
    }

    public static void retryCancelDialog(Context context, String title, String message) {
        if (unifyDialog != null && unifyDialog.isShowing()) {
            unifyDialog.dismiss();
        }
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getText(C0136R.string.Retry), new C07616());
        builder.setNegativeButton(context.getResources().getText(C0136R.string.Cancel), new C07627());
        unifyDialog = builder.show();
        unifyDialog.show();
    }

    public static void noButtonDialog(Context context, String title, String message) {
        if (unifyDialog != null && unifyDialog.isShowing()) {
            unifyDialog.dismiss();
        }
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getText(C0136R.string.Ok), new C07638());
        unifyDialog = builder.show();
        unifyDialog.show();
    }

    public static void okPrintDialog(Context context, String title, String message) {
        if (unifyDialog != null && unifyDialog.isShowing()) {
            unifyDialog.dismiss();
        }
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getText(C0136R.string.Ok), new C07649());
        unifyDialog = builder.show();
        unifyDialog.show();
    }

    public static void openProgressDialog(Context context, String title, String message, ProgressDialog progressDialog) {
        if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            return;
        }
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void closeProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void sptInputNumericDiagnose(Context context, String title, String content, String info, int length) {
        View views = LayoutInflater.from(context).inflate(C0136R.layout.diagnosefloat, null);
        TextView titles = (TextView) views.findViewById(C0136R.id.title);
        TextView contents = (TextView) views.findViewById(C0136R.id.context);
        Button sureBtn = (Button) views.findViewById(C0136R.id.sure);
        Button cancelBtn = (Button) views.findViewById(C0136R.id.Cancel);
        EditText infos = (EditText) views.findViewById(C0136R.id.info);
        if (info != null) {
            infos.setText(info);
        }
        titles.setText(title);
        contents.setText(content);
        sptInputNumericDialog = new Builder(context);
        sptInputNumericDialog.setView(views);
        sptInputNumericDialog.setCancelable(false);
        Dialog dialog = sptInputNumericDialog.show();
        sureBtn.setOnClickListener(new AnonymousClass10(infos, context, dialog));
        cancelBtn.setOnClickListener(new AnonymousClass11(infos, dialog));
        infos.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    s.toString();
                }
            }
        });
        dialog.show();
    }

    public static void sptInputBoxTextDiagnose(Context context, String title, String content) {
        if (unifyDialog != null && unifyDialog.isShowing()) {
            unifyDialog.dismiss();
        }
        View views = LayoutInflater.from(context).inflate(C0136R.layout.diagnoseinteger, null, false);
        TextView contents = (TextView) views.findViewById(C0136R.id.context);
        EditText infos = (EditText) views.findViewById(C0136R.id.info);
        ((TextView) views.findViewById(C0136R.id.title)).setText(title);
        contents.setText(content);
        infos.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        Builder builder = new Builder(context).setView(views);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getText(C0136R.string.sure), new AnonymousClass14(infos, context, title, content));
        unifyDialog = builder.show();
        unifyDialog.show();
    }

    public static void sptInputStringExDiagnose(Context context, String title, String content, String info) {
        if (unifyDialog != null && unifyDialog.isShowing()) {
            unifyDialog.dismiss();
        }
        View views = LayoutInflater.from(context).inflate(C0136R.layout.diagnose, null, false);
        TextView contents = (TextView) views.findViewById(C0136R.id.context);
        EditText infos = (EditText) views.findViewById(C0136R.id.info);
        ((TextView) views.findViewById(C0136R.id.title)).setText(title);
        contents.setText(content);
        if (info != null) {
            infos.setText(info);
        }
        Builder builder = new Builder(context).setView(views).setPositiveButton(context.getResources().getText(C0136R.string.sure), new AnonymousClass15(infos));
        builder.setCancelable(false);
        builder.setNegativeButton(context.getResources().getText(C0136R.string.exitBtn), new AnonymousClass16(infos));
        unifyDialog = builder.show();
        unifyDialog.show();
    }

    public static void sptInputStringDiagnose(Context context, String title, String content) {
        if (unifyDialog != null && unifyDialog.isShowing()) {
            unifyDialog.dismiss();
        }
        View views = LayoutInflater.from(context).inflate(C0136R.layout.diagnose, null, false);
        TextView contents = (TextView) views.findViewById(C0136R.id.context);
        EditText infos = (EditText) views.findViewById(C0136R.id.info);
        ((TextView) views.findViewById(C0136R.id.title)).setText(title);
        contents.setText(content);
        Builder builder = new Builder(context).setView(views).setPositiveButton(context.getResources().getText(C0136R.string.sure), new AnonymousClass17(infos));
        builder.setCancelable(false);
        unifyDialog = builder.show();
        unifyDialog.show();
    }

    public static void sptShowPictureDiagnose(Context context, String imagePath) {
        if (unifyDialog != null && unifyDialog.isShowing()) {
            unifyDialog.dismiss();
        }
        View views = LayoutInflater.from(context).inflate(C0136R.layout.diagnoseimage, null, false);
        AsyncImageView image = (AsyncImageView) views.findViewById(C0136R.id.car_img);
        TextView notFoundImage = (TextView) views.findViewById(C0136R.id.notFoundImage);
        for (String path : imagePath.split(",")) {
            if (path.contains(".BMP")) {
                imagePath = path;
                break;
            }
        }
        if (getBitmap(imagePath) != null) {
            image.setImageBitmap(getBitmap(imagePath));
            image.setVisibility(0);
            notFoundImage.setVisibility(8);
        } else {
            image.setVisibility(8);
            notFoundImage.setVisibility(0);
        }
        Builder builder = new Builder(context).setView(views).setPositiveButton(context.getResources().getText(C0136R.string.sure), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                CToJava.haveData = Boolean.valueOf(true);
                arg0.dismiss();
            }
        });
        builder.setCancelable(false);
        unifyDialog = builder.show();
        unifyDialog.show();
    }

    public static Bitmap getBitmap(String imagePath) {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return null;
        }
        File file = new File(Constant.path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String files = imagePath;
        if (new File(Constant.path + "BMP/" + files).exists()) {
            return BitmapFactory.decodeFile(Constant.path + "BMP/" + files);
        }
        return null;
    }

    public static void ToastToLogin2(Context context, Handler handler, ProgressDialog progressDialogs) {
        new Builder(context).setMessage(context.getResources().getString(C0136R.string.pleaselogin)).setPositiveButton(context.getResources().getString(C0136R.string.Ensure), new AnonymousClass19(context, handler, progressDialogs)).setNegativeButton(context.getResources().getString(C0136R.string.Cancel), new AnonymousClass20(context)).show();
    }

    public static void buyDialog(Context context, ProgressDialog binProgressDialogs, Handler mHandler) {
        EditText input = new EditText(context);
        new Builder(context).setTitle(C0136R.string.input_card_psw).setView(input).setPositiveButton(C0136R.string.enter, new AnonymousClass21(input, context, binProgressDialogs, mHandler)).setNegativeButton(C0136R.string.cancel, null).show();
    }

    public static void needBuy(Context context, ProgressDialog binProgressDialogs, Handler mHandler) {
        new Builder(context).setTitle(C0136R.string.initializeTilte).setMessage(C0136R.string.need_buy).setPositiveButton(C0136R.string.enter, new AnonymousClass22(context, binProgressDialogs, mHandler)).setNegativeButton(C0136R.string.cancel, new AnonymousClass23(context, binProgressDialogs, mHandler)).show();
    }

    public static void chargeSuccess(Context context, ProgressDialog progressDialogs, Handler mHanlder, UpgradeProductResult result, boolean isKeyToUpgrade) {
        String updatimeTv = context.getString(C0136R.string.updateDate);
        String serail = context.getString(C0136R.string.port_num1);
        String oldFreeEndTime = context.getString(C0136R.string.time_before_charge);
        new Builder(context).setTitle(C0136R.string.charge_success).setMessage(new StringBuilder(String.valueOf(updatimeTv)).append(":").append(result.getUpdateDate()).append(SpecilApiUtil.LINE_SEP).append(serail).append(result.getSerial()).append(SpecilApiUtil.LINE_SEP).append(oldFreeEndTime).append(":").append(result.getOldFreeEndTime()).append(SpecilApiUtil.LINE_SEP).append(context.getString(C0136R.string.time_after_charge)).append(":").append(result.getFreeEndTime()).toString()).setPositiveButton(C0136R.string.enter, new AnonymousClass24(isKeyToUpgrade, context, progressDialogs, mHanlder)).show();
    }

    public static void ToastToLogin(Context context) {
        new Builder(context).setMessage(context.getResources().getString(C0136R.string.pleaselogin)).setPositiveButton(context.getResources().getString(C0136R.string.Ensure), new AnonymousClass25(context)).setNegativeButton(context.getResources().getString(C0136R.string.Cancel), new AnonymousClass26(context)).show();
    }

    public static void validTokenDialog(Context context) {
        Builder builder = new Builder(context);
        builder.setTitle(context.getResources().getText(C0136R.string.initializeTilte));
        builder.setMessage(context.getResources().getText(C0136R.string.connect_server_error));
        builder.setPositiveButton(context.getResources().getString(C0136R.string.Ensure), new AnonymousClass27(context));
        builder.setNegativeButton(context.getResources().getString(C0136R.string.Cancel), new AnonymousClass28(context));
        builder.show();
    }

    public static String getActivityName(String names) {
        String[] name = names.split("\\.");
        return name[name.length - 1];
    }

    public static void checkConectior(Context context, String title, String message) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getText(C0136R.string.Ok), new AnonymousClass29(context));
        if (!hasShow) {
            if (unifyDialog != null && unifyDialog.isShowing()) {
                unifyDialog.dismiss();
            }
            unifyDialog = builder.show();
            unifyDialog.show();
            hasShow = true;
        }
    }

    public static void ExitDialog(Context context) {
        Builder builder = new Builder(context);
        builder.setMessage(context.getString(C0136R.string.restart));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getString(C0136R.string.enter), new AnonymousClass30(context));
        builder.create().show();
    }

    public static void reStart(Context context) {
        Builder builder = new Builder(context);
        builder.setMessage(context.getString(C0136R.string.update_success));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getString(C0136R.string.enter), new AnonymousClass31(context));
        builder.create().show();
    }

    public static void openApp(Context context) throws NameNotFoundException {
        PackageInfo pi = context.getPackageManager().getPackageInfo("com.ifoer.expedition.crp229", 0);
        Intent resolveIntent = new Intent("android.intent.action.MAIN", null);
        resolveIntent.addCategory("android.intent.category.LAUNCHER");
        resolveIntent.setPackage(pi.packageName);
        ResolveInfo ri = (ResolveInfo) context.getPackageManager().queryIntentActivities(resolveIntent, 0).iterator().next();
        if (ri != null) {
            String packageName1 = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName(packageName1, className));
            context.startActivity(intent);
        }
    }

    public static void FailedRestart(Context context) {
        Builder builder = new Builder(context);
        builder.setMessage(context.getString(C0136R.string.update_failed));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getString(C0136R.string.enter), new AnonymousClass32(context));
        builder.create().show();
    }
}
