package cn.sharesdk.framework.authorize;

import cn.sharesdk.framework.Platform;

public interface AuthorizeHelper {
    AuthorizeListener getAuthorizeListener();

    String getAuthorizeUrl();

    C1015b getAuthorizeWebviewClient(C1223g c1223g);

    Platform getPlatform();

    String getRedirectUri();

    SSOListener getSSOListener();

    C0031f getSSOProcessor(C1222e c1222e);
}
