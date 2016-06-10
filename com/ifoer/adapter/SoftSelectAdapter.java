package com.ifoer.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431frame.C0136R.drawable;
import com.cnlaunch.x431frame.C0136R.string;
import com.ifoer.entity.PackageDetailDto;
import com.ifoer.image.AsyncImageView;
import java.util.HashMap;
import java.util.List;

public class SoftSelectAdapter extends BaseAdapter {
    public static HashMap<Integer, Boolean> isSelected;
    Item baseItem;
    private Context context;
    private LayoutInflater inflater;
    private List<PackageDetailDto> list;
    private Typeface mTypeface;
    PackageDetailDto soft;

    public class Item {
        TextView carNameText;
        public CheckBox checkBox;
        TextView chexi;
        TextView priceType;
        AsyncImageView softImage;
        TextView softPrice;
        TextView softver;
    }

    static {
        isSelected = new HashMap();
    }

    public SoftSelectAdapter(Context context, List<PackageDetailDto> list, int flag) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(this.context);
        for (int i = 0; i < list.size(); i++) {
            if (flag == 0) {
                isSelected.put(Integer.valueOf(i), Boolean.valueOf(true));
            } else {
                isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
            }
        }
        this.mTypeface = Typeface.createFromAsset(context.getAssets(), "impact.ttf");
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
        this.baseItem = new Item();
        this.soft = (PackageDetailDto) this.list.get(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(C0136R.layout.soft_select_item, null);
            this.baseItem.chexi = (TextView) convertView.findViewById(C0136R.id.carXi);
            this.baseItem.softver = (TextView) convertView.findViewById(C0136R.id.carVer);
            this.baseItem.softPrice = (TextView) convertView.findViewById(C0136R.id.softPrice);
            this.baseItem.priceType = (TextView) convertView.findViewById(C0136R.id.priceType);
            this.baseItem.softImage = (AsyncImageView) convertView.findViewById(C0136R.id.carFlag);
            this.baseItem.carNameText = (TextView) convertView.findViewById(C0136R.id.CarNameText);
            this.baseItem.checkBox = (CheckBox) convertView.findViewById(C0136R.id.checkBox);
            this.baseItem.carNameText.setTypeface(this.mTypeface);
            convertView.setTag(this.baseItem);
        } else {
            this.baseItem = (Item) convertView.getTag();
        }
        try {
            this.baseItem.softImage.setImageResource(drawable.class.getDeclaredField(this.soft.getIcon()).getInt(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String tempName = (String) this.context.getResources().getText(string.class.getDeclaredField(this.soft.getSoftPackageID()).getInt(null));
            this.baseItem.carNameText.setText(tempName.toUpperCase().equals("EOBD2") ? "EOBD" : tempName.toUpperCase());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.baseItem.chexi.setText(this.soft.getSoftName().replaceAll("EOBD2", "EOBD"));
        this.baseItem.softver.setText(this.soft.getVersion());
        this.baseItem.softPrice.setText(new StringBuilder(String.valueOf(this.soft.getPrice())).toString());
        int flag = this.soft.getCurrencyId();
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
        this.baseItem.priceType.setText(priceFlag);
        this.baseItem.checkBox.setChecked(((Boolean) isSelected.get(Integer.valueOf(position))).booleanValue());
        return convertView;
    }
}
