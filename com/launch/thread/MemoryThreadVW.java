package com.launch.thread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.ifoer.entity.Constant;
import com.ifoer.entity.PageInteractiveData;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.BluetoothOrder.PageInteractiveDataAnalysis;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.MeasureConversion;
import com.ifoer.util.MySharedPreferences;
import com.launch.listener.OnVWDataStreamListener;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.ArrayList;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class MemoryThreadVW extends MemoryThread {
    private OnVWDataStreamListener onVWDataStreamListener;

    public MemoryThreadVW() {
        this.onVWDataStreamListener = null;
    }

    public void setOnDataStreamListener(OnVWDataStreamListener onVwDataStreamListener) {
        this.onVWDataStreamListener = onVwDataStreamListener;
    }

    public void handleData(PageInteractiveData mData) {
        ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = PageInteractiveDataAnalysis.spt_vw_datastream_id19(mData);
        String path = Constant.OBD2_EN_GGP_PATH;
        if (path != null && path.length() > 0) {
            for (int i = 0; i < vwDataStreamIdlist.size(); i++) {
                byte[] activeContent;
                int j;
                SptVwDataStreamIdItem vwStreamSelectIdItem = (SptVwDataStreamIdItem) vwDataStreamIdlist.get(i);
                String lString = XmlPullParser.NO_NAMESPACE;
                byte[] databuf = ByteHexHelper.hexStringToBytes(vwStreamSelectIdItem.getStreamTextId());
                byte[] activeContentbytes = CToJava.searchId.getTextFromLibReturnByte(((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + ((databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE)) + (databuf[3] & KEYRecord.PROTOCOL_ANY), 3);
                if (activeContentbytes.length > 0) {
                    activeContent = new byte[(activeContentbytes.length - 1)];
                    for (j = 0; j < activeContent.length; j++) {
                        activeContent[j] = activeContentbytes[j];
                    }
                } else {
                    activeContent = new byte[0];
                }
                String[] datas = PageInteractiveDataAnalysis.separateString(ByteHexHelper.bytesToHexString(activeContent));
                if (datas.length >= 1 && datas[0].equalsIgnoreCase("---")) {
                    vwStreamSelectIdItem.setStreamTextIdContent(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                } else if (datas.length >= 1 && !datas[0].equalsIgnoreCase("---")) {
                    vwStreamSelectIdItem.setStreamTextIdContent(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(datas[0])));
                } else if (datas.length == 0) {
                    vwStreamSelectIdItem.setStreamTextIdContent(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                }
                lString = XmlPullParser.NO_NAMESPACE;
                databuf = ByteHexHelper.hexStringToBytes(vwStreamSelectIdItem.getStreamUnitId());
                activeContentbytes = CToJava.searchId.getTextFromLibReturnByte(((((databuf[0] & KEYRecord.PROTOCOL_ANY) * 16777216) + ((databuf[1] & KEYRecord.PROTOCOL_ANY) * AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED)) + ((databuf[2] & KEYRecord.PROTOCOL_ANY) * KEYRecord.OWNER_ZONE)) + (databuf[3] & KEYRecord.PROTOCOL_ANY), 3);
                if (activeContentbytes.length > 0) {
                    activeContent = new byte[(activeContentbytes.length - 1)];
                    for (j = 0; j < activeContent.length; j++) {
                        activeContent[j] = activeContentbytes[j];
                    }
                } else {
                    activeContent = new byte[0];
                }
                datas = PageInteractiveDataAnalysis.separateString(ByteHexHelper.bytesToHexString(activeContent));
                if (datas.length > 1 && datas[1].equalsIgnoreCase("---")) {
                    vwStreamSelectIdItem.setStreamUnitIdContent(XmlPullParser.NO_NAMESPACE);
                } else if (datas.length > 1 && !datas[1].equalsIgnoreCase("---")) {
                    vwStreamSelectIdItem.setStreamUnitIdContent(ByteHexHelper.byteToWord(ByteHexHelper.hexStringToBytes(datas[1])));
                }
            }
            if (!(MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.conversion) == null || MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.conversion).equals(XmlPullParser.NO_NAMESPACE))) {
                if (MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.conversion).equals("Imperial")) {
                    MeasureConversion.convertMetricToImperialVM(vwDataStreamIdlist, 2);
                } else {
                    MeasureConversion.convertImperialToMetricVM(vwDataStreamIdlist, 2);
                }
            }
            Intent intent = new Intent("SPT_VW_DATASTREAM_ID");
            Bundle bundle = new Bundle();
            bundle.putSerializable("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
            bundle.putInt(SharedPref.TYPE, 19);
            intent.putExtras(bundle);
            MainActivity.contexts.sendBroadcast(intent);
        }
    }
}
