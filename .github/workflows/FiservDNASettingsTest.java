package com.msp.acct.workflow.integrations.fiserv.dna;

import com.msp.acct.workflow.integrations.ApplicantBased;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * (c) 2016-2017 Gro Solutions, Inc. All Rights Reserved.
 * <p>
 * Unit test for Fiserv DNA Settings
 *
 * @author Moulya Pradeep
 * @since 11/25/2021
 */
@RunWith(PowerMockRunner.class)
public class FiservDNASettingsTest extends ApplicantBased {


    //Tells Mockito to create the mocks based on the @Mock annotation
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private FiservDNASettings fiservDNASettings;

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
                "    \"minorRoleCode\": [\n" +
                "      \"MINR\"\n" +
                "    ],\n" +
                "    \"primaryRoleCode\": [\n" +
                "      \"\"\n" +
                "    ]\n" +
                "  }";
        JSONObject acctRoles = new JSONObject(acctRolesString);
        fiservDNASettings.setAccountRoles(acctRoles);
    }



    @Test
    public void testSetIdentTypes() throws JSONException, FiservDNAException {
        //Execute
        JSONObject json = new JSONObject("{\"HELLO\":\"hi\"}");
        fiservDNASettings.setIdentTypes(json);
        assertNotNull(fiservDNASettings.getIdentType("HELLO"));
    }

    @Test
    public void testSetAccountRoles() throws JSONException {
        //Execute
        JSONObject json = new JSONObject("{\"HELLO\":\"hi\"}");
        fiservDNASettings.setAccountRoles(json);
        assertNotNull(fiservDNASettings.getAccountRoles());
    }

}
