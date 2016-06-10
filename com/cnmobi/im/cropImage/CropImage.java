package com.cnmobi.im.cropImage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Environment;
import android.os.Handler;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.util.FileConstant;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.codehaus.jackson.util.BufferRecycler;
import org.xbill.DNS.KEYRecord;

public class CropImage {
    public static final File FILE_LOCAL;
    public static final File FILE_SDCARD;
    private Bitmap mBitmap;
    private Context mContext;
    public HighlightView mCrop;
    private Handler mHandler;
    private CropImageView mImageView;
    Runnable mRunFaceDetection;
    public boolean mSaving;
    public boolean mWaitingToPick;

    /* renamed from: com.cnmobi.im.cropImage.CropImage.1 */
    class C01981 implements Runnable {
        Face[] mFaces;
        Matrix mImageMatrix;
        int mNumFaces;
        float mScale;

        /* renamed from: com.cnmobi.im.cropImage.CropImage.1.1 */
        class C01971 implements Runnable {
            C01971() {
            }

            public void run() {
                CropImage.this.mWaitingToPick = C01981.this.mNumFaces > 1;
                C01981.this.makeDefault();
                CropImage.this.mImageView.invalidate();
                if (CropImage.this.mImageView.mHighlightViews.size() > 0) {
                    CropImage.this.mCrop = (HighlightView) CropImage.this.mImageView.mHighlightViews.get(0);
                    CropImage.this.mCrop.setFocus(true);
                }
            }
        }

        C01981() {
            this.mScale = 1.0f;
            this.mFaces = new Face[3];
        }

        private void handleFace(Face f) {
            PointF midPoint = new PointF();
            int r = ((int) (f.eyesDistance() * this.mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= this.mScale;
            midPoint.y *= this.mScale;
            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;
            HighlightView hv = new HighlightView(CropImage.this.mImageView);
            Rect imageRect = new Rect(0, 0, CropImage.this.mBitmap.getWidth(), CropImage.this.mBitmap.getHeight());
            RectF faceRect = new RectF((float) midX, (float) midY, (float) midX, (float) midY);
            faceRect.inset((float) (-r), (float) (-r));
            if (faceRect.left < 0.0f) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }
            if (faceRect.top < 0.0f) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }
            if (faceRect.right > ((float) imageRect.right)) {
                faceRect.inset(faceRect.right - ((float) imageRect.right), faceRect.right - ((float) imageRect.right));
            }
            if (faceRect.bottom > ((float) imageRect.bottom)) {
                faceRect.inset(faceRect.bottom - ((float) imageRect.bottom), faceRect.bottom - ((float) imageRect.bottom));
            }
            hv.setup(this.mImageMatrix, imageRect, faceRect, false, true);
            CropImage.this.mImageView.add(hv);
        }

        private void makeDefault() {
            HighlightView hv = new HighlightView(CropImage.this.mImageView);
            int width = CropImage.this.mBitmap.getWidth();
            int height = CropImage.this.mBitmap.getHeight();
            int cropWidth = (Math.min(width, height) * 4) / 5;
            int cropHeight = cropWidth;
            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;
            hv.setup(this.mImageMatrix, new Rect(0, 0, width, height), new RectF((float) x, (float) y, (float) (x + cropWidth), (float) (y + cropHeight)), false, true);
            CropImage.this.mImageView.add(hv);
        }

        private Bitmap prepareBitmap() {
            if (CropImage.this.mBitmap == null) {
                return null;
            }
            if (CropImage.this.mBitmap.getWidth() > KEYRecord.OWNER_ZONE) {
                this.mScale = 256.0f / ((float) CropImage.this.mBitmap.getWidth());
            }
            Matrix matrix = new Matrix();
            matrix.setScale(this.mScale, this.mScale);
            return Bitmap.createBitmap(CropImage.this.mBitmap, 0, 0, CropImage.this.mBitmap.getWidth(), CropImage.this.mBitmap.getHeight(), matrix, true);
        }

        public void run() {
            this.mImageMatrix = CropImage.this.mImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();
            this.mScale = 1.0f / this.mScale;
            if (faceBitmap != null) {
                this.mNumFaces = new FaceDetector(faceBitmap.getWidth(), faceBitmap.getHeight(), this.mFaces.length).findFaces(faceBitmap, this.mFaces);
            }
            if (!(faceBitmap == null || faceBitmap == CropImage.this.mBitmap)) {
                faceBitmap.recycle();
            }
            CropImage.this.mHandler.post(new C01971());
        }
    }

    /* renamed from: com.cnmobi.im.cropImage.CropImage.2 */
    class C02002 implements Runnable {
        private final /* synthetic */ float val$degrees;

        /* renamed from: com.cnmobi.im.cropImage.CropImage.2.1 */
        class C01991 implements Runnable {
            private final /* synthetic */ float val$degrees;
            private final /* synthetic */ CountDownLatch val$latch;

            C01991(float f, CountDownLatch countDownLatch) {
                this.val$degrees = f;
                this.val$latch = countDownLatch;
            }

            public void run() {
                try {
                    Matrix m = new Matrix();
                    m.setRotate(this.val$degrees);
                    Bitmap tb = Bitmap.createBitmap(CropImage.this.mBitmap, 0, 0, CropImage.this.mBitmap.getWidth(), CropImage.this.mBitmap.getHeight(), m, false);
                    CropImage.this.mBitmap = tb;
                    CropImage.this.mImageView.resetView(tb);
                    if (CropImage.this.mImageView.mHighlightViews.size() > 0) {
                        CropImage.this.mCrop = (HighlightView) CropImage.this.mImageView.mHighlightViews.get(0);
                        CropImage.this.mCrop.setFocus(true);
                    }
                } catch (Exception e) {
                }
                this.val$latch.countDown();
            }
        }

