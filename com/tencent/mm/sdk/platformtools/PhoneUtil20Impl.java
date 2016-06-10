package com.tencent.mm.sdk.platformtools;

import android.content.Context;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import com.tencent.mm.sdk.platformtools.PhoneUtil.CellInfo;
import com.thoughtworks.xstream.XStream;
import java.util.LinkedList;
import java.util.List;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

class PhoneUtil20Impl {
    private static int aJ;
    private static int aK;
    private TelephonyManager aL;
    private PhoneStateListener aM;
    private int aO;

    /* renamed from: com.tencent.mm.sdk.platformtools.PhoneUtil20Impl.1 */
    class C08711 extends PhoneStateListener {
        final /* synthetic */ PhoneUtil20Impl aP;

        C08711(PhoneUtil20Impl phoneUtil20Impl) {
            this.aP = phoneUtil20Impl;
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (this.aP.aO == 2) {
                PhoneUtil20Impl.aK = signalStrength.getCdmaDbm();
            }
            if (this.aP.aO == 1) {
                PhoneUtil20Impl.aK = (signalStrength.getGsmSignalStrength() * 2) - 113;
            }
            if (this.aP.aL != null) {
                this.aP.aL.listen(this.aP.aM, 0);
            }
        }
    }

    static {
        aJ = XStream.PRIORITY_VERY_HIGH;
        aK = XStream.PRIORITY_VERY_HIGH;
    }

    PhoneUtil20Impl() {
        this.aM = new C08711(this);
    }

    public List<CellInfo> getCellInfoList(Context context) {
        int cid;
        int lac;
        List<NeighboringCellInfo> neighboringCellInfo;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        List<CellInfo> linkedList = new LinkedList();
        String str = "460";
        String str2 = XmlPullParser.NO_NAMESPACE;
        try {
            String networkOperator = telephonyManager.getNetworkOperator();
            if (networkOperator == null || networkOperator.equals(XmlPullParser.NO_NAMESPACE)) {
                networkOperator = telephonyManager.getSimOperator();
                if (!(networkOperator == null || networkOperator.equals(XmlPullParser.NO_NAMESPACE))) {
                    str = networkOperator.substring(0, 3);
                    str2 = networkOperator.substring(3, 5);
                }
                networkOperator = str2;
            } else {
                str = networkOperator.substring(0, 3);
                networkOperator = networkOperator.substring(3, 5);
            }
            String str3;
            GsmCellLocation gsmCellLocation;
            if (telephonyManager.getPhoneType() == 2) {
                try {
                    CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) telephonyManager.getCellLocation();
                    if (cdmaCellLocation != null) {
                        str3 = aK == aJ ? XmlPullParser.NO_NAMESPACE : aK;
                        if (!(cdmaCellLocation.getBaseStationId() == -1 || cdmaCellLocation.getNetworkId() == -1 || cdmaCellLocation.getSystemId() == -1)) {
                            linkedList.add(new CellInfo(str, networkOperator, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, str3, PhoneUtil.CELL_CDMA, cdmaCellLocation.getBaseStationId(), cdmaCellLocation.getNetworkId(), cdmaCellLocation.getSystemId()));
                        }
                    }
                } catch (Exception e) {
                    try {
                        gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
                        if (gsmCellLocation != null) {
                            cid = gsmCellLocation.getCid();
                            lac = gsmCellLocation.getLac();
                            if (!(lac >= InBandBytestreamManager.MAXIMUM_BLOCK_SIZE || lac == -1 || cid == -1)) {
                                linkedList.add(new CellInfo(str, networkOperator, String.valueOf(lac), String.valueOf(cid), XmlPullParser.NO_NAMESPACE, PhoneUtil.CELL_GSM, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE));
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    neighboringCellInfo = telephonyManager.getNeighboringCellInfo();
                    if (neighboringCellInfo != null && neighboringCellInfo.size() > 0) {
                        for (NeighboringCellInfo neighboringCellInfo2 : neighboringCellInfo) {
                            if (!(neighboringCellInfo2.getCid() == -1 || neighboringCellInfo2.getLac() > InBandBytestreamManager.MAXIMUM_BLOCK_SIZE || neighboringCellInfo2.getLac() == -1)) {
                                linkedList.add(new CellInfo(str, networkOperator, neighboringCellInfo2.getLac(), neighboringCellInfo2.getCid(), ((neighboringCellInfo2.getRssi() * 2) - 113), PhoneUtil.CELL_GSM, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE));
                            }
                        }
                    }
                }
            } else {
                try {
                    gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
                    if (gsmCellLocation != null) {
                        cid = gsmCellLocation.getCid();
                        lac = gsmCellLocation.getLac();
                        if (!(lac >= InBandBytestreamManager.MAXIMUM_BLOCK_SIZE || lac == -1 || cid == -1)) {
                            linkedList.add(new CellInfo(str, networkOperator, String.valueOf(lac), String.valueOf(cid), aK == aJ ? XmlPullParser.NO_NAMESPACE : aK, PhoneUtil.CELL_GSM, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE));
                        }
                    }
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
                neighboringCellInfo = telephonyManager.getNeighboringCellInfo();
                if (neighboringCellInfo != null && neighboringCellInfo.size() > 0) {
                    for (NeighboringCellInfo neighboringCellInfo22 : neighboringCellInfo) {
                        if (neighboringCellInfo22.getCid() != -1 && neighboringCellInfo22.getLac() <= InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) {
                            str3 = ((neighboringCellInfo22.getRssi() * 2) - 113);
                            Log.m1663v("checked", "lac:" + neighboringCellInfo22.getLac() + "  cid:" + neighboringCellInfo22.getCid() + " dbm:" + str3);
                            linkedList.add(new CellInfo(str, networkOperator, neighboringCellInfo22.getLac(), neighboringCellInfo22.getCid(), str3, PhoneUtil.CELL_GSM, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE));
                        }
                    }
                }
            }
            return linkedList;
        } catch (Exception e222) {
            e222.printStackTrace();
            return linkedList;
        }
    }

    public void getSignalStrength(Context context) {
        this.aL = (TelephonyManager) context.getSystemService("phone");
        this.aL.listen(this.aM, KEYRecord.OWNER_ZONE);
        this.aO = this.aL.getPhoneType();
    }
}
