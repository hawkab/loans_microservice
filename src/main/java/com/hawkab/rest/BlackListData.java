package com.hawkab.rest;


import java.io.Serializable;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class BlackListData implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String country;
    private String personnelId;

    public BlackListData() {
    }

    public BlackListData(Long id, String firstName, String lastName, String country, String personnelId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.personnelId = personnelId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "BlackListData{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", personnelId='" + personnelId + '\'' +
                '}';
    }
}
