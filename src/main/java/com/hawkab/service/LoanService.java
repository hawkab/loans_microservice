package com.hawkab.service;

import com.hawkab.entity.Loan;
import com.hawkab.entity.enums.ProductStateEnum;
import com.hawkab.repository.LoanRepository;
import com.hawkab.rest.LoanFilterCriteria;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@Service
public class LoanService {
    private static final List<ProductStateEnum> confirmedStatuses = Collections.singletonList(ProductStateEnum.CONFIRMED);
    private static final List<ProductStateEnum> rejectedStatuses = Collections.singletonList(ProductStateEnum.REJECTED);

    private final LoanRepository loanRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan findOne(String uuid) {
        return loanRepository.findOne(uuid);
    }

    public Page<Loan> findAll(Pageable pageable){
        return loanRepository.findAll(pageable);
    }

    public Page<Loan> findAllByCriteria(LoanFilterCriteria filter, Pageable pageable){
        return loanRepository.findAll(StringUtils.isBlank(filter.getPersonnelId()) ?
                StringUtils.EMPTY : filter.getPersonnelId(),
                StringUtils.isBlank(filter.getStatus()) ? StringUtils.EMPTY : filter.getStatus(), pageable);
    }

    public Loan getLoanByUuidAndPersonnelId(String uuid, String personnelId) {
        return loanRepository.findByUuidAndPersonnelId(uuid, personnelId, rejectedStatuses);
    }

    public Loan repayLoan(String uuid, String personnelId) {
        Loan loan = getLoanByUuidAndPersonnelId(uuid, personnelId);
        if (Objects.nonNull(loan)) {
            loan.setProductState(ProductStateEnum.PAYED);
            return loanRepository.save(loan);
        }
        throw new EntityNotFoundException();
    }

    Loan save(Loan loan) {
        return loanRepository.save(loan);
    }

    BigDecimal sumAmountByPersonnelIdAndStatuses(String personnelId) {
        return Optional.ofNullable(loanRepository.sumAmountByPersonnelIdAndStatuses(personnelId, confirmedStatuses)).orElse(BigDecimal.ZERO);
    }

    Long countClaimsByCountryAndCreationDate(String country, LocalDateTime date) {
        return loanRepository.countClaimsByCountryAndCreationDate(country, date, rejectedStatuses);
    }
}
