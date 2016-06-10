package com.ifoer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import java.util.ArrayList;

public class SpecialFunctionAdapter extends BaseAdapter {
    private boolean isTitle;
    Item item;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mList;

    public class Item {
        public TextView mTextContent;
    }

    public SpecialFunctionAdapter(ArrayList<String> list, Context context, int colums, boolean isTitle) {
        this.isTitle = false;
        this.item = null;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
        this.isTitle = isTitle;
    }

    public int getCount() {
        if (this.mList == null) {
            return 0;
        }
        return this.mList.size();
    }

    public Object getItem(int arg0) {
        return this.mList.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public void refresh(ArrayList<String> list) {
        this.mList = null;
        this.mList = list;
        notifyDataSetChanged();
    }

    @SuppressLint({"ResourceAsColor"})
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        if (convertView == null || convertView.getTag() == null) {
            this.item = new Item();
            if (this.isTitle) {
                convertView = this.mInflater.inflate(C0136R.layout.special_title_item, null);
            } else {
                convertView = this.mInflater.inflate(C0136R.layout.special_function_item, null);
            }
            this.item.mTextContent = (TextView) convertView.findViewById(C0136R.id.textContent);
            convertView.setTag(this.item);
        } else {
            this.item = (Item) convertView.getTag();
        }
        this.item.mTextContent.setText(((String) this.mList.get(arg0)).toString());
        if (this.isTitle) {
            this.item.mTextContent.setHeight(30);
            this.item.mTextContent.setMinHeight(20);
        }
        return convertView;
    }
}
