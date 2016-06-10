package org.xbill.DNS.tests;

import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.ZoneTransferIn;
import org.xbill.DNS.ZoneTransferIn.Delta;

public class xfrin {
    private static void usage(String str) {
        System.out.println("Error: " + str);
        System.out.println("usage: xfrin [-i serial] [-k keyname/secret] [-s server] [-p port] [-f] zone");
        System.exit(1);
    }

    public static void main(String[] strArr) throws Exception {
        ZoneTransferIn newIXFR;
        String str = null;
        int i = -1;
        int i2 = 53;
        int i3 = 0;
        boolean z = false;
        TSIG tsig = null;
        while (i3 < strArr.length) {
            int parseInt;
            if (strArr[i3].equals("-i")) {
                i3++;
                parseInt = Integer.parseInt(strArr[i3]);
                if (parseInt < 0) {
                    usage("invalid serial number");
                }
            } else if (strArr[i3].equals("-k")) {
                i3++;
                String str2 = strArr[i3];
                int indexOf = str2.indexOf(47);
                if (indexOf < 0) {
                    usage("invalid key");
                }
                tsig = new TSIG(str2.substring(0, indexOf), str2.substring(indexOf + 1));
                parseInt = i;
            } else if (strArr[i3].equals("-s")) {
                i3++;
                str = strArr[i3];
                parseInt = i;
            } else {
                if (!strArr[i3].equals("-p")) {
                    if (!strArr[i3].equals("-f")) {
                        if (!strArr[i3].startsWith("-")) {
                            break;
                        }
                        usage("invalid option");
                    } else {
                        z = true;
                        parseInt = i;
                    }
                } else {
                    i3++;
                    i2 = Integer.parseInt(strArr[i3]);
                    if (i2 < 0 || i2 > InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) {
                        usage("invalid port");
                        parseInt = i;
                    }
                }
                parseInt = i;
            }
            i3++;
            i = parseInt;
        }
        if (i3 >= strArr.length) {
            usage("no zone name specified");
        }
        Name fromString = Name.fromString(strArr[i3]);
        if (str == null) {
            Lookup lookup = new Lookup(fromString, 2);
            Record[] run = lookup.run();
            if (run == null) {
                System.out.println("failed to look up NS record: " + lookup.getErrorString());
                System.exit(1);
            }
            str2 = run[0].rdataToString();
            System.out.println("sending to server '" + str2 + "'");
        } else {
            str2 = str;
        }
        if (i >= 0) {
            newIXFR = ZoneTransferIn.newIXFR(fromString, (long) i, z, str2, i2, tsig);
        } else {
            newIXFR = ZoneTransferIn.newAXFR(fromString, str2, i2, tsig);
        }
        List<Object> run2 = newIXFR.run();
        if (newIXFR.isAXFR()) {
            if (i >= 0) {
                System.out.println("AXFR-like IXFR response");
            } else {
                System.out.println("AXFR response");
            }
            for (Object println : run2) {
                System.out.println(println);
            }
        } else if (newIXFR.isIXFR()) {
            System.out.println("IXFR response");
            Iterator it = run2.iterator();
            while (it.hasNext()) {
                Delta delta = (Delta) it.next();
                System.out.println("delta from " + delta.start + " to " + delta.end);
                System.out.println("deletes");
                for (Object println2 : delta.deletes) {
                    System.out.println(println2);
                }
                System.out.println("adds");
                for (Object println3 : delta.adds) {
                    System.out.println(println3);
                }
            }
        } else if (newIXFR.isCurrent()) {
            System.out.println("up to date");
        }
    }
}
