package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.SpotTypeDto;
import com.shulpov.spots_app.models.SpotType;
import com.shulpov.spots_app.services.SpotTypeService;
import com.shulpov.spots_app.utils.DtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/spot-types/")
public class SpotTypeController {
    private final SpotTypeService spotTypeService;
    private final static Logger logger = LoggerFactory.getLogger(SpotTypeController.class);

    private final DtoConverter dtoConverter;

    @Autowired
    public SpotTypeController(SpotTypeService spotTypeService, DtoConverter dtoConverter) {
        this.spotTypeService = spotTypeService;
        this.dtoConverter = dtoConverter;
    }

    //Получить все типы спотов
    @GetMapping("/get-all")
    public List<SpotTypeDto> getAllSpots() {
        logger.atInfo().log("/get-all");
        return spotTypeService.getAll().stream().map(dtoConverter::spotTypeToDto).toList();
    }

    //Получить тип спота по id
    @GetMapping("/{id}")
    public SpotType getSpotType(@PathVariable(name = "id")  Integer id) throws NoSuchElementException {
        logger.atInfo().log("get-by-id: /{}", id);
        Optional<SpotType> spotTypeOpt = spotTypeService.getById(id);
        if(spotTypeOpt.isPresent()) {
            return spotTypeOpt.get();
        } else {
            throw new NoSuchElementException("No spot type with such id");
        }
    }
}
