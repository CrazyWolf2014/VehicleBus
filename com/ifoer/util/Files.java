package com.ifoer.util;

import android.os.Environment;
import com.ifoer.entity.Car;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Files {
    private static boolean f1307D;
    static List<Car> picList;

    static {
        picList = new ArrayList();
        f1307D = false;
    }

    public static List<String> getFilePathFromSD(String paht) {
        List<String> fileList = new ArrayList();
        if (Environment.getExternalStorageState().equals("mounted") && paht != null) {
            File[] files = new File(paht).listFiles();
            if (files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        fileList.add(file.getName());
                    }
                    try {
                        if (file.getPath().lastIndexOf(".") <= 0) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return fileList;
            }
        }
        return null;
    }

    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getCountry() {
        return Locale.getDefault().getCountry();
    }
}
