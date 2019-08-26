package com.hawkab.service;

import com.hawkab.entity.SettingEntity;
import com.hawkab.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@Service
public class SettingService {

    private static final String LIMIT_COUNT_CLAIMS = "limit_count_claims";
    private static final String DEFAULT_LIMIT_COUNT_CLAIMS = "5";

    private static final String LIMIT_MINUTES_CLAIMS = "limit_minutes_claims";
    private static final String DEFAULT_LIMIT_MINUTES_CLAIMS = "1";

    private static final String LIMIT_AMOUNT_CLAIMS = "limit_amount_claims";
    private static final String DEFAULT_LIMIT_AMOUNT_CLAIMS = "1000.00";

    private final SettingsRepository settingsRepository;

    @Autowired
    public SettingService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public void setSetting(SettingEntity frontEntity) {
        SettingEntity existsSetting = getSetting(frontEntity.getKey());
        if (Objects.isNull(existsSetting)) {
            settingsRepository.save(new SettingEntity(frontEntity.getKey(), frontEntity.getValue(),
                    frontEntity.getDescription()));
        } else {
            existsSetting.setValue(frontEntity.getValue());
            existsSetting.setDescription(frontEntity.getDescription());
            settingsRepository.save(existsSetting);
        }
    }

    public SettingEntity getSetting(String key) {
        return settingsRepository.findByKey(key);
    }

    long getLimitCountClaims(){
        String value = getValueByKeyOrDefault(LIMIT_COUNT_CLAIMS, DEFAULT_LIMIT_COUNT_CLAIMS);
        return Long.parseLong(value);
    }

    long getLimitMinutes(){
        String value = getValueByKeyOrDefault(LIMIT_MINUTES_CLAIMS, DEFAULT_LIMIT_MINUTES_CLAIMS);
        return Long.parseLong(value);
    }

    BigDecimal getLimitAmount(){
        String value = getValueByKeyOrDefault(LIMIT_AMOUNT_CLAIMS, DEFAULT_LIMIT_AMOUNT_CLAIMS);
        return BigDecimal.valueOf(Double.parseDouble(value));
    }

    private String getValueByKeyOrDefault(String key, String defaultValue){
        SettingEntity settingEntity = settingsRepository.findByKey(key);
        if (Objects.isNull(settingEntity) || StringUtils.isEmpty(settingEntity.getValue())) {
            return defaultValue;
        }
        return settingEntity.getValue();
    }
}
