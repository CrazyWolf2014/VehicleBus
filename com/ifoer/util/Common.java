package com.ifoer.util;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.System;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.expeditionphone.WelcomeActivity;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.tencent.mm.sdk.platformtools.Util;
import com.thoughtworks.xstream.XStream;
import java.util.Locale;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;

public class Common {

    /* renamed from: com.ifoer.util.Common.1 */
    class C07431 implements OnClickListener {
        C07431() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static String replace(String from, String to, String str) {
        if (str != null) {
            return str.replaceAll(from, to);
        }
        return XmlPullParser.NO_NAMESPACE;
    }

    public static String getAndroidId(Context context) {
        return System.getString(context.getContentResolver(), "android_id");
    }

    public static boolean isMobileNO(String mobiles) {
        return Pattern.compile("^\\d{6,11}$").matcher(mobiles).matches();
    }

    public static boolean isEmail(String email) {
        return Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$").matcher(email).matches();
    }

    public static void showDialog(Context context, String title, String message, String buttonText) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(buttonText, new C07431());
        builder.show();
    }

    public static boolean isNumber(String str) {
        if (Pattern.compile("[0-9]*").matcher(str).matches()) {
            return true;
        }
        return false;
    }

    public static int getLocalLanguage(Context context) {
        String lanName = Locale.getDefault().getLanguage();
        if (lanName.equalsIgnoreCase("zh")) {
            return XStream.ID_REFERENCES;
        }
        if (lanName.equalsIgnoreCase(Util.ENGLISH)) {
            return XStream.NO_REFERENCES;
        }
        return XStream.NO_REFERENCES;
    }

    public static String getSdCardPath() {
        return Environment.getExternalStorageDirectory() + FilePathGenerator.ANDROID_DIR_SEP;
    }

    public static long getSDFreeSize() {
        StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((((long) sf.getAvailableBlocks()) * ((long) sf.getBlockSize())) / 1024) / 1024;
    }

    public static long getSDAllSize() {
        StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((((long) sf.getBlockCount()) * ((long) sf.getBlockSize())) / 1024) / 1024;
    }

    public static String getLanguageName(Context context, int lanId) {
        String languageName = XmlPullParser.NO_NAMESPACE;
        switch (lanId) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return context.getResources().getText(C0136R.string.deyu).toString();
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return context.getResources().getText(C0136R.string.riyu).toString();
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return context.getResources().getText(C0136R.string.eyu).toString();
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return context.getResources().getText(C0136R.string.fayu).toString();
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                return context.getResources().getText(C0136R.string.xibanya).toString();
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return context.getResources().getText(C0136R.string.putaoya).toString();
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                return context.getResources().getText(C0136R.string.bolan).toString();
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return context.getResources().getText(C0136R.string.tuerqi).toString();
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                return context.getResources().getText(C0136R.string.helan).toString();
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                return context.getResources().getText(C0136R.string.xila).toString();
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                return context.getResources().getText(C0136R.string.xiongyali).toString();
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                return context.getResources().getText(C0136R.string.alabo).toString();
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                return context.getResources().getText(C0136R.string.danmai).toString();
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                return context.getResources().getText(C0136R.string.hanyu).toString();
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                return context.getResources().getText(C0136R.string.bosi).toString();
            case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                return context.getResources().getText(C0136R.string.luomaniya).toString();
            case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                return context.getResources().getText(C0136R.string.saierweiya).toString();
            case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                return context.getResources().getText(C0136R.string.fenlan).toString();
            case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                return context.getResources().getText(C0136R.string.ruidian).toString();
            case FileOptions.JAVA_GENERATE_EQUALS_AND_HASH_FIELD_NUMBER /*20*/:
                return context.getResources().getText(C0136R.string.jieke).toString();
            case 221:
                return context.getResources().getText(C0136R.string.fanti).toString();
            case XStream.NO_REFERENCES /*1001*/:
                return context.getResources().getText(C0136R.string.english).toString();
            case XStream.ID_REFERENCES /*1002*/:
                return context.getResources().getText(C0136R.string.chinese).toString();
            case XStream.XPATH_RELATIVE_REFERENCES /*1003*/:
                return context.getResources().getText(C0136R.string.yidali).toString();
            default:
                return languageName;
        }
    }
}
