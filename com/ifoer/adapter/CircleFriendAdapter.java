package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.CircleFriend;
import java.util.List;

public class CircleFriendAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<CircleFriend> list;

    class Item {
        TextView groupName;
        ImageView icon;
        TextView name;
        ImageView onLine;

        Item() {
        }
    }

    public CircleFriendAdapter(List<CircleFriend> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        if (this.list.size() > 0) {
            return this.list.size();
        }
        return 0;
    }

    public Object getItem(int arg0) {
        return this.list.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int prostion, View convertView, ViewGroup arg2) {
        Item item;
        CircleFriend friend = (CircleFriend) this.list.get(prostion);
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.circle_friend_item, null);
            item.name = (TextView) convertView.findViewById(C0136R.id.friendName);
            item.groupName = (TextView) convertView.findViewById(C0136R.id.groupName);
            item.onLine = (ImageView) convertView.findViewById(C0136R.id.lineFlag);
            item.icon = (ImageView) convertView.findViewById(C0136R.id.circle_head);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.name.setText(friend.getName());
        item.groupName.setText(friend.getGroupName());
        item.icon.setBackgroundDrawable(friend.getIcon());
        return convertView;
    }
}
