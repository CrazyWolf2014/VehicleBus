package com.cnmobi.im.bo;

import com.cnmobi.im.dto.MessageVo;

public interface MsgCallback {
    void msgChange(MessageVo messageVo);
}
