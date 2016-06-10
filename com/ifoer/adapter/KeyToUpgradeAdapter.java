package com.ifoer.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.module.upgrade.action.UpgradeAction;
import com.cnlaunch.x431pro.module.upgrade.model.HistoryDiagSoftsResponse;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.expeditionphone.KeyToUpgradeActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.Common;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.webservice.X431PadDiagSoftService;
import com.thoughtworks.xstream.XStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public class KeyToUpgradeAdapter extends BaseAdapter {
    private String CName;
    private KeyToUpgradeActivity activity;
    private boolean apkNeedUpgrade;
    private TextView carName;
    private Context context;
    private boolean firmWareNeedUpgrade;
    private Handler handler;
    private List<String> historyVerStr;
    private LayoutInflater inflater;
    private boolean isLoad;
    public Map<Integer, Boolean> isSelected;
    private ListView list;
    private List<X431PadDtoSoft> listResult;
    private PopAdapter mAdapter;
    private ListView mListView;
    private TextView textView;
    private List<String> updatelist;
    private List<String> versionDetailIdList;
    private PopupWindow window;

    /* renamed from: com.ifoer.adapter.KeyToUpgradeAdapter.1 */
    class C03351 implements OnClickListener {
        private final /* synthetic */ int val$arg0;
        private final /* synthetic */ ViewHolder val$viewHolder;

        C03351(int i, ViewHolder viewHolder) {
            this.val$arg0 = i;
            this.val$viewHolder = viewHolder;
        }

        public void onClick(View view) {
            if (((Boolean) KeyToUpgradeAdapter.this.isSelected.get(Integer.valueOf(this.val$arg0))).booleanValue()) {
                KeyToUpgradeAdapter.this.isSelected.put(Integer.valueOf(this.val$arg0), Boolean.valueOf(false));
                this.val$viewHolder.checkbox.setChecked(false);
                return;
            }
            KeyToUpgradeAdapter.this.isSelected.put(Integer.valueOf(this.val$arg0), Boolean.valueOf(true));
            this.val$viewHolder.checkbox.setChecked(true);
        }
    }

    /* renamed from: com.ifoer.adapter.KeyToUpgradeAdapter.2 */
    class C03362 implements OnClickListener {
        private final /* synthetic */ int val$arg0;
        private final /* synthetic */ int val$position1;

        C03362(int i, int i2) {
            this.val$arg0 = i;
            this.val$position1 = i2;
        }

        public void onClick(View view) {
            if (((X431PadDtoSoft) KeyToUpgradeAdapter.this.listResult.get(this.val$arg0)).getType() != 1 && ((X431PadDtoSoft) KeyToUpgradeAdapter.this.listResult.get(this.val$arg0)).getType() != 2) {
                if (Common.isNetworkAvailable(KeyToUpgradeAdapter.this.context)) {
                    new LoadHistorySoftware(this.val$position1).execute(new String[0]);
                } else {
                    Toast.makeText(KeyToUpgradeAdapter.this.context, C0136R.string.network, 0).show();
                }
                if (((X431PadDtoSoft) KeyToUpgradeAdapter.this.listResult.get(this.val$position1)).getSoftName().toUpperCase().equals("EOBD2")) {
                    KeyToUpgradeAdapter.this.CName = "EOBD";
                } else {
                    KeyToUpgradeAdapter.this.CName = ((X431PadDtoSoft) KeyToUpgradeAdapter.this.listResult.get(this.val$position1)).getSoftName();
                }
                KeyToUpgradeAdapter.this.popAwindow((TextView) view, this.val$position1);
            }
        }
    }

    /* renamed from: com.ifoer.adapter.KeyToUpgradeAdapter.3 */
    class C03373 implements OnItemClickListener {
        private final /* synthetic */ int val$index1;
        private final /* synthetic */ TextView val$textv;

        C03373(int i, TextView textView) {
            this.val$index1 = i;
            this.val$textv = textView;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ((X431PadDtoSoft) KeyToUpgradeAdapter.this.listResult.get(this.val$index1)).setVersionNo((String) KeyToUpgradeAdapter.this.historyVerStr.get(position));
            ((X431PadDtoSoft) KeyToUpgradeAdapter.this.listResult.get(this.val$index1)).setVersionDetailId((String) KeyToUpgradeAdapter.this.versionDetailIdList.get(position));
            KeyToUpgradeAdapter.this.handler.sendEmptyMessage(10);
            KeyToUpgradeAdapter.this.window.dismiss();
            this.val$textv.setText(((X431PadDtoSoft) KeyToUpgradeAdapter.this.listResult.get(this.val$index1)).getVersionNo());
        }
    }

    /* renamed from: com.ifoer.adapter.KeyToUpgradeAdapter.4 */
    class C03384 implements OnDismissListener {
        C03384() {
        }

        public void onDismiss() {
            KeyToUpgradeAdapter.this.historyVerStr.clear();
            KeyToUpgradeAdapter.this.versionDetailIdList.clear();
            KeyToUpgradeAdapter.this.handler.sendEmptyMessage(11);
        }
    }

    class LoadHistorySoftware extends AsyncTask<String, String, HistoryDiagSoftsResponse> {
        private final int positionSoft;
        private UpgradeAction upgradeAction;

        public LoadHistorySoftware(int position) {
            this.upgradeAction = new UpgradeAction(KeyToUpgradeAdapter.this.context);
            this.positionSoft = position;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected HistoryDiagSoftsResponse doInBackground(String... params) {
            if (!DBDao.getInstance(KeyToUpgradeAdapter.this.context).isExistSoftId(MainActivity.database)) {
                DBDao.getInstance(KeyToUpgradeAdapter.this.context).updateUpgrade(MainActivity.database);
            }
            X431PadDiagSoftService service = new X431PadDiagSoftService();
            String cc = MySharedPreferences.getStringValue(KeyToUpgradeAdapter.this.context, MySharedPreferences.CCKey);
            String token = MySharedPreferences.getStringValue(KeyToUpgradeAdapter.this.context, MySharedPreferences.TokenKey);
            String serialNo = MySharedPreferences.getStringValue(KeyToUpgradeAdapter.this.context, MySharedPreferences.serialNoKey);
            Integer softId = Integer.valueOf(((X431PadDtoSoft) KeyToUpgradeAdapter.this.listResult.get(this.positionSoft)).getSoftId());
            if (MainActivity.country == null || MainActivity.country.length() <= 0) {
                MainActivity.country = Locale.getDefault().getCountry();
            }
            HistoryDiagSoftsResponse result = null;
            try {
                result = this.upgradeAction.queryHistoryDiagSofts(serialNo, softId, new StringBuilder(String.valueOf(AndroidToLan.getLanId(MainActivity.country))).toString(), new StringBuilder(String.valueOf(XStream.NO_REFERENCES)).toString());
            } catch (NullPointerException e) {
                KeyToUpgradeAdapter.this.handler.obtainMessage(1).sendToTarget();
            } catch (Exception e2) {
                KeyToUpgradeAdapter.this.handler.obtainMessage(0).sendToTarget();
            }
            return result;
        }

        protected void onPostExecute(HistoryDiagSoftsResponse result) {
            super.onPostExecute(result);
            if (result == null) {
                return;
            }
            if (result.getCode() == -1) {
                Toast.makeText(KeyToUpgradeAdapter.this.context, C0136R.string.connect_server_error, 0).show();
            } else if (result.getCode() != 0) {
                Toast.makeText(KeyToUpgradeAdapter.this.context, result.getCode() + result.getMsg(), 0).show();
            } else if (result != null && result.getX431PadSoftList().size() > 0) {
                String serialNo = MySharedPreferences.getStringValue(KeyToUpgradeAdapter.this.context, MySharedPreferences.serialNoKey);
                String lanName = Constants.DEFAULT_LANGUAGE;
                KeyToUpgradeAdapter.this.historyVerStr.clear();
                KeyToUpgradeAdapter.this.versionDetailIdList.clear();
                for (X431PadDtoSoft dto : result.getX431PadSoftList()) {
                    KeyToUpgradeAdapter.this.versionDetailIdList.add(dto.getVersionDetailId());
                    KeyToUpgradeAdapter.this.historyVerStr.add(dto.getVersionNo());
                }
                KeyToUpgradeAdapter.this.mAdapter.refresh(KeyToUpgradeAdapter.this.historyVerStr);
            }
        }
    }

    public class ViewHolder {
        public CheckBox checkbox;
        public TextView name;
        public boolean needRemve;
        public TextView newVersion;
        public TextView oldVersion;
        public TextView popText;
    }

    public KeyToUpgradeAdapter(List<X431PadDtoSoft> listResult, Context context, Map<Integer, Boolean> isSelected, Handler handler) {
        this.updatelist = new ArrayList();
        this.window = null;
        this.handler = null;
        this.historyVerStr = new ArrayList();
        this.versionDetailIdList = new ArrayList();
        this.apkNeedUpgrade = false;
        this.firmWareNeedUpgrade = false;
        this.isLoad = false;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listResult = listResult;
        this.handler = handler;
        this.activity = (KeyToUpgradeActivity) context;
        if (listResult != null) {
            for (int i = 0; i < listResult.size(); i++) {
                String oldVer = ((X431PadDtoSoft) listResult.get(i)).getMaxOldVersion();
                String ver = ((X431PadDtoSoft) listResult.get(i)).getVersionNo();
                double maxOld = 0.0d;
                int type = ((X431PadDtoSoft) listResult.get(i)).getType();
                if (type != 1) {
                    if (oldVer != null) {
                        maxOld = Double.parseDouble(oldVer.split("V")[1]);
                    }
                    if (maxOld >= Double.parseDouble(ver.split("V")[1])) {
                        if (type == 2) {
                            this.firmWareNeedUpgrade = false;
                        }
                        isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
                    } else {
                        if (type == 2) {
                            this.firmWareNeedUpgrade = true;
                        }
                        isSelected.put(Integer.valueOf(i), Boolean.valueOf(true));
                    }
                } else if (Integer.parseInt(oldVer.split("V")[1].replace(".", XmlPullParser.NO_NAMESPACE)) >= Integer.parseInt(ver.split("V")[1].replace(".", XmlPullParser.NO_NAMESPACE))) {
                    isSelected.put(Integer.valueOf(i), Boolean.valueOf(false));
                    this.apkNeedUpgrade = false;
                } else {
                    isSelected.put(Integer.valueOf(i), Boolean.valueOf(true));
                    this.apkNeedUpgrade = true;
                }
            }
        }
        this.isSelected = isSelected;
    }

    public void refreshData(List<X431PadDtoSoft> listResult) {
        this.listResult = listResult;
        notifyDataSetChanged();
    }

    public int getCount() {
        if (this.listResult.size() > 0) {
            return this.listResult.size();
        }
        return 0;
    }

    public Object getItem(int arg0) {
        return this.listResult.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int arg0, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;
        int position1 = arg0;
        this.isLoad = false;
        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
            convertView = this.inflater.inflate(C0136R.layout.software_options_item, null, false);
            viewHolder.name = (TextView) convertView.findViewById(C0136R.id.softwareName);
            viewHolder.oldVersion = (TextView) convertView.findViewById(C0136R.id.softwareVer);
            viewHolder.newVersion = (TextView) convertView.findViewById(C0136R.id.softwareSer);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(C0136R.id.checkBox);
            viewHolder.popText = (TextView) convertView.findViewById(C0136R.id.editText1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (((X431PadDtoSoft) this.listResult.get(arg0)).getSoftName().toUpperCase().equals("EOBD2")) {
            viewHolder.name.setText("EOBD");
        } else {
            viewHolder.name.setText(((X431PadDtoSoft) this.listResult.get(arg0)).getSoftName());
        }
        viewHolder.oldVersion.setText(((X431PadDtoSoft) this.listResult.get(arg0)).getMaxOldVersion());
        viewHolder.newVersion.setText(((X431PadDtoSoft) this.listResult.get(arg0)).getVersionNo());
        viewHolder.popText.setText(((X431PadDtoSoft) this.listResult.get(arg0)).getVersionNo());
        viewHolder.checkbox.setChecked(((Boolean) this.isSelected.get(Integer.valueOf(arg0))).booleanValue());
        this.isLoad = true;
        this.textView = viewHolder.popText;
        if (((X431PadDtoSoft) this.listResult.get(arg0)).getType() == 1) {
            if (this.apkNeedUpgrade) {
                viewHolder.popText.setClickable(false);
                viewHolder.needRemve = false;
                viewHolder.checkbox.setVisibility(0);
            } else {
                viewHolder.popText.setClickable(false);
                viewHolder.needRemve = true;
                viewHolder.checkbox.setVisibility(4);
            }
        } else if (((X431PadDtoSoft) this.listResult.get(arg0)).getType() != 2) {
            viewHolder.popText.setClickable(true);
            viewHolder.needRemve = false;
            viewHolder.checkbox.setClickable(true);
            viewHolder.checkbox.setVisibility(0);
        } else if (this.firmWareNeedUpgrade) {
            viewHolder.popText.setClickable(false);
            viewHolder.needRemve = false;
            viewHolder.checkbox.setVisibility(0);
        } else {
            viewHolder.popText.setClickable(false);
            viewHolder.needRemve = true;
            viewHolder.checkbox.setClickable(false);
            viewHolder.checkbox.setVisibility(8);
        }
        Constant.upgradeAPk = this.apkNeedUpgrade;
        Constant.upgradeFirm = this.firmWareNeedUpgrade;
        viewHolder.checkbox.setOnClickListener(new C03351(arg0, viewHolder));
        Drawable drawable = this.context.getResources().getDrawable(C0136R.drawable.arra_01);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (arg0 == 0 || arg0 == 1) {
            viewHolder.popText.setCompoundDrawables(null, null, null, null);
        } else {
            viewHolder.popText.setCompoundDrawables(null, null, drawable, null);
        }
        viewHolder.popText.setOnClickListener(new C03362(arg0, position1));
        return convertView;
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public void onConfigurationChanged(Activity activity) {
        if (this.window != null && this.window.isShowing()) {
            this.window.update(this.textView.getWidth(), -2);
        }
    }

    private void popAwindow(TextView poptext, int index) {
        int index1 = index;
        TextView textv = poptext;
        String name = this.CName;
        if (this.window == null) {
            View v = this.inflater.inflate(C0136R.layout.popwindow, null);
            this.list = (ListView) v.findViewById(C0136R.id.lv);
            this.list.setItemsCanFocus(false);
            this.list.setChoiceMode(2);
            this.carName = (TextView) v.findViewById(C0136R.id.tvname);
            this.carName.setText(name);
            this.mAdapter = new PopAdapter(this.context, this.historyVerStr);
            this.list.setAdapter(this.mAdapter);
            this.window = new PopupWindow(v, this.activity.getView().getWidth(), -2);
        } else if (!this.window.isShowing()) {
            this.window.setWidth(this.activity.getView().getWidth());
            this.carName.setText(this.CName);
            this.window.update();
        }
        this.list.setOnItemClickListener(new C03373(index1, textv));
        this.window.setFocusable(true);
        this.window.setTouchable(true);
        this.window.setOutsideTouchable(true);
        this.window.setBackgroundDrawable(new ColorDrawable());
        this.window.update();
        this.window.showAsDropDown(this.activity.getView(), 0, 0);
        this.window.setOnDismissListener(new C03384());
    }
}
