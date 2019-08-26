package com.hawkab.converter;

import com.hawkab.entity.SettingEntity;
import com.hawkab.rest.SettingData;
import org.springframework.beans.BeanUtils;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class SettingConverter {
    public static SettingData convert(SettingEntity settingEntity) {
        SettingData settingData = new SettingData();
        BeanUtils.copyProperties(settingEntity, settingData);
        return settingData;
    }

    public static SettingEntity convert(SettingData settingData) {
        SettingEntity settingEntity = new SettingEntity();
        BeanUtils.copyProperties(settingData, settingEntity);
        return settingEntity;
    }
}
