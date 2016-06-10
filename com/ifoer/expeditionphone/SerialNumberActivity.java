package com.ifoer.expeditionphone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.ifoer.adapter.SerialNamberListAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.db.PortThread;
import com.ifoer.util.DialogUtil;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MyDownLoadBinUpdate;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.MySoftUpdate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xmlpull.v1.XmlPullParser;

public class SerialNumberActivity extends Activity {
    private SerialNamberListAdapter adapter;
    private Context context;
    private String flag;
    private ImageView img_close;

    /* renamed from: com.ifoer.expeditionphone.SerialNumberActivity.1 */
    class C06291 implements OnItemClickListener {
        private final /* synthetic */ List val$list;

        C06291(List list) {
            this.val$list = list;
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            String serialNo = ((String) this.val$list.get(arg2)).toString();
            List<HashMap<String, String>> list = new ArrayList();
            HashMap<String, String> map = new HashMap();
            map.put(MultipleAddresses.CC, MySharedPreferences.getStringValue(SerialNumberActivity.this.context, MySharedPreferences.CCKey));
            map.put(Constants.serialNo, serialNo);
            list.add(map);
            new PortThread(SerialNumberActivity.this.context, list).start();
            MySharedPreferences.setString(SerialNumberActivity.this.context, MySharedPreferences.serialNoKey, serialNo);
            if (SerialNumberActivity.this.flag != null && SerialNumberActivity.this.flag.equals("upgrade")) {
                SerialNumberActivity.this.startActivity(new Intent(SerialNumberActivity.this.context, KeyToUpgradeActivity.class));
            }
            if (SerialNumberActivity.this.flag != null && SerialNumberActivity.this.flag.equals("spaceOrder")) {
                SerialNumberActivity.this.context.sendBroadcast(new Intent("spaceOrder"));
            }
            SerialNumberActivity.this.context.sendBroadcast(new Intent("Show_serial"));
            SerialNumberActivity.this.finish();
            SerialNumberActivity.this.sendBroadcast(new Intent("changenumber"));
            if (MySharedPreferences.getStringValue(SerialNumberActivity.this.context, MySharedPreferences.CCKey) != null && !XmlPullParser.NO_NAMESPACE.equalsIgnoreCase(MySharedPreferences.getStringValue(SerialNumberActivity.this.context, MySharedPreferences.CCKey))) {
                new MySoftUpdate(SerialNumberActivity.this.context).checkUpdateAsync();
                MyDownLoadBinUpdate.getMyDownLoadBinUpdate(SerialNumberActivity.this.context).checkUpdateAsync();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.SerialNumberActivity.2 */
    class C06302 implements OnClickListener {
        C06302() {
        }

        public void onClick(View v) {
            SerialNumberActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.serial_number);
        Intent intent = getIntent();
        if (intent != null) {
            this.flag = intent.getStringExtra("flag");
        }
        MyApplication.getInstance().addActivity(this);
        this.context = this;
        DialogUtil.setDialogSize(this);
        ListView listView = (ListView) findViewById(C0136R.id.listview);
        List<String> list = DBDao.getInstance(this).querytAllSerialNo(MainActivity.database);
        this.adapter = new SerialNamberListAdapter(list, this.context);
        listView.setAdapter(this.adapter);
        listView.setOnItemClickListener(new C06291(list));
        if (MySharedPreferences.getIntValue(this, MySharedPreferences.DefScanPad) == 1) {
            this.img_close = (ImageView) findViewById(C0136R.id.img_close);
            this.img_close.setOnClickListener(new C06302());
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DialogUtil.setDialogSize(this);
    }
}
