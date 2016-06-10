package org.vudroid.core.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import com.cnmobi.im.dto.MessageVo;

public class PathFromUri {
    public static String retrieve(ContentResolver resolver, Uri uri) {
        if (uri.getScheme().equals(MessageVo.TYPE_FILE)) {
            return uri.getPath();
        }
        Cursor cursor = resolver.query(uri, new String[]{"_data"}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        throw new RuntimeException("Can't retrieve path from uri: " + uri.toString());
    }
}
