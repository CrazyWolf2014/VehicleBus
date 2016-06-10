package com.ifoer.expeditionphone;

import CRP.utils.CRPTools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.mycar.jni.JniX431FileTest;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.IntData;
import com.ifoer.entity.SpaceInfoRecord;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.expedition.BluetoothChat.DataStreamUtils;
import com.ifoer.mine.Contact;
import com.ifoer.util.DataStreamChartTaskManager;
import com.ifoer.util.DataStreamChartTaskManagerThread;
import com.ifoer.util.GraphView;
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
import org.xmlpull.v1.XmlPullParser;

public class SpaceSingleRecordShow extends Fragment implements OnClickListener {
    private int cols;
    private Context context;
    private int currentFrame;
    private TextView current_frame_number;
    DBDao dao;
    private int fileId;
    private int frameCount;
    private int grp;
    public Handler handler;
    private int hlsx;
    private SpaceInfoRecord info;
    private int item;
    private int itemcount;
    ImageView jietu;
    private JniX431FileTest jnitest;
    private int f2137k;
    private ArrayList<IntData> listdata;
    private ArrayList<?> lists;
    private GraphView mGraphView;
    private ArrayList<Integer> mListCheck;
    private int mSize;
    private DataStreamChartTaskManager mTaskManager;
    String name;
    private String path;
    private boolean play;
    private String sdCardDir;
    private List<Integer> selectedItem;
    private String serialNo;
    ImageView showNextFrame;
    private String softPackageId;
    private List<ArrayList<?>> sptLists;
    private ArrayList<SptExDataStreamIdItem> streamLists;
    ImageView suspended;
    private ArrayList<SptExDataStreamIdItem> tempStreamLists;
    private Timer timer;
    private int times;
    private TextView total_number_of_frames;
    private List<ArrayList<?>> typeLists;
    View view;
    private ArrayList<SptVwDataStreamIdItem> vwStreamLists;

