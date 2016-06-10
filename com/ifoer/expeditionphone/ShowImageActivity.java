package com.ifoer.expeditionphone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.entity.Diagnosisdatabase;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.view.MenuHorizontalScrollView;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowImageActivity extends RelativeLayout implements OnClickListener {
    private static String f1294s;
    private Bitmap bm;
    protected LinearLayout car_maintain;
    private View circleView;
    private Context context;
    private ImageView del;
    private int f1295i;
    private ImageView image;
    private int imagesize;
    private LayoutInflater inflater;
    private ImageView left;
    private List<Diagnosisdatabase> list;
    private RelativeLayout menu;
    public Button menuBtn;
    private int num;
    private ImageView returnBtn;
    private ImageView right;
    private MenuHorizontalScrollView scrollView;
    private String sdCardDir;
    private ImageView share;
    private String ss;

    /* renamed from: com.ifoer.expeditionphone.ShowImageActivity.1 */
    class C06341 implements OnClickListener {
        C06341() {
        }

        public void onClick(View v) {
            MainActivity.panel.removePanelContainer();
            MainActivity.panel.fillPanelContainer(new DiagnoseLogActivity(ShowImageActivity.this.context));
            MainActivity.panel.openthreePanelContainer();
        }
    }

    public ShowImageActivity(Context context, HashMap<String, Object> valuseMap) {
        super(context);
        this.sdCardDir = Constant.SDCARD_IMAGES_PATH;
        this.ss = null;
        this.imagesize = 0;
        this.num = 1;
        this.f1295i = 0;
        this.list = new ArrayList();
        this.inflater = null;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setInitValue(valuseMap);
        initView();
    }

    static {
        f1294s = null;
    }

    private void setInitValue(HashMap<String, Object> valuseMap) {
        if (valuseMap != null) {
            f1294s = (String) valuseMap.get("s");
            this.f1295i = ((Integer) valuseMap.get("i")).intValue();
            this.list = (List) valuseMap.get("list");
        }
    }

    private void initView() {
        this.scrollView = (MenuHorizontalScrollView) findViewById(C0136R.id.scrollView);
        this.circleView = this.inflater.inflate(C0136R.layout.show_image, null);
        this.returnBtn = (ImageView) this.circleView.findViewById(C0136R.id.returnBtn);
        this.returnBtn.setOnClickListener(new C06341());
        this.circleView.setBackgroundDrawable(MySharedPreferences.getBgId(this.context));
        addView(this.circleView, new LayoutParams(-1, -1));
        this.menuBtn = (Button) this.circleView.findViewById(C0136R.id.menuBtn);
        this.menu = (RelativeLayout) findViewById(C0136R.id.main_leftmenu);
        this.ss = ((Diagnosisdatabase) this.list.get(this.f1295i)).getTitle().toString();
        this.image = (ImageView) this.circleView.findViewById(C0136R.id.image);
        this.left = (ImageView) this.circleView.findViewById(C0136R.id.left);
        this.right = (ImageView) this.circleView.findViewById(C0136R.id.right);
        this.del = (ImageView) this.circleView.findViewById(C0136R.id.del);
        this.share = (ImageView) this.circleView.findViewById(C0136R.id.share);
        this.del.setOnClickListener(this);
        this.share.setOnClickListener(this);
        this.left.setVisibility(8);
        this.right.setVisibility(8);
        this.left.setOnClickListener(this);
        this.right.setOnClickListener(this);
        if (new File(this.sdCardDir + f1294s).exists()) {
            getImagePath(this.sdCardDir + f1294s);
            this.imagesize = getImagePath(this.sdCardDir + f1294s).size();
            System.out.println(this.imagesize + "\u6587\u4ef6\u7684\u6570\u91cf");
            if (this.imagesize > 1) {
                this.left.setVisibility(0);
                this.right.setVisibility(0);
            } else {
                this.left.setVisibility(8);
                this.right.setVisibility(8);
            }
            if (this.imagesize == 1) {
                this.bm = BitmapFactory.decodeFile(this.sdCardDir + ((Diagnosisdatabase) this.list.get(this.f1295i)).getTitle());
            } else {
                this.bm = BitmapFactory.decodeFile(this.sdCardDir + f1294s + FilePathGenerator.ANDROID_DIR_SEP + f1294s + "_1.jpg");
            }
        } else {
            this.left.setVisibility(8);
            this.right.setVisibility(8);
            this.bm = BitmapFactory.decodeFile(this.sdCardDir + ((Diagnosisdatabase) this.list.get(this.f1295i)).getTitle());
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
                this.bm = BitmapFactory.decodeFile(this.sdCardDir + f1294s + FilePathGenerator.ANDROID_DIR_SEP + f1294s + "_" + this.num + Util.PHOTO_DEFAULT_EXT);
                this.image.setImageBitmap(this.bm);
            }
        } else if (v.getId() == C0136R.id.right) {
            if (this.num < this.imagesize) {
                this.num++;
                this.bm = BitmapFactory.decodeFile(this.sdCardDir + f1294s + FilePathGenerator.ANDROID_DIR_SEP + f1294s + "_" + this.num + Util.PHOTO_DEFAULT_EXT);
                this.image.setImageBitmap(this.bm);
            }
        } else if (v.getId() != C0136R.id.share && v.getId() == C0136R.id.del) {
            if ((this.sdCardDir + f1294s).length() > 0) {
                new File(this.sdCardDir + this.ss).delete();
                try {
                    deleteFolderFile(this.sdCardDir + f1294s, true);
                    Toast.makeText(this.context, C0136R.string.devfinish, 0).show();
                    this.image.setImageBitmap(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MainActivity.panel.removePanelContainer();
                MainActivity.panel.fillPanelContainer(new DiagnoseLogActivity(this.context));
                MainActivity.panel.openthreePanelContainer();
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
