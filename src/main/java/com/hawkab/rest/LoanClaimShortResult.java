package com.hawkab.rest;


import java.io.Serializable;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class LoanClaimShortResult implements Serializable {

    private String loanId;
    private String status;

    public LoanClaimShortResult() {
    }

    public LoanClaimShortResult(String loanId, String status) {
        this.loanId = loanId;
        this.status = status;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LoanClaimShortResult{" +
                "loanId='" + loanId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
