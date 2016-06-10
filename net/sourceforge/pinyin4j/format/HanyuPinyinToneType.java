package net.sourceforge.pinyin4j.format;

public class HanyuPinyinToneType {
    public static final HanyuPinyinToneType WITHOUT_TONE;
    public static final HanyuPinyinToneType WITH_TONE_MARK;
    public static final HanyuPinyinToneType WITH_TONE_NUMBER;
    protected String name;

    static {
        WITH_TONE_NUMBER = new HanyuPinyinToneType("WITH_TONE_NUMBER");
        WITHOUT_TONE = new HanyuPinyinToneType("WITHOUT_TONE");
        WITH_TONE_MARK = new HanyuPinyinToneType("WITH_TONE_MARK");
    }

    protected HanyuPinyinToneType(String str) {
        setName(str);
    }

    public String getName() {
        return this.name;
    }

    protected void setName(String str) {
        this.name = str;
    }
}
