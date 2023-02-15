package com.msp.acct.workflow.integrations.fiserv.dna;

import com.msp.acct.data.Applicant;
import com.msp.acct.workflow.integrations.ApplicantBased;
import com.msp.acct.workflow.integrations.fiserv.dna.beans.PersonInfo;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * (c) 2016-2017 Gro Solutions, Inc. All Rights Reserved.
 * <p>
 * Unit test for Common Request
 *
 * @author Moulya Pradeep
 * @since 11/25/2021
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Element.class)
public class CommonRequestTest extends ApplicantBased {


    //Tells Mockito to create the mocks based on the @Mock annotation
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private CommonRequest personMaintenanceRequest = new PersonMaintenanceRequest();

    private AccountMaintenanceRequest accountMaintenanceRequest = new AccountMaintenanceRequest();

    private FiservDNASettings fiservDNASettings;
    private PersonInfo primaryPersonInfo;

    CommonRequest personSpy;

    /**
     * Sets up the fiserv DNA integration for testing
     *
     * @throws Exception if test fails
     */
    @Before
    public void setUp() throws Exception {
        initMocks(this);
        String flexFieldsString = "{\n" +
                "    \"person.persTyp.default\": {\n" +
                "      \"PRIMARYPERSTYPE\": \"MMBR\",\n" +
                "      \"JOINTPERSTYPE\": \"NMBR\"\n" +
                "    },\n" +
                "    \"person.persTyp\": {\n" +
                "      \"PRIMARYPERSTYPE\": \"PRIMARY\",\n" +
                "      \"JOINTPERSTYPE\": \"JOINT\"\n" +
                "    },\n" +
                "  \n" +
                "\"person.userFields\": {\n" +
                "            \"MMN\": \"<MOTHERS_MAIDEN_NAME>\",\n" +
                "            \"OCCU\": \"<OCCUPATION>\",\n" +
                "            \"MSEG\": \"<CUSTOM:UV:SEGcode>\"\n" +
                "        }}" ;
        JSONObject flexFields = new JSONObject(flexFieldsString);
        fiservDNASettings = new  FiservDNASettings();
        fiservDNASettings.setFlexFields(flexFields);

        String taxRepresentationString = "{\n" +
                "        \"jointTaxRptForOwnYN\": \"N\",\n" +
                "        \"jointTaxRptForSigYN\": \"N\",\n" +
                "        \"primaryTaxRptForOwnYN\": \"Y\",\n" +
                "        \"primaryTaxRptForSigYN\": \"Y\"\n" +
                "    }" ;
        JSONObject taxRepresentation = new JSONObject(taxRepresentationString);
        fiservDNASettings.setTaxRepresentation(taxRepresentation);

        String identTypesString = "{\n" +
                "            \"DL\": \"1\",\n" +
                "            \"DRIVERSLICENSE\": \"1\",\n" +
                "            \"MILITARYID\": \"9\",\n" +
                "            \"PASSPORT\": \"13\",\n" +
                "            \"PERMANENTRESIDENTCARD\": \"16\",\n" +
                "            \"STATEID\": \"5\"\n" +
                "        }" ;
        JSONObject identTypes = new JSONObject(identTypesString);
        fiservDNASettings.setIdentTypes(identTypes);

        String acctRolesString = "{\n" +
                "    \"entityTypeCode\": \"PERS\",\n" +
                "    \"jointRoleCode\": [\n" +
                "      \"OWN\",\n" +
                "      \"SIGN\"\n" +
                "    ],\n" +
                "    \"minorAge\": \"18\",\n" +
                "    \"sellerID\": \"547793\",\n" +
                "    \"minorRoleCode\": [\n" +
                "      \"MINR\"\n" +
                "    ],\n" +
                "    \"primaryRoleCode\": [\n" +
                "      \"\"\n" +
                "    ]\n" +
                "  }";
        JSONObject acctRoles = new JSONObject(acctRolesString);
        fiservDNASettings.setAccountRoles(acctRoles);
        fiservDNASettings.setAddressUseCodeMail("PRI");
        personMaintenanceRequest.setSettings(fiservDNASettings);
        accountMaintenanceRequest.setSettings(fiservDNASettings);
        personSpy = Mockito.spy(personMaintenanceRequest);
    }



