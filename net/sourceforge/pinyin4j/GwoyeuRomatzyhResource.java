package net.sourceforge.pinyin4j;

import com.hp.hpl.sparta.Document;
import com.hp.hpl.sparta.ParseException;
import com.hp.hpl.sparta.Parser;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;

class GwoyeuRomatzyhResource {
    private Document pinyinToGwoyeuMappingDoc;

    /* renamed from: net.sourceforge.pinyin4j.GwoyeuRomatzyhResource.1 */
    static class C09181 {
    }

    private static class GwoyeuRomatzyhSystemResourceHolder {
        static final GwoyeuRomatzyhResource theInstance;

        static {
            theInstance = new GwoyeuRomatzyhResource(null);
        }

        private GwoyeuRomatzyhSystemResourceHolder() {
        }
    }

    private GwoyeuRomatzyhResource() {
        initializeResource();
    }

    GwoyeuRomatzyhResource(C09181 c09181) {
        this();
    }

    static GwoyeuRomatzyhResource getInstance() {
        return GwoyeuRomatzyhSystemResourceHolder.theInstance;
    }

    private void initializeResource() {
        try {
            setPinyinToGwoyeuMappingDoc(Parser.parse(XmlPullParser.NO_NAMESPACE, ResourceHelper.getResourceInputStream("/pinyindb/pinyin_gwoyeu_mapping.xml")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (ParseException e3) {
            e3.printStackTrace();
        }
    }

    private void setPinyinToGwoyeuMappingDoc(Document document) {
        this.pinyinToGwoyeuMappingDoc = document;
    }

    Document getPinyinToGwoyeuMappingDoc() {
        return this.pinyinToGwoyeuMappingDoc;
    }
}
