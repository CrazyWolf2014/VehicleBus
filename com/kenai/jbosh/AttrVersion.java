package com.kenai.jbosh;

final class AttrVersion extends AbstractAttr<String> implements Comparable {
    private static final AttrVersion DEFAULT;
    private final int major;
    private final int minor;

    static {
        try {
            DEFAULT = createFromString("1.8");
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    private AttrVersion(String str) throws BOSHException {
        super(str);
        int indexOf = str.indexOf(46);
        if (indexOf <= 0) {
            throw new BOSHException("Illegal ver attribute value (not in major.minor form): " + str);
        }
        String substring = str.substring(0, indexOf);
        try {
            this.major = Integer.parseInt(substring);
            if (this.major < 0) {
                throw new BOSHException("Major version may not be < 0");
            }
            substring = str.substring(indexOf + 1);
            try {
                this.minor = Integer.parseInt(substring);
                if (this.minor < 0) {
                    throw new BOSHException("Minor version may not be < 0");
                }
            } catch (Throwable e) {
                throw new BOSHException("Could not parse ver attribute value (minor ver): " + substring, e);
            }
        } catch (Throwable e2) {
            throw new BOSHException("Could not parse ver attribute value (major ver): " + substring, e2);
        }
    }

    static AttrVersion getSupportedVersion() {
        return DEFAULT;
    }

    static AttrVersion createFromString(String str) throws BOSHException {
        if (str == null) {
            return null;
        }
        return new AttrVersion(str);
    }

    int getMajor() {
        return this.major;
    }

    int getMinor() {
        return this.minor;
    }

    public int compareTo(Object obj) {
        if (!(obj instanceof AttrVersion)) {
            return 0;
        }
        AttrVersion attrVersion = (AttrVersion) obj;
        if (this.major < attrVersion.major) {
            return -1;
        }
        if (this.major > attrVersion.major) {
            return 1;
        }
        if (this.minor < attrVersion.minor) {
            return -1;
        }
        if (this.minor > attrVersion.minor) {
            return 1;
        }
        return 0;
    }
}
