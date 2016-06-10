package com.ifoer.db;

import android.content.Context;
import com.ifoer.expeditionphone.MainActivity;

public class UpgradeRunnable implements Runnable {
    private Context context;

    public UpgradeRunnable(Context context) {
        this.context = context;
    }

    public void run() {
        if (!DBDao.getInstance(this.context).isHasData(MainActivity.database)) {
            DBDao.getInstance(this.context).addToUpgrade(MainActivity.database);
        }
    }
}
