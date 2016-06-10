package com.cnlaunch.x431pro.module.mine.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;
import java.util.List;

public class ProductsRegDateResponse extends BaseResponse {
    private static final long serialVersionUID = -7427408992013994138L;
    private List<ProductsRegDateDTO> productsRegDateDTOs;

    public List<ProductsRegDateDTO> getProductsRegDateDTOs() {
        return this.productsRegDateDTOs;
    }

    public void setProductsRegDateDTOs(List<ProductsRegDateDTO> productsRegDateDTOs) {
        this.productsRegDateDTOs = productsRegDateDTOs;
    }
}
