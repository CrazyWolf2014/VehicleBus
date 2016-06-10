package com.ifoer.entity;

import java.util.List;

public class PostAddressInfoResult {
    private int code;
    private List<PostAddressInfoDTO> postAddressInfoDTO;

    public List<PostAddressInfoDTO> getPostAddressInfoDTO() {
        return this.postAddressInfoDTO;
    }

    public void setPostAddressInfoDTO(List<PostAddressInfoDTO> postAddressInfoDTO) {
        this.postAddressInfoDTO = postAddressInfoDTO;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
