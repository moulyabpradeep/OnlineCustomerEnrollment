package com.msp.acct.workflow.integrations.aogateway_services;

import com.google.gson.Gson;
import com.msp.acct.data.Applicant;
import com.msp.acct.data.ProductFunding;
import com.msp.acct.data.message.GroApplicant;
import com.msp.acct.data.service.ApplicantService;
import com.msp.acct.data.service.groapi.GroApplication;
import com.msp.acct.data.service.groapi.GroApplicationMapper;
import com.msp.acct.data.service.groapi.GroMapperConstants;
import com.msp.acct.exception.GroException;
import com.msp.acct.exchange.requests.CheckEMWRequest;
import com.msp.acct.exchange.responses.DecisionServerResponse;
import com.msp.acct.util.AccountUtil;
import com.msp.acct.util.CustomerUtil;
import com.msp.acct.util.MspUtil;
import com.msp.acct.util.logger.Logger;
import com.msp.acct.util.logger.LoggerFactory;
import com.msp.acct.workflow.integrations.AdapterUtility;
import com.msp.acct.workflow.integrations.Integration;
import com.msp.acct.workflow.integrations.IntegrationJsonObject;
import com.msp.acct.workflow.integrations.apex_xp2.MessageExecutor;
import com.msp.acct.workflow.integrations.groserviceV2.AuthService;
import com.msp.acct.workflow.modules.EMC;
import com.msp.acct.workflow.submission.SubmissionWorkflowStep;
import freemarker.template.TemplateException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static com.msp.acct.data.service.analytics.Constants.*;
import static com.msp.acct.data.service.groapi.GroMapperConstants.ApplicantType.PRIMARY;
import static com.msp.acct.util.MessagesUtil.HTML_BREAK_LINE;
import static com.msp.acct.workflow.integrations.aogateway_services.Constants.*;
import static com.msp.acct.util.Constants.*;
import static com.msp.acct.util.XMLUtil.NOT_FOUND_ELEMENT_STRING;
//import static com.msp.acct.workflow.integrations.aogateway_services.Constants.BASE_PACKAGE_PATH;
//import static com.msp.acct.workflow.integrations.aogateway_services.Constants.JSON_KEY_INST_RT_ID;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * (c) 2016-2018 Gro Solutions, Inc. All Rights Reserved.
 * <p>
 * Integration for Silver Lake. Implements Core - Check Existing Customer, Submission steps (CustAdd, AcctIdGen, AcctAdd)
 *
 * @author Moulya Pradeep
 * @since June/22/2021
 */
