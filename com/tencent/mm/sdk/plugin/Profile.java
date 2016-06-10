package com.tencent.mm.sdk.plugin;

import android.net.Uri;
import com.tencent.mm.sdk.storage.IAutoDBItem.MAutoDBInfo;

public class Profile extends BaseProfile {
    public static final Uri CONTENT_URI;
    protected static MAutoDBInfo bO;
    public static String[] columns;

    static {
        CONTENT_URI = Uri.parse("content://com.tencent.mm.sdk.plugin.provider/profile");
        columns = new String[]{BaseProfile.COL_USERNAME, BaseProfile.COL_BINDQQ, BaseProfile.COL_BINDMOBILE, BaseProfile.COL_BINDEMAIL, BaseProfile.COL_ALIAS, BaseProfile.COL_NICKNAME, BaseProfile.COL_SIGNATURE, BaseProfile.COL_PROVINCE, BaseProfile.COL_CITY, BaseProfile.COL_WEIBO, BaseProfile.COL_AVATAR};
        bO = BaseProfile.initAutoDBInfo(Profile.class);
    }

    protected MAutoDBInfo getDBInfo() {
        return bO;
    }
}
