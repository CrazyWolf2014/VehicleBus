package com.kenai.jbosh;

interface HTTPSender {
    void destroy();

    void init(BOSHClientConfig bOSHClientConfig);

    HTTPResponse send(CMSessionParams cMSessionParams, AbstractBody abstractBody);
}
