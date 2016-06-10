package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.SecurityQuestionDTO;
import java.util.List;

public class QuestionAdapter extends BaseAdapter {
    private Context context;
    private SecurityQuestionDTO dto;
    private LayoutInflater inflater;
    private List<SecurityQuestionDTO> list;

    class QuItem {
        TextView qutext;

        QuItem() {
        }
    }

    public QuestionAdapter(Context context, List<SecurityQuestionDTO> list) {
        this.dto = null;
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
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

    public View getView(int position, View convertView, ViewGroup parent) {
        QuItem item;
        if (convertView == null) {
            item = new QuItem();
            convertView = this.inflater.inflate(C0136R.layout.qu_item, null);
            item.qutext = (TextView) convertView.findViewById(C0136R.id.quitem);
            convertView.setTag(item);
        } else {
            item = (QuItem) convertView.getTag();
        }
        item.qutext.setText(((SecurityQuestionDTO) this.list.get(position)).getQuestionDesc());
        return convertView;
    }
}
