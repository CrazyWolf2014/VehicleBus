package com.cnlaunch.x431pro.module.base;

import android.content.Context;
import android.text.TextUtils;
import com.cnlaunch.framework.common.parse.JsonMananger;
import com.cnlaunch.framework.common.parse.SoapManager;
import com.cnlaunch.framework.common.parse.XmlMananger;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.framework.network.http.SyncHttpClient;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.cnlaunch.framework.utils.NLog;
import com.cnmobi.im.dto.Msg;
import com.thoughtworks.xstream.XStream;
import java.io.InputStream;
import org.codehaus.jackson.map.ObjectMapper;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.kxml2.kdom.Element;

public abstract class BaseManager {
    protected static final int BOTH = 3;
    protected static final int JOSN = 1;
    protected static final int XML = 2;
    protected SoapSerializationEnvelope envelope;
    protected SyncHttpClient httpManager;
    protected SyncHttpTransportSE httpTransport;
    protected ObjectMapper jsonMapper;
    protected Context mContext;
    private final String tag;
    protected XStream xmlMapper;

    public BaseManager(Context context) {
        this(context, BOTH);
    }

    public BaseManager(Context context, int parseType) {
        this.tag = BaseManager.class.getSimpleName();
        this.mContext = context;
        this.httpManager = SyncHttpClient.getInstance(context);
        switch (parseType) {
            case JOSN /*1*/:
                this.jsonMapper = JsonMananger.getInstance().getJsonMapper();
            case XML /*2*/:
                this.xmlMapper = XmlMananger.getInstance().getXmlMapper();
            default:
                this.jsonMapper = JsonMananger.getInstance().getJsonMapper();
                this.xmlMapper = XmlMananger.getInstance().getXmlMapper();
        }
    }

    public SyncHttpTransportSE getHttpTransport(String url) {
        String str = this.tag;
        Object[] objArr = new Object[JOSN];
        objArr[0] = "url: " + url;
        NLog.m917e(str, objArr);
        return getHttpTransport(url.split("\\?")[0], SyncHttpTransportSE.timeout);
    }

    public SyncHttpTransportSE getHttpTransport(String url, int timeout) {
        this.httpTransport = new SyncHttpTransportSE(url, timeout);
        if (NLog.isDebug()) {
            this.httpTransport.debug = true;
        }
        return this.httpTransport;
    }

    public SoapSerializationEnvelope getSoapSerializationEnvelope(Element[] elements, SoapObject requestObj) {
        this.envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        this.envelope.headerOut = elements;
        this.envelope.bodyOut = requestObj;
        this.envelope.setOutputSoapObject(requestObj);
        String str = this.tag;
        Object[] objArr = new Object[JOSN];
        objArr[0] = this.httpTransport.requestDump;
        NLog.m917e(str, objArr);
        return this.envelope;
    }

    public <T> T xmlToBean(String xml, Class<T> cls) {
        return XmlMananger.getInstance().xmlToBean(xml, (Class) cls);
    }

    public <T> T xmlToBean(InputStream xml, Class<T> cls) {
        return XmlMananger.getInstance().xmlToBean(xml, (Class) cls);
    }

    public String beanToXml(Object obj) {
        return XmlMananger.getInstance().beanToXml(obj);
    }

    public <T> T jsonToBean(String json, Class<T> cls) throws HttpException {
        return JsonMananger.getInstance().jsonToBean(json, cls);
    }

    public String BeanTojson(Object obj) throws HttpException {
        return JsonMananger.getInstance().beanToJson(obj);
    }

    public <T> T soapToBean(Class<T> clazz) throws SoapFault, HttpException {
        return soapToBean(clazz, new String[0]);
    }

    public <T> T soapToBean(Class<T> clazz, String... listNodeNames) throws SoapFault, HttpException {
        String str = this.tag;
        Object[] objArr = new Object[JOSN];
        objArr[0] = "responseDump: " + this.httpTransport.responseDump;
        NLog.m917e(str, objArr);
        SoapObject soapObject = new SoapObject();
        Element[] headerIn = this.envelope.headerIn;
        if (headerIn != null && headerIn.length > 0) {
            String code = String.valueOf(((Element) headerIn[0].getChild(0)).getChild(0));
            String msg = String.valueOf(((Element) headerIn[0].getChild(JOSN)).getChild(0));
            PropertyInfo property = new PropertyInfo();
            property.setType(SoapPrimitive.class);
            property.setName("code");
            property.setValue(code);
            soapObject.addProperty(property);
            property = new PropertyInfo();
            property.setType(SoapPrimitive.class);
            property.setName(Msg.MSG_CONTENT);
            property.setValue(msg);
            soapObject.addProperty(property);
        } else if (!(this.envelope == null || this.envelope.getResponse() == null)) {
            soapObject = (SoapObject) this.envelope.getResponse();
        }
        String json = SoapManager.getInstance().soapToJson(soapObject, clazz, listNodeNames);
        str = this.tag;
        objArr = new Object[JOSN];
        objArr[0] = "json: " + json;
        NLog.m917e(str, objArr);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return jsonToBean(json, clazz);
    }
}
