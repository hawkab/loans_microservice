package com.hawkab.converter;

import com.hawkab.entity.Loan;
import com.hawkab.rest.LoanClaim;
import org.springframework.beans.BeanUtils;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class LoanConverter {
    public static Loan convert(LoanClaim claim) {
        Loan loan = new Loan();
        BeanUtils.copyProperties(claim, loan);
        return loan;
    }

    public static LoanClaim convert(Loan entity) {
        LoanClaim claim = new LoanClaim();
        BeanUtils.copyProperties(entity, claim);
        claim.setProductState(entity.getProductState().name());
        return claim;
    }
}
