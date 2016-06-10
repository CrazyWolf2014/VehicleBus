package com.ifoer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.expedition.BluetoothChat.DataStreamUtils;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.util.MySharedPreferences;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

@SuppressLint({"UseSparseArrays"})
public class DataStreamAdapter extends BaseAdapter {
    private static HashMap<Integer, Boolean> isSelected;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SptExDataStreamIdItem> list;
    boolean setPage;

    public class Item {
        public TextView content;
        public CheckBox flagIcon;
        public TextView label;
        public TextView state;
    }

    public DataStreamAdapter(ArrayList<SptExDataStreamIdItem> list, Context context) {
        this.setPage = true;
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        isSelected = new HashMap();
        initDate();
    }

    private void initDate() {
        for (int i = 0; i < this.list.size(); i++) {
            getIsSelected().put(Integer.valueOf(i), Boolean.valueOf(false));
        }
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
            convertView = this.inflater.inflate(C0136R.layout.data_stream_item, null);
            item.flagIcon = (CheckBox) convertView.findViewById(C0136R.id.data_stream_select_icon);
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
        item.flagIcon.setChecked(getIsSelected().get(Integer.valueOf(arg0)) == null ? false : ((Boolean) getIsSelected().get(Integer.valueOf(arg0))).booleanValue());
        if (XmlPullParser.NO_NAMESPACE.equals(((SptExDataStreamIdItem) this.list.get(arg0)).getStreamState()) || !DataStreamUtils.isNumeric(((SptExDataStreamIdItem) this.list.get(arg0)).getStreamStr())) {
            item.flagIcon.setVisibility(4);
        } else {
            item.flagIcon.setVisibility(0);
        }
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }
}
