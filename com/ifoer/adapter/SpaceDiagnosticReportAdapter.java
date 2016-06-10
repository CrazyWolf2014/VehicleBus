package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Report;
import java.util.List;

public class SpaceDiagnosticReportAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Report> lists;

    public class Item {
        TextView images_name;
        TextView serialNo;
        TextView time;
    }

    public SpaceDiagnosticReportAdapter(List<Report> lists, Context context) {
        this.context = context;
        this.lists = lists;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        if (this.lists.size() > 0) {
            return this.lists.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        return this.lists.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Item baseItem;
        if (convertView == null) {
            baseItem = new Item();
            convertView = this.inflater.inflate(C0136R.layout.space_diagnostic_report_item, null);
            baseItem.images_name = (TextView) convertView.findViewById(C0136R.id.item_name_textView);
            baseItem.time = (TextView) convertView.findViewById(C0136R.id.item_time_textView);
            baseItem.serialNo = (TextView) convertView.findViewById(C0136R.id.item_number_textView);
            convertView.setTag(baseItem);
        } else {
            baseItem = (Item) convertView.getTag();
        }
        baseItem.images_name.setText(((Report) this.lists.get(position)).getReportName());
        baseItem.time.setText(((Report) this.lists.get(position)).getCreationTime());
        baseItem.serialNo.setText(((Report) this.lists.get(position)).getSerialNo());
        return convertView;
    }
}
