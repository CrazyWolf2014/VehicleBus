package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.CardConsumeDTO;
import java.util.List;

public class RecordofCardAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<CardConsumeDTO> recordList;

    class RecordItem {
        TextView cardNo;
        TextView dataAfter;
        TextView date;
        TextView dateBefore;
        TextView years;

        RecordItem() {
        }
    }

    public RecordofCardAdapter(Context context, List<CardConsumeDTO> list) {
        this.context = context;
        this.recordList = list;
        this.inflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        return this.recordList.size();
    }

    public Object getItem(int arg0) {
        return this.recordList.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RecordItem item;
        if (convertView == null || convertView.getTag() == null) {
            convertView = this.inflater.inflate(C0136R.layout.recrod_of_card_item, null);
            item = new RecordItem();
            item.cardNo = (TextView) convertView.findViewById(C0136R.id.card_no);
            item.date = (TextView) convertView.findViewById(C0136R.id.updateDate);
            item.dateBefore = (TextView) convertView.findViewById(C0136R.id.time_before_charge);
            item.dataAfter = (TextView) convertView.findViewById(C0136R.id.time_after_charge);
            item.years = (TextView) convertView.findViewById(C0136R.id.update_year);
            convertView.setTag(item);
        } else {
            item = (RecordItem) convertView.getTag();
        }
        item.cardNo.setText(((CardConsumeDTO) this.recordList.get(position)).getCardNo());
        item.date.setText(((CardConsumeDTO) this.recordList.get(position)).getDate());
        item.dataAfter.setText(((CardConsumeDTO) this.recordList.get(position)).getFreeEndTime());
        item.dateBefore.setText(((CardConsumeDTO) this.recordList.get(position)).getOldFreeEndTime());
        item.years.setText(new StringBuilder(String.valueOf(((CardConsumeDTO) this.recordList.get(position)).getYears())).toString());
        return convertView;
    }
}
