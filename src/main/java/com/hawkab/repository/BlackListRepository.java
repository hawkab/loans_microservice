package com.hawkab.repository;

import com.hawkab.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    Boolean existsByPersonnelId(String personnelId);
}
