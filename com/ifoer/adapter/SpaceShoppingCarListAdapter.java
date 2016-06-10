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
import java.util.Map;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public class SpaceShoppingCarListAdapter extends BaseAdapter {
    public static Map<Integer, Boolean> isSelected;
    private Context context;
    private LayoutInflater inflater;
    private List<ShoppingCar> shoppingCar;

    public class Item {
        TextView carName;
        public CheckBox checkBox;
        TextView date;
        TextView price;
        TextView serialNo;
        TextView version;
    }

    static {
        isSelected = new HashMap();
    }

    public SpaceShoppingCarListAdapter(Context context, List<ShoppingCar> shoppingCar) {
        this.shoppingCar = shoppingCar;
        for (int i = 0; i < shoppingCar.size(); i++) {
            isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
        }
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        if (this.shoppingCar != null) {
            return this.shoppingCar.size();
        }
        return 0;
    }

    public Object getItem(int arg0) {
        return this.shoppingCar.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int arg0, View convertView, ViewGroup arg2) {
        Item item;
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.space_shopping_car_item, null);
            item.serialNo = (TextView) convertView.findViewById(C0136R.id.serialNo);
            item.date = (TextView) convertView.findViewById(C0136R.id.date);
            item.carName = (TextView) convertView.findViewById(C0136R.id.carName);
            item.version = (TextView) convertView.findViewById(C0136R.id.version);
            item.price = (TextView) convertView.findViewById(C0136R.id.price);
            item.checkBox = (CheckBox) convertView.findViewById(C0136R.id.checkbox);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.serialNo.setText(((ShoppingCar) this.shoppingCar.get(arg0)).getSerialNo());
        item.date.setText(((ShoppingCar) this.shoppingCar.get(arg0)).getDate());
        item.carName.setText(((ShoppingCar) this.shoppingCar.get(arg0)).getSoftName());
        item.version.setText(((ShoppingCar) this.shoppingCar.get(arg0)).getVersion());
        int currencyId = Integer.parseInt(((ShoppingCar) this.shoppingCar.get(arg0)).getCurrencyId());
        String priceFlag = XmlPullParser.NO_NAMESPACE;
        if (currencyId == 4) {
            priceFlag = (String) this.context.getResources().getText(C0136R.string.RMB);
        } else if (currencyId == 11) {
            priceFlag = (String) this.context.getResources().getText(C0136R.string.USD);
        } else if (currencyId == 13) {
            priceFlag = (String) this.context.getResources().getText(C0136R.string.HKD);
        } else if (currencyId == 14) {
            priceFlag = (String) this.context.getResources().getText(C0136R.string.EUR);
        }
        item.price.setText(new StringBuilder(String.valueOf(((ShoppingCar) this.shoppingCar.get(arg0)).getTotalPrice())).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(priceFlag).toString());
        item.checkBox.setChecked(((Boolean) isSelected.get(Integer.valueOf(arg0))).booleanValue());
        return convertView;
    }
}
