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
 * @since 12/11/2020.
 */
public class CustomProductListDataObject {

    private String friendlyName;
    private Boolean allowMultiple;

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public Boolean getAllowMultiple() {
        return allowMultiple;
    }

    public void setAllowMultiple(Boolean allowMultiple) {
        this.allowMultiple = allowMultiple;
    }
}
