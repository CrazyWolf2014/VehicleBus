package org.vudroid.pdfdroid.codec;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import java.nio.ByteBuffer;
import org.vudroid.core.codec.CodecPage;

public class PdfPage implements CodecPage {
    private long docHandle;
    private long pageHandle;

    private static native void free(long j);

    private static native void getMediaBox(long j, float[] fArr);

    private native void nativeCreateView(long j, long j2, int[] iArr, float[] fArr, int[] iArr2);

    private static native long open(long j, int i);

    private static native void render(long j, long j2, int[] iArr, float[] fArr, ByteBuffer byteBuffer, ByteBuffer byteBuffer2);

    private PdfPage(long pageHandle, long docHandle) {
        this.pageHandle = pageHandle;
        this.docHandle = docHandle;
    }

    public boolean isDecoding() {
        return false;
    }

    public void waitForDecode() {
    }

    public int getWidth() {
        return (int) getMediaBox().width();
    }

    public int getHeight() {
        return (int) getMediaBox().height();
    }

    public Bitmap renderBitmap(int width, int height, RectF pageSliceBounds) {
        Matrix matrix = new Matrix();
        matrix.postScale(((float) width) / getMediaBox().width(), ((float) (-height)) / getMediaBox().height());
        matrix.postTranslate(0.0f, (float) height);
        matrix.postTranslate((-pageSliceBounds.left) * ((float) width), (-pageSliceBounds.top) * ((float) height));
        matrix.postScale(1.0f / pageSliceBounds.width(), 1.0f / pageSliceBounds.height());
        return render(new Rect(0, 0, width, height), matrix);
    }

    static PdfPage createPage(long dochandle, int pageno) {
        return new PdfPage(open(dochandle, pageno), dochandle);
    }

    protected void finalize() throws Throwable {
        recycle();
        super.finalize();
    }

    public synchronized void recycle() {
        if (this.pageHandle != 0) {
            free(this.pageHandle);
            this.pageHandle = 0;
        }
    }

    private RectF getMediaBox() {
        float[] box = new float[4];
        getMediaBox(this.pageHandle, box);
        return new RectF(box[0], box[1], box[2], box[3]);
    }

    public Bitmap render(Rect viewbox, Matrix matrix) {
        int[] mRect = new int[]{viewbox.left, viewbox.top, viewbox.right, viewbox.bottom};
        float[] matrixSource = new float[9];
        matrixArray = new float[6];
        matrix.getValues(matrixSource);
        matrixArray[0] = matrixSource[0];
        matrixArray[1] = matrixSource[3];
        matrixArray[2] = matrixSource[1];
        matrixArray[3] = matrixSource[4];
        matrixArray[4] = matrixSource[2];
        matrixArray[5] = matrixSource[5];
        int width = viewbox.width();
        int height = viewbox.height();
        int[] bufferarray = new int[(width * height)];
        nativeCreateView(this.docHandle, this.pageHandle, mRect, matrixArray, bufferarray);
        return Bitmap.createBitmap(bufferarray, width, height, Config.RGB_565);
    }
}
