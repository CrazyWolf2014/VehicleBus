package com.ifoer.expedition.BluetoothChat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.adapter.BluetoothListAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.dbentity.CarVersionInfo;
import com.ifoer.entity.Bluetooth;
import com.ifoer.entity.Constant;
import com.ifoer.expeditionphone.LoadDynamicLibsActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.DialogUtil;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class DeviceListActivity extends Activity implements OnClickListener {
    private static final boolean f1241D = true;
    public static String EXTRA_DEVICE_ADDRESS = null;
    private static final String TAG = "DeviceListActivity";
    private BluetoothListAdapter adapter;
    private Button back;
    private Bluetooth bluetooth;
    private List<String> containsLists;
    private Context context;
    Handler handler;
    private List<Bluetooth> list;
    private ListView listView;
    private List<Bluetooth> listall;
    private String mAddress;
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private OnItemClickListener mPairedDevicesClickListener;
    private final BroadcastReceiver mReceiver;
    private List<Bluetooth> nulist;
    private List<Bluetooth> nulists;
    private int postion;
    private ProgressDialog proDialog;
    private Button restore;
    private Button scanButton;
    private Button seach;
    private String softID;

    /* renamed from: com.ifoer.expedition.BluetoothChat.DeviceListActivity.1 */
    class C04091 implements OnItemClickListener {
        C04091() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int arg2, long arg3) {
            DeviceListActivity.this.listView.setEnabled(false);
            DeviceListActivity.this.mBtAdapter.cancelDiscovery();
            DeviceListActivity.this.scanButton.setText(DeviceListActivity.this.getResources().getString(C0136R.string.button_scan));
            DeviceListActivity.this.postion = arg2;
            if (((Bluetooth) DeviceListActivity.this.list.get(arg2)).getBluetoothpeidui().equals(DeviceListActivity.this.getResources().getString(C0136R.string.ispair))) {
                ((Bluetooth) DeviceListActivity.this.list.get(arg2)).setBluetootzhuantai(DeviceListActivity.this.getResources().getString(C0136R.string.title_connecting));
                DeviceListActivity.this.adapter.notifyDataSetChanged();
                String address = ((TextView) v.findViewById(C0136R.id.bluetooth_xuliehao)).getText().toString();
                DeviceListActivity.this.pair(address, "0000");
                Intent intent = new Intent(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                Bundle bundle = new Bundle();
                bundle.putString(DeviceListActivity.EXTRA_DEVICE_ADDRESS, address);
                bundle.putBoolean("FIRSTCONNECT", false);
                intent.putExtras(bundle);
                DeviceListActivity.this.sendBroadcast(intent);
                return;
            }
            address = ((TextView) v.findViewById(C0136R.id.bluetooth_xuliehao)).getText().toString();
            DeviceListActivity.this.pair(address, "0000");
            intent = new Intent(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            bundle = new Bundle();
            bundle.putString(DeviceListActivity.EXTRA_DEVICE_ADDRESS, address);
            bundle.putBoolean("FIRSTCONNECT", DeviceListActivity.f1241D);
            intent.putExtras(bundle);
            DeviceListActivity.this.sendBroadcast(intent);
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DeviceListActivity.2 */
    class C04102 extends Handler {
        C04102() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    DeviceListActivity.this.pair(DeviceListActivity.this.mAddress, "0000");
                    Intent intent = new Intent(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    Bundle bundle = new Bundle();
                    bundle.putString(DeviceListActivity.EXTRA_DEVICE_ADDRESS, DeviceListActivity.this.mAddress);
                    intent.putExtras(bundle);
                    DeviceListActivity.this.sendBroadcast(intent);
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    DeviceListActivity.this.scanButton.setText(DeviceListActivity.this.getResources().getString(C0136R.string.scanning));
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DeviceListActivity.3 */
    class C04113 extends BroadcastReceiver {
        C04113() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (device.getBondState() != 12) {
                    int i;
                    DeviceListActivity.this.bluetooth = new Bluetooth();
                    DeviceListActivity.this.bluetooth.setBluetoothname(device.getName());
                    System.out.println("\u672a\u914d\u5bf9\u7684\u84dd\u7259\u540d\u79f0" + device.getName());
                    DeviceListActivity.this.bluetooth.setBluetootzhuantai(DeviceListActivity.this.getResources().getString(C0136R.string.notline));
                    DeviceListActivity.this.bluetooth.setBluetoothaddress(device.getAddress());
                    DeviceListActivity.this.bluetooth.setBluetoothpeidui(DeviceListActivity.this.getResources().getString(C0136R.string.notpair));
                    System.out.println("\u672a\u914d\u5bf9\u7684\u84dd\u7259 =" + DeviceListActivity.this.bluetooth.getBluetoothname() + "    id =" + DeviceListActivity.this.bluetooth.getBluetoothaddress());
                    if (DeviceListActivity.this.bluetooth.getBluetoothname() != null) {
                        DeviceListActivity.this.nulist.add(DeviceListActivity.this.bluetooth);
                    }
                    DeviceListActivity.this.getBluetooth();
                    DeviceListActivity.this.list.clear();
                    for (i = 0; i < DeviceListActivity.this.listall.size(); i++) {
                        DeviceListActivity.this.list.add((Bluetooth) DeviceListActivity.this.listall.get(i));
                    }
                    for (i = 0; i < DeviceListActivity.this.nulist.size(); i++) {
                        System.out.println("\u672a\u914d\u5bf9\u7684\u84dd\u7259\u540d\u79f0111" + ((Bluetooth) DeviceListActivity.this.nulist.get(i)).getBluetoothname());
                        DeviceListActivity.this.nulists.add((Bluetooth) DeviceListActivity.this.nulist.get(i));
                    }
                    DeviceListActivity.this.nulist.clear();
                    for (i = 0; i < DeviceListActivity.this.nulists.size(); i++) {
                        if (!DeviceListActivity.this.containsLists.contains(((Bluetooth) DeviceListActivity.this.nulists.get(i)).getBluetoothname())) {
                            DeviceListActivity.this.list.add((Bluetooth) DeviceListActivity.this.nulists.get(i));
                            DeviceListActivity.this.containsLists.add(((Bluetooth) DeviceListActivity.this.nulists.get(i)).getBluetoothname());
                            System.out.println("\u65b0\u641c\u7d22\u51fa\u6765\u7684\u672a\u914d\u5bf9\u7684\u84dd\u7259\u540d\u79f0" + ((Bluetooth) DeviceListActivity.this.nulists.get(i)).getBluetoothname());
                            if (DeviceListActivity.this.adapter != null) {
                                DeviceListActivity.this.adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                DeviceListActivity.this.scanButton.setText(DeviceListActivity.this.getResources().getString(C0136R.string.button_scan));
                DeviceListActivity.this.nulists.clear();
                DeviceListActivity.this.nulist.clear();
                DeviceListActivity.this.setProgressBarIndeterminateVisibility(false);
            } else if ("FirstNotConnected".equals(action)) {
                if (DeviceListActivity.this.postion < DeviceListActivity.this.list.size() && DeviceListActivity.this.postion != -1) {
                    ((Bluetooth) DeviceListActivity.this.list.get(DeviceListActivity.this.postion)).setBluetootzhuantai(DeviceListActivity.this.getResources().getString(C0136R.string.notline));
                    DeviceListActivity.this.adapter.notifyDataSetChanged();
                }
                DeviceListActivity.this.mAddress = ((Bluetooth) DeviceListActivity.this.list.get(DeviceListActivity.this.postion)).getBluetoothaddress();
                DeviceListActivity.this.handler.obtainMessage(0).sendToTarget();
            } else if ("NotConnected".equals(action)) {
                if (DeviceListActivity.this.postion < DeviceListActivity.this.list.size() && DeviceListActivity.this.postion != -1) {
                    ((Bluetooth) DeviceListActivity.this.list.get(DeviceListActivity.this.postion)).setBluetootzhuantai(DeviceListActivity.this.getResources().getString(C0136R.string.notline));
                    DeviceListActivity.this.adapter.notifyDataSetChanged();
                }
            } else if ("Connected".equals(action)) {
                if (DeviceListActivity.this.postion < DeviceListActivity.this.list.size() && DeviceListActivity.this.postion != -1) {
                    ((Bluetooth) DeviceListActivity.this.list.get(DeviceListActivity.this.postion)).setBluetootzhuantai(DeviceListActivity.this.getResources().getString(C0136R.string.connected));
                    DeviceListActivity.this.adapter.notifyDataSetChanged();
                    MySharedPreferences.getSharedPref(DeviceListActivity.this.getApplicationContext()).edit().putString("BluetoothDeviceAddress", ((Bluetooth) DeviceListActivity.this.list.get(DeviceListActivity.this.postion)).getBluetoothaddress()).commit();
                }
                if (!DeviceListActivity.this.isFinishing()) {
                    DeviceListActivity.this.finish();
                }
            } else if ("STATE_LISTEN".equals(action)) {
                DeviceListActivity.this.listView.setEnabled(DeviceListActivity.f1241D);
                System.out.println("BluetoothChatService.STATE_LISTEN");
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DeviceListActivity.4 */
    class C04124 implements OnClickListener {
        C04124() {
        }

        public void onClick(View v) {
            DeviceListActivity.this.scanButton.setText(DeviceListActivity.this.getResources().getString(C0136R.string.scanning));
            DeviceListActivity.this.doDiscovery();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DeviceListActivity.5 */
    class C04135 implements OnKeyListener {
        C04135() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != 4) {
                return false;
            }
            DeviceListActivity.this.proDialog.dismiss();
            return DeviceListActivity.f1241D;
        }
    }

    class StopProgressdiag extends Thread {
        StopProgressdiag() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r4 = this;
            super.run();
            r2 = com.ifoer.expedition.BluetoothChat.DeviceListActivity.this;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.mBtAdapter;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.isEnabled();	 Catch:{ InterruptedException -> 0x0079 }
            if (r2 == 0) goto L_0x001b;
        L_0x000f:
            r2 = com.ifoer.expedition.BluetoothChat.DeviceListActivity.this;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.mBtAdapter;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.disable();	 Catch:{ InterruptedException -> 0x0079 }
            if (r2 == 0) goto L_0x0073;
        L_0x001b:
            r2 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
            sleep(r2);	 Catch:{ InterruptedException -> 0x0079 }
            r2 = com.ifoer.expedition.BluetoothChat.DeviceListActivity.this;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.mBtAdapter;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.isEnabled();	 Catch:{ InterruptedException -> 0x0079 }
            if (r2 != 0) goto L_0x0038;
        L_0x002c:
            r2 = com.ifoer.expedition.BluetoothChat.DeviceListActivity.this;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.mBtAdapter;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.enable();	 Catch:{ InterruptedException -> 0x0079 }
            if (r2 == 0) goto L_0x007e;
        L_0x0038:
            r2 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
            sleep(r2);	 Catch:{ InterruptedException -> 0x0079 }
            r2 = com.ifoer.expedition.BluetoothChat.DeviceListActivity.this;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.mBtAdapter;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.isEnabled();	 Catch:{ InterruptedException -> 0x0079 }
            if (r2 == 0) goto L_0x0038;
        L_0x0049:
            r2 = com.ifoer.expedition.BluetoothChat.DeviceListActivity.this;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.proDialog;	 Catch:{ InterruptedException -> 0x0079 }
            if (r2 == 0) goto L_0x0066;
        L_0x0051:
            r2 = com.ifoer.expedition.BluetoothChat.DeviceListActivity.this;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.proDialog;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.isShowing();	 Catch:{ InterruptedException -> 0x0079 }
            if (r2 == 0) goto L_0x0066;
        L_0x005d:
            r2 = com.ifoer.expedition.BluetoothChat.DeviceListActivity.this;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.proDialog;	 Catch:{ InterruptedException -> 0x0079 }
            r2.dismiss();	 Catch:{ InterruptedException -> 0x0079 }
        L_0x0066:
            r2 = com.ifoer.expedition.BluetoothChat.DeviceListActivity.this;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.handler;	 Catch:{ InterruptedException -> 0x0079 }
            r3 = 1;
            r2 = r2.obtainMessage(r3);	 Catch:{ InterruptedException -> 0x0079 }
            r2.sendToTarget();	 Catch:{ InterruptedException -> 0x0079 }
        L_0x0072:
            return;
        L_0x0073:
            r2 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
            sleep(r2);	 Catch:{ InterruptedException -> 0x0079 }
            goto L_0x000f;
        L_0x0079:
            r0 = move-exception;
            r0.printStackTrace();
            goto L_0x0072;
        L_0x007e:
            r2 = com.ifoer.expedition.BluetoothChat.DeviceListActivity.this;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.mBtAdapter;	 Catch:{ InterruptedException -> 0x0079 }
            if (r2 == 0) goto L_0x0092;
        L_0x0086:
            r2 = com.ifoer.expedition.BluetoothChat.DeviceListActivity.this;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.mBtAdapter;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = r2.isEnabled();	 Catch:{ InterruptedException -> 0x0079 }
            if (r2 != 0) goto L_0x001b;
        L_0x0092:
            r1 = new android.content.Intent;	 Catch:{ InterruptedException -> 0x0079 }
            r2 = "android.bluetooth.adapter.action.REQUEST_ENABLE";
            r1.<init>(r2);	 Catch:{ InterruptedException -> 0x0079 }
            r2 = com.ifoer.expedition.BluetoothChat.DeviceListActivity.this;	 Catch:{ InterruptedException -> 0x0079 }
            r3 = 2;
            r2.startActivityForResult(r1, r3);	 Catch:{ InterruptedException -> 0x0079 }
            goto L_0x001b;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.ifoer.expedition.BluetoothChat.DeviceListActivity.StopProgressdiag.run():void");
        }
    }

    public DeviceListActivity() {
        this.list = new ArrayList();
        this.listall = new ArrayList();
        this.nulist = new ArrayList();
        this.nulists = new ArrayList();
        this.containsLists = new ArrayList();
        this.postion = -1;
        this.softID = XmlPullParser.NO_NAMESPACE;
        this.mPairedDevicesClickListener = new C04091();
        this.handler = new C04102();
        this.mReceiver = new C04113();
    }

    static {
        EXTRA_DEVICE_ADDRESS = "device_address";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.bluetooth);
        MyApplication.getInstance().addActivity(this);
        this.context = this;
        DialogUtil.setDialogSize(this);
        if (getIntent().getExtras() != null) {
            this.softID = getIntent().getStringExtra("softPackageId");
        }
        this.back = (Button) findViewById(C0136R.id.back);
        this.back.setOnClickListener(this);
        if (this.softID.equalsIgnoreCase("DEMO")) {
            this.back.setText(C0136R.string.skip);
        }
        this.restore = (Button) findViewById(C0136R.id.restore);
        this.restore.setOnClickListener(this);
        setResult(0);
        this.list.clear();
        this.scanButton = (Button) findViewById(C0136R.id.seach);
        this.scanButton.setOnClickListener(new C04124());
        this.listView = (ListView) findViewById(C0136R.id.listview);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.device.action.FOUND");
        filter.addAction("NotConnected");
        filter.addAction("FirstNotConnected");
        filter.addAction("Connected");
        filter.addAction("STATE_LISTEN");
        registerReceiver(this.mReceiver, filter);
        registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
        this.mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        getBluetooth();
        this.adapter = new BluetoothListAdapter(this.list, this.context);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(this.mPairedDevicesClickListener);
        this.proDialog = new ProgressDialog(this.context);
        this.proDialog.setMessage(getResources().getText(C0136R.string.bluetooth_reset));
        this.proDialog.setCancelable(false);
        this.proDialog.setOnKeyListener(new C04135());
    }

    public void getBluetooth() {
        Set<BluetoothDevice> pairedDevices = this.mBtAdapter.getBondedDevices();
        this.containsLists.clear();
        this.listall.clear();
        this.list.clear();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                this.bluetooth = new Bluetooth();
                this.bluetooth.setBluetoothname(device.getName());
                this.bluetooth.setBluetootzhuantai(getResources().getString(C0136R.string.notline));
                this.bluetooth.setBluetoothaddress(device.getAddress());
                this.bluetooth.setBluetoothpeidui(getResources().getString(C0136R.string.ispair));
                if (!this.containsLists.contains(device.getName())) {
                    this.containsLists.add(device.getName());
                    this.list.add(this.bluetooth);
                }
            }
        }
        for (int i = 0; i < this.list.size(); i++) {
            this.listall.add((Bluetooth) this.list.get(i));
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mBtAdapter != null) {
            this.mBtAdapter.cancelDiscovery();
        }
        unregisterReceiver(this.mReceiver);
    }

    private void doDiscovery() {
        setProgressBarIndeterminateVisibility(f1241D);
        if (this.mBtAdapter.isDiscovering()) {
            this.mBtAdapter.cancelDiscovery();
        }
        this.mBtAdapter.startDiscovery();
    }

    public boolean pair(String strAddr, String strPsw) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) {
            Log.d("mylog", "devAdd un effient!");
        }
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
        if (device.getBondState() != 12) {
            try {
                Log.d("mylog", "NOT BOND_BONDED");
                PairingUtils.setPin(device.getClass(), device, strPsw);
                PairingUtils.createBond(device.getClass(), device);
                return f1241D;
            } catch (Exception e) {
                Log.d("mylog", "setPiN failed!");
                e.printStackTrace();
                return false;
            }
        }
        Log.d("mylog", "HAS BOND_BONDED");
        return false;
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.back) {
            if (this.softID.equalsIgnoreCase("DEMO")) {
                Constant.isDemo = f1241D;
                if (MainActivity.country == null || MainActivity.country.length() <= 0) {
                    MainActivity.country = Locale.getDefault().getCountry();
                }
                String lanName = AndroidToLan.toLan(MainActivity.country);
                String serialNo1 = MySharedPreferences.getStringValue(this, MySharedPreferences.serialNoKey);
                ArrayList<CarVersionInfo> list = DBDao.getInstance(this).queryCarVersion("DEMO", lanName, serialNo1, MainActivity.database);
                if (list.size() <= 0) {
                    list = DBDao.getInstance(this).queryCarVersion("DEMO", Constants.DEFAULT_LANGUAGE, serialNo1, MainActivity.database);
                }
                if (list.size() > 0) {
                    Intent intent = new Intent(this, LoadDynamicLibsActivity.class);
                    intent.putExtra("list", list);
                    intent.putExtra("carId", ((CarVersionInfo) list.get(0)).getCarId());
                    intent.putExtra("lanName", ((CarVersionInfo) list.get(0)).getLanName());
                    intent.putExtra("wuhong", "wh");
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                    return;
                }
                Toast.makeText(this, getResources().getString(C0136R.string.main_lanuage_null), 0).show();
                return;
            }
            finish();
        } else if (v.getId() == C0136R.id.restore && this.mBtAdapter != null && this.mBtAdapter.isEnabled()) {
            MainActivity.resetConnectTimes = 0;
            Constant.mChatService.stop();
            new StopProgressdiag().start();
            this.proDialog.show();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DialogUtil.setDialogSize(this);
    }
}
