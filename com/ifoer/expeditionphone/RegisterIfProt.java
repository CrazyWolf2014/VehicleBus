package com.ifoer.expeditionphone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.cnlaunch.x431frame.C0136R;

public class RegisterIfProt extends Activity implements OnClickListener {
    private ImageView close;
    private Button noReg;
    private Button regsPort;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.is_register_head);
        initView();
    }

    public void initView() {
        this.regsPort = (Button) findViewById(C0136R.id.regs);
        this.noReg = (Button) findViewById(C0136R.id.noRgs);
        this.close = (ImageView) findViewById(C0136R.id.repu_close);
        this.close.setOnClickListener(this);
        this.regsPort.setOnClickListener(this);
        this.noReg.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.repu_close) {
            finish();
        } else if (v.getId() == C0136R.id.regs) {
            startActivity(new Intent(this, RegisterProt.class));
            finish();
        } else if (v.getId() == C0136R.id.noRgs) {
            finish();
        }
    }
}
