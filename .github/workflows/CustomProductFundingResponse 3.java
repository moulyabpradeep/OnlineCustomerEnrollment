package com.msp.acct.exchange.responses;

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
public class CustomProductFundingResponse {

    private Integer defaultFunding;
    private Integer minFunding;
    private Integer maxFunding;

    public Integer getDefaultFunding() {
        return defaultFunding;
    }

    public void setDefaultFunding(Integer defaultFunding) {
        this.defaultFunding = defaultFunding;
    }

    public Integer getMinFunding() {
        return minFunding;
    }

    public void setMinFunding(Integer minFunding) {
        this.minFunding = minFunding;
    }

    public Integer getMaxFunding() {
        return maxFunding;
    }

    public void setMaxFunding(Integer maxFunding) {
        this.maxFunding = maxFunding;
    }
}
