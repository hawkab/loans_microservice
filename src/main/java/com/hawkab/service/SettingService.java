package com.hawkab.service;

import com.hawkab.entity.SettingEntity;
import com.hawkab.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

import static com.hawkab.utils.Constants.*;

/**
 * Сервис работы с сущностью настроек системы
 *
 * @author hawkab
 * @since 26.08.2019
 */

@Service
public class SettingService {

    private final SettingsRepository settingsRepository;

    @Autowired
    public SettingService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    /**
     * Применить настройку
     *
     * @param frontEntity объект настройки
     */
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

    /**
     * Получить сущность настройки по коду
     *
     * @param key код настройки (идентификатор)
     * @return объект настройки
     */
    public SettingEntity getSetting(String key) {
        return settingsRepository.findByKey(key);
    }

    /**
     * Получить максимальное количество заявок
     *
     * @return целое положительное число заявок
     */
    long getLimitCountClaims(){
        String value = getValueByKeyOrDefault(LIMIT_COUNT_CLAIMS, DEFAULT_LIMIT_COUNT_CLAIMS);
        return Long.parseLong(value);
    }

    /**
     * Получить настроечное количество минут для агрегации количества заявок
     *
     * @return целое положительное число минут
     */
    long getLimitMinutes(){
        String value = getValueByKeyOrDefault(LIMIT_MINUTES_CLAIMS, DEFAULT_LIMIT_MINUTES_CLAIMS);
        return Long.parseLong(value);
    }

    /**
     * Получить максимально допустимую сумму неоплаченных кредитов по гражданину
     *
     * @return дробное число
     */
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