@Lazy
@Scope(PROTOTYPE)
@Service(GLOBAL_SUBMIT_TO_CORE)
public class GlobalSubmitToCore extends Integration implements EMC, SubmissionWorkflowStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.msp.acct.workflow.integrations.aogateway_services.GlobalSubmitToCore.class.getName());

    // Notice how using constructor injection lets the field be marked as final, indicating that it cannot be subsequently changed.
    private final AccountUtil accountUtil;
    private final CustomerUtil customerUtil;
    private final ApplicantService applicantService;
    private final AuthService authService;

    @Autowired
    GlobalSubmitToCore(final AccountUtil accountUtil, final CustomerUtil customerUtil, final ApplicantService applicantService, final AuthService authService) {
        this.accountUtil = accountUtil;
        this.customerUtil = customerUtil;
        this.applicantService = applicantService;
        this.authService = authService;
    }

    @Override
    public void setUp() {
        LOGGER.debug("Initializing GlobalSubmitToCore Integration.");

        // only initialize if null (for testability)
        super.prepareConfiguration(BASE_PACKAGE_PATH);
    }

    ConnectionDetail getConnectionDetail() {
        LOGGER.debug("Pulling settings ...");
        final IntegrationJsonObject connectionDetails = riskEngineUtil.getIntegrationJsonObject(EXISTING_MEMBER_CHECK_CONNECTION_DETAILS);
        if (connectionDetails == null) {
            LOGGER.error("Error loading {} from CONFIG table. Is data present and consistent?", EXISTING_MEMBER_CHECK_CONNECTION_DETAILS);
            throw new GroException("Could not get setting form " + EXISTING_MEMBER_CHECK_CONNECTION_DETAILS);
        } else {
            return ConnectionDetail.toObject(connectionDetails);
        }
    }

    IntegrationJsonObject getEmcSettings() {
        IntegrationJsonObject emcSettings = riskEngineUtil.getIntegrationJsonObject(EXISTING_MEMBER_CHECK_SETTINGS);
        if (emcSettings == null) {
            LOGGER.error("Error loading {}, {} from CONFIG table. Is data present and consistent?", EXISTING_MEMBER_CHECK_SETTINGS);
            throw new GroException("Could not get setting form "  + EXISTING_MEMBER_CHECK_SETTINGS);
        } else {
            return emcSettings;
        }
    }

    /**
     * Checks if the applicant is already a customer of the financial institution
     *
     * @param applicant     The applicant instance
     * @param applicantType the applicant's type (1 for the primary, 2, 3, 4 for the joints)
     * @return A map with found or not found and messages for the consumer (of the method)
     * <p>
     * TODO: For Core Director we should use CustInq instead.
     */
    @Override
    public Map<String, String> checkExistingCustomer(Applicant applicant, GroMapperConstants.ApplicantType applicantType) {
        LOGGER.info("Checking Existing Customer Call to Kessel ...");

        Map<String, String> returnData = new HashMap<>(2);
        String internalComment="";
        try {
            GroApplication groApplication = GroApplicationMapper.toObject(applicant);

            //setup the ao gateway services settings
            RestTemplate restTemplate = new RestTemplate();
            JSONObject aoSetting = null;
            String baseURL = "";
            String coreEndpoint = "";
            String url = "";
            HttpHeaders headers = new HttpHeaders();

            if (riskEngineUtil.getConfigurationData(AO_GATEWAY_SERVICE_SETTING) != null)
                aoSetting = new JSONObject(riskEngineUtil.getConfigurationData(AO_GATEWAY_SERVICE_SETTING));
            if (aoSetting != null) {
                JSONObject core = aoSetting.getJSONObject(SUBMIT_TO_CORE_BLOCK);
                if (core.getString(SUBMIT_TO_CORE_BASE_URL) != null)
                    baseURL = core.getString(SUBMIT_TO_CORE_BASE_URL);
                if (core.getString(SUBMIT_TO_CORE_ENDPOINT) != null)
                    coreEndpoint = core.getString(SUBMIT_TO_CORE_ENDPOINT);
            }
            ResponseEntity<String> responseString = null;
            url = baseURL + coreEndpoint;
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, String> clientSecrets = this.authService.getClientSecrets();
            SubmitToCoreRequest submitToCoreRequest = populateAOGatewayRequestObject(applicant, groApplication, ActionType.EMC.name(), applicantType.name(),null,null);
            String submitToCoreRequestString = new Gson().toJson(submitToCoreRequest);
            String hmacValue = clientSecrets.get(CUSTOMER_ID_STR) + ":" + MspUtil.calculateHMAC(clientSecrets.get(HMAC_KEY_STR), submitToCoreRequestString);
            headers.set(HTTP_HEADER_KEY_HMAC_KEY, hmacValue);
            HttpEntity<String> entity = new HttpEntity<>(submitToCoreRequestString, headers);
            responseString = restTemplate.postForEntity(url, entity, String.class);
            LOGGER.info("Response from Core Integration to Kessel " + responseString);
            if (responseString.getStatusCode().equals(HttpStatus.OK)) {
                LOGGER.info("GRO Acct data were successfully sent to Kessel");
            }
            //Todo : parse response string



                // check tin codes from config
            int applicationID = applicant.getMspApplicationId();
//            if (custIds == null || NOT_FOUND_EMPTY_LIST.equals(custIds.toString())) {
//                customerUtil.saveCustomerId(applicationID, applicantType, null);
//                internalComment = messagesUtil.getLocaleMessage(CUST_SRCH_NOT_EXIST_INTERNAL_COMMENT_KEY, name);
//                returnData.put(KEY_STATUS, RESPONSE_DOES_NOT_EXIST);
//            } else if(custIds.size() == COUNT_ONE) {
//                String custId = custIds.get(0);
//                customerUtil.saveCustomerId(applicationID, applicantType, custId);
//                returnData.put(CUST_ID_TAG, custId);
//                internalComment = messagesUtil.getLocaleMessage(CUST_SRCH_EXIST_INTERNAL_COMMENT_KEY, name, custId);
//                returnData.put(KEY_STATUS, RESPONSE_EXISTS);
//            } else if(custIds.size() > COUNT_ONE){
//                //If the multiple customer is found then set status to referred
//                applicantService.updateApplicationStatusByPk(applicationID, RESPONSE_REFERRED);
//                internalComment = messagesUtil.getLocaleMessage(CUST_SRCH_REFERRED_INTERNAL_COMMENT_KEY, applicant.getSSN());
//                returnData.put(KEY_STATUS, RESPONSE_REFERRED);
//            }
            addInternalComment(returnData, internalComment);
//        } catch (IOException ioe) {
//            LOGGER.error(ERROR_MSG_PREFIX_FOR_CHEC + FREEMARKER_TEMPLATE_NOT_CONFIGURED_PROPERLY, ioe);
//            throw new GroException(FREEMARKER_TEMPLATE_NOT_CONFIGURED_PROPERLY, ioe);
//        } catch (TemplateException te) {
//            LOGGER.error(ERROR_MSG_PREFIX_FOR_CHEC + ERROR_ADDING_VALUES_TO_TEMPLATE, te);
//            throw new GroException(ERROR_ADDING_VALUES_TO_TEMPLATE, te);
        }
        catch (Exception e) {
            returnData.put(KEY_STATUS, RESPONSE_FAILED);
            internalComment = Util.getErrorFromException(e);
            addInternalComment(returnData, internalComment);
            LOGGER.error(ERROR_MSG_PREFIX_FOR_CHEC + internalComment);
        }
        return returnData;
    }

    private SubmitToCoreRequest populateAOGatewayRequestObject(Applicant applicant, GroApplication groApplication, String actionType, String requestApplicantType,
                                                               String emwId, String emwIdType) {
        Gson gson = new Gson();
        SubmitToCoreRequest submitToCoreRequest = new SubmitToCoreRequest();
        ConnectionDetail connectionDetail = getConnectionDetail();
        submitToCoreRequest.setCoreType(com.msp.acct.workflow.integrations.aogateway_services.Constants.CoreType.SILVERLAKE.name());
        submitToCoreRequest.setApplicant(gson.toJson(applicant));
        submitToCoreRequest.setGroApplication(gson.toJson(groApplication));
        submitToCoreRequest.setActionType(actionType);
        submitToCoreRequest.setConnection(gson.toJson(connectionDetail));
        //TODO :
        CoreSpecificSettings coreSpecificSettings = new CoreSpecificSettings();
        IntegrationJsonObject emcSettings = getEmcSettings();
        String emcSetting = emcSettings.toString();
        coreSpecificSettings.setEmcSetting(emcSetting);
        coreSpecificSettings.setInstRtId(Util.getURLParamOrConfiguredValue(connectionDetail.instRtId, JSON_KEY_INST_RT_ID, groApplication));
        coreSpecificSettings.setLogTrackingId(MspUtil.getRandomID());
        coreSpecificSettings.setRequestApplicantType(requestApplicantType);
        coreSpecificSettings.setEmwId(emwId);
        coreSpecificSettings.setEmwIdType(emwIdType);
        submitToCoreRequest.setCoreSpecificSettings(gson.toJson(coreSpecificSettings));
        return submitToCoreRequest;
    }

    @Override
    public List<GECResponse> getExistingCustomer(CheckEMWRequest checkEMWRequest) {
        LOGGER.info("Getting Existing Customer ...");
        //Preparing this object for the response
        List<GECResponse> gecResponseList = new ArrayList<>();
        String xmlResponse;
        String ssn = "";
        String custId = checkEMWRequest.getId();

        GroApplication groApplication = new GroApplication();
        groApplication.setUrlParams(checkEMWRequest.getUrlParams());
        try {
            //setup the ao gateway services settings
            RestTemplate restTemplate = new RestTemplate();
            JSONObject aoSetting = null;
            String baseURL = "";
            String coreEndpoint = "";
            String url = "";
            HttpHeaders headers = new HttpHeaders();

            if (riskEngineUtil.getConfigurationData(AO_GATEWAY_SERVICE_SETTING) != null)
                aoSetting = new JSONObject(riskEngineUtil.getConfigurationData(AO_GATEWAY_SERVICE_SETTING));
            if (aoSetting != null) {
                JSONObject core = aoSetting.getJSONObject(SUBMIT_TO_CORE_BLOCK);
                if (core.getString(SUBMIT_TO_CORE_BASE_URL) != null)
                    baseURL = core.getString(SUBMIT_TO_CORE_BASE_URL);
                if (core.getString(SUBMIT_TO_CORE_ENDPOINT) != null)
                    coreEndpoint = core.getString(SUBMIT_TO_CORE_ENDPOINT);
            }
            ResponseEntity<String> responseString = null;
            url = baseURL + coreEndpoint;
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, String> clientSecrets = this.authService.getClientSecrets();
            SubmitToCoreRequest submitToCoreRequest = populateAOGatewayRequestObject(null, groApplication, ActionType.EMW.name(), null, checkEMWRequest.getId(), checkEMWRequest.getIdType().name());
            String submitToCoreRequestString = new Gson().toJson(submitToCoreRequest);
            String hmacValue = clientSecrets.get(CUSTOMER_ID_STR) + ":" + MspUtil.calculateHMAC(clientSecrets.get(HMAC_KEY_STR), submitToCoreRequestString);
            headers.set(HTTP_HEADER_KEY_HMAC_KEY, hmacValue);
            HttpEntity<String> entity = new HttpEntity<>(submitToCoreRequestString, headers);
            responseString = restTemplate.postForEntity(url, entity, String.class);
            LOGGER.info("Response from Core Integration to Kessel " + responseString);
            if (responseString.getStatusCode().equals(HttpStatus.OK)) {
                LOGGER.info("GRO Acct data were successfully sent to Kessel");
            }
            //TODO : parse response string and return
            GECResponse gecResponse = null;
            gecResponseList.add(gecResponse);

//        } catch (IOException ioe) {
//            LOGGER.error(ERROR_MSG_PREFIX_FOR_GHEC + FREEMARKER_TEMPLATE_NOT_CONFIGURED_PROPERLY, ioe);
//            throw new GroException(FREEMARKER_TEMPLATE_NOT_CONFIGURED_PROPERLY, ioe);
//        } catch (TemplateException te) {
//            LOGGER.error(ERROR_MSG_PREFIX_FOR_GHEC + ERROR_ADDING_VALUES_TO_TEMPLATE, te);
//            throw new GroException(ERROR_ADDING_VALUES_TO_TEMPLATE, te);
        } catch (Exception e) {
            String errDesc = Util.getErrorFromException(e);
            LOGGER.error(ERROR_MSG_PREFIX_FOR_GHEC + errDesc);
        }
        return gecResponseList;
    }




    @Override
    public DecisionServerResponse performSubmission(Applicant applicant, String customerId, JSONObject settings) {
        DecisionServerResponse decisionServerResponse = new DecisionServerResponse(Decision.STOP);

        Setting setting;
        String submissionStopInternalCommentKey, submissionStep, submissionStepNameKey = NO_STEP_INTERNAL_COMMENT_KEY;

        try {
            setting = Setting.toObject(settings);
            submissionStep = setting.getSubmissionStep();
            LOGGER.info("Submission Step: " + submissionStep);
            submissionStepNameKey = submissionStep + STEP_NAME_INTERNAL_COMMENT_KEY;
            submissionStopInternalCommentKey = submissionStep + STOP_INTERNAL_COMMENT_KEY;
            int applicationPK = applicant.getMspApplicationId();
            String logMessage = messagesUtil.getLocaleMessage(submissionStopInternalCommentKey);
            String clientMessage = messagesUtil.getLocaleMessage(SUBMISSION_FAILED_CLIENT_MESSAGE_KEY);
            decisionServerResponse.setMessage(clientMessage);
            // Mapping the legacy applicant object into the GroApplication object for an easy interaction.
            GroApplication groApplication = GroApplicationMapper.toObject(applicant);
            // Text case conversion
            AdapterUtility.getAppCaseTypeSetting(settings)
                    .ifPresent(appCaseTypeSetting -> this.applicantService
                            .changeCaseType(groApplication, appCaseTypeSetting));
            switch (submissionStep) {
                case SUBMISSION_STEP_CUST_ADD:
                    for (GroMapperConstants.ApplicantType applicantType : groApplication.getGroApplicants().keySet()) {
                        List<String> addrAddResList = new ArrayList<>();
                        String idVerifyAddRes = RS_STAT_SUCCESS_VALUE;
                        GroApplicant groApplicant = groApplication.getGroApplicants().get(applicantType);
                        if (isBlank(groApplicant.getCustomerId())) {
                            MessageExecutor messageExecutor = new MessageExecutor(configuration, setting)
                                    .setGroApplication(groApplication)
                                    .setGroApplicant(groApplicant);
                            String custId = messageExecutor.sendCustAdd();
                            if (!NOT_FOUND_ELEMENT_STRING.equals(custId)) {
                                // Add additional address (street or mailing address depends on configuration)
                                addrAddResList = messageExecutor.sendAddrAdd(custId);
                                // Add id type (DL, Passport, ...)
                                idVerifyAddRes = messageExecutor.sendIdVerifyAdd(custId);
                                decisionServerResponse.setDecision(Decision.CONTINUE);
                                clientMessage = messagesUtil.getLocaleMessage(SUCCESS_CLIENT_MESSAGE_KEY);
                                customerUtil.saveCustomerId(applicationPK, applicantType, String.valueOf(custId));
                                String submissionContinueInternalCommentKey = submissionStep + CONTINUE_INTERNAL_COMMENT_KEY;
                                String name = groApplicant.getFirstName() + " " + groApplicant.getLastName();
                                logMessage = messagesUtil.getLocaleMessage(submissionContinueInternalCommentKey, name, custId);
                            }
                        } else {
                            // TODO: ask if the SSN is the same.
                            decisionServerResponse.setDecision(Decision.CONTINUE);
                            logMessage = messagesUtil.getLocaleMessage(CUST_ADD_NOT_EXEC_INTERNAL_COMMENT_KEY, PRIMARY.equals(applicantType)?INTERNAL_COMMENTS_PRIMARY:INTERNAL_COMMENTS_JOINT,groApplicant.getCustomerId());
                            clientMessage = "";
                        }
                        String stepName = this.messagesUtil.getLocaleMessage(submissionStepNameKey);
                        this.messagesUtil.writeToInternalCommentsByRisk(applicationPK, CALLER_METHOD_SL_ADAPTER, stepName, logMessage);
                        for (String addrAddRes : addrAddResList) {
                            if (!addrAddRes.equalsIgnoreCase(RS_STAT_SUCCESS_VALUE)) {
                                // addrAddRes contains the error message
                                stepName = this.messagesUtil.getLocaleMessage(ADDR_ADD_STEP_NAME_INTERNAL_COMMENT_KEY);
                                this.messagesUtil.writeToInternalCommentsByRisk(applicationPK, CALLER_METHOD_SL_ADAPTER, stepName, addrAddRes);
                            }
                        }
                        if (!idVerifyAddRes.equalsIgnoreCase(RS_STAT_SUCCESS_VALUE)) {
                            // idVerifyAddRes contains the error message
                            stepName = this.messagesUtil.getLocaleMessage(ID_VERIFY_ADD_STEP_NAME_INTERNAL_COMMENT_KEY);
                            this.messagesUtil.writeToInternalCommentsByRisk(applicationPK, CALLER_METHOD_SL_ADAPTER, stepName, idVerifyAddRes);
                        }
                    }
                    break;
                case SUBMISSION_STEP_ACCT_ADD:
                    if (isNotBlank(customerId)) {
                        // Pulling the unprocessed products for the current Application.
                        List<ProductFunding> productFundingList = AdapterUtility.getUnprocessedProducts(groApplication.getAccountList(), applicant, data);
                        if (productFundingList != null && !productFundingList.isEmpty()) {
                            String jointCustomerId = null;
                            if (applicant.hasJointAccount()) {
                                jointCustomerId = customerUtil.getCustomerId(applicationPK, GroMapperConstants.ApplicantType.JOINT1);
                            }
                            JSONObject productMap = settings.getJSONObject(JSON_KEY_PRODUCTS);
                            MessageExecutor messageExecutor = new MessageExecutor(configuration, setting)
                                    .setGroApplication(groApplication);
                            AcctIdContainer acctIdContainer = AcctIdGenBuilder
                                    .create()
                                    .setMessageExecutor(messageExecutor)
                                    .setProducts(productMap)
                                    .setProductFundingList(productFundingList)
                                    .build();
                            //Mandatory, set the mspApplicationId before use any method of this utility.
                            this.accountUtil.setApplicant(applicant);
                            for (int i = 0; i < productFundingList.size(); i++) {
                                ProductFunding productFunding = productFundingList.get(i);
                                AccountSetting accountSetting = com.msp.acct.workflow.integrations.silver_lake.Util.getAccountSetting(productMap, productFunding.getProductId());
                                if (accountSetting != null) {
                                    com.msp.acct.workflow.integrations.silver_lake.Acct acct = acctIdContainer.getCode(productFunding.getProductId());
                                    accountSetting.acctId = acct.acctId.trim();
                                    if (acct.brCode != null) accountSetting.brCode = acct.brCode;
                                    accountSetting.custId = customerId;
                                    accountSetting.jointCustId = jointCustomerId;
                                    accountSetting.productAmount = productFunding.getProductAmount();
                                    if (productFunding.getTerm() != null)
                                        accountSetting.term = productFunding.getTerm();
                                    List<com.msp.acct.workflow.integrations.silver_lake.AcctAddBuilderResponse> responseList = AcctAddBuilder.create()
                                            .setMessagesUtil(messagesUtil)
                                            .setMessageExecutor(messageExecutor)
                                            .setAccountSetting(accountSetting)
                                            .setEmbosName(com.msp.acct.workflow.integrations.silver_lake.Util.getEmbosName(applicant,false))
                                            .setEmbosJointName(com.msp.acct.workflow.integrations.silver_lake.Util.getEmbosName(applicant,true))
                                            .build();
                                    for (com.msp.acct.workflow.integrations.silver_lake.AcctAddBuilderResponse response : responseList) {
                                        if (RS_STAT_SUCCESS_VALUE.equals(response.getMessage())) {
                                            decisionServerResponse.setDecision(Decision.CONTINUE);
                                            AccountUtil.AccountInfo accountInfo = new AccountUtil.AccountInfo(productFunding);
                                            accountInfo.setAccountNumber(accountSetting.acctId);
                                            accountInfo.setAPY(accountSetting.intRate);
                                            Date date = new Date();
                                            accountInfo.setMaturityDate(com.msp.acct.workflow.integrations.silver_lake.Util.getMaturityDate(date, accountSetting.term));
                                            this.accountUtil.appendAccount(accountInfo);
                                            String submissionContinueInternalCommentKey = submissionStep + CONTINUE_INTERNAL_COMMENT_KEY;
                                            logMessage = messagesUtil.getLocaleMessage(submissionContinueInternalCommentKey, productFunding.getProductName(), accountSetting.acctId);
                                            if (i < productFundingList.size() - 1) {
                                                messagesUtil.writeToInternalCommentsByRisk(applicationPK, CALLER_METHOD_SL_ADAPTER, messagesUtil.getLocaleMessage(submissionStepNameKey), logMessage);
                                            }
                                        } else {
                                            messagesUtil.writeToInternalCommentsByRisk(applicationPK, CALLER_METHOD_SL_ADAPTER, response.getStepName(), response.getMessage());
                                        }
                                    }
                                } else {
                                    logMessage = messagesUtil.getLocaleMessage(ACCOUNT_NOT_CONFIGURED_INTERNAL_COMMENT_KEY, productFunding.getProductName());
                                    if (i < productFundingList.size() - 1) {
                                        messagesUtil.writeToInternalCommentsByRisk(applicationPK, CALLER_METHOD_SL_ADAPTER, messagesUtil.getLocaleMessage(submissionStepNameKey), logMessage);
                                    }
                                }
                            }
                            this.accountUtil.saveAccounts();
                        } else {
                            logMessage = messagesUtil.getLocaleMessage(ACCT_ADD_NOT_EXEC_INTERNAL_COMMENT_KEY);
                        }
                        String products = messagesUtil.getCongratsMessageProductList(applicant);
                        clientMessage = HTML_BREAK_LINE
                                .concat(messagesUtil.getLocaleMessage(CONGRATS_MESSAGE_PRODUCTS_ACCEPTED))
                                .concat(HTML_BREAK_LINE)
                                .concat(products);
                    } else {
                        logMessage = messagesUtil.getLocaleMessage(ACCT_ADD_CUSTOMER_NOT_FOUND_INTERNAL_COMMENT_KEY);
                    }
                    decisionServerResponse.setLogStatus(messagesUtil, messagesUtil.getLocaleMessage(submissionStepNameKey), logMessage);
                    break;
                default: break;
            }
            decisionServerResponse.setMessage(clientMessage);
        } catch (IOException ioe) {
            LOGGER.error("Method: performSubmission(). Step Name: " + messagesUtil.getLocaleMessage(submissionStepNameKey) + ". " + FREEMARKER_TEMPLATE_NOT_CONFIGURED_PROPERLY, ioe);
            decisionServerResponse.setDecision(Decision.STOP);
            decisionServerResponse.setLogStatus(messagesUtil, messagesUtil.getLocaleMessage(submissionStepNameKey), FREEMARKER_TEMPLATE_NOT_CONFIGURED_PROPERLY);
        } catch (TemplateException te) {
            LOGGER.error("Method: performSubmission(). Step Name: " + messagesUtil.getLocaleMessage(submissionStepNameKey) + ". " + ERROR_ADDING_VALUES_TO_TEMPLATE, te);
            decisionServerResponse.setLogStatus(messagesUtil, messagesUtil.getLocaleMessage(submissionStepNameKey), ERROR_ADDING_VALUES_TO_TEMPLATE);
        } catch (GroException e) {
            String errDesc = Util.getErrorFromException(e);
            decisionServerResponse.setDecision(Decision.STOP);
            decisionServerResponse.setLogStatus(messagesUtil, messagesUtil.getLocaleMessage(submissionStepNameKey), errDesc);
            LOGGER.error("Method: performSubmission(). Step Name: " + messagesUtil.getLocaleMessage(submissionStepNameKey) + ". " + errDesc);
        } catch (JSONException e) {
            decisionServerResponse.setDecision(Decision.STOP);
            decisionServerResponse.setLogStatus(messagesUtil, messagesUtil.getLocaleMessage(submissionStepNameKey), e.getMessage());
            LOGGER.error("Method: performSubmission(). Step Name: " + messagesUtil.getLocaleMessage(submissionStepNameKey) + ". " + e.getMessage());
            return decisionServerResponse;
        }
        return decisionServerResponse;
    }
}