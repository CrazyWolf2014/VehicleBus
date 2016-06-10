package com.ifoer.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.MenuGridviewAdpater;
import com.ifoer.expeditionphone.KeyToUpgradeActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.ui.MoreActivity;
import com.ifoer.ui.SpaceDiagnosticReportActivity;
import com.ifoer.ui.UserActivity;

public class MenuSelectPicPopupWindow extends PopupWindow implements OnClickListener, OnKeyListener {
    private MenuGridviewAdpater adpter;
    private Button btn_account;
    private Button btn_db;
    private Button btn_diag;
    private Button btn_fast;
    private Button btn_golo;
    private Button btn_more;
    private Button btn_mydata;
    private Button btn_update;
    private GridView gridView;
    private LayoutInflater inflater;
    private LinearLayout layout;
    private View mMenuView;
    private MainActivity mainActitity;
    private AnimationSet manimationSet;
    private Context mcontext;

    public MenuSelectPicPopupWindow(Activity context) {
        super(context);
        Context contexts = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mMenuView = this.inflater.inflate(C0136R.layout.memu, null);
        this.mcontext = context;
        this.layout = (LinearLayout) this.mMenuView.findViewById(C0136R.id.pop_layout);
        this.btn_diag = (Button) this.mMenuView.findViewById(C0136R.id.btn_diag);
        this.btn_diag.setFocusable(true);
        this.btn_diag.setFocusableInTouchMode(true);
        this.btn_diag.setOnClickListener(this);
        this.btn_fast = (Button) this.mMenuView.findViewById(C0136R.id.btn_quick);
        this.btn_fast.setOnClickListener(this);
        this.btn_db = (Button) this.mMenuView.findViewById(C0136R.id.btn_data);
        this.btn_db.setOnClickListener(this);
        this.btn_mydata = (Button) this.mMenuView.findViewById(C0136R.id.btn_mydata);
        this.btn_mydata.setOnClickListener(this);
        this.btn_golo = (Button) this.mMenuView.findViewById(C0136R.id.btn_golo);
        this.btn_golo.setOnClickListener(this);
        this.btn_update = (Button) this.mMenuView.findViewById(C0136R.id.btn_update);
        this.btn_update.setOnClickListener(this);
        this.btn_account = (Button) this.mMenuView.findViewById(C0136R.id.btn_count);
        this.btn_account.setOnClickListener(this);
        this.btn_more = (Button) this.mMenuView.findViewById(C0136R.id.btn_more);
        this.btn_more.setOnClickListener(this);
        setContentView(this.mMenuView);
        setWidth(-1);
        setHeight(-1);
        setFocusable(true);
        setAnimationStyle(C0136R.style.AnimBottom);
        setBackgroundDrawable(new ColorDrawable(-1342177280));
        setOnKeyListener();
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

    private void openApp(Context context) throws NameNotFoundException {
        PackageInfo pi = context.getPackageManager().getPackageInfo("com.cnlaunch.golo3", 0);
        Intent resolveIntent = new Intent("android.intent.action.MAIN", null);
        resolveIntent.addCategory("android.intent.category.LAUNCHER");
        resolveIntent.setPackage(pi.packageName);
        ResolveInfo ri = (ResolveInfo) context.getPackageManager().queryIntentActivities(resolveIntent, 0).iterator().next();
        if (ri != null) {
            String packageName1 = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName(packageName1, className));
            context.startActivity(intent);
        }
    }

    public void setOnKeyListener() {
        setBackgroundDrawable(null);
        this.btn_diag.setOnKeyListener(this);
        this.btn_fast.setOnKeyListener(this);
        this.btn_db.setOnKeyListener(this);
        this.btn_mydata.setOnKeyListener(this);
        this.btn_golo.setOnKeyListener(this);
        this.btn_update.setOnKeyListener(this);
        this.btn_account.setOnKeyListener(this);
        this.btn_more.setOnKeyListener(this);
    }

    public void setMainActitity(MainActivity mainActitity) {
        this.mainActitity = mainActitity;
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.btn_diag) {
            dismiss();
        } else if (v.getId() == C0136R.id.btn_quick) {
            Toast.makeText(this.mcontext, "coming soon", 0).show();
        } else if (v.getId() == C0136R.id.btn_data) {
            Toast.makeText(this.mcontext, "coming soon", 0).show();
        } else if (v.getId() == C0136R.id.btn_mydata) {
            intent = new Intent();
            intent.setClass(this.mcontext, SpaceDiagnosticReportActivity.class);
            this.mcontext.startActivity(intent);
        } else if (v.getId() == C0136R.id.btn_golo) {
            try {
                openApp(this.mcontext);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this.mcontext, "coming soon", 0).show();
            }
        } else if (v.getId() == C0136R.id.btn_update) {
            intent = new Intent();
            intent.setClass(this.mcontext, KeyToUpgradeActivity.class);
            this.mcontext.startActivity(intent);
        } else if (v.getId() == C0136R.id.btn_count) {
            intent = new Intent();
            intent.setClass(this.mcontext, UserActivity.class);
            this.mcontext.startActivity(intent);
        } else if (v.getId() == C0136R.id.btn_more) {
            intent = new Intent();
            intent.setClass(this.mcontext, MoreActivity.class);
            this.mcontext.startActivity(intent);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onKey(android.view.View r4, int r5, android.view.KeyEvent r6) {
        /*
        r3 = this;
        r2 = 0;
        r0 = r6.getAction();
        if (r0 != 0) goto L_0x000a;
    L_0x0007:
        switch(r5) {
            case 4: goto L_0x000b;
            case 111: goto L_0x0018;
            default: goto L_0x000a;
        };
    L_0x000a:
        return r2;
    L_0x000b:
        r0 = "KEYCODE_BACK";
        r1 = "menu is  show";
        android.util.Log.i(r0, r1);
        r0 = r3.mainActitity;
        r0 = r0.isShowExist;
        if (r0 == 0) goto L_0x000a;
    L_0x0018:
        r0 = r3.mainActitity;
        r0 = r0.isShowExist;
        if (r0 != 0) goto L_0x000a;
    L_0x001e:
        r0 = "KEYCODE_111";
        r1 = "menu is not show";
        android.util.Log.i(r0, r1);
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.view.MenuSelectPicPopupWindow.onKey(android.view.View, int, android.view.KeyEvent):boolean");
    }
}
