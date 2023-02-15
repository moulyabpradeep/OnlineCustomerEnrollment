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
public class CustomAgeRulesDataObject {

    private Integer maxAge;
    private Integer minAge;
    private Integer needJointIfLessThan;
    private Integer minAgeJoint;
    private Integer maxAgeJoint;

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getNeedJointIfLessThan() {
        return needJointIfLessThan;
    }

    public void setNeedJointIfLessThan(Integer needJointIfLessThan) {
        this.needJointIfLessThan = needJointIfLessThan;
    }

    public Integer getMinAgeJoint() {
        return minAgeJoint;
    }

    public void setMinAgeJoint(Integer minAgeJoint) {
        this.minAgeJoint = minAgeJoint;
    }

    public Integer getMaxAgeJoint() {
        return maxAgeJoint;
    }

    public void setMaxAgeJoint(Integer maxAgeJoint) {
        this.maxAgeJoint = maxAgeJoint;
    }
}
