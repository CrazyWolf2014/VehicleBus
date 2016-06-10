package com.ifoer.expedition.BluetoothOrder;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.TextUtils;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.entity.MenuData;
import com.ifoer.entity.PageInteractiveData;
import com.ifoer.entity.SpecialFunction;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptActiveTestButton;
import com.ifoer.entity.SptActiveTestStream;
import com.ifoer.entity.SptDataStreamId;
import com.ifoer.entity.SptDataStreamIdItem;
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
import com.ifoer.entity.SupportConnectorId;
import com.ifoer.expedition.BluetoothChat.DataStreamUtils;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.mine.Contact;
import java.util.ArrayList;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class PageInteractiveDataAnalysis {
    private static final boolean f1288D = false;

    public static PageInteractiveData dataUtil(byte[] buffer) {
        PageInteractiveData uiData = new PageInteractiveData();
        int bufferLen = buffer.length;
        if (bufferLen > 2) {
            int packageLen = ByteHexHelper.intPackLength(new byte[]{buffer[0], buffer[1]});
            if (bufferLen >= packageLen) {
                String packageId = ByteHexHelper.byteToHexString(buffer[2]);
                uiData.setPackageId(buffer[2]);
                uiData.setPackageType(ByteHexHelper.byteToInt(buffer[3]));
                byte[] temp = new byte[(packageLen - 4)];
                int flag = 0;
                for (int i = 4; i < packageLen; i++) {
                    temp[flag] = buffer[i];
                    flag++;
                }
                uiData.setData(temp);
                if (ByteHexHelper.bytesToHexString(temp) == null) {
                    return uiData;
                }
            }
        }
        return uiData;
    }

    public static ArrayList<MenuData> spt_menu_id1(PageInteractiveData uiData) {
        ArrayList<MenuData> menuList = new ArrayList();
        byte[] menuData = uiData.getData();
        int menuLen = menuData.length;
        int site = 0;
        if (menuLen > 1) {
            if (ByteHexHelper.byteToHexString(menuData[0]).equalsIgnoreCase("FF")) {
            }
            if (menuLen > 2) {
                try {
                    site = Integer.valueOf(ByteHexHelper.intPackLength(ByteHexHelper.byteToHexString(menuData[1]))).intValue();
                } catch (NumberFormatException e) {
                }
                if (menuLen > 3) {
                    int menuNums = ByteHexHelper.byteToInt(menuData[2]);
                    if (menuData.length == (menuNums * 4) + 3) {
                        MenuData item = new MenuData();
                        item.setSite(site);
                        menuList.add(item);
                        String dataStr = ByteHexHelper.bytesToHexString(menuData).substring(6);
                        if (dataStr.length() == menuNums * 8) {
                            for (int i = 0; i < menuNums; i++) {
                                MenuData menu = new MenuData();
                                menu.setMenuId(dataStr.substring(i * 8, (i * 8) + 8));
                                menuList.add(menu);
                            }
                        }
                    }
                }
            }
        }
        return menuList;
    }

    public static Spt_Nobuttonbox_Text Spt_Nobuttonbox_Text5(PageInteractiveData uiData) {
        Spt_Nobuttonbox_Text nobuttonbox = new Spt_Nobuttonbox_Text();
        String[] datas = separateString(ByteHexHelper.bytesToHexString(uiData.getData()));
        if (datas[0].equalsIgnoreCase("---")) {
            nobuttonbox.setTitle(XmlPullParser.NO_NAMESPACE);
        } else {
            nobuttonbox.setTitle(ByteHexHelper.hexStringToWord(datas[0]));
        }
        if (datas[1].equalsIgnoreCase("---")) {
            nobuttonbox.setContent(XmlPullParser.NO_NAMESPACE);
        } else {
            nobuttonbox.setContent(ByteHexHelper.hexStringToWord(datas[1]));
        }
        return nobuttonbox;
    }

    public static String[] Spt_NoWaitMessageBox_Text44(PageInteractiveData uiData) {
        byte[] streamBytes = uiData.getData();
        String string = ByteHexHelper.hexStringToWord(ByteHexHelper.bytesToHexString(streamBytes));
        String[] nowaitmessagebox = ByteHexHelper.bytesToHexString(streamBytes).split("00");
        for (int i = 0; i < nowaitmessagebox.length; i++) {
            nowaitmessagebox[i] = ByteHexHelper.hexStringToWord(nowaitmessagebox[i]);
        }
        return nowaitmessagebox;
    }

    public static String Spt_GGP_Name26(PageInteractiveData uiData) {
        String dataStr = ByteHexHelper.bytesToHexString(uiData.getData()).split("00")[0];
        if (dataStr.length() % 2 == 1) {
            dataStr = new StringBuilder(String.valueOf(dataStr)).append(Contact.RELATION_ASK).toString();
        }
        return ByteHexHelper.hexStringToWord(dataStr);
    }

    public static String[] separateString(String hexString) {
        if (hexString == null || hexString.length() <= 0) {
            return new String[0];
        }
        StringBuffer strB = new StringBuffer();
        int hexStrLen = hexString.length();
        int flag = 0;
        for (int i = 0; i < hexStrLen / 2; i++) {
            if (hexString.substring(i * 2, (i * 2) + 2).equalsIgnoreCase("00")) {
                String content = hexString.substring(flag, i * 2);
                if (content == null || content.length() <= 0) {
                    content = "---";
                }
                strB.append(content);
                flag = (i + 1) * 2;
                strB.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            }
            if (i == (hexStrLen / 2) - 1) {
                strB.append(hexString.substring(flag));
            }
        }
        return strB.toString().split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
    }

    public static String splitString2(String buffer, int sNum) {
        StringBuffer sb = new StringBuffer();
        String bufferStr = buffer;
        int bufferStrLen = bufferStr.length();
        Boolean isId = Boolean.valueOf(true);
        int idNum = 0;
        int start = 0;
        int i = 0;
        while (i < bufferStrLen / 2) {
            if (isId.booleanValue() && idNum < 4) {
                sb.append(bufferStr.substring(i * 2, (i * 2) + 2));
                idNum++;
                if (idNum == 4) {
                    isId = Boolean.valueOf(false);
                    idNum = 0;
                    start = (i + 1) * 2;
                    sb.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                    i++;
                }
            }
            if (!isId.booleanValue() && bufferStr.substring(i * 2, (i * 2) + 2).equalsIgnoreCase("00")) {
                String content = bufferStr.substring(start, i * 2);
                if (content == null || content.length() <= 0) {
                    content = "---";
                }
                sb.append(content);
                sb.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                isId = Boolean.valueOf(true);
            }
            i++;
        }
        return sb.toString();
    }

    public static String splitString3(String buffer, int sNum) {
        StringBuffer sb = new StringBuffer();
        String bufferStr = buffer;
        int bufferStrLen = bufferStr.length();
        Boolean isId = Boolean.valueOf(true);
        int idNum = 0;
        int start = 0;
        int i = 0;
        while (i < bufferStrLen / 2) {
            if (isId.booleanValue() && idNum < 8) {
                sb.append(bufferStr.substring(i * 2, (i * 2) + 2));
                idNum++;
                if (idNum == 4) {
                    sb.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                }
                if (idNum == 8) {
                    isId = Boolean.valueOf(false);
                    idNum = 0;
                    start = (i + 1) * 2;
                    sb.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                    i++;
                }
            }
            if (!isId.booleanValue() && bufferStr.substring(i * 2, (i * 2) + 2).equalsIgnoreCase("00")) {
                String content = bufferStr.substring(start, i * 2);
                if (content == null || content.length() <= 0) {
                    content = "---";
                }
                sb.append(content);
                sb.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                isId = Boolean.valueOf(true);
            }
            i++;
        }
        return sb.toString();
    }

    public static String splitString4(String buffer, int sNum) {
        StringBuffer sb = new StringBuffer();
        String bufferStr = buffer;
        int bufferStrLen = bufferStr.length();
        Boolean isId = Boolean.valueOf(true);
        int idNum = 0;
        int start = 0;
        int i = 0;
        while (i < bufferStrLen / 2) {
            if (isId.booleanValue() && idNum < 8) {
                sb.append(bufferStr.substring(i * 2, (i * 2) + 2));
                idNum++;
                if (idNum == 4) {
                    sb.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                }
                if (idNum == 8) {
                    isId = Boolean.valueOf(false);
                    idNum = 0;
                    start = (i + 1) * 2;
                    sb.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                    i++;
                }
            }
            if (!isId.booleanValue() && bufferStr.substring(i * 2, (i * 2) + 2).equalsIgnoreCase("00")) {
                String content = bufferStr.substring(start, i * 2);
                if (content == null || content.length() <= 0) {
                    content = "---";
                }
                sb.append(content);
                sb.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                isId = Boolean.valueOf(true);
            }
            i++;
        }
        return sb.toString();
    }

    public static SptMessageBoxText spt_messageBox_text6(PageInteractiveData uiData) {
        SptMessageBoxText sptMessageBoxText = new SptMessageBoxText();
        byte[] data = uiData.getData();
        int dataLen = data.length;
        if (dataLen > 0) {
            int i;
            sptMessageBoxText.setDialogType(ByteHexHelper.byteToInt(data[0]));
            byte[] dialogData = new byte[(dataLen - 1)];
            for (i = 0; i < dataLen - 1; i++) {
                dialogData[i] = data[i + 1];
            }
            if (dialogData.length > 0) {
                String[] datas = separateString(ByteHexHelper.bytesToHexString(dialogData));
                for (i = 0; i < datas.length; i += 2) {
                    if (datas[i].equalsIgnoreCase("---")) {
                        sptMessageBoxText.setDialogTitle(XmlPullParser.NO_NAMESPACE);
                    } else {
                        sptMessageBoxText.setDialogTitle(ByteHexHelper.hexStringToWord(datas[i]));
                    }
                    if (datas[i + 1].equalsIgnoreCase("---")) {
                        sptMessageBoxText.setDialogContent(XmlPullParser.NO_NAMESPACE);
                    } else {
                        sptMessageBoxText.setDialogContent(ByteHexHelper.hexStringToWord(datas[i + 1]));
                    }
                }
            }
        }
        return sptMessageBoxText;
    }

    public static ArrayList<SptTroubleTest> spt_trouble_code14(PageInteractiveData uiData) {
        ArrayList<SptTroubleTest> list = new ArrayList();
        byte[] data = uiData.getData();
        int dataLen = data.length;
        if (dataLen > 2) {
            int i;
            int troubleNum = ByteHexHelper.intPackLength(new byte[]{data[0], data[1]});
            byte[] dataStream = new byte[(dataLen - 2)];
            int flag = 0;
            for (i = 2; i < dataLen; i++) {
                dataStream[flag] = data[i];
                flag++;
            }
            String[] stream = splitString3(ByteHexHelper.bytesToHexString(dataStream), troubleNum).split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            for (i = 0; i < stream.length / 3; i++) {
                SptTroubleTest trouble = new SptTroubleTest();
                trouble.setTroubleId(stream[i * 3]);
                trouble.setTroubleStateId(stream[(i * 3) + 1]);
                if (stream[(i * 3) + 2].equalsIgnoreCase("---")) {
                    trouble.setTroubleSpareDescribeContent(XmlPullParser.NO_NAMESPACE);
                } else {
                    trouble.setTroubleSpareDescribeContent(ByteHexHelper.hexStringToWord(stream[(i * 3) + 2]));
                }
                list.add(trouble);
            }
        }
        return list;
    }

    public static SptActiveTest spt_active_test9(PageInteractiveData uiData) {
        SptActiveTest sptActiveTest = new SptActiveTest();
        ArrayList<SptActiveTestButton> buttonList = new ArrayList();
        ArrayList<SptActiveTestStream> streamList = new ArrayList();
        byte[] data = uiData.getData();
        int dataLen = data.length;
        if (dataLen > 0) {
            int buttonNum = ByteHexHelper.byteToInt(data[0]);
            if (dataLen > 1) {
                int i;
                int buttonSite = 0;
                for (i = 0; i < buttonNum * 4; i += 4) {
                    SptActiveTestButton activeTestButton = new SptActiveTestButton();
                    activeTestButton.setActiveButtonId(ByteHexHelper.bytesToHexString(new byte[]{data[i + 1], data[i + 2], data[i + 3], data[i + 4]}));
                    activeTestButton.setActiveButtonSite(buttonSite);
                    buttonList.add(activeTestButton);
                    buttonSite++;
                }
                int index = (buttonNum * 4) + 1;
                int sNum = ByteHexHelper.byteToInt(data[index]);
                byte[] dataStream = new byte[(dataLen - (index + 1))];
                int dataStreamIndex = 0;
                for (i = index + 1; i < dataLen; i++) {
                    dataStream[dataStreamIndex] = data[i];
                    dataStreamIndex++;
                }
                String[] stream = splitString2(ByteHexHelper.bytesToHexString(dataStream), sNum).split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                i = 0;
                while (true) {
                    if (i >= stream.length / 2) {
                        break;
                    }
                    SptActiveTestStream activeTestStream = new SptActiveTestStream();
                    activeTestStream.setDataStreamId(stream[i * 2]);
                    if (stream[(i * 2) + 1].equalsIgnoreCase("---")) {
                        activeTestStream.setDataStreamStr(XmlPullParser.NO_NAMESPACE);
                    } else {
                        activeTestStream.setDataStreamStr(ByteHexHelper.hexStringToWord(stream[(i * 2) + 1]));
                    }
                    streamList.add(activeTestStream);
                    i++;
                }
            }
        }
        sptActiveTest.setActiveTestButtons(buttonList);
        sptActiveTest.setActiveTestStreams(streamList);
        return sptActiveTest;
    }

    public static byte[] feedbackData(byte[] data, PageInteractiveData uiData) {
        if (data == null) {
            data = new byte[0];
        }
        byte pageId = uiData.getPackageId();
        byte pageType = ByteHexHelper.intToHexByte(uiData.getPackageType());
        byte[] pageLen = ByteHexHelper.hexStringToBytes(ByteHexHelper.packLength(data.length + 4));
        byte[] feedbackDatas = new byte[(data.length + 4)];
        feedbackDatas[0] = pageLen[0];
        feedbackDatas[1] = pageLen[1];
        feedbackDatas[2] = pageId;
        feedbackDatas[3] = pageType;
        int flag = 0;
        for (int i = 4; i < data.length + 4; i++) {
            feedbackDatas[i] = data[flag];
            flag++;
        }
        return feedbackDatas;
    }

    public static byte[] feedbackData105(byte[] data, PageInteractiveData uiData) {
        if (data == null) {
            data = new byte[0];
        }
        byte pageId = uiData.getPackageId();
        byte pageType = ByteHexHelper.intToHexByte(uiData.getPackageType());
        byte[] pageLen = ByteHexHelper.hexStringToBytes(ByteHexHelper.packLength(data.length + 4));
        byte[] feedbackDatas = new byte[(data.length + 4)];
        byte[] datas = uiData.getData();
        feedbackDatas[0] = pageLen[0];
        feedbackDatas[1] = pageLen[1];
        feedbackDatas[2] = pageId;
        feedbackDatas[3] = pageType;
        int flag = 0;
        for (int i = 4; i < data.length + 4; i++) {
            feedbackDatas[i] = data[flag];
            flag++;
        }
        return feedbackDatas;
    }

    public static SptDataStreamId spt_dataStream_id4(PageInteractiveData uiData) {
        SptDataStreamId sptDataStreamId = new SptDataStreamId();
        ArrayList<SptDataStreamIdItem> list = new ArrayList();
        byte[] streamValue = uiData.getData();
        int streamValueLen = streamValue.length;
        if (streamValueLen > 0) {
            int streamNum = ByteHexHelper.byteToInt(streamValue[0]);
            sptDataStreamId.setStreamNum(streamNum);
            if (streamValueLen > 4) {
                int i;
                byte[] buffer = new byte[(streamValueLen - 1)];
                int index = 0;
                for (i = 1; i < streamValueLen; i++) {
                    buffer[index] = streamValue[i];
                    index++;
                }
                String[] streamData = splitString2(ByteHexHelper.bytesToHexString(buffer), streamNum).split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                for (i = 0; i < streamData.length; i += 2) {
                    SptDataStreamIdItem sptDataStreamIdItem = new SptDataStreamIdItem();
                    sptDataStreamIdItem.setStreamTextId(streamData[i]);
                    if (streamData[i + 1].equalsIgnoreCase("---")) {
                        sptDataStreamIdItem.setStreamStr(XmlPullParser.NO_NAMESPACE);
                    } else {
                        sptDataStreamIdItem.setStreamStr(ByteHexHelper.hexStringToWord(streamData[i + 1]));
                    }
                    list.add(sptDataStreamIdItem);
                }
                sptDataStreamId.setList(list);
            }
        }
        return sptDataStreamId;
    }

    public static ArrayList<SptStreamSelectIdItem> spt_stream_select_id_ex17(PageInteractiveData uiData) {
        ArrayList<SptStreamSelectIdItem> list = new ArrayList();
        int dataLen = uiData.getData().length;
        if (dataLen > 4) {
            int site = ByteHexHelper.intPackLength(new byte[]{data[0], data[1]});
            int itemNum = ByteHexHelper.intPackLength(new byte[]{data[2], data[3]});
            if (dataLen >= (itemNum + 1) * 4) {
                SptStreamSelectIdItem item = new SptStreamSelectIdItem();
                item.setSite(site);
                list.add(item);
                for (int i = 4; i < (itemNum + 1) * 4; i += 4) {
                    SptStreamSelectIdItem selectIdItem = new SptStreamSelectIdItem();
                    selectIdItem.setStreamSelectId(ByteHexHelper.bytesToHexString(new byte[]{data[i], data[i + 1], data[i + 2], data[i + 3]}));
                    list.add(selectIdItem);
                }
            }
        }
        return list;
    }

    public static ArrayList<SptExDataStreamIdItem> spt_ex_datastream_id18(PageInteractiveData uiData) {
        ArrayList<SptExDataStreamIdItem> list = new ArrayList();
        byte[] streamValue = uiData.getData();
        int streamValueLen = streamValue.length;
        if (streamValueLen > 2) {
            int i;
            int itemNum = ByteHexHelper.intPackLength(new byte[]{streamValue[0], streamValue[1]});
            byte[] buffer = new byte[(streamValueLen - 2)];
            int index = 0;
            for (i = 2; i < streamValueLen; i++) {
                buffer[index] = streamValue[i];
                index++;
            }
            String[] streamData = splitString2(ByteHexHelper.bytesToHexString(buffer), itemNum).split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            for (i = 0; i < streamData.length / 2; i++) {
                SptExDataStreamIdItem item = new SptExDataStreamIdItem();
                item.setStreamTextId(streamData[i * 2]);
                if (streamData[(i * 2) + 1].equalsIgnoreCase("---")) {
                    item.setStreamStr(XmlPullParser.NO_NAMESPACE);
                } else {
                    item.setStreamStr(ByteHexHelper.hexStringToWord(streamData[(i * 2) + 1]));
                }
                list.add(item);
            }
        }
        return list;
    }

    public static ArrayList<SptVwDataStreamIdItem> spt_vw_datastream_id19(PageInteractiveData uiData) {
        ArrayList<SptVwDataStreamIdItem> list = new ArrayList();
        byte[] streamValue = uiData.getData();
        int streamValueLen = streamValue.length;
        if (streamValueLen > 2) {
            int i;
            int itemNum = ByteHexHelper.intPackLength(new byte[]{streamValue[0], streamValue[1]});
            byte[] buffer = new byte[(streamValueLen - 2)];
            int index = 0;
            for (i = 2; i < streamValueLen; i++) {
                buffer[index] = streamValue[i];
                index++;
            }
            String[] streamData = splitString3(ByteHexHelper.bytesToHexString(buffer), itemNum).split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            for (i = 0; i < streamData.length / 3; i++) {
                SptVwDataStreamIdItem item = new SptVwDataStreamIdItem();
                item.setStreamTextId(streamData[i * 3]);
                item.setStreamUnitId(streamData[(i * 3) + 1]);
                if (streamData[(i * 3) + 2].equalsIgnoreCase("---")) {
                    item.setStreamStr(XmlPullParser.NO_NAMESPACE);
                } else {
                    item.setStreamStr(ByteHexHelper.hexStringToWord(streamData[(i * 3) + 2]));
                }
                list.add(item);
            }
        }
        return list;
    }

    public static SptInputStringEx spt_inputString_ex16(PageInteractiveData uiData) {
        SptInputStringEx inputString = new SptInputStringEx();
        byte[] data = uiData.getData();
        if (data.length > 0) {
            String[] datas = separateString(ByteHexHelper.bytesToHexString(data));
            for (int i = 0; i < datas.length; i += 3) {
                if (datas[i].equalsIgnoreCase("---")) {
                    inputString.setDialogTitle(XmlPullParser.NO_NAMESPACE);
                } else {
                    inputString.setDialogTitle(ByteHexHelper.hexStringToWord(datas[i]));
                }
                if (datas[i + 1].equalsIgnoreCase("---")) {
                    inputString.setDialogContent(XmlPullParser.NO_NAMESPACE);
                } else {
                    inputString.setDialogContent(ByteHexHelper.hexStringToWord(datas[i + 1]));
                }
                if (datas[i + 2].equalsIgnoreCase("---")) {
                    inputString.setInputHint(XmlPullParser.NO_NAMESPACE);
                } else {
                    inputString.setInputHint(ByteHexHelper.hexStringToWord(datas[i + 2]));
                }
            }
        }
        return inputString;
    }

    public static SptInputNumric spt_input_numric15(PageInteractiveData uiData) {
        SptInputNumric inputNumric = new SptInputNumric();
        byte[] data = uiData.getData();
        int dataLen = data.length;
        if (dataLen > 0) {
            int i;
            inputNumric.setDigit(ByteHexHelper.byteToInt(data[dataLen - 1]));
            byte[] buffer = new byte[(dataLen - 1)];
            for (i = 0; i < dataLen - 1; i++) {
                buffer[i] = data[i];
            }
            String[] datas = separateString(ByteHexHelper.bytesToHexString(buffer));
            for (i = 0; i < datas.length; i += 3) {
                if (datas[i].equalsIgnoreCase("---")) {
                    inputNumric.setDialogTitle(XmlPullParser.NO_NAMESPACE);
                } else {
                    inputNumric.setDialogTitle(ByteHexHelper.hexStringToWord(datas[i]));
                }
                if (datas[i + 1].equalsIgnoreCase("---")) {
                    inputNumric.setDialogContent(XmlPullParser.NO_NAMESPACE);
                } else {
                    inputNumric.setDialogContent(ByteHexHelper.hexStringToWord(datas[i + 1]));
                }
                if (datas[i + 2].equalsIgnoreCase("---")) {
                    inputNumric.setInputHint(XmlPullParser.NO_NAMESPACE);
                } else {
                    inputNumric.setInputHint(ByteHexHelper.hexStringToWord(datas[i + 2]));
                }
            }
        }
        return inputNumric;
    }

    public static Spt_Nobuttonbox_Text spt_inputBox_text7(PageInteractiveData uiData) {
        Spt_Nobuttonbox_Text inputBox = new Spt_Nobuttonbox_Text();
        String[] datas = separateString(ByteHexHelper.bytesToHexString(uiData.getData()));
        if (datas[0].equalsIgnoreCase("---")) {
            inputBox.setTitle(XmlPullParser.NO_NAMESPACE);
        } else {
            inputBox.setTitle(ByteHexHelper.hexStringToWord(datas[0]));
        }
        if (datas[1].equalsIgnoreCase("---")) {
            inputBox.setContent(XmlPullParser.NO_NAMESPACE);
        } else {
            inputBox.setContent(ByteHexHelper.hexStringToWord(datas[1]));
        }
        return inputBox;
    }

    public static Spt_Nobuttonbox_Text spt_inputString8(PageInteractiveData uiData) {
        Spt_Nobuttonbox_Text inputBox = new Spt_Nobuttonbox_Text();
        String[] datas = separateString(ByteHexHelper.bytesToHexString(uiData.getData()));
        if (datas[0].equalsIgnoreCase("---")) {
            inputBox.setTitle(XmlPullParser.NO_NAMESPACE);
        } else {
            inputBox.setTitle(ByteHexHelper.hexStringToWord(datas[0]));
        }
        if (datas[1].equalsIgnoreCase("---")) {
            inputBox.setContent(XmlPullParser.NO_NAMESPACE);
        } else {
            inputBox.setContent(ByteHexHelper.hexStringToWord(datas[1]));
        }
        return inputBox;
    }

    public static String spt_aging_window30(PageInteractiveData uiData) {
        byte[] aging;
        String agingContent = XmlPullParser.NO_NAMESPACE;
        byte[] nobuttonboxData = uiData.getData();
        if (nobuttonboxData.length > 0) {
            aging = new byte[(nobuttonboxData.length - 1)];
            for (int j = 0; j < aging.length; j++) {
                aging[j] = nobuttonboxData[j];
            }
        } else {
            aging = new byte[0];
        }
        if (nobuttonboxData == null) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return ByteHexHelper.hexStringToWord(ByteHexHelper.bytesToHexString(aging)).trim();
    }

    public static ArrayList<MenuData> spt_file_menu(PageInteractiveData uiData) {
        ArrayList<MenuData> menuList = new ArrayList();
        byte[] menuData = uiData.getData();
        int menuLen = menuData.length;
        if (menuLen > 1) {
            if (ByteHexHelper.byteToHexString(menuData[0]).equalsIgnoreCase("FF")) {
            }
            if (menuLen > 2) {
                try {
                    int site = Integer.valueOf(ByteHexHelper.intPackLength(ByteHexHelper.byteToHexString(menuData[1]))).intValue();
                } catch (NumberFormatException e) {
                }
                if (menuLen > 3) {
                    int i;
                    int menuNums = ByteHexHelper.byteToInt(menuData[2]);
                    byte[] buffer = new byte[(menuLen - 3)];
                    int index = 0;
                    for (i = 3; i < menuLen - 3; i++) {
                        buffer[index] = menuData[i];
                        index++;
                    }
                    String[] datas = separateString(ByteHexHelper.bytesToHexString(buffer));
                    for (i = 0; i < menuNums; i++) {
                        MenuData menu = new MenuData();
                        menu.setMenuContent(ByteHexHelper.hexStringToWord(datas[i]));
                        menuList.add(menu);
                    }
                }
            }
        }
        return menuList;
    }

    public static ArrayList<SptExDataStreamIdItem34> spt_datastream_item_back_call34(PageInteractiveData uiData) {
        ArrayList<SptExDataStreamIdItem34> list = new ArrayList();
        byte[] streamValue = uiData.getData();
        int streamValueLen = streamValue.length;
        if (r0 > 2) {
            int i;
            byte[] dataStreamNumBytes = new byte[2];
            dataStreamNumBytes = getBuffer(streamValue, 0, 2);
            int dataStreamNum = ByteHexHelper.intPackLength(dataStreamNumBytes);
            byte[] dataStreamHandelNumBytes = new byte[4];
            dataStreamHandelNumBytes = getBuffer(streamValue, 6, 4);
            int dataStreamHandelNum = ByteHexHelper.intPackLength(dataStreamHandelNumBytes);
            byte[] handelMakeBufferLengthBytes = getBuffer(streamValue, (dataStreamNumBytes.length + 4) + dataStreamHandelNumBytes.length, new byte[((dataStreamHandelNum / 8) + (dataStreamHandelNum % 8 == 0 ? 0 : 1))].length);
            StringBuffer choiceItem = new StringBuffer();
            for (i = 1; i <= dataStreamHandelNum; i++) {
                choiceItem.append(Contact.RELATION_ASK);
            }
            for (i = 0; i < Constant.streamSelectHashMap_totle; i++) {
                int index = i + (Constant.streamSelectHashMap_totle * Constant.currentPage);
                if (index == Constant.streamIsSelectedList.size()) {
                    break;
                }
                choiceItem.setCharAt(((Integer) Constant.streamIsSelectedList.get(index)).intValue() - 1, '1');
            }
            byte[] byteChoice = ByteHexHelper.hexStringToBytes(ByteHexHelper.binaryString2hexString(choiceItem.toString()));
            SptExDataStreamIdItem34 item = new SptExDataStreamIdItem34();
            item.setChoseMaskBufferList(byteChoice);
            item.setDataStreamHandelItemCount(dataStreamHandelNum);
            item.setDataStreamItemCount(dataStreamNum);
            list.add(item);
        }
        return list;
    }

    public static ArrayList<SptExDataStreamIdItem> spt_datastream_item34(PageInteractiveData uiData) {
        ArrayList<SptExDataStreamIdItem> list = new ArrayList();
        byte[] streamValue = uiData.getData();
        int streamValueLen = streamValue.length;
        if (streamValueLen > 2) {
            byte[] dataStreamNumBytes = new byte[2];
            dataStreamNumBytes = getBuffer(streamValue, 0, 2);
            int i = (streamValue[4] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
            byte[] dataStreamTitleBytes = CToJava.searchId.getTextFromLibReturnByte(((((streamValue[2] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((streamValue[3] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (streamValue[5] & KEYRecord.PROTOCOL_ANY), 1);
            byte[] dataStreamHandelNumBytes = new byte[4];
            dataStreamHandelNumBytes = getBuffer(streamValue, 6, 4);
            int dataStreamHandelNum = ByteHexHelper.intPackLength(dataStreamHandelNumBytes);
            byte[] handelMakeBufferLengthBytes = getBuffer(streamValue, (dataStreamNumBytes.length + 4) + dataStreamHandelNumBytes.length, new byte[((dataStreamHandelNum / 8) + (dataStreamHandelNum % 8 == 0 ? 0 : 1))].length);
            int length = dataStreamNumBytes.length;
            int startPosition = ((r0 + 4) + dataStreamHandelNumBytes.length) + handelMakeBufferLengthBytes.length;
            String[] troubleCodeData = splitString2(ByteHexHelper.bytesToHexString(getBuffer(streamValue, startPosition, new byte[(streamValueLen - startPosition)].length)), dataStreamHandelNum).split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            byte[] spaceByte = new byte[1];
            int i2 = 0;
            while (true) {
                length = troubleCodeData.length;
                if (i2 >= r0) {
                    break;
                }
                SptExDataStreamIdItem item = new SptExDataStreamIdItem();
                byte[] tempLabelBytes = ByteHexHelper.hexStringToBytes(troubleCodeData[i2]);
                i = (tempLabelBytes[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                String tempTitle = ByteHexHelper.byteToWord(CToJava.searchId.getTextFromLibReturnByte(((((tempLabelBytes[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((tempLabelBytes[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (tempLabelBytes[3] & KEYRecord.PROTOCOL_ANY), 3));
                int position = tempTitle.indexOf(ByteHexHelper.byteToWord(spaceByte));
                item.setStreamTextIdContent(tempTitle.substring(0, position));
                String streamStr = ByteHexHelper.hexStringToWord(troubleCodeData[i2 + 1]);
                String streamState = tempTitle.substring(position + 1, tempTitle.length() - 1);
                if (TextUtils.isEmpty(streamState) && !TextUtils.isEmpty(streamStr)) {
                    if (streamStr.contains(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR)) {
                        String temp = streamStr.substring(0, streamStr.indexOf(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR));
                        if (DataStreamUtils.isNumeric(temp)) {
                            streamState = streamStr.substring(streamStr.indexOf(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR) + 1, streamStr.length());
                            streamStr = temp;
                        }
                    }
                }
                item.setStreamStr(streamStr);
                item.setStreamState(streamState);
                item.setStreamTopTitle(ByteHexHelper.byteToWord(dataStreamTitleBytes));
                list.add(item);
                i2 += 2;
            }
        }
        return list;
    }

    public static byte[] spt_datastreamitem_id35(PageInteractiveData uiData) {
        int i = 0;
        byte[] streamValue = uiData.getData();
        if (streamValue.length > 2) {
            int i2;
            int dataStreamHandelNum = ByteHexHelper.intPackLength(getBuffer(streamValue, 0, new byte[4].length));
            int i3 = dataStreamHandelNum / 8;
            if (dataStreamHandelNum % 8 != 0) {
                i = 1;
            }
            int handelMakeBufferLength = i3 + i;
            byte[] handelMakeBufferLengthBytes = new byte[handelMakeBufferLength];
            handelMakeBufferLengthBytes = getBuffer(streamValue, 4, handelMakeBufferLength);
            StringBuffer choiceItem = new StringBuffer();
            for (i2 = 1; i2 <= dataStreamHandelNum; i2++) {
                choiceItem.append(Contact.RELATION_ASK);
            }
            for (i2 = 0; i2 < Constant.streamSelectHashMap_totle; i2++) {
                int index = i2 + (Constant.streamSelectHashMap_totle * Constant.currentPage);
                if (index == Constant.streamIsSelectedList.size()) {
                    break;
                }
                choiceItem.setCharAt(((Integer) Constant.streamIsSelectedList.get(index)).intValue() - 1, '1');
            }
            Constant.spt_data_stream = ByteHexHelper.hexStringToBytes(ByteHexHelper.binaryString2hexString(choiceItem.toString()));
        }
        return Constant.spt_data_stream;
    }

    public static ArrayList<SpecialFunction> spt_special_function_id36(PageInteractiveData uiData) {
        ArrayList<SpecialFunction> list = new ArrayList();
        byte[] streamValue = uiData.getData();
        int streamValueLen = streamValue.length;
        ArrayList<String> titleList = new ArrayList();
        ArrayList<String> contentList = new ArrayList();
        String currentString = XmlPullParser.NO_NAMESPACE;
        if (streamValueLen > 2) {
            int titleHexId;
            byte[] columsNumBytes = new byte[]{streamValue[1]};
            int buttonNum = ByteHexHelper.intPackLength(new byte[]{streamValue[0]});
            int columsNum = ByteHexHelper.intPackLength(columsNumBytes);
            int i = streamValue[2] & KEYRecord.PROTOCOL_ANY;
            int i2 = streamValue[3] & KEYRecord.PROTOCOL_ANY;
            i2 = (streamValue[4] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
            int titleId = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (streamValue[5] & KEYRecord.PROTOCOL_ANY);
            String titleString = ByteHexHelper.byteToWord(CToJava.searchId.getTextFromLibReturnByte(titleId, 1));
            byte[] columsTitleBuffer = getBuffer(streamValue, 6, new byte[(columsNum * 6)].length);
            byte[] buttonBuffer = new byte[(buttonNum * 4)];
            buttonBuffer = getBuffer(streamValue, columsTitleBuffer.length + 6, buttonBuffer.length);
            byte[] speciaItemBuffer = new byte[2];
            speciaItemBuffer = getBuffer(streamValue, (buttonBuffer.length + columsTitleBuffer.length) + 6, 2);
            int rowNum = Integer.valueOf(Integer.toString(Integer.parseInt(ByteHexHelper.bytesToHexString(speciaItemBuffer), 16))).intValue();
            i = streamValue.length;
            i2 = speciaItemBuffer.length;
            int length = buttonBuffer.length;
            byte[] columsContentBuffer = new byte[(r0 - (((r0 + r0) + columsTitleBuffer.length) + 6))];
            i = speciaItemBuffer.length;
            i2 = buttonBuffer.length;
            columsContentBuffer = getBuffer(streamValue, ((r0 + r0) + columsTitleBuffer.length) + 6, columsContentBuffer.length);
            SpecialFunction item = new SpecialFunction();
            int i3 = 0;
            while (true) {
                i = columsTitleBuffer.length;
                if (i3 >= r0) {
                    break;
                }
                i = columsTitleBuffer[i3] & KEYRecord.PROTOCOL_ANY;
                i2 = columsTitleBuffer[i3 + 1] & KEYRecord.PROTOCOL_ANY;
                i2 = (columsTitleBuffer[i3 + 2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                titleHexId = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (columsTitleBuffer[i3 + 3] & KEYRecord.PROTOCOL_ANY);
                titleList.add(ByteHexHelper.byteToWord(CToJava.searchId.getTextFromLibReturnByte(titleHexId, 1)));
                i3 += 6;
            }
            currentString = ByteHexHelper.bytesToHexString(columsContentBuffer);
            for (int r = 0; r < rowNum; r++) {
                for (i3 = 1; i3 <= columsNum; i3++) {
                    int m = columsTitleBuffer[(i3 * 4) + ((i3 - 1) * 2)];
                    if (m == 1) {
                        if (currentString == null || currentString.length() <= 0) {
                            contentList.add(XmlPullParser.NO_NAMESPACE);
                        } else {
                            byte[] tempLabelBytes = ByteHexHelper.hexStringToBytes(currentString.substring(0, 8));
                            i = tempLabelBytes[0] & KEYRecord.PROTOCOL_ANY;
                            i2 = tempLabelBytes[1] & KEYRecord.PROTOCOL_ANY;
                            i2 = (tempLabelBytes[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                            titleHexId = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (tempLabelBytes[3] & KEYRecord.PROTOCOL_ANY);
                            contentList.add(ByteHexHelper.byteToWord(CToJava.searchId.getTextFromLibReturnByte(titleHexId, 1)));
                            currentString = currentString.substring(8);
                        }
                    } else if (m == 2) {
                        if (currentString == null || currentString.length() <= 0) {
                            contentList.add(XmlPullParser.NO_NAMESPACE);
                        } else {
                            String[] strs = currentString.split("(?<=00)");
                            String tempStr = strs[0];
                            String str2 = XmlPullParser.NO_NAMESPACE;
                            String splitStr = XmlPullParser.NO_NAMESPACE;
                            if (tempStr.length() % 2 != 0) {
                                tempStr = strs[0] + Contact.RELATION_ASK;
                            }
                            str2 = currentString.substring(tempStr.length());
                            if (tempStr.contains("000")) {
                                splitStr = tempStr.substring(0, tempStr.lastIndexOf("00") - 1);
                            } else {
                                splitStr = tempStr.replace("00", XmlPullParser.NO_NAMESPACE);
                            }
                            if (splitStr.equals(XmlPullParser.NO_NAMESPACE)) {
                                contentList.add(XmlPullParser.NO_NAMESPACE);
                            } else {
                                if (splitStr.length() % 2 != 0) {
                                    splitStr = new StringBuilder(String.valueOf(splitStr)).append(Contact.RELATION_ASK).toString();
                                }
                                contentList.add(ByteHexHelper.hexStringToWord(splitStr));
                            }
                            if (str2.length() % 2 == 0) {
                                currentString = str2;
                            } else {
                                currentString = str2.substring(1);
                            }
                        }
                    }
                }
            }
            ArrayList<String> buttonList = new ArrayList();
            i3 = 0;
            while (true) {
                i = buttonBuffer.length;
                if (i3 >= r0) {
                    break;
                }
                i = buttonBuffer[i3] & KEYRecord.PROTOCOL_ANY;
                i2 = buttonBuffer[i3 + 1] & KEYRecord.PROTOCOL_ANY;
                i2 = (buttonBuffer[i3 + 2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                int buttonTextHexId = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (buttonBuffer[i3 + 3] & KEYRecord.PROTOCOL_ANY);
                buttonList.add(ByteHexHelper.byteToWord(CToJava.searchId.getTextFromLibReturnByte(buttonTextHexId, 1)));
                i3 += 4;
            }
            if (titleString != null) {
                item.setCustomTitle(titleString);
            }
            item.setButtonCount(buttonNum);
            item.setColumsCount(columsNum);
            if (titleList != null && titleList.size() > 0) {
                item.setColumsTitleList(titleList);
            }
            if (contentList != null && contentList.size() > 0) {
                item.setColumsContentList(contentList);
            }
            if (buttonList != null && buttonList.size() > 0) {
                item.setButtonList(buttonList);
            }
            list.add(item);
        }
        return list;
    }

    public static ArrayList<SpecialFunction> spt_special_function_id36_old(PageInteractiveData uiData) {
        ArrayList<SpecialFunction> list = new ArrayList();
        byte[] streamValue = uiData.getData();
        int streamValueLen = streamValue.length;
        if (r0 > 2) {
            int titleHexId;
            byte[] columsNumBytes = new byte[]{streamValue[1]};
            int buttonNum = ByteHexHelper.intPackLength(new byte[]{streamValue[0]});
            int columsNum = ByteHexHelper.intPackLength(columsNumBytes);
            int i = streamValue[2] & KEYRecord.PROTOCOL_ANY;
            int i2 = streamValue[3] & KEYRecord.PROTOCOL_ANY;
            i2 = (streamValue[4] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
            int titleId = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (streamValue[5] & KEYRecord.PROTOCOL_ANY);
            String titleString = ByteHexHelper.byteToWord(CToJava.searchId.getTextFromLibReturnByte(titleId, 1));
            byte[] columsTitleBuffer = getBuffer(streamValue, 6, new byte[(columsNum * 6)].length);
            byte[] buttonBuffer = new byte[(buttonNum * 4)];
            buttonBuffer = getBuffer(streamValue, columsTitleBuffer.length + 6, buttonBuffer.length);
            byte[] speciaItemBuffer = new byte[2];
            speciaItemBuffer = getBuffer(streamValue, (buttonBuffer.length + columsTitleBuffer.length) + 6, 2);
            i = streamValue.length;
            i2 = speciaItemBuffer.length;
            int length = buttonBuffer.length;
            byte[] columsContentBuffer = new byte[(r0 - (((r0 + r0) + columsTitleBuffer.length) + 6))];
            i = speciaItemBuffer.length;
            i2 = buttonBuffer.length;
            columsContentBuffer = getBuffer(streamValue, ((r0 + r0) + columsTitleBuffer.length) + 6, columsContentBuffer.length);
            SpecialFunction item = new SpecialFunction();
            ArrayList<String> titleList = new ArrayList();
            int i3 = 0;
            while (true) {
                i = columsTitleBuffer.length;
                if (i3 >= r0) {
                    break;
                }
                i = columsTitleBuffer[i3] & KEYRecord.PROTOCOL_ANY;
                i2 = columsTitleBuffer[i3 + 1] & KEYRecord.PROTOCOL_ANY;
                i2 = (columsTitleBuffer[i3 + 2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                titleHexId = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (columsTitleBuffer[i3 + 3] & KEYRecord.PROTOCOL_ANY);
                titleList.add(ByteHexHelper.byteToWord(CToJava.searchId.getTextFromLibReturnByte(titleHexId, 1)));
                i3 += 6;
            }
            ArrayList<String> contentList = new ArrayList();
            if (!ByteHexHelper.bytesToHexString(columsContentBuffer).equals(XmlPullParser.NO_NAMESPACE)) {
                String[] troubleCodeData = splitString2(ByteHexHelper.bytesToHexString(columsContentBuffer), 6).split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                i3 = 0;
                while (true) {
                    i = troubleCodeData.length;
                    if (i3 >= r0) {
                        break;
                    }
                    byte[] tempLabelBytes = ByteHexHelper.hexStringToBytes(troubleCodeData[i3]);
                    i = tempLabelBytes[0] & KEYRecord.PROTOCOL_ANY;
                    i2 = tempLabelBytes[1] & KEYRecord.PROTOCOL_ANY;
                    i2 = (tempLabelBytes[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                    titleHexId = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (tempLabelBytes[3] & KEYRecord.PROTOCOL_ANY);
                    contentList.add(ByteHexHelper.byteToWord(CToJava.searchId.getTextFromLibReturnByte(titleHexId, 1)));
                    contentList.add(ByteHexHelper.hexStringToWord(troubleCodeData[i3 + 1]));
                    i3 += 2;
                }
            }
            ArrayList<String> buttonList = new ArrayList();
            i3 = 0;
            while (true) {
                i = buttonBuffer.length;
                if (i3 >= r0) {
                    break;
                }
                i = buttonBuffer[i3] & KEYRecord.PROTOCOL_ANY;
                i2 = buttonBuffer[i3 + 1] & KEYRecord.PROTOCOL_ANY;
                i2 = (buttonBuffer[i3 + 2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                int buttonTextHexId = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (buttonBuffer[i3 + 3] & KEYRecord.PROTOCOL_ANY);
                buttonList.add(ByteHexHelper.byteToWord(CToJava.searchId.getTextFromLibReturnByte(buttonTextHexId, 1)));
                i3 += 4;
            }
            if (titleString != null) {
                item.setCustomTitle(titleString);
            }
            item.setButtonCount(buttonNum);
            item.setColumsCount(columsNum);
            if (titleList != null) {
                item.setColumsTitleList(titleList);
            }
            if (contentList != null) {
                item.setColumsContentList(contentList);
            }
            if (buttonList != null) {
                item.setButtonList(buttonList);
            }
            list.add(item);
        }
        return list;
    }

    public static ArrayList<SptTroubleTest> spe_trouble_bode_loopmode(PageInteractiveData uiData, int type) {
        byte[] streamValue = uiData.getData();
        int streamValueLen = streamValue.length;
        ArrayList<SptTroubleTest> list = new ArrayList();
        if (streamValueLen > 2) {
            String[] datas;
            SptTroubleTest troublecodewithGAG = new SptTroubleTest();
            int itemNum = ByteHexHelper.intPackLength(new byte[]{streamValue[0], streamValue[1]});
            byte[] tempStreamBuffer = new byte[(streamValue.length - 2)];
            String dataStr = ByteHexHelper.bytesToHexString(getBuffer(streamValue, 2, streamValue.length - 2));
            String dataBaseName = XmlPullParser.NO_NAMESPACE;
            if (type == 37 || type == 38) {
                datas = spt_trouble_str_split(dataStr);
            } else {
                datas = separateString(dataStr);
            }
            dataBaseName = ByteHexHelper.hexStringToWord(datas[0]);
            String path = Constant.OBD2_EN_GGP_PATH;
            int startIndex = 0;
            if (type == 37 || type == 38 || type == 41 || type == 42 || type == 43) {
                startIndex = 1;
            }
            if (path != null && path.length() > 0) {
                String[] name = path.split("_");
                String lan = name[name.length - 1].substring(0, 2);
                String stateGAGName = "TCODE_" + lan + ".GAG";
                byte[] spaceByte = new byte[1];
                int i = startIndex;
                while (true) {
                    int length = datas.length;
                    if (i >= r0) {
                        break;
                    }
                    troublecodewithGAG = new SptTroubleTest();
                    troublecodewithGAG.setTroubleGAGname(dataBaseName);
                    if (datas[i].length() > 7) {
                        String strs = datas[i].substring(0, 8);
                        String state = datas[i].substring(8, 16);
                        String contentString = datas[i].substring(16);
                        byte[] titleStr = ByteHexHelper.hexStringToBytes(strs);
                        byte[] stateStr = ByteHexHelper.hexStringToBytes(state);
                        byte[] troubleCodeIdBybytes = null;
                        byte[] troubleStateIdBybytes = null;
                        length = titleStr[0] & KEYRecord.PROTOCOL_ANY;
                        int i2 = titleStr[1] & KEYRecord.PROTOCOL_ANY;
                        i2 = (titleStr[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                        int title_id = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (titleStr[3] & KEYRecord.PROTOCOL_ANY);
                        if (type == 37 || type == 38) {
                            length = stateStr[0] & KEYRecord.PROTOCOL_ANY;
                            i2 = stateStr[1] & KEYRecord.PROTOCOL_ANY;
                            i2 = (stateStr[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                            int state_id = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (stateStr[3] & KEYRecord.PROTOCOL_ANY);
                            troubleStateIdBybytes = CToJava.searchId.getTextFromLibByGAGReturnByte(state_id, stateGAGName);
                        }
                        if (type == 37 || type == 38 || type == 41 || type == 42 || type == 43) {
                            troubleCodeIdBybytes = CToJava.searchId.getTextFromLibByGAGReturnByte(title_id, troublecodewithGAG.getTroubleGAGname());
                        } else if (type == 39 || type == 40) {
                            troubleCodeIdBybytes = CToJava.searchId.getTextFromLibReturnByte(title_id, 6);
                        }
                        if (troubleCodeIdBybytes == null || troubleCodeIdBybytes.length == 0) {
                            troublecodewithGAG.setTroubleCodeContent(contentString);
                            troublecodewithGAG.setTroubleDescribeContent(MainActivity.contexts.getString(C0136R.string.research));
                            if (type == 37 || type == 38) {
                                troublecodewithGAG.setTroubleStateContent(ByteHexHelper.byteToWord(troubleStateIdBybytes));
                            } else {
                                troublecodewithGAG.setTroubleStateContent(ByteHexHelper.hexStringToWord(state));
                            }
                        } else {
                            String titleAndContent = ByteHexHelper.byteToWord(troubleCodeIdBybytes);
                            if (type == 39 || type == 40 || type == 37 || type == 38 || type == 41 || type == 42 || type == 43) {
                                String title = titleAndContent.substring(0, titleAndContent.indexOf(ByteHexHelper.byteToWord(spaceByte)));
                                String content = titleAndContent.substring(titleAndContent.indexOf(ByteHexHelper.byteToWord(spaceByte)) + 1);
                                troublecodewithGAG.setTroubleCodeContent(title);
                                troublecodewithGAG.setTroubleDescribeContent(content);
                            } else {
                                troublecodewithGAG.setTroubleCodeContent(titleAndContent);
                            }
                            if (type == 37 || type == 38) {
                                troublecodewithGAG.setTroubleStateContent(ByteHexHelper.byteToWord(troubleStateIdBybytes));
                            } else {
                                troublecodewithGAG.setTroubleStateContent(ByteHexHelper.hexStringToWord(state));
                            }
                        }
                        list.add(troublecodewithGAG);
                    }
                    i++;
                }
            }
        }
        return list;
    }

    public static Spt_Combination_Menu spt_combination_menu46(PageInteractiveData uiData) {
        byte[] aging;
        Spt_Combination_Menu item = new Spt_Combination_Menu();
        byte[] nobuttonboxData = uiData.getData();
        byte[] totle = new byte[]{nobuttonboxData[1]};
        byte[] btnState = new byte[]{nobuttonboxData[2]};
        item.setFirstNum(ByteHexHelper.intPackLength(new byte[]{nobuttonboxData[0]}));
        item.setIntTotle(ByteHexHelper.intPackLength(totle));
        item.setBtnState(ByteHexHelper.intPackLength(btnState));
        String strBlack = ByteHexHelper.byteToWord(new byte[1]);
        if (nobuttonboxData.length > 3) {
            aging = new byte[(nobuttonboxData.length - 3)];
            for (int j = 0; j < aging.length; j++) {
                aging[j] = nobuttonboxData[j + 3];
            }
        } else {
            aging = new byte[0];
        }
        String[] strs = ByteHexHelper.byteToWord(aging).split(strBlack);
        ArrayList<String> data = new ArrayList();
        for (Object add : strs) {
            data.add(add);
        }
        item.setData(data);
        return item;
    }

    public static Spt_Progressbar_Box Spt_Progressbar_Box45(PageInteractiveData uiData) {
        Spt_Progressbar_Box progressBarBox = new Spt_Progressbar_Box();
        byte[] streamValue = uiData.getData();
        int streamValueLen = streamValue.length;
        if (streamValueLen > 2) {
            progressBarBox.setProgressbarLen(ByteHexHelper.intPackLength(new byte[]{streamValue[0], streamValue[1]}));
            byte[] progressBarBoxData = new byte[(streamValue.length - 2)];
            int index = 0;
            for (int i = 2; i < streamValueLen; i++) {
                progressBarBoxData[index] = streamValue[i];
                index++;
            }
            String[] datas = separateString(ByteHexHelper.bytesToHexString(progressBarBoxData));
            if (datas[0].equalsIgnoreCase("---")) {
                progressBarBox.setTitle(XmlPullParser.NO_NAMESPACE);
            } else {
                progressBarBox.setTitle(ByteHexHelper.hexStringToWord(datas[0]));
            }
            if (datas[1].equalsIgnoreCase("---")) {
                progressBarBox.setContent(XmlPullParser.NO_NAMESPACE);
            } else {
                progressBarBox.setContent(ByteHexHelper.hexStringToWord(datas[1]));
            }
        }
        return progressBarBox;
    }

    public static String SPT_SET_VIN_47(PageInteractiveData uiData) {
        byte[] VinValue = uiData.getData();
        int VinValueLen = VinValue.length;
        if (VinValueLen <= 1) {
            return null;
        }
        int mVinLen = ByteHexHelper.intPackLength(new byte[]{VinValue[0]});
        byte[] vinData = new byte[(VinValue.length - 1)];
        int index = 0;
        for (int i = 1; i < VinValueLen; i++) {
            vinData[index] = VinValue[i];
            index++;
        }
        return ByteHexHelper.byteToWord(vinData);
    }

    public static byte[] getBuffer(byte[] streamValue, int startIndex, int bufferLength) {
        byte[] tempBuffer = new byte[bufferLength];
        for (int i = startIndex; i < tempBuffer.length + startIndex; i++) {
            tempBuffer[i - startIndex] = streamValue[i];
        }
        return tempBuffer;
    }

    private static String[] spt_trouble_str_split(String hexString) {
        if (hexString == null || hexString.length() <= 0) {
            return new String[0];
        }
        int i;
        StringBuffer strB = new StringBuffer();
        int hexStrLen = hexString.length();
        int flag = 0;
        for (i = 0; i < hexStrLen / 2; i++) {
            String item = hexString.substring(i * 2, (i * 2) + 2);
            if (item.equalsIgnoreCase("00")) {
                flag = (i + 1) * 2;
                strB.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                break;
            }
            strB.append(item);
        }
        String tempHexStr = hexString.substring(flag);
        i = 0;
        while (i < tempHexStr.length()) {
            String labelItem = tempHexStr.substring(i, i + 8);
            String stateItem = tempHexStr.substring(i + 8, i + 16);
            String tempHexStr2 = tempHexStr.substring(i + 16);
            String contentItem = XmlPullParser.NO_NAMESPACE;
            for (int j = 0; j < tempHexStr2.length() / 2; j++) {
                if (tempHexStr2.substring(j * 2, (j * 2) + 2).equalsIgnoreCase("00")) {
                    flag = (j + 1) * 2;
                    contentItem = ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(tempHexStr2.substring(0, flag - 2)));
                    break;
                }
            }
            i = (flag + i) + 16;
            strB.append(labelItem);
            strB.append(stateItem);
            strB.append(contentItem);
            strB.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        }
        return strB.toString().split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
    }

    public static ArrayList<MenuData> spt_menu_help_id49(PageInteractiveData uiData) {
        ArrayList<MenuData> menuList = new ArrayList();
        byte[] menuData = uiData.getData();
        int menuLen = menuData.length;
        int site = 0;
        if (menuLen > 1) {
            if (ByteHexHelper.byteToHexString(menuData[0]).equalsIgnoreCase("FF")) {
            }
            if (menuLen > 2) {
                try {
                    site = Integer.valueOf(ByteHexHelper.intPackLength(ByteHexHelper.byteToHexString(menuData[1]))).intValue();
                } catch (NumberFormatException e) {
                }
                if (menuLen > 3) {
                    int menuNums = ByteHexHelper.byteToInt(menuData[2]);
                    int length = menuData.length;
                    if (r0 == (menuNums * 4) + 3) {
                        String dataStr = ByteHexHelper.bytesToHexString(menuData).substring(6);
                        if (dataStr.length() == menuNums * 8) {
                            String MenuTitleId = dataStr.substring(0, 8);
                            byte[] databufTitle = ByteHexHelper.hexStringToBytes(MenuTitleId);
                            length = databufTitle[0] & KEYRecord.PROTOCOL_ANY;
                            int i = databufTitle[1] & KEYRecord.PROTOCOL_ANY;
                            i = (databufTitle[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                            int v_idTitle = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databufTitle[3] & KEYRecord.PROTOCOL_ANY);
                            String menu_helpId = dataStr.substring(8, 16);
                            byte[] databufHelp = ByteHexHelper.hexStringToBytes(menu_helpId);
                            length = databufHelp[0] & KEYRecord.PROTOCOL_ANY;
                            i = databufHelp[1] & KEYRecord.PROTOCOL_ANY;
                            i = (databufHelp[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                            int v_idHelp = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databufHelp[3] & KEYRecord.PROTOCOL_ANY);
                            byte[] MenuTitleContentbytes = CToJava.searchId.getTextFromLibReturnByte(v_idTitle, 1);
                            byte[] MenuHelpContentbytes = CToJava.searchId.getTextFromLibReturnByte(v_idHelp, 15);
                            String menuTitleContent = XmlPullParser.NO_NAMESPACE;
                            String menuHelpContent = XmlPullParser.NO_NAMESPACE;
                            if (MenuTitleContentbytes.length > 0) {
                                menuTitleContent = ByteHexHelper.byteToWord(MenuTitleContentbytes);
                            }
                            if (MenuHelpContentbytes.length > 0) {
                                menuHelpContent = ByteHexHelper.byteToWord(MenuHelpContentbytes);
                            }
                            for (int i2 = 0; i2 < menuNums; i2++) {
                                byte[] menuContentbytes;
                                MenuData item_new = new MenuData();
                                item_new.setSite(site);
                                item_new.setMenuId(dataStr.substring(i2 * 8, (i2 * 8) + 8));
                                item_new.setMenuTitleId(MenuTitleId);
                                item_new.setMenuHelpId(menu_helpId);
                                item_new.setMenuTitleContent(menuTitleContent);
                                item_new.setMenuHelpContent(menuHelpContent);
                                byte[] databuf = ByteHexHelper.hexStringToBytes(dataStr.substring(i2 * 8, (i2 * 8) + 8));
                                length = databuf[0] & KEYRecord.PROTOCOL_ANY;
                                i = databuf[1] & KEYRecord.PROTOCOL_ANY;
                                i = (databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                                int v_id = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (databuf[3] & KEYRecord.PROTOCOL_ANY);
                                byte[] MenuContentbytes = CToJava.searchId.getTextFromLibReturnByte(v_id, 1);
                                if (MenuContentbytes.length > 0) {
                                    menuContentbytes = new byte[(MenuContentbytes.length - 1)];
                                    int j = 0;
                                    while (true) {
                                        length = menuContentbytes.length;
                                        if (j >= r0) {
                                            break;
                                        }
                                        menuContentbytes[j] = MenuContentbytes[j];
                                        j++;
                                    }
                                } else {
                                    menuContentbytes = new byte[0];
                                }
                                String str = XmlPullParser.NO_NAMESPACE;
                                str = ByteHexHelper.byteToWord(menuContentbytes);
                                if (str.length() > 0) {
                                    item_new.setMenuContent(str);
                                } else {
                                    item_new.setMenuContent(item_new.getMenuId() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MainActivity.contexts.getResources().getString(C0136R.string.ReferManual));
                                }
                                menuList.add(item_new);
                            }
                        }
                    }
                }
            }
        }
        return menuList;
    }

    public static ArrayList<MenuData> spt_file_menu_help50(PageInteractiveData uiData) {
        ArrayList<MenuData> menuList = new ArrayList();
        byte[] menuData = uiData.getData();
        int menuLen = menuData.length;
        int site = 0;
        if (menuLen > 1) {
            if (ByteHexHelper.byteToHexString(menuData[0]).equalsIgnoreCase("FF")) {
            }
            if (menuLen > 2) {
                try {
                    site = Integer.valueOf(ByteHexHelper.intPackLength(ByteHexHelper.byteToHexString(menuData[1]))).intValue();
                } catch (NumberFormatException e) {
                }
                if (menuLen > 3) {
                    MenuData item = new MenuData();
                    int menuNums = ByteHexHelper.byteToInt(menuData[2]);
                    byte[] helpID = new byte[]{menuData[3], menuData[4], menuData[5], menuData[6]};
                    item.setMenuHelpId(ByteHexHelper.bytesToHexString(helpID));
                    int i = helpID[0] & KEYRecord.PROTOCOL_ANY;
                    int i2 = helpID[1] & KEYRecord.PROTOCOL_ANY;
                    i2 = (helpID[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE;
                    int v_id = (((r0 * 16777216) + (r0 * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + r0) + (helpID[3] & KEYRecord.PROTOCOL_ANY);
                    item.setMenuHelpContent(ByteHexHelper.bytesToHexString(CToJava.searchId.getTextFromLibReturnByte(v_id, 15)));
                    String[] datas = separateString(ByteHexHelper.bytesToHexString(menuData).substring(10));
                    item.setSite(site);
                    item.setMenuHelpId(ByteHexHelper.bytesToHexString(helpID));
                    menuList.add(item);
                    if (datas[0].equalsIgnoreCase("---")) {
                        item.setMenuTitleContent(XmlPullParser.NO_NAMESPACE);
                    } else {
                        item.setMenuTitleContent(ByteHexHelper.hexStringToWord(datas[0]));
                    }
                    int i3 = 1;
                    while (true) {
                        i = datas.length;
                        if (i3 >= r0) {
                            break;
                        }
                        MenuData menu = new MenuData();
                        if (datas[i3].equalsIgnoreCase("---")) {
                            menu.setMenuContent(XmlPullParser.NO_NAMESPACE);
                        } else {
                            menu.setMenuContent(ByteHexHelper.hexStringToWord(datas[i3]));
                        }
                        menuList.add(menu);
                        i3++;
                    }
                }
            }
        }
        return menuList;
    }

    public static ArrayList<SupportConnectorId> spt_supportConnector(PageInteractiveData uiData) {
        ArrayList<SupportConnectorId> list = new ArrayList();
        byte[] data = uiData.getData();
        int dataLen = data.length;
        int connectorNum = ByteHexHelper.byteToInt(data[0]);
        if (dataLen > 1) {
            int buttonSite = 0;
            SupportConnectorId connectID = new SupportConnectorId();
            for (int i = 0; i < connectorNum * 4; i += 4) {
                connectID.setConnectorId(ByteHexHelper.bytesToHexString(new byte[]{data[i + 1], data[i + 2], data[i + 3], data[i + 4]}));
                buttonSite++;
            }
        }
        return list;
    }

    public static ArrayList<MenuData> spt_menu_id56(PageInteractiveData uiData) {
        ArrayList<MenuData> menuList = new ArrayList();
        byte[] menuData = uiData.getData();
        for (int a = 0; a < menuData.length; a++) {
        }
        int menuLen = menuData.length;
        int site = 0;
        if (menuLen > 1) {
            if (ByteHexHelper.byteToHexString(menuData[0]).equalsIgnoreCase("FF")) {
            }
            if (menuLen > 2) {
                try {
                    site = Integer.valueOf(ByteHexHelper.intPackLength(ByteHexHelper.byteToHexString(menuData[1]))).intValue();
                } catch (NumberFormatException e) {
                }
                if (menuLen > 3) {
                    int menuNums = ByteHexHelper.byteToInt(menuData[2]);
                    if (menuData.length == (menuNums * 4) + 5) {
                        MenuData item = new MenuData();
                        item.setSite(site);
                        menuList.add(item);
                        String dataStr = ByteHexHelper.bytesToHexString(menuData).substring(10);
                        if (dataStr.length() == menuNums * 8) {
                            for (int i = 0; i < menuNums; i++) {
                                MenuData menu = new MenuData();
                                menu.setMenuId(dataStr.substring(i * 8, (i * 8) + 8));
                                menuList.add(menu);
                            }
                        }
                    }
                }
            }
        }
        return menuList;
    }

    public static String[] SPT_GGP_PATHNAME(PageInteractiveData uiData) {
        return separateString(ByteHexHelper.bytesToHexString(uiData.getData()));
    }
}
