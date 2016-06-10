package com.ifoer.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.ifoer.dbentity.DownloadStatus;
import java.util.ArrayList;
import java.util.Map;

public class DownloadSoftwareAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<X431PadDtoSoft> data;
    private Map<String, DownloadStatus> downloads;
    private Handler handler;
    private LayoutInflater inflater;

    /* renamed from: com.ifoer.adapter.DownloadSoftwareAdapter.1 */
    class C03341 implements OnClickListener {
        private final /* synthetic */ int val$arg0;

        C03341(int i) {
            this.val$arg0 = i;
        }

        public void onClick(View v) {
            DownloadSoftwareAdapter.this.handler.obtainMessage(7, ((X431PadDtoSoft) DownloadSoftwareAdapter.this.data.get(this.val$arg0)).getSoftId()).sendToTarget();
        }
    }

    class ViewHolder {
        TextView name;
        ProgressBar progressBar;
        LinearLayout redownload;
        TextView status;
        TextView version;

        ViewHolder() {
        }
    }

    public DownloadSoftwareAdapter(ArrayList<X431PadDtoSoft> data, Map<String, DownloadStatus> downloads, Context context, Handler handler) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.downloads = downloads;
        this.handler = handler;
    }

    public int getCount() {
        if (this.data.size() > 0) {
            return this.data.size();
        }
        return 0;
    }

    public Object getItem(int arg0) {
        return this.data.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int arg0, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;
        CharSequence charSequence;
        if (null == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(C0136R.layout.download_software_item, null, false);
            viewHolder.name = (TextView) convertView.findViewById(C0136R.id.name);
            viewHolder.version = (TextView) convertView.findViewById(C0136R.id.version);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(C0136R.id.progress);
            viewHolder.status = (TextView) convertView.findViewById(C0136R.id.status);
            viewHolder.redownload = (LinearLayout) convertView.findViewById(C0136R.id.reDownload);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TextView textView = viewHolder.name;
        if (((X431PadDtoSoft) this.data.get(arg0)).getSoftName().equals("EOBD2")) {
            charSequence = "EOBD";
        } else {
            charSequence = ((X431PadDtoSoft) this.data.get(arg0)).getSoftName();
        }
        textView.setText(charSequence);
        viewHolder.version.setText(((X431PadDtoSoft) this.data.get(arg0)).getVersionNo());
        long downloadSize = ((DownloadStatus) this.downloads.get(((X431PadDtoSoft) this.data.get(arg0)).getSoftId())).getDownloadSize();
        long fileSize = ((DownloadStatus) this.downloads.get(((X431PadDtoSoft) this.data.get(arg0)).getSoftId())).getFileSize();
        if (fileSize != 0) {
            viewHolder.progressBar.setProgress((int) ((100 * downloadSize) / fileSize));
        }
        if (((DownloadStatus) this.downloads.get(((X431PadDtoSoft) this.data.get(arg0)).getSoftId())).isStatusRedownload()) {
            viewHolder.status.setVisibility(8);
            viewHolder.redownload.setVisibility(0);
            viewHolder.redownload.setClickable(true);
            viewHolder.redownload.setOnClickListener(new C03341(arg0));
        } else {
            viewHolder.status.setVisibility(0);
            viewHolder.redownload.setVisibility(8);
            viewHolder.status.setText(((DownloadStatus) this.downloads.get(((X431PadDtoSoft) this.data.get(arg0)).getSoftId())).getStatus());
            viewHolder.status.setClickable(false);
        }
        return convertView;
    }
}
