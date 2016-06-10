package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cnlaunch.framework.utils.NLog;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.expeditionphone.DownloadFragment;
import java.io.File;
import java.util.List;
import org.xbill.DNS.KEYRecord;

public class DownloadAdapter extends BaseAdapter {
    private DownloadFragment fragment;
    private List<X431PadDtoSoft> list;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean mIsRoot;

    class ViewHolder {
        ProgressBar pb_progress_item;
        TextView tv_name_item;
        TextView tv_state_item;
        TextView tv_version_item;

        ViewHolder() {
        }
    }

    public DownloadAdapter(Context context, DownloadFragment mfragment) {
        this.mIsRoot = false;
        this.mContext = context;
        this.fragment = mfragment;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mIsRoot = isRoot();
    }

    public static boolean isRoot() {
        boolean is = false;
        try {
            if (new File("/system/xbin/per-up").exists()) {
                is = true;
            } else {
                is = false;
            }
            NLog.m916d("DownloadAdapter", "isRoot = " + is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    public List<X431PadDtoSoft> getList() {
        return this.list;
    }

    public void setList(List<X431PadDtoSoft> list) {
        this.list = list;
    }

    public int getCount() {
        if (this.list != null) {
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
        ViewHolder holder;
        X431PadDtoSoft bean = (X431PadDtoSoft) getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(C0136R.layout.upgrade_download_item, null);
            holder.tv_name_item = (TextView) convertView.findViewById(C0136R.id.tv_name_item);
            holder.tv_version_item = (TextView) convertView.findViewById(C0136R.id.tv_version_item);
            holder.pb_progress_item = (ProgressBar) convertView.findViewById(C0136R.id.pb_progress_item);
            holder.tv_state_item = (TextView) convertView.findViewById(C0136R.id.tv_state_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name_item.setText(bean.getSoftName());
        holder.tv_version_item.setText(bean.getVersionNo());
        holder.pb_progress_item.setProgress(bean.getProgress());
        switch (bean.getState()) {
            case KEYRecord.OWNER_USER /*0*/:
                holder.tv_state_item.setText(this.mContext.getString(C0136R.string.download_wait));
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                holder.tv_state_item.setText(this.mContext.getString(C0136R.string.download_now));
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (bean.getType() != 1 && bean.getType() != 2) {
                    holder.tv_state_item.setText(this.mContext.getString(C0136R.string.unzip_success));
                    break;
                }
                holder.tv_state_item.setText(this.mContext.getString(C0136R.string.download_finish));
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                holder.tv_state_item.setText(this.mContext.getString(C0136R.string.download_fail));
                break;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                holder.tv_state_item.setText(this.mContext.getString(C0136R.string.unzip_success));
                break;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                holder.tv_state_item.setText(this.mContext.getString(C0136R.string.unzip_fail));
                break;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                holder.tv_state_item.setText(this.mContext.getString(C0136R.string.sdcard_storage_insufficient));
                break;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                holder.tv_state_item.setText(this.mContext.getString(C0136R.string.sdcard_storage_insufficient));
                break;
        }
        return convertView;
    }
}
