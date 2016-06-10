package com.ifoer.image;

import android.app.Application;
import android.content.Intent;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;

public class GDApplication extends Application {
    private static final int CORE_POOL_SIZE = 5;
    public static final String PATH_ERROR_LOG;
    public static String SYS_CACHE_PATH;
    private static final ThreadFactory sThreadFactory;
    private ExecutorService mExecutorService;
    private ImageCache mImageCache;
    private ArrayList<WeakReference<OnLowMemoryListener>> mLowMemoryListeners;
    private boolean need2Exit;
    public Thread serialportNoThread;
    private UEHandler ueHandler;

    /* renamed from: com.ifoer.image.GDApplication.1 */
    class C06741 implements ThreadFactory {
        private final AtomicInteger mCount;

        C06741() {
            this.mCount = new AtomicInteger(1);
        }

        public Thread newThread(Runnable r) {
            return new Thread(r, "GreenDroid thread #" + this.mCount.getAndIncrement());
        }
    }

    public interface OnLowMemoryListener {
        void onLowMemoryReceived();
    }

    static {
        PATH_ERROR_LOG = File.separator + DataPacketExtension.ELEMENT_NAME + File.separator + DataPacketExtension.ELEMENT_NAME + File.separator + "lab.sodino.errorreport" + File.separator + "files" + File.separator + "error.log";
        sThreadFactory = new C06741();
    }

    public GDApplication() {
        this.mLowMemoryListeners = new ArrayList();
    }

    public void onCreate() {
        super.onCreate();
        SYS_CACHE_PATH = getFilesDir().getPath();
        CrashHandler.getInstance().init(getApplicationContext());
        this.need2Exit = false;
        this.ueHandler = new UEHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(this.ueHandler);
    }

    public void setNeed2Exit(boolean bool) {
        this.need2Exit = bool;
    }

    public boolean need2Exit() {
        return this.need2Exit;
    }

    public ExecutorService getExecutor() {
        if (this.mExecutorService == null) {
            this.mExecutorService = Executors.newFixedThreadPool(CORE_POOL_SIZE, sThreadFactory);
        }
        return this.mExecutorService;
    }

    public ImageCache getImageCache() {
        if (this.mImageCache == null) {
            this.mImageCache = new ImageCache(this);
        }
        return this.mImageCache;
    }

    public Class<?> getHomeActivityClass() {
        return null;
    }

    public Intent getMainApplicationIntent() {
        return null;
    }

    public void registerOnLowMemoryListener(OnLowMemoryListener listener) {
        if (listener != null) {
            this.mLowMemoryListeners.add(new WeakReference(listener));
        }
    }

    public void unregisterOnLowMemoryListener(OnLowMemoryListener listener) {
        if (listener != null) {
            int i = 0;
            while (i < this.mLowMemoryListeners.size()) {
                OnLowMemoryListener l = (OnLowMemoryListener) ((WeakReference) this.mLowMemoryListeners.get(i)).get();
                if (l == null || l == listener) {
                    this.mLowMemoryListeners.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        int i = 0;
        while (i < this.mLowMemoryListeners.size()) {
            OnLowMemoryListener listener = (OnLowMemoryListener) ((WeakReference) this.mLowMemoryListeners.get(i)).get();
            if (listener == null) {
                this.mLowMemoryListeners.remove(i);
            } else {
                listener.onLowMemoryReceived();
                i++;
            }
        }
    }
}
