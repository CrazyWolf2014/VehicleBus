package com.ifoer.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.cnlaunch.x431frame.C0136R;

public class SelectPicPopupWindow extends PopupWindow implements OnClickListener {
    private CheckBox checkBtn;
    private ImageView closeSg;
    private Button reputBtn;
    private View reputView;

    public SelectPicPopupWindow(Activity context) {
        super(context);
        this.reputView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0136R.layout.reputation_pop, null);
        this.closeSg = (ImageView) this.reputView.findViewById(C0136R.id.repu_close);
        this.closeSg.setOnClickListener(this);
        this.checkBtn = (CheckBox) this.reputView.findViewById(C0136R.id.repu_select);
        this.reputBtn = (Button) this.reputView.findViewById(C0136R.id.repu_btn);
        this.reputBtn.setOnClickListener(this);
        setContentView(this.reputView);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(false);
        setAnimationStyle(C0136R.style.AnimBottom);
        setBackgroundDrawable(new ColorDrawable(-1342177280));
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.repu_btn) {
            dismiss();
        } else if (v.getId() == C0136R.id.repu_close) {
            dismiss();
        }
    }
}
