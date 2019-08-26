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

    public void addPerson(BlackListEntity blackListEntity) {
        BlackListEntity existsEntity = getPerson(blackListEntity.getId());
        if (Objects.isNull(existsEntity)) {
            blackListRepository.save(blackListEntity);
        } else {
            BeanUtils.copyProperties(blackListEntity, existsEntity);
            blackListRepository.save(existsEntity);
        }
    }

    public BlackListEntity getPerson(String personnelId) {
        return blackListRepository.findByPersonnelId(personnelId);
    }

    private BlackListEntity getPerson(Long id) {
        return blackListRepository.findOne(id);
    }

    public Page<BlackListEntity> getPersons(Pageable pageable) {
        return blackListRepository.findAll(pageable);
    }
}
