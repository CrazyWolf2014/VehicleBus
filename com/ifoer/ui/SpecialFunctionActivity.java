package com.ifoer.ui;

import CRP.serialport.SerialPortManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.ifoer.adapter.BaseDiagAdapter;
import com.ifoer.adapter.SpecialFunctionActivityAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.dbentity.CarVersionInfo;
import com.ifoer.entity.Constant;
import com.ifoer.expedition.BluetoothChat.BluetoothChatService;
import com.ifoer.expedition.client.NotificationService;
import com.ifoer.expeditionphone.BaseActivity;
import com.ifoer.expeditionphone.LoadDynamicLibsActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.freememory.FreeMemory;
import com.ifoer.mine.Contact;
import com.ifoer.serialport.SerialPort;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.view.WorkSpace;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class SpecialFunctionActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    public static String country;
    public static SQLiteDatabase database;
    private GridView SpecialGridView;
    private String cc;
    private Context contexts;
    private boolean hasLoadLib;
    private boolean isExecuteM;
    private String lanName;
    private String language;
    private long lastTime;
    public Handler mHandler;
    private SpecialFunctionActivityAdapter mSpecilaBaseDiagAdapeter;
    private WorkSpace mWorkSpace;
    public IntentFilter myIntentFilter;
    private String packgeId;
    public mBroadcastReceiver receiver;
    private String serialNo;
    private SerialPort serialPort;
    private List<HashMap<String, Object>> specialList;

    /* renamed from: com.ifoer.ui.SpecialFunctionActivity.1 */
    class C07291 extends Handler {
        C07291() {
        }

        public void handleMessage(Message message) {
            if (message.what == 18) {
                if (SpecialFunctionActivity.this.mSpecilaBaseDiagAdapeter == null) {
                    SpecialFunctionActivity.this.mSpecilaBaseDiagAdapeter = SpecialFunctionActivity.this.createBaseDiagAdapter(SpecialFunctionActivity.this.specialList);
                    SpecialFunctionActivity.this.SpecialGridView.setAdapter(SpecialFunctionActivity.this.mSpecilaBaseDiagAdapeter);
                } else {
                    SpecialFunctionActivity.this.mSpecilaBaseDiagAdapeter.refresh(SpecialFunctionActivity.this.specialList);
                }
            }
            if (message.what == 4) {
                SpecialFunctionActivity.this.sendBroadcast(new Intent("Connected"));
            }
        }
    }

    class GetSpecial extends Thread {
        GetSpecial() {
        }

        public void run() {
            boolean isopen;
            String serialNo = MySharedPreferences.getStringValue(SpecialFunctionActivity.this, MySharedPreferences.serialNoKey);
            String ifshow = MySharedPreferences.getStringValue(SpecialFunctionActivity.this, MySharedPreferences.IfShow);
            if (ifshow == null || !ifshow.equals(Contact.RELATION_FRIEND)) {
                isopen = false;
            } else {
                isopen = true;
            }
            SpecialFunctionActivity.this.specialList = SpecialFunctionActivity.this.GetSpecialList(serialNo, Boolean.valueOf(isopen), XmlPullParser.NO_NAMESPACE);
            SpecialFunctionActivity.this.mHandler.obtainMessage(18).sendToTarget();
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - SpecialFunctionActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
                if (SpecialFunctionActivity.this.hasLoadLib) {
                    MyApplication.getInstance().exitActivity("com.ifoer.expeditionphone.LoadDynamicLibsActivity");
                }
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                SpecialFunctionActivity.this.lastTime = System.currentTimeMillis();
            }
            if (SpecialFunctionActivity.this.isExecuteM && intent.getAction().equals("Connected")) {
                SpecialFunctionActivity.this.loadDymicLid(SpecialFunctionActivity.this.serialNo, SpecialFunctionActivity.this.packgeId);
            }
        }
    }

    public SpecialFunctionActivity() {
        this.mSpecilaBaseDiagAdapeter = null;
        this.specialList = new ArrayList();
        this.hasLoadLib = false;
        this.mHandler = new C07291();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.specia_function2);
        this.contexts = this;
        if (MainActivity.serialPort != null) {
            this.serialPort = MainActivity.serialPort;
        } else {
            this.serialPort = new SerialPortManager().getSerialPort();
        }
        mainView();
        registerBoradcastReceiver();
    }

    private void mainView() {
        if (MainMenuActivity.database != null) {
            database = MainMenuActivity.database;
        } else {
            database = DBDao.getInstance(this).getConnection();
        }
        Locale locale = Locale.getDefault();
        country = MainMenuActivity.country;
        if (country == null) {
            country = locale.getCountry();
        }
        this.lanName = AndroidToLan.toLan(country);
        this.language = locale.getLanguage();
        Constant.language = locale.toString();
        this.SpecialGridView = (GridView) findViewById(C0136R.id.main_WorkSpace);
        this.SpecialGridView.setHorizontalSpacing(15);
        this.SpecialGridView.setVerticalSpacing(15);
        this.SpecialGridView.setSelector(C0136R.color.transparent);
        this.SpecialGridView.setNumColumns(3);
        this.mSpecilaBaseDiagAdapeter = createBaseDiagAdapter(this.specialList);
        this.SpecialGridView.setAdapter(this.mSpecilaBaseDiagAdapeter);
        this.SpecialGridView.setOnItemClickListener(this);
        new GetSpecial().start();
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        BaseDiagAdapter baseDiagAdapter = (BaseDiagAdapter) arg0.getAdapter();
        baseDiagAdapter.setSelectItem(arg2);
        baseDiagAdapter.notifyDataSetChanged();
        this.cc = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.CCKey);
        if (Environment.getExternalStorageState().equals("mounted")) {
            HashMap<String, Object> map = (HashMap) arg0.getItemAtPosition(arg2);
            String softPackageId = map.get("softPackageId").toString();
            String softId = map.get("softId").toString();
            String icon = map.get("icon").toString();
            String tempSoftPackage = softPackageId;
            tempSoftPackage = softPackageId.replaceAll("EOBD2", "EOBD");
            MySharedPreferences.setString(this.contexts, MySharedPreferences.savesoftPackageId, softPackageId);
            String serialNo1 = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.serialNoKey);
            MySharedPreferences.setString(this.contexts, MySharedPreferences.generateOperatingRecord, Contact.RELATION_ASK);
            if (serialNo1 != null && !XmlPullParser.NO_NAMESPACE.equals(serialNo1)) {
                Log.i("MainActivity", "\u68c0\u67e5\u5e8f\u5217\u53f7" + serialNo1);
                if (country == null || country.length() <= 0) {
                    country = Locale.getDefault().getCountry();
                }
                country = Locale.getDefault().getCountry();
                if (DBDao.getInstance(this).isDownload(softPackageId, serialNo1, AndroidToLan.toLan(country), database) && this.serialPort != null) {
                    openConnect(serialNo1, softPackageId);
                    return;
                }
                return;
            }
            return;
        }
        Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
    }

    public void onClick(View arg0) {
    }

    public synchronized List<HashMap<String, Object>> GetSpecialList(String serialNo, Boolean isopen, String carTypeSearch) {
        List<HashMap<String, Object>> list;
        String quaryLanName;
        List<HashMap<String, Object>> carAdds = new ArrayList();
        List<HashMap<String, Object>> listImage = new ArrayList();
        if (this.lanName == "HK" || this.lanName == "TW") {
            quaryLanName = Constants.DEFAULT_LANGUAGE;
        } else {
            quaryLanName = this.lanName;
        }
        this.specialList = DBDao.getInstance(this).query(Contact.RELATION_AGREE, serialNo, quaryLanName, database);
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
            list = this.specialList;
        } else {
            if (carTypeSearch != null) {
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
                    list = this.specialList;
                }
            }
            list = this.specialList;
        }
        return list;
    }

    public SpecialFunctionActivityAdapter createBaseDiagAdapter(List<HashMap<String, Object>> carList) {
        return new SpecialFunctionActivityAdapter(this, carList, this.language);
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        this.myIntentFilter.addAction("Connected");
        this.myIntentFilter.addAction("refreshUi");
        registerReceiver(this.receiver, this.myIntentFilter);
    }

    public void openConnect(String serialNo1, String softPackageId) {
        this.serialNo = serialNo1;
        this.packgeId = softPackageId;
        if (Constant.mChatService.getState() == 3) {
            loadDymicLid(serialNo1, softPackageId);
        } else {
            Constant.mChatService.connect(this.serialPort, true);
        }
    }

    public void loadDymicLid(String serialNo1, String softPackageId) {
        ArrayList<CarVersionInfo> list;
        String tempLanName = Locale.getDefault().getCountry().toUpperCase();
        if (tempLanName.equals(Constants.DEFAULT_LANGUAGE) || tempLanName.equals("ZH") || tempLanName.equals("CN") || tempLanName.equals("TW") || tempLanName.equals("HK")) {
            tempLanName = this.lanName;
            list = DBDao.getInstance(this.contexts).queryCarVersion(softPackageId, this.lanName, serialNo1, database);
            if (list.size() <= 0 && (tempLanName.equals("TW") || tempLanName.equals("HK"))) {
                country = Constants.DEFAULT_LANGUAGE;
                list = DBDao.getInstance(this.contexts).queryCarVersion(softPackageId, Constants.DEFAULT_LANGUAGE, serialNo1, database);
            }
        } else {
            list = DBDao.getInstance(this.contexts).queryCarVersion(softPackageId, tempLanName, serialNo1, database);
            if (list.size() <= 0) {
                country = Constants.DEFAULT_LANGUAGE;
                list = DBDao.getInstance(this.contexts).queryCarVersion(softPackageId, Constants.DEFAULT_LANGUAGE, serialNo1, database);
            }
        }
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String carId = ((CarVersionInfo) list.get(i)).getCarId();
                String lanName = ((CarVersionInfo) list.get(i)).getLanName();
                String versionDir = ((CarVersionInfo) list.get(i)).getVersionDir();
                String serialNo = ((CarVersionInfo) list.get(i)).getVersionDir();
                ((CarVersionInfo) list.get(i)).getSoftId();
            }
            Intent intent = new Intent(this.contexts, LoadDynamicLibsActivity.class);
            intent.putExtra("list", list);
            intent.putExtra("carId", ((CarVersionInfo) list.get(0)).getCarId());
            intent.putExtra("lanName", ((CarVersionInfo) list.get(0)).getLanName());
            startActivity(intent);
            this.hasLoadLib = true;
            overridePendingTransition(0, 0);
            return;
        }
        Toast.makeText(this.contexts, getResources().getString(C0136R.string.main_lanuage_null), 0).show();
    }

    private void setupChat() {
        Constant.mChatService = new BluetoothChatService(this, this.mHandler, true);
    }

    public void onStart() {
        super.onStart();
        if (Constant.exsitApp) {
            finish();
        }
        if (this.serialPort != null && Constant.mChatService == null) {
            setupChat();
        }
    }

    public synchronized void onResume() {
        super.onResume();
        FreeMemory.getInstance(this).freeMemory();
        this.isExecuteM = true;
        this.cc = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.CCKey);
        if (Constant.mChatService != null && Constant.mChatService.getState() == 0) {
            Constant.mChatService.start();
        }
    }

    public void sendMsg() {
        sendBroadcast(new Intent("Show_names"));
    }

    public synchronized void onPause() {
        super.onPause();
        this.isExecuteM = true;
    }

    public void onStop() {
        super.onStop();
        this.isExecuteM = false;
        this.mSpecilaBaseDiagAdapeter = null;
    }

    public void onDestroy() {
        super.onDestroy();
        NotificationService.startActivity = Boolean.valueOf(false);
        try {
            if (this.receiver != null) {
                unregisterReceiver(this.receiver);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (Constant.mChatService != null) {
            Constant.mChatService.stop();
            this.serialPort.close();
        }
        this.mSpecilaBaseDiagAdapeter = null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finish();
            return true;
        } else if (keyCode == Service.CISCO_TNA) {
            finish();
            return true;
        } else if (keyCode == Service.SUNRPC) {
            finish();
            return true;
        } else if (keyCode != Service.CISCO_SYS) {
            return super.onKeyDown(keyCode, event);
        } else {
            return true;
        }
    }
}
