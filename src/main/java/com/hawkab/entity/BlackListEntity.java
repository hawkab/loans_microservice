package com.hawkab.entity;


import javax.persistence.*;

/**
 * Справочник "чёрный список"
 * @author hawkab
 * @since 26.08.2019
 */

@Entity
@Table(name = "black_list")
public class BlackListEntity {

    /**
     * Идентификатор записи справочника
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя кредитора
     */
    private String firstName;

    /**
     * Фамилия кредитора
     */
    private String lastName;

    /**
     * Страна, в которой осуществляется продажа кредита
     */
    private String country;

    /**
     * Идентификатор гражданина
     */
    private String personnelId;

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

}
