package com.ifoer.expeditionphone;

import CRP.utils.CRPTools;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.framework.common.KeyConstant;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.module.config.ConfigDBManager;
import com.cnlaunch.x431pro.module.upgrade.action.UpgradeAction;
import com.cnlaunch.x431pro.module.upgrade.model.LatestDiagSoftsResponse;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.adapter.KeyToUpgradeAdapter;
import com.ifoer.adapter.KeyToUpgradeAdapter.ViewHolder;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.X431PadSoftDTO;
import com.ifoer.ui.MainMenuActivity;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.ApkDownLoad;
import com.ifoer.util.Common;
import com.ifoer.util.GetPublicSofl;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.webservice.X431PadDiagSoftService;
import com.ifoer.webservice.X431PadDiagSoftServiceCodes;
import com.launch.service.BundleBuilder;
import com.thoughtworks.xstream.XStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class KeyToUpgradeActivity extends Activity {
    private static int nowSoftPosition;
    private static String versionData;
    private final int REQ_QUERYHISTORYDIAGSOFTS_CODE;
    private final int REQ_QUERYLATESTDIAGSOFTS_CODE;
    private final int REQ_QUERYLATESTPUBLICSOFTS_CODE;
    private KeyToUpgradeAdapter adapter;
    public X431PadDtoSoft apkDto;
    private Button back;
    public CheckBox checkBoxs;
    private Context context;
    private String diagsoftUrl;
    public X431PadDtoSoft firmDto;
    private boolean hasRequest;
    private boolean isLock;
    public Map<Integer, Boolean> isSelected;
    private int listposition;
    private final Handler mHandler;
    public IntentFilter myIntentFilter;
    private boolean needExist;
    private TextView newVersion;
    private Button next;
    private ProgressDialog progressDialogs;
    private String publicsoftUrl;
    public mBroadcastReceiver receiver;
    private List<X431PadDtoSoft> resultList;
    private UpgradeAction upgradeAction;
    private ListView upgradeListview;
    private List<X431PadSoftDTO> versionSelectedList;
    private List<X431PadDtoSoft> x431DiagSoftList;
    private List<X431PadDtoSoft> x431PublicSoftList;

    /* renamed from: com.ifoer.expeditionphone.KeyToUpgradeActivity.1 */
    class C05791 extends Handler {
        C05791() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (KeyToUpgradeActivity.this.progressDialogs != null && KeyToUpgradeActivity.this.progressDialogs.isShowing()) {
                        KeyToUpgradeActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(KeyToUpgradeActivity.this, C0136R.string.timeout, 0).show();
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    if (KeyToUpgradeActivity.this.progressDialogs != null && KeyToUpgradeActivity.this.progressDialogs.isShowing()) {
                        KeyToUpgradeActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(KeyToUpgradeActivity.this, C0136R.string.get_data_fail, 1).show();
                    KeyToUpgradeActivity.this.finish();
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    if (KeyToUpgradeActivity.this.progressDialogs != null && KeyToUpgradeActivity.this.progressDialogs.isShowing()) {
                        KeyToUpgradeActivity.this.progressDialogs.dismiss();
                    }
                    SimpleDialog.ToastToLogin2(KeyToUpgradeActivity.this.context, KeyToUpgradeActivity.this.mHandler, KeyToUpgradeActivity.this.progressDialogs);
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    Toast.makeText(KeyToUpgradeActivity.this, KeyToUpgradeActivity.this.getString(C0136R.string.check_file), 0).show();
                    KeyToUpgradeActivity.this.finish();
                case Constant.TIME_OUT /*800*/:
                    if (KeyToUpgradeActivity.this.progressDialogs != null && KeyToUpgradeActivity.this.progressDialogs.isShowing()) {
                        KeyToUpgradeActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(KeyToUpgradeActivity.this.context, C0136R.string.timeout, 1).show();
                    KeyToUpgradeActivity.this.finish();
                case Constant.SETVICE_ABNORMAL /*801*/:
                    if (KeyToUpgradeActivity.this.progressDialogs != null && KeyToUpgradeActivity.this.progressDialogs.isShowing()) {
                        KeyToUpgradeActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(KeyToUpgradeActivity.this.context, C0136R.string.the_service_side_abnormal, 1).show();
                    KeyToUpgradeActivity.this.finish();
                case Constant.LOGIN_SUCCESS /*802*/:
                    if (Common.isNetworkAvailable(KeyToUpgradeActivity.this.context)) {
                        KeyToUpgradeActivity.this.hasRequest = true;
                        new GetPublicSofl(KeyToUpgradeActivity.this.context, KeyToUpgradeActivity.this.progressDialogs, KeyToUpgradeActivity.this.mHandler).execute(new String[0]);
                        return;
                    }
                    Toast.makeText(KeyToUpgradeActivity.this.context, C0136R.string.network, 0).show();
                case Constant.ERROR_NETWORK /*803*/:
                    Toast.makeText(KeyToUpgradeActivity.this.context, C0136R.string.ERROR_NETWORK, 1).show();
                    KeyToUpgradeActivity.this.finish();
                case Constant.ERROR_SERVER /*804*/:
                    if (msg.getData() == null || msg.getData().getString(BundleBuilder.AskFromMessage) == null || msg.getData().getString(BundleBuilder.AskFromMessage).equals(XmlPullParser.NO_NAMESPACE)) {
                        Toast.makeText(KeyToUpgradeActivity.this.context, KeyToUpgradeActivity.this.context.getString(C0136R.string.ERROR_SERVER), 1).show();
                    } else {
                        Toast.makeText(KeyToUpgradeActivity.this.context, msg.getData().getString(BundleBuilder.AskFromMessage), 0).show();
                    }
                    KeyToUpgradeActivity.this.finish();
                case Constant.ERROR_CODE /*805*/:
                    if (msg.getData() == null || msg.getData().getString(BundleBuilder.AskFromMessage) == null || msg.getData().getString(BundleBuilder.AskFromMessage).equals(XmlPullParser.NO_NAMESPACE)) {
                        Toast.makeText(KeyToUpgradeActivity.this.context, KeyToUpgradeActivity.this.getString(C0136R.string.error_server), 0).show();
                    } else {
                        Toast.makeText(KeyToUpgradeActivity.this.context, msg.getData().getString(BundleBuilder.AskFromMessage), 0).show();
                    }
                    KeyToUpgradeActivity.this.finish();
                case Constant.ERROR_GET_DATA_FAILED /*806*/:
                    if (msg.getData() == null || msg.getData().getString(BundleBuilder.AskFromMessage) == null || msg.getData().getString(BundleBuilder.AskFromMessage).equals(XmlPullParser.NO_NAMESPACE)) {
                        Toast.makeText(KeyToUpgradeActivity.this.context, KeyToUpgradeActivity.this.context.getResources().getString(C0136R.string.get_data_fail), 0).show();
                    } else {
                        Toast.makeText(KeyToUpgradeActivity.this.context, msg.getData().getString(BundleBuilder.AskFromMessage), 0).show();
                    }
                    KeyToUpgradeActivity.this.finish();
                case Constant.CHECK_UPDATE_SUCCESS /*807*/:
                    new GetPublicSofl(KeyToUpgradeActivity.this.context, KeyToUpgradeActivity.this.progressDialogs, KeyToUpgradeActivity.this.mHandler).execute(new String[0]);
                case Constant.CHARGE_SECCUSS /*808*/:
                    SimpleDialog.chargeSuccess(KeyToUpgradeActivity.this.context, KeyToUpgradeActivity.this.progressDialogs, KeyToUpgradeActivity.this.mHandler, msg.obj, true);
                case Constant.NEED_BUY /*809*/:
                    SimpleDialog.needBuy(KeyToUpgradeActivity.this.context, KeyToUpgradeActivity.this.progressDialogs, KeyToUpgradeActivity.this.mHandler);
                case Constant.GET_APK_VERSION /*912*/:
                    new LoadSoftware().execute(new String[0]);
                case ApkDownLoad.DOWNlOAD_APK_BEGIN /*913*/:
                    new LoadSoftware().execute(new String[0]);
                case 1011:
                    if (KeyToUpgradeActivity.this.isLock) {
                        KeyToUpgradeActivity.this.needExist = true;
                    } else {
                        KeyToUpgradeActivity.this.needExist = false;
                    }
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.KeyToUpgradeActivity.2 */
    class C05802 implements OnItemClickListener {
        C05802() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            ViewHolder item = (ViewHolder) arg1.getTag();
            if (((Boolean) KeyToUpgradeActivity.this.isSelected.get(Integer.valueOf(arg2))).booleanValue() || item.needRemve) {
                KeyToUpgradeActivity.this.isSelected.put(Integer.valueOf(arg2), Boolean.valueOf(false));
                item.checkbox.setChecked(false);
                return;
            }
            KeyToUpgradeActivity.this.isSelected.put(Integer.valueOf(arg2), Boolean.valueOf(true));
            item.checkbox.setChecked(true);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.KeyToUpgradeActivity.3 */
    class C05813 implements OnClickListener {
        C05813() {
        }

        public void onClick(View v) {
            int i;
            ArrayList<Integer> list = new ArrayList();
            for (i = 0; i < KeyToUpgradeActivity.this.upgradeListview.getCount(); i++) {
                if (((Boolean) KeyToUpgradeActivity.this.isSelected.get(Integer.valueOf(i))).booleanValue()) {
                    list.add(Integer.valueOf(i));
                }
            }
            if (list.size() > 0) {
                ArrayList<X431PadDtoSoft> downloadList = new ArrayList();
                for (i = 0; i < list.size(); i++) {
                    downloadList.add((X431PadDtoSoft) KeyToUpgradeActivity.this.adapter.getItem(((Integer) list.get(i)).intValue()));
                }
                Intent intent = new Intent(KeyToUpgradeActivity.this, DownloadAllSoftwareActivity.class);
                intent.putExtra("downloadList", downloadList);
                KeyToUpgradeActivity.this.startActivityForResult(intent, 12);
                return;
            }
            Toast.makeText(KeyToUpgradeActivity.this, C0136R.string.shopping_please, 0).show();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.KeyToUpgradeActivity.4 */
    class C05824 implements OnClickListener {
        C05824() {
        }

        public void onClick(View v) {
            KeyToUpgradeActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.KeyToUpgradeActivity.5 */
    class C05835 implements OnCheckedChangeListener {
        C05835() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (KeyToUpgradeActivity.this.resultList != null) {
                int i;
                if (isChecked) {
                    if (KeyToUpgradeActivity.this.upgradeListview.getCount() > 0) {
                        i = 0;
                        while (i < KeyToUpgradeActivity.this.upgradeListview.getCount()) {
                            if (!Constant.upgradeAPk && i == 0) {
                                KeyToUpgradeActivity.this.isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
                                Log.i("KeyToUpgraderActivity", "apk \u4e0d\u9700\u8981\u5347\u7ea7");
                            } else if (Constant.upgradeFirm || i != 1) {
                                KeyToUpgradeActivity.this.isSelected.put(Integer.valueOf(i), Boolean.valueOf(true));
                            } else {
                                KeyToUpgradeActivity.this.isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
                                Log.i("KeyToUpgradeActivity", "firm \u4e0d\u9700\u8981\u5347\u7ea7");
                            }
                            i++;
                        }
                    }
                } else if (KeyToUpgradeActivity.this.upgradeListview.getCount() > 0) {
                    for (i = 0; i < KeyToUpgradeActivity.this.upgradeListview.getCount(); i++) {
                        KeyToUpgradeActivity.this.isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
                    }
                }
                if (KeyToUpgradeActivity.this.adapter != null) {
                    KeyToUpgradeActivity.this.adapter.notifyDataSetChanged();
                }
            }
        }
    }

    class LoadSoftware extends AsyncTask<String, String, LatestDiagSoftsResponse> {
        LoadSoftware() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            KeyToUpgradeActivity.this.progressDialogs = new ProgressDialog(KeyToUpgradeActivity.this);
            KeyToUpgradeActivity.this.progressDialogs.setMessage(KeyToUpgradeActivity.this.getResources().getString(C0136R.string.shopping_wait));
            KeyToUpgradeActivity.this.progressDialogs.setCancelable(false);
            KeyToUpgradeActivity.this.progressDialogs.show();
        }

        protected LatestDiagSoftsResponse doInBackground(String... params) {
            if (!DBDao.getInstance(KeyToUpgradeActivity.this.context).isExistSoftId(MainActivity.database)) {
                DBDao.getInstance(KeyToUpgradeActivity.this.context).updateUpgrade(MainActivity.database);
            }
            try {
                KeyToUpgradeActivity.this.diagsoftUrl = ConfigDBManager.getInstance(KeyToUpgradeActivity.this.context).getUrlByKey(KeyConstant.diagsoft_download_ex);
            } catch (HttpException e2) {
                e2.printStackTrace();
            }
            X431PadDiagSoftService service = new X431PadDiagSoftService();
            String cc = MySharedPreferences.getStringValue(KeyToUpgradeActivity.this, MySharedPreferences.CCKey);
            String token = MySharedPreferences.getStringValue(KeyToUpgradeActivity.this, MySharedPreferences.TokenKey);
            String serialNo = CRPTools.getSnNo(KeyToUpgradeActivity.this.context);
            if (serialNo.equals(XmlPullParser.NO_NAMESPACE)) {
                KeyToUpgradeActivity.this.mHandler.sendEmptyMessage(3);
            }
            if (MainActivity.country == null || MainActivity.country.length() <= 0) {
                MainActivity.country = Locale.getDefault().getCountry();
            }
            int lanId = AndroidToLan.getLanId(MainActivity.country);
            LatestDiagSoftsResponse result = null;
            try {
                KeyToUpgradeActivity.this.diagsoftUrl = ConfigDBManager.getInstance(KeyToUpgradeActivity.this.context).getUrlByKey(KeyConstant.diagsoft_download_ex);
            } catch (HttpException e22) {
                e22.printStackTrace();
            }
            if (cc == null || cc.equals(XmlPullParser.NO_NAMESPACE)) {
                KeyToUpgradeActivity.this.mHandler.obtainMessage(2).sendToTarget();
                KeyToUpgradeActivity.this.hasRequest = false;
            } else {
                try {
                    KeyToUpgradeActivity.this.hasRequest = true;
                    result = KeyToUpgradeActivity.this.upgradeAction.queryLatestDiagSofts(cc, serialNo, new StringBuilder(String.valueOf(lanId)).toString(), new StringBuilder(String.valueOf(XStream.NO_REFERENCES)).toString());
                } catch (NullPointerException e) {
                    KeyToUpgradeActivity.this.mHandler.obtainMessage(1).sendToTarget();
                } catch (Exception e3) {
                }
            }
            return result;
        }

        protected void onPostExecute(LatestDiagSoftsResponse result) {
            super.onPostExecute(result);
            Intent intents;
            if (result != null) {
                if (KeyToUpgradeActivity.this.progressDialogs != null && KeyToUpgradeActivity.this.progressDialogs.isShowing()) {
                    KeyToUpgradeActivity.this.progressDialogs.dismiss();
                }
                if (result.getCode() == -1) {
                    SimpleDialog.validTokenDialog(KeyToUpgradeActivity.this);
                } else if (result.getCode() == 0) {
                    KeyToUpgradeActivity.this.isSelected.put(Integer.valueOf(0), Boolean.valueOf(false));
                    KeyToUpgradeActivity.this.isSelected.put(Integer.valueOf(1), Boolean.valueOf(false));
                    if (result != null && result.getX431PadSoftList().size() > 0) {
                        for (int i = 2; i < result.getX431PadSoftList().size() + 2; i++) {
                            KeyToUpgradeActivity.this.isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
                        }
                        String serialNo = MySharedPreferences.getStringValue(KeyToUpgradeActivity.this.context, MySharedPreferences.serialNoKey);
                        String lanName = AndroidToLan.toLan(MainMenuActivity.country);
                        KeyToUpgradeActivity.this.resultList.add(KeyToUpgradeActivity.this.getApkDto());
                        KeyToUpgradeActivity.this.resultList.add(KeyToUpgradeActivity.this.getFirmDto());
                        for (X431PadDtoSoft dto : result.getX431PadSoftList()) {
                            dto.setUrl(KeyToUpgradeActivity.this.diagsoftUrl);
                            String name = dto.getSoftName().replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE);
                            if (name.equalsIgnoreCase("EOBD")) {
                                name = "EOBD2";
                            }
                            if (name.equalsIgnoreCase("ISUZU(Japan)")) {
                                name = "JPISUZU";
                            }
                            if (name.equalsIgnoreCase("AUSTFORD")) {
                                name = "FORD";
                            }
                            String maxOldVersion2 = DBDao.getInstance(KeyToUpgradeActivity.this.context).getMaxVersion(serialNo, name, lanName, MainActivity.database);
                            if (maxOldVersion2 != null) {
                                double maxOld = Double.parseDouble(maxOldVersion2.split("V")[1]);
                                dto.setMaxOldVersion(maxOldVersion2);
                            }
                            double version = Double.parseDouble(dto.getVersionNo().split("V")[1]);
                            KeyToUpgradeActivity.this.resultList.add(dto);
                        }
                        if (KeyToUpgradeActivity.this.resultList.size() > 0) {
                            KeyToUpgradeActivity.this.next.setVisibility(0);
                            KeyToUpgradeActivity keyToUpgradeActivity = KeyToUpgradeActivity.this;
                            KeyToUpgradeActivity.this.adapter = new KeyToUpgradeAdapter(KeyToUpgradeActivity.this.resultList, r0.context, KeyToUpgradeActivity.this.isSelected, KeyToUpgradeActivity.this.mHandler);
                            KeyToUpgradeActivity.this.upgradeListview.setAdapter(KeyToUpgradeActivity.this.adapter);
                            intents = new Intent("Show_new_action");
                            intents.putExtra("show_new", true);
                            KeyToUpgradeActivity.this.context.sendBroadcast(intents);
                            return;
                        }
                        intents = new Intent("Show_new_action");
                        intents.putExtra("show_new", false);
                        KeyToUpgradeActivity.this.context.sendBroadcast(intents);
                        Toast.makeText(KeyToUpgradeActivity.this, C0136R.string.no_new_software, 0).show();
                    } else if (result != null && result.getCode() == 0 && result.getX431PadSoftList().size() == 0) {
                        Toast.makeText(KeyToUpgradeActivity.this, C0136R.string.ERROR_NETWORK, 0).show();
                        KeyToUpgradeActivity.this.finish();
                    }
                } else {
                    intents = new Intent("Show_new_action");
                    intents.putExtra("show_new", false);
                    KeyToUpgradeActivity.this.context.sendBroadcast(intents);
                    String message = new X431PadDiagSoftServiceCodes().queryLatestDiagSoftsCode(result.getCode(), KeyToUpgradeActivity.this);
                    if (!message.equals(XmlPullParser.NO_NAMESPACE)) {
                        if (message.equalsIgnoreCase("No Software in this Language")) {
                            Toast.makeText(KeyToUpgradeActivity.this, KeyToUpgradeActivity.this.getString(C0136R.string.check_serialNo2), 1).show();
                        } else {
                            Toast.makeText(KeyToUpgradeActivity.this, message, 1).show();
                        }
                    }
                    KeyToUpgradeActivity.this.finish();
                }
            } else if (KeyToUpgradeActivity.this.hasRequest) {
                if (KeyToUpgradeActivity.this.progressDialogs != null && KeyToUpgradeActivity.this.progressDialogs.isShowing()) {
                    KeyToUpgradeActivity.this.progressDialogs.dismiss();
                }
                Toast.makeText(KeyToUpgradeActivity.this.context, KeyToUpgradeActivity.this.context.getString(C0136R.string.ERROR_NETWORK), 1).show();
                intents = new Intent("Show_new_action");
                intents.putExtra("show_new", false);
                KeyToUpgradeActivity.this.context.sendBroadcast(intents);
                KeyToUpgradeActivity.this.finish();
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                KeyToUpgradeActivity.this.isLock = false;
                if (KeyToUpgradeActivity.this.needExist) {
                    SimpleDialog.ExitDialog(context);
                }
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                KeyToUpgradeActivity.this.isLock = true;
                KeyToUpgradeActivity.this.mHandler.sendEmptyMessageDelayed(1011, 30000);
            }
        }
    }

    public KeyToUpgradeActivity() {
        this.listposition = 0;
        this.isLock = false;
        this.needExist = false;
        this.resultList = new ArrayList();
        this.isSelected = new HashMap();
        this.versionSelectedList = new ArrayList();
        this.hasRequest = true;
        this.REQ_QUERYLATESTPUBLICSOFTS_CODE = 2101;
        this.REQ_QUERYLATESTDIAGSOFTS_CODE = 2102;
        this.REQ_QUERYHISTORYDIAGSOFTS_CODE = 2103;
        this.x431PublicSoftList = new ArrayList();
        this.x431DiagSoftList = new ArrayList();
        this.mHandler = new C05791();
    }

    static {
        versionData = XmlPullParser.NO_NAMESPACE;
        nowSoftPosition = 0;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.key_to_upgrade);
        MyApplication.getInstance().addActivity(this);
        this.context = this;
        this.upgradeAction = new UpgradeAction(this.context);
        this.upgradeListview = (ListView) findViewById(C0136R.id.upgradeListview);
        this.newVersion = (TextView) findViewById(C0136R.id.newVersion);
        this.upgradeListview.setOnItemClickListener(new C05802());
        this.next = (Button) findViewById(C0136R.id.next);
        this.next.setVisibility(4);
        this.next.setOnClickListener(new C05813());
        this.back = (Button) findViewById(C0136R.id.back);
        this.back.setOnClickListener(new C05824());
        this.checkBoxs = (CheckBox) findViewById(C0136R.id.software_checkbox);
        this.checkBoxs.setOnCheckedChangeListener(new C05835());
        if (!Common.isNetworkAvailable(this)) {
            Toast.makeText(this, C0136R.string.network, 0).show();
        } else if (TextUtils.isEmpty(MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey))) {
            SimpleDialog.ToastToLogin2(this.context, this.mHandler, this.progressDialogs);
        } else {
            new GetPublicSofl(this, this.progressDialogs, this.mHandler).execute(new String[0]);
        }
        registerBoradcastReceiver();
    }

    public List<X431PadDtoSoft> getX431PublicSoftList() {
        return this.x431PublicSoftList;
    }

    public void setX431PublicSoftList(List<X431PadDtoSoft> x431PublicSoftList) {
        this.x431PublicSoftList = x431PublicSoftList;
    }

    public List<X431PadDtoSoft> getX431DiagSoftList() {
        return this.x431DiagSoftList;
    }

    public void setX431DiagSoftList(List<X431PadDtoSoft> x431DiagSoftList) {
        this.x431DiagSoftList = x431DiagSoftList;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0136R.menu.main, menu);
        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onActivityResult(int requestCode, int reSultCode, Intent data) {
        if (requestCode == 11) {
            if (reSultCode != 10 && reSultCode != 13) {
                finish();
            } else if (Common.isNetworkAvailable(this)) {
                this.hasRequest = true;
                new GetPublicSofl(this, this.progressDialogs, this.mHandler).execute(new String[0]);
            } else {
                Toast.makeText(this, C0136R.string.network, 0).show();
            }
        }
        if (requestCode != 12) {
            return;
        }
        if (reSultCode == 10) {
            this.checkBoxs.setChecked(false);
            if (Common.isNetworkAvailable(this)) {
                this.resultList.clear();
                if (this.adapter != null) {
                    this.adapter.notifyDataSetChanged();
                }
                new GetPublicSofl(this, this.progressDialogs, this.mHandler).execute(new String[0]);
                return;
            }
            Toast.makeText(this, C0136R.string.network, 0).show();
            return;
        }
        finish();
    }

    public TextView getView() {
        return this.newVersion;
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

    public X431PadDtoSoft getFirmDto() {
        return this.firmDto;
    }

    public void setFirmDto(X431PadDtoSoft firmDto) {
        this.firmDto = firmDto;
    }

    public X431PadDtoSoft getApkDto() {
        return this.apkDto;
    }

    public void setApkDto(X431PadDtoSoft apkDto) {
        this.apkDto = apkDto;
    }
}
