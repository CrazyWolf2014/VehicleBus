package com.cnlaunch.x431pro.module.base;

import android.text.TextUtils;
import com.cnlaunch.framework.utils.NLog;
import org.ksoap2.serialization.SoapObject;

public class SoapObjectParams extends SoapObject {
    private final String tag;

    public SoapObjectParams() {
        this.tag = SoapObjectParams.class.getSimpleName();
    }

    public SoapObjectParams(String namespace, String name) {
        super(namespace, name);
        this.tag = SoapObjectParams.class.getSimpleName();
    }

    public SoapObject addProperty(String name, Object value) {
        if (value != null && !TextUtils.isEmpty(String.valueOf(value))) {
            return super.addProperty(name, value);
        }
        NLog.m920i(this.tag, "addProperty name is " + name + " and value is " + value);
        return this;
    }
}
