package com.cnmobi.im.cropImage;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.ImMainActivity;
import com.cnmobi.im.MineActivity;
import com.cnmobi.im.bo.LogoManager;
import com.cnmobi.im.util.FileConstant;
import com.cnmobi.im.util.SharePreferenceUtils;
import com.cnmobi.im.util.UiUtils;
import com.cnmobi.im.util.Utils;
import com.cnmobi.im.util.XmppConnection;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import org.xmlpull.v1.XmlPullParser;

public class CropImageActivity extends Activity implements OnClickListener {
    private static final int ERROR = 100;
    private static final int FAILURE_STATUE = 0;
    public static final int REMOVE_PROGRESS = 2001;
    public static final int SHOW_PROGRESS = 2000;
    private static final int SUCCESS_STATUE = 1;
    private String TAG;
    private LoadingDialog dialog;
    private UpLoadHeadPicHandler handler;
    private Bitmap mBitmap;
    private Button mCancel;
    private CropImage mCrop;
    private Handler mHandler;
    private CropImageView mImageView;
    private String mPath;
    private ProgressBar mProgressBar;
    private Button mSave;
    private Button rotateLeft;
    private Button rotateRight;
    public int screenHeight;
    public int screenWidth;
    private SharePreferenceUtils spInfo;

    /* renamed from: com.cnmobi.im.cropImage.CropImageActivity.1 */
    class C02041 extends Handler {
        C02041() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CropImageActivity.SHOW_PROGRESS /*2000*/:
                    CropImageActivity.this.mProgressBar.setVisibility(CropImageActivity.FAILURE_STATUE);
                case CropImageActivity.REMOVE_PROGRESS /*2001*/:
                    CropImageActivity.this.mHandler.removeMessages(CropImageActivity.SHOW_PROGRESS);
                    CropImageActivity.this.mProgressBar.setVisibility(4);
                default:
            }
        }
    }

    private class RetryQuery implements DialogInterface.OnClickListener {
        private RetryQuery() {
        }

        public void onClick(DialogInterface dialog, int which) {
            CropImageActivity.this.startThread();
        }
    }

    private class UpLoadHeadPicHandler extends Handler {
        private UpLoadHeadPicHandler() {
        }

        public void handleMessage(Message msg) {
            if (CropImageActivity.this.dialog != null && CropImageActivity.this.dialog.isShowing()) {
                CropImageActivity.this.dialog.dismiss();
            }
            switch (msg.what) {
                case CropImageActivity.FAILURE_STATUE /*0*/:
                    Toast.makeText(CropImageActivity.this, CropImageActivity.this.getString(C0136R.string.uploadHeadFail), CropImageActivity.FAILURE_STATUE).show();
                case CropImageActivity.SUCCESS_STATUE /*1*/:
                    Toast.makeText(CropImageActivity.this, CropImageActivity.this.getString(C0136R.string.uploadHeadSuccess), CropImageActivity.FAILURE_STATUE).show();
                    CropImageActivity.this.finish();
                case CropImageActivity.ERROR /*100*/:
                    Toast.makeText(CropImageActivity.this, CropImageActivity.this.getString(C0136R.string.connectedServerException), CropImageActivity.FAILURE_STATUE).show();
                default:
            }
        }
    }

    private class UpLoadHeadPicThread extends Thread {
        private UpLoadHeadPicThread() {
        }

        public void run() {
            VCard vcard = new VCard();
            try {
                vcard.load(XmppConnection.getConnection());
                try {
                    byte[] bytes = CropImageActivity.getByte(new File(CropImageActivity.this.mCrop.saveToLocal(CropImageActivity.this.mCrop.cropAndSave())));
                    String encodedImage = StringUtils.encodeBase64(bytes);
                    vcard.setAvatar(bytes, encodedImage);
                    vcard.setEncodedImage(encodedImage);
                    vcard.setField("PHOTO", "<TYPE>image/jpg</TYPE><BINVAL>" + encodedImage + "</BINVAL>", true);
                    try {
                        vcard.save(XmppConnection.getConnection());
                        CropImageActivity.this.handler.sendEmptyMessage(CropImageActivity.SUCCESS_STATUE);
                        LogoManager.getInstance().removeFromMap(ImMainActivity.mOwerJid);
                        File file = new File(FileConstant.FILE_LOGOS + UiUtils.getCCNumber(ImMainActivity.mOwerJid) + Util.PHOTO_DEFAULT_EXT);
                        if (file.exists()) {
                            file.delete();
                        }
                        MineActivity.context.getLogo();
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    CropImageActivity.this.handler.sendEmptyMessage(CropImageActivity.ERROR);
                }
            } catch (Exception e3) {
                CropImageActivity.this.handler.sendEmptyMessage(CropImageActivity.ERROR);
            }
        }
    }

    public CropImageActivity() {
        this.mPath = "CropImageActivity";
        this.TAG = XmlPullParser.NO_NAMESPACE;
        this.screenWidth = FAILURE_STATUE;
        this.screenHeight = FAILURE_STATUE;
        this.mHandler = new C02041();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.activity_crop_image);
        init();
    }

    protected void onStop() {
        super.onStop();
        if (this.mBitmap != null) {
            this.mBitmap = null;
        }
    }

    private void init() {
        this.spInfo = new SharePreferenceUtils(this);
        this.handler = new UpLoadHeadPicHandler();
        getWindowWH();
        this.mPath = getIntent().getStringExtra("path");
        this.mImageView = (CropImageView) findViewById(C0136R.id.gl_modify_avatar_image);
        this.mSave = (Button) findViewById(C0136R.id.gl_modify_avatar_save);
        this.mCancel = (Button) findViewById(C0136R.id.gl_modify_avatar_cancel);
        this.rotateLeft = (Button) findViewById(C0136R.id.gl_modify_avatar_rotate_left);
        this.rotateRight = (Button) findViewById(C0136R.id.gl_modify_avatar_rotate_right);
        this.mSave.setOnClickListener(this);
        this.mCancel.setOnClickListener(this);
        this.rotateLeft.setOnClickListener(this);
        this.rotateRight.setOnClickListener(this);
        try {
            this.mBitmap = createBitmap(this.mPath, this.screenWidth, this.screenHeight);
            if (this.mBitmap == null) {
                Toast.makeText(this, getString(C0136R.string.pictureNotFound), FAILURE_STATUE).show();
                finish();
            } else {
                resetImageView(this.mBitmap);
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(C0136R.string.pictureNotFound), FAILURE_STATUE).show();
            finish();
        }
        addProgressbar();
    }

    private void getWindowWH() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.screenWidth = dm.widthPixels;
        this.screenHeight = dm.heightPixels;
    }

    private void resetImageView(Bitmap b) {
        this.mImageView.clear();
        this.mImageView.setImageBitmap(b);
        this.mImageView.setImageBitmapResetBase(b, true);
        this.mCrop = new CropImage(this, this.mImageView, this.mHandler);
        this.mCrop.crop(b);
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.gl_modify_avatar_cancel) {
            finish();
        } else if (v.getId() == C0136R.id.gl_modify_avatar_save) {
            startThread();
        } else if (v.getId() == C0136R.id.gl_modify_avatar_rotate_left) {
            this.mCrop.startRotate(270.0f);
        } else if (v.getId() == C0136R.id.gl_modify_avatar_rotate_right) {
            this.mCrop.startRotate(90.0f);
        }
    }

    protected void addProgressbar() {
        this.mProgressBar = new ProgressBar(this);
        LayoutParams params = new LayoutParams(-2, -2);
        params.gravity = 17;
        addContentView(this.mProgressBar, params);
        this.mProgressBar.setVisibility(4);
    }

    public Bitmap createBitmap(String path, int w, int h) {
        try {
            double ratio;
            int destWidth;
            int destHeight;
            Options opts = new Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;
            int srcHeight = opts.outHeight;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0d;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {
                ratio = ((double) srcWidth) / ((double) w);
                destWidth = w;
                destHeight = (int) (((double) srcHeight) / ratio);
            } else {
                ratio = ((double) srcHeight) / ((double) h);
                destHeight = h;
                destWidth = (int) (((double) srcWidth) / ratio);
            }
            Options newOpts = new Options();
            newOpts.inSampleSize = ((int) ratio) + SUCCESS_STATUE;
            newOpts.inJustDecodeBounds = false;
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            return BitmapFactory.decodeFile(path, newOpts);
        } catch (Exception e) {
            return null;
        }
    }

    private void startThread() {
        this.dialog = Utils.getLoadingDialog(this);
        UpLoadHeadPicThread thread = new UpLoadHeadPicThread();
        this.dialog.show();
        thread.start();
    }

    private boolean renameFile(String picUrl) {
        String path = FileConstant.FILE_HEAD_PORTRAIT + "head portrait";
        int point = picUrl.lastIndexOf(".");
        int slash = picUrl.lastIndexOf(FilePathGenerator.ANDROID_DIR_SEP);
        if (point < 0 || slash < 0 || slash + SUCCESS_STATUE > point) {
            return false;
        }
        String name = picUrl.substring(slash + SUCCESS_STATUE, point);
        new File(path).renameTo(new File(FileConstant.FILE_HEAD_PORTRAIT + name));
        this.spInfo.setHeadPicName(name);
        this.spInfo.setHeadPicUrl(picUrl);
        return true;
    }

    public static byte[] getByte(File file) throws Exception {
        byte[] bytes = null;
        if (file != null) {
            InputStream is = new FileInputStream(file);
            int length = (int) file.length();
            if (length > Integer.MAX_VALUE) {
                System.out.println("this file is max ");
                return null;
            }
            bytes = new byte[length];
            int offset = FAILURE_STATUE;
            while (offset < bytes.length) {
                int numRead = is.read(bytes, offset, bytes.length - offset);
                if (numRead < 0) {
                    break;
                }
                offset += numRead;
            }
            if (offset < bytes.length) {
                System.out.println("file length is error");
                return null;
            }
            is.close();
        }
        return bytes;
    }

    protected Dialog onCreateDialog(int id) {
        return null;
    }
}
