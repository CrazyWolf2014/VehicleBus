package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.CntSynNews;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class DiagnosisdatabaseNewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<CntSynNews> list;

    class Item {
        TextView author;
        TextView time;
        TextView title;

        Item() {
        }
    }

    public DiagnosisdatabaseNewAdapter(List<CntSynNews> list, Context context) {
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
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.diagnosis_database_item, null);
            item.title = (TextView) convertView.findViewById(C0136R.id.title);
            item.time = (TextView) convertView.findViewById(C0136R.id.time);
            item.author = (TextView) convertView.findViewById(C0136R.id.author);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.title.setText(((CntSynNews) this.list.get(arg0)).getSubject());
        item.time.setText(((CntSynNews) this.list.get(arg0)).getPublishDate().split(NDEFRecord.TEXT_WELL_KNOWN_TYPE)[0]);
        item.author.setText(XmlPullParser.NO_NAMESPACE);
        return convertView;
    }
}
