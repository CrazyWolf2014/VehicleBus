package com.ifoer.serialport;

import CRP.utils.CRPTools;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import com.cnlaunch.x431pro.common.Constants;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.util.MySharedPreferences;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class SerialPortNoReadThread extends Thread {
    public static final String Builder;
    public static int recvCount;
    public static int recvLoop;
    public static int[] recvSize;
    public static int f1299x;
    private String category;
    private Context context;
    private String datetime;
    private String devboot;
    private ArrayList<String> listNumber;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private String pcbversion;
    byte[] recvBuf;
    private String serialportID;
    private String serialportNO;
    private String softversion;
    private String stringdownloadbin;

    static {
        Builder = Build.DISPLAY;
        f1299x = 0;
        recvCount = 0;
        recvSize = new int[8];
        recvLoop = 0;
    }

    public String getSerialportNO() {
        return this.serialportNO;
    }

    public void setSerialportNO(String serialportNO) {
        this.serialportNO = serialportNO;
    }

    public String getSerialportID() {
        return this.serialportID;
    }

    public void setSerialportID(String serialportID) {
        this.serialportID = serialportID;
    }

    public OutputStream getmOutputStream() {
        return this.mOutputStream;
    }

    public void setmOutputStream(OutputStream mOutputStream) {
        this.mOutputStream = mOutputStream;
    }

    public SerialPortNoReadThread(SerialPort serialPort, Context context) {
        this.recvBuf = new byte[Flags.FLAG5];
        this.serialportNO = XmlPullParser.NO_NAMESPACE;
        this.serialportID = XmlPullParser.NO_NAMESPACE;
        this.devboot = XmlPullParser.NO_NAMESPACE;
        this.softversion = XmlPullParser.NO_NAMESPACE;
        this.stringdownloadbin = XmlPullParser.NO_NAMESPACE;
        this.pcbversion = XmlPullParser.NO_NAMESPACE;
        this.datetime = XmlPullParser.NO_NAMESPACE;
        this.category = XmlPullParser.NO_NAMESPACE;
        this.listNumber = new ArrayList();
        this.mInputStream = serialPort.getInputStream();
        this.mOutputStream = serialPort.getOutputStream();
        this.context = context;
    }

    public void run() {
        super.run();
        while (!isInterrupted()) {
            byte[] buffer = new byte[Flags.FLAG5];
            int i = 0;
            while (i < KEYRecord.OWNER_ZONE) {
                try {
                    buffer[i] = (byte) 0;
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (this.mInputStream != null) {
                int size = this.mInputStream.read(buffer);
                if (size > 0) {
                    onDataReceived(buffer, size);
                }
            } else {
                return;
            }
        }
    }

    public byte[] toByte(String str) {
        String str1 = str.replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE);
        byte[] d = new byte[(str1.length() / 2)];
        int i = 0;
        while (i < str1.length()) {
            int c;
            int tmp = str1.substring(i, i + 1).getBytes()[0];
            if (tmp > 96) {
                c = ((tmp - 97) + 10) * 16;
            } else if (tmp > 64) {
                c = ((tmp - 65) + 10) * 16;
            } else {
                c = (tmp - 48) * 16;
            }
            i++;
            tmp = str1.substring(i, i + 1).getBytes()[0];
            if (tmp > 96) {
                c += (tmp - 97) + 10;
            } else if (tmp > 64) {
                c += (tmp - 65) + 10;
            } else {
                c += tmp - 48;
            }
            d[i / 2] = (byte) c;
            i++;
        }
        return d;
    }

    public String ByteToStr(byte[] dat, int begin) {
        int count = 0;
        StringBuffer str = new StringBuffer();
        int i = begin;
        while (i < dat.length && dat[i] != null) {
            str.append((char) dat[i]);
            count++;
            i++;
        }
        return str.substring(0, count);
    }

    public int getCount(byte[] dat, int begin) {
        int count = 0;
        int i = begin;
        while (i < dat.length && dat[i] != null) {
            if (dat[i] == 85 && dat[i + 1] == 170 && dat[i + 2] == 248 && dat[i + 3] == 240) {
                count += 11;
            } else {
                count++;
            }
            i++;
        }
        return count + 3;
    }

    public ArrayList<String> getListNumber(byte[] dat) {
        ArrayList<String> numberList = new ArrayList();
        int i = 11;
        while (i < dat.length) {
            numberList.add(ByteToStr(dat, i));
            i += getCount(dat, i);
        }
        return numberList;
    }

    protected void onDataReceived(byte[] buffer, int size) {
        int i;
        StringBuffer rText = new StringBuffer();
        String ary = "0123456789ABCDEF";
        for (i = 0; i < size; i++) {
            int d = (buffer[i] >> 4) & 15;
            rText.append(ary.substring(d, d + 1));
            d = buffer[i] & 15;
            rText.append(ary.substring(d, d + 1));
            rText.append(XmlPullParser.NO_NAMESPACE);
            byte[] bArr = this.recvBuf;
            int i2 = recvCount;
            recvCount = i2 + 1;
            bArr[i2] = buffer[i];
            if (this.recvBuf[0] != 85) {
                recvCount--;
            }
        }
        if (buffer[0] == 85 && buffer[1] == -86) {
            int[] iArr = recvSize;
            i2 = recvLoop;
            iArr[i2] = iArr[i2] + (buffer[5] + 7);
            recvLoop++;
        }
        if (recvLoop >= 2 && recvCount == recvSize[0] + recvSize[1]) {
            rText.setLength(0);
            StringBuffer sb = new StringBuffer();
            for (i = 0; i < recvCount; i++) {
                d = (this.recvBuf[i] >> 4) & 15;
                rText.append(ary.substring(d, d + 1));
                d = this.recvBuf[i] & 15;
                rText.append(ary.substring(d, d + 1));
                rText.append(XmlPullParser.NO_NAMESPACE);
                sb = rText;
            }
            this.listNumber = getListNumber(ByteHexHelper.hexStringToBytes(rText.toString()));
            this.serialportID = ((String) this.listNumber.get(0)).substring(0, 12);
            MySharedPreferences.setString(this.context, Constants.SERIALID, this.serialportID.trim());
            f1299x = 1;
            this.serialportNO = (String) this.listNumber.get(1);
            MySharedPreferences.setString(this.context, Constants.SERIALNO, this.serialportNO.trim());
            this.pcbversion = (String) this.listNumber.get(2);
            MySharedPreferences.setString(this.context, "pcbverSion", this.pcbversion.trim());
            f1299x = 2;
            this.datetime = (String) this.listNumber.get(3);
            MySharedPreferences.setString(this.context, "datetime", this.datetime.trim());
            f1299x = 3;
            this.category = (String) this.listNumber.get(4);
            f1299x = 5;
            this.devboot = (String) this.listNumber.get(7);
            MySharedPreferences.setString(this.context, MySharedPreferences.devboot, this.devboot.trim());
            this.stringdownloadbin = (String) this.listNumber.get(8);
            MySharedPreferences.setString(this.context, Constants.DOWNLOADBIN, this.stringdownloadbin.trim());
            f1299x = 4;
            this.softversion = (String) this.listNumber.get(9);
            MySharedPreferences.setString(this.context, MySharedPreferences.softversion, this.softversion.trim());
            f1299x = 0;
            interrupt();
        }
    }

    public void WriteData() {
        String filePath = Environment.getExternalStorageDirectory() + CRPTools.CRP_SERIALPORTINFO_VERSION;
        String message = this.serialportID + "_" + this.serialportNO + "_" + this.stringdownloadbin;
        File fileD = new File(Environment.getExternalStorageDirectory() + "/cnlaunch");
        if (!fileD.exists()) {
            fileD.mkdirs();
        }
        File file = new File(filePath);
        if (file.exists()) {
            try {
                FileOutputStream fout = new FileOutputStream(file);
                fout.write(message.getBytes());
                fout.close();
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                return;
            }
        }
        try {
            file.createNewFile();
            fout = new FileOutputStream(file);
            fout.write(message.getBytes());
            fout.close();
        } catch (IOException e22) {
            e22.printStackTrace();
        }
    }
}