        C02002(float f) {
            this.val$degrees = f;
        }

        public void run() {
            CountDownLatch latch = new CountDownLatch(1);
            CropImage.this.mHandler.post(new C01991(this.val$degrees, latch));
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* renamed from: com.cnmobi.im.cropImage.CropImage.3 */
    class C02023 implements Runnable {

        /* renamed from: com.cnmobi.im.cropImage.CropImage.3.1 */
        class C02011 implements Runnable {
            private final /* synthetic */ Bitmap val$b;
            private final /* synthetic */ CountDownLatch val$latch;

            C02011(Bitmap bitmap, CountDownLatch countDownLatch) {
                this.val$b = bitmap;
                this.val$latch = countDownLatch;
            }

            public void run() {
                if (!(this.val$b == CropImage.this.mBitmap || this.val$b == null)) {
                    CropImage.this.mImageView.setImageBitmapResetBase(this.val$b, true);
                    CropImage.this.mBitmap.recycle();
                    CropImage.this.mBitmap = this.val$b;
                }
                if (CropImage.this.mImageView.getScale() == 1.0f) {
                    CropImage.this.mImageView.center(true, true);
                }
                this.val$latch.countDown();
            }
        }

        C02023() {
        }

        public void run() {
            CountDownLatch latch = new CountDownLatch(1);
            CropImage.this.mHandler.post(new C02011(CropImage.this.mBitmap, latch));
            try {
                latch.await();
                CropImage.this.mRunFaceDetection.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class BackgroundJob implements Runnable {
        private Handler mHandler;
        private Runnable mJob;
        private String message;

        /* renamed from: com.cnmobi.im.cropImage.CropImage.BackgroundJob.1 */
        class C02031 implements Runnable {
            private final /* synthetic */ CountDownLatch val$latch;

            C02031(CountDownLatch countDownLatch) {
                this.val$latch = countDownLatch;
            }

            public void run() {
                try {
                    BackgroundJob.this.mHandler.sendMessage(BackgroundJob.this.mHandler.obtainMessage(BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN));
                } catch (Exception e) {
                }
                this.val$latch.countDown();
            }
        }

        public BackgroundJob(String m, Runnable job, Handler handler) {
            this.message = m;
            this.mJob = job;
            this.mHandler = handler;
        }

        public void run() {
            CountDownLatch latch = new CountDownLatch(1);
            this.mHandler.post(new C02031(latch));
            try {
                latch.await();
                try {
                    this.mJob.run();
                } finally {
                    this.mHandler.sendMessage(this.mHandler.obtainMessage(CropImageActivity.REMOVE_PROGRESS));
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static {
        FILE_SDCARD = Environment.getExternalStorageDirectory();
        FILE_LOCAL = new File(FILE_SDCARD, "weixin");
    }

    public CropImage(Context context, CropImageView imageView, Handler handler) {
        this.mRunFaceDetection = new C01981();
        this.mContext = context;
        this.mImageView = imageView;
        this.mImageView.setCropImage(this);
        this.mHandler = handler;
    }

    public void crop(Bitmap bm) {
        this.mBitmap = bm;
        startFaceDetection();
    }

    public void startRotate(float d) {
        if (!((Activity) this.mContext).isFinishing()) {
            showProgressDialog(this.mContext.getResources().getString(C0136R.string.imWait), new C02002(d), this.mHandler);
        }
    }

    private void startFaceDetection() {
        if (!((Activity) this.mContext).isFinishing()) {
            showProgressDialog(this.mContext.getResources().getString(C0136R.string.imWait), new C02023(), this.mHandler);
        }
    }

    public Bitmap cropAndSave() {
        Bitmap bmp = onSaveClicked(this.mBitmap);
        this.mImageView.mHighlightViews.clear();
        return bmp;
    }

    public Bitmap cropAndSave(Bitmap bm) {
        Bitmap bmp = onSaveClicked(bm);
        this.mImageView.mHighlightViews.clear();
        return bmp;
    }

    public void cropCancel() {
        this.mImageView.mHighlightViews.clear();
        this.mImageView.invalidate();
    }

    private Bitmap onSaveClicked(Bitmap bm) {
        if (this.mSaving || this.mCrop == null) {
            return bm;
        }
        this.mSaving = true;
        Rect r = this.mCrop.getCropRect();
        Rect dr = this.mCrop.mDrawRect;
        int width = dr.width();
        int height = dr.height();
        Bitmap croppedImage = Bitmap.createBitmap(width, height, Config.RGB_565);
        new Canvas(croppedImage).drawBitmap(bm, r, new Rect(0, 0, width, height), null);
        return croppedImage;
    }

    public String saveToLocal(Bitmap bm) {
        String path = FileConstant.FILE_HEAD_PORTRAIT + "head portrait";
        try {
            FileOutputStream fos = new FileOutputStream(path);
            bm.compress(CompressFormat.JPEG, 75, fos);
            fos.flush();
            fos.close();
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private void showProgressDialog(String msg, Runnable job, Handler handler) {
        new Thread(new BackgroundJob(msg, job, handler)).start();
    }
}