    @Test
    public void testSetSettings() {
        //Execute
        personSpy.setSettings(fiservDNASettings);
        assertNotNull(personSpy.settings);
    }

    @Test
    public void testSetRequestDocument() {
        //Arrange
        Document requestDocument = PowerMockito.mock(Document.class);
        // Execute
        personSpy.setRequestDocument(requestDocument);
        assertNotNull(personSpy.getRequestDocument());
    }

    @Test
    public void testAddSellerAccountRoles() {
        //Arrange
        Element account = PowerMockito.mock(Element.class);
        PowerMockito.mockStatic(Element.class);
        Document requestDocument = PowerMockito.mock(Document.class);
        Element child = PowerMockito.mock(Element.class);
        when(requestDocument.createElement(any())).thenReturn(child);
        accountMaintenanceRequest.setRequestDocument(requestDocument);
        // Execute
        accountMaintenanceRequest.addSellerAccountRoles(account);
        assertNotNull(account);
    }

    @Test
    public void testAddSellerAccountRolesNegativeCase() throws JSONException {
        //Arrange
        String acctRolesString = "{\n" +
                "    \"entityTypeCode\": \"PERS\",\n" +
                "    \"jointRoleCode\": [\n" +
                "      \"OWN\",\n" +
                "      \"SIGN\"\n" +
                "    ],\n" +
                "    \"minorAge\": \"18\",\n" +
                "    \"minorRoleCode\": [\n" +
                "      \"MINR\"\n" +
                "    ],\n" +
                "    \"primaryRoleCode\": [\n" +
                "      \"\"\n" +
                "    ]\n" +
                "  }";
        JSONObject acctRoles = new JSONObject(acctRolesString);
        fiservDNASettings.setAccountRoles(acctRoles);
        accountMaintenanceRequest.setSettings(fiservDNASettings);

        Element account = PowerMockito.mock(Element.class);
        PowerMockito.mockStatic(Element.class);
        Document requestDocument = PowerMockito.mock(Document.class);
        Element child = PowerMockito.mock(Element.class);
        when(requestDocument.createElement(any())).thenReturn(child);
        accountMaintenanceRequest.setRequestDocument(requestDocument);
        // Execute
        accountMaintenanceRequest.addSellerAccountRoles(account);
        assertNotNull(account);
    }

