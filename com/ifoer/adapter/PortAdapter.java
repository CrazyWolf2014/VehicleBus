package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import java.util.List;

public class PortAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> list;

    class SerItem {
        TextView textView;

        SerItem() {
        }
    }

    public PortAdapter(Context context, List<String> list) {
        this.list = null;
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        if (this.list.size() > 0) {
            return this.list.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SerItem item;
        if (convertView == null) {
            item = new SerItem();
            convertView = this.inflater.inflate(C0136R.layout.ser_item, null);
            item.textView = (TextView) convertView.findViewById(C0136R.id.serText);
            convertView.setTag(item);
        } else {
            item = (SerItem) convertView.getTag();
        }
        item.textView.setText((CharSequence) this.list.get(position));
        return convertView;
    }
}
