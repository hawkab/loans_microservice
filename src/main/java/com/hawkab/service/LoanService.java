package com.hawkab.service;

import com.hawkab.entity.LoanEntity;
import com.hawkab.entity.enums.LoanStatusEnum;
import com.hawkab.repository.LoanRepository;
import com.hawkab.rest.LoanFilterRq;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@Service
public class LoanService {
    private static final List<LoanStatusEnum> confirmedStatuses = Collections.singletonList(LoanStatusEnum.CONFIRMED);
    private static final List<LoanStatusEnum> rejectedStatuses = Collections.singletonList(LoanStatusEnum.REJECTED);

    private final LoanRepository loanRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public LoanEntity findOne(String uuid) {
        return loanRepository.findOne(uuid);
    }

    public Page<LoanEntity> findAll(Pageable pageable) {
        return loanRepository.findAll(pageable);
    }

    public Page<LoanEntity> findAllByCriteria(LoanFilterRq filter, Pageable pageable) {
        return loanRepository.findAll(StringUtils.isBlank(filter.getPersonnelId()) ?
                StringUtils.EMPTY : filter.getPersonnelId(),
                StringUtils.isBlank(filter.getStatus()) ? StringUtils.EMPTY : filter.getStatus(), pageable);
    }

    public LoanEntity getLoanByUuidAndPersonnelId(String uuid, String personnelId) {
        return loanRepository.findByUuidAndPersonnelId(uuid, personnelId, rejectedStatuses);
    }

    public LoanEntity repayLoan(String uuid, String personnelId) {
        LoanEntity loanEntity = getLoanByUuidAndPersonnelId(uuid, personnelId);
        if (Objects.nonNull(loanEntity)) {
            loanEntity.setProductState(LoanStatusEnum.PAYED);
            return loanRepository.save(loanEntity);
        }
        throw new EntityNotFoundException();
    }

    LoanEntity save(LoanEntity loanEntity) {
        return loanRepository.save(loanEntity);
    }

    BigDecimal sumAmountByPersonnelIdAndStatuses(String personnelId) {
        return Optional.ofNullable(loanRepository.sumAmountByPersonnelIdAndStatuses(personnelId, confirmedStatuses)).orElse(BigDecimal.ZERO);
    }

    Long countClaimsByCountryAndCreationDate(String country, LocalDateTime date) {
        return loanRepository.countClaimsByCountryAndCreationDate(country, date, rejectedStatuses);
    }
}
