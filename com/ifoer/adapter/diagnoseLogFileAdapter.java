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
import com.ifoer.entity.Diagnosisdatabase;
import java.util.List;

public class diagnoseLogFileAdapter extends BaseAdapter {
    private CheckBox check;
    private Context context;
    private LayoutInflater inflater;
    private List<Diagnosisdatabase> list;
    private int selectedPosition;
    private RelativeLayout top;

    class Item {
        TextView time;
        TextView wenzi;

        Item() {
        }
    }

    public diagnoseLogFileAdapter(List<Diagnosisdatabase> list, Context context) {
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
            convertView = this.inflater.inflate(C0136R.layout.show_file_item, null);
            item.wenzi = (TextView) convertView.findViewById(C0136R.id.liebiao);
            item.time = (TextView) convertView.findViewById(C0136R.id.time);
            this.check = (CheckBox) convertView.findViewById(C0136R.id.check);
            this.top = (RelativeLayout) convertView.findViewById(C0136R.id.top);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.wenzi.setText(((Diagnosisdatabase) this.list.get(arg0)).getTitle().toString());
        item.time.setText(((Diagnosisdatabase) this.list.get(arg0)).getTime().toString());
        this.check.setVisibility(8);
        if (this.selectedPosition == arg0) {
            this.top.setBackgroundResource(C0136R.drawable.check);
        } else {
            this.top.setBackgroundColor(0);
        }
        return convertView;
    }
}
