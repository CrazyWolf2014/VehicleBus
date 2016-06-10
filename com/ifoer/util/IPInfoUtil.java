package com.ifoer.util;

import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.ifoer.mine.Contact;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IPInfoUtil {
    public String getNetIp() {
        Exception e;
        try {
            URL infoUrl = new URL("http://20140507.ip138.com/ic.asp");
            URL url;
            try {
                HttpURLConnection httpConnection = (HttpURLConnection) infoUrl.openConnection();
                if (httpConnection.getResponseCode() == AsyncTaskManager.REQUEST_SUCCESS_CODE) {
                    InputStream inStream = httpConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "gb2312"));
                    StringBuilder strber = new StringBuilder();
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            inStream.close();
                            int start = strber.indexOf("[");
                            url = infoUrl;
                            return strber.substring(start + 1, strber.indexOf("]", start + 1));
                        }
                        strber.append(new StringBuilder(String.valueOf(line)).append(SpecilApiUtil.LINE_SEP).toString());
                    }
                } else {
                    return null;
                }
            } catch (Exception e2) {
                e = e2;
                url = infoUrl;
                e.printStackTrace();
                return null;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return null;
        }
    }

    public String ipToArea(String ip) {
        return Contact.RELATION_BACKNAME;
    }
}
