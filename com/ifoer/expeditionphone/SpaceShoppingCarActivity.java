package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.car.result.DiagSoftOrderResult;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.adapter.SpaceShoppingCarListAdapter;
import com.ifoer.adapter.SpaceShoppingCarListAdapter.Item;
import com.ifoer.db.DBDao;
import com.ifoer.dbentity.ShoppingCar;
import com.ifoer.entity.DiagSoftOrder;
import com.ifoer.entity.DiagSoftOrderInfo;
import com.ifoer.util.Common;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.webservice.ProductService;
import java.net.SocketTimeoutException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class SpaceShoppingCarActivity extends MySpaceManagermentLayout implements OnClickListener {
    private SpaceShoppingCarListAdapter adapter;
    private View baseView;
    private String cc;
    private LinearLayout checkAll;
    private ImageView checkImage;
    private TextView checkTextview;
    private Context context;
    private LinearLayout delete;
    private int flag;
    Handler handler;
    private ListView listview;
    private DiagSoftOrderResult orderListResult;
    private LinearLayout placeOrder;
    private ProgressDialog progressDialogs;
    private List<ShoppingCar> selected;
    private List<ShoppingCar> shoppingCar;

    /* renamed from: com.ifoer.expeditionphone.SpaceShoppingCarActivity.1 */
    class C06441 extends Handler {
        C06441() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (SpaceShoppingCarActivity.this.progressDialogs != null && SpaceShoppingCarActivity.this.progressDialogs.isShowing()) {
                        SpaceShoppingCarActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(SpaceShoppingCarActivity.this.context, C0136R.string.timeout, 0).show();
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    for (int i = 0; i < SpaceShoppingCarActivity.this.shoppingCar.size(); i++) {
                        SpaceShoppingCarListAdapter.isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
                    }
                    if (SpaceShoppingCarActivity.this.adapter != null) {
                        SpaceShoppingCarActivity.this.adapter.notifyDataSetChanged();
                    }
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.SpaceShoppingCarActivity.2 */
    class C06452 implements OnItemClickListener {
        C06452() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            Item item = (Item) arg1.getTag();
            item.checkBox.toggle();
            SpaceShoppingCarListAdapter.isSelected.put(Integer.valueOf(arg2), Boolean.valueOf(item.checkBox.isChecked()));
        }
    }

    /* renamed from: com.ifoer.expeditionphone.SpaceShoppingCarActivity.3 */
    class C06463 implements Runnable {
        C06463() {
        }

        public void run() {
            List<ShoppingCar> cars = DBDao.getInstance(SpaceShoppingCarActivity.this.context).getShoppingList(MySharedPreferences.getStringValue(SpaceShoppingCarActivity.this.context, MySharedPreferences.CCKey), MainActivity.database);
            if (SpaceShoppingCarActivity.this.shoppingCar != null) {
                SpaceShoppingCarActivity.this.shoppingCar.clear();
            }
            for (ShoppingCar car : cars) {
                SpaceShoppingCarActivity.this.shoppingCar.add(car);
            }
            SpaceShoppingCarActivity.this.handler.obtainMessage(1).sendToTarget();
        }
    }

    class CreatOrderTask extends AsyncTask<String, String, String> {
        DiagSoftOrder orderInfo;

        CreatOrderTask() {
            this.orderInfo = null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            SpaceShoppingCarActivity.this.progressDialogs = new ProgressDialog(SpaceShoppingCarActivity.this.context);
            SpaceShoppingCarActivity.this.progressDialogs.setMessage(SpaceShoppingCarActivity.this.getResources().getString(C0136R.string.shopping_now));
            SpaceShoppingCarActivity.this.progressDialogs.setCancelable(false);
            SpaceShoppingCarActivity.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            ProductService softService = new ProductService();
            String cc = MySharedPreferences.getStringValue(SpaceShoppingCarActivity.this.context, MySharedPreferences.CCKey);
            String token = MySharedPreferences.getStringValue(SpaceShoppingCarActivity.this.context, MySharedPreferences.TokenKey);
            softService.setCc(cc);
            softService.setToken(token);
            int buyType = 0;
            int currencyId = 0;
            String serialNo = null;
            List<DiagSoftOrderInfo> dsois = new ArrayList();
            double totalPrice = 0.0d;
            for (ShoppingCar shoppingCar : SpaceShoppingCarActivity.this.selected) {
                buyType = Integer.parseInt(shoppingCar.getBuyType());
                currencyId = Integer.parseInt(shoppingCar.getCurrencyId());
                String price = shoppingCar.getPrice();
                totalPrice += Double.parseDouble(price);
                serialNo = shoppingCar.getSerialNo();
                int softId = Integer.parseInt(shoppingCar.getSoftId());
                String softName = shoppingCar.getSoftName();
                String version = shoppingCar.getVersion();
                DiagSoftOrderInfo dsoi = new DiagSoftOrderInfo();
                dsoi.setCurrencyId(currencyId);
                dsoi.setPrice(price);
                dsoi.setSerialNo(serialNo);
                dsoi.setSoftId(softId);
                dsoi.setSoftName(softName);
                dsoi.setVersion(version.replace("V", XmlPullParser.NO_NAMESPACE));
                dsois.add(dsoi);
            }
            try {
                this.orderInfo = new DiagSoftOrder();
                this.orderInfo.setBuyType(buyType);
                this.orderInfo.setCc(cc);
                this.orderInfo.setCurrencyId(currencyId);
                this.orderInfo.setSerialNo(serialNo);
                this.orderInfo.setSoftOrderList(dsois);
                NumberFormat numberFormat = NumberFormat.getNumberInstance();
                numberFormat.setMaximumFractionDigits(2);
                totalPrice = Double.parseDouble(numberFormat.format(totalPrice));
                this.orderInfo.setTotalPrice(new StringBuilder(String.valueOf(totalPrice)).toString());
                SpaceShoppingCarActivity.this.orderListResult = softService.createDiagSoftOrder(this.orderInfo);
            } catch (SocketTimeoutException e) {
                SpaceShoppingCarActivity.this.handler.obtainMessage(0).sendToTarget();
            } catch (NullPointerException e2) {
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (SpaceShoppingCarActivity.this.orderListResult != null) {
                if (SpaceShoppingCarActivity.this.progressDialogs != null && SpaceShoppingCarActivity.this.progressDialogs.isShowing()) {
                    SpaceShoppingCarActivity.this.progressDialogs.dismiss();
                }
                if (SpaceShoppingCarActivity.this.orderListResult.getCode() == -1) {
                    SimpleDialog.validTokenDialog(SpaceShoppingCarActivity.this.context);
                } else if (SpaceShoppingCarActivity.this.orderListResult.getCode() == 0) {
                    SpaceShoppingCarActivity.this.delete();
                    Toast.makeText(SpaceShoppingCarActivity.this.context, C0136R.string.soft_order_right, 0).show();
                } else if (SpaceShoppingCarActivity.this.orderListResult.getCode() == 793) {
                    if (this.orderInfo.getCurrencyId() == 4) {
                        Toast.makeText(SpaceShoppingCarActivity.this.context, C0136R.string.buy_softpackage_by_usd, 0).show();
                    } else if (this.orderInfo.getCurrencyId() == 11) {
                        Toast.makeText(SpaceShoppingCarActivity.this.context, C0136R.string.buy_softpackage_by_rmb, 0).show();
                    }
                } else if (SpaceShoppingCarActivity.this.orderListResult.getCode() != 794) {
                    new MyHttpException(SpaceShoppingCarActivity.this.orderListResult.getCode()).showToast(SpaceShoppingCarActivity.this.context);
                } else if (this.orderInfo.getCurrencyId() == 4) {
                    Toast.makeText(SpaceShoppingCarActivity.this.context, C0136R.string.buy_soft_by_usd, 0).show();
                } else if (this.orderInfo.getCurrencyId() == 11) {
                    Toast.makeText(SpaceShoppingCarActivity.this.context, C0136R.string.buy_soft_by_rmb, 0).show();
                }
            } else if (SpaceShoppingCarActivity.this.progressDialogs != null && SpaceShoppingCarActivity.this.progressDialogs.isShowing()) {
                SpaceShoppingCarActivity.this.progressDialogs.dismiss();
            }
        }
    }

    public SpaceShoppingCarActivity(Context context) {
        super(context);
        this.flag = 0;
        this.handler = new C06441();
        this.context = context;
        this.baseView = ((Activity) context).getLayoutInflater().inflate(C0136R.layout.space_shopping_car, this);
        initTopView(this.baseView);
        setTopView(context, 3);
        init();
    }

    private void init() {
        this.placeOrder = (LinearLayout) findViewById(C0136R.id.placeOrder);
        this.placeOrder.setOnClickListener(this);
        this.delete = (LinearLayout) findViewById(C0136R.id.delete);
        this.delete.setOnClickListener(this);
        this.checkAll = (LinearLayout) findViewById(C0136R.id.checkAll);
        this.checkAll.setOnClickListener(this);
        this.checkTextview = (TextView) findViewById(C0136R.id.checkTextview);
        this.checkImage = (ImageView) findViewById(C0136R.id.checkImage);
        this.listview = (ListView) findViewById(C0136R.id.shopping_list);
        this.cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        this.shoppingCar = DBDao.getInstance(this.context).getShoppingList(this.cc, MainActivity.database);
        this.adapter = new SpaceShoppingCarListAdapter(this.context, this.shoppingCar);
        this.listview.setAdapter(this.adapter);
        this.listview.setOnItemClickListener(new C06452());
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.placeOrder) {
            createOrder();
        } else if (v.getId() == C0136R.id.delete) {
            delete();
        } else if (v.getId() == C0136R.id.checkAll) {
            int i;
            if (this.flag == 0) {
                this.checkTextview.setText(getResources().getText(C0136R.string.cancel));
                this.checkImage.setImageResource(C0136R.drawable.diag_unselect_btn);
                for (i = 0; i < this.listview.getCount(); i++) {
                    SpaceShoppingCarListAdapter.isSelected.put(Integer.valueOf(i), Boolean.valueOf(true));
                }
                this.flag = 1;
            } else {
                this.checkTextview.setText(getResources().getText(C0136R.string.check_all));
                this.checkImage.setImageResource(C0136R.drawable.diag_select_btn);
                for (i = 0; i < this.listview.getCount(); i++) {
                    SpaceShoppingCarListAdapter.isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
                }
                this.flag = 0;
            }
            if (this.adapter != null) {
                this.adapter.notifyDataSetChanged();
            }
        }
    }

    private void delete() {
        List<ShoppingCar> deleteLists = new ArrayList();
        if (this.adapter != null) {
            for (int i = 0; i < this.listview.getCount(); i++) {
                if (((Boolean) SpaceShoppingCarListAdapter.isSelected.get(Integer.valueOf(i))).booleanValue()) {
                    deleteLists.add((ShoppingCar) this.adapter.getItem(i));
                }
            }
        }
        if (deleteLists.size() > 0) {
            DBDao.getInstance(this.context).deleteShoppingCar(deleteLists, MainActivity.database);
            new Thread(new C06463()).start();
            return;
        }
        Toast.makeText(this.context, C0136R.string.soft_check, 0).show();
    }

    private void createOrder() {
        this.selected = new ArrayList();
        Set<String> serialNoList = new HashSet();
        if (this.adapter != null) {
            for (int i = 0; i < this.listview.getCount(); i++) {
                if (((Boolean) SpaceShoppingCarListAdapter.isSelected.get(Integer.valueOf(i))).booleanValue()) {
                    ShoppingCar shoppingCar = (ShoppingCar) this.adapter.getItem(i);
                    this.selected.add(shoppingCar);
                    serialNoList.add(shoppingCar.getSerialNo());
                }
            }
        }
        if (this.selected.size() <= 0) {
            Toast.makeText(this.context, C0136R.string.soft_check, 0).show();
        } else if (serialNoList.size() != 1) {
            Toast.makeText(this.context, C0136R.string.serialNo_same, 0).show();
        } else if (Common.isNetworkAvailable(this.context)) {
            new CreatOrderTask().execute(new String[0]);
        } else {
            Toast.makeText(this.context, C0136R.string.network_exception, 0).show();
        }
    }
}
