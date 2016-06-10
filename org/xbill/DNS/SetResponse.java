package org.xbill.DNS;

import java.util.ArrayList;
import java.util.List;

public class SetResponse {
    static final int CNAME = 4;
    static final int DELEGATION = 3;
    static final int DNAME = 5;
    static final int NXDOMAIN = 1;
    static final int NXRRSET = 2;
    static final int SUCCESSFUL = 6;
    static final int UNKNOWN = 0;
    private static final SetResponse nxdomain;
    private static final SetResponse nxrrset;
    private static final SetResponse unknown;
    private Object data;
    private int type;

    static {
        unknown = new SetResponse(0);
        nxdomain = new SetResponse(NXDOMAIN);
        nxrrset = new SetResponse(NXRRSET);
    }

    private SetResponse() {
    }

    SetResponse(int i, RRset rRset) {
        if (i < 0 || i > SUCCESSFUL) {
            throw new IllegalArgumentException("invalid type");
        }
        this.type = i;
        this.data = rRset;
    }

    SetResponse(int i) {
        if (i < 0 || i > SUCCESSFUL) {
            throw new IllegalArgumentException("invalid type");
        }
        this.type = i;
        this.data = null;
    }

    static SetResponse ofType(int i) {
        switch (i) {
            case KEYRecord.OWNER_USER /*0*/:
                return unknown;
            case NXDOMAIN /*1*/:
                return nxdomain;
            case NXRRSET /*2*/:
                return nxrrset;
            case DELEGATION /*3*/:
            case CNAME /*4*/:
            case DNAME /*5*/:
            case SUCCESSFUL /*6*/:
                SetResponse setResponse = new SetResponse();
                setResponse.type = i;
                setResponse.data = null;
                return setResponse;
            default:
                throw new IllegalArgumentException("invalid type");
        }
    }

    void addRRset(RRset rRset) {
        if (this.data == null) {
            this.data = new ArrayList();
        }
        ((List) this.data).add(rRset);
    }

    public boolean isUnknown() {
        return this.type == 0;
    }

    public boolean isNXDOMAIN() {
        return this.type == NXDOMAIN;
    }

    public boolean isNXRRSET() {
        return this.type == NXRRSET;
    }

    public boolean isDelegation() {
        return this.type == DELEGATION;
    }

    public boolean isCNAME() {
        return this.type == CNAME;
    }

    public boolean isDNAME() {
        return this.type == DNAME;
    }

    public boolean isSuccessful() {
        return this.type == SUCCESSFUL;
    }

    public RRset[] answers() {
        if (this.type != SUCCESSFUL) {
            return null;
        }
        List list = (List) this.data;
        return (RRset[]) list.toArray(new RRset[list.size()]);
    }

    public CNAMERecord getCNAME() {
        return (CNAMERecord) ((RRset) this.data).first();
    }

    public DNAMERecord getDNAME() {
        return (DNAMERecord) ((RRset) this.data).first();
    }

    public RRset getNS() {
        return (RRset) this.data;
    }

    public String toString() {
        switch (this.type) {
            case KEYRecord.OWNER_USER /*0*/:
                return "unknown";
            case NXDOMAIN /*1*/:
                return "NXDOMAIN";
            case NXRRSET /*2*/:
                return "NXRRSET";
            case DELEGATION /*3*/:
                return "delegation: " + this.data;
            case CNAME /*4*/:
                return "CNAME: " + this.data;
            case DNAME /*5*/:
                return "DNAME: " + this.data;
            case SUCCESSFUL /*6*/:
                return "successful";
            default:
                throw new IllegalStateException();
        }
    }
}
