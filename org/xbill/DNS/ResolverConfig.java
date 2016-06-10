package org.xbill.DNS;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class ResolverConfig {
    private static ResolverConfig currentConfig;
    private int ndots;
    private Name[] searchlist;
    private String[] servers;

    static {
        refresh();
    }

    public ResolverConfig() {
        this.servers = null;
        this.searchlist = null;
        this.ndots = -1;
        if (!findProperty() && !findSunJVM()) {
            if (this.servers == null || this.searchlist == null) {
                String property = System.getProperty("os.name");
                String property2 = System.getProperty("java.vendor");
                if (property.indexOf("Windows") != -1) {
                    if (property.indexOf("95") == -1 && property.indexOf("98") == -1 && property.indexOf("ME") == -1) {
                        findNT();
                    } else {
                        find95();
                    }
                } else if (property.indexOf("NetWare") != -1) {
                    findNetware();
                } else if (property2.indexOf("Android") != -1) {
                    findAndroid();
                } else {
                    findUnix();
                }
            }
        }
    }

    private void addServer(String str, List list) {
        if (!list.contains(str)) {
            if (Options.check("verbose")) {
                System.out.println("adding server " + str);
            }
            list.add(str);
        }
    }

    private void addSearch(String str, List list) {
        if (Options.check("verbose")) {
            System.out.println("adding search " + str);
        }
        try {
            Name fromString = Name.fromString(str, Name.root);
            if (!list.contains(fromString)) {
                list.add(fromString);
            }
        } catch (TextParseException e) {
        }
    }

    private int parseNdots(String str) {
        String substring = str.substring(6);
        try {
            int parseInt = Integer.parseInt(substring);
            if (parseInt >= 0) {
                if (!Options.check("verbose")) {
                    return parseInt;
                }
                System.out.println("setting ndots " + substring);
                return parseInt;
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    private void configureFromLists(List list, List list2) {
        if (this.servers == null && list.size() > 0) {
            this.servers = (String[]) list.toArray(new String[0]);
        }
        if (this.searchlist == null && list2.size() > 0) {
            this.searchlist = (Name[]) list2.toArray(new Name[0]);
        }
    }

    private void configureNdots(int i) {
        if (this.ndots < 0 && i > 0) {
            this.ndots = i;
        }
    }

    private boolean findProperty() {
        StringTokenizer stringTokenizer;
        List arrayList = new ArrayList(0);
        List arrayList2 = new ArrayList(0);
        String property = System.getProperty("dns.server");
        if (property != null) {
            stringTokenizer = new StringTokenizer(property, ",");
            while (stringTokenizer.hasMoreTokens()) {
                addServer(stringTokenizer.nextToken(), arrayList);
            }
        }
        property = System.getProperty("dns.search");
        if (property != null) {
            stringTokenizer = new StringTokenizer(property, ",");
            while (stringTokenizer.hasMoreTokens()) {
                addSearch(stringTokenizer.nextToken(), arrayList2);
            }
        }
        configureFromLists(arrayList, arrayList2);
        if (this.servers == null || this.searchlist == null) {
            return false;
        }
        return true;
    }

    private boolean findSunJVM() {
        List arrayList = new ArrayList(0);
        List arrayList2 = new ArrayList(0);
        try {
            Class[] clsArr = new Class[0];
            Object[] objArr = new Object[0];
            Class cls = Class.forName("sun.net.dns.ResolverConfiguration");
            Object invoke = cls.getDeclaredMethod("open", clsArr).invoke(null, objArr);
            List<String> list = (List) cls.getMethod("nameservers", clsArr).invoke(invoke, objArr);
            List<String> list2 = (List) cls.getMethod("searchlist", clsArr).invoke(invoke, objArr);
            if (list.size() == 0) {
                return false;
            }
            if (list.size() > 0) {
                for (String addServer : list) {
                    addServer(addServer, arrayList);
                }
            }
            if (list2.size() > 0) {
                for (String addServer2 : list2) {
                    addSearch(addServer2, arrayList2);
                }
            }
            configureFromLists(arrayList, arrayList2);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void findResolvConf(String str) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(str)));
            List arrayList = new ArrayList(0);
            List arrayList2 = new ArrayList(0);
            int i = -1;
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                } else if (readLine.startsWith("nameserver")) {
                    r5 = new StringTokenizer(readLine);
                    r5.nextToken();
                    addServer(r5.nextToken(), arrayList);
                } else {
                    try {
                        if (readLine.startsWith("domain")) {
                            r5 = new StringTokenizer(readLine);
                            r5.nextToken();
                            if (r5.hasMoreTokens() && arrayList2.isEmpty()) {
                                addSearch(r5.nextToken(), arrayList2);
                            }
                        } else if (readLine.startsWith("search")) {
                            if (!arrayList2.isEmpty()) {
                                arrayList2.clear();
                            }
                            r5 = new StringTokenizer(readLine);
                            r5.nextToken();
                            while (r5.hasMoreTokens()) {
                                addSearch(r5.nextToken(), arrayList2);
                            }
                        } else if (readLine.startsWith("options")) {
                            r5 = new StringTokenizer(readLine);
                            r5.nextToken();
                            while (r5.hasMoreTokens()) {
                                readLine = r5.nextToken();
                                if (readLine.startsWith("ndots:")) {
                                    i = parseNdots(readLine);
                                }
                            }
                        }
                    } catch (IOException e) {
                    }
                }
            }
            bufferedReader.close();
            configureFromLists(arrayList, arrayList2);
            configureNdots(i);
        } catch (FileNotFoundException e2) {
        }
    }

    private void findUnix() {
        findResolvConf("/etc/resolv.conf");
    }

    private void findNetware() {
        findResolvConf("sys:/etc/resolv.cfg");
    }

    private void findWin(InputStream inputStream, Locale locale) {
        ResourceBundle bundle;
        String str = ResolverConfig.class.getPackage().getName() + ".windows.DNSServer";
        if (locale != null) {
            bundle = ResourceBundle.getBundle(str, locale);
        } else {
            bundle = ResourceBundle.getBundle(str);
        }
        String string = bundle.getString("host_name");
        String string2 = bundle.getString("primary_dns_suffix");
        String string3 = bundle.getString("dns_suffix");
        String string4 = bundle.getString("dns_servers");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        Object obj = null;
        Object obj2 = null;
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(readLine);
                if (stringTokenizer.hasMoreTokens()) {
                    String nextToken = stringTokenizer.nextToken();
                    if (readLine.indexOf(":") != -1) {
                        obj = null;
                        obj2 = null;
                    }
                    if (readLine.indexOf(string) != -1) {
                        while (stringTokenizer.hasMoreTokens()) {
                            nextToken = stringTokenizer.nextToken();
                        }
                        try {
                            try {
                                if (Name.fromString(nextToken, null).labels() != 1) {
                                    addSearch(nextToken, arrayList2);
                                }
                            } catch (IOException e) {
                                return;
                            }
                        } catch (TextParseException e2) {
                        }
                    } else if (readLine.indexOf(string2) != -1) {
                        while (stringTokenizer.hasMoreTokens()) {
                            nextToken = stringTokenizer.nextToken();
                        }
                        if (!nextToken.equals(":")) {
                            addSearch(nextToken, arrayList2);
                            obj2 = 1;
                        }
                    } else if (obj2 != null || readLine.indexOf(string3) != -1) {
                        while (stringTokenizer.hasMoreTokens()) {
                            nextToken = stringTokenizer.nextToken();
                        }
                        if (!nextToken.equals(":")) {
                            addSearch(nextToken, arrayList2);
                            obj2 = 1;
                        }
                    } else if (obj != null || readLine.indexOf(string4) != -1) {
                        while (stringTokenizer.hasMoreTokens()) {
                            nextToken = stringTokenizer.nextToken();
                        }
                        if (!nextToken.equals(":")) {
                            addServer(nextToken, arrayList);
                            obj = 1;
                        }
                    }
                } else {
                    obj = null;
                    obj2 = null;
                }
            } else {
                configureFromLists(arrayList, arrayList2);
                return;
            }
        }
    }

    private void findWin(InputStream inputStream) {
        int intValue = Integer.getInteger("org.xbill.DNS.windows.parse.buffer", Flags.FLAG2).intValue();
        InputStream bufferedInputStream = new BufferedInputStream(inputStream, intValue);
        bufferedInputStream.mark(intValue);
        findWin(bufferedInputStream, null);
        if (this.servers == null) {
            try {
                bufferedInputStream.reset();
                findWin(bufferedInputStream, new Locale(XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE));
            } catch (IOException e) {
            }
        }
    }

    private void find95() {
        String str = "winipcfg.out";
        try {
            Runtime.getRuntime().exec("winipcfg /all /batch " + str).waitFor();
            findWin(new FileInputStream(new File(str)));
            new File(str).delete();
        } catch (Exception e) {
        }
    }

    private void findNT() {
        try {
            Process exec = Runtime.getRuntime().exec("ipconfig /all");
            findWin(exec.getInputStream());
            exec.destroy();
        } catch (Exception e) {
        }
    }

    private void findAndroid() {
        String str = "^\\d+(\\.\\d+){3}$";
        String str2 = "^[0-9a-f]+(:[0-9a-f]*)+:[0-9a-f]+$";
        try {
            List arrayList = new ArrayList();
            List arrayList2 = new ArrayList();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop").getInputStream()));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    StringTokenizer stringTokenizer = new StringTokenizer(readLine, ":");
                    if (stringTokenizer.nextToken().indexOf("net.dns") > -1) {
                        readLine = stringTokenizer.nextToken().replaceAll("[ \\[\\]]", XmlPullParser.NO_NAMESPACE);
                        if ((readLine.matches(str) || readLine.matches(str2)) && !arrayList.contains(readLine)) {
                            arrayList.add(readLine);
                        }
                    }
                } else {
                    configureFromLists(arrayList, arrayList2);
                    return;
                }
            }
        } catch (Exception e) {
        }
    }

    public String[] servers() {
        return this.servers;
    }

    public String server() {
        if (this.servers == null) {
            return null;
        }
        return this.servers[0];
    }

    public Name[] searchPath() {
        return this.searchlist;
    }

    public int ndots() {
        if (this.ndots < 0) {
            return 1;
        }
        return this.ndots;
    }

    public static synchronized ResolverConfig getCurrentConfig() {
        ResolverConfig resolverConfig;
        synchronized (ResolverConfig.class) {
            resolverConfig = currentConfig;
        }
        return resolverConfig;
    }

    public static void refresh() {
        ResolverConfig resolverConfig = new ResolverConfig();
        synchronized (ResolverConfig.class) {
            currentConfig = resolverConfig;
        }
    }
}
