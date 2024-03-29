package com.shulpov.spots_app.locations.controllers;

import com.shulpov.spots_app.common.ResponseData;
import com.shulpov.spots_app.locations.dto.CountryDto;
import com.shulpov.spots_app.locations.services.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Orlov Daniil
 * @since 1.0
 * @version 1.0
 */
@Tag(name="Контроллер стран (справочник)", description="Выдает страны")
@RestController
@RequestMapping(value = "/api/v1/countries", produces = "application/json")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @Operation(
            summary = "Получение списка всех стран",
            description = "Позволяет пользователю получить перечень всех имеющихся стран"
    )
    @GetMapping("/all")
    public ResponseEntity<ResponseData<CountryDto>> getAll() {
        ResponseData<CountryDto> response = new ResponseData<>();
        response.setDataList(countryService.getAllDto());
        return ResponseEntity.ok(response);
    }
}