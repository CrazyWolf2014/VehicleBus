package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import java.util.ArrayList;
import java.util.List;

public class MenuGridviewAdpater extends BaseAdapter {
    private Integer[] TextArray;
    private Context context;
    private Integer[] drawableArray;
    private List<ImageView> imageList;
    private int isSelected;
    private AnimationSet manimationSet;

    public MenuGridviewAdpater(Context context) {
        this.imageList = new ArrayList();
        this.drawableArray = new Integer[]{Integer.valueOf(C0136R.drawable.grid_01_selector), Integer.valueOf(C0136R.drawable.grid_02_selector), Integer.valueOf(C0136R.drawable.grid_03_selector), Integer.valueOf(C0136R.drawable.grid_04_selector), Integer.valueOf(C0136R.drawable.grid_05_selector), Integer.valueOf(C0136R.drawable.grid_06_selecor), Integer.valueOf(C0136R.drawable.grid_07_selector), Integer.valueOf(C0136R.drawable.grid_09_selector)};
        this.TextArray = new Integer[]{Integer.valueOf(C0136R.string.left_btn_zhenduan), Integer.valueOf(C0136R.string.fast_diagnosis), Integer.valueOf(C0136R.string.left_btn_db), Integer.valueOf(C0136R.string.left_btn_kongjian), Integer.valueOf(C0136R.string.golo), Integer.valueOf(C0136R.string.a_key_to_upgrade), Integer.valueOf(C0136R.string.left_btn_guanli), Integer.valueOf(C0136R.string.left_btn_gduo)};
        this.isSelected = 10;
        this.context = context;
        list();
    }

    public int getCount() {
        return this.imageList.size();
    }

    public Object getItem(int arg0) {
        return this.imageList.get(arg0);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = (LinearLayout) ((LayoutInflater) this.context.getApplicationContext().getSystemService("layout_inflater")).inflate(C0136R.layout.menu_grid_item, null);
        ((ImageView) layout.findViewById(C0136R.id.image)).setImageResource(this.drawableArray[position].intValue());
        ((TextView) layout.findViewById(C0136R.id.text)).setText(this.TextArray[position].intValue());
        return layout;
    }

    private void list() {
        for (int i = 0; i < this.drawableArray.length; i++) {
            this.imageList.add(new ImageView(this.context));
            ((ImageView) this.imageList.get(i)).setImageResource(this.drawableArray[i].intValue());
        }
    }

    public void setSelect(int position) {
        this.isSelected = position;
    }

    public static String getActivityName(String names) {
        String[] name = names.split("\\.");
        return name[name.length - 1];
    }
}
