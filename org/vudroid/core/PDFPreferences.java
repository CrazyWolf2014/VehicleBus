package org.vudroid.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import com.ifoer.mine.Contact;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import org.xmlpull.v1.XmlPullParser;

public class PDFPreferences {
    private static final String FONT_LIB_PATH = "fontlibpath";
    private static final String FULL_SCREEN = "FullScreen";
    private SharedPreferences sharedPreferences;

    public PDFPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences("ViewerPreferences", 0);
    }

    public void setFullScreen(boolean fullscreen) {
        Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(FULL_SCREEN, fullscreen);
        editor.commit();
    }

    public boolean isFullScreen() {
        return this.sharedPreferences.getBoolean(FULL_SCREEN, false);
    }

    public void setFontPath(String fontPath) {
        Editor editor = this.sharedPreferences.edit();
        editor.putString(FONT_LIB_PATH, fontPath);
        editor.commit();
    }

    public String getFontPath() {
        return this.sharedPreferences.getString(FONT_LIB_PATH, XmlPullParser.NO_NAMESPACE);
    }

    public void addRecent(Uri uri) {
        Editor editor = this.sharedPreferences.edit();
        editor.putString("recent:" + uri.toString(), uri.toString() + SpecilApiUtil.LINE_SEP + System.currentTimeMillis());
        editor.commit();
    }

    public List<Uri> getRecent() {
        TreeMap<Long, Uri> treeMap = new TreeMap();
        for (String key : this.sharedPreferences.getAll().keySet()) {
            if (key.startsWith("recent")) {
                String str;
                String[] uriThenDate = this.sharedPreferences.getString(key, null).split(SpecilApiUtil.LINE_SEP);
                if (uriThenDate.length > 1) {
                    str = uriThenDate[1];
                } else {
                    str = Contact.RELATION_ASK;
                }
                treeMap.put(Long.valueOf(Long.parseLong(str)), Uri.parse(uriThenDate[0]));
            }
        }
        ArrayList<Uri> list = new ArrayList(treeMap.values());
        Collections.reverse(list);
        return list;
    }
}
