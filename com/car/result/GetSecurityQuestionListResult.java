package com.car.result;

import com.ifoer.entity.SecurityQuestionDTO;
import java.util.ArrayList;
import java.util.List;

public class GetSecurityQuestionListResult extends WSResult {
    private int code;
    private List<SecurityQuestionDTO> securityQuestionList;

    public GetSecurityQuestionListResult() {
        this.securityQuestionList = new ArrayList();
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<SecurityQuestionDTO> getSecurityQuestionList() {
        return this.securityQuestionList;
    }

    public void setSecurityQuestionList(List<SecurityQuestionDTO> securityQuestionList) {
        this.securityQuestionList = securityQuestionList;
    }
}
