package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.CountryDto;
import com.shulpov.spots_app.services.CountryService;
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
@RequestMapping("/api/countries")
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
    public List<CountryDto> getAll() {
        logger.atInfo().log("/get-all");
        return countryService.getAll().stream().map(dtoConverter::countryToDto).toList();
    }
}