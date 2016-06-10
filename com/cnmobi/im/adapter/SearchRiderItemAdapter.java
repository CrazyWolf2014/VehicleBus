package com.cnmobi.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.dto.RiderVo;
import java.util.List;

public class SearchRiderItemAdapter extends BaseAdapter {
    private Context context;
    private boolean isRiderList;
    private List<RiderVo> riders;

    public SearchRiderItemAdapter(Context context, boolean isRiderList) {
        this.context = context;
        this.isRiderList = isRiderList;
    }

    public void resetRiders(List<RiderVo> riders) {
        this.riders = riders;
    }

    public int getCount() {
        if (this.riders != null) {
            return this.riders.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RiderVo rider = (RiderVo) this.riders.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(C0136R.layout.rider_list_item, null);
        }
        ((TextView) convertView.findViewById(C0136R.id.riderName)).setText(rider.name);
        convertView.setTag(C0136R.id.riderName, rider.name);
        convertView.setTag(C0136R.id.JID, rider.jId);
        TextView additionalMsg = (TextView) convertView.findViewById(C0136R.id.additionalMsg);
        if (this.isRiderList) {
            additionalMsg.setText(rider.signature);
        }
        return convertView;
    }
}
