package com.hawkab.rest;


import java.io.Serializable;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class LoanClaimInfo implements Serializable {

    private String loanId;
    private String personnelId;

    public LoanClaimInfo() {
    }

    public LoanClaimInfo(String loanId, String personnelId) {
        this.loanId = loanId;
        this.personnelId = personnelId;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    @Override
    public String toString() {
        return "LoanClaimInfo{" +
                "loanId='" + loanId + '\'' +
                ", personnelId='" + personnelId + '\'' +
                '}';
    }
}
