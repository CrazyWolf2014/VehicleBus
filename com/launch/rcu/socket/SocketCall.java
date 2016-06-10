package com.launch.rcu.socket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.entity.Constant;
import com.ifoer.entity.MenuData;
import com.ifoer.entity.SpecialFunction;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptInputNumric;
import com.ifoer.entity.SptInputStringEx;
import com.ifoer.entity.SptMessageBoxText;
import com.ifoer.entity.SptStreamSelectIdItem;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.entity.Spt_Combination_Menu;
import com.ifoer.entity.Spt_Nobuttonbox_Text;
import com.ifoer.entity.Spt_Progressbar_Box;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.WelcomeActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.MySharedPreferences;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.ArrayList;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class SocketCall {
    public static boolean resureFlag;
    public static int resureResult;

    public native int send(byte[] bArr, int i);

    public native void start(String str, int i, int i2, byte[] bArr);

    public native void stop();

    static {
        resureResult = 1;
        resureFlag = true;
        System.loadLibrary("Socket");
    }

    public static void receive(byte[] data) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new String(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            Log.e("RemoteDiagData", "JSON is null");
            return;
        }
        try {
            if (MySharedPreferences.getStringValue(MainActivity.contexts, BundleBuilder.IdentityType).equals(Contact.RELATION_ASK)) {
                handleMasochism(jsonObject);
            } else {
                handleSadism(jsonObject);
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    public static void state(byte reg) {
        int type = -1;
        switch (reg) {
            case KEYRecord.OWNER_USER /*0*/:
                Log.d("SandaLog", "\u672c\u7aef\u548c\u4e2d\u8f6c\u670d\u52a1\u5668\u8fde\u63a5\u6210\u529f");
                MainActivity.contexts.sendBroadcast(new Intent("RCU_State_00"));
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                Log.d("SandaLog", "\u672c\u7aef\u548c\u4e2d\u8f6c\u670d\u52a1\u5668\u8fde\u63a5\u5931\u8d25");
                MainActivity.contexts.sendBroadcast(new Intent("RCU_State_01"));
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                Log.d("SandaLog", "\u672c\u7aef\u7ed9\u4e2d\u8f6c\u670d\u52a1\u5668\u53d1\u9001\u5e8f\u5217\u53f7\u6210\u529f");
                MainActivity.contexts.sendBroadcast(new Intent("RCU_State_02"));
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                Log.d("SandaLog", "\u672c\u7aef\u7ed9\u4e2d\u8f6c\u670d\u52a1\u5668\u53d1\u9001\u5e8f\u5217\u53f7\u5931\u8d25");
                MainActivity.contexts.sendBroadcast(new Intent("RCU_State_03"));
                break;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                Log.d("SandaLog", "\u672c\u7aef\u8eab\u4efd\u9a8c\u8bc1\u6210\u529f");
                MainActivity.contexts.sendBroadcast(new Intent("RCU_State_04"));
                break;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                Log.d("SandaLog", "\u672c\u7aef\u8eab\u4efd\u9a8c\u8bc1\u5931\u8d25");
                MainActivity.contexts.sendBroadcast(new Intent("RCU_State_05"));
                break;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                Log.d("SandaLog", "\u7f51\u7edc\u8fde\u63a5\u6210\u529f");
                MainActivity.contexts.sendBroadcast(new Intent("RCU_State_06"));
                break;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                Log.d("SandaLog", "\u7f51\u7edc\u8fde\u63a5\u5931\u8d25");
                type = Service.SUNRPC;
                break;
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                Log.d("SandaLog", "\u5bf9\u65b9\u7ed3\u675f\u8fdc\u7a0b\u8bca\u65ad");
                type = Opcodes.IREM;
                break;
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                Log.d("SandaLog", "\u672c\u7aef\u7ed3\u675f\u8fdc\u7a0b\u8bca\u65ad");
                type = Service.AUTH;
                break;
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                Log.d("SandaLog", "\u5176\u4ed6\u5f02\u5e38");
                type = Opcodes.FREM;
                break;
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                Log.e("SandaLog", "Socket\u5df2\u7ecf\u65ad\u5f00");
                break;
        }
        if (type != -1) {
            Intent intentStatus = new Intent("RemoteDiagStatus");
            Bundle bundle = new Bundle();
            bundle.putInt(SharedPref.TYPE, type);
            intentStatus.putExtras(bundle);
            MainActivity.contexts.sendBroadcast(intentStatus);
        }
    }

    public static int resure(byte[] information) {
        Intent intentStatus = new Intent("RemoteDiagStatus");
        Bundle bundle = new Bundle();
        bundle.putInt(SharedPref.TYPE, Service.CISCO_FNA);
        intentStatus.putExtras(bundle);
        MainActivity.contexts.sendBroadcast(intentStatus);
        while (resureFlag) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return resureResult;
    }

    private static void TroubleCodeUI(String action, int type, Object object) {
        ArrayList<SptTroubleTest> troubleCodeByLoopModeList = (ArrayList) object;
        Intent intent = new Intent(action);
        Bundle bundle = new Bundle();
        bundle.putSerializable(action, troubleCodeByLoopModeList);
        bundle.putInt(SharedPref.TYPE, type);
        intent.putExtras(bundle);
        MainActivity.contexts.sendBroadcast(intent);
    }

    private static void handleSadism(JSONObject jsonObject) throws JSONException {
        int type = jsonObject.getInt(SharedPref.TYPE);
        ArrayList<MenuData> object = ObjectAndBytes.getDataFromWeb(jsonObject, type);
        Log.d("RemoteDiagData", "\u6536\u5230\u6570\u636e\uff1a" + jsonObject.toString());
        Intent intent;
        switch (type) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                Constant.pageType = 1;
                ArrayList<MenuData> menuDataList = object;
                intent = new Intent("feedbackMeauData");
                Bundle bundle = new Bundle();
                bundle.putSerializable("menuData", menuDataList);
                bundle.putInt(SharedPref.TYPE, 1);
                intent.putExtras(bundle);
                MainActivity.contexts.sendBroadcast(intent);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                ArrayList<SptTroubleTest> troubleTestList = object;
                intent = new Intent("SPT_TROUBLE_CODE");
                Bundle bundle14 = new Bundle();
                bundle14.putSerializable("SPT_TROUBLE_CODE", troubleTestList);
                bundle14.putInt(SharedPref.TYPE, 14);
                intent.putExtras(bundle14);
                MainActivity.contexts.sendBroadcast(intent);
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) object;
                intent = new Intent("SPT_NOBUTTONBOX_TEXT");
                Bundle bundleNobutton = new Bundle();
                bundleNobutton.putSerializable("Nobuttonbox", nobuttonbox);
                bundleNobutton.putInt(SharedPref.TYPE, 5);
                intent.putExtras(bundleNobutton);
                MainActivity.contexts.sendBroadcast(intent);
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) object;
                intent = new Intent("SPT_MESSAGEBOX_TEXT");
                Bundle bundleMessageBox = new Bundle();
                bundleMessageBox.putSerializable("SPT_MESSAGEBOX_TEXT", sptMessageBoxText);
                bundleMessageBox.putInt(SharedPref.TYPE, 6);
                intent.putExtras(bundleMessageBox);
                MainActivity.contexts.sendBroadcast(intent);
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) object;
                intent = new Intent("SPT_INPUTBOX_TEXT");
                Bundle bundleInputBox = new Bundle();
                bundleInputBox.putSerializable("SPT_INPUTBOX_TEXT", inputBox);
                bundleInputBox.putInt(SharedPref.TYPE, 7);
                intent.putExtras(bundleInputBox);
                MainActivity.contexts.sendBroadcast(intent);
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) object;
                intent = new Intent("SPT_INPUTSTRING");
                Bundle bundleInputStr = new Bundle();
                bundleInputStr.putSerializable("SPT_INPUTSTRING", inputStr);
                bundleInputStr.putInt(SharedPref.TYPE, 8);
                intent.putExtras(bundleInputStr);
                MainActivity.contexts.sendBroadcast(intent);
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                SptActiveTest sptActiveTest = (SptActiveTest) object;
                intent = new Intent("SPT_ACTIVE_TEST");
                Bundle bundle9 = new Bundle();
                bundle9.putSerializable("ACTIVE_TEST", sptActiveTest);
                bundle9.putInt(SharedPref.TYPE, 9);
                intent.putExtras(bundle9);
                MainActivity.contexts.sendBroadcast(intent);
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                intent = new Intent("SPT_EXIT_SHOW_WINDOW");
                MainActivity.contexts.sendBroadcast(intent);
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                String pictureName = (String) object;
                intent = new Intent("SPT_SHOW_PICTURE");
                Bundle bundle11 = new Bundle();
                bundle11.putString("SPT_SHOW_PICTURE", pictureName);
                bundle11.putInt(SharedPref.TYPE, 11);
                intent.putExtras(bundle11);
                MainActivity.contexts.sendBroadcast(intent);
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                SptInputNumric inputNumric = (SptInputNumric) object;
                intent = new Intent("SPT_INPUT_NUMERIC");
                Bundle bundleInputString = new Bundle();
                bundleInputString.putSerializable("SPT_INPUT_NUMERIC", inputNumric);
                bundleInputString.putInt(SharedPref.TYPE, 15);
                intent.putExtras(bundleInputString);
                MainActivity.contexts.sendBroadcast(intent);
            case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                SptInputStringEx inputString = (SptInputStringEx) object;
                intent = new Intent("SPT_INPUTSTRING_EX");
                Bundle bundleInputString16 = new Bundle();
                bundleInputString16.putSerializable("SPT_INPUTSTRING_EX", inputString);
                bundleInputString16.putInt(SharedPref.TYPE, 16);
                intent.putExtras(bundleInputString16);
                MainActivity.contexts.sendBroadcast(intent);
            case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                ArrayList<SptStreamSelectIdItem> streamSelectIdlist = object;
                intent = new Intent("SPT_STREAM_SELECT_ID_EX");
                Bundle bundle17 = new Bundle();
                bundle17.putSerializable("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                bundle17.putInt(SharedPref.TYPE, 17);
                intent.putExtras(bundle17);
                MainActivity.contexts.sendBroadcast(intent);
            case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = object;
                intent = new Intent("SPT_EX_DATASTREAM_ID");
                Bundle bundle18 = new Bundle();
                bundle18.putSerializable("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                bundle18.putInt(SharedPref.TYPE, 18);
                intent.putExtras(bundle18);
                MainActivity.contexts.sendBroadcast(intent);
            case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = object;
                intent = new Intent("SPT_VW_DATASTREAM_ID");
                Bundle bundle19 = new Bundle();
                bundle19.putSerializable("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                bundle19.putInt(SharedPref.TYPE, 19);
                intent.putExtras(bundle19);
                MainActivity.contexts.sendBroadcast(intent);
            case Service.NSW_FE /*27*/:
                ArrayList<SptTroubleTest> troubleCodeFrozenList = object;
                intent = new Intent("SPT_TROUBLE_CODE_FROZEN");
                Bundle bundle27 = new Bundle();
                bundle27.putSerializable("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                bundle27.putInt(SharedPref.TYPE, 27);
                intent.putExtras(bundle27);
                MainActivity.contexts.sendBroadcast(intent);
            case Protocol.IRTP /*28*/:
                Constant.pageType = 28;
                ArrayList<MenuData> dsMenuDataList = object;
                intent = new Intent("SPT_DS_MENU_ID");
                Bundle bundle28 = new Bundle();
                bundle28.putSerializable("SPT_DS_MENU_ID", dsMenuDataList);
                bundle28.putInt(SharedPref.TYPE, 28);
                intent.putExtras(bundle28);
                MainActivity.contexts.sendBroadcast(intent);
            case Service.MSG_ICP /*29*/:
                Constant.pageType = 29;
                ArrayList<MenuData> filemenuDataList = object;
                intent = new Intent("SPT_FILE_MENU");
                Bundle bundle29 = new Bundle();
                bundle29.putSerializable("SPT_FILE_MENU", filemenuDataList);
                bundle29.putInt(SharedPref.TYPE, 29);
                intent.putExtras(bundle29);
                MainActivity.contexts.sendBroadcast(intent);
            case Protocol.NETBLT /*30*/:
                String agingContent = object.toString();
                intent = new Intent("SPT_AGING_WINDOW");
                Bundle bundle30 = new Bundle();
                bundle30.putString("SPT_AGING_WINDOW", agingContent);
                bundle30.putInt(SharedPref.TYPE, 30);
                intent.putExtras(bundle30);
                MainActivity.contexts.sendBroadcast(intent);
            case Type.ATMA /*34*/:
                ArrayList<SptExDataStreamIdItem> list34 = object;
                intent = new Intent("SPT_DATASTREAM_ID_EX");
                Bundle bundle34 = new Bundle();
                bundle34.putSerializable("SPT_DATASTREAM_ID_EX", list34);
                bundle34.putInt(SharedPref.TYPE, 34);
                intent.putExtras(bundle34);
                MainActivity.contexts.sendBroadcast(intent);
            case Type.KX /*36*/:
                ArrayList<SpecialFunction> list36 = object;
                intent = new Intent("SPT_SPECIAL_FUNCTION_ID");
                Bundle bundle36 = new Bundle();
                bundle36.putSerializable("SPT_SPECIAL_FUNCTION_ID", list36);
                bundle36.putInt(SharedPref.TYPE, 36);
                intent.putExtras(bundle36);
                MainActivity.contexts.sendBroadcast(intent);
            case Service.TIME /*37*/:
                TroubleCodeUI("SPT_TROUBLE_CODE_ID_EX", 37, object);
            case Type.A6 /*38*/:
                TroubleCodeUI("SPT_TROUBLE_CODE_FROZEN_EX", 38, object);
            case Service.RLP /*39*/:
                TroubleCodeUI("SPT_TROUBLE_CODE_ID_BY_COMBINE", 39, object);
            case SmileConstants.TOKEN_MISC_FP /*40*/:
                TroubleCodeUI("SPT_TROUBLE_CODE_FROZEN_BY_COMBINE", 40, object);
            case Service.GRAPHICS /*41*/:
                TroubleCodeUI("SPT_TROUBLE_CODE_ID_EX_BY_COMBINE", 41, object);
            case Service.NAMESERVER /*42*/:
                TroubleCodeUI("SPT_TROUBLE_CODE_FROZEN_EX_BY_COMBINE", 42, object);
            case Service.NICNAME /*43*/:
                TroubleCodeUI("SPT_TROUBLE_CODE_ID_BY_LOOPMODE", 43, object);
            case Service.MPM_FLAGS /*44*/:
                String[] nowaitmessagebox = (String[]) object;
                intent = new Intent("SPT_NOWAITMESSAGEBOX_TEXT");
                Bundle bundleNowaitmessagebox = new Bundle();
                bundleNowaitmessagebox.putStringArray("SPT_NOWAITMESSAGEBOX_TEXT", nowaitmessagebox);
                bundleNowaitmessagebox.putInt(SharedPref.TYPE, 44);
                intent.putExtras(bundleNowaitmessagebox);
                MainActivity.contexts.sendBroadcast(intent);
            case Service.MPM /*45*/:
                Spt_Progressbar_Box spb = (Spt_Progressbar_Box) object;
                intent = new Intent("SPT_PROGRESSBAR_BOX");
                Bundle bundleProgerssbar = new Bundle();
                bundleProgerssbar.putSerializable("SPT_PROGRESSBAR_BOX", spb);
                bundleProgerssbar.putInt(SharedPref.TYPE, 45);
                intent.putExtras(bundleProgerssbar);
                MainActivity.contexts.sendBroadcast(intent);
            case Service.MPM_SND /*46*/:
                Spt_Combination_Menu spt_Combination_menu = (Spt_Combination_Menu) object;
                intent = new Intent("SPT_COMBINATION_MENU");
                Bundle bundle46 = new Bundle();
                bundle46.putSerializable("SPT_COMBINATION_MENU", spt_Combination_menu);
                bundle46.putInt(SharedPref.TYPE, 46);
                intent.putExtras(bundle46);
                MainActivity.contexts.sendBroadcast(intent);
            default:
        }
    }

    private static void handleMasochism(JSONObject jsonObject) throws JSONException {
        int type = jsonObject.getInt(SharedPref.TYPE);
        String dataContent = jsonObject.getString(DataPacketExtension.ELEMENT_NAME);
        Log.d("RemoteDiagData", "\u6536\u5230\u6570\u636e\uff1a" + jsonObject.toString());
        byte[] feedback;
        Intent intent;
        Bundle bundle;
        switch (type) {
            case -9:
                Constant.activeNextCode = ByteHexHelper.intToHexBytes(Integer.parseInt(dataContent));
                CToJava.activeChangeButton = Boolean.valueOf(true);
                CToJava.activeFlag = Boolean.valueOf(false);
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                if (dataContent.equals(SocketCode.REMOTE_BACK)) {
                    Constant.feedback = null;
                    Constant.feedback = Constant.previousMenu;
                    if (Constant.bridge != null) {
                        Constant.bridge.putData();
                        return;
                    }
                    return;
                }
                Constant.pageType = 1;
                Constant.feedback = null;
                Constant.feedback = ByteHexHelper.hexStringToBytes(new StringBuilder(String.valueOf(ByteHexHelper.checkedSite(Integer.parseInt(dataContent)))).append(ByteHexHelper.checkedSite(Integer.parseInt(dataContent))).toString());
                CToJava.activeFlag = Boolean.valueOf(false);
                CToJava.streamFlag = Boolean.valueOf(true);
                CToJava.agingFlag = Boolean.valueOf(true);
                CToJava.nowaitmessageboxtext = Boolean.valueOf(true);
                CToJava.progressbarFlag = Boolean.valueOf(true);
                if (Constant.bridge != null) {
                    Constant.bridge.putData();
                }
                Constant.streamNextCode = Constant.noInterruptStreamCode;
                Constant.activeNextCode = Constant.noButton;
                Constant.continueObtain = Constant.noInterruptStreamCode;
                Constant.noWaitMessageButton = Constant.noInterruptStreamCode;
                Constant.progressbarBox = Constant.noInterruptStreamCode;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                Constant.streamNextCode = Constant.previousMenu;
                if (Constant.bridge != null) {
                    Constant.bridge.putData();
                }
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                byte[] ok = new byte[]{ByteHexHelper.intToHexByte(Integer.parseInt(dataContent))};
                Constant.feedback = null;
                Constant.feedback = ok;
                CToJava.haveData = Boolean.valueOf(true);
                CToJava.inputBox = Boolean.valueOf(true);
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                feedback = ByteHexHelper.intToFourHexBytesTwo(Integer.valueOf(dataContent).intValue());
                Constant.feedback = null;
                Constant.feedback = feedback;
                CToJava.haveData = Boolean.valueOf(true);
                CToJava.inputBox = Boolean.valueOf(true);
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                feedback = new StringBuilder(String.valueOf(dataContent)).append("\u0000").toString().getBytes();
                Constant.feedback = null;
                Constant.feedback = feedback;
                CToJava.haveData = Boolean.valueOf(true);
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                CToJava.activeChangeButton = Boolean.valueOf(true);
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                CToJava.haveData = Boolean.valueOf(true);
                Constant.feedback = null;
                Constant.feedback = Constant.streamNextCode;
                CToJava.haveData = Boolean.valueOf(true);
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
            case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                String order;
                String value = dataContent.substring(0, 1);
                byte[] str = (dataContent.substring(1, dataContent.length()) + "\u0000").getBytes();
                if (value.equals(Contact.RELATION_ASK)) {
                    order = "00" + ByteHexHelper.bytesToHexString(str);
                } else {
                    order = "01" + ByteHexHelper.bytesToHexString(str);
                }
                Constant.feedback = null;
                Constant.feedback = ByteHexHelper.hexStringToBytes(order);
                CToJava.haveData = Boolean.valueOf(true);
            case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                if (dataContent.equals(SocketCode.REMOTE_BACK)) {
                    StringBuffer choiceItem = new StringBuffer();
                    int total = MySharedPreferences.getIntValue(MainActivity.mContexts, MySharedPreferences.dataStreamTotal);
                    for (int i = 0; i < total; i++) {
                        choiceItem.append(Contact.RELATION_ASK);
                    }
                    feedback = ByteHexHelper.hexStringToBytes(ByteHexHelper.binaryString2hexString(choiceItem.toString()));
                    Constant.feedback = null;
                    Constant.feedback = feedback;
                    CToJava.streamFlag = Boolean.valueOf(false);
                    Constant.streamNextCode = feedback;
                    if (Constant.bridge != null) {
                        Constant.bridge.putData();
                        return;
                    }
                    return;
                }
                intent = new Intent("RemoteDiagSelectItem");
                Bundle bundle17 = new Bundle();
                bundle17.putString("SelectItemFlag", dataContent);
                bundle17.putInt(SharedPref.TYPE, 17);
                intent.putExtras(bundle17);
                MainActivity.contexts.sendBroadcast(intent);
            case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                Log.e("SandaLog", "18:dataContent=" + dataContent);
                intent = new Intent("RCU_OCLICK_DATASTREAM");
                Bundle bundle18 = new Bundle();
                bundle18.putString("orderCode", dataContent);
                bundle18.putInt(SharedPref.TYPE, 18);
                intent.putExtras(bundle18);
                MainActivity.contexts.sendBroadcast(intent);
            case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                intent = new Intent("RCU_OCLICK_VWDATASTREAM");
                Bundle bundle19 = new Bundle();
                bundle19.putString("orderCode", dataContent);
                bundle19.putInt(SharedPref.TYPE, 19);
                intent.putExtras(bundle19);
                MainActivity.contexts.sendBroadcast(intent);
            case Service.NSW_FE /*27*/:
                if (dataContent.equals(SocketCode.REMOTE_BACK)) {
                    Constant.feedback = null;
                    Constant.feedback = Constant.previousMenu;
                    if (Constant.bridge != null) {
                        Constant.bridge.putData();
                        return;
                    }
                    return;
                }
                Constant.feedback = null;
                Constant.feedback = ByteHexHelper.intToHexBytes(Integer.parseInt(dataContent));
                CToJava.activeFlag = Boolean.valueOf(false);
                CToJava.streamFlag = Boolean.valueOf(true);
                Constant.streamNextCode = Constant.noInterruptStreamCode;
                Constant.activeNextCode = Constant.noButton;
                if (Constant.bridge != null) {
                    Constant.bridge.putData();
                }
            case Protocol.IRTP /*28*/:
                Constant.pageType = 28;
                if (dataContent.equals(SocketCode.REMOTE_BACK)) {
                    Constant.feedback = null;
                    Constant.feedback = Constant.previousMenu;
                    if (Constant.bridge != null) {
                        Constant.bridge.putData();
                        return;
                    }
                    return;
                }
                Constant.feedback = null;
                Constant.feedback = ByteHexHelper.hexStringToBytes(new StringBuilder(String.valueOf(ByteHexHelper.checkedSite(Integer.parseInt(dataContent)))).append(ByteHexHelper.checkedSite(Integer.parseInt(dataContent))).toString());
                CToJava.activeFlag = Boolean.valueOf(false);
                CToJava.streamFlag = Boolean.valueOf(true);
                CToJava.agingFlag = Boolean.valueOf(true);
                CToJava.nowaitmessageboxtext = Boolean.valueOf(true);
                CToJava.progressbarFlag = Boolean.valueOf(true);
                if (Constant.bridge != null) {
                    Constant.bridge.putData();
                }
                Constant.streamNextCode = Constant.noInterruptStreamCode;
                Constant.activeNextCode = Constant.noButton;
                Constant.continueObtain = Constant.noInterruptStreamCode;
                Constant.noWaitMessageButton = Constant.noInterruptStreamCode;
                Constant.progressbarBox = Constant.noInterruptStreamCode;
            case Service.MSG_ICP /*29*/:
                Constant.pageType = 29;
                if (dataContent.equals(SocketCode.REMOTE_BACK)) {
                    Constant.feedback = null;
                    Constant.feedback = Constant.previousMenu;
                    if (Constant.bridge != null) {
                        Constant.bridge.putData();
                        return;
                    }
                    return;
                }
                Constant.feedback = null;
                Constant.feedback = ByteHexHelper.hexStringToBytes(new StringBuilder(String.valueOf(ByteHexHelper.checkedSite(Integer.parseInt(dataContent)))).append(ByteHexHelper.checkedSite(Integer.parseInt(dataContent))).toString());
                CToJava.activeFlag = Boolean.valueOf(false);
                CToJava.streamFlag = Boolean.valueOf(true);
                CToJava.agingFlag = Boolean.valueOf(true);
                CToJava.nowaitmessageboxtext = Boolean.valueOf(true);
                CToJava.progressbarFlag = Boolean.valueOf(true);
                if (Constant.bridge != null) {
                    Constant.bridge.putData();
                }
                Constant.streamNextCode = Constant.noInterruptStreamCode;
                Constant.activeNextCode = Constant.noButton;
                Constant.continueObtain = Constant.noInterruptStreamCode;
                Constant.noWaitMessageButton = Constant.noInterruptStreamCode;
                Constant.progressbarBox = Constant.noInterruptStreamCode;
            case Protocol.NETBLT /*30*/:
                Constant.continueObtain = null;
                Constant.continueObtain = Constant.previousMenu;
                CToJava.agingFlag = Boolean.valueOf(true);
            case Type.ATMA /*34*/:
                Constant.spt_data_stream_button = new byte[]{(byte) 7};
            case Type.KX /*36*/:
                int tempOrder = Integer.parseInt(dataContent.substring(1, dataContent.length()));
                byte[] buttonOrder;
                if (dataContent.substring(0, 1).equals(Contact.RELATION_ASK)) {
                    buttonOrder = new byte[2];
                    buttonOrder[1] = (byte) (buttonOrder[1] + ByteHexHelper.intToHexByte(tempOrder));
                    Constant.continueSpecia = buttonOrder;
                } else if (dataContent.substring(0, 1).equals(Contact.RELATION_FRIEND)) {
                    buttonOrder = new byte[2];
                    buttonOrder[1] = Byte.MIN_VALUE;
                    buttonOrder[1] = (byte) (buttonOrder[1] + ByteHexHelper.intToHexByte(tempOrder));
                    Constant.continueSpecia = buttonOrder;
                }
            case Service.TIME /*37*/:
                Constant.feedback = null;
                Constant.feedback = Constant.previousMenu;
                CToJava.haveData = Boolean.valueOf(true);
                if (Constant.bridge != null) {
                    Constant.bridge.putData();
                }
            case Type.A6 /*38*/:
                if (dataContent.equals(SocketCode.REMOTE_BACK)) {
                    Constant.feedback = null;
                    Constant.feedback = Constant.previousMenu;
                    if (Constant.bridge != null) {
                        Constant.bridge.putData();
                        return;
                    }
                    return;
                }
                Constant.feedback = null;
                Constant.feedback = ByteHexHelper.intToHexBytes(Integer.parseInt(dataContent));
                CToJava.activeFlag = Boolean.valueOf(false);
                CToJava.streamFlag = Boolean.valueOf(true);
                Constant.streamNextCode = Constant.noInterruptStreamCode;
                Constant.activeNextCode = Constant.noButton;
                if (Constant.bridge != null) {
                    Constant.bridge.putData();
                }
            case Service.RLP /*39*/:
                if (dataContent.equals(SocketCode.REMOTE_BACK)) {
                    Constant.streamNextCode = Constant.previousMenu;
                    if (Constant.bridge != null) {
                        Constant.bridge.putData();
                    }
                }
            case SmileConstants.TOKEN_MISC_FP /*40*/:
                if (dataContent.equals(SocketCode.REMOTE_BACK)) {
                    Constant.streamNextCode = Constant.previousMenu;
                    if (Constant.bridge != null) {
                        Constant.bridge.putData();
                        return;
                    }
                    return;
                }
                Constant.feedback = null;
                Constant.feedback = ByteHexHelper.intToHexBytes(Integer.parseInt(dataContent));
                CToJava.activeFlag = Boolean.valueOf(false);
                CToJava.streamFlag = Boolean.valueOf(true);
                Constant.streamNextCode = Constant.noInterruptStreamCode;
                Constant.activeNextCode = Constant.noButton;
                if (Constant.bridge != null) {
                    Constant.bridge.putData();
                }
            case Service.GRAPHICS /*41*/:
                Constant.streamNextCode = Constant.previousMenu;
                if (Constant.bridge != null) {
                    Constant.bridge.putData();
                }
            case Service.NAMESERVER /*42*/:
                if (dataContent.equals(SocketCode.REMOTE_BACK)) {
                    Constant.streamNextCode = Constant.previousMenu;
                    if (Constant.bridge != null) {
                        Constant.bridge.putData();
                        return;
                    }
                    return;
                }
                Constant.feedback = null;
                Constant.feedback = ByteHexHelper.intToHexBytes(Integer.parseInt(dataContent));
                CToJava.activeFlag = Boolean.valueOf(false);
                CToJava.streamFlag = Boolean.valueOf(true);
                Constant.streamNextCode = Constant.noInterruptStreamCode;
                Constant.activeNextCode = Constant.noButton;
                if (Constant.bridge != null) {
                    Constant.bridge.putData();
                }
            case Service.NICNAME /*43*/:
                if (jsonObject.getString("mode").equals("CMD")) {
                    intent = new Intent("RCU_OCLICK_FaultCodeByLoopMode");
                    bundle = new Bundle();
                    bundle.putString("orderCode", dataContent);
                    bundle.putInt(SharedPref.TYPE, 43);
                    intent.putExtras(bundle);
                    MainActivity.contexts.sendBroadcast(intent);
                }
            case Service.MPM_FLAGS /*44*/:
                Constant.noWaitMessageButton = null;
                Constant.noWaitMessageButton = Constant.previousMenu;
                CToJava.nowaitmessageboxtext = Boolean.valueOf(true);
            case Service.MPM /*45*/:
                Constant.progressbarBox = null;
                Constant.progressbarBox = Constant.previousMenu;
                CToJava.progressbarFlag = Boolean.valueOf(true);
            case Service.MPM_SND /*46*/:
                if (!dataContent.equals(SocketCode.REMOTE_COMBINATION_SURE)) {
                    if (!dataContent.equals(SocketCode.REMOTE_COMBINATION_CLEAR)) {
                        if (!dataContent.equals(SocketCode.REMOTE_COMBINATION_BACK)) {
                            byte[] CombinationMenu = new byte[2];
                            CombinationMenu[1] = (byte) (ByteHexHelper.intToHexByte(Integer.parseInt(dataContent)) + Flags.FLAG8);
                            CToJava.combinationFlag = Boolean.valueOf(true);
                            Constant.feedback = null;
                            Constant.CombinationMenu = CombinationMenu;
                            if (Constant.bridge != null) {
                                Constant.bridge.putData();
                                return;
                            }
                            return;
                        }
                    }
                }
                intent = new Intent("RCU_OCLICK_Combination");
                bundle = new Bundle();
                bundle.putString("orderCode", dataContent);
                bundle.putInt(SharedPref.TYPE, 46);
                intent.putExtras(bundle);
                MainActivity.contexts.sendBroadcast(intent);
            case 1017:
                String str2 = XmlPullParser.NO_NAMESPACE;
                str2 = dataContent;
                intent = new Intent("RCU_L_STREAMSELECT_CHOICE");
                bundle = new Bundle();
                bundle.putString("RCU_L_STREAMSELECT_CHOICE_DATA", str2);
                intent.putExtras(bundle);
                MainActivity.contexts.sendBroadcast(intent);
            case 1018:
                intent = new Intent("RCU_L_DATA_STREAM_SELECT_LISTVIEW");
                Bundle bundle1018 = new Bundle();
                bundle1018.putString("RCU_L_DATA_STREAM_SELECT_LISTVIEW_DATA", dataContent);
                intent.putExtras(bundle1018);
                MainActivity.contexts.sendBroadcast(intent);
            case 1801:
                intent = new Intent("RCU_OCLICK_DATASTREAM");
                Bundle bundle1801 = new Bundle();
                bundle1801.putString("orderCode", dataContent);
                bundle1801.putInt(SharedPref.TYPE, 18);
                intent.putExtras(bundle1801);
                MainActivity.contexts.sendBroadcast(intent);
            case 1802:
                intent = new Intent("RCU_OCLICK_DataStreamChartTab");
                Bundle bundle1802 = new Bundle();
                bundle1802.putString("orderCode", dataContent);
                bundle1802.putInt(SharedPref.TYPE, 18);
                intent.putExtras(bundle1802);
                MainActivity.contexts.sendBroadcast(intent);
            default:
        }
    }
}
