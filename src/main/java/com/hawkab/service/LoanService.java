package com.hawkab.service;

import com.hawkab.entity.Loan;
import com.hawkab.entity.enums.ProductStateEnum;
import com.hawkab.repository.BlackListRepository;
import com.hawkab.repository.LoanRepository;
import com.hawkab.rest.LoanFilterCriteria;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@Service
public class LoanService {
    private static final Logger LOGGER = Logger.getLogger(LoanService.class);

    private final LoanRepository loanRepository;
    private final BlackListRepository blackListRepository;
    private final SettingService settingService;

    @Autowired
    public LoanService(LoanRepository loanRepository, BlackListRepository blackListRepository, SettingService settingService) {
        this.loanRepository = loanRepository;
        this.blackListRepository = blackListRepository;
        this.settingService = settingService;
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

    public Loan addLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    public Loan getLoanByUuidAndPersonnelId(String uuid, String personnelId) {
        return loanRepository.findByUuidAndPersonnelId(uuid, personnelId);
    }

    public Loan repayLoan(String uuid, String personnelId) {
        Loan loan = getLoanByUuidAndPersonnelId(uuid, personnelId);
        if (Objects.nonNull(loan)) {
            loan.setProductState(ProductStateEnum.PAYED);
            return loanRepository.save(loan);
        }
        throw new EntityNotFoundException();
    }

    void save(Loan loan) {
        loanRepository.save(loan);
    }

    BigDecimal sumAmountByPersonnelIdAndStatuses(String personnelId, List<ProductStateEnum> asList) {
        return loanRepository.sumAmountByPersonnelIdAndStatuses(personnelId, asList);
    }

    Long countClaimsByCountryAndCreationDate(String country, LocalDateTime date) {
        return loanRepository.countClaimsByCountryAndCreationDate(country, date);
    }

}