    /* renamed from: com.ifoer.expeditionphone.SpaceSingleRecordShow.1 */
    class C06471 extends Handler {
        C06471() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    SpaceSingleRecordShow.this.sptLists.clear();
                    SpaceSingleRecordShow.this.times = 0;
                    SpaceSingleRecordShow.this.item = SpaceSingleRecordShow.this.jnitest.readDsDataFirstItemCount(SpaceSingleRecordShow.this.grp);
                    if (SpaceSingleRecordShow.this.item > 0) {
                        String[] textStrs = SpaceSingleRecordShow.this.jnitest.readDsDataFirstItemData(SpaceSingleRecordShow.this.grp, SpaceSingleRecordShow.this.cols, SpaceSingleRecordShow.this.item);
                        System.out.println("\u7b2c\u4e00\u5e27");
                        SpaceSingleRecordShow.this.tempStreamLists = new ArrayList();
                        for (int i = 0; i < SpaceSingleRecordShow.this.selectedItem.size(); i++) {
                            String streamStr = textStrs[((Integer) SpaceSingleRecordShow.this.selectedItem.get(i)).intValue()];
                            SptExDataStreamIdItem sptItem = new SptExDataStreamIdItem();
                            sptItem.setStreamTextIdContent(((SptExDataStreamIdItem) SpaceSingleRecordShow.this.streamLists.get(i)).getStreamTextIdContent());
                            sptItem.setStreamStr(streamStr);
                            sptItem.setStreamState(((SptExDataStreamIdItem) SpaceSingleRecordShow.this.streamLists.get(i)).getStreamStr());
                            SpaceSingleRecordShow.this.tempStreamLists.add(sptItem);
                        }
                        if (SpaceSingleRecordShow.this.sptLists.size() >= 30) {
                            SpaceSingleRecordShow.this.sptLists.remove(0);
                            SpaceSingleRecordShow.this.sptLists.add(SpaceSingleRecordShow.this.tempStreamLists);
                        } else {
                            SpaceSingleRecordShow.this.sptLists.add(SpaceSingleRecordShow.this.tempStreamLists);
                        }
                        SpaceSingleRecordShow.this.handler.obtainMessage(19).sendToTarget();
                    }
                    SpaceSingleRecordShow.this.current_frame_number.setText(String.valueOf(SpaceSingleRecordShow.this.currentFrame));
                case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                    SpaceSingleRecordShow.this.times = SpaceSingleRecordShow.this.currentFrame;
                    SpaceSingleRecordShow.this.current_frame_number.setText(String.valueOf(SpaceSingleRecordShow.this.currentFrame));
                    if (SpaceSingleRecordShow.this.sptLists != null && SpaceSingleRecordShow.this.sptLists.size() > 0) {
                        SpaceSingleRecordShow.this.mGraphView.pushDataToChartCombine(SpaceSingleRecordShow.this.mSize, SpaceSingleRecordShow.this.sptLists, (double) SpaceSingleRecordShow.this.times, SpaceSingleRecordShow.this.mListCheck);
                    }
                case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                    SpaceSingleRecordShow.this.play = false;
                    if (SpaceSingleRecordShow.this.timer != null) {
                        SpaceSingleRecordShow.this.timer.cancel();
                        SpaceSingleRecordShow.this.timer = null;
                    }
                    SpaceSingleRecordShow.this.currentFrame = 1;
                    SpaceSingleRecordShow.this.current_frame_number.setText(String.valueOf(SpaceSingleRecordShow.this.currentFrame));
                    SpaceSingleRecordShow.this.suspended.setImageResource(C0136R.drawable.right_bigen_selector);
                    SpaceSingleRecordShow.this.showNextFrame.setVisibility(0);
                    SpaceSingleRecordShow.this.suspended.setNextFocusRightId(C0136R.id.down);
                    SpaceSingleRecordShow.this.jietu.setNextFocusLeftId(C0136R.id.down);
                    SpaceSingleRecordShow.this.times = SpaceSingleRecordShow.this.currentFrame;
                    if (SpaceSingleRecordShow.this.sptLists != null && SpaceSingleRecordShow.this.sptLists.size() > 0) {
                        SpaceSingleRecordShow.this.mGraphView.pushDataToChartCombine(SpaceSingleRecordShow.this.mSize, SpaceSingleRecordShow.this.sptLists, (double) SpaceSingleRecordShow.this.times, SpaceSingleRecordShow.this.mListCheck);
                    }
                default:
            }
        }
    }

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            SpaceSingleRecordShow.this.tempStreamLists = new ArrayList();
            if (!SpaceSingleRecordShow.this.play) {
                return;
            }
            if (SpaceSingleRecordShow.this.currentFrame >= SpaceSingleRecordShow.this.itemcount) {
                SpaceSingleRecordShow.this.handler.obtainMessage(1).sendToTarget();
            } else if (SpaceSingleRecordShow.this.item > 0) {
                String[] textStrs = SpaceSingleRecordShow.this.jnitest.readDsDataNextItemData(SpaceSingleRecordShow.this.grp, SpaceSingleRecordShow.this.cols, SpaceSingleRecordShow.this.item);
                System.out.println("\u4e0b\u4e00\u5e27");
                for (int i = 0; i < SpaceSingleRecordShow.this.selectedItem.size(); i++) {
                    try {
                        String streamStr = textStrs[((Integer) SpaceSingleRecordShow.this.selectedItem.get(i)).intValue()];
                        SptExDataStreamIdItem sptItem = new SptExDataStreamIdItem();
                        sptItem.setStreamTextIdContent(((SptExDataStreamIdItem) SpaceSingleRecordShow.this.streamLists.get(i)).getStreamTextIdContent());
                        sptItem.setStreamStr(streamStr);
                        sptItem.setStreamState(((SptExDataStreamIdItem) SpaceSingleRecordShow.this.streamLists.get(i)).getStreamStr());
                        SpaceSingleRecordShow.this.tempStreamLists.add(sptItem);
                    } catch (Exception e) {
                        SpaceSingleRecordShow.this.handler.sendEmptyMessage(19);
                    }
                }
                if (SpaceSingleRecordShow.this.sptLists.size() >= 30) {
                    SpaceSingleRecordShow.this.sptLists.remove(0);
                    SpaceSingleRecordShow.this.sptLists.add(SpaceSingleRecordShow.this.tempStreamLists);
                } else {
                    SpaceSingleRecordShow.this.sptLists.add(SpaceSingleRecordShow.this.tempStreamLists);
                }
                SpaceSingleRecordShow spaceSingleRecordShow = SpaceSingleRecordShow.this;
                spaceSingleRecordShow.currentFrame = spaceSingleRecordShow.currentFrame + 1;
                SpaceSingleRecordShow.this.handler.obtainMessage(18).sendToTarget();
            }
        }
    }

    public SpaceSingleRecordShow() {
        this.sptLists = new ArrayList();
        this.typeLists = new ArrayList();
        this.vwStreamLists = new ArrayList();
        this.mListCheck = new ArrayList();
        this.listdata = new ArrayList();
        this.jnitest = new JniX431FileTest();
        this.frameCount = 0;
        this.currentFrame = 1;
        this.play = false;
        this.times = 0;
        this.softPackageId = XmlPullParser.NO_NAMESPACE;
        this.serialNo = XmlPullParser.NO_NAMESPACE;
        this.name = this.softPackageId + "_DS_";
        this.dao = DBDao.getInstance(this.context);
        this.f2137k = 1;
        this.handler = new C06471();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        this.view = inflater.inflate(C0136R.layout.space_single_grap, null);
        this.context = getActivity();
        bundle = getArguments();
        this.softPackageId = MySharedPreferences.getStringValue(this.context, MySharedPreferences.savesoftPackageId);
        this.serialNo = MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey);
        this.info = (SpaceInfoRecord) bundle.getSerializable("info");
        this.path = this.info.getPath();
        this.selectedItem = this.info.getSeletedId();
        this.streamLists = this.info.getFilterLists();
        DataStreamChartTaskManager.getInstance();
        new Thread(new DataStreamChartTaskManagerThread()).start();
        this.mTaskManager = DataStreamChartTaskManager.getInstance();
        this.suspended = (ImageView) this.view.findViewById(C0136R.id.suspended);
        this.showNextFrame = (ImageView) this.view.findViewById(C0136R.id.down);
        this.jietu = (ImageView) this.view.findViewById(C0136R.id.jietu);
        this.jietu.setOnClickListener(this);
        this.suspended.setOnClickListener(this);
        this.showNextFrame.setOnClickListener(this);
        this.total_number_of_frames = (TextView) this.view.findViewById(C0136R.id.total_number_of_frames);
        this.current_frame_number = (TextView) this.view.findViewById(C0136R.id.current_frame_number);
        init();
        return this.view;
    }

    public void init() {
        int i;
        this.mListCheck.clear();
        for (i = 0; i < this.info.getSeletedId().size(); i++) {
            IntData intData = new IntData();
            intData.setItem(i);
            intData.setItemCheckedState(true);
            this.listdata.add(intData);
        }
        if (new File(this.path).exists()) {
            this.hlsx = this.jnitest.init();
            this.fileId = this.jnitest.openFile(this.path, this.hlsx);
            this.grp = this.jnitest.readGroupId(this.fileId);
            this.itemcount = this.jnitest.readGroupItemCount(this.grp);
            this.cols = this.jnitest.readGroupItemColCount(this.grp);
            this.item = this.jnitest.readDsDataFirstItemCount(this.grp);
            this.total_number_of_frames.setText(String.valueOf(this.itemcount));
            if (this.item > 0) {
                String[] textStrs = this.jnitest.readDsDataFirstItemData(this.grp, this.cols, this.item);
                this.tempStreamLists = new ArrayList();
                for (i = 0; i < this.selectedItem.size(); i++) {
                    String streamStr = textStrs[((Integer) this.selectedItem.get(i)).intValue()];
                    if (DataStreamUtils.isNum(streamStr)) {
                        SptExDataStreamIdItem sptItem = new SptExDataStreamIdItem();
                        sptItem.setStreamTextIdContent(((SptExDataStreamIdItem) this.streamLists.get(i)).getStreamTextIdContent());
                        sptItem.setStreamStr(streamStr);
                        sptItem.setStreamState(((SptExDataStreamIdItem) this.streamLists.get(i)).getStreamState());
                        this.tempStreamLists.add(sptItem);
                    }
                }
                this.handler.obtainMessage(18).sendToTarget();
            }
        } else {
            Toast.makeText(this.context, C0136R.string.main_file_null, 1).show();
        }
        this.softPackageId = this.path.substring(this.path.lastIndexOf(FilePathGenerator.ANDROID_DIR_SEP) + 1, this.path.indexOf("_"));
        this.name = this.softPackageId + "_DS_";
        this.current_frame_number.setText(String.valueOf(this.currentFrame));
        this.mSize = this.listdata.size();
        String[] titles = new String[this.mSize];
        String[] units = new String[this.mSize];
        for (i = 0; i < this.mSize; i++) {
            titles[i] = new String(((SptExDataStreamIdItem) this.tempStreamLists.get(((IntData) this.listdata.get(i)).getItem())).getStreamTextIdContent());
            units[i] = ((SptExDataStreamIdItem) this.tempStreamLists.get(((IntData) this.listdata.get(i)).getItem())).getStreamState();
            this.mListCheck.add(Integer.valueOf(((IntData) this.listdata.get(i)).getItem()));
        }
        LinearLayout layout = (LinearLayout) this.view.findViewById(C0136R.id.chart);
        this.mGraphView = null;
        this.mGraphView = new GraphView(this.context, layout, AsyncTaskManager.REQUEST_SUCCESS_CODE, AsyncTaskManager.REQUEST_SUCCESS_CODE, titles, units, this.mListCheck);
        this.mGraphView.setBackgroundDrawable(null);
        layout.addView(this.mGraphView, new LayoutParams(-1, -1));
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
            if (this.timer == null) {
                this.timer = new Timer();
                this.timer.schedule(new MyTimerTask(), 1500, 1500);
            }
            this.suspended.setNextFocusRightId(C0136R.id.jietu);
            this.jietu.setNextFocusLeftId(C0136R.id.suspended);
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
                    this.handler.obtainMessage(19).sendToTarget();
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
                this.handler.obtainMessage(18).sendToTarget();
            }
            this.currentFrame++;
        } else if (v.getId() == C0136R.id.jietu) {
            this.f2137k = 1;
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
        if (Environment.getExternalStorageState().equals("mounted")) {
            this.sdCardDir = Constant.DST_FILE;
            File dirFile = new File(this.sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            this.name = this.name.replaceAll("EOBD2", "EOBD");
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
                Toast.makeText(this.context, getResources().getString(C0136R.string.devfinish), 0).show();
                return;
            } catch (FileNotFoundException e) {
                Toast.makeText(this.context, getResources().getString(C0136R.string.notfinish), 0).show();
                e.printStackTrace();
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                Toast.makeText(this.context, getResources().getString(C0136R.string.notfinish), 0).show();
                return;
            }
        }
        Toast.makeText(this.context, getResources().getString(C0136R.string.sdcard), 0).show();
    }

    private boolean checksd(long filenght) {
        long sdcardUsableSize = CRPTools.getUsableSDCardSize();
        Log.e("SpaceSingleRecordShow nxy", "fileLenght" + filenght);
        Log.e("SpaceSingleRecordShow nxy", "sdcardUsableSize" + sdcardUsableSize);
        if (filenght <= sdcardUsableSize) {
            return true;
        }
        Toast.makeText(this.context, this.context.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
        return false;
    }
}
