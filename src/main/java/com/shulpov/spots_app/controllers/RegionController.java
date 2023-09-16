package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.CityDto;
import com.shulpov.spots_app.dto.RegionDto;
import com.shulpov.spots_app.models.City;
import com.shulpov.spots_app.models.Region;
import com.shulpov.spots_app.services.RegionService;
import com.shulpov.spots_app.utils.DtoConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Map;

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

    @Operation(
            summary = "Получение списка всех регионов по стране",
            description = "Позволяет пользователю получить перечень всех регионов, находящихся в определенной стране по ее id"
    )
    @GetMapping("/get-by-country-id/{id}")
    public ResponseEntity<?> getByCountryId(@PathVariable("id") Integer id){
        try {
            List<Region> regions = regionService.getByCountryId(id);
            List<RegionDto> regionDtoList = regions.stream().map(dtoConverter::regionToDto).toList();
            return ResponseEntity.ok(regionDtoList);
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Страна с id=" + id + " не найдена"));
        }
    }


}
