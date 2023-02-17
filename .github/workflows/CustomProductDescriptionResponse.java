package com.msp.acct.exchange.responses;

import java.util.List;

/**
 * (c) 2016-2017 Gro Solutions, Inc. All Rights Reserved.
 *
 * This is the POJO that contains the request returns by the integration that return Product types.
 * This POJO used to iterate to get product type in getListOfProductTypes method.
 *
 * Keep in mind this object never can be pass to any Controller nor to the client-side.
 *
 * @author Moulya Pradeep.
 * @since 12/14/2020.
 */
public class CustomProductDescriptionResponse {

    private String productId;
    private String productName;
    private String title;
    private String bankAccount;
    private String configRef;
    private Boolean overdraftOptions;
    private String footer;
    private Boolean isRequired;
    private Boolean isDefault;
    private Boolean isHidden;
    private Boolean courtesyPay;
    private Boolean OLBEnroll;
    private Boolean isFunding;
    private String productType;
    private List<String> discDocs;
    private CustomProductBenefitsDataObject benefits;
    private CustomProductRequirementsDataObject requirements;
    private CustomProductFundingDataObject productFunding;
    private CustomAgeRulesDataObject ageRules;

    public CustomProductDescriptionResponse() {
    }

    public CustomProductDescriptionResponse(String productId, String productName, String configRef, String productType) {
        this.productId = productId;
        this.productName = productName;
        this.configRef = configRef;
        this.productType = productType;
    }

    public CustomProductDescriptionResponse(String productId, String productName, String title, String bankAccount, Boolean overdraftOptions, String footer, Boolean isRequired, Boolean isDefault, Boolean isHidden, Boolean courtesyPay, Boolean OLBEnroll, Boolean isFunding, String productType, List<String> discDocs) {
        this.productId = productId;
        this.productName = productName;
        this.title = title;
        this.bankAccount = bankAccount;
        this.overdraftOptions = overdraftOptions;
        this.footer = footer;
        this.isRequired = isRequired;
        this.isDefault = isDefault;
        this.isHidden = isHidden;
        this.courtesyPay = courtesyPay;
        this.OLBEnroll = OLBEnroll;
        this.isFunding = isFunding;
        this.productType = productType;
        this.discDocs = discDocs;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getConfigRef() {
        return configRef;
    }

    public void setConfigRef(String configRef) {
        this.configRef = configRef;
    }

    public Boolean getOverdraftOptions() {
        return overdraftOptions;
    }

    public void setOverdraftOptions(Boolean overdraftOptions) {
        this.overdraftOptions = overdraftOptions;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getHidden() {
        return isHidden;
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
    }

    public Boolean getCourtesyPay() {
        return courtesyPay;
    }

    public void setCourtesyPay(Boolean courtesyPay) {
        this.courtesyPay = courtesyPay;
    }

    public Boolean getOLBEnroll() {
        return OLBEnroll;
    }

    public void setOLBEnroll(Boolean OLBEnroll) {
        this.OLBEnroll = OLBEnroll;
    }

    public Boolean getFunding() {
        return isFunding;
    }

    public void setFunding(Boolean funding) {
        isFunding = funding;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public CustomProductBenefitsDataObject getBenefits() {
        return benefits;
    }

    public void setBenefits(CustomProductBenefitsDataObject benefits) {
        this.benefits = benefits;
    }

    public CustomProductRequirementsDataObject getRequirements() {
        return requirements;
    }

    public void setRequirements(CustomProductRequirementsDataObject requirements) {
        this.requirements = requirements;
    }

    public CustomProductFundingDataObject getProductFunding() {
        return productFunding;
    }

    public void setProductFunding(CustomProductFundingDataObject productFunding) {
        this.productFunding = productFunding;
    }

    public CustomAgeRulesDataObject getAgeRules() {
        return ageRules;
    }

    public void setAgeRules(CustomAgeRulesDataObject ageRules) {
        this.ageRules = ageRules;
    }

    public List<String> getDiscDocs() {
        return discDocs;
    }

    public void setDiscDocs(List<String> discDocs) {
        this.discDocs = discDocs;
    }
}
