package com.ifoer.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.SpaceOperationRecordsAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.entity.OperatingRecordInfo;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.MySharedPreferences;
import java.util.List;

public class SpaceOperationRecordsActivity extends Activity {
    private SpaceOperationRecordsAdapter adapter;
    Context context;
    private List<OperatingRecordInfo> lists;
    private ListView listview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.space_operation_records1);
        this.context = this;
        init();
    }

    private void init() {
        this.lists = DBDao.getInstance(this.context).getOperatingRecord(MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey), MainActivity.database);
        this.listview = (ListView) findViewById(C0136R.id.listview);
        this.adapter = new SpaceOperationRecordsAdapter(this.lists, this.context);
        this.listview.setAdapter(this.adapter);
    }
}
