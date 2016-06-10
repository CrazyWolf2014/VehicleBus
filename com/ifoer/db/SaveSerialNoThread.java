package com.ifoer.db;

import android.content.Context;
import android.os.Handler;
import com.cnlaunch.x431pro.common.Constants;
import com.ifoer.entity.Constant;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.WelcomeActivity;
import com.ifoer.util.Common;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xmlpull.v1.XmlPullParser;

public class SaveSerialNoThread extends Thread {
    private Context context;
    private Handler handler;

    public SaveSerialNoThread(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void run() {
        super.run();
        DBDao.getInstance(this.context).addSerialNo(findSerialNo(), WelcomeActivity.database);
        WelcomeActivity.thread3 = true;
        if (WelcomeActivity.thread1 && WelcomeActivity.thread2 && WelcomeActivity.thread3 && WelcomeActivity.thread4) {
            this.handler.obtainMessage(MainActivity.MESSAGE_SHOW_MENU).sendToTarget();
        }
    }

    private List<HashMap<String, String>> findSerialNo() {
        List<HashMap<String, String>> list = new ArrayList();
        File file = new File(Constant.LOCAL_SERIALNO_PATH);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File dir : files) {
                    if (dir.isDirectory()) {
                        String name = dir.getName();
                        if (name.length() == 12 && Common.isNumber(name)) {
                            HashMap<String, String> map = new HashMap();
                            map.put(MultipleAddresses.CC, XmlPullParser.NO_NAMESPACE);
                            map.put(Constants.serialNo, name);
                            list.add(map);
                        }
                    }
                }
            }
        }
        return list;
    }
}
