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
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.ifoer.mine.Contact;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.ksoap2.SoapEnvelope;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class VinPhotoActivity extends Activity {
    public static final String DATA_PATH;
    public static final String RESULTS_PATH;
    private static final String SDPATH;
    private String DEFAULT_LANGUAGE;
    private String EXPECTED_FILE;
    private String PICTURE_FILE;
    String TAG;
    private String TESSBASE_PATH;
    private Button cancel;
    private Button capture;
    private ImageView iv;
    private Bitmap mBitmap;
    private LinearLayout mChildLayout;
    private Button mCurrentButton;
    int mCurrentIndex;
    private String mImagePath;
    private PopupWindow mPopwin;
    private String mVin;
    private TextView mVinText;
    private LinearLayout mVinTextLayout;
    private Handler m_MessageHandler;

    /* renamed from: com.ifoer.expeditionphone.vin.VinPhotoActivity.1 */
    class C06641 extends Handler {
        C06641() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    try {
                        msg.obj = null;
                        System.gc();
                        Bitmap bmp = Bitmap.createBitmap(VinPhotoActivity.this.mBitmap.getWidth(), VinPhotoActivity.this.mBitmap.getHeight(), Config.ARGB_8888);
                        ColorMatrix cMatrix = new ColorMatrix();
                        cMatrix.set(new float[]{0.6796875f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.6796875f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.6796875f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
                        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(cMatrix);
                        Paint paint = new Paint();
                        paint.setColorFilter(colorMatrixFilter);
                        new Canvas(bmp).drawBitmap(VinPhotoActivity.this.mBitmap, 0.0f, 0.0f, paint);
                        VinPhotoActivity.this.mBitmap = bmp;
                        if (VinPhotoActivity.this.mBitmap == null) {
                            VinPhotoActivity.this.mVinText.setText(C0136R.string.analysisFail);
                            Toast.makeText(VinPhotoActivity.this, C0136R.string.analysisFail, 0).show();
                            break;
                        }
                        VinPhotoActivity.this.mVinText.setText(C0136R.string.pictureInfoAnalysis);
                        Toast.makeText(VinPhotoActivity.this, C0136R.string.pictureInfoAnalysis, 0).show();
                        VinPhotoActivity.this.m_MessageHandler.sendEmptyMessageDelayed(9, 700);
                        break;
                    } catch (Throwable th) {
                        Log.v(VinPhotoActivity.this.TAG, "exception: handler-cmrequestpic: " + th.toString());
                        break;
                    }
                case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                    VinPhotoActivity.this.ocr();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.vin.VinPhotoActivity.2 */
    class C06652 implements OnClickListener {
        C06652() {
        }

        public void onClick(View v) {
            VinPhotoActivity.this.mVin = VinPhotoActivity.this.mVinText.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("CAMERA", VinPhotoActivity.this.mVin);
            VinPhotoActivity.this.setResult(XStream.PRIORITY_VERY_HIGH, intent);
            VinPhotoActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.vin.VinPhotoActivity.3 */
    class C06663 implements OnClickListener {
        C06663() {
        }

        public void onClick(View v) {
            VinPhotoActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.vin.VinPhotoActivity.4 */
    class C06674 implements OnClickListener {
        private final /* synthetic */ Button val$b;

        C06674(Button button) {
            this.val$b = button;
        }

        public void onClick(View v) {
            if (VinPhotoActivity.isNum(this.val$b.getText())) {
                VinPhotoActivity.this.mCurrentButton.setText(this.val$b.getText());
                VinPhotoActivity.this.mCurrentButton.setTextColor(-65536);
                char[] f = VinPhotoActivity.this.mVinText.getText().toString().toCharArray();
                f[VinPhotoActivity.this.mCurrentButton.getId()] = this.val$b.getText().toString().toCharArray()[0];
                VinPhotoActivity.this.mVinText.setText(new String(f));
            }
            VinPhotoActivity.this.mPopwin.dismiss();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.vin.VinPhotoActivity.5 */
    class C06685 implements OnClickListener {
        private final /* synthetic */ Button val$button;

        C06685(Button button) {
            this.val$button = button;
        }

        public void onClick(View v) {
            VinPhotoActivity.this.mCurrentButton = this.val$button;
            if (VinPhotoActivity.this.mPopwin.isShowing()) {
                VinPhotoActivity.this.mPopwin.dismiss();
            }
            VinPhotoActivity.this.mPopwin.showAtLocation(v, 48, 0, SoapEnvelope.VER12);
            VinPhotoActivity.this.mPopwin.isShowing();
            VinPhotoActivity.this.mPopwin.setFocusable(true);
        }
    }

    public VinPhotoActivity() {
        this.TAG = "bcf";
        this.TESSBASE_PATH = SDPATH + "/tesseract/";
        this.DEFAULT_LANGUAGE = "eng";
        this.EXPECTED_FILE = this.TESSBASE_PATH + "/tessdata/" + this.DEFAULT_LANGUAGE + ".traineddata";
        this.PICTURE_FILE = SDPATH + "/vinscan/";
        this.mBitmap = null;
        this.mChildLayout = null;
        this.mCurrentIndex = 0;
        this.m_MessageHandler = new C06641();
    }

    static {
        SDPATH = Environment.getExternalStorageDirectory().getPath();
        DATA_PATH = SDPATH + "/tessdata/";
        RESULTS_PATH = DATA_PATH + "out/";
    }

    protected void onCreate(Bundle savedInstanceState) {
        Throwable e;
        super.onCreate(savedInstanceState);
        setRequestedOrientation(0);
        requestWindowFeature(1);
        getWindow().setFlags(KEYRecord.OWNER_HOST, KEYRecord.OWNER_HOST);
        getWindow().addFlags(Flags.FLAG8);
        setContentView(C0136R.layout.vin_photo);
        this.mImagePath = getIntent().getStringExtra("IMAGE_PATH");
        this.mVinText = (TextView) findViewById(C0136R.id.field);
        this.mVinTextLayout = (LinearLayout) findViewById(C0136R.id.vin_layout);
        this.iv = (ImageView) findViewById(C0136R.id.image);
        Options opts = new Options();
        opts.inSampleSize = 2;
        this.mBitmap = BitmapFactory.decodeFile(this.mImagePath, opts);
        this.iv.setImageBitmap(this.mBitmap);
        this.capture = (Button) findViewById(C0136R.id.capture);
        this.capture.setOnClickListener(new C06652());
        this.cancel = (Button) findViewById(C0136R.id.cancel);
        this.cancel.setOnClickListener(new C06663());
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
                    }
                    this.m_MessageHandler.sendEmptyMessage(0);
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

    protected void onStart() {
        super.onStart();
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(C0136R.layout.vm_keybord, null);
        this.mPopwin = new PopupWindow(layout, -2, -2);
        for (int i = 0; i < layout.getChildCount(); i++) {
            this.mChildLayout = (LinearLayout) layout.getChildAt(i);
            for (int j = 0; j < this.mChildLayout.getChildCount(); j++) {
                Button b = (Button) this.mChildLayout.getChildAt(j);
                this.mCurrentIndex = j;
                b.setOnClickListener(new C06674(b));
            }
        }
        this.mPopwin.update();
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
        new Options().inSampleSize = 2;
        Bitmap bitmap = this.mBitmap;
        try {
            int rotate = 0;
            switch (new ExifInterface(saveMyBitmap("scanimg", bitmap)).getAttributeInt(Constants.Orientation, 1)) {
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    rotate = Opcodes.GETFIELD;
                    break;
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    rotate = 90;
                    break;
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    rotate = 270;
                    break;
            }
            if (rotate != 0) {
                Matrix mtx = new Matrix();
                mtx.preRotate((float) rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, this.mBitmap.getWidth(), this.mBitmap.getHeight(), mtx, false).copy(Config.ARGB_8888, true);
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
                for (int i = 0; i < index - 1; i++) {
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
        for (int i = 0; i < recognizedText.length(); i++) {
            Button button = new Button(this);
            button.setId(i);
            button.setText(recognizedText.subSequence(i, i + 1));
            button.setTextColor(-1);
            button.setLayoutParams(new LayoutParams(-1, -2, 1.0f));
            this.mVinTextLayout.addView(button);
            button.getBackground().setAlpha(100);
            button.setOnClickListener(new C06685(button));
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
