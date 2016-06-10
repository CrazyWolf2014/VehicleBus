package com.ifoer.mine;

import com.cnmobi.im.util.XmppConnection;
import java.util.Comparator;

public class PinyinComparator implements Comparator<SortModel> {
    public int compare(SortModel o1, SortModel o2) {
        if (o1.getSortLetters().equals(XmppConnection.JID_SEPARATOR) || o2.getSortLetters().equals("#")) {
            return -1;
        }
        if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals(XmppConnection.JID_SEPARATOR)) {
            return 1;
        }
        return o1.getSortLetters().compareTo(o2.getSortLetters());
    }
}
