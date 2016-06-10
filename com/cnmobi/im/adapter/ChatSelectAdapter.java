package com.cnmobi.im.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.folder.FolderActivity;
import java.util.ArrayList;
import java.util.List;

public class ChatSelectAdapter extends BaseAdapter {
    public static final int REQUEST_CODE_EMOTION = 100;
    public static final int REQUEST_CODE_FILE = 102;
    public static final int REQUEST_CODE_PICTURE = 101;
    private static final String TYPE_EMOTION = "emotion";
    private static final String TYPE_FILE = "file";
    private static final String TYPE_PICTURE = "picture";
    private Context context;
    private List<ChatSelectItem> items;
    private LayoutInflater layoutInflater;

    /* renamed from: com.cnmobi.im.adapter.ChatSelectAdapter.1 */
    class C01941 implements OnClickListener {
        private final /* synthetic */ ChatSelectItem val$item;

        C01941(ChatSelectItem chatSelectItem) {
            this.val$item = chatSelectItem;
        }

        public void onClick(View v) {
            ((Activity) ChatSelectAdapter.this.context).startActivityForResult(new Intent(ChatSelectAdapter.this.context, FolderActivity.class), this.val$item.requestCode);
        }
    }

    /* renamed from: com.cnmobi.im.adapter.ChatSelectAdapter.2 */
    class C01952 implements OnClickListener {
        private final /* synthetic */ ChatSelectItem val$item;

        C01952(ChatSelectItem chatSelectItem) {
            this.val$item = chatSelectItem;
        }

        public void onClick(View v) {
            Intent getImage = new Intent("android.intent.action.GET_CONTENT");
            getImage.setType("image/*");
            ((Activity) ChatSelectAdapter.this.context).startActivityForResult(getImage, this.val$item.requestCode);
        }
    }

    /* renamed from: com.cnmobi.im.adapter.ChatSelectAdapter.3 */
    class C01963 implements OnClickListener {
        C01963() {
        }

        public void onClick(View v) {
            Toast.makeText(ChatSelectAdapter.this.context, "\u8868\u60c5\u53d1\u9001\u529f\u80fd\u6b63\u5728\u5efa\u8bbe\u4e2d", 0).show();
        }
    }

    class ChatSelectItem {
        private int imageId;
        private String name;
        private int requestCode;
        private String type;

        ChatSelectItem() {
        }
    }

    public ChatSelectAdapter(Context context) {
        this.items = new ArrayList();
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        initItems();
    }

    private void initItems() {
        ChatSelectItem item = new ChatSelectItem();
        item.imageId = C0136R.drawable.im_attachment_emotion_btn;
        item.name = "\u8868\u60c5";
        item.type = TYPE_EMOTION;
        item.requestCode = REQUEST_CODE_EMOTION;
        this.items.add(item);
        item = new ChatSelectItem();
        item.imageId = C0136R.drawable.im_attachment_picture_btn;
        item.name = "\u56fe\u7247";
        item.type = TYPE_PICTURE;
        item.requestCode = REQUEST_CODE_PICTURE;
        this.items.add(item);
        item = new ChatSelectItem();
        item.imageId = C0136R.drawable.im_attachment_card_btn;
        item.name = "\u6587\u4ef6";
        item.type = TYPE_FILE;
        item.requestCode = REQUEST_CODE_FILE;
        this.items.add(item);
    }

    public int getCount() {
        return this.items.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChatSelectItem item = (ChatSelectItem) this.items.get(position);
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(C0136R.layout.base_grid_item, null);
        }
        ImageButton ib = (ImageButton) convertView.findViewById(C0136R.id.griditem_pic);
        TextView tv = (TextView) convertView.findViewById(C0136R.id.griditem_title);
        ib.setImageResource(item.imageId);
        tv.setText(item.name);
        if (TYPE_FILE.equals(item.type)) {
            ib.setOnClickListener(new C01941(item));
        }
        if (TYPE_PICTURE.equals(item.type)) {
            ib.setOnClickListener(new C01952(item));
        }
        if (TYPE_EMOTION.equals(item.type)) {
            ib.setOnClickListener(new C01963());
        }
        return convertView;
    }
}
