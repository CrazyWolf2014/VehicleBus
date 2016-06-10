package org.jivesoftware.smackx.workgroup.agent;

public class UserRequest extends OfferContent {
    private static UserRequest instance;

    static {
        instance = new UserRequest();
    }

    public static OfferContent getInstance() {
        return instance;
    }

    boolean isUserRequest() {
        return true;
    }

    boolean isInvitation() {
        return false;
    }

    boolean isTransfer() {
        return false;
    }
}
