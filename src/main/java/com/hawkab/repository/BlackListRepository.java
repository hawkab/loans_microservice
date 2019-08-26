package com.hawkab.repository;

import com.hawkab.entity.BlackListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@Repository
public interface BlackListRepository extends JpaRepository<BlackListEntity, Long> {

    BlackListEntity findByPersonnelId(String personnelId);
    Boolean existsByPersonnelId(String personnelId);
}
