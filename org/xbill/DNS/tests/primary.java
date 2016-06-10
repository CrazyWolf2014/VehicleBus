package org.xbill.DNS.tests;

import java.util.Iterator;
import org.xbill.DNS.Name;
import org.xbill.DNS.Zone;

public class primary {
    private static void usage() {
        System.out.println("usage: primary [-t] [-a | -i] origin file");
        System.exit(1);
    }

    public static void main(String[] strArr) throws Exception {
        int i;
        int i2;
        int i3;
        int i4;
        if (strArr.length < 2) {
            i = 0;
            i2 = 0;
            i3 = 0;
            i4 = 0;
            usage();
        } else {
            i = 0;
            i2 = 0;
            i3 = 0;
            i4 = 0;
        }
        while (strArr.length - i > 2) {
            if (strArr[0].equals("-t")) {
                i4 = 1;
            } else if (strArr[0].equals("-a")) {
                i3 = 1;
            } else if (strArr[0].equals("-i")) {
                i2 = 1;
            }
            i++;
        }
        int i5 = i + 1;
        Name fromString = Name.fromString(strArr[i], Name.root);
        int i6 = i5 + 1;
        String str = strArr[i5];
        long currentTimeMillis = System.currentTimeMillis();
        Zone zone = new Zone(fromString, str);
        long currentTimeMillis2 = System.currentTimeMillis();
        Iterator AXFR;
        if (i3 != 0) {
            AXFR = zone.AXFR();
            while (AXFR.hasNext()) {
                System.out.println(AXFR.next());
            }
        } else if (i2 != 0) {
            AXFR = zone.iterator();
            while (AXFR.hasNext()) {
                System.out.println(AXFR.next());
            }
        } else {
            System.out.println(zone);
        }
        if (i4 != 0) {
            System.out.println("; Load time: " + (currentTimeMillis2 - currentTimeMillis) + " ms");
        }
    }
}
