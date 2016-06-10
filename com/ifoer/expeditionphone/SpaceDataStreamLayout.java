package com.ifoer.expeditionphone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.ifoer.mine.Contact;
import com.ifoer.util.MySharedPreferences;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
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

public class SpaceDataStreamLayout extends MySpaceManagermentLayout implements OnClickListener {
    private SpaceCurrentModeAdapter adapter;
    private View baseView;
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
    private ImageView jietu;
    private JniX431FileTest jnitest;
    private int f2135k;
    private ListView listview;
    String name;
    private String path;
    private boolean play;
    private String f2136s;
    private String sdCardDir;
    private List<Integer> selectedItem;
    private String serialNo;
    private ImageView showNextFrame;
    private String softPackageId;
    private ArrayList<SptExDataStreamIdItem> streamLists;
    private ImageView suspended;
    private Timer timer;
    private ImageView toright;
    private TextView total_number_of_frames;

    /* renamed from: com.ifoer.expeditionphone.SpaceDataStreamLayout.1 */
    class C06401 extends Handler {
        C06401() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i;
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    SpaceDataStreamLayout.this.current_frame_number.setText(String.valueOf(SpaceDataStreamLayout.this.currentFrame));
                    for (i = 0; i < SpaceDataStreamLayout.this.listview.getChildCount(); i++) {
                        ((Item) SpaceDataStreamLayout.this.listview.getChildAt(i).getTag()).jietu.setText(((SptExDataStreamIdItem) SpaceDataStreamLayout.this.streamLists.get(i)).getStreamStr());
                    }
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    SpaceDataStreamLayout.this.item = SpaceDataStreamLayout.this.jnitest.readDsDataFirstItemCount(SpaceDataStreamLayout.this.grp);
                    if (SpaceDataStreamLayout.this.item > 0) {
                        String[] textStrs = SpaceDataStreamLayout.this.jnitest.readDsDataFirstItemData(SpaceDataStreamLayout.this.grp, SpaceDataStreamLayout.this.cols, SpaceDataStreamLayout.this.item);
                        System.out.println("\u7b2c\u4e00\u5e27");
                        for (i = 0; i < SpaceDataStreamLayout.this.selectedItem.size(); i++) {
                            ((SptExDataStreamIdItem) SpaceDataStreamLayout.this.streamLists.get(i)).setStreamStr(textStrs[((Integer) SpaceDataStreamLayout.this.selectedItem.get(i)).intValue()]);
                        }
                        SpaceDataStreamLayout.this.handler.obtainMessage(0).sendToTarget();
                    }
                    SpaceDataStreamLayout.this.play = false;
                    SpaceDataStreamLayout.this.timer.cancel();
                    SpaceDataStreamLayout.this.timer = null;
                    SpaceDataStreamLayout.this.currentFrame = 1;
                    SpaceDataStreamLayout.this.suspended.setImageResource(C0136R.drawable.up);
                    SpaceDataStreamLayout.this.showNextFrame.setVisibility(0);
                    SpaceDataStreamLayout.this.toright.setVisibility(0);
                    SpaceDataStreamLayout.this.current_frame_number.setText(String.valueOf(SpaceDataStreamLayout.this.currentFrame));
                    SpaceDataStreamLayout.this.total_number_of_frames.setText(String.valueOf(SpaceDataStreamLayout.this.itemcount));
                default:
            }
        }
    }

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            if (!SpaceDataStreamLayout.this.play) {
                return;
            }
            if (SpaceDataStreamLayout.this.currentFrame >= SpaceDataStreamLayout.this.itemcount) {
                SpaceDataStreamLayout.this.handler.obtainMessage(1).sendToTarget();
            } else if (SpaceDataStreamLayout.this.item > 0) {
                String[] textStrs = SpaceDataStreamLayout.this.jnitest.readDsDataNextItemData(SpaceDataStreamLayout.this.grp, SpaceDataStreamLayout.this.cols, SpaceDataStreamLayout.this.item);
                System.out.println("\u4e0b\u4e00\u5e27");
                for (int i = 0; i < SpaceDataStreamLayout.this.selectedItem.size(); i++) {
                    ((SptExDataStreamIdItem) SpaceDataStreamLayout.this.streamLists.get(i)).setStreamStr(textStrs[((Integer) SpaceDataStreamLayout.this.selectedItem.get(i)).intValue()]);
                }
                SpaceDataStreamLayout spaceDataStreamLayout = SpaceDataStreamLayout.this;
                spaceDataStreamLayout.currentFrame = spaceDataStreamLayout.currentFrame + 1;
                SpaceDataStreamLayout.this.handler.obtainMessage(0).sendToTarget();
            }
        }
    }

    public SpaceDataStreamLayout(ArrayList<SptExDataStreamIdItem> streamLists, List<Integer> selectedItem, int id, String path, Context context) {
        super(context);
        this.jnitest = new JniX431FileTest();
        this.frameCount = 0;
        this.currentFrame = 1;
        this.play = false;
        this.softPackageId = XmlPullParser.NO_NAMESPACE;
        this.serialNo = XmlPullParser.NO_NAMESPACE;
        this.name = XmlPullParser.NO_NAMESPACE;
        this.dao = DBDao.getInstance(this.context);
        this.f2135k = 1;
        this.handler = new C06401();
        this.context = context;
        this.path = path;
        this.id = id;
        this.streamLists = streamLists;
        this.selectedItem = selectedItem;
        this.softPackageId = MySharedPreferences.getStringValue(context, MySharedPreferences.savesoftPackageId);
        this.serialNo = MySharedPreferences.getStringValue(context, MySharedPreferences.serialNoKey);
        this.name = this.softPackageId + "_DS_";
        int iStart = path.lastIndexOf(FilePathGenerator.ANDROID_DIR_SEP);
        int iEnd = path.indexOf("_");
        if (!(iStart == -1 || iEnd == -1)) {
            this.softPackageId = path.substring(iStart + 1, iEnd);
            this.name = this.softPackageId + "_DS_";
        }
        this.baseView = ((Activity) context).getLayoutInflater().inflate(C0136R.layout.space_current_mode, this);
        initTopView(this.baseView);
        setTopView(context, 0);
        init();
    }

    private void init() {
        this.suspended = (ImageView) this.baseView.findViewById(C0136R.id.suspended);
        this.showNextFrame = (ImageView) this.baseView.findViewById(C0136R.id.down);
        this.toright = (ImageView) this.baseView.findViewById(C0136R.id.toright);
        this.suspended.setOnClickListener(this);
        this.showNextFrame.setOnClickListener(this);
        this.toright.setOnClickListener(this);
        this.listview = (ListView) findViewById(C0136R.id.listview);
        this.current_frame_number = (TextView) findViewById(C0136R.id.current_frame_number);
        this.total_number_of_frames = (TextView) findViewById(C0136R.id.total_number_of_frames);
        this.jietu = (ImageView) this.baseView.findViewById(C0136R.id.jietu);
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
                System.out.println("\u7b2c\u4e00\u5e27");
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
                this.suspended.setImageResource(C0136R.drawable.up);
                this.play = false;
                this.showNextFrame.setVisibility(0);
                this.toright.setVisibility(0);
                return;
            }
            this.suspended.setImageResource(C0136R.drawable.start);
            this.play = true;
            this.showNextFrame.setVisibility(8);
            this.toright.setVisibility(4);
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
                    System.out.println("\u7b2c\u4e00\u5e27");
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
            MainActivity.panel.removePanelContainer();
            MainActivity.panel.fillPanelContainer(new SpaceCurrentModeLayout(this.id, this.path, this.context));
            MainActivity.panel.openthreePanelContainer();
        } else if (v.getId() == C0136R.id.jietu) {
            this.f2135k = 1;
            saveImage();
        }
    }

    private void saveImage() {
        View view = ((Activity) this.context).getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        saveBitmaps(zoomBitmap(view.getDrawingCache()));
        Toast.makeText(this.context, getResources().getString(C0136R.string.devfinish), 0).show();
    }

    public Bitmap zoomBitmap(Bitmap target) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) width) / ((float) width), ((float) height) / ((float) height));
        Rect frame = new Rect();
        ((Activity) this.context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int y = frame.top + 100;
        System.out.println("\u5934\u90e8\u7684\u9ad8\u5ea6" + y);
        return Bitmap.createBitmap(target, 0, y, width, height - y, matrix, true);
    }

    public void saveBitmaps(Bitmap bitmap) {
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
                this.dao.addReport(this.name + date + Util.PHOTO_DEFAULT_EXT, date, this.serialNo, this.sdCardDir + this.name + date + Util.PHOTO_DEFAULT_EXT, Contact.RELATION_ASK, MainActivity.database);
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
        Toast.makeText(this.context, getResources().getString(C0136R.string.sdcard), 0).show();
    }
}
