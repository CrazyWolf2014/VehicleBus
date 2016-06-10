package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import java.util.List;

public class CircleMemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Integer> list;
    private int mainCC;
    private List<Integer> onLineList;
    private List<Integer> userList;

    class MemberItem {
        ImageView memSign;
        ImageView memberHead;
        TextView userName;

        MemberItem() {
        }
    }

    public CircleMemAdapter(Context context, List<Integer> userList, List<Integer> onLineList, int mainCC) {
        this.userList = userList;
        this.onLineList = onLineList;
        this.context = context;
        this.mainCC = mainCC;
        this.inflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        if (this.userList.size() > 0) {
            return this.userList.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        return this.userList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MemberItem item;
        Integer cc = (Integer) this.userList.get(position);
        if (convertView == null) {
            item = new MemberItem();
            convertView = this.inflater.inflate(C0136R.layout.circle_member_item, null);
            item.memberHead = (ImageView) convertView.findViewById(C0136R.id.member_head);
            item.memSign = (ImageView) convertView.findViewById(C0136R.id.member_sign);
            item.userName = (TextView) convertView.findViewById(C0136R.id.userName);
            convertView.setTag(item);
        } else {
            item = (MemberItem) convertView.getTag();
        }
        if (cc.intValue() == this.mainCC) {
            item.memSign.setVisibility(0);
        } else {
            item.memSign.setVisibility(8);
        }
        item.userName.setText(cc.toString());
        if (this.onLineList != null && this.onLineList.size() > 0) {
            if (this.onLineList.contains(cc)) {
                item.userName.setTextColor(-65536);
            } else {
                item.userName.setTextColor(-1);
            }
        }
        return convertView;
    }
}
