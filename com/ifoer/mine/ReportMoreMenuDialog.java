package com.ifoer.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.util.MyApplication;

public class ReportMoreMenuDialog extends Activity {
    private Button cancelBtn;
    private Button deleteBtn;
    private Button shareBtn;

    /* renamed from: com.ifoer.mine.ReportMoreMenuDialog.1 */
    class C06751 implements OnClickListener {
        C06751() {
        }

        public void onClick(View arg0) {
            ReportMoreMenuDialog.this.finish();
        }
    }

    /* renamed from: com.ifoer.mine.ReportMoreMenuDialog.2 */
    class C06762 implements OnClickListener {
        C06762() {
        }

        public void onClick(View arg0) {
            ReportMoreMenuDialog.this.sendBroadcast(new Intent("report_share"));
            ReportMoreMenuDialog.this.finish();
        }
    }

    /* renamed from: com.ifoer.mine.ReportMoreMenuDialog.3 */
    class C06773 implements OnClickListener {
        C06773() {
        }

        public void onClick(View arg0) {
            ReportMoreMenuDialog.this.sendBroadcast(new Intent("report_delete"));
            ReportMoreMenuDialog.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.layout_pic_menu_dialog);
        MyApplication.getInstance().addActivity(this);
        Display display = getWindowManager().getDefaultDisplay();
        getWindow().getAttributes().width = display.getWidth();
        init();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        this.shareBtn = (Button) findViewById(C0136R.id.photographBtn);
        this.deleteBtn = (Button) findViewById(C0136R.id.localPhotosBtn);
        this.cancelBtn = (Button) findViewById(C0136R.id.cancelBtn);
        this.shareBtn.setText(getResources().getString(C0136R.string.share));
        this.deleteBtn.setText(getResources().getString(C0136R.string.del));
        this.cancelBtn.setOnClickListener(new C06751());
        this.shareBtn.setOnClickListener(new C06762());
        this.deleteBtn.setOnClickListener(new C06773());
    }

    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
