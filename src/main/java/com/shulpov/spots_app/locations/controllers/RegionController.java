package com.shulpov.spots_app.locations.controllers;

import com.shulpov.spots_app.common.ResponseData;
import com.shulpov.spots_app.locations.dto.RegionDto;
import com.shulpov.spots_app.locations.services.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Orlov Daniil
 * @since 1.0
 * @version 1.0
 */
@Tag(name="Контроллер регионов (справочник)", description="Выдает регионы")
@RestController
@RequestMapping(value = "/api/v1/regions", produces = "application/json")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @Operation(
            summary = "Получение списка всех регионов",
            description = "Позволяет пользователю получить перечень всех имеющихся регионов"
    )
    @GetMapping("/all")
    public ResponseEntity<ResponseData<RegionDto>> getAll() {
        ResponseData<RegionDto> response = new ResponseData<>();
        response.setDataList(regionService.getAllDto());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Получение списка всех регионов по стране",
            description = "Позволяет пользователю получить перечень всех регионов, находящихся в определенной стране по ее id" +
                    "(БОЛЬШАЯ НАГРУЗКА НА СЕТЬ! СНАЧАЛА НУЖНО ПОЛУЧАТЬ СПИСОК РЕГИОНОВ ДЛЯ ОПРЕДЕЛЕННОЙ СТРАНЫ!)"
    )
    @GetMapping("/by-country/{id}")
    public ResponseEntity<ResponseData<RegionDto>> getByCountryId(
            @Parameter(name = "id", description = "Идентификатор страны", required = true)
            @PathVariable("id") Integer id) {
        ResponseData<RegionDto> response = new ResponseData<>();
        List<RegionDto> regionDtoList = regionService.getDtoByCountryId(id);
        if (regionDtoList.isEmpty()) {
            response.setMessage("Country isn't exist or no regions in country");
        } else {
            response.setDataList(regionDtoList);
        }

        return ResponseEntity.ok(response);
    }


}
