package com.cnlaunch.x431pro.module.setting.action;

import android.content.Context;
import com.cnlaunch.framework.common.KeyConstant;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.module.base.BaseAction;
import com.cnlaunch.x431pro.module.setting.model.UploadDiagnosticLogResponse;
import com.cnlaunch.x431pro.module.setting.model.UploadServerCheckLogResponse;
import java.util.HashMap;
import java.util.Map;

public class SettingAction extends BaseAction {
    public SettingAction(Context mContext) {
        super(mContext);
    }

    public UploadServerCheckLogResponse sendServerCheckLog(String serialNo, String logFilePath) throws HttpException {
        String requestUrl = getUrlByKey(KeyConstant.send_Server_Check_Log);
        Map<String, String> params = new HashMap();
        params.put(Constants.serialNo, serialNo);
        params.put(com.cnlaunch.framework.common.Constants.SIGN, getSign(logFilePath));
        return (UploadServerCheckLogResponse) jsonToBean(uploadFile(requestUrl, params, logFilePath), UploadServerCheckLogResponse.class);
    }

    public UploadDiagnosticLogResponse sendDiagnosticLog(String serialNo, String vehicleType, String remark, String logFilePath) throws HttpException {
        String requestUrl = getUrlByKey(KeyConstant.sendDiagnosticLog);
        Map<String, String> params = new HashMap();
        params.put(Constants.serialNo, serialNo);
        params.put(com.cnlaunch.framework.common.Constants.SIGN, getSign(logFilePath));
        params.put("vehicleType", vehicleType);
        params.put("remark", remark);
        return (UploadDiagnosticLogResponse) jsonToBean(uploadFile(requestUrl, params, logFilePath), UploadDiagnosticLogResponse.class);
    }
}
