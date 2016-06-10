package com.ifoer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import java.util.ArrayList;

public class FaultCodeAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SptTroubleTest> list;
    private int width;

    class Item {
        TextView guili;
        TextView jietu;
        TextView wenzi;

        Item() {
        }
    }

    public FaultCodeAdapter(ArrayList<SptTroubleTest> list, Context context) {
        this.list = list;
        this.context = context;
        this.width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        if (this.list.size() > 0) {
            return this.list.size();
        }
        return 0;
    }

    public void refresh(ArrayList<SptTroubleTest> list) {
        this.list = null;
        this.list = list;
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
            convertView = this.inflater.inflate(C0136R.layout.fault_code_item, null);
            item.wenzi = (TextView) convertView.findViewById(C0136R.id.oneText);
            item.jietu = (TextView) convertView.findViewById(C0136R.id.twoText);
            item.guili = (TextView) convertView.findViewById(C0136R.id.threeText);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.wenzi.setText(ByteHexHelper.replaceBlank(((SptTroubleTest) this.list.get(arg0)).getTroubleCodeContent()));
        item.jietu.setText(ByteHexHelper.replaceBlank(((SptTroubleTest) this.list.get(arg0)).getTroubleDescribeContent()));
        item.guili.setText(ByteHexHelper.replaceBlank(((SptTroubleTest) this.list.get(arg0)).getTroubleStateContent()));
        return convertView;
    }
}
