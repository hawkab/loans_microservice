package com.hawkab.service;

import com.hawkab.entity.Loan;
import com.hawkab.entity.enums.ProductStateEnum;
import com.hawkab.repository.BlackListRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@Service
public class LoanDecisionService {
    private static final Logger LOGGER = Logger.getLogger(LoanDecisionService.class);

    private final LoanService loanService;
    private final BlackListRepository blackListRepository;
    private final SettingService settingService;

    @Autowired
    public LoanDecisionService(LoanService loanService, BlackListRepository blackListRepository, SettingService settingService) {
        this.loanService = loanService;
        this.blackListRepository = blackListRepository;
        this.settingService = settingService;
    }

    public ProductStateEnum getLoanDecision(Loan loan) {
        try {
            blackListCheck(loan);
            limitClaimByCountryPerMinuteCheck(loan);
            limitAmountClaimByUserCheck(loan);
        } catch (ValidationException ex) {
            LOGGER.warn(ex);
            ProductStateEnum decision = ProductStateEnum.REJECTED;
            loan.setProductState(decision);
            loanService.save(loan);
            return decision;
        }

        ProductStateEnum decision = ProductStateEnum.CONFIRMED;
        loan.setProductState(decision);
        loanService.save(loan);
        return decision;
    }

    private void limitAmountClaimByUserCheck(Loan loan) throws ValidationException {
        BigDecimal limitAmount = settingService.getLimitAmount();
        BigDecimal debt = loanService.sumAmountByPersonnelIdAndStatuses
                (StringUtils.isBlank(loan.getPersonnelId()) ? StringUtils.EMPTY : loan.getPersonnelId(),
                Arrays.asList(ProductStateEnum.AWAITING_PAYMENT, ProductStateEnum.CONFIRMED));
        if (Objects.nonNull(debt) && debt.compareTo(limitAmount) > 0) {
            throw new ValidationException(String.format("Превышен лимит по сумме непогашенных кредитов %s у " +
                            "пользователя (по личному идентификатору)",
                    limitAmount));
        }
    }

    private void limitClaimByCountryPerMinuteCheck(Loan loan) throws ValidationException {
        long limitCountClaims = settingService.getLimitCountClaims();
        long limitMinutes = settingService.getLimitMinutes();
        Long factCountClaims = loanService.countClaimsByCountryAndCreationDate(
                StringUtils.isBlank(loan.getCountry()) ? StringUtils.EMPTY : loan.getCountry(),
                LocalDateTime.now().minusMinutes(limitMinutes));
        if (factCountClaims > limitCountClaims) {
            throw new ValidationException(String.format("Превышен лимит '%s' заявок в '%s' минут от страны '%s'.",
                    limitCountClaims, limitMinutes, loan.getCountry()));
        }
    }

    private void blackListCheck(Loan loan) throws ValidationException {
        Boolean isExistsInBlackList = blackListRepository.existsByPersonnelId(loan.getPersonnelId());
        if (Boolean.TRUE.equals(isExistsInBlackList)) {
            throw new ValidationException("Заявка поступает от пользователя в чёрном списке");
        }
    }
}
