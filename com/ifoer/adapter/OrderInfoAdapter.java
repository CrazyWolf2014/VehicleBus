package com.ifoer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431frame.C0136R.drawable;
import com.cnlaunch.x431frame.C0136R.string;
import com.ifoer.entity.OrderDetail;
import com.ifoer.image.AsyncImageView;
import java.util.List;

public class OrderInfoAdapter extends BaseAdapter {
    private Bitmap bm;
    private Context context;
    private LayoutInflater inflater;
    List<OrderDetail> list;
    private Typeface mTypeface;

    class Item {
        TextView carNameText;
        AsyncImageView softLogo;
        TextView softName;
        TextView softPrice;
        TextView softVer;

        Item() {
        }
    }

    public OrderInfoAdapter(Context context, List<OrderDetail> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
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
        Item baseItem = new Item();
        OrderDetail orderInfo = (OrderDetail) this.list.get(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(C0136R.layout.order_info_item, null);
            baseItem.softName = (TextView) convertView.findViewById(C0136R.id.Chexi);
            baseItem.softVer = (TextView) convertView.findViewById(C0136R.id.orderVer);
            baseItem.softPrice = (TextView) convertView.findViewById(C0136R.id.orderPrice);
            baseItem.carNameText = (TextView) convertView.findViewById(C0136R.id.CarNameText);
            baseItem.softLogo = (AsyncImageView) convertView.findViewById(C0136R.id.softLogo);
            baseItem.carNameText.setTypeface(this.mTypeface);
            convertView.setTag(baseItem);
        } else {
            baseItem = (Item) convertView.getTag();
        }
        baseItem.softName.setText(orderInfo.getSoftName());
        baseItem.softVer.setText(orderInfo.getVersion());
        baseItem.softPrice.setText(new StringBuilder(String.valueOf(orderInfo.getPrice())).toString());
        try {
            String tempName = (String) this.context.getResources().getText(string.class.getDeclaredField(orderInfo.getSoftPackageID()).getInt(null));
            baseItem.carNameText.setText(tempName.toUpperCase().equals("EOBD2") ? "EOBD" : tempName.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            baseItem.softLogo.setImageResource(drawable.class.getDeclaredField(orderInfo.getIcon()).getInt(null));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return convertView;
    }
}
