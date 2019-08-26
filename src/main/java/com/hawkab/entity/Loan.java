package com.hawkab.entity;

import com.hawkab.entity.enums.ProductStateEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "uuid", nullable = false, updatable = false)
    private String uuid;

    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "product_state")
    private ProductStateEnum productState;

    @Column(name = "decision_description")
    private String decisionDescription;

    private BigDecimal amount;
    private Integer duration;
    private String firstName;
    private String lastName;
    private String country;
    private String personnelId;

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

    public ProductStateEnum getProductState() {
        return productState;
    }

    public void setProductState(ProductStateEnum productState) {
        this.productState = productState;
    }

    public String getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    public String getUuid() {
        return uuid;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getDecisionDescription() {
        return decisionDescription;
    }

    public void setDecisionDescription(String decisionDescription) {
        this.decisionDescription = decisionDescription;
    }
}
