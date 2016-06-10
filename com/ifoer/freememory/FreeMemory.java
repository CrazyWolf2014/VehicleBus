package com.ifoer.freememory;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Looper;
import android.text.format.Formatter;
import java.util.List;

public class FreeMemory {
    private static FreeMemory instance;
    private ActivityManager am;
    private long endMemory;
    private long firstMemory;
    private Context mContext;

    /* renamed from: com.ifoer.freememory.FreeMemory.1 */
    class C06711 implements Runnable {
        C06711() {
        }

        public void run() {
            FreeMemory.this.getRunningProcess();
        }
    }

    public FreeMemory(Context context) {
        this.am = null;
        this.firstMemory = 0;
        this.endMemory = 0;
        this.mContext = context;
        this.am = (ActivityManager) this.mContext.getSystemService("activity");
    }

    public static FreeMemory getInstance(Context context) {
        if (instance == null) {
            instance = new FreeMemory(context);
        }
        return instance;
    }

    public void getProcessList() {
        freeMemory();
    }

    public void freeMemory() {
        new Thread(new C06711()).start();
    }

    public void getRunningProcess() {
        List<RunningAppProcessInfo> list2 = this.am.getRunningAppProcesses();
        this.firstMemory = getRunningTaskMemory().longValue();
        for (RunningAppProcessInfo ti : list2) {
            if (!(ti.processName.equals("system") || ti.processName.equals("com.android.phone") || !ti.processName.equals("com.ifoer.expedition.crp229"))) {
                this.am.restartPackage(ti.processName);
                this.am.killBackgroundProcesses(ti.processName);
            }
        }
        this.endMemory = getRunningTaskMemory().longValue();
        Looper.prepare();
        if (this.endMemory > this.firstMemory) {
            new Builder(this.mContext).setMessage("\u91ca\u653e\u5185\u5b58: " + Formatter.formatFileSize(this.mContext, this.endMemory - this.firstMemory)).setPositiveButton("OK", null);
        }
        Looper.loop();
    }

    public List<RunningTaskInfo> _getRunningTask() {
        List<RunningTaskInfo> list = this.am.getRunningTasks(100);
        this.am.getMemoryInfo(new MemoryInfo());
        return list;
    }

    public Long getRunningTaskMemory() {
        MemoryInfo outInfo = new MemoryInfo();
        this.am.getMemoryInfo(outInfo);
        return Long.valueOf(outInfo.availMem);
    }
}
