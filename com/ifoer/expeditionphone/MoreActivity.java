package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.listener.OnChangedListener;
import com.ifoer.util.Common;
import com.ifoer.util.Files;
import com.ifoer.util.MyApkUpdate;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.view.MenuHorizontalScrollView;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.io.File;
import java.util.Locale;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public class MoreActivity extends RelativeLayout implements OnChangedListener, OnClickListener {
    private static final String FROMPATH;
    private static final int ISNEW = 100;
    public static ProgressDialog mProgressDialog;
    AlertDialog aboutDialog;
    private View aboutDialogView;
    private Button aboutKnowBtn;
    private RelativeLayout about_btn;
    protected LinearLayout car_maintain;
    private RelativeLayout check_version;
    private Context context;
    private RelativeLayout download_btn;
    private RelativeLayout guide_btn;
    private int height;
    private LayoutInflater inflater;
    private PackageInfo info;
    private Intent intent;
    private String language;
    private Button login;
    private final Handler mHandler;
    private TextView mUserNameText;
    private RelativeLayout manual_btn;
    private RelativeLayout menu;
    public Button menuBtn;
    private View morView;
    private TextView repu_notic;
    public AlertDialog reputPopupWindow;
    private Button reput_btn;
    private CheckBox reput_check;
    private ImageView reput_close;
    private RelativeLayout reput_lay;
    private RelativeLayout rlPop;
    private MenuHorizontalScrollView scrollView;
    private RelativeLayout statment;
    private RelativeLayout user_rules;
    private String verLocal;
    private TextView version;
    private int width;

    /* renamed from: com.ifoer.expeditionphone.MoreActivity.1 */
    class C06031 extends Handler {
        C06031() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MoreActivity.ISNEW /*100*/:
                    if (MoreActivity.mProgressDialog != null && MoreActivity.mProgressDialog.isShowing()) {
                        MoreActivity.mProgressDialog.dismiss();
                    }
                    Toast.makeText(MoreActivity.this.context, C0136R.string.isnew, 0).show();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MoreActivity.2 */
    class C06042 implements OnKeyListener {
        C06042() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != 4) {
                return false;
            }
            MoreActivity.this.reputPopupWindow.dismiss();
            return true;
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MoreActivity.3 */
    class C06053 implements OnClickListener {
        C06053() {
        }

        public void onClick(View arg0) {
            if (MoreActivity.this.reput_check.isChecked()) {
                MySharedPreferences.setBoolean(MoreActivity.this.context, MySharedPreferences.IfShowResponsibilityKey, false);
            }
            MoreActivity.this.reputPopupWindow.dismiss();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MoreActivity.4 */
    class C06064 implements OnClickListener {
        C06064() {
        }

        public void onClick(View arg0) {
            MoreActivity.this.reputPopupWindow.dismiss();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MoreActivity.5 */
    class C06075 implements OnClickListener {
        C06075() {
        }

        public void onClick(View v) {
            MoreActivity.this.aboutDialog.dismiss();
        }
    }

    public MoreActivity(Context context) {
        super(context);
        this.inflater = null;
        this.width = 0;
        this.height = 0;
        this.mHandler = new C06031();
        this.context = context;
        this.language = Files.getLanguage();
        try {
            this.info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            this.verLocal = "Ver: " + this.info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        this.inflater = LayoutInflater.from(context);
        creatView();
        this.aboutDialog = new Builder(context).create();
    }

    static {
        FROMPATH = Constant.LOCAL_SERIALNO_PATH + Constant.PRODUCT_TYPE + FilePathGenerator.ANDROID_DIR_SEP;
    }

    private void creatView() {
        this.scrollView = (MenuHorizontalScrollView) findViewById(C0136R.id.scrollView);
        this.menu = (RelativeLayout) findViewById(C0136R.id.main_leftmenu);
        this.morView = this.inflater.inflate(C0136R.layout.more_popa, null);
        this.morView.setBackgroundDrawable(MySharedPreferences.getBgId(this.context));
        addView(this.morView, new LayoutParams(-1, -1));
        this.guide_btn = (RelativeLayout) this.morView.findViewById(C0136R.id.content_lay1);
        this.guide_btn.setOnClickListener(this);
        this.about_btn = (RelativeLayout) this.morView.findViewById(C0136R.id.about);
        this.about_btn.setOnClickListener(this);
        this.download_btn = (RelativeLayout) this.morView.findViewById(C0136R.id.download);
        this.download_btn.setOnClickListener(this);
        this.manual_btn = (RelativeLayout) this.morView.findViewById(C0136R.id.manual);
        this.manual_btn.setOnClickListener(this);
        this.check_version = (RelativeLayout) this.morView.findViewById(C0136R.id.check_version);
        this.check_version.setOnClickListener(this);
        this.user_rules = (RelativeLayout) this.morView.findViewById(C0136R.id.more_rulesss);
        this.user_rules.setOnClickListener(this);
        this.statment = (RelativeLayout) this.morView.findViewById(C0136R.id.rule_statment);
        this.statment.setOnClickListener(this);
        if (Locale.getDefault().getLanguage().equalsIgnoreCase(LocaleUtil.JAPANESE)) {
            this.user_rules.setVisibility(0);
        }
        this.menuBtn = (Button) this.morView.findViewById(C0136R.id.menuBtn);
    }

    public void initPopupWindow() {
        this.reputPopupWindow = new Builder(this.context).create();
        this.reputPopupWindow.setCanceledOnTouchOutside(false);
        this.reputPopupWindow.setView(reputDialogView());
        this.reputPopupWindow.show();
        this.reputPopupWindow.setOnKeyListener(new C06042());
        this.reputPopupWindow.setContentView(reputDialogView(), new LayoutParams(-1, -1));
    }

    private View reputDialogView() {
        this.reput_lay = (RelativeLayout) LayoutInflater.from(this.context).inflate(C0136R.layout.reputation_pop, null);
        this.rlPop = (RelativeLayout) this.reput_lay.findViewById(C0136R.id.rl_popdialog);
        if (getResources().getConfiguration().orientation == 2) {
            this.rlPop.setPadding(0, 60, 0, 60);
        } else {
            this.rlPop.setPadding(30, Opcodes.FCMPG, 30, Opcodes.FCMPG);
        }
        this.reput_close = (ImageView) this.reput_lay.findViewById(C0136R.id.repu_close);
        this.reput_check = (CheckBox) this.reput_lay.findViewById(C0136R.id.repu_select);
        this.repu_notic = (TextView) this.reput_lay.findViewById(C0136R.id.repu_notic);
        this.reput_check.setVisibility(8);
        this.repu_notic.setVisibility(8);
        this.reput_btn = (Button) this.reput_lay.findViewById(C0136R.id.repu_btn);
        this.reput_btn.setOnClickListener(new C06053());
        this.reput_close.setOnClickListener(new C06064());
        return this.reput_lay;
    }

    private View AboutDialog() {
        WindowManager wm = (WindowManager) this.context.getSystemService("window");
        this.width = wm.getDefaultDisplay().getWidth();
        this.height = wm.getDefaultDisplay().getHeight();
        this.aboutDialogView = LayoutInflater.from(this.context).inflate(C0136R.layout.about_pop, null, false);
        this.version = (TextView) this.aboutDialogView.findViewById(C0136R.id.about_text);
        String info = new StringBuilder(String.valueOf(this.context.getResources().getString(C0136R.string.about_pop_text))).append(this.verLocal).append("\n\n").append(Constant.buildStr).append(this.context.getResources().getString(C0136R.string.about_web)).toString();
        this.version.setText(info);
        Log.e("MoreActivity", "version info" + info);
        this.aboutKnowBtn = (Button) this.aboutDialogView.findViewById(C0136R.id.ikonwBtn);
        this.aboutKnowBtn.setOnClickListener(new C06075());
        return this.aboutDialogView;
    }

    public void OnChanged(String strName, boolean CheckState) {
    }

    private View GuideView() {
        View view = LayoutInflater.from(this.context).inflate(C0136R.layout.information_info, null, false);
        WebView mWebView = (WebView) view.findViewById(C0136R.id.wv);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.requestFocus();
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + "http://www8.cao.go.jp/okinawa/8/2012/0409-1-1.pdf");
        return view;
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.about) {
            this.aboutDialog = new Builder(this.context).create();
            this.aboutDialog.show();
            this.aboutDialog.setCancelable(true);
            if (getResources().getConfiguration().orientation == 2) {
                this.aboutDialog.setContentView(AboutDialog(), new LayoutParams((this.width * 58) / ISNEW, (this.height * 84) / ISNEW));
            } else if (getResources().getConfiguration().orientation == 1) {
                this.aboutDialog.setContentView(AboutDialog(), new LayoutParams(-1, -1));
            }
        } else if (v.getId() == C0136R.id.content_lay1) {
            if (new File(FROMPATH + MySharedPreferences.getStringValue(this.context, "MANUAL_CN")).exists()) {
                if (this.language.equals("zh")) {
                    this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "MANUAL_CN"));
                    Log.d("bcf", "intent===" + FROMPATH + "&&&&&" + this.intent);
                } else {
                    this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "MANUAL_EN"));
                }
                try {
                    this.context.startActivity(this.intent);
                    return;
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.main_tools_pdf), 0).show();
                    return;
                }
            }
            Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.main_file_null), 0).show();
        } else if (v.getId() == C0136R.id.download) {
            if (this.language.equals("zh")) {
                this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_CN"));
            } else if (this.language.equalsIgnoreCase("JP")) {
                this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_JA"));
            } else if (this.language.equalsIgnoreCase("DE")) {
                this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_DE"));
            } else if (this.language.equalsIgnoreCase("FR")) {
                this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_FR"));
            } else if (this.language.equalsIgnoreCase("RU")) {
                this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_RU"));
            } else if (this.language.equalsIgnoreCase("IT")) {
                this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_IT"));
            } else {
                this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_EN"));
            }
            try {
                this.context.startActivity(this.intent);
            } catch (ActivityNotFoundException e2) {
                Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.main_tools_pdf), 0).show();
            }
        } else if (v.getId() == C0136R.id.manual) {
            if (new File(FROMPATH + MySharedPreferences.getStringValue(this.context, "USER_MANUAL_CN")).exists() || new File(FROMPATH + MySharedPreferences.getStringValue(this.context, "USER_MANUAL_EN")).exists()) {
                if (this.language.equals("zh")) {
                    if (MySharedPreferences.getStringValue(this.context, "PRODUCT_TYPE").equalsIgnoreCase("X431VPlus") || MySharedPreferences.getStringValue(this.context, "PRODUCT_TYPE").equalsIgnoreCase("X431V")) {
                        Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.main_file_null), 0).show();
                        return;
                    } else {
                        this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "USER_MANUAL_CN"));
                        Log.d("bcf", "intent====" + FROMPATH + "*****" + this.intent);
                    }
                } else if (!this.language.equals(LocaleUtil.JAPANESE)) {
                    this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "USER_MANUAL_EN"));
                } else if (MySharedPreferences.getStringValue(this.context, "PRODUCT_TYPE").equalsIgnoreCase("X431VPlus") || MySharedPreferences.getStringValue(this.context, "PRODUCT_TYPE").equalsIgnoreCase("X431V")) {
                    this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "USER_MANUAL_EN"));
                } else {
                    this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(this.context, "USER_MANUAL_JA"));
                }
                try {
                    this.context.startActivity(this.intent);
                    return;
                } catch (ActivityNotFoundException e3) {
                    Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.main_tools_pdf), 0).show();
                    return;
                }
            }
            Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.main_file_null), 0).show();
        } else if (v.getId() == C0136R.id.check_version) {
            if (Common.isNetworkAvailable(this.context)) {
                mProgressDialog = new ProgressDialog(this.context);
                mProgressDialog.setMessage(this.context.getResources().getText(C0136R.string.getting_version_info));
                mProgressDialog.show();
                MyApkUpdate.getMyApkUpdate(this.context, this.mHandler, false).checkUpdateAsync();
                return;
            }
            Toast.makeText(this.context, C0136R.string.network_exception, 0).show();
        } else if (v.getId() == C0136R.id.more_rulesss) {
            this.context.startActivity(new Intent(this.context, RegRuleActivity.class).putExtra("isShow", false));
            ((Activity) this.context).overridePendingTransition(0, 0);
        } else if (v.getId() == C0136R.id.rule_statment) {
            initPopupWindow();
        }
    }

    public Intent getPdfFileIntent(String path) {
        Intent i = new Intent("android.intent.action.VIEW");
        i.addCategory("android.intent.category.DEFAULT");
        i.addFlags(268435456);
        i.setDataAndType(Uri.fromFile(new File(path)), "application/pdf");
        return i;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.aboutDialog == null) {
            this.aboutDialog = new Builder(this.context).create();
        }
        if (getResources().getConfiguration().orientation == 2) {
            this.aboutDialog.setContentView(AboutDialog(), new LayoutParams((this.width * 58) / ISNEW, (this.height * 84) / ISNEW));
        } else if (getResources().getConfiguration().orientation == 1) {
            this.aboutDialog.setContentView(AboutDialog(), new LayoutParams(-1, -1));
        }
    }
}
