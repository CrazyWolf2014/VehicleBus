package com.ifoer.entity;

import com.car.result.WSResult;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardConsumeListResult extends WSResult implements Serializable {
    private static final long serialVersionUID = -2407217813289095599L;
    private List<CardConsumeDTO> cardListReust;

    public CardConsumeListResult() {
        this.cardListReust = new ArrayList();
    }

    public List<CardConsumeDTO> getCardListReust() {
        return this.cardListReust;
    }

    public void setCardListReust(List<CardConsumeDTO> cardListReust) {
        this.cardListReust = cardListReust;
    }
}
