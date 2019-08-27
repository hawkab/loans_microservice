package com.hawkab.service;

import com.hawkab.entity.LoanEntity;
import com.hawkab.entity.enums.LoanStatusEnum;
import com.hawkab.repository.BlackListRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
        loanEntity.setDecisionDescription("Заявка успешно прошла проверку автоматическим средством контроля и подтерждена");
        return loanService.save(loanEntity);
    }

    private void limitAmountClaimByUserCheck(LoanEntity loanEntity) throws ValidationException {
        BigDecimal limitAmount = settingService.getLimitAmount();
        BigDecimal debt = loanService.sumAmountByPersonnelIdAndStatuses
                (StringUtils.isBlank(loanEntity.getPersonnelId()) ? StringUtils.EMPTY : loanEntity.getPersonnelId());
        debt = debt.add(loanEntity.getAmount());
        if (debt.compareTo(limitAmount) > 0) {
            throw new ValidationException(String.format("Превышен лимит по сумме непогашенных кредитов '%s' у " +
                            "пользователя (по личному идентификатору)",
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
            throw new ValidationException(String.format("Превышен лимит '%s' заявок в '%s' минут от страны '%s'.",
                    limitCountClaims, limitMinutes, loanEntity.getCountry()));
        }
    }

    private long getSeconds(long limitMinutes) {
        return limitMinutes * 60;
    }

    private void blackListCheck(LoanEntity loanEntity) throws ValidationException {
        Boolean isExistsInBlackList = blackListRepository.existsByPersonnelId(loanEntity.getPersonnelId());
        if (Boolean.TRUE.equals(isExistsInBlackList)) {
            throw new ValidationException("Заявка поступает от пользователя в чёрном списке");
        }
    }
}
