package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.webservice.UserServiceClient;
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

public class ReadFile extends MySpaceManagermentLayout implements OnClickListener {
    private ImageView back;
    private View baseView;
    private Bitmap bm;
    private Bundle bundle;
    Context context;
    private ImageView del;
    private int id;
    private String mail;
    private String name;
    ProgressDialog progressDialogs;
    private ImageView rename;
    private String f2130s;
    private String sdCardDir;
    private ImageView share;
    private String type;
    private String verLocal;

    /* renamed from: com.ifoer.expeditionphone.ReadFile.1 */
    class C06191 implements DialogInterface.OnClickListener {
        C06191() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (DBDao.getInstance(ReadFile.this.context).deleteReport(ReadFile.this.id, MainActivity.database) > 0) {
                Toast.makeText(ReadFile.this.context, C0136R.string.log_succcess, 0).show();
            }
            try {
                ReadFile.this.deleteFolderFile(ReadFile.this.f2130s, false);
                MainActivity.panel.removePanelContainer();
                MainActivity.panel.fillPanelContainer(new SpaceDiagnosticReportLayout(ReadFile.this.context));
                MainActivity.panel.openthreePanelContainer();
                if (ReadFile.this.bm != null) {
                    ReadFile.this.bm.recycle();
                }
                dialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ReadFile.2 */
    class C06202 implements DialogInterface.OnClickListener {
        private final /* synthetic */ EditText val$infos;

        C06202(EditText editText) {
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
            File file = new File(ReadFile.this.f2130s);
            ReadFile.this.name = this.val$infos.getText().toString();
            String newPath = new StringBuilder(String.valueOf(ReadFile.this.sdCardDir)).append(ReadFile.this.name).append(ReadFile.this.type).toString();
            if (new File(newPath).exists()) {
                Toast.makeText(ReadFile.this.context, C0136R.string.renaem_error, 0).show();
            } else if (ReadFile.this.name.length() >= 21) {
                Toast.makeText(ReadFile.this.context, C0136R.string.out_of_Length, 0).show();
            } else if (ReadFile.this.nameRule(ReadFile.this.name)) {
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
                if (DBDao.getInstance(ReadFile.this.context).UpdateReport(new StringBuilder(String.valueOf(ReadFile.this.id)).toString(), new StringBuilder(String.valueOf(ReadFile.this.name)).append(ReadFile.this.type).toString(), newPath, MainActivity.database) > 0) {
                    Toast.makeText(ReadFile.this.context, C0136R.string.log_succcess, 0).show();
                }
            } else {
                Toast.makeText(ReadFile.this.context, C0136R.string.can_not_contain_special, 0).show();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ReadFile.3 */
    class C06213 implements DialogInterface.OnClickListener {
        C06213() {
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
            ReadFile.this.progressDialogs = new ProgressDialog(ReadFile.this.context);
            ReadFile.this.progressDialogs.setMessage(ReadFile.this.getResources().getString(C0136R.string.find_wait));
            ReadFile.this.progressDialogs.setCancelable(false);
            ReadFile.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            UserServiceClient client = new UserServiceClient();
            String cc = MySharedPreferences.getStringValue(ReadFile.this.context, MySharedPreferences.CCKey);
            String token = MySharedPreferences.getStringValue(ReadFile.this.context, MySharedPreferences.TokenKey);
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
                ReadFile.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == -1) {
                ReadFile.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == 0) {
                ReadFile.this.progressDialogs.dismiss();
                if (!XmlPullParser.NO_NAMESPACE.equals(this.getEndUserFullResult.getEmail()) && this.getEndUserFullResult.getEmail() != null) {
                    ReadFile.this.mail = this.getEndUserFullResult.getEmail().toString();
                }
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_PARAMETER) {
                Toast.makeText(ReadFile.this.context, ReadFile.this.context.getResources().getString(C0136R.string.input_wrong), 1).show();
                ReadFile.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_PARAMETER_EMPTY) {
                Toast.makeText(ReadFile.this.context, ReadFile.this.context.getResources().getString(C0136R.string.request_wrong), 1).show();
                ReadFile.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_PARAMETER_ILLEGAL) {
                Toast.makeText(ReadFile.this.context, ReadFile.this.context.getResources().getString(C0136R.string.request_legal), 1).show();
                ReadFile.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_RESULT_NOT_EXIST) {
                Toast.makeText(ReadFile.this.context, ReadFile.this.context.getResources().getString(C0136R.string.request_result_null), 1).show();
                ReadFile.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_SERVER) {
                Toast.makeText(ReadFile.this.context, ReadFile.this.context.getResources().getString(C0136R.string.notic_serv), 1).show();
                ReadFile.this.progressDialogs.dismiss();
            } else if (this.getEndUserFullResult.getCode() == MyHttpException.ERROR_NETWORK) {
                Toast.makeText(ReadFile.this.context, ReadFile.this.context.getResources().getString(C0136R.string.network), 1).show();
                ReadFile.this.progressDialogs.dismiss();
            }
        }
    }

    public ReadFile(int id, String s, Context context) {
        super(context);
        this.sdCardDir = Constant.DST_FILE;
        this.name = null;
        this.type = null;
        this.context = context;
        this.f2130s = s;
        this.id = id;
        this.baseView = ((Activity) context).getLayoutInflater().inflate(C0136R.layout.show_files, this);
        init();
    }

    private void init() {
        new GetDataAsy().execute(new String[0]);
        if (this.f2130s.substring(this.f2130s.length() - 4, this.f2130s.length()).equalsIgnoreCase(".txt")) {
            this.type = ".txt";
            ((TextView) this.baseView.findViewById(C0136R.id.texts)).setText(readFileSdcard(this.f2130s));
        } else {
            this.type = Util.PHOTO_DEFAULT_EXT;
            ImageView images = (ImageView) this.baseView.findViewById(C0136R.id.images);
            this.bm = BitmapFactory.decodeFile(this.f2130s);
            images.setImageBitmap(this.bm);
        }
        this.back = (ImageView) this.baseView.findViewById(C0136R.id.toright);
        this.del = (ImageView) this.baseView.findViewById(C0136R.id.del);
        this.rename = (ImageView) this.baseView.findViewById(C0136R.id.rename);
        this.share = (ImageView) this.baseView.findViewById(C0136R.id.share);
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

    public void onClick(View v) {
        if (v.getId() == C0136R.id.toright) {
            MainActivity.panel.removePanelContainer();
            MainActivity.panel.fillPanelContainer(new SpaceDiagnosticReportLayout(this.context));
            MainActivity.panel.openthreePanelContainer();
            if (this.bm != null) {
                this.bm.recycle();
            }
        } else if (v.getId() == C0136R.id.del) {
            new Builder(this.context).setTitle(C0136R.string.is_del).setPositiveButton(C0136R.string.enter, new C06191()).setNegativeButton(C0136R.string.cancel, null).show();
        } else if (v.getId() == C0136R.id.rename) {
            showEditDialog();
        } else if (v.getId() == C0136R.id.share) {
            try {
                this.verLocal = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionName;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            String date = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date());
            File file = new File(this.f2130s);
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
        buidler.setPositiveButton(this.context.getResources().getText(C0136R.string.sure), new C06202(infos));
        buidler.setNegativeButton(this.context.getResources().getText(C0136R.string.cancel), new C06213());
        buidler.show();
    }

    public boolean nameRule(String name) {
        Pattern p;
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("ZH")) {
            p = Pattern.compile("^[\u4e00-\u9fa5A-Za-z0-9_]+$");
        } else {
            p = Pattern.compile("^[A-Za-z0-9_]+$");
        }
        return p.matcher(name).matches();
    }
}
