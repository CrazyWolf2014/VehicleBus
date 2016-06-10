package com.ifoer.image;

import android.graphics.Bitmap;

public class ChainImageProcessor implements ImageProcessor {
    ImageProcessor[] mProcessors;

    public ChainImageProcessor(ImageProcessor... processors) {
        this.mProcessors = processors;
    }

    public Bitmap processImage(Bitmap bitmap) {
        for (ImageProcessor processor : this.mProcessors) {
            bitmap = processor.processImage(bitmap);
        }
        return bitmap;
    }
}
