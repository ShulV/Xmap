package com.shulpov.spots_app.locations.controllers;

import com.shulpov.spots_app.common.ApiResponse;
import com.shulpov.spots_app.common.ApiResponseStatus;
import com.shulpov.spots_app.locations.dto.CityDto;
import com.shulpov.spots_app.locations.services.CityService;
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
@Tag(name="Контроллер городов (справочник)", description="Выдает города")
@RestController
@RequestMapping(value ="/api/v1/cities", produces = "application/json")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @Operation(
            summary = "Получение списка всех городов",
            description = "Позволяет получить перечень всех имеющихся городов " +
                    "(БОЛЬШАЯ НАГРУЗКА НА СЕТЬ! НУЖНО ПОЛУЧАТЬ СПИСОК ГОРОДОВ ИСХОДЯ ИЗ СТРАНЫ, А ЗАТЕМ ИЗ РЕГИОНА)"
    )
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<CityDto>> getAll() {
        ApiResponse<CityDto> response = new ApiResponse<>();
        response.setDataList(cityService.getAllDto());
        response.setCustomStatus(ApiResponseStatus.SUCCESS);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Получение списка всех городов по региону",
            description = "Позволяет пользователю получить перечень всех городов, находящихся в определенном регионе по ее id"
    )
    @GetMapping("/by-region/{id}")
    public ResponseEntity<ApiResponse<CityDto>> getByRegionId(
            @Parameter(name = "id", description = "Идентификатор региона", required = true)
            @PathVariable("id") Integer id){
        ApiResponse<CityDto> response = new ApiResponse<>();
        List<CityDto> cityDtoList = cityService.getDtoByRegionId(id);
        if (cityDtoList.isEmpty()) {
            response.setCustomStatus(ApiResponseStatus.CLIENT_ERROR);
            response.setMessage("Region isn't exist or no cities in region");
        } else {
            response.setCustomStatus(ApiResponseStatus.SUCCESS);
            response.setDataList(cityDtoList);
        }
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Получение списка всех городов по стране",
            description = "Позволяет пользователю получить перечень всех городов, находящихся в определенной стране по ее id"
    )
    @GetMapping("/by-country/{id}")
    public ResponseEntity<ApiResponse<CityDto>> getByCountryId(
            @Parameter(name = "id", description = "Идентификатор страны", required = true)
            @PathVariable("id") Integer id) {
        ApiResponse<CityDto> response = new ApiResponse<>();
        List<CityDto> cityDtoList = cityService.getDtoByCountryId(id);
        if (cityDtoList.isEmpty()) {
            response.setCustomStatus(ApiResponseStatus.CLIENT_ERROR);
            response.setMessage("Country isn't exist or no cities in country");
        } else {
            response.setCustomStatus(ApiResponseStatus.SUCCESS);
            response.setDataList(cityDtoList);
        }
        return ResponseEntity.ok(response);
    }
}
