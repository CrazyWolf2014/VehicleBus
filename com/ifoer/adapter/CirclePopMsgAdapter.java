package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import java.util.List;

public class CirclePopMsgAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> stayList;
    private List<String> titleList;

    class Item {
        TextView msgStay;
        TextView msgTitle;

        Item() {
        }
    }

    public CirclePopMsgAdapter(Context context, List<String> titleList, List<String> stayList) {
        this.titleList = titleList;
        this.stayList = stayList;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        if (this.titleList.size() > 0) {
            return this.titleList.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        return this.titleList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Item item;
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.circle_pop_msg_item, null);
            item.msgTitle = (TextView) convertView.findViewById(C0136R.id.circleMsgTitle);
            item.msgStay = (TextView) convertView.findViewById(C0136R.id.circleMsgStay);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.msgTitle.setText((CharSequence) this.titleList.get(position));
        item.msgStay.setText((CharSequence) this.stayList.get(position));
        return convertView;
    }
}
