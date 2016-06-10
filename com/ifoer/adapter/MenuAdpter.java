package com.ifoer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Menu;
import java.util.List;

public class MenuAdpter extends BaseAdapter {
    private int clicktemp;
    private Context context;
    private LayoutInflater inflater;
    private List<Menu> list;

    public class MenuItem {
        public ImageView button;
        public int name;
        public TextView textView;
    }

    public MenuAdpter(Context context, List<Menu> list) {
        this.clicktemp = -1;
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    @SuppressLint({"NewApi"})
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuItem menuItem;
        if (convertView == null) {
            menuItem = new MenuItem();
            convertView = this.inflater.inflate(C0136R.layout.menu_item, null);
            menuItem.button = (ImageView) convertView.findViewById(C0136R.id.menu_button);
            menuItem.textView = (TextView) convertView.findViewById(C0136R.id.menu_text);
            convertView.setTag(menuItem);
        } else {
            menuItem = (MenuItem) convertView.getTag();
        }
        menuItem.button.setBackground(this.context.getResources().getDrawable(((Menu) this.list.get(position)).getIcon()));
        menuItem.textView.setText(this.context.getString(((Menu) this.list.get(position)).getName()));
        menuItem.name = ((Menu) this.list.get(position)).getName();
        return convertView;
    }
}
