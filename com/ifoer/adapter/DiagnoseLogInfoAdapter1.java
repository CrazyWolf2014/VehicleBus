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
import com.ifoer.entity.UserOrder;
import java.util.List;

public class DiagnoseLogInfoAdapter1 extends BaseAdapter {
    private CheckBox check;
    private Context context;
    private LayoutInflater inflater;
    private List<UserOrder> list;
    private int selectedPosition;
    private RelativeLayout top;

    class Item {
        TextView address;
        TextView chexing;
        TextView price;
        TextView time;

        Item() {
        }
    }

    public DiagnoseLogInfoAdapter1(Context context, List<UserOrder> list) {
        this.list = null;
        this.selectedPosition = -1;
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    public int getCount() {
        if (this.list != null) {
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
            convertView = this.inflater.inflate(C0136R.layout.diagnosis_database_info_item, null);
            item.time = (TextView) convertView.findViewById(C0136R.id.time);
            item.address = (TextView) convertView.findViewById(C0136R.id.address);
            item.chexing = (TextView) convertView.findViewById(C0136R.id.chexing);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        int flag = ((UserOrder) this.list.get(arg0)).getCurrencyId();
        String priceFlag = "RMB";
        if (flag == 4) {
            priceFlag = (String) this.context.getResources().getText(C0136R.string.RMB);
        } else if (flag == 11) {
            priceFlag = (String) this.context.getResources().getText(C0136R.string.USD);
        } else if (flag == 13) {
            priceFlag = (String) this.context.getResources().getText(C0136R.string.HKD);
        } else if (flag == 14) {
            priceFlag = (String) this.context.getResources().getText(C0136R.string.EUR);
        }
        item.time.setText(((UserOrder) this.list.get(arg0)).getOrderTime());
        item.address.setText(((UserOrder) this.list.get(arg0)).getOrderName());
        item.chexing.setText(new StringBuilder(String.valueOf(String.valueOf(((UserOrder) this.list.get(arg0)).getTotalPrice()))).append(priceFlag).toString());
        return convertView;
    }
}
