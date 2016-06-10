package net.sourceforge.pinyin4j;

class PinyinRomanizationType {
    static final PinyinRomanizationType GWOYEU_ROMATZYH;
    static final PinyinRomanizationType HANYU_PINYIN;
    static final PinyinRomanizationType MPS2_PINYIN;
    static final PinyinRomanizationType TONGYONG_PINYIN;
    static final PinyinRomanizationType WADEGILES_PINYIN;
    static final PinyinRomanizationType YALE_PINYIN;
    protected String tagName;

    static {
        HANYU_PINYIN = new PinyinRomanizationType("Hanyu");
        WADEGILES_PINYIN = new PinyinRomanizationType("Wade");
        MPS2_PINYIN = new PinyinRomanizationType("MPSII");
        YALE_PINYIN = new PinyinRomanizationType("Yale");
        TONGYONG_PINYIN = new PinyinRomanizationType("Tongyong");
        GWOYEU_ROMATZYH = new PinyinRomanizationType("Gwoyeu");
    }

    protected PinyinRomanizationType(String str) {
        setTagName(str);
    }

    String getTagName() {
        return this.tagName;
    }

    protected void setTagName(String str) {
        this.tagName = str;
    }
}
