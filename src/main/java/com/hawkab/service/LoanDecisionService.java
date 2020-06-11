package com.hawkab.service;

import com.hawkab.entity.LoanEntity;
import com.hawkab.entity.enums.LoanStatusEnum;
import com.hawkab.repository.BlackListRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.hawkab.utils.Constants.*;

/**
 * Сервис обработки кредитных заявок и принятия автоматических решений по заявкам на кредит
 *
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

    /**
     * Обогатить переданный объект кредита автоматическим решением по кредиту
     *
     * @param loanEntity Объект кредит
     * @return обогащённый автоматическим решением по кредиту переданный объект кредита
     */
    public LoanEntity getLoanDecision(LoanEntity loanEntity) {
        try {
            blackListCheck(loanEntity);
            limitClaimByCountryPerMinuteCheck(loanEntity);
            limitAmountClaimByUserCheck(loanEntity);
        } catch (ValidationException ex) {
            LOGGER.warn(ex);
            LoanStatusEnum decision = LoanStatusEnum.REJECTED;
            loanEntity.setProductState(decision);
            loanEntity.setDecisionDescription(ex.getMessage());
            return loanService.save(loanEntity);
        }

        LoanStatusEnum decision = LoanStatusEnum.CONFIRMED;
        loanEntity.setProductState(decision);
        loanEntity.setDecisionDescription(CLAIM_APPROVED);
        return loanService.save(loanEntity);
    }

    private void limitAmountClaimByUserCheck(LoanEntity loanEntity) throws ValidationException {
        BigDecimal limitAmount = settingService.getLimitAmount();
        BigDecimal debt = loanService.sumAmountByPersonnelIdAndStatuses
                (StringUtils.isBlank(loanEntity.getPersonnelId()) ? StringUtils.EMPTY : loanEntity.getPersonnelId());
        debt = debt.add(loanEntity.getAmount());
        if (debt.compareTo(limitAmount) > 0) {
            throw new ValidationException(String.format(AMOUNT_LIMIT_MESSAGE,
                    limitAmount));
        }
    }

    private void limitClaimByCountryPerMinuteCheck(LoanEntity loanEntity) throws ValidationException {
        long limitCountClaims = settingService.getLimitCountClaims();
        long limitMinutes = settingService.getLimitMinutes();
        Long factCountClaims = loanService.countClaimsByCountryAndCreationDate(
                StringUtils.isBlank(loanEntity.getCountry()) ? StringUtils.EMPTY : loanEntity.getCountry(),
                LocalDateTime.now().minusSeconds(getSeconds(limitMinutes)));
        if (factCountClaims >= limitCountClaims) {
            throw new ValidationException(String.format(CLAIM_COUNT_LIMIT_MESSAGE,
                    limitCountClaims, limitMinutes, loanEntity.getCountry()));
        }
    }

    private long getSeconds(long limitMinutes) {
        return limitMinutes * COUNT_SECONDS_IN_MINUTE;
    }

    private void blackListCheck(LoanEntity loanEntity) throws ValidationException {
        Boolean isExistsInBlackList = blackListRepository.existsByPersonnelId(loanEntity.getPersonnelId());
        if (Boolean.TRUE.equals(isExistsInBlackList)) {
            throw new ValidationException(BLACK_LIST_CLAIM_MESSAGE);
        }
    }
}
