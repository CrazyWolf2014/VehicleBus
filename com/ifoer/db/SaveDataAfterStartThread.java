package com.ifoer.db;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.ifoer.dbentity.Car;
import com.ifoer.dbentity.SerialNumber;
import com.ifoer.entity.Constant;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.WelcomeActivity;
import com.ifoer.util.FindAllDynamicLibs;
import java.util.List;

public class SaveDataAfterStartThread extends Thread {
    private static final boolean f1223D = false;
    private List<Car> carList;
    private Context context;
    private Handler handler;
    private String motorcycleTypePath;
    private String serailNo;
    private List<SerialNumber> serialNumList;

    public SaveDataAfterStartThread(Context context, Handler handler, String serailNo) {
        this.motorcycleTypePath = Constant.LOCAL_SERIALNO_PATH;
        this.context = context;
        this.handler = handler;
        this.serailNo = serailNo;
    }

    public void run() {
        super.run();
        Log.i("SaveDataAfterStartThread", "\u5e8f\u5217\u53f7\u76ee\u5f55" + this.motorcycleTypePath);
        this.serialNumList = FindAllDynamicLibs.getAllCarInfo(this.motorcycleTypePath, this.serailNo);
        if (this.serialNumList == null || this.serialNumList.size() <= 0) {
            WelcomeActivity.thread2 = true;
            if (WelcomeActivity.thread1 && WelcomeActivity.thread2 && WelcomeActivity.thread3 && WelcomeActivity.thread4) {
                this.handler.obtainMessage(MainActivity.MESSAGE_SHOW_MENU).sendToTarget();
                return;
            }
            return;
        }
        DBDao.getInstance(this.context).add(this.serialNumList, WelcomeActivity.database);
        WelcomeActivity.thread2 = true;
        if (WelcomeActivity.thread1 && WelcomeActivity.thread2 && WelcomeActivity.thread3 && WelcomeActivity.thread4) {
            this.handler.obtainMessage(MainActivity.MESSAGE_SHOW_MENU).sendToTarget();
        }
    }
}
