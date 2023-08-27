package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.RegionDto;
import com.shulpov.spots_app.services.RegionService;
import com.shulpov.spots_app.utils.DtoConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@Tag(name="Контроллер регионов (справочник)", description="Выдает регионы")
public class RegionController {

    private final RegionService regionService;
    private final DtoConverter dtoConverter;
    private final Logger logger;

    public RegionController(RegionService regionService, DtoConverter dtoConverter) {
        this.regionService = regionService;
        this.dtoConverter = dtoConverter;
        this.logger = LoggerFactory.getLogger(RegionController.class);
    }

    @Operation(
            summary = "Получение списка всех регионов",
            description = "Позволяет пользователю получить перечень всех имеющихся регионов"
    )
    @GetMapping("/get-all")
    public List<RegionDto> getAll() {
        logger.atInfo().log("/get-all");
        return regionService.getAll().stream().map(dtoConverter::regionToDto).toList();
    }
}
