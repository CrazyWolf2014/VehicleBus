package com.ifoer.image;

import android.content.Context;
import android.graphics.Bitmap;
import com.ifoer.image.GDApplication.OnLowMemoryListener;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class ImageCache implements OnLowMemoryListener {
    private final HashMap<String, SoftReference<Bitmap>> mSoftCache;

    public ImageCache(Context context) {
        this.mSoftCache = new HashMap();
        GDUtils.getGDApplication(context).registerOnLowMemoryListener(this);
    }

    public static ImageCache from(Context context) {
        return GDUtils.getImageCache(context);
    }

    public Bitmap get(String url) {
        SoftReference<Bitmap> ref = (SoftReference) this.mSoftCache.get(url);
        if (ref == null) {
            return null;
        }
        Bitmap bitmap = (Bitmap) ref.get();
        if (bitmap != null) {
            return bitmap;
        }
        this.mSoftCache.remove(url);
        return bitmap;
    }

    public void put(String url, Bitmap bitmap) {
        this.mSoftCache.put(url, new SoftReference(bitmap));
    }

    public void flush() {
        this.mSoftCache.clear();
    }

    public void onLowMemoryReceived() {
        flush();
    }
}
