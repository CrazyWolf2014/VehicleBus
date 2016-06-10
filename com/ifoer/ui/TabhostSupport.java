package com.ifoer.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;

public class TabhostSupport {
    public static View getTabItem(Context context, String text, int index) {
        RelativeLayout layout = getLayout(context, index);
        ((TextView) layout.findViewById(C0136R.id.tabItemLabel)).setText(text);
        return layout;
    }

    private static RelativeLayout getLayout(Context context, int index) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        Log.i("TabhostSupport", "screenWidth: " + screenWidth);
        Log.i("TabhostSupport", "screenHeight: " + dm.heightPixels);
        RelativeLayout layout = (RelativeLayout) ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0136R.layout.tab_item, null);
        layout.setLayoutParams(new LayoutParams(screenWidth / 4, 80, 1.0f));
        layout.setGravity(49);
        return layout;
    }

    public static void whenTabChanged(TabHost tabHost, String tabId) {
        View tabView_0 = tabHost.getTabWidget().getChildAt(0);
        View tabView_1 = tabHost.getTabWidget().getChildAt(1);
        View tabView_2 = tabHost.getTabWidget().getChildAt(2);
        View tabView_3 = tabHost.getTabWidget().getChildAt(3);
        if (tabId.equals("tab_item_1")) {
            ((TextView) tabView_0.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_01);
            ((TextView) tabView_1.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_02);
            ((TextView) tabView_2.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_02);
            ((TextView) tabView_3.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_02);
        } else if (tabId.equals("tab_item_2")) {
            ((TextView) tabView_0.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_02);
            ((TextView) tabView_1.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_01);
            ((TextView) tabView_2.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_02);
            ((TextView) tabView_3.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_02);
        } else if (tabId.equals("tab_item_3")) {
            ((TextView) tabView_0.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_02);
            ((TextView) tabView_1.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_02);
            ((TextView) tabView_2.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_01);
            ((TextView) tabView_3.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_02);
        } else if (tabId.equals("tab_item_4")) {
            ((TextView) tabView_0.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_02);
            ((TextView) tabView_1.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_02);
            ((TextView) tabView_2.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_02);
            ((TextView) tabView_3.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_c_01);
        } else {
            ((TextView) tabView_0.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.btn_car_down);
            ((TextView) tabView_1.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.car_button);
            ((TextView) tabView_2.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.car_button);
            ((TextView) tabView_3.findViewById(C0136R.id.tabItemLabel)).setBackgroundResource(C0136R.drawable.car_button);
        }
    }
}
