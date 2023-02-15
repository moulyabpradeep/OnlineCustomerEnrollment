package com.msp.acct.exchange.responses;

import java.util.List;

/**
 * (c) 2016-2017 Gro Solutions, Inc. All Rights Reserved.
 *
 * This is the POJO that contains the request returns by the integration that return custom Q&A.
 * This POJO should be created in getListOfProductTypes method.
 *
 * Keep in mind this object never can be pass to any Controller nor to the client-side.
 *
 * @author Moulya Pradeep.
 * @since 12/11/2020.
 */
public class ProductTypeResponse {
    private String productType;
    private Boolean isMultiSelect;
    private String productImage;
    private Integer noOfProducts;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Boolean getMultiSelect() {
        return isMultiSelect;
    }

    public void setMultiSelect(Boolean multiSelect) {
        isMultiSelect = multiSelect;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Integer getNoOfProducts() {
        return noOfProducts;
    }

    public void setNoOfProducts(Integer noOfProducts) {
        this.noOfProducts = noOfProducts;
    }
}
