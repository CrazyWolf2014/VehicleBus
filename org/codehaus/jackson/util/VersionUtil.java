package org.codehaus.jackson.util;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import org.codehaus.jackson.Version;

public class VersionUtil {
    public static final String VERSION_FILE = "VERSION.txt";
    private static final Pattern VERSION_SEPARATOR;

    static {
        VERSION_SEPARATOR = Pattern.compile("[-_./;:]");
    }

    public static Version versionFor(Class<?> cls) {
        InputStream in;
        Version version = null;
        try {
            in = cls.getResourceAsStream(VERSION_FILE);
            if (in != null) {
                version = parseVersion(new BufferedReader(new InputStreamReader(in, AsyncHttpResponseHandler.DEFAULT_CHARSET)).readLine());
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        } catch (IOException e3) {
        } catch (Throwable th) {
            in.close();
        }
        return version == null ? Version.unknownVersion() : version;
    }

    public static Version parseVersion(String versionStr) {
        int patch = 0;
        String snapshot = null;
        if (versionStr == null) {
            return null;
        }
        versionStr = versionStr.trim();
        if (versionStr.length() == 0) {
            return null;
        }
        String[] parts = VERSION_SEPARATOR.split(versionStr);
        if (parts.length < 2) {
            return null;
        }
        int major = parseVersionPart(parts[0]);
        int minor = parseVersionPart(parts[1]);
        if (parts.length > 2) {
            patch = parseVersionPart(parts[2]);
        }
        if (parts.length > 3) {
            snapshot = parts[3];
        }
        return new Version(major, minor, patch, snapshot);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static int parseVersionPart(java.lang.String r6) {
        /*
        r6 = r6.toString();
        r2 = r6.length();
        r3 = 0;
        r1 = 0;
    L_0x000a:
        if (r1 >= r2) goto L_0x0018;
    L_0x000c:
        r0 = r6.charAt(r1);
        r4 = 57;
        if (r0 > r4) goto L_0x0018;
    L_0x0014:
        r4 = 48;
        if (r0 >= r4) goto L_0x0019;
    L_0x0018:
        return r3;
    L_0x0019:
        r4 = r3 * 10;
        r5 = r0 + -48;
        r3 = r4 + r5;
        r1 = r1 + 1;
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.util.VersionUtil.parseVersionPart(java.lang.String):int");
    }
}
