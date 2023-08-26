package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.RegionDto;
import com.shulpov.spots_app.services.RegionService;
import com.shulpov.spots_app.utils.DtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;
    private final DtoConverter dtoConverter;
    private final Logger logger;

    public RegionController(RegionService regionService, DtoConverter dtoConverter) {
        this.regionService = regionService;
        this.dtoConverter = dtoConverter;
        this.logger = LoggerFactory.getLogger(RegionController.class);
    }

    @GetMapping("/get-all")
    public List<RegionDto> getAll() {
        logger.atInfo().log("/get-all");
        return regionService.getAll().stream().map(dtoConverter::regionToDto).toList();
    }
}
