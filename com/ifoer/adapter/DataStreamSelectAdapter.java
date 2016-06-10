package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.DataStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DataStreamSelectAdapter extends BaseAdapter {
    public static HashMap<Integer, Boolean> isSelected;
    private Context context;
    private LayoutInflater inflater;
    Item item;
    private ArrayList<DataStream> list;

    public class Item {
        public CheckBox checkBox;
        public TextView textName;
    }

    public DataStreamSelectAdapter(ArrayList<DataStream> list, Context context) {
        this.item = null;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        init();
    }

    private void init() {
        isSelected = new HashMap();
        for (int i = 0; i < this.list.size(); i++) {
            isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
        }
    }

    public int getCount() {
        if (this.list == null) {
            return 0;
        }
        return this.list.size();
    }

    public Object getItem(int arg0) {
        return this.list.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int arg0, View convertView, ViewGroup arg2) {
        View layout = (LinearLayout) this.inflater.inflate(C0136R.layout.data_stream_seleted_item2, null);
        if (convertView == null || convertView.getTag() == null) {
            this.item = new Item();
            convertView = layout;
            this.item.textName = (TextView) convertView.findViewById(C0136R.id.textName);
            this.item.checkBox = (CheckBox) convertView.findViewById(C0136R.id.checkBox);
            convertView.setTag(this.item);
        } else {
            this.item = (Item) convertView.getTag();
        }
        this.item.textName.setText(((DataStream) this.list.get(arg0)).getName());
        this.item.checkBox.setChecked(((Boolean) isSelected.get(Integer.valueOf(arg0))).booleanValue());
        return convertView;
    }
}
