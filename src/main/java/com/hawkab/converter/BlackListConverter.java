package com.hawkab.converter;

import com.hawkab.entity.BlackListEntity;
import com.hawkab.rest.BlackListData;
import org.springframework.beans.BeanUtils;

/**
 * Конвертор элемента справочника "Чёрный список"
 *
 * @author hawkab
 * @since 26.08.2019
 */

public class BlackListConverter {
    /**
     * Конвертировать сущность БД в формат презентации
     *
     * @param entity элемент чёрного списка БД
     * @return элемент чёрного списка на фронте
     */
    public static BlackListData convert(BlackListEntity entity) {
        BlackListData data = new BlackListData();
        BeanUtils.copyProperties(entity, data);
        return data;
    }
    /**
     * Конвертировать из презентации в формат сущности БД
     *
     * @param data элемент чёрного списка на фронте
     * @return элемент чёрного списка настройки БД
     */
    public static BlackListEntity convert(BlackListData data) {
        BlackListEntity entity = new BlackListEntity();
        BeanUtils.copyProperties(data, entity);
        return entity;
    }
}
