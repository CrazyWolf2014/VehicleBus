package com.car.result;

import com.ifoer.entity.AlipayRSATradeDTO;

public class AlipayRSATradeDTOResult extends WSResult {
    protected AlipayRSATradeDTO alipayRSATradeDTO;

    public AlipayRSATradeDTO getAlipayRSATradeDTO() {
        return this.alipayRSATradeDTO;
    }

    public void setAlipayRSATradeDTO(AlipayRSATradeDTO value) {
        this.alipayRSATradeDTO = value;
    }
}
