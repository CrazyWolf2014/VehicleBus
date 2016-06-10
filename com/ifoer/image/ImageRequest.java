package com.ifoer.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import com.ifoer.image.ImageLoader.ImageLoaderCallback;
import java.util.concurrent.Future;

public class ImageRequest {
    private static ImageLoader sImageLoader;
    private ImageProcessor mBitmapProcessor;
    private ImageRequestCallback mCallback;
    private Future<?> mFuture;
    private Options mOptions;
    private String mUrl;

    public interface ImageRequestCallback {
        void onImageRequestCancelled(ImageRequest imageRequest);

        void onImageRequestEnded(ImageRequest imageRequest, Bitmap bitmap);

        void onImageRequestFailed(ImageRequest imageRequest, Throwable th);

        void onImageRequestStarted(ImageRequest imageRequest);
    }

    private class InnerCallback implements ImageLoaderCallback {
        private InnerCallback() {
        }

        public void onImageLoadingStarted(ImageLoader loader) {
            if (ImageRequest.this.mCallback != null) {
                ImageRequest.this.mCallback.onImageRequestStarted(ImageRequest.this);
            }
        }

        public void onImageLoadingEnded(ImageLoader loader, Bitmap bitmap) {
            if (!(ImageRequest.this.mCallback == null || ImageRequest.this.isCancelled())) {
                ImageRequest.this.mCallback.onImageRequestEnded(ImageRequest.this, bitmap);
            }
            ImageRequest.this.mFuture = null;
        }

        public void onImageLoadingFailed(ImageLoader loader, Throwable exception) {
            if (!(ImageRequest.this.mCallback == null || ImageRequest.this.isCancelled())) {
                ImageRequest.this.mCallback.onImageRequestFailed(ImageRequest.this, exception);
            }
            ImageRequest.this.mFuture = null;
        }
    }

    public ImageRequest(String url, ImageRequestCallback callback) {
        this(url, callback, null);
    }

    public ImageRequest(String url, ImageRequestCallback callback, ImageProcessor bitmapProcessor) {
        this(url, callback, bitmapProcessor, null);
    }

    public ImageRequest(String url, ImageRequestCallback callback, ImageProcessor bitmapProcessor, Options options) {
        this.mUrl = url;
        this.mCallback = callback;
        this.mBitmapProcessor = bitmapProcessor;
        this.mOptions = options;
    }

    public void setImageRequestCallback(ImageRequestCallback callback) {
        this.mCallback = callback;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void load(Context context) {
        if (this.mFuture == null) {
            if (sImageLoader == null) {
                sImageLoader = new ImageLoader(context);
            }
            this.mFuture = sImageLoader.loadImage(this.mUrl, new InnerCallback(), this.mBitmapProcessor, this.mOptions);
        }
    }

    public void cancel() {
        if (!isCancelled()) {
            this.mFuture.cancel(false);
            if (this.mCallback != null) {
                this.mCallback.onImageRequestCancelled(this);
            }
        }
    }

    public final boolean isCancelled() {
        return this.mFuture.isCancelled();
    }
}
