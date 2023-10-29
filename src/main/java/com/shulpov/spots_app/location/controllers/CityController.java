package com.shulpov.spots_app.location.controllers;

import com.shulpov.spots_app.location.dto.CityDto;
import com.shulpov.spots_app.location.models.City;
import com.shulpov.spots_app.location.services.CityService;
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
@RequestMapping(value ="/api/cities", produces = "application/json")
@Tag(name="Контроллер городов (справочник)", description="Выдает города")
public class CityController {
    private static final String ERROR_MESSAGE_KEY = "errorMessage";
    private final CityService cityService;

    private final DtoConverter dtoConverter;
    private final Logger logger;

    public CityController(CityService cityService, DtoConverter dtoConverter) {
        this.cityService = cityService;
        this.dtoConverter = dtoConverter;
        this.logger = LoggerFactory.getLogger(CityController.class);
    }

    @Operation(
            summary = "Получение списка всех городов",
            description = "Позволяет пользователю получить перечень всех имеющихся городов"
    )
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        logger.atInfo().log("Getting all cities");
        try {
            List<CityDto> cityDtoList = cityService.getAll().stream().map(dtoConverter::cityToDto).toList();
            return ResponseEntity.ok(cityDtoList);
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERROR_MESSAGE_KEY, "There is no data in the table"));
        }
    }

    @Operation(
            summary = "Получение списка всех городов по региону",
            description = "Позволяет пользователю получить перечень всех городов, находящихся в определенном регионе по ее id"
    )
    @GetMapping("/get-by-region-id/{id}")
    public ResponseEntity<?> getByRegionId(@PathVariable("id") Integer id){
        logger.atInfo().log("Getting all cities by region id = {}", id);
        try {
            List<City> cities = cityService.getByRegionId(id);
            List<CityDto> cityDtoList = cities.stream().map(dtoConverter::cityToDto).toList();
            return ResponseEntity.ok(cityDtoList);
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERROR_MESSAGE_KEY, "Region with id=" + id + " not found"));
        }
    }

    @Operation(
            summary = "Получение списка всех городов по стране",
            description = "Позволяет пользователю получить перечень всех городов, находящихся в определенной стране по ее id"
    )
    @GetMapping("/get-by-country-id/{id}")
    public ResponseEntity<?> getByCountryId(@PathVariable("id") Integer id) {
        logger.atInfo().log("Getting all cities by region id = {}", id);
        try {
            List<City> cities = cityService.getByCountryId(id);
            List<CityDto> cityDtoList = cities.stream().map(dtoConverter::cityToDto).toList();
            return ResponseEntity.ok(cityDtoList);
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERROR_MESSAGE_KEY, "Country with id=" + id + " not found"));
        }
    }
}
