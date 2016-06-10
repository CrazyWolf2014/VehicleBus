package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.car.result.DiagSoftIdListResult;
import com.car.result.ProductDTOResult;
import com.car.result.X431PadSoftListResult;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.example.gpiomanager.MainActivity;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.adapter.LoadDynamicAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.db.SaveSoftId;
import com.ifoer.dbentity.CarVersionInfo;
import com.ifoer.entity.Constant;
import com.ifoer.entity.DiagSoftIdDTO;
import com.ifoer.entity.OperatingRecordInfo;
import com.ifoer.entity.ProductDTO;
import com.ifoer.entity.X431PadSoftDTO;
import com.ifoer.expedition.BluetoothChat.CarDiagnoseActivity;
import com.ifoer.expeditionphone.inteface.ILoadDynamicLibsActivityInterface;
import com.ifoer.freememory.FreeMemory;
import com.ifoer.mine.Contact;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.Common;
import com.ifoer.util.Copy_File;
import com.ifoer.util.DialogUtil;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.Tools;
import com.ifoer.webservice.ProductService;
import com.ifoer.webservice.X431PadDiagSoftService;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class LoadDynamicLibsActivity extends Activity implements ILoadDynamicLibsActivityInterface {
    private static final boolean f2127D = true;
    public static final String DEVICE_NAME = "/dev/mtgpio";
    public static boolean HardFlag = false;
    public static final String TAG = "LoadDynamicLibsActivity";
    private LoadDynamicAdapter adapter;
    private String carId;
    private Button checkLatestVersion;
    private Context context;
    private String dataDir;
    public String delVersion;
    private String getSerialNoProductType;
    Handler handler;
    private boolean hasConnected;
    private ImageView img_close;
    private ArrayList<CarVersionInfo> infos;
    private boolean isClick;
    private boolean isMTK;
    private String lanName;
    private ListView listview;
    MainActivity f2128m;
    private String paths;
    private ProgressDialog proDialog;
    private String productType;
    private String sdPaths;

    /* renamed from: com.ifoer.expeditionphone.LoadDynamicLibsActivity.1 */
    class C05841 extends Handler {
        C05841() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoadDynamicLibsActivity.this.checkLatestVersion.setClickable(LoadDynamicLibsActivity.f2127D);
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (LoadDynamicLibsActivity.this.proDialog != null && LoadDynamicLibsActivity.this.proDialog.isShowing()) {
                        LoadDynamicLibsActivity.this.proDialog.dismiss();
                    }
                    Toast.makeText(LoadDynamicLibsActivity.this.context, C0136R.string.timeout, 1).show();
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    if (LoadDynamicLibsActivity.this.proDialog != null && LoadDynamicLibsActivity.this.proDialog.isShowing()) {
                        LoadDynamicLibsActivity.this.proDialog.dismiss();
                    }
                    Toast.makeText(LoadDynamicLibsActivity.this.context, C0136R.string.check_serialNo, 1).show();
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    if (LoadDynamicLibsActivity.this.proDialog != null && LoadDynamicLibsActivity.this.proDialog.isShowing()) {
                        LoadDynamicLibsActivity.this.proDialog.dismiss();
                    }
                    Toast.makeText(LoadDynamicLibsActivity.this.context, C0136R.string.isnew, 1).show();
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    if (LoadDynamicLibsActivity.this.proDialog != null && LoadDynamicLibsActivity.this.proDialog.isShowing()) {
                        LoadDynamicLibsActivity.this.proDialog.dismiss();
                    }
                    Toast.makeText(LoadDynamicLibsActivity.this.context, C0136R.string.get_data_fail, 1).show();
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    Toast.makeText(LoadDynamicLibsActivity.this.context, C0136R.string.delete_success, 1).show();
                    LoadDynamicLibsActivity.this.infos = LoadDynamicLibsActivity.this.adapter.getDataList();
                    LoadDynamicLibsActivity.this.adapter.notifyDataSetChanged();
                    if (LoadDynamicLibsActivity.this.infos.size() == 0) {
                        LoadDynamicLibsActivity.this.finish();
                    }
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    Toast.makeText(LoadDynamicLibsActivity.this.context, C0136R.string.delete_fail, 1).show();
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    if (LoadDynamicLibsActivity.this.proDialog != null && LoadDynamicLibsActivity.this.proDialog.isShowing()) {
                        LoadDynamicLibsActivity.this.proDialog.dismiss();
                    }
                    ArrayList<X431PadSoftDTO> downloadList = msg.obj;
                    Intent intent = new Intent(LoadDynamicLibsActivity.this.context, DownloadUpdateActivity.class);
                    intent.putExtra("downloadList", downloadList);
                    LoadDynamicLibsActivity.this.startActivity(intent);
                    LoadDynamicLibsActivity.this.finish();
                case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                    if (LoadDynamicLibsActivity.this.proDialog != null && LoadDynamicLibsActivity.this.proDialog.isShowing()) {
                        LoadDynamicLibsActivity.this.proDialog.dismiss();
                    }
                    SimpleDialog.validTokenDialog(LoadDynamicLibsActivity.this.context);
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    if (LoadDynamicLibsActivity.this.proDialog != null && LoadDynamicLibsActivity.this.proDialog.isShowing()) {
                        LoadDynamicLibsActivity.this.proDialog.dismiss();
                    }
                    Toast.makeText(LoadDynamicLibsActivity.this.context, C0136R.string.get_data_fail, 1).show();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.LoadDynamicLibsActivity.2 */
    class C05862 implements OnItemClickListener {

        /* renamed from: com.ifoer.expeditionphone.LoadDynamicLibsActivity.2.1 */
        class C05851 implements OnClickListener {
            C05851() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }

        C05862() {
        }

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (LoadDynamicLibsActivity.this.isMTK) {
                if (MainActivity.SanmuGetV(LoadDynamicLibsActivity.this.context) == 1) {
                    LoadDynamicLibsActivity.this.hasConnected = LoadDynamicLibsActivity.f2127D;
                } else if (Constant.isDemo || Constant.isReset || Constant.isLANDROVER_DEMO) {
                    MainActivity.SanMusetHigh(LoadDynamicLibsActivity.this.context);
                    Constant.hasSetHigh = LoadDynamicLibsActivity.f2127D;
                    LoadDynamicLibsActivity.this.hasConnected = LoadDynamicLibsActivity.f2127D;
                } else {
                    LoadDynamicLibsActivity.this.hasConnected = false;
                }
            } else if (!LoadDynamicLibsActivity.HardFlag) {
                Log.i("LoadLib", "04\u677f\u5b50");
                Constant.mChatService.reset2505();
                SystemClock.sleep(100);
                if (MainActivity.serialPort.getInputStream() == null) {
                    LoadDynamicLibsActivity.this.hasConnected = false;
                } else {
                    LoadDynamicLibsActivity.this.hasConnected = LoadDynamicLibsActivity.f2127D;
                }
            } else if (MainActivity.ODB_ISOK(LoadDynamicLibsActivity.HardFlag) == 1) {
                LoadDynamicLibsActivity.this.hasConnected = LoadDynamicLibsActivity.f2127D;
            } else if (Constant.isDemo || Constant.isReset || Constant.isLANDROVER_DEMO) {
                MainActivity.setHigh_ON();
                LoadDynamicLibsActivity.this.hasConnected = LoadDynamicLibsActivity.f2127D;
                Constant.hasSetHigh = LoadDynamicLibsActivity.f2127D;
            } else {
                LoadDynamicLibsActivity.this.hasConnected = false;
            }
            if (LoadDynamicLibsActivity.this.hasConnected) {
                MainActivity.reSet(LoadDynamicLibsActivity.this.context);
                Log.i("MainActivity", "reSet\u540e");
                SystemClock.sleep(500);
                String downloadBin = Constant.mChatService.readDPUVersionInfo2105().substring(1);
                if (downloadBin.isEmpty() || !Tools.isNumeric(downloadBin.replace(".", XmlPullParser.NO_NAMESPACE))) {
                    MySharedPreferences.setString(LoadDynamicLibsActivity.this.context, "downloadBin", "1.00");
                    MySharedPreferences.setString(LoadDynamicLibsActivity.this.context, Constants.DOWNLOADBIN, "1.00");
                    Toast.makeText(LoadDynamicLibsActivity.this.context, LoadDynamicLibsActivity.this.getString(C0136R.string.firm_fix), 0).show();
                    return;
                }
                MySharedPreferences.setString(LoadDynamicLibsActivity.this.context, Constants.DOWNLOADBIN, downloadBin);
                MainActivity.reSet(LoadDynamicLibsActivity.this.context);
                SystemClock.sleep(50);
                Constant.mChatService.smartBox2109(1);
                SystemClock.sleep(100);
                CarVersionInfo info = (CarVersionInfo) arg0.getItemAtPosition(arg2);
                LoadDynamicLibsActivity.this.handler.sendEmptyMessageDelayed(9, 50);
                MySharedPreferences.setString(LoadDynamicLibsActivity.this.context, MySharedPreferences.diagnosticSoftwareVersionNo, info.getVersionNo());
                String carSoftWare = info.getVersionDir();
                File file = new File(carSoftWare);
                if (!file.exists()) {
                    String[] tempPaths = Constant.getDefaultExternalStoragePathList();
                    if (tempPaths.length > 1) {
                        carSoftWare = carSoftWare.replaceAll(tempPaths[0], tempPaths[1]);
                        file = new File(carSoftWare);
                    }
                }
                if (file.exists()) {
                    LoadDynamicLibsActivity.this.loadSo(new StringBuilder(String.valueOf(carSoftWare)).append(FilePathGenerator.ANDROID_DIR_SEP).toString(), carSoftWare.split(Constants.des_password)[1]);
                    return;
                }
                Builder builder = new Builder(LoadDynamicLibsActivity.this.context);
                builder.setTitle(LoadDynamicLibsActivity.this.getResources().getText(C0136R.string.initializeTilte));
                builder.setMessage(LoadDynamicLibsActivity.this.getResources().getText(C0136R.string.library_not_exist1));
                builder.setPositiveButton(LoadDynamicLibsActivity.this.getResources().getText(C0136R.string.sure), new C05851());
                builder.show();
                return;
            }
            Toast.makeText(LoadDynamicLibsActivity.this.context, LoadDynamicLibsActivity.this.getString(C0136R.string.check_connector_before_upgrade), 1).show();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.LoadDynamicLibsActivity.3 */
    class C05873 implements View.OnClickListener {
        C05873() {
        }

        public void onClick(View v) {
            String cc = MySharedPreferences.getStringValue(LoadDynamicLibsActivity.this.context, MySharedPreferences.CCKey);
            if (cc == null || cc.equals(XmlPullParser.NO_NAMESPACE)) {
                SimpleDialog.ToastToLogin(LoadDynamicLibsActivity.this.context);
            } else if (Common.isNetworkAvailable(LoadDynamicLibsActivity.this.context)) {
                LoadDynamicLibsActivity.this.checkLatestVersion.setClickable(false);
                new CheckLatestVersion().execute(new String[0]);
            } else {
                Toast.makeText(LoadDynamicLibsActivity.this.context, C0136R.string.network, 1).show();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.LoadDynamicLibsActivity.4 */
    class C05884 implements View.OnClickListener {
        C05884() {
        }

        public void onClick(View v) {
            LoadDynamicLibsActivity.this.finish();
        }
    }

    class CheckLatestVersion extends AsyncTask<String, String, String> {
        ProductDTOResult res;

        CheckLatestVersion() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            LoadDynamicLibsActivity.this.proDialog = new ProgressDialog(LoadDynamicLibsActivity.this.context);
            LoadDynamicLibsActivity.this.proDialog.setMessage(LoadDynamicLibsActivity.this.getResources().getText(C0136R.string.checking));
            LoadDynamicLibsActivity.this.proDialog.setCancelable(false);
            LoadDynamicLibsActivity.this.proDialog.show();
        }

        protected String doInBackground(String... params) {
            List<Double> versions = new ArrayList();
            double maxOldVersion = 0.0d;
            if (LoadDynamicLibsActivity.this.adapter.data.size() > 0) {
                for (int i = 0; i < LoadDynamicLibsActivity.this.adapter.data.size(); i++) {
                    String versionNoStr = ((CarVersionInfo) LoadDynamicLibsActivity.this.adapter.data.get(i)).getVersionNo();
                    List<Double> list = versions;
                    list.add(Double.valueOf(Double.parseDouble(versionNoStr.substring(1, versionNoStr.length()))));
                }
                Collections.sort(versions);
                maxOldVersion = ((Double) versions.get(versions.size() - 1)).doubleValue();
            }
            boolean isSame = false;
            if (MainActivity.country == null || MainActivity.country.length() <= 0) {
                MainActivity.country = Locale.getDefault().getCountry();
            }
            int lanId = AndroidToLan.getLanId(MainActivity.country);
            ProductService productService = new ProductService();
            String cc = MySharedPreferences.getStringValue(LoadDynamicLibsActivity.this.context, MySharedPreferences.CCKey);
            String token = MySharedPreferences.getStringValue(LoadDynamicLibsActivity.this.context, MySharedPreferences.TokenKey);
            String oldSerialNo = MySharedPreferences.getStringValue(LoadDynamicLibsActivity.this.context, MySharedPreferences.serialNoKey);
            productService.setCc(cc);
            productService.setToken(token);
            try {
                this.res = productService.getRegisteredProductsForPad(LoadDynamicLibsActivity.this.getSerialNoProductType);
                if (this.res.getCode() != 0 || this.res.getProductDTOs().size() <= 0) {
                    if (this.res.getCode() == -1) {
                        LoadDynamicLibsActivity.this.handler.obtainMessage(7).sendToTarget();
                    } else {
                        LoadDynamicLibsActivity.this.handler.obtainMessage(8).sendToTarget();
                    }
                    return null;
                }
                for (ProductDTO dto : this.res.getProductDTOs()) {
                    if (oldSerialNo.equals(dto.getSerialNo())) {
                        isSame = LoadDynamicLibsActivity.f2127D;
                    }
                }
                if (isSame) {
                    int softId = DBDao.getInstance(LoadDynamicLibsActivity.this.context).querySoftId(LoadDynamicLibsActivity.this.carId, MainActivity.database);
                    X431PadDiagSoftService service = new X431PadDiagSoftService();
                    service.setCc(cc);
                    service.setToken(token);
                    if (softId == 0) {
                        DiagSoftIdListResult softIdListRes = service.getDiagSoftIdList(LoadDynamicLibsActivity.this.productType);
                        if (softIdListRes != null && softIdListRes.getCode() == 0 && softIdListRes.getDiagSoftIdList() != null && softIdListRes.getDiagSoftIdList().size() > 0) {
                            new SaveSoftId(LoadDynamicLibsActivity.this.context, softIdListRes).start();
                            for (DiagSoftIdDTO dto2 : softIdListRes.getDiagSoftIdList()) {
                                if (dto2.getSoftPackageId().equalsIgnoreCase(LoadDynamicLibsActivity.this.carId)) {
                                    softId = dto2.getSoftId().intValue();
                                    break;
                                }
                            }
                        } else if (softIdListRes.getCode() == -1) {
                            LoadDynamicLibsActivity.this.handler.obtainMessage(7).sendToTarget();
                        } else {
                            LoadDynamicLibsActivity.this.handler.obtainMessage(8).sendToTarget();
                        }
                    }
                    if (softId != 0) {
                        X431PadSoftListResult softListResult = service.getDiagSoftInfoBySoftId(oldSerialNo, Integer.valueOf(softId), Integer.valueOf(lanId), Integer.valueOf(XStream.NO_REFERENCES), cc, token);
                        if (softListResult.getCode() == 0 && softListResult.getDtoList().size() > 0) {
                            String versionNo = ((X431PadSoftDTO) softListResult.getDtoList().get(0)).getVersionNo();
                            if (Double.parseDouble(versionNo.substring(1, versionNo.length())) > maxOldVersion) {
                                ArrayList<X431PadSoftDTO> downloadList = new ArrayList();
                                for (X431PadSoftDTO dto3 : softListResult.getDtoList()) {
                                    downloadList.add(dto3);
                                }
                                LoadDynamicLibsActivity.this.handler.obtainMessage(6, downloadList).sendToTarget();
                            } else {
                                LoadDynamicLibsActivity.this.handler.obtainMessage(2).sendToTarget();
                            }
                        } else if (softListResult.getCode() == -1) {
                            LoadDynamicLibsActivity.this.handler.obtainMessage(7).sendToTarget();
                        } else {
                            LoadDynamicLibsActivity.this.handler.obtainMessage(8).sendToTarget();
                        }
                    }
                } else {
                    LoadDynamicLibsActivity.this.handler.obtainMessage(1).sendToTarget();
                }
                return null;
            } catch (SocketTimeoutException e) {
                LoadDynamicLibsActivity.this.handler.obtainMessage(0).sendToTarget();
            } catch (NullPointerException e2) {
                LoadDynamicLibsActivity.this.handler.obtainMessage(0).sendToTarget();
            }
        }
    }

    class PublicCircleAsy extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        PublicCircleAsy() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(LoadDynamicLibsActivity.this.context);
            this.progressDialog.setMessage(LoadDynamicLibsActivity.this.getResources().getText(C0136R.string.initializeMessage));
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        protected String doInBackground(String... params) {
            if (Copy_File.copy(LoadDynamicLibsActivity.this.sdPaths, LoadDynamicLibsActivity.this.paths) == 0) {
                Copy_File.list = new ArrayList();
                Copy_File.findAllSoFile(LoadDynamicLibsActivity.this.paths);
                if (Copy_File.list.size() > 0) {
                    return null;
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            this.progressDialog.cancel();
            LoadDynamicLibsActivity.this.isClick = false;
            if (Copy_File.list.size() > 0) {
                Intent menu = new Intent(LoadDynamicLibsActivity.this, CarDiagnoseActivity.class);
                menu.putStringArrayListExtra("FileList", Copy_File.list);
                menu.putExtra("paths", LoadDynamicLibsActivity.this.sdPaths);
                Constant.path = LoadDynamicLibsActivity.this.sdPaths;
                menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                LoadDynamicLibsActivity.this.startActivity(menu);
                if (LoadDynamicLibsActivity.this.getIntent().getExtras().containsKey("wuhong") && MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.generateOperatingRecord).equalsIgnoreCase(Contact.RELATION_ASK)) {
                    String softPackageId = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.savesoftPackageId);
                    String serialNo = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.serialNoKey);
                    if (Constant.ADDRESS == null || Constant.ADDRESS.equals(XmlPullParser.NO_NAMESPACE)) {
                        Constant.ADDRESS = MySharedPreferences.getStringValue(MainActivity.contexts, "CurrentPosition");
                    }
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    OperatingRecordInfo recordInfo = new OperatingRecordInfo();
                    recordInfo.setSerialNumber(serialNo);
                    recordInfo.setTestTime(date);
                    recordInfo.setTestSite(Constant.ADDRESS);
                    recordInfo.setTestCar(softPackageId);
                    if (Boolean.valueOf(DBDao.getInstance(MainActivity.contexts).addOperatingRecord(recordInfo, MainActivity.database)).booleanValue()) {
                        MySharedPreferences.setString(MainActivity.contexts, MySharedPreferences.generateOperatingRecord, Contact.RELATION_FRIEND);
                    }
                }
                LoadDynamicLibsActivity.this.overridePendingTransition(0, 0);
                LoadDynamicLibsActivity.this.finish();
                return;
            }
            Toast.makeText(LoadDynamicLibsActivity.this, LoadDynamicLibsActivity.this.getResources().getString(C0136R.string.so_error), BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT).show();
        }
    }

    public LoadDynamicLibsActivity() {
        this.dataDir = XmlPullParser.NO_NAMESPACE;
        this.paths = XmlPullParser.NO_NAMESPACE;
        this.sdPaths = XmlPullParser.NO_NAMESPACE;
        this.infos = new ArrayList();
        this.productType = "X431ADiag";
        this.getSerialNoProductType = "X431 Auto Diag For Android";
        this.isClick = false;
        this.isMTK = false;
        this.f2128m = new MainActivity();
        this.hasConnected = false;
        this.handler = new C05841();
    }

    static {
        HardFlag = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.load_dynamic_libs);
        FreeMemory.getInstance(this).freeMemory();
        MyApplication.getInstance().addActivity(this);
        this.context = this;
        DialogUtil.setDialogSize(this);
        Intent intent = getIntent();
        boolean isFunction = false;
        if (intent != null) {
            this.infos = (ArrayList) intent.getSerializableExtra("list");
            this.carId = intent.getStringExtra("carId");
            this.lanName = intent.getStringExtra("lanName");
            if (this.carId.equalsIgnoreCase("DEMO")) {
                Constant.isDemo = f2127D;
            } else {
                Constant.isDemo = false;
            }
            if (this.carId.equalsIgnoreCase("LANDROVER_DEMO")) {
                Constant.isLANDROVER_DEMO = f2127D;
            } else {
                Constant.isLANDROVER_DEMO = false;
            }
            isFunction = intent.getBooleanExtra("loadSpecail", false);
        }
        if (MySharedPreferences.getBooleanValue(this.context, Constants.isSanMu, false)) {
            this.isMTK = f2127D;
        }
        try {
            this.dataDir = getPackageManager().getApplicationInfo(getPackageName(), 0).dataDir;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView loadTitle = (TextView) findViewById(C0136R.id.loadTitle);
        if (isFunction) {
            loadTitle.setText(getResources().getString(C0136R.string.function));
            Constant.isReset = f2127D;
        } else {
            Constant.isReset = false;
        }
        this.productType = MySharedPreferences.getStringValue(this, "PRODUCT_TYPE");
        this.getSerialNoProductType = MySharedPreferences.getStringValue(this, "SERIA_NO_PRODUCT_TYPE");
        this.listview = (ListView) findViewById(C0136R.id.listview);
        this.adapter = new LoadDynamicAdapter(this.infos, this, this.handler, this.carId, this.lanName);
        this.adapter.setLoadDynamicLibsActivity(this);
        this.listview.setAdapter(this.adapter);
        this.listview.setItemsCanFocus(f2127D);
        HardFlag = MySharedPreferences.getBooleanValue(this.context, Constants.hasODB, false);
        this.listview.setOnItemClickListener(new C05862());
        this.checkLatestVersion = (Button) findViewById(C0136R.id.checkVersion);
        this.checkLatestVersion.setOnClickListener(new C05873());
        if (MySharedPreferences.getIntValue(this, MySharedPreferences.DefScanPad) == 1) {
            this.img_close = (ImageView) findViewById(C0136R.id.img_close);
            this.img_close.setOnClickListener(new C05884());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0136R.menu.main, menu);
        return f2127D;
    }

    public byte[] toByte(String str) {
        String str1 = str.replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE);
        byte[] d = new byte[(str1.length() / 2)];
        int i = 0;
        while (i < str1.length()) {
            int c;
            int tmp = str1.substring(i, i + 1).getBytes()[0];
            if (tmp > 96) {
                c = ((tmp - 97) + 10) * 16;
            } else if (tmp > 64) {
                c = ((tmp - 65) + 10) * 16;
            } else {
                c = (tmp - 48) * 16;
            }
            i++;
            tmp = str1.substring(i, i + 1).getBytes()[0];
            if (tmp > 96) {
                c += (tmp - 97) + 10;
            } else if (tmp > 64) {
                c += (tmp - 65) + 10;
            } else {
                c += tmp - 48;
            }
            d[i / 2] = (byte) c;
            i++;
        }
        return d;
    }

    public void loadSo(String dataDirs, String sdPath) {
        this.paths = this.dataDir + "/libs/cnlaunch" + sdPath + FilePathGenerator.ANDROID_DIR_SEP;
        this.sdPaths = dataDirs;
        new PublicCircleAsy().execute(new String[0]);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DialogUtil.setDialogSize(this);
    }
}
