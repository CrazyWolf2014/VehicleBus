package com.ifoer.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.cnmobi.im.util.XmppConnection;
import com.ifoer.adapter.MenuAdpter;
import com.ifoer.adapter.MenuAdpter.MenuItem;
import com.ifoer.db.DBDao;
import com.ifoer.db.UpgradeRunnable;
import com.ifoer.entity.Constant;
import com.ifoer.entity.InterfaceDao;
import com.ifoer.entity.Menu;
import com.ifoer.event.OnScrollCompleteListener;
import com.ifoer.event.ScrollEvent;
import com.ifoer.expeditionphone.BaseActivity;
import com.ifoer.expeditionphone.FindPwdActivity;
import com.ifoer.expeditionphone.KeyToUpgradeActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.vin.VinPhotoActivity;
import com.ifoer.mine.Contact;
import com.ifoer.service.GetConfigTool;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.Common;
import com.ifoer.util.DataCleanManager;
import com.ifoer.util.Files;
import com.ifoer.util.IPInfoUtil;
import com.ifoer.util.MyApkUpdate;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.view.SlidePointView2;
import com.ifoer.view.WorkSpace2;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class MainMenuActivity extends BaseActivity implements OnClickListener, OnItemClickListener, OnScrollCompleteListener {
    public static final int COPY_PDF = 6;
    public static final String IfShowDialog = "IfShowDialog";
    public static final int REFRESH_UI = 7;
    public static final int REQUEST_USER_LOGIN_OUT = 25;
    public static String country;
    public static String countryName;
    public static SQLiteDatabase database;
    public static Intent mKeyToUpgradeIntent;
    public static Intent mMainActivityIntent;
    public static int mSlidePoint;
    private int[] String1;
    private int[] String2;
    private MenuAdpter adpter1;
    private MenuAdpter adpter2;
    private List<HashMap<String, Object>> americaLists;
    private List<HashMap<String, Object>> asiaList;
    private Button btn_account;
    private Button btn_db;
    private Button btn_diag;
    private Button btn_fast;
    private Button btn_golo;
    private Button btn_more;
    private Button btn_mydata;
    private Button btn_update;
    public List<HashMap<String, Object>> carList;
    private String carTypeSearch;
    private String cc;
    private List<HashMap<String, Object>> chinaList;
    private List<HashMap<String, Object>> euroList;
    private GridView gridView1;
    private GridView gridView2;
    private Boolean hasGetAmerican;
    private Boolean hasGetAsia;
    private Boolean hasGetEuro;
    private Boolean hasGetSpecail;
    private int[] icon1;
    private int[] icon2;
    private int initSlidingFlag;
    String lanName;
    public String language;
    private long lastTime;
    private List<Menu> list1;
    private List<Menu> list2;
    private List<String> listPdf;
    private ProgressDialog loadProgress;
    private Timer loadTimer;
    final Handler mHandler;
    private SlidePointView2 mSlidePoinView;
    private Context mcontext;
    public IntentFilter myIntentFilter;
    public ImageView page1;
    public ImageView page2;
    private ProgressDialog progressDialog;
    public mBroadcastReceiver receiver;
    public AlertDialog reputPopupWindow;
    private Button reput_btn;
    private CheckBox reput_check;
    private ImageView reput_close;
    private RelativeLayout reput_lay;
    private RelativeLayout rlPop;
    private int showDialogFlag;
    private List<HashMap<String, Object>> specialList;
    public AlertDialog toastPopupWindow;
    private Button toast_btn;
    private CheckBox toast_check;
    private RelativeLayout toast_lay;
    private String token;
    private WorkSpace2 workSpace;

    /* renamed from: com.ifoer.ui.MainMenuActivity.1 */
    class C07021 extends Handler {
        C07021() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MainMenuActivity.COPY_PDF /*6*/:
                    MainMenuActivity.this.listPdf = new ArrayList();
                    MainMenuActivity.this.listPdf.add(MySharedPreferences.getStringValue(MainMenuActivity.this.mcontext, "CRP229_MANUAL_EN"));
                    MainMenuActivity.this.listPdf.add(MySharedPreferences.getStringValue(MainMenuActivity.this.mcontext, "CRP229_GUIDE_EN"));
                    MainMenuActivity.this.listPdf.add(MySharedPreferences.getStringValue(MainMenuActivity.this.mcontext, "STD"));
                    File file = new File(Constant.assestsPath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(Constant.iniPath);
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }
                    int i = 0;
                    while (i < MainMenuActivity.this.listPdf.size()) {
                        try {
                            MainMenuActivity.forJava(((String) MainMenuActivity.this.listPdf.get(i)).toString());
                            i++;
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            return;
                        }
                    }
                case MainMenuActivity.REFRESH_UI /*7*/:
                    MainMenuActivity.this.refreshUi();
                case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                    MainMenuActivity.this.progressDialog.dismiss();
                    Toast.makeText(MainMenuActivity.this.mcontext, C0136R.string.timeout, 1).show();
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    if (MainMenuActivity.this.progressDialog != null && MainMenuActivity.this.progressDialog.isShowing()) {
                        MainMenuActivity.this.progressDialog.dismiss();
                    }
                    Toast.makeText(MainMenuActivity.this.mcontext, C0136R.string.get_data_fail, 1).show();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.ui.MainMenuActivity.2 */
    class C07032 implements DialogInterface.OnClickListener {
        C07032() {
        }

        public void onClick(DialogInterface dialog, int which) {
            MainMenuActivity.database.close();
            if (Constant.mChatService != null) {
                Constant.mChatService.stop();
                if (MainActivity.serialPort != null) {
                    MainActivity.serialPort.close();
                }
            }
            if (MainActivity.serviceManager != null) {
                MainActivity.serviceManager.stopService();
            }
            Intent bService = new Intent();
            bService.setAction("com.ifoer.service.BatteryService");
            MainMenuActivity.this.stopService(bService);
            DataCleanManager.cleanLibs(MainMenuActivity.this.mcontext);
            dialog.dismiss();
            MySharedPreferences.getSharedPref(MainMenuActivity.this.mcontext).edit().putString(MySharedPreferences.CCKey, XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(MainMenuActivity.this.mcontext).edit().putString(MySharedPreferences.TokenKey, XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(MainMenuActivity.this.mcontext).edit().putString("BluetoothDeviceAddress", XmlPullParser.NO_NAMESPACE).commit();
            try {
                XmppConnection.closeConnection();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            MyApplication.getInstance().exit();
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }

    /* renamed from: com.ifoer.ui.MainMenuActivity.3 */
    class C07043 implements DialogInterface.OnClickListener {
        C07043() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.ui.MainMenuActivity.4 */
    class C07054 implements DialogInterface.OnClickListener {
        C07054() {
        }

        public void onClick(DialogInterface dialog, int which) {
            MainMenuActivity.database.close();
            if (Constant.mChatService != null) {
                Constant.mChatService.stop();
            }
            if (MainActivity.serviceManager != null) {
                MainActivity.serviceManager.stopService();
            }
            if (MainActivity.dataDir != null) {
                MainActivity.deleteFiles(MainActivity.dataDir + "/libs/cnlaunch/");
            }
            dialog.dismiss();
            MySharedPreferences.getSharedPref(MainMenuActivity.this.mcontext).edit().putString(MySharedPreferences.CCKey, XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(MainMenuActivity.this.mcontext).edit().putString(MySharedPreferences.TokenKey, XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(MainMenuActivity.this.mcontext).edit().putString("BluetoothDeviceAddress", XmlPullParser.NO_NAMESPACE).commit();
            try {
                XmppConnection.closeConnection();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            MyApplication.getInstance().exit();
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }

    /* renamed from: com.ifoer.ui.MainMenuActivity.5 */
    class C07065 implements OnKeyListener {
        C07065() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != 4) {
                return false;
            }
            MySharedPreferences.share.getBoolean(MySharedPreferences.IfShowNoticKey, true);
            MySharedPreferences.setString(MainMenuActivity.this.mcontext, "showNoticKey", Contact.RELATION_FRIEND);
            MainMenuActivity.this.toastPopupWindow.dismiss();
            return true;
        }
    }

    /* renamed from: com.ifoer.ui.MainMenuActivity.6 */
    class C07076 implements OnKeyListener {
        C07076() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != 4) {
                return false;
            }
            MySharedPreferences.setBoolean(MainMenuActivity.this.mcontext, MySharedPreferences.IfShowResponsibilityKey, true);
            MySharedPreferences.setString(MainMenuActivity.this.mcontext, "showResponsibility", Contact.RELATION_FRIEND);
            MainMenuActivity.this.reputPopupWindow.dismiss();
            return true;
        }
    }

    class GetAmerica extends Thread {
        GetAmerica() {
        }

        public void run() {
            boolean isopen;
            String serialNo = MySharedPreferences.getStringValue(MainMenuActivity.this.mcontext, MySharedPreferences.serialNoKey);
            String ifshow = MySharedPreferences.getStringValue(MainMenuActivity.this.mcontext, MySharedPreferences.IfShow);
            if (ifshow == null || !ifshow.equals(Contact.RELATION_FRIEND)) {
                isopen = false;
            } else {
                isopen = true;
            }
            MainMenuActivity.this.americaLists = MainMenuActivity.this.GetAmericaLists(serialNo, Boolean.valueOf(isopen), MainMenuActivity.this.carTypeSearch);
            MainMenuActivity.this.mHandler.obtainMessage(13).sendToTarget();
        }
    }

    class GetAsia extends Thread {
        GetAsia() {
        }

        public void run() {
            boolean isopen;
            String serialNo = MySharedPreferences.getStringValue(MainMenuActivity.this.mcontext, MySharedPreferences.serialNoKey);
            String ifshow = MySharedPreferences.getStringValue(MainMenuActivity.this.mcontext, MySharedPreferences.IfShow);
            if (ifshow == null || !ifshow.equals(Contact.RELATION_FRIEND)) {
                isopen = false;
            } else {
                isopen = true;
            }
            MainMenuActivity.this.asiaList = MainMenuActivity.this.GetAsiaList(serialNo, Boolean.valueOf(isopen), MainMenuActivity.this.carTypeSearch);
            MainMenuActivity.this.mHandler.obtainMessage(11).sendToTarget();
        }
    }

    class GetChina extends Thread {
        GetChina() {
        }

        public void run() {
            boolean isopen;
            String serialNo = MySharedPreferences.getStringValue(MainMenuActivity.this.mcontext, MySharedPreferences.serialNoKey);
            String ifshow = MySharedPreferences.getStringValue(MainMenuActivity.this.mcontext, MySharedPreferences.IfShow);
            if (ifshow == null || !ifshow.equals(Contact.RELATION_FRIEND)) {
                isopen = false;
            } else {
                isopen = true;
            }
            MainMenuActivity.this.chinaList = MainMenuActivity.this.GetChinaList(serialNo, Boolean.valueOf(isopen), MainMenuActivity.this.carTypeSearch);
            MainMenuActivity.this.mHandler.obtainMessage(10).sendToTarget();
        }
    }

    class GetConfigAsyn extends AsyncTask<String, String, String> {
        GetConfigAsyn() {
        }

        protected String doInBackground(String... params) {
            IPInfoUtil ipUitl = new IPInfoUtil();
            if (Common.isNetworkAvailable(MainMenuActivity.mContexts)) {
                String nowArea = ipUitl.ipToArea(ipUitl.getNetIp());
                if (InterfaceDao.getInstance().getConfigArea(GetConfigTool.CONFIG_NAME).equals(nowArea)) {
                    InterfaceDao.getInstance();
                    InterfaceDao.getConfigFile(false);
                } else {
                    GetConfigTool.tempArea = nowArea;
                    InterfaceDao.getInstance();
                    InterfaceDao.getConfigFile(true);
                }
            }
            return null;
        }
    }

    class GetEuro extends Thread {
        GetEuro() {
        }

        public void run() {
            boolean isopen;
            String serialNo = MySharedPreferences.getStringValue(MainMenuActivity.this.mcontext, MySharedPreferences.serialNoKey);
            String ifshow = MySharedPreferences.getStringValue(MainMenuActivity.this.mcontext, MySharedPreferences.IfShow);
            if (ifshow == null || !ifshow.equals(Contact.RELATION_FRIEND)) {
                isopen = false;
            } else {
                isopen = true;
            }
            MainMenuActivity.this.euroList = MainMenuActivity.this.GetEuroList(serialNo, Boolean.valueOf(isopen), MainMenuActivity.this.carTypeSearch);
            MainMenuActivity.this.mHandler.obtainMessage(12).sendToTarget();
        }
    }

    class GetSpecial extends Thread {
        GetSpecial() {
        }

        public void run() {
            boolean isopen;
            String serialNo = MySharedPreferences.getStringValue(MainMenuActivity.this, MySharedPreferences.serialNoKey);
            String ifshow = MySharedPreferences.getStringValue(MainMenuActivity.this, MySharedPreferences.IfShow);
            if (ifshow == null || !ifshow.equals(Contact.RELATION_FRIEND)) {
                isopen = false;
            } else {
                isopen = true;
            }
            MainMenuActivity.this.specialList = MainMenuActivity.this.GetSpecialList(serialNo, Boolean.valueOf(isopen), MainMenuActivity.this.carTypeSearch);
            MainMenuActivity.this.mHandler.obtainMessage(18).sendToTarget();
        }
    }

    public class LoadTimeTask extends TimerTask {
        public void run() {
            if (MainMenuActivity.this.hasGetAsia.booleanValue() && MainMenuActivity.this.hasGetEuro.booleanValue() && MainMenuActivity.this.hasGetAmerican.booleanValue()) {
                MainMenuActivity.this.loadProgress.dismiss();
                MainMenuActivity.this.loadTimer.cancel();
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        @SuppressLint({"ShowToast"})
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - MainMenuActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                MainMenuActivity.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    public MainMenuActivity() {
        this.carList = new ArrayList();
        this.chinaList = new ArrayList();
        this.asiaList = new ArrayList();
        this.euroList = new ArrayList();
        this.americaLists = new ArrayList();
        this.specialList = new ArrayList();
        this.carTypeSearch = XmlPullParser.NO_NAMESPACE;
        this.initSlidingFlag = 0;
        this.showDialogFlag = 0;
        this.hasGetAsia = Boolean.valueOf(false);
        this.hasGetEuro = Boolean.valueOf(false);
        this.hasGetAmerican = Boolean.valueOf(false);
        this.loadTimer = new Timer();
        this.hasGetSpecail = Boolean.valueOf(false);
        int[] iArr = new int[COPY_PDF];
        iArr[0] = C0136R.drawable.grid_01_selector;
        iArr[1] = C0136R.drawable.grid_02_selector;
        iArr[2] = C0136R.drawable.grid_04_selector;
        iArr[3] = C0136R.drawable.grid_06_selecor;
        iArr[4] = C0136R.drawable.grid_07_selector;
        iArr[5] = C0136R.drawable.grid_09_selector;
        this.icon1 = iArr;
        this.icon2 = new int[]{C0136R.drawable.grid_05_selector};
        iArr = new int[COPY_PDF];
        iArr[0] = C0136R.string.left_btn_zhenduan;
        iArr[1] = C0136R.string.special;
        iArr[2] = C0136R.string.left_btn_kongjian;
        iArr[3] = C0136R.string.a_key_to_upgrade;
        iArr[4] = C0136R.string.left_btn_guanli;
        iArr[5] = C0136R.string.left_btn_gduo;
        this.String1 = iArr;
        this.String2 = new int[]{C0136R.string.golo};
        this.list1 = new ArrayList();
        this.list2 = new ArrayList();
        this.mHandler = new C07021();
    }

    static {
        mSlidePoint = 1;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetConfigAsyn().execute(new String[0]);
    }

    protected void createView() {
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.menu2);
        if (BaseActivity.mContexts == null) {
            BaseActivity.mContexts = this;
        }
        registerBoradcastReceiver();
        MyApplication.getInstance().addActivity(this);
        this.mcontext = this;
        createKeyUpGrade();
        createMainActivity();
        Locale locale = Locale.getDefault();
        country = locale.getCountry();
        this.language = locale.getLanguage();
        this.lanName = AndroidToLan.toLan(country);
        Constant.language = locale.toString();
        database = DBDao.getInstance(this).getConnection();
        MainActivity.database = database;
        mainview2();
        new Thread(new UpgradeRunnable(this.mcontext)).start();
        String ln = Files.getLanguage();
        if (ln.equalsIgnoreCase(Constants.DEFAULT_LANGUAGE) || ln.equalsIgnoreCase("FR") || ln.equalsIgnoreCase("ES") || ln.equalsIgnoreCase("IT") || ln.equalsIgnoreCase("DE") || ln.equalsIgnoreCase("RU")) {
            this.loadProgress = new ProgressDialog(this);
            this.loadProgress.setMessage(getString(C0136R.string.shopping_wait));
            this.loadProgress.setCancelable(false);
            this.loadProgress.show();
            refreshUi();
            this.mHandler.sendMessage(this.mHandler.obtainMessage(COPY_PDF));
            Intent intent = getIntent();
            this.initSlidingFlag = intent.getIntExtra("initSlidingFlag", 0);
            this.showDialogFlag = intent.getIntExtra(IfShowDialog, 0);
            this.loadTimer.schedule(new LoadTimeTask(), 1000, 1000);
            if (Common.isNetworkAvailable(this.mcontext)) {
                MyApkUpdate.getMyApkUpdate(this.mcontext, this.mHandler, false).checkUpdateAsync();
                return;
            }
            return;
        }
        ExitDialog2();
    }

    private void mainview2() {
        int i;
        this.mSlidePoinView = new SlidePointView2(this);
        ((LinearLayout) findViewById(C0136R.id.llayout)).addView(this.mSlidePoinView);
        this.mSlidePoinView.postInvalidate();
        this.page1 = (ImageView) findViewById(C0136R.id.menu_page1);
        this.page2 = (ImageView) findViewById(C0136R.id.menu_page2);
        for (i = 0; i < this.icon1.length; i++) {
            Menu menu = new Menu();
            menu.setIcon(this.icon1[i]);
            menu.setName(this.String1[i]);
            this.list1.add(menu);
        }
        for (i = 0; i < this.icon2.length; i++) {
            menu = new Menu();
            menu.setIcon(this.icon2[i]);
            menu.setName(this.String2[i]);
            this.list2.add(menu);
        }
        this.gridView1 = new GridView(this);
        this.gridView2 = new GridView(this);
        this.gridView1.setHorizontalSpacing(15);
        this.gridView1.setVerticalSpacing(15);
        this.gridView2.setHorizontalSpacing(15);
        this.gridView2.setVerticalSpacing(15);
        this.gridView1.setVerticalScrollBarEnabled(false);
        this.gridView2.setVerticalScrollBarEnabled(false);
        this.gridView1.setSelector(C0136R.drawable.mgrid_focused);
        this.gridView2.setSelector(C0136R.drawable.mgrid_focused);
        LayoutParams linearParams = new LayoutParams(-2, -2);
        linearParams.width = C0136R.dimen.itemSize;
        linearParams.height = C0136R.dimen.itemSize;
        this.gridView1.setLayoutParams(linearParams);
        this.gridView2.setLayoutParams(linearParams);
        this.gridView1.setNumColumns(3);
        this.gridView2.setNumColumns(3);
        this.adpter1 = new MenuAdpter(this.mcontext, this.list1);
        this.adpter2 = new MenuAdpter(this.mcontext, this.list2);
        this.gridView1.setAdapter(this.adpter1);
        this.gridView2.setAdapter(this.adpter2);
        this.workSpace = (WorkSpace2) findViewById(C0136R.id.menu_workspace);
        this.workSpace.setOnScrollCompleteLinstenner(this);
        this.workSpace.addView(this.gridView1);
        this.workSpace.addView(this.gridView2);
        this.gridView1.setOnItemClickListener(this);
        this.gridView2.setOnItemClickListener(this);
    }

    public void sendMsg() {
        sendBroadcast(new Intent("Show_names"));
    }

    protected void openApp(Context context) throws NameNotFoundException {
        PackageInfo pi = context.getPackageManager().getPackageInfo("com.cnlaunch.golo3", 0);
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

    public static Intent getmKeyToUpgradeIntent() {
        return mKeyToUpgradeIntent;
    }

    public void setmKeyToUpgradeIntent(Intent mKeyToUpgradeActivity) {
        mKeyToUpgradeIntent = mKeyToUpgradeActivity;
    }

    public void createKeyUpGrade() {
        setmKeyToUpgradeIntent(new Intent(this, KeyToUpgradeActivity.class));
    }

    public void createMainActivity() {
        mMainActivityIntent = new Intent(this, MainActivity.class);
    }

    public void onClick(View v) {
        Intent intent;
        if (v.getId() == C0136R.id.btn_diag) {
            if (this.hasGetAsia.booleanValue() && this.hasGetEuro.booleanValue() && this.hasGetAmerican.booleanValue()) {
                intent = mMainActivityIntent;
                intent.putExtra(IfShowDialog, 2);
                intent.putExtra("loadSpecail", false);
                startActivity(intent);
            }
        } else if (v.getId() == C0136R.id.btn_quick) {
            if (this.hasGetSpecail.booleanValue()) {
                intent = mMainActivityIntent;
                intent.putExtra(IfShowDialog, 2);
                intent.putExtra("loadSpecail", true);
                startActivity(intent);
            }
        } else if (v.getId() == C0136R.id.btn_data) {
            intent = new Intent();
            intent.setClass(this.mcontext, DiagnosisdatabaseActivity.class);
            this.mcontext.startActivity(intent);
        } else if (v.getId() == C0136R.id.btn_mydata) {
            intent = new Intent();
            intent.setClass(this.mcontext, SpaceDiagnosticReportActivity.class);
            this.mcontext.startActivity(intent);
        } else if (v.getId() == C0136R.id.btn_golo) {
            Toast.makeText(this.mcontext, this.mcontext.getString(C0136R.string.coming), 0).show();
        } else if (v.getId() == C0136R.id.btn_update) {
            intent = new Intent();
            intent.setClass(this.mcontext, KeyToUpgradeActivity.class);
            this.mcontext.startActivity(intent);
        } else if (v.getId() == C0136R.id.btn_count) {
            intent = new Intent();
            intent.setClass(this.mcontext, UserActivity.class);
            this.mcontext.startActivity(intent);
        } else if (v.getId() == C0136R.id.btn_more) {
            intent = new Intent();
            intent.setClass(this.mcontext, MoreActivity.class);
            this.mcontext.startActivity(intent);
        }
    }

    private void refreshUi() {
        new GetAsia().start();
        new GetEuro().start();
        new GetAmerica().start();
        new GetSpecial().start();
    }

    public synchronized List<HashMap<String, Object>> GetSpecialList(String serialNo, Boolean isopen, String carTypeSearch) {
        String quaryLanName;
        List<HashMap<String, Object>> carAdds = new ArrayList();
        List<HashMap<String, Object>> listImage = new ArrayList();
        if (this.lanName == "HK" || this.lanName == "TW") {
            quaryLanName = Constants.DEFAULT_LANGUAGE;
        } else {
            quaryLanName = this.lanName;
        }
        this.specialList = createDBDao().query(Contact.RELATION_AGREE, serialNo, quaryLanName, database);
        listImage.clear();
        int i;
        if (isopen.booleanValue()) {
            for (i = 0; i < this.specialList.size(); i++) {
                if (Integer.parseInt(((HashMap) this.specialList.get(i)).get("flag").toString()) > 0) {
                    listImage.add((HashMap) this.specialList.get(i));
                }
            }
            this.specialList.clear();
            this.specialList = listImage;
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (!(carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE))) {
                for (i = 0; i < this.specialList.size(); i++) {
                    if (getString(((Integer) ((HashMap) this.specialList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.specialList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.specialList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.specialList.get(i));
                    }
                }
                this.specialList.clear();
                this.specialList = carAdds;
            }
        } else if (carTypeSearch != null) {
            if (!carTypeSearch.equals(XmlPullParser.NO_NAMESPACE)) {
                for (i = 0; i < this.specialList.size(); i++) {
                    if (getString(((Integer) ((HashMap) this.specialList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.specialList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.specialList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.specialList.get(i));
                    }
                }
                this.specialList.clear();
                this.specialList = carAdds;
            }
        }
        this.hasGetSpecail = Boolean.valueOf(true);
        return this.specialList;
    }

    public synchronized List<HashMap<String, Object>> GetChinaList(String serialNo, Boolean isopen, String carTypeSearch) {
        List<HashMap<String, Object>> list;
        String quaryLanName;
        List<HashMap<String, Object>> carAdds = new ArrayList();
        List<HashMap<String, Object>> listImage = new ArrayList();
        if (this.lanName == "HK" || this.lanName == "TW") {
            quaryLanName = Constants.DEFAULT_LANGUAGE;
        } else {
            quaryLanName = this.lanName;
        }
        this.chinaList = createDBDao().query(Contact.RELATION_BACKNAME, serialNo, quaryLanName, database);
        listImage.clear();
        int i;
        if (isopen.booleanValue()) {
            for (i = 0; i < this.chinaList.size(); i++) {
                if (Integer.parseInt(((HashMap) this.chinaList.get(i)).get("flag").toString()) > 0) {
                    listImage.add((HashMap) this.chinaList.get(i));
                }
            }
            this.chinaList.clear();
            this.chinaList = listImage;
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (!(carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE))) {
                for (i = 0; i < this.chinaList.size(); i++) {
                    if (getString(((Integer) ((HashMap) this.chinaList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.chinaList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.chinaList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.chinaList.get(i));
                    }
                }
                this.chinaList.clear();
                this.chinaList = carAdds;
            }
            list = this.chinaList;
        } else {
            if (carTypeSearch != null) {
                if (!carTypeSearch.equals(XmlPullParser.NO_NAMESPACE)) {
                    for (i = 0; i < this.chinaList.size(); i++) {
                        if (getString(((Integer) ((HashMap) this.chinaList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                            carAdds.add((HashMap) this.chinaList.get(i));
                        } else if (getString(((Integer) ((HashMap) this.chinaList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                            carAdds.add((HashMap) this.chinaList.get(i));
                        }
                    }
                    this.chinaList.clear();
                    this.chinaList = carAdds;
                    list = this.chinaList;
                }
            }
            list = this.chinaList;
        }
        return list;
    }

    public synchronized List<HashMap<String, Object>> GetAsiaList(String serialNo, Boolean isopen, String carTypeSearch) {
        List<HashMap<String, Object>> carAdds = new ArrayList();
        List<HashMap<String, Object>> listImage = new ArrayList();
        this.asiaList.clear();
        this.asiaList = createDBDao().query(Contact.RELATION_NOAGREE, serialNo, this.lanName, database);
        int i;
        if (isopen.booleanValue()) {
            for (i = 0; i < this.asiaList.size(); i++) {
                if (Integer.parseInt(((HashMap) this.asiaList.get(i)).get("flag").toString()) > 0) {
                    listImage.add((HashMap) this.asiaList.get(i));
                }
            }
            this.asiaList.clear();
            this.asiaList = listImage;
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (!(carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE))) {
                for (i = 0; i < this.asiaList.size(); i++) {
                    if (getString(((Integer) ((HashMap) this.asiaList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.asiaList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.asiaList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.asiaList.get(i));
                    }
                }
                this.asiaList.clear();
                this.asiaList = carAdds;
            }
        } else {
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (!(carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE))) {
                for (i = 0; i < this.asiaList.size(); i++) {
                    if (getString(((Integer) ((HashMap) this.asiaList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.asiaList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.asiaList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.asiaList.get(i));
                    }
                }
                this.asiaList.clear();
                this.asiaList = carAdds;
            }
        }
        this.hasGetAsia = Boolean.valueOf(true);
        return this.asiaList;
    }

    public synchronized List<HashMap<String, Object>> GetEuroList(String serialNo, Boolean isopen, String carTypeSearch) {
        List<HashMap<String, Object>> carAdds = new ArrayList();
        List<HashMap<String, Object>> listImage = new ArrayList();
        this.euroList.clear();
        this.euroList = createDBDao().query(Contact.RELATION_FRIEND, serialNo, this.lanName, database);
        int i;
        if (isopen.booleanValue()) {
            for (i = 0; i < this.euroList.size(); i++) {
                if (Integer.parseInt(((HashMap) this.euroList.get(i)).get("flag").toString()) > 0) {
                    listImage.add((HashMap) this.euroList.get(i));
                }
            }
            this.euroList.clear();
            this.euroList = listImage;
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (!(carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE))) {
                for (i = 0; i < this.euroList.size(); i++) {
                    if (getString(((Integer) ((HashMap) this.euroList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.euroList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.euroList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.euroList.get(i));
                    }
                }
                this.euroList.clear();
                this.euroList = carAdds;
            }
        } else {
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (!(carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE))) {
                for (i = 0; i < this.euroList.size(); i++) {
                    if (getString(((Integer) ((HashMap) this.euroList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.euroList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.euroList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.euroList.get(i));
                    }
                }
                this.euroList.clear();
                this.euroList = carAdds;
            }
        }
        this.hasGetEuro = Boolean.valueOf(true);
        return this.euroList;
    }

    public synchronized List<HashMap<String, Object>> GetAmericaLists(String serialNo, Boolean isopen, String carTypeSearch) {
        List<HashMap<String, Object>> carAdds = new ArrayList();
        List<HashMap<String, Object>> listImage = new ArrayList();
        this.americaLists.clear();
        this.americaLists = createDBDao().query(Contact.RELATION_NODONE, serialNo, this.lanName, database);
        int i;
        if (isopen.booleanValue()) {
            for (i = 0; i < this.americaLists.size(); i++) {
                if (Integer.parseInt(((HashMap) this.americaLists.get(i)).get("flag").toString()) > 0) {
                    listImage.add((HashMap) this.americaLists.get(i));
                }
            }
            this.americaLists.clear();
            this.americaLists = listImage;
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (!(carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE))) {
                for (i = 0; i < this.americaLists.size(); i++) {
                    if (getString(((Integer) ((HashMap) this.americaLists.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.americaLists.get(i));
                    } else if (getString(((Integer) ((HashMap) this.americaLists.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.americaLists.get(i));
                    }
                }
                this.americaLists.clear();
                this.americaLists = carAdds;
            }
        } else if (carTypeSearch != null) {
            if (!carTypeSearch.equals(XmlPullParser.NO_NAMESPACE)) {
                for (i = 0; i < this.americaLists.size(); i++) {
                    if (getString(((Integer) ((HashMap) this.americaLists.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.americaLists.get(i));
                    } else if (getString(((Integer) ((HashMap) this.americaLists.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.americaLists.get(i));
                    }
                }
                this.americaLists.clear();
                this.americaLists = carAdds;
            }
        }
        this.hasGetAmerican = Boolean.valueOf(true);
        return this.americaLists;
    }

    public DBDao createDBDao() {
        return DBDao.getInstance(this);
    }

    public static void forJava(String name) {
        try {
            OutputStream fosto;
            InputStream fosfrom = mContexts.getAssets().open(name);
            if (name.equals("StdCfg.ini")) {
                fosto = new FileOutputStream(Constant.iniPath + name);
            } else {
                fosto = new FileOutputStream(Constant.assestsPath + name);
                File oldFile = new File(Constant.pdfPath + name);
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }
            byte[] bt = new byte[Flags.FLAG5];
            while (true) {
                int c = fosfrom.read(bt);
                if (c <= 0) {
                    fosfrom.close();
                    fosto.close();
                    return;
                }
                fosto.write(bt, 0, c);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void openApp() throws NameNotFoundException {
        PackageInfo pi = getPackageManager().getPackageInfo("com.cnlaunch.golo3", 0);
        Intent resolveIntent = new Intent("android.intent.action.MAIN", null);
        resolveIntent.addCategory("android.intent.category.LAUNCHER");
        resolveIntent.setPackage(pi.packageName);
        ResolveInfo ri = (ResolveInfo) getPackageManager().queryIntentActivities(resolveIntent, 0).iterator().next();
        if (ri != null) {
            String packageName1 = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName(packageName1, className));
            startActivity(intent);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 || keyCode == Service.SUNRPC) {
            ExitDialog();
            return true;
        } else if (keyCode == Service.CISCO_SYS) {
            Toast.makeText(this.mcontext, this.mcontext.getString(C0136R.string.coming), 0).show();
            return true;
        } else {
            doLeftRight(keyCode);
            return super.onKeyDown(keyCode, event);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                if (resultCode == 10) {
                    this.showDialogFlag = data.getExtras().getInt(IfShowDialog, 0);
                    checkIfFirstCome();
                } else if (resultCode == 11 && data.getExtras().getInt("findPassword", 0) == 1) {
                    startActivity(new Intent(this, FindPwdActivity.class));
                }
            case REQUEST_USER_LOGIN_OUT /*25*/:
                if (resultCode == 11) {
                    startActivity(new Intent(this, FindPwdActivity.class));
                }
            case XStream.PRIORITY_VERY_HIGH /*10000*/:
                if (data != null) {
                    if (!XmlPullParser.NO_NAMESPACE.equals(data.getStringExtra("CAMERA"))) {
                        FastDiagnosisActivity.mVin = data.getStringExtra("CAMERA");
                        FastDiagnosisActivity.etVin.setText(data.getStringExtra("CAMERA"));
                    }
                }
            case FastDiagnosisActivity.PHOTO_ALBUM_REQUEST_CODE /*10001*/:
                if (data == null) {
                    return;
                }
                if (resultCode != -1) {
                    Toast.makeText(this, C0136R.string.ImageGetFail, 0).show();
                } else if (Environment.getExternalStorageState().equals("mounted")) {
                    Cursor cursor = managedQuery(data.getData(), new String[]{"_data"}, null, null, null);
                    if (cursor != null) {
                        int column_index = cursor.getColumnIndexOrThrow("_data");
                        if (cursor.getCount() <= 0 || !cursor.moveToFirst()) {
                            Toast.makeText(this, C0136R.string.circleImageNoFound, 0).show();
                            return;
                        }
                        String mImagePath = cursor.getString(column_index);
                        Intent intentPhoto = new Intent(this, VinPhotoActivity.class);
                        intentPhoto.putExtra("IMAGE_PATH", mImagePath);
                        startActivityForResult(intentPhoto, XStream.PRIORITY_VERY_HIGH);
                        return;
                    }
                    Toast.makeText(this, C0136R.string.circleImageNoFound, 0).show();
                } else {
                    Toast.makeText(this, C0136R.string.circleSDDisable, 0).show();
                }
            case FastDiagnosisActivity.VOICE_RECOGNITION_REQUEST_CODE /*10002*/:
                if (data != null) {
                    if (!XmlPullParser.NO_NAMESPACE.equals(data.getStringExtra("VOICE"))) {
                        String mStrVin = FastDiagnosisActivity.str2NumbersOrLetters(data.getStringExtra("VOICE")).replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE).toUpperCase();
                        Intent intent = new Intent("SPT_SET_VIN");
                        intent.putExtra("SPT_SET_VIN", mStrVin);
                        MainActivity.contexts.sendBroadcast(intent);
                    }
                }
            default:
        }
    }

    public void ExitDialog() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0136R.string.exitApp));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(C0136R.string.enter), new C07032());
        builder.setNegativeButton(getString(C0136R.string.cancel), new C07043());
        builder.create().show();
    }

    public void ExitDialog2() {
        Builder builder = new Builder(this);
        builder.setMessage("Currently support only English, French , German,Italian,Spanish,Russian.CRP229 will exit.");
        builder.setCancelable(false);
        builder.setPositiveButton(getString(C0136R.string.enter), new C07054());
        builder.create().show();
    }

    public void checkIfFirstCome() {
        boolean showReput = MySharedPreferences.share.getBoolean(MySharedPreferences.IfShowResponsibilityKey, true);
        boolean showToast = MySharedPreferences.share.getBoolean(MySharedPreferences.IfShowNoticKey, true);
        String showResponsibility = MySharedPreferences.getStringValue(this, "showResponsibility");
        String firstLogin = MySharedPreferences.getStringValue(this, "firstLogin");
        String showNoticKey = MySharedPreferences.getStringValue(this, "showNoticKey");
        if (showReput && this.showDialogFlag == 2) {
            if (showResponsibility == null || showResponsibility.equalsIgnoreCase(Contact.RELATION_FRIEND)) {
                MySharedPreferences.setString(this, "showResponsibility", Contact.RELATION_BACKNAME);
                initPopupWindow();
            }
        } else if (showReput || this.showDialogFlag != 2) {
            if (!showToast || this.showDialogFlag != 1) {
                return;
            }
            if (showNoticKey == null || showNoticKey.equalsIgnoreCase(Contact.RELATION_FRIEND)) {
                MySharedPreferences.setString(this, "showNoticKey", Contact.RELATION_BACKNAME);
                initToastPopupWindow();
            }
        } else if (firstLogin == null || firstLogin.equalsIgnoreCase(Contact.RELATION_FRIEND)) {
            MySharedPreferences.setString(this, "firstLogin", Contact.RELATION_BACKNAME);
        }
    }

    public void initToastPopupWindow() {
        this.toastPopupWindow = new Builder(this).create();
        this.toastPopupWindow.setCanceledOnTouchOutside(false);
        this.toastPopupWindow.setView(toastDialogView());
        this.toastPopupWindow.show();
        this.toastPopupWindow.setOnKeyListener(new C07065());
        this.toastPopupWindow.setContentView(toastDialogView(), new ViewGroup.LayoutParams(-1, -1));
    }

    public void initPopupWindow() {
        this.reputPopupWindow = new Builder(this).create();
        this.reputPopupWindow.setCanceledOnTouchOutside(false);
        this.reputPopupWindow.setView(reputDialogView());
        this.reputPopupWindow.show();
        this.reputPopupWindow.setOnKeyListener(new C07076());
        this.reputPopupWindow.setContentView(reputDialogView(), new ViewGroup.LayoutParams(-1, -1));
    }

    private View reputDialogView() {
        this.reput_lay = (RelativeLayout) LayoutInflater.from(this.mcontext).inflate(C0136R.layout.reputation_pop, null);
        this.rlPop = (RelativeLayout) this.reput_lay.findViewById(C0136R.id.rl_popdialog);
        if (getResources().getConfiguration().orientation == 2) {
            this.rlPop.setPadding(0, 60, 0, 60);
        } else {
            this.rlPop.setPadding(30, Opcodes.FCMPG, 30, Opcodes.FCMPG);
        }
        this.reput_close = (ImageView) this.reput_lay.findViewById(C0136R.id.repu_close);
        this.reput_check = (CheckBox) this.reput_lay.findViewById(C0136R.id.repu_select);
        this.reput_btn = (Button) this.reput_lay.findViewById(C0136R.id.repu_btn);
        this.reput_btn.setOnClickListener(this);
        this.reput_close.setOnClickListener(this);
        return this.reput_lay;
    }

    private View toastDialogView() {
        this.toast_lay = (RelativeLayout) LayoutInflater.from(this.mcontext).inflate(C0136R.layout.main_notic, null);
        this.toast_check = (CheckBox) this.toast_lay.findViewById(C0136R.id.main_selected);
        this.toast_btn = (Button) this.toast_lay.findViewById(C0136R.id.readbtn);
        this.toast_btn.setOnClickListener(this);
        return this.toast_lay;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.receiver, this.myIntentFilter);
    }

    public void doLeftRight(int keyCode) {
        int mCurScreen = this.workSpace.getmCurScreen();
        if (keyCode == 21) {
            if (mCurScreen > 0) {
                this.workSpace.setmCurScreen(mCurScreen - 1);
                this.workSpace.snapToScreen(this.workSpace.getmCurScreen());
            }
        } else if (keyCode == 22 && mCurScreen < this.workSpace.getChildCount() - 1) {
            this.workSpace.setmCurScreen(mCurScreen + 1);
            this.workSpace.snapToScreen(this.workSpace.getmCurScreen());
        }
    }

    private void setScrollWorkspace(int slidePoint) {
        mSlidePoint = slidePoint + 1;
        this.mSlidePoinView.postInvalidate();
        if (slidePoint == 0) {
            this.page1.setSelected(true);
            this.page2.setSelected(false);
        } else if (slidePoint == 1) {
            this.page1.setSelected(false);
            this.page2.setSelected(true);
        }
    }

    private void reset() {
        int load_cur_screen = MySharedPreferences.getIntValue(this, "CUR_SCREEN2");
        if (load_cur_screen != 0) {
            setScrollWorkspace(load_cur_screen);
            this.workSpace.setToScreen(load_cur_screen);
            if (load_cur_screen == 1) {
                this.page1.setSelected(false);
                this.page2.setSelected(true);
                return;
            }
            return;
        }
        setScrollWorkspace(0);
        this.page2.setSelected(false);
        this.page1.setSelected(true);
        this.workSpace.setToScreen(0);
    }

    public void click(int name) {
        Intent intent;
        if (name == C0136R.string.left_btn_zhenduan) {
            if (this.hasGetAsia.booleanValue() && this.hasGetEuro.booleanValue() && this.hasGetAmerican.booleanValue()) {
                intent = mMainActivityIntent;
                intent.putExtra(IfShowDialog, 2);
                intent.putExtra("loadSpecail", false);
                startActivity(intent);
            }
        } else if (name == C0136R.string.special) {
            if (this.hasGetSpecail.booleanValue()) {
                intent = mMainActivityIntent;
                intent.putExtra(IfShowDialog, 2);
                intent.putExtra("loadSpecail", true);
                startActivity(intent);
            }
        } else if (name == C0136R.string.left_btn_db) {
            intent = new Intent();
            intent.setClass(this.mcontext, DiagnosisdatabaseActivity.class);
            this.mcontext.startActivity(intent);
        } else if (name == C0136R.string.left_btn_kongjian) {
            intent = new Intent();
            intent.setClass(this.mcontext, SpaceDiagnosticReportActivity.class);
            this.mcontext.startActivity(intent);
        } else if (name == C0136R.string.golo) {
            Toast.makeText(this.mcontext, this.mcontext.getString(C0136R.string.coming), 0).show();
        } else if (name == C0136R.string.a_key_to_upgrade) {
            intent = new Intent();
            intent.setClass(this.mcontext, KeyToUpgradeActivity.class);
            this.mcontext.startActivity(intent);
        } else if (name == C0136R.string.left_btn_guanli) {
            intent = new Intent();
            intent.setClass(this.mcontext, UserActivity.class);
            this.mcontext.startActivity(intent);
        } else if (name == C0136R.string.left_btn_gduo) {
            intent = new Intent();
            intent.setClass(this.mcontext, MoreActivity.class);
            this.mcontext.startActivity(intent);
        } else if (name == C0136R.string.fast_diagnosis) {
            intent = new Intent();
            intent.setClass(this.mcontext, FastDiagnosisActivity.class);
            this.mcontext.startActivity(intent);
        }
    }

    public void onScrollComplete(ScrollEvent e) {
        setScrollWorkspace(e.curScreen);
        MySharedPreferences.setInt(this, "CUR_SCREEN2", e.curScreen);
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
        click(((MenuItem) arg1.getTag()).name);
    }

    public synchronized void onResume() {
        super.onResume();
        reset();
    }
}
