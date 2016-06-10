package com.ifoer.expeditionphone;

import android.annotation.SuppressLint;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.SpaceInfoRecord;
import com.ifoer.util.SimpleDialog;

public class SpaceDataRecord extends FragmentActivity implements OnClickListener {
    private Context context;
    private SpaceInfoRecord info;
    private long lastTime;
    private Button mBackSuperior;
    private FrameLayout mBoday;
    private Button mCombinButton;
    private Button mMostButton;
    public int mTimes;
    private FragmentManager manager;
    LocalActivityManager mlocalActivityManager;
    private Fragment moreChart;
    public IntentFilter myIntentFilter;
    public mBroadcastReceiver receiver;
    private Fragment singleChart;

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        @SuppressLint({"ShowToast"})
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - SpaceDataRecord.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                SpaceDataRecord.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    public SpaceDataRecord() {
        this.mTimes = 0;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        this.context = this;
        this.moreChart = new SpaceMoreRecordShow();
        this.singleChart = new SpaceSingleRecordShow();
        this.info = (SpaceInfoRecord) getIntent().getSerializableExtra("info");
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", this.info);
        this.moreChart.setArguments(bundle);
        this.singleChart.setArguments(bundle);
        this.manager = getSupportFragmentManager();
        FragmentTransaction tran = this.manager.beginTransaction();
        tran.replace(C0136R.id.frame, this.singleChart);
        tran.add(C0136R.id.frame, this.moreChart);
        tran.hide(this.singleChart);
        tran.commit();
        setRequestedOrientation(0);
        setContentView(C0136R.layout.space_record);
        initView();
    }

    public void initView() {
        this.mBoday = (FrameLayout) findViewById(C0136R.id.frame);
        this.mMostButton = (Button) findViewById(C0136R.id.MostActivity);
        this.mCombinButton = (Button) findViewById(C0136R.id.CombinActivity);
        this.mMostButton.setSelected(true);
        this.mBackSuperior = (Button) findViewById(C0136R.id.backSuperior1);
        this.mBackSuperior.setVisibility(0);
        this.mMostButton.setOnClickListener(this);
        this.mCombinButton.setOnClickListener(this);
        this.mBackSuperior.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.MostActivity) {
            this.mMostButton.setEnabled(false);
            this.mCombinButton.setEnabled(true);
            FragmentTransaction tran = this.manager.beginTransaction();
            tran.hide(this.singleChart);
            tran.show(this.moreChart);
            tran.commit();
            this.mMostButton.setSelected(true);
            this.mCombinButton.setSelected(false);
        } else if (v.getId() == C0136R.id.CombinActivity) {
            this.mMostButton.setEnabled(true);
            this.mCombinButton.setEnabled(false);
            FragmentTransaction tran2 = this.manager.beginTransaction();
            tran2.hide(this.moreChart);
            tran2.show(this.singleChart);
            tran2.commit();
            this.mMostButton.setSelected(false);
            this.mCombinButton.setSelected(true);
        } else if (v.getId() == C0136R.id.backSuperior1) {
            finish();
        }
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
