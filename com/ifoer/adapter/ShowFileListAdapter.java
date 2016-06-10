package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import java.util.List;

public class ShowFileListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> list;
    private int selectedPosition;
    private RelativeLayout top;

    class Item {
        TextView time;
        TextView wenzi;

        Item() {
        }
    }

    public ShowFileListAdapter(List<String> list, Context context) {
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
            convertView = this.inflater.inflate(C0136R.layout.show_file_list_item, null);
            item.wenzi = (TextView) convertView.findViewById(C0136R.id.file_name);
            this.top = (RelativeLayout) convertView.findViewById(C0136R.id.top);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.wenzi.setText(((String) this.list.get(arg0)).toString());
        if (this.selectedPosition == arg0) {
            this.top.setBackgroundColor(-65536);
        } else {
            this.top.setBackgroundColor(0);
        }
        return convertView;
    }
}
