package com.ifoer.db;

import android.content.Context;
import com.car.result.DiagSoftIdListResult;
import com.ifoer.expeditionphone.MainActivity;

public class SaveSoftId extends Thread {
    private Context context;
    private DiagSoftIdListResult result;

    public SaveSoftId(Context context, DiagSoftIdListResult result) {
        this.context = context;
        this.result = result;
    }

    public void run() {
        super.run();
        DBDao.getInstance(this.context).updateCarIcon(this.result, MainActivity.database);
    }
}