    @Test
    public void testAddSellerFromURLParamAccountRoles() throws JSONException, FiservDNAException {
        //Arrange
        String acctRolesString = "{\n" +
                "    \"entityTypeCode\": \"PERS\",\n" +
                "    \"jointRoleCode\": [\n" +
                "      \"OWN\",\n" +
                "      \"SIGN\"\n" +
                "    ],\n" +
                "    \"minorAge\": \"18\",\n" +
                "    \"sellerIDAppUrl\": \"true\",\n" +
                "    \"sellerID\": \"12345\",\n" +
                "    \"minorRoleCode\": [\n" +
                "      \"MINR\"\n" +
                "    ],\n" +
                "    \"primaryRoleCode\": [\n" +
                "      \"\"\n" +
                "    ]\n" +
                "  }";
        JSONObject acctRoles = new JSONObject(acctRolesString);
        fiservDNASettings.setAccountRoles(acctRoles);
        accountMaintenanceRequest.setSettings(fiservDNASettings);
        Applicant applicant = this.createApplicantForTesting();
        applicant.setUnstructuredVars("{\"mothersMaidenNameApp2\":\"null\",\"isReturningUser\":\"true\",\"mobileCarrier\":\"\",\"isValidSEG\":\"false\",\"workAddr2App2\":\"Null\",\"isFundingBalanceVerified\":\"undefined\",\"applicants\":\"\\\"{\\\\\\\"1\\\\\\\":{\\\\\\\"mailAddrStateAddr\\\\\\\":\\\\\\\"\\\\\\\",\\\\\\\"firstName\\\\\\\":\\\\\\\"Test\\\\\\\",\\\\\\\"lastName\\\\\\\":\\\\\\\"Lastnam\\\\\\\",\\\\\\\"existingCustomerSignal\\\\\\\":\\\\\\\"continue\\\\\\\"}}\\\"\",\"ssnApp2\":\"null\",\"sendApp2\":\"false\",\"occupancyDuration\":\"0\",\"isNative\":\"false\",\"cart\":\"[\\\"chk_summit\\\"]\",\"jointAccount\":\"false\",\"sellerId\":\"12345\",\"isJointAuth\":\"false\",\"applicationStatusDateTime\":\"2022-02-08 09:58:07.932\",\"idTypeApp2\":\"null\",\"brand\":\"999999\",\"jobTitleApp2\":\"Null\",\"workPhoneApp2\":\"null\",\"cookie\":\"_ga=GA1.1.260219305.1633526911; _msuuid_1068lnm41522=216E6428-58A0-40BD-9E74-61DB50413672; _uetvid=4d8b4ef026a911ec999f6d4b7446eb9c; nmstat=95d2219c-5156-8406-b6df-f2b93bc05575; _mkto_trk=id:914-DOA-764&token:_mch-localhost-1633526917186-70016; calltrk_referrer=direct; calltrk_landing=http%3A//localhost%3A9090/; calltrk_session_id=3b039f09-3624-4ade-a978-d585513fc6ed; _hjid=07a7ea5e-0cff-4f82-a1d7-5d2c50511fd9; cookieTimeoutDays2=30; _hjSessionUser_1273173=eyJpZCI6IjNiNTAzZTA1LWZhNTktNWJjMC04YWQ5LWMwMTMxNzhhODFjZSIsImNyZWF0ZWQiOjE2MzgxOTM4NzM4NjUsImV4aXN0aW5nIjp0cnVlfQ==; _ga_21W6M8FD7E=GS1.1.1638350952.6.0.1638350952.0; cookieDeviceInfo2=Mozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/97.0.4692.71+Safari/537.36; mprtcl-v4_4850A2AD={'gs':{'ie':1|'dt':'b9f4a77b0f885145a56c5955fcd18444'|'cgid':'a4976610-1dc3-4a59-aa96-34110dd4ea3c'|'das':'aaf38c83-37ae-4f27-a532-667b897cb903'|'csm':'WyI3Njk4NjQ4MzIxNDAxMDE5MzI0Il0='|'ssd':1643037518840|'sid':'B2AC943E-2183-44AA-A367-CED2C8D0981A'|'les':1643037518848}|'l':0|'-6753790904480232419':{'fst':1634645756389|'ua':'eyJPbmJvYXJkaW5nOiBVc2VybmFtZSI6IkNoYW5nZW1hbmFnZXIiLCJFbWFpbCI6Im0uc3NAcTIuY29tIiwiJE1vYmlsZSI6IjA5ODc2NTQzMjEwIn0='|'lst':1634645763632}|'cu':'7698648321401019324'|'2134702038670824100':{'fst':1634645763634|'ui':'eyI3IjoibS5zc0BxMi5jb20ifQ=='|'ua':'eyJPbmJvYXJkaW5nOiBQcmltYXJ5IFNvdXJjZSBvZiBGdW5kcyI6IlNlbGYtRW1wbG95bWVudCBJbmNvbWUiLCJPbmJvYXJkaW5nOiBDb25zZW50ZWQgdG8gVzkgVGF4IERpc2NsYWltZXIiOnRydWUsIk9uYm9hcmRpbmc6IENvbnNlbnRlZCB0byBSZWNlaXZlIEVsZWN0cm9uaWMgRGlzY2xvc3VyZXMiOnRydWUsIk9uYm9hcmRpbmc6IEFjY2VwdGVkIFRlcm1zIGFuZCBDb25kaXRpb25zIjp0cnVlLCIkTW9iaWxlIjoiKDA5OCkgNzY1LTQzMjEiLCIkRmlyc3ROYW1lIjoiVGVzdCIsIiRMYXN0TmFtZSI6Ikxhc3RuYW0iLCIkQWRkcmVzcyI6IjEyMyBKZWZmZXJzb24gU3RyZWV0IiwiJENpdHkiOiJBdGxhbnRhIiwiJFN0YXRlIjoiQUwiLCIkWmlwIjoiMDI0NTMiLCJET0IiOiIxMS8xMS8xOTkwIiwiT25ib2FyZGluZzogVXNlcm5hbWUiOiJDaGFuZ2VtYW5hZ2VyIiwiRW1haWwiOiJtb3VseWEucHJhZGVlcEBxMi5jb20ifQ=='|'lst':1642672340934}|'9128777581453518099':{'fst':1642672340936|'lst':1642672341607}|'7698648321401019324':{'fst':1642672341609|'ui':'eyI3IjoibW91bHlhLnByYWRlZXBAcTIuY29tIn0='|'ua':'eyJPbmJvYXJkaW5nOiBVc2VybmFtZSI6IkNoYW5nZW1hbmFnZXIiLCJPbmJvYXJkaW5nOiBDb25zZW50ZWQgdG8gUmVjZWl2ZSBFbGVjdHJvbmljIERpc2Nsb3N1cmVzIjp0cnVlLCJPbmJvYXJkaW5nOiBBY2NlcHRlZCBUZXJtcyBhbmQgQ29uZGl0aW9ucyI6dHJ1ZSwiRW1haWwiOiJtb3VseWEucHJhZGVlcEBxMi5jb20iLCIkTW9iaWxlIjoiKDQwNCkgOTY2LTU1NzYiLCIkRmlyc3ROYW1lIjoiVHJldm9yIFdpbGxpYW0iLCIkTGFzdE5hbWUiOiJGb3JyZXN0IiwiJEFkZHJlc3MiOiIxNDcgQnJlbnR3b29kIERyaXZlIiwiJENpdHkiOiJOYXNodmlsbGUiLCIkU3RhdGUiOiJUTiIsIiRaaXAiOiIzNzIxNCIsIkRPQiI6IjEwLzA5LzE5NjMifQ=='}}; cookieDeviceInfo6=Mozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/97.0.4692.99+Safari/537.36; cookieTimeoutDays6=30; cookieDeviceInfo3=Mozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/97.0.4692.99+Safari/537.36; cookieTimeoutDays3=30; cookieDeviceInfo5=Mozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/98.0.4758.80+Safari/537.36; _gid=GA1.1.127833634.1644304556\",\"mspApplicationId\":\"41\",\"employmentStatusApp2\":\"null\",\"emailValidationOverriden\":\"false\",\"lastNameApp2\":\"Null\",\"productIds\":\"[\\\"chk_summit\\\"]\",\"domain\":\"http://localhost:9090\",\"workAddr1App2\":\"Null\",\"sentKBA\":\"true\",\"idExpiresApp1\":\"11/11/2022\",\"isFramework\":\"false\",\"birthDateApp2\":\"null\",\"danalUsed\":\"false\",\"isOlbEnrolled\":\"false\",\"overdraft\":\"false\",\"OLBUserExists\":\"N\",\"workPostalApp2\":\"null\",\"authToken\":\"vMx1V\",\"willJoinCommunity\":\"false\",\"isAuthenticated\":\"false\",\"iovationBlackbox\":\"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\",\"myRealIP\":\"49.206.14.85\",\"challengeKey\":\"challengeMothersMaiden\",\"disclosuresRead\":\"\\\"{\\\\\\\"PrivacyPolicy\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1644314174516,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null},\\\\\\\"eSignature\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1644314174516,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null},\\\\\\\"ElectronicTransactions\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1644314302002,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null,\\\\\\\"attachments\\\\\\\":[]}}\\\"\",\"workCityApp2\":\"Null\",\"products\":\"[\\\"\\\\\\\"{\\\\\\\\\\\\\\\"productId\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"chk_summit\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"productName\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Summit Checking\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"productAmount\\\\\\\\\\\\\\\":0,\\\\\\\\\\\\\\\"isForFunding\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"true\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"productType\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"dda\\\\\\\\\\\\\\\"}\\\\\\\"\\\"]\",\"idNumApp2\":\"null\",\"carrierConsentId\":\"null\",\"isExistingCustomer\":\"false\",\"idIssueDateApp1\":\"11/11/2000\",\"totalApplicants\":\"1\",\"firstNameApp2\":\"Null\",\"productType\":\"dda\",\"remoteAddr\":\"0:0:0:0:0:0:0:1\",\"monthsOnJobApp2\":\"null\",\"workStateApp2\":\"null\",\"isCellphoneValidated\":\"false\",\"appAccountProducts\":\"[]\",\"custom\":\"\\\"\\\\\\\"[{\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"employedOrUnemployed\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"empOptionEmployed\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"employerName\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"q2\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\"occupation\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"se\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\"itemsover5000\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\"}]\\\\\\\"\\\"\",\"approvedEsignature\":\"true\",\"urlParams\":\"\\\"{\\\\\\\"sellerid\\\\\\\":\\\\\\\"12345\\\\\\\"}\\\"\",\"monthlyIncomeApp2\":\"null\",\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36\",\"idState\":\"WA\",\"smsValPk\":\"70\",\"middleNameApp2\":\"Null\",\"correlationID\":\"null\",\"isExistingMemberWorkflow\":\"false\",\"isFundingSourceVerified\":\"undefined\",\"carrierAuthenticationKey\":\"null\"}");
        accountMaintenanceRequest.setApplicant(applicant);

        Element account = PowerMockito.mock(Element.class);
        PowerMockito.mockStatic(Element.class);
        Document requestDocument = PowerMockito.mock(Document.class);
        Element child = PowerMockito.mock(Element.class);
        when(requestDocument.createElement(any())).thenReturn(child);
        accountMaintenanceRequest.setRequestDocument(requestDocument);
        // Execute
        accountMaintenanceRequest.addSellerAccountRoles(account);
        assertNotNull(account);
    }

