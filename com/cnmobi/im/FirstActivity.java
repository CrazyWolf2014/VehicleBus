package com.cnmobi.im;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.ImConstants;
import org.xmlpull.v1.XmlPullParser;

public class FirstActivity extends Activity {
    private Button mGotoIm;
    private EditText mRiderNumber;

    /* renamed from: com.cnmobi.im.FirstActivity.1 */
    class C01611 implements OnClickListener {
        C01611() {
        }

        public void onClick(View v) {
            String riderNumberStr = FirstActivity.this.mRiderNumber.getText().toString().trim();
            if (XmlPullParser.NO_NAMESPACE.endsWith(riderNumberStr)) {
                Toast.makeText(FirstActivity.this, "\u8bf7\u8f93\u5165CC\u53f7", 0).show();
                return;
            }
            Intent intent = new Intent(FirstActivity.this, ImMainActivity.class);
            intent.putExtra(ImConstants.USER_ID, riderNumberStr);
            FirstActivity.this.startActivity(intent);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.im_first);
        this.mGotoIm = (Button) findViewById(C0136R.id.searchRiderBtn);
        this.mRiderNumber = (EditText) findViewById(C0136R.id.riderNumber);
        this.mGotoIm.setOnClickListener(new C01611());
    }
}
