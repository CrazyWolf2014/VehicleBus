package com.ifoer.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.PushMessageDTO;
import com.ifoer.mine.Contact;
import java.util.List;

public class PushInfoListAdapter extends BaseAdapter {
    private Context context;
    private Handler handler;
    private List<PushMessageDTO> list;

    /* renamed from: com.ifoer.adapter.PushInfoListAdapter.1 */
    class C03411 implements OnClickListener {
        private final /* synthetic */ int val$position;

        C03411(int i) {
            this.val$position = i;
        }

        public void onClick(View v) {
            if (((PushMessageDTO) PushInfoListAdapter.this.list.get(this.val$position)).getResolvedFlag().equals(Contact.RELATION_ASK)) {
                Message msg = PushInfoListAdapter.this.handler.obtainMessage();
                msg.what = 1;
                msg.arg1 = Integer.parseInt(((PushMessageDTO) PushInfoListAdapter.this.list.get(this.val$position)).getMessageId());
                msg.arg2 = this.val$position;
                PushInfoListAdapter.this.handler.sendMessage(msg);
            }
        }
    }

    class Item {
        TextView messageDesc;
        TextView messageTitle;
        TextView pushTime;
        Button resolvedFlag;

        Item() {
        }
    }

    public PushInfoListAdapter(Context context, List<PushMessageDTO> list, Handler handler) {
        this.context = context;
        this.list = list;
        this.handler = handler;
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

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Item item;
        if (convertView == null) {
            item = new Item();
            convertView = LayoutInflater.from(this.context).inflate(C0136R.layout.client_promat_info_itme, null);
            item.resolvedFlag = (Button) convertView.findViewById(C0136R.id.resolvedFlag);
            item.pushTime = (TextView) convertView.findViewById(C0136R.id.pushTime);
            item.messageDesc = (TextView) convertView.findViewById(C0136R.id.messageDesc);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        if (((PushMessageDTO) this.list.get(position)).getResolvedFlag().equals(Contact.RELATION_ASK)) {
            item.resolvedFlag.setBackgroundResource(C0136R.drawable.untreated);
            item.resolvedFlag.setText(this.context.getResources().getText(C0136R.string.untreated));
        } else {
            item.resolvedFlag.setBackgroundResource(C0136R.drawable.treated);
            item.resolvedFlag.setText(this.context.getResources().getText(C0136R.string.treated));
        }
        item.resolvedFlag.setOnClickListener(new C03411(position));
        item.pushTime.setText(((PushMessageDTO) this.list.get(position)).getPushTime());
        item.messageDesc.setText(((PushMessageDTO) this.list.get(position)).getMessageDesc());
        return convertView;
    }
}
