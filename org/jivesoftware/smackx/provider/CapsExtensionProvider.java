package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.provider.PacketExtensionProvider;

public class CapsExtensionProvider implements PacketExtensionProvider {
    private static final int MAX_DEPTH = 10;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.jivesoftware.smack.packet.PacketExtension parseExtension(org.xmlpull.v1.XmlPullParser r8) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, org.jivesoftware.smack.XMPPException {
        /*
        r7 = this;
        r4 = 0;
        r0 = 0;
        r3 = r0;
        r1 = r4;
        r2 = r4;
        r0 = r4;
    L_0x0006:
        r5 = r8.getEventType();
        r6 = 2;
        if (r5 != r6) goto L_0x002b;
    L_0x000d:
        r5 = r8.getName();
        r6 = "c";
        r5 = r5.equalsIgnoreCase(r6);
        if (r5 == 0) goto L_0x002b;
    L_0x0019:
        r0 = "hash";
        r2 = r8.getAttributeValue(r4, r0);
        r0 = "ver";
        r1 = r8.getAttributeValue(r4, r0);
        r0 = "node";
        r0 = r8.getAttributeValue(r4, r0);
    L_0x002b:
        r5 = r8.getEventType();
        r6 = 3;
        if (r5 != r6) goto L_0x004a;
    L_0x0032:
        r5 = r8.getName();
        r6 = "c";
        r5 = r5.equalsIgnoreCase(r6);
        if (r5 == 0) goto L_0x004a;
    L_0x003e:
        if (r2 == 0) goto L_0x005c;
    L_0x0040:
        if (r1 == 0) goto L_0x005c;
    L_0x0042:
        if (r0 == 0) goto L_0x005c;
    L_0x0044:
        r3 = new org.jivesoftware.smackx.entitycaps.packet.CapsExtension;
        r3.<init>(r0, r1, r2);
        return r3;
    L_0x004a:
        r8.next();
        r5 = 10;
        if (r3 >= r5) goto L_0x0054;
    L_0x0051:
        r3 = r3 + 1;
        goto L_0x0006;
    L_0x0054:
        r0 = new org.jivesoftware.smack.XMPPException;
        r1 = "Malformed caps element";
        r0.<init>(r1);
        throw r0;
    L_0x005c:
        r0 = new org.jivesoftware.smack.XMPPException;
        r1 = "Caps elment with missing attributes";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.provider.CapsExtensionProvider.parseExtension(org.xmlpull.v1.XmlPullParser):org.jivesoftware.smack.packet.PacketExtension");
    }
}
