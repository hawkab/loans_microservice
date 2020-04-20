package com.hawkab.controller;

import com.hawkab.converter.SettingConverter;
import com.hawkab.entity.SettingEntity;
import com.hawkab.rest.SettingData;
import com.hawkab.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author hawkab
 * @since 26.08.2019
 */

@RestController
@RequestMapping("/settings")
public class SettingController {

    private SettingService settingService;

    @Autowired
    public void setSettingService(SettingService settingService) {
        this.settingService = settingService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> applySetting(@RequestBody SettingData settingData) {
        settingService.setSetting(SettingConverter.convert(settingData));
        return ResponseEntity.ok().build();
    }

    @RequestMapping(params = {"key"}, method = RequestMethod.GET)
    public ResponseEntity<?> getSettingByKey(@RequestParam(value = "key") String key) {
        SettingEntity settingEntity = settingService.getSetting(key);
        if (Objects.isNull(settingEntity)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(SettingConverter.convert(settingEntity));
    }
}
