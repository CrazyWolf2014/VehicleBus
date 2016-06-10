package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.SptExDataStreamIdItem;
import java.util.ArrayList;

public class SpaceCurrentModeAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SptExDataStreamIdItem> streamLists;

    public class Item {
        public TextView guili;
        public TextView jietu;
        public TextView wenzi;
    }

    public SpaceCurrentModeAdapter(Context context, ArrayList<SptExDataStreamIdItem> streamLists) {
        this.context = context;
        this.streamLists = streamLists;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.streamLists.size() > 0 ? this.streamLists.size() : 0;
    }

    public Object getItem(int arg0) {
        return this.streamLists.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int arg0, View convertView, ViewGroup arg2) {
        Item item;
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.data_stream_item, null);
            item.wenzi = (TextView) convertView.findViewById(C0136R.id.oneText);
            item.jietu = (TextView) convertView.findViewById(C0136R.id.twoText);
            item.guili = (TextView) convertView.findViewById(C0136R.id.threeText);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.wenzi.setText(((SptExDataStreamIdItem) this.streamLists.get(arg0)).getStreamTextIdContent());
        item.jietu.setText(((SptExDataStreamIdItem) this.streamLists.get(arg0)).getStreamStr());
        item.guili.setText(((SptExDataStreamIdItem) this.streamLists.get(arg0)).getStreamState());
        return convertView;
    }
}
