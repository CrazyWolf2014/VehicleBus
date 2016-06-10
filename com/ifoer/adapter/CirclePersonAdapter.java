package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.CircleInfoDTO;
import java.util.List;

public class CirclePersonAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<CircleInfoDTO> list;
    private int selectedPosition;

    class Item {
        RelativeLayout circle_layout;
        TextView host;
        ImageView portrait;
        TextView subTitle;

        Item() {
        }
    }

    public CirclePersonAdapter(Context context, List<CircleInfoDTO> list) {
        this.list = null;
        this.selectedPosition = -1;
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

    public Object getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Item item;
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.circle_item, null);
            item.portrait = (ImageView) convertView.findViewById(C0136R.id.circle_head);
            item.subTitle = (TextView) convertView.findViewById(C0136R.id.circle_name);
            item.circle_layout = (RelativeLayout) convertView.findViewById(C0136R.id.circle_circle);
            item.host = (TextView) convertView.findViewById(C0136R.id.circle_host);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        if (this.selectedPosition == position) {
            item.circle_layout.setSelected(true);
            item.circle_layout.setPressed(true);
            item.circle_layout.setBackgroundColor(this.context.getResources().getColor(C0136R.color.lightblack));
        } else {
            item.circle_layout.setSelected(false);
            item.circle_layout.setPressed(false);
            item.circle_layout.setBackgroundColor(0);
        }
        if (((CircleInfoDTO) this.list.get(position)).isJoined()) {
            item.subTitle.setTextColor(this.context.getResources().getColor(C0136R.color.red));
        } else {
            item.subTitle.setTextColor(this.context.getResources().getColor(C0136R.color.white));
        }
        item.subTitle.setText(((CircleInfoDTO) this.list.get(position)).getCircleName());
        item.host.setText(((CircleInfoDTO) this.list.get(position)).getCircleAdmin());
        return convertView;
    }
}
