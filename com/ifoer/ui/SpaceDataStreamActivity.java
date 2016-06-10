package com.ifoer.ui;

import CRP.utils.CRPTools;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.mycar.jni.JniX431FileTest;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.adapter.SpaceCurrentModeAdapter;
import com.ifoer.adapter.SpaceCurrentModeAdapter.Item;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class SpaceDataStreamActivity extends Activity implements OnClickListener {
    private SpaceCurrentModeAdapter adapter;
    String cc;
    private int cols;
    private Context context;
    private int currentFrame;
    private TextView current_frame_number;
    DBDao dao;
    private int fileId;
    private int frameCount;
    private int grp;
    Handler handler;
    private int hlsx;
    private int id;
    private int item;
    private int itemcount;
    private Button jietu;
    private JniX431FileTest jnitest;
    private int f1302k;
    private long lastTime;
    private ListView listview;
    public IntentFilter myIntentFilter;
    String name;
    private String path;
    private boolean play;
    public mBroadcastReceiver receiver;
    private String f1303s;
    private String sdCardDir;
    private List<Integer> selectedItem;
    private String serialNo;
    private Button showNextFrame;
    private String softPackageId;
    private ArrayList<SptExDataStreamIdItem> streamLists;
    private Button suspended;
    private Timer timer;
    private ImageView toright;
    private TextView total_number_of_frames;

    /* renamed from: com.ifoer.ui.SpaceDataStreamActivity.1 */
    class C07261 extends Handler {
        C07261() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i;
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    SpaceDataStreamActivity.this.current_frame_number.setText(String.valueOf(SpaceDataStreamActivity.this.currentFrame));
                    for (i = 0; i < SpaceDataStreamActivity.this.listview.getChildCount(); i++) {
                        ((Item) SpaceDataStreamActivity.this.listview.getChildAt(i).getTag()).jietu.setText(((SptExDataStreamIdItem) SpaceDataStreamActivity.this.streamLists.get(i)).getStreamStr());
                    }
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    SpaceDataStreamActivity.this.item = SpaceDataStreamActivity.this.jnitest.readDsDataFirstItemCount(SpaceDataStreamActivity.this.grp);
                    if (SpaceDataStreamActivity.this.item > 0) {
                        String[] textStrs = SpaceDataStreamActivity.this.jnitest.readDsDataFirstItemData(SpaceDataStreamActivity.this.grp, SpaceDataStreamActivity.this.cols, SpaceDataStreamActivity.this.item);
                        for (i = 0; i < SpaceDataStreamActivity.this.selectedItem.size(); i++) {
                            ((SptExDataStreamIdItem) SpaceDataStreamActivity.this.streamLists.get(i)).setStreamStr(textStrs[((Integer) SpaceDataStreamActivity.this.selectedItem.get(i)).intValue()]);
                        }
                        SpaceDataStreamActivity.this.handler.obtainMessage(0).sendToTarget();
                    }
                    SpaceDataStreamActivity.this.play = false;
                    SpaceDataStreamActivity.this.timer.cancel();
                    SpaceDataStreamActivity.this.timer = null;
                    SpaceDataStreamActivity.this.currentFrame = 1;
                    SpaceDataStreamActivity.this.suspended.setBackgroundResource(C0136R.drawable.right_bigen_selector);
                    SpaceDataStreamActivity.this.showNextFrame.setVisibility(0);
                    SpaceDataStreamActivity.this.toright.setVisibility(0);
                    SpaceDataStreamActivity.this.suspended.setNextFocusRightId(C0136R.id.down);
                    SpaceDataStreamActivity.this.jietu.setNextFocusLeftId(C0136R.id.down);
                    SpaceDataStreamActivity.this.current_frame_number.setText(String.valueOf(SpaceDataStreamActivity.this.currentFrame));
                    SpaceDataStreamActivity.this.total_number_of_frames.setText(String.valueOf(SpaceDataStreamActivity.this.itemcount));
                default:
            }
        }
    }

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            if (!SpaceDataStreamActivity.this.play) {
                return;
            }
            if (SpaceDataStreamActivity.this.currentFrame >= SpaceDataStreamActivity.this.itemcount) {
                SpaceDataStreamActivity.this.handler.obtainMessage(1).sendToTarget();
            } else if (SpaceDataStreamActivity.this.item > 0) {
                String[] textStrs = SpaceDataStreamActivity.this.jnitest.readDsDataNextItemData(SpaceDataStreamActivity.this.grp, SpaceDataStreamActivity.this.cols, SpaceDataStreamActivity.this.item);
                for (int i = 0; i < SpaceDataStreamActivity.this.selectedItem.size(); i++) {
                    ((SptExDataStreamIdItem) SpaceDataStreamActivity.this.streamLists.get(i)).setStreamStr(textStrs[((Integer) SpaceDataStreamActivity.this.selectedItem.get(i)).intValue()]);
                }
                SpaceDataStreamActivity spaceDataStreamActivity = SpaceDataStreamActivity.this;
                spaceDataStreamActivity.currentFrame = spaceDataStreamActivity.currentFrame + 1;
                SpaceDataStreamActivity.this.handler.obtainMessage(0).sendToTarget();
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        @SuppressLint({"ShowToast"})
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - SpaceDataStreamActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                SpaceDataStreamActivity.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    public SpaceDataStreamActivity() {
        this.jnitest = new JniX431FileTest();
        this.frameCount = 0;
        this.currentFrame = 1;
        this.play = false;
        this.softPackageId = XmlPullParser.NO_NAMESPACE;
        this.serialNo = XmlPullParser.NO_NAMESPACE;
        this.name = XmlPullParser.NO_NAMESPACE;
        this.dao = DBDao.getInstance(this.context);
        this.f1302k = 1;
        this.handler = new C07261();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.space_current_mode);
        registerBoradcastReceiver();
        this.context = this;
        this.path = getIntent().getStringExtra("path");
        this.id = getIntent().getIntExtra(LocaleUtil.INDONESIAN, 0);
        this.streamLists = (ArrayList) getIntent().getSerializableExtra("filterLists");
        this.selectedItem = (List) getIntent().getSerializableExtra("selectedItem");
        this.softPackageId = MySharedPreferences.getStringValue(this.context, MySharedPreferences.savesoftPackageId);
        this.serialNo = MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey);
        this.name = this.softPackageId + "_DS_";
        int iStart = this.path.lastIndexOf(FilePathGenerator.ANDROID_DIR_SEP);
        int iEnd = this.path.indexOf("_");
        if (!(iStart == -1 || iEnd == -1)) {
            this.softPackageId = this.path.substring(iStart + 1, iEnd);
            this.name = this.softPackageId + "_DS_";
        }
        init();
    }

    private void init() {
        this.suspended = (Button) findViewById(C0136R.id.suspended);
        this.showNextFrame = (Button) findViewById(C0136R.id.down);
        this.toright = (ImageView) findViewById(C0136R.id.toright);
        this.suspended.setOnClickListener(this);
        this.showNextFrame.setOnClickListener(this);
        this.toright.setOnClickListener(this);
        this.listview = (ListView) findViewById(C0136R.id.listview);
        this.current_frame_number = (TextView) findViewById(C0136R.id.current_frame_number);
        this.total_number_of_frames = (TextView) findViewById(C0136R.id.total_number_of_frames);
        this.jietu = (Button) findViewById(C0136R.id.jietu);
        this.jietu.setOnClickListener(this);
        if (new File(this.path).exists()) {
            this.hlsx = this.jnitest.init();
            this.fileId = this.jnitest.openFile(this.path, this.hlsx);
            this.grp = this.jnitest.readGroupId(this.fileId);
            this.itemcount = this.jnitest.readGroupItemCount(this.grp);
            this.cols = this.jnitest.readGroupItemColCount(this.grp);
            this.item = this.jnitest.readDsDataFirstItemCount(this.grp);
            if (this.item > 0) {
                String[] textStrs = this.jnitest.readDsDataFirstItemData(this.grp, this.cols, this.item);
                for (int i = 0; i < this.selectedItem.size(); i++) {
                    ((SptExDataStreamIdItem) this.streamLists.get(i)).setStreamStr(textStrs[((Integer) this.selectedItem.get(i)).intValue()]);
                }
                this.handler.obtainMessage(0).sendToTarget();
            }
        } else {
            Toast.makeText(this.context, C0136R.string.main_file_null, 1).show();
        }
        this.current_frame_number.setText(String.valueOf(this.currentFrame));
        this.total_number_of_frames.setText(String.valueOf(this.itemcount));
        this.adapter = new SpaceCurrentModeAdapter(this.context, this.streamLists);
        this.listview.setAdapter(this.adapter);
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.suspended) {
            if (this.play) {
                this.suspended.setBackgroundResource(C0136R.drawable.right_bigen_selector);
                this.play = false;
                this.showNextFrame.setVisibility(0);
                this.toright.setVisibility(0);
                this.suspended.setNextFocusRightId(C0136R.id.down);
                this.jietu.setNextFocusLeftId(C0136R.id.down);
                return;
            }
            this.suspended.setBackgroundResource(C0136R.drawable.right_stop_focuses);
            this.play = true;
            this.showNextFrame.setVisibility(8);
            this.toright.setVisibility(4);
            this.suspended.setNextFocusRightId(C0136R.id.jietu);
            this.jietu.setNextFocusLeftId(C0136R.id.suspended);
            if (this.timer == null) {
                this.timer = new Timer();
                this.timer.schedule(new MyTimerTask(), 1500, 1500);
            }
        } else if (v.getId() == C0136R.id.down) {
            String[] textStrs;
            int i;
            if (this.currentFrame == this.itemcount) {
                this.currentFrame = 1;
                this.item = this.jnitest.readDsDataFirstItemCount(this.grp);
                if (this.item > 0) {
                    textStrs = this.jnitest.readDsDataFirstItemData(this.grp, this.cols, this.item);
                    for (i = 0; i < this.selectedItem.size(); i++) {
                        ((SptExDataStreamIdItem) this.streamLists.get(i)).setStreamStr(textStrs[((Integer) this.selectedItem.get(i)).intValue()]);
                    }
                    this.handler.obtainMessage(0).sendToTarget();
                    return;
                }
                return;
            }
            if (this.item > 0) {
                textStrs = this.jnitest.readDsDataNextItemData(this.grp, this.cols, this.item);
                for (i = 0; i < this.selectedItem.size(); i++) {
                    ((SptExDataStreamIdItem) this.streamLists.get(i)).setStreamStr(textStrs[((Integer) this.selectedItem.get(i)).intValue()]);
                }
                this.handler.obtainMessage(0).sendToTarget();
            }
            this.currentFrame++;
        } else if (v.getId() == C0136R.id.toright) {
            this.jnitest.readEndCloseFile(this.fileId, this.hlsx);
            finish();
        } else if (v.getId() == C0136R.id.jietu) {
            this.f1302k = 1;
            saveImage();
        }
    }

    @SuppressLint({"NewApi"})
    private void saveImage() {
        View view = ((Activity) this.context).getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        if (checksd((long) bitmap.getByteCount())) {
            saveBitmaps(zoomBitmap(bitmap));
        }
        view.setDrawingCacheEnabled(false);
    }

    public Bitmap zoomBitmap(Bitmap target) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) width) / ((float) width), ((float) height) / ((float) height));
        Rect frame = new Rect();
        ((Activity) this.context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int y = frame.top + 100;
        return Bitmap.createBitmap(target, 0, y, width, height - y, matrix, true);
    }

    public void saveBitmaps(Bitmap bitmap) {
        String path = XmlPullParser.NO_NAMESPACE;
        if (Environment.getExternalStorageState().equals("mounted")) {
            this.sdCardDir = Constant.DST_FILE;
            File dirFile = new File(this.sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            File file = new File(this.sdCardDir, this.name + date + Util.PHOTO_DEFAULT_EXT);
            if (file.exists()) {
                file.delete();
                file = new File(this.sdCardDir, this.name + date + Util.PHOTO_DEFAULT_EXT);
            } else {
                String[] tempPaths = Constant.getDefaultExternalStoragePathList();
                if (tempPaths.length > 1) {
                    this.sdCardDir = this.sdCardDir.replaceAll(tempPaths[1], tempPaths[0]);
                }
                path = this.sdCardDir + this.name + date + Util.PHOTO_DEFAULT_EXT;
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                this.dao.addReport(this.name + date + Util.PHOTO_DEFAULT_EXT, date, this.serialNo, path, Contact.RELATION_ASK, MainActivity.database);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            file = new File(path);
            if (file != null && file.exists() && file.length() <= 0) {
                file.delete();
                Toast.makeText(this.context, getResources().getString(C0136R.string.notfinish), 0).show();
                return;
            } else if (file != null && file.exists() && file.length() > 0) {
                Toast.makeText(this.context, getResources().getString(C0136R.string.devfinish), 0).show();
                return;
            } else {
                return;
            }
        }
        Toast.makeText(this.context, getResources().getString(C0136R.string.sdcard), 0).show();
    }

    private boolean checksd(long filenght) {
        if (filenght <= CRPTools.getUsableSDCardSize()) {
            return true;
        }
        Toast.makeText(this.context, this.context.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
        return false;
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
}
