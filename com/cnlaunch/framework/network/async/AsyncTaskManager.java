package com.cnlaunch.framework.network.async;

import android.content.Context;
import android.os.Build.VERSION;
import com.cnlaunch.framework.utils.NLog;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTaskManager {
    public static final int DEFAULT_DOWNLOAD_CODE = 10000;
    public static final int HTTP_ERROR_CODE = -200;
    public static final int HTTP_NULL_CODE = -400;
    public static final int JSONMAPPING_ERROR_CODE = -300;
    public static final int REQUEST_ERROR_CODE = -999;
    public static final int REQUEST_SUCCESS_CODE = 200;
    private static AsyncTaskManager instance;
    private static ExecutorService mExecutorService;
    private static Map<Integer, WeakReference<BaseAsyncTask>> requestMap;
    public final int MAX_CONNECTIONS_NUM;
    private Context mContext;
    private final String tag;

    private AsyncTaskManager(Context context) {
        this.tag = AsyncTaskManager.class.getSimpleName();
        this.MAX_CONNECTIONS_NUM = 10;
        this.mContext = context;
        mExecutorService = Executors.newFixedThreadPool(10);
        requestMap = new WeakHashMap();
    }

    public static AsyncTaskManager getInstance(Context context) {
        if (instance == null) {
            synchronized (AsyncTaskManager.class) {
                if (instance == null) {
                    instance = new AsyncTaskManager(context);
                }
            }
        }
        return instance;
    }

    public void request(int requestCode, OnDataListener listener) {
        request(requestCode, true, listener);
    }

    public void request(int requestCode, boolean isCheckNetwork, OnDataListener listener) {
        DownLoad bean = new DownLoad(requestCode, isCheckNetwork, listener);
        if (requestCode > 0) {
            BaseAsyncTask mAsynctask = new BaseAsyncTask(bean, this.mContext);
            if (VERSION.SDK_INT >= 11) {
                mAsynctask.executeOnExecutor(mExecutorService, new Object[0]);
            } else {
                mAsynctask.execute(new Object[0]);
            }
            requestMap.put(Integer.valueOf(requestCode), new WeakReference(mAsynctask));
            return;
        }
        NLog.m917e(this.tag, "the error is requestCode < 0");
    }

    public void cancelRequest(int requestCode) {
        WeakReference<BaseAsyncTask> requestTask = (WeakReference) requestMap.get(Integer.valueOf(requestCode));
        if (requestTask != null) {
            BaseAsyncTask request = (BaseAsyncTask) requestTask.get();
            if (request != null) {
                request.cancel(true);
            }
        }
        requestMap.remove(Integer.valueOf(requestCode));
    }

    public void cancelRequest() {
        if (requestMap != null) {
            for (Entry<Integer, WeakReference<BaseAsyncTask>> entry : requestMap.entrySet()) {
                cancelRequest(((Integer) entry.getKey()).intValue());
            }
            requestMap.clear();
        }
    }
}
