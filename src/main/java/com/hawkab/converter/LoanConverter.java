package com.hawkab.converter;

import com.hawkab.entity.LoanEntity;
import com.hawkab.rest.LoanClaimData;
import org.springframework.beans.BeanUtils;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class LoanConverter {
    public static LoanEntity convert(LoanClaimData claim) {
        LoanEntity loanEntity = new LoanEntity();
        BeanUtils.copyProperties(claim, loanEntity);
        return loanEntity;
    }

    public static LoanClaimData convert(LoanEntity entity) {
        LoanClaimData claim = new LoanClaimData();
        BeanUtils.copyProperties(entity, claim);
        claim.setProductState(entity.getProductState().name());
        return claim;
    }
}
