package com.shulpov.spots_app.location.controllers;

import com.shulpov.spots_app.location.dto.CountryDto;
import com.shulpov.spots_app.location.services.CountryService;
import com.shulpov.spots_app.utils.DtoConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/countries", produces = "application/json")
@Tag(name="Контроллер стран (справочник)", description="Выдает страны")
public class CountryController {

    private final CountryService countryService;
    private final DtoConverter dtoConverter;
    private final Logger logger;

    public CountryController(CountryService countryService, DtoConverter dtoConverter) {
        this.countryService = countryService;
        this.dtoConverter = dtoConverter;
        this.logger = LoggerFactory.getLogger(CountryController.class);
    }

    @Operation(
            summary = "Получение списка всех стран",
            description = "Позволяет пользователю получить перечень всех имеющихся стран"
    )
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        logger.atInfo().log("Getting all countries");
        try {
            List<CountryDto> countryDtoList = countryService.getAll().stream().map(dtoConverter::countryToDto).toList();
            return ResponseEntity.ok(countryDtoList);
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("errorMessage", "There is no data in the table"));
        }
    }
}