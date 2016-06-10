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
import com.ifoer.entity.CntSynUpdateInfo;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class NewsUpgradeAnnouncemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ImageView jiantou;
    private List<CntSynUpdateInfo> list;
    private RelativeLayout top;

    class Item {
        TextView author;
        TextView keyword;
        TextView time;
        TextView title;

        Item() {
        }
    }

    public NewsUpgradeAnnouncemAdapter(List<CntSynUpdateInfo> list, Context context) {
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

    public View getView(int arg0, View convertView, ViewGroup arg2) {
        Item item;
        CntSynUpdateInfo newsInfo = (CntSynUpdateInfo) this.list.get(arg0);
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.news_upgrade, null);
            item.title = (TextView) convertView.findViewById(C0136R.id.title);
            item.time = (TextView) convertView.findViewById(C0136R.id.time);
            item.author = (TextView) convertView.findViewById(C0136R.id.author);
            this.top = (RelativeLayout) convertView.findViewById(C0136R.id.top);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.title.setText(newsInfo.getName());
        item.time.setText(newsInfo.getLastUpdateDate().split(NDEFRecord.TEXT_WELL_KNOWN_TYPE)[0]);
        if ("anyType{}".equalsIgnoreCase(newsInfo.getFstrUser())) {
            item.author.setText(XmlPullParser.NO_NAMESPACE);
        } else {
            item.author.setText(XmlPullParser.NO_NAMESPACE);
        }
        return convertView;
    }
}
