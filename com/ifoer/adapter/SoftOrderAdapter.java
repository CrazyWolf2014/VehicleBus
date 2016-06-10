package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.UserOrder;
import java.util.List;

public class SoftOrderAdapter extends BaseAdapter {
    OnCheckedChangeListener CheckedChange;
    private Context context;
    private LayoutInflater inflater;
    List<UserOrder> list;
    private long millionSeconds0;
    private long millionSeconds1;
    UserOrder order;
    private boolean toDel;

    /* renamed from: com.ifoer.adapter.SoftOrderAdapter.1 */
    class C03421 implements OnCheckedChangeListener {
        C03421() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                for (int i = 0; i < SoftOrderAdapter.this.list.size(); i++) {
                    ((UserOrder) SoftOrderAdapter.this.list.get(i)).setSelected(false);
                }
                SoftOrderAdapter.this.notifyDataSetChanged();
            }
            ((UserOrder) buttonView.getTag()).setSelected(isChecked);
        }
    }

    class Item {
        CheckBox checkBox;
        TextView orderID;
        TextView orderPrice;
        TextView orderPriceType;
        TextView orderState;
        TextView orderTime;
        TextView softName;

        Item() {
        }
    }

    public SoftOrderAdapter(Context context, List<UserOrder> list, boolean toDel) {
        this.CheckedChange = new C03421();
        this.context = context;
        this.list = list;
        this.toDel = toDel;
        this.inflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        if (this.list.size() > 0) {
            return this.list.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Item baseItem = new Item();
        this.order = (UserOrder) this.list.get(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(C0136R.layout.orderlist_item, null);
            baseItem.softName = (TextView) convertView.findViewById(C0136R.id.softName);
            baseItem.orderID = (TextView) convertView.findViewById(C0136R.id.orderID);
            baseItem.orderState = (TextView) convertView.findViewById(C0136R.id.orderState);
            baseItem.orderTime = (TextView) convertView.findViewById(C0136R.id.orderTime);
            baseItem.orderPrice = (TextView) convertView.findViewById(C0136R.id.orderPrice);
            baseItem.orderPriceType = (TextView) convertView.findViewById(C0136R.id.priceType);
            baseItem.checkBox = (CheckBox) convertView.findViewById(C0136R.id.checkBox);
            convertView.setTag(baseItem);
        } else {
            baseItem = (Item) convertView.getTag();
        }
        baseItem.softName.setText(this.order.getOrderName());
        baseItem.orderID.setText(this.order.getOrderSN());
        if (this.order.getStatus() == 1) {
            baseItem.orderState.setText(this.context.getResources().getText(C0136R.string.order_list_finish));
        } else {
            baseItem.orderState.setText(this.context.getResources().getText(C0136R.string.order_list_none));
        }
        String orderTime = this.order.getOrderTime();
        if (orderTime != null) {
            orderTime = orderTime.substring(0, orderTime.length() - 2);
        }
        baseItem.orderTime.setText(orderTime);
        baseItem.orderPrice.setText(new StringBuilder(String.valueOf(this.order.getTotalPrice())).toString());
        int flag = this.order.getCurrencyId();
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
        baseItem.orderPriceType.setText(priceFlag);
        baseItem.checkBox.setOnCheckedChangeListener(this.CheckedChange);
        baseItem.checkBox.setTag(this.order);
        baseItem.checkBox.setChecked(this.order.isSelected());
        if (this.toDel) {
            baseItem.checkBox.setVisibility(0);
        } else {
            this.order.setSelected(false);
            baseItem.checkBox.setVisibility(8);
        }
        return convertView;
    }
}
