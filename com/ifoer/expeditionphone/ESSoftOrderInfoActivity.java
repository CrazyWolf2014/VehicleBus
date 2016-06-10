package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alipay.android.appDemo4.BaseHelper;
import com.alipay.android.appDemo4.MobileSecurePayHelper;
import com.alipay.android.appDemo4.MobileSecurePayer;
import com.alipay.android.appDemo4.PartnerConfig;
import com.alipay.android.appDemo4.ResultChecker;
import com.alipay.android.appDemo4.Rsa;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.car.result.AlipayRSATradeDTOResult;
import com.car.result.OrderDetailInfoResult;
import com.car.result.WSResult;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.adapter.OrderInfoAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.OrderBillPostInfoResult;
import com.ifoer.entity.OrderDetail;
import com.ifoer.entity.QueryAlipayRSATrade;
import com.ifoer.entity.UserOrder;
import com.ifoer.mine.Contact;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.Common;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.webservice.BillService;
import com.ifoer.webservice.ProductService;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalPayment;
import com.tencent.mm.sdk.platformtools.Util;
import com.tencent.mm.sdk.plugin.BaseProfile;
import com.thoughtworks.xstream.XStream;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Locale;
import org.apache.harmony.javax.security.auth.callback.ConfirmationCallback;
import org.xmlpull.v1.XmlPullParser;

public class ESSoftOrderInfoActivity extends BaseActivity implements OnClickListener {
    static String TAG;
    private Button backBtn;
    private RelativeLayout bill;
    private LinearLayout billLinear;
    private String cc;
    private Context context;
    private OrderDetailInfoResult detailInfoListResult;
    private LinearLayout huiKuanLay;
    private ImageView img1;
    private OrderInfoAdapter infoAdapter;
    private ListView infoListView;
    private int lanId;
    private LinearLayout linear1;
    private LinearLayout linear2;
    private LinearLayout linear3;
    private LinearLayout linear4;
    private LinearLayout linear5;
    private List<OrderDetail> list;
    private Handler mHandler;
    private ProgressDialog mProgress;
    private TextView orderCount;
    private OrderDetail orderDetail;
    private UserOrder orderInfo;
    private boolean orderListChange;
    private TextView orderName;
    private OrderBillPostInfoResult orderResult;
    private TextView orderSN;
    private String payKey;
    private PayPal payPal;
    private TextView payment_method;
    private LinearLayout paypalLinear;
    private String priceFlag;
    private TextView priceType;
    private ProgressDialog progressDialogs;
    private Button purchaseNotes;
    private String purchasedFlag;
    private QueryAlipayRSATrade queryAlipayRSATrade;
    private TextView serialNo;
    private TextView show_name;
    private AlipayRSATradeDTOResult taoBaoInfoResult;
    private LinearLayout taoBaoLay;
    private String token;
    private LinearLayout wangYeLay;

    /* renamed from: com.ifoer.expeditionphone.ESSoftOrderInfoActivity.1 */
    class C05561 extends Handler {

        /* renamed from: com.ifoer.expeditionphone.ESSoftOrderInfoActivity.1.1 */
        class C05551 implements DialogInterface.OnClickListener {
            C05551() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ESSoftOrderInfoActivity.this.orderListChange = true;
            }
        }

        C05561() {
        }

