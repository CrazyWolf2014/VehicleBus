package com.ifoer.expedition.client;

import com.ifoer.util.Copy_File;
import java.util.ArrayList;
import java.util.Iterator;

public class DbscarUpgradeUtil {
    public static Boolean inspectIntegrity(String path) {
        Copy_File.list = new ArrayList();
        Copy_File.findAllDownloadFile(path);
        if (Copy_File.list.size() < 2) {
            return Boolean.valueOf(false);
        }
        Boolean file1 = Boolean.valueOf(false);
        Boolean file2 = Boolean.valueOf(false);
        Iterator it = Copy_File.list.iterator();
        while (it.hasNext()) {
            String fileName = (String) it.next();
            if ("DOWNLOAD.bin".equalsIgnoreCase(fileName)) {
                file1 = Boolean.valueOf(true);
            }
            if ("DOWNLOAD.ini".equalsIgnoreCase(fileName)) {
                file2 = Boolean.valueOf(true);
            }
        }
        if (file1.booleanValue() && file2.booleanValue()) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }
}
