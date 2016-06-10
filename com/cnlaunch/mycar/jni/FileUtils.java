package com.cnlaunch.mycar.jni;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.cnlaunch.framework.utils.NLog;
import com.cnlaunch.x431pro.utils.file.UnZipListener;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class FileUtils {
    private String SDPATH;

    public FileUtils() {
        this.SDPATH = Environment.getExternalStorageDirectory() + File.separator;
    }

    public File createFileOnSD(String newFileName) throws IOException {
        File file = new File(this.SDPATH + newFileName);
        file.createNewFile();
        return file;
    }

    public File createDirOnSD(String dirName) throws IOException {
        File dir = new File(this.SDPATH + dirName);
        Log.d("System.out", this.SDPATH + dirName);
        dir.mkdir();
        return dir;
    }

    public static boolean isFileExist(String filaPath) {
        return new File(filaPath).exists();
    }

    public File writeToSDFromInput(String path, String fileName, InputStream input) {
        IOException e;
        FileNotFoundException e2;
        Throwable th;
        File file = null;
        OutputStream output = null;
        try {
            createDirOnSD(path);
            Log.d("System.out", "file dir exist is:" + new File(this.SDPATH + path).exists());
            file = createFileOnSD(new StringBuilder(String.valueOf(path)).append(fileName).toString());
            OutputStream output2 = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[Flags.EXTEND];
                while (true) {
                    int len = input.read(buffer);
                    if (len == -1) {
                        break;
                    }
                    output2.write(buffer, 0, len);
                }
                output2.flush();
                try {
                    output2.close();
                    output = output2;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    output = output2;
                }
            } catch (FileNotFoundException e4) {
                e2 = e4;
                output = output2;
            } catch (IOException e5) {
                e3 = e5;
                output = output2;
            } catch (Throwable th2) {
                th = th2;
                output = output2;
            }
        } catch (FileNotFoundException e6) {
            e2 = e6;
            try {
                e2.printStackTrace();
                try {
                    output.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
                return file;
            } catch (Throwable th3) {
                th = th3;
                try {
                    output.close();
                } catch (IOException e322) {
                    e322.printStackTrace();
                }
                throw th;
            }
        } catch (IOException e7) {
            e322 = e7;
            e322.printStackTrace();
            try {
                output.close();
            } catch (IOException e3222) {
                e3222.printStackTrace();
            }
            return file;
        }
        return file;
    }

    public String read(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        StringBuffer sb = new StringBuffer();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                bufferedReader.close();
                return sb.toString();
            }
            sb.append(new StringBuilder(String.valueOf(line)).append(SpecilApiUtil.LINE_SEP).toString());
        }
    }

    public void memoryInput(String fileName) throws IOException {
        StringReader stringReader = new StringReader(read(fileName));
        while (true) {
            int c = stringReader.read();
            if (c != -1) {
                System.out.println((char) c);
            } else {
                return;
            }
        }
    }

    public static void main(String[] args) {
    }

    public File getFile(String path) {
        if (isFileExist(path)) {
            return new File(this.SDPATH + path);
        }
        return null;
    }

    public static void readFileByBytes(String fileName) {
        IOException e;
        InputStream inputStream;
        Exception e1;
        Throwable th;
        File file = new File(fileName);
        try {
            System.out.println("\u4ee5\u5b57\u8282\u4e3a\u5355\u4f4d\u8bfb\u53d6\u6587\u4ef6\u5185\u5bb9\uff0c\u4e00\u6b21\u8bfb\u4e00\u4e2a\u5b57\u8282\uff1a");
            InputStream in = new FileInputStream(file);
            while (true) {
                try {
                    int tempbyte = in.read();
                    if (tempbyte == -1) {
                        break;
                    }
                    System.out.write(tempbyte);
                } catch (IOException e2) {
                    e = e2;
                    inputStream = in;
                }
            }
            in.close();
            try {
                System.out.println("\u4ee5\u5b57\u8282\u4e3a\u5355\u4f4d\u8bfb\u53d6\u6587\u4ef6\u5185\u5bb9\uff0c\u4e00\u6b21\u8bfb\u591a\u4e2a\u5b57\u8282\uff1a");
                byte[] tempbytes = new byte[100];
                inputStream = new FileInputStream(fileName);
                while (true) {
                    int byteread = inputStream.read(tempbytes);
                    if (byteread == -1) {
                        break;
                    }
                    try {
                        System.out.write(tempbytes, 0, byteread);
                    } catch (Exception e3) {
                        e1 = e3;
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e4) {
                    }
                }
            } catch (Exception e5) {
                e1 = e5;
                inputStream = in;
                try {
                    e1.printStackTrace();
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e6) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e7) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream = in;
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        } catch (IOException e8) {
            e = e8;
            e.printStackTrace();
        }
    }

    public static void readFileByChars(String fileName) {
        Reader reader;
        Exception e;
        char[] tempchars;
        int charread;
        int i;
        File file = new File(fileName);
        Reader reader2 = null;
        try {
            System.out.println("\u4ee5\u5b57\u7b26\u4e3a\u5355\u4f4d\u8bfb\u53d6\u6587\u4ef6\u5185\u5bb9\uff0c\u4e00\u6b21\u8bfb\u4e00\u4e2a\u5b57\u8282\uff1a");
            reader = new InputStreamReader(new FileInputStream(file));
            while (true) {
                int tempchar = reader.read();
                if (tempchar == -1) {
                    break;
                } else if (((char) tempchar) != 'r') {
                    try {
                        System.out.print((char) tempchar);
                    } catch (Exception e2) {
                        e = e2;
                        reader2 = reader;
                    }
                }
            }
            reader.close();
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            reader = reader2;
            System.out.println("\u4ee5\u5b57\u7b26\u4e3a\u5355\u4f4d\u8bfb\u53d6\u6587\u4ef6\u5185\u5bb9\uff0c\u4e00\u6b21\u8bfb\u591a\u4e2a\u5b57\u8282\uff1a");
            tempchars = new char[30];
            reader2 = new InputStreamReader(new FileInputStream(fileName));
            while (true) {
                charread = reader2.read(tempchars);
                if (charread != -1) {
                    if (charread == tempchars.length) {
                    }
                    while (i < charread) {
                        try {
                            if (tempchars[i] == 'r') {
                                System.out.print(tempchars[i]);
                            }
                        } catch (Exception e4) {
                            e1 = e4;
                        }
                    }
                    continue;
                } else {
                    break;
                    if (reader2 != null) {
                        try {
                            reader2.close();
                        } catch (IOException e5) {
                            return;
                        }
                    }
                }
            }
        }
        try {
            System.out.println("\u4ee5\u5b57\u7b26\u4e3a\u5355\u4f4d\u8bfb\u53d6\u6587\u4ef6\u5185\u5bb9\uff0c\u4e00\u6b21\u8bfb\u591a\u4e2a\u5b57\u8282\uff1a");
            tempchars = new char[30];
            reader2 = new InputStreamReader(new FileInputStream(fileName));
            while (true) {
                charread = reader2.read(tempchars);
                if (charread != -1) {
                    break;
                } else if (charread == tempchars.length || tempchars[tempchars.length - 1] == 'r') {
                    for (i = 0; i < charread; i++) {
                        if (tempchars[i] == 'r') {
                            System.out.print(tempchars[i]);
                        }
                    }
                    continue;
                } else {
                    System.out.print(tempchars);
                }
            }
            if (reader2 != null) {
                reader2.close();
            }
        } catch (Exception e6) {
            e1 = e6;
            reader2 = reader;
            try {
                Exception e1;
                e1.printStackTrace();
                if (reader2 != null) {
                    try {
                        reader2.close();
                    } catch (IOException e7) {
                    }
                }
            } catch (Throwable th) {
                Throwable th2 = th;
                if (reader2 != null) {
                    try {
                        reader2.close();
                    } catch (IOException e8) {
                    }
                }
                throw th2;
            }
        } catch (Throwable th3) {
            th2 = th3;
            reader2 = reader;
            if (reader2 != null) {
                reader2.close();
            }
            throw th2;
        }
    }

    public static void readFileByLines(String fileName) {
        IOException e;
        Throwable th;
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("\u4ee5\u884c\u4e3a\u5355\u4f4d\u8bfb\u53d6\u6587\u4ef6\u5185\u5bb9\uff0c\u4e00\u6b21\u8bfb\u4e00\u6574\u884c\uff1a");
            BufferedReader reader2 = new BufferedReader(new FileReader(file));
            int line = 1;
            while (true) {
                try {
                    String tempString = reader2.readLine();
                    if (tempString == null) {
                        break;
                    }
                    System.out.println("line " + line + ": " + tempString);
                    line++;
                } catch (IOException e2) {
                    e = e2;
                    reader = reader2;
                } catch (Throwable th2) {
                    th = th2;
                    reader = reader2;
                }
            }
            reader2.close();
            if (reader2 != null) {
                try {
                    reader2.close();
                    reader = reader2;
                    return;
                } catch (IOException e3) {
                    reader = reader2;
                    return;
                }
            }
        } catch (IOException e4) {
            e = e4;
            try {
                e.printStackTrace();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e5) {
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e6) {
                    }
                }
                throw th;
            }
        }
    }

    public static void readFileByRandomAccess(String fileName) {
        IOException e;
        Throwable th;
        int beginIndex = 0;
        RandomAccessFile randomFile = null;
        try {
            System.out.println("\u968f\u673a\u8bfb\u53d6\u4e00\u6bb5\u6587\u4ef6\u5185\u5bb9\uff1a");
            RandomAccessFile randomFile2 = new RandomAccessFile(fileName, "r");
            try {
                if (randomFile2.length() > 4) {
                    beginIndex = 4;
                }
                randomFile2.seek((long) beginIndex);
                byte[] bytes = new byte[10];
                while (true) {
                    int byteread = randomFile2.read(bytes);
                    if (byteread == -1) {
                        break;
                    }
                    System.out.write(bytes, 0, byteread);
                }
                if (randomFile2 != null) {
                    try {
                        randomFile2.close();
                        randomFile = randomFile2;
                        return;
                    } catch (IOException e2) {
                        randomFile = randomFile2;
                        return;
                    }
                }
            } catch (IOException e3) {
                e = e3;
                randomFile = randomFile2;
            } catch (Throwable th2) {
                th = th2;
                randomFile = randomFile2;
            }
        } catch (IOException e4) {
            e = e4;
            try {
                e.printStackTrace();
                if (randomFile != null) {
                    try {
                        randomFile.close();
                    } catch (IOException e5) {
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                if (randomFile != null) {
                    try {
                        randomFile.close();
                    } catch (IOException e6) {
                    }
                }
                throw th;
            }
        }
    }

    public static void appendMethodA(String fileName, String content) {
        try {
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            randomFile.seek(randomFile.length());
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendMethodB(String fileName, String content) {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final int byteArrayToShort(byte[] b) {
        return (b[0] << 8) + (b[1] & KEYRecord.PROTOCOL_ANY);
    }

    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) ((value >>> (((b.length - 1) - i) * 8)) & KEYRecord.PROTOCOL_ANY);
        }
        return b;
    }

    public static final int byteArrayToInt(byte[] b) {
        return (((b[0] << 24) + ((b[1] & KEYRecord.PROTOCOL_ANY) << 16)) + ((b[2] & KEYRecord.PROTOCOL_ANY) << 8)) + (b[3] & KEYRecord.PROTOCOL_ANY);
    }

    public static boolean sdCardIsMount() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static File sdCardGetDirectoryFile() {
        if (sdCardIsMount()) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }

    public static String sdCardGetDirectoryPath() {
        if (sdCardIsMount()) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return XmlPullParser.NO_NAMESPACE;
    }

    public static int sdCardGetBlockSize() {
        if (sdCardIsMount()) {
            return new StatFs(sdCardGetDirectoryPath()).getBlockSize();
        }
        return -1;
    }

    public static int sdCardGetBlockCount() {
        if (sdCardIsMount()) {
            return new StatFs(sdCardGetDirectoryPath()).getBlockCount();
        }
        return -1;
    }

    public static int sdCardGetAvailableBlocks() {
        if (sdCardIsMount()) {
            return new StatFs(sdCardGetDirectoryPath()).getAvailableBlocks();
        }
        return -1;
    }

    public static int sdCardGetFreeBlocks() {
        if (sdCardIsMount()) {
            return new StatFs(sdCardGetDirectoryPath()).getFreeBlocks();
        }
        return -1;
    }

    public static int sdCardGetSizeAsMB() {
        if (!sdCardIsMount()) {
            return -1;
        }
        StatFs stat = new StatFs(sdCardGetDirectoryPath());
        return ((stat.getBlockSize() * stat.getBlockCount()) / Flags.FLAG5) / Flags.FLAG5;
    }

    public static int sdCardAvailableSizePercent() {
        if (!sdCardIsMount()) {
            return -1;
        }
        StatFs stat = new StatFs(sdCardGetDirectoryPath());
        return (stat.getAvailableBlocks() * 100) / stat.getBlockCount();
    }

    public static boolean DeleFile(String strFilePath) {
        return new File(strFilePath).delete();
    }

    public static long GetFileSize(String strFilePath) {
        return new File(strFilePath).length();
    }

    public static synchronized String unZipEx(String srcFile, String dest, boolean deleteFile, UnZipListener unZipCallBack) {
        String message;
        synchronized (FileUtils.class) {
            String message2 = "success";
            if (unZipCallBack != null) {
                unZipCallBack.start();
            }
            File file = new File(srcFile);
            if (file.exists()) {
                try {
                    ZipFile zipFile = new ZipFile(file);
                    Enumeration<? extends ZipEntry> e = zipFile.entries();
                    int step = 0;
                    int total = zipFile.size();
                    while (e.hasMoreElements()) {
                        ZipEntry zipEntry = (ZipEntry) e.nextElement();
                        if (zipEntry.isDirectory()) {
                            String name = zipEntry.getName();
                            name = name.substring(0, name.length() - 1);
                            new File(new StringBuilder(String.valueOf(dest)).append(name).toString()).mkdirs();
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
                        if (unZipCallBack != null) {
                            step++;
                            unZipCallBack.progress(step, total);
                        }
                    }
                    if (zipFile != null) {
                        zipFile.close();
                    }
                    if (deleteFile) {
                        file.deleteOnExit();
                    }
                } catch (ZipException e2) {
                    e2.printStackTrace();
                    message2 = e2.getMessage();
                    if (unZipCallBack != null) {
                        unZipCallBack.error(-2, e2);
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                    String str = "FileUtils";
                    NLog.m917e(r18, "unZipEx Error: " + e3.getMessage());
                    message2 = e3.getMessage();
                    if (unZipCallBack != null) {
                        unZipCallBack.error(-3, e3);
                    }
                } catch (Exception e4) {
                    e4.printStackTrace();
                    message2 = e4.getMessage();
                    if (unZipCallBack != null) {
                        unZipCallBack.error(-4, e4);
                    }
                }
                if (unZipCallBack != null) {
                    unZipCallBack.finished();
                }
                message = message2;
            } else {
                message2 = "File does not exist";
                if (unZipCallBack != null) {
                    unZipCallBack.error(-1, null);
                    unZipCallBack.finished();
                }
                message = message2;
            }
        }
        return message;
    }
}
