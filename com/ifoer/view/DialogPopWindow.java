package com.ifoer.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.expeditionphone.ComRegActivity;

public class DialogPopWindow extends PopupWindow {
    public static boolean isConfirm;
    private View mMenuView;
    private AnimationSet manimationSet;

    /* renamed from: com.ifoer.view.DialogPopWindow.1 */
    class C07651 implements OnClickListener {
        private final /* synthetic */ ComRegActivity val$activitys;

        C07651(ComRegActivity comRegActivity) {
            this.val$activitys = comRegActivity;
        }

        public void onClick(View v) {
            DialogPopWindow.this.dismiss();
            this.val$activitys.Register2();
        }
    }

    /* renamed from: com.ifoer.view.DialogPopWindow.2 */
    class C07662 implements OnClickListener {
        C07662() {
        }

        public void onClick(View v) {
            DialogPopWindow.this.dismiss();
        }
    }

    static {
        isConfirm = true;
    }

    public DialogPopWindow(Activity context, ComRegActivity activity) {
        super(context);
        Context contexts = context;
        ComRegActivity activitys = activity;
        this.mMenuView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0136R.layout.dialog_pop, null);
        ((Button) this.mMenuView.findViewById(C0136R.id.btn_confirm)).setOnClickListener(new C07651(activitys));
        ((Button) this.mMenuView.findViewById(C0136R.id.btn_cancel)).setOnClickListener(new C07662());
        setContentView(this.mMenuView);
        setWidth(-1);
        setHeight(-1);
        setFocusable(true);
        setAnimationStyle(C0136R.style.AnimBottom);
        setBackgroundDrawable(new ColorDrawable(-1342177280));
    }

    private void animation(View view) {
        ScaleAnimation scaleAnimation;
        AnimationSet animationSet = new AnimationSet(true);
        if (!(this.manimationSet == null || this.manimationSet == animationSet)) {
            scaleAnimation = new ScaleAnimation(2.0f, 0.5f, 2.0f, 0.5f, 1, 0.5f, 1, 0.5f);
            scaleAnimation.setDuration(1000);
            this.manimationSet.addAnimation(scaleAnimation);
            this.manimationSet.setFillAfter(true);
            view.startAnimation(this.manimationSet);
        }
        scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setDuration(1000);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setFillAfter(true);
        view.startAnimation(animationSet);
        this.manimationSet = animationSet;
    }
}
