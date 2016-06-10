package com.ifoer.expeditionphone;

import CRP.utils.CRPTools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.mycar.jni.JniX431FileTest;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.adapter.MostChartPlayAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.IntData;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.expedition.BluetoothChat.DataStreamUtils;
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

public class SpaceCurrentModeGraphLayout extends MySpaceManagermentLayout implements OnClickListener {
    private MostChartPlayAdapter adapter;
    private View baseView;
    private String cc;
    private int cols;
    private Context context;
    private int currentFrame;
    private TextView current_frame_number;
    DBDao dao;
    private int fileId;
    private int frameCount;
    private GridView gridview;
    private int grp;
    Handler handler;
    private int hlsx;
    private int id;
    private int item;
    private int itemcount;
    private ImageView jietu;
    private JniX431FileTest jnitest;
    private int f2134k;
    private ArrayList<IntData> listStr;
    private WindowManager manager;
    String name;
    private String path;
    private boolean play;
    private String sdCardDir;
    private List<Integer> selectedItem;
    private String serialNo;
    private ImageView showNextFrame;
    private String softPackageId;
    private List<ArrayList<?>> sptLists;
    private ArrayList<SptExDataStreamIdItem> streamLists;
    private ImageView suspended;
    private ArrayList<SptExDataStreamIdItem> tempStreamLists;
    private Timer timer;
    private TextView total_number_of_frames;

