package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.ConcernedProductDTO;
import java.util.List;

public class CustomerInfoAdapter extends BaseAdapter {
    private Context context;
    private List<ConcernedProductDTO> dtoList;

    class Item {
        TextView customerName;
        TextView serialNo;

        Item() {
        }
    }

    public CustomerInfoAdapter(Context context, List<ConcernedProductDTO> dtoList) {
        this.context = context;
        this.dtoList = dtoList;
    }

    public int getCount() {
        if (this.dtoList.size() > 0) {
            return this.dtoList.size();
        }
        return 0;
    }

    public Object getItem(int arg0) {
        return this.dtoList.get(arg0);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Item item;
        if (convertView == null) {
            item = new Item();
            convertView = LayoutInflater.from(this.context).inflate(C0136R.layout.client_info, null);
            item.customerName = (TextView) convertView.findViewById(C0136R.id.customer_name);
            item.serialNo = (TextView) convertView.findViewById(C0136R.id.serialNo);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.customerName.setText(((ConcernedProductDTO) this.dtoList.get(position)).getCustomerName());
        item.serialNo.setText(((ConcernedProductDTO) this.dtoList.get(position)).getSerialNo());
        return convertView;
    }
}
