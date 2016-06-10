package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.car.result.GetEndUserFullResult;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.webservice.UserServiceClient;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.platformtools.Util;
import com.tencent.mm.sdk.plugin.BaseProfile;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.http.util.EncodingUtils;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class DiagnoseDetailActivity extends Activity implements OnClickListener {
    private ImageView back;
    private Bitmap bm;
    private Context context;
    private ImageView del;
    private int id;
    private long lastTime;
    private mBroadcastReceiver mReceiver;
    private String mail;
    private IntentFilter myIntentFilter;
    private String name;
    private ProgressDialog progressDialogs;
    private ImageView rename;
    private String f1293s;
    private String sdCardDir;
    private ImageView share;
    private String type;
    private String verLocal;

    /* renamed from: com.ifoer.expeditionphone.DiagnoseDetailActivity.1 */
    class C05331 implements DialogInterface.OnClickListener {
        C05331() {
        }

        public void onClick(DialogInterface dialog, int which) {
            try {
                deleteFolderFile(DiagnoseDetailActivity.this.f1293s, true);
                if (DiagnoseDetailActivity.this.bm != null) {
                    DiagnoseDetailActivity.this.bm.recycle();
                }
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void deleteFolderFile(String filePath, boolean deleteThisPath) {
            if (DBDao.getInstance(DiagnoseDetailActivity.this.context).deleteReport(DiagnoseDetailActivity.this.id, MainActivity.database) > 0 && !TextUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File absolutePath : files) {
                        deleteFolderFile(absolutePath.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
                DiagnoseDetailActivity.this.finish();
                Toast.makeText(DiagnoseDetailActivity.this.context, C0136R.string.log_succcess, 0).show();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DiagnoseDetailActivity.2 */
    class C05342 implements DialogInterface.OnClickListener {
        private final /* synthetic */ EditText val$infos;

        C05342(EditText editText) {
            this.val$infos = editText;
        }

        public void onClick(DialogInterface dialog, int which) {
            try {
                Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                field.setAccessible(true);
                field.set(dialog, Boolean.valueOf(false));
            } catch (Exception e) {
                e.printStackTrace();
            }
            File file = new File(DiagnoseDetailActivity.this.f1293s);
            DiagnoseDetailActivity.this.name = this.val$infos.getText().toString();
            String newPath = new StringBuilder(String.valueOf(DiagnoseDetailActivity.this.sdCardDir)).append(DiagnoseDetailActivity.this.name).append(DiagnoseDetailActivity.this.type).toString();
            if (new File(newPath).exists()) {
                Toast.makeText(DiagnoseDetailActivity.this.context, C0136R.string.renaem_error, 0).show();
            } else if (DiagnoseDetailActivity.this.name.length() >= 21) {
                Toast.makeText(DiagnoseDetailActivity.this.context, C0136R.string.out_of_Length, 0).show();
            } else if (DiagnoseDetailActivity.this.nameRule(DiagnoseDetailActivity.this.name)) {
                file.renameTo(new File(newPath));
                file.delete();
                try {
                    Field fields = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                    fields.setAccessible(true);
                    fields.set(dialog, Boolean.valueOf(true));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (DBDao.getInstance(DiagnoseDetailActivity.this.context).UpdateReport(new StringBuilder(String.valueOf(DiagnoseDetailActivity.this.id)).toString(), new StringBuilder(String.valueOf(DiagnoseDetailActivity.this.name)).append(DiagnoseDetailActivity.this.type).toString(), newPath, MainActivity.database) > 0) {
                    Toast.makeText(DiagnoseDetailActivity.this.context, C0136R.string.log_succcess, 0).show();
                }
            } else {
                Toast.makeText(DiagnoseDetailActivity.this.context, C0136R.string.can_not_contain_special, 0).show();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DiagnoseDetailActivity.3 */
    class C05353 implements DialogInterface.OnClickListener {
        C05353() {
        }

        public void onClick(DialogInterface dialog, int which) {
            try {
                Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                field.setAccessible(true);
                field.set(dialog, Boolean.valueOf(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class GetDataAsy extends AsyncTask<String, String, String> {
        GetEndUserFullResult getEndUserFullResult;

        GetDataAsy() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            DiagnoseDetailActivity.this.progressDialogs = new ProgressDialog(DiagnoseDetailActivity.this.context);
            DiagnoseDetailActivity.this.progressDialogs.setMessage(DiagnoseDetailActivity.this.getResources().getString(C0136R.string.find_wait));
            DiagnoseDetailActivity.this.progressDialogs.setCancelable(false);
            DiagnoseDetailActivity.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            UserServiceClient client = new UserServiceClient();
            String cc = MySharedPreferences.getStringValue(DiagnoseDetailActivity.this.context, MySharedPreferences.CCKey);
            String token = MySharedPreferences.getStringValue(DiagnoseDetailActivity.this.context, MySharedPreferences.TokenKey);
            client.setCc(cc);
            client.setToken(token);
            try {
                this.getEndUserFullResult = client.getEndUserFull(cc);
            } catch (SocketTimeoutException e) {
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.getEndUserFullResult == null) {
                DiagnoseDetailActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == -1) {
                DiagnoseDetailActivity.this.progressDialogs.dismiss();
                SimpleDialog.validTokenDialog(DiagnoseDetailActivity.this.context);
            } else if (this.getEndUserFullResult.getCode() == 0) {
                DiagnoseDetailActivity.this.progressDialogs.dismiss();
                if (!XmlPullParser.NO_NAMESPACE.equals(this.getEndUserFullResult.getEmail()) && this.getEndUserFullResult.getEmail() != null) {
                    DiagnoseDetailActivity.this.mail = this.getEndUserFullResult.getEmail().toString();
                }
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_PARAMETER) {
                Toast.makeText(DiagnoseDetailActivity.this.context, DiagnoseDetailActivity.this.context.getResources().getString(C0136R.string.input_wrong), 1).show();
                DiagnoseDetailActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_PARAMETER_EMPTY) {
                Toast.makeText(DiagnoseDetailActivity.this.context, DiagnoseDetailActivity.this.context.getResources().getString(C0136R.string.request_wrong), 1).show();
                DiagnoseDetailActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_PARAMETER_ILLEGAL) {
                Toast.makeText(DiagnoseDetailActivity.this.context, DiagnoseDetailActivity.this.context.getResources().getString(C0136R.string.request_legal), 1).show();
                DiagnoseDetailActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_RESULT_NOT_EXIST) {
                Toast.makeText(DiagnoseDetailActivity.this.context, DiagnoseDetailActivity.this.context.getResources().getString(C0136R.string.request_result_null), 1).show();
                DiagnoseDetailActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_SERVER) {
                Toast.makeText(DiagnoseDetailActivity.this.context, DiagnoseDetailActivity.this.context.getResources().getString(C0136R.string.notic_serv), 1).show();
                DiagnoseDetailActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_NETWORK) {
                Toast.makeText(DiagnoseDetailActivity.this.context, DiagnoseDetailActivity.this.context.getResources().getString(C0136R.string.network), 1).show();
                DiagnoseDetailActivity.this.progressDialogs.dismiss();
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                DiagnoseDetailActivity.this.finish();
                DiagnoseDetailActivity.this.overridePendingTransition(0, 0);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - DiagnoseDetailActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                DiagnoseDetailActivity.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    public DiagnoseDetailActivity() {
        this.f1293s = XmlPullParser.NO_NAMESPACE;
        this.sdCardDir = XmlPullParser.NO_NAMESPACE;
        this.name = null;
        this.type = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.diagnose_report_detail);
        MyApplication.getInstance().addActivity(this);
        inintResource();
        registerBoradcastReceiver();
    }

    private void inintResource() {
        this.context = this;
        this.sdCardDir = Constant.DST_FILE;
        Bundle bundle = getIntent().getBundleExtra("bundle");
        this.id = bundle.getInt(LocaleUtil.INDONESIAN);
        this.f1293s = bundle.getString("path");
        this.del = (ImageView) findViewById(C0136R.id.del);
        this.rename = (ImageView) findViewById(C0136R.id.rename);
        this.share = (ImageView) findViewById(C0136R.id.share);
        this.del.setOnClickListener(this);
        this.rename.setOnClickListener(this);
        this.share.setOnClickListener(this);
        this.back = (ImageView) findViewById(C0136R.id.toright);
        this.back.setOnClickListener(this);
        if (this.id == -2) {
            this.del.setVisibility(8);
            this.rename.setVisibility(8);
        }
        if (this.f1293s.substring(this.f1293s.length() - 4, this.f1293s.length()).equalsIgnoreCase(".txt")) {
            this.type = ".txt";
            ((TextView) findViewById(C0136R.id.texts)).setText(readFileSdcard(this.f1293s));
            return;
        }
        this.type = Util.PHOTO_DEFAULT_EXT;
        ImageView images = (ImageView) findViewById(C0136R.id.images);
        this.bm = BitmapFactory.decodeFile(this.f1293s);
        images.setImageBitmap(this.bm);
    }

    private String readFileSdcard(String fileName) {
        String res = XmlPullParser.NO_NAMESPACE;
        try {
            FileInputStream fin = new FileInputStream(fileName);
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, AsyncHttpResponseHandler.DEFAULT_CHARSET);
            fin.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mReceiver != null) {
            unregisterReceiver(this.mReceiver);
        }
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.del) {
            new Builder(this.context).setTitle(C0136R.string.is_del).setPositiveButton(C0136R.string.enter, new C05331()).setNegativeButton(C0136R.string.cancel, null).show();
            return;
        }
        if (v.getId() == C0136R.id.rename) {
            showEditDialog();
            return;
        }
        if (v.getId() == C0136R.id.share) {
            String packageName = this.context.getPackageName();
            try {
                this.verLocal = this.context.getPackageManager().getPackageInfo(packageName, 0).versionName;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            Date date = new Date();
            String date2 = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(r16);
            File file = new File(this.f1293s);
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            List<ResolveInfo> resInfo = this.context.getPackageManager().queryIntentActivities(intent, 0);
            if (!resInfo.isEmpty()) {
                List<Intent> targetedShareIntents = new ArrayList();
                String[] tos = new String[1];
                tos[0] = this.mail;
                for (ResolveInfo info : resInfo) {
                    Intent targeted = new Intent("android.intent.action.SEND");
                    targeted.setType("text/plain");
                    ActivityInfo activityInfo = info.activityInfo;
                    if (!activityInfo.packageName.contains("bluetooth")) {
                        if (!activityInfo.packageName.contains(BaseProfile.COL_WEIBO)) {
                            if (file.getName().endsWith(Util.PHOTO_DEFAULT_EXT)) {
                                targeted.setType("image/*");
                            } else if (file.getName().endsWith(".txt")) {
                                targeted.setType("text/plain");
                            } else {
                                targeted.setType("application/octet-stream");
                            }
                            targeted.putExtra("subject", file.getName());
                            targeted.putExtra("android.intent.extra.EMAIL", tos);
                            StringBuilder append = new StringBuilder("V").append(this.verLocal).append("  DATE:");
                            targeted.putExtra("android.intent.extra.TEXT", r18.append(date2).toString());
                            targeted.putExtra("android.intent.extra.SUBJECT", "\u8bca\u65ad\u62a5\u544a");
                            targeted.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                            targeted.setPackage(activityInfo.packageName);
                            targetedShareIntents.add(targeted);
                        }
                    }
                }
                Intent chooserIntent = Intent.createChooser((Intent) targetedShareIntents.remove(0), this.context.getString(C0136R.string.file_common));
                if (chooserIntent != null) {
                    chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) targetedShareIntents.toArray(new Parcelable[0]));
                    try {
                        this.context.startActivity(chooserIntent);
                        return;
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                        return;
                    }
                }
                return;
            }
            return;
        }
        if (v.getId() == C0136R.id.toright) {
            finish();
            if (this.bm != null) {
                this.bm.recycle();
            }
        }
    }

    private void showEditDialog() {
        View view = ((Activity) this.context).getLayoutInflater().inflate(C0136R.layout.diagnose, null);
        ((TextView) view.findViewById(C0136R.id.title)).setVisibility(8);
        ((TextView) view.findViewById(C0136R.id.context)).setVisibility(8);
        EditText infos = (EditText) view.findViewById(C0136R.id.info);
        Builder buidler = new Builder(this.context);
        buidler.setTitle(this.context.getResources().getText(C0136R.string.edit));
        buidler.setTitle(C0136R.string.rename);
        buidler.setView(view);
        buidler.setCancelable(false);
        buidler.setPositiveButton(this.context.getResources().getText(C0136R.string.sure), new C05342(infos));
        buidler.setNegativeButton(this.context.getResources().getText(C0136R.string.cancel), new C05353());
        buidler.show();
    }

    private boolean nameRule(String name) {
        Pattern p;
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("ZH")) {
            p = Pattern.compile("^[\u4e00-\u9fa5A-Za-z0-9_]+$");
        } else if (Locale.getDefault().getLanguage().equalsIgnoreCase("RU")) {
            p = Pattern.compile("^[\u0419\u0426\u0423\u041a\u0415\u041d\u0413\u0428\u0429\u0417\u0425\u0424\u042b\u0412\u0410\u041f\u0420\u041e\u041b\u0414\u0416\u042d\u042f\u0427\u0421\u041c\u0418\u0422\u042c\u0411\u042e\u0439\u0446\u0443\u043a\u0435\u043d\u0433\u0448\u0449\u0437\u0445\u0444\u044b\u0432\u0430\u043f\u0440\u043e\u043b\u0434\u0436\u044d\u044f\u0447\u0441\u043c\u0438\u0442\u044c\u0431\u044eA-Za-z0-9_]+$");
        } else {
            p = Pattern.compile("^[A-Za-z0-9_]+$");
        }
        return p.matcher(name).matches();
    }

    private void registerBoradcastReceiver() {
        this.mReceiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.mReceiver, this.myIntentFilter);
    }
}
