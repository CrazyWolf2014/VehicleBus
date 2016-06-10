package net.sourceforge.pinyin4j.format;

public class HanyuPinyinCaseType {
    public static final HanyuPinyinCaseType LOWERCASE;
    public static final HanyuPinyinCaseType UPPERCASE;
    protected String name;

    static {
        UPPERCASE = new HanyuPinyinCaseType("UPPERCASE");
        LOWERCASE = new HanyuPinyinCaseType("LOWERCASE");
    }

    protected HanyuPinyinCaseType(String str) {
        setName(str);
    }

    public String getName() {
        return this.name;
    }

    protected void setName(String str) {
        this.name = str;
    }
}
