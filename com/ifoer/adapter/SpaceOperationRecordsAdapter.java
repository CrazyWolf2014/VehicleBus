package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.OperatingRecordInfo;
import java.util.HashMap;
import java.util.List;

public class SpaceOperationRecordsAdapter extends BaseAdapter {
    public static HashMap<Integer, Boolean> isSelected;
    private Context context;
    private LayoutInflater inflater;
    private List<OperatingRecordInfo> lists;

    public class Item {
        TextView address;
        TextView chexing;
        TextView serialNo;
        TextView time;
    }

    static {
        isSelected = new HashMap();
    }

    public SpaceOperationRecordsAdapter(List<OperatingRecordInfo> lists, Context context) {
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
            convertView = this.inflater.inflate(C0136R.layout.space_operation_records_item, null);
            baseItem.serialNo = (TextView) convertView.findViewById(C0136R.id.serialNo);
            baseItem.time = (TextView) convertView.findViewById(C0136R.id.time);
            baseItem.address = (TextView) convertView.findViewById(C0136R.id.address);
            baseItem.chexing = (TextView) convertView.findViewById(C0136R.id.chexing);
            convertView.setTag(baseItem);
        } else {
            baseItem = (Item) convertView.getTag();
        }
        baseItem.serialNo.setText(((OperatingRecordInfo) this.lists.get(position)).getSerialNumber());
        baseItem.time.setText(((OperatingRecordInfo) this.lists.get(position)).getTestTime());
        baseItem.address.setText(((OperatingRecordInfo) this.lists.get(position)).getTestSite());
        baseItem.chexing.setText(((OperatingRecordInfo) this.lists.get(position)).getTestCar().replaceAll("EOBD2", "EOBD"));
        return convertView;
    }
}
