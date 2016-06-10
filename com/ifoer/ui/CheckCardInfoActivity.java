package com.ifoer.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.entity.CardInfo;
import com.ifoer.util.MyApplication;
import com.ifoer.util.SimpleDialog;
import org.jivesoftware.smackx.Form;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;

public class CheckCardInfoActivity extends Activity {
    private Button btn_back;
    private TextView carNo;
    private TextView cardConsumeDate;
    private TextView cardName;
    private TextView cardRechargeYear;
    private TextView cardState;
    private LinearLayout chargeDateLay;
    private Context context;
    private long lastTime;
    private BroadcastReceiver mBroadcastReceiver;
    private TextView serialNo;
    private LinearLayout serialNoLay;
    private TextView softConfName;

    /* renamed from: com.ifoer.ui.CheckCardInfoActivity.1 */
    class C06881 extends BroadcastReceiver {
        C06881() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - CheckCardInfoActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                CheckCardInfoActivity.this.lastTime = System.currentTimeMillis();
            }
            if (!intent.getAction().equals("upgrade")) {
            }
        }
    }

    /* renamed from: com.ifoer.ui.CheckCardInfoActivity.2 */
    class C06892 implements OnClickListener {
        C06892() {
        }

        public void onClick(View arg0) {
            CheckCardInfoActivity.this.finish();
        }
    }

    public CheckCardInfoActivity() {
        this.mBroadcastReceiver = new C06881();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.card_info);
        MyApplication.getInstance().addActivity(this);
        CardInfo result = (CardInfo) getIntent().getSerializableExtra(Form.TYPE_RESULT);
        this.serialNoLay = (LinearLayout) findViewById(C0136R.id.serialLay);
        this.chargeDateLay = (LinearLayout) findViewById(C0136R.id.charge_date_lay);
        this.carNo = (TextView) findViewById(C0136R.id.card_no);
        this.cardName = (TextView) findViewById(C0136R.id.cardConfName);
        this.softConfName = (TextView) findViewById(C0136R.id.softConfName);
        this.cardRechargeYear = (TextView) findViewById(C0136R.id.cardRechargeYear);
        this.cardState = (TextView) findViewById(C0136R.id.card_state);
        this.cardConsumeDate = (TextView) findViewById(C0136R.id.cardConsumeDate);
        this.serialNo = (TextView) findViewById(C0136R.id.serailNo);
        this.context = this;
        this.carNo.setText(result.getCarNo());
        this.cardName.setText(result.getCardName());
        this.softConfName.setText(result.getSoftConfName());
        this.cardRechargeYear.setText(new StringBuilder(String.valueOf(result.getCardRechargeYear())).toString());
        if (result.getCardState() != 3) {
            this.serialNoLay.setVisibility(8);
            this.chargeDateLay.setVisibility(8);
            switch (result.getCardState()) {
                case KEYRecord.OWNER_USER /*0*/:
                    this.cardState.setText(getString(C0136R.string.card_unregisted));
                    break;
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    this.cardState.setText(getString(C0136R.string.card_registed));
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    this.cardState.setText(getString(C0136R.string.card_actived));
                    break;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    this.cardState.setText(getString(C0136R.string.card_Obsolete));
                    break;
                default:
                    break;
            }
        }
        this.cardConsumeDate.setText(result.getCardConsumeDate());
        this.serialNo.setText(result.getSerialNo());
        this.cardState.setText(getString(C0136R.string.card_charged));
        this.btn_back = (Button) findViewById(C0136R.id.returnBtn);
        this.btn_back.setOnClickListener(new C06892());
        registerBoradcastReceiver();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        this.context.registerReceiver(this.mBroadcastReceiver, myIntentFilter);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mBroadcastReceiver != null) {
            unregisterReceiver(this.mBroadcastReceiver);
        }
    }
}
