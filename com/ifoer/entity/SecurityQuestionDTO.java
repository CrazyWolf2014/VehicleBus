package com.ifoer.entity;

import java.io.Serializable;

public class SecurityQuestionDTO implements Serializable {
    private static final long serialVersionUID = 1;
    protected String questionDesc;
    protected Integer questionId;

    public String getQuestionDesc() {
        return this.questionDesc;
    }

    public void setQuestionDesc(String value) {
        this.questionDesc = value;
    }

    public Integer getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(Integer value) {
        this.questionId = value;
    }
}
