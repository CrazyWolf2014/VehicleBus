package com.nostra13.universalimageloader.utils;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public final class ImageSizeUtils {

    /* renamed from: com.nostra13.universalimageloader.utils.ImageSizeUtils.1 */
    static /* synthetic */ class C07961 {
        static final /* synthetic */ int[] f1344x841fdc36;

        static {
            f1344x841fdc36 = new int[ViewScaleType.values().length];
            try {
                f1344x841fdc36[ViewScaleType.FIT_INSIDE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1344x841fdc36[ViewScaleType.CROP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private ImageSizeUtils() {
    }

    public static ImageSize defineTargetSizeForView(ImageAware imageAware, ImageSize maxImageSize) {
        int width = imageAware.getWidth();
        if (width <= 0) {
            width = maxImageSize.getWidth();
        }
        int height = imageAware.getHeight();
        if (height <= 0) {
            height = maxImageSize.getHeight();
        }
        return new ImageSize(width, height);
    }

    public static int computeImageSampleSize(ImageSize srcSize, ImageSize targetSize, ViewScaleType viewScaleType, boolean powerOf2Scale) {
        int srcWidth = srcSize.getWidth();
        int srcHeight = srcSize.getHeight();
        int targetWidth = targetSize.getWidth();
        int targetHeight = targetSize.getHeight();
        int scale = 1;
        int widthScale = srcWidth / targetWidth;
        int heightScale = srcHeight / targetHeight;
        switch (C07961.f1344x841fdc36[viewScaleType.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                if (!powerOf2Scale) {
                    scale = Math.max(widthScale, heightScale);
                    break;
                }
                while (true) {
                    if (srcWidth / 2 < targetWidth && srcHeight / 2 < targetHeight) {
                        break;
                    }
                    srcWidth /= 2;
                    srcHeight /= 2;
                    scale *= 2;
                }
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (!powerOf2Scale) {
                    scale = Math.min(widthScale, heightScale);
                    break;
                }
                while (srcWidth / 2 >= targetWidth && srcHeight / 2 >= targetHeight) {
                    srcWidth /= 2;
                    srcHeight /= 2;
                    scale *= 2;
                }
                break;
        }
        if (scale < 1) {
            return 1;
        }
        return scale;
    }

    public static float computeImageScale(ImageSize srcSize, ImageSize targetSize, ViewScaleType viewScaleType, boolean stretch) {
        int destWidth;
        int srcWidth = srcSize.getWidth();
        int srcHeight = srcSize.getHeight();
        int targetWidth = targetSize.getWidth();
        int targetHeight = targetSize.getHeight();
        float widthScale = ((float) srcWidth) / ((float) targetWidth);
        float heightScale = ((float) srcHeight) / ((float) targetHeight);
        int destHeight;
        if ((viewScaleType != ViewScaleType.FIT_INSIDE || widthScale < heightScale) && (viewScaleType != ViewScaleType.CROP || widthScale >= heightScale)) {
            destWidth = (int) (((float) srcWidth) / heightScale);
            destHeight = targetHeight;
        } else {
            destWidth = targetWidth;
            destHeight = (int) (((float) srcHeight) / widthScale);
        }
        if ((stretch || destWidth >= srcWidth || destHeight >= srcHeight) && (!stretch || destWidth == srcWidth || destHeight == srcHeight)) {
            return 1.0f;
        }
        return ((float) destWidth) / ((float) srcWidth);
    }
}
