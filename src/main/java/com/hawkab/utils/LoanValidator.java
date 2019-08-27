package com.hawkab.utils;

import com.hawkab.entity.LoanEntity;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class LoanValidator {
    public static void validate(LoanEntity loanEntity) throws ValidationException {
        if (Objects.isNull(loanEntity)) {
            throw new ValidationException("Получен пустой объект заявки");
        }
        if (AppUtils.isNullOrWhitespaceAnything(loanEntity.getCountry(), loanEntity.getPersonnelId())
                || AppUtils.isNullAnything(loanEntity.getAmount(), loanEntity.getDuration())) {
            throw new ValidationException("Не заполнено одно из обязательных полей: amount, duration, " +
                    "country или personnelId");
        }
        if (Objects.nonNull(loanEntity.getAmount()) && loanEntity.getAmount().compareTo(BigDecimal.ZERO) < 1) {
            throw new ValidationException("Сумма кредита должна быть больше нуля");
        }
    }
}
