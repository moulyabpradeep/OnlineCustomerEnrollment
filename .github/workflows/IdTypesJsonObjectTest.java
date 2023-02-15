package com.msp.acct.workflow.integrations.silver_lake;


import com.msp.acct.workflow.integrations.IdTypesJsonObject;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * Id types POJO object file
 *
 * @author Moulya Pradeep
 * @since Dec/18/2021
 */
public class IdTypesJsonObjectTest {

    private IdTypesJsonObject idTypesJsonObject;

    @Before
    public void setUp() throws Exception {
        idTypesJsonObject = new IdTypesJsonObject();
    }

    @Test
    public void gettersTest() {
        idTypesJsonObject.getAllowFutureIssueDate();
        idTypesJsonObject.getAllowPastExpiryDate();
        idTypesJsonObject.getCollectCountry();
        idTypesJsonObject.getCollectExpiryDate();
        idTypesJsonObject.getCollectIssueDate();
        idTypesJsonObject.getCollectState();
        idTypesJsonObject.getIdType();
        idTypesJsonObject.getIdTypeDriversLicense();
    }

    @Test
    public void settersTest() {
        idTypesJsonObject.setAllowFutureIssueDate(true);
        idTypesJsonObject.setAllowPastExpiryDate(true);
        idTypesJsonObject.setCollectCountry(true);
        idTypesJsonObject.setCollectExpiryDate(true);
        idTypesJsonObject.setCollectIssueDate(true);
        idTypesJsonObject.setCollectState(false);
        idTypesJsonObject.setIdType("passport");
        idTypesJsonObject.setIdTypeDriversLicense("DL");
    }

}