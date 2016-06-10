package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import java.util.ArrayList;

public class CarCombinationAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> list;
    private int selectedPosition;

    class Item {
        TextView tvLeft;
        TextView tvRight;

        Item() {
        }
    }

    public CarCombinationAdapter(ArrayList<String> list, Context context) {
        this.selectedPosition = -1;
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    public int getCount() {
        if (this.list.size() <= 0) {
            return 0;
        }
        if (this.list.size() % 2 == 0) {
            return this.list.size() / 2;
        }
        return (this.list.size() - 1) / 2;
    }

    public Object getItem(int arg0) {
        return this.list.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int arg0, View convertView, ViewGroup arg2) {
        Item item;
        if (convertView == null || convertView.getTag() == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.car_combination_item, null);
            item.tvLeft = (TextView) convertView.findViewById(C0136R.id.combination_item_left);
            item.tvLeft.setTextColor(-1);
            item.tvRight = (TextView) convertView.findViewById(C0136R.id.combination_item_right);
            item.tvRight.setTextColor(-1);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.tvLeft.setText((CharSequence) this.list.get(arg0 * 2));
        item.tvRight.setText((CharSequence) this.list.get((arg0 * 2) + 1));
        return convertView;
    }
}
