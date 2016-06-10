package net.sourceforge.pinyin4j;

import com.hp.hpl.sparta.Document;
import com.hp.hpl.sparta.ParseException;
import com.hp.hpl.sparta.Parser;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;

class PinyinRomanizationResource {
    private Document pinyinMappingDoc;

    /* renamed from: net.sourceforge.pinyin4j.PinyinRomanizationResource.1 */
    static class C09191 {
    }

    private static class PinyinRomanizationSystemResourceHolder {
        static final PinyinRomanizationResource theInstance;

        static {
            theInstance = new PinyinRomanizationResource(null);
        }

        private PinyinRomanizationSystemResourceHolder() {
        }
    }

    private PinyinRomanizationResource() {
        initializeResource();
    }

    PinyinRomanizationResource(C09191 c09191) {
        this();
    }

    static PinyinRomanizationResource getInstance() {
        return PinyinRomanizationSystemResourceHolder.theInstance;
    }

    private void initializeResource() {
        try {
            setPinyinMappingDoc(Parser.parse(XmlPullParser.NO_NAMESPACE, ResourceHelper.getResourceInputStream("/pinyindb/pinyin_mapping.xml")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (ParseException e3) {
            e3.printStackTrace();
        }
    }

    private void setPinyinMappingDoc(Document document) {
        this.pinyinMappingDoc = document;
    }

    Document getPinyinMappingDoc() {
        return this.pinyinMappingDoc;
    }
}
