package com.msp.acct.workflow.integrations.fiserv.dna;

import com.google.gson.Gson;
import com.msp.acct.data.Applicant;
import com.msp.acct.workflow.integrations.ApplicantBased;
import com.msp.acct.workflow.integrations.fiserv.dna.beans.PersonInfo;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * (c) 2016-2017 Gro Solutions, Inc. All Rights Reserved.
 * <p>
 * Unit test for Fiserv Person Maintenance Request
 *
 * @author Moulya Pradeep
 * @since 5/11/2021
 */

public class PersonMaintenanceRequestTest extends ApplicantBased {


    //Tells Mockito to create the mocks based on the @Mock annotation
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Element child;

    @Mock
    Element personElement;

    @Mock
    Document requestDocument;

    private CommonRequest personMaintenanceRequest = new PersonMaintenanceRequest();

    private FiservDNASettings fiservDNASettings;

    @Mock
    FiservDNASettings mockFiservDNASettings;


    CommonRequest pmSpy;

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
                "            \"EMPR\": \"<EMPLOYER_NAME>\",\n" +
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
                "    \"minorRoleCode\": [\n" +
                "      \"MINR\"\n" +
                "    ],\n" +
                "    \"primaryRoleCode\": [\n" +
                "      \"\"\n" +
                "    ]\n" +
                "  }";
        JSONObject acctRoles = new JSONObject(acctRolesString);
        fiservDNASettings.setAccountRoles(acctRoles);


