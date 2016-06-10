package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import java.util.ArrayList;
import java.util.List;
import org.achartengine.renderer.DefaultRenderer;

public class PopAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public List<String> mList;

    public class ViewHolder2 {
        public TextView mText;
    }

    public PopAdapter(Context context, List<String> historyVerStr) {
        this.mList = new ArrayList();
        this.inflater = LayoutInflater.from(context);
        this.mList = historyVerStr;
    }

    public void refresh(List<String> historyVerStr) {
        this.mList = historyVerStr;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder2 holder;
        if (convertView == null) {
            view = this.inflater.inflate(C0136R.layout.item, null);
            holder = new ViewHolder2();
            holder.mText = (TextView) view.findViewById(C0136R.id.tv);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder2) view.getTag();
        }
        holder.mText.setText((CharSequence) this.mList.get(position));
        holder.mText.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        return view;
    }

    public static float px2pxByHVGA(Context context, float pxValue) {
        return ((pxValue + 0.5f) * context.getResources().getDisplayMetrics().density) + 0.5f;
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
