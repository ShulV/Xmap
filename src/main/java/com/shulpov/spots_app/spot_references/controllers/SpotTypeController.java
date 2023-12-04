package com.shulpov.spots_app.spot_references.controllers;

import com.shulpov.spots_app.spot_references.dto.SpotTypeDto;
import com.shulpov.spots_app.spot_references.models.SpotType;
import com.shulpov.spots_app.spot_references.services.SpotTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Tag(name="Контроллер типов мест для катания (справочник)", description="Выдает типы мест для катания")
@RestController
@RequestMapping("/api/v1/spot-types")
public class SpotTypeController {
    private final SpotTypeService spotTypeService;
    private final Logger logger = LoggerFactory.getLogger(SpotTypeController.class);

    public SpotTypeController(SpotTypeService spotTypeService) {
        this.spotTypeService = spotTypeService;
    }

    @Operation(
            summary = "Получение всех типов спотов",
            description = "Позволяет пользователю получить все типы спотов"
    )
    @GetMapping("/all")
    public List<SpotTypeDto> getAllSpots() {
        return spotTypeService.getAllDto();
    }

    @Operation(
            summary = "Получение конкретного типа спота",
            description = "Позволяет пользователю получить тип спота по его id"
    )
    @GetMapping("/{id}")
    public SpotType getSpotType(@PathVariable(name = "id")  Integer id) throws NoSuchElementException {
        Optional<SpotType> spotTypeOpt = spotTypeService.getById(id);
        if(spotTypeOpt.isPresent()) {
            return spotTypeOpt.get();
        } else {
            logger.atError().log("No spot type with such id");
            throw new NoSuchElementException("No spot type with such id");
        }
    }
}