        personMaintenanceRequest.setSettings(fiservDNASettings);
        pmSpy = Mockito.spy(personMaintenanceRequest);

    }

    private String getPersonalinfo() {
        return "{\n" +
                "  \"firstName\": \"Test\",\n" +
                "  \"middleName\": \"\",\n" +
                "  \"lastName\": \"Lastnam\",\n" +
                "  \"ssn\": \"111111112\",\n" +
                "  \"birthDate\": \"11/11/2000\",\n" +
                "  \"mothersMaidenName\": \"mom\",\n" +
                "  \"idType\": \"driversLicense\",\n" +
                "  \"idState\": \"MA\",\n" +
                "  \"idIssued\": \"11/11/2000\",\n" +
                "  \"idExpires\": \"11/11/2022\",\n" +
                "  \"idNumber\": \"222222\",\n" +
                "  \"employmentStatus\": \"Employed\",\n" +
                "  \"employerPhone\": \"\",\n" +
                "  \"jobTitle\": \"Sse\",\n" +
                "  \"monthsOnJob\": 0,\n" +
                "  \"addr1\": \"123 Jefferson Street\",\n" +
                "  \"city\": \"Atlanta\",\n" +
                "  \"state\": \"MA\",\n" +
                "  \"postal\": \"02453\",\n" +
                "  \"workAddr1\": \"addr1\",\n" +
                "  \"workAddr2\": \"addr2\",\n" +
                "  \"mailAddr1\": \"mailAddr1\",\n" +
                "  \"mailAddr2\": \"mailAddr2\",\n" +
                "  \"mailCity\": \"mailCity\",\n" +
                "  \"mailState\": \"mailState\",\n" +
                "  \"mailPostal\": \"mailPostal\",\n" +
                "  \"emailId\": \"m.ss@q2.com\",\n" +
                "  \"workCity\": \"city\",\n" +
                "  \"workState\": \"state\",\n" +
                "  \"workPostal\": \"postal\",\n" +
                "  \"company\": \"Q2\",\n" +
                "  \"mobilePhone\": \"(098) 765-4321\",\n" +
                "  \"homePhone\": \"\",\n" +
                "  \"workPhone\": \"\",\n" +
                "  \"occupancyDuration\": 0,\n" +
                "  \"monthlyRent\": \"\",\n" +
                "  \"employerName\": \"\",\n" +
                "  \"middleInitial\": \"\"\n" +
                "}";
    }

    private String getMockedXML() {
        return "<TransactioncommitRollbackYN=\"Y\"reqId=\"\"><Input><UserAuthenticationauthTyp=\"2\"><UserId>CMCDEP</UserId><Passwd/>" +
                "<ApplNbr>13804</ApplNbr><PhysAddr>tc-1-1-45</PhysAddr><LocOrgNbr/></UserAuthentication><Requests><RequestreqNbr=\"\"reqTypCd=\"7707\">" +
                "<Persons><PersonNbr=\"\"><Name><FirstName>Test</FirstName><LastName>Lastnam</LastName></Name><Addresses><AddressAddrUseCd=\"PRI\">" +
                "<CountryCtryCd=\"USA\"/><CityName>Atlanta</CityName><State>AL</State><ZipCd>02453</ZipCd><ZipSuf/><AddressLines>" +
                "<AddressLineAddrLineTypCd=\"ST\">123JeffersonStreet</AddressLine></AddressLines></Address><AddressAddrUseCd=\"HEML\">" +
                "<CountryCtryCd=\"USA\"/><CityName>Atlanta</CityName><State>AL</State><AddressLines><AddressLineAddrLineTypCd=\"ATTN\">m.ss@q2.com</AddressLine>" +
                "</AddressLines></Address></Addresses><Phones><Phone><Usage>NCEL</Usage><AreaCd>098</AreaCd><Exchange>765</Exchange><Number>4321</Number>" +
                "<CountryCtryCd=\"USA\"/></Phone></Phones><Identification><TaxpayerNbr=\"111111112\"><DateTaxIdApplyFORMAT=\"MM/dd/yyyy\"/>" +
                "<DateTaxCertFORMAT=\"MM/dd/yyyy\">05/07/2021</DateTaxCert><TaxRptForOwnYN>N</TaxRptForOwnYN><TaxRptForSigYN>N</TaxRptForSigYN>" +
                "<FrgnCertExpDateFORMAT=\"MM/dd/yyyy\"/></Taxpayer><CustKeyWord>mom</CustKeyWord><DateBirthFORMAT=\"MM/dd/yyyy\">11/11/1992</DateBirth>" +
                "</Identification><PersIds><PersIdPersIdTypCd=\"1\"><CountryCtryCd=\"USA\"/><StateStateCd=\"AL\"/><IssueDateFORMAT=\"MM/dd/yyyy\">11/11/2000</IssueDate>" +
                "<ExpireDateFORMAT=\"MM/dd/yyyy\">11/11/2022</ExpireDate><IdValue>222222</IdValue></PersId></PersIds><Demographics/><UserFields>" +
                "<UserFieldUserFieldCd=\"OCCU\">Sse</UserField><UserFieldUserFieldCd=\"MSEG\">O#5409</UserField><UserFieldUserFieldCd=\"MMN\">mom</UserField>" +
                "</UserFields><PersTyps><PersTyppersTypCd=\"NMBR\"/></PersTyps><AcctRoles><AcctRoleAcctRoleCd=\"OWN\"EntityTypCd=\"PERS\"/>" +
                "<AcctRoleAcctRoleCd=\"SIGN\"EntityTypCd=\"PERS\"/></AcctRoles></Person></Persons></Request></Requests></Input></Transaction>";
    }


    @Test
    public void testBuildRequestForJointApplicant() throws FiservDNAException {
        String mockedXML = getMockedXML();
        String personInfoMocked = getPersonalinfo();

        // Prepare
        pmSpy.applicant = this.createApplicantForTesting();
        when(pmSpy.getXmlString()).thenReturn(mockedXML);
        PersonInfo personInfo = new Gson().fromJson(personInfoMocked, PersonInfo.class);
        Mockito.doReturn(personInfo).when(pmSpy).getActivePersonalInfo();

        //Execute
        boolean result = pmSpy.build(true);
        assertTrue(result);
    }

    @Test
    public void testGenerateFlexFieldsForPersonPersTypeCase1() throws Exception {
        // Arrange
        PowerMockito.mockStatic(Element.class);

        Document requestDocument = PowerMockito.mock(Document.class);

        Element child = PowerMockito.mock(Element.class);
        when(requestDocument.createElement(any())).thenReturn(child);

        personMaintenanceRequest.setRequestDocument(requestDocument);

        Element personElement = PowerMockito.mock(Element.class);

        when(requestDocument.createElement(any())).thenReturn(personElement);

        PowerMockito.mockStatic(Document.class);

        //Execute
        personMaintenanceRequest.generateFlexFieldsForPersonPersType(personElement, "person.persTyp","person.persTyp.default", "PersTyps", "PersTyp", "persTypCd", false);
    }

    @Test
    public void testGenerateFlexFieldsForPersonPersTypeCase2() throws Exception {
        // Arrange
        PowerMockito.mockStatic(Element.class);
        String flexFieldsString = "{\n" +
                "    \"person.persTyp.default\": {\n" +
                "      \"PRIMARYPERSTYPE\": \"MMBR\"\n" +

                "    },\n" +
                "    \"person.persTyp\": {\n" +
                "      \"PRIMARYPERSTYPE\": \"PRIMARY\"\n" +

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
        personMaintenanceRequest.setSettings(fiservDNASettings);
        Document requestDocument = PowerMockito.mock(Document.class);

        Element child = PowerMockito.mock(Element.class);
        when(requestDocument.createElement(any())).thenReturn(child);

        personMaintenanceRequest.setRequestDocument(requestDocument);

        Element personElement = PowerMockito.mock(Element.class);

        when(requestDocument.createElement(any())).thenReturn(personElement);

        PowerMockito.mockStatic(Document.class);

        //Execute
        personMaintenanceRequest.generateFlexFieldsForPersonPersType(personElement, "person.persTyp","person.persTyp.default", "PersTyps", "PersTyp", "persTypCd", false);
    }

    @Test
    public void testGenerateFlexFieldsForPersonPersTypeCase3() throws Exception {
        // Arrange
        PowerMockito.mockStatic(Element.class);
        String flexFieldsString = "{\n" +
                "    \"person.persTyp.default\": {\n" +
                "      \"JOINTPERSTYPE\": \"NMBR\"\n" +
                "    },\n" +
                "    \"person.persTyp\": {\n" +
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
        personMaintenanceRequest.setSettings(fiservDNASettings);
        Document requestDocument = PowerMockito.mock(Document.class);

        Element child = PowerMockito.mock(Element.class);
        when(requestDocument.createElement(any())).thenReturn(child);

        personMaintenanceRequest.setRequestDocument(requestDocument);

        Element personElement = PowerMockito.mock(Element.class);

        when(requestDocument.createElement(any())).thenReturn(personElement);

        PowerMockito.mockStatic(Document.class);

        //Execute
        personMaintenanceRequest.generateFlexFieldsForPersonPersType(personElement, "person.persTyp","person.persTyp.default", "PersTyps", "PersTyp", "persTypCd", false);
    }



    @Test
    public void testGenerateFlexFieldsForPersonUserFields() throws Exception {

        when(requestDocument.createElement(any())).thenReturn(child);
        personMaintenanceRequest.setRequestDocument(requestDocument);

        String personInfoMocked = getPersonalinfo();
        PersonInfo personInfo = new Gson().fromJson(personInfoMocked, PersonInfo.class);
        personMaintenanceRequest.setActivePersonalInfo(personInfo);

        when(requestDocument.createElement(any())).thenReturn(personElement);

        PowerMockito.mockStatic(Document.class);

        //Execute
        personMaintenanceRequest.generateFlexFieldsForPersonUserFields(personElement, "person.userFields", "UserFields", "UserField", "UserFieldCd", false);


        // Test condition for UV occupation and employer

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
                "            \"MSEG\": \"<CUSTOM:UV:SEGcode>\",\n" +
                "            \"OCCU\": \"<CUSTOM:UV:occupation>\",\n" +
                "            \"EMPR\": \"<CUSTOM:UV:employerName>\"\n" +
                "        }}" ;
        JSONObject flexFields = new JSONObject(flexFieldsString);
        fiservDNASettings = new  FiservDNASettings();
        fiservDNASettings.setFlexFields(flexFields);
        personMaintenanceRequest.setSettings(fiservDNASettings);
        String s ="{\"mspApplicationId\":83,\"timestamp\":\"Aug 16, 2021 9:29:18 AM\",\"isFinal\":false,\"smsTxnId\":\"deb60a21-8471-49b0-a1d0-2362ff82ead6\",\"emailTxnId\":\"d9dfcf28-5322-4324-84f6-b0a51283ffeb\",\"lat\":0.0,\"lon\":0.0,\"addr1\":\"123 Jefferson Street\",\"addr2\":\"\",\"city\":\"Atlanta\",\"state\":\"WA\",\"postal\":\"02453\",\"email\":\"m.ss@q2.com\",\"company\":\"\",\"isCuMember\":false,\"isValidSEG\":false,\"willJoinCommunity\":false,\"firstName\":\"Test\",\"middleName\":\"\",\"lastName\":\"Lastnam\",\"suffix\":\"\",\"mobilePhone\":\"0987654321\",\"internalComments\":\"\\r\\n08-16-2021 09:20:04 AM   PST - Returning customer detected, Related Application ids: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 77, 78, 79, 80, 81, 82].\\r\\n08-16-2021 09:28:55 AM   DNA - Check Existing Customer: Existing Customer Not found - Primary: Test Lastnam\\r\\n08-16-2021 09:29:36 AM   ALY - Identity Verification - Test Lastnam: Applicant \\u003cstrong\\u003epassed\\u003c/strong\\u003e Identity Verification with outcome: \\u003cstrong\\u003eApproved\\u003c/strong\\u003e and KBA Challenge Questions are not required. Click here – \\u003ca target\\u003d\\\"blank\\\" href\\u003d\\\"https://app.alloy.co/login/\\\"\\u003ehttps://app.alloy.co/login/\\u003c/a\\u003e – to log into Alloy for more information.\",\"queueId\":2,\"jointAccount\":false,\"firstNameApp2\":\"\",\"middleNameApp2\":\"\",\"lastNameApp2\":\"\",\"suffixApp2\":\"\",\"ssnApp2\":\"\",\"birthDateApp2\":\"\",\"mothersMaidenNameApp2\":\"\",\"idTypeApp2\":\"\",\"idNumberApp2\":\"\",\"isSmsValidated\":false,\"isEmailValidated\":true,\"idType\":\"driversLicense\",\"idNumber\":\"11111\",\"monthsOnJob\":0,\"birthDate\":\"11/11/1990\",\"ssn\":\"111111111\",\"mothersMaidenName\":\"mom\",\"privacyApproved\":true,\"esigApproved\":false,\"etxnApproved\":false,\"jobTitle\":\"\",\"workAddr1\":\"\",\"workAddr2\":\"\",\"workCity\":\"\",\"workState\":\"\",\"workPostal\":\"\",\"disclosuresRead\":\"\\\"{\\\\\\\"PrivacyPolicy\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1629105596644,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null},\\\\\\\"eSignature\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1629105596644,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null},\\\\\\\"ElectronicTransactions\\\\\\\":{\\\\\\\"clicked\\\\\\\":false,\\\\\\\"clickedTimeStamp\\\\\\\":null,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null,\\\\\\\"attachments\\\\\\\":[]}}\\\"\",\"unstructuredVars\":\"{\\\"mothersMaidenNameApp2\\\":\\\"null\\\",\\\"isReturningUser\\\":\\\"true\\\",\\\"mobileCarrier\\\":\\\"\\\",\\\"isValidSEG\\\":\\\"false\\\",\\\"workAddr2App2\\\":\\\"Null\\\",\\\"isFundingBalanceVerified\\\":\\\"undefined\\\",\\\"applicants\\\":\\\"\\\\\\\"{\\\\\\\\\\\\\\\"1\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"mailAddrStateAddr\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"firstName\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Test\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"lastName\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Lastnam\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"existingCustomerSignal\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"continue\\\\\\\\\\\\\\\"}}\\\\\\\"\\\",\\\"ssnApp2\\\":\\\"null\\\",\\\"sendApp2\\\":\\\"false\\\",\\\"occupancyDuration\\\":\\\"0\\\",\\\"isNative\\\":\\\"false\\\",\\\"cart\\\":\\\"[\\\\\\\"chk_summit\\\\\\\"]\\\",\\\"jointAccount\\\":\\\"false\\\",\\\"isJointAuth\\\":\\\"false\\\",\\\"applicationStatusDateTime\\\":\\\"2021-08-16 09:29:17.721\\\",\\\"idTypeApp2\\\":\\\"null\\\",\\\"brand\\\":\\\"999999\\\",\\\"jobTitleApp2\\\":\\\"Null\\\",\\\"workPhoneApp2\\\":\\\"null\\\",\\\"cookie\\\":\\\"_ga\\u003dGA1.1.1177092643.1628530779; wcuId\\u003d887936333; cookieTimeoutDays2\\u003d30; _gcl_au\\u003d1.1.2057365608.1628836560; _msuuid_1068lnm41522\\u003dDAB08AFF-5146-48B1-922A-88E25D8C4519; _uetvid\\u003db9e89920fc0011eba61e032f80784591; nmstat\\u003d3a2ddea7-81fa-c7c7-4a2b-675eaf70cdb4; _mkto_trk\\u003did:914-DOA-764\\u0026token:_mch-localhost-1628836562436-63289; _gid\\u003dGA1.1.1069097507.1629090303; _uetsid\\u003d84114080fe4f11eb95ed55d74516ede7; cookieDeviceInfo2\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieDeviceInfo6\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieTimeoutDays6\\u003d30; cookieDeviceInfo3\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieTimeoutDays3\\u003d30\\\",\\\"mspApplicationId\\\":\\\"83\\\",\\\"employmentStatusApp2\\\":\\\"null\\\",\\\"emailValidationOverriden\\\":\\\"false\\\",\\\"lastNameApp2\\\":\\\"Null\\\",\\\"productIds\\\":\\\"[\\\\\\\"chk_summit\\\\\\\"]\\\",\\\"domain\\\":\\\"http://localhost:9090\\\",\\\"workAddr1App2\\\":\\\"Null\\\",\\\"sentKBA\\\":\\\"true\\\",\\\"idExpiresApp1\\\":\\\"11/11/2022\\\",\\\"isFramework\\\":\\\"false\\\",\\\"birthDateApp2\\\":\\\"null\\\",\\\"danalUsed\\\":\\\"false\\\",\\\"isOlbEnrolled\\\":\\\"false\\\",\\\"overdraft\\\":\\\"false\\\",\\\"workPostalApp2\\\":\\\"null\\\",\\\"authToken\\\":\\\"nwnLB\\\",\\\"willJoinCommunity\\\":\\\"false\\\",\\\"isAuthenticated\\\":\\\"false\\\",\\\"iovationBlackbox\\\":\\\"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\\u003d\\\",\\\"myRealIP\\\":\\\"199.231.199.20\\\",\\\"challengeKey\\\":\\\"challengeMothersMaiden\\\",\\\"disclosuresRead\\\":\\\"\\\\\\\"{\\\\\\\\\\\\\\\"PrivacyPolicy\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629105596644,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null},\\\\\\\\\\\\\\\"eSignature\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629105596644,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null},\\\\\\\\\\\\\\\"ElectronicTransactions\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629106172705,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null,\\\\\\\\\\\\\\\"attachments\\\\\\\\\\\\\\\":[]}}\\\\\\\"\\\",\\\"workCityApp2\\\":\\\"Null\\\",\\\"products\\\":\\\"[\\\\\\\"\\\\\\\\\\\\\\\"{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productId\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"chk_summit\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productName\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"Summit Checking\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productAmount\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":0,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"isForFunding\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"true\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productType\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"dda\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}\\\\\\\\\\\\\\\"\\\\\\\"]\\\",\\\"idNumApp2\\\":\\\"null\\\",\\\"carrierConsentId\\\":\\\"null\\\",\\\"isExistingCustomer\\\":\\\"false\\\",\\\"idIssueDateApp1\\\":\\\"11/11/2000\\\",\\\"totalApplicants\\\":\\\"1\\\",\\\"firstNameApp2\\\":\\\"Null\\\",\\\"productType\\\":\\\"dda\\\",\\\"remoteAddr\\\":\\\"0:0:0:0:0:0:0:1\\\",\\\"monthsOnJobApp2\\\":\\\"null\\\",\\\"workStateApp2\\\":\\\"null\\\",\\\"isCellphoneValidated\\\":\\\"false\\\",\\\"appAccountProducts\\\":\\\"[]\\\",\\\"custom\\\":\\\"\\\\\\\"\\\\\\\\\\\\\\\"[{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"employedOrUnemployed\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"empOptionEmployed\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"employerName\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"q2\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"occupation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"se\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"frequentTraveler\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"travelOutsideUs\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"safeDepositBox\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"declaredBehavior\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositExceed5000\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"sourceOfFunds\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"SOFGift\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositOrWriteChecks\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositOrWithdrawCash\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willSendOrReceiveWireTransfers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willsendReceiveNonWireTransfers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"declaredBehavior\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"nonresident\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"usCitizen\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":true},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"haveDualCitizenship\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"areYouPEP\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"relationshipToPEP\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"PEPNo\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"foreignEmployee\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"nonresident\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}]\\\\\\\\\\\\\\\"\\\\\\\"\\\",\\\"approvedEsignature\\\":\\\"true\\\",\\\"monthlyIncomeApp2\\\":\\\"null\\\",\\\"userAgent\\\":\\\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36\\\",\\\"idState\\\":\\\"WA\\\",\\\"smsValPk\\\":\\\"168\\\",\\\"middleNameApp2\\\":\\\"Null\\\",\\\"correlationID\\\":\\\"null\\\",\\\"isExistingMemberWorkflow\\\":\\\"false\\\",\\\"isFundingSourceVerified\\\":\\\"undefined\\\",\\\"carrierAuthenticationKey\\\":\\\"null\\\"}\",\"idFront\":\"\",\"idBack\":\"\",\"ipAddress\":\"\",\"employmentStatus\":\"\",\"employmentStatusApp2\":\"\",\"employerPhone\":\"\",\"employerPhoneApp2\":\"\",\"jobTitleApp2\":\"\",\"monthsOnJobApp2\":0,\"workAddr1App2\":\"\",\"workAddr2App2\":\"\",\"workCityApp2\":\"\",\"workStateApp2\":\"\",\"workPostalApp2\":\"\",\"companyApp2\":\"\",\"workPhoneApp1\":\"\",\"workPhoneApp2\":\"\",\"occupancyDuration\":0,\"occupancyDurationApp2\":0,\"occupancyStatusApp1\":\"\",\"productIds\":[],\"idState\":\"WA\",\"isNative\":false,\"isFramework\":false,\"sendApp2\":false,\"webAppAuthKey\":\"\",\"webAppAuthCode\":\"\",\"webAppAuthRef\":\"\",\"webAppAuthTime\":\"\",\"promoCode\":\"\",\"fundingSuccess\":false,\"isFundingSourceVerified\":false,\"overdraft\":false,\"created\":\"08-16-2021 09:29 AM\",\"newAccounts\":{},\"bankState\":\"\",\"bankName\":\"\",\"fundingType\":\"\",\"frontEndFunding\":\"\",\"bankAccountType\":\"\",\"transferAccountType\":\"\",\"nameOnCard\":\"\",\"routingNumber\":\"\",\"bankAccountNumber\":\"\",\"ccCardType\":\"\",\"ccCardNumber\":\"\",\"ccCardLast4\":\"\",\"ccCardExpDateMonth\":\"\",\"ccCardExpDateYear\":\"\",\"ccCvnNumber\":\"0\",\"ccChargeFailureCount\":0,\"amountDeposit\":0.0,\"bankNameOnCard\":\"\",\"transferAccountNumber\":\"\",\"billingStreetAddress\":\"\",\"billingCity\":\"\",\"billingState\":\"\",\"billingZip\":\"\",\"sourceOfFunds\":\"\",\"futureFundingFirstName\":\"\",\"futureFundingLastName\":\"\",\"futureFundingMiddleName\":\"\",\"futureFundingSuffix\":\"\",\"futureFundingSsn\":\"\",\"futureFundingIraType\":\"\",\"futureFundingContributionType\":\"\",\"futureFundingAccountNumber\":\"\",\"fundingSourceId\":\"\",\"clfFundingSourceId\":\"\",\"microdepositAmount1\":0.0,\"microdepositAmount2\":0.0,\"microdepositFailureCount\":0,\"microdepositStatus\":\"\",\"paypalPaykey\":\"\",\"signature\":\"\",\"guidId\":\"\",\"clfApprovedAccountTypeId\":\"\",\"clfInternalTransferId\":\"\",\"isCreated\":\"\",\"checkRoutingNumber\":\"\",\"fundingDate\":\"\",\"fundedStatus\":\"\",\"isRateOverride\":\"\",\"description\":\"\",\"serviceType\":\"\",\"serviceCode\":\"\",\"benefactorType\":\"\",\"beneficiaryPriority\":\"\",\"beneficiaryPercentShare\":0.0,\"paymentFrequency\":\"\",\"paymentAccountNumber\":\"\",\"paymentAccountType\":\"\",\"paymentRoutingNumber\":\"\",\"paymentFiName\":\"\",\"paymentFiCity\":\"\",\"paymentFiState\":\"\",\"paymentFiZip\":\"\",\"isInterestRateAnException\":\"\",\"iraType\":\"\",\"contributionType\":\"\",\"contributionYear\":\"\",\"isPrimaryRetirementAccount\":\"\",\"custodianName\":\"\",\"custodianAddress\":\"\",\"custodianCity\":\"\",\"custodianState\":\"\",\"custodianZip\":\"\",\"custodianPhone\":\"\",\"accountTransferringFrom\":\"\",\"transferFromType\":\"\",\"transferPercent\":0.0,\"closeIra\":\"\",\"placeInConduitIra\":\"\",\"assetDescription\":\"\",\"amountInIra\":0.0,\"amountToBeTransferred\":0.0,\"transferInstruction\":\"\",\"requiredMinDistribution\":\"\",\"canChangeBeneficiary\":\"\",\"continueServiceAfterAgeOfMaturity\":\"\",\"depositYear\":\"\",\"approvedAccountTypeLinkId\":\"\",\"referenceType\":\"\",\"referenceId\":\"\",\"isLinked\":\"\",\"dateLinked\":\"\",\"directDepositType\":\"\",\"applicationStatus\":\"incomplete\",\"applicationStatusDateTime\":\"Aug 16, 2021 9:20:05 AM\",\"appAccountType\":\"\",\"isAccountOpened\":false,\"offersRequired\":false,\"primaryShareAccountAmountDeposit\":0.0,\"chkGoldAmountDeposit\":0.0,\"wingsFoundationAmountDeposit\":0.0,\"backupWithHoldingFlag\":false,\"skipKBA\":false,\"skipDecisioning\":false,\"isAuthenticated\":false,\"existingCustomer\":false,\"relationshipId\":\"\",\"webAppResponseTokenValue\":\"\",\"webAppResponseTokenName\":\"\",\"webAppResponseTokenType\":\"\",\"webAppResponseTokenExpiry\":\"\",\"webAppResponseTokenSecNum\":\"\",\"webAppResponseErrorMessages\":\"\",\"achPPDEntry\":\"\",\"achFileStatus\":\"\",\"products\":[],\"customPages\":[]}";
        personMaintenanceRequest.applicant = new Gson().fromJson(s, Applicant.class);

        //Execute
        personMaintenanceRequest.generateFlexFieldsForPersonUserFields(personElement, "person.userFields", "UserFields", "UserField", "UserFieldCd", false);


    }

    @Test
    public void testGenerateFlexFieldsForEmployerNameAndOccupation() throws Exception {

        when(requestDocument.createElement(any())).thenReturn(child);
        personMaintenanceRequest.setRequestDocument(requestDocument);

        String personInfoMocked = getPersonalinfo();
        PersonInfo personInfo = new Gson().fromJson(personInfoMocked, PersonInfo.class);
        personMaintenanceRequest.setActivePersonalInfo(personInfo);

        when(requestDocument.createElement(any())).thenReturn(personElement);


        // Test condition for occupation and employer

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
                "            \"OCCU\": \"<OCCUPATION>\",\n" +
                "            \"EMPR\": \"<EMPLOYER_NAME>\"\n" +
                "        }}";
        JSONObject flexFields = new JSONObject(flexFieldsString);
        fiservDNASettings = new FiservDNASettings();
        fiservDNASettings.setFlexFields(flexFields);
        personMaintenanceRequest.setSettings(fiservDNASettings);
        personInfo.setJobTitle("");
        String s = "{\"mspApplicationId\":83,\"timestamp\":\"Aug 16, 2021 9:29:18 AM\",\"isFinal\":false,\"smsTxnId\":\"deb60a21-8471-49b0-a1d0-2362ff82ead6\",\"emailTxnId\":\"d9dfcf28-5322-4324-84f6-b0a51283ffeb\",\"lat\":0.0,\"lon\":0.0,\"addr1\":\"123 Jefferson Street\",\"addr2\":\"\",\"city\":\"Atlanta\",\"state\":\"WA\",\"postal\":\"02453\",\"email\":\"m.ss@q2.com\",\"company\":\"\",\"isCuMember\":false,\"isValidSEG\":false,\"willJoinCommunity\":false,\"firstName\":\"Test\",\"middleName\":\"\",\"lastName\":\"Lastnam\",\"suffix\":\"\",\"mobilePhone\":\"0987654321\",\"internalComments\":\"\\r\\n08-16-2021 09:20:04 AM   PST - Returning customer detected, Related Application ids: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 77, 78, 79, 80, 81, 82].\\r\\n08-16-2021 09:28:55 AM   DNA - Check Existing Customer: Existing Customer Not found - Primary: Test Lastnam\\r\\n08-16-2021 09:29:36 AM   ALY - Identity Verification - Test Lastnam: Applicant \\u003cstrong\\u003epassed\\u003c/strong\\u003e Identity Verification with outcome: \\u003cstrong\\u003eApproved\\u003c/strong\\u003e and KBA Challenge Questions are not required. Click here – \\u003ca target\\u003d\\\"blank\\\" href\\u003d\\\"https://app.alloy.co/login/\\\"\\u003ehttps://app.alloy.co/login/\\u003c/a\\u003e – to log into Alloy for more information.\",\"queueId\":2,\"jointAccount\":false,\"firstNameApp2\":\"\",\"middleNameApp2\":\"\",\"lastNameApp2\":\"\",\"suffixApp2\":\"\",\"ssnApp2\":\"\",\"birthDateApp2\":\"\",\"mothersMaidenNameApp2\":\"\",\"idTypeApp2\":\"\",\"idNumberApp2\":\"\",\"isSmsValidated\":false,\"isEmailValidated\":true,\"idType\":\"driversLicense\",\"idNumber\":\"11111\",\"monthsOnJob\":0,\"birthDate\":\"11/11/1990\",\"ssn\":\"111111111\",\"mothersMaidenName\":\"mom\",\"privacyApproved\":true,\"esigApproved\":false,\"etxnApproved\":false,\"jobTitle\":\"\",\"workAddr1\":\"\",\"workAddr2\":\"\",\"workCity\":\"\",\"workState\":\"\",\"workPostal\":\"\",\"disclosuresRead\":\"\\\"{\\\\\\\"PrivacyPolicy\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1629105596644,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null},\\\\\\\"eSignature\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1629105596644,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null},\\\\\\\"ElectronicTransactions\\\\\\\":{\\\\\\\"clicked\\\\\\\":false,\\\\\\\"clickedTimeStamp\\\\\\\":null,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null,\\\\\\\"attachments\\\\\\\":[]}}\\\"\",\"unstructuredVars\":\"{\\\"mothersMaidenNameApp2\\\":\\\"null\\\",\\\"isReturningUser\\\":\\\"true\\\",\\\"mobileCarrier\\\":\\\"\\\",\\\"isValidSEG\\\":\\\"false\\\",\\\"workAddr2App2\\\":\\\"Null\\\",\\\"isFundingBalanceVerified\\\":\\\"undefined\\\",\\\"applicants\\\":\\\"\\\\\\\"{\\\\\\\\\\\\\\\"1\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"mailAddrStateAddr\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"firstName\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Test\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"lastName\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Lastnam\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"existingCustomerSignal\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"continue\\\\\\\\\\\\\\\"}}\\\\\\\"\\\",\\\"ssnApp2\\\":\\\"null\\\",\\\"sendApp2\\\":\\\"false\\\",\\\"occupancyDuration\\\":\\\"0\\\",\\\"isNative\\\":\\\"false\\\",\\\"cart\\\":\\\"[\\\\\\\"chk_summit\\\\\\\"]\\\",\\\"jointAccount\\\":\\\"false\\\",\\\"isJointAuth\\\":\\\"false\\\",\\\"applicationStatusDateTime\\\":\\\"2021-08-16 09:29:17.721\\\",\\\"idTypeApp2\\\":\\\"null\\\",\\\"brand\\\":\\\"999999\\\",\\\"jobTitleApp2\\\":\\\"Null\\\",\\\"workPhoneApp2\\\":\\\"null\\\",\\\"cookie\\\":\\\"_ga\\u003dGA1.1.1177092643.1628530779; wcuId\\u003d887936333; cookieTimeoutDays2\\u003d30; _gcl_au\\u003d1.1.2057365608.1628836560; _msuuid_1068lnm41522\\u003dDAB08AFF-5146-48B1-922A-88E25D8C4519; _uetvid\\u003db9e89920fc0011eba61e032f80784591; nmstat\\u003d3a2ddea7-81fa-c7c7-4a2b-675eaf70cdb4; _mkto_trk\\u003did:914-DOA-764\\u0026token:_mch-localhost-1628836562436-63289; _gid\\u003dGA1.1.1069097507.1629090303; _uetsid\\u003d84114080fe4f11eb95ed55d74516ede7; cookieDeviceInfo2\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieDeviceInfo6\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieTimeoutDays6\\u003d30; cookieDeviceInfo3\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieTimeoutDays3\\u003d30\\\",\\\"mspApplicationId\\\":\\\"83\\\",\\\"employmentStatusApp2\\\":\\\"null\\\",\\\"emailValidationOverriden\\\":\\\"false\\\",\\\"lastNameApp2\\\":\\\"Null\\\",\\\"productIds\\\":\\\"[\\\\\\\"chk_summit\\\\\\\"]\\\",\\\"domain\\\":\\\"http://localhost:9090\\\",\\\"workAddr1App2\\\":\\\"Null\\\",\\\"sentKBA\\\":\\\"true\\\",\\\"idExpiresApp1\\\":\\\"11/11/2022\\\",\\\"isFramework\\\":\\\"false\\\",\\\"birthDateApp2\\\":\\\"null\\\",\\\"danalUsed\\\":\\\"false\\\",\\\"isOlbEnrolled\\\":\\\"false\\\",\\\"overdraft\\\":\\\"false\\\",\\\"workPostalApp2\\\":\\\"null\\\",\\\"authToken\\\":\\\"nwnLB\\\",\\\"willJoinCommunity\\\":\\\"false\\\",\\\"isAuthenticated\\\":\\\"false\\\",\\\"iovationBlackbox\\\":\\\"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\\u003d\\\",\\\"myRealIP\\\":\\\"199.231.199.20\\\",\\\"challengeKey\\\":\\\"challengeMothersMaiden\\\",\\\"disclosuresRead\\\":\\\"\\\\\\\"{\\\\\\\\\\\\\\\"PrivacyPolicy\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629105596644,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null},\\\\\\\\\\\\\\\"eSignature\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629105596644,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null},\\\\\\\\\\\\\\\"ElectronicTransactions\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629106172705,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null,\\\\\\\\\\\\\\\"attachments\\\\\\\\\\\\\\\":[]}}\\\\\\\"\\\",\\\"workCityApp2\\\":\\\"Null\\\",\\\"products\\\":\\\"[\\\\\\\"\\\\\\\\\\\\\\\"{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productId\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"chk_summit\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productName\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"Summit Checking\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productAmount\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":0,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"isForFunding\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"true\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productType\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"dda\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}\\\\\\\\\\\\\\\"\\\\\\\"]\\\",\\\"idNumApp2\\\":\\\"null\\\",\\\"carrierConsentId\\\":\\\"null\\\",\\\"isExistingCustomer\\\":\\\"false\\\",\\\"idIssueDateApp1\\\":\\\"11/11/2000\\\",\\\"totalApplicants\\\":\\\"1\\\",\\\"firstNameApp2\\\":\\\"Null\\\",\\\"productType\\\":\\\"dda\\\",\\\"remoteAddr\\\":\\\"0:0:0:0:0:0:0:1\\\",\\\"monthsOnJobApp2\\\":\\\"null\\\",\\\"workStateApp2\\\":\\\"null\\\",\\\"isCellphoneValidated\\\":\\\"false\\\",\\\"appAccountProducts\\\":\\\"[]\\\",\\\"custom\\\":\\\"\\\\\\\"\\\\\\\\\\\\\\\"[{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"employedOrUnemployed\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"empOptionEmployed\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"employerName\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"q2\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"occupation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"se\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"frequentTraveler\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"travelOutsideUs\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"safeDepositBox\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"declaredBehavior\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositExceed5000\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"sourceOfFunds\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"SOFGift\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositOrWriteChecks\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositOrWithdrawCash\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willSendOrReceiveWireTransfers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willsendReceiveNonWireTransfers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"declaredBehavior\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"nonresident\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"usCitizen\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":true},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"haveDualCitizenship\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"areYouPEP\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"relationshipToPEP\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"PEPNo\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"foreignEmployee\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"nonresident\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}]\\\\\\\\\\\\\\\"\\\\\\\"\\\",\\\"approvedEsignature\\\":\\\"true\\\",\\\"monthlyIncomeApp2\\\":\\\"null\\\",\\\"userAgent\\\":\\\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36\\\",\\\"idState\\\":\\\"WA\\\",\\\"smsValPk\\\":\\\"168\\\",\\\"middleNameApp2\\\":\\\"Null\\\",\\\"correlationID\\\":\\\"null\\\",\\\"isExistingMemberWorkflow\\\":\\\"false\\\",\\\"isFundingSourceVerified\\\":\\\"undefined\\\",\\\"carrierAuthenticationKey\\\":\\\"null\\\"}\",\"idFront\":\"\",\"idBack\":\"\",\"ipAddress\":\"\",\"employmentStatus\":\"\",\"employmentStatusApp2\":\"\",\"employerPhone\":\"\",\"employerPhoneApp2\":\"\",\"jobTitleApp2\":\"\",\"monthsOnJobApp2\":0,\"workAddr1App2\":\"\",\"workAddr2App2\":\"\",\"workCityApp2\":\"\",\"workStateApp2\":\"\",\"workPostalApp2\":\"\",\"companyApp2\":\"\",\"workPhoneApp1\":\"\",\"workPhoneApp2\":\"\",\"occupancyDuration\":0,\"occupancyDurationApp2\":0,\"occupancyStatusApp1\":\"\",\"productIds\":[],\"idState\":\"WA\",\"isNative\":false,\"isFramework\":false,\"sendApp2\":false,\"webAppAuthKey\":\"\",\"webAppAuthCode\":\"\",\"webAppAuthRef\":\"\",\"webAppAuthTime\":\"\",\"promoCode\":\"\",\"fundingSuccess\":false,\"isFundingSourceVerified\":false,\"overdraft\":false,\"created\":\"08-16-2021 09:29 AM\",\"newAccounts\":{},\"bankState\":\"\",\"bankName\":\"\",\"fundingType\":\"\",\"frontEndFunding\":\"\",\"bankAccountType\":\"\",\"transferAccountType\":\"\",\"nameOnCard\":\"\",\"routingNumber\":\"\",\"bankAccountNumber\":\"\",\"ccCardType\":\"\",\"ccCardNumber\":\"\",\"ccCardLast4\":\"\",\"ccCardExpDateMonth\":\"\",\"ccCardExpDateYear\":\"\",\"ccCvnNumber\":\"0\",\"ccChargeFailureCount\":0,\"amountDeposit\":0.0,\"bankNameOnCard\":\"\",\"transferAccountNumber\":\"\",\"billingStreetAddress\":\"\",\"billingCity\":\"\",\"billingState\":\"\",\"billingZip\":\"\",\"sourceOfFunds\":\"\",\"futureFundingFirstName\":\"\",\"futureFundingLastName\":\"\",\"futureFundingMiddleName\":\"\",\"futureFundingSuffix\":\"\",\"futureFundingSsn\":\"\",\"futureFundingIraType\":\"\",\"futureFundingContributionType\":\"\",\"futureFundingAccountNumber\":\"\",\"fundingSourceId\":\"\",\"clfFundingSourceId\":\"\",\"microdepositAmount1\":0.0,\"microdepositAmount2\":0.0,\"microdepositFailureCount\":0,\"microdepositStatus\":\"\",\"paypalPaykey\":\"\",\"signature\":\"\",\"guidId\":\"\",\"clfApprovedAccountTypeId\":\"\",\"clfInternalTransferId\":\"\",\"isCreated\":\"\",\"checkRoutingNumber\":\"\",\"fundingDate\":\"\",\"fundedStatus\":\"\",\"isRateOverride\":\"\",\"description\":\"\",\"serviceType\":\"\",\"serviceCode\":\"\",\"benefactorType\":\"\",\"beneficiaryPriority\":\"\",\"beneficiaryPercentShare\":0.0,\"paymentFrequency\":\"\",\"paymentAccountNumber\":\"\",\"paymentAccountType\":\"\",\"paymentRoutingNumber\":\"\",\"paymentFiName\":\"\",\"paymentFiCity\":\"\",\"paymentFiState\":\"\",\"paymentFiZip\":\"\",\"isInterestRateAnException\":\"\",\"iraType\":\"\",\"contributionType\":\"\",\"contributionYear\":\"\",\"isPrimaryRetirementAccount\":\"\",\"custodianName\":\"\",\"custodianAddress\":\"\",\"custodianCity\":\"\",\"custodianState\":\"\",\"custodianZip\":\"\",\"custodianPhone\":\"\",\"accountTransferringFrom\":\"\",\"transferFromType\":\"\",\"transferPercent\":0.0,\"closeIra\":\"\",\"placeInConduitIra\":\"\",\"assetDescription\":\"\",\"amountInIra\":0.0,\"amountToBeTransferred\":0.0,\"transferInstruction\":\"\",\"requiredMinDistribution\":\"\",\"canChangeBeneficiary\":\"\",\"continueServiceAfterAgeOfMaturity\":\"\",\"depositYear\":\"\",\"approvedAccountTypeLinkId\":\"\",\"referenceType\":\"\",\"referenceId\":\"\",\"isLinked\":\"\",\"dateLinked\":\"\",\"directDepositType\":\"\",\"applicationStatus\":\"incomplete\",\"applicationStatusDateTime\":\"Aug 16, 2021 9:20:05 AM\",\"appAccountType\":\"\",\"isAccountOpened\":false,\"offersRequired\":false,\"primaryShareAccountAmountDeposit\":0.0,\"chkGoldAmountDeposit\":0.0,\"wingsFoundationAmountDeposit\":0.0,\"backupWithHoldingFlag\":false,\"skipKBA\":false,\"skipDecisioning\":false,\"isAuthenticated\":false,\"existingCustomer\":false,\"relationshipId\":\"\",\"webAppResponseTokenValue\":\"\",\"webAppResponseTokenName\":\"\",\"webAppResponseTokenType\":\"\",\"webAppResponseTokenExpiry\":\"\",\"webAppResponseTokenSecNum\":\"\",\"webAppResponseErrorMessages\":\"\",\"achPPDEntry\":\"\",\"achFileStatus\":\"\",\"products\":[],\"customPages\":[]}";
        personMaintenanceRequest.applicant = new Gson().fromJson(s, Applicant.class);
        //Execute
        personMaintenanceRequest.generateFlexFieldsForPersonUserFields(personElement, "person.userFields", "UserFields", "UserField", "UserFieldCd", false);

        personInfo.setCompany("");
        s = "{\"mspApplicationId\":83,\"timestamp\":\"Aug 16, 2021 9:29:18 AM\",\"isFinal\":false,\"smsTxnId\":\"deb60a21-8471-49b0-a1d0-2362ff82ead6\",\"emailTxnId\":\"d9dfcf28-5322-4324-84f6-b0a51283ffeb\",\"lat\":0.0,\"lon\":0.0,\"addr1\":\"123 Jefferson Street\",\"addr2\":\"\",\"city\":\"Atlanta\",\"state\":\"WA\",\"postal\":\"02453\",\"email\":\"m.ss@q2.com\",\"company\":\"\",\"isCuMember\":false,\"isValidSEG\":false,\"willJoinCommunity\":false,\"firstName\":\"Test\",\"middleName\":\"\",\"lastName\":\"Lastnam\",\"suffix\":\"\",\"mobilePhone\":\"0987654321\",\"internalComments\":\"\\r\\n08-16-2021 09:20:04 AM   PST - Returning customer detected, Related Application ids: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 77, 78, 79, 80, 81, 82].\\r\\n08-16-2021 09:28:55 AM   DNA - Check Existing Customer: Existing Customer Not found - Primary: Test Lastnam\\r\\n08-16-2021 09:29:36 AM   ALY - Identity Verification - Test Lastnam: Applicant \\u003cstrong\\u003epassed\\u003c/strong\\u003e Identity Verification with outcome: \\u003cstrong\\u003eApproved\\u003c/strong\\u003e and KBA Challenge Questions are not required. Click here – \\u003ca target\\u003d\\\"blank\\\" href\\u003d\\\"https://app.alloy.co/login/\\\"\\u003ehttps://app.alloy.co/login/\\u003c/a\\u003e – to log into Alloy for more information.\",\"queueId\":2,\"jointAccount\":false,\"firstNameApp2\":\"\",\"middleNameApp2\":\"\",\"lastNameApp2\":\"\",\"suffixApp2\":\"\",\"ssnApp2\":\"\",\"birthDateApp2\":\"\",\"mothersMaidenNameApp2\":\"\",\"idTypeApp2\":\"\",\"idNumberApp2\":\"\",\"isSmsValidated\":false,\"isEmailValidated\":true,\"idType\":\"driversLicense\",\"idNumber\":\"11111\",\"monthsOnJob\":0,\"birthDate\":\"11/11/1990\",\"ssn\":\"111111111\",\"mothersMaidenName\":\"mom\",\"privacyApproved\":true,\"esigApproved\":false,\"etxnApproved\":false,\"jobTitle\":\"\",\"workAddr1\":\"\",\"workAddr2\":\"\",\"workCity\":\"\",\"workState\":\"\",\"workPostal\":\"\",\"disclosuresRead\":\"\\\"{\\\\\\\"PrivacyPolicy\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1629105596644,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null},\\\\\\\"eSignature\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1629105596644,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null},\\\\\\\"ElectronicTransactions\\\\\\\":{\\\\\\\"clicked\\\\\\\":false,\\\\\\\"clickedTimeStamp\\\\\\\":null,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null,\\\\\\\"attachments\\\\\\\":[]}}\\\"\",\"unstructuredVars\":\"{\\\"mothersMaidenNameApp2\\\":\\\"null\\\",\\\"isReturningUser\\\":\\\"true\\\",\\\"mobileCarrier\\\":\\\"\\\",\\\"isValidSEG\\\":\\\"false\\\",\\\"workAddr2App2\\\":\\\"Null\\\",\\\"isFundingBalanceVerified\\\":\\\"undefined\\\",\\\"applicants\\\":\\\"\\\\\\\"{\\\\\\\\\\\\\\\"1\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"mailAddrStateAddr\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"firstName\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Test\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"lastName\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Lastnam\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"existingCustomerSignal\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"continue\\\\\\\\\\\\\\\"}}\\\\\\\"\\\",\\\"ssnApp2\\\":\\\"null\\\",\\\"sendApp2\\\":\\\"false\\\",\\\"occupancyDuration\\\":\\\"0\\\",\\\"isNative\\\":\\\"false\\\",\\\"cart\\\":\\\"[\\\\\\\"chk_summit\\\\\\\"]\\\",\\\"jointAccount\\\":\\\"false\\\",\\\"isJointAuth\\\":\\\"false\\\",\\\"applicationStatusDateTime\\\":\\\"2021-08-16 09:29:17.721\\\",\\\"idTypeApp2\\\":\\\"null\\\",\\\"brand\\\":\\\"999999\\\",\\\"jobTitleApp2\\\":\\\"Null\\\",\\\"workPhoneApp2\\\":\\\"null\\\",\\\"cookie\\\":\\\"_ga\\u003dGA1.1.1177092643.1628530779; wcuId\\u003d887936333; cookieTimeoutDays2\\u003d30; _gcl_au\\u003d1.1.2057365608.1628836560; _msuuid_1068lnm41522\\u003dDAB08AFF-5146-48B1-922A-88E25D8C4519; _uetvid\\u003db9e89920fc0011eba61e032f80784591; nmstat\\u003d3a2ddea7-81fa-c7c7-4a2b-675eaf70cdb4; _mkto_trk\\u003did:914-DOA-764\\u0026token:_mch-localhost-1628836562436-63289; _gid\\u003dGA1.1.1069097507.1629090303; _uetsid\\u003d84114080fe4f11eb95ed55d74516ede7; cookieDeviceInfo2\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieDeviceInfo6\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieTimeoutDays6\\u003d30; cookieDeviceInfo3\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieTimeoutDays3\\u003d30\\\",\\\"mspApplicationId\\\":\\\"83\\\",\\\"employmentStatusApp2\\\":\\\"null\\\",\\\"emailValidationOverriden\\\":\\\"false\\\",\\\"lastNameApp2\\\":\\\"Null\\\",\\\"productIds\\\":\\\"[\\\\\\\"chk_summit\\\\\\\"]\\\",\\\"domain\\\":\\\"http://localhost:9090\\\",\\\"workAddr1App2\\\":\\\"Null\\\",\\\"sentKBA\\\":\\\"true\\\",\\\"idExpiresApp1\\\":\\\"11/11/2022\\\",\\\"isFramework\\\":\\\"false\\\",\\\"birthDateApp2\\\":\\\"null\\\",\\\"danalUsed\\\":\\\"false\\\",\\\"isOlbEnrolled\\\":\\\"false\\\",\\\"overdraft\\\":\\\"false\\\",\\\"workPostalApp2\\\":\\\"null\\\",\\\"authToken\\\":\\\"nwnLB\\\",\\\"willJoinCommunity\\\":\\\"false\\\",\\\"isAuthenticated\\\":\\\"false\\\",\\\"iovationBlackbox\\\":\\\"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\\u003d\\\",\\\"myRealIP\\\":\\\"199.231.199.20\\\",\\\"challengeKey\\\":\\\"challengeMothersMaiden\\\",\\\"disclosuresRead\\\":\\\"\\\\\\\"{\\\\\\\\\\\\\\\"PrivacyPolicy\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629105596644,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null},\\\\\\\\\\\\\\\"eSignature\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629105596644,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null},\\\\\\\\\\\\\\\"ElectronicTransactions\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629106172705,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null,\\\\\\\\\\\\\\\"attachments\\\\\\\\\\\\\\\":[]}}\\\\\\\"\\\",\\\"workCityApp2\\\":\\\"Null\\\",\\\"products\\\":\\\"[\\\\\\\"\\\\\\\\\\\\\\\"{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productId\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"chk_summit\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productName\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"Summit Checking\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productAmount\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":0,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"isForFunding\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"true\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productType\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"dda\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}\\\\\\\\\\\\\\\"\\\\\\\"]\\\",\\\"idNumApp2\\\":\\\"null\\\",\\\"carrierConsentId\\\":\\\"null\\\",\\\"isExistingCustomer\\\":\\\"false\\\",\\\"idIssueDateApp1\\\":\\\"11/11/2000\\\",\\\"totalApplicants\\\":\\\"1\\\",\\\"firstNameApp2\\\":\\\"Null\\\",\\\"productType\\\":\\\"dda\\\",\\\"remoteAddr\\\":\\\"0:0:0:0:0:0:0:1\\\",\\\"monthsOnJobApp2\\\":\\\"null\\\",\\\"workStateApp2\\\":\\\"null\\\",\\\"isCellphoneValidated\\\":\\\"false\\\",\\\"appAccountProducts\\\":\\\"[]\\\",\\\"custom\\\":\\\"\\\\\\\"\\\\\\\\\\\\\\\"[{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"employedOrUnemployed\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"empOptionEmployed\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"employerName\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"q2\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"occupation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"se\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"frequentTraveler\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"travelOutsideUs\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"safeDepositBox\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"declaredBehavior\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositExceed5000\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"sourceOfFunds\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"SOFGift\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositOrWriteChecks\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositOrWithdrawCash\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willSendOrReceiveWireTransfers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willsendReceiveNonWireTransfers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"declaredBehavior\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"nonresident\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"usCitizen\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":true},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"haveDualCitizenship\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"areYouPEP\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"relationshipToPEP\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"PEPNo\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"foreignEmployee\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"nonresident\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}]\\\\\\\\\\\\\\\"\\\\\\\"\\\",\\\"approvedEsignature\\\":\\\"true\\\",\\\"monthlyIncomeApp2\\\":\\\"null\\\",\\\"userAgent\\\":\\\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36\\\",\\\"idState\\\":\\\"WA\\\",\\\"smsValPk\\\":\\\"168\\\",\\\"middleNameApp2\\\":\\\"Null\\\",\\\"correlationID\\\":\\\"null\\\",\\\"isExistingMemberWorkflow\\\":\\\"false\\\",\\\"isFundingSourceVerified\\\":\\\"undefined\\\",\\\"carrierAuthenticationKey\\\":\\\"null\\\"}\",\"idFront\":\"\",\"idBack\":\"\",\"ipAddress\":\"\",\"employmentStatus\":\"\",\"employmentStatusApp2\":\"\",\"employerPhone\":\"\",\"employerPhoneApp2\":\"\",\"jobTitleApp2\":\"\",\"monthsOnJobApp2\":0,\"workAddr1App2\":\"\",\"workAddr2App2\":\"\",\"workCityApp2\":\"\",\"workStateApp2\":\"\",\"workPostalApp2\":\"\",\"companyApp2\":\"\",\"workPhoneApp1\":\"\",\"workPhoneApp2\":\"\",\"occupancyDuration\":0,\"occupancyDurationApp2\":0,\"occupancyStatusApp1\":\"\",\"productIds\":[],\"idState\":\"WA\",\"isNative\":false,\"isFramework\":false,\"sendApp2\":false,\"webAppAuthKey\":\"\",\"webAppAuthCode\":\"\",\"webAppAuthRef\":\"\",\"webAppAuthTime\":\"\",\"promoCode\":\"\",\"fundingSuccess\":false,\"isFundingSourceVerified\":false,\"overdraft\":false,\"created\":\"08-16-2021 09:29 AM\",\"newAccounts\":{},\"bankState\":\"\",\"bankName\":\"\",\"fundingType\":\"\",\"frontEndFunding\":\"\",\"bankAccountType\":\"\",\"transferAccountType\":\"\",\"nameOnCard\":\"\",\"routingNumber\":\"\",\"bankAccountNumber\":\"\",\"ccCardType\":\"\",\"ccCardNumber\":\"\",\"ccCardLast4\":\"\",\"ccCardExpDateMonth\":\"\",\"ccCardExpDateYear\":\"\",\"ccCvnNumber\":\"0\",\"ccChargeFailureCount\":0,\"amountDeposit\":0.0,\"bankNameOnCard\":\"\",\"transferAccountNumber\":\"\",\"billingStreetAddress\":\"\",\"billingCity\":\"\",\"billingState\":\"\",\"billingZip\":\"\",\"sourceOfFunds\":\"\",\"futureFundingFirstName\":\"\",\"futureFundingLastName\":\"\",\"futureFundingMiddleName\":\"\",\"futureFundingSuffix\":\"\",\"futureFundingSsn\":\"\",\"futureFundingIraType\":\"\",\"futureFundingContributionType\":\"\",\"futureFundingAccountNumber\":\"\",\"fundingSourceId\":\"\",\"clfFundingSourceId\":\"\",\"microdepositAmount1\":0.0,\"microdepositAmount2\":0.0,\"microdepositFailureCount\":0,\"microdepositStatus\":\"\",\"paypalPaykey\":\"\",\"signature\":\"\",\"guidId\":\"\",\"clfApprovedAccountTypeId\":\"\",\"clfInternalTransferId\":\"\",\"isCreated\":\"\",\"checkRoutingNumber\":\"\",\"fundingDate\":\"\",\"fundedStatus\":\"\",\"isRateOverride\":\"\",\"description\":\"\",\"serviceType\":\"\",\"serviceCode\":\"\",\"benefactorType\":\"\",\"beneficiaryPriority\":\"\",\"beneficiaryPercentShare\":0.0,\"paymentFrequency\":\"\",\"paymentAccountNumber\":\"\",\"paymentAccountType\":\"\",\"paymentRoutingNumber\":\"\",\"paymentFiName\":\"\",\"paymentFiCity\":\"\",\"paymentFiState\":\"\",\"paymentFiZip\":\"\",\"isInterestRateAnException\":\"\",\"iraType\":\"\",\"contributionType\":\"\",\"contributionYear\":\"\",\"isPrimaryRetirementAccount\":\"\",\"custodianName\":\"\",\"custodianAddress\":\"\",\"custodianCity\":\"\",\"custodianState\":\"\",\"custodianZip\":\"\",\"custodianPhone\":\"\",\"accountTransferringFrom\":\"\",\"transferFromType\":\"\",\"transferPercent\":0.0,\"closeIra\":\"\",\"placeInConduitIra\":\"\",\"assetDescription\":\"\",\"amountInIra\":0.0,\"amountToBeTransferred\":0.0,\"transferInstruction\":\"\",\"requiredMinDistribution\":\"\",\"canChangeBeneficiary\":\"\",\"continueServiceAfterAgeOfMaturity\":\"\",\"depositYear\":\"\",\"approvedAccountTypeLinkId\":\"\",\"referenceType\":\"\",\"referenceId\":\"\",\"isLinked\":\"\",\"dateLinked\":\"\",\"directDepositType\":\"\",\"applicationStatus\":\"incomplete\",\"applicationStatusDateTime\":\"Aug 16, 2021 9:20:05 AM\",\"appAccountType\":\"\",\"isAccountOpened\":false,\"offersRequired\":false,\"primaryShareAccountAmountDeposit\":0.0,\"chkGoldAmountDeposit\":0.0,\"wingsFoundationAmountDeposit\":0.0,\"backupWithHoldingFlag\":false,\"skipKBA\":false,\"skipDecisioning\":false,\"isAuthenticated\":false,\"existingCustomer\":false,\"relationshipId\":\"\",\"webAppResponseTokenValue\":\"\",\"webAppResponseTokenName\":\"\",\"webAppResponseTokenType\":\"\",\"webAppResponseTokenExpiry\":\"\",\"webAppResponseTokenSecNum\":\"\",\"webAppResponseErrorMessages\":\"\",\"achPPDEntry\":\"\",\"achFileStatus\":\"\",\"products\":[],\"customPages\":[]}";
        personMaintenanceRequest.applicant = new Gson().fromJson(s, Applicant.class);
        //Execute
        personMaintenanceRequest.generateFlexFieldsForPersonUserFields(personElement, "person.userFields", "UserFields", "UserField", "UserFieldCd", false);

    }

    @Test
    public void testGenerateFlexFields() throws Exception {

        when(requestDocument.createElement(any())).thenReturn(child);
        personMaintenanceRequest.setRequestDocument(requestDocument);

        String personInfoMocked = getPersonalinfo();
        PersonInfo personInfo = new Gson().fromJson(personInfoMocked, PersonInfo.class);
        personMaintenanceRequest.setActivePersonalInfo(personInfo);

        when(requestDocument.createElement(any())).thenReturn(personElement);


        // Test condition for occupation and employer

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
                "            \"OCCU\": \"<OCCUPATION>\",\n" +
                "            \"EMPR\": \"<EMPLOYER_NAME>\"\n" +
                "        }}";
        JSONObject flexFields = new JSONObject(flexFieldsString);
        fiservDNASettings = new FiservDNASettings();
        fiservDNASettings.setFlexFields(flexFields);
        personMaintenanceRequest.setSettings(fiservDNASettings);
        personInfo.setJobTitle("");
        String s = "{\"mspApplicationId\":83,\"timestamp\":\"Aug 16, 2021 9:29:18 AM\",\"isFinal\":false,\"smsTxnId\":\"deb60a21-8471-49b0-a1d0-2362ff82ead6\",\"emailTxnId\":\"d9dfcf28-5322-4324-84f6-b0a51283ffeb\",\"lat\":0.0,\"lon\":0.0,\"addr1\":\"123 Jefferson Street\",\"addr2\":\"\",\"city\":\"Atlanta\",\"state\":\"WA\",\"postal\":\"02453\",\"email\":\"m.ss@q2.com\",\"company\":\"\",\"isCuMember\":false,\"isValidSEG\":false,\"willJoinCommunity\":false,\"firstName\":\"Test\",\"middleName\":\"\",\"lastName\":\"Lastnam\",\"suffix\":\"\",\"mobilePhone\":\"0987654321\",\"internalComments\":\"\\r\\n08-16-2021 09:20:04 AM   PST - Returning customer detected, Related Application ids: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 77, 78, 79, 80, 81, 82].\\r\\n08-16-2021 09:28:55 AM   DNA - Check Existing Customer: Existing Customer Not found - Primary: Test Lastnam\\r\\n08-16-2021 09:29:36 AM   ALY - Identity Verification - Test Lastnam: Applicant \\u003cstrong\\u003epassed\\u003c/strong\\u003e Identity Verification with outcome: \\u003cstrong\\u003eApproved\\u003c/strong\\u003e and KBA Challenge Questions are not required. Click here – \\u003ca target\\u003d\\\"blank\\\" href\\u003d\\\"https://app.alloy.co/login/\\\"\\u003ehttps://app.alloy.co/login/\\u003c/a\\u003e – to log into Alloy for more information.\",\"queueId\":2,\"jointAccount\":false,\"firstNameApp2\":\"\",\"middleNameApp2\":\"\",\"lastNameApp2\":\"\",\"suffixApp2\":\"\",\"ssnApp2\":\"\",\"birthDateApp2\":\"\",\"mothersMaidenNameApp2\":\"\",\"idTypeApp2\":\"\",\"idNumberApp2\":\"\",\"isSmsValidated\":false,\"isEmailValidated\":true,\"idType\":\"driversLicense\",\"idNumber\":\"11111\",\"monthsOnJob\":0,\"birthDate\":\"11/11/1990\",\"ssn\":\"111111111\",\"mothersMaidenName\":\"mom\",\"privacyApproved\":true,\"esigApproved\":false,\"etxnApproved\":false,\"jobTitle\":\"\",\"workAddr1\":\"\",\"workAddr2\":\"\",\"workCity\":\"\",\"workState\":\"\",\"workPostal\":\"\",\"disclosuresRead\":\"\\\"{\\\\\\\"PrivacyPolicy\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1629105596644,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null},\\\\\\\"eSignature\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1629105596644,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null},\\\\\\\"ElectronicTransactions\\\\\\\":{\\\\\\\"clicked\\\\\\\":false,\\\\\\\"clickedTimeStamp\\\\\\\":null,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null,\\\\\\\"attachments\\\\\\\":[]}}\\\"\",\"unstructuredVars\":\"{\\\"mothersMaidenNameApp2\\\":\\\"null\\\",\\\"isReturningUser\\\":\\\"true\\\",\\\"mobileCarrier\\\":\\\"\\\",\\\"isValidSEG\\\":\\\"false\\\",\\\"workAddr2App2\\\":\\\"Null\\\",\\\"isFundingBalanceVerified\\\":\\\"undefined\\\",\\\"applicants\\\":\\\"\\\\\\\"{\\\\\\\\\\\\\\\"1\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"mailAddrStateAddr\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"firstName\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Test\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"lastName\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Lastnam\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"existingCustomerSignal\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"continue\\\\\\\\\\\\\\\"}}\\\\\\\"\\\",\\\"ssnApp2\\\":\\\"null\\\",\\\"sendApp2\\\":\\\"false\\\",\\\"occupancyDuration\\\":\\\"0\\\",\\\"isNative\\\":\\\"false\\\",\\\"cart\\\":\\\"[\\\\\\\"chk_summit\\\\\\\"]\\\",\\\"jointAccount\\\":\\\"false\\\",\\\"isJointAuth\\\":\\\"false\\\",\\\"applicationStatusDateTime\\\":\\\"2021-08-16 09:29:17.721\\\",\\\"idTypeApp2\\\":\\\"null\\\",\\\"brand\\\":\\\"999999\\\",\\\"jobTitleApp2\\\":\\\"Null\\\",\\\"workPhoneApp2\\\":\\\"null\\\",\\\"cookie\\\":\\\"_ga\\u003dGA1.1.1177092643.1628530779; wcuId\\u003d887936333; cookieTimeoutDays2\\u003d30; _gcl_au\\u003d1.1.2057365608.1628836560; _msuuid_1068lnm41522\\u003dDAB08AFF-5146-48B1-922A-88E25D8C4519; _uetvid\\u003db9e89920fc0011eba61e032f80784591; nmstat\\u003d3a2ddea7-81fa-c7c7-4a2b-675eaf70cdb4; _mkto_trk\\u003did:914-DOA-764\\u0026token:_mch-localhost-1628836562436-63289; _gid\\u003dGA1.1.1069097507.1629090303; _uetsid\\u003d84114080fe4f11eb95ed55d74516ede7; cookieDeviceInfo2\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieDeviceInfo6\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieTimeoutDays6\\u003d30; cookieDeviceInfo3\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieTimeoutDays3\\u003d30\\\",\\\"mspApplicationId\\\":\\\"83\\\",\\\"employmentStatusApp2\\\":\\\"null\\\",\\\"emailValidationOverriden\\\":\\\"false\\\",\\\"lastNameApp2\\\":\\\"Null\\\",\\\"productIds\\\":\\\"[\\\\\\\"chk_summit\\\\\\\"]\\\",\\\"domain\\\":\\\"http://localhost:9090\\\",\\\"workAddr1App2\\\":\\\"Null\\\",\\\"sentKBA\\\":\\\"true\\\",\\\"idExpiresApp1\\\":\\\"11/11/2022\\\",\\\"isFramework\\\":\\\"false\\\",\\\"birthDateApp2\\\":\\\"null\\\",\\\"danalUsed\\\":\\\"false\\\",\\\"isOlbEnrolled\\\":\\\"false\\\",\\\"overdraft\\\":\\\"false\\\",\\\"workPostalApp2\\\":\\\"null\\\",\\\"authToken\\\":\\\"nwnLB\\\",\\\"willJoinCommunity\\\":\\\"false\\\",\\\"isAuthenticated\\\":\\\"false\\\",\\\"iovationBlackbox\\\":\\\"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\\u003d\\\",\\\"myRealIP\\\":\\\"199.231.199.20\\\",\\\"challengeKey\\\":\\\"challengeMothersMaiden\\\",\\\"disclosuresRead\\\":\\\"\\\\\\\"{\\\\\\\\\\\\\\\"PrivacyPolicy\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629105596644,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null},\\\\\\\\\\\\\\\"eSignature\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629105596644,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null},\\\\\\\\\\\\\\\"ElectronicTransactions\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629106172705,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null,\\\\\\\\\\\\\\\"attachments\\\\\\\\\\\\\\\":[]}}\\\\\\\"\\\",\\\"workCityApp2\\\":\\\"Null\\\",\\\"products\\\":\\\"[\\\\\\\"\\\\\\\\\\\\\\\"{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productId\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"chk_summit\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productName\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"Summit Checking\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productAmount\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":0,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"isForFunding\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"true\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productType\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"dda\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}\\\\\\\\\\\\\\\"\\\\\\\"]\\\",\\\"idNumApp2\\\":\\\"null\\\",\\\"carrierConsentId\\\":\\\"null\\\",\\\"isExistingCustomer\\\":\\\"false\\\",\\\"idIssueDateApp1\\\":\\\"11/11/2000\\\",\\\"totalApplicants\\\":\\\"1\\\",\\\"firstNameApp2\\\":\\\"Null\\\",\\\"productType\\\":\\\"dda\\\",\\\"remoteAddr\\\":\\\"0:0:0:0:0:0:0:1\\\",\\\"monthsOnJobApp2\\\":\\\"null\\\",\\\"workStateApp2\\\":\\\"null\\\",\\\"isCellphoneValidated\\\":\\\"false\\\",\\\"appAccountProducts\\\":\\\"[]\\\",\\\"custom\\\":\\\"\\\\\\\"\\\\\\\\\\\\\\\"[{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"employedOrUnemployed\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"empOptionEmployed\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"employerName\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"q2\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"occupation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"se\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"frequentTraveler\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"travelOutsideUs\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"safeDepositBox\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"declaredBehavior\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositExceed5000\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"sourceOfFunds\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"SOFGift\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositOrWriteChecks\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositOrWithdrawCash\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willSendOrReceiveWireTransfers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willsendReceiveNonWireTransfers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"declaredBehavior\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"nonresident\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"usCitizen\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":true},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"haveDualCitizenship\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"areYouPEP\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"relationshipToPEP\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"PEPNo\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"foreignEmployee\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"nonresident\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}]\\\\\\\\\\\\\\\"\\\\\\\"\\\",\\\"approvedEsignature\\\":\\\"true\\\",\\\"monthlyIncomeApp2\\\":\\\"null\\\",\\\"userAgent\\\":\\\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36\\\",\\\"idState\\\":\\\"WA\\\",\\\"smsValPk\\\":\\\"168\\\",\\\"middleNameApp2\\\":\\\"Null\\\",\\\"correlationID\\\":\\\"null\\\",\\\"isExistingMemberWorkflow\\\":\\\"false\\\",\\\"isFundingSourceVerified\\\":\\\"undefined\\\",\\\"carrierAuthenticationKey\\\":\\\"null\\\"}\",\"idFront\":\"\",\"idBack\":\"\",\"ipAddress\":\"\",\"employmentStatus\":\"\",\"employmentStatusApp2\":\"\",\"employerPhone\":\"\",\"employerPhoneApp2\":\"\",\"jobTitleApp2\":\"\",\"monthsOnJobApp2\":0,\"workAddr1App2\":\"\",\"workAddr2App2\":\"\",\"workCityApp2\":\"\",\"workStateApp2\":\"\",\"workPostalApp2\":\"\",\"companyApp2\":\"\",\"workPhoneApp1\":\"\",\"workPhoneApp2\":\"\",\"occupancyDuration\":0,\"occupancyDurationApp2\":0,\"occupancyStatusApp1\":\"\",\"productIds\":[],\"idState\":\"WA\",\"isNative\":false,\"isFramework\":false,\"sendApp2\":false,\"webAppAuthKey\":\"\",\"webAppAuthCode\":\"\",\"webAppAuthRef\":\"\",\"webAppAuthTime\":\"\",\"promoCode\":\"\",\"fundingSuccess\":false,\"isFundingSourceVerified\":false,\"overdraft\":false,\"created\":\"08-16-2021 09:29 AM\",\"newAccounts\":{},\"bankState\":\"\",\"bankName\":\"\",\"fundingType\":\"\",\"frontEndFunding\":\"\",\"bankAccountType\":\"\",\"transferAccountType\":\"\",\"nameOnCard\":\"\",\"routingNumber\":\"\",\"bankAccountNumber\":\"\",\"ccCardType\":\"\",\"ccCardNumber\":\"\",\"ccCardLast4\":\"\",\"ccCardExpDateMonth\":\"\",\"ccCardExpDateYear\":\"\",\"ccCvnNumber\":\"0\",\"ccChargeFailureCount\":0,\"amountDeposit\":0.0,\"bankNameOnCard\":\"\",\"transferAccountNumber\":\"\",\"billingStreetAddress\":\"\",\"billingCity\":\"\",\"billingState\":\"\",\"billingZip\":\"\",\"sourceOfFunds\":\"\",\"futureFundingFirstName\":\"\",\"futureFundingLastName\":\"\",\"futureFundingMiddleName\":\"\",\"futureFundingSuffix\":\"\",\"futureFundingSsn\":\"\",\"futureFundingIraType\":\"\",\"futureFundingContributionType\":\"\",\"futureFundingAccountNumber\":\"\",\"fundingSourceId\":\"\",\"clfFundingSourceId\":\"\",\"microdepositAmount1\":0.0,\"microdepositAmount2\":0.0,\"microdepositFailureCount\":0,\"microdepositStatus\":\"\",\"paypalPaykey\":\"\",\"signature\":\"\",\"guidId\":\"\",\"clfApprovedAccountTypeId\":\"\",\"clfInternalTransferId\":\"\",\"isCreated\":\"\",\"checkRoutingNumber\":\"\",\"fundingDate\":\"\",\"fundedStatus\":\"\",\"isRateOverride\":\"\",\"description\":\"\",\"serviceType\":\"\",\"serviceCode\":\"\",\"benefactorType\":\"\",\"beneficiaryPriority\":\"\",\"beneficiaryPercentShare\":0.0,\"paymentFrequency\":\"\",\"paymentAccountNumber\":\"\",\"paymentAccountType\":\"\",\"paymentRoutingNumber\":\"\",\"paymentFiName\":\"\",\"paymentFiCity\":\"\",\"paymentFiState\":\"\",\"paymentFiZip\":\"\",\"isInterestRateAnException\":\"\",\"iraType\":\"\",\"contributionType\":\"\",\"contributionYear\":\"\",\"isPrimaryRetirementAccount\":\"\",\"custodianName\":\"\",\"custodianAddress\":\"\",\"custodianCity\":\"\",\"custodianState\":\"\",\"custodianZip\":\"\",\"custodianPhone\":\"\",\"accountTransferringFrom\":\"\",\"transferFromType\":\"\",\"transferPercent\":0.0,\"closeIra\":\"\",\"placeInConduitIra\":\"\",\"assetDescription\":\"\",\"amountInIra\":0.0,\"amountToBeTransferred\":0.0,\"transferInstruction\":\"\",\"requiredMinDistribution\":\"\",\"canChangeBeneficiary\":\"\",\"continueServiceAfterAgeOfMaturity\":\"\",\"depositYear\":\"\",\"approvedAccountTypeLinkId\":\"\",\"referenceType\":\"\",\"referenceId\":\"\",\"isLinked\":\"\",\"dateLinked\":\"\",\"directDepositType\":\"\",\"applicationStatus\":\"incomplete\",\"applicationStatusDateTime\":\"Aug 16, 2021 9:20:05 AM\",\"appAccountType\":\"\",\"isAccountOpened\":false,\"offersRequired\":false,\"primaryShareAccountAmountDeposit\":0.0,\"chkGoldAmountDeposit\":0.0,\"wingsFoundationAmountDeposit\":0.0,\"backupWithHoldingFlag\":false,\"skipKBA\":false,\"skipDecisioning\":false,\"isAuthenticated\":false,\"existingCustomer\":false,\"relationshipId\":\"\",\"webAppResponseTokenValue\":\"\",\"webAppResponseTokenName\":\"\",\"webAppResponseTokenType\":\"\",\"webAppResponseTokenExpiry\":\"\",\"webAppResponseTokenSecNum\":\"\",\"webAppResponseErrorMessages\":\"\",\"achPPDEntry\":\"\",\"achFileStatus\":\"\",\"products\":[],\"customPages\":[]}";
        personMaintenanceRequest.applicant = new Gson().fromJson(s, Applicant.class);
        //Execute
        personMaintenanceRequest.generateFlexFieldsForPersonUserFields(personElement, "person.userFields", "UserFields", "UserField", "UserFieldCd", false);

        personInfo.setCompany("");
        s = "{\"mspApplicationId\":83,\"timestamp\":\"Aug 16, 2021 9:29:18 AM\",\"isFinal\":false,\"smsTxnId\":\"deb60a21-8471-49b0-a1d0-2362ff82ead6\",\"emailTxnId\":\"d9dfcf28-5322-4324-84f6-b0a51283ffeb\",\"lat\":0.0,\"lon\":0.0,\"addr1\":\"123 Jefferson Street\",\"addr2\":\"\",\"city\":\"Atlanta\",\"state\":\"WA\",\"postal\":\"02453\",\"email\":\"m.ss@q2.com\",\"company\":\"\",\"isCuMember\":false,\"isValidSEG\":false,\"willJoinCommunity\":false,\"firstName\":\"Test\",\"middleName\":\"\",\"lastName\":\"Lastnam\",\"suffix\":\"\",\"mobilePhone\":\"0987654321\",\"internalComments\":\"\\r\\n08-16-2021 09:20:04 AM   PST - Returning customer detected, Related Application ids: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 77, 78, 79, 80, 81, 82].\\r\\n08-16-2021 09:28:55 AM   DNA - Check Existing Customer: Existing Customer Not found - Primary: Test Lastnam\\r\\n08-16-2021 09:29:36 AM   ALY - Identity Verification - Test Lastnam: Applicant \\u003cstrong\\u003epassed\\u003c/strong\\u003e Identity Verification with outcome: \\u003cstrong\\u003eApproved\\u003c/strong\\u003e and KBA Challenge Questions are not required. Click here – \\u003ca target\\u003d\\\"blank\\\" href\\u003d\\\"https://app.alloy.co/login/\\\"\\u003ehttps://app.alloy.co/login/\\u003c/a\\u003e – to log into Alloy for more information.\",\"queueId\":2,\"jointAccount\":false,\"firstNameApp2\":\"\",\"middleNameApp2\":\"\",\"lastNameApp2\":\"\",\"suffixApp2\":\"\",\"ssnApp2\":\"\",\"birthDateApp2\":\"\",\"mothersMaidenNameApp2\":\"\",\"idTypeApp2\":\"\",\"idNumberApp2\":\"\",\"isSmsValidated\":false,\"isEmailValidated\":true,\"idType\":\"driversLicense\",\"idNumber\":\"11111\",\"monthsOnJob\":0,\"birthDate\":\"11/11/1990\",\"ssn\":\"111111111\",\"mothersMaidenName\":\"mom\",\"privacyApproved\":true,\"esigApproved\":false,\"etxnApproved\":false,\"jobTitle\":\"\",\"workAddr1\":\"\",\"workAddr2\":\"\",\"workCity\":\"\",\"workState\":\"\",\"workPostal\":\"\",\"disclosuresRead\":\"\\\"{\\\\\\\"PrivacyPolicy\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1629105596644,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null},\\\\\\\"eSignature\\\\\\\":{\\\\\\\"clicked\\\\\\\":true,\\\\\\\"clickedTimeStamp\\\\\\\":1629105596644,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null},\\\\\\\"ElectronicTransactions\\\\\\\":{\\\\\\\"clicked\\\\\\\":false,\\\\\\\"clickedTimeStamp\\\\\\\":null,\\\\\\\"read\\\\\\\":false,\\\\\\\"readTimeStamp\\\\\\\":null,\\\\\\\"attachments\\\\\\\":[]}}\\\"\",\"unstructuredVars\":\"{\\\"mothersMaidenNameApp2\\\":\\\"null\\\",\\\"isReturningUser\\\":\\\"true\\\",\\\"mobileCarrier\\\":\\\"\\\",\\\"isValidSEG\\\":\\\"false\\\",\\\"workAddr2App2\\\":\\\"Null\\\",\\\"isFundingBalanceVerified\\\":\\\"undefined\\\",\\\"applicants\\\":\\\"\\\\\\\"{\\\\\\\\\\\\\\\"1\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"mailAddrStateAddr\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"firstName\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Test\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"lastName\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Lastnam\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"existingCustomerSignal\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"continue\\\\\\\\\\\\\\\"}}\\\\\\\"\\\",\\\"ssnApp2\\\":\\\"null\\\",\\\"sendApp2\\\":\\\"false\\\",\\\"occupancyDuration\\\":\\\"0\\\",\\\"isNative\\\":\\\"false\\\",\\\"cart\\\":\\\"[\\\\\\\"chk_summit\\\\\\\"]\\\",\\\"jointAccount\\\":\\\"false\\\",\\\"isJointAuth\\\":\\\"false\\\",\\\"applicationStatusDateTime\\\":\\\"2021-08-16 09:29:17.721\\\",\\\"idTypeApp2\\\":\\\"null\\\",\\\"brand\\\":\\\"999999\\\",\\\"jobTitleApp2\\\":\\\"Null\\\",\\\"workPhoneApp2\\\":\\\"null\\\",\\\"cookie\\\":\\\"_ga\\u003dGA1.1.1177092643.1628530779; wcuId\\u003d887936333; cookieTimeoutDays2\\u003d30; _gcl_au\\u003d1.1.2057365608.1628836560; _msuuid_1068lnm41522\\u003dDAB08AFF-5146-48B1-922A-88E25D8C4519; _uetvid\\u003db9e89920fc0011eba61e032f80784591; nmstat\\u003d3a2ddea7-81fa-c7c7-4a2b-675eaf70cdb4; _mkto_trk\\u003did:914-DOA-764\\u0026token:_mch-localhost-1628836562436-63289; _gid\\u003dGA1.1.1069097507.1629090303; _uetsid\\u003d84114080fe4f11eb95ed55d74516ede7; cookieDeviceInfo2\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieDeviceInfo6\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieTimeoutDays6\\u003d30; cookieDeviceInfo3\\u003dMozilla/5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML%2C+like+Gecko)+Chrome/92.0.4515.131+Safari/537.36; cookieTimeoutDays3\\u003d30\\\",\\\"mspApplicationId\\\":\\\"83\\\",\\\"employmentStatusApp2\\\":\\\"null\\\",\\\"emailValidationOverriden\\\":\\\"false\\\",\\\"lastNameApp2\\\":\\\"Null\\\",\\\"productIds\\\":\\\"[\\\\\\\"chk_summit\\\\\\\"]\\\",\\\"domain\\\":\\\"http://localhost:9090\\\",\\\"workAddr1App2\\\":\\\"Null\\\",\\\"sentKBA\\\":\\\"true\\\",\\\"idExpiresApp1\\\":\\\"11/11/2022\\\",\\\"isFramework\\\":\\\"false\\\",\\\"birthDateApp2\\\":\\\"null\\\",\\\"danalUsed\\\":\\\"false\\\",\\\"isOlbEnrolled\\\":\\\"false\\\",\\\"overdraft\\\":\\\"false\\\",\\\"workPostalApp2\\\":\\\"null\\\",\\\"authToken\\\":\\\"nwnLB\\\",\\\"willJoinCommunity\\\":\\\"false\\\",\\\"isAuthenticated\\\":\\\"false\\\",\\\"iovationBlackbox\\\":\\\"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\\u003d\\\",\\\"myRealIP\\\":\\\"199.231.199.20\\\",\\\"challengeKey\\\":\\\"challengeMothersMaiden\\\",\\\"disclosuresRead\\\":\\\"\\\\\\\"{\\\\\\\\\\\\\\\"PrivacyPolicy\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629105596644,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null},\\\\\\\\\\\\\\\"eSignature\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629105596644,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null},\\\\\\\\\\\\\\\"ElectronicTransactions\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"clicked\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\"clickedTimeStamp\\\\\\\\\\\\\\\":1629106172705,\\\\\\\\\\\\\\\"read\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\"readTimeStamp\\\\\\\\\\\\\\\":null,\\\\\\\\\\\\\\\"attachments\\\\\\\\\\\\\\\":[]}}\\\\\\\"\\\",\\\"workCityApp2\\\":\\\"Null\\\",\\\"products\\\":\\\"[\\\\\\\"\\\\\\\\\\\\\\\"{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productId\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"chk_summit\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productName\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"Summit Checking\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productAmount\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":0,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"isForFunding\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"true\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"productType\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"dda\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}\\\\\\\\\\\\\\\"\\\\\\\"]\\\",\\\"idNumApp2\\\":\\\"null\\\",\\\"carrierConsentId\\\":\\\"null\\\",\\\"isExistingCustomer\\\":\\\"false\\\",\\\"idIssueDateApp1\\\":\\\"11/11/2000\\\",\\\"totalApplicants\\\":\\\"1\\\",\\\"firstNameApp2\\\":\\\"Null\\\",\\\"productType\\\":\\\"dda\\\",\\\"remoteAddr\\\":\\\"0:0:0:0:0:0:0:1\\\",\\\"monthsOnJobApp2\\\":\\\"null\\\",\\\"workStateApp2\\\":\\\"null\\\",\\\"isCellphoneValidated\\\":\\\"false\\\",\\\"appAccountProducts\\\":\\\"[]\\\",\\\"custom\\\":\\\"\\\\\\\"\\\\\\\\\\\\\\\"[{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"employedOrUnemployed\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"empOptionEmployed\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"employerName\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"q2\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"occupation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"se\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"frequentTraveler\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":true,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"travelOutsideUs\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"safeDepositBox\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"declaredBehavior\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositExceed5000\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"sourceOfFunds\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"SOFGift\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositOrWriteChecks\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willDepositOrWithdrawCash\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willSendOrReceiveWireTransfers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"willsendReceiveNonWireTransfers\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"declaredBehavior\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"},{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"nonresident\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"usCitizen\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":true},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"haveDualCitizenship\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"areYouPEP\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false,\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"relationshipToPEP\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"PEPNo\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"foreignEmployee\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":false}},\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"nonresident\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"}]\\\\\\\\\\\\\\\"\\\\\\\"\\\",\\\"approvedEsignature\\\":\\\"true\\\",\\\"monthlyIncomeApp2\\\":\\\"null\\\",\\\"userAgent\\\":\\\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36\\\",\\\"idState\\\":\\\"WA\\\",\\\"smsValPk\\\":\\\"168\\\",\\\"middleNameApp2\\\":\\\"Null\\\",\\\"correlationID\\\":\\\"null\\\",\\\"isExistingMemberWorkflow\\\":\\\"false\\\",\\\"isFundingSourceVerified\\\":\\\"undefined\\\",\\\"carrierAuthenticationKey\\\":\\\"null\\\"}\",\"idFront\":\"\",\"idBack\":\"\",\"ipAddress\":\"\",\"employmentStatus\":\"\",\"employmentStatusApp2\":\"\",\"employerPhone\":\"\",\"employerPhoneApp2\":\"\",\"jobTitleApp2\":\"\",\"monthsOnJobApp2\":0,\"workAddr1App2\":\"\",\"workAddr2App2\":\"\",\"workCityApp2\":\"\",\"workStateApp2\":\"\",\"workPostalApp2\":\"\",\"companyApp2\":\"\",\"workPhoneApp1\":\"\",\"workPhoneApp2\":\"\",\"occupancyDuration\":0,\"occupancyDurationApp2\":0,\"occupancyStatusApp1\":\"\",\"productIds\":[],\"idState\":\"WA\",\"isNative\":false,\"isFramework\":false,\"sendApp2\":false,\"webAppAuthKey\":\"\",\"webAppAuthCode\":\"\",\"webAppAuthRef\":\"\",\"webAppAuthTime\":\"\",\"promoCode\":\"\",\"fundingSuccess\":false,\"isFundingSourceVerified\":false,\"overdraft\":false,\"created\":\"08-16-2021 09:29 AM\",\"newAccounts\":{},\"bankState\":\"\",\"bankName\":\"\",\"fundingType\":\"\",\"frontEndFunding\":\"\",\"bankAccountType\":\"\",\"transferAccountType\":\"\",\"nameOnCard\":\"\",\"routingNumber\":\"\",\"bankAccountNumber\":\"\",\"ccCardType\":\"\",\"ccCardNumber\":\"\",\"ccCardLast4\":\"\",\"ccCardExpDateMonth\":\"\",\"ccCardExpDateYear\":\"\",\"ccCvnNumber\":\"0\",\"ccChargeFailureCount\":0,\"amountDeposit\":0.0,\"bankNameOnCard\":\"\",\"transferAccountNumber\":\"\",\"billingStreetAddress\":\"\",\"billingCity\":\"\",\"billingState\":\"\",\"billingZip\":\"\",\"sourceOfFunds\":\"\",\"futureFundingFirstName\":\"\",\"futureFundingLastName\":\"\",\"futureFundingMiddleName\":\"\",\"futureFundingSuffix\":\"\",\"futureFundingSsn\":\"\",\"futureFundingIraType\":\"\",\"futureFundingContributionType\":\"\",\"futureFundingAccountNumber\":\"\",\"fundingSourceId\":\"\",\"clfFundingSourceId\":\"\",\"microdepositAmount1\":0.0,\"microdepositAmount2\":0.0,\"microdepositFailureCount\":0,\"microdepositStatus\":\"\",\"paypalPaykey\":\"\",\"signature\":\"\",\"guidId\":\"\",\"clfApprovedAccountTypeId\":\"\",\"clfInternalTransferId\":\"\",\"isCreated\":\"\",\"checkRoutingNumber\":\"\",\"fundingDate\":\"\",\"fundedStatus\":\"\",\"isRateOverride\":\"\",\"description\":\"\",\"serviceType\":\"\",\"serviceCode\":\"\",\"benefactorType\":\"\",\"beneficiaryPriority\":\"\",\"beneficiaryPercentShare\":0.0,\"paymentFrequency\":\"\",\"paymentAccountNumber\":\"\",\"paymentAccountType\":\"\",\"paymentRoutingNumber\":\"\",\"paymentFiName\":\"\",\"paymentFiCity\":\"\",\"paymentFiState\":\"\",\"paymentFiZip\":\"\",\"isInterestRateAnException\":\"\",\"iraType\":\"\",\"contributionType\":\"\",\"contributionYear\":\"\",\"isPrimaryRetirementAccount\":\"\",\"custodianName\":\"\",\"custodianAddress\":\"\",\"custodianCity\":\"\",\"custodianState\":\"\",\"custodianZip\":\"\",\"custodianPhone\":\"\",\"accountTransferringFrom\":\"\",\"transferFromType\":\"\",\"transferPercent\":0.0,\"closeIra\":\"\",\"placeInConduitIra\":\"\",\"assetDescription\":\"\",\"amountInIra\":0.0,\"amountToBeTransferred\":0.0,\"transferInstruction\":\"\",\"requiredMinDistribution\":\"\",\"canChangeBeneficiary\":\"\",\"continueServiceAfterAgeOfMaturity\":\"\",\"depositYear\":\"\",\"approvedAccountTypeLinkId\":\"\",\"referenceType\":\"\",\"referenceId\":\"\",\"isLinked\":\"\",\"dateLinked\":\"\",\"directDepositType\":\"\",\"applicationStatus\":\"incomplete\",\"applicationStatusDateTime\":\"Aug 16, 2021 9:20:05 AM\",\"appAccountType\":\"\",\"isAccountOpened\":false,\"offersRequired\":false,\"primaryShareAccountAmountDeposit\":0.0,\"chkGoldAmountDeposit\":0.0,\"wingsFoundationAmountDeposit\":0.0,\"backupWithHoldingFlag\":false,\"skipKBA\":false,\"skipDecisioning\":false,\"isAuthenticated\":false,\"existingCustomer\":false,\"relationshipId\":\"\",\"webAppResponseTokenValue\":\"\",\"webAppResponseTokenName\":\"\",\"webAppResponseTokenType\":\"\",\"webAppResponseTokenExpiry\":\"\",\"webAppResponseTokenSecNum\":\"\",\"webAppResponseErrorMessages\":\"\",\"achPPDEntry\":\"\",\"achFileStatus\":\"\",\"products\":[],\"customPages\":[]}";
        personMaintenanceRequest.applicant = new Gson().fromJson(s, Applicant.class);
        //Execute
        personMaintenanceRequest.generateFlexFields(personElement, "person.userFields", "UserFields", "UserField", "UserFieldCd");

    }

    @Test
    public void testPopulateValueForOccupationCustomKeyTest () throws Exception {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setJobTitle("SDE");
        Applicant applicant = new Applicant();
        String uv = "{\"occupation\":\"custOccupation\"}";
        applicant.setUnstructuredVars(uv);
        personMaintenanceRequest.setApplicant(applicant);
        personMaintenanceRequest.setActivePersonalInfo(personInfo);
        String customValue = personMaintenanceRequest.populateParams("<OCCUPATION>",false);
        assertEquals(customValue,"SDE");
    }

    @Test
    public void testPopulateValueForOccupationCustomKeyFromUVTest () throws Exception {
        PersonInfo personInfo = new PersonInfo();
        Applicant applicant = new Applicant();
        String uv = "  {\"custom\":\"\\\"\\\\\\\"[{\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"employedOrUnemployed\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"EmpOp2\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"occupation\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"occupationFromUv\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\"employerName\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"asdf\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\"acctpurpose\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Purpose2\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"employmentAndIncome\\\\\\\\\\\\\\\"}]\\\\\\\"\\\"\"}";
        applicant.setUnstructuredVars(uv);
        personMaintenanceRequest.setApplicant(applicant);
        personMaintenanceRequest.setActivePersonalInfo(personInfo);
        String customValue = personMaintenanceRequest.populateParams("<OCCUPATION>",false);
        assertEquals(customValue,"occupationFromUv");
    }

    @Test
    public void testPopulateValueForEmployerNameTest () throws Exception{
        PersonInfo personInfo = new PersonInfo();
        personInfo.setEmployerName("GRO");
        personInfo.setCompany("Q2");
        Applicant applicant = new Applicant();
        String uv = "  {\"custom\":\"\\\"\\\\\\\"[{\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"employedOrUnemployed\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"EmpOp2\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"occupation\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"custOccupation\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\"employerName\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"asdf\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\"acctpurpose\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Purpose2\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"employmentAndIncome\\\\\\\\\\\\\\\"}]\\\\\\\"\\\"\"}";
        applicant.setUnstructuredVars(uv);
        personMaintenanceRequest.setApplicant(applicant);
        personMaintenanceRequest.setActivePersonalInfo(personInfo);
        String customValue = personMaintenanceRequest.populateParams("<EMPLOYER_NAME>",false);
        assertEquals(customValue,"GRO");
    }

    @Test
    public void testPopulateValueForCompanyNameTest () throws Exception{
        PersonInfo personInfo = new PersonInfo();
        personInfo.setCompany("Q2");
        Applicant applicant = new Applicant();
        String uv = "  {\"custom\":\"\\\"\\\\\\\"[{\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"employedOrUnemployed\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"EmpOp2\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"occupation\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"custOccupation\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\"employerName\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"asdf\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\"acctpurpose\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Purpose2\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"employmentAndIncome\\\\\\\\\\\\\\\"}]\\\\\\\"\\\"\"}";
        applicant.setUnstructuredVars(uv);
        personMaintenanceRequest.setApplicant(applicant);
        personMaintenanceRequest.setActivePersonalInfo(personInfo);
        String customValue = personMaintenanceRequest.populateParams("<EMPLOYER_NAME>",false);
        assertEquals(customValue,"Q2");
    }

    @Test
    public void testPopulateValueForEmployerNameFromUvTest () throws Exception{
        PersonInfo personInfo = new PersonInfo();
        Applicant applicant = new Applicant();
        String uv = "  {\"custom\":\"\\\"\\\\\\\"[{\\\\\\\\\\\\\\\"generalInformation\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"employedOrUnemployed\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"EmpOp2\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"employerName\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"EmployerFromUv\\\\\\\\\\\\\\\"},\\\\\\\\\\\\\\\"employerName1\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"asdf\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\"acctpurpose\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"Purpose2\\\\\\\\\\\\\\\"}},\\\\\\\\\\\\\\\"name\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"employmentAndIncome\\\\\\\\\\\\\\\"}]\\\\\\\"\\\"\"}";
        applicant.setUnstructuredVars(uv);
        personMaintenanceRequest.setApplicant(applicant);
        personMaintenanceRequest.setActivePersonalInfo(personInfo);
        String customValue = personMaintenanceRequest.populateParams("<EMPLOYER_NAME>",false);
        assertEquals(customValue,"EmployerFromUv");
    }

    @Test
    public void testDemographicsFields () throws Exception{
        personMaintenanceRequest.setSettings(mockFiservDNASettings);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("elementValue","element1");
        jsonObject.put("attributeValue","attribute1");
        JSONObject result = new JSONObject();
        result.put("0",jsonObject);
        when(mockFiservDNASettings.getDemographics()).thenReturn(result);
        personMaintenanceRequest.setRequestDocument(requestDocument);
        personMaintenanceRequest.demographicsFields(personElement);

    }
    @Test
    public void testAddAccountRoles_ForSellerID() throws FiservDNAException, JSONException {
        String acctRolesSeller = "{\n" +
                "\t\"primaryRoleCode\": [\n" +
                "\t\t\"OEMP\"\n" +
                "\t],\n" +
                "\t\"jointRoleCode\": [\n" +
                "\t\t\"OWN\",\n" +
                "\t\t\"SIGN\"\n" +
                "\t],\n" +
                "\t\"minorRoleCode\": [\n" +
                "\t\t\"MINR\"\n" +
                "\t],\n" +
                "\t\"sellerID\": {\n" +
                "\t\t\"entityTypeCode\": \"PERS\",\n" +
                "\t\t\"accountRoleCode\": \"OEMP\",\n" +
                "\t\t\"accountRoleValue\": \"50000\"\n" +
                "\t},\n" +
                "\t\"entityTypeCode\": \"PERS\",\n" +
                "\t\"minorAge\": \"18\"\n" +
                "\n" +
                "}";
        JSONObject acctRoles = new JSONObject(acctRolesSeller);
        fiservDNASettings.setAccountRoles(acctRoles);


        personMaintenanceRequest.setSettings(fiservDNASettings);
        pmSpy = Mockito.spy(personMaintenanceRequest);
        String mockedXML = getMockedXML();
        String personInfoMocked = getPersonalinfo();

        // Prepare
        pmSpy.applicant = this.createApplicantForTesting();
        when(pmSpy.getXmlString()).thenReturn(mockedXML);
        PersonInfo personInfo = new Gson().fromJson(personInfoMocked, PersonInfo.class);
        Mockito.doReturn(personInfo).when(pmSpy).getActivePersonalInfo();

        //Execute
        boolean result = pmSpy.build(true);
        assertTrue(result);
    }


    @Test
    public void testAddAccountRoles_ForSellerID_NegativeCase() throws FiservDNAException, JSONException {
        String acctRolesSeller = "{\n" +
                "\t\"primaryRoleCode\": [\n" +
                "\t\t\"OEMP\"\n" +
                "\t],\n" +
                "\t\"jointRoleCode\": [\n" +
                "\t\t\"OWN\",\n" +
                "\t\t\"SIGN\"\n" +
                "\t],\n" +
                "\t\"minorRoleCode\": [\n" +
                "\t\t\"MINR\"\n" +
                "\t],\n" +
                "\t\"sellerID\": {\n" +
                "\t\t\"accountRoleCode\": \"OEMP\",\n" +
                "\t\t\"accountRoleValue\": \"50000\"\n" +
                "\t},\n" +
                "\t\"entityTypeCode\": \"PERS\",\n" +
                "\t\"minorAge\": \"18\"\n" +
                "\n" +
                "}";
        JSONObject acctRoles = new JSONObject(acctRolesSeller);
        fiservDNASettings.setAccountRoles(acctRoles);


        personMaintenanceRequest.setSettings(fiservDNASettings);
        pmSpy = Mockito.spy(personMaintenanceRequest);
        String mockedXML = getMockedXML();
        String personInfoMocked = getPersonalinfo();

        // Prepare
        pmSpy.applicant = this.createApplicantForTesting();
        when(pmSpy.getXmlString()).thenReturn(mockedXML);
        PersonInfo personInfo = new Gson().fromJson(personInfoMocked, PersonInfo.class);
        Mockito.doReturn(personInfo).when(pmSpy).getActivePersonalInfo();

        //Execute
        boolean result = pmSpy.build(true);
        assertTrue(result);
    }

    @Test
    public void testAddAccountRoles_ForSellerID_Null() throws FiservDNAException, JSONException {
        String acctRolesSeller = "{\n" +
                "\t\"primaryRoleCode\": [\n" +
                "\t\t\"OEMP\"\n" +
                "\t],\n" +
                "\t\"jointRoleCode\": [\n" +
                "\t\t\"OWN\",\n" +
                "\t\t\"SIGN\"\n" +
                "\t],\n" +
                "\t\"minorRoleCode\": [\n" +
                "\t\t\"MINR\"\n" +
                "\t],\n" +
                "\n" +
                "}";
        JSONObject acctRoles = new JSONObject(acctRolesSeller);
        fiservDNASettings.setAccountRoles(acctRoles);


        personMaintenanceRequest.setSettings(fiservDNASettings);
        pmSpy = Mockito.spy(personMaintenanceRequest);
        String mockedXML = getMockedXML();
        String personInfoMocked = getPersonalinfo();

        // Prepare
        pmSpy.applicant = this.createApplicantForTesting();
        when(pmSpy.getXmlString()).thenReturn(mockedXML);
        PersonInfo personInfo = new Gson().fromJson(personInfoMocked, PersonInfo.class);
        Mockito.doReturn(personInfo).when(pmSpy).getActivePersonalInfo();

        //Execute
        boolean result = pmSpy.build(true);
        assertTrue(result);
    }

    @Test
    public void testAddAccountRoles_accountRoleCode_Null() throws FiservDNAException, JSONException {
        String acctRolesSeller = "{\n" +
                "\t\"primaryRoleCode\": [\n" +
                "\t\t\"OEMP\"\n" +
                "\t],\n" +
                "\t\"jointRoleCode\": [\n" +
                "\t\t\"OWN\",\n" +
                "\t\t\"SIGN\"\n" +
                "\t],\n" +
                "\t\"minorRoleCode\": [\n" +
                "\t\t\"MINR\"\n" +
                "\t],\n" +
                "\t\"sellerID\": {\n" +
                "\t\t\"entityTypeCode\": \"\",\n" +
                "\t\t\"accountRoleCode\": \"\",\n" +
                "\t\t\"accountRoleValue\": \"\"\n" +
                "\t},\n" +
                "\t\"entityTypeCode\": \"PERS\",\n" +
                "\t\"minorAge\": \"18\"\n" +
                "\n" +
                "}";
        JSONObject acctRoles = new JSONObject(acctRolesSeller);
        fiservDNASettings.setAccountRoles(acctRoles);


        personMaintenanceRequest.setSettings(fiservDNASettings);
        pmSpy = Mockito.spy(personMaintenanceRequest);
        String mockedXML = getMockedXML();
        String personInfoMocked = getPersonalinfo();

        // Prepare
        pmSpy.applicant = this.createApplicantForTesting();
        when(pmSpy.getXmlString()).thenReturn(mockedXML);
        PersonInfo personInfo = new Gson().fromJson(personInfoMocked, PersonInfo.class);
        Mockito.doReturn(personInfo).when(pmSpy).getActivePersonalInfo();

        //Execute
        boolean result = pmSpy.build(true);
        assertTrue(result);
    }

    @Test
    public void test_AddressCodes() throws FiservDNAException, JSONException {
        String acctRolesSeller = "{\n" +
                "\t\"primaryRoleCode\": [\n" +
                "\t\t\"OEMP\"\n" +
                "\t],\n" +
                "\t\"jointRoleCode\": [\n" +
                "\t\t\"OWN\",\n" +
                "\t\t\"SIGN\"\n" +
                "\t],\n" +
                "\t\"minorRoleCode\": [\n" +
                "\t\t\"MINR\"\n" +
                "\t],\n" +
                "\t\"sellerID\": {\n" +
                "\t\t\"entityTypeCode\": \"\",\n" +
                "\t\t\"accountRoleCode\": \"\",\n" +
                "\t\t\"accountRoleValue\": \"\"\n" +
                "\t},\n" +
                "\t\"entityTypeCode\": \"PERS\",\n" +
                "\t\"minorAge\": \"18\"\n" +
                "\n" +
                "}";
        JSONObject acctRoles = new JSONObject(acctRolesSeller);
        fiservDNASettings.setAccountRoles(acctRoles);
        fiservDNASettings.setAddressUseCodePri("PHAD");
        fiservDNASettings.setAddressUseCodeMail("PRI");


        personMaintenanceRequest.setSettings(fiservDNASettings);
        pmSpy = Mockito.spy(personMaintenanceRequest);
        String mockedXML = getMockedXML();
        String personInfoMocked = getPersonalinfo();

        // Prepare
        pmSpy.applicant = this.createApplicantForTesting();
        when(pmSpy.getXmlString()).thenReturn(mockedXML);
        PersonInfo personInfo = new Gson().fromJson(personInfoMocked, PersonInfo.class);
        Mockito.doReturn(personInfo).when(pmSpy).getActivePersonalInfo();

        //Execute
        boolean result = pmSpy.build(true);
        assertTrue(result);
    }



}