package com.ifoer.adapter;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.db.DBDao;
import com.ifoer.dbentity.CarVersionInfo;
import com.ifoer.expeditionphone.LoadDynamicLibsActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.MySharedPreferences;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.File;
import java.util.ArrayList;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;

public class LoadDynamicAdapter extends BaseAdapter {
    private LoadDynamicLibsActivity activity;
    private String carId;
    private Context context;
    public ArrayList<CarVersionInfo> data;
    private Handler handler;
    private LayoutInflater inflater;
    private String lanName;

    /* renamed from: com.ifoer.adapter.LoadDynamicAdapter.1 */
    class C03401 implements OnClickListener {
        private final /* synthetic */ int val$arg0;

        /* renamed from: com.ifoer.adapter.LoadDynamicAdapter.1.1 */
        class C03391 implements DialogInterface.OnClickListener {
            private final /* synthetic */ int val$arg0;

            C03391(int i) {
                this.val$arg0 = i;
            }

            public void onClick(DialogInterface dialog, int which) {
                String serialNo = MySharedPreferences.getStringValue(LoadDynamicAdapter.this.context, MySharedPreferences.serialNoKey);
                boolean isSuccess = LoadDynamicAdapter.this.deleteDynamicFile(((CarVersionInfo) LoadDynamicAdapter.this.data.get(this.val$arg0)).getVersionDir());
                int deleteResult = DBDao.getInstance(LoadDynamicAdapter.this.context).deleteDynamicLibrary(serialNo, ((CarVersionInfo) LoadDynamicAdapter.this.data.get(this.val$arg0)).getCarId(), ((CarVersionInfo) LoadDynamicAdapter.this.data.get(this.val$arg0)).getVersionNo(), MainActivity.database);
                int deleteResult2 = DBDao.getInstance(LoadDynamicAdapter.this.context).deleteUpgradeVersionInfo(serialNo, ((CarVersionInfo) LoadDynamicAdapter.this.data.get(this.val$arg0)).getCarId(), ((CarVersionInfo) LoadDynamicAdapter.this.data.get(this.val$arg0)).getVersionNo(), MainActivity.database);
                if (deleteResult > 0) {
                    LoadDynamicAdapter.this.handler.obtainMessage(4).sendToTarget();
                    LoadDynamicAdapter.this.activity.delVersion = ((CarVersionInfo) LoadDynamicAdapter.this.data.get(this.val$arg0)).getVersionNo();
                    if (LoadDynamicAdapter.this.data != null) {
                        LoadDynamicAdapter.this.data.clear();
                    }
                    LoadDynamicAdapter.this.data = DBDao.getInstance(LoadDynamicAdapter.this.context).queryCarVersion(LoadDynamicAdapter.this.carId, LoadDynamicAdapter.this.lanName, serialNo, MainActivity.database);
                    Log.i("LoadDynamicAdapter", new StringBuilder(DataPacketExtension.ELEMENT_NAME).append(LoadDynamicAdapter.this.data.size()).toString());
                    LoadDynamicAdapter.this.notifyDataSetChanged();
                } else {
                    LoadDynamicAdapter.this.handler.obtainMessage(5).sendToTarget();
                }
                LoadDynamicAdapter.this.context.sendBroadcast(new Intent("refreshUi"));
            }
        }

        C03401(int i) {
            this.val$arg0 = i;
        }

        public void onClick(View v) {
            new Builder(LoadDynamicAdapter.this.context).setTitle(C0136R.string.is_del).setPositiveButton(C0136R.string.enter, new C03391(this.val$arg0)).setNegativeButton(C0136R.string.cancel, null).show();
        }
    }

    class ViewHolder {
        TextView carName;
        Button delete;
        TextView version;

        ViewHolder() {
        }
    }

    public LoadDynamicAdapter(ArrayList<CarVersionInfo> data, Context context, Handler handler, String carId, String lanName) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.handler = handler;
        this.carId = carId;
        this.lanName = lanName;
    }

    public int getCount() {
        if (this.data.size() > 0) {
            return this.data.size();
        }
        return 0;
    }

    public Object getItem(int arg0) {
        return this.data.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int arg0, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;
        if (null == null) {
            viewHolder = new ViewHolder();
            convertView = this.inflater.inflate(C0136R.layout.load_dynamic_libs_item, null, false);
            viewHolder.carName = (TextView) convertView.findViewById(C0136R.id.carName);
            viewHolder.version = (TextView) convertView.findViewById(C0136R.id.version);
            viewHolder.delete = (Button) convertView.findViewById(C0136R.id.delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String carName = ((CarVersionInfo) this.data.get(arg0)).getCarId();
        if (carName.equals("EOBD2")) {
            carName = "EOBD";
        }
        if (arg0 == 0) {
            viewHolder.delete.setVisibility(4);
        } else {
            viewHolder.delete.setVisibility(0);
        }
        viewHolder.carName.setText(carName);
        viewHolder.version.setText(((CarVersionInfo) this.data.get(arg0)).getVersionNo());
        viewHolder.delete.setOnClickListener(new C03401(arg0));
        return convertView;
    }

    private boolean deleteDynamicFile(String filePath) {
        deleteAllFile(filePath);
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    private void deleteAllFile(String path) {
        File file = new File(new StringBuilder(String.valueOf(path)).append(FilePathGenerator.ANDROID_DIR_SEP).toString());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (File eachFile : files) {
                    if (eachFile.exists()) {
                        eachFile.delete();
                    }
                }
            }
        }
    }

    public void setLoadDynamicLibsActivity(LoadDynamicLibsActivity activity) {
        this.activity = activity;
    }

    public ArrayList<CarVersionInfo> getDataList() {
        return this.data;
    }
}
