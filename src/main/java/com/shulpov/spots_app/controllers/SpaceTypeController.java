package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.SpaceTypeDto;
import com.shulpov.spots_app.services.SpaceTypeService;
import com.shulpov.spots_app.utils.DtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/space-types/")
public class SpaceTypeController {
    private final SpaceTypeService spaceTypeService;
    private final static Logger logger = LoggerFactory.getLogger(SpaceTypeController.class);

    @Autowired
    public SpaceTypeController(SpaceTypeService spaceTypeService) {
        this.spaceTypeService = spaceTypeService;
    }

    //Получить все типы спорта
    @GetMapping("/get-all")
    public List<SpaceTypeDto> getAllSpaceTypes() {
        logger.atInfo().log("/get-all");
        return spaceTypeService.getAll().stream().map(DtoConverter::spaceTypeToDto).toList();
    }
}
