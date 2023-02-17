package com.msp.acct.exchange.responses;

import java.util.Map;

/**
 * (c) 2016-2017 Gro Solutions, Inc. All Rights Reserved.
 *
 * This is the POJO that contains the request returns by the integration that return Product types.
 * This POJO that holds CustomProductDescriptionResponse map and count Of Products map.
 *
 * Keep in mind this object never can be pass to any Controller nor to the client-side.
 *
 * @author Moulya Pradeep.
 * @since Apr/27/2021.
 */
public class ProductTypeDetailsResponse {

    private Map<String, CustomProductDescriptionResponse> productDescResponseMap;
    private Map<String, Integer> countOfProducts;

    public Map<String, CustomProductDescriptionResponse> getProductDescResponseMap() {
        return productDescResponseMap;
    }

    public void setProductDescResponseMap(Map<String, CustomProductDescriptionResponse> productDescResponseMap) {
        this.productDescResponseMap = productDescResponseMap;
    }

    public Map<String, Integer> getCountOfProducts() {
        return countOfProducts;
    }

    public void setCountOfProducts(Map<String, Integer> countOfProducts) {
        this.countOfProducts = countOfProducts;
    }
}
