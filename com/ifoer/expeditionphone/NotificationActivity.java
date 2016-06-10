package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.car.result.WSResult;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.NotificationAdapter;
import com.ifoer.entity.PushMessageContentResult;
import com.ifoer.entity.PushMessageDTO;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.expedition.client.Constants;
import com.ifoer.expedition.client.LogUtil;
import com.ifoer.mine.Contact;
import com.ifoer.util.Common;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.webservice.RegisteredProduct;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.achartengine.renderer.DefaultRenderer;
import org.apache.harmony.javax.security.auth.callback.ConfirmationCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class NotificationActivity extends Activity {
    private static final String LOGTAG;
    private NotificationAdapter adapter;
    private LinearLayout back;
    private TextView carType;
    private PushMessageContentResult contentResult;
    private Context context;
    private PushMessageDTO dto;
    Handler handler;
    private TextView language;
    private ListView listview;
    private String messageId;
    private final String packageName;
    private ProgressDialog progressDialogs;
    private Button resolvedFlag;
    private TextView time;
    private TextView title;
    private ArrayList<SptTroubleTest> troubleTestList;
    private TextView version;

    /* renamed from: com.ifoer.expeditionphone.NotificationActivity.1 */
    class C06151 extends Handler {
        C06151() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (NotificationActivity.this.progressDialogs != null && NotificationActivity.this.progressDialogs.isShowing()) {
                        NotificationActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(NotificationActivity.this.context, C0136R.string.timeout, 0).show();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.NotificationActivity.2 */
    class C06162 implements OnClickListener {
        C06162() {
        }

        public void onClick(View v) {
            if (!NotificationActivity.this.dto.getResolvedFlag().equals(Contact.RELATION_ASK)) {
                return;
            }
            if (Common.isNetworkAvailable(NotificationActivity.this.context)) {
                new SetFaultResolvedFlag(Integer.parseInt(NotificationActivity.this.dto.getMessageId())).execute(new String[0]);
                return;
            }
            Toast.makeText(NotificationActivity.this.context, C0136R.string.network, 0).show();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.NotificationActivity.3 */
    class C06173 implements OnClickListener {
        C06173() {
        }

        public void onClick(View v) {
            ClassNotFoundException e;
            Boolean flag = Boolean.valueOf(NotificationActivity.isServiceStarted(NotificationActivity.this.context, "com.cnlaunch.x431frame"));
            System.out.println("flag = " + flag);
            if (flag.booleanValue()) {
                String className = NotificationActivity.this.isAppOnBefore();
                if (className.length() > 0) {
                    try {
                        Intent intent = new Intent(NotificationActivity.this, Class.forName(className));
                        Intent intent2;
                        try {
                            intent.setFlags(335544320);
                            NotificationActivity.this.startActivity(intent);
                            intent2 = intent;
                        } catch (ClassNotFoundException e2) {
                            e = e2;
                            intent2 = intent;
                            e.printStackTrace();
                            NotificationActivity.this.finish();
                            NotificationActivity.this.overridePendingTransition(0, 0);
                            return;
                        }
                    } catch (ClassNotFoundException e3) {
                        e = e3;
                        e.printStackTrace();
                        NotificationActivity.this.finish();
                        NotificationActivity.this.overridePendingTransition(0, 0);
                        return;
                    }
                    NotificationActivity.this.finish();
                    NotificationActivity.this.overridePendingTransition(0, 0);
                    return;
                }
                NotificationActivity.this.finish();
                NotificationActivity.this.overridePendingTransition(0, 0);
                return;
            }
            NotificationActivity.openCLD("com.cnlaunch.x431frame", NotificationActivity.this.context);
            NotificationActivity.this.finish();
            NotificationActivity.this.overridePendingTransition(0, 0);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.NotificationActivity.4 */
    class C06184 implements OnClickListener {
        C06184() {
        }

        public void onClick(View view) {
            Intent intent;
            ClassNotFoundException e;
            Boolean flag = Boolean.valueOf(NotificationActivity.isServiceStarted(NotificationActivity.this.context, "com.cnlaunch.x431frame"));
            System.out.println("flag = " + flag);
            if (flag.booleanValue()) {
                String className = NotificationActivity.this.isAppOnBefore();
                if (className.length() > 0) {
                    try {
                        Intent intent2 = new Intent(NotificationActivity.this, Class.forName(className));
                        try {
                            intent2.setFlags(335544320);
                            NotificationActivity.this.startActivity(intent2);
                            intent = intent2;
                        } catch (ClassNotFoundException e2) {
                            e = e2;
                            intent = intent2;
                            e.printStackTrace();
                            NotificationActivity.this.finish();
                            NotificationActivity.this.overridePendingTransition(0, 0);
                            return;
                        }
                    } catch (ClassNotFoundException e3) {
                        e = e3;
                        e.printStackTrace();
                        NotificationActivity.this.finish();
                        NotificationActivity.this.overridePendingTransition(0, 0);
                        return;
                    }
                    NotificationActivity.this.finish();
                    NotificationActivity.this.overridePendingTransition(0, 0);
                    return;
                }
                NotificationActivity.this.finish();
                NotificationActivity.this.overridePendingTransition(0, 0);
                return;
            }
            NotificationActivity.openCLD("com.cnlaunch.x431frame", NotificationActivity.this.context);
            NotificationActivity.this.finish();
            NotificationActivity.this.overridePendingTransition(0, 0);
        }
    }

    class MessageDetail extends AsyncTask<String, String, String> {
        MessageDetail() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            NotificationActivity.this.progressDialogs = new ProgressDialog(NotificationActivity.this.context);
            NotificationActivity.this.progressDialogs.setMessage(NotificationActivity.this.getResources().getString(C0136R.string.find_wait));
            NotificationActivity.this.progressDialogs.setCancelable(false);
            NotificationActivity.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            RegisteredProduct softService = new RegisteredProduct();
            String cc = MySharedPreferences.getStringValue(NotificationActivity.this.context, MySharedPreferences.CCKey);
            String token = MySharedPreferences.getStringValue(NotificationActivity.this.context, MySharedPreferences.TokenKey);
            softService.setCc(cc);
            softService.setToken(token);
            try {
                NotificationActivity.this.contentResult = softService.getPushMessageDetailContent(Integer.valueOf(Integer.parseInt(NotificationActivity.this.messageId)));
            } catch (SocketTimeoutException e) {
                NotificationActivity.this.handler.obtainMessage(0).sendToTarget();
            } catch (NumberFormatException e2) {
            } catch (NullPointerException e3) {
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (NotificationActivity.this.contentResult != null) {
                if (NotificationActivity.this.progressDialogs != null && NotificationActivity.this.progressDialogs.isShowing()) {
                    NotificationActivity.this.progressDialogs.dismiss();
                }
                switch (NotificationActivity.this.contentResult.getCode()) {
                    case ConfirmationCallback.UNSPECIFIED_OPTION /*-1*/:
                        SimpleDialog.validTokenDialog(NotificationActivity.this.context);
                    case KEYRecord.OWNER_USER /*0*/:
                        String detail = NotificationActivity.this.contentResult.getDetailContent();
                        if (detail != null && detail.length() > 0) {
                            String[] details = detail.split("\\*##\\*");
                            if (details != null && details.length == 5) {
                                NotificationActivity.this.dto.setVehicleType(details[0]);
                                NotificationActivity.this.dto.setVersions(details[1]);
                                NotificationActivity.this.dto.setLanguage(details[2]);
                                NotificationActivity.this.dto.setPushTime(details[3]);
                                NotificationActivity.this.title.setText(NotificationActivity.this.dto.getMessageTitle());
                                NotificationActivity.this.carType.setText(NotificationActivity.this.dto.getVehicleType());
                                NotificationActivity.this.version.setText(NotificationActivity.this.dto.getVersions());
                                NotificationActivity.this.language.setText(NotificationActivity.this.dto.getLanguage());
                                NotificationActivity.this.time.setText(NotificationActivity.this.dto.getPushTime());
                                if (NotificationActivity.this.dto.getResolvedFlag().equals(Contact.RELATION_ASK)) {
                                    NotificationActivity.this.resolvedFlag.setBackgroundResource(C0136R.drawable.untreated);
                                    NotificationActivity.this.resolvedFlag.setText(NotificationActivity.this.context.getResources().getText(C0136R.string.untreated));
                                } else {
                                    NotificationActivity.this.resolvedFlag.setBackgroundResource(C0136R.drawable.treated);
                                    NotificationActivity.this.resolvedFlag.setText(NotificationActivity.this.context.getResources().getText(C0136R.string.treated));
                                }
                                NotificationActivity.this.troubleTestList = NotificationActivity.getTroubleList(details[4]);
                                NotificationActivity.this.adapter = new NotificationAdapter(NotificationActivity.this.troubleTestList, NotificationActivity.this);
                                NotificationActivity.this.listview.setAdapter(NotificationActivity.this.adapter);
                            }
                        }
                    case MyHttpException.ERROR_PARAMETER_EMPTY /*401*/:
                        Toast.makeText(NotificationActivity.this.context, C0136R.string.notic_null, 0).show();
                    case MyHttpException.ERROR_SERVER /*500*/:
                        Toast.makeText(NotificationActivity.this.context, C0136R.string.the_service_side_abnormal, 0).show();
                    case MyHttpException.ERROR_SERIAL_NOEXIST /*658*/:
                        Toast.makeText(NotificationActivity.this.context, C0136R.string.port_null, 0).show();
                    case 741:
                        Toast.makeText(NotificationActivity.this.context, C0136R.string.push_info_not_exist, 0).show();
                    default:
                }
            } else if (NotificationActivity.this.progressDialogs != null && NotificationActivity.this.progressDialogs.isShowing()) {
                NotificationActivity.this.progressDialogs.dismiss();
            }
        }
    }

    class SetFaultResolvedFlag extends AsyncTask<String, String, String> {
        int flag;
        int messageId;
        WSResult wsResult;

        public SetFaultResolvedFlag(int messageId) {
            this.messageId = messageId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            NotificationActivity.this.progressDialogs = new ProgressDialog(NotificationActivity.this.context);
            NotificationActivity.this.progressDialogs.setMessage(NotificationActivity.this.getResources().getString(C0136R.string.find_wait));
            NotificationActivity.this.progressDialogs.setCancelable(false);
            NotificationActivity.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            RegisteredProduct softService = new RegisteredProduct();
            String cc = MySharedPreferences.getStringValue(NotificationActivity.this.context, MySharedPreferences.CCKey);
            String token = MySharedPreferences.getStringValue(NotificationActivity.this.context, MySharedPreferences.TokenKey);
            softService.setCc(cc);
            softService.setToken(token);
            try {
                this.wsResult = softService.setFaultResolvedFlag(Integer.valueOf(this.messageId), Integer.valueOf(1));
            } catch (NullPointerException e) {
            } catch (SocketTimeoutException e2) {
                NotificationActivity.this.handler.obtainMessage(0).sendToTarget();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.wsResult != null) {
                if (NotificationActivity.this.progressDialogs != null && NotificationActivity.this.progressDialogs.isShowing()) {
                    NotificationActivity.this.progressDialogs.dismiss();
                }
                switch (this.wsResult.getCode()) {
                    case ConfirmationCallback.UNSPECIFIED_OPTION /*-1*/:
                        SimpleDialog.validTokenDialog(NotificationActivity.this.context);
                    case KEYRecord.OWNER_USER /*0*/:
                        NotificationActivity.this.resolvedFlag.setText(NotificationActivity.this.context.getResources().getText(C0136R.string.treated));
                        NotificationActivity.this.dto.setResolvedFlag(Contact.RELATION_FRIEND);
                    case MyHttpException.ERROR_PARAMETER_EMPTY /*401*/:
                        Toast.makeText(NotificationActivity.this.context, C0136R.string.notic_null, 0).show();
                    case MyHttpException.ERROR_SERVER /*500*/:
                        Toast.makeText(NotificationActivity.this.context, C0136R.string.the_service_side_abnormal, 0).show();
                    case 741:
                        Toast.makeText(NotificationActivity.this.context, C0136R.string.push_info_not_exist, 0).show();
                    default:
                }
            } else if (NotificationActivity.this.progressDialogs != null && NotificationActivity.this.progressDialogs.isShowing()) {
                NotificationActivity.this.progressDialogs.dismiss();
            }
        }
    }

    public NotificationActivity() {
        this.packageName = "com.cnlaunch.x431frame";
        this.troubleTestList = new ArrayList();
        this.handler = new C06151();
    }

    static {
        LOGTAG = LogUtil.makeLogTag(NotificationActivity.class);
    }

    public void onCreate(Bundle savedInstanceState) {
        ClassNotFoundException e;
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.client_push_details_activity);
        MyApplication.getInstance().addActivity(this);
        this.context = this;
        initView();
        if (Common.isNetworkAvailable(this.context)) {
            new MessageDetail().execute(new String[0]);
            return;
        }
        Boolean flag = Boolean.valueOf(isServiceStarted(this.context, "com.cnlaunch.x431frame"));
        System.out.println("flag = " + flag);
        if (flag.booleanValue()) {
            String className = isAppOnBefore();
            if (className.length() > 0) {
                try {
                    Intent intent = new Intent(this, Class.forName(className));
                    Intent intent2;
                    try {
                        intent.setFlags(335544320);
                        startActivity(intent);
                        intent2 = intent;
                    } catch (ClassNotFoundException e2) {
                        e = e2;
                        intent2 = intent;
                        e.printStackTrace();
                        finish();
                        overridePendingTransition(0, 0);
                        return;
                    }
                } catch (ClassNotFoundException e3) {
                    e = e3;
                    e.printStackTrace();
                    finish();
                    overridePendingTransition(0, 0);
                    return;
                }
                finish();
                overridePendingTransition(0, 0);
                return;
            }
            finish();
            overridePendingTransition(0, 0);
            return;
        }
        openCLD("com.cnlaunch.x431frame", this.context);
        finish();
        overridePendingTransition(0, 0);
    }

    private void initView() {
        Intent intent = getIntent();
        this.messageId = intent.getStringExtra(Constants.NOTIFICATION_ID);
        String notificationApiKey = intent.getStringExtra(Constants.NOTIFICATION_API_KEY);
        String notificationTitle = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
        String notificationMessage = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
        String notificationUri = intent.getStringExtra(Constants.NOTIFICATION_URI);
        String notificationType = intent.getStringExtra(Constants.NOTIFICATION_TYPE);
        this.dto = new PushMessageDTO();
        this.dto.setMessageId(this.messageId);
        this.dto.setMessageDesc(notificationMessage);
        this.dto.setResolvedFlag(Contact.RELATION_ASK);
        this.dto.setMessageTitle(notificationTitle);
        this.dto.setMessageUrl(notificationUri);
        this.dto.setPushTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
        this.dto.setFaultType(notificationType);
        this.listview = (ListView) findViewById(C0136R.id.view);
        this.title = (TextView) findViewById(C0136R.id.title);
        this.carType = (TextView) findViewById(C0136R.id.carType);
        this.version = (TextView) findViewById(C0136R.id.version);
        this.language = (TextView) findViewById(C0136R.id.language);
        this.time = (TextView) findViewById(C0136R.id.time);
        this.resolvedFlag = (Button) findViewById(C0136R.id.resolvedFlag);
        this.resolvedFlag.setOnClickListener(new C06162());
        this.back = (LinearLayout) findViewById(C0136R.id.return_btn);
        this.back.setOnClickListener(new C06173());
    }

    private View createView(String title, String message, String uri) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(-1118482);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(5, 5, 5, 5);
        linearLayout.setLayoutParams(new LayoutParams(-1, -1));
        TextView textTitle = new TextView(this);
        textTitle.setText(title);
        textTitle.setTextSize(18.0f);
        textTitle.setTypeface(Typeface.DEFAULT, 1);
        textTitle.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        textTitle.setGravity(17);
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.setMargins(30, 30, 30, 0);
        textTitle.setLayoutParams(layoutParams);
        linearLayout.addView(textTitle);
        TextView textDetails = new TextView(this);
        textDetails.setText(message);
        textDetails.setTextSize(14.0f);
        textDetails.setTextColor(-13421773);
        textDetails.setGravity(17);
        layoutParams = new LayoutParams(-1, -2);
        layoutParams.setMargins(30, 10, 30, 20);
        textDetails.setLayoutParams(layoutParams);
        linearLayout.addView(textDetails);
        Button okButton = new Button(this);
        okButton.setText("Ok");
        okButton.setWidth(100);
        okButton.setOnClickListener(new C06184());
        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setGravity(17);
        innerLayout.addView(okButton);
        linearLayout.addView(innerLayout);
        return linearLayout;
    }

    public static boolean isServiceStarted(Context context, String PackageName) {
        try {
            for (RunningServiceInfo amService : ((ActivityManager) context.getSystemService("activity")).getRunningServices(1000)) {
                if (amService.service.getPackageName().compareTo(PackageName) == 0) {
                    return true;
                }
            }
            return false;
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void openCLD(String packageName, Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = packageManager.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
        }
        Intent resolveIntent = new Intent("android.intent.action.MAIN", null);
        resolveIntent.addCategory("android.intent.category.LAUNCHER");
        resolveIntent.setPackage(pi.packageName);
        ResolveInfo ri = (ResolveInfo) packageManager.queryIntentActivities(resolveIntent, 0).iterator().next();
        if (ri != null) {
            String className = ri.activityInfo.name;
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName(packageName, className));
            context.startActivity(intent);
        }
    }

    public String isAppOnBefore() {
        String className = XmlPullParser.NO_NAMESPACE;
        List<RunningAppProcessInfo> appProcesses = ((ActivityManager) getApplicationContext().getSystemService("activity")).getRunningAppProcesses();
        if (appProcesses == null) {
            return className;
        }
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals("com.cnlaunch.x431frame") && appProcess.importance == 100) {
                String activityName = getActivityName(appProcess.getClass().getName().toString());
                if (!activityName.equalsIgnoreCase("NotificationActivity")) {
                    return activityName;
                }
            }
        }
        return className;
    }

    private String getActivityName(String names) {
        String[] name = names.split("\\.");
        return name[name.length - 1];
    }

    public static ArrayList<SptTroubleTest> getTroubleList(String json) {
        ArrayList<SptTroubleTest> list = new ArrayList();
        if (json == null || json.equals(XmlPullParser.NO_NAMESPACE) || json.equals("null")) {
            return null;
        }
        if (!json.subSequence(0, 1).equals("[")) {
            return null;
        }
        if (json.length() > 2) {
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    SptTroubleTest data = new SptTroubleTest();
                    data.setTroubleCodeContent(item.getString("DTCValue"));
                    data.setTroubleDescribeContent(item.getString("DTCDescription"));
                    data.setTroubleStateContent(item.getString("DTCStatus"));
                    list.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        } else if (json.length() <= 2) {
            return null;
        } else {
            return list;
        }
    }
}
