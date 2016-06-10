package com.ifoer.mine;

import android.content.Context;
import android.os.Environment;

public class ChatAppContext {
    public static String RECORD_ROOT_PATH;
    public static String contactId;
    public static Context imContext;
    public static String logoMd5;
    public static String token;

    static {
        RECORD_ROOT_PATH = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/chat/record").toString();
    }
}
