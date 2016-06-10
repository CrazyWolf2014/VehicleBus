package com.cnmobi.im;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.cropImage.CropImageActivity;
import com.cnmobi.im.util.FileConstant;
import java.io.File;

public class PicMenuDialog extends Activity {
    private Button cancelBtn;
    private Button localPhotosBtn;
    private Button photographBtn;

    /* renamed from: com.cnmobi.im.PicMenuDialog.1 */
    class C01781 implements OnClickListener {
        C01781() {
        }

        public void onClick(View arg0) {
            PicMenuDialog.this.finish();
        }
    }

    /* renamed from: com.cnmobi.im.PicMenuDialog.2 */
    class C01792 implements OnClickListener {
        C01792() {
        }

        public void onClick(View arg0) {
            if (Environment.getExternalStorageState().equals("mounted")) {
                String name = FileConstant.FILE_HEAD_PORTRAIT + "head portrait";
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra("output", Uri.fromFile(new File(name)));
                PicMenuDialog.this.startActivityForResult(intent, 0);
                return;
            }
            Toast.makeText(PicMenuDialog.this, C0136R.string.notSdCard, 0).show();
        }
    }

    /* renamed from: com.cnmobi.im.PicMenuDialog.3 */
    class C01803 implements OnClickListener {
        C01803() {
        }

        public void onClick(View arg0) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction("android.intent.action.GET_CONTENT");
            PicMenuDialog.this.startActivityForResult(intent, 1);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.layout_pic_menu_dialog);
        init();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        this.photographBtn = (Button) findViewById(C0136R.id.photographBtn);
        this.localPhotosBtn = (Button) findViewById(C0136R.id.localPhotosBtn);
        this.cancelBtn = (Button) findViewById(C0136R.id.cancelBtn);
        this.cancelBtn.setOnClickListener(new C01781());
        this.photographBtn.setOnClickListener(new C01792());
        this.localPhotosBtn.setOnClickListener(new C01803());
    }

    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path;
        Intent intent;
        if (requestCode == 0) {
            if (resultCode == -1) {
                path = FileConstant.FILE_HEAD_PORTRAIT + "head portrait";
                intent = new Intent(this, CropImageActivity.class);
                intent.putExtra("path", path);
                startActivityForResult(intent, 3);
                finish();
            }
        } else if (1 == requestCode && resultCode == -1 && data != null) {
            Uri uri = data.getData();
            if (TextUtils.isEmpty(uri.getAuthority())) {
                intent = new Intent(this, CropImageActivity.class);
                intent.putExtra("path", uri.getPath());
                startActivityForResult(intent, 3);
            } else {
                Cursor cursor = getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
                if (cursor == null) {
                    Toast.makeText(this, getString(C0136R.string.pictureNotFound), 0).show();
                    return;
                }
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex("_data"));
                cursor.close();
                intent = new Intent(this, CropImageActivity.class);
                intent.putExtra("path", path);
                startActivityForResult(intent, 3);
            }
            finish();
        }
    }
}
