package com.shulpov.spots_app.spot_references.controllers;

import com.shulpov.spots_app.common.ResponseData;
import com.shulpov.spots_app.spot_references.dto.SpotTypeDto;
import com.shulpov.spots_app.spot_references.services.SpotTypeService;
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
@Tag(name="Контроллер типов мест для катания (справочник)", description="Выдает типы мест для катания")
@RestController
@RequestMapping("/api/v1/spot-types")
@RequiredArgsConstructor
public class SpotTypeController {
    private final SpotTypeService spotTypeService;

    @Operation(
            summary = "Получение всех типов спотов",
            description = "Позволяет пользователю получить все типы спотов"
    )
    @GetMapping("/all")
    public ResponseEntity<ResponseData<SpotTypeDto>> getAllSpots() {
        ResponseData<SpotTypeDto> response = new ResponseData<>();
        response.setDataList(spotTypeService.getAllDto());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Получение конкретного типа спота",
            description = "Позволяет пользователю получить тип спота по его id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<SpotTypeDto>> getSpotType(
            @Parameter(description = "Идентификатор типа спота", example = "1")
            @PathVariable(name = "id")  Integer id) throws NoSuchElementException {
        ResponseData<SpotTypeDto> response = new ResponseData<>();
        try {
            response.setData(spotTypeService.getDtoById(id));
        } catch (NoSuchElementException e) {
            response.setMessage("No spot type with id=" + id);
        }
        return ResponseEntity.ok(response);
    }
}
