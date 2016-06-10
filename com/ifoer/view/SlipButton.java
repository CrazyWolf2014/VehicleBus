package com.ifoer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.listener.OnChangedListener;
import org.xbill.DNS.KEYRecord;

public class SlipButton extends View implements OnTouchListener {
    private Rect Btn_Off;
    private Rect Btn_On;
    private OnChangedListener ChgLsn;
    public float DownX;
    public boolean NowChoose;
    public float NowX;
    private boolean OnSlip;
    private Bitmap bg_off;
    private Bitmap bg_on;
    private boolean enabled;
    public boolean flag;
    private boolean isChgLsnOn;
    private Bitmap slip_btn;
    private String strName;

    public SlipButton(Context context) {
        super(context);
        this.enabled = true;
        this.flag = false;
        this.NowChoose = false;
        this.OnSlip = false;
        this.DownX = 0.0f;
        this.NowX = 0.0f;
        this.isChgLsnOn = false;
        init();
    }

    public SlipButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
        this.flag = false;
        this.NowChoose = false;
        this.OnSlip = false;
        this.DownX = 0.0f;
        this.NowX = 0.0f;
        this.isChgLsnOn = false;
        init();
    }

    public void setChecked(boolean fl) {
        if (fl) {
            this.flag = true;
            this.NowChoose = true;
            this.NowX = 80.0f;
            return;
        }
        this.flag = false;
        this.NowChoose = false;
        this.NowX = 0.0f;
    }

    public void setEnabled(boolean b) {
        if (b) {
            this.enabled = true;
        } else {
            this.enabled = false;
        }
    }

    private void init() {
        this.bg_on = BitmapFactory.decodeResource(getResources(), C0136R.drawable.on_btn);
        this.bg_off = BitmapFactory.decodeResource(getResources(), C0136R.drawable.off_btn);
        this.slip_btn = BitmapFactory.decodeResource(getResources(), C0136R.drawable.white_btn);
        this.Btn_On = new Rect(0, 0, this.slip_btn.getWidth(), this.slip_btn.getHeight());
        this.Btn_Off = new Rect(this.bg_off.getWidth() - this.slip_btn.getWidth(), 0, this.bg_off.getWidth(), this.slip_btn.getHeight());
        setOnTouchListener(this);
    }

    protected void onDraw(Canvas canvas) {
        float x;
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        Paint paint = new Paint();
        if (this.flag) {
            this.NowX = 80.0f;
            this.flag = false;
        }
        if (this.NowX < ((float) (this.bg_on.getWidth() / 2))) {
            canvas.drawBitmap(this.bg_off, matrix, paint);
        } else {
            canvas.drawBitmap(this.bg_on, matrix, paint);
        }
        if (this.OnSlip) {
            if (this.NowX >= ((float) this.bg_on.getWidth())) {
                x = (float) (this.bg_on.getWidth() - (this.slip_btn.getWidth() / 2));
            } else {
                x = this.NowX - ((float) (this.slip_btn.getWidth() / 2));
            }
        } else if (this.NowChoose) {
            x = (float) this.Btn_Off.left;
        } else {
            x = (float) this.Btn_On.left;
        }
        if (x < 0.0f) {
            x = 0.0f;
        } else if (x > ((float) (this.bg_on.getWidth() - this.slip_btn.getWidth()))) {
            x = (float) (this.bg_on.getWidth() - this.slip_btn.getWidth());
        }
        canvas.drawBitmap(this.slip_btn, x, 0.0f, paint);
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (!this.enabled) {
            return false;
        }
        switch (event.getAction()) {
            case KEYRecord.OWNER_USER /*0*/:
                if (event.getX() <= ((float) this.bg_on.getWidth()) && event.getY() <= ((float) this.bg_on.getHeight())) {
                    this.OnSlip = true;
                    this.DownX = event.getX();
                    this.NowX = this.DownX;
                    break;
                }
                return false;
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.OnSlip = false;
                boolean LastChoose = this.NowChoose;
                if (event.getX() >= ((float) (this.bg_on.getWidth() / 2))) {
                    this.NowChoose = true;
                } else {
                    this.NowChoose = false;
                }
                if (this.isChgLsnOn && LastChoose != this.NowChoose) {
                    this.ChgLsn.OnChanged(this.strName, this.NowChoose);
                    break;
                }
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                this.NowX = event.getX();
                break;
        }
        invalidate();
        return true;
    }

    public void SetOnChangedListener(String name, OnChangedListener l) {
        this.strName = name;
        this.isChgLsnOn = true;
        this.ChgLsn = l;
    }
}
