package com.ifoer.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.expeditionphone.BaseActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.MySharedPreferences;

public class SlidePointView extends View {
    private Bitmap mBackGround;
    private DisplayMetrics mDisplayMetrics;
    private Bitmap mPointBitmap;
    private int mScreenHeight;
    private int mScreenWidth;
    private int mWorkCount;
    private int[] mWorkCountIndex;

    public SlidePointView(Context context) {
        super(context);
        this.mDisplayMetrics = null;
        this.mScreenWidth = 0;
        this.mScreenHeight = 0;
        this.mPointBitmap = null;
        this.mBackGround = null;
        this.mWorkCount = MySharedPreferences.getIntValue(BaseActivity.mContexts, "WORK_COUNT");
        this.mWorkCountIndex = new int[(this.mWorkCount + 1)];
        this.mDisplayMetrics = new DisplayMetrics();
        this.mDisplayMetrics = getResources().getDisplayMetrics();
        this.mScreenWidth = this.mDisplayMetrics.widthPixels;
        this.mScreenHeight = this.mDisplayMetrics.heightPixels;
        this.mPointBitmap = loadImageView(getResources(), C0136R.drawable.red_point, 25, 25);
        this.mBackGround = loadImageView(getResources(), C0136R.drawable.red_line, this.mScreenWidth, 2);
        int averageWidth = this.mScreenWidth / this.mWorkCount;
        for (int i = 1; i <= this.mWorkCount; i++) {
            this.mWorkCountIndex[i] = ((averageWidth * i) - (averageWidth / 2)) - (this.mPointBitmap.getWidth() / 2);
        }
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        if (this.mDisplayMetrics == null) {
            this.mDisplayMetrics = new DisplayMetrics();
        }
        this.mDisplayMetrics = getResources().getDisplayMetrics();
        this.mScreenWidth = this.mDisplayMetrics.widthPixels;
        this.mScreenHeight = this.mDisplayMetrics.heightPixels;
        this.mBackGround = loadImageView(getResources(), C0136R.drawable.red_line, this.mScreenWidth, 2);
        int averageWidth = this.mScreenWidth / this.mWorkCount;
        for (int i = 1; i <= this.mWorkCount; i++) {
            this.mWorkCountIndex[i] = ((averageWidth * i) - (averageWidth / 2)) - (this.mPointBitmap.getWidth() / 2);
        }
        invalidate();
    }

    private static Bitmap loadImageView(Resources resources, int resId, int width, int height) {
        Drawable image = resources.getDrawable(resId);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        image.setBounds(0, 0, width, height);
        image.draw(canvas);
        return bitmap;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.mBackGround, 0.0f, (float) (this.mPointBitmap.getHeight() / 2), null);
        canvas.drawBitmap(this.mPointBitmap, (float) this.mWorkCountIndex[MainActivity.mSlidePoint], 0.0f, null);
    }
}
