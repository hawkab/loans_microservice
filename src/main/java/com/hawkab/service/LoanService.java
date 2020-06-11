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
 * Сервис работы с сущностью кредита
 *
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

    /**
     * Получить кредит по уникальному идентификатору
     *
     * @param uuid уникальный идентификатор
     * @return найденный кредит
     */
    public LoanEntity findOne(String uuid) {
        return loanRepository.findOne(uuid);
    }

    /**
     * Получить страницу с кредитами по заданным условиям номера и размера страницы, сортировки
     *
     * @param pageable Объект настроек постраничного вывода
     * @return страница элементов таблицы кредитов
     */
    public Page<LoanEntity> findAll(Pageable pageable) {
        return loanRepository.findAll(pageable);
    }

    /**
     * Получить страницу с удовлетворяющими фильтрации кредитами по заданным условиям номера и размера страницы, сортировки
     *
     * @param filter Объект фильтрации
     * @param pageable Объект настроек постраничного вывода
     * @return страница элементов таблицы кредитов
     */
    public Page<LoanEntity> findAllByCriteria(LoanFilterRq filter, Pageable pageable) {
        return loanRepository.findAll(StringUtils.isBlank(filter.getPersonnelId()) ?
                StringUtils.EMPTY : filter.getPersonnelId(),
                StringUtils.isBlank(filter.getStatus()) ? StringUtils.EMPTY : filter.getStatus(), pageable);
    }

    /**
     * Получить кредит по уникальному идентификатору кредита и идентификатору гражданина
     *
     * @param uuid уникальный идентификатор кредита
     * @param personnelId идентификатор гражданина
     * @return найденный объект кредита
     */
    public LoanEntity getLoanByUuidAndPersonnelId(String uuid, String personnelId) {
        return loanRepository.findByUuidAndPersonnelId(uuid, personnelId, rejectedStatuses);
    }

    /**
     * Осуществить полную оплату кредита о уникальному идентификатору кредита и идентификатору гражданина
     *
     * @param uuid уникальный идентификатор кредита
     * @param personnelId идентификатор гражданина
     * @return оплаченный объект кредита
     */
    public LoanEntity repayLoan(String uuid, String personnelId) {
        LoanEntity loanEntity = getLoanByUuidAndPersonnelId(uuid, personnelId);
        if (Objects.nonNull(loanEntity)) {
            loanEntity.setProductState(LoanStatusEnum.PAYED);
            return loanRepository.save(loanEntity);
        }
        throw new EntityNotFoundException();
    }

    /**
     * Осуществить сохранение объекта кредита
     *
     * @param loanEntity объект кредита
     * @return объект кредита
     */
    LoanEntity save(LoanEntity loanEntity) {
        return loanRepository.save(loanEntity);
    }

    /**
     * Получить сумму неоплаченных кредитов по идентификатору гражданина
     *
     * @param personnelId идентификатор гражданина
     * @return общая сумма неоплаченных кредитов данного гражданина
     */
    BigDecimal sumAmountByPersonnelIdAndStatuses(String personnelId) {
        return Optional.ofNullable(loanRepository.sumAmountByPersonnelIdAndStatuses(personnelId, confirmedStatuses)).orElse(BigDecimal.ZERO);
    }

    /**
     * Количество заявок в разрезе страны за указанный промежуток времени
     *
     * @param country страна
     * @param date дата и время, от которой необходимо считать промежуток
     * @return Целое положительное число заявок в разрезе страны за указанный промежуток времени
     */
    Long countClaimsByCountryAndCreationDate(String country, LocalDateTime date) {
        return loanRepository.countClaimsByCountryAndCreationDate(country, date, rejectedStatuses);
    }
}
