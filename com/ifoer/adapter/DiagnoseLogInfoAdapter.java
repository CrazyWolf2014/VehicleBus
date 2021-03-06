package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.TimelyInfo;
import java.util.List;

public class DiagnoseLogInfoAdapter extends BaseAdapter {
    private CheckBox check;
    private Context context;
    private LayoutInflater inflater;
    private List<TimelyInfo> list;
    private int selectedPosition;
    private RelativeLayout top;

    class Item {
        TextView address;
        TextView chexing;
        TextView time;

        Item() {
        }
    }

    public DiagnoseLogInfoAdapter(List<TimelyInfo> list, Context context) {
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
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.diagnosis_database_info_item, null);
            item.time = (TextView) convertView.findViewById(C0136R.id.time);
            item.address = (TextView) convertView.findViewById(C0136R.id.address);
            item.chexing = (TextView) convertView.findViewById(C0136R.id.chexing);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.time.setText(((TimelyInfo) this.list.get(arg0)).getTime().toString());
        item.address.setText(((TimelyInfo) this.list.get(arg0)).getAddress().toString());
        item.chexing.setText(((TimelyInfo) this.list.get(arg0)).getChexing().toString());
        return convertView;
    }
}
