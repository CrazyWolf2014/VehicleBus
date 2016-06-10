package com.cnmobi.im.folder;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.ChatActivity;
import com.cnmobi.im.dto.MessageVo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class FolderActivity extends ListActivity {
    private String curPath;
    private List<String> items;
    private TextView mPath;
    private List<String> paths;
    private String rootPath;

    public FolderActivity() {
        this.items = null;
        this.paths = null;
        this.rootPath = getSDDir();
        this.curPath = getSDDir();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.fileselect);
        this.mPath = (TextView) findViewById(C0136R.id.mPath);
        getFileDir(this.rootPath);
    }

    private void getFileDir(String filePath) {
        this.mPath.setText(filePath);
        this.items = new ArrayList();
        this.paths = new ArrayList();
        File f = new File(filePath);
        File[] files = f.listFiles();
        if (!filePath.equals(this.rootPath)) {
            this.items.add("b1");
            this.paths.add(this.rootPath);
            this.items.add("b2");
            this.paths.add(f.getParent());
        }
        for (File file : files) {
            this.items.add(file.getName());
            this.paths.add(file.getPath());
        }
        setListAdapter(new FolderAdapter(this, this.items, this.paths));
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File((String) this.paths.get(position));
        if (file.isDirectory()) {
            this.curPath = (String) this.paths.get(position);
            getFileDir((String) this.paths.get(position));
            return;
        }
        Intent data = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MessageVo.TYPE_FILE, file.getPath());
        data.putExtras(bundle);
        setResult(-1, data);
        finish();
    }

    public boolean checkShapeFile(File file) {
        String fileNameString = file.getName();
        String endNameString = fileNameString.substring(fileNameString.lastIndexOf(".") + 1, fileNameString.length()).toLowerCase();
        if (fileNameString.lastIndexOf(".") == -1 || endNameString.equals("txt")) {
            return true;
        }
        return false;
    }

    protected final String getSDDir() {
        if (checkSDcard()) {
            try {
                return Environment.getExternalStorageDirectory().toString();
            } catch (Exception e) {
                return XmlPullParser.NO_NAMESPACE;
            }
        }
        Toast.makeText(this, "no sdcard", 0).show();
        return XmlPullParser.NO_NAMESPACE;
    }

    public boolean checkSDcard() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }
}
