package org.xbill.DNS.spi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import org.xbill.DNS.AAAARecord;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.PTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.TextParseException;

public class DNSJavaNameService implements InvocationHandler {
    private static final String domainProperty = "sun.net.spi.nameservice.domain";
    private static final String nsProperty = "sun.net.spi.nameservice.nameservers";
    private static final String v6Property = "java.net.preferIPv6Addresses";
    private boolean preferV6;

    protected DNSJavaNameService() {
        int i = 0;
        this.preferV6 = false;
        String property = System.getProperty(nsProperty);
        String property2 = System.getProperty(domainProperty);
        String property3 = System.getProperty(v6Property);
        if (property != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(property, ",");
            String[] strArr = new String[stringTokenizer.countTokens()];
            while (stringTokenizer.hasMoreTokens()) {
                int i2 = i + 1;
                strArr[i] = stringTokenizer.nextToken();
                i = i2;
            }
            try {
                Lookup.setDefaultResolver(new ExtendedResolver(strArr));
            } catch (UnknownHostException e) {
                System.err.println("DNSJavaNameService: invalid sun.net.spi.nameservice.nameservers");
            }
        }
        if (property2 != null) {
            try {
                Lookup.setDefaultSearchPath(new String[]{property2});
            } catch (TextParseException e2) {
                System.err.println("DNSJavaNameService: invalid sun.net.spi.nameservice.domain");
            }
        }
        if (property3 != null && property3.equalsIgnoreCase("true")) {
            this.preferV6 = true;
        }
    }

    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        try {
            if (method.getName().equals("getHostByAddr")) {
                return getHostByAddr((byte[]) objArr[0]);
            }
            if (method.getName().equals("lookupAllHostAddr")) {
                Object lookupAllHostAddr = lookupAllHostAddr((String) objArr[0]);
                Class returnType = method.getReturnType();
                if (returnType.equals(InetAddress[].class)) {
                    return lookupAllHostAddr;
                }
                if (returnType.equals(byte[][].class)) {
                    int length = lookupAllHostAddr.length;
                    Object obj2 = new byte[length][];
                    for (int i = 0; i < length; i++) {
                        obj2[i] = lookupAllHostAddr[i].getAddress();
                    }
                    return obj2;
                }
            }
            throw new IllegalArgumentException("Unknown function name or arguments.");
        } catch (Throwable th) {
            System.err.println("DNSJavaNameService: Unexpected error.");
            th.printStackTrace();
        }
    }

    public InetAddress[] lookupAllHostAddr(String str) throws UnknownHostException {
        try {
            Record[] recordArr;
            Name name = new Name(str);
            Record[] recordArr2 = null;
            if (this.preferV6) {
                recordArr2 = new Lookup(name, 28).run();
            }
            if (recordArr2 == null) {
                recordArr2 = new Lookup(name, 1).run();
            }
            if (recordArr2 != null || this.preferV6) {
                recordArr = recordArr2;
            } else {
                recordArr = new Lookup(name, 28).run();
            }
            if (recordArr == null) {
                throw new UnknownHostException(str);
            }
            InetAddress[] inetAddressArr = new InetAddress[recordArr.length];
            for (int i = 0; i < recordArr.length; i++) {
                Record record = recordArr[i];
                if (recordArr[i] instanceof ARecord) {
                    inetAddressArr[i] = ((ARecord) recordArr[i]).getAddress();
                } else {
                    inetAddressArr[i] = ((AAAARecord) recordArr[i]).getAddress();
                }
            }
            return inetAddressArr;
        } catch (TextParseException e) {
            throw new UnknownHostException(str);
        }
    }

    public String getHostByAddr(byte[] bArr) throws UnknownHostException {
        Record[] run = new Lookup(ReverseMap.fromAddress(InetAddress.getByAddress(bArr)), 12).run();
        if (run != null) {
            return ((PTRRecord) run[0]).getTarget().toString();
        }
        throw new UnknownHostException();
    }
}
