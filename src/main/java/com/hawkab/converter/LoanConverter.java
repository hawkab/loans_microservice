package com.hawkab.converter;

import com.hawkab.entity.LoanEntity;
import com.hawkab.rest.LoanClaimData;
import org.springframework.beans.BeanUtils;

/**
 * Конвертор объекта кредита
 *
 * @author hawkab
 * @since 26.08.2019
 */

public class LoanConverter {

    /**
     * Конвертировать сущность БД в формат презентации
     *
     * @param entity объект кредита БД
     * @return объект кредита на фронте
     */
    public static LoanClaimData convert(LoanEntity entity) {
        LoanClaimData claim = new LoanClaimData();
        BeanUtils.copyProperties(entity, claim);
        claim.setProductState(entity.getProductState().name());
        return claim;
    }

    /**
     * Конвертировать из презентации в формат сущности БД
     *
     * @param claim объект кредита на фронте
     * @return объект кредита БД
     */
    public static LoanEntity convert(LoanClaimData claim) {
        LoanEntity loanEntity = new LoanEntity();
        BeanUtils.copyProperties(claim, loanEntity);
        return loanEntity;
    }

}
