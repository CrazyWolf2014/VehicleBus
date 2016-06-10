package com.ifoer.image;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.codehaus.jackson.smile.SmileConstants;
import org.xmlpull.v1.XmlPullParser;

public class ImageLoader {
    private static final String LOG_TAG;
    private static final int ON_END = 258;
    private static final int ON_FAIL = 257;
    private static final int ON_START = 256;
    private static AssetManager sAssetManager;
    private static Options sDefaultOptions;
    private static ExecutorService sExecutor;
    private static ImageCache sImageCache;

    private class ImageFetcher implements Runnable {
        private ImageProcessor mBitmapProcessor;
        private ImageHandler mHandler;
        private Options mOptions;
        private String mUrl;

        public ImageFetcher(String url, ImageLoaderCallback callback, ImageProcessor bitmapProcessor, Options options) {
            this.mUrl = url;
            this.mHandler = new ImageHandler(url, callback, null);
            this.mBitmapProcessor = bitmapProcessor;
            this.mOptions = options;
        }

        public void run() {
            Process.setThreadPriority(10);
            Handler h = this.mHandler;
            Bitmap bitmap = null;
            Throwable throwable = null;
            h.sendMessage(Message.obtain(h, ImageLoader.ON_START));
            try {
                if (TextUtils.isEmpty(this.mUrl)) {
                    throw new Exception("The given URL cannot be null or empty");
                }
                InputStream inputStream;
                if (this.mUrl.startsWith("file:///android_asset/")) {
                    inputStream = ImageLoader.sAssetManager.open(this.mUrl.replaceFirst("file:///android_asset/", XmlPullParser.NO_NAMESPACE));
                } else {
                    inputStream = new URL(this.mUrl).openStream();
                }
                bitmap = BitmapFactory.decodeStream(inputStream, null, this.mOptions == null ? ImageLoader.sDefaultOptions : this.mOptions);
                if (!(this.mBitmapProcessor == null || bitmap == null)) {
                    Bitmap processedBitmap = this.mBitmapProcessor.processImage(bitmap);
                    if (processedBitmap != null) {
                        bitmap = processedBitmap;
                    }
                }
                if (bitmap == null) {
                    if (throwable == null) {
                        throwable = new Exception("Skia image decoding failed");
                    }
                    h.sendMessage(Message.obtain(h, ImageLoader.ON_FAIL, throwable));
                    return;
                }
                h.sendMessage(Message.obtain(h, ImageLoader.ON_END, bitmap));
            } catch (Throwable e) {
                throwable = e;
            }
        }
    }

    private class ImageHandler extends Handler {
        private ImageLoaderCallback mCallback;
        private String mUrl;

        private ImageHandler(String url, ImageLoaderCallback callback) {
            this.mUrl = url;
            this.mCallback = callback;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ImageLoader.ON_START /*256*/:
                    if (this.mCallback != null) {
                        this.mCallback.onImageLoadingStarted(ImageLoader.this);
                    }
                case ImageLoader.ON_FAIL /*257*/:
                    if (this.mCallback != null) {
                        this.mCallback.onImageLoadingFailed(ImageLoader.this, (Throwable) msg.obj);
                    }
                case ImageLoader.ON_END /*258*/:
                    Bitmap bitmap = msg.obj;
                    ImageLoader.sImageCache.put(this.mUrl, bitmap);
                    if (this.mCallback != null) {
                        this.mCallback.onImageLoadingEnded(ImageLoader.this, bitmap);
                    }
                default:
                    super.handleMessage(msg);
            }
        }
    }

    public interface ImageLoaderCallback {
        void onImageLoadingEnded(ImageLoader imageLoader, Bitmap bitmap);

        void onImageLoadingFailed(ImageLoader imageLoader, Throwable th);

        void onImageLoadingStarted(ImageLoader imageLoader);
    }

    static {
        LOG_TAG = ImageLoader.class.getSimpleName();
    }

    public ImageLoader(Context context) {
        if (sImageCache == null) {
            sImageCache = GDUtils.getImageCache(context);
        }
        if (sExecutor == null) {
            sExecutor = GDUtils.getExecutor(context);
        }
        if (sDefaultOptions == null) {
            sDefaultOptions = new Options();
            sDefaultOptions.inDither = true;
            sDefaultOptions.inScaled = true;
            sDefaultOptions.inSampleSize = 4;
            sDefaultOptions.inDensity = SmileConstants.TOKEN_PREFIX_SHORT_UNICODE;
            sDefaultOptions.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
        }
        sAssetManager = context.getAssets();
    }

    public Future<?> loadImage(String url, ImageLoaderCallback callback) {
        return loadImage(url, callback, null);
    }

    public Future<?> loadImage(String url, ImageLoaderCallback callback, ImageProcessor bitmapProcessor) {
        return loadImage(url, callback, bitmapProcessor, null);
    }

    public Future<?> loadImage(String url, ImageLoaderCallback callback, ImageProcessor bitmapProcessor, Options options) {
        return sExecutor.submit(new ImageFetcher(url, callback, bitmapProcessor, options));
    }
}
