package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.SportTypeDto;
import com.shulpov.spots_app.models.SportType;
import com.shulpov.spots_app.services.SportTypeService;
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
@RequestMapping("/api/sport-types")
public class SportTypeController {
    private final SportTypeService sportTypeService;

    private final DtoConverter dtoConverter;
    private final Logger logger = LoggerFactory.getLogger(SportTypeController.class);

    @Autowired
    public SportTypeController(SportTypeService sportTypeService, DtoConverter dtoConverter) {
        this.sportTypeService = sportTypeService;
        this.dtoConverter = dtoConverter;
    }

    //Получить все типы спорта
    @GetMapping("/get-all")
    public List<SportTypeDto> getAllSportTypes() {
        logger.atInfo().log("/get-all");
        return sportTypeService.getAll().stream().map(dtoConverter::sportTypeToDto).toList();
    }

    //Получить тип спорта по id
    @GetMapping("/{id}")
    public SportType getSportType(@PathVariable(name = "id")  Integer id) throws NoSuchElementException {
        logger.atInfo().log("get-by-id: /{}", id);
        Optional<SportType> sportTypeOpt = sportTypeService.getById(id);
        if(sportTypeOpt.isPresent()) {
            return sportTypeOpt.get();
        } else {
            throw new NoSuchElementException("No sport type with such id");
        }
    }
}
