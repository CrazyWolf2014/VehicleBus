package com.ifoer.pro.expeditionphone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.adapter.KeyToUpgradeAdapter;
import com.ifoer.adapter.KeyToUpgradeAdapter.ViewHolder;
import com.ifoer.entity.X431PadSoftDTO;
import com.ifoer.expedition.crp229.C0501R;
import com.ifoer.expeditionphone.DownloadAllSoftwareActivity;
import com.ifoer.util.DialogUtil;
import com.ifoer.util.MyApplication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class KeyToUpgradeActivity extends Activity {
    private KeyToUpgradeAdapter adapter;
    private Button back;
    private CheckBox checkBoxs;
    private Context context;
    private List<X431PadDtoSoft> historyVerList;
    private LayoutInflater inflater;
    private Map<Integer, Boolean> isSelected;
    private final Handler mHandler;
    private TextView newVersion;
    private Button next;
    private ProgressDialog progressDialogs;
    private List<X431PadDtoSoft> resHaveLastVerList;
    private List<X431PadDtoSoft> resultList;
    private ListView upgradeListview;

    /* renamed from: com.ifoer.pro.expeditionphone.KeyToUpgradeActivity.1 */
    class C06791 extends Handler {
        C06791() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (KeyToUpgradeActivity.this.progressDialogs != null && KeyToUpgradeActivity.this.progressDialogs.isShowing()) {
                        KeyToUpgradeActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(KeyToUpgradeActivity.this, C0501R.string.timeout, 0).show();
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    if (KeyToUpgradeActivity.this.progressDialogs != null && KeyToUpgradeActivity.this.progressDialogs.isShowing()) {
                        KeyToUpgradeActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(KeyToUpgradeActivity.this, C0501R.string.get_data_fail, 1).show();
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                    KeyToUpgradeActivity.this.adapter.notifyDataSetChanged();
                case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                    KeyToUpgradeActivity.this.adapter.onConfigurationChanged(KeyToUpgradeActivity.this);
                default:
            }
        }
    }

    /* renamed from: com.ifoer.pro.expeditionphone.KeyToUpgradeActivity.2 */
    class C06802 implements OnItemClickListener {
        C06802() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            ViewHolder item = (ViewHolder) arg1.getTag();
            if (((X431PadDtoSoft) KeyToUpgradeActivity.this.resultList.get(arg2)).getVersionNo() != XmlPullParser.NO_NAMESPACE) {
                item.checkbox.toggle();
                KeyToUpgradeActivity.this.isSelected.put(Integer.valueOf(arg2), Boolean.valueOf(item.checkbox.isChecked()));
            }
        }
    }

    /* renamed from: com.ifoer.pro.expeditionphone.KeyToUpgradeActivity.3 */
    class C06813 implements OnClickListener {
        C06813() {
        }

        public void onClick(View v) {
            int i;
            ArrayList<Integer> list = new ArrayList();
            for (i = 0; i < KeyToUpgradeActivity.this.upgradeListview.getCount(); i++) {
                if (((Boolean) KeyToUpgradeActivity.this.isSelected.get(Integer.valueOf(i))).booleanValue()) {
                    list.add(Integer.valueOf(i));
                }
            }
            if (list.size() > 0) {
                ArrayList<X431PadSoftDTO> downloadList = new ArrayList();
                for (i = 0; i < list.size(); i++) {
                    downloadList.add((X431PadSoftDTO) KeyToUpgradeActivity.this.adapter.getItem(((Integer) list.get(i)).intValue()));
                }
                Intent intent = new Intent(KeyToUpgradeActivity.this, DownloadAllSoftwareActivity.class);
                intent.putExtra("downloadList", downloadList);
                KeyToUpgradeActivity.this.startActivity(intent);
                KeyToUpgradeActivity.this.finish();
                return;
            }
            Toast.makeText(KeyToUpgradeActivity.this, C0501R.string.shopping_please, 0).show();
        }
    }

    /* renamed from: com.ifoer.pro.expeditionphone.KeyToUpgradeActivity.4 */
    class C06824 implements OnClickListener {
        C06824() {
        }

        public void onClick(View v) {
            KeyToUpgradeActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.pro.expeditionphone.KeyToUpgradeActivity.5 */
    class C06835 implements OnCheckedChangeListener {
        C06835() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (KeyToUpgradeActivity.this.resultList != null) {
                int i;
                if (isChecked) {
                    if (KeyToUpgradeActivity.this.upgradeListview.getCount() > 0) {
                        for (i = 0; i < KeyToUpgradeActivity.this.upgradeListview.getCount(); i++) {
                            KeyToUpgradeActivity.this.isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
                        }
                    }
                    KeyToUpgradeActivity.this.checkBoxs.setText(KeyToUpgradeActivity.this.getResources().getString(C0501R.string.check_all));
                } else {
                    if (KeyToUpgradeActivity.this.upgradeListview.getCount() > 0) {
                        for (i = 0; i < KeyToUpgradeActivity.this.upgradeListview.getCount(); i++) {
                            if (!XmlPullParser.NO_NAMESPACE.equalsIgnoreCase(((X431PadDtoSoft) KeyToUpgradeActivity.this.resultList.get(i)).getVersionNo())) {
                                KeyToUpgradeActivity.this.isSelected.put(Integer.valueOf(i), Boolean.valueOf(true));
                            }
                        }
                    }
                    KeyToUpgradeActivity.this.checkBoxs.setText(KeyToUpgradeActivity.this.getResources().getString(C0501R.string.invert));
                }
                if (KeyToUpgradeActivity.this.adapter != null) {
                    KeyToUpgradeActivity.this.adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public KeyToUpgradeActivity() {
        this.resultList = new ArrayList();
        this.resHaveLastVerList = new ArrayList();
        this.historyVerList = new ArrayList();
        this.isSelected = new HashMap();
        this.mHandler = new C06791();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0501R.layout.key_to_upgrade);
        MyApplication.getInstance().addActivity(this);
        this.context = this;
        DialogUtil.setDialogSize(this);
        this.inflater = LayoutInflater.from(this.context);
        this.upgradeListview = (ListView) findViewById(C0501R.id.upgradeListview);
        this.newVersion = (TextView) findViewById(C0501R.id.newVersion);
        this.upgradeListview.setOnItemClickListener(new C06802());
        this.next = (Button) findViewById(C0501R.id.next);
        this.next.setVisibility(4);
        this.next.setOnClickListener(new C06813());
        this.back = (Button) findViewById(C0501R.id.back);
        this.back.setOnClickListener(new C06824());
        this.checkBoxs = (CheckBox) findViewById(C0501R.id.software_checkbox);
        this.checkBoxs.setOnCheckedChangeListener(new C06835());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0501R.menu.main, menu);
        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DialogUtil.setDialogSize(this);
        this.mHandler.sendEmptyMessage(11);
    }
}
