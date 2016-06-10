package com.mapabc.minimap.map.vmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import com.amap.mapapi.core.ResUtil;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.File;
import org.codehaus.jackson.smile.SmileConstants;
import org.ksoap2.SoapEnvelope;
import org.xbill.DNS.KEYRecord;

public class NativeMapEngine {
    public static final int ICON_HEIGHT = 12;
    public static final int ICON_WIDTH = 12;
    public static final int MAX_ICON_SIZE = 128;
    public static final int MAX_LABELAINE = 7;
    Bitmap[] f1321a;
    int f1322b;

    private static native void nativeClearBackground(int i);

    private static native int nativeCreate(String str);

    private static native void nativeFillBitmapBufferData(int i, String str, byte[] bArr);

    private static native void nativeFinalizer(int i);

    private static native int nativeGetBKColor(int i, int i2);

    private static native int nativeGetBitmapCacheSize(int i);

    private static native byte[] nativeGetGridData(int i, String str);

    private static native boolean nativeHasBitMapData(int i, String str);

    private static native boolean nativeHasGridData(int i, String str);

    private static native void nativePutBitmapData(int i, String str, byte[] bArr, int i2, int i3);

    private static native void nativePutGridData(int i, byte[] bArr, int i2, int i3);

    private static native void nativeRemoveBitmapData(int i, String str, int i2);

    private static native void nativeSetBackgroundImageData(int i, byte[] bArr);

    private static native void nativeSetBitmapCacheMaxSize(int i, int i2);

    private static native void nativeSetIconData(int i, int i2, byte[] bArr);

    private static native void nativeSetStyleData(int i, byte[] bArr);

    private static native void nativeSetVectormapCacheMaxSize(int i, int i2);

    static {
        try {
            System.loadLibrary(NativeMap.MINIMAP_VERSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NativeMapEngine(Context context) {
        String str;
        this.f1321a = new Bitmap[MAX_ICON_SIZE];
        this.f1322b = 0;
        if (Environment.getExternalStorageState().equals("mounted")) {
            File file = new File(Environment.getExternalStorageDirectory(), "Mapabc");
            if (!file.exists()) {
                file.mkdir();
            }
            File file2 = new File(file, "mini_mapv2");
            if (!file2.exists()) {
                file2.mkdir();
            }
            str = file2.toString() + FilePathGenerator.ANDROID_DIR_SEP;
        } else {
            str = context.getCacheDir().toString() + FilePathGenerator.ANDROID_DIR_SEP;
        }
        this.f1322b = nativeCreate(str);
        setBitmapCacheMaxSize(KEYRecord.OWNER_ZONE);
        setVecotormapCacheMaxSize(AsyncTaskManager.REQUEST_SUCCESS_CODE);
    }

    public Bitmap getIconBitmap(int i) {
        return this.f1321a[i];
    }

    public void initIconData(Context context) {
        int i = 1;
        setBackgroudImageData(ResUtil.m539a(context, "bk.data"));
        Options options = new Options();
        options.inSampleSize = 1;
        options.inPreferredConfig = Config.ARGB_8888;
        while (i < 53) {
            byte[] a = ResUtil.m539a(context, i + ".png");
            if (a != null) {
                this.f1321a[i] = BitmapFactory.decodeByteArray(a, 0, a.length, options);
            }
            i++;
        }
    }

    public void initStyleData(Context context) {
        String str = "style_l.data";
        if (context.getResources().getDisplayMetrics().densityDpi == SoapEnvelope.VER12 || context.getResources().getDisplayMetrics().densityDpi == SmileConstants.TOKEN_PREFIX_SHORT_UNICODE) {
            str = "style_s.data";
        }
        setStyleData(ResUtil.m539a(context, str));
    }

    protected void finalize() throws Throwable {
        destory();
    }

    public void destory() {
        if (this.f1322b != 0) {
            nativeFinalizer(this.f1322b);
            this.f1322b = 0;
            for (int i = 1; i < 53; i++) {
                if (this.f1321a[i] != null) {
                    this.f1321a[i].recycle();
                    this.f1321a[i] = null;
                }
            }
        }
    }

    public void clearBackground() {
        nativeClearBackground(this.f1322b);
    }

    public void putGridData(byte[] bArr, int i, int i2) {
        nativePutGridData(this.f1322b, bArr, i, i2);
    }

    public byte[] getGridData(String str) {
        return nativeGetGridData(this.f1322b, str);
    }

    public boolean hasGridData(String str) {
        return nativeHasGridData(this.f1322b, str);
    }

    public void setIconData(int i, byte[] bArr) {
        nativeSetIconData(this.f1322b, i, bArr);
    }

    public void setBackgroudImageData(byte[] bArr) {
        nativeSetBackgroundImageData(this.f1322b, bArr);
    }

    public void setStyleData(byte[] bArr) {
        nativeSetStyleData(this.f1322b, bArr);
    }

    public int getBKColor(int i) {
        return nativeGetBKColor(this.f1322b, i);
    }

    public boolean hasBitMapData(String str) {
        return nativeHasBitMapData(this.f1322b, str);
    }

    public void putBitmapData(String str, byte[] bArr, int i, int i2) {
        nativePutBitmapData(this.f1322b, str, bArr, i, i2);
    }

    public void removeBitmapData(String str, int i) {
        nativeRemoveBitmapData(this.f1322b, str, i);
    }

    public void fillBitmapBufferData(String str, byte[] bArr) {
        nativeFillBitmapBufferData(this.f1322b, str, bArr);
    }

    public int getBitmapCacheSize() {
        return nativeGetBitmapCacheSize(this.f1322b);
    }

    public void setBitmapCacheMaxSize(int i) {
        nativeSetBitmapCacheMaxSize(this.f1322b, i);
    }

    public void setVecotormapCacheMaxSize(int i) {
        nativeSetVectormapCacheMaxSize(this.f1322b, i);
    }
}
