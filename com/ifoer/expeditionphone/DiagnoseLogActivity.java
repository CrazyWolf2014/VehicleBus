package com.ifoer.expeditionphone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.car.result.UserOrderListResult;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.DiagnoseLogInfoAdapter;
import com.ifoer.adapter.DiagnoseLogInfoAdapter1;
import com.ifoer.adapter.SoftOrderAdapter;
import com.ifoer.entity.UserOrder;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.webservice.ProductService;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.KEYRecord;

public class DiagnoseLogActivity extends MySpaceManagermentLayout {
    private RelativeLayout My_order;
    private DiagnoseLogInfoAdapter1 adapter1;
    private DiagnoseLogInfoAdapter adapter2;
    private SoftOrderAdapter adapterOrder;
    private TextView address;
    private RelativeLayout baogao;
    private TextView bgtext;
    private TextView buytext;
    protected LinearLayout car_maintain;
    private String cc;
    private TextView chexing;
    private View circleView;
    Context context;
    private LayoutInflater inflater;
    private Intent intent;
    private RelativeLayout jilu;
    private List<UserOrder> listOrder;
    private UserOrderListResult orderListResult;
    private List<UserOrder> payList;
    private UserOrderListResult res;
    private RelativeLayout shoppingcar;
    private TextView shoppingcartext;
    private TextView shtext;
    private TextView time;
    private String token;
    private List<UserOrder> unPayList;
    private List<UserOrder> userOrder;

    class GetOrderTask extends AsyncTask<String, String, String> {
        private final Handler mHandler;
        ProgressDialog progressDialogs;

        /* renamed from: com.ifoer.expeditionphone.DiagnoseLogActivity.GetOrderTask.1 */
        class C05361 extends Handler {
            C05361() {
            }

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case KEYRecord.OWNER_USER /*0*/:
                        if (GetOrderTask.this.progressDialogs != null && GetOrderTask.this.progressDialogs.isShowing()) {
                            GetOrderTask.this.progressDialogs.dismiss();
                        }
                        Toast.makeText(DiagnoseLogActivity.this.context, C0136R.string.timeout, 0).show();
                    default:
                }
            }
        }

        GetOrderTask() {
            this.mHandler = new C05361();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialogs = new ProgressDialog(DiagnoseLogActivity.this.context);
            this.progressDialogs.setMessage(DiagnoseLogActivity.this.context.getResources().getString(C0136R.string.find_wait));
            this.progressDialogs.setCancelable(false);
            this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            ProductService softService = new ProductService();
            DiagnoseLogActivity.this.cc = MySharedPreferences.getStringValue(DiagnoseLogActivity.this.context, MySharedPreferences.CCKey);
            DiagnoseLogActivity.this.token = MySharedPreferences.getStringValue(DiagnoseLogActivity.this.context, MySharedPreferences.TokenKey);
            softService.setCc(DiagnoseLogActivity.this.cc);
            softService.setToken(DiagnoseLogActivity.this.token);
            try {
                DiagnoseLogActivity.this.orderListResult = softService.getUserOrderList(DiagnoseLogActivity.this.cc, params[0]);
            } catch (SocketTimeoutException e) {
                this.mHandler.obtainMessage(0).sendToTarget();
            } catch (NullPointerException e2) {
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (DiagnoseLogActivity.this.orderListResult != null) {
                if (this.progressDialogs != null && this.progressDialogs.isShowing()) {
                    this.progressDialogs.dismiss();
                }
                if (DiagnoseLogActivity.this.orderListResult.getCode() == -1) {
                    SimpleDialog.validTokenDialog(DiagnoseLogActivity.this.context);
                } else if (DiagnoseLogActivity.this.orderListResult.getCode() == 0) {
                    if (DiagnoseLogActivity.this.payList != null) {
                        DiagnoseLogActivity.this.payList.clear();
                    }
                    DiagnoseLogActivity.this.listOrder = DiagnoseLogActivity.this.orderListResult.getUserOrder();
                    for (int i = 0; i < DiagnoseLogActivity.this.listOrder.size(); i++) {
                        UserOrder order = (UserOrder) DiagnoseLogActivity.this.listOrder.get(i);
                        if (order.getStatus() == 1) {
                            DiagnoseLogActivity.this.payList.add(order);
                        }
                    }
                    DiagnoseLogActivity.this.adapter1 = new DiagnoseLogInfoAdapter1(DiagnoseLogActivity.this.context, DiagnoseLogActivity.this.payList);
                } else if (DiagnoseLogActivity.this.orderListResult.getCode() == MyHttpException.ERROR_OTHER_REGISTER) {
                    Toast.makeText(DiagnoseLogActivity.this.context, C0136R.string.check_serialNo, 0).show();
                } else {
                    new MyHttpException(DiagnoseLogActivity.this.orderListResult.getCode()).showToast(DiagnoseLogActivity.this.context);
                }
            } else if (this.progressDialogs != null && this.progressDialogs.isShowing()) {
                this.progressDialogs.dismiss();
            }
        }
    }

    public DiagnoseLogActivity(Context context) {
        super(context);
        this.inflater = null;
        this.adapter1 = null;
        this.adapter2 = null;
        this.userOrder = null;
        this.unPayList = new ArrayList();
        this.payList = new ArrayList();
        this.listOrder = new ArrayList();
        this.adapterOrder = null;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        this.circleView = this.inflater.inflate(C0136R.layout.diagnose_log, null);
        addView(this.circleView, new LayoutParams(-1, -1));
        initTopView(this.circleView);
        setTopView(this.context, 0);
    }

    public void deleteFolderFile(String filePath, boolean deleteThisPath) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File absolutePath : files) {
                    deleteFolderFile(absolutePath.getAbsolutePath(), true);
                }
            }
            if (!deleteThisPath) {
                return;
            }
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.listFiles().length == 0) {
                file.delete();
            }
        }
    }
}
