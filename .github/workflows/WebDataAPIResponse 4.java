package com.msp.acct.exchange.responses;

import com.msp.acct.data.Applicant;
import com.msp.acct.data.service.groapi.GroLostApplicant;

import java.util.List;

/**
 * (c) 2014-2017 Mobile Strategy Partners LLC. All Rights Reserved.
 * <p>
 * Representation for the WebDataAPI Response
 *
 * @author Moulya Pradeep
 * @since Jan/13/2021
 */
public class WebDataAPIResponse {
    private Boolean isAuthenticated;
    private List<Applicant> applicantList;
    private List<Applicant> abandonedApplicantDetails;
    private List<GroLostApplicant> abandonedApplicantInfo;
    private Integer resultCount;
    private List<String> errorMessage;

    public Boolean getAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public List<Applicant> getApplicantList() {
        return applicantList;
    }

    public void setApplicantList(List<Applicant> applicantList) {
        this.applicantList = applicantList;
    }

    public List<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(List<String> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public List<Applicant> getAbandonedApplicantDetails() {
        return abandonedApplicantDetails;
    }

    public void setAbandonedApplicantDetails(List<Applicant> abandonedApplicantDetails) {
        this.abandonedApplicantDetails = abandonedApplicantDetails;
    }

    public List<GroLostApplicant> getAbandonedApplicantInfo() {
        return abandonedApplicantInfo;
    }

    public void setAbandonedApplicantInfo(List<GroLostApplicant> abandonedApplicantInfo) {
        this.abandonedApplicantInfo = abandonedApplicantInfo;
    }
}