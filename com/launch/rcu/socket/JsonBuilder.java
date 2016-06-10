package com.launch.rcu.socket;

import com.cnlaunch.framework.common.Constants;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonBuilder {
    public static byte[] jsonCreateData(String identity, String deviceSN, String userId, String appId, String sign, String lan, String ver) {
        JSONObject json = new JSONObject();
        Object jsondata = new JSONObject();
        try {
            jsondata.put("identity", Integer.parseInt(identity));
            jsondata.put("deviceSN", (Object) deviceSN);
            jsondata.put(Constants.SIGN, (Object) sign);
            jsondata.put("userId", (Object) userId);
            jsondata.put("appId", (Object) appId);
            jsondata.put("lan", (Object) lan);
            jsondata.put(Constants.VER, (Object) ver);
            json.put("protocolVer", 1);
            json.put(DataPacketExtension.ELEMENT_NAME, jsondata);
            return json.toString().getBytes();
        } catch (JSONException e) {
            return null;
        }
    }
}
