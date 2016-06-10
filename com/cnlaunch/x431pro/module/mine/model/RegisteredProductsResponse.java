package com.cnlaunch.x431pro.module.mine.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;
import java.util.List;

public class RegisteredProductsResponse extends BaseResponse {
    private static final long serialVersionUID = 1824386259294829009L;
    private List<ProductDTO> productDTOs;

    public List<ProductDTO> getProductDTOs() {
        return this.productDTOs;
    }

    public void setProductDTOs(List<ProductDTO> productDTOs) {
        this.productDTOs = productDTOs;
    }
}
