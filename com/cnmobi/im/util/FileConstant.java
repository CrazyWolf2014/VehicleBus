package com.cnmobi.im.util;

import android.os.Environment;

public interface FileConstant {
    public static final String FILE_EVENT_REPORTING;
    public static final String FILE_HEAD_PORTRAIT;
    public static final String FILE_LOGOS;
    public static final String FILE_VHOME;

    static {
        FILE_VHOME = Environment.getExternalStorageDirectory() + "/cnmobi/";
        FILE_LOGOS = FILE_VHOME + "/cnmobi/logos/";
        FILE_EVENT_REPORTING = FILE_VHOME + "event reporting image/";
        FILE_HEAD_PORTRAIT = FILE_VHOME + "head portrait/";
    }
}
