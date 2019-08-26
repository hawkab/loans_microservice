package com.hawkab.controller;

import com.hawkab.converter.BlackListConverter;
import com.hawkab.entity.BlackListEntity;
import com.hawkab.rest.BlackListData;
import com.hawkab.service.BlackListService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Objects;

/**
 * @author hawkab
 * @since 26.08.2019
 */
@RestController
@RequestMapping("/black-list")
public class BlackListController {

    private BlackListService blackListService;

    @Inject
    public void setBlackListService(BlackListService blackListService) {
        this.blackListService = blackListService;
    }

    @RequestMapping(consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<?> addPerson(@RequestBody BlackListData blackListData) {
        blackListService.addPerson(BlackListConverter.convert(blackListData));
        return ResponseEntity.ok().body("Успешно добавлена запись в чёрный список");
    }

    @RequestMapping(params = {"personnelId"}, method = RequestMethod.GET)
    public ResponseEntity<?> getPersonByKey(@RequestParam(value = "personnelId") String personnelId) {
        BlackListEntity blackListEntity = blackListService.getPerson(personnelId);
        if (Objects.isNull(blackListEntity)) {
            return ResponseEntity.status(HttpStatus.OK).body("В чёрном списке запись с таким personnelId не найдена");
        }
        return ResponseEntity.ok().body(BlackListConverter.convert(blackListEntity));
    }

    @RequestMapping(consumes = "application/json", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<?> getPersons(@PageableDefault(value = 50) Pageable pageable) {
        return ResponseEntity.ok().body(blackListService.getPersons(pageable).map(BlackListConverter::convert));
    }
}