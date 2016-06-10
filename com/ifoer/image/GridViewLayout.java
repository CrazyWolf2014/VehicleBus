package com.ifoer.image;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.cnlaunch.x431frame.C0136R;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public class GridViewLayout extends RelativeLayout {
    public GridViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewLayout(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        super.onMeasure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(Opcodes.IF_ACMPEQ, 1073741824));
    }

    public int getHeightMeasureSpec() {
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) getContext().getApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(metric);
        return (int) (getContext().getResources().getDimension(C0136R.dimen.top_title_height) / metric.density);
    }
}
