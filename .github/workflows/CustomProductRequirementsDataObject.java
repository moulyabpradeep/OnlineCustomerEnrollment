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
public class CustomProductRequirementsDataObject {

    private String title;
    private List<String> requirements;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }
}
