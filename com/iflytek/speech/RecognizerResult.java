package com.iflytek.speech;

import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

public class RecognizerResult {
    public int confidence;
    public ArrayList<HashMap<String, String>> semanteme;
    public String text;

    public RecognizerResult() {
        this.text = XmlPullParser.NO_NAMESPACE;
        this.confidence = 100;
        this.semanteme = null;
        this.semanteme = new ArrayList();
    }
}
