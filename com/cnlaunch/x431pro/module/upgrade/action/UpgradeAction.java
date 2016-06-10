package com.cnlaunch.x431pro.module.upgrade.action;

import android.content.Context;
import com.cnlaunch.framework.common.KeyConstant;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.module.base.BaseAction;
import com.cnlaunch.x431pro.module.upgrade.model.HistoryDiagSoftsResponse;
import com.cnlaunch.x431pro.module.upgrade.model.LatestDiagSoftsResponse;
import com.cnlaunch.x431pro.module.upgrade.model.LatestPublicSoftsResponse;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xmlpull.v1.XmlPullParser;

public class UpgradeAction extends BaseAction {
    public UpgradeAction(Context mContext) {
        super(mContext);
    }

    public LatestDiagSoftsResponse queryLatestDiagSofts(String cc, String serialNo, String lanId, String defaultLanId) throws HttpException {
        String serviceUrl = getUrlByKey(KeyConstant.x431paddiagsoftservice_all);
        this.requestObj = getSoapObjectParams("queryLatestDiagSofts");
        this.requestObj.addProperty(Constants.serialNo, serialNo);
        this.requestObj.addProperty("lanId", lanId);
        this.requestObj.addProperty("defaultLanId", defaultLanId);
        this.requestObj.addProperty(MultipleAddresses.CC, cc);
        try {
            this.httpTransport = getHttpTransport(serviceUrl);
            this.envelope = getSoapSerializationEnvelope(createHead(this.requestObj), this.requestObj);
            this.httpTransport.call(XmlPullParser.NO_NAMESPACE, this.envelope);
            if (this.envelope == null) {
                return null;
            }
            return (LatestDiagSoftsResponse) soapToBean(LatestDiagSoftsResponse.class, "x431PadSoftList");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LatestPublicSoftsResponse queryLatestPublicSofts(String serialNo, String lanId, String defaultLanId) throws HttpException {
        String serviceUrl = getUrlByKey(KeyConstant.x431padpublicsoftservice_all);
        this.requestObj = getSoapObjectParams("queryLatestPublicSofts");
        this.requestObj.addProperty(Constants.serialNo, serialNo);
        this.requestObj.addProperty("lanId", lanId);
        this.requestObj.addProperty("defaultLanId", defaultLanId);
        try {
            this.httpTransport = getHttpTransport(serviceUrl);
            this.envelope = getSoapSerializationEnvelope(createHead(this.requestObj), this.requestObj);
            this.httpTransport.call(XmlPullParser.NO_NAMESPACE, this.envelope);
            if (this.envelope == null) {
                return null;
            }
            return (LatestPublicSoftsResponse) soapToBean(LatestPublicSoftsResponse.class, "x431PadSoftList");
        } catch (Throwable e) {
            throw new HttpException(e);
        } catch (Throwable e2) {
            throw new HttpException(e2);
        }
    }

    public HistoryDiagSoftsResponse queryHistoryDiagSofts(String serialNo, String softId, String lanId, String defaultLanId) throws HttpException {
        String serviceUrl = getUrlByKey(KeyConstant.x431paddiagsoftservice_all);
        this.requestObj = getSoapObjectParams("queryHistoryDiagSofts");
        this.requestObj.addProperty(Constants.serialNo, serialNo);
        this.requestObj.addProperty("softId", softId);
        this.requestObj.addProperty("lanId", lanId);
        this.requestObj.addProperty("defaultLanId", defaultLanId);
        try {
            this.httpTransport = getHttpTransport(serviceUrl);
            this.envelope = getSoapSerializationEnvelope(createHead(this.requestObj), this.requestObj);
            this.httpTransport.call(XmlPullParser.NO_NAMESPACE, this.envelope);
            if (this.envelope == null) {
                return null;
            }
            return (HistoryDiagSoftsResponse) soapToBean(HistoryDiagSoftsResponse.class, "x431PadSoftList");
        } catch (Throwable e) {
            throw new HttpException(e);
        } catch (Throwable e2) {
            throw new HttpException(e2);
        }
    }
}
