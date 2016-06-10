package com.cnlaunch.framework.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class FreeMemory {
    private static FreeMemory instance;
    private ActivityManager mAppManager;
    private Context mContext;
    private long mEndMemory;
    private long mFirstMemory;
    private ArrayList<String> mSelfProcess;

    /* renamed from: com.cnlaunch.framework.utils.FreeMemory.1 */
    class C01341 implements Runnable {
        C01341() {
        }

        public void run() {
            FreeMemory.this.getRunningProcess();
        }
    }

    public FreeMemory(Context context) {
        this.mAppManager = null;
        this.mFirstMemory = 0;
        this.mEndMemory = 0;
        this.mSelfProcess = new ArrayList();
        this.mContext = context;
        this.mAppManager = (ActivityManager) this.mContext.getSystemService("activity");
    }

    public static FreeMemory getInstance(Context context) {
        if (instance == null) {
            instance = new FreeMemory(context);
        }
        return instance;
    }

    public void freeMemory() {
        new Thread(new C01341()).start();
    }

    public void getRunningProcess() {
        List<RunningAppProcessInfo> runningAppList = this.mAppManager.getRunningAppProcesses();
        this.mSelfProcess.clear();
        this.mSelfProcess.add("com.cnlaunch.diagnoseservice");
        this.mSelfProcess.add(getSelfPackageName());
        this.mFirstMemory = getRunningTaskMemory().longValue();
        for (RunningAppProcessInfo ti : runningAppList) {
            if (!(ti.processName.equals("system") || ti.processName.equals("com.android.phone"))) {
                for (int i = 0; i < this.mSelfProcess.size(); i++) {
                    if (!ti.processName.equals(this.mSelfProcess.get(i))) {
                        this.mAppManager.restartPackage(ti.processName);
                        this.mAppManager.killBackgroundProcesses(ti.processName);
                    }
                }
            }
        }
        this.mEndMemory = getRunningTaskMemory().longValue();
    }

    public List<RunningTaskInfo> _getRunningTask() {
        List<RunningTaskInfo> list = this.mAppManager.getRunningTasks(100);
        this.mAppManager.getMemoryInfo(new MemoryInfo());
        return list;
    }

    public Long getRunningTaskMemory() {
        MemoryInfo outInfo = new MemoryInfo();
        this.mAppManager.getMemoryInfo(outInfo);
        return Long.valueOf(outInfo.availMem);
    }

    private String getSelfPackageName() {
        String packageNames = XmlPullParser.NO_NAMESPACE;
        try {
            return this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return packageNames;
        }
    }
}
