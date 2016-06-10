package com.ifoer.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.mine.Contact;
import com.ifoer.util.IPInfoUtil;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.util.EncodingUtils;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class GetConfigTool {
    public static String APP_ID;
    public static String CONFIG_NAME;
    public static String CONFIG_URL;
    public static String IM_APP_ID;
    public static String IM_CONFIG_NAME;
    public static String config_json;
    public static String im_config_json;
    public static String tempArea;
    private String TEMP_APP_ID;
    private ArrayList<ConfigBean> config_object;
    String mAreaID;
    private Context mContext;
    private SharedPreferences sp;

    /* renamed from: com.ifoer.service.GetConfigTool.1 */
    class C06871 extends Thread {
        C06871() {
        }

        public void run() {
            IPInfoUtil ipUitl = new IPInfoUtil();
            GetConfigTool.this.mAreaID = ipUitl.ipToArea(ipUitl.getNetIp());
        }
    }

    public class ConfigBean {
        private String key;
        private String value;

        public String getKey() {
            return this.key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    static {
        CONFIG_URL = Constant.CONFIG_WEBSITE_PATH + "/?action=config_service.urls&app_id=" + Constant.APP_ID + "&config_no=0.0.0&area=cn";
        IM_APP_ID = "2013120200000002";
        APP_ID = Constant.APP_ID;
        CONFIG_NAME = "Config.json";
        IM_CONFIG_NAME = "IMConfig.json";
        config_json = null;
        im_config_json = null;
    }

    public GetConfigTool(Context context) {
        this.config_object = null;
        this.mAreaID = Contact.RELATION_FRIEND;
        this.mContext = context;
        this.sp = PreferenceManager.getDefaultSharedPreferences(this.mContext);
    }

    private String getUrl(String app_id) {
        String config_no = this.sp.getString("config_no", Contact.RELATION_FRIEND);
        if (app_id.equals(IM_APP_ID)) {
            config_no = this.sp.getString("im_config_no", Contact.RELATION_FRIEND);
        }
        int config = Integer.parseInt(config_no) - 1;
        String area = XmlPullParser.NO_NAMESPACE;
        if (tempArea == null || tempArea.equals(XmlPullParser.NO_NAMESPACE)) {
            area = getArea();
        } else {
            area = tempArea;
        }
        CONFIG_URL = Constant.CONFIG_WEBSITE_PATH + "/?action=config_service.urls&app_id=" + app_id + "&config_no=" + config + "&area=" + area;
        Log.i("nxy", "CONFIG_URL" + CONFIG_URL);
        return CONFIG_URL;
    }

    public int configJsonToBean(String content, String config_name) {
        JSONObject object;
        if (this.config_object == null) {
            this.config_object = new ArrayList();
        }
        int count = 0;
        if (content == null) {
            try {
                FileInputStream fin = this.mContext.openFileInput(config_name);
                byte[] buffer = new byte[fin.available()];
                fin.read(buffer);
                object = new JSONObject(EncodingUtils.getString(buffer, codetype(buffer)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            object = new JSONObject(content);
        }
        if (object.has(DataPacketExtension.ELEMENT_NAME) && !object.getString(DataPacketExtension.ELEMENT_NAME).equals("null")) {
            JSONArray urlsArray = object.getJSONObject(DataPacketExtension.ELEMENT_NAME).getJSONArray("urls");
            count = urlsArray.length();
            for (int i = 0; i < count; i++) {
                JSONObject valueObject = urlsArray.getJSONObject(i);
                ConfigBean bean = new ConfigBean();
                bean.setKey(valueObject.getString(SharedPref.KEY).toString());
                bean.setValue(valueObject.getString(SharedPref.VALUE).toString());
                this.config_object.add(bean);
            }
        }
        return count;
    }

    public InputStream getInputStream(String app_id) {
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(getUrl(app_id)).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            conn.connect();
            is = conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    public String getWebText(String app_id) {
        Exception e;
        Throwable th;
        InputStream is = getInputStream(app_id);
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        String s = XmlPullParser.NO_NAMESPACE;
        try {
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is));
            while (true) {
                try {
                    s = br2.readLine();
                    if (s == null) {
                        try {
                            break;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            br = br2;
                        }
                    } else {
                        sb.append(s);
                    }
                } catch (Exception e3) {
                    e2 = e3;
                    br = br2;
                } catch (Throwable th2) {
                    th = th2;
                    br = br2;
                }
            }
            br2.close();
            is.close();
            br = br2;
        } catch (Exception e4) {
            e2 = e4;
            try {
                e2.printStackTrace();
                Log.e("TAG", "\u6d41\u6587\u4ef6\u8bfb\u5199\u9519\u8bef");
                try {
                    br.close();
                    is.close();
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
                return sb.toString();
            } catch (Throwable th3) {
                th = th3;
                try {
                    br.close();
                    is.close();
                } catch (Exception e222) {
                    e222.printStackTrace();
                }
                throw th;
            }
        }
        return sb.toString();
    }

    public void createFile(InputStream is, String filepath, String filename) {
        FileNotFoundException e;
        IOException e2;
        try {
            OutputStream os = new FileOutputStream(new File(new StringBuilder(String.valueOf(filepath)).append(FilePathGenerator.ANDROID_DIR_SEP).append(filename).toString()));
            OutputStream outputStream;
            try {
                byte[] buffer = new byte[Flags.FLAG5];
                while (true) {
                    int len = is.read(buffer);
                    if (len == -1) {
                        outputStream = os;
                        return;
                    }
                    os.write(buffer, 0, len);
                }
            } catch (FileNotFoundException e3) {
                e = e3;
                outputStream = os;
                e.printStackTrace();
            } catch (IOException e4) {
                e2 = e4;
                outputStream = os;
                e2.printStackTrace();
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
        } catch (IOException e6) {
            e2 = e6;
            e2.printStackTrace();
        }
    }

    public void downloader(InputStream is, String path, String filename) {
        String filepath = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
            filepath = Environment.getExternalStorageDirectory() + path;
        } else {
            Toast.makeText(this.mContext, C0136R.string.sdcard_abnormal, 0).show();
        }
        if (!filepathExist(filepath)) {
            createFilepath(filepath);
        }
        if (!fileExist(new StringBuilder(String.valueOf(filepath)).append(FilePathGenerator.ANDROID_DIR_SEP).append(filename).toString())) {
            createFile(is, filepath, filename);
        }
    }

    public boolean filepathExist(String filepath) {
        return new File(filepath).exists();
    }

    public void createFilepath(String filepath) {
        new File(filepath).mkdirs();
    }

    public boolean fileExist(String filename) {
        return filepathExist(filename);
    }

    public void loadToSdcard(String url, String filepath, String filename) {
        Log.e("GetConfigTool nxy", "\u4e0b\u8f7d\u751f\u6210\u6587\u4ef6");
        downloader(getInputStream(url), filepath, filename);
    }

    public boolean loadToLocation(String config_name, String app_id) {
        Log.i("GetConfigTo", "loadToLocation \u4fdd\u5b58\u914d\u7f6e\u6587\u4ef6");
        this.TEMP_APP_ID = app_id;
        return checkJsonContent(getWebText(app_id), config_name);
    }

    public void refershConfiList() {
        this.config_object = null;
        configJsonToBean(config_json, CONFIG_NAME);
    }

    public String search(String key) {
        if (this.config_object == null) {
            configJsonToBean(config_json, CONFIG_NAME);
        }
        for (int i = 0; i < this.config_object.size(); i++) {
            if (((ConfigBean) this.config_object.get(i)).getKey().toLowerCase().equals(key.toLowerCase())) {
                return ((ConfigBean) this.config_object.get(i)).getValue();
            }
        }
        return null;
    }

    public void sendIMConfig() {
        Intent intent = new Intent("IM_CONFIG_CONTENT");
        intent.putExtra("IM_CONFIG_CONTENT", getConfigContent(IM_CONFIG_NAME));
        this.mContext.sendBroadcast(intent);
    }

    public String getConfigContent(String config_name) {
        String result = XmlPullParser.NO_NAMESPACE;
        FileInputStream fin = null;
        try {
            fin = this.mContext.openFileInput(config_name);
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            result = EncodingUtils.getString(buffer, codetype(buffer));
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
        return result;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean checkJsonContent(java.lang.String r18, java.lang.String r19) {
        /*
        r17 = this;
        r7 = new org.json.JSONObject;	 Catch:{ Exception -> 0x007c }
        r0 = r18;
        r7.<init>(r0);	 Catch:{ Exception -> 0x007c }
        r13 = "code";
        r13 = r7.has(r13);	 Catch:{ Exception -> 0x007c }
        if (r13 == 0) goto L_0x0018;
    L_0x000f:
        r13 = "code";
        r3 = r7.getInt(r13);	 Catch:{ Exception -> 0x007c }
        switch(r3) {
            case 0: goto L_0x001a;
            default: goto L_0x0018;
        };	 Catch:{ Exception -> 0x007c }
    L_0x0018:
        r13 = 0;
    L_0x0019:
        return r13;
    L_0x001a:
        r0 = r17;
        r13 = r0.mContext;	 Catch:{ Exception -> 0x007c }
        r12 = android.preference.PreferenceManager.getDefaultSharedPreferences(r13);	 Catch:{ Exception -> 0x007c }
        r13 = CONFIG_NAME;	 Catch:{ Exception -> 0x007c }
        r0 = r19;
        r13 = r0.equals(r13);	 Catch:{ Exception -> 0x007c }
        if (r13 == 0) goto L_0x0081;
    L_0x002c:
        config_json = r18;	 Catch:{ Exception -> 0x007c }
        r13 = "config_no";
        r14 = "1";
        r13 = r12.getString(r13, r14);	 Catch:{ Exception -> 0x007c }
        r9 = java.lang.Integer.parseInt(r13);	 Catch:{ Exception -> 0x007c }
    L_0x003a:
        r13 = "data";
        r4 = r7.getJSONObject(r13);	 Catch:{ Exception -> 0x007c }
        r13 = "version";
        r13 = r4.getString(r13);	 Catch:{ Exception -> 0x007c }
        r11 = java.lang.Integer.parseInt(r13);	 Catch:{ Exception -> 0x007c }
        if (r9 >= r11) goto L_0x00ab;
    L_0x004c:
        r13 = CONFIG_NAME;	 Catch:{ Exception -> 0x007c }
        r0 = r19;
        r13 = r0.equals(r13);	 Catch:{ Exception -> 0x007c }
        if (r13 == 0) goto L_0x0090;
    L_0x0056:
        r13 = r12.edit();	 Catch:{ Exception -> 0x007c }
        r14 = "config_no";
        r15 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x007c }
        r16 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x007c }
        r15.<init>(r16);	 Catch:{ Exception -> 0x007c }
        r15 = r15.toString();	 Catch:{ Exception -> 0x007c }
        r13 = r13.putString(r14, r15);	 Catch:{ Exception -> 0x007c }
        r13.commit();	 Catch:{ Exception -> 0x007c }
    L_0x0070:
        r0 = r17;
        r13 = r0.TEMP_APP_ID;	 Catch:{ Exception -> 0x007c }
        r0 = r17;
        r1 = r19;
        r0.loadToLocation(r1, r13);	 Catch:{ Exception -> 0x007c }
        goto L_0x0018;
    L_0x007c:
        r5 = move-exception;
        r5.printStackTrace();
        goto L_0x0018;
    L_0x0081:
        im_config_json = r18;	 Catch:{ Exception -> 0x007c }
        r13 = "im_config_no";
        r14 = "1";
        r13 = r12.getString(r13, r14);	 Catch:{ Exception -> 0x007c }
        r9 = java.lang.Integer.parseInt(r13);	 Catch:{ Exception -> 0x007c }
        goto L_0x003a;
    L_0x0090:
        r13 = r12.edit();	 Catch:{ Exception -> 0x007c }
        r14 = "im_config_no";
        r15 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x007c }
        r16 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x007c }
        r15.<init>(r16);	 Catch:{ Exception -> 0x007c }
        r15 = r15.toString();	 Catch:{ Exception -> 0x007c }
        r13 = r13.putString(r14, r15);	 Catch:{ Exception -> 0x007c }
        r13.commit();	 Catch:{ Exception -> 0x007c }
        goto L_0x0070;
    L_0x00ab:
        r0 = r17;
        r13 = r0.mContext;	 Catch:{ Exception -> 0x007c }
        r14 = 3;
        r0 = r19;
        r10 = r13.openFileOutput(r0, r14);	 Catch:{ Exception -> 0x007c }
        r6 = new java.io.ByteArrayInputStream;	 Catch:{ Exception -> 0x007c }
        r13 = r7.toString();	 Catch:{ Exception -> 0x007c }
        r13 = r13.getBytes();	 Catch:{ Exception -> 0x007c }
        r6.<init>(r13);	 Catch:{ Exception -> 0x007c }
        r8 = 0;
        r13 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r2 = new byte[r13];	 Catch:{ Exception -> 0x007c }
    L_0x00c8:
        r8 = r6.read(r2);	 Catch:{ Exception -> 0x007c }
        r13 = -1;
        if (r8 != r13) goto L_0x0110;
    L_0x00cf:
        r6.close();	 Catch:{ Exception -> 0x007c }
        r10.close();	 Catch:{ Exception -> 0x007c }
        r13 = CONFIG_NAME;	 Catch:{ Exception -> 0x007c }
        r0 = r19;
        r13 = r0.equals(r13);	 Catch:{ Exception -> 0x007c }
        if (r13 == 0) goto L_0x0115;
    L_0x00df:
        r17.configJsonToBean(r18, r19);	 Catch:{ Exception -> 0x007c }
        r13 = r12.edit();	 Catch:{ Exception -> 0x007c }
        r14 = "config_no";
        r15 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x007c }
        r16 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x007c }
        r15.<init>(r16);	 Catch:{ Exception -> 0x007c }
        r15 = r15.toString();	 Catch:{ Exception -> 0x007c }
        r13 = r13.putString(r14, r15);	 Catch:{ Exception -> 0x007c }
        r13.commit();	 Catch:{ Exception -> 0x007c }
        r13 = r12.edit();	 Catch:{ Exception -> 0x007c }
        r14 = "stamptime";
        r15 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x007c }
        r13 = r13.putLong(r14, r15);	 Catch:{ Exception -> 0x007c }
        r13.commit();	 Catch:{ Exception -> 0x007c }
    L_0x010d:
        r13 = 1;
        goto L_0x0019;
    L_0x0110:
        r13 = 0;
        r10.write(r2, r13, r8);	 Catch:{ Exception -> 0x007c }
        goto L_0x00c8;
    L_0x0115:
        r13 = r12.edit();	 Catch:{ Exception -> 0x007c }
        r14 = "im_config_no";
        r15 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x007c }
        r16 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x007c }
        r15.<init>(r16);	 Catch:{ Exception -> 0x007c }
        r15 = r15.toString();	 Catch:{ Exception -> 0x007c }
        r13 = r13.putString(r14, r15);	 Catch:{ Exception -> 0x007c }
        r13.commit();	 Catch:{ Exception -> 0x007c }
        goto L_0x010d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.service.GetConfigTool.checkJsonContent(java.lang.String, java.lang.String):boolean");
    }

    public int searchAll() {
        int count = 0;
        try {
            FileInputStream fin = this.mContext.openFileInput(CONFIG_NAME);
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            JSONObject object = new JSONObject(EncodingUtils.getString(buffer, codetype(buffer)));
            if (object.has(DataPacketExtension.ELEMENT_NAME) && !object.getString(DataPacketExtension.ELEMENT_NAME).equals("null")) {
                count = object.getJSONObject(DataPacketExtension.ELEMENT_NAME).getJSONArray("urls").length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    private String codetype(byte[] head) {
        String type = XmlPullParser.NO_NAMESPACE;
        byte[] codehead = new byte[3];
        System.arraycopy(head, 0, codehead, 0, 3);
        if (codehead[0] == (byte) -1 && codehead[1] == (byte) -2) {
            return "UTF-16";
        }
        if (codehead[0] == (byte) -2 && codehead[1] == (byte) -1) {
            return "UNICODE";
        }
        if (codehead[0] == -17 && codehead[1] == -69 && codehead[2] == -65) {
            return AsyncHttpResponseHandler.DEFAULT_CHARSET;
        }
        return "GB2312";
    }

    public String getConfigArea(String config_name) {
        String area = Contact.RELATION_FRIEND;
        try {
            FileInputStream fin = this.mContext.openFileInput(config_name);
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            area = new JSONObject(EncodingUtils.getString(buffer, codetype(buffer))).getJSONObject(DataPacketExtension.ELEMENT_NAME).getString("area");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return area;
    }

    private synchronized String getArea() {
        new C06871().start();
        return this.mAreaID;
    }
}
