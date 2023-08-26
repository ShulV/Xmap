package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.CityDto;
import com.shulpov.spots_app.services.CityService;
import com.shulpov.spots_app.utils.DtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    private final CityService cityService;
    private final DtoConverter dtoConverter;
    private final Logger logger;

    public CityController(CityService cityService, DtoConverter dtoConverter) {
        this.cityService = cityService;
        this.dtoConverter = dtoConverter;
        this.logger = LoggerFactory.getLogger(CityController.class);
    }
    @GetMapping("/get-all")
    public List<CityDto> getAll() {
        logger.atInfo().log("/get-all");
        return cityService.getAll().stream().map(dtoConverter::cityToDto).toList();
    }
}
