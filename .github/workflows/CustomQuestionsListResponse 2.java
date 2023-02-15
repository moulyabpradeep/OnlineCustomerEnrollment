package com.msp.acct.exchange.responses;

import com.msp.acct.util.Constants;

import java.util.List;

/**
 * (c) 2016-2017 Gro Solutions, Inc. All Rights Reserved.
 *
 * This is the POJO that contains the request returns by the integration that return custom Q&A.
 * This POJO should be a return type of method getListOfCustomQuestions method.
 *
 * Keep in mind this object never can be pass to any Controller nor to the client-side.
 *
 * @author Moulya Pradeep.
 * @since 12/07/2020.
 */
public class CustomQuestionsListResponse {
    private List<CustomQuestionsResponse> customQuestionList;

    public CustomQuestionsListResponse(List<CustomQuestionsResponse> customQuestionList) {
        this.customQuestionList = customQuestionList;
    }

    public List<CustomQuestionsResponse> getCustomQuestionList() {
        return customQuestionList;
    }

    public void setCustomQuestionList(List<CustomQuestionsResponse> customQuestionList) {
        this.customQuestionList = customQuestionList;
    }
}
