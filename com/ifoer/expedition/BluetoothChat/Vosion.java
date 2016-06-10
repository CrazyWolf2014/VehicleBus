package com.ifoer.expedition.BluetoothChat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.ShowFileListAdapter;
import com.ifoer.util.Copy_File;
import com.ifoer.util.Files;
import com.ifoer.util.MyApplication;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class Vosion extends Activity {
    private static final boolean f1285D = true;
    private static final String FROMPATH;
    private Context context;
    private String dataDir;
    private List<String> list;
    private ListView listview;
    private String path;
    private String paths;
    private String sdCardDir;
    private String sdPaths;

    /* renamed from: com.ifoer.expedition.BluetoothChat.Vosion.1 */
    class C04931 implements OnItemClickListener {
        C04931() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            String[] paths = Vosion.this.path.split(FilePathGenerator.ANDROID_DIR_SEP);
            Vosion.this.loadSo((String) Vosion.this.list.get(arg2), paths[paths.length - 1] + FilePathGenerator.ANDROID_DIR_SEP + ((String) Vosion.this.list.get(arg2)) + FilePathGenerator.ANDROID_DIR_SEP);
        }
    }

    class PublicCircleAsy extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        PublicCircleAsy() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(Vosion.this.context);
            this.progressDialog.setMessage(Vosion.this.getResources().getText(C0136R.string.initializeMessage));
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        protected String doInBackground(String... params) {
            if (Copy_File.copy(Vosion.this.sdPaths, Vosion.this.paths) == 0) {
                Copy_File.list = new ArrayList();
                Copy_File.findAllSoFile(Vosion.this.paths);
                if (Copy_File.list.size() > 0) {
                    return null;
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            this.progressDialog.cancel();
            if (Copy_File.list.size() > 0) {
                Intent menu = new Intent(Vosion.this, CarDiagnoseActivity.class);
                menu.putStringArrayListExtra("FileList", Copy_File.list);
                menu.putExtra("paths", Vosion.this.sdPaths);
                menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                Vosion.this.startActivity(menu);
                Vosion.this.overridePendingTransition(0, 0);
                Vosion.this.finish();
                Vosion.this.overridePendingTransition(0, 0);
            }
        }
    }

    public Vosion() {
        this.list = new ArrayList();
        this.path = XmlPullParser.NO_NAMESPACE;
        this.dataDir = XmlPullParser.NO_NAMESPACE;
        this.paths = XmlPullParser.NO_NAMESPACE;
        this.sdPaths = XmlPullParser.NO_NAMESPACE;
    }

    static {
        FROMPATH = Environment.getExternalStorageDirectory() + "/cnlaunch/DIAGNOSTIC/VEHICLES/";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        requestWindowFeature(5);
        setContentView(C0136R.layout.vosion);
        MyApplication.getInstance().addActivity(this);
        init();
        try {
            this.dataDir = getPackageManager().getApplicationInfo(getPackageName(), 0).dataDir;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        Intent intent = getIntent();
        if (intent != null) {
            this.path = intent.getStringExtra("path");
        }
        this.listview = (ListView) findViewById(C0136R.id.view);
        this.list = Files.getFilePathFromSD(this.path);
        this.listview.setAdapter(new ShowFileListAdapter(this.list, this));
        this.listview.setOnItemClickListener(new C04931());
    }

    public void loadSo(String dataDirs, String sdPath) {
        this.paths = this.dataDir + "/libs/cnlaunch/DIAGNOSTIC/VEHICLES/" + dataDirs + FilePathGenerator.ANDROID_DIR_SEP;
        this.sdPaths = FROMPATH + sdPath;
        new PublicCircleAsy().execute(new String[0]);
    }
}
