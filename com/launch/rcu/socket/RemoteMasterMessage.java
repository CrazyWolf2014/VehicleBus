package com.launch.rcu.socket;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.json.JSONObject;

public class RemoteMasterMessage {
    private String UseID;
    private String userName;

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUseID() {
        return this.UseID;
    }

    public void setUseID(String useID) {
        this.UseID = useID;
    }

    public RemoteMasterMessage(byte[] information) {
        try {
            JSONObject jsonData = new JSONObject(new JSONObject(new String(information, AsyncHttpResponseHandler.DEFAULT_CHARSET)).getString(DataPacketExtension.ELEMENT_NAME));
            setUserName(jsonData.getString("userName"));
            setUseID(jsonData.getString("userId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
