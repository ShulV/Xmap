package com.shulpov.spots_app.spot_references.controllers;

import com.shulpov.spots_app.common.ResponseData;
import com.shulpov.spots_app.spot_references.dto.SportTypeDto;
import com.shulpov.spots_app.spot_references.services.SportTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Tag(name="Контроллер типов спорта (справочник)", description="Выдает типы спорта")
@RestController
@RequestMapping("/api/v1/sport-types")
@RequiredArgsConstructor
public class SportTypeController {
    private final SportTypeService sportTypeService;

    @Operation(
            summary = "Получение всех типов спорта",
            description = "Позволяет пользователю получить все типы спотов"
    )
    @GetMapping("/all")
    public ResponseEntity<ResponseData<SportTypeDto>> getAllSportTypes() {
        ResponseData<SportTypeDto> response = new ResponseData<>();
        response.setDataList(sportTypeService.getAllDto());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Получение конкретного типа спорта",
            description = "Позволяет пользователю получить тип спорта по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<SportTypeDto>> getSportType(
            @Parameter(description = "Идентификатор типа спорта", example = "1")
            @PathVariable(name = "id")  Integer id) {
        ResponseData<SportTypeDto> response = new ResponseData<>();
        try {
            response.setData(sportTypeService.getDtoById(id));
        } catch (NoSuchElementException e) {
            response.setMessage("No sport type with id=" + id);
        }
        return ResponseEntity.ok(response);
    }
}
