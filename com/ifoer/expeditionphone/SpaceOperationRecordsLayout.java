package com.ifoer.expeditionphone;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.SpaceOperationRecordsAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.entity.OperatingRecordInfo;
import com.ifoer.util.MySharedPreferences;
import java.util.List;

public class SpaceOperationRecordsLayout extends MySpaceManagermentLayout {
    private SpaceOperationRecordsAdapter adapter;
    private View baseView;
    Context context;
    private List<OperatingRecordInfo> lists;
    private ListView listview;

    public SpaceOperationRecordsLayout(Context context) {
        super(context);
        this.context = context;
        this.baseView = ((Activity) context).getLayoutInflater().inflate(C0136R.layout.space_operation_records, this);
        initTopView(this.baseView);
        setTopView(context, 1);
        init();
    }

    private void init() {
        this.lists = DBDao.getInstance(this.context).getOperatingRecord(MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey), MainActivity.database);
        this.listview = (ListView) this.baseView.findViewById(C0136R.id.listview);
        this.adapter = new SpaceOperationRecordsAdapter(this.lists, this.context);
        this.listview.setAdapter(this.adapter);
    }
}
