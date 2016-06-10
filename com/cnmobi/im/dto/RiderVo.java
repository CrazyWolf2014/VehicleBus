package com.cnmobi.im.dto;

import android.widget.ImageView;

public class RiderVo {
    public static final String TYPE_BOTH = "both";
    public static final String TYPE_FROM = "from";
    public static final String TYPE_NONE = "none";
    public static final String TYPE_TO = "to";
    public String jId;
    public int logoResourceId;
    public String logoUri;
    public ImageView logoView;
    public String name;
    public int online;
    public String ownerJid;
    public String signature;
    public String type;
}
