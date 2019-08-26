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
import com.hawkab.utils.LoanValidator;
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
import javax.xml.bind.ValidationException;
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
        Loan converted = LoanConverter.convert(loanClaim);
        try {
            LoanValidator.validate(converted);
        } catch (ValidationException ex) {
            LOGGER.warn(ex);
            return ResponseEntity.badRequest().body(new LoanClaimResult(null, ex.getMessage()));
        }

        Loan loan = loanDecisionService.getLoanDecision(converted);
            return ResponseEntity.ok().body(new LoanClaimResult(loan.getUuid(),
                    String.format("%s - %s", loan.getProductState().name(), loan.getDecisionDescription())));
    }

    @RequestMapping(produces = "text/plain;charset=UTF-8", value = "/repay", method = RequestMethod.POST)
    public ResponseEntity<String> repayLoan(@RequestBody LoanClaimInfo loanClaimInfo) {
        if (AppUtils.isNotNullOrWhitespace(loanClaimInfo.getLoanId(), loanClaimInfo.getPersonnelId())) {
            Loan loan;
            try {
                loan = loanService.repayLoan(loanClaimInfo.getLoanId(), loanClaimInfo.getPersonnelId());
            } catch (EntityNotFoundException ex) {
                LOGGER.warn(ex);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Подтверждённый кредит с такими данными не найден");
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

    @RequestMapping(produces = "application/json", consumes = "application/json", value = "/readd", method = RequestMethod.POST)
    public ResponseEntity<LoanClaimResult> reAddLoan(@RequestBody LoanClaimInfo loanClaimInfo) {
        if (AppUtils.isNotNullOrWhitespace(loanClaimInfo.getLoanId(), loanClaimInfo.getPersonnelId())) {
            Loan loan = loanService.getLoanByUuidAndPersonnelId(loanClaimInfo.getLoanId(), loanClaimInfo.getPersonnelId());
            if (Objects.isNull(loan)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoanClaimResult(null,
                        "По заданным параметрам отказанный кредит не найден"));
            }
            try {
                LoanValidator.validate(loan);
            } catch (ValidationException ex) {
                LOGGER.warn(ex);
                return ResponseEntity.badRequest().body(new LoanClaimResult(null, ex.getMessage()));
            }
            loanDecisionService.getLoanDecision(loan);
            return ResponseEntity.ok().body(new LoanClaimResult(loan.getUuid(),
                    String.format("%s - %s", loan.getProductState().name(), loan.getDecisionDescription())));
        }
        return ResponseEntity.badRequest().body(new LoanClaimResult(null,
                "Не заполнено одно из обязательных полей: loanId или personnelId"));
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

    @RequestMapping(produces = "application/json", value = "/filter", method = RequestMethod.POST)
    public ResponseEntity<Page<LoanClaim>> getLoanByCriteria(@PageableDefault(value = 50) Pageable pageable,
                                                             @RequestBody(required = false) LoanFilterCriteria filter) {
        if (Objects.isNull(filter) || AppUtils.isNullOrWhitespace(filter.getStatus(), filter.getPersonnelId())) {
            return ResponseEntity.ok().body(loanService.findAll(pageable).map(LoanConverter::convert));
        }
        return ResponseEntity.ok().body(loanService.findAllByCriteria(filter, pageable).map(LoanConverter::convert));
    }
}
