package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SptActiveTestStream;
import com.ifoer.expedition.BluetoothChat.ActiveTestActivity;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

public class StremAdapter extends BaseAdapter {
    private ArrayList<SptActiveTestStream> activeTestList;
    private Context context;
    private LayoutInflater inflater;

    class ViewHolder {
        TextView name;
        TextView units;
        TextView value;

        ViewHolder() {
        }
    }

    public StremAdapter(Context context, ArrayList<SptActiveTestStream> activeTestList) {
        this.activeTestList = new ArrayList();
        this.context = context;
        this.activeTestList = activeTestList;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        if (this.activeTestList.size() > 0) {
            return this.activeTestList.size();
        }
        return 0;
    }

    public Object getItem(int arg0) {
        return this.activeTestList.get(arg0);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void refresh(ArrayList<SptActiveTestStream> list) {
        this.activeTestList = list;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = this.inflater.inflate(C0136R.layout.stream_item, null);
            viewHolder.name = (TextView) convertView.findViewById(C0136R.id.oneText);
            viewHolder.value = (TextView) convertView.findViewById(C0136R.id.twoText);
            viewHolder.units = (TextView) convertView.findViewById(C0136R.id.threeText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(((SptActiveTestStream) this.activeTestList.get(position)).getDataStreamContent().trim());
        viewHolder.value.setText(((SptActiveTestStream) this.activeTestList.get(position)).getDataStreamStr().trim());
        viewHolder.units.setText(((SptActiveTestStream) this.activeTestList.get(position)).getUnit().trim());
        ActiveTestActivity activity = this.context;
        if (((SptActiveTestStream) this.activeTestList.get(0)).getUnit().trim() == null || ((SptActiveTestStream) this.activeTestList.get(0)).getUnit().trim().equals(XmlPullParser.NO_NAMESPACE)) {
            if (activity.isPro) {
                viewHolder.units.setVisibility(8);
            } else if (((SptActiveTestStream) this.activeTestList.get(0)).getDataStreamContent().trim().substring(0, 1).equals(".")) {
                activity.setText(this.context.getString(C0136R.string.FourText), this.context.getString(C0136R.string.download_software_condition), false);
                viewHolder.units.setVisibility(8);
            } else if (((SptActiveTestStream) this.activeTestList.get(0)).getDataStreamContent().trim().equalsIgnoreCase("System Search")) {
                activity.setText(this.context.getString(C0136R.string.system_name), this.context.getString(C0136R.string.download_software_condition), false);
                viewHolder.units.setVisibility(8);
            } else if (Constant.itemContent.equalsIgnoreCase("system search") || Constant.itemContent.contains("system")) {
                activity.setText(this.context.getString(C0136R.string.system_name), this.context.getString(C0136R.string.download_software_condition), false);
                viewHolder.units.setVisibility(8);
            }
        }
        return convertView;
    }
}
