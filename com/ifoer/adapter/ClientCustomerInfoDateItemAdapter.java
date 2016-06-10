package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.List;
import java.util.Map;

public class ClientCustomerInfoDateItemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Map<String, String>> list;
    private RelativeLayout top;

    class Item {
        TextView info;
        TextView name;

        Item() {
        }
    }

    public ClientCustomerInfoDateItemAdapter(List<Map<String, String>> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.client_info_detail_item, null);
            item.name = (TextView) convertView.findViewById(C0136R.id.name);
            item.info = (TextView) convertView.findViewById(C0136R.id.info);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.name.setText((CharSequence) ((Map) this.list.get(arg0)).get(SharedPref.VALUE));
        item.info.setText((CharSequence) ((Map) this.list.get(arg0)).get(SharedPref.KEY));
        return convertView;
    }
}
