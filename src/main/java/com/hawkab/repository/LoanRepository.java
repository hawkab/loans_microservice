package com.hawkab.repository;

import com.hawkab.entity.Loan;
import com.hawkab.entity.enums.ProductStateEnum;
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
public interface LoanRepository extends JpaRepository<Loan, String> {

    Loan findByUuidAndPersonnelId(String uuid, String personnelId);

    @Query(value = "SELECT COUNT(l) FROM Loan l WHERE LOWER(l.country) = LOWER(:country) AND l.creationDate >= :date")
    Long countClaimsByCountryAndCreationDate(@Param("country") String country, @Param("date") LocalDateTime date);


    @Query(value = "SELECT SUM(l.amount) FROM Loan l WHERE " +
            "LOWER(l.personnelId) = LOWER(:personnelId) AND " +
            "l.productState in :statuses")
    BigDecimal sumAmountByPersonnelIdAndStatuses(@Param("personnelId") String personnelId, @Param("statuses") List<ProductStateEnum> statuses);

    @Query(value = "SELECT l FROM Loan l WHERE " +
            "(:personnelId = '' or LOWER(l.personnelId) = LOWER(:personnelId)) AND " +
            "(:status = '' or LOWER(l.productState) = LOWER(:status))")
    Page<Loan> findAll(@Param("personnelId") String personnelId, @Param("status") String status,
                       Pageable pageable);

}
