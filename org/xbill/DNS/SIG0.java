package org.xbill.DNS;

import com.cnlaunch.mycar.jni.JniX431File;
import java.security.PrivateKey;
import java.util.Date;
import org.xbill.DNS.DNSSEC.DNSSECException;

public class SIG0 {
    private static final short VALIDITY = (short) 300;

    private SIG0() {
    }

    public static void signMessage(Message message, KEYRecord kEYRecord, PrivateKey privateKey, SIGRecord sIGRecord) throws DNSSECException {
        int intValue = Options.intValue("sig0validity");
        if (intValue < 0) {
            intValue = JniX431File.MAX_DS_COLNUMBER;
        }
        long currentTimeMillis = System.currentTimeMillis();
        message.addRecord(DNSSEC.signMessage(message, sIGRecord, kEYRecord, privateKey, new Date(currentTimeMillis), new Date(currentTimeMillis + ((long) (intValue * 1000)))), 3);
    }

    public static void verifyMessage(Message message, byte[] bArr, KEYRecord kEYRecord, SIGRecord sIGRecord) throws DNSSECException {
        SIGRecord sIGRecord2;
        Record[] sectionArray = message.getSectionArray(3);
        int i = 0;
        while (i < sectionArray.length) {
            if (sectionArray[i].getType() == 24 && ((SIGRecord) sectionArray[i]).getTypeCovered() == 0) {
                sIGRecord2 = (SIGRecord) sectionArray[i];
                break;
            }
            i++;
        }
        sIGRecord2 = null;
        DNSSEC.verifyMessage(message, bArr, sIGRecord2, sIGRecord, kEYRecord);
    }
}
