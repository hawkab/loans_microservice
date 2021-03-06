package com.hawkab.rest;


import java.io.Serializable;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class LoanFilterRq implements Serializable {

    private String status;
    private String personnelId;

    public LoanFilterRq() {
    }

    public LoanFilterRq(String status, String personnelId) {
        this.status = status;
        this.personnelId = personnelId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    @Override
    public String toString() {
        return "LoanFilterRq{" +
                "status='" + status + '\'' +
                ", personnelId='" + personnelId + '\'' +
                '}';
    }
}
