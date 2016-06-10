package com.cnlaunch.x431pro.common;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import java.io.File;

public class ImageLoaderHelper {

    /* renamed from: com.cnlaunch.x431pro.common.ImageLoaderHelper.1 */
    class C12701 extends SimpleImageLoadingListener {
        boolean cacheFound;

        C12701() {
        }

        public void onLoadingStarted(String imageUri, View view) {
            this.cacheFound = !MemoryCacheUtil.findCacheKeysForImageUri(imageUri, ImageLoader.getInstance().getMemoryCache()).isEmpty();
            if (!this.cacheFound) {
                File discCache = DiscCacheUtil.findInCache(imageUri, ImageLoader.getInstance().getDiscCache());
                if (discCache != null) {
                    this.cacheFound = discCache.exists();
                }
            }
        }

        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (this.cacheFound) {
                MemoryCacheUtil.removeFromCache(imageUri, ImageLoader.getInstance().getMemoryCache());
                DiscCacheUtil.removeFromCache(imageUri, ImageLoader.getInstance().getDiscCache());
                ImageLoader.getInstance().displayImage(imageUri, (ImageView) view);
            }
        }
    }

    public static SimpleImageLoadingListener getImageLoadingListener() {
        return new C12701();
    }
}
