package com.hawkab.converter;

import com.hawkab.entity.BlackListEntity;
import com.hawkab.rest.BlackListData;
import org.springframework.beans.BeanUtils;

/**
 * @author hawkab
 * @since 26.08.2019
 */

public class BlackListConverter {

    public static BlackListData convert(BlackListEntity entity) {
        BlackListData data = new BlackListData();
        BeanUtils.copyProperties(entity, data);
        return data;
    }

    public static BlackListEntity convert(BlackListData data) {
        BlackListEntity entity = new BlackListEntity();
        BeanUtils.copyProperties(data, entity);
        return entity;
    }
}
