package com.ifoer.expeditionphone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.entity.Diagnosisdatabase;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class ShowFileInfoActivity extends BaseActivity implements OnClickListener {
    private static String f2132s;
    private Bitmap bm;
    private Bundle bundle;
    protected LinearLayout car_maintain;
    private LinearLayout circleLay;
    private Context context;
    private LinearLayout dataLay;
    private ImageView del;
    private int f2133i;
    private ImageView image;
    private int imagesize;
    private LayoutInflater inflater;
    private LinearLayout inforLay;
    private Intent intent;
    private ImageView left;
    private List<Diagnosisdatabase> list;
    private RelativeLayout menu;
    private LinearLayout moreLay;
    private LinearLayout mySpaceLay;
    private int num;
    private ImageView right;
    private String sdCardDir;
    private ImageView share;
    private TextView show_name;
    private String ss;
    private LinearLayout userLay;

    public ShowFileInfoActivity() {
        this.sdCardDir = Constant.SDCARD_IMAGES_PATH;
        this.ss = null;
        this.imagesize = 0;
        this.num = 1;
        this.f2133i = 0;
        this.list = new ArrayList();
        this.inflater = null;
    }

    static {
        f2132s = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.inflater = LayoutInflater.from(this);
        setContentView(C0136R.layout.show_file_info);
        MyApplication.getInstance().addActivity(this);
        this.context = this;
        initView();
    }

    private void setInitValue(HashMap<String, Object> valuseMap) {
        if (valuseMap != null) {
            f2132s = (String) valuseMap.get("s");
            this.f2133i = ((Integer) valuseMap.get("i")).intValue();
            this.list = (List) valuseMap.get("list");
        }
    }

    private void initView() {
        initLeftBtnNew(this.context, 0);
        Intent intent = getIntent();
        if (intent != null) {
            f2132s = intent.getStringExtra("s");
            this.f2133i = Integer.valueOf(intent.getIntExtra("i", 0)).intValue();
            this.list = (List) intent.getSerializableExtra("list");
        }
        this.menuBtn = (LinearLayout) findViewById(C0136R.id.menuBtn);
        this.menu = (RelativeLayout) findViewById(C0136R.id.main_leftmenu);
        this.show_name = (TextView) findViewById(C0136R.id.show_name);
        this.show_name.setVisibility(0);
        String name = MySharedPreferences.getStringValue(this.context, "usernames");
        if (!(name == null || XmlPullParser.NO_NAMESPACE.equals(name))) {
            this.show_name.setText(name);
        }
        this.ss = ((Diagnosisdatabase) this.list.get(this.f2133i)).getTitle().toString();
        this.image = (ImageView) findViewById(C0136R.id.image);
        this.left = (ImageView) findViewById(C0136R.id.left);
        this.right = (ImageView) findViewById(C0136R.id.right);
        this.del = (ImageView) findViewById(C0136R.id.del);
        this.share = (ImageView) findViewById(C0136R.id.share);
        this.del.setOnClickListener(this);
        this.share.setOnClickListener(this);
        this.left.setVisibility(8);
        this.right.setVisibility(8);
        this.left.setOnClickListener(this);
        this.right.setOnClickListener(this);
        if (new File(this.sdCardDir + f2132s).exists()) {
            getImagePath(this.sdCardDir + f2132s);
            this.imagesize = getImagePath(this.sdCardDir + f2132s).size();
            System.out.println(this.imagesize + "\u6587\u4ef6\u7684\u6570\u91cf");
            if (this.imagesize > 1) {
                this.left.setVisibility(0);
                this.right.setVisibility(0);
            } else {
                this.left.setVisibility(8);
                this.right.setVisibility(8);
            }
            if (this.imagesize == 1) {
                this.bm = BitmapFactory.decodeFile(this.sdCardDir + ((Diagnosisdatabase) this.list.get(this.f2133i)).getTitle());
            } else {
                this.bm = BitmapFactory.decodeFile(this.sdCardDir + f2132s + FilePathGenerator.ANDROID_DIR_SEP + f2132s + "_1.jpg");
            }
        } else {
            this.left.setVisibility(8);
            this.right.setVisibility(8);
            this.bm = BitmapFactory.decodeFile(this.sdCardDir + ((Diagnosisdatabase) this.list.get(this.f2133i)).getTitle());
        }
        this.image.setImageBitmap(this.bm);
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
        if (v.getId() == C0136R.id.left) {
            if (this.num > 1) {
                this.num--;
                this.bm = BitmapFactory.decodeFile(this.sdCardDir + f2132s + FilePathGenerator.ANDROID_DIR_SEP + f2132s + "_" + this.num + Util.PHOTO_DEFAULT_EXT);
                this.image.setImageBitmap(this.bm);
            }
        } else if (v.getId() == C0136R.id.right) {
            if (this.num < this.imagesize) {
                this.num++;
                this.bm = BitmapFactory.decodeFile(this.sdCardDir + f2132s + FilePathGenerator.ANDROID_DIR_SEP + f2132s + "_" + this.num + Util.PHOTO_DEFAULT_EXT);
                this.image.setImageBitmap(this.bm);
            }
        } else if (v.getId() != C0136R.id.share && v.getId() == C0136R.id.del) {
            if ((this.sdCardDir + f2132s).length() > 0) {
                new File(this.sdCardDir + this.ss).delete();
                try {
                    deleteFolderFile(this.sdCardDir + f2132s, true);
                    Toast.makeText(this.context, C0136R.string.devfinish, 0).show();
                    this.image.setImageBitmap(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
                return;
            }
            Toast.makeText(this.context, C0136R.string.log_del_file, 0).show();
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
