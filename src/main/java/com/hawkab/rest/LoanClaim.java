package com.hawkab.rest;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class LoanClaim implements Serializable {

    private BigDecimal amount;
    private Integer duration;
    private String firstName;
    private String lastName;
    private String country;
    private String personnelId;

    public LoanClaim() {
    }

    public LoanClaim(BigDecimal amount, Integer duration, String firstName, String lastName, String country, String personnelId) {
        this.amount = amount;
        this.duration = duration;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.personnelId = personnelId;
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

    public String getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "LoanClaim{" +
                "amount=" + amount +
                ", duration=" + duration +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", personnelId='" + personnelId + '\'' +
                '}';
    }
}
