package com.msp.acct.exchange.requests;

import com.msp.acct.data.Applicant;
import com.msp.acct.data.service.groapi.GroLostApplicant;

import java.util.List;

/**
 * (c) 2014-2017 Mobile Strategy Partners LLC. All Rights Reserved.
 * <p>
 * Representation for the Applicant Details with count
 *
 * @author Moulya Pradeep
 * @since Jan/13/2021
 */
public class ApplicantDetails {
    private List<Applicant> applicantList;
    private List<Applicant> abandonedApplicantDetails;
    private List<GroLostApplicant> abandonedApplicantsInfo;
    private Integer resultCount;

    public List<Applicant> getApplicantList() {
        return applicantList;
    }

    public void setApplicantList(List<Applicant> applicantList) {
        this.applicantList = applicantList;
    }

    public List<Applicant> getAbandonedApplicantDetails() {
        return abandonedApplicantDetails;
    }

    public void setAbandonedApplicantDetails(List<Applicant> abandonedApplicantDetails) {
        this.abandonedApplicantDetails = abandonedApplicantDetails;
    }

    public List<GroLostApplicant> getAbandonedApplicantsInfo() {
        return abandonedApplicantsInfo;
    }

    public void setAbandonedApplicantsInfo(List<GroLostApplicant> abandonedApplicantsInfo) {
        this.abandonedApplicantsInfo = abandonedApplicantsInfo;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }
}