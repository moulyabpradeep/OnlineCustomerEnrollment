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
public class CustomProductDescriptionDataObject {

    private String product_id;
    private String name;
    private String title;
    private String bankAccount;
    private List<String> discDocs;
    private String configRef;
    private Boolean overdraftOptions;
    private String footer;
    private Boolean isRequired;
    private Boolean isDefault;
    private Boolean isHidden;
    private Boolean courtesyPay;
    private Boolean OLBEnroll;
    private Boolean isFunding;
    private String product_type;
    private CustomProductBenefitsDataObject benefits;
    private CustomProductRequirementsDataObject requirements;
    private CustomProductFundingDataObject funding;
    private CustomAgeRulesDataObject ageRules;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<String> getDiscDocs() {
        return discDocs;
    }

    public void setDiscDocs(List<String> discDocs) {
        this.discDocs = discDocs;
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

    public Boolean getIsFunding() {
        return isFunding;
    }

    public void setFunding(Boolean funding) {
        isFunding = funding;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
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

    public CustomProductFundingDataObject getFunding() {
        return funding;
    }

    public void setFunding(CustomProductFundingDataObject funding) {
        this.funding = funding;
    }

    public CustomAgeRulesDataObject getAgeRules() {
        return ageRules;
    }

    public void setAgeRules(CustomAgeRulesDataObject ageRules) {
        this.ageRules = ageRules;
    }
}