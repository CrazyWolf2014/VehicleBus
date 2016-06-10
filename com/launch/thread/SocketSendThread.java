package com.launch.thread;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.ifoer.expeditionphone.MainActivity;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

public class SocketSendThread extends Thread {
    public static List<String> dataArraryList;
    private static boolean isRunning;
    private static boolean isWait;

    static {
        dataArraryList = new ArrayList();
        isRunning = true;
        isWait = true;
    }

    public SocketSendThread() {
        isRunning = true;
        isWait = false;
    }

    public void addListInArrary(String dataStr) {
        Log.i("RemoteDiag", "----------->\u961f\u5217\u6dfb\u52a0\u4e00\u4e2a\u6570\u636e<--------------");
        dataArraryList.add(dataStr);
    }

    public void stopThread() {
        isRunning = false;
    }

    public boolean isWait() {
        return isWait;
    }

    public void run() {
        while (isRunning) {
            Log.w("RemoteDiag", "---------------->\u7ebf\u7a0b\u5faa\u73af<---------------------------");
            Log.d("RemoteDiag", "------------->\u961f\u5217\u5927\u5c0f\uff1a" + dataArraryList.size() + "<--------------------");
            if (dataArraryList.size() > 0) {
                Log.w("RemoteDiag", "---------------->\u53d1\u9001\u6570\u636e<---------------------------");
                String dataString = (String) dataArraryList.get(0);
                if (MainActivity.socketCall == null) {
                    return;
                }
                if (MainActivity.socketCall.send(dataString.getBytes(), 1) == 0) {
                    Log.e("RemoteDiagData", "---->\u53d1\u9001\u5931\u8d25\uff1a" + dataString);
                    Intent intentStatus = new Intent("RemoteDiagStatus");
                    Bundle bundle = new Bundle();
                    bundle.putInt(SharedPref.TYPE, Service.PWDGEN);
                    intentStatus.putExtras(bundle);
                    MainActivity.contexts.sendBroadcast(intentStatus);
                    sleepThread();
                } else {
                    Log.i("RemoteDiagData", "---->\u53d1\u9001\u6210\u529f\uff1a" + dataString);
                    dataArraryList.remove(0);
                }
            } else {
                sleepThread();
            }
        }
        Log.w("RemoteDiag", "------------>\u7ebf\u7a0b\u7ed3\u675f<-------------------");
    }

    public synchronized void notifyThread() {
        Log.w("RemoteDiag", "------------>\u7ebf\u7a0b\u5524\u9192<-------------------");
        isWait = false;
        notify();
    }

    public synchronized void sleepThread() {
        Log.w("RemoteDiag", "------------>\u7ebf\u7a0b\u4f11\u7720<-------------------");
        isWait = true;
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
