package com.shulpov.spots_app.spot_references.controllers;

import com.shulpov.spots_app.common.ApiResponse;
import com.shulpov.spots_app.common.ApiResponseStatus;
import com.shulpov.spots_app.spot_references.dto.SpaceTypeDto;
import com.shulpov.spots_app.spot_references.services.SpaceTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Tag(name="Контроллер типов помещений (справочник)", description="Выдает типы помещений")
@RestController
@RequestMapping("/api/v1/space-types")
@RequiredArgsConstructor()
public class SpaceTypeController {
    private final Logger logger = LoggerFactory.getLogger(SpaceTypeController.class);
    private final SpaceTypeService spaceTypeService;

    @Operation(
            summary = "Получение всех типов помещений",
            description = "Позволяет пользователю получить все типы помещений"
    )
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<SpaceTypeDto>> getAllSpaceTypes() {
        ApiResponse<SpaceTypeDto> response = new ApiResponse<>();
        response.setDataList(spaceTypeService.getAllDto());
        response.setCustomStatus(ApiResponseStatus.SUCCESS);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Получение конкретного типа помещения",
            description = "Позволяет пользователю получить тип помещения по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SpaceTypeDto>> getSpaceType(
            @Parameter(description = "Идентификатор типа помещения", example = "1")
            @PathVariable(name = "id")  Integer id) {
        ApiResponse<SpaceTypeDto> response = new ApiResponse<>();
        try {
            response.setData(spaceTypeService.getDtoById(id));
            response.setCustomStatus(ApiResponseStatus.SUCCESS);
        } catch (NoSuchElementException e) {
            response.setCustomStatus(ApiResponseStatus.CLIENT_ERROR);
            response.setMessage("No space type with id=" + id);
        }
        return ResponseEntity.ok(response);
    }
}
