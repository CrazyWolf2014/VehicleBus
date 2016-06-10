package com.cnmobi.im;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.LogoManager;
import com.cnmobi.im.bo.MsgManager;
import com.cnmobi.im.dto.RiderVo;
import java.util.List;

public class RiderItemAdapter extends BaseAdapter {
    private Context context;
    private boolean isNeedShowMsgCount;
    private boolean isRiderList;
    private List<RiderVo> riders;

    public RiderItemAdapter(Context context, boolean isRiderList, boolean isNeedShowMsgCount) {
        this.context = context;
        this.isRiderList = isRiderList;
        this.isNeedShowMsgCount = isNeedShowMsgCount;
    }

    public void resetRiders(List<RiderVo> riders) {
        this.riders = riders;
    }

    public List<RiderVo> getRiders() {
        return this.riders;
    }

    public ImageView getLogoView(String jid) {
        if (this.riders == null) {
            return null;
        }
        for (RiderVo rider : this.riders) {
            if (rider.jId.equals(jid)) {
                return rider.logoView;
            }
        }
        return null;
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
        rider.logoView = (ImageView) convertView.findViewById(C0136R.id.riderLogo);
        ((TextView) convertView.findViewById(C0136R.id.riderName)).setText(rider.name);
        convertView.setTag(C0136R.id.riderName, rider.name);
        convertView.setTag(C0136R.id.JID, rider.jId);
        TextView additionalMsg = (TextView) convertView.findViewById(C0136R.id.additionalMsg);
        if (this.isRiderList) {
            additionalMsg.setText(rider.signature);
        }
        if (this.isNeedShowMsgCount) {
            TextView newMsgCountView = (TextView) convertView.findViewById(C0136R.id.riderNewMsg);
            int count = MsgManager.getInstance().getUnReadCountByJid(rider.jId);
            newMsgCountView.setText(String.valueOf(count));
            if (count > 0) {
                newMsgCountView.setVisibility(0);
            } else {
                newMsgCountView.setVisibility(8);
            }
        }
        LogoManager.getInstance().start2GetLogo(rider.jId);
        return convertView;
    }
}
