package com.hawkab.service;

import com.hawkab.entity.BlackListEntity;
import com.hawkab.entity.SettingEntity;
import com.hawkab.repository.BlackListRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Objects;

/**
 * Сервис, осуществляющий работу со справочником "чёрный список"
 *
 * @author hawkab
 * @since 26.08.2019
 */

@Service
public class BlackListService {

    private final BlackListRepository blackListRepository;

    @Autowired
    public BlackListService(BlackListRepository blackListRepository) {
        this.blackListRepository = blackListRepository;
    }

    /**
     * Добавить новый элемент в справочник
     *
     * @param blackListEntity элемент справочника "чёрный список"
     */
    public void addPerson(BlackListEntity blackListEntity) {
        BlackListEntity existsEntity = getPerson(blackListEntity.getId());
        if (Objects.isNull(existsEntity)) {
            blackListRepository.save(blackListEntity);
        } else {
            BeanUtils.copyProperties(blackListEntity, existsEntity);
            blackListRepository.save(existsEntity);
        }
    }

    /**
     * Получить элемент по идентификатору гражданина из справочника
     *
     * @param personnelId идентификатор гражданина
     * @return элемент справочника "чёрный список"
     */
    public BlackListEntity getPerson(String personnelId) {
        return blackListRepository.findByPersonnelId(personnelId);
    }

    /**
     * Получить элемент по идентификатору из справочника
     *
     * @param id идентификатор элемента из справочника "чёрный список"
     * @return элемент справочника "чёрный список"
     */
    private BlackListEntity getPerson(Long id) {
        return blackListRepository.findOne(id);
    }

    /**
     * Получить страницу справочника "Чёрный список" по заданным условиям номера и размера страницы, сортировки
     *
     * @param pageable Объект настроек постраничного вывода
     * @return страница элементов справочника "чёрный список"
     */
    public Page<BlackListEntity> getPersons(Pageable pageable) {
        return blackListRepository.findAll(pageable);
    }
}
