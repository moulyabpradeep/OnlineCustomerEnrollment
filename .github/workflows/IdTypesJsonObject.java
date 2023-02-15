package com.msp.acct.workflow.integrations;

/**
 * Created by Moulya Pradeep on Dec/16/2021.
 *
 * Representation of an instance of id types details
 */
public class IdTypesJsonObject {
    private Boolean collectExpiryDate;
    private String idType;
    private Boolean allowFutureIssueDate;
    private Boolean collectState;
    private Boolean collectCountry;
    private Boolean allowPastExpiryDate;
    private String idTypeDriversLicense;
    private Boolean collectIssueDate;

    public Boolean getCollectExpiryDate() {
        return collectExpiryDate;
    }

    public void setCollectExpiryDate(Boolean collectExpiryDate) {
        this.collectExpiryDate = collectExpiryDate;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public Boolean getAllowFutureIssueDate() {
        return allowFutureIssueDate;
    }

    public void setAllowFutureIssueDate(Boolean allowFutureIssueDate) {
        this.allowFutureIssueDate = allowFutureIssueDate;
    }

    public Boolean getCollectState() {
        return collectState;
    }

    public void setCollectState(Boolean collectState) {
        this.collectState = collectState;
    }

    public Boolean getCollectCountry() {
        return collectCountry;
    }

    public void setCollectCountry(Boolean collectCountry) {
        this.collectCountry = collectCountry;
    }

    public Boolean getAllowPastExpiryDate() {
        return allowPastExpiryDate;
    }

    public void setAllowPastExpiryDate(Boolean allowPastExpiryDate) {
        this.allowPastExpiryDate = allowPastExpiryDate;
    }

    public String getIdTypeDriversLicense() {
        return idTypeDriversLicense;
    }

    public void setIdTypeDriversLicense(String idTypeDriversLicense) {
        this.idTypeDriversLicense = idTypeDriversLicense;
    }

    public Boolean getCollectIssueDate() {
        return collectIssueDate;
    }

    public void setCollectIssueDate(Boolean collectIssueDate) {
        this.collectIssueDate = collectIssueDate;
    }
}
