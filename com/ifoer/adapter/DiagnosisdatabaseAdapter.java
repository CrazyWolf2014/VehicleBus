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
import com.ifoer.entity.Diagnosisdatabase;
import java.util.List;

public class DiagnosisdatabaseAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ImageView jiantou;
    private List<Diagnosisdatabase> list;
    private RelativeLayout top;

    class Item {
        TextView info;
        TextView time;
        TextView title;

        Item() {
        }
    }

    public DiagnosisdatabaseAdapter(List<Diagnosisdatabase> list, Context context) {
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
            convertView = this.inflater.inflate(C0136R.layout.diagnosis_database_item, null);
            item.title = (TextView) convertView.findViewById(C0136R.id.title);
            item.time = (TextView) convertView.findViewById(C0136R.id.time);
            item.info = (TextView) convertView.findViewById(C0136R.id.info);
            this.top = (RelativeLayout) convertView.findViewById(C0136R.id.top);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.title.setText(((Diagnosisdatabase) this.list.get(arg0)).getTitle());
        item.time.setText(((Diagnosisdatabase) this.list.get(arg0)).getTime());
        item.info.setText(((Diagnosisdatabase) this.list.get(arg0)).getInfo());
        return convertView;
    }
}
