package com.msp.acct.exchange.responses;

import java.util.List;

/**
 * (c) 2016-2017 Gro Solutions, Inc. All Rights Reserved.
 *
 * This is the POJO that contains the request returns by the integration that return Product types.
 * This POJO used to iterate to get product type in getListOfProductTypes method.
 *
 * Keep in mind this object never can be pass to any Controller nor to the client-side.
 *
 * @author Moulya Pradeep.
 * @since 12/14/2020.
 */
public class CustomProductFundingDataObject {

    private Integer default_funding;
    private Integer min_funding;
    private Integer max_funding;
    private List<String> types;

    public Integer getDefault_funding() {
        return default_funding;
    }

    public void setDefault_funding(Integer default_funding) {
        this.default_funding = default_funding;
    }

    public Integer getMin_funding() {
        return min_funding;
    }

    public void setMin_funding(Integer min_funding) {
        this.min_funding = min_funding;
    }

    public Integer getMax_funding() {
        return max_funding;
    }

    public void setMax_funding(Integer max_funding) {
        this.max_funding = max_funding;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
