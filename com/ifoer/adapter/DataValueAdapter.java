package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.util.MySharedPreferences;
import java.util.ArrayList;

public class DataValueAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SptExDataStreamIdItem> list;
    boolean setPage;

    class Item {
        TextView content;
        TextView label;
        TextView state;

        Item() {
        }
    }

    public DataValueAdapter(ArrayList<SptExDataStreamIdItem> list, Context context) {
        this.setPage = true;
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

    public void refresh(ArrayList<SptExDataStreamIdItem> streamList) {
        this.list = null;
        this.list = streamList;
        notifyDataSetChanged();
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
            convertView = this.inflater.inflate(C0136R.layout.data_stream_value_item, null);
            item.label = (TextView) convertView.findViewById(C0136R.id.oneText);
            item.content = (TextView) convertView.findViewById(C0136R.id.twoText);
            item.state = (TextView) convertView.findViewById(C0136R.id.threeText);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        if (Constant.spt_datastream_id_ex_page >= arg0 || !this.setPage || MySharedPreferences.getSharedPref(this.context).getInt("spt_datastream_id_ex_page", 0) > 0) {
            this.setPage = false;
            if (Constant.spt_datastream_id_ex_page > MySharedPreferences.getSharedPref(this.context).getInt("spt_datastream_id_ex_page", 0)) {
                MySharedPreferences.setInt(this.context, "spt_datastream_id_ex_page", Constant.spt_datastream_id_ex_page);
            }
        } else {
            Constant.spt_datastream_id_ex_page = arg0 - 1;
        }
        item.label.setText(((SptExDataStreamIdItem) this.list.get(arg0)).getStreamTextIdContent());
        item.content.setText(ByteHexHelper.replaceBlank(((SptExDataStreamIdItem) this.list.get(arg0)).getStreamStr()));
        item.state.setText(ByteHexHelper.replaceBlank(((SptExDataStreamIdItem) this.list.get(arg0)).getStreamState()));
        return convertView;
    }
}
