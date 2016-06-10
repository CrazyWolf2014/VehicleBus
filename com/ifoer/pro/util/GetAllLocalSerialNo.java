package com.ifoer.pro.util;

import android.content.Context;
import com.cnlaunch.x431pro.common.Constants;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.Common;
import com.ifoer.util.MySharedPreferences;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xmlpull.v1.XmlPullParser;

public class GetAllLocalSerialNo {
    public static List<HashMap<String, String>> findSerialNo(Context context) {
        List<HashMap<String, String>> list = new ArrayList();
        File file = new File(SrorageManager.getDefaultExternalStoragePath(context) + "/cnlaunch/");
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File dir : files) {
                    if (dir.isDirectory()) {
                        String name = dir.getName();
                        String qianZhui = XmlPullParser.NO_NAMESPACE;
                        if (name.length() > 5) {
                            qianZhui = name.substring(0, 5);
                        }
                        if (name.length() == 12 && Common.isNumber(name) && qianZhui.equals(MySharedPreferences.getStringValue(MainActivity.contexts, "PORT_START"))) {
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
