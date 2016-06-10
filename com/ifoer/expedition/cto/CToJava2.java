package com.ifoer.expedition.cto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.dbs.SearchId;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.MenuData;
import com.ifoer.entity.OperatingRecordInfo;
import com.ifoer.entity.PageInteractiveData;
import com.ifoer.entity.SpecialFunction;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptActiveTestButton;
import com.ifoer.entity.SptActiveTestStream;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptExDataStreamIdItem34;
import com.ifoer.entity.SptInputNumric;
import com.ifoer.entity.SptInputStringEx;
import com.ifoer.entity.SptMessageBoxText;
import com.ifoer.entity.SptStreamSelectIdItem;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.entity.Spt_Combination_Menu;
import com.ifoer.entity.Spt_Nobuttonbox_Text;
import com.ifoer.entity.Spt_Progressbar_Box;
import com.ifoer.expedition.BluetoothChat.BluetoothChatService;
import com.ifoer.expedition.BluetoothOrder.Analysis;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.BluetoothOrder.OrderMontage;
import com.ifoer.expedition.BluetoothOrder.PageInteractiveDataAnalysis;
import com.ifoer.expedition.ndk.VINStdJni;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.WelcomeActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.Bridge;
import com.ifoer.util.CarDiagnose;
import com.ifoer.util.CarDiagnoseBridge;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.WriteByteData;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.codehaus.jackson.smile.SmileConstants;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class CToJava2 {
    private static final boolean f1290D = false;
    public static Boolean activeChangeButton;
    public static Boolean activeFlag;
    public static Boolean agingFlag;
    public static Boolean combinationFlag;
    public static Boolean dataStreamFlag;
    public static Boolean faultCodeFlag;
    public static Boolean haveData;
    public static Boolean inputBox;
    public static Boolean nowaitmessageboxtext;
    public static Boolean progressbarFlag;
    public static Boolean streamFlag;

    static {
        activeChangeButton = Boolean.valueOf(false);
        activeFlag = Boolean.valueOf(false);
        streamFlag = Boolean.valueOf(false);
        agingFlag = Boolean.valueOf(false);
        progressbarFlag = Boolean.valueOf(false);
        haveData = Boolean.valueOf(false);
        nowaitmessageboxtext = Boolean.valueOf(false);
        inputBox = Boolean.valueOf(true);
        dataStreamFlag = Boolean.valueOf(false);
        faultCodeFlag = Boolean.valueOf(false);
        combinationFlag = Boolean.valueOf(false);
    }

    public static byte[] WriteAndRead(byte[] sendBuffer, int sendDataLength, byte[] receiveBuffer, int receiveBufferLength, int maxWaitTime) {
        VINStdJni stdJni = new VINStdJni();
        byte[] pSendBuffer = sendBuffer;
        int iSendDataLength = sendDataLength;
        byte[] pReceiveBuffer = receiveBuffer;
        int iReceiveBufferLength = receiveBufferLength;
        int iMaxWaitTime = maxWaitTime;
        Log.v("databuffer", "\u5ef6\u8fdf\u65f6\u95f4\u4e3a\uff1a   " + iMaxWaitTime);
        String backOrder = XmlPullParser.NO_NAMESPACE;
        byte[] sendOrder = OrderMontage.smartBox2701No(pSendBuffer);
        BluetoothChatService mChatService = Constant.mChatService;
        if (mChatService == null || mChatService.getState() != 3) {
            return null;
        }
        mChatService.setWaitTime(iMaxWaitTime);
        if (sendOrder.length > 0) {
            Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
            BluetoothChatService.readData = XmlPullParser.NO_NAMESPACE;
            Thread thread = new Thread(dynamic);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            backOrder = BluetoothChatService.readData;
        }
        if (!Analysis.analysisData(sendOrder, ByteHexHelper.hexStringToBytes(backOrder))) {
            backOrder = null;
        }
        if (backOrder == null || backOrder.length() <= 0) {
            stdJni.setStateCode(-14);
            pReceiveBuffer = new byte[0];
        } else {
            stdJni.setStateCode(0);
            iReceiveBufferLength = ByteHexHelper.intPackLength(backOrder.substring(8, 12)) - 1;
            pReceiveBuffer = new byte[iReceiveBufferLength];
            byte[] allOrder = ByteHexHelper.hexStringToBytes(backOrder);
            int flag = 0;
            for (int i = 7; i < iReceiveBufferLength + 7; i++) {
                pReceiveBuffer[flag] = allOrder[i];
                flag++;
            }
        }
        return pReceiveBuffer;
    }

    public static byte[] WriteAndRead2701(byte[] sendBuffer, int sendDataLength, byte[] receiveBuffer, int receiveBufferLength, int maxWaitTime) {
        VINStdJni stdJni = new VINStdJni();
        byte[] pSendBuffer = sendBuffer;
        int iSendDataLength = sendDataLength;
        byte[] pReceiveBuffer = receiveBuffer;
        int iReceiveBufferLength = receiveBufferLength;
        int iMaxWaitTime = maxWaitTime;
        String backOrder = XmlPullParser.NO_NAMESPACE;
        byte[] sendOrder = OrderMontage.smartBox2701(pSendBuffer);
        BluetoothChatService mChatService = Constant.mChatService;
        if (mChatService == null || mChatService.getState() != 3) {
            return null;
        }
        mChatService.setWaitTime(iMaxWaitTime);
        if (sendOrder.length > 0) {
            Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
            BluetoothChatService.readData = XmlPullParser.NO_NAMESPACE;
            Thread thread = new Thread(dynamic);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            backOrder = BluetoothChatService.readData;
        }
        if (backOrder == null || backOrder.length() <= 0) {
            stdJni.setStateCode(-14);
            pReceiveBuffer = new byte[0];
        } else {
            stdJni.setStateCode(0);
            iReceiveBufferLength = ByteHexHelper.intPackLength(backOrder.substring(8, 12)) - 3;
            pReceiveBuffer = new byte[iReceiveBufferLength];
            byte[] allOrder = ByteHexHelper.hexStringToBytes(backOrder);
            int flag = 0;
            for (int i = 9; i < iReceiveBufferLength + 9; i++) {
                pReceiveBuffer[flag] = allOrder[i];
                flag++;
            }
        }
        if (MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.generateOperatingRecord).equalsIgnoreCase(Contact.RELATION_ASK)) {
            String softPackageId = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.savesoftPackageId);
            String serialNo = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.serialNoKey);
            if (Constant.ADDRESS == null || Constant.ADDRESS.equals(XmlPullParser.NO_NAMESPACE)) {
                Constant.ADDRESS = MySharedPreferences.getStringValue(MainActivity.contexts, "CurrentPosition");
            }
            Date date = new Date();
            String date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(r25);
            OperatingRecordInfo recordInfo = new OperatingRecordInfo();
            recordInfo.setSerialNumber(serialNo);
            recordInfo.setTestTime(date2);
            recordInfo.setTestSite(Constant.ADDRESS);
            recordInfo.setTestCar(softPackageId);
            if (Boolean.valueOf(DBDao.getInstance(MainActivity.contexts).addOperatingRecord(recordInfo, MainActivity.database)).booleanValue()) {
                MySharedPreferences.setString(MainActivity.contexts, MySharedPreferences.generateOperatingRecord, Contact.RELATION_FRIEND);
            }
        }
        return pReceiveBuffer;
    }

    public static byte[] getProtData(byte[] databuffer) {
        PageInteractiveData pageInteractiveData = PageInteractiveDataAnalysis.dataUtil(databuffer);
        String path;
        SearchId searchId;
        int back;
        int i;
        MenuData menuData;
        String lString;
        byte[] databuf;
        int i2;
        byte[] MenuContentbytes;
        int j;
        Intent intent;
        Bundle bundle;
        Thread thread;
        byte[] backData;
        byte[] feedback;
        byte[] activeContentbytes;
        byte[] activeContent;
        int length;
        SptTroubleTest troubleTest;
        byte[] troubleIdContentbytes;
        String[] troubleIdContent;
        byte[] databufs;
        byte[] troubleStateContentbytes;
        String[] datas;
        switch (pageInteractiveData.getPackageType()) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                ArrayList<MenuData> menuDataList = PageInteractiveDataAnalysis.spt_menu_id1(pageInteractiveData);
                path = Constant.OBD2_EN_GGP_PATH;
                if (path == null || path.length() <= 0) {
                    return null;
                }
                searchId = new SearchId();
                back = searchId.ggpOpen(Constant.OBD2_EN_GGP_PATH);
                for (i = 1; i < menuDataList.size(); i++) {
                    byte[] menuContentbytes;
                    menuData = (MenuData) menuDataList.get(i);
                    lString = XmlPullParser.NO_NAMESPACE;
                    databuf = ByteHexHelper.hexStringToBytes(menuData.getMenuId());
                    i2 = (databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                    MenuContentbytes = searchId.getTextFromLibReturnByte(((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databuf[3] & KEYRecord.PROTOCOL_ANY), 1);
                    if (MenuContentbytes.length > 0) {
                        menuContentbytes = new byte[(MenuContentbytes.length - 1)];
                        for (j = 0; j < menuContentbytes.length; j++) {
                            menuContentbytes[j] = MenuContentbytes[j];
                        }
                        break;
                    }
                    menuContentbytes = new byte[0];
                    lString = ByteHexHelper.byteToWord(menuContentbytes);
                    if (lString.length() > 0) {
                        menuData.setMenuContent(lString);
                    } else {
                        menuData.setMenuContent(menuData.getMenuId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                    }
                }
                searchId.ggpClose();
                intent = new Intent("feedbackMeauData");
                bundle = new Bundle();
                bundle.putSerializable("menuData", menuDataList);
                bundle.putInt(SharedPref.TYPE, 1);
                intent.putExtras(bundle);
                MainActivity.contexts.sendBroadcast(intent);
                thread = new Thread(new CarDiagnose(new CarDiagnoseBridge()));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backData = Constant.feedback;
                Constant.feedback = null;
                return PageInteractiveDataAnalysis.feedbackData(backData, pageInteractiveData);
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                Spt_Nobuttonbox_Text nobuttonbox = PageInteractiveDataAnalysis.Spt_Nobuttonbox_Text5(pageInteractiveData);
                byte[] back5 = new byte[0];
                intent = new Intent("SPT_NOBUTTONBOX_TEXT");
                Bundle bundleNobutton = new Bundle();
                bundleNobutton.putSerializable("Nobuttonbox", nobuttonbox);
                bundleNobutton.putInt(SharedPref.TYPE, 5);
                intent.putExtras(bundleNobutton);
                MainActivity.contexts.sendBroadcast(intent);
                feedback = PageInteractiveDataAnalysis.feedbackData(back5, pageInteractiveData);
                Constant.feedback = null;
                return feedback;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                SptMessageBoxText sptMessageBoxText = PageInteractiveDataAnalysis.spt_messageBox_text6(pageInteractiveData);
                intent = new Intent("SPT_MESSAGEBOX_TEXT");
                Bundle bundleMessageBox = new Bundle();
                bundleMessageBox.putSerializable("SPT_MESSAGEBOX_TEXT", sptMessageBoxText);
                bundleMessageBox.putInt(SharedPref.TYPE, 6);
                intent.putExtras(bundleMessageBox);
                MainActivity.contexts.sendBroadcast(intent);
                haveData = Boolean.valueOf(false);
                i = 0;
                while (!haveData.booleanValue()) {
                    i++;
                }
                haveData = Boolean.valueOf(false);
                backData = Constant.feedback;
                Constant.feedback = null;
                return PageInteractiveDataAnalysis.feedbackData(backData, pageInteractiveData);
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                Spt_Nobuttonbox_Text inputBox = PageInteractiveDataAnalysis.spt_inputBox_text7(pageInteractiveData);
                intent = new Intent("SPT_INPUTBOX_TEXT");
                Bundle bundleInputBox = new Bundle();
                bundleInputBox.putSerializable("SPT_INPUTBOX_TEXT", inputBox);
                bundleInputBox.putInt(SharedPref.TYPE, 7);
                intent.putExtras(bundleInputBox);
                MainActivity.contexts.sendBroadcast(intent);
                i = 0;
                while (!haveData.booleanValue()) {
                    i++;
                }
                haveData = Boolean.valueOf(false);
                feedback = PageInteractiveDataAnalysis.feedbackData(Constant.feedback, pageInteractiveData);
                Constant.feedback = null;
                return feedback;
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                Spt_Nobuttonbox_Text inputStr = PageInteractiveDataAnalysis.spt_inputString8(pageInteractiveData);
                intent = new Intent("SPT_INPUTSTRING");
                Bundle bundleInputStr = new Bundle();
                bundleInputStr.putSerializable("SPT_INPUTSTRING", inputStr);
                bundleInputStr.putInt(SharedPref.TYPE, 8);
                intent.putExtras(bundleInputStr);
                MainActivity.contexts.sendBroadcast(intent);
                i = 0;
                while (!haveData.booleanValue()) {
                    i++;
                }
                haveData = Boolean.valueOf(false);
                feedback = PageInteractiveDataAnalysis.feedbackData(Constant.feedback, pageInteractiveData);
                Constant.feedback = null;
                return feedback;
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                SptActiveTest sptActiveTest = PageInteractiveDataAnalysis.spt_active_test9(pageInteractiveData);
                ArrayList<SptActiveTestButton> buttonList = sptActiveTest.getActiveTestButtons();
                ArrayList<SptActiveTestStream> streamList = sptActiveTest.getActiveTestStreams();
                path = Constant.OBD2_EN_GGP_PATH;
                if (path == null || path.length() <= 0) {
                    return null;
                }
                searchId = new SearchId();
                back = searchId.ggpOpen(Constant.OBD2_EN_GGP_PATH);
                for (i = 0; i < streamList.size(); i++) {
                    SptActiveTestStream activeTestStream = (SptActiveTestStream) streamList.get(i);
                    lString = XmlPullParser.NO_NAMESPACE;
                    databuf = ByteHexHelper.hexStringToBytes(activeTestStream.getDataStreamId());
                    i2 = (databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                    activeContentbytes = searchId.getTextFromLibReturnByte(((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databuf[3] & KEYRecord.PROTOCOL_ANY), 3);
                    if (activeContentbytes.length > 0) {
                        activeContent = new byte[(activeContentbytes.length - 1)];
                        for (j = 0; j < activeContent.length; j++) {
                            activeContent[j] = activeContentbytes[j];
                        }
                        break;
                    }
                    activeContent = new byte[0];
                    String[] activeIdContent = PageInteractiveDataAnalysis.separateString(ByteHexHelper.bytesToHexString(activeContent));
                    if (activeIdContent == null || activeIdContent.length <= 0) {
                        lString = ByteHexHelper.byteToWord(activeContent);
                        if (lString.length() > 0) {
                            activeTestStream.setDataStreamContent(lString);
                        } else {
                            activeTestStream.setDataStreamContent(activeTestStream.getDataStreamId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                        }
                    } else {
                        length = activeIdContent.length;
                        if (r0 < 1 || !activeIdContent[0].equalsIgnoreCase("---")) {
                            length = activeIdContent.length;
                            if (r0 >= 1 && !activeIdContent[0].equalsIgnoreCase("---")) {
                                activeTestStream.setDataStreamContent(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(activeIdContent[0])));
                            }
                        } else {
                            activeTestStream.setDataStreamContent(activeTestStream.getDataStreamId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                        }
                        length = activeIdContent.length;
                        if (r0 <= 1 || !activeIdContent[1].equalsIgnoreCase("---")) {
                            length = activeIdContent.length;
                            if (r0 > 1 && !activeIdContent[1].equalsIgnoreCase("---")) {
                                activeTestStream.setUnit(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(activeIdContent[1])));
                            }
                        } else {
                            activeTestStream.setUnit(XmlPullParser.NO_NAMESPACE);
                        }
                    }
                }
                for (i = 0; i < buttonList.size(); i++) {
                    SptActiveTestButton activeTestButton = (SptActiveTestButton) buttonList.get(i);
                    lString = XmlPullParser.NO_NAMESPACE;
                    databuf = ByteHexHelper.hexStringToBytes(activeTestButton.getActiveButtonId());
                    i2 = (databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                    activeContentbytes = searchId.getTextFromLibReturnByte(((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databuf[3] & KEYRecord.PROTOCOL_ANY), 1);
                    if (activeContentbytes.length > 0) {
                        activeContent = new byte[(activeContentbytes.length - 1)];
                        for (j = 0; j < activeContent.length; j++) {
                            activeContent[j] = activeContentbytes[j];
                        }
                        break;
                    }
                    activeContent = new byte[0];
                    lString = ByteHexHelper.byteToWord(activeContent);
                    if (lString.length() > 0) {
                        activeTestButton.setActiveButtonContent(lString);
                    } else {
                        activeTestButton.setActiveButtonContent(activeTestButton.getActiveButtonId());
                    }
                }
                searchId.ggpClose();
                intent = new Intent("SPT_ACTIVE_TEST");
                bundle = new Bundle();
                bundle.putSerializable("ACTIVE_TEST", sptActiveTest);
                bundle.putInt(SharedPref.TYPE, 9);
                intent.putExtras(bundle);
                MainActivity.contexts.sendBroadcast(intent);
                i = 0;
                while (true) {
                    if (activeFlag.booleanValue() && inputBox.booleanValue()) {
                        activeFlag = Boolean.valueOf(false);
                        feedback = PageInteractiveDataAnalysis.feedbackData(Constant.activeNextCode, pageInteractiveData);
                        if (ByteHexHelper.intPackLength(Constant.activeNextCode) != 255) {
                            Constant.activeNextCode = Constant.noButton;
                        }
                        haveData = Boolean.valueOf(false);
                        return feedback;
                    }
                    i++;
                }
                break;
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                MainActivity.contexts.sendBroadcast(new Intent("SPT_EXIT_SHOW_WINDOW"));
                return PageInteractiveDataAnalysis.feedbackData(new byte[0], pageInteractiveData);
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                byte[] pictureByte = pageInteractiveData.getData();
                String pictureName = XmlPullParser.NO_NAMESPACE;
                path = Constant.OBD2_EN_GGP_PATH;
                if (path == null || path.length() <= 0) {
                    return null;
                }
                searchId = new SearchId();
                back = searchId.ggpOpen(Constant.OBD2_EN_GGP_PATH);
                i2 = (pictureByte[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                pictureName = ByteHexHelper.replaceBlank(ByteHexHelper.byteToWord(searchId.getTextFromLibReturnByte(((((pictureByte[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((pictureByte[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (pictureByte[3] & KEYRecord.PROTOCOL_ANY), 12)));
                searchId.ggpClose();
                intent = new Intent("SPT_SHOW_PICTURE");
                bundle = new Bundle();
                bundle.putString("SPT_SHOW_PICTURE", pictureName);
                bundle.putInt(SharedPref.TYPE, 11);
                intent.putExtras(bundle);
                MainActivity.contexts.sendBroadcast(intent);
                i = 0;
                while (!haveData.booleanValue()) {
                    i++;
                }
                haveData = Boolean.valueOf(false);
                return PageInteractiveDataAnalysis.feedbackData(Constant.streamNextCode, pageInteractiveData);
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                ArrayList<SptTroubleTest> troubleTestList = PageInteractiveDataAnalysis.spt_trouble_code14(pageInteractiveData);
                path = Constant.OBD2_EN_GGP_PATH;
                if (path != null && path.length() > 0) {
                    searchId = new SearchId();
                    back = searchId.ggpOpen(Constant.OBD2_EN_GGP_PATH);
                    for (i = 0; i < troubleTestList.size(); i++) {
                        byte[] troubleStateContent;
                        troubleTest = (SptTroubleTest) troubleTestList.get(i);
                        databuf = ByteHexHelper.hexStringToBytes(troubleTest.getTroubleId());
                        i2 = (databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                        troubleIdContentbytes = searchId.getTextFromLibReturnByte(((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databuf[3] & KEYRecord.PROTOCOL_ANY), 6);
                        if (troubleIdContentbytes.length > 0) {
                            troubleStateContent = new byte[(troubleIdContentbytes.length - 1)];
                            for (j = 0; j < troubleStateContent.length; j++) {
                                troubleStateContent[j] = troubleIdContentbytes[j];
                            }
                            break;
                        }
                        troubleStateContent = new byte[0];
                        troubleIdContent = PageInteractiveDataAnalysis.separateString(ByteHexHelper.bytesToHexString(troubleStateContent));
                        if (troubleIdContent == null || troubleIdContent.length <= 0) {
                            troubleTest.setTroubleCodeContent(troubleTest.getTroubleCodeContent());
                            troubleTest.setTroubleDescribeContent(troubleTest.getTroubleId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                        } else {
                            length = troubleIdContent.length;
                            if (r0 < 1 || !troubleIdContent[0].equalsIgnoreCase("---")) {
                                length = troubleIdContent.length;
                                if (r0 >= 1 && !troubleIdContent[0].equalsIgnoreCase("---")) {
                                    troubleTest.setTroubleCodeContent(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(troubleIdContent[0])));
                                }
                            } else {
                                troubleTest.setTroubleCodeContent(XmlPullParser.NO_NAMESPACE);
                            }
                            length = troubleIdContent.length;
                            if (r0 <= 1 || !troubleIdContent[1].equalsIgnoreCase("---")) {
                                length = troubleIdContent.length;
                                if (r0 > 1 && !troubleIdContent[1].equalsIgnoreCase("---")) {
                                    troubleTest.setTroubleDescribeContent(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(troubleIdContent[1])));
                                } else if (troubleIdContent.length == 0) {
                                    troubleTest.setTroubleDescribeContent(troubleTest.getTroubleId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                                }
                            } else {
                                troubleTest.setTroubleDescribeContent(troubleTest.getTroubleId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                            }
                        }
                        if (!troubleTest.getTroubleStateId().equalsIgnoreCase("ffffffff")) {
                            databufs = ByteHexHelper.hexStringToBytes(troubleTest.getTroubleStateId());
                            i2 = (databufs[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                            troubleStateContentbytes = searchId.getTextFromLibReturnByte(((((databufs[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databufs[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databufs[3] & KEYRecord.PROTOCOL_ANY), 7);
                            if (troubleStateContentbytes.length > 0) {
                                troubleStateContent = new byte[(troubleStateContentbytes.length - 1)];
                                for (j = 0; j < troubleStateContent.length; j++) {
                                    troubleStateContent[j] = troubleStateContentbytes[j];
                                }
                                break;
                            }
                            troubleStateContent = new byte[0];
                            troubleTest.setTroubleStateContent(ByteHexHelper.byteToWord(troubleStateContent));
                        }
                    }
                    searchId.ggpClose();
                    intent = new Intent("SPT_TROUBLE_CODE");
                    bundle = new Bundle();
                    bundle.putSerializable("SPT_TROUBLE_CODE", troubleTestList);
                    bundle.putInt(SharedPref.TYPE, 14);
                    intent.putExtras(bundle);
                    MainActivity.contexts.sendBroadcast(intent);
                    thread = new Thread(new CarDiagnose(new CarDiagnoseBridge()));
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
                feedback = PageInteractiveDataAnalysis.feedbackData(Constant.feedback, pageInteractiveData);
                Constant.feedback = null;
                return feedback;
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                SptInputNumric inputNumric = PageInteractiveDataAnalysis.spt_input_numric15(pageInteractiveData);
                intent = new Intent("SPT_INPUT_NUMERIC");
                Bundle bundleInputString = new Bundle();
                bundleInputString.putSerializable("SPT_INPUT_NUMERIC", inputNumric);
                bundleInputString.putInt(SharedPref.TYPE, 15);
                intent.putExtras(bundleInputString);
                MainActivity.contexts.sendBroadcast(intent);
                i = 0;
                while (!haveData.booleanValue()) {
                    i++;
                }
                haveData = Boolean.valueOf(false);
                feedback = PageInteractiveDataAnalysis.feedbackData(Constant.feedback, pageInteractiveData);
                Constant.feedback = null;
                return feedback;
            case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                SptInputStringEx inputString = PageInteractiveDataAnalysis.spt_inputString_ex16(pageInteractiveData);
                intent = new Intent("SPT_INPUTSTRING_EX");
                Bundle bundleInputString16 = new Bundle();
                bundleInputString16.putSerializable("SPT_INPUTSTRING_EX", inputString);
                bundleInputString16.putInt(SharedPref.TYPE, 16);
                intent.putExtras(bundleInputString16);
                MainActivity.contexts.sendBroadcast(intent);
                i = 0;
                while (!haveData.booleanValue()) {
                    i++;
                }
                haveData = Boolean.valueOf(false);
                feedback = PageInteractiveDataAnalysis.feedbackData(Constant.feedback, pageInteractiveData);
                Constant.feedback = null;
                return feedback;
            case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                ArrayList<SptStreamSelectIdItem> streamSelectIdlist = PageInteractiveDataAnalysis.spt_stream_select_id_ex17(pageInteractiveData);
                path = Constant.OBD2_EN_GGP_PATH;
                if (path == null || path.length() <= 0) {
                    return null;
                }
                searchId = new SearchId();
                back = searchId.ggpOpen(Constant.OBD2_EN_GGP_PATH);
                for (i = 1; i < streamSelectIdlist.size(); i++) {
                    SptStreamSelectIdItem streamSelectIdItem = (SptStreamSelectIdItem) streamSelectIdlist.get(i);
                    lString = XmlPullParser.NO_NAMESPACE;
                    databuf = ByteHexHelper.hexStringToBytes(streamSelectIdItem.getStreamSelectId());
                    i2 = (databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                    activeContentbytes = searchId.getTextFromLibReturnByte(((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databuf[3] & KEYRecord.PROTOCOL_ANY), 3);
                    if (activeContentbytes.length > 0) {
                        activeContent = new byte[(activeContentbytes.length - 1)];
                        for (j = 0; j < activeContent.length; j++) {
                            activeContent[j] = activeContentbytes[j];
                        }
                        break;
                    }
                    activeContent = new byte[0];
                    String dataStr = XmlPullParser.NO_NAMESPACE;
                    if (activeContent.length > 0) {
                        datas = PageInteractiveDataAnalysis.separateString(ByteHexHelper.bytesToHexString(activeContent));
                        length = datas.length;
                        if (r0 >= 1) {
                            dataStr = datas[0].equalsIgnoreCase("---") ? XmlPullParser.NO_NAMESPACE : datas[0];
                        }
                    }
                    lString = ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(dataStr));
                    if (lString.length() > 0) {
                        streamSelectIdItem.setStreamSelectStr(lString);
                    } else {
                        streamSelectIdItem.setStreamSelectStr(streamSelectIdItem.getStreamSelectId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                    }
                }
                searchId.ggpClose();
                intent = new Intent("SPT_STREAM_SELECT_ID_EX");
                bundle = new Bundle();
                bundle.putSerializable("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                bundle.putInt(SharedPref.TYPE, 17);
                intent.putExtras(bundle);
                MainActivity.contexts.sendBroadcast(intent);
                thread = new Thread(new CarDiagnose(new CarDiagnoseBridge()));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e22) {
                    e22.printStackTrace();
                }
                int firstSite = ByteHexHelper.hexString2binaryString(ByteHexHelper.bytesToHexString(Constant.feedback)).indexOf(Contact.RELATION_FRIEND);
                if (firstSite == -1) {
                    firstSite = 0;
                }
                byte[] serialNum = ByteHexHelper.hexStringToBytes(ByteHexHelper.packLength(firstSite));
                byte[] number = ByteHexHelper.hexStringToBytes(ByteHexHelper.packLength(Constant.feedback.length));
                byte[] feedbackByte = new byte[((serialNum.length + Constant.feedback.length) + number.length)];
                feedbackByte[0] = serialNum[0];
                feedbackByte[1] = serialNum[1];
                feedbackByte[2] = number[0];
                feedbackByte[3] = number[1];
                int flag = 0;
                i = 4;
                while (true) {
                    if (i >= (serialNum.length + Constant.feedback.length) + number.length) {
                        feedback = PageInteractiveDataAnalysis.feedbackData(feedbackByte, pageInteractiveData);
                        Constant.feedback = null;
                        return feedback;
                    }
                    feedbackByte[i] = Constant.feedback[flag];
                    flag++;
                    i++;
                }
                break;
            case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = PageInteractiveDataAnalysis.spt_ex_datastream_id18(pageInteractiveData);
                path = Constant.OBD2_EN_GGP_PATH;
                if (path == null || path.length() <= 0) {
                    return null;
                }
                searchId = new SearchId();
                back = searchId.ggpOpen(Constant.OBD2_EN_GGP_PATH);
                for (i = 0; i < exDataStreamIdlist.size(); i++) {
                    SptExDataStreamIdItem exStreamSelectIdItem = (SptExDataStreamIdItem) exDataStreamIdlist.get(i);
                    lString = XmlPullParser.NO_NAMESPACE;
                    databuf = ByteHexHelper.hexStringToBytes(exStreamSelectIdItem.getStreamTextId());
                    i2 = (databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                    activeContentbytes = searchId.getTextFromLibReturnByte(((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databuf[3] & KEYRecord.PROTOCOL_ANY), 3);
                    if (activeContentbytes.length > 0) {
                        activeContent = new byte[(activeContentbytes.length - 1)];
                        for (j = 0; j < activeContent.length; j++) {
                            activeContent[j] = activeContentbytes[j];
                        }
                        break;
                    }
                    activeContent = new byte[0];
                    datas = PageInteractiveDataAnalysis.separateString(ByteHexHelper.bytesToHexString(activeContent));
                    length = datas.length;
                    if (r0 < 1 || !datas[0].equalsIgnoreCase("---")) {
                        length = datas.length;
                        if (r0 >= 1 && !datas[0].equalsIgnoreCase("---")) {
                            exStreamSelectIdItem.setStreamTextIdContent(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(datas[0])));
                        } else if (datas.length == 0) {
                            exStreamSelectIdItem.setStreamTextIdContent(exStreamSelectIdItem.getStreamTextId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                        }
                    } else {
                        exStreamSelectIdItem.setStreamTextIdContent(exStreamSelectIdItem.getStreamTextId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                    }
                    length = datas.length;
                    if (r0 <= 1 || !datas[1].equalsIgnoreCase("---")) {
                        length = datas.length;
                        if (r0 > 1 && !datas[1].equalsIgnoreCase("---")) {
                            exStreamSelectIdItem.setStreamState(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(datas[1])));
                        }
                    } else {
                        exStreamSelectIdItem.setStreamState(XmlPullParser.NO_NAMESPACE);
                    }
                }
                searchId.ggpClose();
                intent = new Intent("SPT_EX_DATASTREAM_ID");
                bundle = new Bundle();
                bundle.putSerializable("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                bundle.putInt(SharedPref.TYPE, 18);
                intent.putExtras(bundle);
                MainActivity.contexts.sendBroadcast(intent);
                thread = new Thread(new CarDiagnose(new CarDiagnoseBridge()));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e222) {
                    e222.printStackTrace();
                }
                haveData = Boolean.valueOf(false);
                return PageInteractiveDataAnalysis.feedbackData(Constant.streamNextCode, pageInteractiveData);
            case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = PageInteractiveDataAnalysis.spt_vw_datastream_id19(pageInteractiveData);
                path = Constant.OBD2_EN_GGP_PATH;
                if (path == null || path.length() <= 0) {
                    return null;
                }
                searchId = new SearchId();
                back = searchId.ggpOpen(Constant.OBD2_EN_GGP_PATH);
                for (i = 0; i < vwDataStreamIdlist.size(); i++) {
                    SptVwDataStreamIdItem vwStreamSelectIdItem = (SptVwDataStreamIdItem) vwDataStreamIdlist.get(i);
                    lString = XmlPullParser.NO_NAMESPACE;
                    databuf = ByteHexHelper.hexStringToBytes(vwStreamSelectIdItem.getStreamTextId());
                    i2 = (databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                    activeContentbytes = searchId.getTextFromLibReturnByte(((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databuf[3] & KEYRecord.PROTOCOL_ANY), 3);
                    if (activeContentbytes.length > 0) {
                        activeContent = new byte[(activeContentbytes.length - 1)];
                        for (j = 0; j < activeContent.length; j++) {
                            activeContent[j] = activeContentbytes[j];
                        }
                        break;
                    }
                    activeContent = new byte[0];
                    datas = PageInteractiveDataAnalysis.separateString(ByteHexHelper.bytesToHexString(activeContent));
                    length = datas.length;
                    if (r0 < 1 || !datas[0].equalsIgnoreCase("---")) {
                        length = datas.length;
                        if (r0 >= 1 && !datas[0].equalsIgnoreCase("---")) {
                            vwStreamSelectIdItem.setStreamTextIdContent(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(datas[0])));
                        } else if (datas.length == 0) {
                            vwStreamSelectIdItem.setStreamTextIdContent(vwStreamSelectIdItem.getStreamTextId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                        }
                    } else {
                        vwStreamSelectIdItem.setStreamTextIdContent(vwStreamSelectIdItem.getStreamTextId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                    }
                    lString = XmlPullParser.NO_NAMESPACE;
                    databuf = ByteHexHelper.hexStringToBytes(vwStreamSelectIdItem.getStreamUnitId());
                    i2 = (databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                    activeContentbytes = searchId.getTextFromLibReturnByte(((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databuf[3] & KEYRecord.PROTOCOL_ANY), 3);
                    if (activeContentbytes.length > 0) {
                        activeContent = new byte[(activeContentbytes.length - 1)];
                        for (j = 0; j < activeContent.length; j++) {
                            activeContent[j] = activeContentbytes[j];
                        }
                        break;
                    }
                    activeContent = new byte[0];
                    datas = PageInteractiveDataAnalysis.separateString(ByteHexHelper.bytesToHexString(activeContent));
                    length = datas.length;
                    if (r0 <= 1 || !datas[1].equalsIgnoreCase("---")) {
                        length = datas.length;
                        if (r0 > 1 && !datas[1].equalsIgnoreCase("---")) {
                            vwStreamSelectIdItem.setStreamUnitIdContent(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(datas[1])));
                        }
                    } else {
                        vwStreamSelectIdItem.setStreamUnitIdContent(XmlPullParser.NO_NAMESPACE);
                    }
                }
                searchId.ggpClose();
                intent = new Intent("SPT_VW_DATASTREAM_ID");
                bundle = new Bundle();
                bundle.putSerializable("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                bundle.putInt(SharedPref.TYPE, 19);
                intent.putExtras(bundle);
                MainActivity.contexts.sendBroadcast(intent);
                thread = new Thread(new CarDiagnose(new CarDiagnoseBridge()));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e2222) {
                    e2222.printStackTrace();
                }
                haveData = Boolean.valueOf(false);
                feedback = PageInteractiveDataAnalysis.feedbackData(Constant.streamNextCode, pageInteractiveData);
                if (Constant.streamNextCode == Constant.noInterruptStreamCode) {
                    return feedback;
                }
                Constant.streamNextCode = Constant.noInterruptStreamCode;
                return feedback;
            case Protocol.LEAF_2 /*26*/:
                Constant.OBD2_EN_GGP_PATH = PageInteractiveDataAnalysis.Spt_GGP_Name26(pageInteractiveData);
                return PageInteractiveDataAnalysis.feedbackData(new byte[0], pageInteractiveData);
            case Service.NSW_FE /*27*/:
                ArrayList<SptTroubleTest> troubleCodeFrozenList = PageInteractiveDataAnalysis.spt_trouble_code14(pageInteractiveData);
                path = Constant.OBD2_EN_GGP_PATH;
                if (path == null || path.length() <= 0) {
                    return null;
                }
                searchId = new SearchId();
                back = searchId.ggpOpen(Constant.OBD2_EN_GGP_PATH);
                for (i = 0; i < troubleCodeFrozenList.size(); i++) {
                    byte[] troubleId;
                    troubleTest = (SptTroubleTest) troubleCodeFrozenList.get(i);
                    databuf = ByteHexHelper.hexStringToBytes(troubleTest.getTroubleId());
                    i2 = (databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                    troubleIdContentbytes = searchId.getTextFromLibReturnByte(((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databuf[3] & KEYRecord.PROTOCOL_ANY), 6);
                    if (troubleIdContentbytes.length > 0) {
                        troubleId = new byte[(troubleIdContentbytes.length - 1)];
                        for (j = 0; j < troubleId.length; j++) {
                            troubleId[j] = troubleIdContentbytes[j];
                        }
                        break;
                    }
                    troubleId = new byte[0];
                    troubleIdContent = PageInteractiveDataAnalysis.separateString(ByteHexHelper.bytesToHexString(troubleId));
                    if (troubleIdContent == null || troubleIdContent.length <= 0) {
                        lString = troubleTest.getTroubleSpareDescribeContent();
                        if (lString.length() > 0) {
                            troubleTest.setTroubleCodeContent(lString);
                        } else {
                            troubleTest.setTroubleCodeContent(troubleTest.getTroubleId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                        }
                    } else {
                        length = troubleIdContent.length;
                        if (r0 < 1 || !troubleIdContent[0].equalsIgnoreCase("---")) {
                            length = troubleIdContent.length;
                            if (r0 >= 1 && !troubleIdContent[0].equalsIgnoreCase("---")) {
                                troubleTest.setTroubleCodeContent(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(troubleIdContent[0])));
                            }
                        } else {
                            troubleTest.setTroubleCodeContent(XmlPullParser.NO_NAMESPACE);
                        }
                        length = troubleIdContent.length;
                        if (r0 <= 1 || !troubleIdContent[1].equalsIgnoreCase("---")) {
                            length = troubleIdContent.length;
                            if (r0 > 1 && !troubleIdContent[1].equalsIgnoreCase("---")) {
                                troubleTest.setTroubleDescribeContent(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(troubleIdContent[1])));
                            }
                        } else {
                            troubleTest.setTroubleDescribeContent(troubleTest.getTroubleId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                        }
                    }
                    if (!troubleTest.getTroubleStateId().equalsIgnoreCase("ffffffff")) {
                        byte[] troubleState;
                        databufs = ByteHexHelper.hexStringToBytes(troubleTest.getTroubleStateId());
                        i2 = (databufs[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                        troubleStateContentbytes = searchId.getTextFromLibReturnByte(((((databufs[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databufs[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databufs[3] & KEYRecord.PROTOCOL_ANY), 7);
                        if (troubleStateContentbytes.length > 0) {
                            troubleState = new byte[(troubleStateContentbytes.length - 1)];
                            for (j = 0; j < troubleState.length; j++) {
                                troubleState[j] = troubleStateContentbytes[j];
                            }
                            break;
                        }
                        troubleState = new byte[0];
                        troubleTest.setTroubleStateContent(ByteHexHelper.byteToWord(troubleState));
                    }
                }
                searchId.ggpClose();
                intent = new Intent("SPT_TROUBLE_CODE_FROZEN");
                bundle = new Bundle();
                bundle.putSerializable("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                bundle.putInt(SharedPref.TYPE, 27);
                intent.putExtras(bundle);
                MainActivity.contexts.sendBroadcast(intent);
                thread = new Thread(new CarDiagnose(new CarDiagnoseBridge()));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e22222) {
                    e22222.printStackTrace();
                }
                feedback = PageInteractiveDataAnalysis.feedbackData(Constant.feedback, pageInteractiveData);
                Constant.feedback = null;
                return feedback;
            case Protocol.IRTP /*28*/:
                ArrayList<MenuData> dsMenuDataList = PageInteractiveDataAnalysis.spt_menu_id1(pageInteractiveData);
                path = Constant.OBD2_EN_GGP_PATH;
                if (path == null || path.length() <= 0) {
                    return null;
                }
                searchId = new SearchId();
                back = searchId.ggpOpen(Constant.OBD2_EN_GGP_PATH);
                for (i = 1; i < dsMenuDataList.size(); i++) {
                    byte[] menu;
                    menuData = (MenuData) dsMenuDataList.get(i);
                    lString = XmlPullParser.NO_NAMESPACE;
                    databuf = ByteHexHelper.hexStringToBytes(menuData.getMenuId());
                    i2 = (databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                    MenuContentbytes = searchId.getTextFromLibReturnByte(((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databuf[3] & KEYRecord.PROTOCOL_ANY), 3);
                    if (MenuContentbytes.length > 0) {
                        menu = new byte[(MenuContentbytes.length - 1)];
                        for (j = 0; j < menu.length; j++) {
                            menu[j] = MenuContentbytes[j];
                        }
                        break;
                    }
                    menu = new byte[0];
                    String[] menuIdContent = PageInteractiveDataAnalysis.separateString(ByteHexHelper.bytesToHexString(menu));
                    if (menuIdContent == null || menuIdContent.length <= 0) {
                        menuData.setMenuContent(menuData.getMenuId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                    } else {
                        length = menuIdContent.length;
                        if (r0 < 1 || !menuIdContent[0].equalsIgnoreCase("---")) {
                            length = menuIdContent.length;
                            if (r0 >= 1 && !menuIdContent[0].equalsIgnoreCase("---")) {
                                menuData.setMenuContent(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(menuIdContent[0])));
                            }
                        } else {
                            menuData.setMenuContent(menuData.getMenuId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                        }
                    }
                }
                searchId.ggpClose();
                intent = new Intent("SPT_DS_MENU_ID");
                bundle = new Bundle();
                bundle.putSerializable("SPT_DS_MENU_ID", dsMenuDataList);
                bundle.putInt(SharedPref.TYPE, 28);
                intent.putExtras(bundle);
                MainActivity.contexts.sendBroadcast(intent);
                thread = new Thread(new CarDiagnose(new CarDiagnoseBridge()));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e222222) {
                    e222222.printStackTrace();
                }
                backData = Constant.feedback;
                Constant.feedback = null;
                return PageInteractiveDataAnalysis.feedbackData(backData, pageInteractiveData);
            case Service.MSG_ICP /*29*/:
                ArrayList<MenuData> filemenuDataList = PageInteractiveDataAnalysis.spt_file_menu(pageInteractiveData);
                intent = new Intent("SPT_FILE_MENU");
                Bundle bundle29 = new Bundle();
                bundle29.putSerializable("SPT_FILE_MENU", filemenuDataList);
                bundle29.putInt(SharedPref.TYPE, 29);
                intent.putExtras(bundle29);
                MainActivity.contexts.sendBroadcast(intent);
                thread = new Thread(new CarDiagnose(new CarDiagnoseBridge()));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e2222222) {
                    e2222222.printStackTrace();
                }
                byte[] backData1 = Constant.feedback;
                Constant.feedback = null;
                return PageInteractiveDataAnalysis.feedbackData(backData1, pageInteractiveData);
            case Protocol.NETBLT /*30*/:
                String agingContent = PageInteractiveDataAnalysis.spt_aging_window30(pageInteractiveData);
                intent = new Intent("SPT_AGING_WINDOW");
                bundle = new Bundle();
                bundle.putString("SPT_AGING_WINDOW", agingContent);
                bundle.putInt(SharedPref.TYPE, 30);
                intent.putExtras(bundle);
                MainActivity.contexts.sendBroadcast(intent);
                i = 0;
                while (!agingFlag.booleanValue()) {
                    i++;
                }
                agingFlag = Boolean.valueOf(false);
                haveData = Boolean.valueOf(false);
                return PageInteractiveDataAnalysis.feedbackData(Constant.continueObtain, pageInteractiveData);
            case Type.ATMA /*34*/:
                return dataStreamItem34(pageInteractiveData, null);
            case Type.NAPTR /*35*/:
                return dataStreamItem35(pageInteractiveData, null);
            case Type.KX /*36*/:
                return specialFunchtion(pageInteractiveData, null);
            case Service.TIME /*37*/:
                return spt_trouble_bode_id_ex(pageInteractiveData, null, "SPT_TROUBLE_CODE_ID_EX", 37);
            case Type.A6 /*38*/:
                return spt_trouble_bode_id_ex(pageInteractiveData, null, "SPT_TROUBLE_CODE_FROZEN_EX", 38);
            case Service.RLP /*39*/:
                return spt_trouble_bode_id_ex(pageInteractiveData, null, "SPT_TROUBLE_CODE_ID_BY_COMBINE", 39);
            case SmileConstants.TOKEN_MISC_FP /*40*/:
                return spt_trouble_bode_id_ex(pageInteractiveData, null, "SPT_TROUBLE_CODE_FROZEN_BY_COMBINE", 40);
            case Service.GRAPHICS /*41*/:
                return spt_trouble_bode_id_ex(pageInteractiveData, null, "SPT_TROUBLE_CODE_ID_EX_BY_COMBINE", 41);
            case Service.NAMESERVER /*42*/:
                return spt_trouble_bode_id_ex(pageInteractiveData, null, "SPT_TROUBLE_CODE_FROZEN_EX_BY_COMBINE", 42);
            case Service.NICNAME /*43*/:
                return spt_trouble_bode_id_ex(pageInteractiveData, null, "SPT_TROUBLE_CODE_ID_BY_LOOPMODE", 43);
            case Service.MPM_FLAGS /*44*/:
                String[] nowaitmessagebox = PageInteractiveDataAnalysis.Spt_NoWaitMessageBox_Text44(pageInteractiveData);
                intent = new Intent("SPT_NOWAITMESSAGEBOX_TEXT");
                Bundle bundleNowaitmessagebox = new Bundle();
                bundleNowaitmessagebox.putStringArray("SPT_NOWAITMESSAGEBOX_TEXT", nowaitmessagebox);
                bundleNowaitmessagebox.putInt(SharedPref.TYPE, 44);
                intent.putExtras(bundleNowaitmessagebox);
                MainActivity.contexts.sendBroadcast(intent);
                i = 0;
                while (!nowaitmessageboxtext.booleanValue()) {
                    i++;
                }
                nowaitmessageboxtext = Boolean.valueOf(false);
                haveData = Boolean.valueOf(false);
                return PageInteractiveDataAnalysis.feedbackData(Constant.noWaitMessageButton, pageInteractiveData);
            case Service.MPM /*45*/:
                Spt_Progressbar_Box spb = PageInteractiveDataAnalysis.Spt_Progressbar_Box45(pageInteractiveData);
                intent = new Intent("SPT_PROGRESSBAR_BOX");
                Bundle bundleProgerssbar = new Bundle();
                bundleProgerssbar.putSerializable("SPT_PROGRESSBAR_BOX", spb);
                bundleProgerssbar.putInt(SharedPref.TYPE, 45);
                intent.putExtras(bundleProgerssbar);
                MainActivity.contexts.sendBroadcast(intent);
                i = 0;
                while (!progressbarFlag.booleanValue()) {
                    i++;
                }
                progressbarFlag = Boolean.valueOf(false);
                haveData = Boolean.valueOf(false);
                return PageInteractiveDataAnalysis.feedbackData(Constant.progressbarBox, pageInteractiveData);
            case Service.MPM_SND /*46*/:
                Spt_Combination_Menu spt_Combination_menu = PageInteractiveDataAnalysis.spt_combination_menu46(pageInteractiveData);
                intent = new Intent("SPT_COMBINATION_MENU");
                Bundle bundle46 = new Bundle();
                bundle46.putSerializable("SPT_COMBINATION_MENU", spt_Combination_menu);
                bundle46.putInt(SharedPref.TYPE, 46);
                intent.putExtras(bundle46);
                MainActivity.contexts.sendBroadcast(intent);
                i = 0;
                while (!combinationFlag.booleanValue()) {
                    i++;
                }
                combinationFlag = Boolean.valueOf(false);
                haveData = Boolean.valueOf(false);
                return PageInteractiveDataAnalysis.feedbackData(Constant.CombinationMenu, pageInteractiveData);
            case Service.NI_FTP /*47*/:
                Log.e("bcf", "SPT_SET_VIN");
                String mVin = PageInteractiveDataAnalysis.SPT_SET_VIN_47(pageInteractiveData);
                intent = new Intent("SPT_SET_VIN");
                intent.putExtra("SPT_SET_VIN", mVin);
                Log.e("bcf", "47  SPT_SET_VIN  mVin:" + mVin);
                MainActivity.contexts.getApplicationContext().sendBroadcast(intent);
                return PageInteractiveDataAnalysis.feedbackData(Constant.noButton, pageInteractiveData);
            case Type.DNSKEY /*48*/:
                Log.e("bcf", "SPT_GET_VIN");
                Log.e("bcf", "vincode\uff1a--------");
                String vincode = MySharedPreferences.getStringValue(MainActivity.contexts, "VIN_CODE");
                Log.e("bcf", "vincode\uff1a" + vincode);
                String vinAscii = ByteHexHelper.parseAscii(vincode);
                Object mVinBack = ByteHexHelper.hexStringToBytes(vinAscii);
                Log.e("weizewei", "\u8f6c\u5316vincode\u7684\u5b57\u4e32\uff1a" + vincode);
                Log.e("weizewei", "\u8f6c\u5316vinAscii\u7684\u5b57\u4e32\uff1a" + vinAscii);
                Log.e("weizewei", "\u8f6c\u5316mVinBack\u7684\u5b57\u4e32\uff1a" + mVinBack);
                Log.e("weizewei", "\u8f6c\u5316\u540e\u7684\u5b57\u4e32\u8f6c\u5316\u4e3a\u5b57\u7b26\uff1a" + ByteHexHelper.hexStringToWord(vinAscii));
                return PageInteractiveDataAnalysis.feedbackData(mVinBack, pageInteractiveData);
            default:
                return null;
        }
    }

    private static byte[] dataStreamItem34(PageInteractiveData uiData, byte[] feedback) {
        ArrayList<SptExDataStreamIdItem34> backList = PageInteractiveDataAnalysis.spt_datastream_item_back_call34(uiData);
        ArrayList<SptExDataStreamIdItem> list = PageInteractiveDataAnalysis.spt_datastream_item34(uiData);
        Intent intent = new Intent("SPT_DATASTREAM_ID_EX");
        Bundle bundle = new Bundle();
        bundle.putSerializable("SPT_DATASTREAM_ID_EX", list);
        bundle.putInt(SharedPref.TYPE, 34);
        intent.putExtras(bundle);
        MainActivity.contexts.sendBroadcast(intent);
        byte[] dataStreamEmptyBack = new byte[2];
        byte[] dataStreamCountBack = ByteHexHelper.intToTwoHexBytes(((SptExDataStreamIdItem34) backList.get(0)).getDataStreamItemCount());
        byte[] dataStreamButtonBack = Constant.spt_data_stream_button;
        byte[] dataStreamMaskLengthBack = ByteHexHelper.intToTwoHexBytes(((SptExDataStreamIdItem34) backList.get(0)).getChoseMaskBufferList().length);
        byte[] dataStreamMaskBack = ((SptExDataStreamIdItem34) backList.get(0)).getChoseMaskBufferList();
        int i = 0;
        while (!dataStreamFlag.booleanValue()) {
            i++;
        }
        byte[] comboDataStreamEmptyBack = new byte[]{dataStreamEmptyBack[0], dataStreamEmptyBack[1], dataStreamCountBack[0], dataStreamCountBack[1], dataStreamButtonBack[0], dataStreamMaskLengthBack[1], dataStreamMaskBack[0], dataStreamMaskBack[1], dataStreamMaskBack[2]};
        dataStreamFlag = Boolean.valueOf(false);
        Constant.spt_data_stream = comboDataStreamEmptyBack;
        feedback = PageInteractiveDataAnalysis.feedbackData(Constant.spt_data_stream, uiData);
        Constant.spt_data_stream_button = new byte[1];
        return feedback;
    }

    private static byte[] dataStreamItem35(PageInteractiveData uiData, byte[] feedback) {
        Constant.spt_data_stream = PageInteractiveDataAnalysis.spt_datastreamitem_id35(uiData);
        return PageInteractiveDataAnalysis.feedbackData(Constant.spt_data_stream, uiData);
    }

    private static byte[] specialFunchtion(PageInteractiveData uiData, byte[] feedback) {
        ArrayList<SpecialFunction> list = PageInteractiveDataAnalysis.spt_special_function_id36(uiData);
        Intent intent = new Intent("SPT_SPECIAL_FUNCTION_ID");
        Bundle bundle = new Bundle();
        bundle.putSerializable("SPT_SPECIAL_FUNCTION_ID", list);
        bundle.putInt(SharedPref.TYPE, 36);
        intent.putExtras(bundle);
        MainActivity.contexts.sendBroadcast(intent);
        feedback = PageInteractiveDataAnalysis.feedbackData(Constant.continueSpecia, uiData);
        Constant.continueSpecia = new byte[]{(byte) -1};
        return feedback;
    }

    private static byte[] spt_trouble_bode_id_ex(PageInteractiveData uiData, byte[] feedback, String action, int type) {
        ArrayList<SptTroubleTest> troubleCodeByLoopModeList = PageInteractiveDataAnalysis.spe_trouble_bode_loopmode(uiData, type);
        Intent intent = new Intent(action);
        Bundle bundle = new Bundle();
        bundle.putSerializable(action, troubleCodeByLoopModeList);
        bundle.putInt(SharedPref.TYPE, type);
        intent.putExtras(bundle);
        MainActivity.contexts.sendBroadcast(intent);
        Thread t = new Thread(new CarDiagnose(new CarDiagnoseBridge()));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        haveData = Boolean.valueOf(false);
        return PageInteractiveDataAnalysis.feedbackData(Constant.streamNextCode, uiData);
    }

    public static void getGGPname(String name) {
    }

    public static void notifyError(int error) {
    }

    public static int GetLocalLanguage() {
        if (MainActivity.country == null || MainActivity.country.length() <= 0) {
            MainActivity.country = Locale.getDefault().getCountry();
        }
        return AndroidToLan.languages(MainActivity.country);
    }
}