        public void handleMessage(Message msg) {
            String strRet = msg.obj;
            switch (msg.what) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    ESSoftOrderInfoActivity.this.closeProgress();
                    try {
                        String tradeStatus = strRet.substring(strRet.indexOf("resultStatus=") + "resultStatus={".length(), strRet.indexOf("};memo="));
                        ResultChecker resultChecker = new ResultChecker(strRet);
                        if (tradeStatus.equals("9000")) {
                            Builder builder = new Builder(ESSoftOrderInfoActivity.this.context);
                            builder.setTitle(ESSoftOrderInfoActivity.this.getResources().getString(C0136R.string.order_notic));
                            builder.setMessage(ESSoftOrderInfoActivity.this.getResources().getString(C0136R.string.pay_success));
                            builder.setCancelable(false);
                            builder.setPositiveButton(ESSoftOrderInfoActivity.this.getResources().getString(C0136R.string.Ensure), new C05551());
                            builder.show();
                            return;
                        }
                        BaseHelper.showDialog((Activity) ESSoftOrderInfoActivity.this.context, ESSoftOrderInfoActivity.this.context.getResources().getString(C0136R.string.order_notic), ESSoftOrderInfoActivity.this.getResources().getString(C0136R.string.pay_faild), C0136R.drawable.infoicon);
                    } catch (Exception e) {
                        e.printStackTrace();
                        BaseHelper.showDialog((Activity) ESSoftOrderInfoActivity.this.context, ESSoftOrderInfoActivity.this.context.getResources().getString(C0136R.string.order_notic), strRet, C0136R.drawable.infoicon);
                    }
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.timeout, 0).show();
                case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                    MySharedPreferences.setString(ESSoftOrderInfoActivity.this.getApplicationContext(), "showPurchase", Contact.RELATION_BACKNAME);
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ESSoftOrderInfoActivity.2 */
    class C05572 implements DialogInterface.OnClickListener {
        C05572() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            ESSoftOrderInfoActivity.this.byTaoBao();
        }
    }

    class CompletePay extends AsyncTask<String, String, String> {
        com.ifoer.entity.CompletePay completePay;

        CompletePay() {
        }

        protected String doInBackground(String... params) {
            try {
                ProductService service = new ProductService();
                service.setCc(MySharedPreferences.getStringValue(ESSoftOrderInfoActivity.this.context, MySharedPreferences.CCKey));
                service.setToken(MySharedPreferences.getStringValue(ESSoftOrderInfoActivity.this.context, MySharedPreferences.TokenKey));
                int orderId = 0;
                if (ESSoftOrderInfoActivity.this.orderInfo != null) {
                    orderId = ESSoftOrderInfoActivity.this.orderInfo.getOrderId();
                }
                this.completePay = service.completePay(Integer.valueOf(orderId), Integer.valueOf(1), ESSoftOrderInfoActivity.this.context);
            } catch (SocketTimeoutException e) {
                ESSoftOrderInfoActivity.this.mHandler.obtainMessage(10).sendToTarget();
            } catch (NullPointerException e2) {
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.completePay == null) {
                return;
            }
            if (this.completePay.getCode() == -1) {
                SimpleDialog.validTokenDialog(ESSoftOrderInfoActivity.this.context);
            } else if (this.completePay.getCode() != 0) {
                Toast.makeText(ESSoftOrderInfoActivity.this, this.completePay.getMessage(), 0).show();
            }
        }
    }

    class GetOrderBillInfoTask extends AsyncTask<String, String, String> {
        GetOrderBillInfoTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ESSoftOrderInfoActivity.this.progressDialogs = new ProgressDialog(ESSoftOrderInfoActivity.this.context);
            ESSoftOrderInfoActivity.this.progressDialogs.setMessage(ESSoftOrderInfoActivity.this.getResources().getString(C0136R.string.find_wait));
            ESSoftOrderInfoActivity.this.progressDialogs.setCancelable(false);
            ESSoftOrderInfoActivity.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            BillService billService = new BillService();
            ESSoftOrderInfoActivity.this.cc = MySharedPreferences.getStringValue(ESSoftOrderInfoActivity.this.context, MySharedPreferences.CCKey);
            ESSoftOrderInfoActivity.this.token = MySharedPreferences.getStringValue(ESSoftOrderInfoActivity.this.context, MySharedPreferences.TokenKey);
            billService.setCc(ESSoftOrderInfoActivity.this.cc);
            billService.setToken(ESSoftOrderInfoActivity.this.token);
            try {
                ESSoftOrderInfoActivity.this.orderResult = billService.getOrderBillPostInfo(ESSoftOrderInfoActivity.this.orderInfo.getOrderSN());
            } catch (NullPointerException e) {
            } catch (SocketTimeoutException e2) {
                ESSoftOrderInfoActivity.this.mHandler.obtainMessage(10).sendToTarget();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (ESSoftOrderInfoActivity.this.progressDialogs != null && ESSoftOrderInfoActivity.this.progressDialogs.isShowing()) {
                ESSoftOrderInfoActivity.this.progressDialogs.dismiss();
            }
            if (ESSoftOrderInfoActivity.this.orderResult != null && ESSoftOrderInfoActivity.this.orderResult.getCode() == -1) {
                SimpleDialog.validTokenDialog(ESSoftOrderInfoActivity.this.context);
            } else if ((ESSoftOrderInfoActivity.this.orderResult == null || ESSoftOrderInfoActivity.this.orderResult.getCode() != 792) && ESSoftOrderInfoActivity.this.orderResult != null && ESSoftOrderInfoActivity.this.orderResult.getCode() == 0) {
                Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.exist_bill, 0).show();
            }
        }
    }

    class GetOrderInfoTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialogs;

        /* renamed from: com.ifoer.expeditionphone.ESSoftOrderInfoActivity.GetOrderInfoTask.1 */
        class C05581 implements OnClickListener {
            C05581() {
            }

            public void onClick(View v) {
                ESSoftOrderInfoActivity.this.payPalWay();
            }
        }

        GetOrderInfoTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialogs = new ProgressDialog(ESSoftOrderInfoActivity.this.context);
            this.progressDialogs.setMessage(ESSoftOrderInfoActivity.this.getResources().getString(C0136R.string.find_wait));
            this.progressDialogs.setCancelable(false);
            this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            ProductService softService = new ProductService();
            ESSoftOrderInfoActivity.this.cc = MySharedPreferences.getStringValue(ESSoftOrderInfoActivity.this.context, MySharedPreferences.CCKey);
            ESSoftOrderInfoActivity.this.token = MySharedPreferences.getStringValue(ESSoftOrderInfoActivity.this.context, MySharedPreferences.TokenKey);
            softService.setCc(ESSoftOrderInfoActivity.this.cc);
            softService.setToken(ESSoftOrderInfoActivity.this.token);
            try {
                ESSoftOrderInfoActivity.this.detailInfoListResult = softService.getUserOrderDetailInfo(Integer.valueOf(ESSoftOrderInfoActivity.this.orderInfo.getOrderId()));
            } catch (NullPointerException e) {
            } catch (SocketTimeoutException e2) {
                ESSoftOrderInfoActivity.this.mHandler.obtainMessage(10).sendToTarget();
            }
            ESSoftOrderInfoActivity.this.payPal = PayPal.initWithAppID(ESSoftOrderInfoActivity.this.context, "APP-4WU5991108007560L", 1);
            ESSoftOrderInfoActivity.this.payPal.setLanguage(Util.ENGLISH);
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (ESSoftOrderInfoActivity.this.detailInfoListResult != null) {
                if (this.progressDialogs != null && this.progressDialogs.isShowing()) {
                    this.progressDialogs.dismiss();
                }
                if (ESSoftOrderInfoActivity.this.detailInfoListResult.getCode() == -1) {
                    SimpleDialog.validTokenDialog(ESSoftOrderInfoActivity.this.context);
                } else if (ESSoftOrderInfoActivity.this.detailInfoListResult.getCode() == 0) {
                    ESSoftOrderInfoActivity.this.list = DBDao.getInstance(ESSoftOrderInfoActivity.this).getIcons(ESSoftOrderInfoActivity.this.detailInfoListResult.getOrderDetailList(), MainActivity.database);
                    ESSoftOrderInfoActivity.this.infoAdapter = new OrderInfoAdapter(ESSoftOrderInfoActivity.this.context, ESSoftOrderInfoActivity.this.list);
                    ESSoftOrderInfoActivity.this.infoListView.setAdapter(ESSoftOrderInfoActivity.this.infoAdapter);
                } else {
                    new MyHttpException(ESSoftOrderInfoActivity.this.detailInfoListResult.getCode()).showToast(ESSoftOrderInfoActivity.this.context);
                }
            } else if (this.progressDialogs != null && this.progressDialogs.isShowing()) {
                this.progressDialogs.dismiss();
            }
            CheckoutButton checkoutButton = ESSoftOrderInfoActivity.this.payPal.getCheckoutButton(ESSoftOrderInfoActivity.this, 0, 2);
            checkoutButton.setLayoutParams(new LayoutParams(-2, -2));
            ESSoftOrderInfoActivity.this.paypalLinear.addView(checkoutButton);
            checkoutButton.setOnClickListener(new C05581());
            ESSoftOrderInfoActivity.this.mHandler.obtainMessage(11).sendToTarget();
        }
    }

    class GetPaypalInfoTask extends AsyncTask<String, String, String> {
        WSResult wSResult;

        GetPaypalInfoTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ESSoftOrderInfoActivity.this.progressDialogs = new ProgressDialog(ESSoftOrderInfoActivity.this.context);
            ESSoftOrderInfoActivity.this.progressDialogs.setMessage(ESSoftOrderInfoActivity.this.getResources().getString(C0136R.string.find_wait));
            ESSoftOrderInfoActivity.this.progressDialogs.setCancelable(false);
            ESSoftOrderInfoActivity.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            BillService softService = new BillService();
            ESSoftOrderInfoActivity.this.cc = MySharedPreferences.getStringValue(ESSoftOrderInfoActivity.this.context, MySharedPreferences.CCKey);
            ESSoftOrderInfoActivity.this.token = MySharedPreferences.getStringValue(ESSoftOrderInfoActivity.this.context, MySharedPreferences.TokenKey);
            softService.setCc(ESSoftOrderInfoActivity.this.cc);
            softService.setToken(ESSoftOrderInfoActivity.this.token);
            try {
                this.wSResult = softService.checkMobilePaypalPayment(ESSoftOrderInfoActivity.this.payKey);
            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.wSResult != null) {
                ESSoftOrderInfoActivity.this.progressDialogs.dismiss();
                if (this.wSResult.getCode() == -1) {
                    SimpleDialog.validTokenDialog(ESSoftOrderInfoActivity.this.context);
                    return;
                } else if (this.wSResult.getCode() == 0) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.pay_success, 0).show();
                    DBDao.getInstance(ESSoftOrderInfoActivity.this).updatePayKey(ESSoftOrderInfoActivity.this.payKey, MainActivity.database);
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_PARAMETER_EMPTY) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.notic_null, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_UNREGISTER) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.ERROR_UNREGISTER_PRODUCT, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_SERVER) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.the_service_side_abnormal, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.IDIAG_PACKAGE_EXISTS) {
                    DBDao.getInstance(ESSoftOrderInfoActivity.this).updatePayKey(ESSoftOrderInfoActivity.this.payKey, MainActivity.database);
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.Package_has_to_buy, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_UNREGISTER_PRODUCT) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.io_exception, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_USER_PRODUCT_MISMATCHING) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.not_match_the_product, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_UNLOGIN_USER) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.User_didnt_log_in, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_NOEXIST_PACKAGE) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.package_null, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 774) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.Serial_numbe_is_empty, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 775) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.Select_software_number_is_greater, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 776) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.software_in_the_package, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 778) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.requesting_data_configuration_errors, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 779) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.data_is_used_in_invalid_credentials, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 780) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.Http_request_data_anomalies, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 781) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.response_when_the_request_data, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 782) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.When_the_requested_data_CLIENT_ACTION_REQUIRED, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 783) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.request_data_lack_of_credentials, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 784) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.request_data_authentication_anomalies, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 785) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.Request_to_interrupt, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 786) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.In_response_to_failure, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 787) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.PAYPAL_TRANSACTION_NOT_COMPLETE, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 788) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.PAYPAL_PRICE_ERROR, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 789) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.PAYPAL_ORDER_NULL, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 790) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.pay_success, 0).show();
                    DBDao.getInstance(ESSoftOrderInfoActivity.this).updatePayKey(ESSoftOrderInfoActivity.this.payKey, MainActivity.database);
                    return;
                } else if (this.wSResult.getCode() == 791) {
                    Toast.makeText(ESSoftOrderInfoActivity.this.context, C0136R.string.PAYPAL_CURRENCY_CODE_ERROR, 0).show();
                    return;
                } else {
                    return;
                }
            }
            ESSoftOrderInfoActivity.this.progressDialogs.dismiss();
        }
    }

    class GetTaobaoInfoTask extends AsyncTask<String, String, String> {
        GetTaobaoInfoTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ESSoftOrderInfoActivity.this.progressDialogs = new ProgressDialog(ESSoftOrderInfoActivity.this.context);
            ESSoftOrderInfoActivity.this.progressDialogs.setMessage(ESSoftOrderInfoActivity.this.getResources().getString(C0136R.string.find_wait));
            ESSoftOrderInfoActivity.this.progressDialogs.setCancelable(false);
            ESSoftOrderInfoActivity.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            ProductService softService = new ProductService();
            ESSoftOrderInfoActivity.this.cc = MySharedPreferences.getStringValue(ESSoftOrderInfoActivity.this.context, MySharedPreferences.CCKey);
            ESSoftOrderInfoActivity.this.token = MySharedPreferences.getStringValue(ESSoftOrderInfoActivity.this.context, MySharedPreferences.TokenKey);
            softService.setCc(ESSoftOrderInfoActivity.this.cc);
            softService.setToken(ESSoftOrderInfoActivity.this.token);
            ESSoftOrderInfoActivity.this.queryAlipayRSATrade = new QueryAlipayRSATrade();
            ESSoftOrderInfoActivity.this.queryAlipayRSATrade.setBasePath(Constant.urlBusiness);
            ESSoftOrderInfoActivity.this.queryAlipayRSATrade.setOrderId(ESSoftOrderInfoActivity.this.orderInfo.getOrderId());
            try {
                ESSoftOrderInfoActivity.this.taoBaoInfoResult = softService.queryAlipayRSATrade(ESSoftOrderInfoActivity.this.queryAlipayRSATrade);
            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (ESSoftOrderInfoActivity.this.taoBaoInfoResult != null) {
                if (ESSoftOrderInfoActivity.this.progressDialogs != null && ESSoftOrderInfoActivity.this.progressDialogs.isShowing()) {
                    ESSoftOrderInfoActivity.this.progressDialogs.dismiss();
                }
                if (ESSoftOrderInfoActivity.this.taoBaoInfoResult.getCode() == -1) {
                    SimpleDialog.validTokenDialog(ESSoftOrderInfoActivity.this.context);
                } else if (ESSoftOrderInfoActivity.this.taoBaoInfoResult.getCode() == 0) {
                    System.out.println("getSignData=" + ESSoftOrderInfoActivity.this.taoBaoInfoResult.getAlipayRSATradeDTO().getSignData());
                    System.out.println("getSignType=" + ESSoftOrderInfoActivity.this.taoBaoInfoResult.getAlipayRSATradeDTO().getSignType());
                    System.out.println("getSign=" + ESSoftOrderInfoActivity.this.taoBaoInfoResult.getAlipayRSATradeDTO().getSign());
                    ESSoftOrderInfoActivity.this.payTaoBao();
                } else {
                    new MyHttpException(ESSoftOrderInfoActivity.this.taoBaoInfoResult.getCode()).showToast(ESSoftOrderInfoActivity.this.context);
                }
            } else if (ESSoftOrderInfoActivity.this.progressDialogs != null && ESSoftOrderInfoActivity.this.progressDialogs.isShowing()) {
                ESSoftOrderInfoActivity.this.progressDialogs.dismiss();
            }
        }
    }

    public ESSoftOrderInfoActivity() {
        this.mProgress = null;
        this.payKey = null;
        this.priceFlag = null;
        this.mHandler = new C05561();
        this.list = null;
        this.orderListChange = false;
    }

    static {
        TAG = "\u5143\u5f81\u6c7d\u8f66";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.es_order_content);
        initLeftBtnNew(this, 0);
        this.context = this;
        MyApplication.getInstance().addActivity(this);
        Intent intent = getIntent();
        this.orderInfo = (UserOrder) intent.getSerializableExtra("orderInfo");
        this.purchasedFlag = intent.getStringExtra("flag");
        if (this.orderInfo == null) {
            Toast.makeText(this.context, C0136R.string.order_exit_null, 0).show();
            finish();
            overridePendingTransition(0, 0);
            return;
        }
        initView();
    }

    private void initView() {
        if (MainActivity.country == null || MainActivity.country.length() <= 0) {
            MainActivity.country = Locale.getDefault().getCountry();
        }
        this.lanId = AndroidToLan.getLanId(MainActivity.country);
        this.payment_method = (TextView) findViewById(C0136R.id.payment_method);
        this.linear1 = (LinearLayout) findViewById(C0136R.id.linear1);
        this.linear2 = (LinearLayout) findViewById(C0136R.id.linear2);
        this.linear3 = (LinearLayout) findViewById(C0136R.id.linear3);
        this.linear4 = (LinearLayout) findViewById(C0136R.id.linear4);
        this.linear5 = (LinearLayout) findViewById(C0136R.id.linear5);
        this.img1 = (ImageView) findViewById(C0136R.id.img1);
        this.bill = (RelativeLayout) findViewById(C0136R.id.bill);
        this.bill.setOnClickListener(this);
        this.paypalLinear = (LinearLayout) findViewById(C0136R.id.paypal);
        this.billLinear = (LinearLayout) findViewById(C0136R.id.billLinear);
        this.billLinear.setVisibility(8);
        this.backBtn = (Button) findViewById(C0136R.id.backBtn);
        this.backBtn.setOnClickListener(this);
        this.purchaseNotes = (Button) findViewById(C0136R.id.purchaseNotes);
        this.purchaseNotes.setVisibility(4);
        this.purchaseNotes.setOnClickListener(this);
        this.taoBaoLay = (LinearLayout) findViewById(C0136R.id.taobao);
        this.taoBaoLay.setOnClickListener(this);
        this.huiKuanLay = (LinearLayout) findViewById(C0136R.id.huikuan);
        this.huiKuanLay.setOnClickListener(this);
        this.taoBaoLay.setVisibility(8);
        this.paypalLinear.setVisibility(0);
        this.huiKuanLay.setVisibility(8);
        this.wangYeLay = (LinearLayout) findViewById(C0136R.id.wangye);
        this.wangYeLay.setOnClickListener(this);
        this.show_name = (TextView) findViewById(C0136R.id.show_name);
        String name = MySharedPreferences.getStringValue(this.context, BaseProfile.COL_USERNAME);
        if (!(name == null || XmlPullParser.NO_NAMESPACE.equals(name))) {
            this.show_name.setText(name);
        }
        this.orderName = (TextView) findViewById(C0136R.id.softName);
        this.orderName.setText(this.orderInfo.getOrderName());
        this.orderSN = (TextView) findViewById(C0136R.id.orderID);
        this.orderSN.setText(this.orderInfo.getOrderSN());
        this.serialNo = (TextView) findViewById(C0136R.id.orderXulhao);
        this.serialNo.setText(this.orderInfo.getSerialNo());
        this.orderCount = (TextView) findViewById(C0136R.id.orderCount);
        this.orderCount.setText(new StringBuilder(String.valueOf(this.orderInfo.getTotalPrice())).toString());
        int flag = this.orderInfo.getCurrencyId();
        this.priceFlag = "RMB";
        if (flag == 4) {
            this.priceFlag = (String) this.context.getResources().getText(C0136R.string.RMB);
        } else if (flag == 11) {
            this.priceFlag = (String) this.context.getResources().getText(C0136R.string.USD);
        } else if (flag == 13) {
            this.priceFlag = (String) this.context.getResources().getText(C0136R.string.HKD);
        } else if (flag == 14) {
            this.priceFlag = (String) this.context.getResources().getText(C0136R.string.EUR);
        }
        this.priceType = (TextView) findViewById(C0136R.id.priceType);
        this.priceType.setText(this.priceFlag);
        if (this.orderInfo == null || this.orderInfo.getStatus() != 0 || this.lanId != XStream.NO_REFERENCES) {
            this.paypalLinear.setVisibility(0);
        } else if (this.priceFlag.equalsIgnoreCase("RMB")) {
            this.paypalLinear.setVisibility(8);
            Toast.makeText(this, C0136R.string.ERROR_UNSUPPORT_paypal_CURRENCY_TYPE, 0).show();
        } else {
            this.paypalLinear.setVisibility(0);
        }
        this.infoListView = (ListView) findViewById(C0136R.id.infoListView);
        if (Common.isNetworkAvailable(this)) {
            new GetOrderInfoTask().execute(new String[0]);
        } else {
            Toast.makeText(this, C0136R.string.network_exception, 0).show();
        }
        this.cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        if ("purchased".equals(this.purchasedFlag)) {
            this.payment_method.setVisibility(8);
            this.taoBaoLay.setVisibility(8);
            this.paypalLinear.setVisibility(8);
            this.wangYeLay.setVisibility(8);
            this.huiKuanLay.setVisibility(8);
            this.linear1.setVisibility(8);
            this.linear2.setVisibility(8);
            this.linear3.setVisibility(8);
            this.linear4.setVisibility(8);
            this.linear5.setVisibility(8);
            this.img1.setVisibility(8);
        }
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.purchaseNotes) {
            Common.showDialog(this.context, getResources().getText(C0136R.string.purchase_notes).toString(), getResources().getText(C0136R.string.purchase_notes_content).toString(), getResources().getText(C0136R.string.Ok).toString());
        } else if (v.getId() == C0136R.id.taobao) {
            if (this.orderInfo.getTotalPrice() != 0.0d) {
                Builder builder = new Builder(this.context);
                builder.setTitle(getResources().getString(C0136R.string.order_notic));
                builder.setMessage(getResources().getString(C0136R.string.prompt));
                builder.setPositiveButton(getResources().getString(C0136R.string.Ensure), new C05572());
                builder.show();
            } else if (Common.isNetworkAvailable(this.context)) {
                new CompletePay().execute(new String[0]);
            } else {
                Toast.makeText(this.context, C0136R.string.network_exception, 0).show();
            }
        } else if (v.getId() == C0136R.id.huikuan) {
        } else {
            if (v.getId() == C0136R.id.wangye) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse("http://mycar.x431.com/mycar/pad/order/showOrder_user.action"));
                startActivity(intent);
            } else if (v.getId() != C0136R.id.bill) {
            } else {
                if (!Common.isNetworkAvailable(this.context)) {
                    Toast.makeText(this.context, C0136R.string.network_exception, 0).show();
                } else if (this.orderResult == null || this.orderResult.getCode() != 0) {
                    new GetOrderBillInfoTask().execute(new String[0]);
                } else {
                    Toast.makeText(this.context, C0136R.string.exist_bill, 0).show();
                }
            }
        }
    }

    private void payPalWay() {
        PayPalPayment newPayment = new PayPalPayment();
        newPayment.setSubtotal(new BigDecimal(this.orderInfo.getTotalPrice()));
        newPayment.setCurrencyType(this.priceFlag);
        newPayment.setRecipient("jiangbo.zhang@cnlaunch.com");
        newPayment.setMerchantName("LAUNCH TECH CO.,LTD.");
        newPayment.setMemo(this.orderInfo.getOrderSN());
        PayPalInvoiceData invoice = new PayPalInvoiceData();
        invoice.setTax(new BigDecimal(Contact.RELATION_ASK));
        invoice.setShipping(new BigDecimal(Contact.RELATION_ASK));
        startActivityForResult(this.payPal.checkout(newPayment, (Context) this), 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ConfirmationCallback.UNSPECIFIED_OPTION /*-1*/:
                this.payKey = data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                DBDao.getInstance(this).addPayKey(this.cc, MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey), this.orderInfo.getOrderSN(), this.payKey, Contact.RELATION_ASK, MainActivity.database);
                new GetPaypalInfoTask().execute(new String[0]);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                Toast.makeText(this.context, C0136R.string.pay_faild, 0).show();
            default:
        }
    }

    private void showDialog(String message) {
        Builder dialog = new Builder(this);
        dialog.setTitle("\u63d0\u793a");
        dialog.setMessage(message);
        dialog.show();
    }

    private void byTaoBao() {
        if (Common.isNetworkAvailable(this)) {
            new GetTaobaoInfoTask().execute(new String[0]);
        } else {
            Toast.makeText(this, C0136R.string.network_exception, 0).show();
        }
    }

    String sign(String signType, String content) {
        return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
    }

    String getSignType() {
        return "sign_type=\"RSA\"";
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

    private void payTaoBao() {
        if (new MobileSecurePayHelper(this.context).detectMobile_sp()) {
            try {
                String orderInfo = this.taoBaoInfoResult.getAlipayRSATradeDTO().getSignData();
                String signStr1 = this.taoBaoInfoResult.getAlipayRSATradeDTO().getSign();
                Log.v("sign:", signStr1);
                String info = new StringBuilder(String.valueOf(orderInfo)).append("&sign=").append("\"").append(signStr1).append("\"").append(AlixDefine.split).append(getSignType()).toString();
                Log.v("orderInfo:", info);
                if (new MobileSecurePayer().pay(info, this.mHandler, 1, (Activity) this.context)) {
                    closeProgress();
                    this.mProgress = BaseHelper.showProgress(this.context, null, getResources().getString(C0136R.string.pay_now), false, true);
                }
            } catch (Exception e) {
                Toast.makeText(this.context, C0136R.string.remote_call_failed, 0).show();
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
