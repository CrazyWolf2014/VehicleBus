package com.ifoer.adapter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Drafts;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.Common;
import java.lang.reflect.Field;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class ClientDraftAdapter extends BaseAdapter {
    private Context context;
    private Handler handler;
    private LayoutInflater inflater;
    private List<Drafts> list;

    /* renamed from: com.ifoer.adapter.ClientDraftAdapter.1 */
    class C03291 implements OnClickListener {
        private final /* synthetic */ int val$position;

        C03291(int i) {
            this.val$position = i;
        }

        public void onClick(View arg0) {
            Drafts drafts = new Drafts();
            drafts.setId(((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getId());
            drafts.setName(((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getName());
            drafts.setPassword(((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getPassword());
            drafts.setPhoneNum(((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getPhoneNum());
            drafts.setSerialNo(((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getSerialNo());
            drafts.setServiceCycle(((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getServiceCycle());
            ClientDraftAdapter.this.handler.obtainMessage(1, drafts).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.adapter.ClientDraftAdapter.2 */
    class C03302 implements OnClickListener {
        private final /* synthetic */ int val$position;

        C03302(int i) {
            this.val$position = i;
        }

        public void onClick(View v) {
            ClientDraftAdapter.this.modifyDraftDialog(((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getId(), ((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getName(), ((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getPhoneNum(), ((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getSerialNo(), ((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getPassword(), ((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getServiceCycle());
        }
    }

    /* renamed from: com.ifoer.adapter.ClientDraftAdapter.3 */
    class C03313 implements OnClickListener {
        private final /* synthetic */ int val$position;

        C03313(int i) {
            this.val$position = i;
        }

        public void onClick(View arg0) {
            if (DBDao.getInstance(ClientDraftAdapter.this.context).deleteDrafts(((Drafts) ClientDraftAdapter.this.list.get(this.val$position)).getId(), MainActivity.database) > 0) {
                ClientDraftAdapter.this.list.clear();
                ClientDraftAdapter.this.list = DBDao.getInstance(ClientDraftAdapter.this.context).queryDrafts(MainActivity.database);
                ClientDraftAdapter.this.notifyDataSetChanged();
                Toast.makeText(ClientDraftAdapter.this.context, C0136R.string.delete_success, 0).show();
                return;
            }
            Toast.makeText(ClientDraftAdapter.this.context, C0136R.string.delete_fail, 0).show();
        }
    }

    /* renamed from: com.ifoer.adapter.ClientDraftAdapter.4 */
    class C03324 implements DialogInterface.OnClickListener {
        private final /* synthetic */ int val$id;
        private final /* synthetic */ EditText val$mCustomerName;
        private final /* synthetic */ EditText val$mMobile;
        private final /* synthetic */ EditText val$mNext;
        private final /* synthetic */ EditText val$mSerialNo;
        private final /* synthetic */ EditText val$mSerialNoPw;

        C03324(EditText editText, EditText editText2, EditText editText3, EditText editText4, EditText editText5, int i) {
            this.val$mCustomerName = editText;
            this.val$mMobile = editText2;
            this.val$mSerialNo = editText3;
            this.val$mSerialNoPw = editText4;
            this.val$mNext = editText5;
            this.val$id = i;
        }

        public void onClick(DialogInterface dialog, int which) {
            try {
                Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                field.setAccessible(true);
                field.set(dialog, Boolean.valueOf(false));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String customerName = this.val$mCustomerName.getText().toString();
            String mobile = this.val$mMobile.getText().toString();
            String serialNo = this.val$mSerialNo.getText().toString();
            String serialNoPw = this.val$mSerialNoPw.getText().toString();
            String nextMaintanceMileage = this.val$mNext.getText().toString();
            if (customerName.equals(XmlPullParser.NO_NAMESPACE)) {
                Toast.makeText(ClientDraftAdapter.this.context, C0136R.string.client_name_is_null, 0).show();
            } else if (mobile.equals(XmlPullParser.NO_NAMESPACE)) {
                Toast.makeText(ClientDraftAdapter.this.context, C0136R.string.modi_phone_right, 0).show();
            } else if (serialNo.length() != 12 || !Common.isNumber(serialNo)) {
                Toast.makeText(ClientDraftAdapter.this.context, ClientDraftAdapter.this.context.getResources().getString(C0136R.string.port_error), 0).show();
            } else if (serialNoPw.equals(XmlPullParser.NO_NAMESPACE)) {
                Toast.makeText(ClientDraftAdapter.this.context, C0136R.string.pwd_not_null, 0).show();
            } else if (nextMaintanceMileage.equals(XmlPullParser.NO_NAMESPACE)) {
                Toast.makeText(ClientDraftAdapter.this.context, C0136R.string.next_maintenance_mileage_is_null, 0).show();
            } else {
                try {
                    field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                    field.setAccessible(true);
                    field.set(dialog, Boolean.valueOf(true));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                dialog.dismiss();
                DBDao.getInstance(ClientDraftAdapter.this.context).updateDrafts(this.val$id, customerName, mobile, serialNo, serialNoPw, nextMaintanceMileage, MainActivity.database);
                if (ClientDraftAdapter.this.list != null) {
                    ClientDraftAdapter.this.list.clear();
                    ClientDraftAdapter.this.list = DBDao.getInstance(ClientDraftAdapter.this.context).queryDrafts(MainActivity.database);
                    ClientDraftAdapter.this.notifyDataSetChanged();
                }
            }
        }
    }

    /* renamed from: com.ifoer.adapter.ClientDraftAdapter.5 */
    class C03335 implements DialogInterface.OnClickListener {
        C03335() {
        }

        public void onClick(DialogInterface dialog, int which) {
            try {
                Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                field.setAccessible(true);
                field.set(dialog, Boolean.valueOf(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }
    }

    class Item {
        Button del;
        Button edit;
        TextView first_name;
        TextView next_maintenance_mileage;
        TextView phone_number;
        Button submit;
        TextView verification_code;

        Item() {
        }
    }

    public ClientDraftAdapter(Context context, List<Drafts> list, Handler handler) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
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

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int position, View convertView, ViewGroup arg2) {
        Item item;
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.client_draft_item, null);
            item.first_name = (TextView) convertView.findViewById(C0136R.id.first_name);
            item.next_maintenance_mileage = (TextView) convertView.findViewById(C0136R.id.maintenance_mileage);
            item.phone_number = (TextView) convertView.findViewById(C0136R.id.phone_number);
            item.verification_code = (TextView) convertView.findViewById(C0136R.id.verification_code);
            item.submit = (Button) convertView.findViewById(C0136R.id.Submit);
            item.edit = (Button) convertView.findViewById(C0136R.id.edit);
            item.del = (Button) convertView.findViewById(C0136R.id.del);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.first_name.setText(((Drafts) this.list.get(position)).getName());
        item.phone_number.setText(((Drafts) this.list.get(position)).getPhoneNum());
        item.verification_code.setText(((Drafts) this.list.get(position)).getSerialNo());
        item.next_maintenance_mileage.setText(((Drafts) this.list.get(position)).getServiceCycle());
        item.submit.setOnClickListener(new C03291(position));
        item.edit.setOnClickListener(new C03302(position));
        item.del.setOnClickListener(new C03313(position));
        return convertView;
    }

    public void refresh(List<Drafts> draftLists) {
        this.list = draftLists;
        notifyDataSetChanged();
    }

    private void modifyDraftDialog(int id, String oldCustomerName, String oldMobile, String oldSerialNo, String oldSerialNoPw, String oldNext) {
        View view = ((Activity) this.context).getLayoutInflater().inflate(C0136R.layout.edit_draft, null);
        EditText mCustomerName = (EditText) view.findViewById(C0136R.id.customerName);
        mCustomerName.setText(oldCustomerName);
        EditText mMobile = (EditText) view.findViewById(C0136R.id.mobile);
        mMobile.setText(oldMobile);
        EditText mSerialNo = (EditText) view.findViewById(C0136R.id.serialNo);
        mSerialNo.setText(oldSerialNo);
        EditText mSerialNoPw = (EditText) view.findViewById(C0136R.id.serialNoPwd);
        mSerialNoPw.setText(oldSerialNoPw);
        EditText mNext = (EditText) view.findViewById(C0136R.id.nextMaintanceMileage);
        mNext.setText(oldNext);
        Builder buidler = new Builder(this.context);
        buidler.setTitle(this.context.getResources().getText(C0136R.string.edit));
        buidler.setView(view);
        buidler.setCancelable(false);
        buidler.setPositiveButton(this.context.getResources().getText(C0136R.string.sure), new C03324(mCustomerName, mMobile, mSerialNo, mSerialNoPw, mNext, id));
        buidler.setNegativeButton(this.context.getResources().getText(C0136R.string.cancel), new C03335());
        buidler.show();
    }
}
