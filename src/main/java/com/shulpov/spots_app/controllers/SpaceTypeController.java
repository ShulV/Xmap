package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.SpaceTypeDto;
import com.shulpov.spots_app.models.SpaceType;
import com.shulpov.spots_app.services.SpaceTypeService;
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
@RequestMapping("/api/space-types/")
public class SpaceTypeController {
    private final SpaceTypeService spaceTypeService;

    private final DtoConverter dtoConverter;
    private final static Logger logger = LoggerFactory.getLogger(SpaceTypeController.class);

    @Autowired
    public SpaceTypeController(SpaceTypeService spaceTypeService, DtoConverter dtoConverter) {
        this.spaceTypeService = spaceTypeService;
        this.dtoConverter = dtoConverter;
    }

    //Получить все типы спорта
    @GetMapping("/get-all")
    public List<SpaceTypeDto> getAllSpaceTypes() {
        logger.atInfo().log("/get-all");
        return spaceTypeService.getAll().stream().map(dtoConverter::spaceTypeToDto).toList();
    }

    //Получить тип помещения по id
    @GetMapping("/{id}")
    public SpaceType getSpaceType(@PathVariable(name = "id")  Integer id) throws NoSuchElementException {
        logger.atInfo().log("get-by-id: /{}", id);
        Optional<SpaceType> spaceTypeOpt = spaceTypeService.getById(id);
        if(spaceTypeOpt.isPresent()) {
            return spaceTypeOpt.get();
        } else {
            throw new NoSuchElementException("No space type with such id");
        }
    }
}
