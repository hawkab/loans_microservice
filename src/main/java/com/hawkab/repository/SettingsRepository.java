package com.hawkab.repository;

import com.hawkab.entity.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@Repository
public interface SettingsRepository extends JpaRepository<SettingEntity, String> {
    SettingEntity findByKey(String key);
}