    @Test
    public void testAddSellerFromURLParamAccountRolesExceptionCase() throws JSONException, FiservDNAException {
        //Arrange
        String acctRolesString = "{\n" +
                "    \"entityTypeCode\": \"PERS\",\n" +
                "    \"jointRoleCode\": [\n" +
                "      \"OWN\",\n" +
                "      \"SIGN\"\n" +
                "    ],\n" +
                "    \"minorAge\": \"18\",\n" +
                "    \"sellerIDAppUrl\": \"true\",\n" +
                "    \"sellerID\": \"12345\",\n" +
                "    \"minorRoleCode\": [\n" +
                "      \"MINR\"\n" +
                "    ],\n" +
                "    \"primaryRoleCode\": [\n" +
                "      \"\"\n" +
                "    ]\n" +
                "  }";
        JSONObject acctRoles = new JSONObject(acctRolesString);
        fiservDNASettings.setAccountRoles(acctRoles);
        accountMaintenanceRequest.setSettings(fiservDNASettings);
        Applicant applicant = this.createApplicantForTesting();
        applicant.setUnstructuredVars("{\"mothersMaidenNameApp2\":\"null\",\"isReturningUser\":\"true\",\"mobileCarrier\":\"\",\"isValidSEG\":\"false\",\"workAddr2App2\":\"Null\",\"isFundingBalanceVerified\":\"undefined\",\"applicants\":\"\\\\\"{\\\\\\\\\\\\\"1\\\\\\\\\\\\\":{\\\\\\\\\\\\\"mailAddrStateAddr\\\\\\\\\\\\\":\\\\\\\\\\\\\"\\\\\\\\\\\\\",\\\\\\\\\\\\\"firstName\\\\\\\\\\\\\":\\\\\\\\\\\\\"Test\\\\\\\\\\\\\",\\\\\\\\\\\\\"lastName\\\\\\\\\\\\\":\\\\\\\\\\\\\"Lastnam\\\\\\\\\\\\\",\\\\\\\\\\\\\"existingCustomerSignal\\\\\\\\\\\\\":\\\\\\\\\\\\\"continue\\\\\\\\\\\\\"}}\\\\\"\",\"ssnApp2\":\"null\",\"sendApp2\":\"false\",\"occupancyDuration\":\"0\",\"isNative\":\"false\",\"cart\":\"[\\\\\"chk_summit\\\\\"]\",\"jointAccount\":\"false\",\" +    " +
                "             \"\"sellerId\":\"12345\",\"isJointAuth\":\"false\",\"applicationStatusDateTime\":\"2022-02-08 09:58:07.932\",\"idTypeApp2\":\"null\",\"brand\":\"999999\",\"jobTitleApp2\":\"Null\",\"workPhoneApp2\":\"null\",\"cookie\":\"_ga=GA1.1.260219305.1633526911; _msuuid_1068lnm41522=216E6428-58A0-40BD-9E74-61DB50413672; _uetvid=4d8b4ef026a911ec999f6d4b7446eb9c; nmstat=95d2219c-5156-8406-b6df-f2b93bc05575; _mkto_trk=id:914-DOA-764&token:_mch-localhost-1633526917186-70016; calltrk_referrer=direct; calltrk_landing=http%3A//localhost%3A9090/; calltrk_session_id=3b039f09-3624-4ade-a978-d585513fc6ed; _hjid=07a7ea5e-0cff-4f82-a1d7-5d2c50511fd9; cookieTimeoutDays2=30; _hjSessionUser_1273173=eyJpZCI6IjNiNTAzZTA1LWZhNTktNWJjMC04YWQ5LWMwMTMxNzhhODFjZSIsImNyZWF0ZWQiOjE2MzgxOTM4NzM4NjUsImV4aXN0aW5nIjp0cnVlfQ==; _ga_21W6M8FD7E=GS1.1.1638350952.6.0.1638350952.0; cookieDeviceInfo2=Mozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/97.0.4692.71+Safari/537.36; mprtcl-v4_4850A2AD={'gs':{'ie':1|'dt':'b9f4a77b0f885145a56c5955fcd18444'|'cgid':'a4976610-1dc3-4a59-aa96-34110dd4ea3c'|'das':'aaf38c83-37ae-4f27-a532-667b897cb903'|'csm':'WyI3Njk4NjQ4MzIxNDAxMDE5MzI0Il0='|'ssd':1643037518840|'sid':'B2AC943E-2183-44AA-A367-CED2C8D0981A'|'les':1643037518848}|'l':0|'-6753790904480232419':{'fst':1634645756389|'ua':'eyJPbmJvYXJkaW5nOiBVc2VybmFtZSI6IkNoYW5nZW1hbmFnZXIiLCJFbWFpbCI6Im0uc3NAcTIuY29tIiwiJE1vYmlsZSI6IjA5ODc2NTQzMjEwIn0='|'lst':1634645763632}|'cu':'7698648321401019324'|'2134702038670824100':{'fst':1634645763634|'ui':'eyI3IjoibS5zc0BxMi5jb20ifQ=='|'ua':'eyJPbmJvYXJkaW5nOiBQcmltYXJ5IFNvdXJjZSBvZiBGdW5kcyI6IlNlbGYtRW1wbG95bWVudCBJbmNvbWUiLCJPbmJvYXJkaW5nOiBDb25zZW50ZWQgdG8gVzkgVGF4IERpc2NsYWltZXIiOnRydWUsIk9uYm9hcmRpbmc6IENvbnNlbnRlZCB0byBSZWNlaXZlIEVsZWN0cm9uaWMgRGlzY2xvc3VyZXMiOnRydWUsIk9uYm9hcmRpbmc6IEFjY2VwdGVkIFRlcm1zIGFuZCBDb25kaXRpb25zIjp0cnVlLCIkTW9iaWxlIjoiKDA5OCkgNzY1LTQzMjEiLCIkRmlyc3ROYW1lIjoiVGVzdCIsIiRMYXN0TmFtZSI6Ikxhc3RuYW0iLCIkQWRkcmVzcyI6IjEyMyBKZWZmZXJzb24gU3RyZWV0IiwiJENpdHkiOiJBdGxhbnRhIiwiJFN0YXRlIjoiQUwiLCIkWmlwIjoiMDI0NTMiLCJET0IiOiIxMS8xMS8xOTkwIiwiT25ib2FyZGluZzogVXNlcm5hbWUiOiJDaGFuZ2VtYW5hZ2VyIiwiRW1haWwiOiJtb3VseWEucHJhZGVlcEBxMi5jb20ifQ=='|'lst':1642672340934}|'9128777581453518099':{'fst':1642672340936|'lst':1642672341607}|'7698648321401019324':{'fst':1642672341609|'ui':'eyI3IjoibW91bHlhLnByYWRlZXBAcTIuY29tIn0='|'ua':'eyJPbmJvYXJkaW5nOiBVc2VybmFtZSI6IkNoYW5nZW1hbmFnZXIiLCJPbmJvYXJkaW5nOiBDb25zZW50ZWQgdG8gUmVjZWl2ZSBFbGVjdHJvbmljIERpc2Nsb3N1cmVzIjp0cnVlLCJPbmJvYXJkaW5nOiBBY2NlcHRlZCBUZXJtcyBhbmQgQ29uZGl0aW9ucyI6dHJ1ZSwiRW1haWwiOiJtb3VseWEucHJhZGVlcEBxMi5jb20iLCIkTW9iaWxlIjoiKDQwNCkgOTY2LTU1NzYiLCIkRmlyc3ROYW1lIjoiVHJldm9yIFdpbGxpYW0iLCIkTGFzdE5hbWUiOiJGb3JyZXN0IiwiJEFkZHJlc3MiOiIxNDcgQnJlbnR3b29kIERyaXZlIiwiJENpdHkiOiJOYXNodmlsbGUiLCIkU3RhdGUiOiJUTiIsIiRaaXAiOiIzNzIxNCIsIkRPQiI6IjEwLzA5LzE5NjMifQ=='}}; cookieDeviceInfo6=Mozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/97.0.4692.99+Safari/537.36; cookieTimeoutDays6=30; cookieDeviceInfo3=Mozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/97.0.4692.99+Safari/537.36; cookieTimeoutDays3=30; cookieDeviceInfo5=Mozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/98.0.4758.80+Safari/537.36; _gid=GA1.1.127833634.1644304556\",\"mspApplicationId\":\"41\",\"employmentStatusApp2\":\"null\",\"emailValidationOverriden\":\"false\",\"lastNameApp2\":\"Null\",\"productIds\":\"[\\\\\"chk_summit\\\\\"]\",\"domain\":\"http://localhost:9090\",\"workAddr1App2\":\"Null\",\"sentKBA\":\"true\",\"idExpiresApp1\":\"11/11/2022\",\"isFramework\":\"false\",\"birthDateApp2\":\"null\",\"danalUsed\":\"false\",\"isOlbEnrolled\":\"false\",\"overdraft\":\"false\",\"OLBUserExists\":\"N\",\"workPostalApp2\":\"null\",\"authToken\":\"vMx1V\",\"willJoinCommunity\":\"false\",\"isAuthenticated\":\"false\",\"iovationBlackbox\":\"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\",\"myRealIP\":\"49.206.14.85\",\"challengeKey\":\"challengeMothersMaiden\",\"disclosuresRead\":\"\\\\\"{\\\\\\\\\\\\\"PrivacyPolicy\\\\\\\\\\\\\":{\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\":true,\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\":1644314174516,\\\\\\\\\\\\\"read\\\\\\\\\\\\\":false,\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\":null},\\\\\\\\\\\\\"eSignature\\\\\\\\\\\\\":{\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\":true,\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\":1644314174516,\\\\\\\\\\\\\"read\\\\\\\\\\\\\":false,\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\":null},\\\\\\\\\\\\\"ElectronicTransactions\\\\\\\\\\\\\":{\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\":true,\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\":1644314302002,\\\\\\\\\\\\\"read\\\\\\\\\\\\\":false,\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\":null,\\\\\\\\\\\\\"attachments\\\\\\\\\\\\\":[]}}\\\\\"\",\"workCityApp2\":\"Null\",\"products\":\"[\\\\\"\\\\\\\\\\\\\"{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productId\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"chk_summit\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productName\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"Summit Checking\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productAmount\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":0,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"isForFunding\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"true\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productType\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"dda\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}\\\\\\\\\\\\\"\\\\\"]\",\"idNumApp2\":\"null\",\"carrierConsentId\":\"null\",\"isExistingCustomer\":\"false\",\"idIssueDateApp1\":\"11/11/2000\",\"totalApplicants\":\"1\",\"firstNameApp2\":\"Null\",\"productType\":\"dda\",\"remoteAddr\":\"0:0:0:0:0:0:0:1\",\"monthsOnJobApp2\":\"null\",\"workStateApp2\":\"null\",\"isCellphoneValidated\":\"false\",\"appAccountProducts\":\"[]\",\"custom\":\"\\\\\"\\\\\\\\\\\\\"[{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"employedOrUnemployed\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"empOptionEmployed\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"employerName\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"q2\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"occupation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"se\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"itemsover5000\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}]\\\\\\\\\\\\\"\\\\\"\",\"approvedEsignature\":\"true\",\"monthlyIncomeApp2\":\"null\",\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36\",\"idState\":\"WA\",\"smsValPk\":\"70\",\"middleNameApp2\":\"Null\",\"correlationID\":\"null\",\"isExistingMemberWorkflow\":\"false\",\"isFundingSourceVerified\":\"undefined\",\"carrierAuthenticationKey\":\"null\"}");
        accountMaintenanceRequest.setApplicant(applicant);

        Element account = PowerMockito.mock(Element.class);
        PowerMockito.mockStatic(Element.class);
        Document requestDocument = PowerMockito.mock(Document.class);
        Element child = PowerMockito.mock(Element.class);
        when(requestDocument.createElement(any())).thenReturn(child);
        accountMaintenanceRequest.setRequestDocument(requestDocument);
        // Execute
        accountMaintenanceRequest.addSellerAccountRoles(account);
        assertNotNull(account);
    }

    @Test
    public void testAddMailingAddress() {
        //Arrange
        Element account = PowerMockito.mock(Element.class);
        PowerMockito.mockStatic(Element.class);
        Document requestDocument = PowerMockito.mock(Document.class);
        Element child = PowerMockito.mock(Element.class);
        when(requestDocument.createElement(any())).thenReturn(child);
        accountMaintenanceRequest.setRequestDocument(requestDocument);

        primaryPersonInfo = new PersonInfo();
        primaryPersonInfo.setMailAddr1("321 street");
        primaryPersonInfo.setMailAddr2("NY");
        primaryPersonInfo.setMailCity("New York");
        primaryPersonInfo.setMailState("PA");
        primaryPersonInfo.setMailPostal("12121");
        accountMaintenanceRequest.setPrimaryPersonInfo(primaryPersonInfo);
        // Execute
        accountMaintenanceRequest.addMailingAddress(account);
        assertNotNull(account);
    }

}
