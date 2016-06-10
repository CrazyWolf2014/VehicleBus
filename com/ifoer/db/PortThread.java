package com.ifoer.db;

import android.content.Context;
import com.ifoer.expeditionphone.MainActivity;
import java.util.HashMap;
import java.util.List;

public class PortThread extends Thread {
    private Context context;
    private List<HashMap<String, String>> list;

    public PortThread(Context context, List<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    public void run() {
        super.run();
        DBDao.getInstance(this.context).addSerialNo(this.list, MainActivity.database);
    }
}
