package com.ifoer.pro.expeditionphone;

import android.content.Intent;
import android.os.Bundle;
import com.ifoer.expeditionphone.inteface.IGuideActivityInterface;
import com.ifoer.ui.MainMenuActivity;
import java.util.Timer;
import java.util.TimerTask;

public class GuideActivity extends com.ifoer.expeditionphone.GuideActivity implements IGuideActivityInterface {

    /* renamed from: com.ifoer.pro.expeditionphone.GuideActivity.1 */
    class C06781 extends TimerTask {
        C06781() {
        }

        public void run() {
            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
            intent.putExtra(MainMenuActivity.IfShowDialog, 2);
            GuideActivity.this.startActivity(intent);
            GuideActivity.this.overridePendingTransition(0, 0);
            GuideActivity.this.finish();
            GuideActivity.this.overridePendingTransition(0, 0);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void hideKeyboard() {
        super.hideKeyboard();
    }

    public void initPageImage() {
        super.initPageImage();
    }

    public void creatView() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainMenuActivity.IfShowDialog, 2);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
    }

    public void jump() {
        new Timer().schedule(new C06781(), 500);
    }
}