    /* renamed from: com.ifoer.expeditionphone.SpaceCurrentModeGraphLayout.1 */
    class C06351 extends Handler {
        C06351() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    SpaceCurrentModeGraphLayout.this.current_frame_number.setText(String.valueOf(SpaceCurrentModeGraphLayout.this.currentFrame));
                    SpaceCurrentModeGraphLayout.this.adapter.refresh(SpaceCurrentModeGraphLayout.this.sptLists, (double) SpaceCurrentModeGraphLayout.this.currentFrame);
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    SpaceCurrentModeGraphLayout.this.sptLists.clear();
                    SpaceCurrentModeGraphLayout.this.item = SpaceCurrentModeGraphLayout.this.jnitest.readDsDataFirstItemCount(SpaceCurrentModeGraphLayout.this.grp);
                    if (SpaceCurrentModeGraphLayout.this.item > 0) {
                        String[] textStrs = SpaceCurrentModeGraphLayout.this.jnitest.readDsDataFirstItemData(SpaceCurrentModeGraphLayout.this.grp, SpaceCurrentModeGraphLayout.this.cols, SpaceCurrentModeGraphLayout.this.item);
                        System.out.println("\u7b2c\u4e00\u5e27");
                        SpaceCurrentModeGraphLayout.this.tempStreamLists = new ArrayList();
                        for (int i = 0; i < SpaceCurrentModeGraphLayout.this.selectedItem.size(); i++) {
                            String streamStr = textStrs[((Integer) SpaceCurrentModeGraphLayout.this.selectedItem.get(i)).intValue()];
                            SptExDataStreamIdItem sptItem = new SptExDataStreamIdItem();
                            sptItem.setStreamTextIdContent(((SptExDataStreamIdItem) SpaceCurrentModeGraphLayout.this.streamLists.get(i)).getStreamTextIdContent());
                            sptItem.setStreamStr(streamStr);
                            sptItem.setStreamState(((SptExDataStreamIdItem) SpaceCurrentModeGraphLayout.this.streamLists.get(i)).getStreamStr());
                            SpaceCurrentModeGraphLayout.this.tempStreamLists.add(sptItem);
                        }
                        if (SpaceCurrentModeGraphLayout.this.sptLists.size() >= 30) {
                            SpaceCurrentModeGraphLayout.this.sptLists.remove(0);
                            SpaceCurrentModeGraphLayout.this.sptLists.add(SpaceCurrentModeGraphLayout.this.tempStreamLists);
                        } else {
                            SpaceCurrentModeGraphLayout.this.sptLists.add(SpaceCurrentModeGraphLayout.this.tempStreamLists);
                        }
                        SpaceCurrentModeGraphLayout.this.handler.obtainMessage(0).sendToTarget();
                    }
                    SpaceCurrentModeGraphLayout.this.play = false;
                    SpaceCurrentModeGraphLayout.this.timer.cancel();
                    SpaceCurrentModeGraphLayout.this.timer = null;
                    SpaceCurrentModeGraphLayout.this.currentFrame = 1;
                    SpaceCurrentModeGraphLayout.this.suspended.setImageResource(C0136R.drawable.up);
                    SpaceCurrentModeGraphLayout.this.showNextFrame.setVisibility(0);
                    SpaceCurrentModeGraphLayout.this.suspended.setNextFocusRightId(C0136R.id.down);
                    SpaceCurrentModeGraphLayout.this.jietu.setNextFocusLeftId(C0136R.id.down);
                    SpaceCurrentModeGraphLayout.this.current_frame_number.setText(String.valueOf(SpaceCurrentModeGraphLayout.this.currentFrame));
                    SpaceCurrentModeGraphLayout.this.total_number_of_frames.setText(String.valueOf(SpaceCurrentModeGraphLayout.this.itemcount));
                default:
            }
        }
    }

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            if (!SpaceCurrentModeGraphLayout.this.play) {
                return;
            }
            if (SpaceCurrentModeGraphLayout.this.currentFrame >= SpaceCurrentModeGraphLayout.this.itemcount) {
                SpaceCurrentModeGraphLayout.this.handler.obtainMessage(1).sendToTarget();
            } else if (SpaceCurrentModeGraphLayout.this.item > 0) {
                String[] textStrs = SpaceCurrentModeGraphLayout.this.jnitest.readDsDataNextItemData(SpaceCurrentModeGraphLayout.this.grp, SpaceCurrentModeGraphLayout.this.cols, SpaceCurrentModeGraphLayout.this.item);
                System.out.println("\u4e0b\u4e00\u5e27");
                SpaceCurrentModeGraphLayout.this.tempStreamLists = new ArrayList();
                for (int i = 0; i < SpaceCurrentModeGraphLayout.this.selectedItem.size(); i++) {
                    String streamStr = textStrs[((Integer) SpaceCurrentModeGraphLayout.this.selectedItem.get(i)).intValue()];
                    SptExDataStreamIdItem sptItem = new SptExDataStreamIdItem();
                    sptItem.setStreamTextIdContent(((SptExDataStreamIdItem) SpaceCurrentModeGraphLayout.this.streamLists.get(i)).getStreamTextIdContent());
                    sptItem.setStreamStr(streamStr);
                    sptItem.setStreamState(((SptExDataStreamIdItem) SpaceCurrentModeGraphLayout.this.streamLists.get(i)).getStreamStr());
                    SpaceCurrentModeGraphLayout.this.tempStreamLists.add(sptItem);
                }
                if (SpaceCurrentModeGraphLayout.this.sptLists.size() >= 30) {
                    SpaceCurrentModeGraphLayout.this.sptLists.remove(0);
                    SpaceCurrentModeGraphLayout.this.sptLists.add(SpaceCurrentModeGraphLayout.this.tempStreamLists);
                } else {
                    SpaceCurrentModeGraphLayout.this.sptLists.add(SpaceCurrentModeGraphLayout.this.tempStreamLists);
                }
                SpaceCurrentModeGraphLayout spaceCurrentModeGraphLayout = SpaceCurrentModeGraphLayout.this;
                spaceCurrentModeGraphLayout.currentFrame = spaceCurrentModeGraphLayout.currentFrame + 1;
                SpaceCurrentModeGraphLayout.this.handler.obtainMessage(0).sendToTarget();
            }
        }
    }

    public SpaceCurrentModeGraphLayout(int id, String path, List<Integer> selectedItem, ArrayList<SptExDataStreamIdItem> streamLists, Context context) {
        super(context);
        this.softPackageId = XmlPullParser.NO_NAMESPACE;
        this.serialNo = XmlPullParser.NO_NAMESPACE;
        this.name = XmlPullParser.NO_NAMESPACE;
        this.dao = DBDao.getInstance(this.context);
        this.f2134k = 1;
        this.sptLists = new ArrayList();
        this.jnitest = new JniX431FileTest();
        this.frameCount = 0;
        this.currentFrame = 1;
        this.play = false;
        this.listStr = new ArrayList();
        this.handler = new C06351();
        this.context = context;
        this.path = path;
        this.id = id;
        this.selectedItem = selectedItem;
        this.softPackageId = MySharedPreferences.getStringValue(context, MySharedPreferences.savesoftPackageId);
        this.serialNo = MySharedPreferences.getStringValue(context, MySharedPreferences.serialNoKey);
        this.name = this.softPackageId + "_DS_";
        for (int i = 0; i < selectedItem.size(); i++) {
            IntData intData = new IntData();
            intData.setItem(i);
            intData.setItemCheckedState(true);
            this.listStr.add(intData);
        }
        this.streamLists = streamLists;
        int iStart = path.lastIndexOf(FilePathGenerator.ANDROID_DIR_SEP);
        int iEnd = path.indexOf("_");
        if (!(iStart == -1 || iEnd == -1)) {
            this.softPackageId = path.substring(iStart + 1, iEnd);
            this.name = this.softPackageId + "_DS_";
        }
        this.baseView = ((Activity) context).getLayoutInflater().inflate(C0136R.layout.space_current_mode_graph, this);
        init();
    }

    private void init() {
        this.suspended = (ImageView) this.baseView.findViewById(C0136R.id.suspended);
        this.showNextFrame = (ImageView) this.baseView.findViewById(C0136R.id.down);
        this.jietu = (ImageView) this.baseView.findViewById(C0136R.id.jietu);
        this.jietu.setOnClickListener(this);
        this.suspended.setOnClickListener(this);
        this.showNextFrame.setOnClickListener(this);
        this.current_frame_number = (TextView) findViewById(C0136R.id.current_frame_number);
        this.total_number_of_frames = (TextView) findViewById(C0136R.id.total_number_of_frames);
        this.gridview = (GridView) findViewById(C0136R.id.gridView);
        if (this.selectedItem.size() < 2) {
            this.gridview.setNumColumns(this.selectedItem.size());
        } else {
            this.gridview.setNumColumns(2);
        }
        this.gridview.setGravity(17);
        this.manager = ((Activity) this.context).getWindowManager();
        if (new File(this.path).exists()) {
            this.hlsx = this.jnitest.init();
            this.fileId = this.jnitest.openFile(this.path, this.hlsx);
            this.grp = this.jnitest.readGroupId(this.fileId);
            this.itemcount = this.jnitest.readGroupItemCount(this.grp);
            this.cols = this.jnitest.readGroupItemColCount(this.grp);
            this.item = this.jnitest.readDsDataFirstItemCount(this.grp);
            if (this.item > 0) {
                String[] textStrs = this.jnitest.readDsDataFirstItemData(this.grp, this.cols, this.item);
                this.tempStreamLists = new ArrayList();
                for (int i = 0; i < this.selectedItem.size(); i++) {
                    String streamStr = textStrs[((Integer) this.selectedItem.get(i)).intValue()];
                    if (DataStreamUtils.isNum(streamStr)) {
                        SptExDataStreamIdItem sptItem = new SptExDataStreamIdItem();
                        sptItem.setStreamTextIdContent(((SptExDataStreamIdItem) this.streamLists.get(i)).getStreamTextIdContent());
                        sptItem.setStreamStr(streamStr);
                        sptItem.setStreamState(((SptExDataStreamIdItem) this.streamLists.get(i)).getStreamState());
                        this.tempStreamLists.add(sptItem);
                    }
                }
                if (this.sptLists.size() >= 30) {
                    this.sptLists.remove(0);
                    this.sptLists.add(this.tempStreamLists);
                } else {
                    this.sptLists.add(this.tempStreamLists);
                }
            }
        } else {
            Toast.makeText(this.context, C0136R.string.main_file_null, 1).show();
        }
        this.current_frame_number.setText(String.valueOf(this.currentFrame));
        this.total_number_of_frames.setText(String.valueOf(this.itemcount));
        this.adapter = new MostChartPlayAdapter(this.context, this.sptLists, (double) this.currentFrame, this.listStr, this.manager);
        this.gridview.setAdapter(this.adapter);
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.suspended) {
            if (this.play) {
                this.suspended.setImageResource(C0136R.drawable.right_bigen_selector);
                this.play = false;
                this.showNextFrame.setVisibility(0);
                this.suspended.setNextFocusRightId(C0136R.id.down);
                this.jietu.setNextFocusLeftId(C0136R.id.down);
                return;
            }
            this.suspended.setImageResource(C0136R.drawable.right_stop_focuses);
            this.play = true;
            this.showNextFrame.setVisibility(8);
            this.suspended.setNextFocusRightId(C0136R.id.jietu);
            this.jietu.setNextFocusLeftId(C0136R.id.suspended);
            if (this.timer == null) {
                this.timer = new Timer();
                this.timer.schedule(new MyTimerTask(), 1500, 1500);
            }
        } else if (v.getId() == C0136R.id.down) {
            String[] textStrs;
            int i;
            String streamStr;
            SptExDataStreamIdItem sptItem;
            if (this.currentFrame == this.itemcount) {
                this.sptLists.clear();
                this.currentFrame = 1;
                this.item = this.jnitest.readDsDataFirstItemCount(this.grp);
                if (this.item > 0) {
                    textStrs = this.jnitest.readDsDataFirstItemData(this.grp, this.cols, this.item);
                    System.out.println("\u7b2c\u4e00\u5e27");
                    this.tempStreamLists = new ArrayList();
                    for (i = 0; i < this.selectedItem.size(); i++) {
                        streamStr = textStrs[((Integer) this.selectedItem.get(i)).intValue()];
                        sptItem = new SptExDataStreamIdItem();
                        sptItem.setStreamTextIdContent(((SptExDataStreamIdItem) this.streamLists.get(i)).getStreamTextIdContent());
                        sptItem.setStreamStr(streamStr);
                        sptItem.setStreamState(((SptExDataStreamIdItem) this.streamLists.get(i)).getStreamStr());
                        this.tempStreamLists.add(sptItem);
                    }
                    if (this.sptLists.size() >= 30) {
                        this.sptLists.remove(0);
                        this.sptLists.add(this.tempStreamLists);
                    } else {
                        this.sptLists.add(this.tempStreamLists);
                    }
                    this.handler.obtainMessage(0).sendToTarget();
                    return;
                }
                return;
            }
            if (this.item > 0) {
                textStrs = this.jnitest.readDsDataNextItemData(this.grp, this.cols, this.item);
                this.tempStreamLists = new ArrayList();
                for (i = 0; i < this.selectedItem.size(); i++) {
                    streamStr = textStrs[((Integer) this.selectedItem.get(i)).intValue()];
                    sptItem = new SptExDataStreamIdItem();
                    sptItem.setStreamTextIdContent(((SptExDataStreamIdItem) this.streamLists.get(i)).getStreamTextIdContent());
                    sptItem.setStreamStr(streamStr);
                    sptItem.setStreamState(((SptExDataStreamIdItem) this.streamLists.get(i)).getStreamStr());
                    this.tempStreamLists.add(sptItem);
                }
                if (this.sptLists.size() >= 30) {
                    this.sptLists.remove(0);
                    this.sptLists.add(this.tempStreamLists);
                } else {
                    this.sptLists.add(this.tempStreamLists);
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
            this.f2134k = 1;
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
        System.out.println("\u5934\u90e8\u7684\u9ad8\u5ea6" + y);
        return Bitmap.createBitmap(target, 0, y, width, height - y, matrix, true);
    }

    public void saveBitmaps(Bitmap bitmap) {
        String path = XmlPullParser.NO_NAMESPACE;
        Log.e("SpaceCurrentModeGraphLayout nxy", "saveBitmaps");
        if (Environment.getExternalStorageState().equals("mounted")) {
            this.sdCardDir = Constant.DST_FILE;
            File dirFile = new File(this.sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            this.name = this.name.replaceAll("EOBD2", "EOBD");
            path = this.sdCardDir + this.name + date + Util.PHOTO_DEFAULT_EXT;
            File file = new File(path);
            if (file.exists()) {
                file.delete();
                file = new File(path);
            } else {
                String[] tempPaths = Constant.getDefaultExternalStoragePathList();
                if (tempPaths.length > 1) {
                    this.sdCardDir = this.sdCardDir.replaceAll(tempPaths[1], tempPaths[0]);
                }
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            file = new File(path);
            if (!file.exists()) {
                return;
            }
            if (file.length() <= 0) {
                file.delete();
                Log.e("SpaceCurrentModeGraphLayout", "\u5220\u9664\u6587\u4ef6");
                Toast.makeText(this.context, getResources().getString(C0136R.string.notfinish), 0).show();
                return;
            }
            Log.e("SpaceCurrentModeGraphLayout", "\u6dfb\u52a0\u8bb0\u5f55");
            this.dao.addReport(this.name + date + Util.PHOTO_DEFAULT_EXT, date, this.serialNo, path, Contact.RELATION_ASK, MainActivity.database);
            Toast.makeText(this.context, getResources().getString(C0136R.string.devfinish), 0).show();
            return;
        }
        Toast.makeText(this.context, getResources().getString(C0136R.string.sdcard), 0).show();
    }

    private boolean checksd(long filenght) {
        long sdcardUsableSize = CRPTools.getUsableSDCardSize();
        Log.e("SpaceCurrentModeGraphLayout nxy", "fileLenght" + filenght);
        Log.e("SpaceCurrentModeGraphLayout nxy", "sdcardUsableSize" + sdcardUsableSize);
        if (filenght <= sdcardUsableSize) {
            return true;
        }
        Toast.makeText(this.context, this.context.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("MainActivity", "on keydown " + keyCode);
        if (keyCode == 4) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
