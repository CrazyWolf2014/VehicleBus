package com.ifoer.db;

import android.content.Context;
import android.os.Handler;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.WelcomeActivity;
import com.ifoer.util.CarIcon;

public class InsertToCarIcon extends Thread {
    private Context context;
    private Handler handler;

    public InsertToCarIcon(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void run() {
        super.run();
        DBDao.getInstance(this.context).insertToCarIcon(CarIcon.data(), WelcomeActivity.database);
        WelcomeActivity.thread1 = true;
        if (WelcomeActivity.thread1 && WelcomeActivity.thread2 && WelcomeActivity.thread3 && WelcomeActivity.thread4) {
            this.handler.obtainMessage(MainActivity.MESSAGE_SHOW_MENU).sendToTarget();
        }
    }
}
