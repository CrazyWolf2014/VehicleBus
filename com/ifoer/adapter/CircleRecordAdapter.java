package com.ifoer.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.ChatInfo;
import com.ifoer.util.MyApplication;
import java.util.List;

public class CircleRecordAdapter extends BaseAdapter {
    private Context context;
    private Drawable icon;
    private LayoutInflater inflater;
    private List<ChatInfo> list;

    class RecordItem {
        ImageView leftImage;
        ImageView leftPortrait;
        RelativeLayout leftRecord;
        TextView lefttext;
        TextView myName;
        TextView myTime;
        ImageView rightImage;
        ImageView rightPortrait;
        RelativeLayout rightRecord;
        TextView righttext;
        TextView sendName;
        TextView sendTime;

        RecordItem() {
        }
    }

    public CircleRecordAdapter(Context context, List<ChatInfo> list, Drawable icon) {
        this.context = context;
        this.list = list;
        this.icon = icon;
        this.inflater = LayoutInflater.from(context);
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

    @TargetApi(8)
    public View getView(int position, View convertView, ViewGroup parent) {
        RecordItem redItem = new RecordItem();
        ChatInfo chatInfo = (ChatInfo) this.list.get(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(C0136R.layout.circle_text_item, null);
            redItem.leftPortrait = (ImageView) convertView.findViewById(C0136R.id.text_head);
            redItem.rightPortrait = (ImageView) convertView.findViewById(C0136R.id.text_head1);
            redItem.leftImage = (ImageView) convertView.findViewById(C0136R.id.left_image);
            redItem.rightImage = (ImageView) convertView.findViewById(C0136R.id.right_image);
            redItem.lefttext = (TextView) convertView.findViewById(C0136R.id.left_text);
            redItem.righttext = (TextView) convertView.findViewById(C0136R.id.right_text);
            redItem.rightRecord = (RelativeLayout) convertView.findViewById(C0136R.id.right_record);
            redItem.leftRecord = (RelativeLayout) convertView.findViewById(C0136R.id.left_record);
            redItem.sendName = (TextView) convertView.findViewById(C0136R.id.sendName);
            redItem.myName = (TextView) convertView.findViewById(C0136R.id.myName);
            redItem.sendTime = (TextView) convertView.findViewById(C0136R.id.sendTime);
            redItem.myTime = (TextView) convertView.findViewById(C0136R.id.mysendTime);
            convertView.setTag(redItem);
        } else {
            redItem = (RecordItem) convertView.getTag();
        }
        if (this.icon != null) {
            redItem.leftPortrait.setBackgroundDrawable(this.icon);
        }
        byte[] bitmapArray;
        if (chatInfo.isMySend()) {
            redItem.leftRecord.setVisibility(8);
            redItem.rightRecord.setVisibility(0);
            if (chatInfo.isImage()) {
                try {
                    bitmapArray = MyApplication.decode(chatInfo.getMessage());
                    redItem.rightImage.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                redItem.righttext.setVisibility(8);
                redItem.rightImage.setVisibility(0);
            } else {
                redItem.righttext.setText(chatInfo.getMessage());
                redItem.righttext.setVisibility(0);
                redItem.rightImage.setVisibility(8);
            }
            redItem.myName.setText(chatInfo.getUserName());
            redItem.myTime.setText(chatInfo.getTime());
        } else {
            redItem.rightRecord.setVisibility(8);
            redItem.leftRecord.setVisibility(0);
            if (chatInfo.isImage()) {
                try {
                    bitmapArray = MyApplication.decode(chatInfo.getMessage());
                    redItem.leftImage.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length)));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                redItem.lefttext.setVisibility(8);
                redItem.leftImage.setVisibility(0);
            } else {
                redItem.lefttext.setText(chatInfo.getMessage());
                redItem.lefttext.setVisibility(0);
                redItem.leftImage.setVisibility(8);
            }
            redItem.sendName.setText(chatInfo.getFromName());
            redItem.sendTime.setText(chatInfo.getTime());
        }
        return convertView;
    }
}
