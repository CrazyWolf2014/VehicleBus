package com.cnmobi.im.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.io.File;
import java.io.IOException;
import org.xbill.DNS.KEYRecord;

@SuppressLint({"NewApi"})
public class RecordButton extends Button {
    public static final int MAX_TIME = 60;
    private static final int MIN_INTERVAL_TIME = 2000;
    private static int[] res;
    private static ImageView view;
    private OnFinishedRecordListener finishedListener;
    private String mFileName;
    private OnDismissListener onDismiss;
    private Dialog recordIndicator;
    private MediaRecorder recorder;
    private long startTime;
    private ObtainDecibelThread thread;
    private Handler volumeHandler;

    /* renamed from: com.cnmobi.im.view.RecordButton.1 */
    class C02091 implements OnDismissListener {
        C02091() {
        }

        public void onDismiss(DialogInterface dialog) {
            RecordButton.this.stopRecording();
        }
    }

    private class ObtainDecibelThread extends Thread {
        private volatile boolean running;

        private ObtainDecibelThread() {
            this.running = true;
        }

        public void exit() {
            this.running = false;
        }

        public void run() {
            while (this.running) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (RecordButton.this.recorder != null && this.running) {
                    int x = RecordButton.this.recorder.getMaxAmplitude();
                    if (x != 0) {
                        int f = (int) ((Math.log((double) x) * 10.0d) / Math.log(10.0d));
                        if (f < 26) {
                            RecordButton.this.volumeHandler.sendEmptyMessage(0);
                        } else if (f < 32) {
                            RecordButton.this.volumeHandler.sendEmptyMessage(1);
                        } else if (f < 38) {
                            RecordButton.this.volumeHandler.sendEmptyMessage(2);
                        } else {
                            RecordButton.this.volumeHandler.sendEmptyMessage(3);
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    public interface OnFinishedRecordListener {
        void onFinishedRecord(String str, int i);
    }

    static class ShowVolumeHandler extends Handler {
        ShowVolumeHandler() {
        }

        public void handleMessage(Message msg) {
            RecordButton.view.setImageResource(RecordButton.res[msg.what]);
        }
    }

    public RecordButton(Context context) {
        super(context);
        this.mFileName = null;
        this.onDismiss = new C02091();
        init();
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mFileName = null;
        this.onDismiss = new C02091();
        init();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mFileName = null;
        this.onDismiss = new C02091();
        init();
    }

    public void setSavePath(String path) {
        this.mFileName = path;
    }

    public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
        this.finishedListener = listener;
    }

    static {
        res = new int[]{C0136R.drawable.mic_2, C0136R.drawable.mic_3, C0136R.drawable.mic_4, C0136R.drawable.mic_5};
    }

    private void init() {
        this.volumeHandler = new ShowVolumeHandler();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mFileName == null) {
            return false;
        }
        switch (event.getAction()) {
            case KEYRecord.OWNER_USER /*0*/:
                setText(getResources().getString(C0136R.string.letGoofSend));
                initDialogAndStartRecord();
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                setText(getResources().getString(C0136R.string.pressedToSpeak));
                finishRecord();
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                cancelRecord();
                Toast.makeText(getContext(), getResources().getString(C0136R.string.cancelRecordAudio), 0).show();
                break;
        }
        return true;
    }

    private void initDialogAndStartRecord() {
        this.startTime = System.currentTimeMillis();
        this.recordIndicator = new Dialog(getContext(), C0136R.style.like_toast_dialog_style);
        view = new ImageView(getContext());
        view.setImageResource(C0136R.drawable.mic_2);
        this.recordIndicator.setContentView(view, new LayoutParams(-2, -2));
        this.recordIndicator.setOnDismissListener(this.onDismiss);
        this.recordIndicator.getWindow().getAttributes().gravity = 17;
        startRecording();
        this.recordIndicator.show();
    }

    private void finishRecord() {
        stopRecording();
        this.recordIndicator.dismiss();
        long intervalTime = System.currentTimeMillis() - this.startTime;
        if (intervalTime < 2000) {
            Toast.makeText(getContext(), getResources().getString(C0136R.string.timeWasShort), 0).show();
            new File(this.mFileName).delete();
        } else if (this.finishedListener != null) {
            this.finishedListener.onFinishedRecord(this.mFileName, (int) (intervalTime / 1000));
        }
    }

    private void cancelRecord() {
        stopRecording();
        this.recordIndicator.dismiss();
        Toast.makeText(getContext(), getResources().getString(C0136R.string.cancelRecordAudio), 0).show();
        new File(this.mFileName).delete();
    }

    private void startRecording() {
        this.recorder = new MediaRecorder();
        this.recorder.setAudioSource(1);
        this.recorder.setAudioChannels(1);
        this.recorder.setAudioEncodingBitRate(4000);
        this.recorder.setOutputFormat(0);
        this.recorder.setAudioEncoder(0);
        this.recorder.setOutputFile(this.mFileName);
        try {
            this.recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.recorder.start();
        this.thread = new ObtainDecibelThread();
        this.thread.start();
    }

    private void stopRecording() {
        if (this.thread != null) {
            this.thread.exit();
            this.thread = null;
        }
        if (this.recorder != null) {
            this.recorder.stop();
            this.recorder.release();
            this.recorder = null;
        }
    }
}
