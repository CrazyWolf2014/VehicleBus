package org.vudroid.core;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.view.View;

public interface DecodeService {

    public interface DecodeCallback {
        void decodeComplete(Bitmap bitmap);
    }

    void decodePage(Object obj, int i, DecodeCallback decodeCallback, float f, RectF rectF);

    int getEffectivePagesHeight();

    int getEffectivePagesWidth();

    int getPageCount();

    int getPageHeight(int i);

    int getPageWidth(int i);

    void open(Uri uri);

    void recycle();

    void setContainerView(View view);

    void setContentResolver(ContentResolver contentResolver);

    void stopDecoding(Object obj);
}
