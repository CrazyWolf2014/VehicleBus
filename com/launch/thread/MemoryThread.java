package com.launch.thread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.entity.PageInteractiveData;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.BluetoothOrder.PageInteractiveDataAnalysis;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.MeasureConversion;
import com.ifoer.util.MySharedPreferences;
import com.launch.listener.OnDataStreamListener;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class MemoryThread extends Thread {
    public static List<PageInteractiveData> bytesList;
    private static boolean isRunning;
    private static boolean isWait;
    private OnDataStreamListener onDataStreamListener;
    public String tag;

    static {
        bytesList = new ArrayList();
        isRunning = true;
        isWait = false;
    }

    public MemoryThread() {
        this.tag = "MemoryThread";
        this.onDataStreamListener = null;
        isRunning = true;
        isWait = false;
    }

    public void addDataInArrary(PageInteractiveData bytes) {
        bytesList.add(bytes);
        if (isWait) {
            notifyThread();
        }
    }

    public void stopThread() {
        isRunning = false;
        if (isWait) {
            notifyThread();
        }
    }

    public void run() {
        while (isRunning) {
            Log.w(this.tag, "---------------->\u7ebf\u7a0b\u5faa\u73af<---------------------------");
            if (bytesList.size() > 0) {
                handleData((PageInteractiveData) bytesList.get(0));
                bytesList.remove(0);
                Log.d(this.tag, "Constant.streamNextCode=" + Constant.streamNextCode);
            } else {
                sleepThread();
            }
        }
        Log.w(this.tag, "------------>\u7ebf\u7a0b\u7ed3\u675f<-------------------");
    }

    public synchronized void notifyThread() {
        Log.w(this.tag, "------------>\u7ebf\u7a0b\u5524\u9192<-------------------");
        isWait = false;
        notify();
    }

    public synchronized void sleepThread() {
        Log.w(this.tag, "------------>\u7ebf\u7a0b\u4f11\u7720<-------------------");
        try {
            isWait = true;
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setOnDataStreamListener(OnDataStreamListener onDataStreamListener) {
        this.onDataStreamListener = onDataStreamListener;
    }

    public void handleData(PageInteractiveData mData) {
        ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = PageInteractiveDataAnalysis.spt_ex_datastream_id18(mData);
        String path = Constant.OBD2_EN_GGP_PATH;
        if (path != null && path.length() > 0) {
            for (int i = 0; i < exDataStreamIdlist.size(); i++) {
                byte[] activeContent;
                SptExDataStreamIdItem exStreamSelectIdItem = (SptExDataStreamIdItem) exDataStreamIdlist.get(i);
                String lString = XmlPullParser.NO_NAMESPACE;
                byte[] databuf = ByteHexHelper.hexStringToBytes(exStreamSelectIdItem.getStreamTextId());
                int v_id = ((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + ((databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE)) + (databuf[3] & KEYRecord.PROTOCOL_ANY);
                byte[] activeContentbytes = CToJava.searchId.getTextFromLibReturnByte(v_id, 3);
                Log.d("SEARCHID", "\u6570\u636ev_id" + v_id + "activeContentbytes==" + ByteHexHelper.byteToWord(activeContentbytes));
                if (activeContentbytes.length > 0) {
                    activeContent = new byte[(activeContentbytes.length - 1)];
                    for (int j = 0; j < activeContent.length; j++) {
                        activeContent[j] = activeContentbytes[j];
                    }
                } else {
                    activeContent = new byte[0];
                }
                String[] datas = PageInteractiveDataAnalysis.separateString(ByteHexHelper.bytesToHexString(activeContent));
                if (datas.length >= 1 && datas[0].equalsIgnoreCase("---")) {
                    exStreamSelectIdItem.setStreamTextIdContent(exStreamSelectIdItem.getStreamTextId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                } else if (datas.length >= 1 && !datas[0].equalsIgnoreCase("---")) {
                    exStreamSelectIdItem.setStreamTextIdContent(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(datas[0])));
                } else if (datas.length == 0) {
                    exStreamSelectIdItem.setStreamTextIdContent(exStreamSelectIdItem.getStreamTextId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                }
                if (datas.length > 1 && datas[1].equalsIgnoreCase("---")) {
                    exStreamSelectIdItem.setStreamState(XmlPullParser.NO_NAMESPACE);
                } else if (datas.length > 1 && !datas[1].equalsIgnoreCase("---")) {
                    exStreamSelectIdItem.setStreamState(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(datas[1])));
                }
            }
            Collections.sort(exDataStreamIdlist);
            if (MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.conversion) == null || MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.conversion).equals(XmlPullParser.NO_NAMESPACE)) {
                Log.d("Sanda", "Metric");
            } else if (MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.conversion).equals("Imperial")) {
                Log.d("Sanda", "Imperial");
                MeasureConversion.convertMetricToImperial(exDataStreamIdlist, 2);
            } else {
                MeasureConversion.convertImperialToMetric(exDataStreamIdlist, 2);
            }
            Intent intent = new Intent("SPT_EX_DATASTREAM_ID");
            Bundle bundle = new Bundle();
            bundle.putSerializable("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
            bundle.putInt(SharedPref.TYPE, 18);
            intent.putExtras(bundle);
            MainActivity.contexts.sendBroadcast(intent);
        }
    }
}
