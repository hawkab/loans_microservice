package com.hawkab.rest;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class LoanClaimData implements Serializable {

    private String uuid;
    private BigDecimal amount;
    private Integer duration;
    private String firstName;
    private String lastName;
    private String country;
    private String personnelId;
    private String productState;
    private String decisionDescription;

    public LoanClaimData() {
    }

    public LoanClaimData(String uuid, BigDecimal amount, Integer duration, String firstName, String lastName, String country, String personnelId, String productState, String decisionDescription) {
        this.uuid = uuid;
        this.amount = amount;
        this.duration = duration;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.personnelId = personnelId;
        this.productState = productState;
        this.decisionDescription = decisionDescription;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public String getDecisionDescription() {
        return decisionDescription;
    }

    public void setDecisionDescription(String decisionDescription) {
        this.decisionDescription = decisionDescription;
    }

    @Override
    public String toString() {
        return "LoanClaimData{" +
                "uuid='" + uuid + '\'' +
                ", amount=" + amount +
                ", duration=" + duration +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", personnelId='" + personnelId + '\'' +
                ", productState='" + productState + '\'' +
                ", decisionDescription='" + decisionDescription + '\'' +
                '}';
    }
}
