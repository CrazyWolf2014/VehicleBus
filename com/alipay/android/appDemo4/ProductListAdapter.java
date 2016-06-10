package com.alipay.android.appDemo4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.alipay.android.appDemo4.Products.ProductDetail;
import com.cnlaunch.x431frame.C0136R;
import java.util.ArrayList;

public class ProductListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProductDetail> m_productList;

    private class ProductItemView {
        TextView body;
        TextView price;
        TextView subject;

        private ProductItemView() {
        }
    }

    public ProductListAdapter(Context c, ArrayList<ProductDetail> list) {
        this.m_productList = null;
        this.m_productList = list;
        this.context = c;
    }

    public int getCount() {
        return this.m_productList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ProductItemView itemView;
        if (convertView == null) {
            itemView = new ProductItemView();
            convertView = LayoutInflater.from(this.context).inflate(C0136R.layout.product_item, null);
            itemView.subject = (TextView) convertView.findViewById(C0136R.id.subject);
            itemView.body = (TextView) convertView.findViewById(C0136R.id.body);
            itemView.price = (TextView) convertView.findViewById(C0136R.id.price);
            convertView.setTag(itemView);
        } else {
            itemView = (ProductItemView) convertView.getTag();
        }
        itemView.subject.setText(((ProductDetail) this.m_productList.get(position)).subject);
        itemView.body.setText(((ProductDetail) this.m_productList.get(position)).body);
        itemView.price.setText(((ProductDetail) this.m_productList.get(position)).price);
        return convertView;
    }
}
