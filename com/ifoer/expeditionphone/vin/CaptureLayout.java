package com.ifoer.expeditionphone.vin;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.googlecode.leptonica.android.Skew;
import java.util.ArrayList;
import org.achartengine.renderer.DefaultRenderer;
import org.xmlpull.v1.XmlPullParser;

public final class CaptureLayout extends View {
    private static final String TAG = "MLOG: CaptureLayout.java: ";
    private final int MAX_CHARS_PER_LINE;
    private final int MAX_LINES_TO_DISPLAY;
    private Rect m_Box;
    private final Paint m_Paint;
    private boolean m_bDisplayFocusImage;
    private boolean m_bDrawFocused;
    private boolean m_bFocused;
    private boolean m_bLineMode;
    private boolean m_bOrientationFocusImage;
    private final int m_nFrameColor;
    private final int m_nMaskColor;
    private Context m_oContext;
    private final Paint m_pFocus;
    private String m_sUpperText;
    private String m_sWaitingText;

    public CaptureLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.MAX_CHARS_PER_LINE = 50;
        this.MAX_LINES_TO_DISPLAY = 10;
        this.m_pFocus = new Paint();
        this.m_Paint = new Paint();
        this.m_bDisplayFocusImage = false;
        this.m_bOrientationFocusImage = false;
        this.m_bFocused = false;
        this.m_bDrawFocused = false;
        this.m_bLineMode = true;
        this.m_sWaitingText = XmlPullParser.NO_NAMESPACE;
        this.m_sUpperText = XmlPullParser.NO_NAMESPACE;
        this.m_oContext = context;
        this.m_Box = new Rect();
        Resources resources = getResources();
        this.m_nMaskColor = resources.getColor(C0136R.color.capturelayout_mask);
        this.m_nFrameColor = resources.getColor(C0136R.color.capturelayout_frame);
    }

    public void setRect(ArrayList<Rect> rect) {
        this.m_Box = (Rect) rect.get(0);
    }

    public void DrawFocusIcon(boolean bEnable, boolean orientation) {
        this.m_bDisplayFocusImage = bEnable;
        this.m_bOrientationFocusImage = orientation;
        invalidate();
    }

    public void DrawFocused(boolean bEnable, boolean focused) {
        this.m_bDrawFocused = bEnable;
        this.m_bFocused = focused;
        invalidate();
    }

    public void SetLineMode(boolean mode) {
        this.m_bLineMode = mode;
        invalidate();
    }

    public void SetText(String text) {
        this.m_sUpperText = text;
        invalidate();
    }

    public void ShowWaiting(String wait_text) {
        this.m_sWaitingText = wait_text;
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        try {
            Rect frame = CameraManager.get().GetFramingRect(this.m_bLineMode);
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            frame.top -= 50;
            frame.bottom -= 50;
            this.m_Paint.setColor(this.m_nMaskColor);
            this.m_Box.set(0, 0, width, frame.top);
            canvas.drawRect(this.m_Box, this.m_Paint);
            this.m_Box.set(0, frame.top, frame.left, frame.bottom + 1);
            canvas.drawRect(this.m_Box, this.m_Paint);
            this.m_Box.set(frame.right + 1, frame.top, width, frame.bottom + 1);
            canvas.drawRect(this.m_Box, this.m_Paint);
            this.m_Box.set(0, frame.bottom + 1, width, height);
            canvas.drawRect(this.m_Box, this.m_Paint);
            this.m_Paint.setColor(this.m_nFrameColor);
            this.m_Box.set(frame.left, frame.top, frame.right + 1, frame.top + 2);
            canvas.drawRect(this.m_Box, this.m_Paint);
            this.m_Box.set(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1);
            canvas.drawRect(this.m_Box, this.m_Paint);
            this.m_Box.set(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1);
            canvas.drawRect(this.m_Box, this.m_Paint);
            this.m_Box.set(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1);
            canvas.drawRect(this.m_Box, this.m_Paint);
            if (!this.m_bLineMode && this.m_bDisplayFocusImage) {
                if (this.m_bOrientationFocusImage) {
                    canvas.drawBitmap(BitmapFactory.decodeResource(this.m_oContext.getResources(), C0136R.drawable.landscape), 20.0f, 20.0f, null);
                } else {
                    canvas.drawBitmap(BitmapFactory.decodeResource(this.m_oContext.getResources(), C0136R.drawable.portrait), 20.0f, (float) (height - 80), null);
                }
            }
            if (this.m_bDrawFocused) {
                if (this.m_bFocused) {
                    this.m_pFocus.setColor(-16711936);
                } else {
                    this.m_pFocus.setColor(-65536);
                }
                Paint pblack = new Paint();
                pblack.setColor(-256);
                canvas.drawCircle((float) (width - 25), (float) (height - 55), 11.0f, pblack);
                canvas.drawCircle((float) (width - 25), (float) (height - 55), 10.0f, this.m_pFocus);
            }
            if (this.m_bLineMode) {
                int i = 0;
                int start = this.m_sUpperText.length() - 500;
                String str = this.m_sUpperText;
                if (start < 0) {
                    start = 0;
                }
                this.m_sUpperText = str.substring(start, this.m_sUpperText.length());
                Paint p = new Paint();
                p.setColor(DefaultRenderer.TEXT_COLOR);
                p.setTextSize(p.getTextSize() + Skew.SWEEP_DELTA);
                byte[] buff = new byte[this.m_sUpperText.length()];
                buff = this.m_sUpperText.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET);
                while (i < this.m_sUpperText.length() / 50) {
                    canvas.drawText(new String(buff, i * 50, 50), 20.0f, (float) ((i * 20) + 20), p);
                    i++;
                }
                if (this.m_sUpperText.length() % 50 != 0) {
                    canvas.drawText(new String(buff, i * 50, this.m_sUpperText.length() % 50), 20.0f, (float) ((i * 20) + 20), p);
                }
                if (this.m_sWaitingText.length() > 0) {
                    p = new Paint();
                    p.setColor(DefaultRenderer.TEXT_COLOR);
                    p.setStyle(Style.FILL_AND_STROKE);
                    canvas.drawText(this.m_sWaitingText, (float) frame.left, (float) (frame.bottom + 20), p);
                }
            }
        } catch (Exception ex) {
            Log.v(TAG, ex.toString());
        }
    }
}
