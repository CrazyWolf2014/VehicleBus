package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.ui.LoginActivity3;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.view.MenuSelectPicPopupWindow;
import java.io.File;
import java.util.ArrayList;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class BaseActivity extends Activity {
    public static Context mContexts;
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_START;
    private ImageView accountImg;
    Handler baseHandler;
    Runnable bmp;
    private LinearLayout btn_quick_check;
    private LinearLayout car_maintain;
    String cc;
    private LinearLayout circleLay;
    private LinearLayout dataLay;
    public LinearLayout dayinBtn;
    private LinearLayout derma;
    private int height;
    private LinearLayout inforLay;
    private ImageView launchInfoImg;
    private Button login;
    private ImageView mCarCheck;
    private ImageView mLib;
    private ImageView mManager;
    private ImageView mUpgrade;
    public LinearLayout menuBtn;
    public View menuView;
    public MenuSelectPicPopupWindow menuWindow;
    private ImageView moreImg;
    private LinearLayout moreLay;
    private LinearLayout mySpaceLay;
    private ImageView mydataImg;
    private TextView name;
    int printResult;
    public LinearLayout searchLay;
    Dialog skinDialog;
    private LinearLayout upgrade;
    private LinearLayout userLay;
    private int width;
    private WindowManager wm;

    /* renamed from: com.ifoer.expeditionphone.BaseActivity.10 */
    class AnonymousClass10 implements OnClickListener {
        private final /* synthetic */ Context val$context;

        AnonymousClass10(Context context) {
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

    /* renamed from: com.ifoer.expeditionphone.BaseActivity.1 */
    class C05041 implements Runnable {
        C05041() {
        }

        public void run() {
            Bitmap bitmap = BaseActivity.this.screenShot();
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(BaseActivity.this.getApplicationContext());
            Bitmap printBitmap = bmpPrinter.bitmapFanzhuan(bmpPrinter.bitmapDuiBi(bmpPrinter.zoomBitmap(bitmap)));
            BaseActivity.this.printResult = bmpPrinter.printPic(printBitmap);
            bmpPrinter.resultToast(BaseActivity.this.printResult);
            BaseActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(BaseActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.BaseActivity.2 */
    class C05052 extends Handler {
        C05052() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    BaseActivity.this.dayinBtn.setClickable(true);
                    BaseActivity.this.dayinBtn.setBackgroundResource(C0136R.drawable.red_btn_bg_print);
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    BaseActivity.this.dayin();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.BaseActivity.3 */
    class C05063 implements View.OnClickListener {
        C05063() {
        }

        public void onClick(View v) {
            BaseActivity.this.showMenu(BaseActivity.mContexts);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.BaseActivity.4 */
    class C05074 implements View.OnClickListener {
        private final /* synthetic */ Context val$context;

        C05074(Context context) {
            this.val$context = context;
        }

        public void onClick(View v) {
            BaseActivity.this.initLeftBtn(this.val$context, 0);
            String name = BaseActivity.getActivityName(((RunningTaskInfo) ((ActivityManager) this.val$context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName());
            MainActivity.panel.closePanelContainer();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.BaseActivity.5 */
    class C05085 implements View.OnClickListener {
        private final /* synthetic */ Context val$context;

        C05085(Context context) {
            this.val$context = context;
        }

        public void onClick(View v) {
            String name = BaseActivity.getActivityName(((RunningTaskInfo) ((ActivityManager) this.val$context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName());
            if (name.equals("MainActivity") || name.contains("pdf") || name.contains("PDF")) {
                BaseActivity.this.initLeftBtn(this.val$context, 1);
                MainActivity.panel.removePanelContainer();
                MainActivity.panel.fillPanelContainer(new DiagnosisdatabaseActivity(MainActivity.contexts));
                MainActivity.panel.openAllPanel();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.BaseActivity.6 */
    class C05096 implements View.OnClickListener {
        private final /* synthetic */ Context val$context;

        C05096(Context context) {
            this.val$context = context;
        }

        public void onClick(View v) {
            String name = BaseActivity.getActivityName(((RunningTaskInfo) ((ActivityManager) this.val$context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName());
            if (name.equals("MainActivity") || name.contains("pdf") || name.contains("PDF")) {
                String cc = MySharedPreferences.getStringValue(this.val$context, MySharedPreferences.CCKey);
                BaseActivity.this.initLeftBtn(this.val$context, 2);
                MainActivity.panel.removePanelContainer();
                MainActivity.panel.fillPanelContainer(new SpaceDiagnosticReportLayout(MainActivity.contexts));
                MainActivity.panel.openAllPanel();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.BaseActivity.7 */
    class C05107 implements View.OnClickListener {
        private final /* synthetic */ Context val$context;

        C05107(Context context) {
            this.val$context = context;
        }

        public void onClick(View arg0) {
            String name = BaseActivity.getActivityName(((RunningTaskInfo) ((ActivityManager) this.val$context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName());
            if (name.equals("MainActivity") || name.contains("pdf") || name.contains("PDF")) {
                String mSerialNo = MySharedPreferences.getStringValue(this.val$context, MySharedPreferences.serialNoKey);
                String cc = MySharedPreferences.getStringValue(this.val$context, MySharedPreferences.CCKey);
                if (mSerialNo == null || XmlPullParser.NO_NAMESPACE.equals(mSerialNo)) {
                    this.val$context.startActivity(new Intent(this.val$context, SerialNumberActivity.class));
                    return;
                }
                if (BaseActivity.this.getMaxVersion("/mnt/sdcard/cnlaunch/" + mSerialNo + "/DIAGNOSTIC/VEHICLES/AUTOSEARCH/") != 0.0d) {
                    BaseActivity.this.initLeftBtn(this.val$context, 4);
                    MainActivity.panel.removePanelContainer();
                    MainActivity.panel.fillPanelContainer(FastDiagnosisActivity.getInstense(MainActivity.contexts));
                    MainActivity.panel.openAllPanel();
                } else if (cc == null || cc.equals(XmlPullParser.NO_NAMESPACE)) {
                    SimpleDialog.ToastToLogin(this.val$context);
                } else {
                    BaseActivity.this.initLeftBtn(this.val$context, 4);
                    MainActivity.panel.removePanelContainer();
                    MainActivity.panel.fillPanelContainer(FastDiagnosisActivity.getInstense(MainActivity.contexts));
                    MainActivity.panel.openAllPanel();
                }
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.BaseActivity.8 */
    class C05118 implements View.OnClickListener {
        private final /* synthetic */ Context val$context;

        C05118(Context context) {
            this.val$context = context;
        }

        public void onClick(View v) {
            String name = BaseActivity.getActivityName(((RunningTaskInfo) ((ActivityManager) this.val$context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName());
            if (name.equals("MainActivity") || name.contains("pdf") || name.contains("PDF")) {
                BaseActivity.this.initLeftBtn(this.val$context, 6);
                MainActivity.panel.removePanelContainer();
                MainActivity.panel.fillPanelContainer(new MoreActivity(MainActivity.contexts));
                MainActivity.panel.openAllPanel();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.BaseActivity.9 */
    class C05129 implements View.OnClickListener {
        private final /* synthetic */ Context val$context;

        C05129(Context context) {
            this.val$context = context;
        }

        public void onClick(View v) {
            String name = BaseActivity.getActivityName(((RunningTaskInfo) ((ActivityManager) this.val$context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName());
            if (name.equals("MainActivity") || name.contains("pdf") || name.contains("PDF")) {
                BaseActivity.this.initLeftBtn(this.val$context, 8);
                String cc = MySharedPreferences.getStringValue(this.val$context, MySharedPreferences.CCKey);
                String serialNo = MySharedPreferences.getStringValue(this.val$context, MySharedPreferences.serialNoKey);
                if (cc == null || cc.equals(XmlPullParser.NO_NAMESPACE)) {
                    SimpleDialog.ToastToLogin(this.val$context);
                } else if (serialNo == null || serialNo.equals(XmlPullParser.NO_NAMESPACE)) {
                    BaseActivity.this.startActivity(new Intent(this.val$context, SerialNumberActivity.class));
                } else {
                    BaseActivity.this.startActivity(MainActivity.getmKeyToUpgradeIntent());
                }
            }
        }
    }

    public BaseActivity() {
        this.width = 0;
        this.height = 0;
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.bmp = new C05041();
        this.baseHandler = new C05052();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.activity_main);
        mContexts = this;
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void initTopBtnNew(Context context) {
        this.searchLay = (LinearLayout) ((Activity) context).findViewById(C0136R.id.searchLay);
        this.name = (TextView) ((Activity) context).findViewById(C0136R.id.show_name);
        this.menuBtn = (LinearLayout) ((Activity) context).findViewById(C0136R.id.menuBtn);
        this.menuBtn.setOnClickListener(new C05063());
    }

    public Bitmap screenShot() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }

    public void dayin() {
        new Thread(this.bmp).start();
    }

    public void setVisable() {
        this.login.setVisibility(8);
    }

    protected void initMenu(Context context) {
        this.wm = (WindowManager) getSystemService("window");
        this.menuView = LayoutInflater.from(context).inflate(C0136R.layout.left, null);
        onWindowsChange();
        this.mydataImg = (ImageView) this.menuView.findViewById(C0136R.id.mydata_img);
        this.launchInfoImg = (ImageView) this.menuView.findViewById(C0136R.id.launch_info);
        this.accountImg = (ImageView) this.menuView.findViewById(C0136R.id.account);
        this.moreImg = (ImageView) this.menuView.findViewById(C0136R.id.more);
        this.mCarCheck = (ImageView) this.menuView.findViewById(C0136R.id.car_check);
        this.mLib = (ImageView) this.menuView.findViewById(C0136R.id.car_lib);
        this.mManager = (ImageView) this.menuView.findViewById(C0136R.id.manager);
        this.mUpgrade = (ImageView) this.menuView.findViewById(C0136R.id.upgradeimg);
    }

    public int dip2px(Context context, float dipValue) {
        return (int) ((dipValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    protected void initLeftBtnNew(Context context, int i) {
        initMenu(context);
        initTopBtnNew(context);
        if (MySharedPreferences.mDrawable == null) {
            ((Activity) context).getWindow().setBackgroundDrawable(MySharedPreferences.getBgId(mContexts));
        }
        this.car_maintain = (LinearLayout) this.menuView.findViewById(C0136R.id.btn_car_maintain);
        this.car_maintain.setOnClickListener(new C05074(context));
        this.dataLay = (LinearLayout) this.menuView.findViewById(C0136R.id.btn_maindb);
        this.dataLay.setOnClickListener(new C05085(context));
        this.mySpaceLay = (LinearLayout) this.menuView.findViewById(C0136R.id.btn_space);
        this.mySpaceLay.setOnClickListener(new C05096(context));
        this.inforLay = (LinearLayout) this.menuView.findViewById(C0136R.id.btn_advisory);
        this.btn_quick_check = (LinearLayout) this.menuView.findViewById(C0136R.id.btn_quick_check);
        this.btn_quick_check.setVisibility(0);
        this.btn_quick_check.setOnClickListener(new C05107(context));
        this.circleLay = (LinearLayout) this.menuView.findViewById(C0136R.id.btn_circle);
        this.userLay = (LinearLayout) this.menuView.findViewById(C0136R.id.btn_user);
        this.moreLay = (LinearLayout) this.menuView.findViewById(C0136R.id.btn_more);
        this.moreLay.setOnClickListener(new C05118(context));
        this.upgrade = (LinearLayout) this.menuView.findViewById(C0136R.id.upgrade);
        this.upgrade.setOnClickListener(new C05129(context));
        this.dataLay.setBackgroundColor(0);
        this.mySpaceLay.setBackgroundColor(0);
        this.inforLay.setBackgroundColor(0);
        this.circleLay.setBackgroundColor(0);
        this.userLay.setBackgroundColor(0);
        this.moreLay.setBackgroundColor(0);
        this.upgrade.setBackgroundColor(0);
        switch (i) {
        }
    }

    public static String getActivityName(String names) {
        String[] name = names.split("\\.");
        return name[name.length - 1];
    }

    private void ToastToLogin(Context context) {
        new Builder(context).setMessage(context.getResources().getString(C0136R.string.pleaselogin)).setPositiveButton(context.getResources().getString(C0136R.string.Ensure), new AnonymousClass10(context)).setNegativeButton(context.getResources().getString(C0136R.string.Cancel), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    protected void initLeftBtn(Context context, int i) {
        if (MySharedPreferences.mDrawable == null) {
            this.dataLay.setBackgroundColor(0);
        }
        this.mySpaceLay.setBackgroundColor(0);
        this.inforLay.setBackgroundColor(0);
        this.circleLay.setBackgroundColor(0);
        this.userLay.setBackgroundColor(0);
        this.moreLay.setBackgroundColor(0);
        this.car_maintain.setEnabled(true);
        this.dataLay.setEnabled(true);
        this.mySpaceLay.setEnabled(true);
        this.inforLay.setEnabled(true);
        this.circleLay.setEnabled(true);
        this.userLay.setEnabled(true);
        this.moreLay.setEnabled(true);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onWindowsChange();
    }

    private void onWindowsChange() {
        this.wm = (WindowManager) getSystemService("window");
        this.width = this.wm.getDefaultDisplay().getWidth();
        this.height = this.wm.getDefaultDisplay().getHeight();
    }

    private double getMaxVersion(String Path) {
        File[] files = new File(Path).listFiles();
        if (files == null) {
            return 0.0d;
        }
        ArrayList<String> paths = new ArrayList();
        for (File f : files) {
            if (f.getName().substring(0, 1).equalsIgnoreCase("V")) {
                paths.add(f.getName());
            }
        }
        if (paths.size() == 0 || paths.size() != 1) {
            return 0.0d;
        }
        return Double.parseDouble(((String) paths.get(0)).split("V")[1]);
    }

    protected void showMenu(Context context) {
        Activity activity = (Activity) context;
        this.menuWindow = new MenuSelectPicPopupWindow((Activity) context);
        Log.i("BaseActivity", "menuWindow.isShowing()" + this.menuWindow.isShowing());
        if (this.menuWindow.isShowing()) {
            this.menuWindow.dismiss();
        } else {
            this.menuWindow.showAtLocation(activity.findViewById(16908290).getRootView(), 17, 0, 0);
        }
    }
}
