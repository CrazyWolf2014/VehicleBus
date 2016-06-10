package com.iflytek.ui.p015a;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.TypedValue;
import java.io.InputStream;
import org.codehaus.jackson.smile.SmileConstants;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;

/* renamed from: com.iflytek.ui.a.a */
public class C0297a {
    private static int f1124a;

    static {
        f1124a = 0;
    }

    public static Bitmap m1354a(Resources resources, TypedValue typedValue, InputStream inputStream, Rect rect, Options options) {
        if (options == null) {
            options = new Options();
        }
        if (options.inDensity == 0 && typedValue != null) {
            int i = typedValue.density;
            if (i == 0) {
                options.inDensity = SmileConstants.TOKEN_PREFIX_SHORT_UNICODE;
            } else if (i != InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) {
                options.inDensity = i;
            }
        }
        if (options.inTargetDensity == 0 && resources != null) {
            options.inTargetDensity = resources.getDisplayMetrics().densityDpi;
        }
        return BitmapFactory.decodeStream(inputStream, rect, options);
    }

    private static Drawable m1355a(Resources resources, Bitmap bitmap, byte[] bArr, Rect rect, String str) {
        return bArr != null ? new NinePatchDrawable(resources, bitmap, bArr, rect, str) : new BitmapDrawable(resources, bitmap);
    }

    public static Drawable m1356a(Resources resources, TypedValue typedValue, InputStream inputStream, String str, Options options) {
        Rect rect = null;
        if (inputStream == null) {
            return null;
        }
        Rect rect2 = new Rect();
        if (options == null) {
            options = new Options();
        }
        Bitmap a = C0297a.m1354a(resources, typedValue, inputStream, rect2, options);
        if (a == null) {
            return null;
        }
        byte[] ninePatchChunk = a.getNinePatchChunk();
        if (ninePatchChunk == null || !NinePatch.isNinePatchChunk(ninePatchChunk)) {
            ninePatchChunk = null;
        } else {
            rect = rect2;
        }
        return C0297a.m1355a(resources, a, ninePatchChunk, rect, str);
    }
}
