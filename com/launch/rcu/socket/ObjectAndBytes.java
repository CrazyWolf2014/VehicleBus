package com.launch.rcu.socket;

import android.util.Base64;
import android.util.Log;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnmobi.im.dto.MessageVo;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.entity.MenuData;
import com.ifoer.entity.SpecialFunction;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptActiveTestButton;
import com.ifoer.entity.SptActiveTestStream;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptInputNumric;
import com.ifoer.entity.SptInputStringEx;
import com.ifoer.entity.SptMessageBoxText;
import com.ifoer.entity.SptStreamSelectIdItem;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.entity.Spt_Nobuttonbox_Text;
import com.ifoer.entity.Spt_Progressbar_Box;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expeditionphone.WelcomeActivity;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import org.achartengine.ChartFactory;
import org.codehaus.jackson.smile.SmileConstants;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class ObjectAndBytes {
    public static Object readBytesToObject(byte[] bytes) {
        Object menuData = null;
        try {
            menuData = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(ByteHexHelper.byteToWord(bytes), 0))).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuData;
    }

    public static byte[] writeObjectToBytes(Object obj) {
        byte[] bytesMemuData = null;
        try {
            ByteArrayOutputStream toByte = new ByteArrayOutputStream();
            new ObjectOutputStream(toByte).writeObject(obj);
            bytesMemuData = Base64.encode(toByte.toByteArray(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytesMemuData;
    }

    public static Object getDataFromWeb(JSONObject json, int packageType) {
        Log.d("weiwei111", "getDataFromWeb" + packageType);
        int i;
        String string;
        JSONObject json17;
        switch (packageType) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case Protocol.IRTP /*28*/:
            case Service.MSG_ICP /*29*/:
                try {
                    JSONArray jsonArray = json.getJSONArray(DataPacketExtension.ELEMENT_NAME);
                    ArrayList menuList = new ArrayList();
                    MenuData menudata = new MenuData();
                    menudata.setMenuContent(XmlPullParser.NO_NAMESPACE);
                    menuList.add(menudata);
                    menuList.add(menudata);
                    for (i = 0; i < jsonArray.length(); i++) {
                        JSONObject json1 = jsonArray.getJSONObject(i);
                        MenuData menu = new MenuData();
                        menu.setMenuContent(json1.getString("content"));
                        menuList.add(menu);
                        menu.setSite(0);
                    }
                    if (menuList == null || menuList.size() <= 2) {
                        return menuList;
                    }
                    ((MenuData) menuList.get(2)).setSite(0);
                    return menuList;
                } catch (JSONException e) {
                    e.printStackTrace();
                    break;
                }
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
            case Service.NSW_FE /*27*/:
            case Service.TIME /*37*/:
            case Type.A6 /*38*/:
            case Service.RLP /*39*/:
            case SmileConstants.TOKEN_MISC_FP /*40*/:
            case Service.GRAPHICS /*41*/:
            case Service.NAMESERVER /*42*/:
            case Service.NICNAME /*43*/:
                ArrayList<SptTroubleTest> sptTroubleTests = new ArrayList();
                JSONArray jsonArray37 = json.getJSONArray(DataPacketExtension.ELEMENT_NAME);
                if (jsonArray37 != null) {
                    for (i = 0; i < jsonArray37.length(); i++) {
                        JSONObject json37 = jsonArray37.getJSONObject(i);
                        SptTroubleTest sptTroubleTest = new SptTroubleTest();
                        sptTroubleTest.setTroubleCodeContent(json37.getString("dtcCode"));
                        sptTroubleTest.setTroubleDescribeContent(json37.getString("dtcDescription"));
                        sptTroubleTest.setTroubleStateContent(json37.getString("dtcStatus"));
                        if (json37.has("dtcHelp")) {
                            string = json37.getString("dtcHelp");
                        } else {
                            string = XmlPullParser.NO_NAMESPACE;
                        }
                        sptTroubleTest.setTroubleHelp(string);
                        sptTroubleTests.add(sptTroubleTest);
                    }
                }
                return sptTroubleTests;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                JSONObject json5 = json.getJSONObject(DataPacketExtension.ELEMENT_NAME);
                Log.d("weige", "====================================" + json5.getString("messageTitle"));
                Spt_Nobuttonbox_Text spt_Nobuttonbox_Text = new Spt_Nobuttonbox_Text();
                spt_Nobuttonbox_Text.setTitle(json5.getString("messageTitle"));
                spt_Nobuttonbox_Text.setContent(json5.getString("messageContent"));
                return spt_Nobuttonbox_Text;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                JSONObject json6 = json.getJSONObject(DataPacketExtension.ELEMENT_NAME);
                SptMessageBoxText sptMessageBoxText = new SptMessageBoxText();
                sptMessageBoxText.setDialogType(Integer.parseInt(json6.getString("dialogStyle")));
                sptMessageBoxText.setDialogTitle(json6.getString("messageTitle"));
                sptMessageBoxText.setDialogContent(json6.getString("messageContent"));
                return sptMessageBoxText;
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                SptActiveTest sptActiveTest = new SptActiveTest();
                ArrayList<SptActiveTestButton> btnList = new ArrayList();
                ArrayList<SptActiveTestStream> streamList = new ArrayList();
                JSONArray jsonArray91 = json.getJSONArray(DataPacketExtension.ELEMENT_NAME);
                if (jsonArray91 != null) {
                    for (i = 0; i < jsonArray91.length(); i++) {
                        JSONObject json91 = jsonArray91.getJSONObject(i);
                        SptActiveTestButton sptActiveTestButton = new SptActiveTestButton();
                        sptActiveTestButton.setActiveButtonSite(Integer.parseInt(json91.getString("buttonId")));
                        sptActiveTestButton.setActiveButtonContent(json91.getString(MessageVo.TYPE_TEXT));
                        btnList.add(sptActiveTestButton);
                    }
                }
                sptActiveTest.setActiveTestButtons(btnList);
                JSONArray jsonArray92 = json.getJSONArray("data1");
                if (jsonArray91 != null) {
                    for (i = 0; i < jsonArray92.length(); i++) {
                        JSONObject json92 = jsonArray92.getJSONObject(i);
                        SptActiveTestStream sptActiveTestStream = new SptActiveTestStream();
                        sptActiveTestStream.setDataStreamContent(json92.getString("datastreamName"));
                        sptActiveTestStream.setDataStreamStr(json92.getString("datastreamValue"));
                        sptActiveTestStream.setUnit(json92.getString("datastreamUnit"));
                        streamList.add(sptActiveTestStream);
                    }
                }
                sptActiveTest.setActiveTestStreams(streamList);
                return sptActiveTest;
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                return XmlPullParser.NO_NAMESPACE;
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                return json.getString(DataPacketExtension.ELEMENT_NAME);
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                SptInputNumric sptInputNumric = new SptInputNumric();
                JSONObject json15 = json.getJSONObject(DataPacketExtension.ELEMENT_NAME);
                sptInputNumric.setDialogContent(json15.getString("messageContent"));
                sptInputNumric.setDigit(Integer.parseInt(json15.getString("messageDiait")));
                sptInputNumric.setInputHint(json15.getString("messageInputHint"));
                sptInputNumric.setDialogTitle(json15.getString("messageTitle"));
                return sptInputNumric;
            case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                JSONObject json16 = json.getJSONObject(DataPacketExtension.ELEMENT_NAME);
                SptInputStringEx sptInputStringEx = new SptInputStringEx();
                sptInputStringEx.setDialogContent(json16.getString("messageContent"));
                sptInputStringEx.setInputHint(json16.getString("messageInputHint"));
                sptInputStringEx.setDialogTitle(json16.getString("messageTitle"));
                return sptInputStringEx;
            case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                JSONArray jsonArray17 = json.getJSONArray(DataPacketExtension.ELEMENT_NAME);
                ArrayList<SptStreamSelectIdItem> sptSelectIdItems = new ArrayList();
                if (jsonArray17 != null) {
                    for (i = 0; i < jsonArray17.length(); i++) {
                        json17 = jsonArray17.getJSONObject(i);
                        SptStreamSelectIdItem sptSelectIdItem = new SptStreamSelectIdItem();
                        sptSelectIdItem.setStreamSelectStr(json17.getString("content"));
                        sptSelectIdItems.add(sptSelectIdItem);
                    }
                }
                if (sptSelectIdItems != null && sptSelectIdItems.size() > 0) {
                    ((SptStreamSelectIdItem) sptSelectIdItems.get(0)).setSite(Integer.parseInt(json.getString("position")));
                }
                return sptSelectIdItems;
            case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
            case Type.ATMA /*34*/:
                JSONArray jsonArray18 = json.getJSONArray(DataPacketExtension.ELEMENT_NAME);
                ArrayList<SptExDataStreamIdItem> sptExDataStreamIdItems = new ArrayList();
                if (jsonArray18 != null) {
                    for (i = 0; i < jsonArray18.length(); i++) {
                        json17 = jsonArray18.getJSONObject(i);
                        SptExDataStreamIdItem sptExDataStreamIdItem = new SptExDataStreamIdItem();
                        sptExDataStreamIdItem.setStreamStr(json17.getString("datastreamValue"));
                        sptExDataStreamIdItem.setStreamTextIdContent(json17.getString("datastreamName"));
                        sptExDataStreamIdItem.setStreamState(json17.getString("datastreamUnit"));
                        if (json17.has("datastreamID")) {
                            string = json17.getString("datastreamID");
                        } else {
                            string = XmlPullParser.NO_NAMESPACE;
                        }
                        sptExDataStreamIdItem.setStreamTextId(string);
                        sptExDataStreamIdItems.add(sptExDataStreamIdItem);
                    }
                }
                return sptExDataStreamIdItems;
            case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                JSONArray jsonArray19 = json.getJSONArray(DataPacketExtension.ELEMENT_NAME);
                ArrayList<SptVwDataStreamIdItem> sptVwDataStreamIdItems = new ArrayList();
                if (jsonArray19 != null) {
                    for (i = 0; i < jsonArray19.length(); i++) {
                        JSONObject json19 = jsonArray19.getJSONObject(i);
                        SptVwDataStreamIdItem sptVwDataStreamIdItem = new SptVwDataStreamIdItem();
                        sptVwDataStreamIdItem.setStreamTextId(json19.getString("datastreamID"));
                        sptVwDataStreamIdItem.setStreamTextIdContent(json19.getString("datastreamName"));
                        sptVwDataStreamIdItem.setStreamUnitIdContent(json19.getString("datastreamUnit"));
                        sptVwDataStreamIdItem.setStreamStr(json19.getString("datastreamValue"));
                        sptVwDataStreamIdItems.add(sptVwDataStreamIdItem);
                    }
                }
                return sptVwDataStreamIdItems;
            case Protocol.NETBLT /*30*/:
                return json.get("content");
            case Type.KX /*36*/:
                ArrayList<SpecialFunction> list = new ArrayList();
                JSONArray jsonArray36 = json.getJSONArray(DataPacketExtension.ELEMENT_NAME);
                if (jsonArray36 != null) {
                    for (i = 0; i < jsonArray36.length(); i++) {
                        int y;
                        SpecialFunction specialFunction = new SpecialFunction();
                        JSONObject json36 = jsonArray36.getJSONObject(i);
                        specialFunction.setColumsCount(json36.getInt("columsCount"));
                        specialFunction.setButtonCount(json36.getInt("bottonCount"));
                        specialFunction.setCustomTitle(json36.getString(ChartFactory.TITLE));
                        ArrayList<String> list1 = new ArrayList();
                        if (!json36.isNull("bottonList")) {
                            JSONArray jsonArray361 = json36.getJSONArray("bottonList");
                            if (jsonArray361 != null) {
                                Log.d("weiwei111", "jsonArray361.length()" + jsonArray361.length());
                                for (y = 0; y < jsonArray361.length(); y++) {
                                    list1.add(jsonArray361.getJSONObject(y).getString("content"));
                                }
                            }
                            specialFunction.setButtonList(list1);
                        }
                        ArrayList<String> list2 = new ArrayList();
                        JSONArray jsonArray362 = json36.getJSONArray("columsContentList");
                        if (jsonArray362 != null) {
                            for (y = 0; y < jsonArray362.length(); y++) {
                                list2.add(jsonArray362.getJSONObject(y).getString("content"));
                            }
                        }
                        specialFunction.setColumsContentList(list2);
                        ArrayList<String> list3 = new ArrayList();
                        JSONArray jsonArray363 = json36.getJSONArray("columsTitleList");
                        if (jsonArray363 != null) {
                            for (y = 0; y < jsonArray363.length(); y++) {
                                list3.add(jsonArray363.getJSONObject(y).getString("content"));
                            }
                        }
                        specialFunction.setColumsTitleList(list3);
                        list.add(specialFunction);
                    }
                }
                return list;
            case Service.MPM_FLAGS /*44*/:
                JSONArray jsonArray44 = json.getJSONArray(DataPacketExtension.ELEMENT_NAME);
                Object strs = null;
                if (jsonArray44 != null) {
                    strs = new String[jsonArray44.length()];
                    for (i = 0; i < jsonArray44.length(); i++) {
                        strs[i] = jsonArray44.getJSONObject(i).getString("content");
                    }
                }
                return strs;
            case Service.MPM /*45*/:
                Spt_Progressbar_Box spt_Progressbar_Box = new Spt_Progressbar_Box();
                spt_Progressbar_Box.setContent(json.getString("content"));
                spt_Progressbar_Box.setTitle(json.getString("progressbarTtitle"));
                spt_Progressbar_Box.setProgressbarLen(Integer.parseInt(json.getString("length")));
                return spt_Progressbar_Box;
            default:
                return null;
        }
        e.printStackTrace();
        return null;
    }
}
