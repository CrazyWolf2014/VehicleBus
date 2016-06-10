package com.ifoer.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.mycar.jni.JniX431FileTest;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.dto.Msg;
import com.ifoer.adapter.SpaceDataStreamAdapter;
import com.ifoer.adapter.SpaceDataStreamAdapter.Item;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SpaceInfoRecord;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.expedition.BluetoothChat.DataStreamUtils;
import com.ifoer.expeditionphone.BaseActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.SpaceDataRecord;
import com.ifoer.util.SimpleDialog;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public class SpaceCurrentModeActivity extends Activity implements OnClickListener {
    public static Map<Integer, Boolean> isSelected;
    private SpaceDataStreamAdapter adapter;
    private int cols;
    Context context;
    private Button del;
    private int fileId;
    private ArrayList<SptExDataStreamIdItem> filterLists;
    private Button font;
    private int grp;
    private int hlsx;
    private int id;
    private int item;
    private JniX431FileTest jnitest;
    private long lastTime;
    private ListView listview;
    private List<Integer> mselectedItem;
    public IntentFilter myIntentFilter;
    private String name;
    private String[] namestrs;
    private String path;
    private Button pic;
    public mBroadcastReceiver receiver;
    private String sdCardDir;
    private List<Integer> selectedItem;
    private ArrayList<SptExDataStreamIdItem> streamLists;
    private String[] textStrs;
    private ImageView toright;
    private String[] unitstrs;

    /* renamed from: com.ifoer.ui.SpaceCurrentModeActivity.1 */
    class C07221 implements OnItemClickListener {
        C07221() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            Item item = (Item) arg1.getTag();
            item.checkBox.toggle();
            SpaceCurrentModeActivity.isSelected.put(Integer.valueOf(arg2), Boolean.valueOf(item.checkBox.isChecked()));
        }
    }

    /* renamed from: com.ifoer.ui.SpaceCurrentModeActivity.2 */
    class C07232 implements DialogInterface.OnClickListener {
        C07232() {
        }

        public void onClick(DialogInterface dialog, int which) {
            try {
                SpaceCurrentModeActivity.this.deleteFolderFile(SpaceCurrentModeActivity.this.path, true);
                dialog.dismiss();
                SpaceCurrentModeActivity.this.setResult(1);
                SpaceCurrentModeActivity.this.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.ifoer.ui.SpaceCurrentModeActivity.3 */
    class C07243 implements DialogInterface.OnClickListener {
        private final /* synthetic */ EditText val$infos;

        C07243(EditText editText) {
            this.val$infos = editText;
        }

        public void onClick(DialogInterface dialog, int which) {
            File file = new File(SpaceCurrentModeActivity.this.path);
            SpaceCurrentModeActivity.this.name = this.val$infos.getText().toString();
            String newPath = new StringBuilder(String.valueOf(SpaceCurrentModeActivity.this.sdCardDir)).append(SpaceCurrentModeActivity.this.name).append(".x431").toString();
            if (new File(newPath).exists()) {
                Toast.makeText(SpaceCurrentModeActivity.this.context, C0136R.string.renaem_error, 0).show();
            } else if (SpaceCurrentModeActivity.this.name.length() < 21) {
                System.out.println(newPath.toString());
                file.renameTo(new File(newPath));
                file.delete();
                dialog.dismiss();
                if (DBDao.getInstance(SpaceCurrentModeActivity.this.context).UpdateReport(new StringBuilder(String.valueOf(SpaceCurrentModeActivity.this.id)).toString(), new StringBuilder(String.valueOf(SpaceCurrentModeActivity.this.name)).append(".x431").toString(), newPath, MainActivity.database) > 0) {
                    Toast.makeText(SpaceCurrentModeActivity.this.context, C0136R.string.log_succcess, 0).show();
                }
            } else {
                Toast.makeText(SpaceCurrentModeActivity.this.context, C0136R.string.out_of_Length, 0).show();
            }
        }
    }

    /* renamed from: com.ifoer.ui.SpaceCurrentModeActivity.4 */
    class C07254 implements DialogInterface.OnClickListener {
        C07254() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        @SuppressLint({"ShowToast"})
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - SpaceCurrentModeActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                SpaceCurrentModeActivity.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    public SpaceCurrentModeActivity() {
        this.streamLists = new ArrayList();
        this.name = null;
        this.jnitest = new JniX431FileTest();
    }

    static {
        isSelected = new HashMap();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.space_data_flow);
        registerBoradcastReceiver();
        BaseActivity.mContexts = this;
        this.sdCardDir = Constant.DST_FILE;
        Log.i("SpaceCurrentModeActivity", "SpaceCurrentModeActivity oncreat");
        this.context = this;
        this.path = getIntent().getStringExtra("path");
        this.id = getIntent().getIntExtra(LocaleUtil.INDONESIAN, 0);
        init();
    }

    private void init() {
        this.font = (Button) findViewById(C0136R.id.font);
        this.pic = (Button) findViewById(C0136R.id.pic);
        this.del = (Button) findViewById(C0136R.id.del);
        this.toright = (ImageView) findViewById(C0136R.id.toright);
        this.font.setOnClickListener(this);
        this.pic.setOnClickListener(this);
        this.del.setOnClickListener(this);
        this.toright.setOnClickListener(this);
        if (new File(this.path).exists()) {
            this.hlsx = this.jnitest.init();
            this.fileId = this.jnitest.openFile(this.path, this.hlsx);
            this.grp = this.jnitest.readGroupId(this.fileId);
            this.cols = this.jnitest.readGroupItemColCount(this.grp);
            this.namestrs = this.jnitest.readDsNames(this.grp, this.cols);
            this.unitstrs = this.jnitest.readDsunitstrs(this.grp, this.cols);
            this.item = this.jnitest.readDsDataFirstItemCount(this.grp);
            if (this.item > 0) {
                this.textStrs = this.jnitest.readDsDataFirstItemData(this.grp, this.cols, this.item);
                for (int i = 0; i < this.namestrs.length; i++) {
                    SptExDataStreamIdItem sSptStreamSelectIdItem = new SptExDataStreamIdItem();
                    sSptStreamSelectIdItem.setStreamTextIdContent(this.namestrs[i]);
                    sSptStreamSelectIdItem.setStreamState(this.unitstrs[i]);
                    sSptStreamSelectIdItem.setStreamStr(this.textStrs[i]);
                    this.streamLists.add(sSptStreamSelectIdItem);
                    isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
                }
            } else {
                Toast.makeText(this.context, C0136R.string.main_file_null, 0).show();
            }
            this.jnitest.readEndCloseFile(this.fileId, this.hlsx);
        } else {
            Toast.makeText(this.context, C0136R.string.main_file_null, 1).show();
        }
        this.listview = (ListView) findViewById(C0136R.id.listview);
        this.adapter = new SpaceDataStreamAdapter(this.streamLists, this.context);
        this.listview.setAdapter(this.adapter);
        this.listview.setOnItemClickListener(new C07221());
    }

    public void onClick(View v) {
        int i;
        Intent intent;
        if (v.getId() == C0136R.id.font) {
            if (isSelected.size() > 0) {
                this.selectedItem = new ArrayList();
                this.filterLists = new ArrayList();
                for (i = 0; i < isSelected.size(); i++) {
                    if (((Boolean) isSelected.get(Integer.valueOf(i))).booleanValue()) {
                        this.selectedItem.add(Integer.valueOf(i));
                        this.filterLists.add((SptExDataStreamIdItem) this.streamLists.get(i));
                    }
                }
                if (this.selectedItem.size() > 0) {
                    intent = new Intent(this, SpaceDataStreamActivity.class);
                    intent.putExtra("path", this.path);
                    intent.putExtra(LocaleUtil.INDONESIAN, this.id);
                    intent.putExtra("filterLists", this.filterLists);
                    intent.putExtra("selectedItem", (Serializable) this.selectedItem);
                    startActivity(intent);
                    return;
                }
                Toast.makeText(this.context, C0136R.string.pleaseselect, 1).show();
            }
        } else if (v.getId() == C0136R.id.pic) {
            if (isSelected.size() > 0) {
                this.selectedItem = new ArrayList();
                this.mselectedItem = new ArrayList();
                this.filterLists = new ArrayList();
                for (i = 0; i < isSelected.size(); i++) {
                    if (((Boolean) isSelected.get(Integer.valueOf(i))).booleanValue()) {
                        this.selectedItem.add(Integer.valueOf(i));
                        this.mselectedItem.add(Integer.valueOf(i));
                    }
                }
                if (this.selectedItem.size() > 0) {
                    this.selectedItem.clear();
                    i = 0;
                    while (i < isSelected.size()) {
                        if (((Boolean) isSelected.get(Integer.valueOf(i))).booleanValue() && ((SptExDataStreamIdItem) this.streamLists.get(i)).getStreamStr() != null && DataStreamUtils.isNum(((SptExDataStreamIdItem) this.streamLists.get(i)).getStreamStr())) {
                            this.selectedItem.add(Integer.valueOf(i));
                            this.filterLists.add((SptExDataStreamIdItem) this.streamLists.get(i));
                        }
                        i++;
                    }
                    if (this.selectedItem.size() > 4) {
                        Toast.makeText(this.context, C0136R.string.max_four, 1).show();
                        return;
                    } else if (isAllDigit(this.mselectedItem, this.selectedItem)) {
                        intent = new Intent((Activity) this.context, SpaceDataRecord.class);
                        SpaceInfoRecord info = new SpaceInfoRecord();
                        Bundle bundle = new Bundle();
                        info.setId(this.id);
                        info.setPath(this.path);
                        info.setFilterLists(this.filterLists);
                        info.setSeletedId(this.selectedItem);
                        bundle.putSerializable("info", info);
                        intent.putExtras(bundle);
                        ((Activity) this.context).startActivity(intent);
                        ((Activity) this.context).overridePendingTransition(0, 0);
                        return;
                    } else {
                        return;
                    }
                }
                Toast.makeText(this.context, C0136R.string.pleaseselect, 1).show();
            }
        } else if (v.getId() == C0136R.id.del) {
            new Builder(this.context).setTitle(C0136R.string.is_del).setPositiveButton(C0136R.string.enter, new C07232()).setNegativeButton(C0136R.string.cancel, null).show();
        } else if (v.getId() == C0136R.id.toright) {
            setResult(0);
            finish();
        }
    }

    public void deleteFolderFile(String filePath, boolean deleteThisPath) throws IOException {
        Log.i("SpaceCurrentModeActivity", "\u6267\u884c\u5220\u9664");
        int del = DBDao.getInstance(this.context).deleteReport(this.id, MainActivity.database);
        Log.i("SpaceCurrentModeActivity", "del" + del);
        if (del > 0) {
            Log.i("SpaceCurrentModeActivity", new StringBuilder(Msg.FIL_PAHT).append(filePath).toString());
            if (!TextUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File absolutePath : files) {
                        deleteFolderFile(absolutePath.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    Log.i("SpaceCurrentModeActivity", "\u5220\u9664\u6587\u4ef6");
                    if (!file.isDirectory()) {
                        file.delete();
                    } else if (file.listFiles().length == 0) {
                        file.delete();
                    }
                    Toast.makeText(this.context, C0136R.string.log_succcess, 0).show();
                }
            }
        }
    }

    private void showEditDialog() {
        View view = ((Activity) this.context).getLayoutInflater().inflate(C0136R.layout.diagnose, null);
        ((TextView) view.findViewById(C0136R.id.title)).setVisibility(8);
        ((TextView) view.findViewById(C0136R.id.context)).setVisibility(8);
        EditText infos = (EditText) view.findViewById(C0136R.id.info);
        Builder buidler = new Builder(this.context);
        buidler.setTitle(this.context.getResources().getText(C0136R.string.edit));
        buidler.setTitle(C0136R.string.rename);
        buidler.setView(view);
        buidler.setCancelable(false);
        buidler.setPositiveButton(this.context.getResources().getText(C0136R.string.sure), new C07243(infos));
        buidler.setNegativeButton(this.context.getResources().getText(C0136R.string.cancel), new C07254());
        buidler.show();
    }

    public boolean isAllDigit(List<Integer> selectedItem, List<Integer> mselectedItem) {
        if (selectedItem.size() == mselectedItem.size()) {
            return true;
        }
        String streamTextId = XmlPullParser.NO_NAMESPACE;
        String log = XmlPullParser.NO_NAMESPACE;
        for (int i = 0; i < selectedItem.size(); i++) {
            Integer j = (Integer) selectedItem.get(i);
            if (!mselectedItem.contains(j)) {
                log = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(log)).append("[").toString())).append(((SptExDataStreamIdItem) this.streamLists.get(j.intValue())).getStreamTextIdContent()).append("] ").toString();
            }
        }
        Toast.makeText(this.context, new StringBuilder(String.valueOf(log)).append(getResources().getString(C0136R.string.data_stream_item_select_show_info)).toString(), 0).show();
        return false;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.receiver, this.myIntentFilter);
    }
}
