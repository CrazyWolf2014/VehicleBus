package com.ifoer.expeditionphone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.ifoer.expeditionphone.ComIsRegActivity.AsyTask;
import com.ifoer.expeditionphone.inteface.IGuideActivityInterface;
import com.ifoer.image.GDApplication;
import com.ifoer.serialport.SerialPort;
import com.ifoer.serialport.SerialPortNoReadThread;
import com.ifoer.ui.MainMenuActivity;
import com.ifoer.util.MySharedPreferences;
import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public class GuideActivity extends Activity implements IGuideActivityInterface {
    private static int viewID;
    private GestureDetector mGestureDetector;
    private ProgressBar mProgressBar;
    private ViewFlipper mViewFlipper;
    private SerialPort serialPort;
    private SerialPortNoReadThread serialPortReadThread;

    /* renamed from: com.ifoer.expeditionphone.GuideActivity.1 */
    class C05771 implements OnGestureListener {
        C05771() {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() > e2.getX()) {
                MySharedPreferences.getIntValue(GuideActivity.this, "GUIDE_COUNT");
                int guideCount = 4;
                if (MySharedPreferences.getIntValue(GuideActivity.this, "GUIDE_COUNT") > 0) {
                    guideCount = MySharedPreferences.getIntValue(GuideActivity.this, "GUIDE_COUNT");
                }
                if (GuideActivity.viewID >= guideCount) {
                    GuideActivity.this.mProgressBar.setVisibility(0);
                    GuideActivity.this.jump();
                } else {
                    GuideActivity.this.mViewFlipper.showNext();
                    GuideActivity.viewID = GuideActivity.viewID + 1;
                }
            } else if (e1.getX() >= e2.getX()) {
                return false;
            } else {
                if (GuideActivity.viewID > 0) {
                    GuideActivity.this.mViewFlipper.setInAnimation(GuideActivity.this.getApplicationContext(), C0136R.anim.right_in);
                    GuideActivity.this.mViewFlipper.setOutAnimation(GuideActivity.this.getApplicationContext(), C0136R.anim.right_out);
                    GuideActivity.this.mViewFlipper.showPrevious();
                    GuideActivity.viewID = GuideActivity.viewID - 1;
                    GuideActivity.this.mViewFlipper.setInAnimation(GuideActivity.this.getApplicationContext(), C0136R.anim.left_in);
                    GuideActivity.this.mViewFlipper.setOutAnimation(GuideActivity.this.getApplicationContext(), C0136R.anim.left_out);
                }
            }
            return true;
        }

        public boolean onDown(MotionEvent e) {
            return false;
        }
    }

    /* renamed from: com.ifoer.expeditionphone.GuideActivity.2 */
    class C05782 implements OnTouchListener {
        C05782() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return GuideActivity.this.mGestureDetector.onTouchEvent(event);
        }
    }

    public GuideActivity() {
        this.serialPort = null;
    }

    static {
        viewID = 0;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MySharedPreferences.getBooleanValue(this, "FirstLoginBoolean", true)) {
            setContentView(C0136R.layout.guide_activity);
            MySharedPreferences.setBoolean(this, "FirstLoginBoolean", false);
            boolean firstLogin = MySharedPreferences.getBooleanValue(this, "FirstLoginBoolean", true);
            viewID = 0;
            initPageImage();
            return;
        }
        creatView();
    }

    public void initPageImage() {
        this.mProgressBar = (ProgressBar) findViewById(C0136R.id.progressBar);
        this.mViewFlipper = (ViewFlipper) findViewById(C0136R.id.guide_view);
        this.mViewFlipper.setVisibility(0);
        toReg();
        this.mGestureDetector = new GestureDetector(new C05771());
        this.mViewFlipper.setLongClickable(true);
        this.mViewFlipper.setOnTouchListener(new C05782());
    }

    public void creatView() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainMenuActivity.IfShowDialog, 2);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
    }

    public void jump() {
        String isSerialPortIDReg = MySharedPreferences.getStringValue(this, "isSerialPortIDReg");
        String serialPortID = MySharedPreferences.getStringValue(this, Constants.SERIALID);
        String serialPortNO = MySharedPreferences.getStringValue(this, Constants.SERIALNO);
        Log.e("zdy", "**isSerialPortIDReg****" + isSerialPortIDReg);
        Intent intent = new Intent(this, ComRegActivity.class);
        intent.putExtra(Constants.serialNo, serialPortNO.toString());
        intent.putExtra("portPsw", serialPortID.toString());
        startActivity(intent);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService("input_method");
    }

    public void doSerialPortNoReadThread() {
        if (MySharedPreferences.getStringValue(this, Constants.SERIALNO) == null) {
            GDApplication application = (GDApplication) getApplication();
            if (this.serialPort == null) {
                Log.i("zdy", "\u6ca1\u6709\u4e32\u53e3");
                return;
            }
            Log.i("zdy", "\u6709\u4e32\u53e3");
            this.serialPortReadThread = new SerialPortNoReadThread(this.serialPort, this);
            this.serialPortReadThread.start();
        }
    }

    public void doOutputStream(SerialPortNoReadThread serialPortReadThread, Context context) {
        try {
            serialPortReadThread.getmOutputStream().write(toByte("55 aa f0 f8  00 03 0f 21  03 26"));
            try {
                serialPortReadThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public byte[] toByte(String str) {
        String str1 = str.replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE);
        Log.i("zdy", str1);
        byte[] d = new byte[(str1.length() / 2)];
        int i = 0;
        while (i < str1.length()) {
            int c;
            int tmp = str1.substring(i, i + 1).getBytes()[0];
            if (tmp > 96) {
                c = ((tmp - 97) + 10) * 16;
            } else if (tmp > 64) {
                c = ((tmp - 65) + 10) * 16;
            } else {
                c = (tmp - 48) * 16;
            }
            i++;
            tmp = str1.substring(i, i + 1).getBytes()[0];
            if (tmp > 96) {
                c += (tmp - 97) + 10;
            } else if (tmp > 64) {
                c += (tmp - 65) + 10;
            } else {
                c += tmp - 48;
            }
            d[i / 2] = (byte) c;
            i++;
        }
        return d;
    }

    public void toReg() {
        String isSerialPortIDReg = MySharedPreferences.getStringValue(this, "isSerialPortIDReg");
        String serialPortID = MySharedPreferences.getStringValue(this, Constants.SERIALID);
        String serialPortNO = MySharedPreferences.getStringValue(this, Constants.SERIALNO);
        Log.e("zdy", "ComIsRegActivity****" + serialPortNO);
        if (serialPortID != null && serialPortNO != null) {
            Log.e("zdy", "ComIsRegActivity****" + serialPortID);
            ComIsRegActivity comIsRegActivity = null;
            try {
                comIsRegActivity = (ComIsRegActivity) ComIsRegActivity.class.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
            if (comIsRegActivity != null) {
                comIsRegActivity.setEditText(serialPortNO);
                comIsRegActivity.setPswText(serialPortID);
                comIsRegActivity.getClass();
                new AsyTask().execute(new Void[0]);
            }
        }
    }
}
