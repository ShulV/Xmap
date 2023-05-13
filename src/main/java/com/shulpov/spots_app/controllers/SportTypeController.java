package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.SportTypeDto;
import com.shulpov.spots_app.models.SportType;
import com.shulpov.spots_app.models.SpotType;
import com.shulpov.spots_app.services.SportTypeService;
import com.shulpov.spots_app.utils.DtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sport-types/")
public class SportTypeController {
    private final SportTypeService sportTypeService;
    private final static Logger logger = LoggerFactory.getLogger(SportTypeController.class);

    @Autowired
    public SportTypeController(SportTypeService sportTypeService) {
        this.sportTypeService = sportTypeService;
    }

    //Получить все типы спорта
    @GetMapping("/get-all")
    public List<SportTypeDto> getAllSportTypes() {
        logger.atInfo().log("/get-all");
        return sportTypeService.getAll().stream().map(DtoConverter::sportTypeToDto).toList();
    }
}
