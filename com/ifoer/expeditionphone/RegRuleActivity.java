package com.ifoer.expeditionphone;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.googlecode.leptonica.android.Skew;
import com.ifoer.util.DialogUtil;
import com.ifoer.util.MyApplication;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.HTTP;
import org.xmlpull.v1.XmlPullParser;

public class RegRuleActivity extends Activity implements OnClickListener {
    private FrameLayout frameLayout;
    private Handler handler;
    private ImageView imageBack;
    private boolean isAgree;
    private boolean isShow;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private String sContent;
    private String sTitle;
    private TextView textView;
    private TextView tvAgree;
    private TextView tvNext;
    private TextView tvTitle;

    /* renamed from: com.ifoer.expeditionphone.RegRuleActivity.1 */
    class C06221 extends Handler {
        C06221() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    RegRuleActivity.this.tvTitle.setText(RegRuleActivity.this.sTitle);
                    RegRuleActivity.this.textView.setText(RegRuleActivity.this.sContent);
                default:
            }
        }
    }

    public RegRuleActivity() {
        this.sTitle = XmlPullParser.NO_NAMESPACE;
        this.sContent = HTTP.CRLF;
        this.isAgree = false;
        this.isShow = false;
        this.handler = new C06221();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.register_rule_ja);
        MyApplication.getInstance().addActivity(this);
        DialogUtil.setDialogSize(this);
        initView();
    }

    private void initView() {
        if (getIntent().getExtras() != null) {
            this.isShow = getIntent().getExtras().getBoolean("isShow", false);
        }
        this.textView = (TextView) findViewById(C0136R.id.content_textView);
        this.tvTitle = (TextView) findViewById(C0136R.id.title_textView);
        this.textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.tvAgree = (TextView) findViewById(C0136R.id.rule_agree);
        this.tvNext = (TextView) findViewById(C0136R.id.rule_next);
        this.imageBack = (ImageView) findViewById(C0136R.id.rule_back);
        this.frameLayout = (FrameLayout) findViewById(C0136R.id.layout_rule_frame);
        this.relativeLayout = (RelativeLayout) findViewById(C0136R.id.layout_rule_relative);
        this.linearLayout = (LinearLayout) findViewById(C0136R.id.layout_rule_linear);
        LayoutParams params = new LayoutParams(-1, -1);
        if (!this.isShow) {
            this.linearLayout.setVisibility(8);
            params.bottomMargin = px2dip(Skew.SWEEP_DELTA);
            this.frameLayout.removeAllViews();
            this.frameLayout.addView(this.relativeLayout, params);
        }
        getFromAssets("ccc.txt");
        this.tvAgree.setOnClickListener(this);
        this.tvNext.setOnClickListener(this);
        this.imageBack.setOnClickListener(this);
    }

    public String getFromAssets(String fileName) {
        try {
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(getResources().getAssets().open(fileName), AsyncHttpResponseHandler.DEFAULT_CHARSET));
            String line = XmlPullParser.NO_NAMESPACE;
            String Result = XmlPullParser.NO_NAMESPACE;
            int i = 0;
            while (true) {
                line = bufReader.readLine();
                if (line == null) {
                    this.handler.sendEmptyMessage(1);
                    return Result;
                } else if (i < 2) {
                    this.sTitle += line;
                    i++;
                } else {
                    this.sContent += line + HTTP.CRLF;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return XmlPullParser.NO_NAMESPACE;
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DialogUtil.setDialogSize(this);
    }

    public void onClick(View v) {
        if (v == this.tvAgree) {
            if (this.isAgree) {
                this.isAgree = false;
                this.tvAgree.setBackgroundResource(C0136R.drawable.rule_disagree);
                this.tvNext.setBackgroundResource(C0136R.drawable.rule_disagree_next);
                return;
            }
            this.isAgree = true;
            this.tvAgree.setBackgroundResource(C0136R.drawable.rule_agree);
            this.tvNext.setBackgroundResource(C0136R.drawable.rule_agree_next);
        } else if (v == this.tvNext) {
            if (this.isAgree) {
                startActivityForResult(new Intent(this, ComRegActivity.class), 11);
                this.tvNext.setClickable(false);
                finish();
                overridePendingTransition(0, 0);
            }
        } else if (v == this.imageBack) {
            finish();
            overridePendingTransition(0, 0);
        }
    }

    public int px2dip(float pxValue) {
        return (int) ((pxValue / getResources().getDisplayMetrics().density) + 0.5f);
    }
}
