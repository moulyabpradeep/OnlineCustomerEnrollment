package com.msp.acct.exchange.responses;

import com.msp.acct.util.Constants;

import java.util.List;

/**
 * (c) 2016-2017 Gro Solutions, Inc. All Rights Reserved.
 *
 * This is the POJO that contains the request returns by the integration that return custom Q&A.
 * This POJO should be created in getCustomQAConfigsByTypeIds method.
 *
 * Keep in mind this object never can be pass to any Controller nor to the client-side.
 *
 * @author Moulya Pradeep.
 * @since 12/07/2020.
 */
public class CustomQuestionsResponse {
    private String question;
    private String type;
    private Boolean isMultiselect = Boolean.FALSE;
    private List<String> values;
    private String additionalInfo;
    private String defaultValue;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getMultiselect() {
        return isMultiselect;
    }

    public void setMultiselect(Boolean multiselect) {
        isMultiselect = multiselect;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
