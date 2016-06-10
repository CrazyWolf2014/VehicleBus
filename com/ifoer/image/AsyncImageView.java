package com.ifoer.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View.BaseSavedState;
import android.widget.ImageView;
import com.ifoer.image.ImageRequest.ImageRequestCallback;

public class AsyncImageView extends ImageView implements ImageRequestCallback {
    private static final int IMAGE_SOURCE_BITMAP = 2;
    private static final int IMAGE_SOURCE_DRAWABLE = 1;
    private static final int IMAGE_SOURCE_RESOURCE = 0;
    private static final int IMAGE_SOURCE_UNKNOWN = -1;
    private static final String LOG_TAG;
    private Bitmap mBitmap;
    private Bitmap mDefaultBitmap;
    private Drawable mDefaultDrawable;
    private int mDefaultResId;
    private ImageProcessor mImageProcessor;
    private int mImageSource;
    private OnImageViewLoadListener mOnImageViewLoadListener;
    private Options mOptions;
    private boolean mPaused;
    private ImageRequest mRequest;
    private String mUrl;

    public interface OnImageViewLoadListener {
        void onLoadingEnded(AsyncImageView asyncImageView, Bitmap bitmap);

        void onLoadingFailed(AsyncImageView asyncImageView, Throwable th);

        void onLoadingStarted(AsyncImageView asyncImageView);
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        String url;

        /* renamed from: com.ifoer.image.AsyncImageView.SavedState.1 */
        class C06721 implements Creator<SavedState> {
            C06721() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.url = in.readString();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.url);
        }

        static {
            CREATOR = new C06721();
        }
    }

    static {
        LOG_TAG = AsyncImageView.class.getSimpleName();
    }

    public AsyncImageView(Context context) {
        this(context, null);
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        this(context, attrs, IMAGE_SOURCE_RESOURCE);
    }

    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeDefaultValues();
    }

    private void initializeDefaultValues() {
        this.mImageSource = IMAGE_SOURCE_UNKNOWN;
        this.mPaused = false;
    }

    public boolean isLoading() {
        return this.mRequest != null;
    }

    public boolean isLoaded() {
        return this.mRequest == null && this.mBitmap != null;
    }

    public void setPaused(boolean paused) {
        if (this.mPaused != paused) {
            this.mPaused = paused;
            if (!paused) {
                reload();
            }
        }
    }

    public void setInDensity(int inDensity) {
        if (this.mOptions == null) {
            this.mOptions = new Options();
            this.mOptions.inDither = true;
            this.mOptions.inScaled = true;
            this.mOptions.inTargetDensity = getContext().getResources().getDisplayMetrics().densityDpi;
        }
        this.mOptions.inDensity = inDensity;
    }

    public void setOptions(Options options) {
        this.mOptions = options;
    }

    public void reload() {
        reload(false);
    }

    public void reload(boolean force) {
        if (this.mRequest == null && this.mUrl != null) {
            this.mBitmap = null;
            if (!force) {
                this.mBitmap = GDUtils.getImageCache(getContext()).get(this.mUrl);
            }
            if (this.mBitmap != null) {
                setImageBitmap(this.mBitmap);
                return;
            }
            setDefaultImage();
            this.mRequest = new ImageRequest(this.mUrl, this, this.mImageProcessor, this.mOptions);
            this.mRequest.load(getContext());
        }
    }

    public void stopLoading() {
        if (this.mRequest != null) {
            this.mRequest.cancel();
            this.mRequest = null;
        }
    }

    public void setOnImageViewLoadListener(OnImageViewLoadListener listener) {
        this.mOnImageViewLoadListener = listener;
    }

    public void setUrl(String url) {
        if (this.mBitmap == null || url == null || !url.equals(this.mUrl)) {
            stopLoading();
            this.mUrl = url;
            if (TextUtils.isEmpty(this.mUrl)) {
                this.mBitmap = null;
                setDefaultImage();
            } else if (this.mPaused) {
                this.mBitmap = GDUtils.getImageCache(getContext()).get(this.mUrl);
                if (this.mBitmap != null) {
                    setImageBitmap(this.mBitmap);
                } else {
                    setDefaultImage();
                }
            } else {
                reload();
            }
        }
    }

    public void setDefaultImageBitmap(Bitmap bitmap) {
        this.mImageSource = IMAGE_SOURCE_BITMAP;
        this.mDefaultBitmap = bitmap;
        setDefaultImage();
    }

    public void setDefaultImageDrawable(Drawable drawable) {
        this.mImageSource = IMAGE_SOURCE_DRAWABLE;
        this.mDefaultDrawable = drawable;
        setDefaultImage();
    }

    public void setDefaultImageResource(int resId) {
        this.mImageSource = IMAGE_SOURCE_RESOURCE;
        this.mDefaultResId = resId;
        setDefaultImage();
    }

    public void setImageProcessor(ImageProcessor imageProcessor) {
        this.mImageProcessor = imageProcessor;
    }

    private void setDefaultImage() {
        if (this.mBitmap == null) {
            switch (this.mImageSource) {
                case IMAGE_SOURCE_RESOURCE /*0*/:
                    setImageResource(this.mDefaultResId);
                case IMAGE_SOURCE_DRAWABLE /*1*/:
                    setImageDrawable(this.mDefaultDrawable);
                case IMAGE_SOURCE_BITMAP /*2*/:
                    setImageBitmap(this.mDefaultBitmap);
                default:
                    setImageDrawable(null);
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.url = this.mUrl;
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setUrl(ss.url);
    }

    public void onImageRequestStarted(ImageRequest request) {
        if (this.mOnImageViewLoadListener != null) {
            this.mOnImageViewLoadListener.onLoadingStarted(this);
        }
    }

    public void onImageRequestFailed(ImageRequest request, Throwable throwable) {
        this.mRequest = null;
        if (this.mOnImageViewLoadListener != null) {
            this.mOnImageViewLoadListener.onLoadingFailed(this, throwable);
        }
    }

    public void onImageRequestEnded(ImageRequest request, Bitmap image) {
        this.mBitmap = image;
        setImageBitmap(image);
        if (this.mOnImageViewLoadListener != null) {
            this.mOnImageViewLoadListener.onLoadingEnded(this, image);
        }
        this.mRequest = null;
    }

    public void onImageRequestCancelled(ImageRequest request) {
        this.mRequest = null;
        if (this.mOnImageViewLoadListener != null) {
            this.mOnImageViewLoadListener.onLoadingFailed(this, null);
        }
    }
}
