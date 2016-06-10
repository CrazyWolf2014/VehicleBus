package com.ifoer.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431frame.C0136R.drawable;
import com.cnmobi.im.util.XmppConnection;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SecurityQuestionDTO;
import com.ifoer.expedition.client.NotificationService;
import com.ifoer.expeditionphone.FindPwdActivity;
import com.ifoer.expeditionphone.KeyToUpgradeActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.ModiPassWordActivity;
import com.ifoer.expeditionphone.MoreDataActivty;
import com.ifoer.expeditionphone.SerialNumberActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.Common;
import com.ifoer.util.GetChargCardInfo;
import com.ifoer.util.GetUpdateRecord;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.Tools;
import com.ifoer.util.UpgradeProduct;
import com.ifoer.util.XmppTool;
import com.ifoer.view.MenuHorizontalScrollView;
import com.ifoer.webservice.KeyToLogin;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.plugin.BaseProfile;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class UserActivity extends Activity {
    public static String DOWNLOAD_BIN_BASE_VERSION;
    private static final String LOCAL_APK_PATH;
    public static boolean hasDownLoad;
    public static IntentFilter myIntentFilter;
    private boolean f1304D;
    private int DefScanPad;
    private String Display_models;
    private RelativeLayout Local_upgrade;
    private String Show_models;
    private ProgressDialog binProgressDialogs;
    private Builder builder;
    private RelativeLayout charge;
    private RelativeLayout checkCarInfo;
    Class<drawable> cls;
    private Context context;
    private RelativeLayout derma;
    private AlertDialog dialog_close;
    private RelativeLayout feed_btn;
    private LayoutInflater inflater;
    private RelativeLayout keyToUpgrade;
    private long lastTime;
    private LinearLayout layout_close;
    private Button logout;
    private BroadcastReceiver mBroadcastReceiver;
    private final Handler mHandler;
    private RelativeLayout menu;
    public Button menuBtn;
    private RelativeLayout modi_btn;
    private RelativeLayout modidata_btn;
    private RelativeLayout modiprintdata_btn;
    private RelativeLayout modipwdLay;
    private RelativeLayout modipwdquesLay;
    private RelativeLayout portLay;
    private RelativeLayout port_btn;
    private RelativeLayout question_btn;
    private MenuHorizontalScrollView scrollView;
    List<SecurityQuestionDTO> securityQuestionList;
    private TextView serial;
    private RelativeLayout serial_number;
    private RelativeLayout show_btn;
    Dialog skinDialog;
    private ToggleButton slipButton;
    private RelativeLayout toast_lay;
    private String unzipPath;
    private RelativeLayout upgradeRecord;

    /* renamed from: com.ifoer.ui.UserActivity.17 */
    class AnonymousClass17 implements OnItemClickListener {
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ ArrayList val$lstImageItem;

        AnonymousClass17(Context context, ArrayList arrayList) {
            this.val$context = context;
            this.val$lstImageItem = arrayList;
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            try {
                MySharedPreferences.setString(this.val$context, MySharedPreferences.BgIdKey, (String) ((HashMap) this.val$lstImageItem.get(arg2)).get("ItemImage"));
                new Options().inSampleSize = 2;
                ((Activity) this.val$context).getWindow().setBackgroundDrawable(MySharedPreferences.getBgId(this.val$context));
                if (MySharedPreferences.getIntValue(this.val$context, MySharedPreferences.DefScanPad) != 1) {
                    UserActivity.this.skinDialog.dismiss();
                } else {
                    UserActivity.this.dialog_close.dismiss();
                }
                Toast.makeText(this.val$context, this.val$context.getResources().getString(C0136R.string.base_derma), 1).show();
                this.val$context.sendBroadcast(new Intent("changeSkinSuccess"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.ifoer.ui.UserActivity.18 */
    class AnonymousClass18 implements OnClickListener {
        private final /* synthetic */ EditText val$input;

        AnonymousClass18(EditText editText) {
            this.val$input = editText;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (TextUtils.isEmpty(this.val$input.getText().toString()) || !Tools.isNumeric(this.val$input.getText().toString())) {
                Toast.makeText(UserActivity.this.context, UserActivity.this.context.getString(C0136R.string.ERROR_USERPASW_FORM), 0).show();
            } else {
                new UpgradeProduct(UserActivity.this.context, UserActivity.this.binProgressDialogs, UserActivity.this.mHandler, this.val$input.getText().toString()).execute(new String[0]);
            }
        }
    }

    /* renamed from: com.ifoer.ui.UserActivity.19 */
    class AnonymousClass19 implements OnClickListener {
        private final /* synthetic */ EditText val$input;

        AnonymousClass19(EditText editText) {
            this.val$input = editText;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (TextUtils.isEmpty(this.val$input.getText().toString()) || !Tools.isNumeric(this.val$input.getText().toString())) {
                Toast.makeText(UserActivity.this.context, UserActivity.this.context.getString(C0136R.string.ERROR_USERPASW_FORM), 0).show();
            } else {
                new GetChargCardInfo(UserActivity.this.context, this.val$input.getText().toString(), UserActivity.this.binProgressDialogs, UserActivity.this.mHandler).execute(new String[0]);
            }
        }
    }

    /* renamed from: com.ifoer.ui.UserActivity.1 */
    class C07311 extends BroadcastReceiver {
        C07311() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String number = MySharedPreferences.getStringValue(context, MySharedPreferences.serialNoKey);
            if (action.equals("changenumber")) {
                UserActivity.this.serial.setText(number);
            } else if (action.equals("Show_names")) {
                if (TextUtils.isEmpty(MySharedPreferences.getStringValue(context, MySharedPreferences.CCKey))) {
                    UserActivity.this.logout.setText(C0136R.string.Login_of_the_user);
                } else {
                    UserActivity.this.logout.setText(C0136R.string.Cancellation_of_the_user);
                }
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - UserActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
                if (UserActivity.hasDownLoad) {
                    MyApplication.getInstance().exitActivity("com.ifoer.expeditionphone.DownloadBinActivity");
                }
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                UserActivity.this.lastTime = System.currentTimeMillis();
            }
            if (!intent.getAction().equals("upgrade")) {
            }
        }
    }

    /* renamed from: com.ifoer.ui.UserActivity.2 */
    class C07322 extends Handler {
        C07322() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (UserActivity.this.binProgressDialogs != null && UserActivity.this.binProgressDialogs.isShowing()) {
                        UserActivity.this.binProgressDialogs.dismiss();
                    }
                    Toast.makeText(UserActivity.this.context, C0136R.string.timeout, 0).show();
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    if (UserActivity.this.binProgressDialogs != null && UserActivity.this.binProgressDialogs.isShowing()) {
                        UserActivity.this.binProgressDialogs.dismiss();
                    }
                    Toast.makeText(UserActivity.this.context, C0136R.string.get_data_fail, 1).show();
                case Constant.TIME_OUT /*800*/:
                    if (UserActivity.this.binProgressDialogs != null && UserActivity.this.binProgressDialogs.isShowing()) {
                        UserActivity.this.binProgressDialogs.dismiss();
                    }
                    Toast.makeText(UserActivity.this.context, C0136R.string.timeout, 1).show();
                case Constant.SETVICE_ABNORMAL /*801*/:
                    if (UserActivity.this.binProgressDialogs != null && UserActivity.this.binProgressDialogs.isShowing()) {
                        UserActivity.this.binProgressDialogs.dismiss();
                    }
                    Toast.makeText(UserActivity.this.context, C0136R.string.the_service_side_abnormal, 1).show();
                case Constant.ERROR_NETWORK /*803*/:
                    Toast.makeText(UserActivity.this.context, C0136R.string.ERROR_NETWORK, 1).show();
                case Constant.ERROR_SERVER /*804*/:
                    Toast.makeText(UserActivity.this.context, UserActivity.this.context.getString(C0136R.string.ERROR_SERVER), 1).show();
                case Constant.ERROR_CODE /*805*/:
                    if (msg.getData() == null || msg.getData().getString(BundleBuilder.AskFromMessage) == null || msg.getData().getString(BundleBuilder.AskFromMessage).equals(XmlPullParser.NO_NAMESPACE)) {
                        Toast.makeText(UserActivity.this.context, UserActivity.this.getString(C0136R.string.error_server), 0).show();
                    } else {
                        Toast.makeText(UserActivity.this.context, msg.getData().getString(BundleBuilder.AskFromMessage), 0).show();
                    }
                case Constant.CHARGE_SECCUSS /*808*/:
                    SimpleDialog.chargeSuccess(UserActivity.this.context, UserActivity.this.binProgressDialogs, UserActivity.this.mHandler, msg.obj, false);
                default:
            }
        }
    }

    /* renamed from: com.ifoer.ui.UserActivity.3 */
    class C07333 implements View.OnClickListener {
        C07333() {
        }

        public void onClick(View arg0) {
            if (TextUtils.isEmpty(MySharedPreferences.getStringValue(UserActivity.this.context, MySharedPreferences.CCKey))) {
                SimpleDialog.ToastToLogin2(UserActivity.this.context, UserActivity.this.mHandler, UserActivity.this.binProgressDialogs);
                return;
            }
            Log.i(XmlPullParser.NO_NAMESPACE, "\u5145\u503c");
            UserActivity.this.buy();
        }
    }

    /* renamed from: com.ifoer.ui.UserActivity.4 */
    class C07344 implements View.OnClickListener {
        C07344() {
        }

        public void onClick(View arg0) {
            if (TextUtils.isEmpty(MySharedPreferences.getStringValue(UserActivity.this.context, MySharedPreferences.CCKey))) {
                SimpleDialog.ToastToLogin2(UserActivity.this.context, UserActivity.this.mHandler, UserActivity.this.binProgressDialogs);
                return;
            }
            Log.i(XmlPullParser.NO_NAMESPACE, "\u5145\u503c");
            UserActivity.this.checkcardInfo();
        }
    }

    /* renamed from: com.ifoer.ui.UserActivity.5 */
    class C07355 implements View.OnClickListener {
        C07355() {
        }

        public void onClick(View arg0) {
            if (TextUtils.isEmpty(MySharedPreferences.getStringValue(UserActivity.this.context, MySharedPreferences.CCKey))) {
                SimpleDialog.ToastToLogin2(UserActivity.this.context, UserActivity.this.mHandler, UserActivity.this.binProgressDialogs);
                return;
            }
            Log.i(XmlPullParser.NO_NAMESPACE, "\u83b7\u53d6\u70b9\u5361\u5145\u503c\u8bb0\u5f55");
            new GetUpdateRecord(UserActivity.this.context, UserActivity.this.binProgressDialogs, UserActivity.this.mHandler).execute(new String[0]);
        }
    }

    /* renamed from: com.ifoer.ui.UserActivity.6 */
    class C07376 implements View.OnClickListener {

        /* renamed from: com.ifoer.ui.UserActivity.6.1 */
        class C07361 implements OnClickListener {
            C07361() {
            }

            public void onClick(DialogInterface dialog, int which) {
                MySharedPreferences.setBoolean(UserActivity.this.context, MySharedPreferences.iflogout, true);
                Intent intent = new Intent("Show_new_action");
                intent.putExtra("show_new", false);
                UserActivity.this.context.sendBroadcast(intent);
                NotificationService.startActivity = Boolean.valueOf(false);
                if (MainActivity.serviceManager != null) {
                    MainActivity.serviceManager.stopService();
                }
                MainActivity.deleteFiles(MainActivity.dataDir + "/libs/cnlaunch/");
                MySharedPreferences.getSharedPref(UserActivity.this.context).edit().putString(MySharedPreferences.CCKey, XmlPullParser.NO_NAMESPACE).commit();
                MySharedPreferences.getSharedPref(UserActivity.this.context).edit().putString(BaseProfile.COL_USERNAME, XmlPullParser.NO_NAMESPACE).commit();
                MySharedPreferences.getSharedPref(UserActivity.this.context).edit().putString("password", XmlPullParser.NO_NAMESPACE).commit();
                MySharedPreferences.getSharedPref(UserActivity.this.context).edit().putString(MySharedPreferences.TokenKey, XmlPullParser.NO_NAMESPACE).commit();
                MySharedPreferences.setString(UserActivity.this.context, "openshowuser", Contact.RELATION_ASK);
                UserActivity.this.context.sendBroadcast(new Intent("Nushow_names"));
                UserActivity.this.logout.setText(C0136R.string.Login_of_the_user);
                try {
                    XmppConnection.closeConnection();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }

        C07376() {
        }

        public void onClick(View v) {
            String cc = MySharedPreferences.getStringValue(UserActivity.this.context, MySharedPreferences.CCKey);
            if (UserActivity.this.logout.getText().equals(UserActivity.this.context.getString(C0136R.string.Cancellation_of_the_user))) {
                new Builder(UserActivity.this.context).setTitle(C0136R.string.if_log_out).setPositiveButton(C0136R.string.enter, new C07361()).setNegativeButton(C0136R.string.cancel, null).show();
            } else {
                new KeyToLogin(UserActivity.this.context, UserActivity.this.mHandler, UserActivity.this.binProgressDialogs).login();
            }
        }
    }

    /* renamed from: com.ifoer.ui.UserActivity.7 */
    class C07387 implements OnCheckedChangeListener {
        C07387() {
        }

        public void onCheckedChanged(CompoundButton arg0, boolean CheckState) {
            if (CheckState) {
                MySharedPreferences.setString(UserActivity.this.context, MySharedPreferences.IfShow, Contact.RELATION_FRIEND);
                UserActivity.this.context.sendBroadcast(new Intent("Display_models"));
                return;
            }
            MySharedPreferences.setString(UserActivity.this.context, MySharedPreferences.IfShow, Contact.RELATION_ASK);
            UserActivity.this.context.sendBroadcast(new Intent("Show_models"));
        }
    }

    /* renamed from: com.ifoer.ui.UserActivity.8 */
    class C07398 implements View.OnClickListener {
        C07398() {
        }

        public void onClick(View v) {
            if (TextUtils.isEmpty(MySharedPreferences.getStringValue(UserActivity.this.context, MySharedPreferences.CCKey))) {
                SimpleDialog.ToastToLogin2(UserActivity.this.context, UserActivity.this.mHandler, UserActivity.this.binProgressDialogs);
                return;
            }
            UserActivity.this.startActivity(new Intent(UserActivity.this, ModiPassWordActivity.class));
        }
    }

    /* renamed from: com.ifoer.ui.UserActivity.9 */
    class C07409 implements View.OnClickListener {
        C07409() {
        }

        public void onClick(View v) {
            UserActivity.this.startActivity(new Intent(UserActivity.this, FindPwdActivity.class));
        }
    }

    public UserActivity() {
        this.Display_models = "Display_models";
        this.Show_models = "Show_models";
        this.f1304D = true;
        this.securityQuestionList = new ArrayList();
        this.cls = drawable.class;
        this.unzipPath = Constant.UNZIP_PATH;
        this.inflater = null;
        this.DefScanPad = 0;
        this.mBroadcastReceiver = new C07311();
        this.mHandler = new C07322();
    }

    static {
        DOWNLOAD_BIN_BASE_VERSION = "00.00";
        LOCAL_APK_PATH = Environment.getExternalStorageDirectory() + MySharedPreferences.getStringValue(MainActivity.contexts, "LOCAL_UPGRADE_APK");
        hasDownLoad = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.user);
        MyApplication.getInstance().addActivity(this);
        this.context = this;
        creatView();
    }

    private void creatView() {
        registerBoradcastReceiver();
        this.scrollView = (MenuHorizontalScrollView) findViewById(C0136R.id.scrollView);
        this.menu = (RelativeLayout) findViewById(C0136R.id.main_leftmenu);
        this.port_btn = (RelativeLayout) findViewById(C0136R.id.login1);
        this.modidata_btn = (RelativeLayout) findViewById(C0136R.id.updatedata);
        this.modiprintdata_btn = (RelativeLayout) findViewById(C0136R.id.modify_diagdata);
        this.modi_btn = (RelativeLayout) findViewById(C0136R.id.update);
        this.question_btn = (RelativeLayout) findViewById(C0136R.id.notic);
        this.feed_btn = (RelativeLayout) findViewById(C0136R.id.feedback);
        this.serial_number = (RelativeLayout) findViewById(C0136R.id.serial_number);
        this.Local_upgrade = (RelativeLayout) findViewById(C0136R.id.Local_upgrade);
        this.derma = (RelativeLayout) findViewById(C0136R.id.derma);
        this.show_btn = (RelativeLayout) findViewById(C0136R.id.show);
        this.upgradeRecord = (RelativeLayout) findViewById(C0136R.id.updaterecord);
        this.charge = (RelativeLayout) findViewById(C0136R.id.buy);
        this.logout = (Button) findViewById(C0136R.id.logout);
        this.serial = (TextView) findViewById(C0136R.id.serial_number_text);
        String sNumber = MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey);
        if (sNumber != null) {
            this.serial.setText(sNumber);
        }
        String cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        this.charge.setOnClickListener(new C07333());
        this.checkCarInfo = (RelativeLayout) findViewById(C0136R.id.carinfo);
        this.checkCarInfo.setOnClickListener(new C07344());
        this.upgradeRecord.setOnClickListener(new C07355());
        this.logout.setOnClickListener(new C07376());
        this.slipButton = (ToggleButton) findViewById(C0136R.id.slipButton);
        String ifshow = MySharedPreferences.getStringValue(this.context, MySharedPreferences.IfShow);
        if (ifshow != null && ifshow.equals(Contact.RELATION_FRIEND)) {
            this.slipButton.setChecked(true);
        } else if (ifshow == null || !ifshow.equals(Contact.RELATION_ASK)) {
            this.slipButton.setChecked(false);
        } else {
            this.slipButton.setChecked(false);
        }
        this.slipButton.setOnCheckedChangeListener(new C07387());
        this.menuBtn = (Button) findViewById(C0136R.id.menuBtn);
        this.modi_btn.setOnClickListener(new C07398());
        this.feed_btn.setOnClickListener(new C07409());
        this.modidata_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String cc = MySharedPreferences.getStringValue(UserActivity.this.context, MySharedPreferences.CCKey);
                Intent intent = new Intent(UserActivity.this, MoreDataActivty.class);
                if (TextUtils.isEmpty(cc)) {
                    intent.putExtra("hasLogin", false);
                } else {
                    intent.putExtra("hasLogin", true);
                }
                UserActivity.this.startActivity(intent);
            }
        });
        this.serial_number.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserActivity.this.context.startActivity(new Intent(UserActivity.this.context, SerialNumberActivity.class));
            }
        });
        this.Local_upgrade.setOnClickListener(new View.OnClickListener() {

            /* renamed from: com.ifoer.ui.UserActivity.12.1 */
            class C07301 implements OnClickListener {
                C07301() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    if (new File(UserActivity.LOCAL_APK_PATH).exists()) {
                        DBDao.getInstance(UserActivity.this.context).deleteAllTable(MainActivity.database);
                        MySharedPreferences.setString(UserActivity.this.context, MySharedPreferences.loginfirst, XmlPullParser.NO_NAMESPACE);
                        MySharedPreferences.getSharedPref(UserActivity.this.context).edit().putInt(MySharedPreferences.BgIdKey, 0).commit();
                        Intent i = new Intent("android.intent.action.VIEW");
                        i.setDataAndType(Uri.parse("file://" + UserActivity.LOCAL_APK_PATH), "application/vnd.android.package-archive");
                        UserActivity.this.context.startActivity(i);
                        if (Constant.mChatService != null) {
                            Constant.mChatService.stop();
                        }
                        if (MainActivity.serviceManager != null) {
                            MainActivity.serviceManager.stopService();
                        }
                        MainActivity.deleteFiles(MainActivity.dataDir + "/libs/cnlaunch/");
                        dialog.dismiss();
                        MySharedPreferences.getSharedPref(MainActivity.contexts).edit().putString(MySharedPreferences.CCKey, XmlPullParser.NO_NAMESPACE).commit();
                        MySharedPreferences.getSharedPref(MainActivity.contexts).edit().putString(MySharedPreferences.TokenKey, XmlPullParser.NO_NAMESPACE).commit();
                        try {
                            XmppTool.closeConnection();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                        MainActivity.database.close();
                        MyApplication.getInstance().exit();
                        Process.killProcess(Process.myPid());
                        System.exit(0);
                        return;
                    }
                    Toast.makeText(UserActivity.this.context, UserActivity.this.getResources().getString(C0136R.string.main_file_null), 0).show();
                }
            }

            public void onClick(View arg0) {
                new Builder(UserActivity.this.context).setTitle(C0136R.string.updatesoft_right).setPositiveButton(C0136R.string.Ensure, new C07301()).setNegativeButton(C0136R.string.cancel, null).show();
            }
        });
        this.derma.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserActivity.this.ChangeSkin(UserActivity.this.context);
            }
        });
        this.keyToUpgrade = (RelativeLayout) findViewById(C0136R.id.keyToUpgrade);
        this.keyToUpgrade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String cc = MySharedPreferences.getStringValue(UserActivity.this.context, MySharedPreferences.CCKey);
                String serialNo = MySharedPreferences.getStringValue(UserActivity.this.context, MySharedPreferences.serialNoKey);
                if (cc == null || cc.equals(XmlPullParser.NO_NAMESPACE)) {
                    SimpleDialog.ToastToLogin2(UserActivity.this.context, UserActivity.this.mHandler, UserActivity.this.binProgressDialogs);
                } else if (serialNo == null || serialNo.equals(XmlPullParser.NO_NAMESPACE)) {
                    Intent intent = new Intent(UserActivity.this.context, SerialNumberActivity.class);
                    intent.putExtra("flag", "upgrade");
                    UserActivity.this.context.startActivity(intent);
                } else {
                    UserActivity.this.context.startActivity(new Intent(UserActivity.this.context, KeyToUpgradeActivity.class));
                }
            }
        });
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("changenumber");
        myIntentFilter.addAction("Show_names");
        myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        myIntentFilter.addAction("upgrade");
        this.context.registerReceiver(this.mBroadcastReceiver, myIntentFilter);
    }

    protected void ChangeSkin(Context context) {
        if (MySharedPreferences.getIntValue(context, MySharedPreferences.DefScanPad) != 1) {
            this.skinDialog = new Builder(context).setCancelable(true).setTitle(context.getResources().getString(C0136R.string.pleaseselect)).setView(SelectSkin(context)).show();
            return;
        }
        this.dialog_close = new Builder(context).create();
        this.dialog_close.setCanceledOnTouchOutside(false);
        this.dialog_close.setCancelable(true);
        this.dialog_close.setView(SelectSkin(context));
        this.dialog_close.show();
        this.dialog_close.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != 4) {
                    return false;
                }
                UserActivity.this.dialog_close.dismiss();
                return true;
            }
        });
        this.dialog_close.setContentView(SelectSkin(context), new LayoutParams(-1, -1));
    }

    private View SelectSkin(Context context) {
        View view = LayoutInflater.from(context).inflate(C0136R.layout.skin_gridview, null);
        if (MySharedPreferences.getIntValue(context, MySharedPreferences.DefScanPad) == 1) {
            this.layout_close = (LinearLayout) view.findViewById(C0136R.id.repu_close);
            this.layout_close.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UserActivity.this.dialog_close.dismiss();
                }
            });
        }
        GridView skinGrid = (GridView) view.findViewById(C0136R.id.gridView);
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList();
        try {
            HashMap<String, Object> map = new HashMap();
            map.put("ItemImage", "skin_hui.png");
            map.put("smallImage", Integer.valueOf(this.cls.getDeclaredField("skin_hui_small").getInt(null)));
            lstImageItem.add(map);
            map = new HashMap();
            map.put("ItemImage", "skin_mu.jpg");
            map.put("smallImage", Integer.valueOf(this.cls.getDeclaredField("skin_mu_small").getInt(null)));
            lstImageItem.add(map);
            map = new HashMap();
            map.put("ItemImage", "skin_xing.jpg");
            map.put("smallImage", Integer.valueOf(this.cls.getDeclaredField("skin_xing_small").getInt(null)));
            lstImageItem.add(map);
            map = new HashMap();
            map.put("ItemImage", "skin_hua.png");
            map.put("smallImage", Integer.valueOf(this.cls.getDeclaredField("skin_hua_small").getInt(null)));
            lstImageItem.add(map);
            skinGrid.setAdapter(new SimpleAdapter(context, lstImageItem, C0136R.layout.skin_gridview_item, new String[]{"smallImage"}, new int[]{C0136R.id.ItemImage}));
        } catch (Exception e) {
            e.printStackTrace();
        }
        skinGrid.setOnItemClickListener(new AnonymousClass17(context, lstImageItem));
        return view;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finish();
            return true;
        } else if (keyCode != Service.SUNRPC) {
            return super.onKeyDown(keyCode, event);
        } else {
            finish();
            return true;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mBroadcastReceiver != null) {
            unregisterReceiver(this.mBroadcastReceiver);
        }
    }

    public void onResume() {
        super.onResume();
        String cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        if (!Common.isNetworkAvailable(this.context)) {
            Toast.makeText(this.context, this.context.getString(C0136R.string.ERROR_NETWORK), 0).show();
        } else if (TextUtils.isEmpty(cc)) {
            this.logout.setText(C0136R.string.Login_of_the_user);
        } else {
            this.logout.setText(C0136R.string.Cancellation_of_the_user);
        }
    }

    private void buy() {
        EditText input = new EditText(this.context);
        input.setHint("24\u4f4d\u6570\u5b57\u5bc6\u7801");
        this.builder = new Builder(this.context);
        this.builder.setTitle(C0136R.string.input_card_psw).setView(input).setPositiveButton(C0136R.string.enter, new AnonymousClass18(input)).setNegativeButton(C0136R.string.cancel, null).show();
    }

    private void checkcardInfo() {
        EditText input = new EditText(this.context);
        input.setText("970290000100");
        this.builder = new Builder(this.context);
        this.builder.setTitle(C0136R.string.input_card_psw).setView(input).setPositiveButton(C0136R.string.enter, new AnonymousClass19(input)).setNegativeButton(C0136R.string.cancel, null).show();
    }
}
