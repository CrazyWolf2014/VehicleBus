package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.ui.SpaceCurrentModeActivity;
import java.util.ArrayList;

public class SpaceDataStreamAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SptExDataStreamIdItem> list;

    public class Item {
        public CheckBox checkBox;
        public TextView textName;
    }

    public SpaceDataStreamAdapter(ArrayList<SptExDataStreamIdItem> list, Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
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
        Item item;
        if (convertView == null || convertView.getTag() == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.space_data_flow_item, null);
            item.textName = (TextView) convertView.findViewById(C0136R.id.textName);
            item.checkBox = (CheckBox) convertView.findViewById(C0136R.id.checkBox);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.textName.setText(((SptExDataStreamIdItem) this.list.get(arg0)).getStreamTextIdContent());
        item.checkBox.setChecked(((Boolean) SpaceCurrentModeActivity.isSelected.get(Integer.valueOf(arg0))).booleanValue());
        return convertView;
    }
}
