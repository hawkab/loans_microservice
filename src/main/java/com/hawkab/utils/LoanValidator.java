package com.hawkab.utils;

import com.hawkab.entity.Loan;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class LoanValidator {
    public static void validate(Loan loan) throws ValidationException {
        if (Objects.isNull(loan)) {
            throw new ValidationException("Получен пустой объект заявки");
        }
        if (AppUtils.isNullOrWhitespaceAnything(loan.getCountry(), loan.getPersonnelId())
                || AppUtils.isNullAnything(loan.getAmount(), loan.getDuration())) {
            throw new ValidationException("Не заполнено одно из обязательных полей: amount, duration, " +
                    "country или personnelId");
        }
        if (Objects.nonNull(loan.getAmount()) && loan.getAmount().compareTo(BigDecimal.ZERO) < 1) {
            throw new ValidationException("Сумма кредита должна быть больше нуля");
        }
    }
}
