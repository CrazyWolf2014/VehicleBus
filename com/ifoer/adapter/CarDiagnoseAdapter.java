package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.MenuData;
import com.ifoer.expedition.cto.CToJava;
import java.util.ArrayList;
import org.achartengine.renderer.DefaultRenderer;

public class CarDiagnoseAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MenuData> list;
    private int selectedPosition;

    public class Item {
        public ImageView jiantou;
        public RelativeLayout top;
        public TextView wenzi;
    }

    public CarDiagnoseAdapter(ArrayList<MenuData> list, Context context) {
        this.selectedPosition = -1;
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    public int getCount() {
        if (this.list.size() > 0) {
            return this.list.size();
        }
        return 0;
    }

    public Object getItem(int arg0) {
        return this.list.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int arg0, View convertView, ViewGroup arg2) {
        Item item;
        if (CToJava.isChexing) {
            if (convertView == null || convertView.getTag() == null) {
                item = new Item();
                convertView = this.inflater.inflate(C0136R.layout.base_item, null);
                item.wenzi = (TextView) convertView.findViewById(C0136R.id.CarNameText);
                item.wenzi.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
                convertView.setTag(item);
            } else {
                item = (Item) convertView.getTag();
            }
            item.wenzi.setText(((MenuData) this.list.get(arg0)).getMenuContent());
        } else {
            if (convertView == null || convertView.getTag() == null) {
                item = new Item();
                convertView = this.inflater.inflate(C0136R.layout.car_diagnose_item, null);
                item.wenzi = (TextView) convertView.findViewById(C0136R.id.liebiao);
                item.wenzi.setTextColor(-1);
                item.top = (RelativeLayout) convertView.findViewById(C0136R.id.top);
                item.jiantou = (ImageView) convertView.findViewById(C0136R.id.jiantou);
                convertView.setTag(item);
            } else {
                item = (Item) convertView.getTag();
            }
            item.wenzi.setText(((MenuData) this.list.get(arg0)).getMenuContent());
        }
        return convertView;
    }
}
