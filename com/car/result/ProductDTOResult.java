package com.car.result;

import com.ifoer.entity.ProductDTO;
import java.util.ArrayList;
import java.util.List;

public class ProductDTOResult extends WSResult {
    protected List<ProductDTO> productDTOs;

    public ProductDTOResult() {
        this.productDTOs = new ArrayList();
    }

    public List<ProductDTO> getProductDTOs() {
        return this.productDTOs;
    }

    public void setProductDTOs(List<ProductDTO> productDTOs) {
        this.productDTOs = productDTOs;
    }
}
