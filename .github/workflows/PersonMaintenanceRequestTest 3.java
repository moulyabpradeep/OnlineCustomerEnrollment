package com.msp.acct.workflow.integrations.fiserv.dna;

import com.google.gson.Gson;
import com.msp.acct.util.*;
import com.msp.acct.workflow.integrations.ApplicantBased;
import com.msp.acct.workflow.integrations.fiserv.dna.beans.PersonInfo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import javax.xml.parsers.ParserConfigurationException;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * (c) 2016-2017 Gro Solutions, Inc. All Rights Reserved.
 * <p>
 * Unit test for Fiserv Person Maintenance Request
 *
 * @author Moulya Pradeep
 * @since 5/11/2021
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UnstructuredVarsUtil.class, MessagesUtil.class})
public class PersonMaintenanceRequestTest extends ApplicantBased {


    //Tells Mockito to create the mocks based on the @Mock annotation
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private CommonRequest personMaintenanceRequest = new PersonMaintenanceRequest();

    /**
     * Sets up the fiserv DNA integration for testing
     *
     * @throws Exception if test fails
     */
    @Before
    public void setUp() throws Exception {
        initMocks(this);

    }

    private String getPersonalinfo() {
        return "{\"firstName\":\"Test\",\"middleName\":\"\",\"lastName\":\"Lastnam\",\"ssn\":\"111111112\",\"birthDate\":\"11/11/2000\"," +
                "\"mothersMaidenName\":\"mom\",\"idType\":\"driversLicense\",\"idState\":\"MA\",\"idIssued\":\"11/11/2000\",\"idExpires\":\"11/11/2022\"," +
                "\"idNumber\":\"222222\",\"employmentStatus\":\"Employed\",\"employerPhone\":\"\",\"jobTitle\":\"Sse\",\"monthsOnJob\":0," +
                "\"addr1\":\"123 Jefferson Street\",\"city\":\"Atlanta\",\"state\":\"MA\",\"postal\":\"02453\",\"workAddr1\":\"\",\"workAddr2\":\"\"," +
                "\"emailId\":\"m.ss@q2.com\",\"workCity\":\"\",\"workState\":\"\",\"workPostal\":\"\",\"company\":\"\",\"mobilePhone\":\"(098) 765-4321\"," +
                "\"homePhone\":\"\",\"workPhone\":\"\",\"occupancyDuration\":0,\"monthlyRent\":\"\",\"employerName\":\"\",\"middleInitial\":\"\"}";
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
    public void testBuildRequestForJointApplicant() throws ParserConfigurationException, FiservDNAException {
        String mockedXML = getMockedXML();
        String personInfoMocked = getPersonalinfo();

        // Prepare
        personMaintenanceRequest.applicant = this.createApplicantForTesting();
        when(personMaintenanceRequest.getXmlString()).thenReturn(mockedXML);
        PersonInfo personInfo = new Gson().fromJson(personInfoMocked, PersonInfo.class);
        doReturn(personInfo).when(personMaintenanceRequest).getActivePersonalInfo();

        //Execute
        personMaintenanceRequest.build(true);
        String requestXml = personMaintenanceRequest.getXmlString();
        assertEquals(mockedXML, requestXml);
    }

}
