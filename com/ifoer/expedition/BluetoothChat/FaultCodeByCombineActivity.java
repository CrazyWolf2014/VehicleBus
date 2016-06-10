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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
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
import org.xmlpull.v1.XmlPullParser;

public class FaultCodeByCombineActivity extends Activity implements OnClickListener {
    private static final boolean f1245D = true;
    public static IntentFilter myIntentFilter;
    private static ProgressDialog progressDialog;
    public static mBroadcastReceiver receiver;
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_RESULT_SCREEN_SHOT;
    private final int MSG_PRINT_START;
    private FaultCodeAdapter adapter;
    Handler baseHandler;
    Bitmap bit_first;
    Bitmap bit_second;
    Bitmap bitmap1;
    private Bundle bundle;
    String cc;
    private Button chakan;
    private int cishu;
    private Context contexts;
    private int count;
    DBDao dao;
    private String data;
    private File file;
    private int f1246i;
    private Intent intent;
    private Boolean isExecuteF;
    private int f1247j;
    private Button jietu;
    private int f1248k;
    private long lastTime;
    private ListView listview;
    private RelativeLayout main_head;
    public LinearLayout menuBtn;
    String name;
    private int pagecount;
    private int pagesize;
    int printResult;
    String f1249s;
    private StringBuffer sb;
    Runnable screenBmp;
    private String sdCardDir;
    private String serialNo;
    private String softPackageId;
    private SptTroubleTest troubleTest;
    private ArrayList<SptTroubleTest> troublebyCombineList;
    private Button wenzi;

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByCombineActivity.1 */
    class C04191 implements Runnable {
        C04191() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(FaultCodeByCombineActivity.this);
            FaultCodeByCombineActivity.this.bit_first = bmpPrinter.drawBitFirst();
            FaultCodeByCombineActivity.this.bit_second = bmpPrinter.drawBitSecond(FaultCodeByCombineActivity.this.sb.toString());
            FaultCodeByCombineActivity.this.bitmap1 = NetPOSPrinter.mixtureBitmap(FaultCodeByCombineActivity.this.bit_first, FaultCodeByCombineActivity.this.bit_second);
            FaultCodeByCombineActivity.this.printResult = bmpPrinter.printPic(FaultCodeByCombineActivity.this.bitmap1);
            bmpPrinter.resultToast(FaultCodeByCombineActivity.this.printResult);
            FaultCodeByCombineActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(FaultCodeByCombineActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByCombineActivity.2 */
    class C04202 extends Handler {
        C04202() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    FaultCodeByCombineActivity.this.dayin();
                case 10101010:
                    if (FaultCodeByCombineActivity.progressDialog != null && FaultCodeByCombineActivity.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    }
                    SimpleDialog.checkConectior(FaultCodeByCombineActivity.this.contexts, FaultCodeByCombineActivity.this.getString(C0136R.string.initializeTilte), FaultCodeByCombineActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (FaultCodeByCombineActivity.this.isExecuteF.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                    if ((System.currentTimeMillis() - FaultCodeByCombineActivity.this.lastTime) / 1000 > 30) {
                        SimpleDialog.ExitDialog(context);
                    }
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    FaultCodeByCombineActivity.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    FaultCodeByCombineActivity.this.finish();
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("menuData");
                    menu = new Intent(FaultCodeByCombineActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    FaultCodeByCombineActivity.this.startActivity(menu);
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByCombineActivity.this.finish();
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    menu = new Intent(FaultCodeByCombineActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    FaultCodeByCombineActivity.this.startActivity(menu);
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByCombineActivity.this.finish();
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    Intent active = new Intent(FaultCodeByCombineActivity.this, ActiveTestActivity.class);
                    active.putExtra("ACTIVE_TEST", sptActiveTest);
                    active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    FaultCodeByCombineActivity.this.startActivity(active);
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByCombineActivity.this.finish();
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (FaultCodeByCombineActivity.progressDialog == null) {
                        FaultCodeByCombineActivity.progressDialog = new ProgressDialog(FaultCodeByCombineActivity.this.contexts);
                        FaultCodeByCombineActivity.progressDialog.setCancelable(false);
                    } else {
                        FaultCodeByCombineActivity.progressDialog.dismiss();
                        FaultCodeByCombineActivity.progressDialog = null;
                        FaultCodeByCombineActivity.progressDialog = new ProgressDialog(FaultCodeByCombineActivity.this.contexts);
                        FaultCodeByCombineActivity.progressDialog.setCancelable(false);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(FaultCodeByCombineActivity.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), FaultCodeByCombineActivity.progressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_ID_EX_BY_COMBINE")) {
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_ID_EX_BY_COMBINE");
                    r0 = new Intent(FaultCodeByCombineActivity.this, FaultCodeByCombineActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    FaultCodeByCombineActivity.this.startActivity(r0);
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByCombineActivity.this.finish();
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    FaultCodeByCombineActivity.this.finish();
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(FaultCodeByCombineActivity.this, StreamSelectActivity.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    FaultCodeByCombineActivity.this.startActivity(r0);
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByCombineActivity.this.finish();
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(FaultCodeByCombineActivity.this, DataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    FaultCodeByCombineActivity.this.startActivity(r0);
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByCombineActivity.this.finish();
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    int dialogType = sptMessageBoxText.getDialogType();
                    SimpleDialogControl.showDiaglog(FaultCodeByCombineActivity.this.contexts, dialogType, sptMessageBoxText);
                } else if (intent.getAction().equals("ConnectionLost")) {
                    Toast.makeText(FaultCodeByCombineActivity.this.getApplicationContext(), "Device connection was lost", 0).show();
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    SimpleDialog.sptInputStringExDiagnose(FaultCodeByCombineActivity.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    SimpleDialog.sptInputNumericDiagnose(FaultCodeByCombineActivity.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    SimpleDialog.sptInputBoxTextDiagnose(FaultCodeByCombineActivity.this.contexts, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    SimpleDialog.sptInputStringDiagnose(FaultCodeByCombineActivity.this.contexts, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(FaultCodeByCombineActivity.this, FaultCodeFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    FaultCodeByCombineActivity.this.startActivity(r0);
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByCombineActivity.this.finish();
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    r0 = new Intent(FaultCodeByCombineActivity.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    FaultCodeByCombineActivity.this.startActivity(r0);
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByCombineActivity.this.finish();
                    FaultCodeByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByCombineActivity.progressDialog);
                    String pictureName = intent.getExtras().getString("SPT_SHOW_PICTURE");
                    SimpleDialog.sptShowPictureDiagnose(FaultCodeByCombineActivity.this.contexts, pictureName);
                }
            }
        }
    }

    public FaultCodeByCombineActivity() {
        this.troublebyCombineList = new ArrayList();
        this.troubleTest = null;
        this.sb = null;
        this.f1248k = 1;
        this.pagesize = 7;
        this.f1246i = 1;
        this.f1247j = 1;
        this.data = null;
        this.pagecount = 0;
        this.cishu = 0;
        this.isExecuteF = Boolean.valueOf(false);
        this.dao = DBDao.getInstance(this);
        this.f1249s = "ni hao o,hhh";
        this.softPackageId = XmlPullParser.NO_NAMESPACE;
        this.serialNo = XmlPullParser.NO_NAMESPACE;
        this.name = XmlPullParser.NO_NAMESPACE;
        this.bitmap1 = null;
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_RESULT_SCREEN_SHOT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.count = 0;
        this.screenBmp = new C04191();
        this.baseHandler = new C04202();
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        for (int i = 0; i < this.troublebyCombineList.size(); i++) {
            this.troubleTest = (SptTroubleTest) this.troublebyCombineList.get(i);
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
        this.softPackageId = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.savesoftPackageId);
        this.serialNo = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.serialNoKey);
        this.name = this.softPackageId + "_DTC_";
        setContentView(C0136R.layout.fault_code);
        MyApplication.getInstance().addActivity(this);
        registerBoradcastReceiver();
        this.cc = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.CCKey);
        BluetoothChatService.setHandler(this.baseHandler);
        initView();
    }

    private void initView() {
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.troublebyCombineList = (ArrayList) this.bundle.getSerializable("SPT_TROUBLE_CODE_ID_BY_COMBINE");
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
        this.adapter = new FaultCodeAdapter(this.troublebyCombineList, this);
        this.listview.setAdapter(this.adapter);
    }

    private void getDate() {
        int i;
        for (i = 0; i < this.dao.queryReport(this.cc, Contact.RELATION_ASK, MainActivity.database).size(); i++) {
        }
        this.sb = new StringBuffer();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (this.f1248k == 1) {
            this.sb.append("=====" + date + "=====");
            this.sb.append("\n\n");
        }
        System.err.println("\u6570\u636e\u603b\u6761\u6570  " + this.troublebyCombineList.size());
        for (i = 0; i < this.troublebyCombineList.size(); i++) {
            this.troubleTest = (SptTroubleTest) this.troublebyCombineList.get(i);
            this.sb.append("   " + this.troubleTest.getTroubleCodeContent() + "   " + this.troubleTest.getTroubleDescribeContent() + "   " + this.troubleTest.getTroubleStateContent() + "   " + "\n\n");
        }
    }

    private void saveImage() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(f1245D);
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
                FileOutputStream outStream = new FileOutputStream(path, f1245D);
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
        return Bitmap.createBitmap(target, 0, y, width, height - y, matrix, f1245D);
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
        myIntentFilter.addAction("SPT_TROUBLE_CODE_ID_EX_BY_COMBINE");
        registerReceiver(receiver, myIntentFilter);
    }

    protected void onResume() {
        super.onResume();
        this.isExecuteF = Boolean.valueOf(f1245D);
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
        return f1245D;
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.wenzi) {
            if (this.f1246i != 1) {
                Toast.makeText(this, getResources().getString(C0136R.string.save_report), 0).show();
            } else if (Environment.getExternalStorageState().equals("mounted")) {
                SaveTxt(this.sb.toString(), this.name, f1245D);
                this.f1246i++;
            } else {
                Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
            }
        } else if (v.getId() == C0136R.id.jietu) {
            if (this.f1247j == 1) {
                this.f1248k = 1;
                if (Environment.getExternalStorageState().equals("mounted")) {
                    saveImage();
                    this.f1247j++;
                    return;
                }
                Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
                return;
            }
            Toast.makeText(this, getResources().getString(C0136R.string.save_report), 0).show();
        } else if (v.getId() == C0136R.id.guanli) {
            this.f1248k = 1;
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
