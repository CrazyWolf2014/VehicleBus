package com.ifoer.entity;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class SupportConnectorId implements Serializable {
    private static final long serialVersionUID = 1;
    private String ConnectorContent;
    private String ConnectorId;
    private String ConnectorIdContent;

    public SupportConnectorId() {
        this.ConnectorContent = XmlPullParser.NO_NAMESPACE;
        this.ConnectorIdContent = XmlPullParser.NO_NAMESPACE;
    }

    public String getConnectorId() {
        return this.ConnectorId;
    }

    public void setConnectorId(String ConnectorId) {
        this.ConnectorId = ConnectorId;
    }

    public String getConnectorIdContent() {
        return this.ConnectorIdContent;
    }

    public void setConnectorIdContent(String ConnectorIdContent) {
        this.ConnectorIdContent = ConnectorIdContent;
    }
}
