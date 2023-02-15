package com.msp.acct.exchange.requests;

import java.util.List;

/**
 * (c) 2014-2017 Mobile Strategy Partners LLC. All Rights Reserved.
 * <p>
 * Representation for the Application Data Request
 *
 * @author Moulya Pradeep
 * @since Jan/27/2021
 */
public class ApplicationDataRequest {

    private Integer pageRequested;
    private Integer pageSize;
    private List<Integer> applicationIdList;
    private List<String> applicationStatusList;
    private String submitApplicationDateRange;
    private String lastModifiedDateRange;
    private String fiId;
    private List<String> productNameList;
    private List<String> productTypeList;
    //orderByAttribute values can be : [ applicationId, applicationStatus, lastModifiedDate, submissionDate(to be implemented in future) ]
    private String orderByAttribute;
    //orderingType values can be : asc/desc
    private String orderingType;

    public Integer getPageRequested() {
        return pageRequested;
    }

    public void setPageRequested(Integer pageRequested) {
        this.pageRequested = pageRequested;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<Integer> getApplicationIdList() {
        return applicationIdList;
    }

    public void setApplicationIdList(List<Integer> applicationIdList) {
        this.applicationIdList = applicationIdList;
    }

    public List<String> getApplicationStatusList() {
        return applicationStatusList;
    }

    public void setApplicationStatusList(List<String> applicationStatusList) {
        this.applicationStatusList = applicationStatusList;
    }

    public String getSubmitApplicationDateRange() {
        return submitApplicationDateRange;
    }

    public void setSubmitApplicationDateRange(String submitApplicationDateRange) {
        this.submitApplicationDateRange = submitApplicationDateRange;
    }

    public String getLastModifiedDateRange() {
        return lastModifiedDateRange;
    }

    public void setLastModifiedDateRange(String lastModifiedDateRange) {
        this.lastModifiedDateRange = lastModifiedDateRange;
    }

    public String getFiId() {
        return fiId;
    }

    public void setFiId(String fiId) {
        this.fiId = fiId;
    }

    public List<String> getProductNameList() {
        return productNameList;
    }

    public void setProductNameList(List<String> productNameList) {
        this.productNameList = productNameList;
    }

    public List<String> getProductTypeList() {
        return productTypeList;
    }

    public void setProductTypeList(List<String> productTypeList) {
        this.productTypeList = productTypeList;
    }

    public String getOrderByAttribute() {
        return orderByAttribute;
    }

    public void setOrderByAttribute(String orderByAttribute) {
        this.orderByAttribute = orderByAttribute;
    }

    public String getOrderingType() {
        return orderingType;
    }

    public void setOrderingType(String orderingType) {
        this.orderingType = orderingType;
    }
}