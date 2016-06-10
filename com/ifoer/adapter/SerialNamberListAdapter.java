package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class SerialNamberListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> list;

    class Item {
        TextView serialNumber;

        Item() {
        }
    }

    public SerialNamberListAdapter(List<String> list, Context context) {
        this.list = new ArrayList();
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        if (this.list != null) {
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
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.serial_number_item, null);
            item.serialNumber = (TextView) convertView.findViewById(C0136R.id.serial_number_name);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        if (!(XmlPullParser.NO_NAMESPACE.equals(this.list.get(arg0)) || this.list.get(arg0) == null)) {
            item.serialNumber.setText(((String) this.list.get(arg0)).toString());
        }
        return convertView;
    }
}
