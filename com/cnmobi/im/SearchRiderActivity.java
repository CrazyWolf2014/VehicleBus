package com.cnmobi.im;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.LogoManager;
import com.cnmobi.im.bo.LogoManager.LogoBackListener;
import com.cnmobi.im.dto.RiderVo;
import com.cnmobi.im.util.UiUtils;
import com.cnmobi.im.util.XmppConnection;
import com.ifoer.entity.Constant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.xmlpull.v1.XmlPullParser;

public class SearchRiderActivity extends Activity implements LogoBackListener {
    private static final int RIDER_LOGO_BACK = 102;
    private static final int SEARCH_RIDER_RESULT_BACK = 100;
    private static final String TAG = "SearchRiderActivity";
    private RiderItemAdapter mAdapter;
    private Handler mHandler;
    private ListView mRiderList;
    private EditText mRiderNumber;
    private Button mSearchRiderBtn;
    private TextView mTopBarTitle;

    /* renamed from: com.cnmobi.im.SearchRiderActivity.1 */
    class C01871 extends Handler {
        C01871() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SearchRiderActivity.SEARCH_RIDER_RESULT_BACK /*100*/:
                    List<RiderVo> riders = msg.obj;
                    SearchRiderActivity.this.mAdapter.resetRiders(riders);
                    SearchRiderActivity.this.mAdapter.notifyDataSetChanged();
                    if (riders.size() == 0) {
                        Toast.makeText(SearchRiderActivity.this, SearchRiderActivity.this.getResources().getString(C0136R.string.searchRiderNullTips), 0).show();
                    }
                case SearchRiderActivity.RIDER_LOGO_BACK /*102*/:
                    LogoHolder holder = msg.obj;
                    ImageView iv = SearchRiderActivity.this.mAdapter.getLogoView(holder.jid);
                    if (iv == null) {
                        return;
                    }
                    if (holder.bitmap != null) {
                        iv.setImageBitmap(holder.bitmap);
                    } else {
                        iv.setImageResource(C0136R.drawable.default_logo);
                    }
                default:
            }
        }
    }

    /* renamed from: com.cnmobi.im.SearchRiderActivity.2 */
    class C01882 implements OnClickListener {
        C01882() {
        }

        public void onClick(View v) {
            String riderNumberStr = SearchRiderActivity.this.mRiderNumber.getText().toString().trim();
            if (XmlPullParser.NO_NAMESPACE.endsWith(riderNumberStr)) {
                Toast.makeText(SearchRiderActivity.this, SearchRiderActivity.this.getString(C0136R.string.pleaseInputCCNumber), 0).show();
                return;
            }
            InputMethodManager imm = (InputMethodManager) SearchRiderActivity.this.getSystemService("input_method");
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(SearchRiderActivity.this.mRiderNumber.getWindowToken(), 0);
            }
            new SearchRiderThread(riderNumberStr, null).start();
        }
    }

    /* renamed from: com.cnmobi.im.SearchRiderActivity.3 */
    class C01893 implements OnItemClickListener {
        C01893() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long arg3) {
            String riderName = (String) view.getTag(C0136R.id.riderName);
            String jid = (String) view.getTag(C0136R.id.JID);
            Intent intent = new Intent(SearchRiderActivity.this, RiderDetailActivity.class);
            intent.putExtra("RiderName", riderName);
            intent.putExtra("jid", jid);
            intent.putExtra(Constant.ACTION, 1);
            SearchRiderActivity.this.startActivity(intent);
        }
    }

    class LogoHolder {
        Bitmap bitmap;
        String jid;

        LogoHolder() {
        }
    }

    class SearchRiderThread extends Thread {
        private String key;

        private SearchRiderThread(String key) {
            this.key = key;
        }

        public void run() {
            XMPPConnection connection = XmppConnection.getConnection();
            UserSearchManager search = new UserSearchManager(connection);
            List<RiderVo> riders = new ArrayList();
            try {
                Form answerForm = search.getSearchForm("search." + connection.getServiceName()).createAnswerForm();
                answerForm.setAnswer("Username", true);
                answerForm.setAnswer("search", this.key);
                Iterator<Row> it = search.getSearchResults(answerForm, "search." + connection.getServiceName()).getRows();
                while (it.hasNext()) {
                    Row row = (Row) it.next();
                    RiderVo rider = new RiderVo();
                    String riderName = row.getValues("Username").next().toString();
                    rider.name = riderName;
                    rider.jId = new StringBuilder(String.valueOf(riderName)).append(XmppConnection.JID_SEPARATOR).append(connection.getServiceName()).toString();
                    Log.d(SearchRiderActivity.TAG, "User Jid : " + rider.jId);
                    riders.add(rider);
                }
                Message msg = SearchRiderActivity.this.mHandler.obtainMessage();
                msg.what = SearchRiderActivity.SEARCH_RIDER_RESULT_BACK;
                msg.obj = riders;
                SearchRiderActivity.this.mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SearchRiderActivity() {
        this.mHandler = new C01871();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.im_search_rider);
        init();
        LogoManager.getInstance().add(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        LogoManager.getInstance().remove(this);
    }

    private void init() {
        this.mTopBarTitle = (TextView) findViewById(C0136R.id.topBarTitle);
        this.mTopBarTitle.setText(getResources().getString(C0136R.string.searchRider));
        UiUtils.enabledBackButton(this);
        this.mRiderNumber = (EditText) findViewById(C0136R.id.riderNumber);
        this.mSearchRiderBtn = (Button) findViewById(C0136R.id.searchRiderBtn);
        this.mSearchRiderBtn.setOnClickListener(new C01882());
        this.mRiderList = (ListView) findViewById(C0136R.id.friendList);
        this.mAdapter = new RiderItemAdapter(this, false, false);
        this.mRiderList.setAdapter(this.mAdapter);
        this.mRiderList.setOnItemClickListener(new C01893());
    }

    public void logoback(String jid, Bitmap bitmap) {
        LogoHolder holder = new LogoHolder();
        holder.jid = jid;
        holder.bitmap = bitmap;
        Message msg = this.mHandler.obtainMessage();
        msg.what = RIDER_LOGO_BACK;
        msg.obj = holder;
        this.mHandler.sendMessage(msg);
    }
}
