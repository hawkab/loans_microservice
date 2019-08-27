package com.hawkab.controller;

import com.hawkab.converter.LoanConverter;
import com.hawkab.entity.LoanEntity;
import com.hawkab.entity.enums.LoanStatusEnum;
import com.hawkab.rest.LoanClaimData;
import com.hawkab.rest.LoanClaimRq;
import com.hawkab.rest.LoanClaimShortResult;
import com.hawkab.rest.LoanFilterRq;
import com.hawkab.service.LoanDecisionService;
import com.hawkab.service.LoanService;
import com.hawkab.utils.AppUtils;
import com.hawkab.utils.LoanValidator;
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
    public ResponseEntity<LoanClaimShortResult> addLoan(@RequestBody LoanClaimData loanClaimData) {
        LoanEntity converted = LoanConverter.convert(loanClaimData);
        try {
            LoanValidator.validate(converted);
        } catch (ValidationException ex) {
            LOGGER.warn(ex);
            return ResponseEntity.badRequest().body(new LoanClaimShortResult(null, ex.getMessage()));
        }

        LoanEntity loanEntity = loanDecisionService.getLoanDecision(converted);
        return ResponseEntity.ok().body(new LoanClaimShortResult(loanEntity.getUuid(),
                String.format("%s - %s", loanEntity.getProductState().name(), loanEntity.getDecisionDescription())));
    }

    @RequestMapping(produces = "text/plain;charset=UTF-8", value = "/repay", method = RequestMethod.POST)
    public ResponseEntity<String> repayLoan(@RequestBody LoanClaimRq loanClaimRq) {
        if (AppUtils.isNotNullOrWhitespace(loanClaimRq.getLoanId(), loanClaimRq.getPersonnelId())) {
            LoanEntity loanEntity;
            try {
                loanEntity = loanService.repayLoan(loanClaimRq.getLoanId(), loanClaimRq.getPersonnelId());
            } catch (EntityNotFoundException ex) {
                LOGGER.warn(ex);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Подтверждённый кредит с такими данными не найден");
            }
            if (Objects.nonNull(loanEntity) && LoanStatusEnum.PAYED.equals(loanEntity.getProductState())) {
                return ResponseEntity.ok("Кредит успешно погашен");
            } else {
                String errorMessage = String.format("Произошла ошибка при погашении кредита %s", loanClaimRq.getLoanId());
                LOGGER.error(errorMessage);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
            }
        }
        return ResponseEntity.badRequest().body("Не заполнено одно из обязательных полей: loanId или personnelId");
    }

    @RequestMapping(produces = "application/json", consumes = "application/json", value = "/readd", method = RequestMethod.POST)
    public ResponseEntity<LoanClaimShortResult> reAddLoan(@RequestBody LoanClaimRq loanClaimRq) {
        if (AppUtils.isNotNullOrWhitespace(loanClaimRq.getLoanId(), loanClaimRq.getPersonnelId())) {
            LoanEntity loanEntity = loanService.getLoanByUuidAndPersonnelId(loanClaimRq.getLoanId(), loanClaimRq.getPersonnelId());
            if (Objects.isNull(loanEntity)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoanClaimShortResult(null,
                        "По заданным параметрам отказанный кредит не найден"));
            }
            try {
                LoanValidator.validate(loanEntity);
            } catch (ValidationException ex) {
                LOGGER.warn(ex);
                return ResponseEntity.badRequest().body(new LoanClaimShortResult(null, ex.getMessage()));
            }
            loanDecisionService.getLoanDecision(loanEntity);
            return ResponseEntity.ok().body(new LoanClaimShortResult(loanEntity.getUuid(),
                    String.format("%s - %s", loanEntity.getProductState().name(), loanEntity.getDecisionDescription())));
        }
        return ResponseEntity.badRequest().body(new LoanClaimShortResult(null,
                "Не заполнено одно из обязательных полей: loanId или personnelId"));
    }

    @RequestMapping(produces = "application/json;charset=UTF-8", value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getLoanByUuid(@PathVariable String uuid) {
        if (StringUtils.isBlank(uuid)) {
            return ResponseEntity.badRequest().body("Не заполнено обязательное поле: uuid");
        }
        LoanEntity loanEntity = loanService.findOne(uuid);
        if (Objects.isNull(loanEntity)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(LoanConverter.convert(loanEntity));
    }

    @RequestMapping(produces = "application/json", value = "/filter", method = RequestMethod.POST)
    public ResponseEntity<Page<LoanClaimData>> getLoanByCriteria(@PageableDefault(value = 50) Pageable pageable,
                                                                 @RequestBody(required = false) LoanFilterRq filter) {
        if (Objects.isNull(filter) || AppUtils.isNullOrWhitespace(filter.getStatus(), filter.getPersonnelId())) {
            return ResponseEntity.ok().body(loanService.findAll(pageable).map(LoanConverter::convert));
        }
        return ResponseEntity.ok().body(loanService.findAllByCriteria(filter, pageable).map(LoanConverter::convert));
    }
}
