package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.utils.C0797L;
import org.achartengine.renderer.DefaultRenderer;

public class RoundedBitmapDisplayer implements BitmapDisplayer {
    private final int roundPixels;

    /* renamed from: com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer.1 */
    static /* synthetic */ class C07921 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType;

        static {
            $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_INSIDE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_START.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_END.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_CROP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_XY.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.MATRIX.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    public RoundedBitmapDisplayer(int roundPixels) {
        this.roundPixels = roundPixels;
    }

    public Bitmap display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (imageAware instanceof ImageViewAware) {
            Bitmap roundedBitmap = roundCorners(bitmap, (ImageViewAware) imageAware, this.roundPixels);
            imageAware.setImageBitmap(roundedBitmap);
            return roundedBitmap;
        }
        throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
    }

    public static Bitmap roundCorners(Bitmap bitmap, ImageViewAware imageAware, int roundPixels) {
        ImageView imageView = imageAware.getWrappedView();
        if (imageView == null) {
            C0797L.m1479w("View is collected probably. Can't round bitmap corners without view properties.", new Object[0]);
            return bitmap;
        }
        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
        int vw = imageAware.getWidth();
        int vh = imageAware.getHeight();
        if (vw <= 0) {
            vw = bw;
        }
        if (vh <= 0) {
            vh = bh;
        }
        ScaleType scaleType = imageView.getScaleType();
        if (scaleType == null) {
            return bitmap;
        }
        Rect srcRect;
        Rect destRect;
        int width;
        int height;
        Bitmap roundBitmap;
        int x;
        int y;
        switch (C07921.$SwitchMap$android$widget$ImageView$ScaleType[scaleType.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                int destHeight;
                int destWidth;
                if (((float) vw) / ((float) vh) > ((float) bw) / ((float) bh)) {
                    destHeight = Math.min(vh, bh);
                    destWidth = (int) (((float) bw) / (((float) bh) / ((float) destHeight)));
                } else {
                    destWidth = Math.min(vw, bw);
                    destHeight = (int) (((float) bh) / (((float) bw) / ((float) destWidth)));
                }
                x = (vw - destWidth) / 2;
                y = (vh - destHeight) / 2;
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(x, y, x + destWidth, y + destHeight);
                width = vw;
                height = vh;
                break;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                int srcWidth;
                int srcHeight;
                if (((float) vw) / ((float) vh) > ((float) bw) / ((float) bh)) {
                    srcWidth = bw;
                    srcHeight = (int) (((float) vh) * (((float) bw) / ((float) vw)));
                    x = 0;
                    y = (bh - srcHeight) / 2;
                } else {
                    srcWidth = (int) (((float) vw) * (((float) bh) / ((float) vh)));
                    srcHeight = bh;
                    x = (bw - srcWidth) / 2;
                    y = 0;
                }
                width = srcWidth;
                height = srcHeight;
                srcRect = new Rect(x, y, x + srcWidth, y + srcHeight);
                destRect = new Rect(0, 0, width, height);
                break;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                width = vw;
                height = vh;
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(0, 0, width, height);
                break;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                width = Math.min(vw, bw);
                height = Math.min(vh, bh);
                x = (bw - width) / 2;
                y = (bh - height) / 2;
                srcRect = new Rect(x, y, x + width, y + height);
                destRect = new Rect(0, 0, width, height);
                break;
            default:
                if (((float) vw) / ((float) vh) > ((float) bw) / ((float) bh)) {
                    width = (int) (((float) bw) / (((float) bh) / ((float) vh)));
                    height = vh;
                } else {
                    width = vw;
                    height = (int) (((float) bh) / (((float) bw) / ((float) vw)));
                }
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(0, 0, width, height);
                break;
        }
        try {
            roundBitmap = getRoundedCornerBitmap(bitmap, roundPixels, srcRect, destRect, width, height);
        } catch (OutOfMemoryError e) {
            C0797L.m1477e(e, "Can't create bitmap with rounded corners. Not enough memory.", new Object[0]);
            roundBitmap = bitmap;
        }
        return roundBitmap;
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPixels, Rect srcRect, Rect destRect, int width, int height) {
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        RectF destRectF = new RectF(destRect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(DefaultRenderer.BACKGROUND_COLOR);
        canvas.drawRoundRect(destRectF, (float) roundPixels, (float) roundPixels, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, srcRect, destRectF, paint);
        return output;
    }
}
