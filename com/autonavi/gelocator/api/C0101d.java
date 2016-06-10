package com.autonavi.gelocator.api;

import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

/* renamed from: com.autonavi.gelocator.api.d */
final class C0101d extends PhoneStateListener {
    private /* synthetic */ LbsManager f806a;

    C0101d(LbsManager lbsManager) {
        this.f806a = lbsManager;
    }

    public final void onCellLocationChanged(CellLocation cellLocation) {
        if (cellLocation != null) {
            if (cellLocation instanceof GsmCellLocation) {
                this.f806a.f760j = 1;
                this.f806a.f761k.clear();
                C0099b c0099b = new C0099b();
                c0099b.f800b = ((GsmCellLocation) cellLocation).getLac();
                c0099b.f799a = ((GsmCellLocation) cellLocation).getCid();
                String networkOperator = this.f806a.f758h.getNetworkOperator();
                if (networkOperator != null && networkOperator.length() >= 5) {
                    c0099b.f802d = networkOperator.substring(0, 3);
                    c0099b.f801c = networkOperator.substring(3, 5);
                }
                c0099b.f803e = this.f806a.f763m;
                c0099b.f804f = System.currentTimeMillis();
                this.f806a.f761k.add(0, c0099b);
                for (NeighboringCellInfo neighboringCellInfo : this.f806a.f758h.getNeighboringCellInfo()) {
                    try {
                        if (networkOperator != null && networkOperator.length() >= 5) {
                            C0099b c0099b2 = new C0099b();
                            c0099b2.f799a = neighboringCellInfo.getCid();
                            c0099b2.f800b = neighboringCellInfo.getLac();
                            c0099b2.f802d = networkOperator.substring(0, 3);
                            c0099b2.f801c = networkOperator.substring(3, 5);
                            c0099b2.f803e = (neighboringCellInfo.getRssi() * 2) - 133;
                            c0099b2.f804f = System.currentTimeMillis();
                            this.f806a.f761k.add(c0099b2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                while (this.f806a.f761k.size() > 4) {
                    this.f806a.f761k.remove(this.f806a.f761k.size() - 1);
                }
            } else {
                try {
                    Class.forName("android.telephony.cdma.CdmaCellLocation");
                    this.f806a.f760j = 2;
                    this.f806a.f762l.clear();
                    CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
                    C0098a c0098a = new C0098a();
                    c0098a.f790c = cdmaCellLocation.getBaseStationId() >= 0 ? cdmaCellLocation.getBaseStationId() : c0098a.f790c;
                    c0098a.f791d = cdmaCellLocation.getNetworkId() >= 0 ? cdmaCellLocation.getNetworkId() : c0098a.f791d;
                    c0098a.f792e = cdmaCellLocation.getSystemId() >= 0 ? cdmaCellLocation.getSystemId() : c0098a.f792e;
                    String networkOperator2 = this.f806a.f758h.getNetworkOperator();
                    if (networkOperator2 != null && networkOperator2.length() >= 5) {
                        c0098a.f794g = networkOperator2.substring(0, 3);
                        c0098a.f793f = networkOperator2.substring(3, 5);
                    }
                    c0098a.f795h = this.f806a.f763m;
                    c0098a.f796i = System.currentTimeMillis();
                    int baseStationLatitude = cdmaCellLocation.getBaseStationLatitude();
                    int baseStationLongitude = cdmaCellLocation.getBaseStationLongitude();
                    if (baseStationLatitude < Integer.MAX_VALUE && baseStationLongitude < Integer.MAX_VALUE) {
                        c0098a.f788a = baseStationLatitude;
                        c0098a.f789b = baseStationLongitude;
                    }
                    this.f806a.f762l.add(0, c0098a);
                    for (NeighboringCellInfo neighboringCellInfo2 : this.f806a.f758h.getNeighboringCellInfo()) {
                        if (networkOperator2 != null && networkOperator2.length() >= 5) {
                            C0098a c0098a2 = new C0098a();
                            c0098a2.f793f = networkOperator2.substring(3, 5);
                            c0098a2.f798k = new StringBuilder(String.valueOf(neighboringCellInfo2.getLac())).toString();
                            c0098a2.f797j = new StringBuilder(String.valueOf(neighboringCellInfo2.getCid())).toString();
                            c0098a2.f795h = (neighboringCellInfo2.getRssi() * 2) - 113;
                            this.f806a.f762l.add(c0098a2);
                        }
                    }
                    while (this.f806a.f762l.size() > 4) {
                        this.f806a.f762l.remove(this.f806a.f762l.size() - 1);
                    }
                } catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
            super.onCellLocationChanged(cellLocation);
        }
    }

    public final void onServiceStateChanged(ServiceState serviceState) {
        super.onServiceStateChanged(serviceState);
    }

    public final void onSignalStrengthsChanged(SignalStrength signalStrength) {
        this.f806a.f763m = (signalStrength.getGsmSignalStrength() * 2) - 113;
        if (this.f806a.f760j == 1 && this.f806a.f761k.size() > 0) {
            ((C0099b) this.f806a.f761k.get(0)).f803e = this.f806a.f763m;
        } else if (this.f806a.f760j == 2 && this.f806a.f762l.size() > 0) {
            ((C0098a) this.f806a.f762l.get(0)).f795h = this.f806a.f763m;
        }
        super.onSignalStrengthsChanged(signalStrength);
    }
}
