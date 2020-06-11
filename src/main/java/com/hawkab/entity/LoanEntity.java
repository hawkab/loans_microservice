package com.hawkab.entity;

import com.hawkab.entity.enums.LoanStatusEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Кредит
 *
 * @author hawkab
 * @since 26.08.2019
 */

@Entity
@Table(name = "loans")
public class LoanEntity {

    /**
     * Уникальный автогенерируемый идентификатор кредита
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "uuid", nullable = false, updatable = false)
    private String uuid;

    /**
     * Дата создания записи
     */
    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    /**
     * Статус кредита
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_state")
    private LoanStatusEnum productState;

    /**
     * Расшифровка решения по кредиту
     */
    @Column(name = "decision_description")
    private String decisionDescription;

    /**
     * Неоплаченная сумма по кредиту
     */
    private BigDecimal amount;

    /**
     * Срок кредита
     */
    private Integer duration;

    /**
     * Имя кредитора
     */
    private String firstName;

    /**
     * Фамилия кредитора
     */
    private String lastName;

    /**
     * Страна в которой осуществляется продажа кредита
     */
    private String country;

    /**
     * Идентификатор гражданина
     */
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

    public LoanStatusEnum getProductState() {
        return productState;
    }

    public void setProductState(LoanStatusEnum productState) {
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
