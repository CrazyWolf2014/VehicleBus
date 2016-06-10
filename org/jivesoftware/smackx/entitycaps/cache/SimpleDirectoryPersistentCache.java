package org.jivesoftware.smackx.entitycaps.cache;

import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.Base64Encoder;
import org.jivesoftware.smack.util.StringEncoder;
import org.jivesoftware.smackx.entitycaps.EntityCapsManager;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class SimpleDirectoryPersistentCache implements EntityCapsPersistentCache {
    private File cacheDir;
    private StringEncoder stringEncoder;

    public SimpleDirectoryPersistentCache(File file) {
        this(file, Base64Encoder.getInstance());
    }

    public SimpleDirectoryPersistentCache(File file, StringEncoder stringEncoder) {
        if (!file.exists()) {
            throw new IllegalStateException("Cache directory \"" + file + "\" does not exist");
        } else if (file.isDirectory()) {
            this.cacheDir = file;
            this.stringEncoder = stringEncoder;
        } else {
            throw new IllegalStateException("Cache directory \"" + file + "\" is not a directory");
        }
    }

    public void addDiscoverInfoByNodePersistent(String str, DiscoverInfo discoverInfo) {
        File file = new File(this.cacheDir, this.stringEncoder.encode(str));
        try {
            if (file.createNewFile()) {
                writeInfoToFile(file, discoverInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void replay() throws IOException {
        for (File file : this.cacheDir.listFiles()) {
            String decode = this.stringEncoder.decode(file.getName());
            DiscoverInfo restoreInfoFromFile = restoreInfoFromFile(file);
            if (restoreInfoFromFile != null) {
                EntityCapsManager.addDiscoverInfoByNode(decode, restoreInfoFromFile);
            }
        }
    }

    public void emptyCache() {
        for (File delete : this.cacheDir.listFiles()) {
            delete.delete();
        }
    }

    private static void writeInfoToFile(File file, DiscoverInfo discoverInfo) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
        try {
            dataOutputStream.writeUTF(discoverInfo.toXML());
        } finally {
            dataOutputStream.close();
        }
    }

    private static DiscoverInfo restoreInfoFromFile(File file) throws IOException {
        String fileInputStream = new FileInputStream(file);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        try {
            fileInputStream = dataInputStream.readUTF();
            if (fileInputStream == null) {
                return null;
            }
            Reader stringReader = new StringReader(fileInputStream);
            try {
                XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
                newPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                newPullParser.setInput(stringReader);
                IQProvider discoverInfoProvider = new DiscoverInfoProvider();
                try {
                    newPullParser.next();
                    String attributeValue = newPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, LocaleUtil.INDONESIAN);
                    String attributeValue2 = newPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, PrivacyRule.SUBSCRIPTION_FROM);
                    String attributeValue3 = newPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, MultipleAddresses.TO);
                    newPullParser.next();
                    try {
                        DiscoverInfo discoverInfo = (DiscoverInfo) discoverInfoProvider.parseIQ(newPullParser);
                        discoverInfo.setPacketID(attributeValue);
                        discoverInfo.setFrom(attributeValue2);
                        discoverInfo.setTo(attributeValue3);
                        discoverInfo.setType(Type.RESULT);
                        return discoverInfo;
                    } catch (Exception e) {
                        return null;
                    }
                } catch (XmlPullParserException e2) {
                    return null;
                }
            } catch (XmlPullParserException e3) {
                e3.printStackTrace();
                return null;
            }
        } finally {
            dataInputStream.close();
        }
    }
}
