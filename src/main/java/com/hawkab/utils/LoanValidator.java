package com.hawkab.utils;

import com.hawkab.entity.LoanEntity;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.Objects;

import static com.hawkab.utils.Constants.*;

/**
 * Валидатор кредитной заявки
 *
 * @author hawkab
 * @since 26.08.2019
 */
public class LoanValidator {

    /**
     * Проверить объект заявки на предмет несоответствия требованиям, предъявляемым обработкой
     *
     * @param loanEntity объект заявки на кредит
     * @throws ValidationException ошибка, возбуждаемая в случае, если объект не прошёл проверку
     */
    public static void validate(LoanEntity loanEntity) throws ValidationException {
        if (Objects.isNull(loanEntity)) {
            throw new ValidationException(EMPTY_CLAIM_MESSAGE);
        }
        if (AppUtils.isNullOrWhitespaceAnything(loanEntity.getCountry(), loanEntity.getPersonnelId())
                || AppUtils.isNullAnything(loanEntity.getAmount(), loanEntity.getDuration())) {
            throw new ValidationException(REQUIRED_FIELDS_CLAIM_MESSAGE);
        }
        if (Objects.nonNull(loanEntity.getAmount()) && loanEntity.getAmount().compareTo(BigDecimal.ZERO) < 1) {
            throw new ValidationException(LOAN_AMOUNT_VALIDATION_MESSAGE);
        }
    }
}
