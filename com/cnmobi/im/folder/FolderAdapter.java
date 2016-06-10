package com.cnmobi.im.folder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import java.io.File;
import java.util.List;

public class FolderAdapter extends BaseAdapter {
    private List<String> items;
    private Bitmap mIcon1;
    private Bitmap mIcon2;
    private Bitmap mIcon3;
    private Bitmap mIcon4;
    private LayoutInflater mInflater;
    private List<String> paths;

    private class ViewHolder {
        ImageView icon;
        TextView text;

        private ViewHolder() {
        }
    }

    public FolderAdapter(Context context, List<String> it, List<String> pa) {
        this.mInflater = LayoutInflater.from(context);
        this.items = it;
        this.paths = pa;
        this.mIcon1 = BitmapFactory.decodeResource(context.getResources(), C0136R.drawable.im_folder_back01);
        this.mIcon2 = BitmapFactory.decodeResource(context.getResources(), C0136R.drawable.im_folder_back02);
        this.mIcon3 = BitmapFactory.decodeResource(context.getResources(), C0136R.drawable.im_folder_folder);
        this.mIcon4 = BitmapFactory.decodeResource(context.getResources(), C0136R.drawable.im_folder_doc);
    }

    public int getCount() {
        return this.items.size();
    }

    public Object getItem(int position) {
        return this.items.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(C0136R.layout.file_row, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(C0136R.id.text);
            holder.icon = (ImageView) convertView.findViewById(C0136R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File f = new File(((String) this.paths.get(position)).toString());
        if (((String) this.items.get(position)).toString().equals("b1")) {
            holder.text.setText("\u8fd4\u56de\u6839\u76ee\u5f55..");
            holder.icon.setImageBitmap(this.mIcon1);
        } else if (((String) this.items.get(position)).toString().equals("b2")) {
            holder.text.setText("\u8fd4\u56de\u4e0a\u4e00\u5c42..");
            holder.icon.setImageBitmap(this.mIcon2);
        } else {
            holder.text.setText(f.getName());
            if (f.isDirectory()) {
                holder.icon.setImageBitmap(this.mIcon3);
            } else {
                holder.icon.setImageBitmap(this.mIcon4);
            }
        }
        return convertView;
    }
}
