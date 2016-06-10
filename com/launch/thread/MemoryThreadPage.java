package com.launch.thread;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.ifoer.entity.Constant;
import com.ifoer.entity.PageInteractiveData;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.expedition.BluetoothOrder.PageInteractiveDataAnalysis;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.MeasureConversion;
import com.ifoer.util.MySharedPreferences;
import com.launch.listener.OnDataStreamListener;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class MemoryThreadPage extends Thread {
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

    public MemoryThreadPage() {
        this.tag = "MemoryThreadPage";
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

    public void handleData(PageInteractiveData uiData) {
        ArrayList<SptExDataStreamIdItem> list = PageInteractiveDataAnalysis.spt_datastream_item34(uiData);
        Collections.sort(list);
        if (!(MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.conversion) == null || MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.conversion).equals(XmlPullParser.NO_NAMESPACE))) {
            if (MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.conversion).equals("Imperial")) {
                Log.d("Sanda", "Imperial");
                MeasureConversion.convertMetricToImperial(list, 2);
            } else {
                MeasureConversion.convertImperialToMetric(list, 2);
            }
        }
        Intent intent = new Intent("SPT_DATASTREAM_ID_EX");
        Bundle bundle = new Bundle();
        bundle.putSerializable("SPT_DATASTREAM_ID_EX", list);
        bundle.putInt(SharedPref.TYPE, 34);
        intent.putExtras(bundle);
        MainActivity.contexts.sendBroadcast(intent);
    }
}
