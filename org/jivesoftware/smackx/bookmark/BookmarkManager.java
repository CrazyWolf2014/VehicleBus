package org.jivesoftware.smackx.bookmark;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bookmark.Bookmarks.Provider;

public class BookmarkManager {
    private static final Map<Connection, BookmarkManager> bookmarkManagerMap;
    private final Object bookmarkLock;
    private Bookmarks bookmarks;
    private PrivateDataManager privateDataManager;

    static {
        bookmarkManagerMap = new HashMap();
        PrivateDataManager.addPrivateDataProvider("storage", "storage:bookmarks", new Provider());
    }

    public static synchronized BookmarkManager getBookmarkManager(Connection connection) throws XMPPException {
        BookmarkManager bookmarkManager;
        synchronized (BookmarkManager.class) {
            bookmarkManager = (BookmarkManager) bookmarkManagerMap.get(connection);
            if (bookmarkManager == null) {
                bookmarkManager = new BookmarkManager(connection);
                bookmarkManagerMap.put(connection, bookmarkManager);
            }
        }
        return bookmarkManager;
    }

    private BookmarkManager(Connection connection) throws XMPPException {
        this.bookmarkLock = new Object();
        if (connection == null || !connection.isAuthenticated()) {
            throw new XMPPException("Invalid connection.");
        }
        this.privateDataManager = new PrivateDataManager(connection);
    }

    public Collection<BookmarkedConference> getBookmarkedConferences() throws XMPPException {
        retrieveBookmarks();
        return Collections.unmodifiableCollection(this.bookmarks.getBookmarkedConferences());
    }

    public void addBookmarkedConference(String str, String str2, boolean z, String str3, String str4) throws XMPPException {
        retrieveBookmarks();
        BookmarkedConference bookmarkedConference = new BookmarkedConference(str, str2, z, str3, str4);
        List bookmarkedConferences = this.bookmarks.getBookmarkedConferences();
        if (bookmarkedConferences.contains(bookmarkedConference)) {
            bookmarkedConference = (BookmarkedConference) bookmarkedConferences.get(bookmarkedConferences.indexOf(bookmarkedConference));
            if (bookmarkedConference.isShared()) {
                throw new IllegalArgumentException("Cannot modify shared bookmark");
            }
            bookmarkedConference.setAutoJoin(z);
            bookmarkedConference.setName(str);
            bookmarkedConference.setNickname(str3);
            bookmarkedConference.setPassword(str4);
        } else {
            this.bookmarks.addBookmarkedConference(bookmarkedConference);
        }
        this.privateDataManager.setPrivateData(this.bookmarks);
    }

    public void removeBookmarkedConference(String str) throws XMPPException {
        retrieveBookmarks();
        Iterator it = this.bookmarks.getBookmarkedConferences().iterator();
        while (it.hasNext()) {
            BookmarkedConference bookmarkedConference = (BookmarkedConference) it.next();
            if (bookmarkedConference.getJid().equalsIgnoreCase(str)) {
                if (bookmarkedConference.isShared()) {
                    throw new IllegalArgumentException("Conference is shared and can't be removed");
                }
                it.remove();
                this.privateDataManager.setPrivateData(this.bookmarks);
                return;
            }
        }
    }

    public Collection<BookmarkedURL> getBookmarkedURLs() throws XMPPException {
        retrieveBookmarks();
        return Collections.unmodifiableCollection(this.bookmarks.getBookmarkedURLS());
    }

    public void addBookmarkedURL(String str, String str2, boolean z) throws XMPPException {
        retrieveBookmarks();
        BookmarkedURL bookmarkedURL = new BookmarkedURL(str, str2, z);
        List bookmarkedURLS = this.bookmarks.getBookmarkedURLS();
        if (bookmarkedURLS.contains(bookmarkedURL)) {
            bookmarkedURL = (BookmarkedURL) bookmarkedURLS.get(bookmarkedURLS.indexOf(bookmarkedURL));
            if (bookmarkedURL.isShared()) {
                throw new IllegalArgumentException("Cannot modify shared bookmarks");
            }
            bookmarkedURL.setName(str2);
            bookmarkedURL.setRss(z);
        } else {
            this.bookmarks.addBookmarkedURL(bookmarkedURL);
        }
        this.privateDataManager.setPrivateData(this.bookmarks);
    }

    public void removeBookmarkedURL(String str) throws XMPPException {
        retrieveBookmarks();
        Iterator it = this.bookmarks.getBookmarkedURLS().iterator();
        while (it.hasNext()) {
            BookmarkedURL bookmarkedURL = (BookmarkedURL) it.next();
            if (bookmarkedURL.getURL().equalsIgnoreCase(str)) {
                if (bookmarkedURL.isShared()) {
                    throw new IllegalArgumentException("Cannot delete a shared bookmark.");
                }
                it.remove();
                this.privateDataManager.setPrivateData(this.bookmarks);
                return;
            }
        }
    }

    private Bookmarks retrieveBookmarks() throws XMPPException {
        Bookmarks bookmarks;
        synchronized (this.bookmarkLock) {
            if (this.bookmarks == null) {
                this.bookmarks = (Bookmarks) this.privateDataManager.getPrivateData("storage", "storage:bookmarks");
            }
            bookmarks = this.bookmarks;
        }
        return bookmarks;
    }
}
