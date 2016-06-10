package com.ifoer.expeditionphone.vin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.ifoer.mine.Contact;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.achartengine.renderer.DefaultRenderer;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.ksoap2.SoapEnvelope;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class VinCameraActivity extends Activity implements Callback {
    public static final String DATA_PATH;
    public static final String RESULTS_PATH;
    private static final String SDPATH;
    public static final int cameramanager_focus_failed = 7;
    public static final int cameramanager_focus_succeded = 6;
    public static final int cameramanager_requestpicture = 5;
    private static final int installactivity_killApp = 1;
    public static int mBitmapWidth = 0;
    private static final int mezzofanti_ocrFinished = 4;
    private static final int mezzofanti_restartCaptureMode = 2;
    private static final int mezzofanti_startCamera = 3;
    private String DEFAULT_LANGUAGE;
    private String EXPECTED_FILE;
    private String IMAGE_PATH;
    private String PICTURE_FILE;
    String TAG;
    private String TESSBASE_PATH;
    private Button cancel;
    private Button capture;
    private Bitmap mBitmaps;
    private LinearLayout mChildLayout;
    private Button mCurrentButton;
    int mCurrentIndex;
    private PopupWindow mPopwin;
    private ImageView mVinImageView;
    private TextView mVinText;
    private LinearLayout mVinTextLayout;
    private Handler m_MessageHandler;
    private boolean m_bHasSurface;
    private boolean m_bLineMode;
    private boolean m_bPreviewReady;
    private CaptureLayout m_clCapture;

    /* renamed from: com.ifoer.expeditionphone.vin.VinCameraActivity.1 */
    class C06591 extends Handler {
        C06591() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VinCameraActivity.installactivity_killApp /*1*/:
                    Log.v(VinCameraActivity.this.TAG, "Killed by installer.");
                    Process.killProcess(Process.myPid());
                    break;
                case VinCameraActivity.mezzofanti_restartCaptureMode /*2*/:
                    System.gc();
                    break;
                case VinCameraActivity.mezzofanti_startCamera /*3*/:
                    VinCameraActivity.this.InitCamera();
                    break;
                case VinCameraActivity.mezzofanti_ocrFinished /*4*/:
                    if (!VinCameraActivity.this.m_bLineMode) {
                        System.gc();
                        VinCameraActivity.this.StopCamera();
                        break;
                    }
                    break;
                case VinCameraActivity.cameramanager_requestpicture /*5*/:
                    try {
                        Bitmap mBitmap = BitmapFactory.decodeByteArray((byte[]) msg.obj, 0, ((byte[]) msg.obj).length);
                        msg.obj = null;
                        System.gc();
                        if (VinCameraActivity.this.m_bLineMode) {
                            Rect frame = CameraManager.get().GetFramingRect(VinCameraActivity.this.m_bLineMode);
                            mBitmap = Bitmap.createBitmap(mBitmap, frame.left - 0, frame.top - 50, (frame.right - frame.left) + 0, frame.bottom - frame.top);
                        }
                        Bitmap bmp = Bitmap.createBitmap(VinCameraActivity.mBitmapWidth, 100, Config.ARGB_8888);
                        ColorMatrix cMatrix = new ColorMatrix();
                        cMatrix.set(new float[]{0.6796875f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.6796875f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.6796875f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
                        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(cMatrix);
                        Paint paint = new Paint();
                        paint.setColorFilter(colorMatrixFilter);
                        new Canvas(bmp).drawBitmap(mBitmap, 0.0f, 0.0f, paint);
                        VinCameraActivity.this.mBitmaps = bmp;
                        VinCameraActivity.this.mVinImageView = (ImageView) VinCameraActivity.this.findViewById(C0136R.id.image);
                        VinCameraActivity.this.mVinImageView.setImageBitmap(bmp);
                        if (VinCameraActivity.this.mBitmaps == null) {
                            VinCameraActivity.this.mVinText.setText(C0136R.string.analysisFail);
                            Toast.makeText(VinCameraActivity.this, C0136R.string.analysisFail, 0).show();
                            break;
                        }
                        VinCameraActivity.this.mVinText.setText(C0136R.string.pictureInfoAnalysis);
                        Toast.makeText(VinCameraActivity.this, C0136R.string.pictureInfoAnalysis, 0).show();
                        VinCameraActivity.this.m_MessageHandler.sendEmptyMessageDelayed(9, 700);
                        break;
                    } catch (Throwable th) {
                        Log.v(VinCameraActivity.this.TAG, "exception: handler-cmrequestpic: " + th.toString());
                        if (!VinCameraActivity.this.m_bLineMode) {
                            VinCameraActivity.this.m_MessageHandler.sendEmptyMessage(VinCameraActivity.mezzofanti_restartCaptureMode);
                            break;
                        }
                    }
                    break;
                case VinCameraActivity.cameramanager_focus_succeded /*6*/:
                    VinCameraActivity.this.RequestCameraTakePicture();
                    break;
                case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                    VinCameraActivity.this.ocr();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.vin.VinCameraActivity.2 */
    class C06602 implements OnClickListener {
        C06602() {
        }

        public void onClick(View v) {
            if (VinCameraActivity.this.getResources().getString(C0136R.string.dataCollection).equals(VinCameraActivity.this.capture.getText().toString())) {
                if (Environment.getExternalStorageState().equals("mounted")) {
                    VinCameraActivity.this.capture.setText(C0136R.string.sure);
                    VinCameraActivity.this.cancel.setText(C0136R.string.cancel);
                    VinCameraActivity.this.RequestCameraFocus();
                    return;
                }
                Toast.makeText(VinCameraActivity.this, C0136R.string.noCameraRecognition, 0).show();
            } else if (VinCameraActivity.this.getResources().getString(C0136R.string.sure).equals(VinCameraActivity.this.capture.getText().toString())) {
                String strVin = VinCameraActivity.this.mVinText.getText().toString();
                if (!VinCameraActivity.isNum(strVin)) {
                    strVin = XmlPullParser.NO_NAMESPACE;
                }
                Intent intent = new Intent();
                intent.putExtra("CAMERA", strVin);
                VinCameraActivity.this.setResult(XStream.PRIORITY_VERY_HIGH, intent);
                VinCameraActivity.this.finish();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.vin.VinCameraActivity.3 */
    class C06613 implements OnClickListener {
        C06613() {
        }

        public void onClick(View v) {
            if (VinCameraActivity.this.getResources().getString(C0136R.string.exitBtn).equals(VinCameraActivity.this.cancel.getText().toString())) {
                VinCameraActivity.this.finish();
            } else if (VinCameraActivity.this.getResources().getString(C0136R.string.cancel).equals(VinCameraActivity.this.cancel.getText().toString())) {
                VinCameraActivity.this.capture.setText(C0136R.string.dataCollection);
                VinCameraActivity.this.cancel.setText(C0136R.string.exitBtn);
                VinCameraActivity.this.mVinText.setText(XmlPullParser.NO_NAMESPACE);
                if (VinCameraActivity.this.mVinTextLayout.getChildCount() > 0) {
                    VinCameraActivity.this.mVinTextLayout.removeAllViews();
                }
                VinCameraActivity.this.mVinImageView.setImageBitmap(null);
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.vin.VinCameraActivity.4 */
    class C06624 implements OnClickListener {
        private final /* synthetic */ Button val$b;

        C06624(Button button) {
            this.val$b = button;
        }

        public void onClick(View v) {
            if (VinCameraActivity.isNum(this.val$b.getText())) {
                VinCameraActivity.this.mCurrentButton.setText(this.val$b.getText());
                VinCameraActivity.this.mCurrentButton.setTextColor(-65536);
                char[] f = VinCameraActivity.this.mVinText.getText().toString().toCharArray();
                f[VinCameraActivity.this.mCurrentButton.getId()] = this.val$b.getText().toString().toCharArray()[0];
                VinCameraActivity.this.mVinText.setText(new String(f));
            }
            VinCameraActivity.this.mPopwin.dismiss();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.vin.VinCameraActivity.5 */
    class C06635 implements OnClickListener {
        private final /* synthetic */ Button val$button;

        C06635(Button button) {
            this.val$button = button;
        }

        public void onClick(View v) {
            VinCameraActivity.this.mCurrentButton = this.val$button;
            if (VinCameraActivity.this.mPopwin.isShowing()) {
                VinCameraActivity.this.mPopwin.dismiss();
            }
            VinCameraActivity.this.mPopwin.showAtLocation(v, 48, 0, SoapEnvelope.VER12);
            VinCameraActivity.this.mPopwin.isShowing();
            VinCameraActivity.this.mPopwin.setFocusable(true);
        }
    }

    public VinCameraActivity() {
        this.TAG = "bcf";
        this.TESSBASE_PATH = SDPATH + "/tesseract/";
        this.DEFAULT_LANGUAGE = "eng";
        this.IMAGE_PATH = this.TESSBASE_PATH + "/img.png";
        this.EXPECTED_FILE = this.TESSBASE_PATH + "/tessdata/" + this.DEFAULT_LANGUAGE + ".traineddata";
        this.PICTURE_FILE = SDPATH + "/vinscan/";
        this.mBitmaps = null;
        this.m_bLineMode = true;
        this.m_bHasSurface = false;
        this.mChildLayout = null;
        this.mCurrentIndex = 0;
        this.m_MessageHandler = new C06591();
        this.m_bPreviewReady = false;
    }

    static {
        SDPATH = Environment.getExternalStorageDirectory().getPath();
        DATA_PATH = SDPATH + "/tessdata/";
        RESULTS_PATH = DATA_PATH + "out/";
        mBitmapWidth = KEYRecord.OWNER_HOST;
    }

    protected void onCreate(Bundle savedInstanceState) {
        Throwable e;
        super.onCreate(savedInstanceState);
        setRequestedOrientation(0);
        requestWindowFeature(installactivity_killApp);
        getWindow().setFlags(KEYRecord.OWNER_HOST, KEYRecord.OWNER_HOST);
        getWindow().addFlags(Flags.FLAG8);
        CameraManager.Initialize(getApplication());
        setContentView(C0136R.layout.vin_camera);
        this.mVinText = (TextView) findViewById(C0136R.id.field);
        this.mVinTextLayout = (LinearLayout) findViewById(C0136R.id.vin_layout);
        this.m_clCapture = (CaptureLayout) findViewById(C0136R.id.mezzofanti_capturelayout_view);
        this.m_clCapture.invalidate();
        this.m_MessageHandler.sendEmptyMessage(mezzofanti_startCamera);
        this.capture = (Button) findViewById(C0136R.id.capture);
        this.capture.setOnClickListener(new C06602());
        this.cancel = (Button) findViewById(C0136R.id.cancel);
        this.cancel.setOnClickListener(new C06613());
        if (Environment.getExternalStorageState().equals("mounted")) {
            File file = new File(this.TESSBASE_PATH + "tessdata/");
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                File file2 = new File(this.EXPECTED_FILE);
                try {
                    if (!file2.exists()) {
                        copyFileFromAssetsToSDCard(this.TESSBASE_PATH + "tessdata/");
                        return;
                    }
                    return;
                } catch (Throwable th) {
                    e = th;
                    file = file2;
                    e.printStackTrace();
                    return;
                }
            } catch (Throwable th2) {
                e = th2;
                e.printStackTrace();
                return;
            }
        }
        Toast.makeText(this, C0136R.string.noCameraRecognition, 0).show();
    }

    protected void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = ((SurfaceView) findViewById(C0136R.id.mezzofanti_preview_view)).getHolder();
        surfaceHolder.setFormat(-2);
        if (this.m_bHasSurface) {
            InitCamera(surfaceHolder);
            return;
        }
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(mezzofanti_startCamera);
    }

    protected void onStart() {
        super.onStart();
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(C0136R.layout.vm_keybord, null);
        this.mPopwin = new PopupWindow(layout, -2, -2);
        for (int i = 0; i < layout.getChildCount(); i += installactivity_killApp) {
            this.mChildLayout = (LinearLayout) layout.getChildAt(i);
            for (int j = 0; j < this.mChildLayout.getChildCount(); j += installactivity_killApp) {
                Button b = (Button) this.mChildLayout.getChildAt(j);
                this.mCurrentIndex = j;
                b.setOnClickListener(new C06624(b));
            }
        }
        this.mPopwin.update();
    }

    protected void onDestroy() {
        StopCamera();
        super.onDestroy();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(this.TAG, "surfaceCreated.");
        if (!this.m_bHasSurface) {
            this.m_bHasSurface = true;
            SetCameraSurfaceHolder(holder);
            this.m_MessageHandler.sendEmptyMessage(mezzofanti_startCamera);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.m_bHasSurface = false;
    }

    private boolean RequestCameraFocus() {
        CameraManager.get().RequestCameraFocus(this.m_MessageHandler);
        CameraManager.get().RequestAutoFocus();
        return true;
    }

    private Bitmap bitmapFanzhuan(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        bitmap.getPixels(new int[(width * height)], 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i += installactivity_killApp) {
            for (int j = 0; j < width; j += installactivity_killApp) {
                int grey = bitmap.getPixel(j, i);
                int green = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & grey) >> 8;
                int blue = grey & KEYRecord.PROTOCOL_ANY;
                if (((16711680 & grey) >> 16) >= Opcodes.FCMPG || green >= Opcodes.FCMPG || blue >= Opcodes.FCMPG) {
                    bitmap.setPixel(j, i, DefaultRenderer.BACKGROUND_COLOR);
                } else {
                    bitmap.setPixel(j, i, -1);
                }
            }
        }
        return bitmap;
    }

    private boolean RequestCameraTakePicture() {
        if (!this.m_bPreviewReady) {
            return false;
        }
        CameraManager.get().RequestPicture(this.m_MessageHandler);
        CameraManager.get().GetPicture();
        return true;
    }

    private void SetCameraSurfaceHolder(SurfaceHolder surfaceHolder) {
        if (surfaceHolder != null && CameraManager.get() != null) {
            CameraManager.get().SetSurfaceHolder(surfaceHolder);
        }
    }

    private void InitCamera(SurfaceHolder surfaceHolder) {
        if (CameraManager.get() != null) {
            this.m_bPreviewReady = true;
            CameraManager.get().OpenDriver(surfaceHolder);
            CameraManager.get().StartPreview();
            if (this.m_bLineMode) {
                CameraManager.SetImgDivisor(mezzofanti_restartCaptureMode);
                CameraManager.get().SetCameraParameters();
            }
        }
    }

    private void InitCamera() {
        Log.v(this.TAG, "InitCamera: start");
        if (CameraManager.get() != null) {
            Log.v(this.TAG, "InitCamera: OpenDriver");
            this.m_bPreviewReady = true;
            if (CameraManager.get().OpenDriver()) {
                Log.v(this.TAG, "InitCamera: StartPreview");
                CameraManager.get().StartPreview();
            }
            Log.v(this.TAG, "InitCamera: end");
        }
    }

    private void StopCamera() {
        CameraManager.get().StopPreview();
        CameraManager.get().CloseDriver();
        this.m_bPreviewReady = false;
    }

    public String saveMyBitmap(String bitName, Bitmap mBitmap) {
        File files = new File(this.PICTURE_FILE);
        if (!files.exists()) {
            files.mkdirs();
        }
        File file = new File(this.PICTURE_FILE + bitName + ".png");
        try {
            file.createNewFile();
        } catch (IOException e) {
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        mBitmap.compress(CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e32) {
            e32.printStackTrace();
        }
        return file.getPath();
    }

    protected void ocr() {
        new Options().inSampleSize = mezzofanti_restartCaptureMode;
        Bitmap bitmap = this.mBitmaps;
        try {
            int rotate = 0;
            switch (new ExifInterface(saveMyBitmap("scanimg", bitmap)).getAttributeInt(Constants.Orientation, installactivity_killApp)) {
                case mezzofanti_startCamera /*3*/:
                    rotate = Opcodes.GETFIELD;
                    break;
                case cameramanager_focus_succeded /*6*/:
                    rotate = 90;
                    break;
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    rotate = 270;
                    break;
            }
            if (rotate != 0) {
                Matrix mtx = new Matrix();
                mtx.preRotate((float) rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, mBitmapWidth, 100, mtx, false).copy(Config.ARGB_8888, true);
            }
        } catch (IOException e) {
            Log.e(this.TAG, "Rotate or coversion failed: " + e.toString());
        }
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(this.TESSBASE_PATH, this.DEFAULT_LANGUAGE);
        baseApi.setImage(bitmap);
        String recognizedText = baseApi.getUTF8Text();
        baseApi.clear();
        baseApi.end();
        if (this.DEFAULT_LANGUAGE.equalsIgnoreCase("eng")) {
            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        }
        if (recognizedText.length() != 0) {
            recognizedText = recognizedText.replaceAll(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE).toUpperCase();
            if (recognizedText.length() < 17) {
                int index = 17 - recognizedText.length();
                String tempVin = Contact.RELATION_ASK;
                for (int i = 0; i < index - 1; i += installactivity_killApp) {
                    tempVin = new StringBuilder(String.valueOf(tempVin)).append(Contact.RELATION_ASK).toString();
                }
                recognizedText = new StringBuilder(String.valueOf(recognizedText)).append(tempVin).toString();
            } else if (recognizedText.length() > 17) {
                recognizedText = recognizedText.substring(0, 17);
            }
            if (this.mVinTextLayout.getChildCount() > 0) {
                this.mVinTextLayout.removeAllViews();
            }
            this.mVinText.setText(recognizedText.toUpperCase().trim());
            createVinButton(recognizedText);
        }
    }

    private void createVinButton(String recognizedText) {
        for (int i = 0; i < recognizedText.length(); i += installactivity_killApp) {
            Button button = new Button(this);
            button.setId(i);
            button.setText(recognizedText.subSequence(i, i + installactivity_killApp));
            button.setTextColor(-1);
            button.setLayoutParams(new LayoutParams(-1, -2, 1.0f));
            this.mVinTextLayout.addView(button);
            button.getBackground().setAlpha(100);
            button.setOnClickListener(new C06635(button));
        }
    }

    private void copyFileFromAssetsToSDCard(String toPath) throws Throwable {
        File filePaths = new File(toPath);
        if (!filePaths.exists()) {
            filePaths.mkdirs();
        }
        try {
            InputStream fosfrom = getResources().getAssets().open("tessdata/" + this.DEFAULT_LANGUAGE + ".traineddata");
            OutputStream fosto = new FileOutputStream(new StringBuilder(String.valueOf(toPath)).append(this.DEFAULT_LANGUAGE).append(".traineddata").toString());
            byte[] bt = new byte[Flags.FLAG5];
            while (true) {
                int c = fosfrom.read(bt);
                if (c <= 0) {
                    fosfrom.close();
                    fosto.close();
                    return;
                }
                fosto.write(bt, 0, c);
            }
        } catch (Exception e) {
        }
    }

    public static boolean isNum(String str) {
        return str.matches("^[A-Za-z0-9]*$");
    }
}
