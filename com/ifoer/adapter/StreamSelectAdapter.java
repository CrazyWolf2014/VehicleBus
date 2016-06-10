package com.ifoer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.SptStreamSelectIdItem;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint({"UseSparseArrays"})
public class StreamSelectAdapter extends BaseAdapter {
    private static HashMap<Integer, Boolean> isSelected;
    private LayoutInflater inflater;
    Item item;
    private ArrayList<SptStreamSelectIdItem> mSptStreamSelectIdItemList;
    private boolean orientationPortrait;

    public class Item {
        public CheckBox checkBox;
        public TextView textName;
    }

    public StreamSelectAdapter(ArrayList<SptStreamSelectIdItem> sptStreamSelectIdItemList, Context context) {
        this.orientationPortrait = false;
        this.item = null;
        this.inflater = LayoutInflater.from(context);
        this.mSptStreamSelectIdItemList = sptStreamSelectIdItemList;
        isSelected = new HashMap();
        initDate();
    }

    public StreamSelectAdapter(ArrayList<SptStreamSelectIdItem> sptStreamSelectIdItemList, Context context, boolean orientationPortrait, boolean isInitMapData) {
        this.orientationPortrait = false;
        this.item = null;
        this.inflater = LayoutInflater.from(context);
        this.mSptStreamSelectIdItemList = sptStreamSelectIdItemList;
        this.orientationPortrait = orientationPortrait;
        if (isInitMapData) {
            isSelected = new HashMap();
            initDate();
        }
    }

    private void initDate() {
        for (int i = 0; i < this.mSptStreamSelectIdItemList.size(); i++) {
            isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
        }
    }

    public int getCount() {
        if (this.mSptStreamSelectIdItemList == null) {
            return 0;
        }
        return this.mSptStreamSelectIdItemList.size();
    }

    public Object getItem(int arg0) {
        return this.mSptStreamSelectIdItemList.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int arg0, View convertView, ViewGroup arg2) {
        if (convertView == null || convertView.getTag() == null) {
            this.item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.stream_select_item, null);
            this.item.textName = (TextView) convertView.findViewById(C0136R.id.textName);
            this.item.checkBox = (CheckBox) convertView.findViewById(C0136R.id.checkBox);
            convertView.setTag(this.item);
        } else {
            this.item = (Item) convertView.getTag();
        }
        this.item.textName.setText(((SptStreamSelectIdItem) this.mSptStreamSelectIdItemList.get(arg0)).getStreamSelectStr());
        this.item.checkBox.setChecked(((Boolean) getIsSelected().get(Integer.valueOf(arg0))).booleanValue());
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }
}
