package com.ifoer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.xbill.DNS.KEYRecord.Flags;

public class Unzip {
    public static synchronized String unZip(String srcFile, String dest, boolean deleteFile) {
        String message;
        synchronized (Unzip.class) {
            message = "success";
            File file = new File(srcFile);
            if (file.exists()) {
                try {
                    ZipFile zipFile = new ZipFile(file);
                    Enumeration e = zipFile.entries();
                    while (e.hasMoreElements()) {
                        ZipEntry zipEntry = (ZipEntry) e.nextElement();
                        if (zipEntry.isDirectory()) {
                            String name = zipEntry.getName();
                            new File(new StringBuilder(String.valueOf(dest)).append(name.substring(0, name.length() - 1)).toString()).mkdirs();
                        } else {
                            File f = new File(new StringBuilder(String.valueOf(dest)).append(zipEntry.getName()).toString());
                            f.getParentFile().mkdirs();
                            f.createNewFile();
                            InputStream is = zipFile.getInputStream(zipEntry);
                            FileOutputStream fos = new FileOutputStream(f);
                            byte[] b = new byte[Flags.FLAG5];
                            while (true) {
                                int length = is.read(b, 0, Flags.FLAG5);
                                if (length == -1) {
                                    break;
                                }
                                fos.write(b, 0, length);
                            }
                            is.close();
                            fos.close();
                        }
                    }
                    if (zipFile != null) {
                        zipFile.close();
                    }
                    if (deleteFile) {
                        file.deleteOnExit();
                    }
                } catch (ZipException e2) {
                    message = "fail";
                } catch (IOException e3) {
                    message = "fail";
                }
            } else {
                message = "File does not exist";
            }
        }
        return message;
    }
}
