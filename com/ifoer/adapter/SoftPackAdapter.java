package com.ifoer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.SoftPackageDto;
import java.util.List;

public class SoftPackAdapter extends BaseAdapter {
    private Bitmap bm;
    private Context context;
    private LayoutInflater inflater;
    List<SoftPackageDto> list;

    class Item {
        TextView packAmount;
        TextView packID;
        TextView packName;
        TextView packPrice;
        TextView packType;
        RelativeLayout softPackage;

        Item() {
        }
    }

    public SoftPackAdapter(Context context, List<SoftPackageDto> list) {
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

    public Object getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Item baseItem;
        SoftPackageDto sofeInfo = (SoftPackageDto) this.list.get(position);
        if (convertView == null) {
            baseItem = new Item();
            convertView = this.inflater.inflate(C0136R.layout.soft_package_item, null);
            baseItem.packName = (TextView) convertView.findViewById(C0136R.id.packName);
            baseItem.packID = (TextView) convertView.findViewById(C0136R.id.packID);
            baseItem.packType = (TextView) convertView.findViewById(C0136R.id.packType);
            baseItem.packPrice = (TextView) convertView.findViewById(C0136R.id.packPrice);
            baseItem.packAmount = (TextView) convertView.findViewById(C0136R.id.packAmount);
            baseItem.softPackage = (RelativeLayout) convertView.findViewById(C0136R.id.softPackage);
            convertView.setTag(baseItem);
        } else {
            baseItem = (Item) convertView.getTag();
        }
        baseItem.packName.setText(sofeInfo.getSoftPackageName());
        baseItem.packID.setText(new StringBuilder(String.valueOf(sofeInfo.getSoftPackageId())).toString());
        if (sofeInfo.getPackageFlag() == 1) {
            baseItem.packType.setText(this.context.getResources().getText(C0136R.string.Optional_packages));
        } else if (sofeInfo.getPackageFlag() == 0) {
            baseItem.packType.setText(this.context.getResources().getText(C0136R.string.fix_packs));
        }
        int flag = sofeInfo.getCurrencyId();
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
        baseItem.packPrice.setText(sofeInfo.getTotalPrice() + priceFlag);
        baseItem.packAmount.setText(new StringBuilder(String.valueOf(sofeInfo.getAmount())).toString());
        return convertView;
    }
}
