package com.hawkab.controller;

import com.hawkab.converter.LoanConverter;
import com.hawkab.entity.Loan;
import com.hawkab.entity.enums.ProductStateEnum;
import com.hawkab.rest.LoanClaim;
import com.hawkab.rest.LoanClaimInfo;
import com.hawkab.rest.LoanClaimResult;
import com.hawkab.rest.LoanFilterCriteria;
import com.hawkab.service.LoanDecisionService;
import com.hawkab.service.LoanService;
import com.hawkab.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.Objects;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@RestController
@RequestMapping("/loans")
public class LoanController {

    private static final Logger LOGGER = Logger.getLogger(LoanController.class);
    private LoanService loanService;
    private LoanDecisionService loanDecisionService;

    @Inject
    public void setLoanService(LoanService loanService) {
        this.loanService = loanService;
    }

    @Inject
    public void setLoanDecisionService(LoanDecisionService loanDecisionService) {
        this.loanDecisionService = loanDecisionService;
    }

    @RequestMapping(consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<LoanClaimResult> addLoan(@RequestBody LoanClaim loanClaim) {
        if (AppUtils.isNotNullOrWhitespace(loanClaim.getCountry(), loanClaim.getPersonnelId())
                && AppUtils.nonNull(loanClaim.getAmount(), loanClaim.getDuration())) {
            Loan loan = loanService.addLoan(LoanConverter.convert(loanClaim));
            ProductStateEnum loanDecision = loanDecisionService.getLoanDecision(loan);
            return ResponseEntity.ok().body(new LoanClaimResult(loan.getUuid(), loanDecision.name()));
        }
        return ResponseEntity.badRequest().body(
                new LoanClaimResult(null, "Не заполнено одно из обязательных полей: amount, duration, " +
                "country или personnelId"));
    }

    @RequestMapping(produces = "text/plain;charset=UTF-8", value = "/repay", method = RequestMethod.POST)
    public ResponseEntity<String> repayLoan(@RequestBody LoanClaimInfo loanClaimInfo) {
        if (AppUtils.isNotNullOrWhitespace(loanClaimInfo.getLoanId(), loanClaimInfo.getPersonnelId())) {
            Loan loan;
            try {
                loan = loanService.repayLoan(loanClaimInfo.getLoanId(), loanClaimInfo.getPersonnelId());
            } catch (EntityNotFoundException ex) {
                LOGGER.warn(ex);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Кредит с такими данными не найден");
            }
            if (Objects.nonNull(loan) && ProductStateEnum.PAYED.equals(loan.getProductState())) {
                return ResponseEntity.ok("Кредит успешно погашен");
            } else {
                String errorMessage = String.format("Произошла ошибка при погашении кредита %s", loanClaimInfo.getLoanId());
                LOGGER.error(errorMessage);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
            }
        }
        return ResponseEntity.badRequest().body("Не заполнено одно из обязательных полей: loanId или personnelId");
    }


    @RequestMapping(produces = "text/plain;charset=UTF-8", value = "/readd", method = RequestMethod.POST)
    public ResponseEntity<String> reAddLoan(@RequestBody LoanClaimInfo loanClaimInfo) {
        if (AppUtils.isNotNullOrWhitespace(loanClaimInfo.getLoanId(), loanClaimInfo.getPersonnelId())) {
            return ResponseEntity.badRequest().body("Не заполнено одно из обязательных полей: loanId или personnelId");
        }
        Loan loan = loanService.getLoanByUuidAndPersonnelId(loanClaimInfo.getLoanId(), loanClaimInfo.getPersonnelId());
        ProductStateEnum loanDecision = loanDecisionService.getLoanDecision(loan);
        if (ProductStateEnum.REJECTED.equals(loanDecision)) {
            return ResponseEntity.ok().body(loanDecision.name());
        }
        return ResponseEntity.ok().body(loan.getUuid());
    }

    @RequestMapping(produces = "application/json;charset=UTF-8", value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getLoanByUuid(@PathVariable String uuid) {
        if (StringUtils.isBlank(uuid)) {
            return ResponseEntity.badRequest().body("Не заполнено обязательное поле: uuid");
        }
        Loan loan = loanService.findOne(uuid);
        if (Objects.isNull(loan)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(LoanConverter.convert(loan));
    }

    @RequestMapping(consumes = "application/json", produces = "application/json", value = "/filter", method = RequestMethod.POST)
    public ResponseEntity<Page<LoanClaim>> getLoanByCriteria(@PageableDefault(value = 50) Pageable pageable, @RequestBody LoanFilterCriteria filter) {
        if (Objects.isNull(filter) || AppUtils.isNullOrWhitespace(filter.getStatus(), filter.getPersonnelId())) {
            return ResponseEntity.ok().body(loanService.findAll(pageable).map(LoanConverter::convert));
        }
        return ResponseEntity.ok().body(loanService.findAllByCriteria(filter, pageable).map(LoanConverter::convert));
    }
}
