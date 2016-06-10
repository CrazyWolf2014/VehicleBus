package org.jivesoftware.smackx.bookmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smackx.packet.Nick;
import org.jivesoftware.smackx.packet.PrivateData;
import org.jivesoftware.smackx.provider.PrivateDataProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Bookmarks implements PrivateData {
    private List<BookmarkedConference> bookmarkedConferences;
    private List<BookmarkedURL> bookmarkedURLS;

    public static class Provider implements PrivateDataProvider {
        public PrivateData parsePrivateData(XmlPullParser xmlPullParser) throws Exception {
            PrivateData bookmarks = new Bookmarks();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && "url".equals(xmlPullParser.getName())) {
                    BookmarkedURL access$000 = Bookmarks.getURLStorage(xmlPullParser);
                    if (access$000 != null) {
                        bookmarks.addBookmarkedURL(access$000);
                    }
                } else if (next == 2 && "conference".equals(xmlPullParser.getName())) {
                    bookmarks.addBookmarkedConference(Bookmarks.getConferenceStorage(xmlPullParser));
                } else if (next == 3 && "storage".equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            return bookmarks;
        }
    }

    public Bookmarks() {
        this.bookmarkedURLS = new ArrayList();
        this.bookmarkedConferences = new ArrayList();
    }

    public void addBookmarkedURL(BookmarkedURL bookmarkedURL) {
        this.bookmarkedURLS.add(bookmarkedURL);
    }

    public void removeBookmarkedURL(BookmarkedURL bookmarkedURL) {
        this.bookmarkedURLS.remove(bookmarkedURL);
    }

    public void clearBookmarkedURLS() {
        this.bookmarkedURLS.clear();
    }

    public void addBookmarkedConference(BookmarkedConference bookmarkedConference) {
        this.bookmarkedConferences.add(bookmarkedConference);
    }

    public void removeBookmarkedConference(BookmarkedConference bookmarkedConference) {
        this.bookmarkedConferences.remove(bookmarkedConference);
    }

    public void clearBookmarkedConferences() {
        this.bookmarkedConferences.clear();
    }

    public List<BookmarkedURL> getBookmarkedURLS() {
        return this.bookmarkedURLS;
    }

    public List<BookmarkedConference> getBookmarkedConferences() {
        return this.bookmarkedConferences;
    }

    public String getElementName() {
        return "storage";
    }

    public String getNamespace() {
        return "storage:bookmarks";
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<storage xmlns=\"storage:bookmarks\">");
        for (BookmarkedURL bookmarkedURL : getBookmarkedURLS()) {
            if (!bookmarkedURL.isShared()) {
                stringBuilder.append("<url name=\"").append(bookmarkedURL.getName()).append("\" url=\"").append(bookmarkedURL.getURL()).append("\"");
                if (bookmarkedURL.isRss()) {
                    stringBuilder.append(" rss=\"").append(true).append("\"");
                }
                stringBuilder.append(" />");
            }
        }
        for (BookmarkedConference bookmarkedConference : getBookmarkedConferences()) {
            if (!bookmarkedConference.isShared()) {
                stringBuilder.append("<conference ");
                stringBuilder.append("name=\"").append(bookmarkedConference.getName()).append("\" ");
                stringBuilder.append("autojoin=\"").append(bookmarkedConference.isAutoJoin()).append("\" ");
                stringBuilder.append("jid=\"").append(bookmarkedConference.getJid()).append("\" ");
                stringBuilder.append(">");
                if (bookmarkedConference.getNickname() != null) {
                    stringBuilder.append("<nick>").append(bookmarkedConference.getNickname()).append("</nick>");
                }
                if (bookmarkedConference.getPassword() != null) {
                    stringBuilder.append("<password>").append(bookmarkedConference.getPassword()).append("</password>");
                }
                stringBuilder.append("</conference>");
            }
        }
        stringBuilder.append("</storage>");
        return stringBuilder.toString();
    }

    private static BookmarkedURL getURLStorage(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "name");
        String attributeValue2 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "url");
        String attributeValue3 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "rss");
        boolean z = attributeValue3 != null && "true".equals(attributeValue3);
        BookmarkedURL bookmarkedURL = new BookmarkedURL(attributeValue2, attributeValue, z);
        z = false;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2 && "shared_bookmark".equals(xmlPullParser.getName())) {
                bookmarkedURL.setShared(true);
            } else if (next == 3 && "url".equals(xmlPullParser.getName())) {
                z = true;
            }
        }
        return bookmarkedURL;
    }

    private static BookmarkedConference getConferenceStorage(XmlPullParser xmlPullParser) throws Exception {
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "name");
        String attributeValue2 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "autojoin");
        BookmarkedConference bookmarkedConference = new BookmarkedConference(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid"));
        bookmarkedConference.setName(attributeValue);
        bookmarkedConference.setAutoJoin(Boolean.valueOf(attributeValue2).booleanValue());
        boolean z = false;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2 && Nick.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                bookmarkedConference.setNickname(xmlPullParser.nextText());
            } else if (next == 2 && "password".equals(xmlPullParser.getName())) {
                bookmarkedConference.setPassword(xmlPullParser.nextText());
            } else if (next == 2 && "shared_bookmark".equals(xmlPullParser.getName())) {
                bookmarkedConference.setShared(true);
            } else if (next == 3 && "conference".equals(xmlPullParser.getName())) {
                z = true;
            }
        }
        return bookmarkedConference;
    }
}
