package com.ifoer.mine;

import com.ifoer.mine.model.Feedback;
import java.util.Comparator;

public class SortClass implements Comparator {
    public int compare(Object arg0, Object arg1) {
        return ((Feedback) arg1).getUpdated().compareTo(((Feedback) arg0).getUpdated());
    }
}
