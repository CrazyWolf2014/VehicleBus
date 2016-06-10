package com.cnlaunch.x431pro.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class NetWorkUtils {
    public static boolean isNetConnected(Context mContext) {
        NetworkInfo info = ((ConnectivityManager) mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    public static String domainNameResolution(String mdomain) {
        String domainIP = null;
        try {
            domainIP = InetAddress.getByName(mdomain).getHostAddress();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        return domainIP;
    }

    public static String ping(String url) {
        String str = XmlPullParser.NO_NAMESPACE;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("/system/bin/ping -c 10 " + url).getInputStream()));
            char[] buffer = new char[Flags.EXTEND];
            StringBuffer output = new StringBuffer();
            while (true) {
                int i = reader.read(buffer);
                if (i <= 0) {
                    reader.close();
                    return output.toString();
                }
                output.append(buffer, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return str;
        }
    }
}
