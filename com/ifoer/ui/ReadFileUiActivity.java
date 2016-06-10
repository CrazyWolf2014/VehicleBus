package com.ifoer.ui;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.webservice.UserServiceClient;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.http.util.EncodingUtils;
import org.xmlpull.v1.XmlPullParser;

public class ReadFileUiActivity extends Activity implements OnClickListener {
    private ImageView back;
    private Bitmap bm;
    private Bundle bundle;
    Context context;
    private ImageView del;
    private int id;
    private long lastTime;
    private String mail;
    public IntentFilter myIntentFilter;
    private String name;
    ProgressDialog progressDialogs;
    public mBroadcastReceiver receiver;
    private ImageView rename;
    private String f1301s;
    private String sdCardDir;
    private ImageView share;
    private String type;
    private String verLocal;

    /* renamed from: com.ifoer.ui.ReadFileUiActivity.1 */
    class C07171 implements DialogInterface.OnClickListener {
        C07171() {
        }

        public void onClick(DialogInterface dialog, int which) {
            try {
                ReadFileUiActivity.this.deleteFolderFile(ReadFileUiActivity.this.f1301s, true);
                ReadFileUiActivity.this.setResult(1);
                ReadFileUiActivity.this.finish();
                if (ReadFileUiActivity.this.bm != null) {
                    ReadFileUiActivity.this.bm.recycle();
                }
                dialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.ifoer.ui.ReadFileUiActivity.2 */
    class C07182 implements DialogInterface.OnClickListener {
        private final /* synthetic */ EditText val$infos;

        C07182(EditText editText) {
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
            File file = new File(ReadFileUiActivity.this.f1301s);
            ReadFileUiActivity.this.name = this.val$infos.getText().toString();
            String newPath = new StringBuilder(String.valueOf(ReadFileUiActivity.this.sdCardDir)).append(ReadFileUiActivity.this.name).append(ReadFileUiActivity.this.type).toString();
            if (new File(newPath).exists()) {
                Toast.makeText(ReadFileUiActivity.this.context, C0136R.string.renaem_error, 0).show();
            } else if (ReadFileUiActivity.this.name.length() >= 21) {
                Toast.makeText(ReadFileUiActivity.this.context, C0136R.string.out_of_Length, 0).show();
            } else if (ReadFileUiActivity.this.nameRule(ReadFileUiActivity.this.name)) {
                System.out.println(newPath.toString());
                file.renameTo(new File(newPath));
                file.delete();
                try {
                    Field fields = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                    fields.setAccessible(true);
                    fields.set(dialog, Boolean.valueOf(true));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (DBDao.getInstance(ReadFileUiActivity.this.context).UpdateReport(new StringBuilder(String.valueOf(ReadFileUiActivity.this.id)).toString(), new StringBuilder(String.valueOf(ReadFileUiActivity.this.name)).append(ReadFileUiActivity.this.type).toString(), newPath, MainActivity.database) > 0) {
                    Toast.makeText(ReadFileUiActivity.this.context, C0136R.string.log_succcess, 0).show();
                }
            } else {
                Toast.makeText(ReadFileUiActivity.this.context, C0136R.string.can_not_contain_special, 0).show();
            }
        }
    }

    /* renamed from: com.ifoer.ui.ReadFileUiActivity.3 */
    class C07193 implements DialogInterface.OnClickListener {
        C07193() {
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
            ReadFileUiActivity.this.progressDialogs = new ProgressDialog(ReadFileUiActivity.this.context);
            ReadFileUiActivity.this.progressDialogs.setMessage(ReadFileUiActivity.this.getResources().getString(C0136R.string.find_wait));
            ReadFileUiActivity.this.progressDialogs.setCancelable(false);
            ReadFileUiActivity.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            UserServiceClient client = new UserServiceClient();
            String cc = MySharedPreferences.getStringValue(ReadFileUiActivity.this.context, MySharedPreferences.CCKey);
            String token = MySharedPreferences.getStringValue(ReadFileUiActivity.this.context, MySharedPreferences.TokenKey);
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
                ReadFileUiActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == -1) {
                ReadFileUiActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == 0) {
                ReadFileUiActivity.this.progressDialogs.dismiss();
                if (!XmlPullParser.NO_NAMESPACE.equals(this.getEndUserFullResult.getEmail()) && this.getEndUserFullResult.getEmail() != null) {
                    ReadFileUiActivity.this.mail = this.getEndUserFullResult.getEmail().toString();
                }
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_PARAMETER) {
                Toast.makeText(ReadFileUiActivity.this.context, ReadFileUiActivity.this.context.getResources().getString(C0136R.string.input_wrong), 1).show();
                ReadFileUiActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_PARAMETER_EMPTY) {
                Toast.makeText(ReadFileUiActivity.this.context, ReadFileUiActivity.this.context.getResources().getString(C0136R.string.request_wrong), 1).show();
                ReadFileUiActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_PARAMETER_ILLEGAL) {
                Toast.makeText(ReadFileUiActivity.this.context, ReadFileUiActivity.this.context.getResources().getString(C0136R.string.request_legal), 1).show();
                ReadFileUiActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_RESULT_NOT_EXIST) {
                Toast.makeText(ReadFileUiActivity.this.context, ReadFileUiActivity.this.context.getResources().getString(C0136R.string.request_result_null), 1).show();
                ReadFileUiActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_SERVER) {
                Toast.makeText(ReadFileUiActivity.this.context, ReadFileUiActivity.this.context.getResources().getString(C0136R.string.notic_serv), 1).show();
                ReadFileUiActivity.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_NETWORK) {
                Toast.makeText(ReadFileUiActivity.this.context, ReadFileUiActivity.this.context.getResources().getString(C0136R.string.network), 1).show();
                ReadFileUiActivity.this.progressDialogs.dismiss();
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - ReadFileUiActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                ReadFileUiActivity.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    public ReadFileUiActivity() {
        this.sdCardDir = Constant.DST_FILE;
        this.name = null;
        this.type = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.show_files);
        this.context = this;
        this.f1301s = getIntent().getStringExtra("path");
        this.id = getIntent().getIntExtra(LocaleUtil.INDONESIAN, 0);
        Log.e("ReadFileUiActivity", "s" + this.f1301s + LocaleUtil.INDONESIAN + this.id);
        init();
        registerBoradcastReceiver();
    }

    private void init() {
        if (this.f1301s.substring(this.f1301s.length() - 4, this.f1301s.length()).equalsIgnoreCase(".txt")) {
            this.type = ".txt";
            ((TextView) findViewById(C0136R.id.texts)).setText(readFileSdcard(this.f1301s));
        } else {
            this.type = Util.PHOTO_DEFAULT_EXT;
            ImageView images = (ImageView) findViewById(C0136R.id.images);
            this.bm = BitmapFactory.decodeFile(this.f1301s);
            images.setImageBitmap(this.bm);
        }
        this.back = (ImageView) findViewById(C0136R.id.toright);
        this.del = (ImageView) findViewById(C0136R.id.del);
        this.rename = (ImageView) findViewById(C0136R.id.rename);
        this.share = (ImageView) findViewById(C0136R.id.share);
        this.back.setOnClickListener(this);
        this.del.setOnClickListener(this);
        this.rename.setOnClickListener(this);
        this.share.setOnClickListener(this);
    }

    public String readFileSdcard(String fileName) {
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

    public void deleteFolderFile(String filePath, boolean deleteThisPath) throws IOException {
        if (DBDao.getInstance(this.context).deleteReport(this.id, MainActivity.database) > 0 && !TextUtils.isEmpty(filePath)) {
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
                Toast.makeText(this.context, C0136R.string.log_succcess, 0).show();
            }
        }
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.toright) {
            setResult(2);
            finish();
            if (this.bm != null) {
                this.bm.recycle();
            }
        } else if (v.getId() == C0136R.id.del) {
            new Builder(this.context).setTitle(C0136R.string.is_del).setPositiveButton(C0136R.string.enter, new C07171()).setNegativeButton(C0136R.string.cancel, null).show();
        } else if (v.getId() == C0136R.id.rename) {
            showEditDialog();
        } else if (v.getId() == C0136R.id.share) {
            try {
                this.verLocal = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionName;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            String date = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date());
            File file = new File(this.f1301s);
            Intent intent = new Intent("android.intent.action.SEND");
            String[] tos = new String[]{this.mail};
            intent.putExtra("subject", file.getName());
            intent.putExtra("android.intent.extra.EMAIL", tos);
            intent.putExtra("android.intent.extra.TEXT", "V" + this.verLocal + "  DATE:" + date);
            intent.putExtra("android.intent.extra.SUBJECT", "\u8bca\u65ad\u62a5\u544a");
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
            if (file.getName().endsWith(Util.PHOTO_DEFAULT_EXT)) {
                intent.setType("image/*");
            } else if (file.getName().endsWith(".txt")) {
                intent.setType("text/plain");
            } else {
                intent.setType("application/octet-stream");
            }
            this.context.startActivity(intent);
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
        buidler.setPositiveButton(this.context.getResources().getText(C0136R.string.sure), new C07182(infos));
        buidler.setNegativeButton(this.context.getResources().getText(C0136R.string.cancel), new C07193());
        buidler.show();
    }

    public boolean nameRule(String name) {
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

    public void onDestroy() {
        super.onDestroy();
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.receiver, this.myIntentFilter);
    }
}
