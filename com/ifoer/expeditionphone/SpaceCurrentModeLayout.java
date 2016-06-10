package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.mycar.jni.JniX431FileTest;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.SpaceDataStreamAdapter;
import com.ifoer.adapter.SpaceDataStreamAdapter.Item;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SpaceInfoRecord;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.expedition.BluetoothChat.DataStreamUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceCurrentModeLayout extends MySpaceManagermentLayout implements OnClickListener {
    public static Map<Integer, Boolean> isSelected;
    private SpaceDataStreamAdapter adapter;
    private View baseView;
    private int cols;
    Context context;
    private LinearLayout del;
    private LinearLayout edit;
    private int fileId;
    private ArrayList<SptExDataStreamIdItem> filterLists;
    private LinearLayout font;
    private int grp;
    private int hlsx;
    private int id;
    private int item;
    private JniX431FileTest jnitest;
    private ListView listview;
    private String name;
    private String[] namestrs;
    private String path;
    private LinearLayout pic;
    private String sdCardDir;
    private List<Integer> selectedItem;
    private ArrayList<SptExDataStreamIdItem> streamLists;
    private String[] textStrs;
    private ImageView toright;
    private String[] unitstrs;

    /* renamed from: com.ifoer.expeditionphone.SpaceCurrentModeLayout.1 */
    class C06361 implements OnItemClickListener {
        C06361() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            Item item = (Item) arg1.getTag();
            item.checkBox.toggle();
            SpaceCurrentModeLayout.isSelected.put(Integer.valueOf(arg2), Boolean.valueOf(item.checkBox.isChecked()));
        }
    }

    /* renamed from: com.ifoer.expeditionphone.SpaceCurrentModeLayout.2 */
    class C06372 implements DialogInterface.OnClickListener {
        C06372() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (DBDao.getInstance(SpaceCurrentModeLayout.this.context).deleteReport(SpaceCurrentModeLayout.this.id, MainActivity.database) > 0) {
                Toast.makeText(SpaceCurrentModeLayout.this.context, C0136R.string.log_succcess, 0).show();
            }
            try {
                SpaceCurrentModeLayout.this.deleteFolderFile(SpaceCurrentModeLayout.this.path, false);
                MainActivity.panel.removePanelContainer();
                MainActivity.panel.fillPanelContainer(new SpaceDiagnosticReportLayout(SpaceCurrentModeLayout.this.context));
                MainActivity.panel.openthreePanelContainer();
                dialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.SpaceCurrentModeLayout.3 */
    class C06383 implements DialogInterface.OnClickListener {
        private final /* synthetic */ EditText val$infos;

        C06383(EditText editText) {
            this.val$infos = editText;
        }

        public void onClick(DialogInterface dialog, int which) {
            File file = new File(SpaceCurrentModeLayout.this.path);
            SpaceCurrentModeLayout.this.name = this.val$infos.getText().toString();
            String newPath = new StringBuilder(String.valueOf(SpaceCurrentModeLayout.this.sdCardDir)).append(SpaceCurrentModeLayout.this.name).append(".x431").toString();
            if (new File(newPath).exists()) {
                Toast.makeText(SpaceCurrentModeLayout.this.context, C0136R.string.renaem_error, 0).show();
            } else if (SpaceCurrentModeLayout.this.name.length() < 21) {
                System.out.println(newPath.toString());
                file.renameTo(new File(newPath));
                file.delete();
                dialog.dismiss();
                if (DBDao.getInstance(SpaceCurrentModeLayout.this.context).UpdateReport(new StringBuilder(String.valueOf(SpaceCurrentModeLayout.this.id)).toString(), new StringBuilder(String.valueOf(SpaceCurrentModeLayout.this.name)).append(".x431").toString(), newPath, MainActivity.database) > 0) {
                    Toast.makeText(SpaceCurrentModeLayout.this.context, C0136R.string.log_succcess, 0).show();
                }
            } else {
                Toast.makeText(SpaceCurrentModeLayout.this.context, C0136R.string.out_of_Length, 0).show();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.SpaceCurrentModeLayout.4 */
    class C06394 implements DialogInterface.OnClickListener {
        C06394() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    static {
        isSelected = new HashMap();
    }

    public SpaceCurrentModeLayout(int id, String path, Context context) {
        super(context);
        this.streamLists = new ArrayList();
        this.sdCardDir = Constant.DST_FILE;
        this.name = null;
        this.jnitest = new JniX431FileTest();
        this.context = context;
        this.path = path;
        this.id = id;
        this.baseView = ((Activity) context).getLayoutInflater().inflate(C0136R.layout.space_data_flow, this);
        initTopView(this.baseView);
        setTopView(context, 0);
        init();
    }

    private void init() {
        this.font = (LinearLayout) this.baseView.findViewById(C0136R.id.font);
        this.pic = (LinearLayout) this.baseView.findViewById(C0136R.id.pic);
        this.edit = (LinearLayout) this.baseView.findViewById(C0136R.id.edit);
        this.del = (LinearLayout) this.baseView.findViewById(C0136R.id.del);
        this.toright = (ImageView) this.baseView.findViewById(C0136R.id.toright);
        this.font.setOnClickListener(this);
        this.pic.setOnClickListener(this);
        this.edit.setOnClickListener(this);
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
        this.listview = (ListView) this.baseView.findViewById(C0136R.id.listview);
        this.adapter = new SpaceDataStreamAdapter(this.streamLists, this.context);
        this.listview.setAdapter(this.adapter);
        this.listview.setOnItemClickListener(new C06361());
    }

    public void onClick(View v) {
        int i;
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
                    MainActivity.panel.removePanelContainer();
                    MainActivity.panel.fillPanelContainer(new SpaceDataStreamLayout(this.filterLists, this.selectedItem, this.id, this.path, this.context));
                    MainActivity.panel.openthreePanelContainer();
                    return;
                }
                Toast.makeText(this.context, C0136R.string.pleaseselect, 1).show();
            }
        } else if (v.getId() == C0136R.id.pic) {
            if (isSelected.size() > 0) {
                this.selectedItem = new ArrayList();
                this.filterLists = new ArrayList();
                for (i = 0; i < isSelected.size(); i++) {
                    if (((Boolean) isSelected.get(Integer.valueOf(i))).booleanValue()) {
                        this.selectedItem.add(Integer.valueOf(i));
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
                    if (this.selectedItem.size() <= 4) {
                        Intent intent = new Intent((Activity) this.context, SpaceDataRecord.class);
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
                    }
                    Toast.makeText(this.context, C0136R.string.max_four, 1).show();
                    return;
                }
                Toast.makeText(this.context, C0136R.string.pleaseselect, 1).show();
            }
        } else if (v.getId() == C0136R.id.del) {
            new Builder(this.context).setTitle(C0136R.string.is_del).setPositiveButton(C0136R.string.enter, new C06372()).setNegativeButton(C0136R.string.cancel, null).show();
        } else if (v.getId() == C0136R.id.toright) {
            MainActivity.panel.removePanelContainer();
            MainActivity.panel.fillPanelContainer(new SpaceDiagnosticReportLayout(this.context));
            MainActivity.panel.openthreePanelContainer();
        }
    }

    public void deleteFolderFile(String filePath, boolean deleteThisPath) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File absolutePath : files) {
                    deleteFolderFile(absolutePath.getAbsolutePath(), true);
                }
            }
            if (!deleteThisPath) {
                return;
            }
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.listFiles().length == 0) {
                file.delete();
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
        buidler.setPositiveButton(this.context.getResources().getText(C0136R.string.sure), new C06383(infos));
        buidler.setNegativeButton(this.context.getResources().getText(C0136R.string.cancel), new C06394());
        buidler.show();
    }
}
