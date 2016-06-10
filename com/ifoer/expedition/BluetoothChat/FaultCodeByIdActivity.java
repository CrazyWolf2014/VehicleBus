package com.ifoer.expedition.BluetoothChat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.FaultCodeAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptInputNumric;
import com.ifoer.entity.SptInputStringEx;
import com.ifoer.entity.SptMessageBoxText;
import com.ifoer.entity.SptStreamSelectIdItem;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.entity.Spt_Nobuttonbox_Text;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.ShowFileActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SimpleDialogControl;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public class FaultCodeByIdActivity extends Activity implements OnClickListener {
    private static final boolean f1250D = true;
    public static IntentFilter myIntentFilter;
    private static ProgressDialog progressDialog;
    public static mBroadcastReceiver receiver;
    private final int MSG_PRINT_RESULT_SCREEN_SHOT;
    private final int MSG_PRINT_START;
    private FaultCodeAdapter adapter;
    private ImageView backSuperior;
    Handler baseHandler;
    Bitmap bit_first;
    Bitmap bit_second;
    Bitmap bitmap1;
    private Bundle bundle;
    protected LinearLayout car_maintain;
    String cc;
    private Button chakan;
    private int cishu;
    private Context contexts;
    private int count;
    DBDao dao;
    private String data;
    private File file;
    private int f1251i;
    private Intent intent;
    private Boolean isExecuteF;
    private int f1252j;
    private Button jietu;
    private int f1253k;
    private long lastTime;
    private ListView listview;
    private final Handler mHandler;
    private RelativeLayout main_head;
    public LinearLayout menuBtn;
    String name;
    private int pagecount;
    private int pagesize;
    int printResult;
    String f1254s;
    private StringBuffer sb;
    Runnable screenBmp;
    private String sdCardDir;
    private String serialNo;
    private String softPackageId;
    private SptTroubleTest troubleTest;
    private ArrayList<SptTroubleTest> troubleTestList;
    private Button wenzi;

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByIdActivity.1 */
    class C04281 implements Runnable {
        C04281() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(FaultCodeByIdActivity.this);
            FaultCodeByIdActivity.this.bit_first = bmpPrinter.drawBitFirst();
            FaultCodeByIdActivity.this.bit_second = bmpPrinter.drawBitSecond(FaultCodeByIdActivity.this.sb.toString());
            FaultCodeByIdActivity.this.bitmap1 = NetPOSPrinter.mixtureBitmap(FaultCodeByIdActivity.this.bit_first, FaultCodeByIdActivity.this.bit_second);
            FaultCodeByIdActivity.this.printResult = bmpPrinter.printPic(FaultCodeByIdActivity.this.bitmap1);
            bmpPrinter.resultToast(FaultCodeByIdActivity.this.printResult);
            FaultCodeByIdActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(FaultCodeByIdActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByIdActivity.2 */
    class C04292 extends Handler {
        C04292() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    FaultCodeByIdActivity.this.dayin();
                case 10101010:
                    if (FaultCodeByIdActivity.progressDialog != null && FaultCodeByIdActivity.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    }
                    SimpleDialog.checkConectior(FaultCodeByIdActivity.this.contexts, FaultCodeByIdActivity.this.getString(C0136R.string.initializeTilte), FaultCodeByIdActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByIdActivity.3 */
    class C04303 extends Handler {
        C04303() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    FaultCodeByIdActivity.this.adapter.refresh(FaultCodeByIdActivity.this.troubleTestList);
                default:
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {

        /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByIdActivity.mBroadcastReceiver.1 */
        class C04311 extends Thread {
            C04311() {
            }

            public void run() {
                FaultCodeByIdActivity.this.mHandler.sendMessage(FaultCodeByIdActivity.this.mHandler.obtainMessage(15));
            }
        }

        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (FaultCodeByIdActivity.this.isExecuteF.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                    if ((System.currentTimeMillis() - FaultCodeByIdActivity.this.lastTime) / 1000 > 30) {
                        SimpleDialog.ExitDialog(context);
                    }
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    FaultCodeByIdActivity.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    FaultCodeByIdActivity.this.finish();
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("menuData");
                    menu = new Intent(FaultCodeByIdActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    FaultCodeByIdActivity.this.startActivity(menu);
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByIdActivity.this.finish();
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    menu = new Intent(FaultCodeByIdActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    FaultCodeByIdActivity.this.startActivity(menu);
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByIdActivity.this.finish();
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    Intent active = new Intent(FaultCodeByIdActivity.this, ActiveTestActivity.class);
                    active.putExtra("ACTIVE_TEST", sptActiveTest);
                    active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    FaultCodeByIdActivity.this.startActivity(active);
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByIdActivity.this.finish();
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (FaultCodeByIdActivity.progressDialog == null) {
                        FaultCodeByIdActivity.progressDialog = new ProgressDialog(FaultCodeByIdActivity.this.contexts);
                        FaultCodeByIdActivity.progressDialog.setCancelable(false);
                    } else {
                        FaultCodeByIdActivity.progressDialog.dismiss();
                        FaultCodeByIdActivity.progressDialog = null;
                        FaultCodeByIdActivity.progressDialog = new ProgressDialog(FaultCodeByIdActivity.this.contexts);
                        FaultCodeByIdActivity.progressDialog.setCancelable(false);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(FaultCodeByIdActivity.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), FaultCodeByIdActivity.progressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                    r0 = new Intent(FaultCodeByIdActivity.this, FaultCodeByIdActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    FaultCodeByIdActivity.this.startActivity(r0);
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByIdActivity.this.finish();
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    FaultCodeByIdActivity.this.finish();
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(FaultCodeByIdActivity.this, StreamSelectActivity.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    FaultCodeByIdActivity.this.startActivity(r0);
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByIdActivity.this.finish();
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(FaultCodeByIdActivity.this, DataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    FaultCodeByIdActivity.this.startActivity(r0);
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByIdActivity.this.finish();
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    int dialogType = sptMessageBoxText.getDialogType();
                    SimpleDialogControl.showDiaglog(FaultCodeByIdActivity.this.contexts, dialogType, sptMessageBoxText);
                } else if (intent.getAction().equals("ConnectionLost")) {
                    Toast.makeText(FaultCodeByIdActivity.this.getApplicationContext(), "Device connection was lost", 0).show();
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    SimpleDialog.sptInputStringExDiagnose(FaultCodeByIdActivity.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    SimpleDialog.sptInputNumericDiagnose(FaultCodeByIdActivity.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    SimpleDialog.sptInputBoxTextDiagnose(FaultCodeByIdActivity.this.contexts, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    SimpleDialog.sptInputStringDiagnose(FaultCodeByIdActivity.this.contexts, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(FaultCodeByIdActivity.this, FaultCodeFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    FaultCodeByIdActivity.this.startActivity(r0);
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByIdActivity.this.finish();
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    r0 = new Intent(FaultCodeByIdActivity.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    FaultCodeByIdActivity.this.startActivity(r0);
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByIdActivity.this.finish();
                    FaultCodeByIdActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    String pictureName = intent.getExtras().getString("SPT_SHOW_PICTURE");
                    SimpleDialog.sptShowPictureDiagnose(FaultCodeByIdActivity.this.contexts, pictureName);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_ID_EX")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByIdActivity.progressDialog);
                    FaultCodeByIdActivity.this.troubleTestList = null;
                    FaultCodeByIdActivity.this.troubleTestList = (ArrayList) FaultCodeByIdActivity.this.bundle.getSerializable("SPT_TROUBLE_CODE_ID_EX");
                    new C04311().start();
                }
            }
        }
    }

    public FaultCodeByIdActivity() {
        this.troubleTestList = new ArrayList();
        this.troubleTest = null;
        this.sb = null;
        this.f1253k = 1;
        this.pagesize = 7;
        this.f1251i = 1;
        this.f1252j = 1;
        this.data = null;
        this.pagecount = 0;
        this.cishu = 0;
        this.isExecuteF = Boolean.valueOf(false);
        this.dao = DBDao.getInstance(this);
        this.f1254s = "ni hao o,hhh";
        this.softPackageId = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.savesoftPackageId);
        this.serialNo = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.serialNoKey);
        this.name = this.softPackageId + "_DTC_";
        this.bitmap1 = null;
        this.MSG_PRINT_RESULT_SCREEN_SHOT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.count = 0;
        this.screenBmp = new C04281();
        this.baseHandler = new C04292();
        this.mHandler = new C04303();
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        for (int i = 0; i < this.troubleTestList.size(); i++) {
            this.troubleTest = (SptTroubleTest) this.troubleTestList.get(i);
            this.sb.append(new StringBuilder(String.valueOf(this.troubleTest.getTroubleCodeContent())).append("   ").append(this.troubleTest.getTroubleDescribeContent()).append(this.troubleTest.getTroubleStateContent()).append(SpecilApiUtil.LINE_SEP).toString());
        }
    }

    public void dayin() {
        getPrintDate();
        new Thread(this.screenBmp).start();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        this.contexts = this;
        setContentView(C0136R.layout.fault_code);
        MyApplication.getInstance().addActivity(this);
        this.cc = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.CCKey);
        BluetoothChatService.setHandler(this.baseHandler);
        initView();
        registerBoradcastReceiver();
    }

    private void initView() {
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.troubleTestList = (ArrayList) this.bundle.getSerializable("SPT_TROUBLE_CODE_ID_EX");
        }
        this.main_head = (RelativeLayout) findViewById(C0136R.id.mainTop);
        this.wenzi = (Button) findViewById(C0136R.id.wenzi);
        this.jietu = (Button) findViewById(C0136R.id.jietu);
        this.chakan = (Button) findViewById(C0136R.id.guanli);
        this.wenzi.setOnClickListener(this);
        this.jietu.setOnClickListener(this);
        this.chakan.setOnClickListener(this);
        getDate();
        this.listview = (ListView) findViewById(C0136R.id.view);
        this.adapter = new FaultCodeAdapter(this.troubleTestList, this);
        this.listview.setAdapter(this.adapter);
    }

    private void getDate() {
        int i;
        for (i = 0; i < this.dao.queryReport(this.cc, Contact.RELATION_ASK, MainActivity.database).size(); i++) {
        }
        this.sb = new StringBuffer();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (this.f1253k == 1) {
            this.sb.append("=====" + date + "=====");
            this.sb.append("\n\n");
        }
        for (i = 0; i < this.troubleTestList.size(); i++) {
            this.troubleTest = (SptTroubleTest) this.troubleTestList.get(i);
            this.sb.append("   " + this.troubleTest.getTroubleCodeContent() + "   " + this.troubleTest.getTroubleDescribeContent() + "   " + this.troubleTest.getTroubleStateContent() + "   " + "\n\n");
        }
    }

    private void saveImage() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(f1250D);
        saveBitmaps(zoomBitmap(view.getDrawingCache()));
        Toast.makeText(this, getResources().getString(C0136R.string.devfinish), 0).show();
    }

    public void SaveTxt(String s, String name, boolean ss) {
        getDate();
        try {
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            this.sdCardDir = Constant.DST_FILE;
            File dirFile = new File(this.sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            name = name.replaceAll("EOBD2", "EOBD");
            String path = this.sdCardDir + name + date + ".txt";
            if (!new File(path).exists()) {
                DBDao.getInstance(this).addReport(new StringBuilder(String.valueOf(name)).append(date).append(".txt").toString(), date, this.serialNo, path, Contact.RELATION_ASK, MainActivity.database);
                FileOutputStream outStream = new FileOutputStream(path, f1250D);
                OutputStreamWriter writer = new OutputStreamWriter(outStream, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                writer.write(s);
                writer.flush();
                writer.close();
                outStream.close();
                Toast.makeText(this, getResources().getString(C0136R.string.devfinish), 0).show();
            }
        } catch (Exception e) {
        }
    }

    protected void onPause() {
        super.onPause();
        this.cishu++;
    }

    public void saveBitmaps(Bitmap bitmap) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            String sdCardDir = Constant.DST_FILE;
            File dirFile = new File(sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            this.name = this.name.replaceAll("EOBD2", "EOBD");
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            File file = new File(sdCardDir, this.name + date + Util.PHOTO_DEFAULT_EXT);
            if (file.exists()) {
                file.delete();
                file = new File(sdCardDir, this.name + date + Util.PHOTO_DEFAULT_EXT);
            } else {
                file = new File(sdCardDir, this.name + date + Util.PHOTO_DEFAULT_EXT);
                this.dao.addReport(this.name + date + Util.PHOTO_DEFAULT_EXT, date, this.serialNo, new StringBuilder(String.valueOf(sdCardDir)).append(this.name).append(date).append(Util.PHOTO_DEFAULT_EXT).toString(), Contact.RELATION_ASK, MainActivity.database);
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                return;
            }
        }
        Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
    }

    public Bitmap zoomBitmap(Bitmap target) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) width) / ((float) width), ((float) height) / ((float) height));
        int main_height = this.main_head.getHeight();
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int y = main_height + frame.top;
        return Bitmap.createBitmap(target, 0, y, width, height - y, matrix, f1250D);
    }

    public void registerBoradcastReceiver() {
        receiver = new mBroadcastReceiver();
        myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        myIntentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        myIntentFilter.addAction("feedbackMeauData");
        myIntentFilter.addAction("SPT_ACTIVE_TEST");
        myIntentFilter.addAction("SPT_NOBUTTONBOX_TEXT");
        myIntentFilter.addAction("closeNobuttonBox");
        myIntentFilter.addAction("SPT_TROUBLE_CODE");
        myIntentFilter.addAction("SPT_EXIT_SHOW_WINDOW");
        myIntentFilter.addAction("SPT_STREAM_SELECT_ID_EX");
        myIntentFilter.addAction("SPT_EX_DATASTREAM_ID");
        myIntentFilter.addAction("SPT_MESSAGEBOX_TEXT");
        myIntentFilter.addAction("ConnectionLost");
        myIntentFilter.addAction("SPT_INPUTSTRING_EX");
        myIntentFilter.addAction("SPT_INPUT_NUMERIC");
        myIntentFilter.addAction("SPT_INPUTBOX_TEXT");
        myIntentFilter.addAction("SPT_INPUTSTRING");
        myIntentFilter.addAction("SPT_TROUBLE_CODE_FROZEN");
        myIntentFilter.addAction("SPT_VW_DATASTREAM_ID");
        myIntentFilter.addAction("SPT_DS_MENU_ID");
        myIntentFilter.addAction("SPT_SHOW_PICTURE");
        myIntentFilter.addAction("SPT_TROUBLE_CODE_ID_EX");
        registerReceiver(receiver, myIntentFilter);
    }

    protected void onResume() {
        super.onResume();
        this.isExecuteF = Boolean.valueOf(f1250D);
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteF = Boolean.valueOf(false);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        finish();
        overridePendingTransition(0, 0);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 && keyCode != Service.SUNRPC) {
            return super.onKeyDown(keyCode, event);
        }
        back();
        return f1250D;
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.wenzi) {
            if (this.f1251i != 1) {
                Toast.makeText(this, getResources().getString(C0136R.string.save_report), 0).show();
            } else if (Environment.getExternalStorageState().equals("mounted")) {
                SaveTxt(this.sb.toString(), this.name, f1250D);
                this.f1251i++;
            } else {
                Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
            }
        } else if (v.getId() == C0136R.id.jietu) {
            if (this.f1252j == 1) {
                this.f1253k = 1;
                if (Environment.getExternalStorageState().equals("mounted")) {
                    saveImage();
                    this.f1252j++;
                    return;
                }
                Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
                return;
            }
            Toast.makeText(this, getResources().getString(C0136R.string.save_report), 0).show();
        } else if (v.getId() == C0136R.id.guanli) {
            this.f1253k = 1;
            this.intent = new Intent();
            this.intent.setClass(this, ShowFileActivity.class);
            startActivity(this.intent);
            overridePendingTransition(0, 0);
        }
    }

    private void back() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this.contexts);
            progressDialog.setCancelable(false);
        } else {
            progressDialog.dismiss();
            progressDialog = null;
            progressDialog = new ProgressDialog(this.contexts);
            progressDialog.setCancelable(false);
        }
        SimpleDialog.openProgressDialog(this.contexts, getResources().getString(C0136R.string.dataDisposeTilte), getResources().getString(C0136R.string.dataDisposeMessage), progressDialog);
        Constant.feedback = null;
        Constant.feedback = Constant.previousMenu;
        if (Constant.bridge != null) {
            Constant.bridge.putData();
        }
    }
}
