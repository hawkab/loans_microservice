package com.hawkab.converter;

import com.hawkab.entity.SettingEntity;
import com.hawkab.rest.SettingData;
import org.springframework.beans.BeanUtils;

/**
 * Конвертор объекта настройки системы
 *
 * @author hawkab
 * @since 26.08.2019
 */

public class SettingConverter {
    /**
     * Конвертировать сущность БД в формат презентации
     *
     * @param settingEntity объект настройки БД
     * @return объект настройки на фронте
     */
    public static SettingData convert(SettingEntity settingEntity) {
        SettingData settingData = new SettingData();
        BeanUtils.copyProperties(settingEntity, settingData);
        return settingData;
    }

    /**
     * Конвертировать из презентации в формат сущности БД
     *
     * @param settingData объект настройки на фронте
     * @return объект настройки БД
     */
    public static SettingEntity convert(SettingData settingData) {
        SettingEntity settingEntity = new SettingEntity();
        BeanUtils.copyProperties(settingData, settingEntity);
        return settingEntity;
    }
}
