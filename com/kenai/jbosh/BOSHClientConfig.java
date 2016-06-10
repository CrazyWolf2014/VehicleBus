package com.kenai.jbosh;

import com.tencent.mm.sdk.platformtools.Util;
import java.net.URI;
import javax.net.ssl.SSLContext;

public final class BOSHClientConfig {
    private final boolean compressionEnabled;
    private final String from;
    private final String lang;
    private final String proxyHost;
    private final int proxyPort;
    private final String route;
    private final SSLContext sslContext;
    private final String to;
    private final URI uri;

    public static final class Builder {
        private Boolean bCompression;
        private final String bDomain;
        private String bFrom;
        private String bLang;
        private String bProxyHost;
        private int bProxyPort;
        private String bRoute;
        private SSLContext bSSLContext;
        private final URI bURI;

        private Builder(URI uri, String str) {
            this.bURI = uri;
            this.bDomain = str;
        }

        public static Builder create(URI uri, String str) {
            if (uri == null) {
                throw new IllegalArgumentException("Connection manager URI must not be null");
            } else if (str == null) {
                throw new IllegalArgumentException("Target domain must not be null");
            } else {
                String scheme = uri.getScheme();
                if ("http".equals(scheme) || "https".equals(scheme)) {
                    return new Builder(uri, str);
                }
                throw new IllegalArgumentException("Only 'http' and 'https' URI are allowed");
            }
        }

        public static Builder create(BOSHClientConfig bOSHClientConfig) {
            Builder builder = new Builder(bOSHClientConfig.getURI(), bOSHClientConfig.getTo());
            builder.bFrom = bOSHClientConfig.getFrom();
            builder.bLang = bOSHClientConfig.getLang();
            builder.bRoute = bOSHClientConfig.getRoute();
            builder.bProxyHost = bOSHClientConfig.getProxyHost();
            builder.bProxyPort = bOSHClientConfig.getProxyPort();
            builder.bSSLContext = bOSHClientConfig.getSSLContext();
            builder.bCompression = Boolean.valueOf(bOSHClientConfig.isCompressionEnabled());
            return builder;
        }

        public Builder setFrom(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Client ID must not be null");
            }
            this.bFrom = str;
            return this;
        }

        public Builder setXMLLang(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Default language ID must not be null");
            }
            this.bLang = str;
            return this;
        }

        public Builder setRoute(String str, String str2, int i) {
            if (str == null) {
                throw new IllegalArgumentException("Protocol cannot be null");
            } else if (str.contains(":")) {
                throw new IllegalArgumentException("Protocol cannot contain the ':' character");
            } else if (str2 == null) {
                throw new IllegalArgumentException("Host cannot be null");
            } else if (str2.contains(":")) {
                throw new IllegalArgumentException("Host cannot contain the ':' character");
            } else if (i <= 0) {
                throw new IllegalArgumentException("Port number must be > 0");
            } else {
                this.bRoute = str + ":" + str2 + ":" + i;
                return this;
            }
        }

        public Builder setProxy(String str, int i) {
            if (str == null || str.length() == 0) {
                throw new IllegalArgumentException("Proxy host name cannot be null or empty");
            } else if (i <= 0) {
                throw new IllegalArgumentException("Proxy port must be > 0");
            } else {
                this.bProxyHost = str;
                this.bProxyPort = i;
                return this;
            }
        }

        public Builder setSSLContext(SSLContext sSLContext) {
            if (sSLContext == null) {
                throw new IllegalArgumentException("SSL context cannot be null");
            }
            this.bSSLContext = sSLContext;
            return this;
        }

        public Builder setCompressionEnabled(boolean z) {
            this.bCompression = Boolean.valueOf(z);
            return this;
        }

        public BOSHClientConfig build() {
            String str;
            int i;
            boolean z;
            if (this.bLang == null) {
                str = Util.ENGLISH;
            } else {
                str = this.bLang;
            }
            if (this.bProxyHost == null) {
                i = 0;
            } else {
                i = this.bProxyPort;
            }
            if (this.bCompression == null) {
                z = false;
            } else {
                z = this.bCompression.booleanValue();
            }
            return new BOSHClientConfig(this.bDomain, this.bFrom, str, this.bRoute, this.bProxyHost, i, this.bSSLContext, z, null);
        }
    }

    private BOSHClientConfig(URI uri, String str, String str2, String str3, String str4, String str5, int i, SSLContext sSLContext, boolean z) {
        this.uri = uri;
        this.to = str;
        this.from = str2;
        this.lang = str3;
        this.route = str4;
        this.proxyHost = str5;
        this.proxyPort = i;
        this.sslContext = sSLContext;
        this.compressionEnabled = z;
    }

    public URI getURI() {
        return this.uri;
    }

    public String getTo() {
        return this.to;
    }

    public String getFrom() {
        return this.from;
    }

    public String getLang() {
        return this.lang;
    }

    public String getRoute() {
        return this.route;
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

    public SSLContext getSSLContext() {
        return this.sslContext;
    }

    boolean isCompressionEnabled() {
        return this.compressionEnabled;
    }
}
