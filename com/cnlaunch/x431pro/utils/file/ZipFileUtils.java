package com.cnlaunch.x431pro.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.xbill.DNS.KEYRecord.Flags;

public class ZipFileUtils {
    private static FileInputStream inputStream;

    public static void zipFolder(String srcFilePath, String zipFilePath) throws Exception {
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFilePath));
        File file = new File(srcFilePath);
        zipFiles(file.getParent() + File.separator, file.getName(), outZip);
        outZip.finish();
        outZip.close();
    }

    private static void zipFiles(String folderPath, String filePath, ZipOutputStream zipOut) throws Exception {
        if (zipOut != null) {
            File file = new File(new StringBuilder(String.valueOf(folderPath)).append(filePath).toString());
            if (file.isFile()) {
                ZipEntry zipEntry = new ZipEntry(filePath);
                inputStream = new FileInputStream(file);
                zipOut.putNextEntry(zipEntry);
                byte[] buffer = new byte[Flags.EXTEND];
                while (true) {
                    int len = inputStream.read(buffer);
                    if (len == -1) {
                        zipOut.closeEntry();
                        return;
                    }
                    zipOut.write(buffer, 0, len);
                }
            } else {
                String[] fileList = file.list();
                if (fileList.length <= 0) {
                    zipOut.putNextEntry(new ZipEntry(new StringBuilder(String.valueOf(filePath)).append(File.separator).toString()));
                    zipOut.closeEntry();
                }
                for (String append : fileList) {
                    zipFiles(folderPath, new StringBuilder(String.valueOf(filePath)).append(File.separator).append(append).toString(), zipOut);
                }
            }
        }
    }
}
