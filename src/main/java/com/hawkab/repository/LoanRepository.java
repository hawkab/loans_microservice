package com.hawkab.repository;

import com.hawkab.entity.LoanEntity;
import com.hawkab.entity.enums.LoanStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, String> {

    @Query(value = "FROM LoanEntity l WHERE " +
            "LOWER(l.personnelId) = LOWER(:personnelId) AND " +
            "LOWER(l.uuid) = LOWER(:uuid) AND " +
            "l.productState IN :statuses")
    LoanEntity findByUuidAndPersonnelId(@Param("uuid") String uuid, @Param("personnelId") String personnelId,
                                        @Param("statuses") List<LoanStatusEnum> statuses);

    @Query(value = "SELECT COUNT(l) FROM LoanEntity l WHERE LOWER(l.country) = LOWER(:country) AND l.creationDate >= :date AND " +
            "l.productState NOT IN :statuses")
    Long countClaimsByCountryAndCreationDate(@Param("country") String country, @Param("date") LocalDateTime date,
                                             @Param("statuses") List<LoanStatusEnum> statuses);

    @Query(value = "SELECT SUM(l.amount) FROM LoanEntity l WHERE " +
            "LOWER(l.personnelId) = LOWER(:personnelId) AND " +
            "l.productState IN :statuses")
    BigDecimal sumAmountByPersonnelIdAndStatuses(@Param("personnelId") String personnelId,
                                                 @Param("statuses") List<LoanStatusEnum> statuses);

    @Query(value = "SELECT l FROM LoanEntity l WHERE " +
            "(:personnelId = '' or LOWER(l.personnelId) = LOWER(:personnelId)) AND " +
            "(:status = '' or LOWER(l.productState) = LOWER(:status))")
    Page<LoanEntity> findAll(@Param("personnelId") String personnelId, @Param("status") String status,
                             Pageable pageable);

}
