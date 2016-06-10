package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.dbentity.ShoppingCar;
import java.util.HashMap;
import java.util.List;

public class ShoppingCarAdapter extends BaseAdapter {
    public static HashMap<Integer, Boolean> isSelected;
    private Context context;
    private LayoutInflater inflater;
    private List<ShoppingCar> lists;

    public class Item {
        public CheckBox checkbox;
        TextView price;
        TextView serialNo;
        TextView softName;
        TextView version;
    }

    static {
        isSelected = new HashMap();
    }

    public ShoppingCarAdapter(Context context, List<ShoppingCar> lists) {
        this.context = context;
        this.lists = lists;
        this.inflater = LayoutInflater.from(context);
        for (int i = 0; i < lists.size(); i++) {
            isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
        }
    }

    public int getCount() {
        if (this.lists.size() > 0) {
            return this.lists.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        return this.lists.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Item baseItem;
        if (convertView == null) {
            baseItem = new Item();
            convertView = this.inflater.inflate(C0136R.layout.shoppingcenter_item, null);
            baseItem.softName = (TextView) convertView.findViewById(C0136R.id.softName);
            baseItem.version = (TextView) convertView.findViewById(C0136R.id.version);
            baseItem.price = (TextView) convertView.findViewById(C0136R.id.price);
            baseItem.serialNo = (TextView) convertView.findViewById(C0136R.id.serialNo);
            baseItem.checkbox = (CheckBox) convertView.findViewById(C0136R.id.checkbox);
            convertView.setTag(baseItem);
        } else {
            baseItem = (Item) convertView.getTag();
        }
        baseItem.softName.setText(((ShoppingCar) this.lists.get(position)).getSoftName());
        baseItem.version.setText(((ShoppingCar) this.lists.get(position)).getVersion());
        baseItem.price.setText(((ShoppingCar) this.lists.get(position)).getPrice());
        baseItem.serialNo.setText(((ShoppingCar) this.lists.get(position)).getSerialNo());
        baseItem.checkbox.setChecked(((Boolean) isSelected.get(Integer.valueOf(position))).booleanValue());
        return convertView;
    }
}
