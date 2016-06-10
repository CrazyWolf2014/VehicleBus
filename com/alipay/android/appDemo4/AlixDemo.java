package com.alipay.android.appDemo4;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alipay.android.appDemo4.Products.ProductDetail;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import org.xmlpull.v1.XmlPullParser;

public class AlixDemo extends Activity implements OnItemClickListener, OnItemLongClickListener {
    static String TAG;
    private Handler mHandler;
    private ProgressDialog mProgress;
    ProductListAdapter m_listViewAdapter;
    ListView mproductListView;
    ArrayList<ProductDetail> mproductlist;

    /* renamed from: com.alipay.android.appDemo4.AlixDemo.1 */
    class C00771 extends Handler {
        C00771() {
        }

        public void handleMessage(Message msg) {
            try {
                String strRet = msg.obj;
                Log.e(AlixDemo.TAG, strRet);
                switch (msg.what) {
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        AlixDemo.this.closeProgress();
                        BaseHelper.log(AlixDemo.TAG, strRet);
                        try {
                            String tradeStatus = strRet.substring(strRet.indexOf("resultStatus=") + "resultStatus={".length(), strRet.indexOf("};memo="));
                            if (new ResultChecker(strRet).checkSign() != 1) {
                                if (!tradeStatus.equals("9000")) {
                                    BaseHelper.showDialog(AlixDemo.this, "\u63d0\u793a", "\u652f\u4ed8\u5931\u8d25\u3002\u4ea4\u6613\u72b6\u6001\u7801:" + tradeStatus, C0136R.drawable.infoicon);
                                    break;
                                } else {
                                    BaseHelper.showDialog(AlixDemo.this, "\u63d0\u793a", "\u652f\u4ed8\u6210\u529f\u3002\u4ea4\u6613\u72b6\u6001\u7801\uff1a" + tradeStatus, C0136R.drawable.infoicon);
                                    break;
                                }
                            }
                            BaseHelper.showDialog(AlixDemo.this, "\u63d0\u793a", AlixDemo.this.getResources().getString(C0136R.string.check_sign_failed), 17301543);
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            BaseHelper.showDialog(AlixDemo.this, "\u63d0\u793a", strRet, C0136R.drawable.infoicon);
                            break;
                        }
                }
                super.handleMessage(msg);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    static class AlixOnCancelListener implements OnCancelListener {
        Activity mcontext;

        AlixOnCancelListener(Activity context) {
            this.mcontext = context;
        }

        public void onCancel(DialogInterface dialog) {
            this.mcontext.onKeyDown(4, null);
        }
    }

    public AlixDemo() {
        this.mproductListView = null;
        this.m_listViewAdapter = null;
        this.mProgress = null;
        this.mHandler = new C00771();
    }

    static {
        TAG = "AppDemo4";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        new MobileSecurePayHelper(this).detectMobile_sp();
        setContentView(C0136R.layout.remote_service_binding);
        ((TextView) findViewById(C0136R.id.AlipayTitleItemName)).setText(getString(C0136R.string.app_name));
        initProductList();
    }

    void initProductList() {
        this.mproductlist = new Products().retrieveProductInfo();
        this.mproductListView = (ListView) findViewById(C0136R.id.ProductListView);
        this.m_listViewAdapter = new ProductListAdapter(this, this.mproductlist);
        this.mproductListView.setAdapter(this.m_listViewAdapter);
        this.mproductListView.setOnItemClickListener(this);
        this.mproductListView.setOnItemLongClickListener(this);
    }

    String getOrderInfo(int position) {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("partner=\"2088701486169280\"" + AlixDefine.split)).append("seller=\"2088701486169280\"").toString())).append(AlixDefine.split).toString())).append("out_trade_no=\"").append(getOutTradeNo()).append("\"").toString())).append(AlixDefine.split).toString())).append("subject=\"").append(((ProductDetail) this.mproductlist.get(position)).subject).append("\"").toString())).append(AlixDefine.split).toString())).append("body=\"").append(((ProductDetail) this.mproductlist.get(position)).body).append("\"").toString())).append(AlixDefine.split).toString())).append("total_fee=\"").append(((ProductDetail) this.mproductlist.get(position)).price.replace("\u4e00\u53e3\u4ef7:", XmlPullParser.NO_NAMESPACE)).append("\"").toString())).append(AlixDefine.split).toString())).append("notify_url=\"http://notify.java.jpxx.org/index.jsp\"").toString();
    }

    String getOutTradeNo() {
        return new StringBuilder(String.valueOf(new SimpleDateFormat("MMddHHmmss").format(new Date()))).append(new Random().nextInt()).toString().substring(0, 15);
    }

    String sign(String signType, String content) {
        return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
    }

    String getSignType() {
        return "sign_type=\"RSA\"";
    }

    String getCharset() {
        return "charset=\"utf-8\"";
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (!new MobileSecurePayHelper(this).detectMobile_sp()) {
            return;
        }
        if (checkInfo()) {
            try {
                String signType = getSignType();
                String signStr1 = PartnerConfig.RSA_PRIVATE;
                Log.v("sign:", signStr1);
                String info = "partner=\"2088701486169280\"&seller=\"2088701486169280\"&out_trade_no=\"FXO20130330184953175\"&subject=\"\u5927\u8fea...\"&body=\"\u5927\u8fea...\"&total_fee=\"40.0\"&notify_url=\"http://mycar.x431.com/services/alipay/alipayRSANotifyReceiver.action\"" + "&sign=" + "\"" + signStr1 + "\"" + AlixDefine.split + getSignType();
                Log.v("orderInfo:", info);
                if (new MobileSecurePayer().pay(info, this.mHandler, 1, this)) {
                    closeProgress();
                    this.mProgress.setCancelable(false);
                    this.mProgress = BaseHelper.showProgress(this, null, "\u6b63\u5728\u652f\u4ed8", false, true);
                    return;
                }
                return;
            } catch (Exception e) {
                Toast.makeText(this, C0136R.string.remote_call_failed, 0).show();
                return;
            }
        }
        BaseHelper.showDialog(this, "\u63d0\u793a", "\u7f3a\u5c11partner\u6216\u8005seller\uff0c\u8bf7\u5728src/com/alipay/android/appDemo4/PartnerConfig.java\u4e2d\u589e\u52a0\u3002", C0136R.drawable.infoicon);
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        return false;
    }

    private boolean checkInfo() {
        String partner = PartnerConfig.SELLER;
        String seller = PartnerConfig.SELLER;
        if (partner == null || partner.length() <= 0 || seller == null || seller.length() <= 0) {
            return false;
        }
        return true;
    }

    void closeProgress() {
        try {
            if (this.mProgress != null) {
                this.mProgress.dismiss();
                this.mProgress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        BaseHelper.log(TAG, "onKeyDown back");
        finish();
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        try {
            this.mProgress.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
