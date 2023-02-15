package com.msp.acct.exchange.requests;

import com.msp.acct.workflow.modules.EMC;
import org.cufx_4_2.UserIdType;
import org.json.JSONObject;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

/**
 * (c) 2014-2017 Mobile Strategy Partners LLC. All Rights Reserved.
 * <p>
 * Representation for the WebDataAPI Request
 *
 * @author Moulya Pradeep
 * @since Jan/13/2021
 */
public class WebDataAPIRequest {

    private String userName;
    private String password;
    private UserIdType userType;
    private Integer fiId;
    private Integer pageRequested;
    private Integer pageSize;
    private XMLGregorianCalendar startDateTime;
    private XMLGregorianCalendar endDateTime;
    private List<String> applicationId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserIdType getUserType() {
        return userType;
    }

    public void setUserType(UserIdType userType) {
        this.userType = userType;
    }

    public Integer getFiId() {
        return fiId;
    }

    public void setFiId(Integer fiId) {
        this.fiId = fiId;
    }

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

    public XMLGregorianCalendar getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(XMLGregorianCalendar startDateTime) {
        this.startDateTime = startDateTime;
    }

    public XMLGregorianCalendar getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(XMLGregorianCalendar endDateTime) {
        this.endDateTime = endDateTime;
    }

    public List<String> getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(List<String> applicationId) {
        this.applicationId = applicationId;
    }
}