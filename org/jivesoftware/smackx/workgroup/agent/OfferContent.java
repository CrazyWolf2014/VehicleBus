package org.jivesoftware.smackx.workgroup.agent;

public abstract class OfferContent {
    abstract boolean isInvitation();

    abstract boolean isTransfer();

    abstract boolean isUserRequest();
}
