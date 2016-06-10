package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.ConcernedProductDTO;
import com.ifoer.mine.Contact;
import java.util.List;

public class PushInfoAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<ConcernedProductDTO> list;

    class Item {
        TextView count;
        TextView customerName;
        LinearLayout roundBg;
        TextView serialNo;

        Item() {
        }
    }

    public PushInfoAdapter(Context context, List<ConcernedProductDTO> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
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

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Item item;
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.client_push_list_item, null);
            item.customerName = (TextView) convertView.findViewById(C0136R.id.customer_name);
            item.serialNo = (TextView) convertView.findViewById(C0136R.id.serialNo);
            item.count = (TextView) convertView.findViewById(C0136R.id.count);
            item.roundBg = (LinearLayout) convertView.findViewById(C0136R.id.roundBg);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.customerName.setText(((ConcernedProductDTO) this.list.get(position)).getCustomerName());
        item.serialNo.setText(((ConcernedProductDTO) this.list.get(position)).getSerialNo());
        if (((ConcernedProductDTO) this.list.get(position)).getUnResolvedMessageCount().equals(Contact.RELATION_ASK)) {
            item.roundBg.setVisibility(8);
        } else {
            item.roundBg.setVisibility(0);
        }
        item.count.setText(((ConcernedProductDTO) this.list.get(position)).getUnResolvedMessageCount());
        return convertView;
    }
}
