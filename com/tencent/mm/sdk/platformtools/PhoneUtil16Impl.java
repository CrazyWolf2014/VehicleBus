package com.tencent.mm.sdk.platformtools;

import android.content.Context;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import com.tencent.mm.sdk.platformtools.PhoneUtil.CellInfo;
import com.thoughtworks.xstream.XStream;
import java.util.LinkedList;
import java.util.List;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

class PhoneUtil16Impl {
    private static int aJ;
    private static int aK;
    private TelephonyManager aL;
    private PhoneStateListener aM;

    /* renamed from: com.tencent.mm.sdk.platformtools.PhoneUtil16Impl.1 */
    class C08701 extends PhoneStateListener {
        final /* synthetic */ PhoneUtil16Impl aN;

        C08701(PhoneUtil16Impl phoneUtil16Impl) {
            this.aN = phoneUtil16Impl;
        }

        public void onSignalStrengthChanged(int i) {
            super.onSignalStrengthChanged(i);
            PhoneUtil16Impl.aK = (i * 2) - 113;
            if (this.aN.aL != null) {
                this.aN.aL.listen(this.aN.aM, 0);
            }
        }
    }

    static {
        aJ = XStream.PRIORITY_VERY_HIGH;
        aK = XStream.PRIORITY_VERY_HIGH;
    }

    PhoneUtil16Impl() {
        this.aM = new C08701(this);
    }

    public List<CellInfo> getCellInfoList(Context context) {
        List<CellInfo> linkedList = new LinkedList();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        String networkOperator = telephonyManager.getNetworkOperator();
        if (networkOperator == null || networkOperator.equals(XmlPullParser.NO_NAMESPACE)) {
            return linkedList;
        }
        String str = "460";
        String str2 = XmlPullParser.NO_NAMESPACE;
        try {
            str = networkOperator.substring(0, 3);
            str2 = networkOperator.substring(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            if (gsmCellLocation != null) {
                int cid = gsmCellLocation.getCid();
                int lac = gsmCellLocation.getLac();
                if (!(lac >= InBandBytestreamManager.MAXIMUM_BLOCK_SIZE || lac == -1 || cid == -1)) {
                    linkedList.add(new CellInfo(str, str2, String.valueOf(lac), String.valueOf(cid), aK == aJ ? XmlPullParser.NO_NAMESPACE : aK, PhoneUtil.CELL_GSM, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE));
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        List<NeighboringCellInfo> neighboringCellInfo = telephonyManager.getNeighboringCellInfo();
        if (neighboringCellInfo != null && neighboringCellInfo.size() > 0) {
            for (NeighboringCellInfo neighboringCellInfo2 : neighboringCellInfo) {
                if (neighboringCellInfo2.getCid() != -1) {
                    linkedList.add(new CellInfo(str, str2, XmlPullParser.NO_NAMESPACE, neighboringCellInfo2.getCid(), XmlPullParser.NO_NAMESPACE, PhoneUtil.CELL_GSM, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE));
                }
            }
        }
        return linkedList;
    }

    public void getSignalStrength(Context context) {
        this.aL = (TelephonyManager) context.getSystemService("phone");
        this.aL.listen(this.aM, KEYRecord.OWNER_ZONE);
    }
}
