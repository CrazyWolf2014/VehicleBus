package com.ifoer.expeditionphone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.ShowFileAdapter;
import com.ifoer.entity.Constant;
import com.ifoer.entity.Diagnosisdatabase;
import com.ifoer.util.MyApplication;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowFileActivity extends BaseActivity implements OnClickListener {
    private static String f2131s;
    private TextView Invert;
    private ShowFileAdapter adapter;
    private Bitmap bm;
    protected LinearLayout car_maintain;
    private TextView check_all;
    private TextView del;
    private RelativeLayout guzhangma;
    private ImageView gz;
    private TextView gztext;
    private int imagesize;
    private LayoutInflater inflater;
    private LinearLayout inflater1;
    private List<Diagnosisdatabase> list;
    private ListView listview;
    private RelativeLayout menu;
    private int num;
    private String sdCardDir;
    private RelativeLayout shujuliu;
    private ImageView sj;
    private TextView sjtext;
    private String ss;

    /* renamed from: com.ifoer.expeditionphone.ShowFileActivity.1 */
    class C06311 implements OnItemClickListener {
        C06311() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            ShowFileActivity.this.sdCardDir = Constant.SDCARD_IMAGES_PATH;
            ShowFileActivity.this.ss = ((Diagnosisdatabase) ShowFileActivity.this.list.get((int) arg3)).getTitle().toString();
            ShowFileActivity.f2131s = ShowFileActivity.this.ss.substring(0, 17);
            Intent intent = new Intent(ShowFileActivity.this, ShowFileInfoActivity.class);
            intent.addFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            intent.putExtra("s", ShowFileActivity.f2131s);
            intent.putExtra("list", (Serializable) ShowFileActivity.this.list);
            intent.putExtra("i", (int) arg3);
            ShowFileActivity.this.startActivity(intent);
            ShowFileActivity.this.overridePendingTransition(0, 0);
            ShowFileActivity.this.adapter.setSelectedPosition(arg2);
            ShowFileActivity.this.adapter.notifyDataSetInvalidated();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ShowFileActivity.2 */
    class C06322 implements OnItemClickListener {
        C06322() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            ShowFileActivity.this.sdCardDir = Constant.SDCARD_IMAGES_PATH;
            ShowFileActivity.this.ss = ((Diagnosisdatabase) ShowFileActivity.this.list.get((int) arg3)).getTitle().toString();
            ShowFileActivity.f2131s = ShowFileActivity.this.ss.substring(0, 17);
            System.out.println(ShowFileActivity.f2131s + "\u56fe\u7247 \u76ee\u5f55");
            if (new File(new StringBuilder(String.valueOf(ShowFileActivity.this.sdCardDir)).append(ShowFileActivity.f2131s).toString()).exists()) {
                ShowFileActivity.this.getImagePath(new StringBuilder(String.valueOf(ShowFileActivity.this.sdCardDir)).append(ShowFileActivity.f2131s).toString());
                ShowFileActivity.this.imagesize = ShowFileActivity.this.getImagePath(new StringBuilder(String.valueOf(ShowFileActivity.this.sdCardDir)).append(ShowFileActivity.f2131s).toString()).size();
                System.out.println(new StringBuilder(String.valueOf(ShowFileActivity.this.imagesize)).append("\u6587\u4ef6\u7684\u6570\u91cf").toString());
                if (ShowFileActivity.this.imagesize == 1) {
                    ShowFileActivity.this.bm = BitmapFactory.decodeFile(new StringBuilder(String.valueOf(ShowFileActivity.this.sdCardDir)).append(((Diagnosisdatabase) ShowFileActivity.this.list.get((int) arg3)).getTitle()).toString());
                } else {
                    ShowFileActivity.this.bm = BitmapFactory.decodeFile(new StringBuilder(String.valueOf(ShowFileActivity.this.sdCardDir)).append(ShowFileActivity.f2131s).append(FilePathGenerator.ANDROID_DIR_SEP).append(ShowFileActivity.f2131s).append("_1.jpg").toString());
                }
            } else {
                ShowFileActivity.this.bm = BitmapFactory.decodeFile(new StringBuilder(String.valueOf(ShowFileActivity.this.sdCardDir)).append(((Diagnosisdatabase) ShowFileActivity.this.list.get((int) arg3)).getTitle()).toString());
            }
            ShowFileActivity.this.adapter.setSelectedPosition(arg2);
            ShowFileActivity.this.adapter.notifyDataSetInvalidated();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ShowFileActivity.3 */
    class C06333 implements OnItemClickListener {
        C06333() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            ShowFileActivity.this.sdCardDir = Constant.SDCARD_IMAGES_PATH;
            ShowFileActivity.this.ss = ((Diagnosisdatabase) ShowFileActivity.this.list.get((int) arg3)).getTitle().toString();
            ShowFileActivity.f2131s = ShowFileActivity.this.ss.substring(0, 17);
            Intent intent = new Intent(ShowFileActivity.this, ShowFileInfoActivity.class);
            intent.addFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            intent.putExtra("s", ShowFileActivity.f2131s);
            intent.putExtra("list", (Serializable) ShowFileActivity.this.list);
            intent.putExtra("i", (int) arg3);
            ShowFileActivity.this.startActivity(intent);
            ShowFileActivity.this.overridePendingTransition(0, 0);
        }
    }

    public ShowFileActivity() {
        this.list = new ArrayList();
        this.ss = null;
        this.imagesize = 0;
        this.num = 1;
        this.inflater = null;
    }

    static {
        f2131s = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.inflater = LayoutInflater.from(this);
        setContentView(C0136R.layout.show_file);
        MyApplication.getInstance().addActivity(this);
        initView();
        showGuZhangMa();
    }

    private void initView() {
        initLeftBtnNew(this, 0);
        this.listview = (ListView) findViewById(C0136R.id.view);
        this.menuBtn = (LinearLayout) findViewById(C0136R.id.menuBtn);
        this.menu = (RelativeLayout) findViewById(C0136R.id.main_leftmenu);
        this.gz = (ImageView) findViewById(C0136R.id.gz1);
        this.sj = (ImageView) findViewById(C0136R.id.sh1);
        this.gztext = (TextView) findViewById(C0136R.id.gztext);
        this.sjtext = (TextView) findViewById(C0136R.id.shtext);
        this.check_all = (TextView) findViewById(C0136R.id.check_all);
        this.Invert = (TextView) findViewById(C0136R.id.Invert);
        this.del = (TextView) findViewById(C0136R.id.del);
        this.guzhangma = (RelativeLayout) findViewById(C0136R.id.guzhangma);
        this.shujuliu = (RelativeLayout) findViewById(C0136R.id.shujuliu);
        this.inflater1 = (LinearLayout) findViewById(C0136R.id.top);
        this.check_all.setOnClickListener(this);
        this.Invert.setOnClickListener(this);
        this.del.setOnClickListener(this);
        this.guzhangma.setOnClickListener(this);
        this.shujuliu.setOnClickListener(this);
    }

    private List<Diagnosisdatabase> getImagePathFromSD() {
        Exception e;
        List<Diagnosisdatabase> picList = new ArrayList();
        if (Environment.getExternalStorageState().equals("mounted")) {
            this.sdCardDir = Constant.SDCARD_IMAGES_PATH;
            if (this.sdCardDir != null) {
                File mfile = new File(this.sdCardDir);
                if (!mfile.exists()) {
                    mfile.mkdirs();
                }
                File[] files = mfile.listFiles();
                if (files.length > 0) {
                    int i = 0;
                    while (true) {
                        int length = files.length;
                        if (i >= r0) {
                            return picList;
                        }
                        Diagnosisdatabase Diagnosisdatabase = new Diagnosisdatabase();
                        File file = files[i];
                        try {
                            int idx = file.getPath().lastIndexOf(".");
                            if (idx > 0) {
                                String suffix = file.getPath().substring(idx);
                                if (suffix.toLowerCase().equals(Util.PHOTO_DEFAULT_EXT) || suffix.toLowerCase().equals(".jpeg") || suffix.toLowerCase().equals(".bmp") || suffix.toLowerCase().equals(".png") || suffix.toLowerCase().equals(".gif")) {
                                    Diagnosisdatabase.setTitle(file.getName());
                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
                                    File file2 = new File(this.sdCardDir + file.getName());
                                    try {
                                        long modifiedTime = file2.lastModified();
                                        System.out.println(modifiedTime);
                                        Date d = new Date(modifiedTime);
                                        System.out.println(format.format(d));
                                        Diagnosisdatabase.setTime(format.format(d));
                                        picList.add(Diagnosisdatabase);
                                        file = file2;
                                    } catch (Exception e2) {
                                        e = e2;
                                        file = file2;
                                        e.printStackTrace();
                                        i++;
                                    }
                                }
                            }
                        } catch (Exception e3) {
                            e = e3;
                            e.printStackTrace();
                            i++;
                        }
                        i++;
                    }
                } else {
                    Toast.makeText(this, C0136R.string.file_null, 0).show();
                }
            }
        } else {
            Toast.makeText(this, C0136R.string.sdcard, 0).show();
        }
        return null;
    }

    private List<String> getImagePath(String path) {
        File mfile = new File(path);
        List<String> filePathes = new ArrayList();
        File[] files = mfile.listFiles();
        if (files.length <= 0) {
            return null;
        }
        for (File file : files) {
            if (file.getPath().lastIndexOf(".") > 0) {
                filePathes.add(file.getName());
            }
        }
        return filePathes;
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.guzhangma) {
            showGuZhangMa();
        } else if (v.getId() == C0136R.id.shujuliu) {
            this.gz.setImageResource(C0136R.drawable.guzhang1);
            this.gztext.setTextColor(getResources().getColor(C0136R.color.hui1));
            this.sj.setImageResource(C0136R.drawable.shuju2);
            this.sjtext.setTextColor(getResources().getColor(C0136R.color.jinhuang));
            this.list = getImagePathFromSD();
            if (this.list != null) {
                this.adapter = new ShowFileAdapter(this.list, this);
                this.listview.setAdapter(this.adapter);
                this.listview.setOnItemClickListener(new C06311());
            }
        } else if (v.getId() != C0136R.id.check_all && v.getId() != C0136R.id.Invert && v.getId() == C0136R.id.del) {
            if ((this.sdCardDir + f2131s).length() > 0) {
                try {
                    new File(this.sdCardDir + this.ss).delete();
                    deleteFolderFile(this.sdCardDir + f2131s, true);
                    if (this.list != null) {
                        this.adapter.notifyDataSetChanged();
                        Toast.makeText(this, C0136R.string.devfinish, 0).show();
                        this.list = getImagePathFromSD();
                        this.adapter = new ShowFileAdapter(this.list, this);
                        this.listview.setAdapter(this.adapter);
                        this.listview.setOnItemClickListener(new C06322());
                        return;
                    }
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(this, C0136R.string.log_del_file, 0).show();
        }
    }

    private void showGuZhangMa() {
        this.gz.setImageResource(C0136R.drawable.guzhang2);
        this.gztext.setTextColor(getResources().getColor(C0136R.color.jinhuang));
        this.sj.setImageResource(C0136R.drawable.shuju1);
        this.sjtext.setTextColor(getResources().getColor(C0136R.color.hui1));
        this.list = getImagePathFromSD();
        if (this.list != null) {
            this.adapter = new ShowFileAdapter(this.list, this);
            this.listview.setAdapter(this.adapter);
            this.listview.setDividerHeight(0);
            this.listview.setOnItemClickListener(new C06333());
        }
    }

    public void deleteFolderFile(String filePath, boolean deleteThisPath) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File absolutePath : files) {
                    deleteFolderFile(absolutePath.getAbsolutePath(), true);
                }
            }
            if (!deleteThisPath) {
                return;
            }
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.listFiles().length == 0) {
                file.delete();
            }
        }
    }
}
